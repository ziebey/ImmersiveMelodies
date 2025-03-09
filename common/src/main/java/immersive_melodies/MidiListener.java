package immersive_melodies;

import javax.sound.midi.*;

public class MidiListener {
    public static void launch() {
        new Thread(new MidiListenerThread()).start();
    }

    static class MidiListenerThread implements Runnable {
        @Override
        public void run() {
            try {
                MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
                for (MidiDevice.Info info : midiDevices) {
                    try {
                        MidiDevice device = MidiSystem.getMidiDevice(info);
                        Common.LOGGER.info("MIDI Device: {} - {}", info.getName(), info.getDescription());

                        if (device.getMaxTransmitters() != 0) {
                            device.open();
                            Transmitter transmitter = device.getTransmitter();
                            transmitter.setReceiver(new MidiReceiver());
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

    static class MidiReceiver implements Receiver {
        @Override
        public void send(MidiMessage message, long timeStamp) {
            byte[] data = message.getMessage();
            StringBuilder sb = new StringBuilder("MIDI message received: ");
            for (byte b : data) {
                sb.append(String.format("%02X ", b));
            }
            Common.LOGGER.info(sb.toString());
        }

        @Override
        public void close() {
            // Clean up resources if necessary
        }
    }
}
