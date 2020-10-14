package synthesis;

import javax.sound.sampled.AudioFormat;

// Represents a typical envelope-based note amplitude profile
public class EnvelopeAmplitude implements AmplitudeModulator {

    double amplitude;
    double attack;
    double decay;
    double balance;

    // REQUIRES: all of amplitude, attack, decay are in [0,1]; attack + decay <= 1; -1 <= balance <= 1
    // EFFECTS: Constructs an EnvelopeAmplitude modulator
    public EnvelopeAmplitude(double amplitude, double attack, double decay, double balance) {
        this.amplitude = amplitude;
        this.attack = attack;
        this.decay = decay;
        this.balance = balance;
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, instrument is not null
    // MODIFIES: wave
    // EFFECTS: Manipulates the given byte array as per the associated amplitude specification
    public void applyAmplitudeProfile(double[] wave, AudioFormat format) {
        int channels = format.getChannels();
        double attackSamples = attack * wave.length;
        double decaySamples = decay * wave.length;

        for (int i = 0; i < wave.length - channels + 1; i += channels) {
            for (int j = 0; j < channels; j++) {
                wave[i + j] *= amplitude;
                if (i < attackSamples) {
                    wave[i + j] *= i / attackSamples;
                }

                int remainingFrames = wave.length - i;
                if (remainingFrames < decaySamples) {
                    wave[i + j] *= remainingFrames / decaySamples;
                }
            }

            if (channels == 2 && balance != 0) {
                double leftScale = Math.min(1, 1 - balance);
                double rightScale = Math.min(1, 1 + balance);

                wave[i] *= leftScale;
                wave[i + 1] *= rightScale;
            }

        }
    }

    // Getters and Setters

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDecay() {
        return decay;
    }

    public void setDecay(double decay) {
        this.decay = decay;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
