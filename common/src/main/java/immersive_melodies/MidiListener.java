package immersive_melodies;

import io.netty.util.internal.ConcurrentSet;

import javax.sound.midi.*;
import java.util.Set;

public class MidiListener {
    public static void launch() {
        new Thread(new MidiListenerThread()).start();
    }

    static class MidiListenerThread implements Runnable {
        private final Set<MidiDevice.Info> connectedDevices = new ConcurrentSet<>();

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                try {
                    MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
                    for (MidiDevice.Info info : midiDevices) {
                        try {
                            if (connectedDevices.contains(info)) continue;
                            MidiDevice device = MidiSystem.getMidiDevice(info);
                            if (device.getMaxTransmitters() != 0) {
                                device.open();
                                Transmitter transmitter = device.getTransmitter();
                                transmitter.setReceiver(new MidiReceiver(connectedDevices, info));

                                Common.LOGGER.info("MIDI Device: {} - {}", info.getName(), info.getDescription());
                                connectedDevices.add(info);
                            }
                        } catch (MidiUnavailableException e) {
                            Common.LOGGER.warn("MIDI Device unavailable: {}", info.getName(), e);
                        }
                    }
                } catch (Exception e) {
                    Common.LOGGER.error("Error initializing MIDI devices", e);
                }
            }
        }
    }

    static class MidiReceiver implements Receiver {
        private final Set<MidiDevice.Info> connectedDevices;
        private final MidiDevice.Info info;

        public MidiReceiver(Set<MidiDevice.Info> connectedDevices, MidiDevice.Info info) {
            this.connectedDevices = connectedDevices;
            this.info = info;
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (message instanceof ShortMessage sm) {
                int command = sm.getCommand();
                if (command == ShortMessage.NOTE_ON) {
                    int note = sm.getData1();
                    int velocity = sm.getData2();
                    Client.playNote(note, velocity);
                } else if (command == ShortMessage.NOTE_OFF) {
                    int note = sm.getData1();
                    Client.playNote(note, 0);
                }
            }
        }

        @Override
        public void close() {
            connectedDevices.remove(info);
        }
    }
}
