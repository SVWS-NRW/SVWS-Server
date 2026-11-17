package de.svws_nrw.mapper.statistik;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.schule.DTOStatistikZusatzerhebungen;
import de.svws_nrw.service.statistik.ZusatzerhebungenStatistikPatchRequest;

class ZusatzerhebungenStatistikMapperTest {

	private final ZusatzerhebungenStatistikMapper mapper = ZusatzerhebungenStatistikMapper.INSTANCE;

	// -------------------------------------------------------------------------
	// Hilfsmethoden
	// -------------------------------------------------------------------------

	private static DTOStatistikZusatzerhebungen createEntity() {
		final var entity = new DTOStatistikZusatzerhebungen(7L, true);
		entity.statistikAnsprechpartnerName = "Max Mustermann";
		entity.statistikAnsprechpartnerTelefon = "0211-980500";
		entity.statistikAnsprechpartnerEmail = "max@musterschule.de";
		entity.anzahlSchulanmeldungen = 113;
		entity.anzahlZurueckstellungsverfahren = 17;
		entity.anzahlZurueckstellungenInsgesamt = 15;
		entity.anzahlZurueckstellungenWeiblich = 8;
		entity.anzahlZurueckstellungenAuslaenderZusammen = 5;
		entity.anzahlZurueckstellungenAuslaenderWeiblich = 3;
		entity.anzahlZurueckstellungenInsgesamtQuartal4 = 2;
		entity.anzahlZurueckstellungenInsgesamtQuartal1 = 3;
		entity.anzahlZurueckstellungenInsgesamtQuartal2 = 4;
		entity.anzahlZurueckstellungenInsgesamtQuartal3 = 6;
		entity.idInternatArt = 3L;
		entity.anzahlInternatplaetzeJungenMoeglich = 32;
		entity.anzahlInternatplaetzeJungenBelegt = 30;
		entity.anzahlInternatplaetzeMaedchenMoeglich = 24;
		entity.anzahlInternatplaetzeMaedchenBelegt = 23;
		entity.anzahlInternatplaetzeNeutralMoeglich = 84;
		entity.anzahlInternatplaetzeNeutralBelegt = 79;
		entity.durchschnittKlinikschuleAllgemeinbildendInsgesamt = 199.5;
		entity.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = 49.8;
		entity.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = 15.7;
		entity.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = 2.1;
		entity.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = 20.8;
		entity.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = 3.5;
		return entity;
	}

	private static ZusatzerhebungenStatistikGesamt createApi() {
		final var api = new ZusatzerhebungenStatistikGesamt();
		api.idSchuljahresabschnitt = 7L;
		api.hatInternationaleKontakte = true;
		api.statistikAnsprechpartnerName = "Max Mustermann";
		api.statistikAnsprechpartnerTelefon = "0211-980500";
		api.statistikAnsprechpartnerEmail = "max@musterschule.de";
		api.anzahlSchulanmeldungen = 113;
		api.anzahlZurueckstellungsverfahren = 17;
		api.anzahlZurueckstellungenInsgesamt = 15;
		api.anzahlZurueckstellungenWeiblich = 8;
		api.anzahlZurueckstellungenAuslaenderZusammen = 5;
		api.anzahlZurueckstellungenAuslaenderWeiblich = 3;
		api.anzahlZurueckstellungenInsgesamtQuartal4 = 2;
		api.anzahlZurueckstellungenInsgesamtQuartal1 = 3;
		api.anzahlZurueckstellungenInsgesamtQuartal2 = 4;
		api.anzahlZurueckstellungenInsgesamtQuartal3 = 6;
		api.idInternatArt = 3L;
		api.anzahlInternatplaetzeJungenMoeglich = 32;
		api.anzahlInternatplaetzeJungenBelegt = 30;
		api.anzahlInternatplaetzeMaedchenMoeglich = 24;
		api.anzahlInternatplaetzeMaedchenBelegt = 23;
		api.anzahlInternatplaetzeNeutralMoeglich = 84;
		api.anzahlInternatplaetzeNeutralBelegt = 79;
		api.durchschnittKlinikschuleAllgemeinbildendInsgesamt = 199.5;
		api.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = 49.8;
		api.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = 15.7;
		api.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = 2.1;
		api.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = 20.8;
		api.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = 3.5;
		return api;
	}

	// -------------------------------------------------------------------------
	// toApi
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("toApi")
	class ToApi {

		@Test
		@DisplayName("Mappt alle Felder korrekt")
		void toApi_mapptAlleFelder() {
			final var result = mapper.toApi(createEntity());

			assertThat(result).usingRecursiveComparison().isEqualTo(createApi());
		}

		@Test
		@DisplayName("Mappt nicht gesetzte Werte auf die Standardwerte")
		void toApi_standardwerte() {
			final var entity = new DTOStatistikZusatzerhebungen(7L, null);

			final var expected = new ZusatzerhebungenStatistikGesamt();
			expected.idSchuljahresabschnitt = 7L;

			assertThat(mapper.toApi(entity)).usingRecursiveComparison().isEqualTo(expected);
		}

		@Test
		@DisplayName("Rundet Durchschnittswerte aus der Datenbank (FLOAT) auf eine Nachkommastelle")
		void toApi_rundetDurchschnitte() {
			final var entity = createEntity();
			entity.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = (double) 49.8f;
			entity.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = (double) 15.7f;

			final var result = mapper.toApi(entity);

			assertThat(result.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte).isEqualTo(49.8);
			assertThat(result.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt).isEqualTo(15.7);
		}
	}

	// -------------------------------------------------------------------------
	// patch
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("patch")
	class Patch {

		@Test
		@DisplayName("Ein leerer Patch ändert nichts")
		void patch_leer() {
			final var entity = createEntity();

			mapper.patch(new ZusatzerhebungenStatistikPatchRequest(), entity);

			assertThat(entity).usingRecursiveComparison().isEqualTo(createEntity());
		}

		@Test
		@DisplayName("Ändert nur die im Patch definierten Felder")
		void patch_nurDefinierteFelder() {
			final var entity = createEntity();
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.hatInternationaleKontakte = JsonNullable.of(false);
			request.anzahlSchulanmeldungen = JsonNullable.of(120);
			request.idInternatArt = JsonNullable.of(2L);
			request.statistikAnsprechpartnerEmail = JsonNullable.of("neu@musterschule.de");

			mapper.patch(request, entity);

			final var expected = createEntity();
			expected.hatInternationaleKontakte = false;
			expected.anzahlSchulanmeldungen = 120;
			expected.idInternatArt = 2L;
			expected.statistikAnsprechpartnerEmail = "neu@musterschule.de";
			assertThat(entity).usingRecursiveComparison().isEqualTo(expected);
		}

		@Test
		@DisplayName("Ein mit null definierter Wert setzt das Feld auf null")
		void patch_nullLoeschtWert() {
			final var entity = createEntity();
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.anzahlInternatplaetzeJungenMoeglich = JsonNullable.of(null);
			request.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = JsonNullable.of(null);
			request.idInternatArt = JsonNullable.of(null);
			request.statistikAnsprechpartnerName = JsonNullable.of(null);

			mapper.patch(request, entity);

			assertThat(entity.anzahlInternatplaetzeJungenMoeglich).isNull();
			assertThat(entity.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt).isNull();
			assertThat(entity.idInternatArt).isNull();
			assertThat(entity.statistikAnsprechpartnerName).isNull();
		}

		@Test
		@DisplayName("Leere Texte beim Ansprechpartner werden als null gespeichert")
		void patch_leereTexteAlsNull() {
			final var entity = createEntity();
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.statistikAnsprechpartnerName = JsonNullable.of("");
			request.statistikAnsprechpartnerTelefon = JsonNullable.of("   ");
			request.statistikAnsprechpartnerEmail = JsonNullable.of("");

			mapper.patch(request, entity);

			assertThat(entity.statistikAnsprechpartnerName).isNull();
			assertThat(entity.statistikAnsprechpartnerTelefon).isNull();
			assertThat(entity.statistikAnsprechpartnerEmail).isNull();
		}

		@Test
		@DisplayName("Die ID des Schuljahresabschnitts wird durch einen Patch nicht verändert")
		void patch_idUnveraendert() {
			final var entity = createEntity();
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.anzahlSchulanmeldungen = JsonNullable.of(1);

			mapper.patch(request, entity);

			assertThat(entity.idSchuljahresabschnitt).isEqualTo(7L);
		}

		@ParameterizedTest(name = "{0} wird zu {1}")
		@CsvSource({ "49.84, 49.8", "49.85, 49.9", "0.05, 0.1", "0.04, 0.0", "12, 12.0", "199.95, 200.0" })
		@DisplayName("Rundet Durchschnittswerte kaufmännisch auf eine Nachkommastelle")
		void patch_rundetDurchschnitte(final double eingabe, final double erwartet) {
			final var entity = createEntity();
			final var request = new ZusatzerhebungenStatistikPatchRequest();
			request.durchschnittKlinikschuleAllgemeinbildendInsgesamt = JsonNullable.of(eingabe);
			request.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = JsonNullable.of(eingabe);
			request.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = JsonNullable.of(eingabe);
			request.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = JsonNullable.of(eingabe);
			request.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = JsonNullable.of(eingabe);
			request.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = JsonNullable.of(eingabe);

			mapper.patch(request, entity);

			assertThat(entity.durchschnittKlinikschuleAllgemeinbildendInsgesamt).isEqualTo(erwartet);
			assertThat(entity.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte).isEqualTo(erwartet);
			assertThat(entity.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt).isEqualTo(erwartet);
			assertThat(entity.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte).isEqualTo(erwartet);
			assertThat(entity.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt).isEqualTo(erwartet);
			assertThat(entity.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte).isEqualTo(erwartet);
		}
	}

}
