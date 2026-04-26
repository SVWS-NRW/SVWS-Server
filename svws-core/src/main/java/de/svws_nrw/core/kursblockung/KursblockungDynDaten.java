package de.svws_nrw.core.kursblockung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.svws_nrw.asd.adt.PairNN;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.adt.iterator.PairIterable;
import de.svws_nrw.core.adt.iterator.PairIteratorModus;
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
import de.svws_nrw.core.logger.LogLevel;
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
	private final @NotNull Random rnd;

	/** Logger für Benutzerhinweise, Warnungen und Fehler. */
	private final @NotNull Logger log;

	/** Alle Regeln nach ihrer ID gruppiert und in einer Liste der Reihenfolge nach gespeichert. */
	private final @NotNull Map<GostKursblockungRegelTyp, List<GostBlockungRegel>> regelMap;

	/** Die maximale Blockungszeit in Millisekunden. */
	private final long maxTimeMillis;

	/** Diese Datenstruktur speichert die Schienen und ihre Kurse. */
	private @NotNull KursblockungDynSchiene @NotNull [] schienenMenge;

	/** Alles Kurse. */
	private @NotNull KursblockungDynKurs @NotNull [] kursMenge;

	/** Alle Kurse, die noch über Schienen wandern können. */
	private @NotNull KursblockungDynKurs @NotNull [] kursMengeFrei;

	/** Map für schnellen Zugriff auf die Kurse über ihre ID. */
	private final @NotNull HashMap<Long, KursblockungDynKurs> kursMap;

	/** Alle Facharten. Fachart meint Fach + Kursart, z.B. "D;GK". */
	private @NotNull KursblockungDynFachart @NotNull [] fachartMenge;

	/** Map für schnellen Zugriff auf die Facharten über FachID und KursartID. */
	private final @NotNull HashMap2D<Long, Integer, KursblockungDynFachart> fachartMap2D;

	/** Alle SuS. */
	private @NotNull KursblockungDynSchueler @NotNull [] schuelerMenge;

	/** Map für schnellen Zugriff auf die SuS über ihre ID. */
	private final @NotNull HashMap<Long, KursblockungDynSchueler> schuelerMap;

	/** Das Statistik-Objekt speichert die aktuellen Nichtwahlen, Kursdifferenzen und weitere Daten. */
	private final @NotNull KursblockungDynStatistik statistik;

	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link GostBlockungsdatenManager} ein und baut die relevanten Datenstrukturen auf.
	 *
	 * @param random   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger   Ein {@link Logger}-Objekt für Benutzerhinweise, Warnungen und Fehler.
	 * @param input    Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public KursblockungDynDaten(final @NotNull Random random, final @NotNull Logger logger, final @NotNull GostBlockungsdatenManager input) {
		rnd = random;
		log = logger;
		regelMap = new ArrayMap<>(GostKursblockungRegelTyp.values()); // Konstruktor für Enum-Typen.
		maxTimeMillis = input.getMaxTimeMillis();

		schienenMenge = new KursblockungDynSchiene[0];

		kursMenge = new KursblockungDynKurs[0];
		kursMengeFrei = new KursblockungDynKurs[0];
		kursMap = new HashMap<>();

		fachartMenge = new KursblockungDynFachart[0];
		fachartMap2D = new HashMap2D<>();

		schuelerMenge = new KursblockungDynSchueler[0];
		schuelerMap = new HashMap<>();

		statistik = new KursblockungDynStatistik(log);

		// Definiert: ---
		fehlerBeiReferenzen(input);

		// Definiert: regelMap
		fehlerBeiRegelGruppierung(input.daten().regeln);

		// Definiert: schuelerArr, susMap
		fehlerBeiSchuelerErstellung(input);

		// Definiert: schienenArr
		fehlerBeiSchienenErzeugung(input.schieneGetAnzahl());

		// Definiert: fachartArr
		fehlerBeiFachartenErstellung(input, schuelerMenge.length, schienenMenge.length);

		// Definiert: schueler[i].fachartArr
		fehlerBeiSchuelerFachwahlenErstellung(input, schuelerMenge);

		// Definiert: statistik
		fehlerBeiStatistikErstellung(fachartMenge, schuelerMenge, input);

		// Benötigt: fachartArr
		// Definiert: kursArr
		fehlerBeiKursErstellung(input, schuelerMenge.length);

		// Benötigt: kursArr
		// Definiert: kursArrFrei
		fehlerBeiKursFreiErstellung();

		// Benötigt: kursArr
		// Definiert: fachartArr[i].kursArr
		fehlerBeiFachartKursArrayErstellung();

		fehlerBeiRegel4oder5();

		fehlerBeiRegel7oder8();

		fehlerBeiRegel9();

		fehlerBeiRegel10(input);

		fehlerBeiRegel11bis14(input);

		fehlerBeiRegel15(); // Muss nach Regel 4 (Schüler-Kurs-Fixierung) passieren.

		fehlerBeiRegel16();

		fehlerBeiRegel18();

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
		for (final @NotNull Schueler gSchueler : input.daten().schueler) {
			DeveloperNotificationException.ifSetAddsDuplicate(
					"Schüler ID=%d ist bereits vorhanden in 'setSchueler'.".formatted(gSchueler.id),
					setSchueler, gSchueler.id);
		}

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
			for (int i = 0; i < daten.length; i++) {
				DeveloperNotificationException.ifNull("daten[" + i + "]", daten[i]);
			}

			switch (gostRegel) {
				case KURSART_SPERRE_SCHIENEN_VON_BIS:
					fehlerBeiReferenzenRegeltyp1(daten, setKursarten, schienenAnzahl);
					break;
				case KURS_FIXIERE_IN_SCHIENE:
					fehlerBeiReferenzenRegeltyp2(daten, setKurse, schienenAnzahl);
					break;
				case KURS_SPERRE_IN_SCHIENE:
					fehlerBeiReferenzenRegeltyp3(daten, setKurse, schienenAnzahl);
					break;
				case SCHUELER_FIXIEREN_IN_KURS:
					fehlerBeiReferenzenRegeltyp4(daten, setSchueler, setKurse);
					break;
				case SCHUELER_VERBIETEN_IN_KURS:
					fehlerBeiReferenzenRegeltyp5(daten, setSchueler, setKurse);
					break;
				case KURSART_ALLEIN_IN_SCHIENEN_VON_BIS:
					fehlerBeiReferenzenRegeltyp6(daten, setKursarten, schienenAnzahl);
					break;
				case KURS_VERBIETEN_MIT_KURS:
					fehlerBeiReferenzenRegeltyp7(daten, setKurse);
					break;
				case KURS_ZUSAMMEN_MIT_KURS:
					fehlerBeiReferenzenRegeltyp8(daten, setKurse);
					break;
				case KURS_MIT_DUMMY_SUS_AUFFUELLEN:
					fehlerBeiReferenzenRegeltyp9(daten, setKurse);
					break;
				case LEHRKRAEFTE_BEACHTEN:
					fehlerBeiReferenzenRegeltyp10(daten);
					break;
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH:
					fehlerBeiReferenzenRegeltyp11(daten, setSchueler, setFaecher);
					break;
				case SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH:
					fehlerBeiReferenzenRegeltyp12(daten, setSchueler, setFaecher);
					break;
				case SCHUELER_ZUSAMMEN_MIT_SCHUELER:
					fehlerBeiReferenzenRegeltyp13(daten, setSchueler);
					break;
				case SCHUELER_VERBIETEN_MIT_SCHUELER:
					fehlerBeiReferenzenRegeltyp14(daten, setSchueler);
					break;
				case KURS_MAXIMALE_SCHUELERANZAHL:
					fehlerBeiReferenzenRegeltyp15(daten, setKurse);
					break;
				case SCHUELER_IGNORIEREN:
					fehlerBeiReferenzenRegeltyp16(daten, setSchueler);
					break;
				case KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN:
					fehlerBeiReferenzenRegeltyp17(daten, setKurse);
					break;
				case FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE:
					fehlerBeiReferenzenRegeltyp18(daten, setFaecher, setKursarten);
					break;
				default:
					throw new DeveloperNotificationException("Unbekannter Regeltyp!");
			}

		}

	}


	private static void ueberpruefeDatenLaenge(final @NotNull String regelName, final @NotNull Long @NotNull [] daten, final int expectedLength) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue(
				"%s: daten.length=%d, statt %d!".formatted(regelName, length, expectedLength),
				length != expectedLength
		);
	}


	private static void fehlerBeiReferenzenRegeltyp1(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Integer> setKursarten,
			final int schienenAnzahl) {

		ueberpruefeDatenLaenge("KURSART_SPERRE_SCHIENEN_VON_BIS", daten, 3);

		final int kursartID = daten[0].intValue();
		final int von = daten[1].intValue(); // Schiene ist 1-indiziert!
		final int bis = daten[2].intValue(); // Schiene ist 1-indiziert!

		DeveloperNotificationException.ifSetNotContains(
				"KURSART_SPERRE_SCHIENEN_VON_BIS(%d, %d, %d): Kursart nicht vorhanden!".formatted(kursartID, von, bis),
				setKursarten, kursartID);

		DeveloperNotificationException.ifTrue(
				"KURSART_SPERRE_SCHIENEN_VON_BIS(%d, %d, %d): Parameter sind unlogisch!".formatted(kursartID, von, bis),
				!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl))
		);
	}

	private static void fehlerBeiReferenzenRegeltyp2(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse,
			final int schienenAnzahl) {

		ueberpruefeDatenLaenge("KURS_FIXIERE_IN_SCHIENE", daten, 2);

		final long kursID = daten[0];
		final int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!

		DeveloperNotificationException.ifSetNotContains(
				"KURS_FIXIERE_IN_SCHIENE(%d, %d): Kurs-ID nicht vorhanden!".formatted(kursID, schiene),
				setKurse, kursID);

		DeveloperNotificationException.ifTrue(
				"KURS_FIXIERE_IN_SCHIENE(%d, %d): Parameter sind unlogisch!".formatted(kursID, schiene),
				!((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static void fehlerBeiReferenzenRegeltyp3(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse,
			final int schienenAnzahl) {

		ueberpruefeDatenLaenge("KURS_SPERRE_IN_SCHIENE", daten, 2);

		final long kursID = daten[0];
		final int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!

		DeveloperNotificationException.ifSetNotContains(
				"KURS_SPERRE_IN_SCHIENE(%d, %d): Kurs-ID nicht vorhanden!".formatted(kursID, schiene),
				setKurse, kursID);

		DeveloperNotificationException.ifTrue(
				"KURS_SPERRE_IN_SCHIENE(%d, %d): Parameter sind unlogisch!".formatted(kursID, schiene),
				!((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static void fehlerBeiReferenzenRegeltyp4(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
			final @NotNull HashSet<Long> setKurse) {

		ueberpruefeDatenLaenge("SCHUELER_FIXIEREN_IN_KURS", daten, 2);

		final long schuelerID = daten[0];
		final long kursID = daten[1];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_FIXIEREN_IN_KURS(%d, %d): Schüler-ID nicht vorhanden!".formatted(schuelerID, kursID),
				setSchueler, schuelerID);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_FIXIEREN_IN_KURS(%d, %d): Kurs-ID nicht vorhanden!".formatted(schuelerID, kursID),
				setKurse, kursID);
	}

	private static void fehlerBeiReferenzenRegeltyp5(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
			final @NotNull HashSet<Long> setKurse) {

		ueberpruefeDatenLaenge("SCHUELER_VERBIETEN_IN_KURS", daten, 2);

		final long schuelerID = daten[0];
		final long kursID = daten[1];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_VERBIETEN_IN_KURS(%d, %d): Schüler-ID nicht vorhanden!".formatted(schuelerID, kursID),
				setSchueler, schuelerID);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_VERBIETEN_IN_KURS(%d, %d): Kurs-ID nicht vorhanden!".formatted(schuelerID, kursID),
				setKurse, kursID);
	}

	private static void fehlerBeiReferenzenRegeltyp6(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Integer> setKursarten,
			final int schienenAnzahl) {

		ueberpruefeDatenLaenge("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS", daten, 3);

		final int kursartID = daten[0].intValue();
		final int von = daten[1].intValue(); // Schiene ist 1-indiziert!
		final int bis = daten[2].intValue(); // Schiene ist 1-indiziert!

		DeveloperNotificationException.ifSetNotContains(
				"KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(%d, %d, %d): Kursart nicht vorhanden!".formatted(kursartID, von, bis),
				setKursarten, kursartID);

		DeveloperNotificationException.ifTrue(
				"KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(%d, %d, %d): Parameter sind unlogisch!".formatted(kursartID, von, bis),
				!((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}

	private static void fehlerBeiReferenzenRegeltyp7(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {

		ueberpruefeDatenLaenge("KURS_VERBIETEN_MIT_KURS", daten, 2);

		final long kursID1 = daten[0];
		final long kursID2 = daten[1];

		DeveloperNotificationException.ifSetNotContains(
				"KURS_VERBIETEN_MIT_KURS(%d, %d): Kurs-ID1 nicht vorhanden!".formatted(kursID1, kursID2),
				setKurse, kursID1);

		DeveloperNotificationException.ifSetNotContains(
				"KURS_VERBIETEN_MIT_KURS(%d, %d): Kurs-ID2 nicht vorhanden!".formatted(kursID1, kursID2),
				setKurse, kursID2);

		DeveloperNotificationException.ifTrue(
				"KURS_VERBIETEN_MIT_KURS(%d, %d): Wurde mit sich selbst kombiniert!".formatted(kursID1, kursID2),
				kursID1 == kursID2);
	}

	private static void fehlerBeiReferenzenRegeltyp8(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {

		ueberpruefeDatenLaenge("KURS_ZUSAMMEN_MIT_KURS", daten, 2);

		final long kursID1 = daten[0];
		final long kursID2 = daten[1];

		DeveloperNotificationException.ifSetNotContains(
				"KURS_ZUSAMMEN_MIT_KURS(%d, %d): Kurs-ID1 nicht vorhanden!".formatted(kursID1, kursID2),
				setKurse, kursID1);

		DeveloperNotificationException.ifSetNotContains(
				"KURS_ZUSAMMEN_MIT_KURS(%d, %d): Kurs-ID2 nicht vorhanden!".formatted(kursID1, kursID2),
				setKurse, kursID2);

		DeveloperNotificationException.ifTrue(
				"KURS_ZUSAMMEN_MIT_KURS(%d, %d): Wurde mit sich selbst kombiniert!".formatted(kursID1, kursID2),
				kursID1 == kursID2);
	}

	private static void fehlerBeiReferenzenRegeltyp9(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {

		ueberpruefeDatenLaenge("KURS_MIT_DUMMY_SUS_AUFFUELLEN", daten, 2);

		final long kursID = daten[0];
		final int dummySuS = daten[1].intValue();

		DeveloperNotificationException.ifSetNotContains(
				"KURS_MIT_DUMMY_SUS_AUFFUELLEN(%d, %d): Kurs-ID nicht vorhanden!".formatted(kursID, dummySuS),
				setKurse, kursID);

		DeveloperNotificationException.ifSmaller(
				"KURS_MIT_DUMMY_SUS_AUFFUELLEN(%d, %d): Der Wert ist zu klein!".formatted(kursID, dummySuS),
				dummySuS, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN);

		DeveloperNotificationException.ifGreater(
				"KURS_MIT_DUMMY_SUS_AUFFUELLEN(%d, %d): Der Wert ist zu groß!".formatted(kursID, dummySuS),
				dummySuS, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX);
	}

	private static void fehlerBeiReferenzenRegeltyp10(final @NotNull Long @NotNull [] daten) {
		ueberpruefeDatenLaenge("LEHRKRAEFTE_BEACHTEN", daten, 0);
	}

	private static void fehlerBeiReferenzenRegeltyp11(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
			final @NotNull HashSet<Long> setFaecher) {

		ueberpruefeDatenLaenge("SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH", daten, 3);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];
		final long fachID = daten[2];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID1 nicht vorhanden!".formatted(schuelerID1, schuelerID2, fachID),
				setSchueler, schuelerID1);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID2 nicht vorhanden!".formatted(schuelerID1, schuelerID2, fachID),
				setSchueler, schuelerID2);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Fach-ID nicht vorhanden!".formatted(schuelerID1, schuelerID2, fachID),
				setFaecher, fachID);

		DeveloperNotificationException.ifTrue(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Wurde mit sich selbst kombiniert!".formatted(schuelerID1, schuelerID2, fachID),
				schuelerID1 == schuelerID2);
	}

	private static void fehlerBeiReferenzenRegeltyp12(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler,
			final @NotNull HashSet<Long> setFaecher) {

		ueberpruefeDatenLaenge("SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH", daten, 3);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];
		final long fachID = daten[2];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID1 nicht vorhanden!".formatted(schuelerID1, schuelerID2, fachID),
				setSchueler, schuelerID1);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Schüler-ID2 nicht vorhanden!".formatted(schuelerID1, schuelerID2, fachID),
				setSchueler, schuelerID2);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Fach-ID nicht vorhanden!".formatted(schuelerID1, schuelerID2, fachID),
				setFaecher, fachID);

		DeveloperNotificationException.ifTrue(
				"SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(%d, %d, %d): Wurde mit sich selbst kombiniert!".formatted(schuelerID1, schuelerID2, fachID),
				schuelerID1 == schuelerID2);
	}

	private static void fehlerBeiReferenzenRegeltyp13(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler) {

		ueberpruefeDatenLaenge("SCHUELER_ZUSAMMEN_MIT_SCHUELER", daten, 2);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER(%d, %d): Schüler-ID1 nicht vorhanden!".formatted(schuelerID1, schuelerID2),
				setSchueler, schuelerID1);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER(%d, %d): Schüler-ID2 nicht vorhanden!".formatted(schuelerID1, schuelerID2),
				setSchueler, schuelerID2);

		DeveloperNotificationException.ifTrue(
				"SCHUELER_ZUSAMMEN_MIT_SCHUELER(%d, %d): Wurde mit sich selbst kombiniert!".formatted(schuelerID1, schuelerID2),
				schuelerID1 == schuelerID2);
	}

	private static void fehlerBeiReferenzenRegeltyp14(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler) {

		ueberpruefeDatenLaenge("SCHUELER_VERBIETEN_MIT_SCHUELER", daten, 2);

		final long schuelerID1 = daten[0];
		final long schuelerID2 = daten[1];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_VERBIETEN_MIT_SCHUELER(%d, %d): Schüler-ID1 nicht vorhanden!".formatted(schuelerID1, schuelerID2),
				setSchueler, schuelerID1);

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_VERBIETEN_MIT_SCHUELER(%d, %d): Schüler-ID2 nicht vorhanden!".formatted(schuelerID1, schuelerID2),
				setSchueler, schuelerID2);

		DeveloperNotificationException.ifTrue(
				"SCHUELER_VERBIETEN_MIT_SCHUELER(%d, %d): Wurde mit sich selbst kombiniert!".formatted(schuelerID1, schuelerID2),
				schuelerID1 == schuelerID2);
	}

	private static void fehlerBeiReferenzenRegeltyp15(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {

		ueberpruefeDatenLaenge("KURS_MAXIMALE_SCHUELERANZAHL", daten, 2);

		final long kursID = daten[0];
		final int schuelerAnzahl = daten[1].intValue();

		DeveloperNotificationException.ifSetNotContains(
				"KURS_MAXIMALE_SCHUELERANZAHL(%d, %d): Kurs-ID nicht vorhanden!".formatted(kursID, schuelerAnzahl),
				setKurse, kursID);

		DeveloperNotificationException.ifSmaller(
				"KURS_MAXIMALE_SCHUELERANZAHL(%d, %d): Schüleranzahl ist zu klein!".formatted(kursID, schuelerAnzahl),
				schuelerAnzahl, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN);

		DeveloperNotificationException.ifGreater(
				"KURS_MAXIMALE_SCHUELERANZAHL(%d, %d): Schüleranzahl ist zu groß!".formatted(kursID, schuelerAnzahl),
				schuelerAnzahl, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX);
	}

	private static void fehlerBeiReferenzenRegeltyp16(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setSchueler) {

		ueberpruefeDatenLaenge("SCHUELER_IGNORIEREN", daten, 1);

		final long schuelerID = daten[0];

		DeveloperNotificationException.ifSetNotContains(
				"SCHUELER_IGNORIEREN(%d): Schüler-ID nicht vorhanden!".formatted(schuelerID),
				setSchueler, schuelerID);
	}

	private static void fehlerBeiReferenzenRegeltyp17(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setKurse) {

		ueberpruefeDatenLaenge("KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN", daten, 1);

		final long kursID = daten[0];

		DeveloperNotificationException.ifSetNotContains(
				"KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(%d): Kurs-ID nicht vorhanden!".formatted(kursID),
				setKurse, kursID);
	}

	private static void fehlerBeiReferenzenRegeltyp18(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<Long> setFaecher,
			final @NotNull HashSet<Integer> setKursarten) {

		ueberpruefeDatenLaenge("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE", daten, 3);

		final long fachID = daten[0];
		final int kursartID = daten[1].intValue();
		final int maximum = daten[2].intValue();

		DeveloperNotificationException.ifSetNotContains(
				"FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Fach-ID nicht vorhanden!".formatted(fachID, kursartID, maximum),
				setFaecher, fachID);

		DeveloperNotificationException.ifSetNotContains(
				"FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Kursart nicht vorhanden!".formatted(fachID, kursartID, maximum),
				setKursarten, kursartID);

		DeveloperNotificationException.ifSmaller(
				"FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Anzahl ist zu klein!".formatted(fachID, kursartID, maximum),
				maximum, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN);

		DeveloperNotificationException.ifGreater(
				"FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(%d, %d, %d): Anzahl ist zu groß!".formatted(fachID, kursartID, maximum),
				maximum, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX);
	}


	private void fehlerBeiRegelGruppierung(final @NotNull List<GostBlockungRegel> pRegeln) {
		// Regeln nach ID in Listen gruppieren.
		final HashSet<Long> regelDatabaseIDs = new HashSet<>();
		for (final GostBlockungRegel iRegel : pRegeln) {
			DeveloperNotificationException.ifInvalidID("iRegel.id", iRegel.id);
			DeveloperNotificationException.ifSetAddsDuplicate("regelDatabaseIDs", regelDatabaseIDs, iRegel.id);
			// Regel zur Liste hinzufügen (ggf. Liste erzeugen).
			final @NotNull GostKursblockungRegelTyp regelTyp = GostKursblockungRegelTyp.fromTyp(iRegel.typ);
			MapUtils.getOrCreateArrayList(regelMap, regelTyp).add(iRegel);
		}
	}

	private void fehlerBeiSchuelerErstellung(final @NotNull GostBlockungsdatenManager input) {
		final @NotNull HashSet<Long> setSchueler = new HashSet<>();

		// Schüler sammeln.
		for (final @NotNull Schueler gSchueler : input.daten().schueler) {
			setSchueler.add(gSchueler.id);
		}

		// Schüler-Fachwahlen Überprüfen.
		for (final @NotNull GostFachwahl fachwahl : input.daten().fachwahlen) {
			DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, fachwahl.schuelerID);
		}

		final int nSchueler = setSchueler.size();
		final int nSchienen = input.schieneGetAnzahl();
		final int nKurse = input.kursGetAnzahl();

		schuelerMenge = new KursblockungDynSchueler[nSchueler];
		int i = 0;
		for (final long sID : setSchueler) {
			final @NotNull KursblockungDynSchueler schueler = new KursblockungDynSchueler(log, rnd, sID, statistik, nSchienen, nKurse, i);
			schuelerMenge[i] = schueler;
			schuelerMap.put(sID, schueler);
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

			KursblockungDynFachart dynFachart = fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(rnd, nFacharten, fach, kursart, statistik, nSchueler, nSchienen);
				fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}

			dynFachart.aktionMaxKurseErhoehen();
		}

		// Facharten aus SuS-Fachwahlen extrahieren.
		for (final @NotNull GostFachwahl iFachwahl : input.daten().fachwahlen) {
			final @NotNull GostFach fach = input.faecherManager().getOrException(iFachwahl.fachID);
			final @NotNull GostKursart kursart = GostKursart.fromID(iFachwahl.kursartID);

			KursblockungDynFachart dynFachart = fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(rnd, nFacharten, fach, kursart, statistik, nSchueler, nSchienen);
				fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}

		}

		// Keine Facharten? --> Fehler
		DeveloperNotificationException.ifSmaller("nFacharten", nFacharten, 1);

		// fachartMap --> fachartArr
		fachartMenge = new KursblockungDynFachart[nFacharten];
		for (final @NotNull KursblockungDynFachart fachart : fachartMap2D.getNonNullValuesAsList()) {
			fachartMenge[fachart.gibNr()] = fachart;
		}

		// Verteile Kurse verschwunden? --> Fehler
		int kursSumme = 0;
		for (final @NotNull KursblockungDynFachart fa : fachartMenge) {
			kursSumme += fa.gibKurseMax();
		}
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

	private void fehlerBeiStatistikErstellung(
			final @NotNull KursblockungDynFachart @NotNull [] fachartArr,
			final @NotNull KursblockungDynSchueler @NotNull [] susArr,
			final @NotNull GostBlockungsdatenManager input) {

		final int nFacharten = fachartArr.length;

		final @NotNull int @NotNull [][] wahlenMatrixFachart = new int[nFacharten][nFacharten];
		final @NotNull int @NotNull [][] bewertungMatrixFachart = new int[nFacharten][nFacharten];

		// Zähle pro Schüler alle Fachart-Paare.
		for (final @NotNull KursblockungDynSchueler s : susArr) {
			for (final @NotNull PairNN<KursblockungDynFachart, KursblockungDynFachart> pair : new PairIterable<>(s.gibFacharten(),
					PairIteratorModus.LOWER_ONLY)) {
				final int nr1 = pair.a.gibNr();
				final int nr2 = pair.b.gibNr();
				wahlenMatrixFachart[nr1][nr2]++;
				wahlenMatrixFachart[nr2][nr1]++;
			}
		}

		// Berechne "bewertungMatrixFachart".
		final int cMALUS_KOLLISION = 10000;
		final int cMALUS_DIAGONALE = 1000;
		for (int i1 = 0; i1 < nFacharten; i1++) {
			for (int i2 = 0; i2 < nFacharten; i2++) {
				final int kurseVonFachart1 = fachartArr[i1].gibKurseMax();
				final int kurseVonFachart2 = fachartArr[i2].gibKurseMax();
				final int nr1 = fachartArr[i1].gibNr();
				final int nr2 = fachartArr[i2].gibNr();
				bewertungMatrixFachart[nr1][nr2] = (nr1 == nr2) ? cMALUS_DIAGONALE : 0;
				if ((wahlenMatrixFachart[nr1][nr2] == 0) || (kurseVonFachart1 == 0) || (kurseVonFachart2 == 0)) {
					continue;
				}
				// Ab hier: Es gibt von beiden Facharten mindestens einen Kurs
				//          und es gibt mindestens einen S. der beide Fächer gewählt hat.
				final int nenner = kurseVonFachart1 * kurseVonFachart2; // mindestens 1
				bewertungMatrixFachart[nr1][nr2] += cMALUS_KOLLISION / nenner;
			}
		}

		statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length, input.kursGetAnzahl());
	}

	private void fehlerBeiSchienenErzeugung(final int nSchienen) {
		schienenMenge = new KursblockungDynSchiene[nSchienen];
		for (int nr = 0; nr < nSchienen; nr++) {
			schienenMenge[nr] = new KursblockungDynSchiene(log, nr, statistik);
		}
	}

	private void fehlerBeiKursErstellung(final @NotNull GostBlockungsdatenManager input, final int nSchueler) {
		final int nKurse = input.kursGetAnzahl();
		final int nSchienen = input.schieneGetAnzahl();

		kursMenge = new KursblockungDynKurs[nKurse];
		int i = 0;
		for (final @NotNull GostBlockungKurs kurs : input.daten().kurse) {
			final @NotNull KursblockungDynKurs dynKurs = schritt08FehlerBeiKursErstellungErzeuge(input, kurs, nSchienen, i, nSchueler);
			kursMenge[i] = dynKurs;
			DeveloperNotificationException.ifMapPutOverwrites(kursMap, kurs.id, dynKurs);
			i++;
		}

	}

	private @NotNull KursblockungDynKurs schritt08FehlerBeiKursErstellungErzeuge(
			final @NotNull GostBlockungsdatenManager input,
			final @NotNull GostBlockungKurs kurs,
			final int nSchienen,
			final int kursNr,
			final int nSchueler) {

		// Fehler: Kurs belegt zu wenig Schienen.
		DeveloperNotificationException.ifSmaller(
				"Der Kurs mit ID=%d und NR=%d belegt zu wenig (%d) Schienen!".formatted(kurs.id, kursNr, kurs.anzahlSchienen),
				kurs.anzahlSchienen, 1);

		// Fehler: Kurs belegt zu viele Schienen.
		DeveloperNotificationException.ifGreater(
				"Der Kurs mit ID=%d und NR=%d belegt zu viele (%d) Schienen!".formatted(kurs.id, kursNr, kurs.anzahlSchienen),
				kurs.anzahlSchienen, schienenMenge.length);

		// Alle Schienen, in denen der Kurs gerade ist. Anfangs leer.
		final @NotNull List<KursblockungDynSchiene> schieneLage = new ArrayList<>();

		// 'Frei' beinhaltet zunächst alle Schienen permutiert.
		final @NotNull List<KursblockungDynSchiene> schieneFrei = ListUtils.getCopyAsArrayListPermuted(schienenMenge, rnd);

		// Wende Regeln an (Kurs-Sperrungen und Kurs-Fixierungen).
		schritt08FehlerBeiKursErstellungErzeugeWendeRegel1und6An(schieneFrei, kurs, nSchienen);
		schritt08FehlerBeiKursErstellungErzeugeWendeRegel3und2An(schieneLage, schieneFrei, kurs);

		// Fehler: Zu viel fixiert?
		final int anzahlFixierterSchienen = schieneLage.size();
		DeveloperNotificationException.ifGreater(
				"Der Kurs mit ID=%d und NR=%d hat %d Schienen fixert, aber selbst belegt er %d Schienen!"
				.formatted(kurs.id, kursNr, anzahlFixierterSchienen, kurs.anzahlSchienen),
				anzahlFixierterSchienen, kurs.anzahlSchienen);

		// Fülle "schieneLage" auf, bis die richtige Anzahl erreicht ist.
		while (schieneLage.size() < kurs.anzahlSchienen) {

			UserNotificationException.ifTrue(
					input.toStringKurs(kurs.id) + " hat zu viele Schienen gesperrt, so dass seine Schienenanzahl nicht erfüllt werden kann!",
					schieneFrei.isEmpty());

			schieneLage.add(schieneFrei.removeLast());
		}

		// KursblockungDynKurs-Objekt erzeugen.
		final @NotNull KursblockungDynSchiene @NotNull [] schienenLageArray = schieneLage.toArray(new KursblockungDynSchiene[0]);
		final @NotNull KursblockungDynSchiene @NotNull [] schienenFreiArray = schieneFrei.toArray(new KursblockungDynSchiene[0]);
		final @NotNull KursblockungDynFachart dynFachart = gibFachart(kurs.fach_id, kurs.kursart);

		return new KursblockungDynKurs(rnd, schienenLageArray, anzahlFixierterSchienen, schienenFreiArray, kurs.id, dynFachart, log, kursNr, nSchueler);
	}


	private void schritt08FehlerBeiKursErstellungErzeugeWendeRegel1und6An(
			final @NotNull List<KursblockungDynSchiene> schieneFrei,
			final @NotNull GostBlockungKurs kurs,
			final int nSchienen) {

		// Regel 1: Entferne alle Schienen, die durch die Kursart gesperrt sind.
		for (final @NotNull GostBlockungRegel regel1 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)) {

			if (kurs.kursart == regel1.parameter.get(0)) {
				final int von = regel1.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
				final int bis = regel1.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
				for (int schiene = von; schiene <= bis; schiene++) {
					schieneFrei.remove(schienenMenge[schiene - 1]); // Intern 0-indiziert!
				}
			}
		}

		// Regel 6: Entferne alle Schienen, die durch die Kursart gesperrt sind.
		for (final @NotNull GostBlockungRegel regel6 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			final boolean kursartStimmt = kurs.kursart == regel6.parameter.get(0);
			final int von = regel6.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
			final int bis = regel6.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
			for (int schiene = 1; schiene <= nSchienen; schiene++) {
				final boolean innerhalb = (von <= schiene) && (schiene <= bis);
				if (innerhalb != kursartStimmt) {
					schieneFrei.remove(schienenMenge[schiene - 1]); // Intern 0-indiziert!
				}
			}
		}

	}


	private void schritt08FehlerBeiKursErstellungErzeugeWendeRegel3und2An(
			final @NotNull List<KursblockungDynSchiene> schieneLage,
			final @NotNull List<KursblockungDynSchiene> schieneFrei,
			final @NotNull GostBlockungKurs kurs) {

		// Regel 3: Entferne alle Schienen, die explizit gesperrt sind.
		for (final @NotNull GostBlockungRegel regel3 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE)) {
			if (kurs.id == regel3.parameter.get(0)) {
				final int schiene = regel3.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
				schieneFrei.remove(schienenMenge[schiene - 1]); // Intern 0-indiziert!
			}
		}

		// Regel 2: Fixiere Kurse in Schienen.
		for (final @NotNull GostBlockungRegel regel2 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE)) {
			if (kurs.id == regel2.parameter.get(0)) {
				final int schiene = regel2.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
				final @NotNull KursblockungDynSchiene dynSchiene = schienenMenge[schiene - 1]; // Intern 0-indiziert!
				if (schieneLage.contains(dynSchiene)) {
					continue; // Doppelt-Fixierungen ignorieren
				}

				UserNotificationException.ifTrue(
						"KURS_FIXIERE_IN_SCHIENE: Kurs (%d) soll in Schiene (%d) fixiert werden, aber die Schiene wurde bereits gesperrt!"
								.formatted(kurs.id, schiene),
						!schieneFrei.contains(dynSchiene));

				schieneFrei.remove(dynSchiene);
				schieneLage.add(dynSchiene);
			}
		}

	}


	private void fehlerBeiKursFreiErstellung() {
		// Zähle Kurse mit Freiheitsgraden.
		int nKursFrei = 0;
		for (final KursblockungDynKurs kurs : kursMenge) {
			if (kurs.gibHatFreiheitsgrade()) {
				nKursFrei++;
			}
		}

		// Kopiere Kurse mit Freiheitsgraden.
		kursMengeFrei = new KursblockungDynKurs[nKursFrei];
		int j = 0;
		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			if (kurs.gibHatFreiheitsgrade()) {
				kursMengeFrei[j] = kurs;
				j++;
			}
		}
	}

	private void fehlerBeiFachartKursArrayErstellung() {
		final int nFacharten = fachartMenge.length;

		// Map: Fachart-Nummer --> Liste der Kurse
		final @NotNull HashMap<Integer, List<KursblockungDynKurs>> mapFachartList = new HashMap<>();
		for (int i = 0; i < nFacharten; i++) {
			mapFachartList.put(i, new ArrayList<>());
		}

		// Pro Kurs: Der Fachart-Liste hinzufügen.
		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			final int fachartNr = kurs.gibFachart().gibNr();
			DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr).add(kurs);
		}

		// Pro Fachart: Liste zu Array konvertieren und übergeben.
		for (int fachartNr = 0; fachartNr < nFacharten; fachartNr++) {
			final List<KursblockungDynKurs> list = DeveloperNotificationException.ifMapGetIsNull(mapFachartList, fachartNr);
			final @NotNull KursblockungDynKurs @NotNull [] tmpKursArr = list.toArray(new KursblockungDynKurs[0]);
			fachartMenge[fachartNr].aktionSetKurse(tmpKursArr);
		}

	}

	private void fehlerBeiRegel4oder5() {
		// Regel 4 - SCHUELER_FIXIEREN_IN_KURS
		final @NotNull HashMap<Long, List<Long>> mapSchuelerZuFixierungen = new HashMap<>();
		for (final @NotNull GostBlockungRegel regel4 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			final long schuelerID = regel4.parameter.get(0);
			final long kursID = regel4.parameter.get(1);
			MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, schuelerID).add(kursID);
			final @NotNull KursblockungDynSchueler schueler = gibSchueler(schuelerID);
			final @NotNull KursblockungDynKurs fixierterKurs = gibKurs(kursID);
			// Alle anderen Kurse der selben Fachart verbieten ...
			for (final @NotNull KursblockungDynKurs kurs : fixierterKurs.gibFachart().gibKurse()) {
				if (kurs == fixierterKurs) {
					kurs.setzeSchuelerFixierung(schueler.internalSchuelerID);
				} else {
					schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
				}
			}
		}

		// Regel 4 - SCHUELER_FIXIEREN_IN_KURS (Spezialfall - Pro Schüler: Alle Kurspaar-Fixierungen Kurs-Kurs-Zusammen verbieten))
		for (final long idSchueler : mapSchuelerZuFixierungen.keySet()) {
			final @NotNull List<Long> listKursIDs = MapUtils.getOrCreateArrayList(mapSchuelerZuFixierungen, idSchueler);
			// Alle (sortierten) Kurs-Paarungen durchgehen und KURS_VERBIETEN_MIT_KURS definieren.
			for (int index2 = 1; index2 < listKursIDs.size(); index2++) {
				for (int index1 = 0; index1 < index2; index1++) {
					final long kursID1 = listKursIDs.get(index1);
					final long kursID2 = listKursIDs.get(index2);
					final @NotNull KursblockungDynKurs kurs1 = gibKurs(kursID1);
					final @NotNull KursblockungDynKurs kurs2 = gibKurs(kursID2);
					statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
				}
			}
		}


		// Regel 5 - SCHUELER_VERBIETEN_IN_KURS
		for (final @NotNull GostBlockungRegel regel5 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			final long schuelerID = regel5.parameter.get(0);
			final long kursID = regel5.parameter.get(1);
			final @NotNull KursblockungDynSchueler schueler = gibSchueler(schuelerID);
			final @NotNull KursblockungDynKurs verbotenerKurs = gibKurs(kursID);
			// Kurs verbieten
			schueler.aktionSetzeKursSperrung(verbotenerKurs.gibInternalID());
		}
	}

	private void fehlerBeiRegel7oder8() {
		// Regel 7 - KURS_VERBIETEN_MIT_KURS
		for (final @NotNull GostBlockungRegel regel7 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS)) {
			final long kursID1 = regel7.parameter.get(0);
			final long kursID2 = regel7.parameter.get(1);
			final @NotNull KursblockungDynKurs kurs1 = gibKurs(kursID1);
			final @NotNull KursblockungDynKurs kurs2 = gibKurs(kursID2);
			statistik.regelHinzufuegenKursVerbieteMitKurs(kurs1, kurs2);
		}

		// Regel 8 - KURS_ZUSAMMEN_MIT_KURS
		for (final @NotNull GostBlockungRegel regel8 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS)) {
			final long kursID1 = regel8.parameter.get(0);
			final long kursID2 = regel8.parameter.get(1);
			final @NotNull KursblockungDynKurs kurs1 = gibKurs(kursID1);
			final @NotNull KursblockungDynKurs kurs2 = gibKurs(kursID2);
			statistik.regelHinzufuegenKursZusammenMitKurs(kurs1, kurs2);
		}
	}

	private void fehlerBeiRegel9() {
		// Regel 9 - KURS_MIT_DUMMY_SUS_AUFFUELLEN
		for (final @NotNull GostBlockungRegel regel9 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			final long kursID = regel9.parameter.get(0);
			final int susAnzahl = regel9.parameter.get(1).intValue();
			final @NotNull KursblockungDynKurs kurs = gibKurs(kursID);

			for (int i = 0; i < susAnzahl; i++) {
				kurs.aktionSchuelerDummyHinzufuegen();
			}
		}
	}


	private void fehlerBeiRegel10(final @NotNull GostBlockungsdatenManager pInput) {
		final List<GostBlockungRegel> regelnTyp10 = MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN);
		if (regelnTyp10.isEmpty()) {
			return;
		}

		final int size = regelnTyp10.size();
		DeveloperNotificationException.ifGreater(
				"LEHRKRAEFTE_BEACHTEN: Diese Regeln darf es maximal ein mal geben, sie gibt es aber %d mal!".formatted(size),
				size, 1);

		// Ordne jeder Lehrkraft eine Liste von Kursen zu.
		final @NotNull HashMap<Long, List<KursblockungDynKurs>> mapLehrkraftNachKurse = new HashMap<>();
		for (final @NotNull GostBlockungKurs gKurs : pInput.daten().kurse) {
			for (final @NotNull GostBlockungKursLehrer gLehr : gKurs.lehrer) {
				final @NotNull KursblockungDynKurs dynKurs = gibKurs(gKurs.id);
				MapUtils.getOrCreateArrayList(mapLehrkraftNachKurse, gLehr.id).add(dynKurs);
			}
		}

		// Für jede Lehrkraft gilt nun: Alle Kurs-Paare dürfen nicht zusammen in einer Schiene sein.
		for (final @NotNull List<KursblockungDynKurs> kurseDerLehrkraft : mapLehrkraftNachKurse.values()) {
			for (final @NotNull PairNN<KursblockungDynKurs, KursblockungDynKurs> pair : new PairIterable<>(kurseDerLehrkraft, PairIteratorModus.LOWER_ONLY)) {
				statistik.regelHinzufuegenKursVerbieteMitKurs(pair.a, pair.b);
			}
		}

	}


	private void fehlerBeiRegel11bis14(final @NotNull GostBlockungsdatenManager input) {
		// Das Set dient dazu, Widersprüche/Dopplungen bei den Regeln zu finden.
		final @NotNull HashSet<LongArrayKey> setSSF = new HashSet<>();
		final @NotNull String fehlermeldungDopplung = "Dopplung bei Schüler-Schüler-Fach Zusammen/Verbieten!";

		// Regel 11: SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH
		for (final @NotNull GostBlockungRegel regel11 : MapUtils.getOrCreateArrayList(regelMap,
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
			sch1.setzeZusammenMitSchuelerInFach(sch2, idF);
		}

		// Regel 12: SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH
		for (final @NotNull GostBlockungRegel regel12 : MapUtils.getOrCreateArrayList(regelMap,
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
			sch1.setzeVerbietenMitSchuelerInFach(sch2, idF);
		}

		// Regel 13: SCHUELER_ZUSAMMEN_MIT_SCHUELER
		for (final @NotNull GostBlockungRegel regel13 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER)) {
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
				sch1.setzeZusammenMitSchueler(sch2);
			}
		}

		// Regel 14: SCHUELER_VERBIETEN_MIT_SCHUELER
		for (final @NotNull GostBlockungRegel r14 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER)) {
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
			sch1.setzeVerbietenMitSchueler(sch2);
		}

	}

	private void fehlerBeiRegel15() {
		for (final @NotNull GostBlockungRegel r15 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL)) {
			final long idKurs = r15.parameter.get(0);
			final int maxSuS = r15.parameter.get(1).intValue();

			final @NotNull KursblockungDynKurs kurs = gibKurs(idKurs);
			kurs.setzeMaxSuS(maxSuS);
		}
	}

	private void fehlerBeiRegel16() {
		for (final @NotNull GostBlockungRegel r16 : MapUtils.getOrCreateArrayList(regelMap, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			final long idSchueler = r16.parameter.get(0);

			final @NotNull KursblockungDynSchueler schueler = gibSchueler(idSchueler);
			schueler.setzeSperreBeiKursverteilung();
		}
	}

	private void fehlerBeiRegel18() {
		for (final @NotNull GostBlockungRegel r18 : MapUtils.getOrCreateArrayList(regelMap,
				GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE)) {
			final long idFach = r18.parameter.get(0);
			final int idKursart = r18.parameter.get(1).intValue();
			final int maximalProSchiene = r18.parameter.get(2).intValue();

			final @NotNull KursblockungDynFachart fachart = gibFachart(idFach, idKursart);
			fachart.setzeMaxAnzahlProSchiene(maximalProSchiene);
		}
	}

	private @NotNull KursblockungDynFachart gibFachart(final long fachID, final int kursart) {
		return fachartMap2D.getOrException(fachID, kursart);
	}

	private @NotNull KursblockungDynSchueler gibSchueler(final long schuelerID) {
		return DeveloperNotificationException.ifMapGetIsNull(schuelerMap, schuelerID);
	}

	private @NotNull KursblockungDynKurs gibKurs(final long kursID) {
		return DeveloperNotificationException.ifMapGetIsNull(kursMap, kursID);
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
		for (final @NotNull KursblockungDynKurs dynKurs : kursMenge) {
			for (final int schienenNr : dynKurs.gibSchienenLage()) {
				final long idKurs = dynKurs.gibDatenbankID();
				final long idSchiene = out.getOfSchieneID(schienenNr + 1); // Manager hat eine 1-Indizierung der Schiene!
				kursSchienenZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchiene));
			}
		}

		// UPDATE - Kurs - Schiene
		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate uKursSchienen =
				out.kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(kursSchienenZuordnungen);
		out.kursSchienenUpdateExecute(uKursSchienen);

		// Erzeuge die Kurs-Schüler-Zuordnungen. Verwende Update-Objekte, da nur eine Regelvalidierung am Ende erfolgt.
		final @NotNull Set<GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen = new HashSet<>();
		for (final @NotNull KursblockungDynSchueler dynSchueler : schuelerMenge) {
			for (final KursblockungDynKurs kurs : dynSchueler.gibKurswahlen()) {
				if (kurs != null) {
					// Set verhindert Duplikate (sollte hier nicht vorkommen).
					final long idKurs = kurs.gibDatenbankID();
					final long idSchueler = dynSchueler.gibDatenbankID();
					kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}
			}
		}

		// Erzeuge durch Regeln forcierte Schüler-Kurs-Zuordnungen.
		// Das kann später zu Kollisionen führen, was aber richtig ist.
		for (final @NotNull GostBlockungRegel gRegel : pDataManager.regelGetListe()) {
			if (gRegel.typ == GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				// Set verhindert Duplikate (das ist in diesem Fall möglich).
				final long idSchueler = gRegel.parameter.get(0);
				final long idKurs = gRegel.parameter.get(1);
				kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
			}
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
		return log;
	}

	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	@NotNull
	Random gibRandom() {
		return rnd;
	}

	/**
	 * Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 *
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 */
	public @NotNull KursblockungDynStatistik gibStatistik() {
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
	public int gibSchienenAnzahl() {
		return schienenMenge.length;
	}

	/**
	 * Liefert alle Kurse.
	 *
	 * @return Array aller Kurse.
	 */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseAlle() {
		return kursMenge;
	}

	/**
	 * Liefert alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Array aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	@NotNull
	KursblockungDynKurs @NotNull [] gibKurseDieFreiSind() {
		return kursMengeFrei;
	}

	/**
	 * Liefert die Anzahl alle Kurse deren Lage nicht komplett fixiert ist.
	 *
	 * @return Anzahl aller Kurse, deren Schienenlage noch veränderbar ist.
	 */
	int gibKurseDieFreiSindAnzahl() {
		return kursMengeFrei.length;
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
			for (final KursblockungDynSchueler schueler : schuelerMenge) {
				if (schueler.gibHatMultikurs()) {
					list.addLast(schueler);
				}
			}

			final @NotNull KursblockungDynSchueler @NotNull [] temp = new KursblockungDynSchueler[list.size()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = list.removeFirst();
			}
			return temp;
		}
		return schuelerMenge;
	}

	/**
	 * Liefert ein Array aller Schülerinnen und Schüler.
	 *
	 * @return Ein Array aller Schülerinnen und Schüler.
	 */
	@NotNull
	KursblockungDynSchueler @NotNull [] gibSchuelerArrayAlle() {
		return schuelerMenge;
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdifferenzen) des Zustandes S sich
	 * verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Nichtwahlen, Kursdifferenzen) des Zustandes S sich
	 *         verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertungJetztBesserAlsS() {
		return statistik.gibBewertungZustandS1NW2KD();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen,
	 * Fachwahlmatrix) des Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen, Fachwahlmatrix) des
	 *         Zustandes-K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibCompareZustandK1NW2KD3FW() {
		return statistik.gibCompareZustandK1NW2KD3FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen,
	 * Fachwahlmatrix) des Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Nichtwahlen, Kursdifferenzen, Fachwahlmatrix) des
	 *         Zustandes-G sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibCompareZustandG1NW2KD3FW() {
		return statistik.gibCompareZustandG1NW2KD3FW();
	}

	/**
	 * Liefert den Wert {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen,
	 * Kursdifferenzen) des Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 *
	 * @return {@code -1, 0 oder +1}, falls die Bewertung (Reihenfolge: Fachwahlmatrix, Nichtwahlen, Kursdifferenzen) des
	 *         Zustandes K sich verschlechtert (-1), sich verbessert (+1) hat oder gleichgeblieben (0) ist.
	 */
	int gibBewertungK1FW2NW3KD() {
		return statistik.gibCompareZustandK1FW2NW3KD();
	}

	// ########################################
	// ########### SETTER / ACTIONS ###########
	// ########################################

	/**
	 * Liefert TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 *
	 * @param b  Das zu vergleichende Objekt.
	 *
	 * @return TRUE, falls dieses Objekt besser ist als das übergebene Objekt b.
	 */
	boolean gibIstBesserAls1NW2KD3FW(final @NotNull KursblockungDynDaten b) {
		return statistik.gibIstBesserAls1NW2KD3FW(b.statistik);
	}

	/**
	 * Liefert true, falls der Kurs in der Schiene ist.
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @param schieneDB Die Datenbank-ID der Schiene (1-indiziert!).
	 * @return true, falls der Kurs in der Schiene ist.
	 */
	public boolean gibIstKursInSchiene(final int idKursDB, final int schieneDB) {
		for (final @NotNull KursblockungDynKurs k : kursMenge) {
			if ((k.gibDatenbankID() == idKursDB) && (k.gibIstInSchiene(schieneDB - 1))) {
				return true;
			}
		}
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
		for (final @NotNull KursblockungDynKurs k : kursMenge) {
			if (k.gibDatenbankID() == idKursDB) {
				for (final @NotNull KursblockungDynSchueler s : schuelerMenge) {
					if ((s.gibDatenbankID() == idSchuelerDB) && (s.gibIstInKurs(k))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Liefert true, falls die übergebene Schiene nur LK-Kurse enthält (oder keine Kurse).
	 *
	 * @param schienenNr1indiziert  Die Schienen-Nummer
	 * @return true, falls die übergebene Schiene nur LK-Kurse enthält (oder keine Kurse).
	 */
	public boolean gibHatSchieneNurLK(final int schienenNr1indiziert) {
		return schienenMenge[schienenNr1indiziert - 1].gibHatNurLK();
	}

	/**
	 * Liefert true, falls die übergebene Schiene keine LK-Kurse enthält.
	 *
	 * @param schienenNr1indiziert  Die Schienen-Nummer
	 * @return true, falls die übergebene Schiene keine LK-Kurse enthält.
	 */
	public boolean gibHatSchieneKeineLK(final int schienenNr1indiziert) {
		return schienenMenge[schienenNr1indiziert - 1].gibHatKeineLK();
	}

	/**
	 * Liefert die Anzahl an SuS in dem Kurs (oder -1 falls der Kurs nicht existiert).
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return die Anzahl an SuS in dem Kurs (oder -1 falls der Kurs nicht existiert).
	 */
	public int gibKursgroesseDesKurses(final int idKursDB) {
		for (final @NotNull KursblockungDynKurs k : kursMenge) {
			if (k.gibDatenbankID() == idKursDB) {
				return k.gibSchuelerAnzahl();
			}
		}
		return -1;
	}

	/**
	 * Liefert die Kursdifferenz der Fachart des übergebenen Kurses (oder -1 falls der Kurs nicht existiert)..
	 *
	 * @param idKursDB Die Datenbank-ID des Kurses.
	 * @return die Kursdifferenz der Fachart des übergebenen Kurses (oder -1 falls der Kurs nicht existiert)..
	 */
	public int gibKursdifferenzDesKurses(final int idKursDB) {
		for (final @NotNull KursblockungDynKurs k : kursMenge) {
			if (k.gibDatenbankID() == idKursDB) {
				return k.gibFachart().gibKursdifferenz();
			}
		}
		return -1;
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S.
	 */
	void aktionZustandSpeichernS() {
		statistik.aktionBewertungSpeichernS();

		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			kurs.aktionZustandSpeichernS();
		}

		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionZustandSpeichernS();
		}
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K.
	 */
	void aktionZustandSpeichernK() {
		statistik.aktionBewertungSpeichernK();

		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			kurs.aktionZustandSpeichernK();
		}

		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionZustandSpeichernK();
		}
	}

	/**
	 * Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G.
	 */
	void aktionZustandSpeichernG() {
		statistik.aktionBewertungSpeichernG();

		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			kurs.aktionZustandSpeichernG();
		}

		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionZustandSpeichernG();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung).
	 */
	void aktionZustandLadenS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			kurs.aktionZustandLadenS();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionZustandLadenS();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung).
	 */
	void aktionZustandLadenK() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			kurs.aktionZustandLadenK();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionZustandLadenK();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K einer anderen {@link KursblockungDynDaten}-Objekts (Kursverteilung und Schülerverteilung).
	 *
	 * @param b  Das andere {@link KursblockungDynDaten}-Objekt.
	 */
	void aktionZustandLadenVon(final @NotNull KursblockungDynDaten b) {
		if (this == b) {
			log.logLn(LogLevel.WARNING, "KursblockungDynDaten.aktionZustandLadenVon(...) versucht sich selbst zu laden.");
			return;
		}

		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (int i = 0; i < kursMenge.length; i++) {
			kursMenge[i].aktionZustandLadenVon(b.kursMenge[i], schienenMenge);
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (int i = 0; i < schuelerMenge.length; i++) {
			schuelerMenge[i].aktionZustandLadenVon(b.schuelerMenge[i], kursMenge);
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung).
	 */
	void aktionZustandLadenG() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			kurs.aktionZustandLadenG();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionZustandLadenG();
		}
	}

	/**
	 * Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung).
	 */
	void aktionZustandLadenKohneSuS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : kursMenge) {
			kurs.aktionZustandLadenK();
		}
	}

	/**
	 * Entfernt alle SuS aus ihren Kursen.
	 */
	void aktionSchuelerAusAllenKursenEntfernen() {
		for (final @NotNull KursblockungDynSchueler s : schuelerMenge) {
			s.aktionKurseAlleEntfernen();
		}
	}

	/**
	 * Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	void aktionKurseFreieZufaelligVerteilen() {
		for (final @NotNull KursblockungDynKurs kurs : kursMengeFrei) {
			kurs.aktionZufaelligVerteilen();
		}
	}

	/**
	 * Verteilt genau einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert.
	 */
	void aktionKursVerteilenEinenZufaelligenFreien() {
		if (kursMengeFrei.length == 0) {
			return;
		}

		final int index = rnd.nextInt(kursMengeFrei.length);
		final @NotNull KursblockungDynKurs kurs = kursMengeFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben und Multikurse, werden dabei ignoriert.
	 */
	void aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse() {
		if (kursMengeFrei.length == 0) {
			return;
		}

		final int[] perm = KursblockungStatic.gibPermutation(rnd, kursMengeFrei.length);
		for (final int index : perm) {
			final @NotNull KursblockungDynKurs kurs = kursMengeFrei[index];
			if (kurs.gibSchienenAnzahl() == 1) {
				kurs.aktionZufaelligVerteilen();
			}
		}
	}

	/**
	 * Verändert die Lage der Kurse einer zufälligen Fachgruppe komplett neu.
	 */
	public void aktionKursVerteilenEineZufaelligeFachgruppe() {
		if (fachartMenge.length == 0) {
			return;
		}

		final int fachgruppenIndex = rnd.nextInt(fachartMenge.length);
		for (final @NotNull KursblockungDynKurs kurs : fachartMenge[fachgruppenIndex].gibKurse()) {
			kurs.aktionZufaelligVerteilen();
		}
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
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(rnd, schuelerMenge.length);
		for (final int p : perm) {
			final KursblockungDynSchueler schueler = schuelerMenge[p];
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
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(rnd, schuelerMenge.length);
		for (final int p : perm) {
			final KursblockungDynSchueler schueler = schuelerMenge[p];
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
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(rnd, schuelerMenge.length);

		for (final int i : perm) {
			final KursblockungDynSchueler schueler = schuelerMenge[i];
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
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(rnd, schuelerMenge.length);

		for (final int i : perm) {
			final KursblockungDynSchueler schueler = schuelerMenge[i];
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
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			if (schueler.gibDatenbankID() == idSchuelerDB) {
				schueler.aktionKursSetzen(idKursDB);
			}
		}
	}

	/**
	 * Entfernt den S. wenn möglich aus den übergebenen Kurs.
	 *
	 * @param idSchuelerDB  Die Datenbank-ID des S.
	 * @param idKursDB      Die Datenbank-ID des Kurses.
	 */
	public void aktionSchuelerEntfernenAusKurs(final int idSchuelerDB, final int idKursDB) {
		for (final @NotNull KursblockungDynSchueler schueler : schuelerMenge) {
			if (schueler.gibDatenbankID() == idSchuelerDB) {
				schueler.aktionKursEntfernen(idKursDB);
			}
		}
	}

	/**
	 * Verschiebt den Kurs in die Schiene.
	 *
	 * @param idKursDB  Die Datenbank-ID des Kurses.
	 * @param schieneDB Die Datenbank-ID der Schiene (1-indiziert!).
	 */
	public void aktionSetzeKursInSchiene(final int idKursDB, final int schieneDB) {
		for (final @NotNull KursblockungDynKurs k : kursMenge) {
			if (k.gibDatenbankID() == idKursDB) {
				k.aktionSetzeInSchiene(schieneDB - 1);
			}
		}
	}

	/**
	 * Debug Ausgaben. Nur für Testzwecke.
	 */
	public void debug() {
		log.modifyIndent(+4);

		log.logLn("########## Schienen ##########");
		for (int i = 0; i < schienenMenge.length; i++) {
			log.logLn("Schiene " + (i + 1));
			schienenMenge[i].debug(false);
		}

		log.logLn("########## Facharten ##########");
		for (final @NotNull KursblockungDynFachart fa : fachartMenge) {
			log.logLn("Fachart " + fa + " --> " + fa.gibKursdifferenz());
			fa.debug(schuelerMenge);
		}

		log.modifyIndent(-4);

		statistik.debug("");
	}

	/**
	 * Debug Ausgaben (Schienen und Kurse)
	 */
	public void printlnSchienenUndKurse() {
		for (int i = 0; i < schienenMenge.length; i++) {
			schienenMenge[i].printlnKurse();
		}
	}

	/**
	 * Debug-Ausgabe der Schienen mit ihre Kursen und ihren SuS.
	 */
	public void printlnSchienenUndKurseUndSchueler() {
		for (int i = 0; i < schienenMenge.length; i++) {
			schienenMenge[i].printlnKurseUndSchueler(schuelerMenge);
		}
	}

	/**
	 * Debug-Ausgabe aller Facharten mit den zugehörigen Kursen.
	 */
	public void printlnFacharten() {
		for (final @NotNull KursblockungDynFachart fachart : fachartMenge) {
			fachart.printlnKurse();
		}
	}

}
