package de.svws_nrw.core.kursblockung;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
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
	private final @NotNull Map<@NotNull GostKursblockungRegelTyp, @NotNull List<@NotNull GostBlockungRegel>> _regelMap;

	/** Die maximale Blockungszeit in Millisekunden. */
	private final long _maxTimeMillis;

	/** Diese Datenstruktur speichert die Schienen und ihre Kurse. */
	private @NotNull KursblockungDynSchiene @NotNull [] _schienenArr;

	/** Alles Kurse. */
	private @NotNull KursblockungDynKurs @NotNull [] _kursArr;

	/** Alle Kurse, die noch über Schienen wandern können. */
	private @NotNull KursblockungDynKurs @NotNull [] _kursArrFrei;

	/** Map für schnellen Zugriff auf die Kurse über ihre ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull KursblockungDynKurs> _kursMap;

	/** Alle Facharten. Fachart meint Fach + Kursart, z.B. "D;GK". */
	private @NotNull KursblockungDynFachart @NotNull [] _fachartArr;

	/** Map für schnellen Zugriff auf die Facharten über FachID und KursartID. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull KursblockungDynFachart> _fachartMap2D;

	/** Alle SuS. */
	private @NotNull KursblockungDynSchueler @NotNull [] _schuelerArr;

	/** Map für schnellen Zugriff auf die SuS über ihre ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull KursblockungDynSchueler> _schuelerMap;

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
		schritt01FehlerBeiReferenzen(input);

		// Definiert: regelMap
		schritt02FehlerBeiRegelGruppierung(input.daten().regeln);

		// Definiert: fachartArr
		schritt03FehlerBeiFachartenErstellung(input);

		// Definiert: schuelerArr, susMap
		schritt04FehlerBeiSchuelerErstellung(input);

		// Definiert: schueler[i].fachartArr
		schritt05FehlerBeiSchuelerFachwahlenErstellung(input, _schuelerArr);

		// Definiert: statistik
		schritt06FehlerBeiStatistikErstellung(_fachartArr, _schuelerArr, input);

		// Definiert: schienenArr
		schritt07FehlerBeiSchienenErzeugung(input.getSchienenAnzahl());

		// Benötigt: fachartArr
		// Definiert: kursArr
		schritt08FehlerBeiKursErstellung(input);

		// Benötigt: kursArr
		// Definiert: kursArrFrei
		schritt09FehlerBeiKursFreiErstellung();

		// Benötigt: kursArr
		// Definiert: fachartArr[i].kursArr
		schritt10FehlerBeiFachartKursArrayErstellung();

		schritt11FehlerBeiRegel_4_oder_5();

		schritt12FehlerBeiRegel_7_oder_8();

		schritt13FehlerBeiRegel_9(input);

		schritt14FehlerBeiRegel_10(input);

		// Zustände Speichern
		aktionZustandSpeichernS();
		aktionZustandSpeichernK();
		aktionZustandSpeichernG();
	}

	/**
	 * Überprüft alle Referenzen in {@link KursblockungInput} und auch die referentielle Integrität.
	 *
	 * @param pInput Das {@link KursblockungInput}-Objekt von der GUI.
	 */
	@SuppressWarnings("static-method")
	private void schritt01FehlerBeiReferenzen(final @NotNull GostBlockungsdatenManager pInput) {

		DeveloperNotificationException.ifNull("pInput", pInput);
		DeveloperNotificationException.ifNull("pInput.daten()", pInput.daten());
		DeveloperNotificationException.ifNull("pInput.daten().fachwahlen", pInput.daten().fachwahlen);
		DeveloperNotificationException.ifNull("pInput.faecherManager()", pInput.faecherManager());
		DeveloperNotificationException.ifNull("pInput.faecherManager().faecher()", pInput.faecherManager().faecher());
		DeveloperNotificationException.ifNull("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifNull("pInput.daten().kurse", pInput.daten().kurse);
		DeveloperNotificationException.ifNull("pInput.daten().regeln", pInput.daten().regeln);
		DeveloperNotificationException.ifInvalidID("pInput.getID()", pInput.getID());
		DeveloperNotificationException.ifNull("pInput", pInput);
		DeveloperNotificationException.ifNull("pInput", pInput);
		DeveloperNotificationException.ifNull("pInput", pInput);
		DeveloperNotificationException.ifNull("pInput", pInput);
		DeveloperNotificationException.ifNull("pInput", pInput);
		DeveloperNotificationException.ifArrayIsEmpty("GostKursart.values()", GostKursart.values());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().fachwahlen", pInput.daten().fachwahlen);
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.faecherManager().faecher()", pInput.faecherManager().faecher());
		DeveloperNotificationException.ifCollectionIsEmpty("pInput.daten().kurse", pInput.daten().kurse);
		final int schienenAnzahl = pInput.getSchienenAnzahl();
		DeveloperNotificationException.ifSmaller("schienenAnzahl", schienenAnzahl, 1);

		// #################### GostBlockungSchiene ####################

		final HashSet<Integer> usedSchiene = new HashSet<>();
		for (final @NotNull GostBlockungSchiene gSchiene : pInput.daten().schienen) {
			DeveloperNotificationException.ifInvalidID("gSchiene.id", gSchiene.id);
			DeveloperNotificationException.ifSmaller("gSchiene.id", gSchiene.nummer, 1);
			DeveloperNotificationException.ifGreater("gSchiene.id", gSchiene.nummer, schienenAnzahl);
			DeveloperNotificationException.ifSetAddsDuplicate("usedSchiene", usedSchiene, gSchiene.nummer);
		}

		// #################### KursblockungInputKursart ####################

		final @NotNull HashSet<@NotNull Integer> setKursarten = new HashSet<>();
		for (final @NotNull GostKursart iKursart : GostKursart.values()) {
			DeveloperNotificationException.ifNull("iKursart", iKursart);
			DeveloperNotificationException.ifInvalidID("iKursart.id", iKursart.id);
			DeveloperNotificationException.ifSetAddsDuplicate("setKursarten", setKursarten, iKursart.id);
		}

		// #################### KursblockungInputFach ####################

		final @NotNull HashSet<@NotNull Long> setFaecher = new HashSet<>();
		for (final @NotNull GostFach iFach : pInput.faecherManager().faecher()) {
			DeveloperNotificationException.ifNull("iFach", iFach);
			DeveloperNotificationException.ifInvalidID("iFach.id", iFach.id);
			DeveloperNotificationException.ifSetAddsDuplicate("setFaecher", setFaecher, iFach.id);
		}

		// #################### KursblockungInputKurs ####################

		final @NotNull HashSet<@NotNull Long> setKurse = new HashSet<>();
		for (final @NotNull GostBlockungKurs iKurs : pInput.daten().kurse) {
			DeveloperNotificationException.ifNull("iKurs", iKurs);
			DeveloperNotificationException.ifInvalidID("iKurs.id", iKurs.id);
			DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, iKurs.fach_id);
			DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, iKurs.kursart);
			DeveloperNotificationException.ifSetAddsDuplicate("setKurse", setKurse, iKurs.id);
		}

		// #################### KursblockungInputFachwahl ####################
		final @NotNull HashSet<@NotNull Long> setSchueler = new HashSet<>();
		for (final @NotNull Schueler gSchueler : pInput.daten().schueler)
			DeveloperNotificationException.ifSetAddsDuplicate("setSchueler", setSchueler, gSchueler.id);

		// #################### KursblockungInputFachwahl ####################

		for (final @NotNull GostFachwahl iFachwahl : pInput.daten().fachwahlen) {
			DeveloperNotificationException.ifNull("iFachwahl", iFachwahl);
			DeveloperNotificationException.ifInvalidID("iFachwahl.schuelerID", iFachwahl.schuelerID);
			DeveloperNotificationException.ifSetNotContains("setFaecher", setFaecher, iFachwahl.fachID);
			DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, iFachwahl.kursartID);
			DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, iFachwahl.schuelerID);
		}

		// #################### KursblockungInputRegel ####################

		for (final @NotNull GostBlockungRegel iRegel : pInput.daten().regeln) {
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
					schritt01FehlerBeiReferenzen_Regeltyp9(daten);
					break;
				case LEHRKRAEFTE_BEACHTEN:
					schritt01FehlerBeiReferenzen_Regeltyp10(daten);
					break;
				default:
					throw new DeveloperNotificationException("Unbekannter Regeltyp!");
			}

		}

	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp1(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<@NotNull Integer> setKursarten, final int schienenAnzahl) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("KURSART_SPERRE_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!", length != 3);

		final int kursartID = daten[0].intValue();
		DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, kursartID);

		final int von = daten[1].intValue(); // Schiene ist 1-indiziert!
		final int bis = daten[2].intValue(); // Schiene ist 1-indiziert!
		DeveloperNotificationException.ifTrue("KURSART_SPERRE_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!", !((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp2(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<@NotNull Long> setKurse, final int schienenAnzahl) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("KURS_FIXIERE_IN_SCHIENE daten.length=" + length + ", statt 2!", length != 2);

		final long kursID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);

		final int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!
		DeveloperNotificationException.ifTrue("KURS_FIXIERE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!", !((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp3(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<@NotNull Long> setKurse, final int schienenAnzahl) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("KURS_SPERRE_IN_SCHIENE daten.length=" + length + ", statt 2!", length != 2);

		final long kursID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);

		final int schiene = daten[1].intValue(); // Schiene ist 1-indiziert!
		DeveloperNotificationException.ifTrue("KURS_SPERRE_IN_SCHIENE (" + kursID + ", " + schiene + ") ist unlogisch!", !((schiene >= 1) && (schiene <= schienenAnzahl)));
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp4(final @NotNull Long @NotNull [] daten, final @NotNull HashSet<@NotNull Long> setSchueler, final @NotNull HashSet<@NotNull Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_FIXIEREN_IN_KURS daten.length=" + length + ", statt 2!", length != 2);

		final long schuelerID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID);

		final long kursID = daten[1];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp5(@NotNull final Long @NotNull [] daten, final @NotNull HashSet<@NotNull Long> setSchueler, final @NotNull HashSet<@NotNull Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("SCHUELER_VERBIETEN_IN_KURS daten.length=" + length + ", statt 2!", length != 2);

		final long schuelerID = daten[0];
		DeveloperNotificationException.ifSetNotContains("setSchueler", setSchueler, schuelerID);

		final long kursID = daten[1];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID);
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp6(@NotNull final Long @NotNull [] daten, final @NotNull HashSet<@NotNull Integer> setKursarten, final int schienenAnzahl) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS daten.length=" + length + ", statt 3!", length != 3);

		final int kursartID = daten[0].intValue();
		DeveloperNotificationException.ifSetNotContains("setKursarten", setKursarten, kursartID);

		final int von = daten[1].intValue(); // Schiene ist 1-indiziert!
		final int bis = daten[2].intValue(); // Schiene ist 1-indiziert!
		DeveloperNotificationException.ifTrue("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS (" + kursartID + ", " + von + ", " + bis + ") ist unlogisch!", !((von >= 1) && (von <= bis) && (bis <= schienenAnzahl)));
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp7(@NotNull final Long @NotNull [] daten, final @NotNull HashSet<@NotNull Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("KURS_VERBIETEN_MIT_KURS daten.length=" + length + ", statt 2!", length != 2);

		final long kursID1 = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID1);

		final long kursID2 = daten[1];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID2);

		DeveloperNotificationException.ifTrue("Die Regel 'KURS_VERBIETEN_MIT_KURS' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!", kursID1 == kursID2);
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp8(@NotNull final Long @NotNull [] daten, final @NotNull HashSet<@NotNull Long> setKurse) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("KURS_ZUSAMMEN_MIT_KURS daten.length=" + length + ", statt 2!", length != 2);

		final long kursID1 = daten[0];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID1);

		final long kursID2 = daten[1];
		DeveloperNotificationException.ifSetNotContains("setKurse", setKurse, kursID2);

		DeveloperNotificationException.ifTrue("Die Regel 'KURS_ZUSAMMEN_MIT_KURS' wurde mit einem Kurs (" + kursID1 + ") und sich selbst kombiniert!", kursID1 == kursID2);
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp9(final @NotNull Long @NotNull [] daten) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("KURS_MIT_DUMMY_SUS_AUFFUELLEN daten.length=" + length + ", statt 1!", length != 1);

		// BAR TODO schritt01FehlerBeiReferenzen_Regeltyp9
	}

	private static void schritt01FehlerBeiReferenzen_Regeltyp10(@NotNull final Long @NotNull [] daten) {
		final int length = daten.length;
		DeveloperNotificationException.ifTrue("LEHRKRAEFTE_BEACHTEN daten.length=" + length + ", statt 0!", length != 0);
	}

	private void schritt02FehlerBeiRegelGruppierung(final @NotNull List<@NotNull GostBlockungRegel> pRegeln) {
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

	private void schritt03FehlerBeiFachartenErstellung(final @NotNull GostBlockungsdatenManager pInput) {
		int nFacharten = 0;

		// Facharten aus Kursen extrahieren.
		final int nKurse = pInput.daten().kurse.size();
		for (final @NotNull GostBlockungKurs gKurs : pInput.daten().kurse) {
			final @NotNull GostFach fach = pInput.faecherManager().getOrException(gKurs.fach_id);
			final @NotNull GostKursart kursart = GostKursart.fromID(gKurs.kursart);

			KursblockungDynFachart dynFachart = _fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(_random, nFacharten, fach, kursart, _statistik);
				_fachartMap2D.put(fach.id, kursart.id, dynFachart);
				nFacharten++;
			}

			dynFachart.aktionMaxKurseErhoehen();
		}

		// Facharten aus SuS-Fachwahlen extrahieren.
		for (final @NotNull GostFachwahl iFachwahl : pInput.daten().fachwahlen) {
			final @NotNull GostFach fach = pInput.faecherManager().getOrException(iFachwahl.fachID);
			final @NotNull GostKursart kursart = GostKursart.fromID(iFachwahl.kursartID);

			KursblockungDynFachart dynFachart = _fachartMap2D.getOrNull(fach.id, kursart.id);
			if (dynFachart == null) {
				dynFachart = new KursblockungDynFachart(_random, nFacharten, fach, kursart, _statistik);
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
		for (int i = 0; i < _fachartArr.length; i++)
			kursSumme += _fachartArr[i].gibKurseMax();
		DeveloperNotificationException.ifTrue("Die Summe aller auf die Facharten verteilten Kurse ist ungleich der Gesamtkursanzahl.", kursSumme != nKurse);
	}

	private void schritt04FehlerBeiSchuelerErstellung(final @NotNull GostBlockungsdatenManager pInput) {
		// Schüler werden aus den Fachwahlen herausgefiltert.
		final @NotNull HashSet<@NotNull Long> setSchueler = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : pInput.daten().fachwahlen)
			setSchueler.add(fachwahl.schuelerID);

		final int nSchueler = setSchueler.size();
		final int nSchienen = pInput.getSchienenAnzahl();
		final int nKurse = pInput.getKursAnzahl();

		int i = 0;
		_schuelerArr = new KursblockungDynSchueler[nSchueler];
		for (final @NotNull Long schuelerID : setSchueler) {
			final long sID = schuelerID;
			final @NotNull KursblockungDynSchueler schueler = new KursblockungDynSchueler(_logger, _random, sID, _statistik, nSchienen, nKurse);
			_schuelerArr[i] = schueler;
			_schuelerMap.put(schuelerID, schueler);
			i++;
		}
	}

	private void schritt05FehlerBeiSchuelerFachwahlenErstellung(final @NotNull GostBlockungsdatenManager pInput,
			final @NotNull KursblockungDynSchueler @NotNull [] susArr) {

		// Für jeden Schüler eine Liste seiner Facharten (Fach + Kursart) erzeugen.
		final @NotNull HashMap<@NotNull KursblockungDynSchueler, @NotNull LinkedCollection<@NotNull KursblockungDynFachart>> mapSchuelerFA = new HashMap<>();
		for (int i = 0; i < susArr.length; i++)
			mapSchuelerFA.put(susArr[i], new LinkedCollection<>());

		// Fachwahl --> Fachart --> In die Liste des Schülers
		for (final @NotNull GostFachwahl iFachwahl : pInput.daten().fachwahlen) {
			final KursblockungDynSchueler schueler = _schuelerMap.get(iFachwahl.schuelerID);
			if (schueler == null)
				throw new DeveloperNotificationException(
						"GostFachwahl.schueler --> KursblockungDynSchueler (mapping fehlt)!");

			final LinkedCollection<@NotNull KursblockungDynFachart> dynFacharten = mapSchuelerFA.get(schueler);
			if (dynFacharten == null)
				throw new DeveloperNotificationException("dynFacharten == null");

			final @NotNull KursblockungDynFachart dynFachart = gibFachart(iFachwahl.fachID, iFachwahl.kursartID);
			dynFacharten.addLast(dynFachart);
		}

		// Schüler und Facharten verknüpfen.
		for (final @NotNull KursblockungDynSchueler schueler : susArr) {
			final LinkedCollection<@NotNull KursblockungDynFachart> listFA = mapSchuelerFA.get(schueler);
			if (listFA == null)
				throw new DeveloperNotificationException("mapSchuelerFA.get(schueler) == null (mapping fehlt)!");

			final @NotNull KursblockungDynFachart @NotNull [] arrFA = listFA.toArray(new KursblockungDynFachart[0]);
			schueler.aktionSetzeFachartenUndIDs(arrFA);
		}
	}

	private void schritt06FehlerBeiStatistikErstellung(final @NotNull KursblockungDynFachart @NotNull [] fachartArr,
			final @NotNull KursblockungDynSchueler @NotNull [] susArr,
			final @NotNull GostBlockungsdatenManager pInput) {
		final int nFacharten = fachartArr.length;
		final @NotNull int @NotNull [][] bewertungMatrixFachart = new int[nFacharten][nFacharten];

		// Zähle zunächst die Fachart-Paare pro Schüler
		for (int i = 0; i < susArr.length; i++) {
			final @NotNull KursblockungDynFachart @NotNull [] fa = susArr[i].gibFacharten();
			for (int i1 = 0; i1 < fa.length; i1++) {
				final int nr1 = fa[i1].gibNr();
				for (int i2 = i1 + 1; i2 < fa.length; i2++) {
					final int nr2 = fa[i2].gibNr();
					bewertungMatrixFachart[nr1][nr2]++;
					bewertungMatrixFachart[nr2][nr1]++;
				}
			}
		}

		// Der Malus ist relativ zur Anzahl an Kursen.
		// Beispiel: 7 SuS haben PH-CH gewählt. PH gibt es 2 Kurse. CH gibt es 1 Kurs.
		// --> Malus = 7 * 1000 / (2 + 1 - 1)
		for (int i1 = 0; i1 < nFacharten; i1++) {
			final int kursAnz1 = fachartArr[i1].gibKurseMax();
			final int nr1 = fachartArr[i1].gibNr();
			for (int i2 = 0; i2 < nFacharten; i2++) {
				final int kursAnz2 = fachartArr[i2].gibKurseMax();
				final int nr2 = fachartArr[i2].gibNr();
				if ((kursAnz1 == 0) || (kursAnz2 == 0)) {
					bewertungMatrixFachart[nr1][nr2] = 0;
					continue;
				}
				final int nenner = (kursAnz1 + kursAnz2 - 2);
				final int faktor = nenner == 0 ? 1000000 : (100 / nenner);
				bewertungMatrixFachart[nr1][nr2] *= faktor;
			}
			// Gleiche Kurs-Arten in einer Schiene --> großer Malus
			bewertungMatrixFachart[nr1][nr1] += 10000000;
		}

		_statistik.aktionInitialisiere(bewertungMatrixFachart, susArr.length, fachartArr.length,
				pInput.getKursAnzahl());
	}

	private void schritt07FehlerBeiSchienenErzeugung(final int pSchienen) {
		_schienenArr = new KursblockungDynSchiene[pSchienen];
		for (int nr = 0; nr < pSchienen; nr++)
			_schienenArr[nr] = new KursblockungDynSchiene(_logger, nr, _statistik);
	}

	private void schritt08FehlerBeiKursErstellung(final @NotNull GostBlockungsdatenManager pInput) {
		final int nKurse = pInput.getKursAnzahl();
		final int nSchienen = pInput.getSchienenAnzahl();

		// Jedem Kurs zwei Listen (von Schienen) zuordnen.
		final @NotNull HashMap<@NotNull Long, @NotNull LinkedCollection<@NotNull KursblockungDynSchiene>> mapKursSchieneFrei = new HashMap<>();
		final @NotNull HashMap<@NotNull Long, @NotNull LinkedCollection<@NotNull KursblockungDynSchiene>> mapKursSchieneLage = new HashMap<>();
		_kursArr = new KursblockungDynKurs[nKurse];
		int i = 0;
		for (final @NotNull GostBlockungKurs kurs : pInput.daten().kurse) {
			// 'Lage' ist zunächst leer.
			final @NotNull LinkedCollection<@NotNull KursblockungDynSchiene> schieneLage = new LinkedCollection<>();
			mapKursSchieneLage.put(kurs.id, schieneLage);

			// 'Frei' beinhaltet zunächst alle Schienen permutiert.
			final @NotNull LinkedCollection<@NotNull KursblockungDynSchiene> schieneFrei = new LinkedCollection<>();
			mapKursSchieneFrei.put(kurs.id, schieneFrei);
			final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, nSchienen);
			for (int j = 0; j < nSchienen; j++)
				schieneFrei.addLast(_schienenArr[perm[j]]);

			// Regel 1 - Alle Kurse einer bestimmten Kursart sperren.
//			final List<@NotNull GostBlockungRegel> regelnTyp1 = _regelMap.get(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)
	//		if (regelnTyp1 != null)
			for (final @NotNull GostBlockungRegel regel1 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
				if (kurs.kursart == regel1.parameter.get(0)) {
					final int von = regel1.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
					final int bis = regel1.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
					for (int schiene = von; schiene <= bis; schiene++)
						schieneFrei.remove(_schienenArr[schiene - 1]); // Intern 0-indiziert!
				}

			// Regel 6 - Alle Kurse die NICHT eine bestimmte Kursart haben in bestimmten Schienen sperren.
			for (final @NotNull GostBlockungRegel regel6 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
				final int von = regel6.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
				final int bis = regel6.parameter.get(2).intValue(); // DB-Schiene ist 1-indiziert!
				for (int schiene = 1; schiene <= _schienenArr.length; schiene++) {
					final boolean innerhalb = (von <= schiene) && (schiene <= bis);
					final boolean gleicheArt = kurs.kursart == regel6.parameter.get(0);
					if (innerhalb != gleicheArt)
						schieneFrei.remove(_schienenArr[schiene - 1]); // Intern 0-indiziert!
				}
			}

			// Regel 3 - Pro Kurs gesperrte Schienen entfernen.
			for (final @NotNull GostBlockungRegel regel3 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE))
				if (kurs.id == regel3.parameter.get(0)) {
					final int schiene = regel3.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
					schieneFrei.remove(_schienenArr[schiene - 1]); // Intern 0-indiziert!
				}

			// Regel 2 - Pro Kurs fixierte Schiene in "Lage" hinzufügen.
			for (final @NotNull GostBlockungRegel regel2 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
				if (kurs.id == regel2.parameter.get(0)) {
					final int schiene = regel2.parameter.get(1).intValue(); // DB-Schiene ist 1-indiziert!
					final @NotNull KursblockungDynSchiene dynSchiene = _schienenArr[schiene - 1]; // Intern
																									 // 0-indiziert!
					if (schieneLage.contains(dynSchiene))
						continue; // Doppeltfixierungen ignorieren
					if (!schieneFrei.contains(dynSchiene)) // Intern 0-indiziert!
						throw new UserNotificationException(
								"Die Regel 'KURS_FIXIERE_IN_SCHIENE' will Kurs (id=" + kurs.id + ") in Schiene ("
										+ schiene + ") fixieren, aber die Schiene wurde bereits gesperrt!");
					schieneFrei.remove(dynSchiene);
					schieneLage.addLast(dynSchiene);
				}

			// Fehler: Kurs belegt zu wenig Schienen.
			final int schienen = kurs.anzahlSchienen;
			if (schienen <= 0)
				throw new DeveloperNotificationException(
						"Kurs (" + kurs.id + ") belegt nur " + schienen + " Schienen, das ist zu wenig.");

			// Fehler: Kurs belegt zu viele Schienen.
			if (schienen > _schienenArr.length)
				throw new DeveloperNotificationException("Es gibt " + _schienenArr.length + " Schienen, aber der Kurs ("
						+ kurs.id + ") möchte " + schienen + " Schienen belegen.");

			// Fehler: Zu viel fixiert?
			final int pSchienenLageFixiert = schieneLage.size();
			if (pSchienenLageFixiert > schienen)
				throw new DeveloperNotificationException("Kurs (" + kurs.id + ") fixert " + pSchienenLageFixiert
						+ " Schienen, das ist mehr als seine Schienenanzahl " + schienen + ".");

			// ListFrei ---> ListLage --> Fehler: Zu viel gesperrt?
			while (schieneLage.size() < schienen) {
				if (schieneFrei.isEmpty())
					throw new UserNotificationException("Der Kurs (" + kurs.id
							+ ") hat zu viele Schienen gesperrt, so dass seine Schienenanzahl nicht erfüllt werden kann!");
				schieneLage.addLast(schieneFrei.pollFirst());
			}

			// List --> Array
			final @NotNull KursblockungDynSchiene @NotNull [] pSchienenLage = schieneLage
					.toArray(new KursblockungDynSchiene[0]);
			final @NotNull KursblockungDynSchiene @NotNull [] pSchienenFrei = schieneFrei
					.toArray(new KursblockungDynSchiene[0]);
			final @NotNull KursblockungDynFachart dynFachart = gibFachart(kurs.fach_id, kurs.kursart);
			final @NotNull KursblockungDynKurs dynKurs = new KursblockungDynKurs(_random, pSchienenLage,
					pSchienenLageFixiert, pSchienenFrei, kurs.id, dynFachart, _logger, i);

			// Kurs --> Array
			_kursArr[i] = dynKurs;

			// Kurs --> Map
			_kursMap.put(kurs.id, dynKurs);

			// Nächster Kurs...
			i++;
		}

	}

	private void schritt09FehlerBeiKursFreiErstellung() {
		// Zähle Kurse mit Freiheitsgraden.
		int nKursFrei = 0;
		for (final KursblockungDynKurs kurs : _kursArr)
			if (kurs.gibHatFreiheitsgrade())
				nKursFrei++;

		// Kopiere Kurse mit Freiheitsgraden.
		_kursArrFrei = new KursblockungDynKurs[nKursFrei];
		int j = 0;
		for (int i = 0; i < _kursArr.length; i++)
			if (_kursArr[i].gibHatFreiheitsgrade()) {
				_kursArrFrei[j] = _kursArr[i];
				j++;
			}
	}

	private void schritt10FehlerBeiFachartKursArrayErstellung() {
		// Pro Fachart eine Liste zum Speichern aller zugehörigen Kurse.
		final int nFacharten = _fachartArr.length;
		final @NotNull HashMap<@NotNull Integer, @NotNull LinkedCollection<@NotNull KursblockungDynKurs>> mapFachartList = new HashMap<>();
		for (int i = 0; i < nFacharten; i++)
			mapFachartList.put(i, new LinkedCollection<>());

		// Kurse sammeln...
		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			final int fachartNr = kurs.gibFachart().gibNr();
			final LinkedCollection<@NotNull KursblockungDynKurs> fachartKurse = mapFachartList.get(fachartNr);
			if (fachartKurse == null)
				throw new DeveloperNotificationException("mapFachartList.get(fachartNr) == null");
			fachartKurse.addLast(kurs);
		}

		// Pro Fachart: Liste zu Array konvertieren und übergeben.
		for (int nr = 0; nr < nFacharten; nr++) {
			final LinkedCollection<@NotNull KursblockungDynKurs> list = mapFachartList.get(nr);
			if (list == null)
				throw new DeveloperNotificationException("mapFachartList.get(nr) == null");
			final @NotNull KursblockungDynKurs @NotNull [] tmpKursArr = list.toArray(new KursblockungDynKurs[0]);
			_fachartArr[nr].aktionSetKurse(tmpKursArr);
		}

	}

	private void schritt11FehlerBeiRegel_4_oder_5() {
		// Regel 4 - SCHUELER_FIXIEREN_IN_KURS
		for (final @NotNull GostBlockungRegel regel4 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			final long schuelerID = regel4.parameter.get(0);
			final long kursID = regel4.parameter.get(1);
			final @NotNull KursblockungDynSchueler schueler = gibSchueler(schuelerID);
			final @NotNull KursblockungDynKurs fixierterKurs = gibKurs(kursID);
			// Alle anderen Kurse der selben Fachart verbieten ...
			for (final @NotNull KursblockungDynKurs kurs : fixierterKurs.gibFachart().gibKurse())
				if (kurs != fixierterKurs)
					schueler.aktionSetzeKursSperrung(kurs.gibInternalID());
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

	private void schritt12FehlerBeiRegel_7_oder_8() {
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

	private void schritt13FehlerBeiRegel_9(final @NotNull GostBlockungsdatenManager pInput) {
		// Regel 9 - KURS_MIT_DUMMY_SUS_AUFFUELLEN
		for (final @NotNull GostBlockungRegel regel9 : MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			DeveloperNotificationException.ifNull("regel9", regel9);
			// TODO BAR  KURS_MIT_DUMMY_SUS_AUFFUELLEN
		}
	}

	private void schritt14FehlerBeiRegel_10(final @NotNull GostBlockungsdatenManager pInput) {
		// Regel 10 - LEHRKRAEFTE_BEACHTEN
		final List<@NotNull GostBlockungRegel> regelnTyp10 = MapUtils.getOrCreateArrayList(_regelMap, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN);
		if (regelnTyp10.isEmpty()) return;

		DeveloperNotificationException.ifGreater("Liste of regelnTyp10", regelnTyp10.size(), 1);

		// Sammle zunächst alle potentiellen Kurse.
		final @NotNull ArrayList<@NotNull GostBlockungKurs> vKurseMitLehrkraft = new ArrayList<>();
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

	private @NotNull KursblockungDynFachart gibFachart(final long fachID, final int kursart) {
		return _fachartMap2D.getNonNullOrException(fachID, kursart);
	}

	private @NotNull KursblockungDynSchueler gibSchueler(final long schuelerID) {
		final KursblockungDynSchueler schueler = _schuelerMap.get(schuelerID);
		if (schueler == null)
			throw new DeveloperNotificationException("schuelerMap.get(" + schuelerID + ") --> kein Mapping!");
		return schueler;
	}

	private @NotNull KursblockungDynKurs gibKurs(final long kursID) {
		final KursblockungDynKurs kurs = _kursMap.get(kursID);
		if (kurs == null)
			throw new DeveloperNotificationException("kursMap.get(" + kursID + ") --> kein Mapping!");
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
		return _logger;
	}

	/**
	 * Liefert das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 *
	 * @return Das Statistik-Objekt (für Anfragen zu Nichtwahlen, Kursdifferenzen, etc.).
	 */
	@NotNull
	KursblockungDynStatistik gibStatistik() {
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
	int gibSchienenAnzahl() {
		return _schienenArr.length;
	}

	/**
	 * Erzeugt ein Objekt {@link GostBlockungsergebnisManager}. Dieses Objekt beinhaltet alle Informationen aus denen
	 * die GUI die Kurs-Zu-Schiene und die SuS-Zu-Kurs-Zuordnungen rekonstruieren kann.
	 *
	 * @param  pDataManager  Das Eingabe-Objekt (der Daten-Manager).
	 * @param  pErgebnisID   Die ID des Ergebnisses.
	 * @return               Das Blockungsergebnis für die GUI.
	 */
	@NotNull
	GostBlockungsergebnisManager gibErzeugtesKursblockungOutput(final @NotNull GostBlockungsdatenManager pDataManager,
			final long pErgebnisID) {
		final @NotNull GostBlockungsergebnisManager out = new GostBlockungsergebnisManager(pDataManager, pErgebnisID);

		// Erzeuge die Kurs-Schienen-Zuordnungen (Manager hat eine 1-Indizierung der Schiene!)
		for (final @NotNull KursblockungDynKurs dynKurs : _kursArr)
			for (final int schienenNr : dynKurs.gibSchienenLage())
				out.setKursSchienenNr(dynKurs.gibDatenbankID(), schienenNr + 1);

		// Erzeuge die Schüler-Kurs-Zuordnungen.
		for (final @NotNull KursblockungDynSchueler dynSchueler : _schuelerArr)
			for (final KursblockungDynKurs kurs : dynSchueler.gibKurswahlen())
				if (kurs != null)
					out.setSchuelerKurs(dynSchueler.gibDatenbankID(), kurs.gibDatenbankID(), true);

		// Erzeuge durch Regeln forcierte Schüler-Kurs-Zuordnungen.
		for (final @NotNull GostBlockungRegel gRegel : pDataManager.getMengeOfRegeln())
			if (gRegel.typ == GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				final long schuelerID = gRegel.parameter.get(0);
				final long kursID = gRegel.parameter.get(1);
				if (!out.getOfSchuelerOfKursIstZugeordnet(schuelerID, kursID))
					out.setSchuelerKurs(schuelerID, kursID, true); // Kann zu Kollisionen führen!
			}

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
	 * Liefert ein Array aller Schülerinnen und Schüler. Falls der Parameter {@code pNurMultiKurse} TRUE ist, dann
	 * werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @param  pNurMultiKurse Falls TRUE, dann werden nur SuS mit mindestens einem Multikurs ausgewählt.
	 *
	 * @return                Ein Array aller Schülerinnen und Schüler.
	 */
	@NotNull
	KursblockungDynSchueler @NotNull [] gibSchuelerArray(final boolean pNurMultiKurse) {
		if (pNurMultiKurse) {
			final @NotNull LinkedCollection<@NotNull KursblockungDynSchueler> list = new LinkedCollection<>();
			for (final KursblockungDynSchueler schueler : _schuelerArr) {
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

	/** Entfernt alle SuS aus ihren Kursen. */
	void aktionSchuelerAusAllenKursenEntfernen() {
		for (int i = 0; i < _schuelerArr.length; i++) {
			_schuelerArr[i].aktionKurseAlleEntfernen();
		}
	}

	/** Debug Ausgaben. Nur für Testzwecke. */
	void debug() {
		_logger.modifyIndent(+4);

		_logger.logLn("########## Schienen ##########");
		for (int i = 0; i < _schienenArr.length; i++) {
			_logger.logLn("Schiene " + (i + 1));
			_schienenArr[i].debug(false);
		}

		_logger.logLn("########## Facharten ##########");
		for (int i = 0; i < _fachartArr.length; i++) {
			_logger.logLn("Fachart " + _fachartArr[i] + " --> " + _fachartArr[i].gibKursdifferenz());
			_fachartArr[i].debug(_schuelerArr);
		}

		_logger.modifyIndent(-4);

		_statistik.debug("");
	}

	/** Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand S. */
	void aktionZustandSpeichernS() {
		_statistik.aktionBewertungSpeichernS();

		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			kurs.aktionZustandSpeichernS();
		}

		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionZustandSpeichernS();
		}

	}

	/** Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand K. */
	void aktionZustandSpeichernK() {
		_statistik.aktionBewertungSpeichernK();

		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			kurs.aktionZustandSpeichernK();
		}

		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionZustandSpeichernK();
		}
	}

	/** Speichert die Bewertung, die Kursverteilung und die Schülerverteilung im Zustand G. */
	void aktionZustandSpeichernG() {
		_statistik.aktionBewertungSpeichernG();

		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			kurs.aktionZustandSpeichernG();
		}

		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionZustandSpeichernG();
		}

		// statistik.debug("GLOBAL-G gespeichert!");
	}

	/** Lädt den zuvor gespeicherten Zustand S (Kursverteilung und Schülerverteilung). */
	void aktionZustandLadenS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			kurs.aktionZustandLadenS();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionZustandLadenS();
		}
	}

	/** Lädt den zuvor gespeicherten Zustand K (Kursverteilung und Schülerverteilung). */
	void aktionZustandLadenK() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			kurs.aktionZustandLadenK();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionZustandLadenK();
		}

	}

	/** Lädt den zuvor gespeicherten Zustand G (Kursverteilung und Schülerverteilung). */
	void aktionZustandLadenG() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			kurs.aktionZustandLadenG();
		}

		// 3) Dann SuS den Kursen hinzufügen.
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionZustandLadenG();
		}

	}

	/** Lädt den zuvor gespeicherten Zustand K (nur Kursverteilung, ohne Schülerverteilung). */
	void aktionZustandLadenKohneSuS() {
		// Die Reihenfolge ist wichtig!

		// 1) Alle SuS aus den Kursen entfernen
		for (final @NotNull KursblockungDynSchueler schueler : _schuelerArr) {
			schueler.aktionKurseAlleEntfernen();
		}

		// 2) Dann Kurse verschieben
		for (final @NotNull KursblockungDynKurs kurs : _kursArr) {
			kurs.aktionZustandLadenK();
		}

	}

	/** Verteilt alle Kurse auf ihre Schienen zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. */
	void aktionKurseFreieZufaelligVerteilen() {
		for (final @NotNull KursblockungDynKurs kurs : _kursArrFrei)
			kurs.aktionZufaelligVerteilen();
	}

	/** Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. */
	void aktionKursVerteilenEinenZufaelligenFreien() {
		if (_kursArrFrei.length == 0)
			return;
		final int index = _random.nextInt(_kursArrFrei.length);
		final @NotNull KursblockungDynKurs kurs = _kursArrFrei[index];
		kurs.aktionZufaelligVerteilen();
	}

	/**
	 * Verteilt einen Kurs zufällig. Kurse die keinen Freiheitsgrad haben, werden dabei ignoriert. Multikurse werden
	 * ebenso ignoriert.
	 */
	void aktionKursFreienEinenZufaelligVerteilenAberNichtMultikurse() {
		if (_kursArrFrei.length == 0) {
			return;
		}
		final int[] perm = KursblockungStatic.gibPermutation(_random, _kursArrFrei.length);
		for (final int index : perm) {
			final @NotNull KursblockungDynKurs kurs = _kursArrFrei[index];
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
		return _statistik.gibBewertungZustandS_NW_KD();
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
		for (int pSchueler = 0; pSchueler < perm.length; pSchueler++) {
			final KursblockungDynSchueler schueler = _schuelerArr[perm[pSchueler]];
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
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, _schuelerArr.length);

		for (int p = 0; p < perm.length; p++) {
			final int i = perm[p];
			final KursblockungDynSchueler schueler = _schuelerArr[i];
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
		final @NotNull int[] perm = KursblockungStatic.gibPermutation(_random, _schuelerArr.length);

		for (int p = 0; p < perm.length; p++) {
			final int i = perm[p];
			final KursblockungDynSchueler schueler = _schuelerArr[i];
			schueler.aktionKurseVerteilenNurMultikurseZufaellig();
			schueler.aktionKurseVerteilenNurFachartenMitEinemKurs();
			schueler.aktionKurseVerteilenMitBipartiteMatchingGewichtetem();
		}
	}

}
