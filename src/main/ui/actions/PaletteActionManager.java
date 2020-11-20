package ui.actions;

import model.Note;
import persistence.PersistenceUtil;
import synthesis.*;
import ui.SequencerApp;
import ui.components.GraphDrawer;
import ui.components.dialogues.AddPickerDialogue;
import ui.components.dialogues.EnvelopeAmpPropertiesDialogue;
import ui.components.dialogues.KeyedElementPropertiesDialogue;
import ui.components.dialogues.NotePropertiesDialogue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.UUID;

import static persistence.PersistenceUtil.getElementWithID;

public class PaletteActionManager extends ActionManager {

    public PaletteActionManager(SequencerApp app) {
        super(app);
    }

    public void actionPerformed(ActionEvent e) {
        String[] command = e.getActionCommand().split(":");
        switch (command[0]) {
            case "add":
                processAdd(command);
                break;
            case "edit":
                processEdit(command);
        }
    }

    private void processAdd(String[] command) {
        switch (command[1]) {
            case "ampMod":
                processAddAmpMod();
                break;
            case "pitchMod":
                processAddPitchMod();
                break;
            case "note":
                processAddNote();
                break;
            case "instr":
                processAddInstr();
        }
    }

    private void processAddAmpMod() {
        ModelAction addEnvelopeMod = new ModelAction() {
            public String getActionName() {
                return "Envelope Amplitude Modulator";
            }

            public void processAction(Object[] params) {
                editAmpModHelper(new EnvelopeAmplitude(0,0,0));
            }
        };

        ModelAction addKeyedMod = new ModelAction() {
            public String getActionName() {
                return "Keyed Amplitude Modulator";
            }

            public void processAction(Object[] params) {
                editAmpModHelper(new KeyedAmplitude());
            }
        };

        new AddPickerDialogue(target, new ModelAction[]{addEnvelopeMod, addKeyedMod});
    }

    private void editAmpModHelper(AmplitudeModulator ampMod) {
        app.getSeq().addAmpMod(ampMod);
        processEdit(new String[]{"", "ampMod", ampMod.getClass().getName(), ampMod.getUuid().toString()});
    }

    private void processAddPitchMod() {
        ModelAction addConstantMod = new ModelAction() {
            public String getActionName() {
                return "Constant Pitch";
            }

            public void processAction(Object[] params) {
                app.getSeq().addPitchMod(new ConstantPitch());
                app.reinitializeContent();
            }
        };

        ModelAction addKeyedMod = new ModelAction() {
            public String getActionName() {
                return "Keyed Pitch Modulator";
            }

            public void processAction(Object[] params) {
                PitchModulator pitchMod = new KeyedPitch();
                app.getSeq().addPitchMod(pitchMod);
                processEdit(new String[]{"", "pitchMod", pitchMod.getClass().getName(), pitchMod.getUuid().toString()});
            }
        };

        new AddPickerDialogue(target, new ModelAction[]{addConstantMod, addKeyedMod});
    }

    private void processAddNote() {
        ModelAction addNoteAction = new ModelAction() {
            @Override
            public String getActionName() {
                return "Add Note";
            }

            @Override
            public void processAction(Object[] params) {
                app.getSeq().addNote(
                        (double)params[0],
                        (UUID)params[1],
                        (double)params[2],
                        (UUID)params[3],
                        (int)params[4]
                );
                app.reinitializeContent();
            }
        };

        new NotePropertiesDialogue(target, app, addNoteAction);
    }

    private void processAddInstr() {
        ModelAction addSinusoidInstr = new ModelAction() {
            public String getActionName() {
                return "Sinusoid Instrument";
            }

            public void processAction(Object[] params) {
                app.getSeq().addInstrument(new SinusoidInstrument());
                app.reinitializeContent();
            }
        };

        ModelAction addKeyedInstr = new ModelAction() {
            public String getActionName() {
                return "Keyed Instrument";
            }

            public void processAction(Object[] params) {
                Instrument instr = new KeyedInstrument();
                app.getSeq().addInstrument(instr);
                processEdit(new String[]{"", "instr", instr.getClass().getName(), instr.getUuid().toString()});
            }
        };

        new AddPickerDialogue(target, new ModelAction[]{addSinusoidInstr, addKeyedInstr});
    }

    private void processEdit(String[] command) {
        switch (command[1]) {
            case "ampMod":
                processEditAmpMod(command);
                break;
            case "pitchMod":
                processEditPitchMod(command);
                break;
            case "note":
                processEditNote(command);
                break;
            case "instr":
                processEditInstr(command);
        }
        app.reinitializeContent();
    }

    private void processEditAmpMod(String[] command) {
        AmplitudeModulator mod = getElementWithID(app.getSeq().getAmpMods(), UUID.fromString(command[3]));

        switch (command[2]) {
            case "synthesis.EnvelopeAmplitude":
                new EnvelopeAmpPropertiesDialogue(target, (EnvelopeAmplitude)mod);
                break;
            case "synthesis.KeyedAmplitude":
                new KeyedElementPropertiesDialogue(
                        target,
                        (KeyedAmplitude)getElementWithID(app.getSeq().getAmpMods(), UUID.fromString(command[3])),
                        "Draw the desired amplitude curve on the graph and hit OK when satisfied",
                        "Add Keyed Amplitude",
                        new GraphDrawer(0,1,0)
                );
        }
    }

    private void processEditPitchMod(String[] command) {
        switch (command[2]) {
            case "synthesis.ConstantPitch":
                JOptionPane.showMessageDialog(target, "Nothing to edit!");
                break;
            case "synthesis.KeyedPitch":
                new KeyedElementPropertiesDialogue(
                        target,
                        (KeyedPitch)getElementWithID(app.getSeq().getPitchMods(), UUID.fromString(command[3])),
                        "Draw the desired pitch curve on the graph and hit OK when satisfied. "
                                + "Values are in multiples of the base period.",
                        "Add Keyed Pitch",
                        new GraphDrawer(0.5,1.5,1)
                );
        }
    }

    private void processEditNote(String[] command) {
        Note note = getElementWithID(app.getSeq().getNotes(), UUID.fromString(command[3]));

        ModelAction addNoteAction = new ModelAction() {
            @Override
            public String getActionName() {
                return "Add Note";
            }

            @Override
            public void processAction(Object[] params) {
                ArrayList<AmplitudeModulator> ampMods = app.getSeq().getAmpMods();
                ArrayList<PitchModulator> pitchMods = app.getSeq().getPitchMods();

                note.setBaseAmplitude((double)params[0]);
                note.setAmpMod(PersistenceUtil.getElementWithID(ampMods, (UUID)params[1]));
                note.setBasePitch((double)params[2]);
                note.setPitchMod(PersistenceUtil.getElementWithID(pitchMods, (UUID)params[3]));
                note.setDuration((int)params[4]);

                app.reinitializeContent();
            }
        };

        new NotePropertiesDialogue(target, app, addNoteAction);
    }

    public void processEditInstr(String[] command) {
        switch (command[2]) {
            case "synthesis.SinusoidInstrument":
                JOptionPane.showMessageDialog(target, "Nothing to edit!");
                break;
            case "synthesis.KeyedInstrument":
                new KeyedElementPropertiesDialogue(
                        target,
                        (KeyedInstrument)getElementWithID(app.getSeq().getInstruments(), UUID.fromString(command[3])),
                        "Draw the desired amplitude curve on the graph and hit OK when satisfied",
                        "Add Keyed Instrument",
                        new GraphDrawer(-1,1,0)
                );
        }
    }

}
