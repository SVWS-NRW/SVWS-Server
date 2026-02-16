package de.svws_nrw.core.kursblockung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
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
	private final @NotNull Logger _logger;

	/** Alle Regeln nach ihrer ID gruppiert und in einer Liste der Reihenfolge nach gespeichert. */
	private final @NotNull Map<GostKursblockungRegelTyp, List<GostBlockungRegel>> _regelMap;

	/** Die maximale Blockungszeit in Millisekunden. */
	private final long _maxTimeMillis;

	/** Diese Datenstruktur speichert die Schienen und ihre Kurse. */
	private @NotNull KursblockungDynSchiene @NotNull [] _schienenArr;

	/** Alles Kurse. */
	private @NotNull KursblockungDynKurs @NotNull [] _kursArr;

	/** Alle Kurse, die noch über Schienen wandern können. */
	private @NotNull KursblockungDynKurs @NotNull [] _kursArrFrei;

	/** Map für schnellen Zugriff auf die Kurse über ihre ID. */
	private final @NotNull HashMap<Long, KursblockungDynKurs> _kursMap;

	/** Alle Facharten. Fachart meint Fach + Kursart, z.B. "D;GK". */
	private @NotNull KursblockungDynFachart @NotNull [] _fachartArr;

	/** Map für schnellen Zugriff auf die Facharten über FachID und KursartID. */
	private final @NotNull HashMap2D<Long, Integer, KursblockungDynFachart> _fachartMap2D;

	/** Alle SuS. */
	private @NotNull KursblockungDynSchueler @NotNull [] _schuelerArr;

	/** Map für schnellen Zugriff auf die SuS über ihre ID. */
	private final @NotNull HashMap<Long, KursblockungDynSchueler> _schuelerMap;

	/** Das Statistik-Objekt speichert die aktuellen Nichtwahlen, Kursdifferenzen und weitere Daten. */
	private final @NotNull KursblockungDynStatistik _statistik;

	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link GostBlockungsdatenManager} ein und baut die relevanten Datenstrukturen auf.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Ein {@link Logger}-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 * @param input   Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public KursblockungDynDaten(final @NotNull Random random, final @NotNull Logger logger, final @NotNull GostBlockungsdatenManager input) {
		_random = random;
		_logger = logger;
		_regelMap = new ArrayMap<>(GostKursblockungRegelTyp.values()); // Konstruktor für Enum-Typen.
		_maxTimeMillis = input.getMaxTimeMillis();

		_schienenArr = new KursblockungDynSchiene[0];

		_kursArr = new KursblockungDynKurs[0];
		_kursArrFrei = new KursblockungDynKurs[0];
		_kursMap = new HashMap<>();

		_fachartArr = new KursblockungDynFachart[0];
		_fachartMap2D = new HashMap2D<>();

		_schuelerArr = new KursblockungDynSchueler[0];
		_schuelerMap = new HashMap<>();

		_statistik = new KursblockungDynStatistik(_logger);

		// Definiert: ---
		fehlerBeiReferenzen(input);

		// Definiert: regelMap
		fehlerBeiRegelGruppierung(input.daten().regeln);

		// Definiert: schuelerArr, susMap
		fehlerBeiSchuelerErstellung(input);

		// Definiert: schienenArr
		fehlerBeiSchienenErzeugung(input.schieneGetAnzahl());

		// Definiert: fachartArr
		fehlerBeiFachartenErstellung(input, _schuelerArr.length, _schienenArr.length);

		// Definiert: schueler[i].fachartArr
		fehlerBeiSchuelerFachwahlenErstellung(input, _schuelerArr);

		// Definiert: statistik
		fehlerBeiStatistikErstellung(_fachartArr, _schuelerArr, input);

		// Benötigt: fachartArr
		// Definiert: kursArr
		fehlerBeiKursErstellung(input, _schuelerArr.length);

		// Benötigt: kursArr
		// Definiert: kursArrFrei
		fehlerBeiKursFreiErstellung();

		// Benötigt: kursArr
		// Definiert: fachartArr[i].kursArr
		fehlerBeiFachartKursArrayErstellung();

		fehlerBeiRegel_4_oder_5_SCHUELER_KURS_VARIANTEN();

		fehlerBeiRegel_7_oder_8_KURS_MIT_KURS_VARIANTEN();

		fehlerBeiRegel_9_KURS_MIT_DUMMY_SUS_AUFFUELLEN();

		fehlerBeiRegel_10_LEHRKRAEFTE_BEACHTEN(input);

		fehlerBeiRegel_11_bis_14_SCHUELER_MIT_SCHUELER_VARIANTEN(input);

		fehlerBeiRegel_15_KURS_MAXIMALE_SCHUELERANZAHL(); // Muss nach Regel 4 (Schüler-Kurs-Fixierung) passieren.

		fehlerBeiRegel_16_SCHUELER_IGNORIEREN();

		fehlerBeiRegel_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE();

		// Zustände Speichern
		aktionZustandSpeichernS();
		aktionZustandSpeichernK();
		aktionZustandSpeichernG();
	}

	/**
	 * Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 *
	 * @param input Das {@link KursblockungInput}-Objekt von der GUI.
	 */
	@SuppressWarnings("static-method")
	private void fehlerBeiReferenzen(final @NotNull GostBlockungsdatenManager input) {

		DeveloperNotificationException.ifNull("pInput", input);
		DeveloperNotificationException.ifNull("pInput.daten()", input.daten());
		DeveloperNotificationException.ifNull("pInput.daten().fachwahlen", input.daten().fachwahlen);
		DeveloperNotificationException.ifNull("pInput.faecherManager()", input.faecherManager());
		DeveloperNotificationException.ifNull("pInput.faecherManager().faecher()", input.faecherManager().faecher());
		DeveloperNotificationException.ifNull("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifNull("pInput.daten().kurse", input.daten().kurse);
		DeveloperNotificationException.ifNull("pInput.daten().regeln", input.daten().regeln);
		DeveloperNotificationException.ifInvalidID("pInput.getID()", input.getID());
		DeveloperNotificationException.ifArrayIsEmpty("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().fachwahlen", input.daten().fachwahlen);
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.faecherManager().faecher()", input.faecherManager().faecher());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().kurse", input.daten().kurse);
		final int schienenAnzahl = input.schieneGetAnzahl();
		DeveloperNotificationException.ifSmaller("schienenAnzahl", schienenAnzahl, 1);

		// #################### GostBlockungSchiene ####################

		final HashSet<Integer> usedSchiene = new HashSet<>();
		for (final @NotNull GostBlockungSchiene gSchiene : input.daten().schienen) {
			DeveloperNotificationException.ifInvalidID(
					"Die G-Schiene %s hat keine gültige ID.".formatted(gSchiene),
					gSchiene.id);
			DeveloperNotificationException.ifSmaller(
					"Die G-Schiene %s ist zu klein!".formatted(gSchiene),
					gSchiene.nummer, 1);
			DeveloperNotificationException.ifGreater(
					"Die G-Schiene %s ist zu groß!".formatted(gSchiene),
					gSchiene.nummer, schienenAnzahl);
			DeveloperNotificationException.ifSetAddsDuplicate("usedSchiene", usedSchiene, gSchiene.nummer);
		}

		// #################### KursblockungInputKursart ####################

		final @NotNull HashSet<Integer> setKursarten = new HashSet<>();
		for (final @NotNull GostKursart iKursart : GostKursart.values()) {
			DeveloperNotificationException.ifNull("iKursart", iKursart);
			DeveloperNotificationException.ifInvalidID("iKursart.id", iKursart.id);
			DeveloperNotificationException.ifSetAddsDuplicate(
					"Doppelte ID=%d in 'setKursarten'.".formatted(iKursart.id),
					setKursarten, iKursart.id);
		}

		// #################### KursblockungInputFach ####################

		final @NotNull HashSet<Long> setFaecher = new HashSet<>();
		for (final @NotNull GostFach iFach : input.faecherManager().faecher()) {
			DeveloperNotificationException.ifNull("iFach", iFach);
			DeveloperNotificationException.ifInvalidID("iFach.id", iFach.id);
			DeveloperNotificationException.ifSetAddsDuplicate(
					"Doppele ID=%d in 'setFaecher'.".formatted(iFach.id),
					setFaecher, iFach.id);
		}

		// #################### KursblockungInputKurs ####################

		final @NotNull HashSet<Long> setKurse = new HashSet<>();
		for (final @NotNull GostBlockungKurs iKurs : input.daten().kurse) {
			DeveloperNotificationException.ifNull("iKurs", iKurs);
			DeveloperNotificationException.ifInvalidID("iKurs.id", iKurs.id);
			DeveloperNotificationException.ifSetNotContains(
					"Kurs ID=%d/NR=%d referenziert das Fach %d, aber es fehlt in 'setFaecher'."
							.formatted(iKurs.id, iKurs.nummer, iKurs.fach_id),
					setFaecher, iKurs.fach_id);
			DeveloperNotificationException.ifSetNotContains(
					"Kurs ID=%d/NR=%d referenziert die Kursart Fach %d, aber es fehlt in 'setKursarten'."
							.formatted(iKurs.id, iKurs.nummer, iKurs.kursart),
					setKursarten, iKurs.kursart);
			DeveloperNotificationException.ifSetAddsDuplicate(
					"Kurs ID=%d/NR=%d ist bereits vorhanden in 'setKurse'."
							.formatted(iKurs.id, iKurs.nummer),
					setKurse, iKurs.id);
		}

		// #################### Schüler ####################
		final @NotNull HashSet<Long> setSchueler = new HashSet<>();
		for (final @NotNull Schueler gSchueler : input.daten().schueler)
			DeveloperNotificationException.ifSetAddsDuplicate(
					"Schüler ID=%d ist bereits vorhanden in 'setSchueler'.".formatted(gSchueler.id),
					setSchueler, gSchueler.id);

		// #################### KursblockungInputFachwahl ####################

		for (final @NotNull GostFachwahl iFachwahl : input.daten().fachwahlen) {
			DeveloperNotificationException.ifNull("iFachwahl", iFachwahl);
			DeveloperNotificationException.ifInvalidID("iFachwahl.schuelerID", iFachwahl.schuelerID);
			DeveloperNotificationException.ifSetNotContains(
					"Fachwahl (%d,%d,%d) referenziert Fach ID=%d, aber es fehlt in 'setFaecher'."
							.formatted(iFachwahl.fachID, iFachwahl.kursartID, iFachwahl.schuelerID, iFachwahl.fachID),
					setFaecher, iFachwahl.fachID);
			DeveloperNotificationException.ifSetNotContains(
					"Fachwahl (%d,%d,%d) referenziert Kursart ID=%d, aber es fehlt in 'setKursarten'."
							.formatted(iFachwahl.fachID, iFachwahl.kursartID, iFachwahl.schuelerID, iFachwahl.kursartID),
					setKursarten, iFachwahl.kursartID);
			DeveloperNotificationException.ifSetNotContains(
					"Fachwahl (%d,%d,%d) referenziert Schüler ID=%d, aber es fehlt in 'setSchueler'."
							.formatted(iFachwahl.fachID, iFachwahl.kursartID, iFachwahl.schuelerID, iFachwahl.schuelerID),
					setSchueler, iFachwahl.schuelerID);
		}


		// #################### KursblockungInputRegel ####################

		for (final @NotNull GostBlockungRegel iRegel : input.daten().regeln) {
			DeveloperNotificationException.ifNull("iRegel", iRegel);
			DeveloperNotificationException.ifNull("iRegel.parameter", iRegel.parameter);
			DeveloperNotificationException.ifInvalidID("iRegel.id", iRegel.id);
			final @NotNull GostKursblockungRegelTyp gostRegel = GostKursblockungRegelTyp.fromTyp(iRegel.typ);

			final @NotNull Long @NotNull [] daten = iRegel.parameter.toArray(new Long[0]);
			for (int i = 0; i < daten.length; i++)
				DeveloperNotificationException.ifNull("daten[" + i + "]", daten[i]);

			switch (gostRegel) {
				case KURSART_SPERRE_SCHIENEN_VON_BIS:
					schritt01FehlerBeiReferenzen_Regeltyp1(daten, setKursarten, schienenAnzahl);
					break;
				case KURS_FIXIERE_IN_SCHIENE:
					schritt01FehlerBeiReferenzen_Regeltyp2(daten, setKurse, schienenAnzahl);
					break;
				case KURS_SPERRE_IN_SCHIENE:
					schritt01FehlerBeiReferenzen_Regeltyp3(daten, setKurse, schienenAnzahl);
					break;
				case SCHUELER_FIXIEREN_IN_KURS:
					schritt01FehlerBeiReferenzen_Regeltyp4(daten, setSchueler, setKurse);
					break;
				case SCHUELER_VERBIETEN_IN_KURS:
					schritt01FehlerBeiReferenzen_Regeltyp5(daten, setSchueler, setKurse);
					break;
				case KURSART_ALLEIN_IN_SCHIENEN_VON_BIS:
					schritt01FehlerBeiReferenzen_Regeltyp6(daten, setKursarten, schienenAnzahl);
					break;
				case KURS_VERBIETEN_MIT_KURS:
					schritt01FehlerBeiReferenzen_Regeltyp7(daten, setKurse);
					break;
				case KURS_ZUSAMMEN_MIT_KURS:
					schritt01FehlerBeiReferenzen_Regeltyp8(daten, setKurse);
					break;
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN:
					schritt01FehlerBeiReferenzen_Regeltyp9(daten, setKurse);
					break;
				case LEHRKRAEFTE_BEACHTEN:
					schritt01FehlerBeiReferenzen_Regeltyp10(daten);
					break;
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH:
					schritt01FehlerBeiReferenzen_Regeltyp11(daten, setSchueler, setFaecher);
					break;
				case SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH:
					schritt01FehlerBeiReferenzen_Regeltyp12(daten, setSchueler, setFaecher);
					break;
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER:
					schritt01FehlerBeiReferenzen_Regeltyp13(daten, setSchueler);
					break;
				case SCHUELER_VERBIETEN_MIT_SCHUELER:
					schritt01FehlerBeiReferenzen_Regeltyp14(daten, setSchueler);
					break;
				case KURS_MAXIMALE_SCHUELERANZAHL:
					schritt01FehlerBeiReferenzen_Regeltyp15(daten, setKurse);
					break;
				case SCHUELER_IGNORIEREN:
					schritt01FehlerBeiReferenzen_Regeltyp16(daten, setSchueler);
					break;
				case KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN:
					schritt01FehlerBeiReferenzen_Regeltyp17(daten, setKurse);
					break;
				case FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE:
					schritt01FehlerBeiReferenzen_Regeltyp18(daten, setFaecher, setKursarten);
					break;
				default:
					throw new DeveloperNotificationException("Unbekannter Regeltyp!");
			}

		}

	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp1(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Integer> setKursarten,
			final int schienenAnzahl) {

		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=%d, statt 3!".formatted(length),
				length != 3
		);

		final int kursartID = daten[0].intValue();
		final int von = daten[1].intValue(); // Schiene ist 1-indiziert!
		final int bis = daten[2].intValue(); // Schiene ist 1-indiziert!

		DeveloperNotificationException.ifSetNotContains(
				"KURSART_SPERRE_SCHIENEN_VON_BIS(%d, %d, %d) Kursart fehlt in 'setKursarten'."
						.formatted(kursartID, von, bis),
				setKursarten, kursartID);

		DeveloperNotificationException.ifTrue(
				"KURSART_SPERRE_SCHIENEN_VON_BIS(%d,%d,%d) Parameter sind unlogisch!".formatted(kursartID, von, bis),
				!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl))
		);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp2(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse,
			final int schienenAnzahl) {

		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURS_FIXIERE_IN_SCHIENE daten.length=%d, statt 2!"
						.formatted(length),
				length != 2);

		final long kursID = daten[0];
		final int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!

		DeveloperNotificationException.ifSetNotContains(
				"KURS_FIXIERE_IN_SCHIENE(%d, %d): KursID fehlt in 'setKurse'."
						.formatted(kursID, schiene),
				setKurse, kursID);

		DeveloperNotificationException.ifTrue(
				"KURS_FIXIERE_IN_SCHIENE(%d, %d) ist unlogisch!"
						.formatted(kursID, schiene),
				!((schiene >= 1) && (schiene <= schienenAnzahl)));
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp3(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse,
			final int schienenAnzahl) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURS_SPERRE_IN_SCHIENE daten.length=%d, statt 2!".formatted(length),
				length != 2);

		final long kursID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);

		final int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!
		DeveloperNotificationException.ifTrue(
				"KURS_SPERRE_IN_SCHIENE (%d, %d) ist unlogisch!".formatted(kursID, schiene),
				!((schiene >= 1) && (schiene <= schienenAnzahl)));
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp4(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
			final @NotNull HashSet<Long> setKurse) {

		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"SCHUELER_FIXIEREN_IN_KURS daten.length=%d, statt 2!"
						.formatted(length),
				length != 2);

		final long schuelerID = daten[0];
		final long kursID = daten[1];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_FIXIEREN_IN_KURS(%d, %d): Schüler-ID fehlt in 'setSchueler'."
						.formatted(schuelerID, kursID),
				setSchueler, schuelerID);
		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_FIXIEREN_IN_KURS(%d, %d): Kurs-ID fehlt in 'setKurse'."
						.formatted(schuelerID, kursID),
				setKurse, kursID);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp5(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
	        final @NotNull HashSet<Long> setKurse) {
	    final int length = daten.length;
	    DeveloperNotificationException.ifTrue(
	    		"SCHUELER_VERBIETEN_IN_KURS daten.length=%d, statt 2!".formatted(length),
	    		length != 2);

	    final long schuelerID = daten[0];
	    DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID);

	    final long kursID = daten[1];
	    DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp6(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Integer> setKursarten,
			final int schienenAnzahl) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=%d, statt 3!".formatted(length),
				length != 3);

		final int kursartID = daten[0].intValue();
		DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, kursartID);

		final int von = daten[1].intValue(); // Schiene ist 1-indiziert!
		final int bis = daten[2].intValue(); // Schiene ist 1-indiziert!
		DeveloperNotificationException.ifTrue(
				"KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (%d, %d, %d) ist unlogisch!".formatted(kursartID, von, bis),
				!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp7(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURS_VERBIETEN_MIT_KURS daten.length=%d, statt 2!".formatted(length),
				length != 2);

		final long kursID1 = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID1);

		final long kursID2 = daten[1];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID2);

		DeveloperNotificationException.ifTrue(
				"Die Regel 'KURS_VERBIETEN_MIT_KURS' wurde mit einem Kurs (%d) und sich selbst kombiniert!".formatted(kursID1),
				kursID1 == kursID2);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp8(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURS_ZUSAMMEN_MIT_KURS daten.length=%d, statt 2!".formatted(length),
				length != 2);

		final long kursID1 = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID1);

		final long kursID2 = daten[1];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID2);

		DeveloperNotificationException.ifTrue(
				"Die Regel 'KURS_ZUSAMMEN_MIT_KURS' wurde mit einem Kurs (%d) und sich selbst kombiniert!".formatted(kursID1),
				kursID1 == kursID2);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp9(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURS_MIT_DUMMY_SUS_AUFFUELLEN daten.length=%d, statt 2!".formatted(length),
				length != 2);

		final long kursID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);

		final int dummySuS = daten[1].intValue();
		DeveloperNotificationException.ifSmaller("dummySuS", dummySuS, 1);
		DeveloperNotificationException.ifGreater("dummySuS", dummySuS, 99);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp10(final @NotNull Long @NotNull [] daten) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"LEHRKRAEFTE_BEACHTEN daten.length=%d, statt 0!".formatted(length),
				length != 0);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp11(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
			final @NotNull HashSet<Long> setFaecher) {

		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH daten.length=%d, statt 3!".formatted(length),
				length != 3);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];
		final long fachID = daten[2];
		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID1 fehlt in 'setSchueler'."
						.formatted(schuelerID1, schuelerID2, fachID),
				setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID2 fehlt in 'setSchueler'."
						.formatted(schuelerID1, schuelerID2, fachID),
				setSchueler, schuelerID2);
		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Fach-ID fehlt in 'setFaecher'."
						.formatted(schuelerID1, schuelerID2, fachID),
				setFaecher, fachID);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp12(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
			final @NotNull HashSet<Long> setFaecher) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH daten.length=%d, statt 3!".formatted(length),
				length != 3);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID2);

		final long fachID = daten[2];
		DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, fachID);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp13(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER daten.length=%d, statt 2!".formatted(length),
				length != 2);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID2);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp14(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"SCHUELER_VERBIETEN_MIT_SCHUELER daten.length=%d, statt 2!".formatted(length),
				length != 2);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID1);
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID2);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp15(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURS_MAXIMALE_SCHUELERANZAHL daten.length=%d, statt 2!".formatted(length),
				length != 2);

		final long kursID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);

		final int anzahlSus = daten[1].intValue();
		DeveloperNotificationException.ifSmaller("anzahlSus", anzahlSus, 0);
		DeveloperNotificationException.ifGreater("anzahlSus", anzahlSus, 100);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp16(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"SCHUELER_IGNORIEREN daten.length=%d, statt 1!".formatted(length),
				length != 1);

		final long schuelerID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp17(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN daten.length=%d, statt 1!".formatted(length),
				length != 1);

		final long kursID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
	}


	private static void schritt01FehlerBeiReferenzen_Regeltyp18(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setFaecher,
			final @NotNull HashSet<Integer> setKursarten) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE daten.length=%d, statt 3!".formatted(length),
				length != 3);

		final long fachID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, fachID);

		final int kursartID = daten[1].intValue();
		DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, kursartID);

		final int fachArtProSchieneMaximal = daten[2].intValue();
		DeveloperNotificationException.ifSmaller("fachArtProSchieneMaximal", fachArtProSchieneMaximal, 1);
		DeveloperNotificationException.ifGreater("fachArtProSchieneMaximal", fachArtProSchieneMaximal, 9);
	}


	private void fehlerBeiRegelGruppierung(final @NotNull List<GostBlockungRegel> pRegeln) {
		// Regeln nach ID in Listen gruppieren.
		final HashSet<Long> regelDatabaseIDs = new HashSet<>();
		for (final GostBlockungRegel iRegel : pRegeln) {
			DeveloperNotificationException.ifInvalidID("iRegel.id", iRegel.id);
			DeveloperNotificationException.ifSetAddsDuplicate("regelDatabaseIDs", regelDatabaseIDs, iRegel.id);
			// Regel zur Liste hinzufügen (ggf. Liste erzeugen).
			final @NotNull GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			MapUtils.getOrCreateArrayList(_regelMap, regelTyp).add(iRegel);
		}
	}

	private void fehlerBeiSchuelerErstellung(final @NotNull GostBlockungsdatenManager input) {
		final @NotNull HashSet<Long> setSchueler = new HashSet<>();

		// Schüler sammeln.
		for (final @NotNull Schueler gSchueler : input.daten().schueler)
			setSchueler.add(gSchueler.id);

		// Schüler-Fachwahlen Überprüfen.
		for (final @NotNull GostFachwahl fachwahl : input.daten().fachwahlen)
			DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, fachwahl.schuelerID);

		final int nSchueler = setSchueler.size();
		final int nSchienen = input.schieneGetAnzahl();
		final int nKurse = input.kursGetAnzahl();

		_schuelerArr = new KursblockungDynSchueler[nSchueler];
		int i = 0;
		for (final long sID : setSchueler) {
			final @NotNull KursblockungDynSchueler schueler = new KursblockungDynSchueler(_logger, _random, sID, _statistik, nSchienen, nKurse, i);
			_schuelerArr[i] = schueler;
			_schuelerMap.put(sID, schueler);
			i++;
		}
	}

	private void fehlerBeiFachartenErstellung(final @NotNull GostBlockungsdatenManager input, final int nSchueler, final int nSchienen) {
		int nFacharten = 0;

		// Facharten aus Kursen extrahieren.
		final int nKurse = input.daten().kurse.size();
		for (final @NotNull GostBlockungKurs gKurs : input.daten().kurse) {
			final @NotNull GostFach fach = input.faecherManager().getOrException(gKurs.fach_id);
			final @NotNull GostKursart kursart = GostKursart.fromID(gKurs.kursart);

			KursblockungDynFachart dynFachart = _fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(_random, nFacharten, fach, kursart, _statistik, nSchueler, nSchienen);
				_fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}

			dynFachart.aktionMaxKurseErhoehen();
		}

		// Facharten aus SuS-Fachwahlen extrahieren.
		for (final @NotNull GostFachwahl iFachwahl : input.daten().fachwahlen) {
			final @NotNull GostFach fach = input.faecherManager().getOrException(iFachwahl.fachID);
			final @NotNull GostKursart kursart = GostKursart.fromID(iFachwahl.kursartID);

			KursblockungDynFachart dynFachart = _fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(_random, nFacharten, fach, kursart, _statistik, nSchueler, nSchienen);
				_fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}

		}

		// Keine Facharten? --> Fehler
		DeveloperNotificationException.ifSmaller("nFacharten", nFacharten, 1);

		// fachartMap --> fachartArr
		_fachartArr = new KursblockungDynFachart[nFacharten];
		for (final @NotNull KursblockungDynFachart fachart : _fachartMap2D.getNonNullValuesAsList())
			_fachartArr[fachart.gibNr()] = fachart;

		// Verteile Kurse verschwunden? --> Fehler
		int kursSumme = 0;
		for (final @NotNull KursblockungDynFachart fa : _fachartArr)
			kursSumme += fa.gibKurseMax();
		DeveloperNotificationException.ifTrue("Die Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.", kursSumme != nKurse);
	}

	private void fehlerBeiSchuelerFachwahlenErstellung(final @NotNull GostBlockungsdatenManager input,
			final @NotNull KursblockungDynSchueler @NotNull [] susArr) {
		// Schüler-ID --> Liste der Facharten
		final @NotNull HashMap<Long, List<KursblockungDynFachart>> mapSchuelerFA = new HashMap<>();

		// Fachart der Schüler-Liste hinzufügen.
		for (final @NotNull GostFachwahl iFachwahl : input.daten().fachwahlen) {
			final @NotNull KursblockungDynFachart dynFachart = gibFachart(iFachwahl.fachID, iFachwahl.kursartID);
			MapUtils.getOrCreateArrayList(mapSchuelerFA, iFachwahl.schuelerID).add(dynFachart);
		}

		// Pro Schüler Fachart-Array setzen
		for (final @NotNull KursblockungDynSchueler schueler : susArr) {
			final @NotNull List<KursblockungDynFachart> listFA = MapUtils.getOrCreateArrayList(mapSchuelerFA, schueler.gibDatenbankID());
			final @NotNull KursblockungDynFachart @NotNull [] arrFA = listFA.toArray(new KursblockungDynFachart[0]);
			schueler.aktionSetzeFachartenUndIDs(arrFA);
		}
	}

	private void fehlerBeiStatistikErstellung(final @NotNull KursblockungDynFachart @NotNull [] fachartArr,
			final @NotNull KursblockungDynSchueler @NotNull [] susArr, final @NotNull GostBlockungsdatenManager input) {
		final int nFacharten = fachartArr.length;

		final @NotNull int @NotNull [][] wahlenMatrixFachart = new int[nFacharten][nFacharten];
		final @NotNull int @NotNull [][] bewertungMatrixFachart = new int[nFacharten][nFacharten];

		// Berechne "wahlenMatrixFachart", d.h. zähle alle Fachart-Paare, die SuS gewählt haben.
		for (final @NotNull KursblockungDynSchueler s : susArr) {
			final @NotNull KursblockungDynFachart @NotNull [] fa = s.gibFacharten();
			for (int i1 = 0; i1 < fa.length; i1++) {
				final int nr1 = fa[i1].gibNr();
				for (int i2 = i1 + 1; i2 < fa.length; i2++) {
					final int nr2 = fa[i2].gibNr();
					wahlenMatrixFachart[nr1][nr2]++;
					wahlenMatrixFachart[nr2][nr1]++;
				}
			}
		}

		// Berechne "bewertungMatrixFachart".
		final int cMALUS_KOLLISION = 10000;
		final int cMALUS_DIAGONALE = 1000;
		for (int i1 = 0; i1 < nFacharten; i1++)
			for (int i2 = 0; i2 < nFacharten; i2++) {
				final int kurseVonFachart1 = fachartArr[i1].gibKurseMax();
				final int kurseVonFachart2 = fachartArr[i2].gibKurseMax();
				final int nr1 = fachartArr[i1].gibNr();
				final int nr2 = fachartArr[i2].gibNr();
				bewertungMatrixFachart[nr1][nr2] = (nr1 == nr2) ? cMALUS_DIAGONALE : 0;
				if ((wahlenMatrixFachart[nr1][nr2] == 0) || (kurseVonFachart1 == 0) || (kurseVonFachart2 == 0))
					continue;
				// Ab hier: Es gibt von beiden Facharten mindestens einen Kurs
				//          und es gibt mindestens einen S. der beide Fächer gewählt hat.
				final int nenner = kurseVonFachart1 * kurseVonFachart2; // mindestens 1
				bewertungMatrixFachart[nr1][nr2] += cMALUS_KOLLISION / nenner;
			}

		_statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length, input.kursGetAnzahl());
	}

	private void fehlerBeiSchienenErzeugung(final int nSchienen) {
		_schienenArr = new KursblockungDynSchiene[nSchienen];
		for (int nr = 0; nr < nSchienen; nr++)
			_schienenArr[nr] = new KursblockungDynSchiene(_logger, nr, _statistik);
	}

	private void fehlerBeiKursErstellung(final @NotNull GostBlockungsdatenManager input, final int nSchueler) {
		final int nKurse = input.kursGetAnzahl();
		final int nSchienen = input.schieneGetAnzahl();

		_kursArr = new KursblockungDynKurs[nKurse];
		int i = 0;
		for (final @NotNull GostBlockungKurs kurs : input.daten().kurse) {
			final @NotNull KursblockungDynKurs dynKurs = schritt08FehlerBeiKursErstellungErzeuge(input, kurs, nSchienen, i, nSchueler);
			_kursArr[i] = dynKurs;
			DeveloperNotificationException.ifMapPutOverwrites(_kursMap, kurs.id, dynKurs);
			i++;
		}

	}

	private @NotNull KursblockungDynKurs schritt08FehlerBeiKursErstellungErzeuge(final @NotNull GostBlockungsdatenManager input,
			final @NotNull GostBlockungKurs kurs, final int nSchienen, final int kursNr, final int nSchueler) {
		// Fehler: Kurs belegt zu wenig Schienen.
		DeveloperNotificationException.ifSmaller(
				"Der Kurs mit ID=%d und NR=%d belegt zu wenig (%d) Schienen!".formatted(kurs.id, kursNr, kurs.anzahlSchienen),
				kurs.anzahlSchienen, 1);

		// Fehler: Kurs belegt zu viele Schienen.
		DeveloperNotificationException.ifGreater(
				"Der Kurs mit ID=%d und NR=%d belegt zu viele (%d) Schienen!".formatted(kurs.id, kursNr, kurs.anzahlSchienen),
				kurs.anzahlSchienen, _schienenArr.length);

		// Alle Schienen, in denen der Kurs gerade ist. Anfangs leer.
		final @NotNull List<KursblockungDynSchiene> schieneLage = new ArrayList<>();

		// 'Frei' beinhaltet zunächst alle Schienen permutiert.
		final @NotNull List<KursblockungDynSchiene> schieneFrei = ListUtils.getCopyAsArrayListPermuted(_schienenArr, _random);

		// Regel 1: Entferne alle Schienen, die durch die Kursart gesperrt sind.
		for (final @NotNull GostBlockungRegel regel1 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			if (kurs.kursart == regel1.parameter.get(0)) {
				final int von = regel1.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
				final int bis = regel1.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
				for (int schiene = von; schiene <= bis; schiene++)
					schieneFrei.remove(_schienenArr[schiene - 1]); // Intern 0-indiziert!
			}

		// Regel 6: Entferne alle Schienen, die durch die Kursart gesperrt sind.
		for (final @NotNull GostBlockungRegel regel6 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			final boolean kursartStimmt = kurs.kursart == regel6.parameter.get(0);
			final int von = regel6.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
			final int bis = regel6.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
			for (int schiene = 1; schiene <= nSchienen; schiene++) {
				final boolean innerhalb = (von <= schiene) && (schiene <= bis);
				if (innerhalb != kursartStimmt)
					schieneFrei.remove(_schienenArr[schiene - 1]); // Intern 0-indiziert!
			}
		}

		// Regel 3: Entferne alle Schienen, die explizit gesperrt sind.
		for (final @NotNull GostBlockungRegel regel3 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE))
			if (kurs.id == regel3.parameter.get(0)) {
				final int schiene = regel3.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
				schieneFrei.remove(_schienenArr[schiene - 1]); // Intern 0-indiziert!
			}

		// Regel 2: Fixiere Kurse in Schienen.
		for (final @NotNull GostBlockungRegel regel2 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if (kurs.id == regel2.parameter.get(0)) {
				final int schiene = regel2.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
				final @NotNull KursblockungDynSchiene dynSchiene = _schienenArr[schiene - 1]; // Intern 0-indiziert!
				if (schieneLage.contains(dynSchiene))
					continue; // Doppelt-Fixierungen ignorieren
				UserNotificationException.ifTrue("Die Regel 'KURS_FIXIERE_IN_SCHIENE' will Kurs (id=" + kurs.id + ") in Schiene (" + schiene
						+ ") fixieren, aber die Schiene wurde bereits gesperrt!", !schieneFrei.contains(dynSchiene));
				schieneFrei.remove(dynSchiene);
				schieneLage.add(dynSchiene);
			}

		// Fehler: Zu viel fixiert?
		final int anzahlFixierterSchienen = schieneLage.size();
		DeveloperNotificationException.ifGreater("kurs.anzahlSchienen", anzahlFixierterSchienen, kurs.anzahlSchienen);

		// Fülle "schieneLage" auf, bis die richtige Anzahl erreicht ist.
		while (schieneLage.size() < kurs.anzahlSchienen) {
			UserNotificationException.ifTrue(
					input.toStringKurs(kurs.id) + " hat zu viele Schienen gesperrt, so dass seine Schienenanzahl nicht erfüllt werden kann!",
					schieneFrei.isEmpty());
			final int indexLast = schieneFrei.size() - 1;
			final KursblockungDynSchiene s = schieneFrei.get(indexLast);
			if (s != null) {
				schieneFrei.remove(s);
				schieneLage.add(s);
			}
		}

		// KursblockungDynKurs-Objekt erzeugen.
		final @NotNull KursblockungDynSchiene @NotNull [] schienenLageArray = schieneLage.toArray(new KursblockungDynSchiene[0]);
		final @NotNull KursblockungDynSchiene @NotNull [] schienenFreiArray = schieneFrei.toArray(new KursblockungDynSchiene[0]);
		final @NotNull KursblockungDynFachart dynFachart = gibFachart(kurs.fach_id, kurs.kursart);

		return new KursblockungDynKurs(_random, schienenLageArray, anzahlFixierterSchienen, schienenFreiArray, kurs.id, dynFachart, _logger, kursNr, nSchueler);
	}

	private void fehlerBeiKursFreiErstellung() {
		// Zähle Kurse mit Freiheitsgraden.
		int nKursFrei = 0;
		for (final KursblockungDynKurs kurs : _kursArr)
			if (kurs.gibHatFreiheitsgrade())
				nKursFrei++;

		// Kopiere Kurse mit Freiheitsgraden.
		_kursArrFrei = new KursblockungDynKurs[nKursFrei];
		int j = 0;
		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			if (kurs.gibHatFreiheitsgrade()) {
				_kursArrFrei[j] = kurs;
				j++;
			}
	}

	private void fehlerBeiFachartKursArrayErstellung() {
		final int nFacharten = _fachartArr.length;

		// Map: Fachart-Nummer --> Liste der Kurse
		final @NotNull HashMap<Integer, List<KursblockungDynKurs>> mapFachartList = new HashMap<>();
		for (int i = 0; i < nFacharten; i++)
			mapFachartList.put(i, new ArrayList<>());

		// Pro Kurs: Der Fachart-Liste hinzufügen.
		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			final int fachartNr = kurs.gibFachart().gibNr();
			DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr).add(kurs);
		}

		// Pro Fachart: Liste zu Array konvertieren und übergeben.
		for (int fachartNr = 0; fachartNr < nFacharten; fachartNr++) {
			final List<KursblockungDynKurs> list = DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr);
			final @NotNull KursblockungDynKurs @NotNull [] tmpKursArr = list.toArray(new KursblockungDynKurs[0]);
			_fachartArr[fachartNr].aktionSetKurse(tmpKursArr);
		}

	}

	private void fehlerBeiRegel_4_oder_5_SCHUELER_KURS_VARIANTEN() {
		// Regel 4 - SCHUELER_FIXIEREN_IN_KURS
		final @NotNull HashMap<Long, List<Long>> mapSchuelerZuFixierungen = new HashMap<>();
		for (final @NotNull GostBlockungRegel regel4 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			final long schuelerID = regel4.parameter.get(0);
			final long kursID = regel4.parameter.get(1);
			MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, schuelerID).add(kursID);
			final @NotNull KursblockungDynSchueler schueler = gibSchueler(schuelerID);
			final @NotNull KursblockungDynKurs fixierterKurs = gibKurs(kursID);
			// Alle anderen Kurse der selben Fachart verbieten ...
			for (final @NotNull KursblockungDynKurs kurs : fixierterKurs.gibFachart().gibKurse())
				if (kurs == fixierterKurs)
					kurs.regel_04_setzeSchuelerFixierung(schueler.internalSchuelerID);
				else
					schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
		}

		// Regel 4 - SCHUELER_FIXIEREN_IN_KURS (Spezialfall - Pro Schüler: Alle Kurspaar-Fixierungen Kurs-Kurs-Zusammen verbieten))
		for (final long idSchueler : mapSchuelerZuFixierungen.keySet()) {
			final @NotNull List<Long> listKursIDs = MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, idSchueler);
			// Alle (sortierten) Kurs-Paarungen durchgehen und KURS_VERBIETEN_MIT_KURS definieren.
			for (int index2 = 1; index2 < listKursIDs.size(); index2++)
				for (int index1 = 0; index1 < index2; index1++) {
					final long kursID1 = listKursIDs.get(index1);
					final long kursID2 = listKursIDs.get(index2);
					final @NotNull KursblockungDynKurs kurs1 = gibKurs(kursID1);
					final @NotNull KursblockungDynKurs kurs2 = gibKurs(kursID2);
					_statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
				}
		}


		// Regel 5 - SCHUELER_VERBIETEN_IN_KURS
		for (final @NotNull GostBlockungRegel regel5 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			final long schuelerID = regel5.parameter.get(0);
			final long kursID = regel5.parameter.get(1);
			final @NotNull KursblockungDynSchueler schueler = gibSchueler(schuelerID);
			final @NotNull KursblockungDynKurs verbotenerKurs = gibKurs(kursID);
			// Kurs verbieten
			schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
		}
	}

	private void fehlerBeiRegel_7_oder_8_KURS_MIT_KURS_VARIANTEN() {
		// Regel 7 - KURS_VERBIETEN_MIT_KURS
		for (final @NotNull GostBlockungRegel regel7 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS)) {
			final long kursID1 = regel7.parameter.get(0);
			final long kursID2 = regel7.parameter.get(1);
			final @NotNull KursblockungDynKurs kurs1 = gibKurs(kursID1);
			final @NotNull KursblockungDynKurs kurs2 = gibKurs(kursID2);
			_statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
		}

		// Regel 8 - KURS_ZUSAMMEN_MIT_KURS
		for (final @NotNull GostBlockungRegel regel8 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS)) {
			final long kursID1 = regel8.parameter.get(0);
			final long kursID2 = regel8.parameter.get(1);
			final @NotNull KursblockungDynKurs kurs1 = gibKurs(kursID1);
			final @NotNull KursblockungDynKurs kurs2 = gibKurs(kursID2);
			_statistik.regelHinzufuegenKursZusammenMitKurs(kurs1, kurs2);
		}
	}

	private void fehlerBeiRegel_9_KURS_MIT_DUMMY_SUS_AUFFUELLEN() {
		// Regel 9 - KURS_MIT_DUMMY_SUS_AUFFUELLEN
		for (final @NotNull GostBlockungRegel regel9 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			final long kursID = regel9.parameter.get(0);
			final int susAnzahl = regel9.parameter.get(1).intValue();
			final @NotNull KursblockungDynKurs kurs = gibKurs(kursID);

			for (int i = 0; i < susAnzahl; i++)
				kurs.aktionSchuelerDummyHinzufuegen();
		}
	}

	private void fehlerBeiRegel_10_LEHRKRAEFTE_BEACHTEN(final @NotNull GostBlockungsdatenManager pInput) {
		final List<GostBlockungRegel> regelnTyp10 = MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN);
		if (regelnTyp10.isEmpty())
			return;

		DeveloperNotificationException.ifGreater("Liste of regelnTyp10", regelnTyp10.size(), 1);

		// Sammle zunächst alle potentiellen Kurse.
		final @NotNull ArrayList<GostBlockungKurs> vKurseMitLehrkraft = new ArrayList<>();
		for (final @NotNull GostBlockungKurs gKurs : pInput.daten().kurse)
			if (!gKurs.lehrer.isEmpty())
				vKurseMitLehrkraft.add(gKurs);

		// Finde Kurse mit der selben Lehrkraft
		for (final @NotNull GostBlockungKurs gKurs1 : vKurseMitLehrkraft)
			for (final @NotNull GostBlockungKurs gKurs2 : vKurseMitLehrkraft)
				if (gKurs1.id < gKurs2.id)
					for (final @NotNull GostBlockungKursLehrer gLehr1 : gKurs1.lehrer)
						for (final @NotNull GostBlockungKursLehrer gLehr2 : gKurs2.lehrer)
							if (gLehr1.id == gLehr2.id) {
								final @NotNull KursblockungDynKurs kurs1 = gibKurs(gKurs1.id);
								final @NotNull KursblockungDynKurs kurs2 = gibKurs(gKurs2.id);
								_statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
							}
	}

	private void fehlerBeiRegel_11_bis_14_SCHUELER_MIT_SCHUELER_VARIANTEN(final @NotNull GostBlockungsdatenManager input) {
		// Das Set dient dazu, Widersprüche/Dopplungen bei den Regeln zu finden.
		final @NotNull HashSet<LongArrayKey> setSSF = new HashSet<>();
		final @NotNull String fehlermeldungDopplung = "Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!";

		// Regel 11: SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH
		for (final @NotNull GostBlockungRegel regel11 : MapUtils.getOrCreateArrayList(_regelMap,
				GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH)) {
			final long idS1 = regel11.parameter.get(0);
			final long idS2 = regel11.parameter.get(1);
			final long idF = regel11.parameter.get(2);
			final @NotNull LongArrayKey key12F = new LongArrayKey(idS1, idS2, idF);
			final @NotNull LongArrayKey key21F = new LongArrayKey(idS2, idS1, idF);
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
			// Regel 11 persistieren
			final @NotNull KursblockungDynSchueler sch1 = gibSchueler(idS1);
			final @NotNull KursblockungDynSchueler sch2 = gibSchueler(idS2);
			sch1.regel_11_zusammen_mit_schueler_in_fach(sch2, idF);
		}

		// Regel 12: SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH
		for (final @NotNull GostBlockungRegel regel12 : MapUtils.getOrCreateArrayList(_regelMap,
				GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH)) {
			final long idS1 = regel12.parameter.get(0);
			final long idS2 = regel12.parameter.get(1);
			final long idF = regel12.parameter.get(2);
			final @NotNull LongArrayKey key12F = new LongArrayKey(idS1, idS2, idF);
			final @NotNull LongArrayKey key21F = new LongArrayKey(idS2, idS1, idF);
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
			DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
			// Regel 12 persistieren
			final @NotNull KursblockungDynSchueler sch1 = gibSchueler(idS1);
			final @NotNull KursblockungDynSchueler sch2 = gibSchueler(idS2);
			sch1.regel_12_verbieten_mit_schueler_in_fach(sch2, idF);
		}

		// Regel 13: SCHUELER_ZUSAMMEN_MIT_SCHUELER
		for (final @NotNull GostBlockungRegel regel13 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER)) {
			final long idS1 = regel13.parameter.get(0);
			final long idS2 = regel13.parameter.get(1);
			for (final @NotNull GostFach fach : input.schuelerGetFachListeGemeinsamerFacharten(idS1, idS2)) {
				final @NotNull LongArrayKey key12F = new LongArrayKey(idS1, idS2, fach.id);
				final @NotNull LongArrayKey key21F = new LongArrayKey(idS2, idS1, fach.id);
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
				// Regel 13 persistieren
				final @NotNull KursblockungDynSchueler sch1 = gibSchueler(idS1);
				final @NotNull KursblockungDynSchueler sch2 = gibSchueler(idS2);
				sch1.regel_13_zusammen_mit_schueler(sch2);
			}
		}

		// Regel 14: SCHUELER_VERBIETEN_MIT_SCHUELER
		for (final @NotNull GostBlockungRegel r14 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER)) {
			final long idS1 = r14.parameter.get(0);
			final long idS2 = r14.parameter.get(1);
			for (final @NotNull GostFach fach : input.schuelerGetFachListeGemeinsamerFacharten(idS1, idS2)) {
				final @NotNull LongArrayKey key12F = new LongArrayKey(idS1, idS2, fach.id);
				final @NotNull LongArrayKey key21F = new LongArrayKey(idS2, idS1, fach.id);
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key12F));
				DeveloperNotificationException.ifTrue(fehlermeldungDopplung, !setSSF.add(key21F));
			}
			// Regel 14 persistieren
			final @NotNull KursblockungDynSchueler sch1 = gibSchueler(idS1);
			final @NotNull KursblockungDynSchueler sch2 = gibSchueler(idS2);
			sch1.regel_14_verbieten_mit_schueler(sch2);
		}

	}

	private void fehlerBeiRegel_15_KURS_MAXIMALE_SCHUELERANZAHL() {
		for (final @NotNull GostBlockungRegel r15 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL)) {
			final long idKurs = r15.parameter.get(0);
			final int maxSuS = r15.parameter.get(1).intValue();

			final @NotNull KursblockungDynKurs kurs = gibKurs(idKurs);
			kurs.regel_15_setzeMaxSuS(maxSuS);
		}
	}

	private void fehlerBeiRegel_16_SCHUELER_IGNORIEREN() {
		for (final @NotNull GostBlockungRegel r16 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			final long idSchueler = r16.parameter.get(0);

			final @NotNull KursblockungDynSchueler schueler = gibSchueler(idSchueler);
			schueler.regel_16_sperre();
		}
	}

	private void fehlerBeiRegel_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE() {
		for (final @NotNull GostBlockungRegel r18 : MapUtils.getOrCreateArrayList(_regelMap,
				GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE)) {
			final long idFach = r18.parameter.get(0);
			final int idKursart = r18.parameter.get(1).intValue();
			final int maximalProSchiene = r18.parameter.get(2).intValue();

			final @NotNull KursblockungDynFachart fachart = gibFachart(idFach, idKursart);
			fachart.regel_18_maximalProSchiene(maximalProSchiene);
		}
	}

	private @NotNull KursblockungDynFachart gibFachart(final long fachID, final int kursart) {
		return _fachartMap2D.getOrException(fachID, kursart);
	}

	private @NotNull KursblockungDynSchueler gibSchueler(final long schuelerID) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerMap, schuelerID);
	}

	private @NotNull KursblockungDynKurs gibKurs(final long kursID) {
		return DeveloperNotificationException.ifMapGetIsNull(_kursMap, kursID);
	}

	// ########################################
	// ############## PROTECTED ###############
	// ########################################

	/**
	 * Liefert ein neu erzeugtes {@link GostBlockungsergebnisManager}-Objekt.
	 * <br>Dieses Objekt beinhaltet alle Informationen für die GUI.
	 *
	 * @param  pDataManager  Das Eingabe-Objekt (der Daten-Manager).
	 * @param  pErgebnisID   Die ID des Ergebnisses.
	 *
	 * @return ein neu erzeugtes {@link GostBlockungsergebnisManager}-Objekt.
	 */
	@NotNull
	GostBlockungsergebnisManager gibErzeugtesKursblockungOutput(final @NotNull GostBlockungsdatenManager pDataManager, final long pErgebnisID) {
		final @NotNull GostBlockungsergebnisManager out = new GostBlockungsergebnisManager(pDataManager, pErgebnisID);

		// Erzeuge die Kurs-Schienen-Zuordnungen. Verwende Update-Objekte, da nur eine Regelvalidierung am Ende erfolgt.
		final @NotNull Set<GostBlockungsergebnisKursSchienenZuordnung> kursSchienenZuordnungen = new HashSet<>();
		for (final @NotNull KursblockungDynKurs dynKurs : _kursArr)
			for (final int schienenNr : dynKurs.gibSchienenLage()) {
				final long idKurs = dynKurs.gibDatenbankID();
				final long idSchiene = out.getOfSchieneID(schienenNr + 1); // Manager hat eine 1-Indizierung der Schiene!
				kursSchienenZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchiene));
			}

		// UPDATE - Kurs - Schiene
		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate uKursSchienen =
				out.kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(kursSchienenZuordnungen);
		out.kursSchienenUpdateExecute(uKursSchienen);

		// Erzeuge die Kurs-Schüler-Zuordnungen. Verwende Update-Objekte, da nur eine Regelvalidierung am Ende erfolgt.
		final @NotNull Set<GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen = new HashSet<>();
		for (final @NotNull KursblockungDynSchueler dynSchueler : _schuelerArr)
			for (final KursblockungDynKurs kurs : dynSchueler.gibKurswahlen())
				if (kurs != null) {
					// Set verhindert Duplikate (sollte hier nicht vorkommen).
					final long idKurs = kurs.gibDatenbankID();
					final long idSchueler = dynSchueler.gibDatenbankID();
					kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}

		// Erzeuge durch Regeln forcierte Schüler-Kurs-Zuordnungen.
		// Das kann später zu Kollisionen führen, was aber richtig ist.
		for (final @NotNull GostBlockungRegel gRegel : pDataManager.regelGetListe())
			if (gRegel.typ == GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				// Set verhindert Duplikate (das ist in diesem Fall möglich).
				final long idSchueler = gRegel.parameter.get(0);
				final long idKurs = gRegel.parameter.get(1);
				kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
			}

		// UPDATE - Kurs - Schüler
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate uKursSchueler =
				out.kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(kursSchuelerZuordnungen);
		out.kursSchuelerUpdateExecute(uKursSchueler);

		return out;
	}

	/**
	 * Liefert das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 *
	 * @return Das Logger-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 */
	@NotNull
	Logger gibLogger() {
		return _logger;
	}

	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	@NotNull
	Random gibRandom() {
		return _random;
	}

	/**
	 * Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 *
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 */
	public @NotNull KursblockungDynStatistik gibStatistik() {
		return _statistik;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 *
	 * @return Liefert die maximale Blockungszeit in Millisekunden.
	 */
	long gibBlockungszeitMillis() {
		return _maxTimeMillis;
	}

	/**
	 * Liefert die maximal erlaubte Anzahl an Schienen. Entweder handelt es sich um einen Standardwert oder der Wert
	 * wurde im Konstruktor als Regel übergeben.
	 *
	 * @return Liefert die maximal erlaubte Anzahl an Schienen.
	 */
	public int gibSchienenAnzahl() {
		return _schienenArr.length;
	}

	/**
	 * Liefert alle Kurse.
	 *
	 * @return Array aller Kurse.
	 */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseAlle() {
		return _kursArr;
	}

	/**
	 * Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseDieFreiSind() {
		return _kursArrFrei;
	}

	/**
	 * Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	int gibKurseDieFreiSindAnzahl() {
		return _kursArrFrei.length;
	}

	/**
	 * Liefert einen Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht. Je kleiner der Wert, desto besser ist
	 * die Bewertung.
	 *
	 * @return Long-Wert, der einer Bewertung der Fachwahlmatrix entspricht.
	 */
	long gibBewertungFachartPaar() {
		return _statistik.gibBewertungFachartPaar();
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 * Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @return ein Array aller Schülerinnen und Schüler.
	 */
	@NotNull
	KursblockungDynSchueler @NotNull [] gibSchuelerArray(final boolean pNurMultiKurse) {
		if (pNurMultiKurse) {
			final @NotNull LinkedCollection<KursblockungDynSchueler> list = new LinkedCollection<>();
			for (final KursblockungDynSchueler schueler : _schuelerArr)
				if (schueler.gibHatMultikurs())
					list.addLast(schueler);

			final @NotNull KursblockungDynSchueler @NotNull [] temp = new KursblockungDynSchueler[list.size()];
			for (int i = 0; i < temp.length; i++)
				temp[i] = list.removeFirst();
			return temp;
		}
		return _schuelerArr;
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 *
	 * @return Ein Array aller Schülerinnen und Schüler.
	 */
	@NotNull
	KursblockungDynSchueler @NotNull [] gibSchuelerArrayAlle() {
		return _schuelerArr;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertungJetztBesserAlsS() {
		return _statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibCompareZustandK_NW_KD_FW() {
		return _statistik.gibCompareZustandK_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdiffenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibCompareZustandG_NW_KD_FW() {
		return _statistik.gibCompareZustandG_NW_KD_FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdiffenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdiffenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertungK_FW_NW_KD_JetztBesser() {
		return _statistik.gibCompareZustandK_FW_NW_KD();
	}

	// ########################################
	// ########### SETTER / ACTIONS ###########
	// ########################################

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdiffenzen) des Zustandes K sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertung_NW_KD_JetztS() {
		return _statistik.gibBewertungZustandS_NW_KD();
	}

	/**
	 * Liefert TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 *
	 * @param b  Das zu vergleichende Objekt.
	 *
	 * @return TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 */
	boolean gibIstBesser_NW_KD_FW_Als(final @NotNull KursblockungDynDaten b) {
		return _statistik.gibIstBesser_NW_KD_FW_Als(b._statistik);
	}

	/**
	 * Liefert true, falls der Kurs in der Schiene ist.
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @param schieneDB Die Datenbank-ID der Schiene (1-indiziert!).
	 * @return true, falls der Kurs in der Schiene ist.
	 */
	public boolean gibIstKursInSchiene(final int idKursDB, final int schieneDB) {
		for (final @NotNull KursblockungDynKurs k : _kursArr)
			if ((k.gibDatenbankID() == idKursDB) && (k.gibIstInSchiene(schieneDB - 1)))
				return true;
		return false;
	}

	/**
	 * Liefert true, falls der Schüler im Kurs ist.
	 *
	 * @param idSchuelerDB Die Datenbank-ID des Schülers.
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return true, falls der Kurs in der Schiene ist.
	 */
	public boolean gibIstSchuelerInKurs(final int idSchuelerDB, final int idKursDB) {
		for (final @NotNull KursblockungDynKurs k : _kursArr)
			if (k.gibDatenbankID() == idKursDB)
				for (final @NotNull KursblockungDynSchueler s : _schuelerArr)
					if ((s.gibDatenbankID() == idSchuelerDB) && (s.gibIstInKurs(k)))
						return true;
		return false;
	}

	/**
	 * Liefert true, falls die übergebene Schiene nur LK-Kurse enthält (oder keine Kurse).
	 *
	 * @param schienenNr1indiziert  Die Schienen-Nummer
	 * @return true, falls die übergebene Schiene nur LK-Kurse enthält (oder keine Kurse).
	 */
	public boolean gibHatSchieneNurLK(final int schienenNr1indiziert) {
		return _schienenArr[schienenNr1indiziert - 1].gibHatNurLK();
	}

	/**
	 * Liefert true, falls die übergebene Schiene keine LK-Kurse enthält.
	 *
	 * @param schienenNr1indiziert  Die Schienen-Nummer
	 * @return true, falls die übergebene Schiene keine LK-Kurse enthält.
	 */
	public boolean gibHatSchieneKeineLK(final int schienenNr1indiziert) {
		return _schienenArr[schienenNr1indiziert - 1].gibHatKeineLK();
	}

	/**
	 * Liefert die Anzahl an SuS in dem Kurs (oder -1 falls der Kurs nicht existiert).
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return die Anzahl an SuS in dem Kurs (oder -1 falls der Kurs nicht existiert).
	 */
	public int gibKursgroesseDesKurses(final int idKursDB) {
		for (final @NotNull KursblockungDynKurs k : _kursArr)
			if (k.gibDatenbankID() == idKursDB)
				return k.gibSchuelerAnzahl();
		return -1;
	}

	/**
	 * Liefert die Kursdifferenz der Fachart des übergebenen Kurses (oder -1 falls der Kurs nicht existiert)..
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return die Kursdifferenz der Fachart des übergebenen Kurses (oder -1 falls der Kurs nicht existiert)..
	 */
	public int gibKursdifferenzDesKurses(final int idKursDB) {
		for (final @NotNull KursblockungDynKurs k : _kursArr)
			if (k.gibDatenbankID() == idKursDB)
				return k.gibFachart().gibKursdifferenz();
		return -1;
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S.
	 */
	void aktionZustandSpeichernS() {
		_statistik.aktionBewertungSpeichernS();

		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			kurs.aktionZustandSpeichernS();

		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionZustandSpeichernS();
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K.
	 */
	void aktionZustandSpeichernK() {
		_statistik.aktionBewertungSpeichernK();

		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			kurs.aktionZustandSpeichernK();

		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionZustandSpeichernK();
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G.
	 */
	void aktionZustandSpeichernG() {
		_statistik.aktionBewertungSpeichernG();

		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			kurs.aktionZustandSpeichernG();

		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionZustandSpeichernG();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung).
	 */
	void aktionZustandLadenS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionKurseAlleEntfernen();

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			kurs.aktionZustandLadenS();

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionZustandLadenS();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung).
	 */
	void aktionZustandLadenK() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionKurseAlleEntfernen();

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			kurs.aktionZustandLadenK();

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionZustandLadenK();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K einer anderen {@link KursblockungDynDaten}-Objekts (Kursverteilung und Schülerverteilung).
	 *
	 * @param b  Das andere {@link KursblockungDynDaten}-Objekt.
	 */
	void aktionZustandLadenVon(final @NotNull KursblockungDynDaten b) {
		if (this == b) {
			System.out.println("Identisch!");
			return;
		}

		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionKurseAlleEntfernen();

		// 2) Dann Kurse verschieben
		for (int i = 0; i < _kursArr.length; i++)
			_kursArr[i].aktionZustandLadenVon(b._kursArr[i], _schienenArr);

		// 3) Dann SuS den Kursen hinzufügen.
		for (int i = 0; i < _schuelerArr.length; i++)
			_schuelerArr[i].aktionZustandLadenVon(b._schuelerArr[i], _kursArr);
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung).
	 */
	void aktionZustandLadenG() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionKurseAlleEntfernen();

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			kurs.aktionZustandLadenG();

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionZustandLadenG();
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung).
	 */
	void aktionZustandLadenKohneSuS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			schueler.aktionKurseAlleEntfernen();

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr)
			kurs.aktionZustandLadenK();
	}

	/**
	 * Entfernt alle SuS aus ihren Kursen.
	 */
	void aktionSchuelerAusAllenKursenEntfernen() {
		for (final @NotNull KursblockungDynSchueler s : _schuelerArr)
			s.aktionKurseAlleEntfernen();
	}

	/**
	 * Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	void aktionKurseFreieZufaelligVerteilen() {
		for (final @NotNull KursblockungDynKurs kurs : _kursArrFrei)
			kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt genau einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	void aktionKursVerteilenEinenZufaelligenFreien() {
		if (_kursArrFrei.length == 0)
			return;

		final int index = _random.nextInt(_kursArrFrei.length);
		final @NotNull KursblockungDynKurs kurs = _kursArrFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben und Multikurse, werden dabei ignoriert.
	 */
	void aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse() {
		if (_kursArrFrei.length == 0)
			return;

		final int[] perm = KursblockungStatic.gibPermutation(_random, _kursArrFrei.length);
		for (final int index : perm) {
			final @NotNull KursblockungDynKurs kurs = _kursArrFrei[index];
			if (kurs.gibSchienenAnzahl() == 1)
				kurs.aktionZufaelligVerteilen();
		}
	}

	/**
	 * Verändert die Lage der Kurse einer zufälligen Fachgruppe komplett neu.
	 */
	public void aktionKursVerteilenEineZufaelligeFachgruppe() {
		if (_fachartArr.length == 0)
			return;

		final int fachgruppenIndex = _random.nextInt(_fachartArr.length);
		for (final @NotNull KursblockungDynKurs kurs: _fachartArr[fachgruppenIndex].gibKurse())
			kurs.aktionZufaelligVerteilen();
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
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, _schuelerArr.length);
		for (final int p : perm) {
			final KursblockungDynSchueler schueler = _schuelerArr[p];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			kurslagenVeraenderung |= schueler.aktionKurseVerteilenNachDeinemWunsch();
		}

		return kurslagenVeraenderung;
	}


	/**
	 * Gesucht wird der Schüler, der unzufrieden ist. Nach seinem Wunsch werden die Kurse neuverteilt.
	 * <br> Kurzzeitig wird der S. Kursen hinzugefügt, am Ende aber wieder entfernt.
	 *
	 * @return TRUE, falls es zu einer Veränderung der Kurslage kam.
	 */
	boolean aktionKurseVerteilenNachSchuelerwunschSingle() {
		boolean kurslagenVeraenderung = false;

		// Suche einen unzufriedenen Schüler...
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, _schuelerArr.length);
		for (final int p : perm) {
			final KursblockungDynSchueler schueler = _schuelerArr[p];
			// Berechne die Nichtwahlen des Schülers.
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
			final int nichtwahlen = schueler.gibNichtwahlen();
			schueler.aktionKurseAlleEntfernen();

			if (nichtwahlen > 0) {
				schueler.aktionKurseVerteilenNurMultikurseZufaellig();
				kurslagenVeraenderung |= schueler.aktionKurseVerteilenNachDeinemWunsch();
				schueler.aktionKurseAlleEntfernen();
				break;
			}

		}

		return kurslagenVeraenderung;
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines bipartiten Matching-Algorithmus verteilt. Bereits belegte Facharten werden übersprungen.
	 */
	public void aktionSchuelerVerteilenMitBipartitemMatching() {
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, _schuelerArr.length);

		for (final int i : perm) {
			final KursblockungDynSchueler schueler = _schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatching();
		}
	}

	/**
	 * Verteilt die SuS auf die jetzige Kurslage. Pro S. werden erst die Multikurse verteilt, dann werden die übrigen
	 * Kurse mit Hilfe eines gewichteten Bipartiten-Matching-Algorithmus verteilt.
	 */
	public void aktionSchuelerVerteilenMitGewichtetenBipartitemMatching() {
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, _schuelerArr.length);

		for (final int i : perm) {
			final KursblockungDynSchueler schueler = _schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemErlaubtenKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();
		}
	}

	/**
	 * Setzt den S. wenn möglich in den übergebenen Kurs.
	 *
	 * @param idSchuelerDB  Die Datenbank-ID des S.
	 * @param idKursDB      Die Datenbank-ID des Kurses.
	 */
	public void aktionSchuelerSetzenInKurs(final int idSchuelerDB, final int idKursDB) {
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			if (schueler.gibDatenbankID() == idSchuelerDB)
				schueler.aktionKursSetzen(idKursDB);
	}

	/**
	 * Entfernt den S. wenn möglich aus den übergebenen Kurs.
	 *
	 * @param idSchuelerDB  Die Datenbank-ID des S.
	 * @param idKursDB      Die Datenbank-ID des Kurses.
	 */
	public void aktionSchuelerEntfernenAusKurs(final int idSchuelerDB, final int idKursDB) {
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr)
			if (schueler.gibDatenbankID() == idSchuelerDB)
				schueler.aktionKursEntfernen(idKursDB);
	}

	/**
	 * Verschiebt den Kurs in die Schiene.
	 *
	 * @param idKursDB  Die Datenbank-ID des Kurses.
	 * @param schieneDB Die Datenbank-ID der Schiene (1-indiziert!).
	 */
	public void aktionSetzeKursInSchiene(final int idKursDB, final int schieneDB) {
		for (final @NotNull KursblockungDynKurs k : _kursArr)
			if (k.gibDatenbankID() == idKursDB)
				k.aktionSetzeInSchiene(schieneDB - 1);
	}

	/**
	 * Debug Ausgaben. Nur für Testzwecke.
	 */
	public void debug() {
		_logger.modifyIndent(+4);

		_logger.logLn("########## Schienen ##########");
		for (int i = 0; i < _schienenArr.length; i++) {
			_logger.logLn("Schiene " + (i + 1));
			_schienenArr[i].debug(false);
		}

		_logger.logLn("########## Facharten ##########");
		for (final @NotNull KursblockungDynFachart fa : _fachartArr) {
			_logger.logLn("Fachart " + fa + " --> " + fa.gibKursdifferenz());
			fa.debug(_schuelerArr);
		}

		_logger.modifyIndent(-4);

		_statistik.debug("");
	}

	/**
	 * Debug Ausgaben (Schienen und Kurse)
	 */
	public void printlnSchienenUndKurse() {
		for (int i = 0; i < _schienenArr.length; i++)
			_schienenArr[i].printlnKurse();
	}

	/**
	 * Debug-Ausgabe der Schienen mit ihre Kursen und ihren SuS.
	 */
	public void printlnSchienenUndKurseUndSchueler() {
		for (int i = 0; i < _schienenArr.length; i++)
			_schienenArr[i].printlnKurseUndSchueler(_schuelerArr);
	}

	/**
	 * Debug-Ausgabe aller Facharten mit den zugehörigen Kursen.
	 */
	public void printlnFacharten() {
		for (final @NotNull KursblockungDynFachart fachart : _fachartArr)
			fachart.printlnKurse();
	}

}
