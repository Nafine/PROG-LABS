package se.ifmo.shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class which standardizes packet handling between multiple hosts.
 * <p>
 *     Defines some methods to work with packets.
 *     Contains information about packets structure.
 * </p>
 */
public class PacketManager {
    // Network buffer size optimized for Ethernet MTU (1500 bytes).
    public static final int BUFFER_SIZE = 1500;

    /**
     * Assemble gathered packets.
     * <p>
     * Uses java StreamAPI for sorting and assembling messages.
     * Doesn't check errors, packet data etc., only assembles specified packets.
     * </p>
     *
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

    /**
     * Splits given message type of {@code byte[]} into n packets.
     * <p>
     * Uses packet size of {@link PacketManager} class with size of 1500 bytes.
     * </p>
     *
     * @param data to split
     * @return {@link List} of {@code byte[]}, each element represents individual packet.
     * The elements lie in the order of creation: first element -> first packet, last element -> last packet.
     */
    public static List<byte[]> splitMessage(byte[] data) {
        int packetSize = BUFFER_SIZE - 2 * Integer.BYTES;
        int numPackets = (int) Math.ceil((double) data.length / (BUFFER_SIZE + 1));

        List<byte[]> packets = new ArrayList<>(numPackets);

        for (int i = 0; i < numPackets; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.putInt(i);
            buffer.putInt(numPackets);
            buffer.put(data, i * packetSize, Math.min(packetSize, data.length - i * packetSize));
            packets.add(buffer.array());
        }

        return packets;
    }
}
