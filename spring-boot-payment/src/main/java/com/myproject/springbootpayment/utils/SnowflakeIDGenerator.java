package com.myproject.springbootpayment.utils;

import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 2:57 PM Tue 4/22/2025
 */
import lombok.experimental.UtilityClass;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

/**
 * Utility class for generating unique IDs using the Snowflake algorithm.
 * Snowflake IDs are 64-bit values composed of:
 * - 41 bits for timestamp (milliseconds since epoch or custom epoch)
 * - 10 bits for machine ID (to distinguish between different servers/instances)
 * - 12 bits for sequence number (to distinguish IDs generated in the same millisecond)
 */
@UtilityClass
public class SnowflakeIDGenerator {
	// Custom epoch (January 1, 2020 Midnight UTC)
	private static final long CUSTOM_EPOCH = 1577836800000L;

	// Machine ID bits (10 bits)
	private static final long MACHINE_ID_BITS = 10L;
	private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);

	// Sequence bits (12 bits)
	private static final long SEQUENCE_BITS = 12L;
	private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

	// Timestamp shift
	private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
	private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;

	// Machine ID
	private static final long machineId;

	// Sequence increment
	private static long sequence = 0L;

	// Last timestamp
	private static long lastTimestamp = -1L;

	// Initialize machine ID
	static {
		machineId = createMachineId();
	}

	/**
	 * Generates a unique Snowflake ID
	 *
	 * @return a unique ID as a long
	 */
	public static synchronized long nextId() {
		long currentTimestamp = getCurrentTimestamp();

		// Handle clock moving backwards
		if (currentTimestamp < lastTimestamp) {
			throw new IllegalStateException("Clock moved backwards. Refusing to generate ID for "
				+ (lastTimestamp - currentTimestamp) + " milliseconds");
		}

		// If current timestamp is same as last timestamp, increment sequence
		if (currentTimestamp == lastTimestamp) {
			sequence = (sequence + 1) & MAX_SEQUENCE;
			// If sequence overflows, wait until next millisecond
			if (sequence == 0) {
				currentTimestamp = waitNextMillis(currentTimestamp);
			}
		} else {
			// Reset sequence for new millisecond
			sequence = 0;
		}

		lastTimestamp = currentTimestamp;

		// Compose the ID from timestamp, machine ID, and sequence
		return ((currentTimestamp - CUSTOM_EPOCH) << TIMESTAMP_SHIFT)
			| (machineId << MACHINE_ID_SHIFT)
			| sequence;
	}

	/**
	 * Waits until next millisecond if current millisecond is full
	 *
	 * @param currentTimestamp current timestamp
	 * @return next timestamp in milliseconds
	 */
	private static long waitNextMillis(long currentTimestamp) {
		while (currentTimestamp == lastTimestamp) {
			currentTimestamp = getCurrentTimestamp();
		}
		return currentTimestamp;
	}

	/**
	 * Returns current timestamp in milliseconds
	 *
	 * @return current timestamp in milliseconds
	 */
	private static long getCurrentTimestamp() {
		return Instant.now().toEpochMilli();
	}

	/**
	 * Creates a machine ID. Attempts to use the lower 10 bits of the MAC address first.
	 * Falls back to using a random number if MAC address cannot be obtained.
	 *
	 * @return machine ID (0-1023)
	 */
	private static long createMachineId() {
		try {
			// Try to use the MAC address
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface network = networkInterfaces.nextElement();
				byte[] hardwareAddress = network.getHardwareAddress();
				if (hardwareAddress != null && hardwareAddress.length > 0) {
					// Use last 2 bytes of MAC for machine ID
					long id = ((0x000000FF & (long) hardwareAddress[hardwareAddress.length - 2])
						| (0x0000FF00 & (((long) hardwareAddress[hardwareAddress.length - 1]) << 8))) & MAX_MACHINE_ID;
					if (id > 0) {
						return id;
					}
				}
			}
		} catch (Exception e) {
			// Fallback to random value if MAC address cannot be obtained
		}

		// Fallback to random ID if MAC cannot be used
		return new SecureRandom().nextInt((int) MAX_MACHINE_ID) + 1;
	}

	/**
	 * Extracts the timestamp part from a Snowflake ID
	 *
	 * @param id the snowflake ID
	 * @return timestamp in milliseconds since epoch
	 */
	public static long extractTimestamp(long id) {
		return ((id >> TIMESTAMP_SHIFT) + CUSTOM_EPOCH);
	}

	/**
	 * Extracts the machine ID from a Snowflake ID
	 *
	 * @param id the snowflake ID
	 * @return machine ID
	 */
	public static long extractMachineId(long id) {
		return (id >> SEQUENCE_BITS) & MAX_MACHINE_ID;
	}

	/**
	 * Extracts the sequence from a Snowflake ID
	 *
	 * @param id the snowflake ID
	 * @return sequence
	 */
	public static long extractSequence(long id) {
		return id & MAX_SEQUENCE;
	}
}
