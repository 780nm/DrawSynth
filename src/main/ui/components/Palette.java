package ui.components;

import model.Note;
import synthesis.*;
import ui.SequencerApp;
import ui.actions.PaletteActionManager;

import javax.swing.*;
import java.util.ArrayList;

// Palette UI Element, containing a list of available components and facilitating their manipulation
public class Palette extends Panel {

    // MODIFIES: app
    // EFFECTS: generates a new palette panel given an application
    public Palette(SequencerApp app) {
        super(app, new PaletteActionManager(app));

        addRow(this, null, "add:ampMod", "Add", "Amplitude Modulators:");
        addAmpModInfo();
        addSep();
        addRow(this, null, "add:pitchMod", "Add", "Pitch Modulators:");
        addPitchModInfo();
        addSep();
        addRow(this, null, "add:note", "Add", "Notes:");
        addNotesInfo();
        addSep();
        addRow(this, null, "add:instr", "Add", "Instruments:");
        addInstrInfo();
    }

    // Would've liked to consolidate all the below into one generic function,
    // but there's no good way to get runtime class info on generic types

    // MODIFIES: this
    // EFFECTS: Adds the list of amplitude modulators and their controls to the palette
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

    // MODIFIES: this
    // EFFECTS: Adds the list of pitch modulators and their controls to the palette
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

    // MODIFIES: this
    // EFFECTS: Adds the list of notes and their controls to the palette
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

    // MODIFIES: this
    // EFFECTS: Adds the list of instruments and their controls to the palette
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

}
