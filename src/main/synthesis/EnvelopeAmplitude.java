package synthesis;

import org.json.JSONObject;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.UUID;

// Represents a typical envelope-based note amplitude profile
public class EnvelopeAmplitude implements AmplitudeModulator {

    private UUID ampModID;

    double attack;
    double decay;
    double balance;

    // REQUIRES: all of attack, decay are in [0,1]; attack + decay <= 1; -1 <= balance <= 1
    // EFFECTS: Constructs an EnvelopeAmplitude modulator
    public EnvelopeAmplitude(double attack, double decay, double balance) {
        ampModID = UUID.randomUUID();
        this.attack = attack;
        this.decay = decay;
        this.balance = balance;
    }

    // REQUIRES: all of attack, decay are in [0,1]; attack + decay <= 1; -1 <= balance <= 1
    // EFFECTS: Constructs an EnvelopeAmplitude modulator
    public EnvelopeAmplitude(double attack, double decay, double balance, UUID id) {
        ampModID = id;
        this.attack = attack;
        this.decay = decay;
        this.balance = balance;
    }

    // REQUIRES: format has a valid configuration, and encoding is PCM_SIGNED, instrument is not null
    // MODIFIES: wave
    // EFFECTS: Manipulates the given byte array as per the associated amplitude specification
    public void applyAmplitudeProfile(double amplitude, ArrayList<Double> wave, AudioFormat format) {
        int channels = format.getChannels();
        int size = wave.size();
        double attackSamples = attack * size;
        double decaySamples = decay * size;

        for (int i = 0; i < size - channels + 1; i += channels) {
            for (int j = 0; j < channels; j++) {
                double sample = wave.get(i + j);
                sample *= amplitude;
                if (i < attackSamples) {
                    sample *= i / attackSamples;
                }

                int remainingFrames = size - i;
                if (remainingFrames < decaySamples) {
                    sample *= remainingFrames / decaySamples;
                }

                wave.set(i + j, sample);
            }

            applyBalanceProfile(wave, channels, i);

        }
    }

    // MODIFIES: wave
    // EFFECTS: Manipulates waveform balance given current profile settings
    private void applyBalanceProfile(ArrayList<Double> wave, int channels, int i) {
        if (channels == 2 && balance != 0) {
            double leftScale = Math.min(1, 1 - balance);
            double rightScale = Math.min(1, 1 + balance);

            wave.set(i, wave.get(i) * leftScale);
            wave.set(i + 1, wave.get(i + 1) * rightScale);
        }
    }

    // EFFECTS: Returns the state of the Note as a serialized JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", ampModID.toString());
        json.put("class", this.getClass().getName());
        json.put("attack", attack);
        json.put("decay", decay);
        json.put("balance", balance);
        return json;
    }

    // Getters and Setters

    public UUID getUuid() {
        return ampModID;
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
