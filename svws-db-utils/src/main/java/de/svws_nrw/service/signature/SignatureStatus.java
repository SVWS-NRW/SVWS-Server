package de.svws_nrw.service.signature;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Der Status eines Signiervorgangs. Signiervorgänge können mit Hilfe des {@link SignatureService} durchgeführt werden.
 */
public enum SignatureStatus {
	/** OK */
	OK("OK"),
	/** ERROR */
	ERROR("ERROR"),
	/** UNKNOWN */
	UNKNOWN("UNKNOWN");


	private static final Map<String, SignatureStatus> LOOKUP = Arrays.stream(values()).collect(Collectors.toMap(s -> s.text, s -> s));
	private final String text;

	SignatureStatus(final String text) {
		this.text = text;
	}

	/**
	 * Liefert den Status zu einem gegebenen Text.
	 *
	 * @param text Text des Status
	 * @return Status
	 */
	public static Optional<SignatureStatus> getByText(final String text) {
		return Optional.ofNullable(LOOKUP.get(text));
	}
}
