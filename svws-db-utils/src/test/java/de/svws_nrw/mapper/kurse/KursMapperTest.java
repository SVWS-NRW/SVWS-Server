package de.svws_nrw.mapper.kurse;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;

/**
 * Testklasse für den {@link KursMapper}
 */
@DisplayName("Tests für den KursMapper")
class KursMapperTest {

	private final KursMapper mapper = KursMapper.INSTANCE;

	private static DTOKurs createKurs() {
		final var dto = new DTOKurs(1L, 2L, "D-GK1", 3L);
		dto.Jahrgang_ID = 4L;
		dto.Jahrgaenge = "5,6,7";
		dto.Lehrer_ID = 8L;
		dto.KursartAllg = "GK";
		dto.Sortierung = 6;
		dto.Sichtbar = false;
		dto.Schienen = "1,2,3";
		dto.WochenStd = 3;
		dto.WochenstdKL = 2.5;
		dto.Fortschreibungsart = KursFortschreibungsart.KOMPLETT;
		dto.SchulNr = 123456;
		dto.EpochU = true;
		dto.ZeugnisBez = "Deutsch";
		return dto;
	}

	@Test
	@DisplayName("toKursDaten: Alle Felder werden gemappt")
	void toKursDaten_mapptAlleFelder() {
		final var result = mapper.toKursDaten(createKurs());

		assertThat(result).hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", 2L)
				.hasFieldOrPropertyWithValue("kuerzel", "D-GK1")
				.hasFieldOrPropertyWithValue("idJahrgaenge", List.of(4L, 5L, 6L, 7L))
				.hasFieldOrPropertyWithValue("idFach", 3L)
				.hasFieldOrPropertyWithValue("idLehrer", 8L)
				.hasFieldOrPropertyWithValue("kursartAllg", "GK")
				.hasFieldOrPropertyWithValue("sortierung", 6)
				.hasFieldOrPropertyWithValue("istSichtbar", false)
				.hasFieldOrPropertyWithValue("schienen", List.of(1, 2, 3))
				.hasFieldOrPropertyWithValue("wochenstunden", 3)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", 2.5)
				.hasFieldOrPropertyWithValue("idKursFortschreibungsart", KursFortschreibungsart.KOMPLETT.id)
				.hasFieldOrPropertyWithValue("schulnummer", 123456)
				.hasFieldOrPropertyWithValue("istEpochalunterricht", true)
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "Deutsch")
				.satisfies(k -> assertThat(k.schueler).isEmpty())
				.satisfies(k -> assertThat(k.weitereLehrer).isEmpty());
	}

	@Test
	@DisplayName("toKursDaten: Nicht gesetzte Werte erhalten die Defaults")
	void toKursDaten_setztDefaults() {
		final var result = mapper.toKursDaten(new DTOKurs(1L, 2L, "D-GK1", 3L));

		assertThat(result).hasFieldOrPropertyWithValue("kursartAllg", "")
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("wochenstunden", 0)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", 0.0)
				.hasFieldOrPropertyWithValue("idKursFortschreibungsart", KursFortschreibungsart.KEINE.id)
				.hasFieldOrPropertyWithValue("istEpochalunterricht", false)
				.hasFieldOrPropertyWithValue("idLehrer", null)
				.hasFieldOrPropertyWithValue("schulnummer", null)
				.satisfies(k -> assertThat(k.idJahrgaenge).isEmpty())
				.satisfies(k -> assertThat(k.schienen).isEmpty());
	}

	@Test
	@DisplayName("toKursDaten: Die Wochenstunden des Lehrers entsprechen ohne eigenen Wert den Wochenstunden des Kurses")
	void toKursDaten_wochenstundenLehrerAusWochenstunden() {
		final var dto = new DTOKurs(1L, 2L, "D-GK1", 3L);
		dto.WochenStd = 4;

		assertThat(mapper.toKursDaten(dto)).hasFieldOrPropertyWithValue("wochenstunden", 4)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", 4.0);
	}

	@Test
	@DisplayName("toKursDaten: Eine leere Zeugnisbezeichnung wird zu null")
	void toKursDaten_leereZeugnisbezeichnungWirdNull() {
		final var dto = createKurs();
		dto.ZeugnisBez = "  ";

		assertThat(mapper.toKursDaten(dto).bezeichnungZeugnis).isNull();
	}

	@Test
	@DisplayName("mapIdJahrgaenge: Nicht-Zahlen in der kommagetrennten Liste werden ignoriert")
	void mapIdJahrgaenge_ignoriertNichtZahlen() {
		final var dto = new DTOKurs(1L, 2L, "D-GK1", 3L);
		dto.Jahrgang_ID = 4L;
		dto.Jahrgaenge = "5,abc,,6x,7";

		assertThat(mapper.mapIdJahrgaenge(dto)).containsExactly(4L, 5L, 7L);
	}

	@Test
	@DisplayName("mapIdJahrgaenge: Ohne Jahrgang-ID und ohne Liste ist das Ergebnis leer")
	void mapIdJahrgaenge_ohneJahrgaenge() {
		assertThat(mapper.mapIdJahrgaenge(new DTOKurs(1L, 2L, "D-GK1", 3L))).isEmpty();
	}

	@Test
	@DisplayName("mapSchienen: Nicht-Zahlen werden ignoriert, Leerzeichen entfernt")
	void mapSchienen_ignoriertNichtZahlen() {
		assertThat(mapper.mapSchienen(" 1, x ,,2,3a, 4 ")).containsExactly(1, 2, 4);
	}

	@Test
	@DisplayName("mapSchienen: null und leerer Text ergeben eine leere Liste")
	void mapSchienen_nullUndLeer() {
		assertThat(List.of(mapper.mapSchienen(null), mapper.mapSchienen("  "))).allSatisfy(schienen -> assertThat(schienen).isEmpty());
	}

	@Test
	@DisplayName("toKursLehrer: Alle Felder werden gemappt")
	void toKursLehrer_mapptAlleFelder() {
		final var dto = new DTOKursLehrer(1L, 2L);
		dto.Anteil = 1.5;

		assertThat(mapper.toKursLehrer(dto)).hasFieldOrPropertyWithValue("idKurs", 1L)
				.hasFieldOrPropertyWithValue("idLehrer", 2L)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", 1.5);
	}

	@Test
	@DisplayName("toKursLehrer: Ohne Anteil sind es 0 Wochenstunden")
	void toKursLehrer_ohneAnteil() {
		assertThat(mapper.toKursLehrer(new DTOKursLehrer(1L, 2L))).hasFieldOrPropertyWithValue("wochenstundenLehrer", 0.0);
	}

}
