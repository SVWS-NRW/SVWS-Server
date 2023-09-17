package de.svws_nrw.core.stundenplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.Map3DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient dazu eine CSV-Datei im Format {@link StupasSchulmanagerFormatLine} in einen {@link StundenplanManager} zu konvertieren.
 *
 * @author Benjamin A. Bartsch
 *
 */
public class StupasSchulmanagerFormatReader {

	private StundenplanKomplett komplett = new StundenplanKomplett();
	private final HashMap<String, StundenplanFach> fach_by_kuerzel = new HashMap<>();
	private final HashMap<String, StundenplanRaum> raum_by_kuerzel = new HashMap<>();
	private final HashMap<String, StundenplanLehrer> lehrer_by_kuerzel = new HashMap<>();
	private final HashMap<String, StundenplanJahrgang> jahrgang_by_kuerzel = new HashMap<>();
	private final HashMap<String, StundenplanKlasse> klasse_by_kuerzel = new HashMap<>();
	private final HashMap<String, StundenplanSchiene> schiene_by_kuerzel = new HashMap<>();
	private final HashMap<Long, StundenplanUnterricht> unterricht_by_id = new HashMap<>();
	private final HashMap2D<Integer, Integer, StundenplanZeitraster> zeiraster_by_wochentag_and_stunde = new HashMap2D<>();

	/**
	 *  Der Konstruktor.
	 */
	public StupasSchulmanagerFormatReader() {
		// leer
	}

	/**
	 * Liefert ein {@link StundenplanManager}-Objekt, welches die Daten der übergebenen CSV-Datei importiert enthält.
	 *
	 * @param location    Der Ort der CSV-Ressource, die eingelesen werden soll.
	 * @param gueltigAb   Das datum, ab dem der Stundenplan gilt. Format-Beispiel: "2022-03-15"
	 * @param gueltigBis  Das datum, bis der Stundenplan gilt. Format-Beispiel: "2022-09-25"
	 *
	 * @return ein {@link StundenplanManager}-Objekt, welches die Daten der übergebenen CSV-Datei importiert enthält.
	 */
	public StundenplanManager toManager(final String location, final String gueltigAb, final String gueltigBis) {
		fach_by_kuerzel.clear();
		raum_by_kuerzel.clear();
		lehrer_by_kuerzel.clear();
		jahrgang_by_kuerzel.clear();
		klasse_by_kuerzel.clear();
		schiene_by_kuerzel.clear();
		unterricht_by_id.clear();
		zeiraster_by_wochentag_and_stunde.clear();

		// Daten aus CSV extrahieren.
		final List<StupasSchulmanagerFormatLine> csvData = CsvReader.fromResource(location, StupasSchulmanagerFormatLine.class);

		// Fast leeren Manager erzeugen
		komplett = new StundenplanKomplett();
		komplett.daten.bezeichnungStundenplan = "Import aus " + location;
		komplett.daten.wochenTypModell = getWochenTypModell(csvData);
		komplett.daten.gueltigAb = gueltigAb;
		komplett.daten.gueltigBis = gueltigBis;
		final StundenplanManager m = new StundenplanManager(komplett);

		// AB-Wochen erkennen und umwandeln.
		final List<StupasSchulmanagerFormatLine> csvData2 = convertWochentypen(csvData);

		// Daten überprüfen.
		check(m, csvData);

		// Rekursiv importieren.
		importiere(m, csvData2);

		return m;
	}

	private void check(final StundenplanManager m, final List<StupasSchulmanagerFormatLine> lines) {
		for (final StupasSchulmanagerFormatLine line : lines) {
			if (line.Stunde >= 12)
				throw new DeveloperNotificationException("Die CSV Datei hat Pausenaufsichten!. Bitte vorher entfernen: " + lineToString(line));

			final StundenplanFach fach = getCreateFach(line, m);
			if (fach == null)
				throw new DeveloperNotificationException("Die CSV Datei hat undefinierte Fächer!. Bitte vorher entfernen: " + lineToString(line));
		}
	}

	private List<StupasSchulmanagerFormatLine> convertWochentypen(final List<StupasSchulmanagerFormatLine> list) {
		final List<StupasSchulmanagerFormatLine> list2 = new ArrayList<>();

		// Nach (ID, Wochentag, Stunde) gruppieren.
		final HashMap3D<Integer, Integer, Integer, List<StupasSchulmanagerFormatLine>> unterrichtmenge_by_id_and_wochentag_and_stunde = new HashMap3D<>();
		for (final StupasSchulmanagerFormatLine line : list)
			Map3DUtils.getOrCreateArrayList(unterrichtmenge_by_id_and_wochentag_and_stunde, line.KursId, line.Wochentag, line.Stunde).add(line);

		// Nach (ID, Wochentag, Stunde) konvertieren.
		for (final List<StupasSchulmanagerFormatLine> gruppe : unterrichtmenge_by_id_and_wochentag_and_stunde.getNonNullValuesAsList())
			convertWochentypenWochentagStunde(list2, gruppe);

		return list2;
	}

	private void convertWochentypenWochentagStunde(final List<StupasSchulmanagerFormatLine> copy, final List<StupasSchulmanagerFormatLine> list) {
		final int wochentypModell = komplett.daten.wochenTypModell;

		if (wochentypModell == 0) {
			// Unterricht findet jede Woche statt (und es gibt keine Multiwochen).
			for (final StupasSchulmanagerFormatLine line : list) {
				line.Woche = 0;
				copy.add(line);
			}
		} else {
			if (list.size() > wochentypModell) {
				// Fehler
				throw new DeveloperNotificationException("Die CSV Datei hat mehr Unterrichte pro 'Wochentag/Stunde' als Wochentypen!" + linesToString(list));
			}
			if (list.size() < wochentypModell) {
				// Unterricht findet nur in speziellen Wochen statt (A oder B oder C ...).
				copy.addAll(list);
			} else {
				// Unterricht findet jede Woche statt (und es gibt Multiwochen).
				final StupasSchulmanagerFormatLine first = list.get(0);
				first.Woche = 0;
				copy.add(first);
			}
		}

	}

	private void importiere(final StundenplanManager m, final List<StupasSchulmanagerFormatLine> list) {
		// Daten nach der Lerngruppen-ID (Kurs-ID) gruppieren.
		final HashMap<Integer, List<StupasSchulmanagerFormatLine>> unterrichtmenge_by_id = new HashMap<>();
		for (final StupasSchulmanagerFormatLine line : list)
			MapUtils.getOrCreateArrayList(unterrichtmenge_by_id, line.KursId).add(line);

		// Rekursiv importieren.
		for (final Integer key : unterrichtmenge_by_id.keySet())
			importiereKlassenOderKursunterricht(m, unterrichtmenge_by_id.get(key));
	}

	//	Id;KursId;Art;Lehrerkuerzel;Fach;Kurs;Raum;Wochentag;Stunde;Bezeichnung;Woche;Klassen;Kopplung
	//	561;36;Stunde;LEHR3;BI;;RAUM3;2;3; 3;1;05a;
	//	562;36;Stunde;LEHR3;BI;;RAUM3;2;3; 3;2;05a;
	//	563;36;Stunde;LEHR3;BI;;RAUM3;2;4; 4;1;05a;
	//	564;36;Stunde;LEHR3;BI;;RAUM3;2;4; 4;2;05a;
	private void importiereKlassenOderKursunterricht(final StundenplanManager m, final List<StupasSchulmanagerFormatLine> list) {
		final int wochenstunden = getWochenStunden(list);
		final StupasSchulmanagerFormatLine line = list.get(0);
		final List<StundenplanKlasse> klassen = getCreateKlassen(line, m);
		final StundenplanLehrer lehrer = getCreateLehrer(line, m);
		final StundenplanSchiene schiene = getCreateSchiene(line, m);
		final StundenplanFach fach = getCreateFach(line, m);

		if ((klassen.size() == 1) && !getIstOberstufe(klassen.get(0).kuerzel) && !getIstAuffangklasse(klassen.get(0).kuerzel) && !getIstAG(klassen.get(0).kuerzel) && !getIstMIA(klassen.get(0).kuerzel)) {
			// Klassenunterricht (genau eine Klasse und keine Oberstufe)
			final StundenplanKlassenunterricht klassenunterricht = new StundenplanKlassenunterricht();
			klassenunterricht.idKlasse = klassen.get(0).id;
			klassenunterricht.idFach = fach.id;
			klassenunterricht.bezeichnung = "";
			klassenunterricht.wochenstunden = wochenstunden;
			if (schiene != null)
				klassenunterricht.schienen.add(schiene.id);
			klassenunterricht.schueler = new ArrayList<>(); // TODO Einen Schüler pro Klasse simulieren.
			if (lehrer != null)
				klassenunterricht.lehrer.add(lehrer.id);
			m.klassenunterrichtAdd(klassenunterricht);
		} else {
			// Kursunterricht
			final StundenplanKurs kurs = new StundenplanKurs();
			kurs.id = line.KursId;
			kurs.idFach = fach.id;
			kurs.bezeichnung = "";
			kurs.wochenstunden = wochenstunden;
			kurs.sortierung = 0;
			kurs.schienen = new ArrayList<>();
			kurs.jahrgaenge = new ArrayList<>();
			kurs.schueler = new ArrayList<>();
			kurs.lehrer = new ArrayList<>();
		}

		// Rekursiv importieren.
		for (final StupasSchulmanagerFormatLine lineUnterricht : list)
			importiereUnterrichtWochentagStundeWochentyp(m, lineUnterricht);
	}

	private void importiereUnterrichtWochentagStundeWochentyp(final StundenplanManager m, final StupasSchulmanagerFormatLine line) {
		final StundenplanFach fach = getCreateFach(line, m); // muss
		final StundenplanZeitraster zeitraster = getCreateZeitraster(line, m); // muss
		final StundenplanRaum raum = getCreateRaum(line, m); // kann
		final StundenplanSchiene schiene = getCreateSchiene(line, m); // kann
		final StundenplanLehrer lehrer = getCreateLehrer(line, m); // kann
		final List<StundenplanKlasse> klassen = getCreateKlassen(line, m); // kann
		if ((lehrer == null) && (klassen.size() == 0)) // Mindestens Lehrkraft ODER Klasse
			return; // TODO Exception?

		// Unterricht erzeugen und hinzufügen.
		final StundenplanUnterricht u = new StundenplanUnterricht();
		u.id = unterricht_by_id.size();
		u.idZeitraster = zeitraster.id;
		u.wochentyp = line.Woche;
		u.idFach = fach.id;
		if (raum != null)
			u.raeume.add(raum.id);
		if (lehrer != null)
			u.lehrer.add(lehrer.id);
		if (schiene != null)
			u.schienen.add(schiene.id);
		for (final StundenplanKlasse klasse : klassen)
			u.klassen.add(klasse.id);
		unterricht_by_id.put(u.id, u);
		m.unterrichtAdd(u);
	}

	private static int getWochenTypModell(final List<StupasSchulmanagerFormatLine> csvData) {
		int max = 1;
		for (final StupasSchulmanagerFormatLine line : csvData)
			max = Math.max(max, line.Woche);
		return max == 1 ? 0 : max;
	}

	private int getWochenStunden(final List<StupasSchulmanagerFormatLine> list) {
		int summe = 0;
		final int faktor = komplett.daten.wochenTypModell == 0 ? 1 : komplett.daten.wochenTypModell;

		for (final StupasSchulmanagerFormatLine line : list)
			summe += line.Woche == 0 ? faktor : 1;

		if (summe % faktor != 0)
			throw new DeveloperNotificationException("Die CSV Datei hat keine glatten Wochenstunden: " + linesToString(list));

		return summe / faktor;
	}

	private static String convertNull(final String s) {
		return s == null ? "" : s;
	}

	private static String convertNull(final int id) {
		return "" + id;
	}

	private StundenplanSchiene getCreateSchiene(final StupasSchulmanagerFormatLine line, final StundenplanManager m) {
		if (line.Kopplung == null)
			return null;

		if (!schiene_by_kuerzel.containsKey(line.Kopplung)) {
			final StundenplanSchiene schiene = new StundenplanSchiene();
			schiene.id = schiene_by_kuerzel.size();
			schiene.bezeichnung = line.Kopplung;
			schiene.nummer = schiene_by_kuerzel.size() + 1;
			schiene.idJahrgang = getCreateJahrgangOfLine(line, m).id;
			schiene_by_kuerzel.put(line.Kopplung, schiene);
			m.schieneAdd(schiene);
		}

		return schiene_by_kuerzel.get(line.Kopplung);
	}

	private StundenplanZeitraster getCreateZeitraster(final StupasSchulmanagerFormatLine line, final StundenplanManager m) {

		if (!zeiraster_by_wochentag_and_stunde.contains(line.Wochentag, line.Stunde)) {
			final StundenplanZeitraster zeitraster = new StundenplanZeitraster();
			zeitraster.id = zeiraster_by_wochentag_and_stunde.size();
			zeitraster.wochentag = line.Wochentag;
			zeitraster.unterrichtstunde = line.Stunde;
			zeitraster.stundenbeginn = 8 * 60 + (line.Stunde - 1) * 45;
			if (line.Stunde >= 3) zeitraster.stundenbeginn += 25;
			if (line.Stunde >= 5) zeitraster.stundenbeginn += 25;
			if (line.Stunde >= 7) zeitraster.stundenbeginn += 20;
			zeitraster.stundenende   = zeitraster.stundenbeginn + 45;
			zeiraster_by_wochentag_and_stunde.put(line.Wochentag, line.Stunde, zeitraster);
			m.zeitrasterAdd(zeitraster);
		}

		return zeiraster_by_wochentag_and_stunde.getNonNullOrException(line.Wochentag, line.Stunde);
	}

	private StundenplanLehrer getCreateLehrer(final StupasSchulmanagerFormatLine line, final StundenplanManager m) {
		if (line.Lehrerkuerzel == null)
			return null;

		if (!lehrer_by_kuerzel.containsKey(line.Lehrerkuerzel)) {
			final StundenplanLehrer lehrkraft = new StundenplanLehrer();
			lehrkraft.id = lehrer_by_kuerzel.size() + 1; // Lasse Lehrer-IDs bei 1 starten.
			lehrkraft.kuerzel = line.Lehrerkuerzel;
			lehrkraft.nachname = "Nachname " + line.Lehrerkuerzel;
			lehrkraft.vorname = "Vorname " + line.Lehrerkuerzel;
			lehrer_by_kuerzel.put(line.Lehrerkuerzel, lehrkraft);
			m.lehrerAdd(lehrkraft);
		}

		return lehrer_by_kuerzel.get(line.Lehrerkuerzel);
	}

	private StundenplanFach getCreateFach(final StupasSchulmanagerFormatLine line, final StundenplanManager m) {
		if (line.Fach == null)
			return null;

		if (!fach_by_kuerzel.containsKey(line.Fach)) {
			final StundenplanFach fach = new StundenplanFach();
			fach.id = fach_by_kuerzel.size();
			fach.kuerzel = line.Fach;
			fach_by_kuerzel.put(line.Fach, fach);
			m.fachAdd(fach);
		}

		return fach_by_kuerzel.get(line.Fach);
	}

	private StundenplanRaum getCreateRaum(final StupasSchulmanagerFormatLine line, final StundenplanManager m) {
		if (line.Raum == null)
			return null;

		if (!raum_by_kuerzel.containsKey(line.Raum)) {
			final StundenplanRaum raum = new StundenplanRaum();
			raum.id = raum_by_kuerzel.size();
			raum.kuerzel = line.Raum;
			raum.groesse = 34;
			raum_by_kuerzel.put(line.Raum, raum);
			m.raumAdd(raum);
		}

		return raum_by_kuerzel.get(line.Raum);
	}

	private List<StundenplanKlasse> getCreateKlassen(final StupasSchulmanagerFormatLine line, final StundenplanManager m) {
		final List<StundenplanKlasse> list = new ArrayList<>();

		if (line.Klassen == null)
			return list;

		for (final String klasseKuerzel : line.Klassen.split(",")) {
			if (!klasse_by_kuerzel.containsKey(klasseKuerzel)) {
				final StundenplanKlasse klasse = new StundenplanKlasse();
				klasse.id = klasse_by_kuerzel.size();
				klasse.kuerzel = klasseKuerzel;
				klasse_by_kuerzel.put(klasseKuerzel, klasse);

				// Potentieller Jahrgang der Klasse
				final StundenplanJahrgang jahrgang = getCreateJahrgang(klasseKuerzel, m);
				if (jahrgang != null)
					klasse.jahrgaenge.add(jahrgang.id);

				m.klasseAdd(klasse);
			}

			list.add(klasse_by_kuerzel.get(klasseKuerzel));
		}

		return list;
	}

	private StundenplanJahrgang getCreateJahrgangOfLine(final StupasSchulmanagerFormatLine line, final StundenplanManager m) {
		if (line.Klassen == null)
			return null;

		final String[] split = line.Klassen.split(",");
		final StundenplanJahrgang jahrgang = getCreateJahrgang(split[0], m);

		for (int i = 1; i < split.length; i++)
			if (jahrgang != getCreateJahrgang(split[i], m))
				throw new DeveloperNotificationException("Mehrere Jahrgänge: " + lineToString(line));

		return jahrgang;
	}

	private StundenplanJahrgang getCreateJahrgang(final String klassenKuerzel, final StundenplanManager m) {
		if (klassenKuerzel.length() <= 1)
			return null;

		String jahrgangKuerzel = null;

		if (getIstOberstufe(klassenKuerzel)) {
			// Oberstufe
			jahrgangKuerzel = klassenKuerzel;
		} else {
			// Klassenverband (extrahiere 7 aus 7a oder ggf. 07a)
			final String kuerzel2 = klassenKuerzel.substring(0, klassenKuerzel.length() - 1);
			try {
				Integer.parseInt(kuerzel2);
				jahrgangKuerzel = kuerzel2;
			} catch (@SuppressWarnings("unused") final Exception ex) {
				// nichts: handle exception
			}
		}

		if (jahrgangKuerzel == null)
			return null;

		if (!jahrgang_by_kuerzel.containsKey(jahrgangKuerzel)) {
			final StundenplanJahrgang jahrgang = new StundenplanJahrgang();
			jahrgang.id = jahrgang_by_kuerzel.size();
			jahrgang.kuerzel = jahrgangKuerzel;
			jahrgang_by_kuerzel.put(jahrgangKuerzel, jahrgang);
			m.jahrgangAdd(jahrgang);
		}

		return jahrgang_by_kuerzel.get(jahrgangKuerzel);
	}

	private static boolean getIstOberstufe(final String klassenKuerzel) {
		final String kuerzel = klassenKuerzel.toUpperCase();
		return kuerzel.equals("EF") || kuerzel.equals("Q1") || kuerzel.equals("Q2");
	}

	private static boolean getIstAuffangklasse(final String klassenKuerzel) {
		final String kuerzel = klassenKuerzel.toUpperCase();
		return kuerzel.equals("AUF") || kuerzel.equals("AUF1") || kuerzel.equals("AUF2");
	}

	private static boolean getIstAG(@NotNull final String klassenKuerzel) {
		final String kuerzel = klassenKuerzel.toUpperCase();
		return kuerzel.equals("AG") || kuerzel.equals("AG1") || kuerzel.equals("AG2");
	}

	private static boolean getIstMIA(@NotNull final String klassenKuerzel) {
		final String kuerzel = klassenKuerzel.toUpperCase();
		return kuerzel.equals("MIA") || kuerzel.equals("MIA1") || kuerzel.equals("MIA2");
	}

	private static String lineToString(final StupasSchulmanagerFormatLine line) {
		return convertNull(line.Id) + ";" + convertNull(line.KursId) + ";" + convertNull(line.Art) + ";"
	          + convertNull(line.Lehrerkuerzel) + ";" + convertNull(line.Fach) + ";" + convertNull(line.Kurs) + ";"
			  + convertNull(line.Raum) + ";" + convertNull(line.Wochentag) + ";" + convertNull(line.Stunde) + ";"
	          + convertNull(line.Bezeichnung) + ";" + convertNull(line.Woche) + ";" + convertNull(line.Klassen) + ";"
			  + convertNull(line.Kopplung);
	}

	private static String linesToString(final List<StupasSchulmanagerFormatLine> list) {
		String s = "";
		for (final StupasSchulmanagerFormatLine line : list)
			s += "\n" + lineToString(line);
		return s;
	}

}
