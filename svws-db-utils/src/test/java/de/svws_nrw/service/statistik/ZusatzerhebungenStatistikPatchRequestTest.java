package de.svws_nrw.service.statistik;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;

/**
 * Testklasse für die Validierung des {@link ZusatzerhebungenStatistikPatchRequest}
 */
@DisplayName("Tests für die Validierung des Patch-Requests der Zusatzerhebungen")
class ZusatzerhebungenStatistikPatchRequestTest {

	@Test
	@DisplayName("Ein leerer Patch-Request ist gültig")
	void leer_gueltig() {
		assertThatCode(() -> BeanValidator.validate(new ZusatzerhebungenStatistikPatchRequest())).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Der Wert 0 und null sind bei Anzahlen und Durchschnittswerten gültig")
	void wertNullUndNull_gueltig() {
		final var request = new ZusatzerhebungenStatistikPatchRequest();
		request.anzahlSchulanmeldungen = JsonNullable.of(0);
		request.anzahlInternatplaetzeNeutralBelegt = JsonNullable.of(null);
		request.durchschnittKlinikschuleAllgemeinbildendInsgesamt = JsonNullable.of(0.0);
		request.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = JsonNullable.of(null);
		request.idInternatArt = JsonNullable.of(null);
		request.statistikAnsprechpartnerName = JsonNullable.of(null);

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Negative Anzahlen werden abgelehnt")
	void negativeAnzahlen_abgelehnt() {
		final var request = new ZusatzerhebungenStatistikPatchRequest();
		request.anzahlSchulanmeldungen = JsonNullable.of(-1);
		request.anzahlZurueckstellungenInsgesamtQuartal3 = JsonNullable.of(-1);
		request.anzahlInternatplaetzeMaedchenBelegt = JsonNullable.of(-1);

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("anzahlSchulanmeldungen")
				.hasMessageContaining("anzahlZurueckstellungenInsgesamtQuartal3")
				.hasMessageContaining("anzahlInternatplaetzeMaedchenBelegt");
	}

	@Test
	@DisplayName("Negative Durchschnittswerte werden abgelehnt")
	void negativeDurchschnitte_abgelehnt() {
		final var request = new ZusatzerhebungenStatistikPatchRequest();
		request.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = JsonNullable.of(-0.1);

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte");
	}

	@Test
	@DisplayName("Die Texte zum Ansprechpartner sind in der Länge begrenzt")
	void textlaengen_geprueft() {
		final var request = new ZusatzerhebungenStatistikPatchRequest();
		request.statistikAnsprechpartnerName = JsonNullable.of("x".repeat(256));
		request.statistikAnsprechpartnerTelefon = JsonNullable.of("1".repeat(21));
		request.statistikAnsprechpartnerEmail = JsonNullable.of("e".repeat(101));

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("statistikAnsprechpartnerName")
				.hasMessageContaining("statistikAnsprechpartnerTelefon")
				.hasMessageContaining("statistikAnsprechpartnerEmail");
	}

	@Test
	@DisplayName("Die Texte zum Ansprechpartner sind mit maximaler Länge gültig")
	void textlaengen_grenzwerte() {
		final var request = new ZusatzerhebungenStatistikPatchRequest();
		request.statistikAnsprechpartnerName = JsonNullable.of("x".repeat(255));
		request.statistikAnsprechpartnerTelefon = JsonNullable.of("1".repeat(20));
		request.statistikAnsprechpartnerEmail = JsonNullable.of("e".repeat(100));

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Internationale Kontakte dürfen nicht auf null gesetzt werden")
	void hatInternationaleKontakte_nullAbgelehnt() {
		final var request = new ZusatzerhebungenStatistikPatchRequest();
		request.hatInternationaleKontakte = JsonNullable.of(null);

		assertThatThrownBy(() -> BeanValidator.validate(request)).isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("hatInternationaleKontakte");
	}

}
