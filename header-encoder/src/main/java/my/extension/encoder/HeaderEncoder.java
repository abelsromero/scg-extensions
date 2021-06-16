package my.extension.encoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HeaderEncoder {

	public String encode(String value) {
		return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}
}
