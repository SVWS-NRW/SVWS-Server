package de.svws_nrw.service.crypto.secret;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.crypto.SecretKey;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.KeyStoreUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("KeyStoreSecretKeyProvider")
class KeyStoreSecretKeyProviderTest {

	private static final String FEHLER_LADEN = "Der Schlüssel zur Verschlüsselung von Secrets konnte nicht aus dem Keystore geladen werden.";
	private static final String FEHLER_SPEICHERN = "Der Keystore für die Verschlüsselung von Secrets konnte nicht gespeichert werden.";

	private static final String ALIAS = "oauth-credentials";
	private static final String PASSWORD = "svwskeystore";

	@TempDir
	private Path tempDir;

	@Test
	@DisplayName("getKey | erzeugt Keystore-Datei und AES-256-Schlüssel, wenn die Datei fehlt")
	void getKey_DateiFehlt_ErzeugtDateiUndSchluessel() {
		final var cut = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS);

		final SecretKey key = cut.getKey();

		assertThat(key.getAlgorithm()).isEqualTo("AES");
		assertThat(key.getEncoded()).hasSize(32);
		assertThat(getKeystoreFile()).exists();
	}

	@Test
	@DisplayName("getKey | lädt bei wiederholtem Aufruf den Schlüssel jedes Mal neu aus dem Keystore")
	void getKey_Wiederholt_LaedtSchluesselNeuAusKeystore() {
		final var cut = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS);

		final SecretKey erster = cut.getKey();
		final SecretKey zweiter = cut.getKey();

		assertThat(zweiter).isNotSameAs(erster);
		assertThat(zweiter.getEncoded()).isEqualTo(erster.getEncoded());
	}

	@Test
	@DisplayName("getKey | verwendet einen im Keystore ausgetauschten Schlüssel beim nächsten Aufruf")
	void getKey_KeystoreGeaendert_LiefertAktuellenSchluessel() throws Exception {
		final var cut = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS);
		final SecretKey alt = cut.getKey();
		final KeyStore keystore = KeyStoreUtils.getKeystore(getKeystoreFile().toString(), PASSWORD);
		final SecretKey neu = AES.getRandomKey256();
		KeyStoreUtils.setSecretKey(keystore, ALIAS, neu, PASSWORD);
		KeyStoreUtils.storeKeystore(keystore, getKeystoreFile().toString(), PASSWORD);

		final SecretKey ergebnis = cut.getKey();

		assertThat(ergebnis.getEncoded()).isEqualTo(neu.getEncoded()).isNotEqualTo(alt.getEncoded());
	}

	@Test
	@DisplayName("getKey | neue Instanz auf derselben Datei liefert denselben Schlüssel")
	void getKey_NeueInstanz_LiefertPersistiertenSchluessel() {
		final SecretKey erster = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS).getKey();

		final SecretKey zweiter = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS).getKey();

		assertThat(zweiter.getEncoded()).isEqualTo(erster.getEncoded());
	}

	@Test
	@DisplayName("getKey | ergänzt fehlenden Alias und lässt vorhandene Einträge unverändert")
	void getKey_AliasFehlt_ErgaenztUndBehaeltFremdeEintraege() throws Exception {
		final KeyStore vorhanden = KeyStoreUtils.newKeystore();
		final SecretKey fremd = AES.getRandomKey256();
		KeyStoreUtils.setSecretKey(vorhanden, "anderer", fremd, PASSWORD);
		KeyStoreUtils.storeKeystore(vorhanden, getKeystoreFile().toString(), PASSWORD);

		final SecretKey key = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS).getKey();

		final KeyStore geladen = KeyStoreUtils.getKeystore(getKeystoreFile().toString(), PASSWORD);
		final SecretKey geladenerKeyStore = Objects.requireNonNull(KeyStoreUtils.getSecretKey(geladen, ALIAS, PASSWORD));
		final SecretKey existing = Objects.requireNonNull(KeyStoreUtils.getSecretKey(geladen, "anderer", PASSWORD));
		assertThat(geladenerKeyStore.getEncoded()).isEqualTo(key.getEncoded());
		assertThat(existing.getEncoded()).isEqualTo(fremd.getEncoded());
	}

	@Test
	@DisplayName("getKey | falsches Kennwort -> 500, Datei bleibt unverändert")
	void getKey_FalschesKennwort_WirftFehlerUndLaesstDateiUnveraendert() throws Exception {
		new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS).getKey();
		final byte[] vorher = Files.readAllBytes(getKeystoreFile());
		final var cut = new KeyStoreSecretKeyProvider(getKeystoreFile(), "falsch", ALIAS);

		assertThatThrownBy(cut::getKey)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage(FEHLER_LADEN)
				.extracting("status")
				.isEqualTo(Status.INTERNAL_SERVER_ERROR);
		assertThat(Files.readAllBytes(getKeystoreFile())).isEqualTo(vorher);
	}

	@Test
	@DisplayName("getKey | defekte Datei -> 500, Datei bleibt unverändert")
	void getKey_DefekteDatei_WirftFehlerUndLaesstDateiUnveraendert() throws Exception {
		final byte[] vorher = "kein keystore".getBytes(StandardCharsets.UTF_8);
		Files.write(getKeystoreFile(), vorher);
		final var cut = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS);

		assertThatThrownBy(cut::getKey)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage(FEHLER_LADEN);
		assertThat(Files.readAllBytes(getKeystoreFile())).isEqualTo(vorher);
	}

	@Test
	@DisplayName("getKey | leere Datei (0 Byte) -> 500, Datei bleibt unverändert")
	void getKey_LeereDatei_WirftFehlerUndLaesstDateiUnveraendert() throws Exception {
		Files.createFile(getKeystoreFile());
		final var cut = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS);

		assertThatThrownBy(cut::getKey)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage(FEHLER_LADEN);
		assertThat(Files.size(getKeystoreFile())).isZero();
	}

	@Test
	@DisplayName("getKey | nicht existierendes Zielverzeichnis -> 500, Verzeichnis wird nicht angelegt")
	void getKey_VerzeichnisFehlt_WirftFehler() {
		final Path dir = tempDir.resolve("gibt-es-nicht");
		final var cut = new KeyStoreSecretKeyProvider(dir.resolve("secrets.keystore"), PASSWORD, ALIAS);

		assertThatThrownBy(cut::getKey)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage(FEHLER_SPEICHERN)
				.extracting("status")
				.isEqualTo(Status.INTERNAL_SERVER_ERROR);
		assertThat(dir).doesNotExist();
	}

	@Test
	@DisplayName("getKey | parallele Erst-Initialisierung liefert allen Threads denselben Schlüssel")
	void getKey_Parallel_LiefertDenselbenSchluessel() throws Exception {
		final var cut = new KeyStoreSecretKeyProvider(getKeystoreFile(), PASSWORD, ALIAS);
		final int anzahl = 8;
		final CountDownLatch start = new CountDownLatch(1);
		final ExecutorService executor = Executors.newFixedThreadPool(anzahl);
		try {
			final List<Future<SecretKey>> futures = new ArrayList<>();
			for (int i = 0; i < anzahl; i++) {
				futures.add(executor.submit(() -> {
					start.await();
					return cut.getKey();
				}));
			}
			start.countDown();
			final byte[] erwartet = futures.getFirst().get(30, TimeUnit.SECONDS).getEncoded();
			for (final Future<SecretKey> f : futures) {
				assertThat(f.get(30, TimeUnit.SECONDS).getEncoded()).isEqualTo(erwartet);
			}
		} finally {
			executor.shutdownNow();
		}
		try (Stream<Path> files = Files.list(tempDir)) {
			assertThat(files.map(p -> p.getFileName().toString()).toList()).isEqualTo(List.of("secrets.keystore"));
		}
	}

	private Path getKeystoreFile() {
		return tempDir.resolve("secrets.keystore");
	}
}
