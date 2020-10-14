# Draw-Synth

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

Personally, I'm primarily interesting in using this project to explore low-level signal generation and manipulation,
and practicing building up an application using object-oriented design principles.

## User Stories

*The following four user stories have an initial implementation, as of Phase 1:*

- As a user, I would like to define Amplitude and Pitch modulation profiles
- As a user, I would like to assemble modulation profiles into Notes 
- As a user, I would like to add notes to a track associated with a specified instrument
- As a user, I would like to play back a Track

*Upcoming user stories (note stub classes and tests demonstrating proposed hierarchical structure are present):*

- As a user, I would like to add tracks to a sequence
- As a user, I would like to save and read sequences and their associated elements to and from disk
- As a user, I would like to export the encoded audio of a sequence to disk
- As a user, I would like to play back sequence
- As a user, I would like to edit existing sequences and their associated elements
- As a user, I would like to draw instrument and modulator curves
