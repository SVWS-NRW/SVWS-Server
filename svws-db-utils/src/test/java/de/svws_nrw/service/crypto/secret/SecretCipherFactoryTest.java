package de.svws_nrw.service.crypto.secret;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SecretCipherFactory")
class SecretCipherFactoryTest {

	@Test
	@DisplayName("getSecretCipher | liefert einen Cipher, ohne den Schlüssel zu laden")
	void getSecretCipher() {
		assertThat(SecretCipherFactory.getNewInstance().getSecretCipher()).isNotNull();
	}
}
