import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class test {
	public static void main(String[] args) {
		// Generate the key
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		byte[] secretBytes = key.getEncoded();

		// Convert the key to a Base64-encoded string
		String secretString = Base64.getEncoder().encodeToString(secretBytes);

		// Print the key
		System.out.println(secretString);
	}
}
