package se.ifmo.shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class PacketManager {
    public static byte[] assemblePackets(Map<Integer, byte[]> packets) {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        packets.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    try {
                        message.write(entry.getValue());
                    } catch (IOException ignored) {
                    }
                });
        return message.toByteArray();
    }
}
