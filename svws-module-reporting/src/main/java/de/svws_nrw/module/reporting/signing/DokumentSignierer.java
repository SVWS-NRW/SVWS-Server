package de.svws_nrw.module.reporting.signing;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Signiert die XSchule-Dokumente eines Reportlaufs in einem einzigen Batch. Reporting baut das XML und rendert die QR-Codes;
 * diese Klasse liefert die zugehörigen CMS-/P7S-Signaturen. Die Methode wird je Reportlauf genau einmal aufgerufen.
 * <p>
 * Phase 1: Statt einen echten Signierdienst (IT.NRW) aufzurufen, liefert diese Klasse für jede Anfrage dieselbe fest
 * hinterlegte Dummy-Signatur ({@link #DUMMY_CMS_BASE64}) – der Inhalt der Anfrage wird dabei ignoriert. In Phase 2 wird hier
 * die echte Anbindung (OAuth, HTTP, Hashbildung, Schul-Kontext) implementiert bzw. an dieser Naht eine andere Implementierung
 * eingezogen.
 */
public final class DokumentSignierer {

	/** Fest hinterlegte Dummy-Signatur (CMS/P7S, base64). Wird in Phase 2 durch den echten Signaturdienst bei IT.NRW ersetzt. */
	private static final String DUMMY_CMS_BASE64 = "MIIEOAYJKoZIhvcNAQcCoIIEKTCCBCUCAQExDTALBglghkgBZQMEAgEwCwYJKoZIhvcNAQcBoIICeTCCAnUwggIcoAMCAQIC"
			+ "CECyKKsXed2+MAoGCCqGSM49BAMCMIGgMQswCQYDVQQGEwJERTEMMAoGA1UECAwDTlJXMRQwEgYDVQQHDAtEw7xzc2VsZG9yZjEzMDEGA1UECgwqVEVTVCBNU0IgTlJXIENBIGbDvHIgRG"
			+ "lnaXRhbGUgU2NodWxzdGVtcGVsMTgwNgYDVQQDDC9URVNUIE1TQiBOUlcgUm9vdCBDQSBmw7xyIERpZ2l0YWxlIFNjaHVsc3RlbXBlbDAeFw0yNjA0MjAxMTQyMDFaFw0yNzA0MjAxMTQy"
			+ "MDFaMIGMMQswCQYDVQQGEwJERTEMMAoGA1UECAwDTlJXMQ8wDQYDVQQKDAYxMDAwMTIxSTBHBgNVBAMMQFN0w6RkdC4gR2VtZWluc2NoYWZ0c2dydW5kc2NodWxlIEFtIEZyaWVkZW5zcG"
			+ "FyayAtIFByaW1hcnN0dWZlIC0xEzARBgNVBAcMCkxldmVya3VzZW4wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAR70r6Fn+Jwq09YoxW65ho3IATPkHA3lTUO9CTS5V2YaHtQ3W+fE0ac"
			+ "j/g3GF6YgOe0r0pl8GwF7N43h6kQ2M6To1IwUDAOBgNVHQ8BAf8EBAMCBsAwHQYDVR0OBBYEFHvum70Huqph2woHYWLmIiO2hvo1MB8GA1UdIwQYMBaAFC6Tg2ChQoyP1ucThIwPpAbbGu"
			+ "m5MAoGCCqGSM49BAMCA0cAMEQCICLYIQs8q/8E0eRqL5+HAVdxjjaEvWYQJDCevosZTcFLAiARYxjRj3jZ7xOLX2SyxSYo4W/5DgZH82kx0+9bZ6WiTDGCAYUwggGBAgEBMIGtMIGgMQsw"
			+ "CQYDVQQGEwJERTEMMAoGA1UECAwDTlJXMRQwEgYDVQQHDAtEw7xzc2VsZG9yZjEzMDEGA1UECgwqVEVTVCBNU0IgTlJXIENBIGbDvHIgRGlnaXRhbGUgU2NodWxzdGVtcGVsMTgwNgYDVQ"
			+ "QDDC9URVNUIE1TQiBOUlcgUm9vdCBDQSBmw7xyIERpZ2l0YWxlIFNjaHVsc3RlbXBlbAIIQLIoqxd53b4wCwYJYIZIAWUDBAIBoGkwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkq"
			+ "hkiG9w0BCQUxDxcNMjYwNTIzMjA0MTU4WjAvBgkqhkiG9w0BCQQxIgQgcKwrNrwMV8ssGOiAqF7adNmAdvT16iMsyYQYjQlqE6gwCgYIKoZIzj0EAwIESDBGAiEA3UxYCqYAH07vhGFJxK"
			+ "TyZfCVcpcpPpvDVylu2s+BpVECIQCQtZgp/Oc39pkKncLgLXGMp9T54hHNYyz0bkYf836Sog==";

	/** Die einmal dekodierten rohen CMS-Bytes der Dummy-Signatur. */
	private static final byte[] DUMMY_CMS = Base64.getDecoder().decode(DUMMY_CMS_BASE64);

	/**
	 * Signiert alle übergebenen Dokumente in einem einzigen Batch-Aufruf.
	 *
	 * @param anfragen Liste der Signieranfragen (je Schüler eine).
	 *
	 * @return Map von der {@link SignierAnfrage#correlationId()} auf das zugehörige {@link SignierErgebnis}.
	 */
	public Map<UUID, SignierErgebnis> signiereBatch(final List<SignierAnfrage> anfragen) {
		final Map<UUID, SignierErgebnis> ergebnisse = new HashMap<>();
		for (final SignierAnfrage anfrage : anfragen) {
			ergebnisse.put(anfrage.correlationId(), new SignierErgebnis(anfrage.correlationId(), DUMMY_CMS, null));
		}
		return ergebnisse;
	}

}
