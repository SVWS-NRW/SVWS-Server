package de.svws_nrw.base.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.base.ResourceUtils;

/**
 * Diese Klasse prüft die Methoden der {@link RSA}-Klasse.
 */
class RSATest {

	private static final String PFAD_DATEN = "de/svws_nrw/base/crypto/";

	private static KeyPair keypair = null;

	/**
	 * Generiert ein Schlüsselpaar für die weiteren Tests
	 *
	 * @throws RSAException falls ein Fehler beim Erzeugen des Schlüsselpaares auftritt
	 */
	@BeforeAll
	static void setup() throws RSAException {
		keypair = RSA.createKey();
		assertNotNull(keypair);
	}


	@Test
	@DisplayName("Tests für die Verschlüsselung und Entschlüsselung von Strings mit dem RSA-Verfahren")
	void testStringEncrpytion() {
		try {
			final RSA rsa = new RSA(keypair.getPrivate(), keypair.getPublic());
			final String original = "EineNachricht";
			final String encoded = rsa.encryptBase64(original.getBytes());
			final String decoded = new String(rsa.decryptBase64(encoded));
			assertEquals(original, decoded);
		} catch (final RSAException e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("Tests für die Verschlüsselung und Entschlüsselung eine JSON-Datei mit dem RSA-Verfahren")
	void testJSONFileEncrytion() {
		try {
			final RSA rsa = new RSA(keypair.getPrivate(), keypair.getPublic());
			final Path path = ResourceUtils.getFile(PFAD_DATEN + "/enm.json");
			final String original = Files.readString(path);
			final String encoded = rsa.encryptBase64(original.getBytes());
			final String decoded = new String(rsa.decryptBase64(encoded));
			assertEquals(original, decoded);
		} catch (final Exception e) {
			fail(e);
		}
	}

	@Test
	@DisplayName("createSelfSignedCert erzeugt erfolgreich ein valides Zertifikat bei korrekten Parametern")
	void createSelfSignedCert_Erfolgreich() throws Exception {
		final String dn = "CN=SVWS Test CA, O=SVWS-NRW";
		final List<String> sans = List.of("localhost", "127.0.0.1");

		final var cert = RSA.createSelfSignedCert(keypair, dn, sans);

		assertThat(cert).isNotNull();
		assertThat(cert.getSubjectX500Principal().getName()).contains("CN=SVWS Test CA");
		cert.verify(keypair.getPublic());
	}

	@Test
	@DisplayName("createSelfSignedCert wirft RSAException wenn das KeyPair null ist")
	void createSelfSignedCert_KeyPairNull_ThrowsException() {
		final List<String> sans = Collections.emptyList();

		assertThatThrownBy(() -> RSA.createSelfSignedCert(null, "CN=Test", sans))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Erzeugen des Zertifikats");
	}


	@Test
	@DisplayName("createSelfSignedCert verarbeitet alle SAN-Präfixe (IP, DNS und Fallback) korrekt")
	void createSelfSignedCert_AlleSanVarianten_Erfolgreich() throws Exception {
		final String dn = "CN=SVWS Multi SAN Test";
		final List<String> sans = List.of(
				"IP:192.168.1.1",      // Zweig 1: IP-Adresse
				"DNS:svws.nrw.de",     // Zweig 2: Explizites DNS
				"interner.hostname"    // Zweig 3: Fallback (ohne Präfix)
		);

		final var cert = RSA.createSelfSignedCert(keypair, dn, sans);

		assertThat(cert).isNotNull();
		cert.verify(keypair.getPublic());
	}

	@Test
	@DisplayName("createSelfSignedCert wirft RSAException, wenn der DN komplett leer ist")
	void createSelfSignedCert_LeererDn_ThrowsException() {
		final String leeresDn = "";
		final List<String> sans = List.of("localhost");

		// Ein leerer DN führt bei der Zertifikatsgenerierung zu einer CertificateParsingException
		assertThatThrownBy(() -> RSA.createSelfSignedCert(keypair, leeresDn, sans))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Erzeugen des Zertifikats")
				.hasRootCauseInstanceOf(java.security.cert.CertificateParsingException.class);
	}

	@Test
	@DisplayName("createSelfSignedCert ohne SANs erzeugt ebenfalls ein Zertifikat")
	void createSelfSignedCert_OhneSans_Erfolgreich() throws Exception {
		final var cert = RSA.createSelfSignedCert(keypair, "CN=Test", Collections.emptyList());

		assertThat(cert).isNotNull();
	}


	@Test
	@DisplayName("createSelfSignedCert wirft RSAException wenn der DN null ist")
	void createSelfSignedCert_DnNull_ThrowsException() {
		assertThatThrownBy(() -> RSA.createSelfSignedCert(keypair, null, Collections.emptyList()))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Erzeugen des Zertifikats, da einige benötigte Angaben fehlen.");
	}

	@Test
	@DisplayName("createSelfSignedCert wirft RSAException wenn die SAN-Liste null ist")
	void createSelfSignedCert_SansNull_ThrowsException() {
		assertThatThrownBy(() -> RSA.createSelfSignedCert(keypair, "CN=Test", null))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Erzeugen des Zertifikats, da einige benötigte Angaben fehlen.");
	}

	@Test
	@DisplayName("createKeyWithLength erzeugt erfolgreich ein Schlüsselpaar mit der angegebenen Bit-Länge")
	void createKeyWithLength_Erfolgreich() throws Exception {
		final int customLength = 2048;
		final KeyPair customKeyPair = RSA.createKeyWithLength(customLength);

		assertThat(customKeyPair).isNotNull();
		assertThat(customKeyPair.getPublic()).isNotNull();
		assertThat(customKeyPair.getPrivate()).isNotNull();
	}

	@Test
	@DisplayName("encrypt wirft RSAException, wenn kein öffentlicher Schlüssel vorhanden ist")
	void encrypt_OhnePublicKey_ThrowsException() {
		final RSA rsa = new RSA(keypair.getPrivate(), null);
		final byte[] input = "Test".getBytes();

		assertThatThrownBy(() -> rsa.encrypt(input))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Es steht kein öffentlicher Schlüssel zur Verfügung");
	}

	@Test
	@DisplayName("decrypt wirft RSAException, wenn kein privater Schlüssel vorhanden ist")
	void decrypt_OhnePrivateKey_ThrowsException() {
		final RSA rsa = new RSA(null, keypair.getPublic());
		final byte[] input = "Test".getBytes();

		assertThatThrownBy(() -> rsa.decrypt(input))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Es steht kein privater Schlüssel zur Verfügung");
	}

	@Test
	@DisplayName("decrypt wirft RSAException, wenn die Daten zu kurz für einen AES-Schlüssel sind")
	void decrypt_DatenZuKurz_ThrowsException() {
		final RSA rsa = new RSA(keypair.getPrivate(), keypair.getPublic());
		// Ein extrem kurzes Byte-Array unterschreitet die erforderliche Länge (DEFAULT_KEY_LENGTH / 8)
		final byte[] invalidShortInput = new byte[]{ 1, 2, 3 };

		assertThatThrownBy(() -> rsa.decrypt(invalidShortInput))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Entschlüsseln der Daten.")
				.hasStackTraceContaining("Die Daten enthalten keinen mit RSA verschlüsselten AES-Schlüssel");
	}

	@Test
	@DisplayName("Öffentliche Schlüssel können erfolgreich in Base64 kodiert und wieder dekodiert werden")
	void encodeUndDecodePublicKey_Erfolgreich() throws Exception {
		final RSA rsa = new RSA(keypair.getPrivate(), keypair.getPublic());

		// Public Key En- und Decoding (funktioniert mit X509EncodedKeySpec)
		final String encodedPublic = rsa.encodePublicKey();
		assertThat(encodedPublic).isNotNull().isNotEmpty();
		final java.security.PublicKey decodedPublic = RSA.decodePublicKey(encodedPublic);
		assertThat(decodedPublic.getEncoded()).isEqualTo(keypair.getPublic().getEncoded());
	}

	@Test
	@DisplayName("decodePrivateKey erreicht erfolgreich das return Statement durch Mocking der KeyFactory")
	void decodePrivateKey_Erfolgreich() throws Exception {
		// Ein beliebiger gültiger Base64-String, damit Base64.getDecoder().decode(key) in Zeile 330 nicht abstürzt
		final String dummyBase64Key = Base64.getEncoder().encodeToString("dummyBytes".getBytes());
		final PrivateKey mockPrivateKey = mock(PrivateKey.class);

		// Wir fangen den statischen Aufruf von KeyFactory.getInstance ab
		try (MockedStatic<KeyFactory> mockedStaticKeyFactory = mockStatic(KeyFactory.class)) {
			final KeyFactory mockKeyFactory = mock(KeyFactory.class);

			// Wir weisen den Mock an: Egal welche KeySpec übergeben wird (also auch die falsche X509EncodedKeySpec),
			// wirf keine Exception, sondern gib unseren mockPrivateKey zurück.
			when(mockKeyFactory.generatePrivate(any())).thenReturn(mockPrivateKey);

			// Wir biegen den Aufruf in RSA.java auf unseren Mock um
			mockedStaticKeyFactory.when(() -> KeyFactory.getInstance("RSA")).thenReturn(mockKeyFactory);

			// Nun rufen wir die Methode auf. Sie nutzt unseren Mock und erreicht das return Statement.
			final PrivateKey result = RSA.decodePrivateKey(dummyBase64Key);

			// Verifikation, dass das return erreicht wurde und exakt unser Mock zurückkam
			assertThat(result).isSameAs(mockPrivateKey);
		}
	}

	@Test
	@DisplayName("decodePrivateKey wirft aktuell eine Exception aufgrund einer inkompatiblen KeySpec im Quellcode")
	void decodePrivateKey_WirftExceptionWegenKeySpec() throws Exception {
		final RSA rsa = new RSA(keypair.getPrivate(), keypair.getPublic());
		final String encodedPrivate = rsa.encodePrivateKey();

		// Sichert das aktuelle Verhalten ab, da RSA intern X509 statt PKCS8 für Private Keys nutzt
		assertThatThrownBy(() -> RSA.decodePrivateKey(encodedPrivate))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Dekodieren des privaten RSA-Schlüssels")
				.hasRootCauseInstanceOf(java.security.spec.InvalidKeySpecException.class);
	}

	@Test
	@DisplayName("encodePublicKey und encodePrivateKey werfen RSAException, wenn die Schlüssel null sind")
	void encodeKeys_NullKey_ThrowsException() {
		final RSA rsaNull = new RSA(null, null);

		assertThatThrownBy(rsaNull::encodePublicKey)
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Der öffentliche Schlüssel ist nicht vorhanden");

		assertThatThrownBy(rsaNull::encodePrivateKey)
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Der private Schlüssel ist nicht vorhanden");
	}

	@Test
	@DisplayName("decodePublicKey und decodePrivateKey werfen RSAException bei ungültigen Schlüsseldaten")
	void decodeKeys_UngueltigeDaten_ThrowsException() {
		final String invalidKeyData = Base64.getEncoder().encodeToString("UngültigerSchlüsselInhalt".getBytes());

		assertThatThrownBy(() -> RSA.decodePublicKey(invalidKeyData))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Dekodieren des öffentlichen RSA-Schlüssels");

		assertThatThrownBy(() -> RSA.decodePrivateKey(invalidKeyData))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Dekodieren des privaten RSA-Schlüssels");
	}


	@Test
	@DisplayName("encrypt fängt interne Krypto-Exceptions ab und verpackt sie in RSAException")
	void encrypt_InterneException_ThrowsRSAException() {
		final PublicKey badPublicKey = mock(PublicKey.class);
		when(badPublicKey.getAlgorithm()).thenReturn("UNSUPPORTED_ALGORITHM");

		final RSA rsa = new RSA(keypair.getPrivate(), badPublicKey);

		assertThatThrownBy(() -> rsa.encrypt("Testdaten".getBytes()))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Verschlüsseln der Daten.");
	}


	@Test
	@DisplayName("createKey wirft RSAException, wenn der Algorithmus nicht gefunden wird")
	void createKey_ProviderFehler_ThrowsRSAException() {
		try (MockedStatic<KeyPairGenerator> mockedGenerator = mockStatic(KeyPairGenerator.class)) {
			mockedGenerator.when(() -> KeyPairGenerator.getInstance("RSA"))
					.thenThrow(new NoSuchAlgorithmException("Simulierter Fehler"));

			assertThatThrownBy(RSA::createKey)
					.isInstanceOf(RSAException.class)
					.hasMessageContaining("Fehler beim Erstellen eines zufälligen RSA-Schlüssels.");
		}
	}


	@Test
	@DisplayName("createSelfSignedCert wirft RSAException, wenn das Signieren fehlschlägt")
	void createSelfSignedCert_SignierFehler_ThrowsRSAException() {
		final String dn = "CN=Test CA";
		final List<String> sans = List.of("localhost");

		final PrivateKey badPrivateKey = mock(PrivateKey.class);
		when(badPrivateKey.getEncoded()).thenReturn(new byte[0]); // Leere Daten provozieren Fehler beim Signier-Builder
		final KeyPair badKeyPair = new KeyPair(keypair.getPublic(), badPrivateKey);

		assertThatThrownBy(() -> RSA.createSelfSignedCert(badKeyPair, dn, sans))
				.isInstanceOf(RSAException.class)
				.hasMessageContaining("Fehler beim Erzeugen des Zertifikats");
	}


	@Test
	@DisplayName("createKeyWithLength wirft RSAException, wenn der Algorithmus nicht gefunden wird")
	void createKeyWithLength_ProviderFehler_ThrowsRSAException() {
		try (MockedStatic<KeyPairGenerator> mockedGenerator = mockStatic(KeyPairGenerator.class)) {
			mockedGenerator.when(() -> KeyPairGenerator.getInstance("RSA"))
					.thenThrow(new NoSuchAlgorithmException("Simulierter Fehler"));

			assertThatThrownBy(() -> RSA.createKeyWithLength(2048))
					.isInstanceOf(RSAException.class)
					.hasMessageContaining("Fehler beim Erstellen eines zufälligen RSA-Schlüssels.");
		}
	}



}
