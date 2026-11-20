package de.svws_nrw.service.schueler.lernabschnitt;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerLernabschnittEpJahreTest {

	private static final long ID_SCHUELER = 10L;
	private static final long ID_SCHULJAHRESABSCHNITT = 20L;
	private static final Supplier<String> JAHRGANG_NICHT_ABGEFRAGT = () -> fail("Der Jahrgang darf nicht ermittelt werden.");

	@Mock
	private SchuelerLernabschnittRepository repository;

	private SchuelerLernabschnittEpJahre epJahre;

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		epJahre = new SchuelerLernabschnittEpJahre(repository);
	}

	private static DTOSchuelerLernabschnittsdaten createLernabschnitt(final long id, final String asdJahrgang, final Integer epJahre) {
		final var lernabschnitt = new DTOSchuelerLernabschnittsdaten(id, ID_SCHUELER, ID_SCHULJAHRESABSCHNITT, true, null, 1L);
		lernabschnitt.ASDJahrgang = asdJahrgang;
		lernabschnitt.EPJahre = epJahre;
		return lernabschnitt;
	}

	@Nested
	@DisplayName("validateAenderbar")
	class ValidateAenderbar {

		@Test
		@DisplayName("Unveränderter Wert - keine Prüfung, auch nicht an Schulen ohne Primarbereich")
		void validateAenderbar_unveraendert() {
			assertThatCode(() -> epJahre.validateAenderbar(2L, 2, Schulform.GY, JAHRGANG_NICHT_ABGEFRAGT))
					.doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Unveränderter Wert null - keine Prüfung")
		void validateAenderbar_unveraendertNull() {
			assertThatCode(() -> epJahre.validateAenderbar(null, null, Schulform.GY, JAHRGANG_NICHT_ABGEFRAGT))
					.doesNotThrowAnyException();
		}

		@Test
		@DisplayName("Schule ohne Primarbereich - BAD_REQUEST, ohne den Jahrgang zu ermitteln")
		void validateAenderbar_ohnePrimarbereich() {
			assertThatThrownBy(() -> epJahre.validateAenderbar(2L, null, Schulform.GY, JAHRGANG_NICHT_ABGEFRAGT))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Die Besuchsjahre in der Schuleingangsphase können nur an Schulen mit Primarbereich geändert werden.");
		}

		@ParameterizedTest
		@ValueSource(strings = { "01", "02" })
		@DisplayName("Jahrgang der Eingangsphase - Änderung erlaubt")
		void validateAenderbar_eingangsphase(final String asdJahrgang) {
			assertThatCode(() -> epJahre.validateAenderbar(3L, 2, Schulform.G, () -> asdJahrgang))
					.doesNotThrowAnyException();
		}

		@ParameterizedTest
		@ValueSource(strings = { "E3", "03", "04" })
		@DisplayName("Jahrgang außerhalb der Eingangsphase - BAD_REQUEST")
		void validateAenderbar_andererJahrgang(final String asdJahrgang) {
			assertThatThrownBy(() -> epJahre.validateAenderbar(3L, 2, Schulform.G, () -> asdJahrgang))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
					.hasMessage("Die Besuchsjahre in der Schuleingangsphase können nur in den Jahrgängen 1 und 2 geändert werden.");
		}

		@Test
		@DisplayName("Kein Jahrgang - BAD_REQUEST")
		void validateAenderbar_ohneJahrgang() {
			assertThatThrownBy(() -> epJahre.validateAenderbar(3L, null, Schulform.G, () -> null))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

	}

	@Nested
	@DisplayName("propagate")
	class Propagate {

		@Test
		@DisplayName("Schule ohne Primarbereich - keine Fortschreibung")
		void propagate_ohnePrimarbereich() {
			final var geaendert = createLernabschnitt(1L, "01", 2);

			epJahre.propagate(geaendert, Schulform.GY, true);

			verifyNoInteractions(repository);
		}

		@Test
		@DisplayName("Request ohne EP-Jahre und Jahrgang - keine Fortschreibung")
		void propagate_ohneLaufbahnaenderung() {
			final var geaendert = createLernabschnitt(1L, "01", 2);

			epJahre.propagate(geaendert, Schulform.G, false);

			verifyNoInteractions(repository);
		}

		@Test
		@DisplayName("Maximum der EP-Jahre aus Jahrgang 1 und 2 wird auf Jahrgang 3 und 4 fortgeschrieben")
		void propagate_maximumAufFolgeabschnitte() {
			final var jahrgang1 = createLernabschnitt(1L, "01", 1);
			final var jahrgang2 = createLernabschnitt(2L, "02", 3);
			final var jahrgang3 = createLernabschnitt(3L, "03", null);
			final var jahrgang4 = createLernabschnitt(4L, "04", 1);
			final var andererJahrgang = createLernabschnitt(5L, "E3", null);
			when(repository.findListBySchuelerIDs(anyCollection())).thenReturn(List.of(jahrgang1, jahrgang2, jahrgang3, jahrgang4, andererJahrgang));

			epJahre.propagate(jahrgang1, Schulform.G, true);

			assertThat(List.of(jahrgang3, jahrgang4)).allSatisfy(lernabschnitt -> assertThat(lernabschnitt.EPJahre).isEqualTo(3));
			assertThat(andererJahrgang.EPJahre).isNull();
		}

		@Test
		@DisplayName("Der geänderte Lernabschnitt zählt mit seinem aktuellen Stand, nicht mit dem Stand aus der Datenbank")
		void propagate_geaenderterStandZaehlt() {
			final var gespeichert = createLernabschnitt(1L, "02", 1);
			final var jahrgang3 = createLernabschnitt(3L, "03", 1);
			when(repository.findListBySchuelerIDs(anyCollection())).thenReturn(List.of(gespeichert, jahrgang3));
			final var geaendert = createLernabschnitt(1L, "02", 3);

			epJahre.propagate(geaendert, Schulform.G, true);

			assertThat(jahrgang3.EPJahre).isEqualTo(3);
		}

		@Test
		@DisplayName("Keine EP-Jahre in der Eingangsphase - Folgeabschnitte bleiben unverändert")
		void propagate_ohneEpJahreInEingangsphase() {
			final var jahrgang1 = createLernabschnitt(1L, "01", null);
			final var jahrgang3 = createLernabschnitt(3L, "03", 2);
			when(repository.findListBySchuelerIDs(anyCollection())).thenReturn(List.of(jahrgang1, jahrgang3));

			epJahre.propagate(jahrgang1, Schulform.G, true);

			assertThat(jahrgang3.EPJahre).isEqualTo(2);
		}

	}

}
