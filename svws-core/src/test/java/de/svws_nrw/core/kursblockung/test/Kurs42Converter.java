package de.svws_nrw.core.kursblockung.test;

import java.util.HashMap;
import java.util.LinkedList;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.base.kurs42.Kurs42DataBlockplan;
import de.svws_nrw.base.kurs42.Kurs42DataFachwahlen;
import de.svws_nrw.base.kurs42.Kurs42DataKurse;
import de.svws_nrw.base.kurs42.Kurs42DataSchueler;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;

/** Eine Klasse zum Einlesen von exportierten Kurs42-Textdateien mit direkter Umwandlung in das Eingabeobjekt
 * {@linkplain GostBlockungsdatenManager} für die Kursblockung.
 *
 * @author Benjamin A. Bartsch */
public class Kurs42Converter {

	/** Der Logger für Warnungen und Fehlermeldungen. */
	private final Logger _logger;

	/** Die konvertierten Daten. Diese können an einen Blockungsalgorithmus weitergereicht werden. */
	private final GostBlockungsdatenManager _manager;

	/** Der Konstruktor wandelt einen Pfad {@code location} mit exportierten Kurs42-Textdateien in ein Objekt der Klasse
	 * {@linkplain GostBlockungsdatenManager} um. Es werden die folgenden Dateien eingelesen:<br>
	 * {@code 'Schueler.txt', 'Kurse.txt', 'Fachwahlen.txt' und 'Blockplan.txt'}
	 *
	 * @param pLogger           Der logger.
	 * @param pPfad             Der Pfad der Kurs42-Exportdateien.
	 * @param pMaxTimeMillis    Die maximale Blockungszeit in Millisekunden.
	 * @param pFixiereAlleKurse Falls true, dann wird die aktuelle Kurslage fixiert. */
	public Kurs42Converter(final Logger pLogger, final String pPfad, final long pMaxTimeMillis, final boolean pFixiereAlleKurse) {
		this._logger = pLogger;

		final HashMap<String, GostFach> mapFaecher = new HashMap<>();
		final HashMap<String, GostKursart> mapKursarten = new HashMap<>();
		final HashMap<String, GostBlockungKurs> mapKurse = new HashMap<>();
		final HashMap<String, Schueler> mapSchueler = new HashMap<>();
		final HashMap<String, GostFachwahl> mapFachwahlen = new HashMap<>();
		final HashMap<String, LinkedList<GostBlockungKurs>> mapFachart = new HashMap<>();
		final HashMap<Long, GostBlockungRegel> mapRegeln = new HashMap<>();

		// Einlesen der Schüler-Objekte
		for (final Kurs42DataSchueler k42schueler : CsvReader.fromResource(pPfad + "Schueler.txt", Kurs42DataSchueler.class)) {

			// Doppelter Schülername?
			final String sKey = getKeySchueler(k42schueler);
			if (mapSchueler.containsKey(sKey))
				throw new DeveloperNotificationException("Kurs42-Schueler-Inkonsistenz: Schüler (" + sKey + ") existiert doppelt.");

			final Schueler gSchueler = new Schueler();
			gSchueler.id = mapSchueler.size() + 1;
			gSchueler.vorname = k42schueler.Vorname;
			gSchueler.nachname = k42schueler.Name;
			gSchueler.geschlecht = k42schueler.Geschlecht;
			mapSchueler.put(sKey, gSchueler);
		}

		// Einlesen der Kurs-Objekte & Fächer & Kursarten
		for (final Kurs42DataKurse k42kurs : CsvReader.fromResource(pPfad + "Kurse.txt", Kurs42DataKurse.class)) {

			// Doppelter Kursname?
			final String sKursname = k42kurs.Name;
			if (mapKurse.containsKey(sKursname))
				throw new DeveloperNotificationException("Kurs42-Kurse-Inkonsistenz: Kurs '" + sKursname + "' existiert doppelt.");

			// Neues Fach? --> Map
			final String sFachKuerzel = k42kurs.Fach;
			if (!mapFaecher.containsKey(sFachKuerzel)) {
				final GostFach gFach = new GostFach();
				gFach.id = mapFaecher.size();
				gFach.kuerzel = sFachKuerzel;
				mapFaecher.put(sFachKuerzel, gFach);
			}

			// Neue Kursart? --> Map
			final String sKursartKuerzel = convertKursart(k42kurs.Kursart);
			if (!mapKursarten.containsKey(sKursartKuerzel)) {
				final GostKursart gKursart = GostKursart.fromKuerzel(sKursartKuerzel);
				if (gKursart == null)
					throw new DeveloperNotificationException("GostKursart.fromKuerzel(" + sKursartKuerzel + ") == null");
				mapKursarten.put(sKursartKuerzel, gKursart);
			}

			// Neue Fachart? --> Map
			final String sFachart = sFachKuerzel + ";" + sKursartKuerzel;
			if (!mapFachart.containsKey(sFachart))
				mapFachart.put(sFachart, new LinkedList<>());

			// Neuen Kurs erzeugen. Dem Map und ArrayList hinzufügen.
			final GostBlockungKurs gKurs = new GostBlockungKurs();
			mapFachart.get(sFachart).addLast(gKurs);
			gKurs.id = mapKurse.size();
			gKurs.nummer = mapFachart.get(sFachart).size();
			// System.out.println("Kursname "+sKursname+" --> "+gKurs.id);
			gKurs.fach_id = mapFaecher.get(sFachKuerzel).id;
			gKurs.kursart = mapKursarten.get(sKursartKuerzel).id;
			gKurs.anzahlSchienen = k42kurs.Schienenzahl;
			mapKurse.put(sKursname, gKurs);
		}

		// Einlesen der Fachwahl-Objekte
		for (final Kurs42DataFachwahlen k42fachwahl : CsvReader.fromResource(pPfad + "Fachwahlen.txt", Kurs42DataFachwahlen.class)) {
			// Schüler unbekannt?
			final String sSchueler = getKeySchueler(k42fachwahl);
			if (!mapSchueler.containsKey(sSchueler))
				throw new DeveloperNotificationException("Kurs42-Fachwahlen-Inkonsistenz: Schüler (" + sSchueler + ") unbekannt!");

			// Neues Fach? --> Map
			final String sFachKuerzel = k42fachwahl.Fachkrz;
			if (!mapFaecher.containsKey(sFachKuerzel)) {
				final GostFach gFach = new GostFach();
				gFach.id = mapFaecher.size();
				gFach.kuerzel = sFachKuerzel;
				mapFaecher.put(sFachKuerzel, gFach);
			}

			// Neue Kursart? --> Map
			final String sKursartKuerzel = convertKursart(k42fachwahl.Kursart);
			if (!mapKursarten.containsKey(sKursartKuerzel)) {
				final GostKursart gKursart = GostKursart.fromKuerzel(sKursartKuerzel);
				if (gKursart == null)
					throw new NullPointerException("GostKursart.fromKuerzel(" + sKursartKuerzel + ") == null");
				mapKursarten.put(sKursartKuerzel, gKursart);
			}

			// Schüler hat doppelte Fachwahl?
			final String sFachwahl = sSchueler + ";" + sFachKuerzel + ";" + sKursartKuerzel;
			if (mapFachwahlen.containsKey(sFachwahl))
				throw new DeveloperNotificationException("Kurs42-Fachwahlen: Schüler (" + sSchueler + ") hat die Fachwahl (" + sFachKuerzel + ";" + sKursartKuerzel + ") doppelt!");

			// Fachwahl erzeugen
			final GostFachwahl gFachwahl = new GostFachwahl();
			gFachwahl.fachID = mapFaecher.get(sFachKuerzel).id;
			gFachwahl.kursartID = mapKursarten.get(sKursartKuerzel).id;
			gFachwahl.schuelerID = mapSchueler.get(sSchueler).id;
			mapFachwahlen.put(sFachwahl, gFachwahl);
		}

		// Einlesen der Lage der Kurse. Bestimmung der Schienenanzahl
		int schienenAnzahl = 1;
		for (final Kurs42DataBlockplan k42blockplan : CsvReader.fromResource(pPfad + "Blockplan.txt", Kurs42DataBlockplan.class)) {

			// Kurs unbekannt?
			final String sKursname = k42blockplan.Kursbezeichnung;
			if (!mapKurse.containsKey(sKursname))
				throw new DeveloperNotificationException("Kurs42-Blockplan-Inkonsistenz: Kurs (" + sKursname + ") existiert nicht in 'Kurse.txt'.");

			// Schienenanzahl erhöhen?
			final int gSchiene = k42blockplan.Schiene + 1;
			schienenAnzahl = Math.max(schienenAnzahl, gSchiene);

			// Regel 2 - Kursfixierung?
			if (pFixiereAlleKurse) {
				final GostBlockungRegel gRegel = new GostBlockungRegel();
				gRegel.id = mapRegeln.size() + 1;
				gRegel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				gRegel.parameter.add(mapKurse.get(sKursname).id);
				gRegel.parameter.add(Long.valueOf(gSchiene));
				mapRegeln.put(gRegel.id, gRegel);
			}
		}

		// Temporäre Daten --> Manager
		final GostBlockungsdaten gDaten = new GostBlockungsdaten();
		gDaten.id = 1L; // Pseudo-ID
		gDaten.kurse.addAll(mapKurse.values());
		gDaten.regeln.addAll(mapRegeln.values());
		gDaten.schueler.addAll(mapSchueler.values());
		gDaten.fachwahlen.addAll(mapFachwahlen.values());
		for (int i = 1; i <= schienenAnzahl; i++) {
			final GostBlockungSchiene gSchiene = new GostBlockungSchiene();
			gSchiene.id = i; // Pseudo-ID
			gSchiene.nummer = i;
			gSchiene.bezeichnung = "Schiene " + i;
			gDaten.schienen.add(gSchiene);
		}

		final GostFaecherManager fManager = new GostFaecherManager();
		fManager.addAll(mapFaecher.values());

		this._manager = new GostBlockungsdatenManager(gDaten, fManager);
		this._manager.setMaxTimeMillis(pMaxTimeMillis);
		this._manager.setID(1);

		this._logger.modifyIndent(-4);
	}

	/** Diese Methode liefert das zuvor im Konstruktor erzeugte Objekt vom Typ {@link GostBlockungsdatenManager},
	 * welches aus den Kurs42-Textdateien konvertiert wurde.
	 *
	 * @return Liefert das aus den Kurs42-Textdateien konvertierte Objekt. */
	public GostBlockungsdatenManager gibKursblockungInput() {
		return _manager;
	}

	/**
	 * Erzeugt aus den vier Attributen (Name, Vorname, GebDat und Geschlecht) des Objektes {@link Kurs42DataSchueler}
	 * einen eindeutigen Schlüssel.
	 *
	 * @param s   der Schüler
	 *
	 * @return Ein eindeutiger Schlüssel um einen Schüler zu identifizieren.
	 */
	private static String getKeySchueler(final Kurs42DataSchueler s) {
		return s.Name + ";" + s.Vorname + ";" + s.GebDat + ";" + s.Geschlecht;
	}

	/**
	 * Erzeugt aus den vier Attributen (Name, Vorname, GebDat und Geschlecht) des Objektes {@link Kurs42DataFachwahlen}
	 * einen eindeutigen Schlüssel.
	 *
	 * @param f   die Fachwahl
	 *
	 * @return Ein eindeutiger Schlüssel um einen Schüler zu identifizieren.
	 */
	private static String getKeySchueler(final Kurs42DataFachwahlen f) {
		return f.Name + ";" + f.Vorname + ";" + f.GebDat + ";" + f.Geschlecht;
	}

	/** Wandelt ggf. den String {@code kursart} um. LK1 und LK2 werden zu LK umgewandelt und GKM, GKS, AB3 und AB4
	 * werden zu GK umgewandelt. Dies ist nötig, da Kurs42 in der Datei {@codeFachwahlen.txt} die Kursart in dieser Form
	 * speichert.
	 *
	 * @param  kursart die Kursart
	 * @return         Der String {@code kursart} umgewandelt, z.B. 'LK1' zu 'LK'. */
	static String convertKursart(final String kursart) {
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

}
