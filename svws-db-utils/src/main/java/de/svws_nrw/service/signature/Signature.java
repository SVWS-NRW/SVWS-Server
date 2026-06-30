package de.svws_nrw.service.signature;

import java.util.Arrays;
import java.util.Objects;

/**
 * Enthält eine Daten-Signatur und weitere Informationen zum Status des Signiervorgangs.
 *
 * @param content der Inhalt der Signatur
 * @param status der Status des Signiervorgangs
 * @param errorMessage die Fehlermeldung, falls der Signiervorgang fehlgeschlagen ist
 */
public record Signature(byte[] content, SignatureStatus status, String errorMessage) {

	@Override
	public String toString() {
		return """
				Signature{
				   content=%s,
				   status='%s',
				   errorMessage='%s'
				}
				"""
				.formatted(Arrays.toString(content), status, errorMessage);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(status, errorMessage);
		result = (31 * result) + Arrays.hashCode(content);
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Signature(final byte[] contentOther, final SignatureStatus statusOther, final String messageOther))) {
			return false;
		}

		return Arrays.equals(content, contentOther)
				&& (status == statusOther)
				&& Objects.equals(errorMessage, messageOther);
	}
}
