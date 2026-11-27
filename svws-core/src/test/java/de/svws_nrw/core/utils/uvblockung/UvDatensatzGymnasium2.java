package de.svws_nrw.core.utils.uvblockung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenSchiene;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.utils.uv.UvManager;

/**
 * Diese Klasse stellt einen zweiten, großen, realitätsnahen Testdatensatz für die Unterrichtsverteilung bereit.
 * Der Datensatz wird vollständig deterministisch programmatisch erzeugt – und zwar aus einer bekannten
 * Lösung heraus: Jedem Kurs wird bereits bei der Erzeugung eine Lehrkraft zugewiesen und das
 * Pflichtstundensoll jeder Lehrkraft als Summe der zugewiesenen Kursstunden abgeleitet. Damit existiert
 * garantiert eine optimale Lösung (Malus 0).
 *
 * Enthalten sind zusätzlich bewusste Extremfälle:
 * - Lehrkräfte mit nur einem Fach,
 * - Lerngruppen mit nur einer oder sehr wenigen möglichen Lehrkräften (seltene Fächer, Sek-II-Bindung),
 * - Lehrkräfte mit zwei oder drei Fächern, die zu 100 % in KU eingesetzt werden müssen, weil sonst
 *   nicht genügend KU-Lehrkräfte vorhanden sind (ihr KU-Stundenanteil entspricht exakt ihrem Soll).
 *
 * @author Benjamin A. Bartsch
 */
class UvDatensatzGymnasium2 {

	/** Die ID des Planungsabschnitts. */
	public static final long PLANUNGSABSCHNITT = 1L;

	/** Der Gültigkeitsbeginn des Planungsabschnitts. */
	public static final String GUELTIG_VON = "2026-08-01";

	/** Die ID der fixierten Lehrkraft (wird beim Erzeugen gesetzt). */
	public static long idLehrkraftFixiert = 0;

	/** Die ID der Lerngruppe für die Fixierung (wird beim Erzeugen gesetzt). */
	public static long idLerngruppeFixiert = 0;

	/** Die ID der verbotenen Lehrkraft (wird beim Erzeugen gesetzt). */
	public static long idLehrkraftVerboten = 0;

	/** Die ID der Lerngruppe für das Verbot (wird beim Erzeugen gesetzt). */
	public static long idLerngruppeVerboten = 0;

	/** Fächer: Kürzel|Bezeichnung */
	private static final String[] FAECHER = {
					"56FörSp|Fördersport (Stufen 5/6)",
					"BI|BI",
					"CH|CH",
					"D|D",
					"E|E",
					"EK|EK",
					"ER|ER",
					"F7|Französisch (ab 7)",
					"GE|GE",
					"IF|IF",
					"Kl|Klassenlehrerstunde",
					"KR|KR",
					"KU|KU",
					"L7|Latein (ab 7)",
					"M|M",
					"MI|MI",
					"MU|MU",
					"NW|NW",
					"PA|PA",
					"PH|PH",
					"PPL|Praktische Philosophie",
					"RE|RE",
					"Schwi|Sport (Schwimmhalle)",
					"SP|SP",
					"SW|SW",
					"Vert M|Vert M",
					"WP|Wirtschaft/Politik",
	};

	/** Jahrgänge: Kürzel|Kurzbezeichnung|Bezeichnung */
	private static final String[] JAHRGAENGE = {
					"05|5|Jahrgang 5",
					"06|6|Jahrgang 6",
					"07|7|Jahrgang 7",
					"08|8|Jahrgang 8",
					"09|9|Jahrgang 9",
					"10|10|Jahrgang 10",
					"EF|EF|Einführungsphase",
					"Q1|Q1|Qualifikationsphase 1",
					"Q2|Q2|Qualifikationsphase 2",
	};

	/** Die Jahrgänge der Sekundarstufe I. */
	private static final String[] JG_SEK1 = { "05", "06", "07", "08", "09", "10" };

	/** Die Klassenbuchstaben (12 Parallelklassen je Jahrgang). */
	private static final String[] BUCHSTABEN = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l" };

	/** Die Schülerzahlen der Sekundarstufe II: Jahrgang|Schülerzahl */
	private static final String[] SEK2_JAHRGAENGE = {
					"EF|300",
					"Q1|225",
					"Q2|270",
	};

	/**
	 * Lehrkräfte-Fachkombinationen: Fächer|Anzahl|Sekundarstufen (1 = nur Sek I, 2 = nur Sek II, 12 = beides).
	 * Kombinationen mit führendem "KU," sind die KU-100%-Fälle (sie werden nur im Fach KU eingesetzt).
	 */
	private static final String[] KOMBINATIONEN = {
					"D,GE,Kl|12|12",
					"D,EK|6|12",
					"D,KR|4|12",
					"D,RE|4|12",
					"D,MI|2|12",
					"E,GE,Kl|10|12",
					"E,EK|6|12",
					"E,SW|6|12",
					"E,F7|5|12",
					"E,PA|2|12",
					"M,PH,Kl|10|12",
					"M,IF|6|12",
					"M,CH|4|12",
					"M,MI|2|12",
					"BI,CH,Kl|6|12",
					"BI,SP|4|12",
					"BI,NW|3|12",
					"CH,PH,Kl|5|12",
					"CH,NW|3|12",
					"PH,IF|4|12",
					"PH,NW|2|12",
					"GE,EK,Kl|6|12",
					"GE,SW|4|12",
					"EK,SW,Kl|4|12",
					"EK,WP|3|12",
					"KU,D|2|1",
					"KU,E|2|1",
					"KU,M|1|1",
					"KU,E|2|2",
					"MU,D|2|12",
					"MU,E|2|12",
					"MU,M|2|12",
					"SP,BI,Kl|4|12",
					"SP,CH|2|12",
					"SP,Schwi|2|12",
					"RE,KR,Kl|6|12",
					"RE,PPL|2|12",
					"PPL,WP|4|12",
					"WP,SW|3|12",
					"F7,L7,Kl|3|12",
					"L7,KR|2|12",
					"IF,PH|3|12",
					"IF,M|2|12",
					"NW,BI|3|12",
					"NW,CH|2|12",
					"ER,KR|2|12",
					"MI,MU|2|12",
					"Schwi,SP|2|12",
					"56FörSp,SP|1|1",
					"Vert M,M|1|12",
					"D|2|12",
					"E|2|12",
					"M|3|12",
					"KU|2|1",
					"SP|2|12",
					"BI|1|12",
					"MU|1|12",
					"RE|1|12",
	};

	/** Schienen der Sekundarstufe I: Bezeichnung|Jahrgänge */
	private static final String[] SCHIENEN_SEK1 = {
					"5RE|05",
					"6RE|06",
					"7FL|07",
					"7RE|07",
					"8FL|08",
					"8RE|08",
					"9FL|09",
					"9RE|09",
					"9WPL|09",
					"10FL|10",
					"10RE|10",
					"10WPL|10",
	};


	/** Eine Kurs-Spezifikation vor der Erzeugung der Daten-Objekte. */
	private static final class KursSpec {

		/** Das Fachkürzel des Kurses. */
		final String fach;

		/** Die halben Wochenstunden (wie im Kurs42-Export). */
		final int std;

		/** Die beteiligten Klassen (Sek I) bzw. Jahrgänge (Sek II). */
		final List<String> beteiligt = new ArrayList<>();

		/** Die Schiene (oder ein leerer String). */
		String schiene = "";

		/** Die zugewiesene Lehrkraft (aus der Lösung). */
		String lehrer = "";

		/** TRUE, falls es sich um einen Leistungskurs handelt. */
		boolean istLK = false;

		KursSpec(final String fach, final int std, final String... beteiligt) {
			this.fach = fach;
			this.std = std;
			Collections.addAll(this.beteiligt, beteiligt);
		}

	}


	/**
	 * Erzeugt den zweiten Testdatensatz aus einer bekannten Lösung heraus.
	 *
	 * @return der {@link UvManager} mit allen Daten des Datensatzes.
	 */
	public static UvManager erzeugeUvManager() {
		final long pl = PLANUNGSABSCHNITT;
		final Random rnd = new Random(20261016L);

		// Fächer
		final List<FachDaten> listFachDaten = new ArrayList<>();
		final List<UvFach> listUvFach = new ArrayList<>();
		final Map<String, Long> mapFachId = new HashMap<>();
		final Map<String, Long> mapFachDatenId = new HashMap<>();
		long idFachDaten = 201;
		long idUvFach = 301;
		for (final String eintrag : FAECHER) {
			final String[] t = eintrag.split("\\|");
			listFachDaten.add(UvDatensatz.erzeugeFachDaten(idFachDaten, t[0], t[0], t[1], true, (int) (idFachDaten - 200)));
			listUvFach.add(UvDatensatz.erzeugeUvFach(idUvFach, idFachDaten, GUELTIG_VON, null));
			mapFachId.put(t[0], idUvFach);
			mapFachDatenId.put(t[0], idFachDaten);
			idFachDaten++;
			idUvFach++;
		}

		// Jahrgänge
		final List<JahrgangsDaten> listJahrgaenge = new ArrayList<>();
		final Map<String, Long> mapJahrgangId = new HashMap<>();
		long idJahrgang = 101;
		for (final String eintrag : JAHRGAENGE) {
			final String[] t = eintrag.split("\\|");
			listJahrgaenge.add(UvDatensatz.erzeugeJahrgang(idJahrgang, t[0], t[1], t[2], (int) (idJahrgang - 100), true));
			mapJahrgangId.put(t[0], idJahrgang);
			idJahrgang++;
		}

		// Klassen, Klassen-Schülergruppen und Schüler der Sekundarstufe I
		final List<UvKlasse> listKlassen = new ArrayList<>();
		final List<UvSchuelergruppe> listSchuelergruppen = new ArrayList<>();
		final List<UvPlanungsabschnittSchueler> listSchueler = new ArrayList<>();
		final List<UvSchuelergruppeSchueler> listGruppeSchueler = new ArrayList<>();
		final Map<String, Long> mapKlassenId = new HashMap<>();
		final Map<String, List<Long>> mapKlassenSchueler = new HashMap<>();
		long idKlasse = 401;
		long idKlassenGruppe = 501;
		long idSchueler = 10001;
		for (final String jg : JG_SEK1) {
			for (final String buchstabe : BUCHSTABEN) {
				final String klKuerzel = jg + buchstabe;
				final long jgId = mapJahrgangId.get(jg);
				final long idGruppe = idKlassenGruppe++;
				listSchuelergruppen.add(UvDatensatz.erzeugeSchuelergruppe(idGruppe, pl, klKuerzel, jgId));
				listKlassen.add(UvDatensatz.erzeugeKlasse(idKlasse, pl, klKuerzel, idGruppe));
				mapKlassenId.put(klKuerzel, idKlasse);
				final List<Long> schuelerIds = new ArrayList<>();
				for (int s = 0; s < 30; s++) {
					final long idS = idSchueler++;
					listSchueler.add(UvDatensatz.erzeugePlanungsabschnittSchueler(pl, idS, jgId, idKlasse));
					listGruppeSchueler.add(UvDatensatz.erzeugeSchuelergruppeSchueler(pl, idGruppe, idS));
					schuelerIds.add(idS);
				}
				mapKlassenSchueler.put(klKuerzel, schuelerIds);
				idKlasse++;
			}
		}

		// Schüler der Sekundarstufe II (ohne Klassen-Zuordnung)
		final Map<String, List<Long>> mapJahrgangSchueler = new HashMap<>();
		for (final String eintrag : SEK2_JAHRGAENGE) {
			final String[] t = eintrag.split("\\|");
			final long jgId = mapJahrgangId.get(t[0]);
			final List<Long> schuelerIds = new ArrayList<>();
			for (int s = 0; s < Integer.parseInt(t[1]); s++) {
				final UvPlanungsabschnittSchueler ps = new UvPlanungsabschnittSchueler();
				ps.idPlanungsabschnitt = pl;
				ps.idSchueler = idSchueler++;
				ps.idJahrgang = jgId;
				ps.idKlasse = null;
				listSchueler.add(ps);
				schuelerIds.add(ps.idSchueler);
			}
			mapJahrgangSchueler.put(t[0], schuelerIds);
		}

		// Lehrkräfte aus den Fachkombinationen erzeugen
		final List<String> lehrerKuerzel = new ArrayList<>();
		final Map<String, Set<String>> mapLehrerFaecher = new HashMap<>();
		final Map<String, Boolean> mapLehrerSek1 = new HashMap<>();
		final Map<String, Boolean> mapLehrerSek2 = new HashMap<>();
		final Set<String> lehrerKuPflicht = new HashSet<>();
		int nummer = 1;
		for (final String kombi : KOMBINATIONEN) {
			final String[] t = kombi.split("\\|");
			final String[] faecher = t[0].split(",");
			final int anzahl = Integer.parseInt(t[1]);
			final String sek = t[2];
			for (int i = 0; i < anzahl; i++) {
				final String kuerzel = String.format("L%03d", nummer++);
				lehrerKuerzel.add(kuerzel);
				mapLehrerFaecher.put(kuerzel, new TreeSet<>(List.of(faecher)));
				mapLehrerSek1.put(kuerzel, sek.contains("1"));
				mapLehrerSek2.put(kuerzel, sek.contains("2"));
				if (faecher[0].equals("KU") && sek.equals("1")) {
					lehrerKuPflicht.add(kuerzel);
				}
			}
		}

		// --- Kurs-Spezifikationen erzeugen (Sek I und Sek II) ---
		final List<KursSpec> kurse = erzeugeKursSpecs();

		// --- Lösung: Jedem Kurs eine Lehrkraft zuweisen (ausgewogen nach bisherigen Stunden) ---
		final Map<String, Double> mapLehrerStunden = new HashMap<>();
		for (final KursSpec kurs : kurse) {
			final String lehrer = gibLoesungsLehrkraft(kurs, lehrerKuerzel, mapLehrerFaecher, mapLehrerSek1, mapLehrerSek2,
					lehrerKuPflicht, mapLehrerStunden);
			kurs.lehrer = lehrer;
			mapLehrerStunden.merge(lehrer, kurs.std / 2.0, Double::sum);
		}

		// Lehrkräfte, Pflichtstundensoll, Lehrbefähigungen
		final List<UvLehrer> listLehrer = new ArrayList<>();
		final List<UvLehrerPflichtstundensoll> listSoll = new ArrayList<>();
		final List<UvPlanungsabschnittLehrer> listPlanungsabschnittLehrer = new ArrayList<>();
		final List<LehrerUnterrichtsfach> listUnterrichtsfach = new ArrayList<>();
		final Map<String, Long> mapLehrerId = new HashMap<>();
		long idLehrer = 1001;
		long idSoll = 3001;
		long idUnterrichtsfach = 4001;
		for (final String kuerzel : lehrerKuerzel) {
			final long lId = idLehrer++;
			mapLehrerId.put(kuerzel, lId);
			listLehrer.add(UvDatensatz.erzeugeLehrer(lId, kuerzel, "Lehrkraft", kuerzel, "2020-08-01", null));
			listSoll.add(UvDatensatz.erzeugePflichtstundensoll(idSoll++, lId, GUELTIG_VON, null, mapLehrerStunden.getOrDefault(kuerzel, 0.0)));
			listPlanungsabschnittLehrer.add(UvDatensatz.erzeugePlanungsabschnittLehrer(pl, lId));
			for (final String fach : mapLehrerFaecher.getOrDefault(kuerzel, new TreeSet<>())) {
				listUnterrichtsfach.add(UvDatensatz.erzeugeLehrerUnterrichtsfach(idUnterrichtsfach++, lId, mapFachDatenId.get(fach),
						Boolean.TRUE.equals(mapLehrerSek1.get(kuerzel)), Boolean.TRUE.equals(mapLehrerSek2.get(kuerzel)), false));
			}
		}

		// Schienen (Sek I und Sek II)
		final List<UvSchiene> listSchienen = new ArrayList<>();
		final Map<String, Long> mapSchieneId = new HashMap<>();
		long idSchiene = 5001;
		int schienenNummer = 1;
		for (final String eintrag : SCHIENEN_SEK1) {
			final String[] t = eintrag.split("\\|");
			final UvSchiene schiene = new UvSchiene();
			schiene.id = idSchiene;
			schiene.idPlanungsabschnitt = pl;
			schiene.nummer = schienenNummer++;
			schiene.bezeichnung = t[0];
			schiene.idsJahrgaengeErlaubt.add(mapJahrgangId.get(t[1]));
			listSchienen.add(schiene);
			mapSchieneId.put(t[0], idSchiene);
			idSchiene++;
		}
		for (final String jg : new String[] { "EF", "Q1", "Q2" }) {
			final int anzahlB = jg.equals("EF") ? 12 : (jg.equals("Q1") ? 11 : 10);
			for (int i = 1; i <= anzahlB; i++) {
				final String bezeichnung = jg + "B" + String.format("%02d", i);
				final UvSchiene schiene = new UvSchiene();
				schiene.id = idSchiene;
				schiene.idPlanungsabschnitt = pl;
				schiene.nummer = schienenNummer++;
				schiene.bezeichnung = bezeichnung;
				schiene.idsJahrgaengeErlaubt.add(mapJahrgangId.get(jg));
				listSchienen.add(schiene);
				mapSchieneId.put(bezeichnung, idSchiene);
				idSchiene++;
			}
			if (!jg.equals("EF")) {
				for (int i = 1; i <= 2; i++) {
					final String bezeichnung = jg + "LK" + i;
					final UvSchiene schiene = new UvSchiene();
					schiene.id = idSchiene;
					schiene.idPlanungsabschnitt = pl;
					schiene.nummer = schienenNummer++;
					schiene.bezeichnung = bezeichnung;
					schiene.idsJahrgaengeErlaubt.add(mapJahrgangId.get(jg));
					listSchienen.add(schiene);
					mapSchieneId.put(bezeichnung, idSchiene);
					idSchiene++;
				}
			}
		}

		// Kurse (Kursunterricht: Schiene vorhanden oder mehrere Klassen) mit eigenen Schülergruppen
		final List<UvKurs> listKurse = new ArrayList<>();
		final List<UvLerngruppenSchiene> listLerngruppenSchienen = new ArrayList<>();
		long idKursGruppe = 2001;
		for (int index = 0; index < kurse.size(); index++) {
			final KursSpec kurs = kurse.get(index);
			final long kursId = 1000L + index;
			final boolean istKursunterricht = (!kurs.schiene.isEmpty()) || (kurs.beteiligt.size() > 1)
					|| kurs.beteiligt.get(0).matches("EF|Q1|Q2");
			if (!istKursunterricht) {
				continue;
			}
			final long idGruppe = idKursGruppe++;
			final String bezeichnung = kurs.schiene.isEmpty() ? kurs.fach : kurs.schiene;
			final TreeSet<Long> jgIds = new TreeSet<>();
			final List<Long> auswahl = new ArrayList<>();
			final List<Long> rest = new ArrayList<>();
			for (final String kl : kurs.beteiligt) {
				final boolean istKlasse = mapKlassenSchueler.containsKey(kl);
				final List<Long> pool = (istKlasse ? mapKlassenSchueler : mapJahrgangSchueler).get(kl);
				jgIds.add(mapJahrgangId.get(istKlasse ? kl.substring(0, 2) : kl));
				final List<Long> poolKopie = new ArrayList<>(pool);
				Collections.shuffle(poolKopie, rnd);
				auswahl.add(poolKopie.get(0));
				for (int i = 1; i < poolKopie.size(); i++) {
					rest.add(poolKopie.get(i));
				}
			}
			Collections.shuffle(rest, rnd);
			final int ziel = Math.min(15 + rnd.nextInt(11), auswahl.size() + rest.size());
			for (int i = 0; (i < rest.size()) && (auswahl.size() < ziel); i++) {
				auswahl.add(rest.get(i));
			}
			final UvSchuelergruppe gruppe = UvDatensatz.erzeugeSchuelergruppe(idGruppe, pl, bezeichnung, jgIds.toArray(new Long[0]));
			listSchuelergruppen.add(gruppe);
			for (final Long idS : auswahl) {
				listGruppeSchueler.add(UvDatensatz.erzeugeSchuelergruppeSchueler(pl, idGruppe, idS));
			}
			final UvKurs k = new UvKurs();
			k.id = kursId;
			k.idPlanungsabschnitt = pl;
			k.idSchuljahresabschnitt = 1;
			k.idFach = mapFachId.get(kurs.fach);
			k.kursart = kurs.istLK ? "LK" : (kurs.beteiligt.get(0).matches("EF|Q1|Q2") ? "GK" : "");
			k.kursnummer = (int) kursId;
			k.idSchuelergruppe = idGruppe;
			listKurse.add(k);
		}

		// Lerngruppen (Klassen- und Kursunterricht) und Schienen-Zuordnungen
		final List<UvLerngruppe> listLerngruppen = new ArrayList<>();
		for (int index = 0; index < kurse.size(); index++) {
			final KursSpec kurs = kurse.get(index);
			final long kursId = 1000L + index;
			final boolean istKursunterricht = (!kurs.schiene.isEmpty()) || (kurs.beteiligt.size() > 1)
					|| kurs.beteiligt.get(0).matches("EF|Q1|Q2");
			final UvLerngruppe lg = new UvLerngruppe();
			lg.id = kursId;
			lg.idPlanungsabschnitt = pl;
			lg.wochenstunden = kurs.std / 2.0;
			if (istKursunterricht) {
				lg.idKurs = kursId;
				lg.idKlasse = null;
				lg.idFach = null;
			} else {
				lg.idKurs = null;
				lg.idKlasse = mapKlassenId.get(kurs.beteiligt.get(0));
				lg.idFach = mapFachId.get(kurs.fach);
			}
			listLerngruppen.add(lg);
			if (!kurs.schiene.isEmpty()) {
				final UvLerngruppenSchiene ls = new UvLerngruppenSchiene();
				ls.idPlanungsabschnitt = pl;
				ls.idLerngruppe = kursId;
				ls.idSchiene = mapSchieneId.get(kurs.schiene);
				listLerngruppenSchienen.add(ls);
			}
		}

		// Zeitraster (5 Tage x 9 Stunden)
		final UvZeitraster zeitraster = new UvZeitraster();
		zeitraster.id = 6001;
		zeitraster.gueltigVon = GUELTIG_VON;
		zeitraster.bezeichnung = "Standard";
		final List<UvZeitrasterEintrag> listZeitrasterEintraege = new ArrayList<>();
		long idEintrag = 6101;
		for (int tag = 1; tag <= 5; tag++) {
			for (int stunde = 1; stunde <= 9; stunde++) {
				final UvZeitrasterEintrag eintrag = new UvZeitrasterEintrag();
				eintrag.id = idEintrag++;
				eintrag.idZeitraster = zeitraster.id;
				eintrag.wochentag = tag;
				eintrag.stunde = stunde;
				eintrag.beginn = (stunde - 1) * 45;
				eintrag.ende = eintrag.beginn + 45;
				listZeitrasterEintraege.add(eintrag);
			}
		}
		final UvPlanungsabschnittZeitraster planungsabschnittZeitraster = new UvPlanungsabschnittZeitraster();
		planungsabschnittZeitraster.idPlanungsabschnitt = pl;
		planungsabschnittZeitraster.idZeitraster = zeitraster.id;
		for (final String eintrag : JAHRGAENGE) {
			planungsabschnittZeitraster.idsJahrgaenge.add(mapJahrgangId.get(eintrag.split("\\|")[0]));
		}

		// Fixierung und Verbot festlegen (Fixierung: Lösung bleibt gültig, Verbot: eine andere potentielle Lehrkraft)
		final KursSpec kursFixiert = kurse.get(0);
		idLerngruppeFixiert = 1000L + 0;
		idLehrkraftFixiert = mapLehrerId.get(kursFixiert.lehrer);
		idLerngruppeVerboten = idLerngruppeFixiert;
		String verboten = "";
		for (final String kandidat : lehrerKuerzel) {
			if (kandidat.equals(kursFixiert.lehrer)) {
				continue;
			}
			if (mapLehrerFaecher.get(kandidat).contains(kursFixiert.fach) && Boolean.TRUE.equals(mapLehrerSek1.get(kandidat))) {
				verboten = kandidat;
				break;
			}
		}
		idLehrkraftVerboten = mapLehrerId.getOrDefault(verboten, 0L);

		// Manager erzeugen und alle Objekte hinzufügen
		final UvManager man = UvDatensatz.erzeugeUvManager(listJahrgaenge, listFachDaten);
		man.planungsabschnittAdd(UvDatensatz.erzeugePlanungsabschnitt(pl, GUELTIG_VON, null));
		for (final UvFach fach : listUvFach) {
			man.fachAdd(fach);
		}
		for (final UvKlasse klasse : listKlassen) {
			man.klasseAdd(klasse);
		}
		for (final UvLehrer lehrer : listLehrer) {
			man.lehrerAdd(lehrer);
		}
		for (final UvLehrerPflichtstundensoll soll : listSoll) {
			man.lehrerPflichtstundensollAdd(soll);
		}
		for (final UvPlanungsabschnittLehrer pal : listPlanungsabschnittLehrer) {
			man.planungsabschnittLehrerAdd(pal);
		}
		for (final LehrerUnterrichtsfach uf : listUnterrichtsfach) {
			man.lehrerUnterrichtsfachAdd(uf);
		}
		for (final UvSchuelergruppe gruppe : listSchuelergruppen) {
			man.schuelergruppeAdd(gruppe);
		}
		for (final UvPlanungsabschnittSchueler schueler : listSchueler) {
			man.planungsabschnittSchuelerAdd(schueler);
		}
		for (final UvSchuelergruppeSchueler zuordnung : listGruppeSchueler) {
			man.schuelergruppeSchuelerAdd(zuordnung);
		}
		for (final UvKurs kurs : listKurse) {
			man.kursAdd(kurs);
		}
		for (final UvLerngruppe lg : listLerngruppen) {
			man.lerngruppeAdd(lg);
		}
		for (final UvSchiene schiene : listSchienen) {
			man.schieneAdd(schiene);
		}
		for (final UvLerngruppenSchiene zuordnung : listLerngruppenSchienen) {
			man.lerngruppenSchieneAdd(zuordnung);
		}
		man.zeitrasterAdd(zeitraster);
		for (final UvZeitrasterEintrag eintrag : listZeitrasterEintraege) {
			man.zeitrasterEintragAdd(eintrag);
		}
		man.planungsabschnittZeitrasterAdd(planungsabschnittZeitraster);
		return man;
	}


	/**
	 * Erzeugt die Kurs-Spezifikationen für die Sekundarstufe I und II (typischer Fächerkanon mit Kopplungen).
	 *
	 * @return die Liste der Kurs-Spezifikationen.
	 */
	private static List<KursSpec> erzeugeKursSpecs() {
		final List<KursSpec> kurse = new ArrayList<>();

		// Sekundarstufe I
		for (final String jg : JG_SEK1) {
			final boolean istUnterstufe = jg.equals("05") || jg.equals("06");
			final boolean istMittelstufe = jg.equals("07") || jg.equals("08");
			for (int k = 0; k < BUCHSTABEN.length; k++) {
				final String kl = jg + BUCHSTABEN[k];
				kurse.add(new KursSpec("D", 10, kl));
				kurse.add(new KursSpec("E", 10, kl));
				kurse.add(new KursSpec("M", 10, kl));
				kurse.add(new KursSpec("EK", 4, kl));
				kurse.add(new KursSpec("SP", 8, kl));
				kurse.add(new KursSpec("Kl", 2, kl));
				kurse.add(new KursSpec(k < 6 ? "KU" : "MU", k < 6 ? 8 : 4, kl));
				if (istUnterstufe) {
					kurse.add(new KursSpec("BI", 4, kl));
					kurse.add(new KursSpec("NW", 4, kl));
					if (k < 2) {
						kurse.add(new KursSpec("MI", 2, kl));
					}
					if (k == 0) {
						kurse.add(new KursSpec("Vert M", 2, kl));
					}
				} else {
					kurse.add(new KursSpec(k < 6 ? "F7" : "L7", 8, kl));
					kurse.add(new KursSpec("GE", 4, kl));
					kurse.add(new KursSpec("IF", 4, kl));
					kurse.add(new KursSpec(istMittelstufe ? "PH" : "CH", 4, kl));
				}
			}
			// Kopplungen der Sekundarstufe I
			kurse.add(new KursSpec("RE", 4, jg + "a", jg + "b"));
			kurse.add(new KursSpec("RE", 4, jg + "c", jg + "d"));
			kurse.add(new KursSpec("RE", 4, jg + "e", jg + "f"));
			kurse.add(new KursSpec("KR", 4, jg + "g", jg + "h"));
			kurse.add(new KursSpec("KR", 4, jg + "i", jg + "j"));
			kurse.add(new KursSpec("KR", 4, jg + "k", jg + "l"));
			if (istUnterstufe) {
				kurse.add(new KursSpec("PPL", 4, jg + "a", jg + "b"));
				kurse.add(new KursSpec("PPL", 4, jg + "c", jg + "d"));
				kurse.add(new KursSpec("ER", 4, jg + "g", jg + "h"));
				kurse.add(new KursSpec("Schwi", 4, jg + "a", jg + "b"));
			} else {
				kurse.add(new KursSpec("WP", 4, jg + "a", jg + "b"));
				kurse.add(new KursSpec("WP", 4, jg + "c", jg + "d"));
				kurse.add(new KursSpec("Schwi", 4, jg + "e", jg + "f"));
			}
		}
		// 56FörSp: EIN Kurs über alle Klassen der Jahrgänge 05 und 06 (Kopplung)
		final List<String> foerSpKlassen = new ArrayList<>();
		for (final String jg : new String[] { "05", "06" }) {
			for (final String buchstabe : BUCHSTABEN) {
				foerSpKlassen.add(jg + buchstabe);
			}
		}
		final KursSpec foerSp = new KursSpec("56FörSp", 4, foerSpKlassen.toArray(new String[0]));

		// Schienen der Sekundarstufe I zuordnen
		for (final KursSpec kurs : kurse) {
			if (kurs.beteiligt.size() != 1) {
				continue;
			}
			final String jg = kurs.beteiligt.get(0).substring(0, 2);
			final String jgKurz = jg.startsWith("0") ? jg.substring(1) : jg;
			if (kurs.fach.equals("F7")) {
				kurs.schiene = jgKurz + "FL";
			}
		}
		// Die Kopplungs-Kurse RE und WP erhalten die Schienen ihrer Jahrgänge
		for (final KursSpec kurs : kurse) {
			if (kurs.beteiligt.size() < 2) {
				continue;
			}
			final String jg = kurs.beteiligt.get(0).substring(0, 2);
			final String jgKurz = jg.startsWith("0") ? jg.substring(1) : jg;
			if (kurs.fach.equals("RE")) {
				kurs.schiene = jgKurz + "RE";
			}
			if (kurs.fach.equals("WP") && (jg.equals("09") || jg.equals("10"))) {
				kurs.schiene = jgKurz + "WPL";
			}
		}

		// Sekundarstufe II
		for (final String jg : new String[] { "EF", "Q1", "Q2" }) {
			for (final String fach : new String[] { "D", "E", "M" }) {
				for (int i = 0; i < 5; i++) {
					kurse.add(new KursSpec(fach, 8, jg));
				}
				if (!jg.equals("EF")) {
					final KursSpec lk = new KursSpec(fach, 12, jg);
					lk.istLK = true;
					kurse.add(lk);
				}
			}
			for (final String fach : new String[] { "KU", "MU", "SP", "BI", "CH", "PH", "GE", "EK", "SW", "PA", "IF", "RE" }) {
				for (int i = 0; i < 2; i++) {
					kurse.add(new KursSpec(fach, 8, jg));
				}
				if (!jg.equals("EF") && fach.equals("KU")) {
					final KursSpec lk = new KursSpec(fach, 12, jg);
					lk.istLK = true;
					kurse.add(lk);
				}
			}
		}
		// Schienen der Sekundarstufe II zuordnen (fortlaufend B-Schienen, LKs auf LK-Schienen)
		final Map<String, Integer> mapSchienenZaehler = new HashMap<>();
		for (final KursSpec kurs : kurse) {
			if (kurs.beteiligt.size() != 1) {
				continue;
			}
			final String jg = kurs.beteiligt.get(0);
			if (!jg.matches("EF|Q1|Q2") || !kurs.schiene.isEmpty()) {
				continue;
			}
			final int maxB = jg.equals("EF") ? 12 : (jg.equals("Q1") ? 11 : 10);
			if (kurs.istLK) {
				final int nummer = mapSchienenZaehler.merge(jg + "LK", 1, Integer::sum);
				kurs.schiene = jg + "LK" + (((nummer - 1) % 2) + 1);
			} else {
				final int nummer = mapSchienenZaehler.merge(jg + "B", 1, Integer::sum);
				kurs.schiene = jg + "B" + String.format("%02d", ((nummer - 1) % maxB) + 1);
			}
		}

		kurse.add(foerSp);
		return kurse;
	}


	/**
	 * Liefert die Lehrkraft aus der bekannten Lösung für den Kurs (ausgewogen nach bisherigen Stunden).
	 *
	 * @param kurs               Die Kurs-Spezifikation.
	 * @param lehrerKuerzel      Alle Lehrkräfte-Kürzel.
	 * @param mapLehrerFaecher   Die Fächer je Lehrkraft.
	 * @param mapLehrerSek1      Die Sek-I-Fähigkeit je Lehrkraft.
	 * @param mapLehrerSek2      Die Sek-II-Fähigkeit je Lehrkraft.
	 * @param lehrerKuPflicht    Die KU-100%-Lehrkräfte.
	 * @param mapLehrerStunden   Die bisher zugewiesenen Stunden je Lehrkraft.
	 *
	 * @return das Kürzel der zugewiesenen Lehrkraft.
	 */
	private static String gibLoesungsLehrkraft(final KursSpec kurs, final List<String> lehrerKuerzel,
			final Map<String, Set<String>> mapLehrerFaecher, final Map<String, Boolean> mapLehrerSek1,
			final Map<String, Boolean> mapLehrerSek2, final Set<String> lehrerKuPflicht, final Map<String, Double> mapLehrerStunden) {
		final boolean istSek2 = kurs.beteiligt.get(0).matches("EF|Q1|Q2");
		String beste = null;
		double besteStunden = Double.MAX_VALUE;
		for (final String kuerzel : lehrerKuerzel) {
			if (!mapLehrerFaecher.get(kuerzel).contains(kurs.fach)) {
				continue;
			}
			if (istSek2 && !Boolean.TRUE.equals(mapLehrerSek2.get(kuerzel))) {
				continue;
			}
			if (!istSek2 && !Boolean.TRUE.equals(mapLehrerSek1.get(kuerzel))) {
				continue;
			}
			// KU-100%-Lehrkräfte werden in der Lösung ausschließlich in KU eingesetzt.
			if (lehrerKuPflicht.contains(kuerzel) && !kurs.fach.equals("KU")) {
				continue;
			}
			final double stunden = mapLehrerStunden.getOrDefault(kuerzel, 0.0);
			if (stunden < besteStunden) {
				besteStunden = stunden;
				beste = kuerzel;
			}
		}
		if (beste == null) {
			throw new IllegalStateException("Keine Lehrkraft für Kurs " + kurs.fach + " gefunden.");
		}
		return beste;
	}


	/**
	 * Erzeugt die Regeln für den Benchmark: eine Fixierung und ein Verbot (Lehrkraft-Lerngruppe).
	 *
	 * @return die Liste der Regeln.
	 */
	public static List<UvBlockungRegel> erzeugeRegeln() {
		final UvBlockungRegel regelFixierung = new UvBlockungRegel();
		regelFixierung.typ = UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr;
		regelFixierung.prioritaet = UvBlockungRegelPrioritaet.HOCH.nr;
		regelFixierung.parameter.add(idLehrkraftFixiert);
		regelFixierung.parameter.add(idLerngruppeFixiert);

		final UvBlockungRegel regelVerbot = new UvBlockungRegel();
		regelVerbot.typ = UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr;
		regelVerbot.prioritaet = UvBlockungRegelPrioritaet.HOCH.nr;
		regelVerbot.parameter.add(idLehrkraftVerboten);
		regelVerbot.parameter.add(idLerngruppeVerboten);

		return List.of(regelFixierung, regelVerbot);
	}

}
