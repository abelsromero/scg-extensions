package my.extension.encoder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderProcessorTest {

	@Test
	void should_process_header() {
		final String initialValue = "my-header";

		HeaderEncoder headerProcessor = new HeaderEncoder();
		String actual = headerProcessor.encode(initialValue);

		assertThat(actual).isEqualTo("bXktaGVhZGVy");
	}

}
