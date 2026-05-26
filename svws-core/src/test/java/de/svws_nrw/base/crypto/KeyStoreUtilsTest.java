package de.svws_nrw.base.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

}
