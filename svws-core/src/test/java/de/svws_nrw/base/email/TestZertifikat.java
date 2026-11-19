package de.svws_nrw.base.email;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HexFormat;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * Ein selbst signiertes Zertifikat für den {@link SmtpTestServer}. Es wird zur Laufzeit erzeugt, damit kein Schlüssel im
 * Repository liegt. Ohne Angabe eines Namens gilt es für {@code localhost} und {@code 127.0.0.1}.
 */
final class TestZertifikat {

	private static final char[] KENNWORT = "test".toCharArray();

	private final X509Certificate zertifikat;
	private final SSLContext serverKontext;

	/**
	 * Erzeugt ein Zertifikat für {@code localhost} und {@code 127.0.0.1}.
	 *
	 * @throws Exception   falls die Erzeugung fehlschlägt
	 */
	TestZertifikat() throws Exception {
		this(new GeneralName(GeneralName.dNSName, "localhost"),
				new GeneralName(GeneralName.iPAddress, InetAddress.getLoopbackAddress().getHostAddress()));
	}

	/**
	 * Erzeugt ein Zertifikat, das nur für den angegebenen Hostnamen gilt.
	 *
	 * @param hostname   der Hostname im Zertifikat
	 *
	 * @throws Exception   falls die Erzeugung fehlschlägt
	 */
	TestZertifikat(final String hostname) throws Exception {
		this(new GeneralName(GeneralName.dNSName, hostname));
	}

	private TestZertifikat(final GeneralName... namen) throws Exception {
		final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		final KeyPair schluessel = generator.generateKeyPair();
		final X500Name name = new X500Name("CN=SmtpTestServer");
		final Instant jetzt = Instant.now();
		final JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(name, BigInteger.valueOf(jetzt.toEpochMilli()),
				Date.from(jetzt.minus(Duration.ofDays(1))), Date.from(jetzt.plus(Duration.ofDays(1))), name, schluessel.getPublic());
		builder.addExtension(Extension.subjectAlternativeName, false, new GeneralNames(namen));
		zertifikat = new JcaX509CertificateConverter()
				.getCertificate(builder.build(new JcaContentSignerBuilder("SHA256withRSA").build(schluessel.getPrivate())));

		final KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(null, null);
		keyStore.setKeyEntry("server", schluessel.getPrivate(), KENNWORT, new Certificate[] { zertifikat });
		final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, KENNWORT);
		serverKontext = SSLContext.getInstance("TLS");
		serverKontext.init(kmf.getKeyManagers(), null, null);
	}

	/**
	 * Gibt den SSL-Kontext zurück, mit dem der Testserver dieses Zertifikat vorlegt.
	 *
	 * @return der SSL-Kontext für die Serverseite
	 */
	SSLContext getServerKontext() {
		return serverKontext;
	}

	/**
	 * Gibt den SHA-256-Fingerabdruck des Zertifikats zurück, wie ihn openssl anzeigt.
	 *
	 * @return der Fingerabdruck in Großbuchstaben, getrennt durch Doppelpunkte
	 *
	 * @throws Exception   falls der Fingerabdruck nicht berechnet werden kann
	 */
	String getFingerabdruck() throws Exception {
		return HexFormat.ofDelimiter(":").withUpperCase().formatHex(MessageDigest.getInstance("SHA-256").digest(zertifikat.getEncoded()));
	}

	/**
	 * Gibt einen Truststore zurück, der genau diesem Zertifikat vertraut.
	 *
	 * @return der Truststore
	 *
	 * @throws Exception   falls der Truststore nicht erzeugt werden kann
	 */
	KeyStore getTruststore() throws Exception {
		final KeyStore truststore = KeyStore.getInstance("PKCS12");
		truststore.load(null, null);
		truststore.setCertificateEntry("server", zertifikat);
		return truststore;
	}

}
