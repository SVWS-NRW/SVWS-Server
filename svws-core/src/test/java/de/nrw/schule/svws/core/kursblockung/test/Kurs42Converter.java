package de.nrw.schule.svws.core.kursblockung.test;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

import de.nrw.schule.svws.core.data.kursblockung.KursblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFach;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFachwahl;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKursart;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputRegel;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputSchueler;
import de.nrw.schule.svws.core.kursblockung.KursblockungException;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Eine Klasse zum Einlesen von exportierten Kurs42-Textdateien mit direkter Umwandlung in das Eingabeobjekt
 * {@linkplain KursblockungInput} für die Kursblockung.
 * 
 * @author Benjamin A. Bartsch */
public class Kurs42Converter {

	/** Der Logger für Warnungen und Fehlermeldungen. */
	private final Logger logger;

	/** Die konvertierten Daten. Diese können an einen Blockungsalgorithmus weitergereicht werden. */
	private final KursblockungInput ki;

	/** Die Anzahl der Schüler in den Daten. */
	private long schuelerSumme;

	/** Die Anzahl der Kurse in den Daten. */
	private long kursSumme;

	/** Die Anzahl der Fächer (z.B. 'D', 'E', 'M', ...) in den Daten. */
	private long fachSumme;

	/** Die Anzahl der Kursarten (z.B. 'GK', 'LK', 'PJK') in den Daten. */
	private long kursartSumme;

	/** Die Anzahl der Fachwahlen aller SuS in den Daten. */
	private long fachWahlSumme;

	/** Der Konstruktor wandelt einen Pfad {@code location} mit exportierten Kurs42-Textdateien in ein Objekt der Klasse
	 * {@linkplain KursblockungInput} um. Es werden die folgenden Dateien eingelesen:<br>
	 * {@code 'Schueler.txt', 'Kurse.txt', 'Fachwahlen.txt' und 'Blockplan.txt'}
	 * 
	 * @param logger           Der logger.
	 * @param location         Der Pfad der Kurs42-Exportdateien.
	 * @param maxTimeMillis    Die maximale Blockungszeit in Millisekunden.
	 * @param fixiereAlleKurse Falls true, dann wird die aktuelle Kurslage fixiert. */
	public Kurs42Converter(Logger logger, String location, long maxTimeMillis, boolean fixiereAlleKurse) {
		this.logger = logger;

		this.ki = new KursblockungInput();
		this.ki.input = 1; // Pseudo-ID (kommt eigentlich aus der Datenbank)
		this.ki.maxTimeMillis = maxTimeMillis;
		this.ki.maxSchienen = 0; // wird noch erhöht
		this.ki.schueler = new Vector<>();
		this.ki.faecher = new Vector<>();
		this.ki.kursarten = new Vector<>();
		this.ki.kurse = new Vector<>();
		this.ki.fachwahlen = new Vector<>();
		this.ki.regeln = new Vector<>();

		this.schuelerSumme = 0;
		this.kursSumme = 0;
		this.fachSumme = 0;
		this.kursartSumme = 0;
		this.fachWahlSumme = 0;

		HashMap<String, KursblockungInputSchueler> mapSchueler = new HashMap<>();
		HashMap<String, KursblockungInputKurs> mapKurse = new HashMap<>();
		HashMap<String, KursblockungInputFach> mapFaecher = new HashMap<>();
		HashMap<String, KursblockungInputKursart> mapKursarten = new HashMap<>();

		// Einlesen der Schüler-Objekte
		for (Kurs42DataSchueler k42schueler : CsvReader.fromResource(location + "Schueler.txt",
				Kurs42DataSchueler.class)) {
			// Doppelter Schülername?
			String sKey = getKeySchueler(k42schueler);
			if (mapSchueler.containsKey(sKey))
				throw fehler("Kurs42-Schueler-Inkonsistenz: Schüler '" + sKey + "' existiert doppelt.");
			// Neuen Schüler erzeugen, dem Map und Vector hinzufügen.
			KursblockungInputSchueler kiSchueler = new KursblockungInputSchueler();
			kiSchueler.id = schuelerSumme;
			kiSchueler.representation = sKey;
			mapSchueler.put(sKey, kiSchueler); // Dem Map hinzufügen.
			ki.schueler.add(kiSchueler); // Dem Vector hinzufügen.
			schuelerSumme++;
		}

		// Einlesen der Kurs-Objekte & Fächer & Kursarten
		for (Kurs42DataKurs k42kurs : CsvReader.fromResource(location + "Kurse.txt", Kurs42DataKurs.class)) {
			// Doppelter Kursname?
			String sKursname = k42kurs.Name;
			if (mapKurse.containsKey(sKursname))
				throw fehler("Kurs42-Kurse-Inkonsistenz: Kurs '" + sKursname + "' existiert doppelt.");
			// Neues Fach? --> Den Map und Vector hinzufügen.
			String sFachKuerzel = k42kurs.Fach;
			if (!mapFaecher.containsKey(sFachKuerzel)) {
				KursblockungInputFach kiFach = new KursblockungInputFach();
				kiFach.id = fachSumme;
				kiFach.representation = sFachKuerzel;
				mapFaecher.put(sFachKuerzel, kiFach); // Dem Map hinzufügen.
				ki.faecher.add(kiFach); // Dem Vector hinzufügen.
				fachSumme++;
			}
			// Neue Kurs art? --> Dem Map und Vector hinzufügen.
			String sKursartKuerzel = convertKursart(k42kurs.Kursart);
			if (!mapKursarten.containsKey(sKursartKuerzel)) {
				KursblockungInputKursart kiKursart = new KursblockungInputKursart();
				kiKursart.id = kursartSumme;
				kiKursart.representation = sKursartKuerzel;
				mapKursarten.put(sKursartKuerzel, kiKursart); // Dem Map hinzufügen.
				ki.kursarten.add(kiKursart); // Dem Vector hinzufügen.
				kursartSumme++;
			}
			// Neuen Kurs erzeugen. Dem Map und Vector hinzufügen.
			KursblockungInputKurs kiKurs = new KursblockungInputKurs();
			kiKurs.fach = mapFaecher.get(sFachKuerzel).id;
			kiKurs.kursart = mapKursarten.get(sKursartKuerzel).id;
			kiKurs.id = kursSumme;
			kiKurs.representation = sKursname;
			kiKurs.schienen = k42kurs.Schienenzahl;
			mapKurse.put(sKursname, kiKurs); // Dem Map hinzufügen.
			ki.kurse.add(kiKurs); // Dem Vector hinzufügen.
			kursSumme++;
		}

		// Einlesen der Fachwahl-Objekte
		TreeSet<String> schuelerFachwahlen = new TreeSet<>();
		for (Kurs42DataFachwahl k42fachwahl : CsvReader.fromResource(location + "Fachwahlen.txt",
				Kurs42DataFachwahl.class)) {
			// Schüler unbekannt?
			String sSchueler = getKeySchueler(k42fachwahl);
			if (!mapSchueler.containsKey(sSchueler))
				throw fehler("Kurs42-Fachwahlen-Inkonsistenz: Schüler '" + sSchueler + "' unbekannt!");
			// Neues Fach? --> Dem Map und Vector hinzufügen.
			String sFachKuerzel = k42fachwahl.Fachkrz;
			if (!mapFaecher.containsKey(sFachKuerzel)) {
				KursblockungInputFach kiFach = new KursblockungInputFach();
				kiFach.id = fachSumme;
				kiFach.representation = sFachKuerzel;
				mapFaecher.put(sFachKuerzel, kiFach); // Dem Map hinzufügen.
				ki.faecher.add(kiFach); // Dem Vector hinzufügen.
				fachSumme++;
			}
			// Neue Kursart? --> Dem Map und Vector hinzufügen.
			String sKursartKuerzel = convertKursart(k42fachwahl.Kursart);
			if (!mapKursarten.containsKey(sKursartKuerzel)) {
				KursblockungInputKursart kiKursart = new KursblockungInputKursart();
				kiKursart.id = kursartSumme;
				kiKursart.representation = sKursartKuerzel;
				mapKursarten.put(sKursartKuerzel, kiKursart); // Dem Map hinzufügen.
				ki.kursarten.add(kiKursart); // Dem Vector hinzufügen.
				kursartSumme++;
			}
			// Schüler hat doppelte Fachwahl?
			String sRepresentation = sSchueler + ";" + sFachKuerzel + ";" + sKursartKuerzel;
			if (!schuelerFachwahlen.add(sRepresentation))
				throw fehler("Kurs42-Fachwahlen: Schüler '" + sSchueler + "' hat die Wahl '" + sFachKuerzel + ";"
						+ sKursartKuerzel + "' doppelt!");

			// Neue Fachwahl erzeugen und dem Vector hinzufügen
			KursblockungInputFachwahl kiFachwahl = new KursblockungInputFachwahl();
			kiFachwahl.id = fachWahlSumme;
			kiFachwahl.schueler = mapSchueler.get(sSchueler).id;
			kiFachwahl.fach = mapFaecher.get(sFachKuerzel).id;
			kiFachwahl.kursart = mapKursarten.get(sKursartKuerzel).id;
			kiFachwahl.representation = sRepresentation;
			ki.fachwahlen.add(kiFachwahl);
			fachWahlSumme++;
		}

		// Einlesen der Lage der Kurse. Bestimmung der Schienenanzahl
		for (Kurs42DataBlockplan k42blockplan : CsvReader.fromResource(location + "Blockplan.txt",
				Kurs42DataBlockplan.class)) {
			// Kurs unbekannt?
			String sKursname = k42blockplan.Kursbezeichnung;
			if (!mapKurse.containsKey(sKursname))
				throw fehler("Kurs42-Blockplan-Inkonsistenz: Kurs '" + sKursname + "' existiert nicht in 'Kurse.txt'.");
			// Schienenanzahl erhöhen?
			int schiene = k42blockplan.Schiene;
			ki.maxSchienen = Math.max(ki.maxSchienen, schiene);
			// Regel 2 - Kursfixierung?
			if (fixiereAlleKurse) {
				KursblockungInputRegel regel = new KursblockungInputRegel();
				regel.databaseID = ki.regeln.size();
				regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				regel.daten = new Long[] { mapKurse.get(sKursname).id, (long) (schiene + 1) };
				ki.regeln.add(regel);
			}
		}
		ki.maxSchienen = ki.maxSchienen + 1;

		this.logger.modifyIndent(-4);
	}

	/** Diese Methode liefert das zuvor im Konstruktor erzeugte Objekt vom Typ {@link KursblockungInput}, welches aus
	 * den Kurs42-Textdateien konvertiert wurde.
	 * 
	 * @return Liefert das aus den Kurs42-Textdateien konvertierte Objekt. */
	public KursblockungInput gibKursblockungInput() {
		return ki;
	}

	/** Erzeugt aus den vier Attributen (Name, Vorname, GebDat und Geschlecht) des Objektes {@link Kurs42DataSchueler}
	 * einen eindeutigen Schlüssel.
	 * 
	 * @return Ein eindeutiger Schlüssel um einen Schüler zu identifizieren. */
	private static String getKeySchueler(Kurs42DataSchueler s) {
		return s.Name + ";" + s.Vorname + ";" + s.GebDat + ";" + s.Geschlecht;
	}

	/** Erzeugt aus den vier Attributen (Name, Vorname, GebDat und Geschlecht) des Objektes {@link Kurs42DataFachwahl}
	 * einen eindeutigen Schlüssel.
	 * 
	 * @return Ein eindeutiger Schlüssel um einen Schüler zu identifizieren. */
	private static String getKeySchueler(Kurs42DataFachwahl f) {
		return f.Name + ";" + f.Vorname + ";" + f.GebDat + ";" + f.Geschlecht;
	}

	/** Wandelt ggf. den String {@code kursart} um. LK1 und LK2 werden zu LK umgewandelt und GKM, GKS, AB3 und AB4
	 * werden zu GK umgewandelt. Dies ist nötig, da Kurs42 in der Datei {@codeFachwahlen.txt} die Kursart in dieser Form
	 * speichert.
	 * 
	 * @param  kursart die Kursart
	 * @return         Der String {@code kursart} umgewandelt, z.B. 'LK1' zu 'LK'. */
	static String convertKursart(String kursart) {
		if (kursart.equals("LK1")) {
			return "LK";
		}
		if (kursart.equals("LK2")) {
			return "LK";
		}
		if (kursart.equals("GKM")) {
			return "GK";
		}
		if (kursart.equals("GKS")) {
			return "GK";
		}
		if (kursart.equals("AB3")) {
			return "GK";
		}
		if (kursart.equals("AB4")) {
			return "GK";
		}
		return kursart;
	}

	/** Erzeugt einen Fehler via Exception und Logger.
	 * 
	 * @param fehlermeldung Die Fehlermeldung. */
	private KursblockungException fehler(@NotNull String fehlermeldung) {
		logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new KursblockungException(fehlermeldung);
	}

}
