package net.youmi.android;

public final class Coder_XXTEA {

	private static final int delta = 0x9E3779B9;

	private Coder_XXTEA() {
	}

	private static final int MX(int sum, int y, int z, int p, int e, int[] k) {
		return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
	}

	/**
	 * Encrypt data with key.
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static final byte[] encrypt(byte[] data, byte[] key) {
		if (data.length == 0) {
			return data;
		}
		return toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false);
	}

	/**
	 * Decrypt data with key.
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static final byte[] decrypt(byte[] data, byte[] key) {
		if (data.length == 0) {
			return data;
		}
		return toByteArray(decrypt(toIntArray(data, false), toIntArray(key, false)), true);
	}

	/**
	 * Encrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static final int[] encrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], sum = 0, e;
		int p, q = 6 + 52 / (n + 1);

		while (q-- > 0) {
			sum = sum + delta;
			e = sum >>> 2 & 3;
			for (p = 0; p < n; p++) {
				y = v[p + 1];
				z = v[p] += MX(sum, y, z, p, e, k);
			}
			y = v[0];
			z = v[n] += MX(sum, y, z, p, e, k);
		}
		return v;
	}

	/**
	 * Decrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static final int[] decrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], sum, e;
		int p, q = 6 + 52 / (n + 1);

		sum = q * delta;
		while (sum != 0) {
			e = sum >>> 2 & 3;
			for (p = n; p > 0; p--) {
				z = v[p - 1];
				y = v[p] -= MX(sum, y, z, p, e, k);
			}
			z = v[n];
			y = v[0] -= MX(sum, y, z, p, e, k);
			sum = sum - delta;
		}
		return v;
	}

	/**
	 * Convert byte array to int array.
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static final int[] toIntArray(byte[] data, boolean includeLength) {
		int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
		int[] result;

		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	/**
	 * Convert int array to byte array.
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static final byte[] toByteArray(int[] data, boolean includeLength) {
		int n = data.length << 2;

		if (includeLength) {
			int m = data[data.length - 1];

			if (m > n) {
				return null;
			} else {
				n = m;
			}
		}
		byte[] result = new byte[n];

		for (int i = 0; i < n; i++) {
			result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
		}
		return result;
	}
}

// @SuppressLint("NewApi")
// public class Coder_XXTEA {
// public static String Encrypt(String data, String key) {
// return ToHexString(TEAEncrypt(ToLongArray(PadRight(data, MIN_LENGTH).getBytes(Charset.forName("UTF8"))),
// ToLongArray(PadRight(key, MIN_LENGTH).getBytes(Charset.forName("UTF8")))));
// }
//
// public static String Decrypt(String data, String key) {
// if (data == null || data.length() < MIN_LENGTH) {
// return data;
// }
// byte[] code = ToByteArray(TEADecrypt(ToLongArray(data),
// ToLongArray(PadRight(key, MIN_LENGTH).getBytes(Charset.forName("UTF8")))));
// return new String(code, Charset.forName("UTF8"));
// }
//
// private static long[] TEAEncrypt(long[] data, long[] key) {
// int n = data.length;
// if (n < 1) {
// return data;
// }
//
// long z = data[data.length - 1], y = data[0], sum = 0, e, p, q;
// q = 6 + 52 / n;
// while (q-- > 0) {
// sum += DELTA;
// e = (sum >> 2) & 3;
// for (p = 0; p < n - 1; p++) {
// y = data[(int) (p + 1)];
// z = data[(int) p] += (z >> 5 ^ y << 2) + (y >> 3 ^ z << 4) ^ (sum ^ y) + (key[(int) (p & 3 ^ e)] ^ z);
// }
// y = data[0];
// z = data[n - 1] += (z >> 5 ^ y << 2) + (y >> 3 ^ z << 4) ^ (sum ^ y) + (key[(int) (p & 3 ^ e)] ^ z);
// }
//
// return data;
// }
//
// private static long[] TEADecrypt(long[] data, long[] key) {
// int n = data.length;
// if (n < 1) {
// return data;
// }
//
// long z = data[data.length - 1], y = data[0], sum = 0, e, p, q;
// q = 6 + 52 / n;
// sum = q * DELTA;
// while (sum != 0) {
// e = (sum >> 2) & 3;
// for (p = n - 1; p > 0; p--) {
// z = data[(int) (p - 1)];
// y = data[(int) p] -= (z >> 5 ^ y << 2) + (y >> 3 ^ z << 4) ^ (sum ^ y) + (key[(int) (p & 3 ^ e)] ^ z);
// }
// z = data[n - 1];
// y = data[0] -= (z >> 5 ^ y << 2) + (y >> 3 ^ z << 4) ^ (sum ^ y) + (key[(int) (p & 3 ^ e)] ^ z);
// sum -= DELTA;
// }
//
// return data;
// }
//
// private static long[] ToLongArray(byte[] data) {
// int n = (data.length % 8 == 0 ? 0 : 1) + data.length / 8;
// long[] result = new long[n];
//
// for (int i = 0; i < n - 1; i++) {
// result[i] = bytes2long(data, i * 8);
// }
//
// byte[] buffer = new byte[8];
// for (int i = 0, j = (n - 1) * 8; j < data.length; i++, j++) {
// buffer[i] = data[j];
// }
// result[n - 1] = bytes2long(buffer, 0);
//
// return result;
// }
//
// private static byte[] ToByteArray(long[] data) {
// List<Byte> result = new ArrayList<Byte>();
//
// for (int i = 0; i < data.length; i++) {
// byte[] bs = long2bytes(data[i]);
// for (int j = 0; j < 8; j++) {
// result.add(bs[j]);
// }
// }
//
// while (result.get(result.size() - 1) == SPECIAL_CHAR) {
// result.remove(result.size() - 1);
// }
//
// byte[] ret = new byte[result.size()];
// for (int i = 0; i < ret.length; i++) {
// ret[i] = result.get(i);
// }
// return ret;
// }
//
// public static byte[] long2bytes(long num) {
// ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
// buffer.putLong(num);
// return buffer.array();
// }
//
// public static long bytes2long(byte[] b, int index) {
// ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
// buffer.put(b, index, 8);
// return buffer.getLong(0);
// }
//
// private static String ToHexString(long[] data) {
// StringBuilder sb = new StringBuilder();
// for (int i = 0; i < data.length; i++) {
// sb.append(PadLeft(Long.toHexString(data[i]), 16));
// }
// return sb.toString();
// }
//
// private static long[] ToLongArray(String data) {
// int len = data.length() / 16;
// long[] result = new long[len];
// for (int i = 0; i < len; i++) {
// result[i] = new BigInteger(data.substring(i * 16, i * 16 + 16), 16).longValue();
// }
// return result;
// }
//
// private static String PadRight(String source, int length) {
// while (source.length() < length) {
// source += SPECIAL_CHAR;
// }
// return source;
// }
//
// private static String PadLeft(String source, int length) {
// while (source.length() < length) {
// source = '0' + source;
// }
// return source;
// }
//
// private static long DELTA = 2654435769L;
// private static int MIN_LENGTH = 32;
// private static char SPECIAL_CHAR = '\0';
// }

