package de.nrw.schule.svws.core.kursblockung;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import de.nrw.schule.svws.core.adt.collection.LinkedCollection;
import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungRegel;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.logger.LogLevel;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse speichert alle benötigten Daten während des Blockungsvorganges. Primär handelt es sich um die Zuordnung
 * der Kurse auf die Schienen und um die Zuordnung der SuS auf ihre Kurse.
 * 
 * @author Benjamin A. Bartsch
 */
public class KursblockungDynDaten {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger logger;

	/** Alle Regeln nach ihrer ID gruppiert und in einer Liste der Reihenfolge nach gespeichert. */
	private final @NotNull HashMap<@NotNull GostKursblockungRegelTyp, @NotNull LinkedCollection<@NotNull GostBlockungRegel>> regelMap;

	/** Die maximale Blockungszeit in Millisekunden. */
	private long maxTimeMillis;

	/** Diese Datenstruktur speichert die Schienen und ihre Kurse. */
	private @NotNull KursblockungDynSchiene @NotNull [] schienenArr;

	/** Alles Kurse. */
	private @NotNull KursblockungDynKurs @NotNull [] kursArr;

	/** Alle Kurse, die noch über Schienen wandern können. */
	private @NotNull KursblockungDynKurs @NotNull [] kursArrFrei;

	/** Map für schnellen Zugriff auf die Kurse über ihre ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull KursblockungDynKurs> kursMap;

	/** Alle Facharten. Fachart meint Fach + Kursart, z.B. "D;GK". */
	private @NotNull KursblockungDynFachart @NotNull [] fachartArr;

	/** Map für schnellen Zugriff auf die Facharten über FachID und KursartID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Integer, @NotNull KursblockungDynFachart>> fachartMap;

	/** Alle SuS. */
	private @NotNull KursblockungDynSchueler @NotNull [] schuelerArr;

	/** Map für schnellen Zugriff auf die SuS über ihre ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull KursblockungDynSchueler> schuelerMap;

	/** Das Statistik-Objekt speichert die aktuellen Nichtwahlen, Kursdifferenzen und weitere Daten. */
	private final @NotNull KursblockungDynStatistik statistik;

	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link GostBlockungsdatenManager} ein und baut die relevanten
	 * Datenstrukturen auf.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public KursblockungDynDaten(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull GostBlockungsdatenManager pInput) {
		_random = pRandom;
		logger = pLogger;
		regelMap = new HashMap<>();
		maxTimeMillis = pInput.getMaxTimeMillis();

		schienenArr = new KursblockungDynSchiene[0];

		kursArr = new KursblockungDynKurs[0];
		kursArrFrei = new KursblockungDynKurs[0];
		kursMap = new HashMap<>();

		fachartArr = new KursblockungDynFachart[0];
		fachartMap = new HashMap<>();

		schuelerArr = new KursblockungDynSchueler[0];
		schuelerMap = new HashMap<>();

		statistik = new KursblockungDynStatistik();

		// Definiert: ---
		schritt01FehlerBeiReferenzen(pInput);

		// Definiert: regelMap
		schritt02FehlerBeiRegelGruppierung(pInput.daten().regeln);

		// Definiert: fachartArr
		schritt03FehlerBeiFachartenErstellung(pInput);

		// Definiert: schuelerArr, susMap
		schritt04FehlerBeiSchuelerErstellung(pInput);

		// Definiert: schueler[i].fachartArr
		schritt05FehlerBeiSchuelerFachwahlenErstellung(pInput, schuelerArr);

		// Definiert: statistik
		schritt06FehlerBeiStatistikErstellung(fachartArr, schuelerArr);

		// Definiert: schienenArr
		schritt07FehlerBeiSchienenErzeugung(pInput.getSchienenAnzahl());

		// Benötigt: fachartArr
		// Definiert: kursArr
		schritt08FehlerBeiKursErstellung(pInput);

		// Benötigt: kursArr
		// Definiert: kursArrFrei
		schritt09FehlerBeiKursFreiErstellung(pInput);

		// Benötigt: kursArr
		// Definiert: fachartArr[i].kursArr
		schritt10FehlerBeiFachartKursArrayErstellung(pInput);

		schritt11FehlerBeiRegel_4_oder_5();

		// Zustände Speichern
		aktionZustandSpeichernS();
		aktionZustandSpeichernK();
		aktionZustandSpeichernG();
	} // Ende des Konstruktors

	/**
	 * Leert die Datenstruktur und teilt dem Logger einen Fehler mit.
	 * 
	 * @param pFehlermeldung Die Fehlermeldung.
	 */
	private KursblockungException fehler(@NotNull String pFehlermeldung) {
		regelMap.clear();
		maxTimeMillis = 0;

		schienenArr = new KursblockungDynSchiene[0];

		fachartArr = new KursblockungDynFachart[0];
		fachartMap.clear();

		kursArr = new KursblockungDynKurs[0];
		kursArrFrei = new KursblockungDynKurs[0];
		kursMap.clear();

		schuelerArr = new KursblockungDynSchueler[0];
		schuelerMap.clear();

		statistik.clear();

		logger.logLn(LogLevel.ERROR, pFehlermeldung);
		return new KursblockungException(pFehlermeldung);
	}

	/**
	 * Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 * 
	 * @param pInput Das {@link KursblockungInput}-Objekt von der GUI.
	 */
	private void schritt01FehlerBeiReferenzen(@NotNull GostBlockungsdatenManager pInput) {

		if (pInput == null)
			throw fehler("GostBlockungsdatenManager == null");

		if (pInput.daten() == null)
			throw fehler("GostBlockungsdatenManager.daten() == null");

		if (pInput.daten().fachwahlen == null)
			throw fehler("GostBlockungsdatenManager.daten().fachwahlen == null");

		if (pInput.faecherManager() == null)
			throw fehler("GostBlockungsdatenManager.faecherManager() == null");

		if (pInput.faecherManager().faecher() == null)
			throw fehler("GostBlockungsdatenManager.faecherManager().faecher() == null");

		if (GostKursart.values() == null)
			throw fehler("GostKursart.values() == null");

		if (pInput.daten().kurse == null)
			throw fehler("GostBlockungsdatenManager.daten().kurse == null");

		if (pInput.daten().regeln == null)
			throw fehler("GostBlockungsdatenManager.daten().regeln == null");

		if (pInput.getID() < 0)
			throw fehler("GostBlockungsdatenManager.getID() < 0");

		if (pInput.daten().fachwahlen.size() <= 0)
			throw fehler("GostBlockungsdatenManager.daten().fachwahlen.size() <= 0");

		if (pInput.faecherManager().faecher().size() <= 0)
			throw fehler("GostBlockungsdatenManager.faecherManager().faecher().size() <= 0");

		if (GostKursart.values().length <= 0)
			throw fehler("GostKursart.values().length <= 0");

		if (pInput.daten().kurse.size() <= 0)
			throw fehler("GostBlockungsdatenManager.daten().kurse.size() <= 0");

		int schienenAnzahl = pInput.getSchienenAnzahl();
		if (schienenAnzahl <= 0)
			throw fehler("GostBlockungsdatenManager.getSchienenAnzahl() <= 0");

		HashSet<Integer> usedSchiene = new HashSet<>();
		for (@NotNull GostBlockungSchiene gSchiene : pInput.daten().schienen) {
			if (gSchiene.id < 0)
				throw fehler("GostBlockungSchiene.id < 0");
			if (gSchiene.nummer < 1)
				throw fehler("GostBlockungSchiene.nummer < 1");
			if (gSchiene.nummer > schienenAnzahl)
				throw fehler("GostBlockungSchiene.nummer > schienenAnzahl");
			if (usedSchiene.add(gSchiene.nummer) == false)
				throw fehler("GostBlockungSchiene.nummer existiert doppelt!");
		}

		// #################### KursblockungInputKursart ####################

		@NotNull HashSet<@NotNull Integer> setKursarten = new HashSet<>();
		for (@NotNull GostKursart iKursart : GostKursart.values()) {
			if (iKursart == null)
				throw fehler("GostKursart == null");

			if (iKursart.id < 0)
				throw fehler("GostKursart.id < 0 (" + iKursart.kuerzel + ")");

			if (setKursarten.add(iKursart.id) == false)
				throw fehler("GostKursart.id (" + iKursart.id + ") gibt es doppelt!");

		}

		// #################### KursblockungInputFach ####################

		@NotNull HashSet<@NotNull Long> setFaecher = new HashSet<>();
		for (@NotNull GostFach iFach : pInput.faecherManager().faecher()) {
			if (iFach == null)
				throw fehler("GostFach == null");

			if (iFach.id < 0)
				throw fehler("GostFach.id < 0 (" + iFach.kuerzel + ")");

			if (setFaecher.add(iFach.id) == false)
				throw fehler("GostFach.id (" + iFach.id + ") gibt es doppelt!");
		}

		// #################### KursblockungInputKurs ####################

		@NotNull HashSet<@NotNull Long> setKurse = new HashSet<>();
		for (@NotNull GostBlockungKurs iKurs : pInput.daten().kurse) {
			if (iKurs == null)
				throw fehler("GostBlockungKurs == null");

			if (iKurs.id < 0)
				throw fehler("GostBlockungKurs.id < 0");

			long fachID = iKurs.fach_id;
			if (!setFaecher.contains(fachID))
				throw fehler("GostBlockungKurs (id=" + iKurs.id + "): Unbekannte Fach-ID (" + fachID + ")!");

			int kursartID = iKurs.kursart;
			if (!setKursarten.contains(kursartID))
				throw fehler("GostBlockungKurs (id=" + iKurs.id + "): Unbekannte Kursart-ID (" + kursartID + ")!");

			setKurse.add(iKurs.id);
		}

		// #################### KursblockungInputFachwahl ####################

		@NotNull HashSet<@NotNull Long> setSchueler = new HashSet<>();
		for (@NotNull GostFachwahl iFachwahl : pInput.daten().fachwahlen) {
			if (iFachwahl == null)
				throw fehler("GostFachwahl == null");

			long schuelerID = iFachwahl.schuelerID;
			if (schuelerID < 0)
				throw fehler("GostFachwahl.schuelerID < 0");
			setSchueler.add(schuelerID);

			long fachID = iFachwahl.fachID;
			if (!setFaecher.contains(fachID))
				throw fehler("GostFachwahl: Unbekannte Fach-ID (" + fachID + ")");

			int kursartID = iFachwahl.kursartID;
			if (!setKursarten.contains(kursartID))
				throw fehler("GostFachwahl: Unbekannte Kursart-ID (" + kursartID + ")");
		}

		// #################### KursblockungInputRegel ####################

		for (@NotNull GostBlockungRegel iRegel : pInput.daten().regeln) {
			if (iRegel == null)
				throw fehler("GostBlockungRegel == null");

			if (iRegel.parameter == null)
				throw fehler("GostBlockungRegel.parameter == null");

			for (int i = 0; i < iRegel.parameter.size(); i++)
				if (iRegel.parameter.get(i) == null)
					throw fehler("GostBlockungRegel.parameter.get(" + i + ") == null");

			if (iRegel.id < 0)
				throw fehler("GostBlockungRegel.id < 0");

			@NotNull GostKursblockungRegelTyp gostRegel = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			if (gostRegel == GostKursblockungRegelTyp.UNDEFINIERT)
				throw fehler("GostBlockungRegel.typ (" + iRegel.typ + ") unbekannt.");

			// Regeltyp = 1
			@NotNull Long @NotNull [] daten = iRegel.parameter.toArray(new Long[0]);
			if (gostRegel == GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS) {
				int length = daten.length;
				if (length != 3)
					throw fehler("KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!");

				int kursartID = iRegel.parameter.get(0).intValue();
				if (!setKursarten.contains(kursartID))
					throw fehler("KURSART_SPERRE_SCHIENEN_VON_BIS hat unbekannte Kursart-ID (" + kursartID + ")");

				int von = daten[1].intValue(); // Schiene ist 1-indiziert!
				int bis = daten[2].intValue(); // Schiene ist 1-indiziert!
				if (!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)))
					throw fehler("KURSART_SPERRE_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis
							+ ") ist unlogisch!");
			}

			// Regeltyp = 2
			if (gostRegel == GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE) {
				int length = daten.length;
				if (length != 2)
					throw fehler("KURS_FIXIERE_IN_SCHIENE daten.length=" + length + ", statt 2!");

				long kursID = daten[0];
				if (!setKurse.contains(kursID))
					throw fehler("KURS_FIXIERE_IN_SCHIENE hat unbekannte Kurs-ID (" + kursID + ")");

				int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!
				if (!((schiene >= 1) && (schiene <= schienenAnzahl)))
					throw fehler("KURS_FIXIERE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!");
			}

			// Regeltyp = 3
			if (gostRegel == GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE) {
				int length = daten.length;
				if (length != 2)
					throw fehler("KURS_SPERRE_IN_SCHIENE daten.length=" + length + ", statt 2!");

				long kursID = daten[0];
				if (!setKurse.contains(kursID))
					throw fehler("KURS_SPERRE_IN_SCHIENE hat unbekannte Kurs-ID (" + kursID + ")");

				int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!
				if (!((schiene >= 1) && (schiene <= schienenAnzahl)))
					throw fehler("KURS_SPERRE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!");
			}

			// Regeltyp = 4
			if (gostRegel == GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS) {
				int length = daten.length;
				if (length != 2)
					throw fehler("SCHUELER_FIXIEREN_IN_KURS daten.length=" + length + ", statt 2!");

				long schuelerID = daten[0];
				if (!setSchueler.contains(schuelerID))
					throw fehler("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Schüler-ID (" + schuelerID + ")");

				long kursID = daten[1];
				if (!setKurse.contains(kursID))
					throw fehler("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Kurs-ID (" + kursID + ")");
			}

			// Regeltyp = 5
			if (gostRegel == GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS) {
				int length = daten.length;
				if (length != 2)
					throw fehler("SCHUELER_VERBIETEN_IN_KURS daten.length=" + length + ", statt 2!");

				long schuelerID = daten[0];
				if (!setSchueler.contains(schuelerID))
					throw fehler("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Schüler-ID (" + schuelerID + ")");

				long kursID = daten[1];
				if (!setKurse.contains(kursID))
					throw fehler("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Kurs-ID (" + kursID + ")");
			}

			// Regeltyp = 6
			if (gostRegel == GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS) {
				int length = daten.length;
				if (length != 3)
					throw fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!");

				int kursartID = daten[0].intValue();
				if (!setKursarten.contains(kursartID))
					throw fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS hat unbekannte Kursart-ID (" + kursartID + ")");

				int von = daten[1].intValue(); // Schiene ist 1-indiziert!
				int bis = daten[2].intValue(); // Schiene ist 1-indiziert!
				if (!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)))
					throw fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis
							+ ") ist unlogisch!");
			}

		}

	}

	private void schritt02FehlerBeiRegelGruppierung(@NotNull List<@NotNull GostBlockungRegel> pRegeln) {
		// Regeln nach ID in Listen gruppieren.
		HashSet<Long> regelDatabaseIDs = new HashSet<>();
		for (GostBlockungRegel iRegel : pRegeln) {
			if (iRegel.id < 0)
				throw fehler("GostBlockungRegel.id < 0");

			if (regelDatabaseIDs.add(iRegel.id) == false)
				throw fehler("GostBlockungRegel.id (" + iRegel.id + ") gibt es doppelt!");

			// Passende Liste holen, ggf. eine neue Liste erzeugen.
			@NotNull GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			LinkedCollection<@NotNull GostBlockungRegel> list = regelMap.get(regelTyp);
			if (list == null) {
				list = new LinkedCollection<>();
				regelMap.put(regelTyp, list);
			}

			// Regel der Liste hinten hinzufügen
			list.addLast(iRegel);
		}
	}

	private void schritt03FehlerBeiFachartenErstellung(@NotNull GostBlockungsdatenManager pInput) {
		int nFacharten = 0;

		// Facharten aus Kursen extrahieren.
		int nKurse = pInput.daten().kurse.size();
		for (@NotNull GostBlockungKurs gKurs : pInput.daten().kurse) {
			GostFach fach = pInput.faecherManager().get(gKurs.fach_id);
			if (fach == null)
				throw fehler("GostBlockungKurs (id=" + gKurs.id + ") die Fach-ID ist im Manager unbekannt!");

			GostKursart kursart = GostKursart.fromIDorNull(gKurs.kursart);
			if (kursart == null)
				throw fehler("GostBlockungKurs (id=" + gKurs.id + ") die Kursart-ID ist bei GostKursart unbekannt!");

			HashMap<@NotNull Integer, @NotNull KursblockungDynFachart> kursartMap = fachartMap.get(fach.id);
			if (kursartMap == null) {
				kursartMap = new HashMap<>();
				fachartMap.put(fach.id, kursartMap);
			}

			KursblockungDynFachart dynFachart = kursartMap.get(kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(_random, nFacharten, fach, kursart, statistik);
				kursartMap.put(kursart.id, dynFachart);
				nFacharten++;
			}

			dynFachart.aktionMaxKurseErhoehen();
		}

		// Facharten aus SuS-Fachwahlen extrahieren.
		for (@NotNull GostFachwahl iFachwahl : pInput.daten().fachwahlen) {
			GostFach fach = pInput.faecherManager().get(iFachwahl.fachID);
			if (fach == null)
				throw fehler("GostFachwahl: Die Fach-ID ist im Manager unbekannt!");

			GostKursart kursart = GostKursart.fromIDorNull(iFachwahl.kursartID);
			if (kursart == null)
				throw fehler("GostFachwahl: Die Kursart-ID ist bei GostKursart unbekannt!");

			long schuelerID = iFachwahl.schuelerID;
			if (schuelerID < 0)
				throw fehler("GostFachwahl.schuelerID < 0");

			HashMap<@NotNull Integer, @NotNull KursblockungDynFachart> kursartMap = fachartMap.get(fach.id);
			if (kursartMap == null) {
				kursartMap = new HashMap<>();
				fachartMap.put(fach.id, kursartMap);
			}

			KursblockungDynFachart dynFachart = kursartMap.get(kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(_random, nFacharten, fach, kursart, statistik);
				kursartMap.put(kursart.id, dynFachart);
				nFacharten++;
				logger.logLn(LogLevel.WARNING, "Schüler '" + schuelerID + "' wählt '" + dynFachart.toString()
						+ "', ohne das ein Kurs existiert!");
			}

			dynFachart.aktionMaxSchuelerErhoehen();
		}

		// Keine Facharten? --> Fehler
		if (nFacharten <= 0)
			throw fehler("Die Blockung hat keine Facharten (Fach + Kursart).");

		// fachartMap --> fachartArr
		fachartArr = new KursblockungDynFachart[nFacharten];
		for (@NotNull HashMap<@NotNull Integer, @NotNull KursblockungDynFachart> kursartMap : fachartMap.values())
			for (@NotNull KursblockungDynFachart fachart : kursartMap.values())
				fachartArr[fachart.gibNr()] = fachart;

		// Verteile Kurse verschwunden? --> Fehler
		int kursSumme = 0;
		for (int i = 0; i < fachartArr.length; i++)
			kursSumme += fachartArr[i].gibKurseMax();
		if (kursSumme != nKurse)
			throw fehler("Die Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.");
	}

	private void schritt04FehlerBeiSchuelerErstellung(@NotNull GostBlockungsdatenManager pInput) {
		// Schüler werden aus den Fachwahlen herausgefiltert.
		@NotNull HashSet<@NotNull Long> setSchueler = new HashSet<>();
		for (@NotNull GostFachwahl fachwahl : pInput.daten().fachwahlen)
			setSchueler.add(fachwahl.schuelerID);

		int nSchueler = setSchueler.size();
		int nSchienen = pInput.getSchienenAnzahl();
		int nKurse = pInput.getKursAnzahl();

		int i = 0;
		schuelerArr = new KursblockungDynSchueler[nSchueler];
		for (@NotNull Long schuelerID : setSchueler) {
			long sID = schuelerID;
			@NotNull KursblockungDynSchueler schueler = new KursblockungDynSchueler(_random, sID, statistik, nSchienen,
					nKurse);
			schuelerArr[i] = schueler;
			schuelerMap.put(schuelerID, schueler);
			i++;
		}
	}

	private void schritt05FehlerBeiSchuelerFachwahlenErstellung(@NotNull GostBlockungsdatenManager pInput,
			@NotNull KursblockungDynSchueler @NotNull [] susArr) {

		// Für jeden Schüler eine Liste seiner Facharten (Fach + Kursart) erzeugen.
		@NotNull HashMap<@NotNull KursblockungDynSchueler, @NotNull LinkedCollection<@NotNull KursblockungDynFachart>> mapSchuelerFA = new HashMap<>();
		for (int i = 0; i < susArr.length; i++)
			mapSchuelerFA.put(susArr[i], new LinkedCollection<>());

		// Fachwahl --> Fachart --> In die Liste des Schülers
		for (@NotNull GostFachwahl iFachwahl : pInput.daten().fachwahlen) {
			KursblockungDynSchueler schueler = schuelerMap.get(iFachwahl.schuelerID);
			if (schueler == null)
				throw fehler("GostFachwahl.schueler --> KursblockungDynSchueler (mapping fehlt)!");

			LinkedCollection<@NotNull KursblockungDynFachart> dynFacharten = mapSchuelerFA.get(schueler);
			if (dynFacharten == null)
				throw fehler("schritt05FehlerBeiSchuelerFachwahlenErstellung: dynFacharten == null");

			@NotNull KursblockungDynFachart dynFachart = gibFachart(iFachwahl.fachID, iFachwahl.kursartID);
			dynFacharten.addLast(dynFachart);
		}

		// Schüler und Facharten verknüpfen.
		for (@NotNull KursblockungDynSchueler schueler : susArr) {
			LinkedCollection<@NotNull KursblockungDynFachart> listFA = mapSchuelerFA.get(schueler);
			if (listFA == null)
				throw fehler("mapSchuelerFA.get(schueler) == null (mapping fehlt)!");

			@NotNull KursblockungDynFachart @NotNull [] arrFA = listFA.toArray(new KursblockungDynFachart[0]);
			schueler.aktionSetzeFachartenUndIDs(arrFA);
		}
	}

	private void schritt06FehlerBeiStatistikErstellung(@NotNull KursblockungDynFachart @NotNull [] fachartArr,
			@NotNull KursblockungDynSchueler @NotNull [] susArr) {
		int nFacharten = fachartArr.length;
		@NotNull int @NotNull [][] bewertungMatrixFachart = new int[nFacharten][nFacharten];

		// Zähle zunächst die Fachart-Paare pro Schüler
		for (int i = 0; i < susArr.length; i++) {
			@NotNull KursblockungDynFachart @NotNull [] fa = susArr[i].gibFacharten();
			for (int i1 = 0; i1 < fa.length; i1++) {
				int nr1 = fa[i1].gibNr();
				for (int i2 = i1 + 1; i2 < fa.length; i2++) {
					int nr2 = fa[i2].gibNr();
					bewertungMatrixFachart[nr1][nr2]++;
					bewertungMatrixFachart[nr2][nr1]++;
				}
			}
		}

		// Der Malus ist relativ zur Anzahl an Kursen.
		// Beispiel: 7 SuS haben PH-CH gewählt. PH gibt es 2 Kurse. CH gibt es 1 Kurs.
		// --> Malus = 7 * 1000 / (2 + 1 - 1)
		for (int i1 = 0; i1 < nFacharten; i1++) {
			int kursAnz1 = fachartArr[i1].gibKurseMax();
			int nr1 = fachartArr[i1].gibNr();
			for (int i2 = 0; i2 < nFacharten; i2++) {
				int kursAnz2 = fachartArr[i2].gibKurseMax();
				int nr2 = fachartArr[i2].gibNr();
				if ((kursAnz1 == 0) || (kursAnz2 == 0)) {
					bewertungMatrixFachart[nr1][nr2] = 0;
					continue;
				}
				int nenner = (kursAnz1 + kursAnz2 - 2);
				int faktor = nenner == 0 ? 1000000 : (100 / nenner);
				bewertungMatrixFachart[nr1][nr2] *= faktor;
			}
			// Gleiche Kurs-Arten in einer Schiene --> großer Malus
			bewertungMatrixFachart[nr1][nr1] += 10000000;
		}

		statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length);
	}

	private void schritt07FehlerBeiSchienenErzeugung(int pSchienen) {
		schienenArr = new KursblockungDynSchiene[pSchienen];
		for (int nr = 0; nr < pSchienen; nr++)
			schienenArr[nr] = new KursblockungDynSchiene(logger, nr, statistik);
	}

	private void schritt08FehlerBeiKursErstellung(@NotNull GostBlockungsdatenManager pInput) {
		int nKurse = pInput.getKursAnzahl();
		int nSchienen = pInput.getSchienenAnzahl();

		// Jedem Kurs zwei Listen (von Schienen) zuordnen.
		@NotNull HashMap<@NotNull Long, @NotNull LinkedCollection<@NotNull KursblockungDynSchiene>> mapKursSchieneFrei = new HashMap<>();
		@NotNull HashMap<@NotNull Long, @NotNull LinkedCollection<@NotNull KursblockungDynSchiene>> mapKursSchieneLage = new HashMap<>();
		kursArr = new KursblockungDynKurs[nKurse];
		int i = 0;
		for (@NotNull GostBlockungKurs kurs : pInput.daten().kurse) {
			// 'Lage' ist zunächst leer.
			@NotNull LinkedCollection<@NotNull KursblockungDynSchiene> schieneLage = new LinkedCollection<>();
			mapKursSchieneLage.put(kurs.id, schieneLage);

			// 'Frei' beinhaltet zunächst alle Schienen permutiert.
			@NotNull LinkedCollection<@NotNull KursblockungDynSchiene> schieneFrei = new LinkedCollection<>();
			mapKursSchieneFrei.put(kurs.id, schieneFrei);
			@NotNull int[] perm = KursblockungStatic.gibPermutation(_random, nSchienen);
			for (int j = 0; j < nSchienen; j++)
				schieneFrei.addLast(schienenArr[perm[j]]);

			// Regel 1 - Alle Kurse einer bestimmten Kursart sperren.
			LinkedCollection<@NotNull GostBlockungRegel> regelnTyp1 = regelMap
					.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS);
			if (regelnTyp1 != null)
				for (@NotNull GostBlockungRegel regel1 : regelnTyp1)
					if (kurs.kursart == regel1.parameter.get(0)) {
						int von = regel1.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
						int bis = regel1.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
						for (int schiene = von; schiene <= bis; schiene++)
							schieneFrei.remove(schienenArr[schiene - 1]); // Intern 0-indiziert!
					}

			// Regel 6 - Alle Kurse die NICHT eine bestimmte Kursart haben in bestimmten Schienen sperren.
			// Regel 6 ist wie Regel 1 nur statt "==" haben wir "!=".
			LinkedCollection<@NotNull GostBlockungRegel> regelnTyp6 = regelMap
					.get(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
			if (regelnTyp6 != null)
				for (@NotNull GostBlockungRegel regel6 : regelnTyp6)
					if (kurs.kursart != regel6.parameter.get(0)) {
						int von = regel6.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
						int bis = regel6.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
						for (int schiene = von; schiene <= bis; schiene++)
							schieneFrei.remove(schienenArr[schiene - 1]); // Intern 0-indiziert!
					}

			// Regel 3 - Pro Kurs gesperrte Schienen entfernen.
			LinkedCollection<@NotNull GostBlockungRegel> regelnTyp3 = regelMap
					.get(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE);
			if (regelnTyp3 != null)
				for (@NotNull GostBlockungRegel regel3 : regelnTyp3)
					if (kurs.id == regel3.parameter.get(0)) {
						int schiene = regel3.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
						schieneFrei.remove(schienenArr[schiene - 1]); // Intern 0-indiziert!
					}

			// Regel 2 - Pro Kurs fixierte Schiene in "Lage" hinzufügen.
			LinkedCollection<@NotNull GostBlockungRegel> regelnTyp2 = regelMap
					.get(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE);
			if (regelnTyp2 != null)
				for (@NotNull GostBlockungRegel regel2 : regelnTyp2)
					if (kurs.id == regel2.parameter.get(0)) {
						int schiene = regel2.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
						@NotNull KursblockungDynSchiene dynSchiene = schienenArr[schiene - 1]; // Intern 0-indiziert!
						if (schieneLage.contains(dynSchiene))
							continue; // Doppeltfixierungen ignorieren
						if (!schieneFrei.contains(dynSchiene)) // Intern 0-indiziert!
							throw fehler("KURS_FIXIERE_IN_SCHIENE: Kurs (id=" + kurs.id + ") will Schiene (" + schiene
									+ ") fixieren, die Schiene wurde aber bereits gesperrt!");
						schieneFrei.remove(dynSchiene);
						schieneLage.addLast(dynSchiene);
					}

			// Fehler: Kurs belegt zu wenig Schienen.
			int schienen = kurs.anzahlSchienen;
			if (schienen <= 0)
				throw fehler("Kurs (id=" + kurs.id + ") belegt nur " + schienen + " Schienen, das ist zu wenig.");

			// Fehler: Kurs belegt zu viele Schienen.
			if (schienen > schienenArr.length)
				throw fehler("Es gibt " + schienenArr.length + " Schienen, aber der Kurs (id=" + kurs.id + ") möchte "
						+ schienen + " Schienen belegen.");

			// Fehler: Zu viel fixiert?
			int pSchienenLageFixiert = schieneLage.size();
			if (pSchienenLageFixiert > schienen)
				throw fehler("Kurs (" + kurs.id + ") fixert " + pSchienenLageFixiert
						+ " Schienen, das ist mehr als seine Schienenanzahl " + schienen + " .");

			// ListFrei ---> ListLage --> Fehler: Zu viel gesperrt?
			while (schieneLage.size() < schienen) {
				if (schieneFrei.isEmpty())
					throw fehler("Kurs (" + kurs.id + ") hat zu viele Schienen gesperrt,"
							+ " so dass seine Schienenanzahl nicht erfüllt werden kann!");
				schieneLage.addLast(schieneFrei.pollFirst());
			}

			// List --> Array
			@NotNull KursblockungDynSchiene @NotNull [] pSchienenLage = schieneLage
					.toArray(new KursblockungDynSchiene[0]);
			@NotNull KursblockungDynSchiene @NotNull [] pSchienenFrei = schieneFrei
					.toArray(new KursblockungDynSchiene[0]);
			@NotNull KursblockungDynFachart dynFachart = gibFachart(kurs.fach_id, kurs.kursart);
			@NotNull KursblockungDynKurs dynKurs = new KursblockungDynKurs(_random, pSchienenLage, pSchienenLageFixiert,
					pSchienenFrei, kurs.id, dynFachart, logger, i);

			// Kurs --> Array
			kursArr[i] = dynKurs;

			// Kurs --> Map
			kursMap.put(kurs.id, dynKurs);

			// Nächster Kurs...
			i++;
		}

	}

	private void schritt09FehlerBeiKursFreiErstellung(@NotNull GostBlockungsdatenManager pInput) {
		// Zähle Kurse mit Freiheitsgraden.
		int nKursFrei = 0;
		for (KursblockungDynKurs kurs : kursArr)
			if (kurs.gibHatFreiheitsgrade())
				nKursFrei++;

		// Kopiere Kurse mit Freiheitsgraden.
		kursArrFrei = new KursblockungDynKurs[nKursFrei];
		for (int i = 0, j = 0; i < kursArr.length; i++)
			if (kursArr[i].gibHatFreiheitsgrade()) {
				kursArrFrei[j] = kursArr[i];
				j++;
			}
	}

	private void schritt10FehlerBeiFachartKursArrayErstellung(@NotNull GostBlockungsdatenManager pInput) {
		// Pro Fachart eine Liste zum Speichern aller zugehörigen Kurse.
		int nFacharten = fachartArr.length;
		@NotNull HashMap<@NotNull Integer, @NotNull LinkedCollection<@NotNull KursblockungDynKurs>> mapFachartList = new HashMap<>();
		for (int i = 0; i < nFacharten; i++)
			mapFachartList.put(i, new LinkedCollection<>());

		// Kurse sammeln...
		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			int fachartNr = kurs.gibFachart().gibNr();
			LinkedCollection<@NotNull KursblockungDynKurs> fachartKurse = mapFachartList.get(fachartNr);
			if (fachartKurse == null)
				throw fehler("mapFachartList.get(fachartNr) == null");
			fachartKurse.addLast(kurs);
		}

		// Pro Fachart: Liste zu Array konvertieren und übergeben.
		for (int nr = 0; nr < nFacharten; nr++) {
			LinkedCollection<@NotNull KursblockungDynKurs> list = mapFachartList.get(nr);
			if (list == null)
				throw fehler("mapFachartList.get(nr) == null");
			@NotNull KursblockungDynKurs @NotNull [] kursArr = list.toArray(new KursblockungDynKurs[0]);
			fachartArr[nr].aktionSetKurse(kursArr);
		}

	}

	private void schritt11FehlerBeiRegel_4_oder_5() {

		// Regel 4 - SCHUELER_FIXIEREN_IN_KURS
		LinkedCollection<@NotNull GostBlockungRegel> regelnTyp4 = regelMap
				.get(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS);
		if (regelnTyp4 != null)
			for (@NotNull GostBlockungRegel regel4 : regelnTyp4) {
				long schuelerID = regel4.parameter.get(0);
				long kursID = regel4.parameter.get(1);
				@NotNull KursblockungDynSchueler schueler = gibSchueler(schuelerID);
				@NotNull KursblockungDynKurs fixierterKurs = gibKurs(kursID);
				// Alle anderen Kurse der selben Fachart verbieten ...
				for (@NotNull KursblockungDynKurs kurs : fixierterKurs.gibFachart().gibKurse())
					if (kurs != fixierterKurs)
						schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
			}

		// Regel 5 - SCHUELER_VERBIETEN_IN_KURS
		LinkedCollection<@NotNull GostBlockungRegel> regelnTyp5 = regelMap
				.get(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS);
		if (regelnTyp5 != null) {
			for (@NotNull GostBlockungRegel regel5 : regelnTyp5) {
				long schuelerID = regel5.parameter.get(0);
				long kursID = regel5.parameter.get(1);
				@NotNull KursblockungDynSchueler schueler = gibSchueler(schuelerID);
				@NotNull KursblockungDynKurs verbotenerKurs = gibKurs(kursID);
				// Kurs verbieten
				schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
			}
		}

	}

	private @NotNull KursblockungDynFachart gibFachart(long pFachID, int pKursart) {
		HashMap<@NotNull Integer, @NotNull KursblockungDynFachart> kursartMap = fachartMap.get(pFachID);
		if (kursartMap == null)
			throw fehler("gibFachart(" + pFachID + ", " + pKursart + ") schlug fehl (Fach)!");

		KursblockungDynFachart dynFachart = kursartMap.get(pKursart);
		if (dynFachart == null)
			throw fehler("gibFachart(" + pFachID + ", " + pKursart + ") schlug fehl (Kursart)!");

		return dynFachart;
	}

	private @NotNull KursblockungDynSchueler gibSchueler(long schuelerID) {
		KursblockungDynSchueler schueler = schuelerMap.get(schuelerID);
		if (schueler == null)
			throw fehler("schuelerMap.get(" + schuelerID + ") --> kein Mapping!");
		return schueler;
	}

	private @NotNull KursblockungDynKurs gibKurs(long kursID) {
		KursblockungDynKurs kurs = kursMap.get(kursID);
		if (kurs == null)
			throw fehler("kursMap.get(" + kursID + ") --> kein Mapping!");
		return kurs;
	}

	// ########################################
	// ############## PROTECTED ###############
	// ########################################

	/**
	 * Liefert das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 * 
	 * @return Das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 */
	@NotNull
	Logger gibLogger() {
		return logger;
	}

	/**
	 * Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 * 
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 */
	@NotNull
	KursblockungDynStatistik gibStatistik() {
		return statistik;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximale Blockungszeit in Millisekunden.
	 */
	long gibBlockungszeitMillis() {
		return maxTimeMillis;
	}

	/**
	 * Liefert die maximal erlaubte Anzahl an Schienen. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximal erlaubte Anzahl an Schienen.
	 */
	int gibSchienenAnzahl() {
		return schienenArr.length;
	}

	/**
	 * Erzeugt ein Objekt {@link GostBlockungsergebnisManager}. Dieses Objekt beinhaltet alle Informationen aus denen
	 * die GUI die Kurs-Zu-Schiene und die SuS-Zu-Kurs-Zuordnungen rekonstruieren kann.
	 * 
	 * @param  gDataManager Das Eingabe-Objekt (der Daten-Manager).
	 * 
	 * @return              Das Blockungsergebnis für die GUI.
	 */
	@NotNull
	GostBlockungsergebnisManager gibErzeugtesKursblockungOutput(@NotNull GostBlockungsdatenManager gDataManager) {
		@NotNull GostBlockungsergebnisManager out = new GostBlockungsergebnisManager(gDataManager, -1);

		// Erzeuge die Kurs-Schienen-Zuordnungen (Manager hat eine 1-Indizierung der Schiene!)
		for (@NotNull KursblockungDynKurs dynKurs : kursArr)
			for (int schienenNr : dynKurs.gibSchienenLage()) 
				out.setKursSchienenNr(dynKurs.gibDatenbankID(), schienenNr + 1);

		// Erzeuge die Schüler-Kurs-Zuordnungen.
		for (@NotNull KursblockungDynSchueler dynSchueler : schuelerArr)
			for (KursblockungDynKurs kurs : dynSchueler.gibKurswahlen())
				if (kurs != null)
					out.setSchuelerKurs(dynSchueler.gibDatenbankID(), kurs.gibDatenbankID(), true);

		// Debug Zwecke
		/*
		for (@NotNull KursblockungDynKurs dynKurs : kursArr)
			dynKurs.debug(schuelerArr);
		for (@NotNull KursblockungDynSchueler dynSchueler : schuelerArr) 
			dynSchueler.debugKurswahlen();*/

		return out;
	}

	/**
	 * Liefert alle Kurse.
	 * 
	 * @return Array aller Kurse.
	 */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseAlle() {
		return kursArr;
	}

	/**
	 * Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseDieFreiSind() {
		return kursArrFrei;
	}

	/**
	 * Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	int gibKurseDieFreiSindAnzahl() {
		return kursArrFrei.length;
	}

	/**
	 * Liefert einen Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. Je kleiner der Wert, desto besser ist
	 * die Bewertung.
	 * 
	 * @return Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht.
	 */
	long gibBewertungFachartPaar() {
		return statistik.gibBewertungFachartPaar();
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler. Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann
	 * werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 * 
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 * 
	 * @return                Ein Array aller Schülerinnen und Schüler.
	 */
	@NotNull
	KursblockungDynSchueler @NotNull [] gibSchuelerArray(boolean pNurMultiKurse) {
		if (pNurMultiKurse) {
			@NotNull LinkedCollection<@NotNull KursblockungDynSchueler> list = new LinkedCollection<>();
			for (KursblockungDynSchueler schueler : schuelerArr) {
				if (schueler.gibHatMultikurs()) {
					list.addLast(schueler);
				}
			}
			@NotNull KursblockungDynSchueler @NotNull [] temp = new KursblockungDynSchueler[list.size()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = list.removeFirst();
			}
			return temp;
		}
		return schuelerArr;
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 * 
	 * @return Ein Array aller Schülerinnen und Schüler.
	 */
	@NotNull
	KursblockungDynSchueler @NotNull [] gibSchuelerArrayAlle() {
		return schuelerArr;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertungJetztBesserAlsS() {
		return statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibCompareZustandK_NW_KD_FW() {
		return statistik.gibCompareZustandK_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibCompareZustandG_NW_KD_FW() {
		return statistik.gibCompareZustandG_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertungK_FW_NW_KD_JetztBesser() {
		return statistik.gibCompareZustandK_FW_NW_KD();
	}

	// ########################################
	// ########### SETTER / ACTIONS ###########
	// ########################################

	/** Entfernt alle SuS aus ihren Kursen. */
	void aktionSchuelerAusAllenKursenEntfernen() {
		for (int i = 0; i < schuelerArr.length; i++) {
			schuelerArr[i].aktionKurseAlleEntfernen();
		}
	}

	/** Debug Ausgaben. Nur für Testzwecke. */
	void debug() {
		System.out.println("########## Schienen ##########");
		for (int i = 0; i < schienenArr.length; i++) {
			System.out.println("Schiene " + (i + 1));
			schienenArr[i].debug(false);
		}

		System.out.println("########## Facharten ##########");
		for (int i = 0; i < fachartArr.length; i++) {
			System.out.println("Fachart " + fachartArr[i] + " --> " + fachartArr[i].gibKursdifferenz());
			fachartArr[i].debug(schuelerArr);
		}

		statistik.debug("");
	}

	/** Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S. */
	void aktionZustandSpeichernS() {
		statistik.aktionBewertungSpeichernS();

		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			kurs.aktionZustandSpeichernS();
		}

		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionZustandSpeichernS();
		}

	}

	/** Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K. */
	void aktionZustandSpeichernK() {
		statistik.aktionBewertungSpeichernK();

		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			kurs.aktionZustandSpeichernK();
		}

		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionZustandSpeichernK();
		}
	}

	/** Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G. */
	void aktionZustandSpeichernG() {
		statistik.aktionBewertungSpeichernG();

		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			kurs.aktionZustandSpeichernG();
		}

		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionZustandSpeichernG();
		}

		// statistik.debug("GLOBAL-G gespeichert!");
	}

	/** Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung). */
	void aktionZustandLadenS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			kurs.aktionZustandLadenS();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionZustandLadenS();
		}
	}

	/** Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung). */
	void aktionZustandLadenK() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			kurs.aktionZustandLadenK();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionZustandLadenK();
		}

	}

	/** Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung). */
	void aktionZustandLadenG() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			kurs.aktionZustandLadenG();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionZustandLadenG();
		}

	}

	/** Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung). */
	void aktionZustandLadenKohneSuS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (@NotNull KursblockungDynSchueler schueler : schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (@NotNull KursblockungDynKurs kurs : kursArr) {
			kurs.aktionZustandLadenK();
		}

	}

	/** Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. */
	void aktionKurseFreieZufaelligVerteilen() {
		for (@NotNull KursblockungDynKurs kurs : kursArrFrei) {
			kurs.aktionZufaelligVerteilen();
		}
	}

	/** Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. */
	void aktionKursVerteilenEinenZufaelligenFreien() {
		if (kursArrFrei.length == 0) {
			return;
		}
		int index = _random.nextInt(kursArrFrei.length);
		@NotNull KursblockungDynKurs kurs = kursArrFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. Multikurse werden
	 * ebenso ignoriert.
	 */
	void aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse() {
		if (kursArrFrei.length == 0) {
			return;
		}
		int[] perm = KursblockungStatic.gibPermutation(_random, kursArrFrei.length);
		for (int index : perm) {
			@NotNull KursblockungDynKurs kurs = kursArrFrei[index];
			if (kurs.gibSchienenAnzahl() == 1) {
				kurs.aktionZufaelligVerteilen();
			}
		}
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertung_NW_KD_JetztS() {
		return statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines spezielle bipartiten Matching-Algorithmus verteilt. Sobald ein S. seine Nichtwahlen durch
	 * eine Veränderung der Kurslage reduzieren könnte, wird die Kurslage verändert.
	 * 
	 * @return TRUE, falls es zu einer Veränderung der Kurslage kam.
	 */
	boolean aktionKurseVerteilenNachSchuelerwunsch() {
		boolean kurslagenVeraenderung = false;

		// In zufälliger Reihenfolge SuS durchgehen...
		@NotNull int[] perm = KursblockungStatic.gibPermutation(_random, schuelerArr.length);
		for (int pSchueler = 0; pSchueler < perm.length; pSchueler++) {
			KursblockungDynSchueler schueler = schuelerArr[perm[pSchueler]];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			kurslagenVeraenderung |= schueler.aktionKurseVerteilenNachDeinemWunsch();
		}

		return kurslagenVeraenderung;
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines bipartiten Matching-Algorithmus verteilt. Bereits belegte Facharten werden übersprungen.
	 */
	void aktionSchuelerVerteilenMitBipartitemMatching() {
		@NotNull int[] perm = KursblockungStatic.gibPermutation(_random, schuelerArr.length);

		for (int p = 0; p < perm.length; p++) {
			int i = perm[p];
			KursblockungDynSchueler schueler = schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
		}

	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines gewichteten Bipartiten-Matching-Algorithmus verteilt.
	 */
	void aktionSchuelerVerteilenMitGewichtetenBipartitemMatching() {
		@NotNull int[] perm = KursblockungStatic.gibPermutation(_random, schuelerArr.length);

		for (int p = 0; p < perm.length; p++) {
			int i = perm[p];
			KursblockungDynSchueler schueler = schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();
		}

	}

}
