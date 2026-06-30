package de.svws_nrw.service.signature;

import java.util.Map;

public interface SignatureService {

	/**
	 * Es werden Signaturen zu den übergebenen Daten ausgestellt und als Map zurückgegeben.
	 *
	 * @param payloadById Zurodnung zwischen Signatur-Id und Daten, die signiert werden sollen.
	 *
	 * @return Zurodnung zwischen Signatur-Id und Signatur
	 */
	Map<Object, Signature> sign(Map<Object, byte[]> payloadById);
}
