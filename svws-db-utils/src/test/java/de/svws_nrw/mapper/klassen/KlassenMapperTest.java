package de.svws_nrw.mapper.klassen;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

/**
 * Test für die Klasse {@link KlassenMapper}
 */
class KlassenMapperTest {

	private static final int SCHULJAHR = 2024;

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

	private static DTOKlassen createKlasse(final long id, final String kuerzel) {
		return new DTOKlassen(id, 11L, kuerzel);
	}


	@Nested
	@DisplayName("toKlassenDaten")
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
			dto.VKlasse = "06a";
			dto.FKlasse = "08a";
			dto.OrgFormKrz = AllgemeinbildendOrganisationsformen.HALBTAG.daten(SCHULJAHR).kuerzel;
			final Map<String, DTOKlassen> vorher = Map.of("06a", createKlasse(1L, "06a"));
			final Map<String, DTOKlassen> nachher = Map.of("08a", createKlasse(2L, "08a"));
			final Schueler schueler = new Schueler();
			schueler.id = 77L;

			final KlassenMappingContext ctx = new KlassenMappingContext(Schulform.GY, createAbschnitt(1), vorher, nachher, List.of(3L, 4L), List.of(schueler));

			final KlassenDaten result = mapper.toKlassenDaten(dto, ctx);

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
			assertThat(result.idAllgemeinbildendOrganisationsform).isEqualTo(AllgemeinbildendOrganisationsformen.HALBTAG.daten(SCHULJAHR).id);
			assertThat(result.idSchulgliederung).isEqualTo(Schulgliederung.getDefault(Schulform.GY).daten(SCHULJAHR).id);
			assertThat(result.idKlassenart).isEqualTo(Klassenart.RK.daten(SCHULJAHR).id);
			assertThat(result.klassenLeitungen).containsExactly(3L, 4L);
			assertThat(result.schueler).containsExactly(schueler);
		}

		@Test
		@DisplayName("Setzt die Defaults bei nicht gesetzten Werten")
		void setztDefaults() {
			final KlassenDaten result = mapper.toKlassenDaten(createKlasse(1L, "07a"), createContext(Schulform.GY, 1));

			assertThat(result.sortierung).isEqualTo(32000);
			assertThat(result.teilstandort).isEmpty();
			assertThat(result.beschreibung).isEmpty();
			assertThat(result.parallelitaet).isNull();
			assertThat(result.noteneingabeGesperrt).isFalse();
			assertThat(result.verwendungAnkreuzkompetenzen).isFalse();
			assertThat(result.beginnSommersemester).isFalse();
			assertThat(result.kuerzelVorgaengerklasse).isNull();
			assertThat(result.idVorgaengerklasse).isNull();
			assertThat(result.kuerzelFolgeklasse).isNull();
			assertThat(result.idFolgeklasse).isNull();
		}

		@Test
		@DisplayName("Klassenleitungen und Schüler null oder leer ergeben leere Listen")
		void leereListen() {
			final KlassenDaten ohne = mapper.toKlassenDaten(createKlasse(1L, "07a"), createContext(Schulform.GY, 1));
			final KlassenDaten leer = mapper.toKlassenDaten(createKlasse(1L, "07a"),
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

			final KlassenDaten result = mapper.toKlassenDaten(dto, ctx);

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
					.filter(k -> k != Klassenart.RK).findFirst().orElseThrow();

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
					.findFirst().orElseThrow();
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

}
