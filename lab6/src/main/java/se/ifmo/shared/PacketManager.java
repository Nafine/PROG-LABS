package se.ifmo.shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Class which contains all common methods for packet management.
 */
public class PacketManager {
    /**
     * Assemble gathered packets.
     * <p>
     * Uses java StreamAPI for sorting and assembling messages.
     * Doesn't check errors, packet data etc., only assembles specified packets.
     * </p>
     * @param packets to assemble
     * @return data as {@code byte[]}
     */
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
