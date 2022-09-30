package de.nrw.schule.svws.core.kursblockung;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import de.nrw.schule.svws.core.adt.collection.LinkedCollection;
import de.nrw.schule.svws.core.adt.set.AVLSet;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFach;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFachwahl;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKursart;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputRegel;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputSchueler;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutput;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse speichert alle benötigten Daten während des Blockungsvorganges. Primär handelt es sich um die Zuordnung
 * der Kurse auf die Schienen und um die Zuordnung der SuS auf ihre Kurse.
 * 
 * @author Benjamin A. Bartsch */
public class KursblockungDynDaten {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random _random;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger logger;

	/** Alle Regeln nach ihrer ID gruppiert und in einer Liste der Reihenfolge nach gespeichert. */
	private final @NotNull HashMap<@NotNull GostKursblockungRegelTyp, @NotNull LinkedCollection<@NotNull KursblockungInputRegel>> regelMap;

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
	private final @NotNull HashMap<@NotNull Long, @NotNull HashMap<@NotNull Long, @NotNull KursblockungDynFachart>> fachartMap;

	/** Alle SuS. */
	private @NotNull KursblockungDynSchueler @NotNull [] schuelerArr;

	/** Map für schnellen Zugriff auf die SuS über ihre ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull KursblockungDynSchueler> schuelerMap;

	/** Das Statistik-Objekt speichert die aktuellen Nichtwahlen, Kursdifferenzen und weitere Daten. */
	private final @NotNull KursblockungDynStatistik statistik;

	/** Der Konstruktor der Klasse liest alle Daten von {@link KursblockungInput} ein und baut die relevanten
	 * Datenstrukturen auf.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI). 
	 * */
	public KursblockungDynDaten(@NotNull Random pRandom, @NotNull Logger pLogger, @NotNull KursblockungInput pInput) {
		_random = pRandom;
		logger = pLogger;
		regelMap = new HashMap<>();
		maxTimeMillis = pInput.maxTimeMillis;

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
		if (schritt01FehlerBeiReferenzen(pInput)) {
			return;
		}

		// Definiert: regelMap
		if (schritt02FehlerBeiRegelGruppierung(pInput.regeln)) {
			return;
		}

		// Definiert: fachartArr
		if (schritt03FehlerBeiFachartenErstellung(pInput)) {
			return;
		}

		// Definiert: schuelerArr, susMap
		if (schritt04FehlerBeiSchuelerErstellung(pInput)) {
			return;
		}

		// Definiert: schueler[i].fachartArr
		if (schritt05FehlerBeiSchuelerFachwahlenErstellung(pInput.fachwahlen, schuelerArr)) {
			return;
		}

		// Definiert: statistik
		if (schritt06FehlerBeiStatistikErstellung(fachartArr, schuelerArr)) {
			return;
		}

		// Definiert: schienenArr
		if (schritt07FehlerBeiSchienenErzeugung(pInput.maxSchienen)) {
			return;
		}

		// Benötigt: fachartArr
		// Definiert: kursArr
		if (schritt08FehlerBeiKursErstellung(pInput)) {
			return;
		}

		// Benötigt: kursArr
		// Definiert: kursArrFrei
		if (schritt09FehlerBeiKursFreiErstellung(pInput)) {
			return;
		}

		// Benötigt: kursArr
		// Definiert: fachartArr[i].kursArr
		if (schritt10FehlerBeiFachartKursArrayErstellung(pInput)) {
			return;
		}

		if (schritt11FehlerBeiRegel_4_oder_5()) {
			return;
		}

		// Zustände Speichern
		aktionZustandSpeichernS();
		aktionZustandSpeichernK();
		aktionZustandSpeichernG();
	} // Ende des Konstruktors

	/** Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 * 
	 * @param  pInput Das {@link KursblockungInput}-Objekt von der GUI.
	 * @return        {@code true}, falls kein Fehler gefunden wurde. */
	private boolean schritt01FehlerBeiReferenzen(@NotNull KursblockungInput pInput) {

		// #################### KursblockungInput ####################

		if (pInput == null)
			throw fehler("Referenz 'KursblockungInput' ist NULL!");

		if (pInput.input < 0)
			throw fehler("KursblockungInput.input = " + pInput.input + "< 0, das ist bei einer Datenbank-ID unüblich.");

		if (pInput.fachwahlen == null)
			throw fehler("Referenz 'KursblockungInput.fachwahlen' ist NULL!");

		if (pInput.fachwahlen.size() == 0)
			throw fehler("Die Blockung hat 0 Fachwahlen!");

		if (pInput.faecher == null)
			throw fehler("Referenz 'KursblockungInput.faecher' ist NULL!");

		if (pInput.faecher.size() == 0)
			throw fehler("Die Blockung hat 0 Fächer!");

		if (pInput.kursarten == null)
			throw fehler("Referenz 'KursblockungInput.kursarten' ist NULL!");

		if (pInput.kursarten.size() == 0)
			throw fehler("Die Blockung hat 0 Kursarten!");

		if (pInput.kurse == null)
			throw fehler("Referenz 'KursblockungInput.kurse' ist NULL!");

		if (pInput.kurse.size() == 0)
			throw fehler("Die Blockung hat 0 Kurse!");

		if (pInput.schueler == null)
			throw fehler("Referenz 'KursblockungInput.schueler' ist NULL!");

		if (pInput.schueler.size() == 0)
			throw fehler("Die Blockung hat 0 Schüler!");

		if (pInput.regeln == null)
			throw fehler("Referenz 'KursblockungInput.regeln' ist NULL!");

		// #################### KursblockungInputKursart ####################

		@NotNull HashSet<@NotNull Long> setKursarten = new HashSet<>();
		for (int i = 0; i < pInput.kursarten.size(); i++) {
			KursblockungInputKursart iKursart = pInput.kursarten.get(i);

			if (iKursart == null)
				throw fehler(".kursarten.get(" + i + ") ist NULL!");

			if (iKursart.id < 0)
				throw fehler("Kursart=" + iKursart.representation + " hat unübliche DB-ID=" + iKursart.id + "!");

			setKursarten.add(iKursart.id);
		}

		// #################### KursblockungInputFach ####################

		@NotNull HashSet<@NotNull Long> setFaecher = new HashSet<>();
		for (int i = 0; i < pInput.faecher.size(); i++) {
			KursblockungInputFach iFach = pInput.faecher.get(i);

			if (iFach == null)
				throw fehler(".faecher.get(" + i + ") ist NULL!");

			if (iFach.id < 0)
				throw fehler("Fach=" + iFach.representation + " hat unübliche DB-ID=" + iFach.id + "!");

			setFaecher.add(iFach.id);
		}

		// #################### KursblockungInputKurs ####################

		@NotNull HashSet<@NotNull Long> setKurse = new HashSet<>();
		for (int i = 0; i < pInput.kurse.size(); i++) {
			KursblockungInputKurs iKurs = pInput.kurse.get(i);

			if (iKurs == null)
				throw fehler(".kurse.get(" + i + ") ist NULL!");

			if (iKurs.id < 0)
				throw fehler("Kurs=" + iKurs.representation + " hat unübliche DB-ID=" + iKurs.id + "!");

			long fachID = iKurs.fach;
			if (!setFaecher.contains(fachID))
				throw fehler("Kurs=" + iKurs.representation + " hat unbekannte Fach-ID (" + fachID + ")!");

			long kursartID = iKurs.kursart;
			if (!setKursarten.contains(kursartID))
				throw fehler("Kurs=" + iKurs.representation + " hat unbekannte Kursart-ID (" + kursartID + ")!");

			setKurse.add(iKurs.id);
		}

		// #################### KursblockungInputSchueler ####################

		@NotNull HashSet<@NotNull Long> setSchueler = new HashSet<>();
		for (int i = 0; i < pInput.schueler.size(); i++) {
			KursblockungInputSchueler iSchueler = pInput.schueler.get(i);

			if (iSchueler == null)
				throw fehler(".schueler.get(" + i + ") ist NULL");

			if (iSchueler.id < 0)
				throw fehler("Schüler=" + iSchueler.representation + " hat unübliche DB-ID=" + iSchueler.id + "!");

			setSchueler.add(iSchueler.id);
		}

		// #################### KursblockungInputFachwahl ####################

		for (int i = 0; i < pInput.fachwahlen.size(); i++) {
			KursblockungInputFachwahl iFachwahl = pInput.fachwahlen.get(i);

			if (iFachwahl == null)
				throw fehler(".fachwahlen.get(" + i + ") ist NULL!");

			long schuelerID = iFachwahl.schueler;
			if (!setSchueler.contains(schuelerID))
				throw fehler("Fachwahl=" + iFachwahl.representation + " hat unbekannte Schüler-ID=" + schuelerID + "!");

			long fachID = iFachwahl.fach;
			if (!setFaecher.contains(fachID))
				throw fehler("Fachwahl=" + iFachwahl.representation + " hat unbekannte Fach-ID=" + fachID + "!");

			long kursartID = iFachwahl.kursart;
			if (!setKursarten.contains(kursartID))
				throw fehler("Fachwahl=" + iFachwahl.representation + " hat unbekannte Kursart-ID=" + kursartID + "!");
		}

		// #################### KursblockungInputRegel ####################

		for (int i = 0; i < pInput.regeln.size(); i++) {
			KursblockungInputRegel iRegel = pInput.regeln.get(i);

			if (iRegel == null)
				throw fehler(".regeln.get(" + i + ") ist NULL!");

			if (iRegel.daten == null)
				throw fehler(".regeln.get(" + i + ").daten ist NULL!");

			@NotNull GostKursblockungRegelTyp gostRegel = GostKursblockungRegelTyp.fromTyp(iRegel.typ);

			if (gostRegel == GostKursblockungRegelTyp.UNDEFINIERT)
				throw fehler(".regeln.get(" + i + ") hat unbekannte Regel-ID (" + iRegel.typ + ")!");

			// Regeltyp = 1
			if (gostRegel == GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS) {
				int length = iRegel.daten.length;
				if (length != 3)
					throw fehler("KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!");

				long kursartID = iRegel.daten[0];
				if (!setKursarten.contains(kursartID))
					throw fehler("KURSART_SPERRE_SCHIENEN_VON_BIS hat unbekannte Kursart-ID=" + kursartID + "!");

				int von = iRegel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				int bis = iRegel.daten[2].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				if (!((von >= 0) && (von <= bis) && (bis < pInput.maxSchienen)))
					throw fehler("KURSART_SPERRE_SCHIENEN_VON_BIS (" + kursartID + ", " + (von + 1) + ", " + (bis + 1)
							+ ") ist unlogisch!");
			}

			// Regeltyp = 2
			if (gostRegel == GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE) {
				int length = iRegel.daten.length;
				if (length != 2)
					throw fehler("KURS_FIXIERE_IN_SCHIENE daten.length=" + length + ", statt 2!");

				long kursID = iRegel.daten[0];
				if (!setKurse.contains(kursID))
					throw fehler("KURS_FIXIERE_IN_SCHIENE hat unbekannte Kurs-ID=" + kursID + "!");

				int schiene = iRegel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				if (!((schiene >= 0) && (schiene < pInput.maxSchienen)))
					throw fehler("KURS_FIXIERE_IN_SCHIENE (" + kursID + ", " + (schiene + 1) + ") ist unlogisch!");
			}

			// Regeltyp = 3
			if (gostRegel == GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE) {
				int length = iRegel.daten.length;
				if (length != 2)
					throw fehler("KURS_SPERRE_IN_SCHIENE daten.length=" + length + ", statt 2!");

				long kursID = iRegel.daten[0];
				if (!setKurse.contains(kursID))
					throw fehler("KURS_SPERRE_IN_SCHIENE hat unbekannte Kurs-ID=" + kursID + "!");

				int schiene = iRegel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				if (!((schiene >= 0) && (schiene < pInput.maxSchienen)))
					throw fehler("KURS_SPERRE_IN_SCHIENE (" + kursID + ", " + (schiene + 1) + ") ist unlogisch!");
			}

			// Regeltyp = 4
			if (gostRegel == GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS) {
				int length = iRegel.daten.length;
				if (length != 2)
					throw fehler("SCHUELER_FIXIEREN_IN_KURS daten.length=" + length + ", statt 2!");

				long schuelerID = iRegel.daten[0];
				if (!setSchueler.contains(schuelerID))
					throw fehler("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Schüler-ID=" + schuelerID + "!");

				long kursID = iRegel.daten[1];
				if (!setKurse.contains(kursID))
					throw fehler("SCHUELER_FIXIEREN_IN_KURS hat unbekannte Kurs-ID=" + kursID + "!");
			}

			// Regeltyp = 5
			if (gostRegel == GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS) {
				int length = iRegel.daten.length;
				if (length != 2)
					throw fehler("SCHUELER_VERBIETEN_IN_KURS daten.length=" + length + ", statt 2!");

				long schuelerID = iRegel.daten[0];
				if (!setSchueler.contains(schuelerID))
					throw fehler("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Schüler-ID=" + schuelerID + "!");

				long kursID = iRegel.daten[1];
				if (!setKurse.contains(kursID))
					throw fehler("SCHUELER_VERBIETEN_IN_KURS hat unbekannte Kurs-ID=" + kursID + "!");
			}

			// Regeltyp = 6
			if (gostRegel == GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS) {
				int length = iRegel.daten.length;
				if (length != 3)
					throw fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!");

				long kursartID = iRegel.daten[0];
				if (!setKursarten.contains(kursartID))
					throw fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS hat unbekannte Kursart-ID=" + kursartID + "!");

				int von = iRegel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				int bis = iRegel.daten[2].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				if (!((von >= 0) && (von <= bis) && (bis < pInput.maxSchienen)))
					throw fehler("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (" + kursartID + ", " + (von + 1) + ", "
							+ (bis + 1) + ") ist unlogisch!");
			}

		}

		return false;
	}

	private boolean schritt02FehlerBeiRegelGruppierung(@NotNull Vector<@NotNull KursblockungInputRegel> vRegeln) {
		// Regeln nach ID in Listen gruppieren.
		AVLSet<Long> regelDatabaseIDs = new AVLSet<>();
		for (int i = 0; i < vRegeln.size(); i++) {
			@NotNull KursblockungInputRegel regel = vRegeln.get(i);

			if (regel.databaseID != -1)
				if (regelDatabaseIDs.add(regel.databaseID) == false)
					throw fehler("Zwei Regeln haben dieselbe ID (" + regel.databaseID + ") in der Datenbank!");

			// Passende Liste holen, ggf. eine neue Liste erzeugen.
			@NotNull GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
			LinkedCollection<@NotNull KursblockungInputRegel> list = regelMap.get(regelTyp);
			if (list == null) {
				list = new LinkedCollection<>();
				regelMap.put(regelTyp, list);
			}
			// Regel der Liste hinten hinzufügen
			list.addLast(regel);
		}
		return false;
	}

	private boolean schritt03FehlerBeiFachartenErstellung(@NotNull KursblockungInput pInput) {
		// Erstelle ein Fach-Map für schnellen Zugriff.
		@NotNull HashMap<@NotNull Long, @NotNull String> mapFach = new HashMap<>();
		for (@NotNull KursblockungInputFach iFach : pInput.faecher) {
			mapFach.put(iFach.id, iFach.representation);
		}

		// Erstelle ein Kursart-Map für schnellen Zugriff.
		@NotNull HashMap<@NotNull Long, @NotNull String> mapKursart = new HashMap<>();
		for (@NotNull KursblockungInputKursart iKursart : pInput.kursarten) {
			mapKursart.put(iKursart.id, iKursart.representation);
		}

		// Erstelle ein Schüler-Map für schnellen Zugriff.
		@NotNull HashMap<@NotNull Long, @NotNull String> mapSchueler = new HashMap<>();
		for (@NotNull KursblockungInputSchueler iSchueler : pInput.schueler) {
			mapSchueler.put(iSchueler.id, iSchueler.representation);
		}

		int nFacharten = 0;

		// Facharten aus Kursen extrahieren.
		int nKurse = pInput.kurse.size();
		for (int i = 0; i < nKurse; i++) {
			@NotNull KursblockungInputKurs iKurs = pInput.kurse.get(i);
			long fachID = iKurs.fach;
			long kursartID = iKurs.kursart;
			HashMap<@NotNull Long, @NotNull KursblockungDynFachart> kursartMap = fachartMap.get(fachID);
			if (kursartMap == null) {
				kursartMap = new HashMap<>();
				fachartMap.put(fachID, kursartMap);
			}
			KursblockungDynFachart dynFachart = kursartMap.get(kursartID);
			if (dynFachart == null) {
				String strFach = mapFach.get(fachID);
				String strKursart = mapKursart.get(kursartID);
				if ((strFach == null) || (strKursart == null))
					throw new NullPointerException();
				@NotNull String representation = strFach + ";" + strKursart;
				dynFachart = new KursblockungDynFachart(_random, nFacharten, representation, statistik);
				kursartMap.put(kursartID, dynFachart);
				nFacharten++;
			}
			dynFachart.aktionMaxKurseErhoehen();
		}

		// Facharten aus SuS-Fachwahlen extrahieren.
		for (int i = 0; i < pInput.fachwahlen.size(); i++) {
			@NotNull KursblockungInputFachwahl iFachwahl = pInput.fachwahlen.get(i);
			long schuelerID = iFachwahl.schueler;
			long fachID = iFachwahl.fach;
			long kursartID = iFachwahl.kursart;
			HashMap<@NotNull Long, @NotNull KursblockungDynFachart> kursartMap = fachartMap.get(fachID);
			if (kursartMap == null) {
				kursartMap = new HashMap<>();
				fachartMap.put(fachID, kursartMap);
			}
			KursblockungDynFachart dynFachart = kursartMap.get(kursartID);
			if (dynFachart == null) {
				String strFach = mapFach.get(fachID);
				String strKursart = mapKursart.get(kursartID);
				String strSchueler = mapSchueler.get(schuelerID);
				if ((strFach == null) || (strKursart == null) || (strSchueler == null))
					throw new NullPointerException();
				@NotNull String representation = strFach + ";" + strKursart;
				dynFachart = new KursblockungDynFachart(_random, nFacharten, representation, statistik);
				kursartMap.put(kursartID, dynFachart);
				nFacharten++;
				logger.logLn(LogLevel.WARNING,
						"Schüler '" + strSchueler + "' wählt '" + representation + "', ohne das ein Kurs existiert!");
			}
			dynFachart.aktionMaxSchuelerErhoehen();
		}

		// Keine Facharten? --> Fehler
		if (nFacharten == 0) {
			fehler("Die Blockung hat 0 Facharten.");
			return true;
		}

		// fachartArr erzeugen.
		fachartArr = new KursblockungDynFachart[nFacharten];
		for (@NotNull HashMap<@NotNull Long, @NotNull KursblockungDynFachart> map : fachartMap.values()) {
			for (@NotNull KursblockungDynFachart fachart : map.values()) {
				fachartArr[fachart.gibNr()] = fachart;
			}
		}

		// Verteile Kurse verschwunden? --> Fehler
		int kursSumme = 0;
		for (int i = 0; i < fachartArr.length; i++) {
			kursSumme += fachartArr[i].gibKurseMax();
		}
		if (kursSumme != nKurse) {
			fehler("Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.");
			return true;
		}

		return false;
	}

	private boolean schritt04FehlerBeiSchuelerErstellung(@NotNull KursblockungInput pInput) {
		@NotNull Vector<@NotNull KursblockungInputSchueler> vSchueler = pInput.schueler;

		int nSchueler = vSchueler.size();
		schuelerArr = new KursblockungDynSchueler[nSchueler];
		for (int i = 0; i < nSchueler; i++) {
			@NotNull KursblockungInputSchueler iSchueler = vSchueler.get(i);
			// Schüler --> Array
			@NotNull KursblockungDynSchueler schueler = new KursblockungDynSchueler(_random, iSchueler, statistik,
					pInput.maxSchienen, pInput.kurse.size());
			schuelerArr[i] = schueler;
			// Schüler --> Map
			schuelerMap.put(iSchueler.id, schueler);
		}

		return false;
	}

	private boolean schritt05FehlerBeiSchuelerFachwahlenErstellung(
			@NotNull Vector<@NotNull KursblockungInputFachwahl> vFachwahlen,
			@NotNull KursblockungDynSchueler @NotNull [] susArr) {
		// Für jeden Schüler eine Liste mit Facharten + FachwahlID erzeugen.
		@NotNull HashMap<@NotNull KursblockungDynSchueler, @NotNull LinkedCollection<@NotNull KursblockungDynFachart>> mapSchuelerFA = new HashMap<>();
		@NotNull HashMap<@NotNull KursblockungDynSchueler, @NotNull LinkedCollection<@NotNull Long>> mapSchuelerID = new HashMap<>();
		for (int i = 0; i < susArr.length; i++) {
			mapSchuelerFA.put(susArr[i], new LinkedCollection<>());
			mapSchuelerID.put(susArr[i], new LinkedCollection<>());
		}

		// Fachwahlen durchgehen und den Listen hinzufügen.
		int nFachwahlen = vFachwahlen.size();
		for (int i = 0; i < nFachwahlen; i++) {
			@NotNull KursblockungInputFachwahl iFachwahl = vFachwahlen.get(i);
			long susID = iFachwahl.schueler;
			long fachID = iFachwahl.fach;
			long kursartID = iFachwahl.kursart;
			KursblockungDynSchueler schueler = schuelerMap.get(susID);
			HashMap<@NotNull Long, @NotNull KursblockungDynFachart> kursartMap = fachartMap.get(fachID);
			KursblockungDynFachart dynFachart = kursartMap == null ? null : kursartMap.get(kursartID);
			LinkedCollection<@NotNull KursblockungDynFachart> dynFacharten = mapSchuelerFA.get(schueler);
			if (dynFacharten == null)
				throw new NullPointerException();
			dynFacharten.addLast(dynFachart);
			LinkedCollection<@NotNull Long> fachwahlIDs = mapSchuelerID.get(schueler);
			if (fachwahlIDs == null)
				throw new NullPointerException();
			fachwahlIDs.addLast(iFachwahl.id);
		}

		// Schüler und Facharten verknüpfen.
		for (int nr = 0; nr < susArr.length; nr++) {
			@NotNull KursblockungDynSchueler schueler = susArr[nr];
			LinkedCollection<@NotNull KursblockungDynFachart> listFA = mapSchuelerFA.get(schueler);
			LinkedCollection<@NotNull Long> listID = mapSchuelerID.get(schueler);
			if ((listFA == null) || (listID == null))
				throw new NullPointerException();
			int nWahlen = listFA.size();
			@NotNull KursblockungDynFachart @NotNull [] arrFA = new KursblockungDynFachart[nWahlen];
			@NotNull long[] arrID = new long[nWahlen];
			for (int i = 0; i < nWahlen; i++) {
				arrFA[i] = listFA.removeFirst();
				arrID[i] = listID.removeFirst();
			}
			schueler.aktionSetzeFachartenUndIDs(arrFA, arrID);
		}
		return false;
	}

	private boolean schritt06FehlerBeiStatistikErstellung(@NotNull KursblockungDynFachart @NotNull [] fachartArr,
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

		return false;
	}

	private boolean schritt07FehlerBeiSchienenErzeugung(int pSchienen) {
		schienenArr = new KursblockungDynSchiene[pSchienen];
		for (int nr = 0; nr < pSchienen; nr++) {
			schienenArr[nr] = new KursblockungDynSchiene(logger, nr, statistik);
		}
		return false;
	}

	private boolean schritt08FehlerBeiKursErstellung(@NotNull KursblockungInput pInput) {
		@NotNull Vector<@NotNull KursblockungInputKurs> vKurse = pInput.kurse;
		int nKurse = vKurse.size();
		int nSchienen = schienenArr.length;

		// Jedem Kurs zwei Listen (von Schienen) zuordnen.
		@NotNull HashMap<@NotNull Long, @NotNull LinkedCollection<@NotNull KursblockungDynSchiene>> mapKursSchieneFrei = new HashMap<>();
		@NotNull HashMap<@NotNull Long, @NotNull LinkedCollection<@NotNull KursblockungDynSchiene>> mapKursSchieneLage = new HashMap<>();
		for (int i = 0; i < nKurse; i++) {
			long kursID = vKurse.get(i).id;
			@NotNull LinkedCollection<@NotNull KursblockungDynSchiene> schieneFrei = new LinkedCollection<>();
			mapKursSchieneLage.put(kursID, new LinkedCollection<>());
			mapKursSchieneFrei.put(kursID, schieneFrei);

			// Permutation
			@NotNull int[] perm = new int[nSchienen];
			for (int j = 0; j < nSchienen; j++) {
				perm[j] = j;
			}
			for (int j1 = 0; j1 < nSchienen; j1++) {
				int j2 = _random.nextInt(nSchienen);
				int s1 = perm[j1];
				int s2 = perm[j2];
				perm[j1] = s2;
				perm[j2] = s1;
			}

			for (int j = 0; j < nSchienen; j++) {
				schieneFrei.addLast(schienenArr[perm[j]]);
			}
		}

		// Regel 1 - Alle Kurse einer bestimmten Kursart sperren.
		LinkedCollection<@NotNull KursblockungInputRegel> regelnTyp1 = regelMap
				.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS);
		if (regelnTyp1 != null)
			for (@NotNull KursblockungInputRegel regel1 : regelnTyp1) {
				long kursart = regel1.daten[0];
				int von = regel1.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				int bis = regel1.daten[2].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				// Alle Kurse durchgehen ...
				for (int i = 0; i < pInput.kurse.size(); i++) {
					@NotNull KursblockungInputKurs kurs = pInput.kurse.get(i);
					// Ist es die richtige Kursart?
					if (kurs.kursart == kursart) {
						for (int schiene = von; schiene <= bis; schiene++) {
							LinkedCollection<@NotNull KursblockungDynSchiene> schieneFrei = mapKursSchieneFrei
									.get(kurs.id);
							if (schieneFrei == null)
								throw new NullPointerException();
							schieneFrei.remove(schienenArr[schiene]);
						}
					}
				}
			}

		// Regel 6 - Alle Kurse die NICHT eine bestimmte Kursart haben in bestimmten Schienen sperren.
		// Regel 6 ist wie Regel 1 nur statt "==" haben wir "!=".
		LinkedCollection<@NotNull KursblockungInputRegel> regelnTyp6 = regelMap
				.get(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
		if (regelnTyp6 != null)
			for (@NotNull KursblockungInputRegel regel6 : regelnTyp6) {
				long kursart = regel6.daten[0];
				int von = regel6.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				int bis = regel6.daten[2].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				// Alle Kurse durchgehen ...
				for (int i = 0; i < pInput.kurse.size(); i++) {
					@NotNull KursblockungInputKurs kurs = pInput.kurse.get(i);
					// Ist es die richtige Kursart?
					if (kurs.kursart != kursart) {
						for (int schiene = von; schiene <= bis; schiene++) {
							LinkedCollection<@NotNull KursblockungDynSchiene> schieneFrei = mapKursSchieneFrei
									.get(kurs.id);
							if (schieneFrei == null)
								throw new NullPointerException();
							schieneFrei.remove(schienenArr[schiene]);
						}
					}
				}
			}

		// Regel 3 - Pro Kurs gesperrte Schienen entfernen.
		LinkedCollection<@NotNull KursblockungInputRegel> regelnTyp3 = regelMap
				.get(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE);
		if (regelnTyp3 != null)
			for (@NotNull KursblockungInputRegel regel3 : regelnTyp3) {
				long kursID = regel3.daten[0];
				int schiene = regel3.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				LinkedCollection<@NotNull KursblockungDynSchiene> schieneFrei = mapKursSchieneFrei.get(kursID);
				if (schieneFrei == null)
					throw new NullPointerException();
				schieneFrei.remove(schienenArr[schiene]);
			}

		// Regel 2 - Pro Kurs fixierte Schiene in "Lage" hinzufügen.
		LinkedCollection<@NotNull KursblockungInputRegel> regelnTyp2 = regelMap
				.get(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE);
		if (regelnTyp2 != null)
			for (@NotNull KursblockungInputRegel regel2 : regelnTyp2) {
				long kursID = regel2.daten[0];
				int schiene = regel2.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				LinkedCollection<@NotNull KursblockungDynSchiene> schieneFrei = mapKursSchieneFrei.get(kursID);
				LinkedCollection<@NotNull KursblockungDynSchiene> schieneLage = mapKursSchieneLage.get(kursID);
				if ((schieneFrei == null) || (schieneLage == null))
					throw new NullPointerException();
				if (schieneLage.contains(schienenArr[schiene]))
					continue; // Doppeltfixierungen ignorieren
				if (!schieneFrei.contains(schienenArr[schiene])) {
					KursblockungDynKurs kurs = kursMap.get(kursID);
					if (kurs == null)
						throw new NullPointerException();
					fehler("KURS_FIXIERE_IN_SCHIENE: Kurs=" + kurs.gibRepresentation() + " will Schiene=" + schiene
							+ " fixieren, die Schiene wurde aber bereits gesperrt!");
					return true;
				}
				schieneFrei.remove(schienenArr[schiene]);
				schieneLage.addLast(schienenArr[schiene]);
			}

		// Kurse erzeugen
		kursArr = new KursblockungDynKurs[nKurse];
		for (int i = 0; i < nKurse; i++) {
			@NotNull KursblockungInputKurs iKurs = vKurse.get(i);
			@NotNull String representation = iKurs.representation;
			long fach = iKurs.fach;
			long kursart = iKurs.kursart;
			int schienen = iKurs.schienen;

			// Fehler: Kurs belegt zu wenig Schienen.
			if (schienen <= 0)
				throw fehler("Kurs '" + representation + "' belegt nur " + schienen + " Schienen, das ist zu wenig.");

			// Fehler: Kurs belegt zu viele Schienen.
			if (schienen > schienenArr.length)
				throw fehler("Es gibt " + schienenArr.length + " Schienen, aber der Kurs '" + representation
						+ "' möchte " + schienen + " Schienen belegt.");

			// Fehler: Zu viel fixiert?
			LinkedCollection<@NotNull KursblockungDynSchiene> listLage = mapKursSchieneLage.get(iKurs.id);
			if (listLage == null)
				throw fehler("listLage == null");
			int pSchienenLageFixiert = listLage.size();
			if (pSchienenLageFixiert > iKurs.schienen)
				throw fehler("Kurs '" + representation + "' fixert " + pSchienenLageFixiert
						+ " Schienen, das ist mehr als seine Schienenanzahl " + iKurs.schienen + " .");

			// 'listLage' auffüllen --> Fehler: Zu viel gesperrt?
			LinkedCollection<@NotNull KursblockungDynSchiene> listFrei = mapKursSchieneFrei.get(iKurs.id);
			if (listFrei == null)
				throw fehler("listFrei == null");
			while (listLage.size() < iKurs.schienen) {
				if (listFrei.isEmpty())
					throw fehler("Kurs '" + representation + "' hat zu viele Schienen gesperrt,"
							+ " so dass seine seine Schienenanzahl keinen Platz mehr hat .");
				listLage.addLast(listFrei.pollFirst());
			}

			// listLage konvertieren.
			@NotNull KursblockungDynSchiene @NotNull [] pSchienenLage = new KursblockungDynSchiene[listLage.size()];
			for (int j = 0; j < pSchienenLage.length; j++) {
				pSchienenLage[j] = listLage.removeFirst();
			}

			// listFrei konvertieren
			@NotNull KursblockungDynSchiene @NotNull [] pSchienenFrei = new KursblockungDynSchiene[listFrei.size()];
			for (int j = 0; j < pSchienenFrei.length; j++) {
				pSchienenFrei[j] = listFrei.removeFirst();
			}

			HashMap<@NotNull Long, @NotNull KursblockungDynFachart> kursartMap = fachartMap.get(fach);
			KursblockungDynFachart dynFachart = kursartMap == null ? null : kursartMap.get(kursart);
			if (dynFachart == null)
				throw new NullPointerException();
			@NotNull KursblockungDynKurs kurs = new KursblockungDynKurs(_random, pSchienenLage, pSchienenLageFixiert,
					pSchienenFrei, iKurs, dynFachart, logger, i);
			// Kurs --> Array
			kursArr[i] = kurs;
			// Kurs --> Map
			kursMap.put(iKurs.id, kurs);
		}

		return false;
	}

	private boolean schritt09FehlerBeiKursFreiErstellung(@NotNull KursblockungInput pInput) {
		int nKursFrei = 0;
		for (int i = 0; i < kursArr.length; i++) {
			if (kursArr[i].gibHatFreiheitsgrade()) {
				nKursFrei++;
			}
		}

		kursArrFrei = new KursblockungDynKurs[nKursFrei];
		for (int i = 0, j = 0; i < kursArr.length; i++) {
			if (kursArr[i].gibHatFreiheitsgrade()) {
				kursArrFrei[j] = kursArr[i];
				j++;
			}
		}

		return false;
	}

	private boolean schritt10FehlerBeiFachartKursArrayErstellung(@NotNull KursblockungInput pInput) {
		// Pro Fachart eine Liste zum Speichern aller zugehörigen Kurse.
		int nFacharten = fachartArr.length;
		@NotNull HashMap<@NotNull Integer, @NotNull LinkedCollection<@NotNull KursblockungDynKurs>> mapFachartList = new HashMap<>();
		for (int i = 0; i < nFacharten; i++) {
			mapFachartList.put(i, new LinkedCollection<>());
		}

		// Kurse sammeln...
		for (int i = 0; i < kursArr.length; i++) {
			@NotNull KursblockungDynKurs kurs = kursArr[i];
			int fachartNr = kurs.gibFachart().gibNr();
			LinkedCollection<@NotNull KursblockungDynKurs> fachartKurse = mapFachartList.get(fachartNr);
			if (fachartKurse == null)
				throw new NullPointerException();
			fachartKurse.addLast(kurs);
		}

		// Pro Fachart: Liste zu Array konvertieren und übergeben.
		for (int nr = 0; nr < nFacharten; nr++) {
			LinkedCollection<@NotNull KursblockungDynKurs> list = mapFachartList.get(nr);
			if (list == null)
				throw new NullPointerException();

			@NotNull KursblockungDynKurs @NotNull [] kursArr = new KursblockungDynKurs[list.size()];
			for (int i = 0; i < kursArr.length; i++) {
				kursArr[i] = list.removeFirst();
			}
			fachartArr[nr].aktionSetKurse(kursArr);
		}

		return false;
	}

	private boolean schritt11FehlerBeiRegel_4_oder_5() {

		// Regel 4 - Schüler X in Kurs Y fixieren
		LinkedCollection<@NotNull KursblockungInputRegel> regelnTyp4 = regelMap
				.get(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS);
		if (regelnTyp4 != null) {
			for (@NotNull KursblockungInputRegel regel4 : regelnTyp4) {
				long schuelerID = regel4.daten[0];
				long kursID = regel4.daten[1];
				KursblockungDynSchueler schueler = schuelerMap.get(schuelerID);
				if (schueler == null)
					throw new NullPointerException();
				KursblockungDynKurs fixierterKurs = kursMap.get(kursID);
				if (fixierterKurs == null)
					throw new NullPointerException();
				// Alle anderen Kurse der selben Fachart verbieten ...
				for (@NotNull KursblockungDynKurs kurs : fixierterKurs.gibFachart().gibKurse()) {
					if (kurs != fixierterKurs) {
						schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
					}
				}
			}
		}

		// Regel 5 - Verbiete X in Kurs Y
		LinkedCollection<@NotNull KursblockungInputRegel> regelnTyp5 = regelMap
				.get(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS);
		if (regelnTyp5 != null) {
			for (@NotNull KursblockungInputRegel regel5 : regelnTyp5) {
				long schuelerID = regel5.daten[0];
				long kursID = regel5.daten[1];
				KursblockungDynSchueler schueler = schuelerMap.get(schuelerID);
				if (schueler == null)
					throw new NullPointerException();
				KursblockungDynKurs verbotenerKurs = kursMap.get(kursID);
				if (verbotenerKurs == null)
					throw new NullPointerException();
				// Kurs verbieten
				schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
			}
		}

		return false;
	}

	/** Leert die Datenstruktur und teilt dem Logger einen Fehler mit.
	 * 
	 * @param fehlermeldung Die Fehlermeldung. */
	private KursblockungException fehler(@NotNull String fehlermeldung) {
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

		logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new KursblockungException(fehlermeldung);
	}

	private @NotNull Long @NotNull [] gibBewertungsKriterium1_NichtErfuellteRegeln() {
		LinkedCollection<Long> listeNichtErfuellterRegeln = new LinkedCollection<>();

		// Check Regel 1: KURSART_SPERRE_SCHIENEN_VON_BIS
		LinkedCollection<@NotNull KursblockungInputRegel> list1 = regelMap
				.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS);
		if (list1 != null)
			for (@NotNull KursblockungInputRegel regel : list1) {
				long kursArtID = regel.daten[0];
				int schieneVon = regel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				int schieneBis = regel.daten[2].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				for (@NotNull KursblockungDynKurs kurs : kursArr)
					if (kurs.gibDatabaseKursArtID() == kursArtID)
						if (kurs.gibIstImSchienenIntervall(schieneVon, schieneBis))
							listeNichtErfuellterRegeln.add(regel.databaseID);
			}

		// Check Regel 6: KURSART_ALLEIN_IN_SCHIENEN_VON_BIS
		LinkedCollection<@NotNull KursblockungInputRegel> list6 = regelMap
				.get(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
		if (list6 != null)
			for (@NotNull KursblockungInputRegel regel : list6) {
				long kursArtID = regel.daten[0];
				int schieneVon = regel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				int schieneBis = regel.daten[2].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				for (@NotNull KursblockungDynKurs kurs : kursArr)
					if (kurs.gibDatabaseKursArtID() != kursArtID)
						if (kurs.gibIstImSchienenIntervall(schieneVon, schieneBis))
							listeNichtErfuellterRegeln.add(regel.databaseID);
			}

		// Check Regel 2: KURS_FIXIERE_IN_SCHIENE
		LinkedCollection<@NotNull KursblockungInputRegel> list2 = regelMap
				.get(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE);
		if (list2 != null)
			for (@NotNull KursblockungInputRegel regel : list2) {
				long kursDatabaseID = regel.daten[0];
				int schiene = regel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				KursblockungDynKurs kurs = kursMap.get(kursDatabaseID);
				if (kurs != null)
					if (kurs.gibIstInSchiene(schiene) == false)
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}

		// Check Regel 3: KURS_SPERRE_IN_SCHIENE
		LinkedCollection<@NotNull KursblockungInputRegel> list3 = regelMap
				.get(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE);
		if (list3 != null)
			for (@NotNull KursblockungInputRegel regel : list3) {
				long kursDatabaseID = regel.daten[0];
				int schiene = regel.daten[1].intValue() - 1; // GUI ist 1-indiziert, intern aber 0-indiziert.
				KursblockungDynKurs kurs = kursMap.get(kursDatabaseID);
				if (kurs != null)
					if (kurs.gibIstInSchiene(schiene) == true)
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}

		// Check Regel 4: SCHUELER_FIXIEREN_IN_KURS
		LinkedCollection<@NotNull KursblockungInputRegel> list4 = regelMap
				.get(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS);
		if (list4 != null)
			for (@NotNull KursblockungInputRegel regel : list4) {
				long schuelerDatabaseID = regel.daten[0];
				long kursDatabaseID = regel.daten[1];
				KursblockungDynSchueler schueler = schuelerMap.get(schuelerDatabaseID);
				KursblockungDynKurs kurs = kursMap.get(kursDatabaseID);
				if ((schueler != null) && (kurs != null))
					if (schueler.gibIstInKurs(kurs) == false)
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}

		// Check Regel 5: SCHUELER_VERBIETEN_IN_KURS
		LinkedCollection<@NotNull KursblockungInputRegel> list5 = regelMap
				.get(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS);
		if (list5 != null)
			for (@NotNull KursblockungInputRegel regel : list5) {
				long schuelerDatabaseID = regel.daten[0];
				long kursDatabaseID = regel.daten[1];
				KursblockungDynSchueler schueler = schuelerMap.get(schuelerDatabaseID);
				KursblockungDynKurs kurs = kursMap.get(kursDatabaseID);
				if ((schueler != null) && (kurs != null))
					if (schueler.gibIstInKurs(kurs) == true)
						listeNichtErfuellterRegeln.add(regel.databaseID);
			}

		return listeNichtErfuellterRegeln.toArray(new Long[0]);
	}

	private long gibBewertungsKriterium2_NichtZugeordneteWahlen() {
		return statistik.gibBewertungNichtwahlen();
	}

	private @NotNull Integer @NotNull [] gibBewertungsKriterium3_Kursdifferenzen_als_Histogramm() {

		// Das Array der Kursdiffenzen hat die Länge (Schüleranzahl + 1).
		@NotNull int[] original = statistik.gibArrayDerKursdifferenzen();

		// Hat minimal den Wert 0 und maximal den Schüleranzahl + 1
		int max = statistik.gibBewertungKursdifferenz();

		@NotNull Integer @NotNull [] copy = new Integer[max + 1];
		for (int i = 0; i <= max; i++)
			copy[i] = original[i];

		return copy;
	}

	private int gibBewertungsKriterium4_GleicheFachartenProSchiene() {
		int summe = 0;
		for (@NotNull KursblockungDynSchiene schiene : schienenArr)
			summe += schiene.gibAnzahlGleicherFacharten();
		return summe;
	}

	// ########################################
	// ############## PROTECTED ###############
	// ########################################

	/** Liefert das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 * 
	 * @return Das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler. */
	@NotNull
	Logger gibLogger() {
		return logger;
	}

	/** Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 * 
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.). */
	@NotNull
	KursblockungDynStatistik gibStatistik() {
		return statistik;
	}

	/** Liefert die maximale Blockungszeit in Millisekunden. Entweder handelt es sich um einen Standardwert oder der
	 * Wert wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximale Blockungszeit in Millisekunden. */
	long gibBlockungszeitMillis() {
		return maxTimeMillis;
	}

	/** Liefert die maximal erlaubte Anzahl an Schienen. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 * 
	 * @return Liefert die maximal erlaubte Anzahl an Schienen. */
	int gibSchienenAnzahl() {
		return schienenArr.length;
	}

	/** Erzeugt ein Objekt {@link KursblockungOutput}. Dieses Objekt beinhaltet alle Informationen aus denen die GUI die
	 * Kurs-Zu-Schiene und die SuS-Zu-Kurs-Zuordnungen rekonstruieren kann.
	 * 
	 * @param  inputID   Die ID der Eingabe, also Blockung die von der GUI kommt.
	 * @param  inputSeed Der Seed, mit dem das Random-Objekt initialisiert wurde.
	 * @return           Das Blockungsergebnis für die GUI. */
	@NotNull
	KursblockungOutput gibErzeugtesKursblockungOutput(long inputSeed, long inputID) {
		@NotNull KursblockungOutput out = new KursblockungOutput();
		out.seed = inputSeed;
		out.input = inputID;

		// Erzeuge die Kurs-Schienen-Zuordnungen
		for (int i = 0; i < kursArr.length; i++)
			kursArr[i].aktionOutputErzeugen(out.kursZuSchiene);

		// Erzeuge die Fachwahl-Kurs-Zuordnungen
		for (int i = 0; i < schuelerArr.length; i++)
			schuelerArr[i].aktionOutputsErzeugen(out.fachwahlenZuKurs);

		// Erzeuge Bewertungskriterien
		out.bewertungNichtErfuellteRegeln = gibBewertungsKriterium1_NichtErfuellteRegeln();
		out.bewertungNichtZugeordneteFachwahlen = gibBewertungsKriterium2_NichtZugeordneteWahlen();
		out.bewertungHistogrammDerKursdifferenzen = gibBewertungsKriterium3_Kursdifferenzen_als_Histogramm();
		out.bewertungGleicheFachartProSchiene = gibBewertungsKriterium4_GleicheFachartenProSchiene();

		return out;
	}

	/** Liefert alle Kurse.
	 * 
	 * @return Array aller Kurse. */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseAlle() {
		return kursArr;
	}

	/** Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist. */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseDieFreiSind() {
		return kursArrFrei;
	}

	/** Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 * 
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist. */
	int gibKurseDieFreiSindAnzahl() {
		return kursArrFrei.length;
	}

	/** Liefert einen Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. Je kleiner der Wert, desto besser
	 * ist die Bewertung.
	 * 
	 * @return Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. */
	long gibBewertungFachartPaar() {
		return statistik.gibBewertungFachartPaar();
	}

	/** Liefert ein Array aller Schülerinnen und Schüler. Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann
	 * werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 * 
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 * @return                Ein Array aller Schülerinnen und Schüler. */
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

	/** Liefert ein Array aller Schülerinnen und Schüler.
	 * 
	 * @return Ein Array aller Schülerinnen und Schüler. */
	@NotNull
	KursblockungDynSchueler @NotNull [] gibSchuelerArrayAlle() {
		return schuelerArr;
	}

	/** Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. */
	int gibBewertungJetztBesserAlsS() {
		return statistik.gibBewertungZustandS_NW_KD();
	}

	/** Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. */
	int gibCompareZustandK_NW_KD_FW() {
		return statistik.gibCompareZustandK_NW_KD_FW();
	}

	/** Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. */
	int gibCompareZustandG_NW_KD_FW() {
		return statistik.gibCompareZustandG_NW_KD_FW();
	}

	/** Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. */
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

	/** Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. Multikurse werden
	 * ebenso ignoriert. */
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

	/** Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 * 
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist. */
	int gibBewertung_NW_KD_JetztS() {
		return statistik.gibBewertungZustandS_NW_KD();
	}

	/** Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines spezielle bipartiten Matching-Algorithmus verteilt. Sobald ein S. seine Nichtwahlen durch
	 * eine Veränderung der Kurslage reduzieren könnte, wird die Kurslage verändert.
	 * 
	 * @return TRUE, falls es zu einer Veränderung der Kurslage kam. */
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

	/** Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines bipartiten Matching-Algorithmus verteilt. Bereits belegte Facharten werden übersprungen. */
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

	/** Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines gewichteten Bipartiten-Matching-Algorithmus verteilt. */
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
