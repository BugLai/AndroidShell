package net.youmi.android;


import java.io.*;

public class Coder_Base64 {

	/*
	 * <p Encodes a string </p <p Before the string is encoded in Base64, it is converted in a binary sequence using the
	 * system default charset </p
	 * 
	 * @param str The source string
	 * 
	 * @return The encoded string
	 * 
	 * @throw RuntimeException If an unexpected error occurs
	 */
	public static String encode(String str) throws RuntimeException {
		byte[] bytes = str.getBytes();
		byte[] encoded = encode(bytes);
		try {
			return new String(encoded, Global_Charsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 is not supported!", e);
		}
	}

	/*
	 * <p Encodes a string </p <p Before the string is encoded in Base64, it is converted in a binar sequence using the
	 * supplied charset </p
	 * 
	 * @param str The source string
	 * 
	 * @param charset The charset name
	 * 
	 * @return The encoded string
	 * 
	 * @throw RuntimeException If an unexpected error occurs
	 * 
	 * @since 1.
	 */
	public static String encode(String str, String charset) throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
		byte[] encoded = encode(bytes);
		try {
			return new String(encoded, Global_Charsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 is not supported!", e);
		}
	}

	/*
	 * <p Decodes the supplied string </p <p The supplied string is decoded into a binary sequence, and then th sequence
	 * is encoded with the system default charset and returned </p
	 * 
	 * @para st The encoded string
	 * 
	 * @retur Th decoded string
	 * 
	 * @throw RuntimeExceptio If an unexpected error occurs
	 */
	public static String decode(String str) throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(Global_Charsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 is not supported!", e);
		}
		byte[] decoded = decode(bytes);
		return new String(decoded);
	}

	public static byte[] decodeToBytes(String str) throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(Global_Charsets.UTF_8);
			return decode(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 is not supported!", e);
		}
	}

	/*
	 * <p Decodes the supplied string </p <p The supplied string is decoded into a binary sequence, and then th sequence
	 * is encoded with the supplied charset and returned </p
	 * 
	 * @para st The encoded string
	 * 
	 * @para charse The charset name
	 * 
	 * @retur Th decoded string
	 * 
	 * @throw RuntimeExceptio If an unexpected error occurs
	 * 
	 * @sinc 1.
	 */
	public static String decode(String str, String charset) throws RuntimeException {
		byte[] bytes;
		try {
			bytes = str.getBytes(Global_Charsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 is not supported!", e);
		}
		byte[] decoded = decode(bytes);
		try {
			return new String(decoded, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported charset: " + charset, e);
		}
	}

	/*
	 * <p Encodes a binary sequence </p <p If data are large, i.e. if you are working with large binary files consider
	 * to use a {@lin Base64OutputStream} instead of loading too muc data in memory </p
	 * 
	 * @para byte The source sequence
	 * 
	 * @retur Th encoded sequence
	 * 
	 * @throw RuntimeExceptio If an unexpected error occurs
	 * 
	 * @sinc 1.
	 */
	public static byte[] encode(byte[] bytes) throws RuntimeException {
		return encode(bytes, 0);
	}

	/*
	 * <p Encodes a binary sequence, wrapping every encoded line ever <em>wrapAt</em> characters. A <em>wrapAt</em>
	 * value less than 1 disable wrapping </p <p If data are large, i.e. if you are working with large binary files
	 * consider to use a {@lin Base64OutputStream} instead of loading too muc data in memory </p
	 * 
	 * @para byte The source sequence
	 * 
	 * @para wrapA The max line length for encoded data. If less than 1 no wra is applied
	 * 
	 * @retur Th encoded sequence
	 * 
	 * @throw RuntimeExceptio If an unexpected error occurs
	 * 
	 * @sinc 1.
	 */
	public static byte[] encode(byte[] bytes, int wrapAt) throws RuntimeException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			encode(inputStream, outputStream, wrapAt);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O error", e);
		} finally {
			try {
				inputStream.close();
			} catch (Throwable t) {
				;
			}
			try {
				outputStream.close();
			} catch (Throwable t) {
				;
			}
		}
		return outputStream.toByteArray();
	}

	/*
	 * <p Decodes a binary sequence </p <p If data are large, i.e. if you are working with large binary files consider
	 * to use a {@lin Base64InputStream} instead of loading too muc data in memory </p
	 * 
	 * @para byte The encoded sequence
	 * 
	 * @retur Th decoded sequence
	 * 
	 * @throw RuntimeExceptio If an unexpected error occurs
	 * 
	 * @sinc 1.
	 */
	public static byte[] decode(byte[] bytes) throws RuntimeException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			decode(inputStream, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O error", e);
		} finally {
			try {
				inputStream.close();
			} catch (Throwable t) {
				;
			}
			try {
				outputStream.close();
			} catch (Throwable t) {
				;
			}
		}
		return outputStream.toByteArray();
	}

	/*
	 * <p Encodes data from the given input stream and writes them in the give output stream </p <p The supplied input
	 * stream is read until its end is reached, but it's no closed by this method </p <p The supplied output stream is
	 * nor flushed neither closed by this method </p
	 * 
	 * @para inputStream The input stream
	 * 
	 * @para outputStream The output stream
	 * 
	 * @throw java.io.IOException If an I/O error occurs
	 */
	public static void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
		encode(inputStream, outputStream, 0);
	}

	/*
	 * <p Encodes data from the given input stream and writes them in the give output stream, wrapping every encoded
	 * line every <em>wrapAt</em characters. A <em>wrapAt</em> value less than 1 disables wrapping </p <p The supplied
	 * input stream is read until its end is reached, but it's no closed by this method </p <p The supplied output
	 * stream is nor flushed neither closed by this method </p
	 * 
	 * @para inputStrea The input stream from which clear data are read
	 * 
	 * @para outputStrea The output stream in which encoded data are written
	 * 
	 * @para wrapA The max line length for encoded data. If less than 1 no wra is applied
	 * 
	 * @throw java.io.IOExceptio If an I/O error occurs
	 */
	public static void encode(InputStream inputStream, OutputStream outputStream, int wrapAt) throws IOException {
		Coder_Common_Base64_Base64OutputStream aux = new Coder_Common_Base64_Base64OutputStream(outputStream, wrapAt);
		copy(inputStream, aux);
		aux.commit();
	}

	/*
	 * <p Decodes data from the given input stream and writes them in the give output stream </p <p The supplied input
	 * stream is read until its end is reached, but it's no closed by this method </p <p The supplied output stream is
	 * nor flushed neither closed by this method </p
	 * 
	 * @para inputStream The input stream from which encoded data are read
	 * 
	 * @para outputStream The output stream in which decoded data are written
	 * 
	 * @throw java.io.IOException If an I/O error occurs
	 */
	public static void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
		copy(new Coder_Common_Base64_Base64InputStream(inputStream), outputStream);
	}

	/*
	 * <p Encodes data from the given source file contents and writes them in th given target file, wrapping every
	 * encoded line every <em>wrapAt</em characters. A <em>wrapAt</em> value less than 1 disables wrapping </p
	 * 
	 * @para source The source file, from which decoded data are read
	 * 
	 * @para target The target file, in which encoded data are written
	 * 
	 * @para wrapAt The max line length for encoded data. If less than 1 no wra is applied
	 * 
	 * @throw java.io.IOException If an I/O error occurs
	 * 
	 * @since 1.
	 */
	public static void encode(File source, File target, int wrapAt) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			Coder_Base64.encode(inputStream, outputStream, wrapAt);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Throwable t) {
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Throwable t) {
			}
		}
	}

	/*
	 * <p Encodes data from the given source file contents and writes them in th given target file </p
	 * 
	 * @para source The source file, from which decoded data are read
	 * 
	 * @para target The target file, in which encoded data are written
	 * 
	 * @throw java.io.IOException If an I/O error occurs
	 * 
	 * @since 1.
	 */
	public static void encode(File source, File target) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			Coder_Base64.encode(inputStream, outputStream);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Throwable t) {
					;
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}

	/*
	 * <p Decodes data from the given source file contents and writes them in th given target file </p
	 * 
	 * @para source The source file, from which encoded data are read
	 * 
	 * @para target The target file, in which decoded data are written
	 * 
	 * @throw java.io.IOException If an I/O error occurs
	 * 
	 * @since 1.
	 */
	public static void decode(File source, File target) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(target);
			decode(inputStream, outputStream);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Throwable t) {
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Throwable t) {
			}
		}
	}

	/*
	 * Copies data from a stream to another
	 * 
	 * @para inputStream The input stream
	 * 
	 * @para outputStream The output stream
	 * 
	 * @throw java.io.IOException If a unexpected I/O error occurs
	 */
	private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		// 1KB buffer
		byte[] b = new byte[1024];
		int len;
		while ((len = inputStream.read(b)) != -1) {
			outputStream.write(b, 0, len);
		}
	}

}
