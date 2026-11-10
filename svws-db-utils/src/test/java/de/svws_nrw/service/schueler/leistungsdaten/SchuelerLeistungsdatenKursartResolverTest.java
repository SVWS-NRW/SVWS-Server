package de.svws_nrw.service.schueler.leistungsdaten;

import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SchuelerLeistungsdatenKursartResolverTest {

	private static final int SCHULJAHR = 2024;
	private static final long ID_KURS = 30L;
	private static final long ID_KURSLEHRER = 40L;

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	private static DTOSchuelerLeistungsdaten createLeistungsdaten(final String kursartAllg, final String abifach) {
		final var entity = new DTOSchuelerLeistungsdaten(1L, 10L, 20L);
		entity.KursartAllg = kursartAllg;
		entity.Kursart = kursartAllg;
		entity.AbiFach = abifach;
		return entity;
	}

	private static DTOKurs createKurs(final String kursartAllg) {
		final var kurs = new DTOKurs(ID_KURS, 5L, "D-GK1", 20L);
		kurs.KursartAllg = kursartAllg;
		kurs.Lehrer_ID = ID_KURSLEHRER;
		return kurs;
	}

	@Nested
	@DisplayName("assignKurs")
	class UebernehmeKurs {

		@ParameterizedTest(name = "Kurs {0}, Abiturfach {1} -> Kursart {2}, Abiturfach {3}")
		@CsvSource(nullValues = "null", value = {
				// Sonderfälle der Gesamtschule
				"E,   null, E,   null",
				"G,   null, G,   null",
				"DK,  null, G,   null",
				// Grundkurse berücksichtigen das Abiturfach 3 und 4, die Abiturfächer 1 und 2 werden entfernt
				"GK,  null, GKM, null",
				"GK,  1,    GKM, null",
				"GK,  2,    GKM, null",
				"GK,  3,    AB3, 3",
				"GK,  4,    AB4, 4",
				"GK,  5,    GKM, 5",
				// Leistungskurse berücksichtigen das Abiturfach 2, ohne Abiturfach wird 1 gesetzt
				"LK,  null, LK1, 1",
				"LK,  1,    LK1, 1",
				"LK,  2,    LK2, 2",
				"LK,  3,    LK1, 3",
				// Alle übrigen allgemeinen Kursarten verwenden die erste zulässige Kursart
				"PUK, null, PUK, null",
				"SF,  null, SF,  null",
				"PJK, null, PJK, null",
				// Unbekannte allgemeine Kursart
				"XYZ, null, null, null"
		})
		void bestimmtKursartUndAbifach(final String kursartAllgKurs, final String abifach, final String erwarteteKursart,
				final String erwartetesAbifach) {
			final var entity = createLeistungsdaten("alt", abifach);

			SchuelerLeistungsdatenKursartResolver.assignKurs(entity, createKurs(kursartAllgKurs), SCHULJAHR);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Kursart", erwarteteKursart)
					.hasFieldOrPropertyWithValue("KursartAllg", kursartAllgKurs)
					.hasFieldOrPropertyWithValue("AbiFach", erwartetesAbifach)
					.hasFieldOrPropertyWithValue("Kurs_ID", ID_KURS)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_KURSLEHRER);
		}

		@Test
		@DisplayName("Gleiche allgemeine Kursart - Kursart und Abiturfach bleiben, Kurs und Fachlehrer werden gesetzt")
		void gleicheKursartAllg() {
			final var entity = createLeistungsdaten("GK", "3");
			entity.Kursart = "AB3";

			SchuelerLeistungsdatenKursartResolver.assignKurs(entity, createKurs("GK"), SCHULJAHR);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Kursart", "AB3")
					.hasFieldOrPropertyWithValue("KursartAllg", "GK")
					.hasFieldOrPropertyWithValue("AbiFach", "3")
					.hasFieldOrPropertyWithValue("Kurs_ID", ID_KURS)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_KURSLEHRER);
		}

		@Test
		@DisplayName("Kurs ohne allgemeine Kursart - Kursart bleibt, Kurs und Fachlehrer werden gesetzt")
		void kursOhneKursartAllg() {
			final var entity = createLeistungsdaten("GK", "3");
			entity.Kursart = "AB3";

			SchuelerLeistungsdatenKursartResolver.assignKurs(entity, createKurs(null), SCHULJAHR);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Kursart", "AB3")
					.hasFieldOrPropertyWithValue("KursartAllg", "GK")
					.hasFieldOrPropertyWithValue("Kurs_ID", ID_KURS)
					.hasFieldOrPropertyWithValue("Fachlehrer_ID", ID_KURSLEHRER);
		}
	}

	@Nested
	@DisplayName("assignKursart")
	class SetzeKursart {

		@ParameterizedTest(name = "Kursart {0} -> Kursart {1}, allgemeine Kursart {2}")
		@CsvSource({
				"LK1, LK1, LK",
				"GKM, GKM, GK",
				"PUK, PUK, PUK"
		})
		void setztKursartUndAllgemeineKursart(final ZulaessigeKursart kursart, final String erwarteteKursart, final String erwarteteKursartAllg) {
			final var entity = createLeistungsdaten("alt", null);

			SchuelerLeistungsdatenKursartResolver.assignKursart(entity, kursart, SCHULJAHR);

			assertThat(entity)
					.hasFieldOrPropertyWithValue("Kursart", erwarteteKursart)
					.hasFieldOrPropertyWithValue("KursartAllg", erwarteteKursartAllg);
		}
	}

}
