package de.svws_nrw.service.oauth.credential;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.oauth.internal.OAuthDomain;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OAuthDomainBerechtigung")
class OAuthDomainBerechtigungTest {

	private static OAuthDomainBerechtigung berechtigungOhneAdmin(final boolean hatItNrwKompetenz) {
		final var benutzer = mock(Benutzer.class);
		when(benutzer.istAdmin()).thenReturn(false);
		when(benutzer.pruefeKompetenz(Set.of(BenutzerKompetenz.IT_NRW_ADMINISTRIEREN))).thenReturn(hatItNrwKompetenz);

		return new OAuthDomainBerechtigung(benutzer);
	}

	private static OAuthDomainBerechtigung berechtigungAdmin() {
		final var benutzer = mock(Benutzer.class);
		when(benutzer.istAdmin()).thenReturn(true);

		return new OAuthDomainBerechtigung(benutzer);
	}

	@Test
	@DisplayName("alleKompetenzen | enthaelt die Kompetenzen aller Domaenen")
	void alleKompetenzen() {
		assertThat(OAuthDomainBerechtigung.alleKompetenzen()).containsExactly(BenutzerKompetenz.IT_NRW_ADMINISTRIEREN);
	}

	@Test
	@DisplayName("isErlaubt | Domaene mit Kompetenz nur mit dieser Kompetenz erlaubt")
	void isErlaubtMitKompetenz() {
		final var cut = berechtigungOhneAdmin(true);

		assertThat(cut.isErlaubt(OAuthDomain.IT_NRW)).isTrue();
		assertThat(cut.isErlaubt(OAuthDomain.WENOM)).isFalse();
		assertThat(cut.isErlaubt(OAuthDomain.SCHULBEWERBUNG)).isFalse();
	}

	@Test
	@DisplayName("isErlaubt | ohne Kompetenz ist keine Domaene erlaubt")
	void isErlaubtOhneKompetenz() {
		final var cut = berechtigungOhneAdmin(false);

		assertThat(OAuthDomain.values()).allSatisfy(domain -> assertThat(cut.isErlaubt(domain)).isFalse());
	}

	@Test
	@DisplayName("isErlaubt | Administratoren duerfen alle Domaenen, auch ohne hinterlegte Kompetenz")
	void isErlaubtAdmin() {
		final var cut = berechtigungAdmin();

		assertThat(OAuthDomain.values()).allSatisfy(domain -> assertThat(cut.isErlaubt(domain)).isTrue());
	}

	@Test
	@DisplayName("checkDomain | wirft nichts bei erlaubter Domaene")
	void checkDomainErlaubt() {
		final var cut = berechtigungOhneAdmin(true);

		assertThatCode(() -> cut.checkDomain(OAuthDomain.IT_NRW)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("checkDomain | FORBIDDEN bei nicht erlaubter Domaene")
	void checkDomainVerboten() {
		final var cut = berechtigungOhneAdmin(true);

		assertThatThrownBy(() -> cut.checkDomain(OAuthDomain.WENOM))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("WENOM")
				.extracting("status")
				.isEqualTo(Response.Status.FORBIDDEN);
	}
}
