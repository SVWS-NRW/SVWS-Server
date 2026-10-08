package de.svws_nrw.service.crypto.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Base64;

import javax.crypto.SecretKey;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("SecretCipher")
class SecretCipherTest {

	private static final String FEHLER_ENTSCHLUESSELN = "Das Secret konnte nicht entschlüsselt werden. Die Zugangsdaten müssen neu hinterlegt werden.";

	private SecretCipher cut;

	@BeforeEach
	void setUp() throws Exception {
		final SecretKey key = AES.getRandomKey256();
		cut = new SecretCipher(() -> key);
	}

	@ParameterizedTest
	@ValueSource(strings = { "client-secret", "äöüß € Geheimnis", "" })
	@DisplayName("encrypt/decrypt | Roundtrip liefert den Klartext")
	void roundtrip(final String klartext) {
		assertThat(cut.decrypt(cut.encrypt(klartext))).isEqualTo(klartext);
	}

	@Test
	@DisplayName("encrypt/decrypt | Roundtrip mit langem Secret (4096 Zeichen)")
	void roundtrip_LangesSecret() {
		final String klartext = "x".repeat(4096);

		assertThat(cut.decrypt(cut.encrypt(klartext))).isEqualTo(klartext);
	}

	@Test
	@DisplayName("encrypt | gleicher Klartext ergibt wegen zufälligem IV unterschiedliche Chiffrate")
	void encrypt_ZufaelligerIV() {
		final String erstes = cut.encrypt("client-secret");
		final String zweites = cut.encrypt("client-secret");

		assertThat(erstes).isNotEqualTo(zweites).doesNotContain("client-secret");
	}

	@Test
	@DisplayName("encrypt | Ergebnis ist Base64 aus 16 Byte IV und blockweisem Chiffrat")
	void encrypt_Format() {
		final byte[] roh = Base64.getDecoder().decode(cut.encrypt("client-secret"));

		assertThat(roh).hasSizeGreaterThanOrEqualTo(32);
		assertThat(roh.length % 16).isZero();
	}

	@ParameterizedTest
	@ValueSource(strings = { "client-secret", "abcd", "%%%kein-base64%%%" })
	@DisplayName("decrypt | Klartext-Altdaten bzw. ungültige Eingaben -> 500 mit Hinweis auf Neu-Hinterlegen")
	void decrypt_UngueltigeEingabe_WirftFehler(final String eingabe) {
		assertThatThrownBy(() -> cut.decrypt(eingabe))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage(FEHLER_ENTSCHLUESSELN)
				.extracting("status")
				.isEqualTo(Status.INTERNAL_SERVER_ERROR);
	}

	@Test
	@DisplayName("decrypt | mit falschem Schlüssel kommt nie der Klartext heraus")
	void decrypt_FalscherSchluessel_LiefertNieDenKlartext() throws Exception {
		final SecretKey andererKey = AES.getRandomKey256();
		final SecretCipher andererCipher = new SecretCipher(() -> andererKey);
		final String chiffrat = cut.encrypt("client-secret");

		String ergebnis = null;
		try {
			ergebnis = andererCipher.decrypt(chiffrat);
		} catch (final ApiOperationException e) {
			assertThat(e.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
			assertThat(e).hasMessage(FEHLER_ENTSCHLUESSELN);
		}
		assertThat(ergebnis).isNotEqualTo("client-secret");
	}

	@Test
	@DisplayName("encrypt/decrypt | Fehler des SecretKeyProvider werden unverändert durchgereicht")
	void providerFehler_WirdDurchgereicht() {
		final ApiOperationException fehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Keystore kaputt");
		final SecretCipher cipher = new SecretCipher(() -> {
			throw fehler;
		});

		assertThatThrownBy(() -> cipher.encrypt("x")).isSameAs(fehler);
		assertThatThrownBy(() -> cipher.decrypt("x")).isSameAs(fehler);
	}
}
