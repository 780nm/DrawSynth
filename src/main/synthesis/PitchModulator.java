package synthesis;

//Specifies the period of a waveform at any given point in its synthesis
public interface PitchModulator {

    // REQUIRES: 0 <= time <= 1, sampleRate > 0
    // EFFECTS: Returns the period in samples of the waveform's oscillation as a function of the current sampleRate
    //          and the current time as a ratio of the total clip duration.
    double getPeriodAtTime(double time, float sampleRate);

}
