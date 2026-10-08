package de.svws_nrw.service.crypto.secret;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SecretKeyProviderFactory")
class SecretKeyProviderFactoryTest {

	@Test
	@DisplayName("getInstance | liefert immer dieselbe Instanz, ohne auf Konfiguration oder Dateien zuzugreifen")
	void getInstance_Singleton() {
		final SecretKeyProvider erster = SecretKeyProviderFactory.getInstance();

		assertThat(erster).isNotNull().isSameAs(SecretKeyProviderFactory.getInstance());
	}

	@Test
	@DisplayName("resolveKeystoreFile | hängt den Dateinamen an ein absolutes Verzeichnis an")
	void resolveKeystoreFile_AbsoluterPfad() {
		final Path dir = Path.of("opt", "svws", "conf").toAbsolutePath();

		assertThat(SecretKeyProviderFactory.resolveKeystoreFile(dir.toString())).isEqualTo(dir.resolve("secrets.keystore"));
	}

	@Test
	@DisplayName("resolveKeystoreFile | abschließender Separator erzeugt keinen doppelten Separator")
	void resolveKeystoreFile_AbschliessenderSeparator() {
		final Path dir = Path.of("opt", "svws", "conf").toAbsolutePath();

		assertThat(SecretKeyProviderFactory.resolveKeystoreFile(dir + java.io.File.separator)).isEqualTo(dir.resolve("secrets.keystore"));
	}

	@Test
	@DisplayName("resolveKeystoreFile | \".\" ergibt eine Datei im Arbeitsverzeichnis")
	void resolveKeystoreFile_Punkt() {
		assertThat(SecretKeyProviderFactory.resolveKeystoreFile(".").normalize()).isEqualTo(Path.of("secrets.keystore"));
	}

	@Test
	@DisplayName("resolveKeystoreFile | leerer Pfad ergibt eine Datei im Arbeitsverzeichnis, nicht im Wurzelverzeichnis")
	void resolveKeystoreFile_Leer() {
		final Path ergebnis = SecretKeyProviderFactory.resolveKeystoreFile("");

		assertThat(ergebnis).isEqualTo(Path.of("secrets.keystore"))
				.isRelative();
	}
}
