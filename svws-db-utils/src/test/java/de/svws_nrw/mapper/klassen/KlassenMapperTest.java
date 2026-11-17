package de.svws_nrw.mapper.klassen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.json.JsonDaten;
import de.svws_nrw.service.klassen.KlassenCreateRequest;
import de.svws_nrw.service.klassen.KlassenPatchRequest;
import jakarta.ws.rs.core.Response;

/**
 * Test für die Klasse {@link KlassenMapper}
 */
class KlassenMapperTest {

	private static final int SCHULJAHR = 2024;
	private static final long ID_JAHRGANG = 7L;
	private static final long UNBEKANNTE_ID = 999_999_999L;

	private final KlassenMapper mapper = KlassenMapper.INSTANCE;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	private static Schuljahresabschnitt createAbschnitt(final int abschnitt) {
		final Schuljahresabschnitt result = new Schuljahresabschnitt();
		result.id = 10L + abschnitt;
		result.schuljahr = SCHULJAHR;
		result.abschnitt = abschnitt;
		return result;
	}

	private static KlassenMappingContext createContext(final Schulform schulform, final int abschnitt) {
		return new KlassenMappingContext(schulform, createAbschnitt(abschnitt), null, null, null, null);
	}

	private static Schuljahresabschnitt createAbschnitt() {
		return createAbschnitt(1);
	}

	private static DTOKlassen createKlasse(final long id, final String kuerzel) {
		return new DTOKlassen(id, 11L, kuerzel, false, false);
	}

	private static DTOKlassen createKlasse() {
		return createKlasse(5L, "5a");
	}

	private static DTOKlassen createKlasseMitAsdKlasse(final String asdKlasse) {
		final DTOKlassen dto = createKlasse();
		dto.ASDKlasse = asdKlasse;

		return dto;
	}

	private static DTOJahrgang createJahrgang(final String asdJahrgang) {
		final DTOJahrgang jahrgang = new DTOJahrgang(ID_JAHRGANG);
		jahrgang.ASDJahrgang = asdJahrgang;

		return jahrgang;
	}

	private static void assertApiError(final Runnable aufruf, final Response.Status status, final String meldung) {
		assertThatThrownBy(aufruf::run)
				.isInstanceOfSatisfying(ApiOperationException.class, e -> {
					assertThat(e.getStatus()).isEqualTo(status);
					assertThat(e.getBody()).isEqualTo(meldung);
				});
	}


	@Nested
	@DisplayName("toApi")
	class ToKlassenDaten {

		@Test
		@DisplayName("Mappt alle Felder wie im Bestand")
		void mapptAlleFelder() {
			final DTOKlassen dto = createKlasse(4711L, "07a");
			dto.Jahrgang_ID = 7L;
			dto.ASDKlasse = "07A";
			dto.Sortierung = 5;
			dto.AdrMerkmal = "B";
			dto.Bezeichnung = "07a - bilingual";
			dto.PruefOrdnung = "APO-SI";
			dto.NotenGesperrt = true;
			dto.Ankreuzzeugnisse = true;
			dto.Fachklasse_ID = 99L;
			dto.SommerSem = true;
			dto.istJva = true;
			dto.istReformpaedagogik = true;
			dto.VKlasse = "06a";
			dto.FKlasse = "08a";
			dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.HALBTAG.daten(SCHULJAHR).kuerzel;
			final Map<String, DTOKlassen> vorher = Map.of("06a", createKlasse(1L, "06a"));
			final Map<String, DTOKlassen> nachher = Map.of("08a", createKlasse(2L, "08a"));
			final Schueler schueler = new Schueler();
			schueler.id = 77L;

			final KlassenMappingContext ctx = new KlassenMappingContext(Schulform.GY, createAbschnitt(1), vorher, nachher, List.of(3L, 4L), List.of(schueler));

			final KlassenDaten result = mapper.toApi(dto, ctx);

			assertThat(result.id).isEqualTo(4711L);
			assertThat(result.idSchuljahresabschnitt).isEqualTo(11L);
			assertThat(result.kuerzel).isEqualTo("07a");
			assertThat(result.idJahrgang).isEqualTo(7L);
			assertThat(result.parallelitaet).isEqualTo("A");
			assertThat(result.sortierung).isEqualTo(5);
			assertThat(result.teilstandort).isEqualTo("B");
			assertThat(result.beschreibung).isEqualTo("07a - bilingual");
			assertThat(result.pruefungsordnung).isEqualTo("APO-SI");
			assertThat(result.noteneingabeGesperrt).isTrue();
			assertThat(result.verwendungAnkreuzkompetenzen).isTrue();
			assertThat(result.idFachklasse).isEqualTo(99L);
			assertThat(result.beginnSommersemester).isTrue();
			assertThat(result.istJva).isTrue();
			assertThat(result.istReformpaedagogik).isTrue();
			assertThat(result.idAllgemeinbildendOrganisationsform).isEqualTo(AllgemeinbildendOrganisationsformen.HALBTAG.daten(SCHULJAHR).id);
			assertThat(result.idSchulgliederung).isEqualTo(Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).id);
			assertThat(result.idKlassenart).isEqualTo(Klassenart.RK.daten(SCHULJAHR).id);
			assertThat(result.klassenLeitungen).containsExactly(3L, 4L);
			assertThat(result.schueler).containsExactly(schueler);
		}

		@Test
		@DisplayName("Setzt die Defaults bei nicht gesetzten Werten")
		void setztDefaults() {
			final KlassenDaten result = mapper.toApi(createKlasse(1L, "07a"), createContext(Schulform.GY, 1));

			assertThat(result.sortierung).isEqualTo(32000);
			assertThat(result.teilstandort).isEmpty();
			assertThat(result.beschreibung).isEmpty();
			assertThat(result.parallelitaet).isNull();
			assertThat(result.noteneingabeGesperrt).isFalse();
			assertThat(result.verwendungAnkreuzkompetenzen).isFalse();
			assertThat(result.beginnSommersemester).isFalse();
			assertThat(result.istJva).isFalse();
			assertThat(result.istReformpaedagogik).isFalse();
			assertThat(result.kuerzelVorgaengerklasse).isNull();
			assertThat(result.idVorgaengerklasse).isNull();
			assertThat(result.kuerzelFolgeklasse).isNull();
			assertThat(result.idFolgeklasse).isNull();
		}

		@Test
		@DisplayName("Klassenleitungen und Schüler null oder leer ergeben leere Listen")
		void leereListen() {
			final KlassenDaten ohne = mapper.toApi(createKlasse(1L, "07a"), createContext(Schulform.GY, 1));
			final KlassenDaten leer = mapper.toApi(createKlasse(1L, "07a"),
					new KlassenMappingContext(Schulform.GY, createAbschnitt(1), Map.of(), Map.of(), List.of(), List.of()));

			assertThat(ohne.klassenLeitungen).isEmpty();
			assertThat(ohne.schueler).isEmpty();
			assertThat(leer.klassenLeitungen).isEmpty();
			assertThat(leer.schueler).isEmpty();
		}

		@Test
		@DisplayName("Vorgänger- und Folgeklasse: ID und Kürzel werden bestimmt")
		void vorgaengerUndFolgeklasse() {
			final DTOKlassen dto = createKlasse(5L, "07a");
			dto.VKlasse = "06a";
			dto.FKlasse = "08a";

			final KlassenMappingContext ctx = new KlassenMappingContext(Schulform.WB, createAbschnitt(1),
					Map.of("06a", createKlasse(1L, "06a")), Map.of("08a", createKlasse(2L, "08a")), null, null);

			final KlassenDaten result = mapper.toApi(dto, ctx);

			assertThat(result.kuerzelVorgaengerklasse).isEqualTo("06a");
			assertThat(result.idVorgaengerklasse).isEqualTo(1L);
			assertThat(result.kuerzelFolgeklasse).isEqualTo("08a");
			assertThat(result.idFolgeklasse).isEqualTo(2L);
		}

	}


	@Nested
	@DisplayName("Vorgänger- und Folgeklasse")
	class VorgaengerUndFolgeklasse {

		@Test
		@DisplayName("mapKuerzelVorgaengerklasse: Keine Vorgängerklasse gesetzt ergibt null")
		void vorgaengerNichtGesetzt() {
			assertThat(mapper.mapKuerzelVorgaengerklasse("07a", null, Schulform.GY, 2)).isNull();
		}

		@Test
		@DisplayName("mapKuerzelVorgaengerklasse: Im 1. Abschnitt wird das gespeicherte Kürzel verwendet")
		void vorgaengerErsterAbschnitt() {
			assertThat(mapper.mapKuerzelVorgaengerklasse("07a", "06a", Schulform.GY, 1)).isEqualTo("06a");
		}

		@Test
		@DisplayName("mapKuerzelVorgaengerklasse: Im 2. Abschnitt (Halbjahresbetrieb) wird das eigene Kürzel verwendet")
		void vorgaengerZweiterAbschnitt() {
			assertThat(mapper.mapKuerzelVorgaengerklasse("07a", "06a", Schulform.GY, 2)).isEqualTo("07a");
		}

		@Test
		@DisplayName("mapKuerzelVorgaengerklasse: Am Weiterbildungskolleg wird auch im 2. Abschnitt das gespeicherte Kürzel verwendet")
		void vorgaengerWeiterbildungskolleg() {
			assertThat(mapper.mapKuerzelVorgaengerklasse("07a", "06a", Schulform.WB, 2)).isEqualTo("06a");
		}

		@Test
		@DisplayName("mapKuerzelFolgeklasse: Keine Folgeklasse gesetzt ergibt null")
		void folgeNichtGesetzt() {
			assertThat(mapper.mapKuerzelFolgeklasse("07a", null, Schulform.GY, 1)).isNull();
		}

		@Test
		@DisplayName("mapKuerzelFolgeklasse: Im 1. Abschnitt (Halbjahresbetrieb) wird das eigene Kürzel verwendet")
		void folgeErsterAbschnitt() {
			assertThat(mapper.mapKuerzelFolgeklasse("07a", "08a", Schulform.GY, 1)).isEqualTo("07a");
		}

		@Test
		@DisplayName("mapKuerzelFolgeklasse: Im 2. Abschnitt wird das gespeicherte Kürzel verwendet")
		void folgeZweiterAbschnitt() {
			assertThat(mapper.mapKuerzelFolgeklasse("07a", "08a", Schulform.GY, 2)).isEqualTo("08a");
		}

		@Test
		@DisplayName("mapKuerzelFolgeklasse: Am Weiterbildungskolleg wird auch im 1. Abschnitt das gespeicherte Kürzel verwendet")
		void folgeWeiterbildungskolleg() {
			assertThat(mapper.mapKuerzelFolgeklasse("07a", "08a", Schulform.WB, 1)).isEqualTo("08a");
		}

		@Test
		@DisplayName("mapIdKlasse: Klasse vorhanden")
		void idKlasseVorhanden() {
			assertThat(mapper.mapIdKlasse(Map.of("06a", createKlasse(1L, "06a")), "06a")).isEqualTo(1L);
		}

		@Test
		@DisplayName("mapIdKlasse: Klasse nicht vorhanden, Kürzel null oder keine Klassen ergibt null")
		void idKlasseNichtVorhanden() {
			assertThat(mapper.mapIdKlasse(Map.of("06a", createKlasse(1L, "06a")), "06b")).isNull();
			assertThat(mapper.mapIdKlasse(Map.of("06a", createKlasse(1L, "06a")), null)).isNull();
			assertThat(mapper.mapIdKlasse(null, "06a")).isNull();
		}

	}


	@Nested
	@DisplayName("Klassenart")
	class KlassenartTests {

		@Test
		@DisplayName("mapIdKlassenart: Eine zur Schulform passende Klassenart wird verwendet")
		void passendeKlassenart() {
			final Klassenart passend = Klassenart.getBySchuljahrAndSchulform(SCHULJAHR, Schulform.GY).stream()
					.filter(k -> k != Klassenart.RK)
					.findFirst()
					.orElseThrow();

			final Long result = mapper.mapIdKlassenart(passend.daten(SCHULJAHR).kuerzel, createContext(Schulform.GY, 1));

			assertThat(result).isEqualTo(passend.daten(SCHULJAHR).id);
		}

		@Test
		@DisplayName("mapIdKlassenart: Eine nicht zur Schulform passende Klassenart fällt auf RK zurück")
		void nichtPassendeKlassenart() {
			final Klassenart unpassend = Arrays.stream(Klassenart.values())
					.filter(k -> (k != Klassenart.RK) && (k != Klassenart.UNDEFINIERT) && !k.hatSchulform(SCHULJAHR, Schulform.GY)
							&& (k.daten(SCHULJAHR) != null))
					.findFirst().orElseThrow();

			final Long result = mapper.mapIdKlassenart(unpassend.daten(SCHULJAHR).kuerzel, createContext(Schulform.GY, 1));

			assertThat(result).isEqualTo(Klassenart.RK.daten(SCHULJAHR).id);
		}

		@Test
		@DisplayName("mapIdKlassenart: Ein unbekanntes oder fehlendes Kürzel fällt auf RK zurück")
		void unbekannteKlassenart() {
			assertThat(mapper.mapIdKlassenart("gibt-es-nicht", createContext(Schulform.GY, 1))).isEqualTo(Klassenart.RK.daten(SCHULJAHR).id);
			assertThat(mapper.mapIdKlassenart(null, createContext(Schulform.GY, 1))).isEqualTo(Klassenart.RK.daten(SCHULJAHR).id);
		}

	}


	@Nested
	@DisplayName("Schulgliederung")
	class SchulgliederungTests {

		@Test
		@DisplayName("mapIdSchulgliederung: Ein gesetzter Schlüssel liefert die zugehörige Schulgliederung")
		void schluesselGesetzt() {
			final Schulgliederung gliederung = Schulgliederung.data().getEintraegeBySchuljahr(SCHULJAHR).stream()
					.map(e -> Schulgliederung.data().getWertBySchluessel(e.schluessel))
					.filter(g -> (g != null) && g.hatSchulform(SCHULJAHR, Schulform.BK))
					.findFirst()
					.orElseThrow();
			final String schluessel = gliederung.daten(SCHULJAHR).schluessel;

			final long result = mapper.mapIdSchulgliederung(schluessel, createContext(Schulform.BK, 1));

			assertThat(result).isEqualTo(Schulgliederung.getBySchuljahrAndSchulformAndSchluessel(SCHULJAHR, Schulform.BK, schluessel).daten(SCHULJAHR).id);
		}

		@Test
		@DisplayName("mapIdSchulgliederung: Ohne Schlüssel wird die Default-Schulgliederung der Schulform verwendet")
		void schluesselNullMitDefault() {
			final long result = mapper.mapIdSchulgliederung(null, createContext(Schulform.GY, 1));

			assertThat(result).isEqualTo(Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).id);
		}

		@Test
		@DisplayName("mapIdSchulgliederung: Ohne Schlüssel und ohne Default der Schulform ist das Ergebnis -1")
		void schluesselNullOhneDefault() {
			assertThat(Schulgliederung.getDefault(Schulform.BK)).isNull();

			assertThat(mapper.mapIdSchulgliederung(null, createContext(Schulform.BK, 1))).isEqualTo(-1L);
		}

	}


	@Nested
	@DisplayName("Organisationsformen")
	class Organisationsformen {

		@Test
		@DisplayName("Allgemeinbildend: Das Kürzel liefert die ID der Organisationsform")
		void allgemeinbildend() {
			final var eintrag = AllgemeinbildendOrganisationsformen.GANZTAG.daten(SCHULJAHR);

			assertThat(mapper.mapIdAllgemeinbildendOrganisationsform(eintrag.kuerzel, createContext(Schulform.GY, 1))).isEqualTo(eintrag.id);
		}

		@Test
		@DisplayName("Berufsbildend: Das Kürzel liefert die ID der Organisationsform")
		void berufsbildend() {
			final var eintrag = BerufskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR);

			assertThat(mapper.mapIdBerufsbildendOrganisationsform(eintrag.kuerzel, createContext(Schulform.GY, 1))).isEqualTo(eintrag.id);
		}

		@Test
		@DisplayName("Weiterbildung: Das Kürzel liefert die ID der Organisationsform")
		void weiterbildung() {
			final var eintrag = WeiterbildungskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR);

			assertThat(mapper.mapIdWeiterbildungOrganisationsform(eintrag.kuerzel, createContext(Schulform.GY, 1))).isEqualTo(eintrag.id);
		}

		@Test
		@DisplayName("Ein unbekanntes oder fehlendes Kürzel liefert für alle Organisationsformen null")
		void unbekanntesKuerzel() {
			assertThat(mapper.mapIdAllgemeinbildendOrganisationsform("gibt-es-nicht", createContext(Schulform.GY, 1))).isNull();
			assertThat(mapper.mapIdBerufsbildendOrganisationsform("gibt-es-nicht", createContext(Schulform.GY, 1))).isNull();
			assertThat(mapper.mapIdWeiterbildungOrganisationsform("gibt-es-nicht", createContext(Schulform.GY, 1))).isNull();
			assertThat(mapper.mapIdAllgemeinbildendOrganisationsform(null, createContext(Schulform.GY, 1))).isNull();
			assertThat(mapper.mapIdBerufsbildendOrganisationsform(null, createContext(Schulform.GY, 1))).isNull();
			assertThat(mapper.mapIdWeiterbildungOrganisationsform(null, createContext(Schulform.GY, 1))).isNull();
		}

	}


	@Nested
	@DisplayName("Parallelität und Sortierung")
	class ParallelitaetUndSortierung {

		@Test
		@DisplayName("mapParallelitaet: Die Parallelität ist der Teil ab dem 3. Zeichen")
		void parallelitaet() {
			assertThat(mapper.mapParallelitaet("07A")).isEqualTo("A");
			assertThat(mapper.mapParallelitaet("07AB")).isEqualTo("AB");
		}

		@Test
		@DisplayName("mapParallelitaet: Eine kurze ASDKlasse oder null ergibt null")
		void parallelitaetOhne() {
			assertThat(mapper.mapParallelitaet("07")).isNull();
			assertThat(mapper.mapParallelitaet("")).isNull();
			assertThat(mapper.mapParallelitaet(null)).isNull();
		}

		@Test
		@DisplayName("mapSortierung: Der gesetzte Wert wird übernommen, null ergibt den Default 32000")
		void sortierung() {
			assertThat(mapper.mapSortierung(7)).isEqualTo(7);
			assertThat(mapper.mapSortierung(null)).isEqualTo(32000);
		}

	}

	@Nested
	@DisplayName("applySimpleAttributes")
	class ApplySimpleAttributes {

		private static DTOKlassen createKlasseMitWerten() {
			final DTOKlassen dto = createKlasse(1L, "07a");
			dto.Sortierung = 5;
			dto.Bezeichnung = "alt";
			dto.NotenGesperrt = false;
			dto.Ankreuzzeugnisse = false;
			dto.SommerSem = false;

			return dto;
		}

		@Test
		@DisplayName("lässt die Klasse unverändert, wenn kein Attribut gesetzt ist")
		void keepsDtoForUndefinedAttributes() {
			final DTOKlassen dto = createKlasseMitWerten();

			mapper.applySimpleAttributes(new KlassenPatchRequest(), dto);

			assertThat(dto)
					.extracting(d -> d.Klasse, d -> d.Sortierung, d -> d.Bezeichnung, d -> d.NotenGesperrt, d -> d.Ankreuzzeugnisse, d -> d.SommerSem,
							d -> d.istJva, d -> d.istReformpaedagogik)
					.containsExactly("07a", 5, "alt", false, false, false, false, false);
		}

		@Test
		@DisplayName("übernimmt alle gesetzten Attribute aus einem Patch")
		void appliesAllAttributesFromPatch() {
			final DTOKlassen dto = createKlasseMitWerten();
			final KlassenPatchRequest request = new KlassenPatchRequest();
			request.kuerzel = JsonNullable.of("07b");
			request.sortierung = JsonNullable.of(7);
			request.beschreibung = JsonNullable.of("neu");
			request.noteneingabeGesperrt = JsonNullable.of(true);
			request.verwendungAnkreuzkompetenzen = JsonNullable.of(true);
			request.beginnSommersemester = JsonNullable.of(true);
			request.istJva = JsonNullable.of(true);
			request.istReformpaedagogik = JsonNullable.of(true);

			mapper.applySimpleAttributes(request, dto);

			assertThat(dto)
					.extracting(d -> d.Klasse, d -> d.Sortierung, d -> d.Bezeichnung, d -> d.NotenGesperrt, d -> d.Ankreuzzeugnisse, d -> d.SommerSem,
							d -> d.istJva, d -> d.istReformpaedagogik)
					.containsExactly("07b", 7, "neu", true, true, true, true, true);
		}

		@Test
		@DisplayName("übernimmt die gesetzten Attribute aus einem Create-Request und ändert die übrigen nicht")
		void appliesAttributesFromCreateRequest() {
			final DTOKlassen dto = createKlasseMitWerten();
			final KlassenCreateRequest request = new KlassenCreateRequest();
			request.sortierung = 0;
			request.istReformpaedagogik = true;

			mapper.applySimpleAttributes(request, dto);

			assertThat(dto)
					.extracting(d -> d.Sortierung, d -> d.istReformpaedagogik, d -> d.Bezeichnung, d -> d.istJva)
					.containsExactly(0, true, "alt", false);
		}

		@Test
		@DisplayName("übernimmt alle angegebenen Attribute aus einem Create-Request")
		void appliesAllAttributesFromCreateRequest() {
			final DTOKlassen dto = createKlasseMitWerten();
			final KlassenCreateRequest request = new KlassenCreateRequest();
			request.sortierung = 7;
			request.beschreibung = "neu";
			request.noteneingabeGesperrt = true;
			request.verwendungAnkreuzkompetenzen = true;
			request.beginnSommersemester = true;
			request.istJva = true;
			request.istReformpaedagogik = true;

			mapper.applySimpleAttributes(request, dto);

			assertThat(dto)
					.extracting(d -> d.Sortierung, d -> d.Bezeichnung, d -> d.NotenGesperrt, d -> d.Ankreuzzeugnisse, d -> d.SommerSem, d -> d.istJva,
							d -> d.istReformpaedagogik)
					.containsExactly(7, "neu", true, true, true, true, true);
		}

		@Test
		@DisplayName("lässt die Klasse unverändert, wenn im Create-Request kein Attribut angegeben ist")
		void keepsDtoForEmptyCreateRequest() {
			final DTOKlassen dto = createKlasseMitWerten();

			mapper.applySimpleAttributes(new KlassenCreateRequest(), dto);

			assertThat(dto)
					.extracting(d -> d.Sortierung, d -> d.Bezeichnung, d -> d.NotenGesperrt, d -> d.Ankreuzzeugnisse, d -> d.SommerSem, d -> d.istJva,
							d -> d.istReformpaedagogik)
					.containsExactly(5, "alt", false, false, false, false, false);
		}

		@Test
		@DisplayName("übernimmt eine explizit auf null gesetzte Beschreibung")
		void appliesExplicitNullBeschreibung() {
			final DTOKlassen dto = createKlasseMitWerten();
			final KlassenPatchRequest request = new KlassenPatchRequest();
			request.beschreibung = JsonNullable.of(null);

			mapper.applySimpleAttributes(request, dto);

			assertThat(dto)
					.extracting(d -> d.Bezeichnung, d -> d.Sortierung)
					.containsExactly(null, 5);
		}

		@Test
		@DisplayName("ändert weder Kürzel, ASD-Kürzel noch Schulgliederung")
		void keepsNonSimpleAttributes() {
			final DTOKlassen dto = createKlasseMitWerten();
			dto.ASDKlasse = "07A";
			dto.ASDSchulformNr = "X";
			final KlassenPatchRequest request = new KlassenPatchRequest();
			request.sortierung = JsonNullable.of(2);

			mapper.applySimpleAttributes(request, dto);

			assertThat(dto)
					.extracting(d -> d.Klasse, d -> d.ASDKlasse, d -> d.ASDSchulformNr)
					.containsExactly("07a", "07A", "X");
		}

	}

	@Nested
	@DisplayName("applyDefaults")
	class ApplyDefaults {

		@Test
		@DisplayName("setzt bei einer allgemeinbildenden Schulform die Standardwerte")
		void setsDefaultsForAllgemeinbildend() {
			final DTOKlassen dto = createKlasse();

			mapper.applyDefaults(dto, createAbschnitt(), Schulform.GY, "B");

			assertThat(dto)
					.extracting(d -> d.Sortierung, d -> d.AdrMerkmal, d -> d.OrgFormKrz, d -> d.ASDSchulformNr, d -> d.Klassenart)
					.containsExactly(0, "B", AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(SCHULJAHR).kuerzel,
							Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).kuerzel, Klassenart.RK.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("setzt bei einer berufsbildenden Schulform die Organisationsform VOLLZEIT, die erste Schulgliederung und keine Klassenart")
		void setsDefaultsForBerufsbildend() {
			final DTOKlassen dto = createKlasse();
			final var gliederung = Schulgliederung.getBySchuljahrAndSchulform(SCHULJAHR, Schulform.BK).getFirst();

			mapper.applyDefaults(dto, createAbschnitt(), Schulform.BK, "B");

			assertThat(dto)
					.extracting(d -> d.OrgFormKrz, d -> d.ASDSchulformNr, d -> d.Klassenart)
					.containsExactly(BerufskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR).kuerzel, gliederung.daten(SCHULJAHR).kuerzel, null);
		}

		@Test
		@DisplayName("setzt bei einer Schulform der Weiterbildung die Organisationsform VOLLZEIT")
		void setsDefaultsForWeiterbildung() {
			final DTOKlassen dto = createKlasse();
			final var gliederung = Schulgliederung.getBySchuljahrAndSchulform(SCHULJAHR, Schulform.WB).getFirst();

			mapper.applyDefaults(dto, createAbschnitt(), Schulform.WB, "B");

			assertThat(dto)
					.extracting(d -> d.OrgFormKrz, d -> d.ASDSchulformNr, d -> d.Klassenart)
					.containsExactly(WeiterbildungskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR).kuerzel, gliederung.daten(SCHULJAHR).kuerzel,
							Klassenart.RK.daten(SCHULJAHR).kuerzel);
		}

	}

	@Nested
	@DisplayName("applyOrganisationsformen")
	class ApplyOrganisationsformen {

		@Test
		@DisplayName("allgemeinbildend: setzt das Kürzel der Organisationsform")
		void setsAllgemeinbildendOrganisationsform() {
			final DTOKlassen dto = createKlasse();
			final var eintrag = AllgemeinbildendOrganisationsformen.GANZTAG.daten(SCHULJAHR);

			mapper.applyAllgemeinbildendOrganisationsform(dto, createAbschnitt(), Schulform.GY, eintrag.id);

			assertThat(dto.OrgFormKrz).isEqualTo(eintrag.kuerzel);
		}

		@Test
		@DisplayName("allgemeinbildend: setzt bei null die Organisationsform NICHT_ZUGEORDNET")
		void setsNichtZugeordnetForNullAllgemeinbildend() {
			final DTOKlassen dto = createKlasse();

			mapper.applyAllgemeinbildendOrganisationsform(dto, createAbschnitt(), Schulform.GY, null);

			assertThat(dto.OrgFormKrz).isEqualTo(AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("allgemeinbildend: wirft 400 bei einer Schule ohne allgemeinbildende Schulform")
		void throwsForWrongSchulformAllgemeinbildend() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applyAllgemeinbildendOrganisationsform(dto, abschnitt, Schulform.BK, null), Response.Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine allgemeinbildende Schulform hat.");
		}

		@Test
		@DisplayName("allgemeinbildend: wirft 400 bei unbekannter ID und nennt die ID in der Meldung")
		void throwsForUnknownIdAllgemeinbildend() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applyAllgemeinbildendOrganisationsform(dto, abschnitt, Schulform.GY, UNBEKANNTE_ID), Response.Status.BAD_REQUEST,
					"Die ID 999999999 für die Organisationsform (allgemeinbildend) ist ungültig.");
		}

		@Test
		@DisplayName("berufsbildend: setzt das Kürzel der Organisationsform")
		void setsBerufsbildendOrganisationsform() {
			final DTOKlassen dto = createKlasse();
			final var eintrag = BerufskollegOrganisationsformen.TEILZEIT.daten(SCHULJAHR);

			mapper.applyBerufsbildendOrganisationsform(dto, createAbschnitt(), Schulform.BK, eintrag.id);

			assertThat(dto.OrgFormKrz).isEqualTo(eintrag.kuerzel);
		}

		@Test
		@DisplayName("berufsbildend: setzt bei null die Organisationsform NICHT_ZUGEORDNET")
		void setsNichtZugeordnetForNullBerufsbildend() {
			final DTOKlassen dto = createKlasse();

			mapper.applyBerufsbildendOrganisationsform(dto, createAbschnitt(), Schulform.BK, null);

			assertThat(dto.OrgFormKrz).isEqualTo(AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("berufsbildend: wirft 400 bei einer Schule ohne berufsbildende Schulform")
		void throwsForWrongSchulformBerufsbildend() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applyBerufsbildendOrganisationsform(dto, abschnitt, Schulform.GY, null), Response.Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine berufsbildende Schulform hat.");
		}

		@Test
		@DisplayName("berufsbildend: wirft 400 bei unbekannter ID und nennt die ID in der Meldung")
		void throwsForUnknownIdBerufsbildend() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applyBerufsbildendOrganisationsform(dto, abschnitt, Schulform.BK, UNBEKANNTE_ID), Response.Status.BAD_REQUEST,
					"Die ID 999999999 für die Organisationsform (berufsbildend) ist ungültig.");
		}

		@Test
		@DisplayName("Weiterbildung: setzt das Kürzel der Organisationsform")
		void setsWeiterbildungOrganisationsform() {
			final DTOKlassen dto = createKlasse();
			final var eintrag = WeiterbildungskollegOrganisationsformen.TEILZEIT.daten(SCHULJAHR);

			mapper.applyWeiterbildungOrganisationsform(dto, createAbschnitt(), Schulform.WB, eintrag.id);

			assertThat(dto.OrgFormKrz).isEqualTo(eintrag.kuerzel);
		}

		@Test
		@DisplayName("Weiterbildung: setzt bei null die Organisationsform NICHT_ZUGEORDNET")
		void setsNichtZugeordnetForNullWeiterbildung() {
			final DTOKlassen dto = createKlasse();

			mapper.applyWeiterbildungOrganisationsform(dto, createAbschnitt(), Schulform.WB, null);

			assertThat(dto.OrgFormKrz).isEqualTo(AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("Weiterbildung: wirft 400 bei einer Schule ohne Schulform der Weiterbildung")
		void throwsForWrongSchulformWeiterbildung() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applyWeiterbildungOrganisationsform(dto, abschnitt, Schulform.GY, null), Response.Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine Schulform für die Weiterbildung hat.");
		}

		@Test
		@DisplayName("Weiterbildung: wirft 400 bei unbekannter ID und nennt die ID in der Meldung")
		void throwsForUnknownIdWeiterbildung() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applyWeiterbildungOrganisationsform(dto, abschnitt, Schulform.WB, UNBEKANNTE_ID), Response.Status.BAD_REQUEST,
					"Die ID 999999999 für die Organisationsform (Weiterbildungskolleg) ist ungültig.");
		}

	}

	@Nested
	@DisplayName("applySchulgliederung")
	class ApplySchulgliederung {

		@Test
		@DisplayName("setzt die Schulgliederung zur ID, wenn die Schulform sie unterstützt")
		void setsSchulgliederung() {
			final DTOKlassen dto = createKlasse();
			final var gliederung = Schulgliederung.getBySchuljahrAndSchulform(SCHULJAHR, Schulform.BK).getFirst();

			mapper.applySchulgliederung(dto, createAbschnitt(), Schulform.BK, gliederung.daten(SCHULJAHR).id);

			assertThat(dto.ASDSchulformNr).isEqualTo(gliederung.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("setzt bei null die Standard-Schulgliederung der Schulform")
		void setsDefaultForNull() {
			final DTOKlassen dto = createKlasse();

			mapper.applySchulgliederung(dto, createAbschnitt(), Schulform.GY, null);

			assertThat(dto.ASDSchulformNr).isEqualTo(Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("setzt bei -1 die Standard-Schulgliederung der Schulform")
		void setsDefaultForMinusOne() {
			final DTOKlassen dto = createKlasse();

			mapper.applySchulgliederung(dto, createAbschnitt(), Schulform.GY, -1L);

			assertThat(dto.ASDSchulformNr).isEqualTo(Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("entfernt die Schulgliederung bei null, wenn die Schulform keine Standard-Schulgliederung hat")
		void clearsSchulgliederungWithoutDefault() {
			final DTOKlassen dto = createKlasse();
			dto.ASDSchulformNr = "X";

			mapper.applySchulgliederung(dto, createAbschnitt(), Schulform.BK, null);

			assertThat(dto.ASDSchulformNr).isNull();
		}

		@Test
		@DisplayName("wirft 400 bei unbekannter ID")
		void throwsForUnknownId() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applySchulgliederung(dto, abschnitt, Schulform.GY, UNBEKANNTE_ID), Response.Status.BAD_REQUEST,
					"Es konnte keine Schulgliederung für die ID 999999999 gefunden werden.");
		}

		@Test
		@DisplayName("wirft 400, wenn die Schulform die Schulgliederung nicht unterstützt")
		void throwsForUnsupportedSchulgliederung() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();
			final Schulgliederung unpassend = Arrays.stream(Schulgliederung.values())
					.filter(g -> (g.daten(SCHULJAHR) != null) && !g.hatSchulform(SCHULJAHR, Schulform.GY))
					.findFirst()
					.orElseThrow();

			assertApiError(() -> mapper.applySchulgliederung(dto, abschnitt, Schulform.GY, unpassend.daten(SCHULJAHR).id), Response.Status.BAD_REQUEST,
					"Die Schulgliederung wird von der angegeben Schulform nicht unterstützt.");
		}

	}

	@Nested
	@DisplayName("applyKlassenart")
	class ApplyKlassenart {

		@Test
		@DisplayName("setzt das Kürzel der Klassenart")
		void setsKlassenart() {
			final DTOKlassen dto = createKlasse();

			mapper.applyKlassenart(dto, createAbschnitt(), Klassenart.RK.daten(SCHULJAHR).id);

			assertThat(dto.Klassenart).isEqualTo(Klassenart.RK.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("entfernt bei null die Klassenart")
		void clearsKlassenartForNull() {
			final DTOKlassen dto = createKlasse();
			dto.Klassenart = "RK";

			mapper.applyKlassenart(dto, createAbschnitt(), null);

			assertThat(dto.Klassenart).isNull();
		}

		@Test
		@DisplayName("wirft 400 bei unbekannter ID")
		void throwsForUnknownId() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();

			assertApiError(() -> mapper.applyKlassenart(dto, abschnitt, UNBEKANNTE_ID), Response.Status.BAD_REQUEST,
					"Es konnte keine Klassenart für die ID 999999999 gefunden werden.");
		}

		@Test
		@DisplayName("wirft 400, wenn die Klassenart im Schuljahr nicht gültig ist")
		void throwsForKlassenartInvalidInSchuljahr() {
			final DTOKlassen dto = createKlasse();
			final Schuljahresabschnitt abschnitt = createAbschnitt();
			final long id = Klassenart.UNDEFINIERT.daten(1990).id;

			assertThat(Klassenart.UNDEFINIERT.daten(SCHULJAHR)).isNull();
			assertApiError(() -> mapper.applyKlassenart(dto, abschnitt, id), Response.Status.BAD_REQUEST,
					"Die Klassenart mit der ID %d ist für das Schuljahr %d ungültig.".formatted(id, SCHULJAHR));
		}

	}

	@Nested
	@DisplayName("applyFachklasse")
	class ApplyFachklasse {

		@Test
		@DisplayName("setzt die ID einer Fachklasse aus dem Katalog")
		void setsFachklasse() {
			final DTOKlassen dto = createKlasse();
			final long id = JsonDaten.fachklassenManager.getKatalog().indizes.getFirst().fachklassen.getFirst().historie.getFirst().id;

			mapper.applyFachklasse(dto, id);

			assertThat(dto.Fachklasse_ID).isEqualTo(id);
		}

		@Test
		@DisplayName("entfernt bei null die Fachklasse")
		void clearsFachklasseForNull() {
			final DTOKlassen dto = createKlasse();
			dto.Fachklasse_ID = 5L;

			mapper.applyFachklasse(dto, null);

			assertThat(dto.Fachklasse_ID).isNull();
		}

		@Test
		@DisplayName("wirft 400 bei einer Fachklasse, die nicht im Katalog existiert")
		void throwsForUnknownFachklasse() {
			final DTOKlassen dto = createKlasse();

			assertApiError(() -> mapper.applyFachklasse(dto, UNBEKANNTE_ID), Response.Status.BAD_REQUEST,
					"Es konnte keine Fachklasse für die ID 999999999 gefunden werden.");
		}

	}

	@Nested
	@DisplayName("applyJahrgang")
	class ApplyJahrgang {

		@Test
		@DisplayName("setzt den Jahrgang und baut das ASD-Kürzel aus dem Statistik-Jahrgang auf")
		void setsJahrgangAndAsdKlasse() {
			final DTOKlassen dto = createKlasseMitAsdKlasse(null);
			final DTOJahrgang jahrgang = createJahrgang("07");

			mapper.applyJahrgang(dto, jahrgang);

			assertThat(dto)
					.extracting(d -> d.Jahrgang_ID, d -> d.ASDKlasse)
					.containsExactly(ID_JAHRGANG, "07");
		}

		@ParameterizedTest(name = "bisheriges Kürzel {0} und Statistik-Jahrgang {1} ergeben {2}")
		@CsvSource(textBlock = """
				05B, 07, 07B
				05A, E1, 1EA
				05A, E2, 2EA
				05A,  , A
				""")
		@DisplayName("baut das ASD-Kürzel aus Statistik-Jahrgang und bisheriger Parallelität auf")
		void buildsAsdKlasse(final String asdKlasseVorher, final String asdJahrgang, final String erwartet) {
			final DTOKlassen dto = createKlasseMitAsdKlasse(asdKlasseVorher);
			final DTOJahrgang jahrgang = createJahrgang(asdJahrgang);

			mapper.applyJahrgang(dto, jahrgang);

			assertThat(dto.ASDKlasse).isEqualTo(erwartet);
		}

		@Test
		@DisplayName("setzt bei null den Jahrgangsteil JU, entfernt den Jahrgang und behält die Parallelität")
		void setsJuForNull() {
			final DTOKlassen dto = createKlasseMitAsdKlasse("05C");
			dto.Jahrgang_ID = ID_JAHRGANG;

			mapper.applyJahrgang(dto, null);

			assertThat(dto)
					.extracting(d -> d.Jahrgang_ID, d -> d.ASDKlasse)
					.containsExactly(null, "JUC");
		}

		@Test
		@DisplayName("setzt bei null ohne bisheriges ASD-Kürzel nur JU")
		void setsOnlyJuForNullWithoutAsdKlasse() {
			final DTOKlassen dto = createKlasseMitAsdKlasse(null);

			mapper.applyJahrgang(dto, null);

			assertThat(dto.ASDKlasse).isEqualTo("JU");
		}

	}

	@Nested
	@DisplayName("applyParallelitaet")
	class ApplyParallelitaet {

		@Test
		@DisplayName("hängt die Parallelität an den Jahrgangsteil an")
		void appendsParallelitaet() {
			final DTOKlassen dto = createKlasseMitAsdKlasse("07");

			mapper.applyParallelitaet(dto, "B");

			assertThat(dto.ASDKlasse).isEqualTo("07B");
		}

		@Test
		@DisplayName("ersetzt eine bestehende Parallelität")
		void replacesParallelitaet() {
			final DTOKlassen dto = createKlasseMitAsdKlasse("07A");

			mapper.applyParallelitaet(dto, "C");

			assertThat(dto.ASDKlasse).isEqualTo("07C");
		}

		@Test
		@DisplayName("entfernt bei null die Parallelität")
		void removesParallelitaetForNull() {
			final DTOKlassen dto = createKlasseMitAsdKlasse("07A");

			mapper.applyParallelitaet(dto, null);

			assertThat(dto.ASDKlasse).isEqualTo("07");
		}

		@Test
		@DisplayName("verwendet ohne bisheriges ASD-Kürzel nur die Parallelität")
		void usesOnlyParallelitaetWithoutAsdKlasse() {
			final DTOKlassen dto = createKlasseMitAsdKlasse(null);

			mapper.applyParallelitaet(dto, "A");

			assertThat(dto.ASDKlasse).isEqualTo("A");
		}

		@Test
		@DisplayName("setzt ohne bisheriges ASD-Kürzel und ohne Parallelität einen leeren String")
		void setsEmptyStringWithoutAsdKlasseAndParallelitaet() {
			final DTOKlassen dto = createKlasseMitAsdKlasse(null);

			mapper.applyParallelitaet(dto, null);

			assertThat(dto.ASDKlasse).isEmpty();
		}

	}

	@Nested
	@DisplayName("toDomain")
	class ToDomain {

		private KlassenCreateRequest createRequest() {
			final KlassenCreateRequest request = new KlassenCreateRequest();
			request.idSchuljahresabschnitt = 11L;
			request.kuerzel = "05a";

			return request;
		}

		@Test
		@DisplayName("erstellt die Klasse mit den Standardwerten, dem Jahrgang und dem Adressmerkmal")
		void createsKlasseWithDefaults() {
			final DTOKlassen dto = mapper.toDomain(createRequest(), createAbschnitt(), Schulform.GY, createJahrgang("05"), "A");

			assertThat(dto)
					.extracting(d -> d.ID, d -> d.Klasse, d -> d.Schuljahresabschnitts_ID, d -> d.Jahrgang_ID, d -> d.ASDKlasse, d -> d.Sortierung,
							d -> d.AdrMerkmal, d -> d.Klassenart)
					.containsExactly(0L, "05a", 11L, ID_JAHRGANG, "05", 0, "A", Klassenart.RK.daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("hängt die Parallelität an das ASD-Kürzel an, das aus dem Jahrgang entsteht")
		void appendsParallelitaetToJahrgangsteil() {
			final KlassenCreateRequest request = createRequest();
			request.parallelitaet = "B";

			final DTOKlassen dto = mapper.toDomain(request, createAbschnitt(), Schulform.GY, createJahrgang("05"), "A");

			assertThat(dto.ASDKlasse).isEqualTo("05B");
		}

		@Test
		@DisplayName("legt ohne Jahrgang eine jahrgangsübergreifende Klasse an")
		void createsJahrgangsuebergreifendeKlasse() {
			final DTOKlassen dto = mapper.toDomain(createRequest(), createAbschnitt(), Schulform.GY, null, "A");

			assertThat(dto)
					.extracting(d -> d.Jahrgang_ID, d -> d.ASDKlasse)
					.containsExactly(null, "JU");
		}

		@Test
		@DisplayName("übernimmt angegebene Attribute und überschreibt damit die Standardwerte")
		void appliesGivenAttributes() {
			final KlassenCreateRequest request = createRequest();
			request.teilstandort = "C";
			request.sortierung = 4;
			request.idKlassenart = Klassenart.RK.daten(SCHULJAHR).id;
			request.idSchulgliederung = KlassenMapper.KEINE_SCHULGLIEDERUNG;
			request.idAllgemeinbildendOrganisationsform = AllgemeinbildendOrganisationsformen.GANZTAG.daten(SCHULJAHR).id;

			final DTOKlassen dto = mapper.toDomain(request, createAbschnitt(), Schulform.GY, createJahrgang("05"), "A");

			assertThat(dto)
					.extracting(d -> d.AdrMerkmal, d -> d.Sortierung, d -> d.OrgFormKrz, d -> d.ASDSchulformNr)
					.containsExactly("C", 4, AllgemeinbildendOrganisationsformen.GANZTAG.daten(SCHULJAHR).kuerzel,
							Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).kuerzel);
		}

		@Test
		@DisplayName("wirft 400 bei einer Organisationsform, die nicht zur Schulform passt")
		void throwsForWrongOrganisationsform() {
			final KlassenCreateRequest request = createRequest();
			request.idBerufsbildendOrganisationsform = BerufskollegOrganisationsformen.VOLLZEIT.daten(SCHULJAHR).id;
			final Schuljahresabschnitt abschnitt = createAbschnitt();
			final DTOJahrgang jahrgang = createJahrgang("05");

			assertApiError(() -> mapper.toDomain(request, abschnitt, Schulform.GY, jahrgang, "A"), Response.Status.BAD_REQUEST,
					"Der Wert kann nicht gesetzt werden, da die Schule keine berufsbildende Schulform hat.");
		}

	}

	@Nested
	@DisplayName("patch")
	class PatchTests {

		@Test
		@DisplayName("überträgt nur die gesetzten Attribute")
		void appliesOnlyPresentAttributes() {
			final DTOKlassen dto = createKlasseMitAsdKlasse("05A");
			dto.Klassenart = Klassenart.RK.daten(SCHULJAHR).kuerzel;
			dto.Sortierung = 9;
			final KlassenPatchRequest request = new KlassenPatchRequest();
			request.parallelitaet = JsonNullable.of("C");
			request.teilstandort = JsonNullable.of("B");
			request.idKlassenart = JsonNullable.of(null);

			mapper.patch(request, dto, createAbschnitt(), Schulform.GY);

			assertThat(dto)
					.extracting(d -> d.Klasse, d -> d.ASDKlasse, d -> d.AdrMerkmal, d -> d.Klassenart, d -> d.Sortierung)
					.containsExactly("5a", "05C", "B", null, 9);
		}

		@Test
		@DisplayName("ändert bei einem leeren Patch nichts")
		void keepsKlasseForEmptyPatch() {
			final DTOKlassen dto = createKlasseMitAsdKlasse("05A");
			dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.HALBTAG.daten(SCHULJAHR).kuerzel;

			mapper.patch(new KlassenPatchRequest(), dto, createAbschnitt(), Schulform.GY);

			assertThat(dto)
					.extracting(d -> d.ASDKlasse, d -> d.OrgFormKrz, d -> d.AdrMerkmal)
					.containsExactly("05A", AllgemeinbildendOrganisationsformen.HALBTAG.daten(SCHULJAHR).kuerzel, null);
		}

		@Test
		@DisplayName("setzt bei einer Organisationsform null die Organisationsform NICHT_ZUGEORDNET")
		void setsNichtZugeordnetForNullOrganisationsform() {
			final DTOKlassen dto = createKlasse();
			final KlassenPatchRequest request = new KlassenPatchRequest();
			request.idAllgemeinbildendOrganisationsform = JsonNullable.of(null);

			mapper.patch(request, dto, createAbschnitt(), Schulform.GY);

			assertThat(dto.OrgFormKrz).isEqualTo(AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(SCHULJAHR).kuerzel);
		}

	}

}
