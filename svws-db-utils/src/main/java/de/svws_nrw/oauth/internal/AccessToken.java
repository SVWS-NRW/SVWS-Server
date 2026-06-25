package de.svws_nrw.oauth.internal;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * Repraesentiert einen OAuth-Access-Token mit Typ und Ablaufzeitpunkt.
 *
 * @param value     der Token-Wert
 * @param tokenType Token-Typ gemaess RFC 6749, z.B. {@code Bearer}; wird auf {@code Bearer} defaultet falls leer
 * @param expiresAt Zeitpunkt ab dem der Token als abgelaufen gilt
 */
public record AccessToken(String value, String tokenType, Instant expiresAt) {

	/**
	 * Kompakter Konstruktor.
	 * Stellt sicher, dass {@code value} und {@code expiresAt} nicht {@code null} sind
	 * und setzt {@code tokenType} auf {@code Bearer} falls nicht angegeben.
	 */
	public AccessToken {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(expiresAt, "expiresAt");
		if (StringUtils.isBlank(tokenType)) {
			tokenType = "Bearer";
		}
	}

	/**
	 * Erzeugt einen {@link AccessToken} aus einem relativen {@code expires_in}-Wert.
	 *
	 * @param value           der Token-Wert
	 * @param tokenType       Token-Typ, z.B. {@code Bearer}
	 * @param expiresInSeconds Gueltigkeitsdauer in Sekunden ab {@code now}
	 * @param now             aktueller Zeitpunkt
	 * @return neuer {@link AccessToken} mit absolutem Ablaufzeitpunkt
	 */
	public static AccessToken of(final String value, final String tokenType, final long expiresInSeconds, final Instant now) {
		return new AccessToken(value, tokenType, now.plusSeconds(expiresInSeconds));
	}

	/**
	 * Prueft ob der Token zum Zeitpunkt der angegebenen {@link Clock} noch gueltig ist.
	 *
	 * @param clock Zeitquelle; ermoeglicht deterministisches Testen ohne Systemzeit
	 * @return {@code true} wenn der aktuelle Zeitpunkt der Clock vor {@code expiresAt} liegt
	 */
	public boolean isValidAt(final Clock clock) {
		return clock.instant().isBefore(expiresAt);
	}

	/**
	 * Gibt den formatierten Wert fuer den {@code Authorization}-Header zurueck.
	 *
	 * @return z.B. {@code Bearer eyJhbGc...}
	 */
	public String asAuthorizationHeader() {
		return String.format("%s %s", tokenType, value);
	}
}
