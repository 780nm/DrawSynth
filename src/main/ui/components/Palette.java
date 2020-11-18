package ui.components;

import model.Note;
import persistence.PersistenceUtil;
import synthesis.*;
import ui.SequencerApp;
import ui.actions.ModelAction;
import ui.components.dialogues.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.UUID;

import static persistence.PersistenceUtil.getElementWithID;

public class Palette extends Panel {

    public Palette(SequencerApp app) {
        super(app);

        addRow(this, "", "add:ampMod", "Add", "Amplitude Modulators:");
        addAmpModInfo();
        addSep();
        addRow(this, "", "add:pitchMod", "Add", "Pitch Modulators:");
        addPitchModInfo();
        addSep();
        addRow(this, "", "add:note", "Add", "Notes:");
        addNotesInfo();
        addSep();
        addRow(this, "", "add:instr", "Add", "Instruments:");
        addInstrInfo();
    }

    private void addAmpModInfo() {
        JPanel ampModsPanel = panelHelper();

        ArrayList<AmplitudeModulator> ampMods = app.getSeq().getAmpMods();
        for (int i = 0; i < ampMods.size(); i++) {
            AmplitudeModulator mod = ampMods.get(i);
            String uuid = mod.getUuid().toString();
            String className = mod.getClass().getName();
            addRow(ampModsPanel, String.valueOf(i + 1),
                    "edit:ampMod:" + className + ":" + uuid, "Edit", mod.toString());
        }
        add(ampModsPanel);
    }

    public void addPitchModInfo() {
        JPanel pitchModsPanel = panelHelper();

        ArrayList<PitchModulator> pitchMods = app.getSeq().getPitchMods();
        for (int i = 0; i < pitchMods.size(); i++) {
            PitchModulator mod = pitchMods.get(i);
            String uuid = mod.getUuid().toString();
            String className = mod.getClass().getName();
            addRow(pitchModsPanel, String.valueOf(i + 1),
                    "edit:pitchMod:" + className + ":" + uuid, "Edit", mod.toString());
        }
        add(pitchModsPanel);
    }

    public void addNotesInfo() {
        JPanel notesPanel = panelHelper();

        ArrayList<Note> notes = app.getSeq().getNotes();
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            String uuid = note.getUuid().toString();
            addRow(notesPanel, String.valueOf(i + 1),
                    "edit:note:synthesis.Note:" + uuid, "Edit", note.toString());
        }
        add(notesPanel);
    }

    public void addInstrInfo() {
        JPanel instrPanel = panelHelper();

        ArrayList<Instrument> instruments = app.getSeq().getInstruments();
        for (int i = 0; i < instruments.size(); i++) {
            Instrument instr = instruments.get(i);
            String uuid = instr.getUuid().toString();
            String className = instr.getClass().getName();
            addRow(instrPanel, String.valueOf(i + 1),
                    "edit:instr:" + className + ":" + uuid, "Edit", instr.toString());
        }
        add(instrPanel);
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

        new AddPickerDialogue(this, new ModelAction[]{addEnvelopeMod, addKeyedMod});
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

        new AddPickerDialogue(this, new ModelAction[]{addConstantMod, addKeyedMod});
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

        new NotePropertiesDialogue(this, app, addNoteAction);
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

        new AddPickerDialogue(this, new ModelAction[]{addSinusoidInstr, addKeyedInstr});
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
                new EnvelopeAmpPropertiesDialogue(this, (EnvelopeAmplitude)mod);
                break;
            case "synthesis.KeyedAmplitude":
                new KeyedAmpPropertiesDialogue(this, (KeyedAmplitude)mod);
        }
    }

    private void processEditPitchMod(String[] command) {
        switch (command[2]) {
            case "synthesis.ConstantPitch":
                JOptionPane.showMessageDialog(this, "Nothing to edit!");
                break;
            case "synthesis.KeyedPitch":
                new KeyedPitchPropertiesDialogue(this,
                        (KeyedPitch)getElementWithID(app.getSeq().getPitchMods(), UUID.fromString(command[3])));
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

        new NotePropertiesDialogue(this, app, addNoteAction);
    }

    public void processEditInstr(String[] command) {
        switch (command[2]) {
            case "synthesis.SinusoidInstrument":
                JOptionPane.showMessageDialog(this, "Nothing to edit!");
                break;
            case "synthesis.KeyedInstrument":
                new KeyedInstrPropertiesDialogue(this,
                        (KeyedInstrument)getElementWithID(app.getSeq().getInstruments(), UUID.fromString(command[3])));
        }
    }

}
