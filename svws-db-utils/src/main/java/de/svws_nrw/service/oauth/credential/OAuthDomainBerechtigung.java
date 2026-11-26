package de.svws_nrw.service.oauth.credential;

import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.oauth.internal.OAuthDomain;
import jakarta.ws.rs.core.Response;

/**
 * Die Berechtigung eines Benutzers auf die OAuth-Credentials einzelner Domänen.
 *
 * <p>Je {@link OAuthDomain} sind die Kompetenzen hinterlegt, von denen eine für den Zugriff auf deren Credentials
 * genügt. Administratoren haben immer Zugriff. Ist für eine Domäne keine Kompetenz hinterlegt, ist der Zugriff
 * Administratoren vorbehalten.</p>
 */
public final class OAuthDomainBerechtigung {

	private static final String KEINE_BERECHTIGUNG = "Keine Berechtigung für die OAuth-Credentials der Domäne %s.";

	private static final Map<OAuthDomain, Set<BenutzerKompetenz>> DOMAIN_KOMPETENZEN = Map.of(
			OAuthDomain.IT_NRW, Set.of(BenutzerKompetenz.IT_NRW_ADMINISTRIEREN),
			OAuthDomain.SCHULBEWERBUNG, Set.of(),
			OAuthDomain.WENOM, Set.of());

	private final Benutzer benutzer;

	/**
	 * Erstellt die Berechtigung für den übergebenen Benutzer.
	 *
	 * @param benutzer   der angemeldete {@link Benutzer}
	 */
	public OAuthDomainBerechtigung(final Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	/**
	 * Liefert alle Kompetenzen, die für mindestens eine {@link OAuthDomain} zum Zugriff berechtigen.
	 *
	 * @return die Kompetenzen über alle Domänen hinweg
	 */
	public static BenutzerKompetenz[] alleKompetenzen() {
		return DOMAIN_KOMPETENZEN.values().stream()
				.flatMap(Set::stream)
				.toArray(BenutzerKompetenz[]::new);
	}

	/**
	 * Gibt zurück, ob der Benutzer auf die Credentials der Domäne zugreifen darf.
	 *
	 * @param domain   die {@link OAuthDomain}
	 *
	 * @return {@code true}, falls der Zugriff erlaubt ist
	 */
	public boolean isErlaubt(final OAuthDomain domain) {
		return benutzer.istAdmin() || hatKompetenzFuerDomain(domain);
	}

	/**
	 * Stellt sicher, dass der Benutzer auf die Credentials der Domäne zugreifen darf.
	 *
	 * @param domain   die {@link OAuthDomain}
	 *
	 * @throws ApiOperationException (403) falls der Benutzer keine Berechtigung für die Domäne besitzt
	 */
	public void checkDomain(final OAuthDomain domain) {
		if (!isErlaubt(domain)) {
			throw new ApiOperationException(Response.Status.FORBIDDEN, KEINE_BERECHTIGUNG.formatted(domain));
		}
	}

	private boolean hatKompetenzFuerDomain(final OAuthDomain domain) {
		final var kompetenzen = DOMAIN_KOMPETENZEN.getOrDefault(domain, Set.of());

		return !kompetenzen.isEmpty() && benutzer.pruefeKompetenz(kompetenzen);
	}
}
