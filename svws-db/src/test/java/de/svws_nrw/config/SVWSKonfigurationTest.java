package de.svws_nrw.config;

import de.svws_nrw.base.crypto.KeyStoreUtils;
import de.svws_nrw.base.crypto.RSA;
import de.svws_nrw.core.data.TLSCertificateInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;


/**
 * Diese Klasse prüft die Methoden der {@link SVWSKonfiguration}-Klasse.
 */
class SVWSKonfigurationTest {

	private MockedStatic<RSA> mockedRsa;
	private MockedStatic<KeyStoreUtils> mockedKeyStoreUtils;
	private MockedStatic<SVWSKonfiguration> mockedConfig;

	@BeforeEach
	void setUp() {
		mockedRsa = Mockito.mockStatic(RSA.class);
		mockedKeyStoreUtils = Mockito.mockStatic(KeyStoreUtils.class);
		// CALLS_REAL_METHODS sorgt dafür, dass der Rumpf von createPrivateKeyCertificate tatsächlich ausgeführt wird
		mockedConfig = Mockito.mockStatic(SVWSKonfiguration.class, Mockito.CALLS_REAL_METHODS);
	}

	@AfterEach
	void tearDown() {
		mockedRsa.close();
		mockedKeyStoreUtils.close();
		mockedConfig.close();
	}

	@Test
	@DisplayName("createPrivateKeyCertificate durchläuft die gesamte Kette statischer Aufrufe korrekt")
	void createPrivateKeyCertificate_Erfolgreich() throws Exception {
		final String alias = "test-alias";
		final String file = "path/to/keystore";
		final String password = "secretPassword";
		final TLSCertificateInfo info = new TLSCertificateInfo();
		info.dn = "CN=TestServer";
		info.sans = List.of("svws.nrw");

		// Vorbereitung der isolierten Mock-Strukturen für den Datenfluss
		final KeyPair mockKeyPair = mock(KeyPair.class);
		final PrivateKey mockPrivateKey = mock(PrivateKey.class);
		final X509Certificate mockCert = mock(X509Certificate.class);
		final KeyStore mockKeyStore = mock(KeyStore.class);
		final SVWSKonfiguration mockConfigInstance = mock(SVWSKonfiguration.class);

		Mockito.when(mockKeyPair.getPrivate()).thenReturn(mockPrivateKey);

		// Externe krypto-statische Abhängigkeiten stubben
		mockedRsa.when(RSA::createKey).thenReturn(mockKeyPair);
		mockedRsa.when(() -> RSA.createSelfSignedCert(any(), anyString(), any())).thenReturn(mockCert);

		// Instanz-Verhalten des Konfigurationsobjekts festlegen
		Mockito.when(mockConfigInstance.getTLSKeystoreFile()).thenReturn("path/to/keystore");
		Mockito.when(mockConfigInstance.getTLSKeystorePassword()).thenReturn("secretPassword");

		// Interne statische Helfer gezielt überschreiben, um Dateisystem-I/O und NullPointer zu verhindern
		mockedConfig.when(SVWSKonfiguration::get).thenReturn(mockConfigInstance);
		mockedConfig.when(SVWSKonfiguration::getKeystore).thenReturn(mockKeyStore);
		mockedConfig.when(SVWSKonfiguration::write).thenAnswer(invocation -> null);

		// Aufruf der echten Methode über die partitionierte Mock-Umgebung
		SVWSKonfiguration.createPrivateKeyCertificate(alias, info);

		// Unkonkrete, flexible Verifikation des Methodenaufrufs an das Krypto-Subsystem
		mockedKeyStoreUtils.verify(() -> KeyStoreUtils.addPrivateKeyCertificate(eq(mockKeyStore), eq(file), eq(password),
				eq(alias), eq(mockPrivateKey), eq(mockCert)));

		// Verifikation der Folgeinteraktionen auf Instanz- und Klassenebene
		Mockito.verify(mockConfigInstance).setTLSKeyAlias(anyString());
		mockedConfig.verify(SVWSKonfiguration::write);
	}

	@Test
	@DisplayName("createPrivateKeyCertificate fängt Exceptions ab und kapselt sie in eine SVWSKonfigurationException")
	void createPrivateKeyCertificate_Fehlerhaft_WirftSVWSKonfigurationException() {
		final TLSCertificateInfo info = new TLSCertificateInfo();
		mockedRsa.when(RSA::createKey).thenThrow(new RuntimeException("Krypto-Hardwarefehler"));

		assertThatThrownBy(() -> SVWSKonfiguration.createPrivateKeyCertificate("fail-alias", info))
				.isInstanceOf(SVWSKonfigurationException.class)
				.hasMessageContaining("Der private Schlüssel und das Zertifikat konnten nicht");
	}

}
