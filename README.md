# Draw-Synth

A final project for CPSC 210, an introductory course in object oriented software design I took at UBC.
While the process was extremely instructive, the outcome is quite crude thanks to time pressure.
What follows is the writeup I completed as part of the project;
the section regarding my analysis of the completed work and options for future development may be of particular interest.
While I may expand on this idea in the future, any further work on this particular codebase is unlikely.

## Project Description

The goal of this application is to facilitate the creation of music,
via the assembly of notes into tracks and tracks into sequences, which may then be played back synchronously.
Users may create **instruments** by drawing a single oscillation of the instrument's waveform,
and similarly create notes with drawn amplitude and pitch curves, aka **modulators**.
Thus, the properties of the synthesized waveform may vary from note to note with a fine degree of control.
For ease of use, there exist generic **instruments** and **modulators** with parametric controls, but less flexibility.

Upon assembly of notes into a track with an associated instrument,
the software synthesizes and plays back the generated audio in real time.

The target audience for this application is therefore music producers and artists.
As such, the evolution of the synthesis system used in this proof of concept makes a good candidate for
adaptation into a plugin for a more well-established audio system.

Personally, I'm primarily interested in using this project to explore low-level signal generation and manipulation,
and practicing building up an application using object-oriented design principles.

## User Stories

*The following user stories have an implementation, as of Phase 3:*

- As a user, I would like to define Amplitude and Pitch modulation profiles
- As a user, I would like to assemble modulation profiles into Notes 
- As a user, I would like to add notes to a track associated with a specified instrument
- As a user, I would like to play back a Track
- As a user, I would like to add tracks to a sequence
- As a user, I would like to save and read sequences and their associated elements to and from disk
- As a user, I would like to play back a sequence
- As a user, I would like to draw instrument and modulator curves
- As a user, I would like to edit existing sequences and their associated elements

*Some opportunities for exploration:*

- As a user, I would like to export the encoded audio of a sequence to disk
- As a user, i would like to define an instument as a composition of other instruments

## Phase 4 Tasks
For the purpose of *Task 2*, The map interface was utilized in `Track.Java` for storing pairs of notes and their
associated positions in a track, and `Panel.java` for passing the title and actions of menu buttons.

*Task 3:* As one can see in the UML diagram,
there exists a fair amount of coupling present within the codebase.
Given more time, the following actions could be taken:
 - Refactor the UI to abstract away the sequencer's internal palette of notes,
 or make notes wholly elements of tracks and make the process of inserting a note be the point of note creation.
 The two-step process is a remnant of the command line interface, but does not translate as well to a GUI design.
 - There exist a considerable number of explicit casts based on runtime class names, which are liable to change.
 This is not ideal, but the decision to use this workaround was made predominantly due to the inability of
 the mandated JSON serialization library to serialize/deserialize objects without discarding class data.
 Using a library with this functionality would alleviate the need to perform such casting in `JsonReader`,
 but for casting occurring in the UI (such as in the two `ActionManager` classes),
  the UI could be refactored such that each form retains some knowledge of an
 associated model element, and the UI state could subsequently be saved and loaded similarly to the model's,
 retaining this association across user sessions. In the interest of time, this was not implemented,
 and thus the UI is currently generated from the loaded model, and thus runtime classes must be inspected in order
 to determine the appropriate edit dialogues for each model element. A type provider could also be another approach.
 This brings up an interesting issue with the OO paradigm, namely the inability to treat a collection of items
 as having both unique and common properties. It is useful to, say, generate an audio sample with a variety of
 instruments, but then manipulate each of those instruments in different ways.
 If we choose to relax the separation between our UI and model, each instrument class could return a UI uniquely
 suited to manipulating its own contents (perhaps we could standardize this through an interface),
 and thus solve this issue.
 - At the moment, any change to the model necessitates a reload of the UI. Since the application is very small,
 this has no appreciable performance deficit, although it is bad practice.
 Theoretically, one could shuffle components around explicitly after every action,
 but preferably a modern reactive UI library would be utilized instead.
 Such optimizations were avoided due to time constraints.
 A declarative design paradigm would work well in reducing complexity of this part of the codebase,
 but that would not be possible in Java.
 - Some static functions in `PersistenceUtil` could perhaps find a better place
 - In a typical MVC setup, the UI and controller are separated, which is partially achieved here, but in principle
 the separation should be made more explicit. Model separation remains good,
 although as noted above that entailed serious compromises.
 - Improving the robustness of the exceptions defined for the generation of model state from saved data and
 subsequently improving the specificity of error messages would likely help with stability.