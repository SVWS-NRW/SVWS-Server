package de.svws_nrw.service.benutzer;

import java.util.Set;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenutzerKompetenzServiceTest {

	@Mock
	private BenutzerAllgemeinRepository benutzerRepository;

	@Mock
	private Benutzer benutzer;

	private BenutzerKompetenzService service;

	@BeforeEach
	void setUp() {
		service = new BenutzerKompetenzService(benutzerRepository);
		when(benutzerRepository.getAktuellerBenutzer()).thenReturn(benutzer);
	}

	// -------------------------------------------------------------------------
	// pruefeKompetenzLeistungsdatenAendernKlassen
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("pruefeKompetenzLeistungsdatenAendernKlassen")
	class PruefeKompetenzLeistungsdatenAendernKlassen {

		@Test
		@DisplayName("Übergreifende Kompetenz - keine Prüfung der Klassen")
		void alleAendern() {
			when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)).thenReturn(true);

			assertThatCode(() -> service.pruefeKompetenzLeistungsdatenAendernKlassen(46L, 59L)).doesNotThrowAnyException();
			verify(benutzer, never()).getKlassenIDs();
		}

		@Test
		@DisplayName("Nur funktionsbezogene Kompetenz - eigene Klasse ist erlaubt")
		void funktionsbezogenEigeneKlasse() {
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatCode(() -> service.pruefeKompetenzLeistungsdatenAendernKlassen(46L)).doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Nur funktionsbezogene Kompetenz - fremde Klasse - FORBIDDEN")
		void funktionsbezogenFremdeKlasse() {
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatThrownBy(() -> service.pruefeKompetenzLeistungsdatenAendernKlassen(46L, 59L))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN)
					.hasMessage("Der Benutzer hat keine funktionsbezogene Kompetenz für den Zugriff auf die Daten der Klasse mit der ID 59.");
		}

		@Test
		@DisplayName("Nur funktionsbezogene Kompetenz - keine Klasse - FORBIDDEN")
		void funktionsbezogenKeineKlasse() {
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatThrownBy(() -> service.pruefeKompetenzLeistungsdatenAendernKlassen((Long) null))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN)
					.hasMessage("Der Benutzer kann keine funktionsbezogene Kompetenz nutzen, um auf Daten zuzugreifen, die keiner Klasse zugeordnet sind.");
		}

		private void stubNurFunktionsbezogen(final Set<Long> klassenIDs) {
			when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)).thenReturn(false);
			when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN)).thenReturn(true);
			when(benutzer.getKlassenIDs()).thenReturn(klassenIDs);
		}
	}

	// -------------------------------------------------------------------------
	// pruefeKompetenzLeistungsdatenAendern
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("pruefeKompetenzLeistungsdatenAendern")
	class PruefeKompetenzLeistungsdatenAendern {

		private static final long ID_FACHLEHRER = 12L;

		@Test
		@DisplayName("Fachlehrer-Regel anwendbar und Benutzer ist der Fachlehrer - keine Prüfung der Klassen")
		void fachlehrerRegel() {
			when(benutzer.getIdLehrer()).thenReturn(ID_FACHLEHRER);

			assertThatCode(() -> service.checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, true, 59L)).doesNotThrowAnyException();
			verify(benutzer, never()).getKlassenIDs();
		}

		@Test
		@DisplayName("Fachlehrer-Regel anwendbar, aber Benutzer ist ein anderer Lehrer - Prüfung der Klassen - FORBIDDEN")
		void anderesLehrerKeineKlasse() {
			when(benutzer.getIdLehrer()).thenReturn(99L);
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatThrownBy(() -> service.checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, true, 59L))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("Fachlehrer-Regel anwendbar, aber Benutzer ist kein Lehrer - Prüfung der Klassen - FORBIDDEN")
		void keinLehrer() {
			when(benutzer.getIdLehrer()).thenReturn(null);
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatThrownBy(() -> service.checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, true, 59L))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("Fachlehrer-Regel anwendbar, aber kein Fachlehrer eingetragen - Prüfung der Klassen - FORBIDDEN")
		void keinFachlehrerEingetragen() {
			when(benutzer.getIdLehrer()).thenReturn(ID_FACHLEHRER);
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatThrownBy(() -> service.checkKompetenzLeistungsdatenAendern(null, true, 59L))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
		}

		@Test
		@DisplayName("Fachlehrer-Regel nicht anwendbar - Prüfung der Klassen - FORBIDDEN bei fremder Klasse")
		void regelNichtAnwendbar() {
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatThrownBy(() -> service.checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, false, 59L))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
			verify(benutzer, never()).getIdLehrer();
		}

		@Test
		@DisplayName("Fachlehrer-Regel nicht anwendbar - Prüfung der Klassen - eigene Klasse ist erlaubt")
		void regelNichtAnwendbarEigeneKlasse() {
			stubNurFunktionsbezogen(Set.of(46L));

			assertThatCode(() -> service.checkKompetenzLeistungsdatenAendern(ID_FACHLEHRER, false, 46L)).doesNotThrowAnyException();
		}

		private void stubNurFunktionsbezogen(final Set<Long> klassenIDs) {
			when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)).thenReturn(false);
			when(benutzer.hatVerwendeteKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN)).thenReturn(true);
			when(benutzer.getKlassenIDs()).thenReturn(klassenIDs);
		}
	}
}
