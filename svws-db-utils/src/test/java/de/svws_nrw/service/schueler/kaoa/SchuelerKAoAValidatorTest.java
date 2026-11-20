package de.svws_nrw.service.schueler.kaoa;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für den {@link SchuelerKAoAValidator}.
 */
class SchuelerKAoAValidatorTest {

	private static final int SCHULJAHR = 2024;
	private static final long ID_JAHRGANG = 8000000L;
	private static final long ID_JAHRGANG_ANSCHLUSSOPTION = 10000000L;
	private static final String BEMERKUNG = "schule ist toll!";

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	private static SchuelerKAoADaten daten(final long idJahrgang, final long idKategorie, final long idMerkmal, final long idZusatzmerkmal,
			final Consumer<SchuelerKAoADaten> optional) {
		final var daten = new SchuelerKAoADaten();
		daten.idSchuljahresabschnitt = 7L;
		daten.idJahrgang = idJahrgang;
		daten.idKategorie = idKategorie;
		daten.idMerkmal = idMerkmal;
		daten.idZusatzmerkmal = idZusatzmerkmal;
		optional.accept(daten);
		return daten;
	}

	private static SchuelerKAoADaten bemerkung(final String bemerkung) {
		return daten(ID_JAHRGANG, 10L, 51L, 68L, d -> d.bemerkung = bemerkung);
	}

	private static SchuelerKAoADaten berufsfeld(final Long idBerufsfeld, final String bemerkung) {
		return daten(ID_JAHRGANG, 9L, 47L, 44L, d -> {
			d.idBerufsfeld = idBerufsfeld;
			d.bemerkung = bemerkung;
		});
	}

	private static SchuelerKAoADaten anschlussoption(final Long idAnschlussoption, final String bemerkung) {
		return daten(ID_JAHRGANG_ANSCHLUSSOPTION, 14L, 69L, 117L, d -> {
			d.idAnschlussoption = idAnschlussoption;
			d.bemerkung = bemerkung;
		});
	}

	private static SchuelerKAoADaten ebene4(final Long idEbene4, final String bemerkung) {
		return daten(ID_JAHRGANG, 10L, 55L, 80L, d -> {
			d.idEbene4 = idEbene4;
			d.bemerkung = bemerkung;
		});
	}

	private static Stream<Arguments> provideGueltigeDaten() {
		return Stream.of(
				arguments("Bemerkung", bemerkung(BEMERKUNG)),
				arguments("Anschlussoption", anschlussoption(24L, null)),
				arguments("Berufsfeld", berufsfeld(2L, null)),
				arguments("Ebene 4", ebene4(1L, null))
		);
	}

	private static Stream<Arguments> provideFehlerfaelle() {
		return Stream.of(
				arguments("Jahrgang unbekannt", daten(1L, 10L, 51L, 68L, d -> d.bemerkung = BEMERKUNG), Status.NOT_FOUND,
						"Kein Jahrgang mit der ID 1 vorhanden."),
				arguments("Jahrgang passt nicht zur Kategorie", daten(ID_JAHRGANG, 14L, 51L, 68L, d -> d.bemerkung = BEMERKUNG), Status.BAD_REQUEST,
						"Der Jahrgang mit der ID 8000000 ist für die Kategorie"),
				arguments("Kategorie unbekannt", daten(ID_JAHRGANG, 1000000L, 51L, 68L, d -> d.bemerkung = BEMERKUNG), Status.NOT_FOUND,
						"Keine KAoAKategorie mit der ID 1000000 vorhanden."),
				arguments("Merkmal unbekannt", daten(ID_JAHRGANG, 10L, 1000L, 68L, d -> d.bemerkung = BEMERKUNG), Status.NOT_FOUND,
						"Kein KAoAMerkmal mit der ID 1000 vorhanden."),
				arguments("Merkmal passt nicht zur Kategorie", daten(ID_JAHRGANG, 10L, 47L, 44L, d -> d.bemerkung = BEMERKUNG), Status.BAD_REQUEST,
						"Das KAoAMerkmal mit der ID 47 passt nicht zur Kategorie"),
				arguments("Zusatzmerkmal passt nicht zum Merkmal", daten(ID_JAHRGANG, 10L, 55L, 68L, d -> d.bemerkung = BEMERKUNG), Status.BAD_REQUEST,
						"Das KAoAZusatzmerkmal mit der ID 68 passt nicht zum KAoAMerkmal"),
				arguments("Zusatzmerkmal unbekannt", daten(ID_JAHRGANG, 10L, 51L, 3500000L, d -> d.bemerkung = BEMERKUNG), Status.NOT_FOUND,
						"Kein KAoAZusatzmerkmal mit der ID 3500000 vorhanden."),
				arguments("Berufsfeld fehlt", berufsfeld(null, "abc"), Status.BAD_REQUEST, "idBerufsfeld darf nicht null sein."),
				arguments("Berufsfeld unbekannt", berufsfeld(370000L, null), Status.NOT_FOUND, "Kein Berufsfeld mit der ID 370000 vorhanden."),
				arguments("Anschlussoption fehlt", anschlussoption(null, "abc"), Status.BAD_REQUEST, "idAnschlussoption darf nicht null sein."),
				arguments("Anschlussoption unbekannt", anschlussoption(3551545L, null), Status.NOT_FOUND,
						"Keine Anschlussoption mit der ID 3551545 vorhanden."),
				arguments("Anschlussoption passt nicht zum Zusatzmerkmal", anschlussoption(33L, null), Status.BAD_REQUEST,
						"Die Anschlussoption mit der ID 33 passt nicht zum KAoAZusatzmerkmal"),
				arguments("Ebene 4 fehlt", ebene4(null, "abc"), Status.BAD_REQUEST, "idEbene4 darf nicht null sein."),
				arguments("Ebene 4 unbekannt", ebene4(987654321L, null), Status.NOT_FOUND, "Keine Ebene4 mit der ID 987654321 vorhanden."),
				arguments("Ebene 4 passt nicht zum Zusatzmerkmal", ebene4(12L, null), Status.BAD_REQUEST,
						"Die Ebene4 mit der ID 12 passt nicht zum KAoAZusatzmerkmal")
		);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("provideGueltigeDaten")
	@DisplayName("validate | Gültige Daten werden akzeptiert")
	void validate_gueltigeDaten(final String bezeichnung, final SchuelerKAoADaten daten) {
		assertThatCode(() -> SchuelerKAoAValidator.validate(daten, SCHULJAHR)).doesNotThrowAnyException();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("provideFehlerfaelle")
	@DisplayName("validate | Ungültige Daten führen zu einer ApiOperationException mit Status und Meldung")
	void validate_fehlerfaelle(final String bezeichnung, final SchuelerKAoADaten daten, final Status status, final String meldung) {
		assertThatThrownBy(() -> SchuelerKAoAValidator.validate(daten, SCHULJAHR))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", status)
				.hasMessageContaining(meldung);
	}

	@Test
	@DisplayName("validate | Mehr als ein optionales Feld gefüllt führt zu 400")
	void validate_mehrAlsEinOptionalesFeld() {
		final var daten = daten(ID_JAHRGANG, 10L, 51L, 68L, d -> {
			d.bemerkung = BEMERKUNG;
			d.idAnschlussoption = 24L;
			d.idBerufsfeld = 2L;
		});

		assertThatThrownBy(() -> SchuelerKAoAValidator.validate(daten, SCHULJAHR))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
				.hasMessageStartingWith("Es darf nur eines der folgenden Felder gefüllt sein");
	}

	@Test
	@DisplayName("validate | Eine leere Bemerkung zählt nicht als gefülltes Feld")
	void validate_leereBemerkungZaehltNicht() {
		final var daten = anschlussoption(24L, "  ");

		assertThatCode(() -> SchuelerKAoAValidator.validate(daten, SCHULJAHR)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("validateAsdJahrgang | Ein im Schuljahr bekannter Schlüssel wird akzeptiert")
	void validateAsdJahrgang_gueltig() {
		assertThatCode(() -> SchuelerKAoAValidator.validateAsdJahrgang("07", SCHULJAHR)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("validateAsdJahrgang | Fehlender Schlüssel führt zu 400, unbekannter Schlüssel zu 404")
	void validateAsdJahrgang_fehler() {
		assertThatThrownBy(() -> SchuelerKAoAValidator.validateAsdJahrgang(null, SCHULJAHR))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
		assertThatThrownBy(() -> SchuelerKAoAValidator.validateAsdJahrgang("ZZ", SCHULJAHR))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

}
