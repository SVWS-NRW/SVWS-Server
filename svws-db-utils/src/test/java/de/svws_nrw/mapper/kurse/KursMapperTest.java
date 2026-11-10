package de.svws_nrw.mapper.kurse;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.service.kurse.KursCreateRequest;
import de.svws_nrw.service.kurse.KursPatchRequest;

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
	@DisplayName("toApi (KursDaten): Alle Felder werden gemappt")
	void toKursDaten_mapptAlleFelder() {
		final var result = mapper.toApi(createKurs());

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
	@DisplayName("toApi: Nicht gesetzte Werte erhalten die Defaults")
	void toKursDaten_setztDefaults() {
		final var result = mapper.toApi(new DTOKurs(1L, 2L, "D-GK1", 3L));

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
	@DisplayName("toApi: Die Wochenstunden des Lehrers entsprechen ohne eigenen Wert den Wochenstunden des Kurses")
	void toKursDaten_wochenstundenLehrerAusWochenstunden() {
		final var dto = new DTOKurs(1L, 2L, "D-GK1", 3L);
		dto.WochenStd = 4;

		assertThat(mapper.toApi(dto)).hasFieldOrPropertyWithValue("wochenstunden", 4)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", 4.0);
	}

	@Test
	@DisplayName("toApi: Eine leere Zeugnisbezeichnung wird zu null")
	void toKursDaten_leereZeugnisbezeichnungWirdNull() {
		final var dto = createKurs();
		dto.ZeugnisBez = "  ";

		assertThat(mapper.toApi(dto).bezeichnungZeugnis).isNull();
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
	@DisplayName("applyJahrgaenge: Ein Jahrgang setzt Jahrgang_ID und ASDJahrgang und leert die Liste")
	void applyJahrgaenge_einJahrgang() {
		final var dto = new DTOKurs(1L, 10L, "K1", 3L);
		dto.Jahrgaenge = "5,6";
		final var jahrgang = new DTOJahrgang(4L);
		jahrgang.ASDJahrgang = "05";

		mapper.applyJahrgaenge(dto, List.of(jahrgang));

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgang_ID", 4L)
				.hasFieldOrPropertyWithValue("ASDJahrgang", "05")
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("applyJahrgaenge: Mehrere Jahrgänge setzen nur die kommagetrennte Spalte")
	void applyJahrgaenge_mehrereJahrgaenge() {
		final var dto = new DTOKurs(1L, 10L, "K1", 3L);
		dto.Jahrgang_ID = 4L;
		dto.ASDJahrgang = "05";

		mapper.applyJahrgaenge(dto, List.of(new DTOJahrgang(6L), new DTOJahrgang(4L)));

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("ASDJahrgang", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", "6,4");
	}

	@Test
	@DisplayName("applyJahrgaenge: Eine leere Liste leert alle Jahrgangsspalten")
	void applyJahrgaenge_leereListe() {
		final var dto = new DTOKurs(1L, 10L, "K1", 3L);
		dto.Jahrgang_ID = 4L;
		dto.ASDJahrgang = "05";
		dto.Jahrgaenge = "5,6";

		mapper.applyJahrgaenge(dto, List.of());

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("ASDJahrgang", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("applySchienen: Die Schienen werden kommagetrennt gespeichert, eine leere Liste ergibt einen leeren Text")
	void applySchienen_kommagetrennt() {
		final var dto = new DTOKurs(1L, 10L, "K1", 3L);

		mapper.applySchienen(dto, List.of(1, 3));
		assertThat(dto.Schienen).isEqualTo("1,3");

		mapper.applySchienen(dto, List.of());
		assertThat(dto.Schienen).isEmpty();
	}

	private static KursCreateRequest createRequest() {
		final var request = new KursCreateRequest();
		request.idSchuljahresabschnitt = 10L;
		request.kuerzel = "D-GK1";
		request.idFach = 3L;
		request.kursartAllg = "GK";
		return request;
	}

	private static DTOKurs createMinimalenKurs() {
		final var dto = new DTOKurs(1L, 10L, "K1", 3L);
		dto.Sortierung = 5;
		return dto;
	}

	@Test
	@DisplayName("toDomain: Nicht angegebene optionale Felder erhalten die Standardwerte")
	void toDtoKurs_setztDefaults() {
		final var dto = mapper.toDomain(createRequest(), List.of());

		assertThat(dto).hasFieldOrPropertyWithValue("Schuljahresabschnitts_ID", 10L)
				.hasFieldOrPropertyWithValue("KurzBez", "D-GK1")
				.hasFieldOrPropertyWithValue("Fach_ID", 3L)
				.hasFieldOrPropertyWithValue("KursartAllg", "GK")
				.hasFieldOrPropertyWithValue("Sichtbar", true)
				.hasFieldOrPropertyWithValue("Sortierung", 32000)
				.hasFieldOrPropertyWithValue("WochenStd", 3)
				.hasFieldOrPropertyWithValue("Fortschreibungsart", KursFortschreibungsart.KEINE)
				.hasFieldOrPropertyWithValue("EpochU", false)
				.hasFieldOrPropertyWithValue("Lehrer_ID", null)
				.hasFieldOrPropertyWithValue("WochenstdKL", null)
				.hasFieldOrPropertyWithValue("SchulNr", null)
				.hasFieldOrPropertyWithValue("ZeugnisBez", null)
				.hasFieldOrPropertyWithValue("Schienen", null)
				.hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("toDomain: Angegebene Werte werden übernommen")
	void toDtoKurs_uebernimmtAngegebeneWerte() {
		final var request = createRequest();
		request.idLehrer = 7L;
		request.sortierung = 5;
		request.istSichtbar = false;
		request.wochenstunden = 4;
		request.wochenstundenLehrer = 2.5;
		request.idKursFortschreibungsart = 2;
		request.schulnummer = 123456;
		request.istEpochalunterricht = true;
		request.bezeichnungZeugnis = "Deutsch";
		request.schienen = List.of(1, 3);

		assertThat(mapper.toDomain(request, List.of())).hasFieldOrPropertyWithValue("Lehrer_ID", 7L)
				.hasFieldOrPropertyWithValue("Sortierung", 5)
				.hasFieldOrPropertyWithValue("Sichtbar", false)
				.hasFieldOrPropertyWithValue("WochenStd", 4)
				.hasFieldOrPropertyWithValue("WochenstdKL", 2.5)
				.hasFieldOrPropertyWithValue("Fortschreibungsart", KursFortschreibungsart.fromID(2))
				.hasFieldOrPropertyWithValue("SchulNr", 123456)
				.hasFieldOrPropertyWithValue("EpochU", true)
				.hasFieldOrPropertyWithValue("ZeugnisBez", "Deutsch")
				.hasFieldOrPropertyWithValue("Schienen", "1,3");
	}

	@Test
	@DisplayName("toDomain: Mehrere Jahrgänge setzen nur die kommagetrennte Spalte")
	void toDtoKurs_mehrereJahrgaenge() {
		final var dto = mapper.toDomain(createRequest(), List.of(new DTOJahrgang(6L), new DTOJahrgang(4L)));

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("ASDJahrgang", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", "6,4");
	}

	@Test
	@DisplayName("patch: Nicht gesetzte Attribute ändern nichts")
	void patch_nichtGesetzt() {
		final var dto = createMinimalenKurs();

		mapper.patch(new KursPatchRequest(), dto, List.of());

		assertThat(dto).hasFieldOrPropertyWithValue("KurzBez", "K1")
				.hasFieldOrPropertyWithValue("Fach_ID", 3L)
				.hasFieldOrPropertyWithValue("Sortierung", 5);
	}

	@Test
	@DisplayName("patch: Alle gesetzten Attribute werden übernommen")
	void patch_setztAttribute() {
		final var dto = createMinimalenKurs();
		final var request = new KursPatchRequest();
		request.kuerzel = JsonNullable.of("M-LK1");
		request.kursartAllg = JsonNullable.of("LK");
		request.idFach = JsonNullable.of(8L);
		request.idLehrer = JsonNullable.of(7L);
		request.sortierung = JsonNullable.of(7);
		request.istSichtbar = JsonNullable.of(false);
		request.wochenstunden = JsonNullable.of(5);
		request.wochenstundenLehrer = JsonNullable.of(2.5);
		request.istEpochalunterricht = JsonNullable.of(true);
		request.bezeichnungZeugnis = JsonNullable.of("Mathematik");
		request.idKursFortschreibungsart = JsonNullable.of(3);
		request.schulnummer = JsonNullable.of(123456);
		request.schienen = JsonNullable.of(List.of(2, 4));

		mapper.patch(request, dto, List.of());

		assertThat(dto).hasFieldOrPropertyWithValue("KurzBez", "M-LK1")
				.hasFieldOrPropertyWithValue("KursartAllg", "LK")
				.hasFieldOrPropertyWithValue("Fach_ID", 8L)
				.hasFieldOrPropertyWithValue("Lehrer_ID", 7L)
				.hasFieldOrPropertyWithValue("Sortierung", 7)
				.hasFieldOrPropertyWithValue("Sichtbar", false)
				.hasFieldOrPropertyWithValue("WochenStd", 5)
				.hasFieldOrPropertyWithValue("WochenstdKL", 2.5)
				.hasFieldOrPropertyWithValue("EpochU", true)
				.hasFieldOrPropertyWithValue("ZeugnisBez", "Mathematik")
				.hasFieldOrPropertyWithValue("Fortschreibungsart", KursFortschreibungsart.fromID(3))
				.hasFieldOrPropertyWithValue("SchulNr", 123456)
				.hasFieldOrPropertyWithValue("Schienen", "2,4");
	}

	@Test
	@DisplayName("patch: null setzt Lehrer, Schulnummer und Zeugnisbezeichnung auf null und die Lehrer-Wochenstunden auf 0.0")
	void patch_nullBeiErlaubtenAttributen() {
		final var dto = createMinimalenKurs();
		dto.Lehrer_ID = 7L;
		dto.SchulNr = 123456;
		dto.ZeugnisBez = "Deutsch";
		dto.WochenstdKL = 3.0;
		final var request = new KursPatchRequest();
		request.idLehrer = JsonNullable.of(null);
		request.schulnummer = JsonNullable.of(null);
		request.bezeichnungZeugnis = JsonNullable.of(null);
		request.wochenstundenLehrer = JsonNullable.of(null);

		mapper.patch(request, dto, List.of());

		assertThat(dto).hasFieldOrPropertyWithValue("Lehrer_ID", null)
				.hasFieldOrPropertyWithValue("SchulNr", null)
				.hasFieldOrPropertyWithValue("ZeugnisBez", null)
				.hasFieldOrPropertyWithValue("WochenstdKL", 0.0);
	}

	@Test
	@DisplayName("patch: Ein Jahrgang setzt Jahrgang_ID und ASDJahrgang und leert die Liste")
	void patch_einJahrgang() {
		final var dto = createMinimalenKurs();
		dto.Jahrgaenge = "5,6";
		final var jahrgang = new DTOJahrgang(4L);
		jahrgang.ASDJahrgang = "05";
		final var request = new KursPatchRequest();
		request.idJahrgaenge = JsonNullable.of(List.of(4L));

		mapper.patch(request, dto, List.of(jahrgang));

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgang_ID", 4L)
				.hasFieldOrPropertyWithValue("ASDJahrgang", "05")
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("patch: Mehrere Jahrgänge setzen nur die Spalte Jahrgaenge")
	void patch_mehrereJahrgaenge() {
		final var dto = createMinimalenKurs();
		dto.Jahrgang_ID = 4L;
		dto.ASDJahrgang = "05";
		final var request = new KursPatchRequest();
		request.idJahrgaenge = JsonNullable.of(List.of(4L, 5L));

		mapper.patch(request, dto, List.of(new DTOJahrgang(4L), new DTOJahrgang(5L)));

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("ASDJahrgang", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", "4,5");
	}

	@Test
	@DisplayName("patch: Eine leere Jahrgangsliste setzt alle Jahrgangsspalten auf null")
	void patch_leereJahrgaenge() {
		final var dto = createMinimalenKurs();
		dto.Jahrgang_ID = 4L;
		dto.ASDJahrgang = "05";
		final var request = new KursPatchRequest();
		request.idJahrgaenge = JsonNullable.of(List.of());

		mapper.patch(request, dto, List.of());

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgang_ID", null)
				.hasFieldOrPropertyWithValue("ASDJahrgang", null)
				.hasFieldOrPropertyWithValue("Jahrgaenge", null);
	}

	@Test
	@DisplayName("patch: Eine unveränderte Jahrgangsliste und unveränderte Schienen ändern nichts")
	void patch_jahrgaengeUndSchienenUnveraendert() {
		final var dto = createMinimalenKurs();
		dto.Jahrgaenge = "5,6";
		dto.Schienen = "1,2";
		final var request = new KursPatchRequest();
		request.idJahrgaenge = JsonNullable.of(List.of(6L, 5L));
		request.schienen = JsonNullable.of(List.of(2, 1));

		mapper.patch(request, dto, List.of(new DTOJahrgang(6L), new DTOJahrgang(5L)));

		assertThat(dto).hasFieldOrPropertyWithValue("Jahrgaenge", "5,6")
				.hasFieldOrPropertyWithValue("Schienen", "1,2");
	}

	@Test
	@DisplayName("toApi (KursLehrer): Alle Felder werden gemappt")
	void toKursLehrer_mapptAlleFelder() {
		final var dto = new DTOKursLehrer(1L, 2L);
		dto.Anteil = 1.5;

		assertThat(mapper.toApi(dto)).hasFieldOrPropertyWithValue("idKurs", 1L)
				.hasFieldOrPropertyWithValue("idLehrer", 2L)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", 1.5);
	}

	@Test
	@DisplayName("toApi (KursLehrer): Ohne Anteil sind es 0 Wochenstunden")
	void toKursLehrer_ohneAnteil() {
		assertThat(mapper.toApi(new DTOKursLehrer(1L, 2L))).hasFieldOrPropertyWithValue("wochenstundenLehrer", 0.0);
	}

}
