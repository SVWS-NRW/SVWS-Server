package de.svws_nrw.service.schueler.leistungsdaten;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testet den {@link SchuelerLeistungsdatenWerteUtils}.
 */
@DisplayName("SchuelerLeistungsdatenWerteUtils")
class SchuelerLeistungsdatenWerteUtilsTest {

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@ParameterizedTest(name = "validateKursart | {0} ist gültig")
	@NullSource
	@ValueSource(strings = { "PUK", "LK1", "GKM" })
	void validateKursart_gueltig(final String kuerzel) {
		assertThatCode(() -> SchuelerLeistungsdatenWerteUtils.validateKursart(kuerzel)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("validateKursart | unbekannte Kursart - BAD_REQUEST")
	void validateKursart_unbekannt() {
		assertThatThrownBy(() -> SchuelerLeistungsdatenWerteUtils.validateKursart("XYZ"))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Es existiert keine Kursart mit dem Kürzel XYZ.");
	}

	@ParameterizedTest(name = "validateAbifach | {0} ist ungültig")
	@ValueSource(ints = { 0, 6, -1 })
	void validateAbifach_ungueltig(final int abifach) {
		assertThatThrownBy(() -> SchuelerLeistungsdatenWerteUtils.validateAbifach(abifach))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Das Abiturfach muss zwischen 1 und 5 liegen, war aber %d.".formatted(abifach));
	}

	@ParameterizedTest(name = "validateAbifach | {0} ist gültig")
	@ValueSource(ints = { 1, 3, 5 })
	void validateAbifach_gueltig(final int abifach) {
		assertThatCode(() -> SchuelerLeistungsdatenWerteUtils.validateAbifach(abifach)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("validateAbifach | null ist gültig")
	void validateAbifach_null() {
		assertThatCode(() -> SchuelerLeistungsdatenWerteUtils.validateAbifach(null)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("validateNotenkuerzel | unbekanntes Kürzel - BAD_REQUEST")
	void validateNotenkuerzel_ungueltig() {
		assertThatThrownBy(() -> SchuelerLeistungsdatenWerteUtils.validateNotenkuerzel("9x"))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Das Notenkürzel 9x ist ungültig.");
	}

	@ParameterizedTest(name = "validateNotenkuerzel | {0} ist gültig")
	@NullAndEmptySource
	@ValueSource(strings = { "1", "2-", "3+" })
	void validateNotenkuerzel_gueltig(final String kuerzel) {
		assertThatCode(() -> SchuelerLeistungsdatenWerteUtils.validateNotenkuerzel(kuerzel)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("validateUmfangLernstandsbericht | unzulässiger Umfang - BAD_REQUEST")
	void validateUmfang_ungueltig() {
		assertThatThrownBy(() -> SchuelerLeistungsdatenWerteUtils.validateUmfangLernstandsbericht("X"))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Der Umfang des Lernstandsberichts muss V oder R sein, war aber X.");
	}

	@ParameterizedTest(name = "validateUmfangLernstandsbericht | \"{0}\" ist gültig")
	@NullAndEmptySource
	@ValueSource(strings = { "V", "R", " " })
	void validateUmfang_gueltig(final String umfang) {
		assertThatCode(() -> SchuelerLeistungsdatenWerteUtils.validateUmfangLernstandsbericht(umfang)).doesNotThrowAnyException();
	}

}
