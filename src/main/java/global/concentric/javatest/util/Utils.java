package global.concentric.javatest.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

	public static String stringToSha512(String passwordToHash)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String generatedPassword = null;
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] bytes;
		bytes = md.digest(passwordToHash.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		generatedPassword = sb.toString();
		return generatedPassword;
	}
	
	public static String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Operation failed. See server log for more information";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

	

}
