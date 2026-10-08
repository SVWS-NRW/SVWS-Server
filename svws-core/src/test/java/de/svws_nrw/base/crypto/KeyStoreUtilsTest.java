package de.svws_nrw.base.crypto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Diese Klasse enthält die Tests zu {@link KeyStoreUtils}
 */
class KeyStoreUtilsTest {

	@TempDir
	private Path tempDir;

	@Test
	@DisplayName("addPrivateKeyCertificate fügt Schlüssel hinzu und schreibt den Keystore erfolgreich in eine Datei")
	void addPrivateKeyCertificate_Erfolgreich() throws Exception {
		final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(null, null);

		final KeyPair keypair = RSA.createKeyWithLength(2048);
		final X509Certificate cert = RSA.createSelfSignedCert(keypair, "CN=TestStore", Collections.emptyList());

		final Path keystorePath = tempDir.resolve("test_keystore.p12");
		final String password = "changeit";
		final String alias = "svws-alias";

		KeyStoreUtils.addPrivateKeyCertificate(keystore, keystorePath.toString(), password, alias, keypair.getPrivate(), cert);

		assertThat(keystore.containsAlias(alias)).isTrue();
		assertThat(keystore.getKey(alias, password.toCharArray())).isNotNull();
		assertThat(keystorePath.toFile()).exists();
	}

	@Test
	@DisplayName("addPrivateKeyCertificate wirft KeyStoreException wenn Pflichtparameter ungültig oder null sind")
	void addPrivateKeyCertificate_MissingParameters_ThrowsKeyStoreException() throws Exception {
		final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(null, null);
		final KeyPair keypair = RSA.createKeyWithLength(2048);
		final X509Certificate cert = RSA.createSelfSignedCert(keypair, "CN=TestStore", Collections.emptyList());
		final String loc = tempDir.resolve("keystore.p12").toString();

		// Überprüfung unzulässiger Aliase
		assertThatThrownBy(() -> KeyStoreUtils.addPrivateKeyCertificate(keystore, loc, "pass", "", keypair.getPrivate(), cert))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Für das Hinzufügen muss ein Alias angegeben werden.");

		assertThatThrownBy(() -> KeyStoreUtils.addPrivateKeyCertificate(keystore, loc, "pass", null, keypair.getPrivate(), cert))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Für das Hinzufügen muss ein Alias angegeben werden.");

		// Überprüfung fehlender privater Schlüssel
		assertThatThrownBy(() -> KeyStoreUtils.addPrivateKeyCertificate(keystore, loc, "pass", "alias", null, cert))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Für das Hinzufügen muss ein privater Schlüssel angegeben werden.");

		// Überprüfung fehlendes Zertifikat
		assertThatThrownBy(() -> KeyStoreUtils.addPrivateKeyCertificate(keystore, loc, "pass", "alias", keypair.getPrivate(), null))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Für das Hinzufügen muss ein Zertifikat angegeben werden.");
	}

	@Test
	@DisplayName("setSecretKey/storeKeystore/getSecretKey | Schlüssel übersteht Speichern und Laden")
	void secretKey_Roundtrip() throws Exception {
		final String alias = "alias";
		final String password = "pw";
		final KeyStore keystore = KeyStoreUtils.newKeystore();
		final SecretKey key = AES.getRandomKey256();
		final Path location = tempDir.resolve("secrets.keystore");

		KeyStoreUtils.setSecretKey(keystore, alias, key, password);
		KeyStoreUtils.storeKeystore(keystore, location.toString(), password);
		final KeyStore geladen = KeyStoreUtils.getKeystore(location.toString(), password);

		final SecretKey actualSecretKey = Objects.requireNonNull(KeyStoreUtils.getSecretKey(geladen, alias, password));
		assertThat(actualSecretKey.getEncoded()).isEqualTo(key.getEncoded());
	}

	@Test
	@DisplayName("getSecretKey | liefert null, wenn der Alias nicht existiert")
	void getSecretKey_FehlenderAlias_Null() throws Exception {
		final KeyStore keystore = KeyStoreUtils.newKeystore();

		assertThat(KeyStoreUtils.getSecretKey(keystore, "gibt-es-nicht", "pw")).isNull();
	}

	@Test
	@DisplayName("getSecretKey | wirft KeyStoreException, wenn der Alias kein symmetrischer Schlüssel ist")
	void getSecretKey_Zertifikat_ThrowsKeyStoreException() throws Exception {
		final KeyStore keystore = KeyStoreUtils.newKeystore();
		final KeyPair keypair = RSA.createKeyWithLength(2048);
		final X509Certificate cert = RSA.createSelfSignedCert(keypair, "CN=TestStore", Collections.emptyList());
		KeyStoreUtils.addCertificate(keystore, "cert", cert);

		assertThatThrownBy(() -> KeyStoreUtils.getSecretKey(keystore, "cert", "pw"))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("kein symmetrischer Schlüssel");
	}

	@Test
	@DisplayName("getSecretKey | wirft KeyStoreException bei leerem oder fehlendem Alias")
	void getSecretKey_UngueltigerAlias_ThrowsKeyStoreException() throws Exception {
		final KeyStore keystore = KeyStoreUtils.newKeystore();

		assertThatThrownBy(() -> KeyStoreUtils.getSecretKey(keystore, null, "pw"))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Alias");
		assertThatThrownBy(() -> KeyStoreUtils.getSecretKey(keystore, " ", "pw"))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Alias");
	}

	@Test
	@DisplayName("setSecretKey | wirft KeyStoreException bei ungültigen Parametern")
	void setSecretKey_UngueltigeParameter_ThrowsKeyStoreException() throws Exception {
		final KeyStore keystore = KeyStoreUtils.newKeystore();
		final SecretKey key = AES.getRandomKey256();

		assertThatThrownBy(() -> KeyStoreUtils.setSecretKey(keystore, "", key, "pw"))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Für das Hinzufügen muss ein Alias angegeben werden.");
		assertThatThrownBy(() -> KeyStoreUtils.setSecretKey(keystore, null, key, "pw"))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Für das Hinzufügen muss ein Alias angegeben werden.");
		assertThatThrownBy(() -> KeyStoreUtils.setSecretKey(keystore, "alias", null, "pw"))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Für das Hinzufügen muss ein symmetrischer Schlüssel angegeben werden.");
	}

	@Test
	@DisplayName("storeKeystore | wirft NullPointerException bei fehlendem Keystore, Pfad oder Kennwort und hinterlässt keine Temp-Datei")
	void storeKeystore_NullParameter_ThrowsNullPointerExceptionOhneTempReste() throws Exception {
		final KeyStore keystore = KeyStoreUtils.newKeystore();
		final String location = tempDir.resolve("secrets.keystore").toString();
		assertThatThrownBy(() -> KeyStoreUtils.storeKeystore(null, location, "pw"))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Keystore");
		assertThatThrownBy(() -> KeyStoreUtils.storeKeystore(keystore, null, "pw"))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Pfad");
		assertThatThrownBy(() -> KeyStoreUtils.storeKeystore(keystore, location, null))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Kennwort");

		try (Stream<Path> files = Files.list(tempDir)) {
			assertThat(files).isEmpty();
		}
	}

	@Test
	@DisplayName("storeKeystore | ersetzt eine vorhandene Datei und hinterlässt keine Temp-Datei")
	void storeKeystore_ErsetztDateiOhneTempReste() throws Exception {
		final String alias = "alias";
		final String password = "pw";
		final String fileName = "secrets.keystore";
		final Path location = tempDir.resolve(fileName);
		Files.writeString(location, "alter Inhalt");
		final KeyStore keystore = KeyStoreUtils.newKeystore();
		KeyStoreUtils.setSecretKey(keystore, alias, AES.getRandomKey256(), password);

		KeyStoreUtils.storeKeystore(keystore, location.toString(), password);

		assertThat(KeyStoreUtils.getKeystore(location.toString(), password).containsAlias(alias)).isTrue();
		try (Stream<Path> files = Files.list(tempDir)) {
			assertThat(files.map(p -> p.getFileName().toString()).toList()).isEqualTo(List.of(fileName));
		}
	}

	@Test
	@DisplayName("storeKeystore | wirft KeyStoreException, wenn das Zielverzeichnis nicht existiert")
	void storeKeystore_FehlendesVerzeichnis_ThrowsKeyStoreException() throws Exception {
		final Path location = tempDir.resolve("gibt-es-nicht").resolve("secrets.keystore");
		final KeyStore keystore = KeyStoreUtils.newKeystore();

		assertThatThrownBy(() -> KeyStoreUtils.storeKeystore(keystore, location.toString(), "pw"))
				.isInstanceOf(KeyStoreException.class)
				.hasMessageContaining("Fehler beim Schreiben des Keystores");
		assertThat(tempDir.resolve("gibt-es-nicht")).doesNotExist();
	}

}
