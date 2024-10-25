package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.adt.PairNN;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungRegelUpdate;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInputKurs;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.kursblockung.SchuelerblockungAlgorithmus;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelParameterTyp;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.SetUtils;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsergebnis}. Hierbei werden auch Hilfsmethoden zur
 * Interpretation der Daten erzeugt. <br>
 * Nur Methoden, die mit "state" beginnen verändern den Zustand der Daten. <br>
 */
public class GostBlockungsergebnisManager {

	/** Der Blockungsdaten-Manager ist das Elternteil dieses Objektes. */
	private final @NotNull GostBlockungsdatenManager _parent;

	/** Das Blockungsergebnis ist das zugehörige Eltern-Datenobjekt. */
	private @NotNull GostBlockungsergebnis _ergebnis = new GostBlockungsergebnis();


	/** Liste aller Fehlermeldungen. */
	private @NotNull List<String> _fehlermeldungen = new ArrayList<>();

	// ################################# UPDATE 0 #################################

	// Sets der Objekte.

	/** Set aller Schienen-IDs. */
	private @NotNull HashSet<Long> _schienenIDset = new HashSet<>();

	/** Set aller Schienen-Nummern. */
	private @NotNull HashSet<Integer> _schienenNRset = new HashSet<>();

	/** Set aller Kurs-IDs. */
	private @NotNull HashSet<Long> _kursIDset = new HashSet<>();

	/** Set aller Fach-IDs. */
	private @NotNull HashSet<Long> _fachIDset = new HashSet<>();

	/** Set aller Schüler-IDs. */
	private @NotNull HashSet<Long> _schuelerIDset = new HashSet<>();

	// Maps der Objekte.

	/** Map von Schienen-ID nach {@link GostBlockungsergebnisSchiene}. */
	private @NotNull Map<Long, GostBlockungsergebnisSchiene> _schienenID_to_schiene = new HashMap<>();

	/** Map von Schienen-NR nach {@link GostBlockungsergebnisSchiene}. */
	private @NotNull Map<Integer, GostBlockungsergebnisSchiene> _schienenNR_to_schiene = new HashMap<>();

	/** Map von Kurs-ID nach {@link GostBlockungsergebnisKurs}. */
	private @NotNull Map<Long, GostBlockungsergebnisKurs> _kursID_to_kurs = new HashMap<>();

	/** Map von Schueler-ID nach {@link Schueler}. */
	private @NotNull Map<Long, Schueler> _schuelerID_to_schueler = new HashMap<>();

	/** Map von Schienen-ID nach Long-Set (von Kursen). */
	private @NotNull Map<Long, Set<Long>> _schienenID_to_kursIDSet = new HashMap<>();

	/** Map von Schueler-ID nach {@link GostBlockungsergebnisKurs}-Set (Kurse des Schüler, die aufgrund der aktuellen Fachwahlen ungültig sind).*/
	private @NotNull Map<Long, Set<GostBlockungsergebnisKurs>> _schuelerID_to_ungueltigeKurseSet = new HashMap<>();

	// ################################# UPDATE 1 #################################

	/** Map von Kurs-ID nach Long-Set (von Schülern). */
	private @NotNull Map<Long, Set<Long>> _kursID_to_schuelerIDSet = new HashMap<>();

	/** Map von Fach-ID nach {@link GostBlockungsergebnisKurs}-List. */
	private @NotNull Map<Long, List<GostBlockungsergebnisKurs>> _fachID_to_kurseList = new HashMap<>();

	/** Map von Kurs-ID nach {@link GostBlockungsergebnisSchiene}-Set. */
	private @NotNull Map<Long, Set<GostBlockungsergebnisSchiene>> _kursID_to_schienenSet = new HashMap<>();

	/** Map von Kurs-ID nach Integer (Anzahl an externen SuS). */
	private @NotNull Map<Long, Integer> _kursID_to_dummySuS = new HashMap<>();

	/**  Map von Fachart-ID nach {@link GostBlockungsergebnisKurs}-List (Alle Kurse der selben Fachart). */
	private @NotNull Map<Long, List<GostBlockungsergebnisKurs>> _fachartID_to_kurseList = new HashMap<>();

	// ################################# UPDATE 2 #################################

	/** Map von Schüler-ID nach {@link GostBlockungsergebnisKurs}-Set. */
	private @NotNull Map<Long, Set<GostBlockungsergebnisKurs>> _schuelerID_to_kurseSet = new HashMap<>();

	/** Menge aller Fachart-IDs sortiert nach der aktuellen Sortiervariante. */
	private @NotNull List<Long> _fachartIDList_sortiert = new ArrayList<>();

	/** Map von Fachart-ID nach Integer (Kursdifferenz der Fachart). */
	private @NotNull Map<Long, Integer> _fachartID_to_kursdifferenz = new HashMap<>();

	/** Map von Schienen-ID nach Integer (Anzahl an Kollisionen in der Schiene). */
	private @NotNull Map<Long, Integer> _schienenID_to_kollisionen = new HashMap<>();

	/** Map von Schienen-ID nach Integer (Anzahl der SuS in der Schiene). */
	private @NotNull Map<Long, Integer> _schienenID_to_susAnzahl = new HashMap<>();

	/** Map von Schüler-ID nach Map von Schienen-ID nach {@link GostBlockungsergebnisKurs}-Set (Alle Kurse des Schülers in der Schiene).*/
	private @NotNull HashMap2D<Long, Long, Set<GostBlockungsergebnisKurs>> _schuelerID_schienenID_to_kurseSet = new HashMap2D<>();

	/** Map von Schienen-ID nach Map von Fachart-ID nach {@link GostBlockungsergebnisKurs}-List (Alle Kurse pro Schiene und Fachart). */
	private @NotNull HashMap2D<Long, Long, List<GostBlockungsergebnisKurs>> _schienenID_fachartID_to_kurseList = new HashMap2D<>();

	// ################################# UPDATE 3 #################################

	/** Map von Kursdifferenz nach String-List (Facharten mit dieser Kursdifferenzen). */
	private @NotNull Map<Integer, List<String>> _kursdifferenz_to_fachartenList = new HashMap<>();

	/** Map von Schüler-ID Integer (Summe aller Kollisionen des Schülers). */
	private @NotNull Map<Long, Integer> _schuelerID_to_kollisionen = new HashMap<>();

	/**  Map von Schüler-ID nach Map von Fach-ID nach {@link GostBlockungsergebnisKurs} (Die zugeordnete Wahl des Schülers in dem Fach, auch NULL möglich).*/
	private @NotNull HashMap2D<Long, Long, @AllowNull GostBlockungsergebnisKurs> _schuelerID_fachID_to_kurs_or_null = new HashMap2D<>();

	// ############################################################################

	/** Von Regel-ID Regel-TYP nach List (alle Regelverletzungen des Typs als String-Menge). */
	private @NotNull Map<Integer, List<String>> _regelTyp_to_verletzungList = new HashMap<>();

	/** Von Regel-ID nach String (Beschreibung der Regelverletzung). */
	private @NotNull Map<Long, String> _regelID_to_verletzungString = new HashMap<>();

	/** Textuelle Darstellung aller Regelverletzungen der definierten Regeln. */
	private @NotNull String _regelverletzungen_tooltip1_regeln = "";

	/** Textuelle Darstellung aller Regelverletzungen der Wahlkonflikte. */
	private @NotNull String _regelverletzungen_tooltip2_wahlkonflikte = "";

	/** Textuelle Darstellung aller Regelverletzungen der Kursdifferenzen. */
	private @NotNull String _regelverletzungen_tooltip3_kursdifferenzen = "";

	/** Textuelle Darstellung aller Regelverletzungen der Fächerparallelität. */
	private @NotNull String _regelverletzungen_tooltip4_faecherparallelitaet = "";

	/** Entscheidet, welcher Comparator verwendet wird mit 1 = (KURSART, FACH) andernfalls (FACH, KURSART). */
	private int _fachartmenge_sortierung = 1;

	/** Comparator für die Facharten nach (KURSART, FACH). */
	private final @NotNull Comparator<Long> _fachartComparator_kursart_fach;

	/** Comparator für die Facharten nach (FACH, KURSART). */
	private final @NotNull Comparator<Long> _fachartComparator_fach_kursart;

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<GostBlockungsergebnisKurs> _kursComparator_kursart_fach_kursnummer;

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<GostBlockungsergebnisKurs> _kursComparator_fach_kursart_kursnummer;

	private int _bewertung3_KD_nur_LK = 0;
	private int _bewertung3_KD_nur_GK = 0;
	private int _bewertung3_KD_nur_REST = 0;

	/**
	 * Erstellt einen leeren GostBlockungsergebnisManager in Bezug auf GostBlockungsdatenManager. Die ID des leeren
	 * Ergebnisses ist -1 und muss noch gesetzt werden.
	 *
	 * @param pParent                  Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der
	 *                                 Blockung)
	 * @param pGostBlockungsergebnisID Die ID des Blockungsergebnisses.
	 */
	public GostBlockungsergebnisManager(final @NotNull GostBlockungsdatenManager pParent, final long pGostBlockungsergebnisID) {
		_parent = pParent;
		_fachartComparator_kursart_fach = createComparatorFachartKursartFach();
		_fachartComparator_fach_kursart = createComparatorFachartFachKursart();
		_kursComparator_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_kursComparator_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_ergebnis = new GostBlockungsergebnis();
		_ergebnis.id = pGostBlockungsergebnisID;
		_ergebnis.blockungID = _parent.getID();
		_ergebnis.gostHalbjahr = _parent.daten().gostHalbjahr;
		stateClear();
	}

	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis.
	 *
	 * @param pParent   Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der Blockung)
	 * @param pErgebnis Das Ergebnis, welches kopiert wird.
	 */
	public GostBlockungsergebnisManager(final @NotNull GostBlockungsdatenManager pParent, final @NotNull GostBlockungsergebnis pErgebnis) {
		_parent = pParent;
		_fachartComparator_kursart_fach = createComparatorFachartKursartFach();
		_fachartComparator_fach_kursart = createComparatorFachartFachKursart();
		_kursComparator_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_kursComparator_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_ergebnis = pErgebnis;
		_ergebnis.blockungID = _parent.getID();
		_ergebnis.gostHalbjahr = _parent.daten().gostHalbjahr;
		stateClear();
	}

	/**
	 * Baut alle Datenstrukturen neu auf.
	 */
	public void stateRevalidateEverything() {
		stateClear();
	}

	private void stateClear() {
		// 1) Bewertung des GostBlockungsergebnis zurücksetzen.
		_ergebnis.bewertung = new GostBlockungsergebnisBewertung();

		// 2) Aufbau der internen Datenstrukturen (in der Regel Maps).
		_fehlermeldungen = new ArrayList<>();

		update_0_schienenIDset_schienenNRset();
		update_0_kursIDset();
		update_0_fachIDset();
		update_0_schuelerIDset();
		update_0_schienenID_to_schiene_schienenNR_to_schiene();
		update_0_kursID_to_kurs();
		update_0_schuelerID_to_schueler();
		update_0_schienenID_to_kursIDSet();

		update_1_kursID_to_schuelerIDSet_schuelerID_to_ungueltigeKurseSet();     	// _kursIDset, _kursID_to_kurs
		update_1_kursID_to_dummySuS(); 												// _kursIDset
		update_1_fachID_to_kurseList();												// _fachIDset, _kursID_to_kurs
		update_1_kursID_to_schienenSet();           								// _kursIDset, _schienenID_to_kursIDSet
		update_1_fachartID_to_kurseList();											// _kursID_to_kurs

		update_2_schuelerID_to_kurseSet();											// _schuelerIDset, _kursID_to_kurs, _kursID_to_schuelerIDSet,
		update_2_fachartIDList_sortiert();											// _fachartID_to_kurseList
		update_2_fachartID_to_kursdifferenz();										// _fachartID_to_kurseList, _kursID_to_dummySuS
		update_2_schienenID_to_kollisionen();										// _schienenID_to_kursIDSet, _kursID_to_schuelerIDSet
		update_2_schienenID_to_susAnzahl();											// _schienenID_to_kursIDSet, _kursID_to_schuelerIDSet
		update_2_schuelerID_schienenID_to_kurseSet();								// _schienenID_to_kursIDSet, _kursID_to_schuelerIDSet, _schuelerIDset
		update_2_schienenID_fachartID_to_kurseList();								// _kursID_to_schienenSet, _fachartID_to_kurseList, _kursID_to_kurs, _schienenIDset

		update_3_kursdifferenz_to_fachartenList();                                  // _fachartID_to_kursdifferenz
		update_3_schuelerID_to_kollisionen();										// _schuelerID_schienenID_to_kurseSet
		update_3_schuelerID_fachID_to_kurs_or_null();                               // _schuelerID_to_kurseSet

		if (!_fehlermeldungen.isEmpty()) {
			System.out.println("Es sind Fehler aufgetreten: ");
			for (final @NotNull String meldung : _fehlermeldungen)
				System.out.println("    " + meldung);
		}

		// Kursmenge pro Schiene sortieren.
		for (final @NotNull GostBlockungsergebnisSchiene schiene : _ergebnis.schienen) {
			final @NotNull List<GostBlockungsergebnisKurs> kursmenge = schiene.kurse;
			if (_fachartmenge_sortierung == 1) {
				kursmenge.sort(_kursComparator_kursart_fach_kursnummer);
			} else {
				kursmenge.sort(_kursComparator_fach_kursart_kursnummer);
			}
		}

		// 4) "_ergebnis.bewertung" aktualisieren.
		stateClearErgebnisBewertung1();
		stateClearErgebnisBewertung2();
		stateClearErgebnisBewertung3();
		stateClearErgebnisBewertung4();

		// Die Bewertung im DatenManager aktualisieren.
		_parent.ergebnisUpdateBewertung(_ergebnis);
	}

	private void stateClearErgebnisBewertung1() {
		// Bewertungskriterium 1a (regelVerletzungen)
		_regelTyp_to_verletzungList = new HashMap<>();
		_regelID_to_verletzungString = new HashMap<>();

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			stateRegelvalidierung2_kurs_fixieren_in_schiene(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE))
			stateRegelvalidierung3_kurs_sperren_in_schiene(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			stateRegelvalidierung4_schueler_fixieren_in_kurs(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS))
			stateRegelvalidierung5_schueler_verbieten_in_kurs(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS))
			stateRegelvalidierung7_kurs_verbieten_mit_kurs(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS))
			stateRegelvalidierung8_kurs_zusammen_mit_kurs(r);

		// stateRegelvalidierung9 ist nicht nötig

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN))
			stateRegelvalidierung10_lehrkraefte_beachten(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH))
			stateRegelvalidierung11_schueler_zusammen_mit_schueler_in_fach(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			stateRegelvalidierung12_schueler_verbieten_mit_schueler_in_fach(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			stateRegelvalidierung13_schueler_zusammen_mit_schueler(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			stateRegelvalidierung14_schueler_verbieten_mit_schueler(r);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL))
			stateRegelvalidierung15_kurs_maximale_schueleranzahl(r);

		// stateRegelvalidierung16 ist nicht nötig
		// stateRegelvalidierung17 ist nicht nötig

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE))
			stateRegelvalidierung18_fach_kursart_maxProSchiene(r);

		// Bewertungskriterium 1b (anzahlKurseNichtZugeordnet)
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet = 0;
		for (final long idKurs : _kursID_to_schienenSet.keySet()) {
			final int sizeSoll = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_kurs, idKurs).anzahlSchienen;
			final int sizeIst = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schienenSet, idKurs).size();
			_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(sizeSoll - sizeIst);
		}

		_regelverletzungen_tooltip1_regeln = stateClearErgebnisTooltip1(); // benötigt _regelTyp_to_verletzungenList
	}

	private @NotNull String stateClearErgebnisTooltip1() {
		final @NotNull StringBuilder sb = new StringBuilder();

		int konflikte = 0;
		int konflikte_ignored = 0;
		for (final int idRegeltyp : GostKursblockungRegelTyp.ANZEIGE_REIHENFOLGE)
			for (final @NotNull String fehlermeldung : MapUtils.getOrCreateArrayList(_regelTyp_to_verletzungList, idRegeltyp)) {
				if (konflikte < 10) {
					sb.append(fehlermeldung + "\n");
				} else {
					konflikte_ignored++;
				}
				konflikte++;
			}
		if (konflikte == 0)
			return "";

		return konflikte + " Regelverletzungen\n" + sb.toString() + (konflikte_ignored == 0 ? "" : "+" + konflikte_ignored + " weitere Konflikte.");
	}

	private void stateClearErgebnisBewertung2() {
		// Bewertungskriterium 2a (anzahlSchuelerNichtZugeordnet)
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet = 0;
		for (final long idSchueler : _schuelerID_fachID_to_kurs_or_null.getKeySet())
			for (final long idFach : _schuelerID_fachID_to_kurs_or_null.getKeySetOf(idSchueler))
				if (_schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler, idFach) == null)
					_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		// ... wenn ein Schüler ignoriert werden soll, dann bewerte nicht zugeordnete Kurse als zugeordnet.
		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			final long idSchueler = regel.parameter.get(0);
			for (final @NotNull GostFachwahl gFachwahl : _parent.schuelerGetListeOfFachwahlen(idSchueler))
				if (getOfSchuelerOfFachZugeordneterKurs(idSchueler, gFachwahl.fachID) == null)
					_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		}

		// Bewertungskriterium 2b (anzahlSchuelerKollisionen)
		_ergebnis.bewertung.anzahlSchuelerKollisionen = 0;
		for (final long idSchueler : _schuelerID_to_kollisionen.keySet()) {
			final int kollisionen = DeveloperNotificationException.ifMapGetIsNull(_schuelerID_to_kollisionen, idSchueler);
			_ergebnis.bewertung.anzahlSchuelerKollisionen += kollisionen;
		}

		_regelverletzungen_tooltip2_wahlkonflikte = stateClearErgebnisTooltip2();
	}

	private @NotNull String stateClearErgebnisTooltip2() {
		final @NotNull StringBuilder sb = new StringBuilder();

		// Nichtwahlen des Schülers.
		int wahlkonflikte = 0;
		int wahlkonflikte_ignored = 0;
		for (final long idSchueler : _schuelerID_fachID_to_kurs_or_null.getKeySet()) {
			final var entries = _schuelerID_fachID_to_kurs_or_null.getSubMapOrException(idSchueler).entrySet();
			for (final @NotNull Entry<Long, GostBlockungsergebnisKurs> e : entries)
				if (e.getValue() == null) {
					if (wahlkonflikte < 10) {
						final long idFach = e.getKey();
						final int kursart = _parent.schuelerGetOfFachFachwahl(idSchueler, idFach).kursartID;
						sb.append(_parent.toStringSchuelerSimple(idSchueler) + " ist im Fach " + _parent.toStringFachartSimple(idFach, kursart)
								+ " keinem Kurs zugeordnet.\n");
					} else {
						wahlkonflikte_ignored++;
					}
					wahlkonflikte++;
				}
		}

		// Kollisionen des Schülers.
		for (final long idSchueler : _schuelerID_schienenID_to_kurseSet.getKeySet())
			for (final @NotNull Entry<Long, Set<GostBlockungsergebnisKurs>> e : _schuelerID_schienenID_to_kurseSet
					.getSubMapOrException(idSchueler).entrySet()) {
				final @NotNull Set<GostBlockungsergebnisKurs> set = e.getValue();
				if (set.size() <= 1)
					continue;
				final @NotNull ArrayList<GostBlockungsergebnisKurs> list = new ArrayList<>(set);
				if (wahlkonflikte < 10) {
					sb.append(_parent.toStringSchuelerSimple(idSchueler) + " ist in " + _parent.toStringSchieneSimple(e.getKey()) + " in mehreren Kursen:");
					for (int i = 0; i < list.size(); i++)
						sb.append((i == 0 ? "" : ", ") + _parent.toStringKursSimple(list.get(i).id));
					sb.append("\n");
				} else {
					wahlkonflikte_ignored++;
				}
				wahlkonflikte += list.size() - 1;
			}

		return "Wahlkonflikte = " + wahlkonflikte + "\n" + sb.toString()
				+ (wahlkonflikte_ignored == 0 ? "" : "+" + wahlkonflikte_ignored + " weitere Konflikte.");
	}

	private void stateClearErgebnisBewertung3() {
		// Bewertungskriterium 3a (kursdifferenzMax) und 3b (kursdifferenzHistogramm)
		// kursdifferenzMax noch unterteilt in (bewertung3_KD_nur_LK) (bewertung3_KD_nur_GK) (bewertung3_KD_nur_REST)
		_ergebnis.bewertung.kursdifferenzMax = 0;
		_ergebnis.bewertung.kursdifferenzHistogramm = new int[_parent.schuelerGetAnzahl() + 1];
		_bewertung3_KD_nur_LK = 0;
		_bewertung3_KD_nur_GK = 0;
		_bewertung3_KD_nur_REST = 0;

		for (final long idFachart : _fachartID_to_kursdifferenz.keySet()) {
			final int newKD = DeveloperNotificationException.ifMapGetIsNull(_fachartID_to_kursdifferenz, idFachart);
			_ergebnis.bewertung.kursdifferenzHistogramm[newKD]++;
			_ergebnis.bewertung.kursdifferenzMax = Math.max(_ergebnis.bewertung.kursdifferenzMax, newKD);

			final int kursart = GostKursart.getKursartID(idFachart);
			if (kursart == GostKursart.LK.id) {
				_bewertung3_KD_nur_LK = Math.max(_bewertung3_KD_nur_LK, newKD);
			} else {
				if (kursart == GostKursart.GK.id) {
					_bewertung3_KD_nur_GK = Math.max(_bewertung3_KD_nur_GK, newKD);
				} else {
					_bewertung3_KD_nur_REST = Math.max(_bewertung3_KD_nur_REST, newKD);
				}
			}
		}

		_regelverletzungen_tooltip3_kursdifferenzen = stateClearErgebnisTooltip3();
	}

	private @NotNull String stateClearErgebnisTooltip3() {
		final @NotNull StringBuilder sb = new StringBuilder();
		final @NotNull int[] histo = _ergebnis.bewertung.kursdifferenzHistogramm;
		sb.append("Maximale Kursdifferenz (LK, GK, REST): " + _bewertung3_KD_nur_LK + ", " + _bewertung3_KD_nur_GK + ", " + _bewertung3_KD_nur_REST + "\n");

		if (histo.length >= 2)
			sb.append("Optimal 0/1: " + (histo[0] + histo[1]) + "x\n");

		for (int i = 2; i < histo.length; i++) {
			if (histo[i] <= 0)
				continue;

			final @NotNull List<String> listFacharten = DeveloperNotificationException.ifMapGetIsNull(_kursdifferenz_to_fachartenList, i);
			sb.append("Differenz " + i + ": " + histo[i] + "x (" + listFacharten.get(0));
			for (int j = 1; j < listFacharten.size(); j++)
				sb.append(", " + listFacharten.get(j));
			sb.append(")\n");
		}

		return sb.toString();
	}

	private void stateClearErgebnisBewertung4() {
		// Bewertungskriterium 4
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene = 0;
		for (final long idSchiene : _schienenIDset)
			for (final long idFachart : _fachartID_to_kurseList.keySet()) {
				final int gleicheKurseInSchiene = Map2DUtils.getOrCreateArrayList(_schienenID_fachartID_to_kurseList, idSchiene, idFachart).size();
				if (gleicheKurseInSchiene >= 2)
					_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += gleicheKurseInSchiene - 1;
			}

		_regelverletzungen_tooltip4_faecherparallelitaet = stateClearErgebnisTooltip4();
	}

	private @NotNull String stateClearErgebnisTooltip4() {
		final @NotNull StringBuilder sb = new StringBuilder();

		for (int nr = 1; nr <= _schienenNR_to_schiene.size(); nr++) {
			final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
			final @NotNull String proSchiene = stateClearErgebnisTooltip4proSchiene(schiene.id);
			if (!proSchiene.isEmpty())
				sb.append("Schiene " + nr + ":\n" + proSchiene);
		}

		return sb.toString();
	}

	private @NotNull String stateClearErgebnisTooltip4proSchiene(final long idSchiene) {
		final @NotNull StringBuilder sb = new StringBuilder();

		for (final long idFachart : _fachartIDList_sortiert) {
			final @NotNull String proFachart = stateClearErgebnisTooltip4proSchieneUndFachart(idSchiene, idFachart);
			if (!proFachart.isEmpty())
				sb.append(proFachart + "\n");
		}

		return sb.toString();
	}

	private @NotNull String stateClearErgebnisTooltip4proSchieneUndFachart(final long idSchiene, final long idFachart) {
		final @NotNull StringBuilder sb = new StringBuilder();

		if (_schienenID_fachartID_to_kurseList.contains(idSchiene, idFachart)) {
			final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursGruppe = _schienenID_fachartID_to_kurseList.getOrException(idSchiene, idFachart);
			final int n = kursGruppe.size();
			if (n >= 2) {
				sb.append("  " + getOfFachartName(idFachart) + " (+" + (n - 1) + "):");
				for (int i = 0; i < n; i++) {
					final @NotNull GostBlockungsergebnisKurs kurs = ListUtils.getNonNullElementAtOrException(kursGruppe, i);
					sb.append((i == 0 ? "" : ",") + " " + getOfKursName(kurs.id));
				}
			}
		}
		return sb.toString();
	}

	private void update_0_schienenIDset_schienenNRset() {
		_schienenIDset = new HashSet<>();
		_schienenNRset = new HashSet<>();
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			if (gSchiene.id < 0)
				_fehlermeldungen.add("Die Schienen-ID " + gSchiene.id + " ist ungültig!");
			if (!_schienenIDset.add(gSchiene.id))
				_fehlermeldungen.add("Die Schienen-ID " + gSchiene.id + " ist doppelt!");
			if (gSchiene.nummer <= 0)
				_fehlermeldungen.add("Die Schienen-NR " + gSchiene.nummer + " ist ungültig!");
			if (!_schienenNRset.add(gSchiene.nummer))
				_fehlermeldungen.add("Die Schienen-NR " + gSchiene.nummer + " ist doppelt!");
		}
		for (int schienenNr = 1; schienenNr <= _schienenNRset.size(); schienenNr++)
			if (!_schienenNRset.contains(schienenNr))
				_fehlermeldungen.add("Es gibt " + _schienenNRset.size() + " Schienen, aber es fehlt die Schienen-Nr. " + schienenNr + "!");
	}

	private void update_0_kursIDset() {
		_kursIDset = new HashSet<>();
		for (final @NotNull GostBlockungKurs gKurs : _parent.daten().kurse) {
			if (gKurs.id < 0)
				_fehlermeldungen.add("Die Kurs-ID " + gKurs.id + " ist ungültig!");
			if (!_kursIDset.add(gKurs.id))
				_fehlermeldungen.add("Die Kurs-ID " + gKurs.id + " ist doppelt!");
		}
	}

	private void update_0_fachIDset() {
		_fachIDset = new HashSet<>();
		for (final @NotNull GostFach gFach : _parent.faecherManager().faecher()) {
			if (gFach.id < 0)
				_fehlermeldungen.add("Die Fach-ID " + gFach.id + " ist ungültig!");
			if (!_fachIDset.add(gFach.id))
				_fehlermeldungen.add("Die Fach-ID " + gFach.id + " ist doppelt!");
		}
		// Gibt es Kurse ohne definiertes Fach?
		for (final @NotNull GostBlockungKurs gKurs : _parent.daten().kurse)
			if (_fachIDset.add(gKurs.fach_id))
				_fehlermeldungen.add("Kurs " + _parent.toStringKursSimple(gKurs.id) + " hat ein undefiniertes Fach (im Fächer-Manager)!");
		// Gibt es Fachwahlen  ohne definiertes Fach?
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen)
			if (_fachIDset.add(gFachwahl.fachID))
				_fehlermeldungen.add("Fachwahl " + _parent.toStringFachwahlSimple(gFachwahl) + " hat ein undefiniertes Fach (im Fächer-Manager)!");
	}

	private void update_0_schuelerIDset() {
		_schuelerIDset = new HashSet<>();
		for (final @NotNull Schueler schueler : _parent.daten().schueler) {
			if (schueler.id < 0)
				_fehlermeldungen.add("Die Schüler-ID " + schueler.id + " ist ungültig!");
			if (!_schuelerIDset.add(schueler.id))
				_fehlermeldungen.add("Die Schüler-ID " + schueler.id + " ist doppelt!");
		}
	}


	/**
	 * Wichtig: Die Methode muss auf gelöschte und hinzugefügt Schienen reagieren
	 * und die eigene Datenstruktur anpassen.
	 */
	private void update_0_schienenID_to_schiene_schienenNR_to_schiene() {
		_schienenID_to_schiene = new HashMap<>();
		_schienenNR_to_schiene = new HashMap<>();

		// Lösche alle E-Schienen, die es im Elternteil nicht mehr gibt.
		final List<GostBlockungsergebnisSchiene> listZuLoeschen = new ArrayList<>();
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen)
			if (!_parent.schieneGetExistiert(eSchiene.id)) {
				listZuLoeschen.add(eSchiene);
				if (!eSchiene.kurse.isEmpty())
					_fehlermeldungen.add("Schiene ID=" + eSchiene.id + " wird gelöscht, obwohl "
							+ eSchiene.kurse.size() + " Kurse in der Schiene enthalten sind!");
			}
		_ergebnis.schienen.removeAll(listZuLoeschen);

		// Erzeuge für jede E-Schiene ein Mapping
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen) {
			_schienenID_to_schiene.put(eSchiene.id, eSchiene);
			final int nr = _parent.schieneGet(eSchiene.id).nummer;
			_schienenNR_to_schiene.put(nr, eSchiene);
		}

		// Erzeuge fehlende E-Schienen, die nur das Elternteil derzeit hat.
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen)
			if (!_schienenID_to_schiene.containsKey(gSchiene.id)) {
				final @NotNull GostBlockungsergebnisSchiene eSchiene = DTOUtils.newGostBlockungsergebnisSchiene(gSchiene.id);
				_schienenID_to_schiene.put(gSchiene.id, eSchiene);
				_schienenNR_to_schiene.put(gSchiene.nummer, eSchiene);
				_ergebnis.schienen.add(eSchiene);
			}
	}

	private void update_0_kursID_to_kurs() {
		_kursID_to_kurs = new HashMap<>();
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen)
			for (final @NotNull GostBlockungsergebnisKurs eKurs : eSchiene.kurse)
				_kursID_to_kurs.put(eKurs.id, eKurs);

		for (final @NotNull GostBlockungKurs gKurs : _parent.daten().kurse) {
			if (!_kursID_to_kurs.containsKey(gKurs.id)) {
				final @NotNull GostBlockungsergebnisKurs eKurs =
						DTOUtils.newGostBlockungsergebnisKurs(gKurs.id, gKurs.fach_id, gKurs.kursart, gKurs.anzahlSchienen);
				_kursID_to_kurs.put(gKurs.id, eKurs);
			}
		}
	}

	private void update_0_schuelerID_to_schueler() {
		_schuelerID_to_schueler = new HashMap<>();
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler)
			_schuelerID_to_schueler.put(gSchueler.id, gSchueler);
	}

	private void update_0_schienenID_to_kursIDSet() {
		_schienenID_to_kursIDSet = new HashMap<>();
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen)
			for (final @NotNull GostBlockungsergebnisKurs eKurs : eSchiene.kurse)
				MapUtils.getOrCreateHashSet(_schienenID_to_kursIDSet, eSchiene.id).add(eKurs.id);

		// Schienen ohne Kurse ergänzen.
		for (final long idSchiene : _schienenIDset)
			if (!_schienenID_to_kursIDSet.containsKey(idSchiene))
				MapUtils.getOrCreateHashSet(_schienenID_to_kursIDSet, idSchiene);
	}

	private void update_1_kursID_to_schuelerIDSet_schuelerID_to_ungueltigeKurseSet() {
		// Leeren und hinzufügen.
		_kursID_to_schuelerIDSet = new HashMap<>();
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen)
			for (final @NotNull GostBlockungsergebnisKurs eKurs : eSchiene.kurse)
				MapUtils.getOrCreateHashSet(_kursID_to_schuelerIDSet, eKurs.id).addAll(eKurs.schueler);

		// Kurse ohne Schüler ergänzen.
		for (final long idKurs : _kursIDset)
			if (!_kursID_to_schuelerIDSet.containsKey(idKurs))
				MapUtils.getOrCreateHashSet(_kursID_to_schuelerIDSet, idKurs);

		// Ungültige Fachwahlen in eine andere Map kopieren (Änderung: Früher wurden diese verschoben)
		_schuelerID_to_ungueltigeKurseSet = new HashMap<>();
		for (final long idKurs : _kursID_to_schuelerIDSet.keySet()) {
			final @NotNull GostBlockungsergebnisKurs eKurs = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_kurs, idKurs);
			final @NotNull Set<Long> schuelerIDset = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schuelerIDSet, idKurs);

			for (final long idSchueler : new HashSet<>(schuelerIDset))
				if (!_parent.schuelerGetHatFachart(idSchueler, eKurs.fachID, eKurs.kursart))
					MapUtils.getOrCreateHashSet(_schuelerID_to_ungueltigeKurseSet, idSchueler).add(eKurs);
		}

	}

	private void update_1_kursID_to_dummySuS() {
		// Leeren und hinzufügen.
		_kursID_to_dummySuS = new HashMap<>();
		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			final long idKurs = r.parameter.get(0);
			final int anzahl = r.parameter.get(1).intValue();
			if (!_kursIDset.contains(idKurs)) {
				_fehlermeldungen.add("Kurs " + _parent.toStringKursSimple(idKurs) + " soll " + anzahl + " externe SuS haben, aber den Kurs gibt es nicht!");
				continue;
			}
			if ((anzahl < 1) || (anzahl > 99)) {
				_fehlermeldungen.add("Kurs " + _parent.toStringKursSimple(idKurs) + " mit " + anzahl + " externen SuS ist ungültig!");
				continue;
			}
			if (_kursID_to_dummySuS.containsKey(idKurs)) {
				_fehlermeldungen.add("Kurs " + _parent.toStringKursSimple(idKurs) + " mit " + anzahl + " externen SuS. Doppelte Regel gefunden!");
				continue;
			}
			_kursID_to_dummySuS.put(idKurs, anzahl);
		}
		// Kurse ohne Dummy-SuS ergänzen.
		for (final long idKurs : _kursIDset)
			MapUtils.putNonNullIfNotExists(_kursID_to_dummySuS, idKurs, 0);
	}

	private void update_1_fachID_to_kurseList() {
		// Leeren und hinzufügen.
		_fachID_to_kurseList = new HashMap<>();
		for (final @NotNull GostBlockungsergebnisKurs eKurs : _kursID_to_kurs.values())
			MapUtils.getOrCreateArrayList(_fachID_to_kurseList, eKurs.fachID).add(eKurs);

		// Fächer ohne Kurse (ggf. von Fachwahlen)
		for (final long idFach : _fachIDset)
			MapUtils.getOrCreateArrayList(_fachID_to_kurseList, idFach);
	}

	private void update_1_kursID_to_schienenSet() {
		// Leeren und hinzufügen.
		_kursID_to_schienenSet = new HashMap<>();
		for (final long idSchiene : _schienenID_to_kursIDSet.keySet()) {
			final @NotNull GostBlockungsergebnisSchiene eSchiene = DeveloperNotificationException.ifMapGetIsNull(_schienenID_to_schiene, idSchiene);
			for (final long idKurs : MapUtils.getOrCreateHashSet(_schienenID_to_kursIDSet, idSchiene))
				MapUtils.getOrCreateHashSet(_kursID_to_schienenSet, idKurs).add(eSchiene);
		}

		// Kurse ohne Schienen?
		for (final long idKurs : _kursIDset)
			if (!_kursID_to_schienenSet.containsKey(idKurs))
				MapUtils.getOrCreateHashSet(_kursID_to_schienenSet, idKurs);
	}

	private void update_1_fachartID_to_kurseList() {
		// Leeren und hinzufügen (ermittelt über alle Kurse).
		_fachartID_to_kurseList = new HashMap<>();
		for (final @NotNull GostBlockungsergebnisKurs eKurs : _kursID_to_kurs.values()) {
			final long fachartID = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			MapUtils.getOrCreateArrayList(_fachartID_to_kurseList, fachartID).add(eKurs);
		}

		// Ergänze mit Fachwahlen zu denen es keinen Kurs gibt.
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen)
			MapUtils.getOrCreateArrayList(_fachartID_to_kurseList, GostKursart.getFachartIDByFachwahl(gFachwahl));

		// Zugeordnete Listen sortieren.
		for (final long idFachart : _fachartID_to_kurseList.keySet()) {
			final @NotNull List<GostBlockungsergebnisKurs> kursmenge =
					DeveloperNotificationException.ifMapGetIsNull(_fachartID_to_kurseList, idFachart);
			if (_fachartmenge_sortierung == 1) {
				kursmenge.sort(_kursComparator_kursart_fach_kursnummer);
			} else {
				kursmenge.sort(_kursComparator_fach_kursart_kursnummer);
			}
		}
	}

	private void update_2_schuelerID_to_kurseSet() {
		// Leeren und hinzufügen.
		_schuelerID_to_kurseSet = new HashMap<>();
		for (final long idKurs : _kursID_to_schuelerIDSet.keySet()) {
			final @NotNull GostBlockungsergebnisKurs eKurs = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_kurs, idKurs);
			for (final long idSchueler : DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schuelerIDSet, idKurs))
				MapUtils.getOrCreateHashSet(_schuelerID_to_kurseSet, idSchueler).add(eKurs);
		}

		// Schüler ohne Kurse?
		for (final long idSchueler : _schuelerIDset)
			if (!_schuelerID_to_kurseSet.containsKey(idSchueler))
				MapUtils.getOrCreateHashSet(_schuelerID_to_kurseSet, idSchueler);
	}

	private void update_2_fachartIDList_sortiert() {
		// Leeren und hinzufügen.
		_fachartIDList_sortiert = new ArrayList<>(_fachartID_to_kurseList.keySet());

		// Liste sortieren.
		if (_fachartmenge_sortierung == 1) {
			_fachartIDList_sortiert.sort(_fachartComparator_kursart_fach);
		} else {
			_fachartIDList_sortiert.sort(_fachartComparator_fach_kursart);
		}

	}

	private void update_2_fachartID_to_kursdifferenz() {
		// Leeren und hinzufügen.
		_fachartID_to_kursdifferenz = new HashMap<>();

		for (final long idFachart : _fachartID_to_kurseList.keySet()) {
			final List<GostBlockungsergebnisKurs> kursmenge = DeveloperNotificationException.ifMapGetIsNull(_fachartID_to_kurseList, idFachart);

			// Neue Kursdifferenz berechnen
			int min = 10000; // Dummy-Wert
			int max = 0;
			for (final @NotNull GostBlockungsergebnisKurs kurs : kursmenge) {
				// Wichtig: Kurse die zu ignorieren sind, müssen beachtet werden!
				final @NotNull LongArrayKey keyIgnoreID =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, kurs.id });
				if (_parent.regelGetByLongArrayKeyOrNull(keyIgnoreID) != null)
					continue;

				// Wichtig: DummySuS müssen beachtet werden!
				final int size = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schuelerIDSet, kurs.id).size()
						+ DeveloperNotificationException.ifMapGetIsNull(_kursID_to_dummySuS, kurs.id);
				min = Math.min(min, size);
				max = Math.max(max, size);
			}
			int newKD = max - min;

			// Sonderfall: Falls alle Kurse ignoriert wurden, oder es gar keine Kurse gab.
			if (newKD < 0)
				newKD = 0;

			// Kursdifferenz-Map aktualisieren.
			_fachartID_to_kursdifferenz.put(idFachart, newKD);
		}

	}

	private void update_2_schienenID_to_kollisionen() {
		_schienenID_to_kollisionen = new HashMap<>();

		for (final long idSchiene : _schienenID_to_kursIDSet.keySet()) { // Durchläuft alle Schienen.
			final @NotNull Set<Long> kursmenge = DeveloperNotificationException.ifMapGetIsNull(_schienenID_to_kursIDSet, idSchiene);

			int summeMitDoppelten = 0;
			final @NotNull Set<Long> summeOhneDoppelte = new HashSet<>();

			for (final long idKurs : kursmenge) {
				final @NotNull Set<Long> schuelermenge = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schuelerIDSet, idKurs);
				summeMitDoppelten += schuelermenge.size();
				summeOhneDoppelte.addAll(schuelermenge);
			}

			_schienenID_to_kollisionen.put(idSchiene, summeMitDoppelten - summeOhneDoppelte.size());
		}
	}

	private void update_2_schienenID_to_susAnzahl() {
		// Leeren und hinzufügen.
		_schienenID_to_susAnzahl = new HashMap<>();

		for (final long idSchiene : _schienenID_to_kursIDSet.keySet()) { // Durchläuft alle Schienen.
			final @NotNull Set<Long> kursmenge = DeveloperNotificationException.ifMapGetIsNull(_schienenID_to_kursIDSet, idSchiene);

			int summeMitDoppelten = 0;

			for (final long idKurs : kursmenge) {
				final @NotNull Set<Long> schuelermenge = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schuelerIDSet, idKurs);
				summeMitDoppelten += schuelermenge.size();
			}

			_schienenID_to_susAnzahl.put(idSchiene, summeMitDoppelten);
		}
	}

	private void update_2_schuelerID_schienenID_to_kurseSet() {
		// Leeren und hinzufügen.
		_schuelerID_schienenID_to_kurseSet = new HashMap2D<>();

		for (final long idSchiene : _schienenID_to_kursIDSet.keySet()) { // Durchläuft alle Schienen.
			final @NotNull Set<Long> kursmenge = DeveloperNotificationException.ifMapGetIsNull(_schienenID_to_kursIDSet, idSchiene);
			for (final long idKurs : kursmenge) {
				final @NotNull GostBlockungsergebnisKurs eKurs = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_kurs, idKurs);

				final @NotNull Set<Long> schuelermenge = DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schuelerIDSet, idKurs);
				for (final long idSchueler : schuelermenge) {
					Map2DUtils.getOrCreateHashSet(_schuelerID_schienenID_to_kurseSet, idSchueler, idSchiene).add(eKurs);
				}
			}

			// Ergänze fehlende Schüler-Schienen-Kombinationen.
			for (final long idSchueler : _schuelerIDset)
				Map2DUtils.getOrCreateHashSet(_schuelerID_schienenID_to_kurseSet, idSchueler, idSchiene);
		}
	}

	private void update_2_schienenID_fachartID_to_kurseList() {
		// Leeren und hinzufügen.
		_schienenID_fachartID_to_kurseList = new HashMap2D<>();
		for (final @NotNull GostBlockungsergebnisKurs eKurs : _kursID_to_kurs.values()) {
			final long fachartID = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			for (final @NotNull GostBlockungsergebnisSchiene eSchiene : MapUtils.getOrCreateHashSet(_kursID_to_schienenSet, eKurs.id))
				Map2DUtils.getOrCreateArrayList(_schienenID_fachartID_to_kurseList, eSchiene.id, fachartID).add(eKurs);
		}

		// Ergänze leere (Schienen, Fachart) Kombinationen
		for (final long idSchiene : _schienenIDset)
			for (final long idFachart : _fachartID_to_kurseList.keySet())
				Map2DUtils.getOrCreateArrayList(_schienenID_fachartID_to_kurseList, idSchiene, idFachart);
	}

	private void update_3_kursdifferenz_to_fachartenList() {
		_kursdifferenz_to_fachartenList = new HashMap<>();

		for (final long idFachart : _fachartID_to_kursdifferenz.keySet()) {
			final int kursdifferenz = DeveloperNotificationException.ifMapGetIsNull(_fachartID_to_kursdifferenz, idFachart);
			final @NotNull String sFachart = _parent.toStringFachartSimpleByFachartID(idFachart);
			MapUtils.getOrCreateArrayList(_kursdifferenz_to_fachartenList, kursdifferenz).add(sFachart);
		}
	}

	private void update_3_schuelerID_to_kollisionen() {
		// Leeren und hinzufügen.
		_schuelerID_to_kollisionen = new HashMap<>();

		for (final long idSchueler : _schuelerID_schienenID_to_kurseSet.getKeySet()) {
			int summeAllerKollisionenDesSchuelers = 0;

			for (final long idSchiene : _schuelerID_schienenID_to_kurseSet.getKeySetOf(idSchueler)) {
				final int kurseInDerSchiene = _schuelerID_schienenID_to_kurseSet.getOrException(idSchueler, idSchiene).size();
				if (kurseInDerSchiene >= 2)
					summeAllerKollisionenDesSchuelers += kurseInDerSchiene - 1;
			}

			_schuelerID_to_kollisionen.put(idSchueler, summeAllerKollisionenDesSchuelers);
		}
	}

	private void update_3_schuelerID_fachID_to_kurs_or_null() {
		// Leeren und hinzufügen.
		_schuelerID_fachID_to_kurs_or_null = new HashMap2D<>();
		for (final long idSchueler : _schuelerID_to_kurseSet.keySet())
			for (final @NotNull GostBlockungsergebnisKurs eKurs : DeveloperNotificationException.ifMapGetIsNull(_schuelerID_to_kurseSet, idSchueler))
				_schuelerID_fachID_to_kurs_or_null.put(idSchueler, eKurs.fachID, eKurs);

		// Ergänze Fächer des Schülers, die keinen Zuordnung haben.
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen)
			if (!_schuelerID_fachID_to_kurs_or_null.contains(gFachwahl.schuelerID, gFachwahl.fachID))
				_schuelerID_fachID_to_kurs_or_null.put(gFachwahl.schuelerID, gFachwahl.fachID, null);
	}

	private void stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(final @NotNull GostBlockungRegel r) {
		for (int schienenNr = r.parameter.get(1).intValue(); schienenNr <= r.parameter.get(2).intValue(); schienenNr++)
			for (final GostBlockungsergebnisKurs eKurs : getSchieneEmitNr(schienenNr).kurse)
				if (eKurs.kursart == r.parameter.get(0).intValue()) {
					_ergebnis.bewertung.regelVerletzungen.add(r.id);
					final @NotNull String beschreibung = "Kursart " + getOfKursName(eKurs.id) + " sollte nicht auf Schiene " + schienenNr + " liegen.";
					MapUtils.addToList(_regelTyp_to_verletzungList, 1, beschreibung);
					_regelID_to_verletzungString.put(r.id, beschreibung);
				}
	}

	private void stateRegelvalidierung2_kurs_fixieren_in_schiene(final @NotNull GostBlockungRegel r) {
		final long idKurs = r.parameter.get(0);
		final int schienenNr = r.parameter.get(1).intValue();
		if (!getOfKursSchienenmenge(idKurs).contains(getSchieneEmitNr(schienenNr))) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = "Kurs " + getOfKursName(idKurs) + " sollte fixiert sein in Schiene " + schienenNr + ".";
			MapUtils.addToList(_regelTyp_to_verletzungList, 2, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private void stateRegelvalidierung3_kurs_sperren_in_schiene(final @NotNull GostBlockungRegel r) {
		final long idKurs = r.parameter.get(0);
		final int schienenNr = r.parameter.get(1).intValue();
		if (getOfKursSchienenmenge(idKurs).contains(getSchieneEmitNr(schienenNr))) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = "Kurs " + getOfKursName(idKurs) + " sollte gesperrt sein in Schiene " + schienenNr + ".";
			MapUtils.addToList(_regelTyp_to_verletzungList, 3, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private void stateRegelvalidierung4_schueler_fixieren_in_kurs(final @NotNull GostBlockungRegel r) {
		final long idSchueler = r.parameter.get(0);
		final long idKurs = r.parameter.get(1);
		if (!getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler) + " sollte fixiert sein in Kurs " + getOfKursName(idKurs) + ".";
			MapUtils.addToList(_regelTyp_to_verletzungList, 4, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private void stateRegelvalidierung5_schueler_verbieten_in_kurs(final @NotNull GostBlockungRegel r) {
		final long idSchueler = r.parameter.get(0);
		final long idKurs = r.parameter.get(1);
		if (getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler) + " sollte verboten sein in Kurs " + getOfKursName(idKurs) + ".";
			MapUtils.addToList(_regelTyp_to_verletzungList, 5, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private void stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(final @NotNull GostBlockungRegel r) {
		for (final GostBlockungsergebnisKurs eKurs : _kursID_to_kurs.values())
			for (final @NotNull Long eSchieneID : eKurs.schienen) {
				final int nr = getSchieneG(eSchieneID).nummer;
				final int kursart = r.parameter.get(0).intValue();
				final int schienenNrVon = r.parameter.get(1).intValue();
				final int schienenNrBis = r.parameter.get(2).intValue();

				final boolean b1 = eKurs.kursart == kursart;
				final boolean b2 = (schienenNrVon <= nr) && (nr <= schienenNrBis);
				if (b1 != b2) {
					_ergebnis.bewertung.regelVerletzungen.add(r.id);
					final @NotNull String beschreibung =
							"Kursart von " + getOfKursName(eKurs.id) + " sollte innerhalb der Schienen " + schienenNrVon + " bis " + schienenNrBis + " sein.";
					MapUtils.addToList(_regelTyp_to_verletzungList, 6, beschreibung);
					_regelID_to_verletzungString.put(r.id, beschreibung);
				}
			}
	}

	private void stateRegelvalidierung7_kurs_verbieten_mit_kurs(final @NotNull GostBlockungRegel r) {
		final long idKurs1 = r.parameter.get(0);
		final long idKurs2 = r.parameter.get(1);
		for (final @NotNull GostBlockungsergebnisSchiene schiene1 : getOfKursSchienenmenge(idKurs1))
			for (final @NotNull GostBlockungsergebnisSchiene schiene2 : getOfKursSchienenmenge(idKurs2))
				if (schiene1 == schiene2) {
					_ergebnis.bewertung.regelVerletzungen.add(r.id);
					final int nr = getSchieneG(schiene1.id).nummer;
					final @NotNull String beschreibung = "Kurs " + getOfKursName(idKurs1) + " und Kurs " + getOfKursName(idKurs2)
							+ " sollten nicht gemeinsam in einer Schiene (" + nr + ") sein.";
					MapUtils.addToList(_regelTyp_to_verletzungList, 7, beschreibung);
					_regelID_to_verletzungString.put(r.id, beschreibung);
				}
	}

	private void stateRegelvalidierung8_kurs_zusammen_mit_kurs(final @NotNull GostBlockungRegel r) {
		final long idKurs1 = r.parameter.get(0);
		final long idKurs2 = r.parameter.get(1);
		final @NotNull Set<GostBlockungsergebnisSchiene> set1 = getOfKursSchienenmenge(idKurs1);
		final @NotNull Set<GostBlockungsergebnisSchiene> set2 = getOfKursSchienenmenge(idKurs2);
		if (set1.size() < set2.size()) {
			// "set1" muss in "set2" enthalten sein (da es dass kleinere ist).
			for (final @NotNull GostBlockungsergebnisSchiene schiene1 : set1)
				if (!set2.contains(schiene1)) {
					_ergebnis.bewertung.regelVerletzungen.add(r.id);
					final @NotNull String beschreibung =
							"Kurs " + getOfKursName(idKurs1) + " und Kurs " + getOfKursName(idKurs2) + " sollten gemeinsam in einer Schiene sein.";
					MapUtils.addToList(_regelTyp_to_verletzungList, 8, beschreibung);
					_regelID_to_verletzungString.put(r.id, beschreibung);
				}
		} else {
			// "set2" muss in "set1" enthalten sein, da es dass kleinere ist.
			for (final @NotNull GostBlockungsergebnisSchiene schiene2 : set2)
				if (!set1.contains(schiene2)) {
					_ergebnis.bewertung.regelVerletzungen.add(r.id);
					final @NotNull String beschreibung =
							"Kurs " + getOfKursName(idKurs1) + " und Kurs " + getOfKursName(idKurs2) + " sollten gemeinsam in einer Schiene sein.";
					MapUtils.addToList(_regelTyp_to_verletzungList, 8, beschreibung);
					_regelID_to_verletzungString.put(r.id, beschreibung);
				}
		}
	}

	private void stateRegelvalidierung10_lehrkraefte_beachten(final @NotNull GostBlockungRegel r) {
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _schienenID_to_schiene.values())
			for (final @NotNull GostBlockungsergebnisKurs eKurs1 : eSchiene.kurse)
				for (final @NotNull GostBlockungsergebnisKurs eKurs2 : eSchiene.kurse)
					if (eKurs1.id < eKurs2.id)
						for (final @NotNull GostBlockungKursLehrer gLehr1 : getKursG(eKurs1.id).lehrer)
							for (final @NotNull GostBlockungKursLehrer gLehr2 : getKursG(eKurs2.id).lehrer)
								if (gLehr1.id == gLehr2.id) {
									_ergebnis.bewertung.regelVerletzungen.add(r.id);
									final int nr = getSchieneG(eSchiene.id).nummer;
									final @NotNull String beschreibung = "Kurs " + getOfKursName(eKurs1.id) + " und Kurs " + getOfKursName(eKurs2.id)
											+ " haben die Lehrkraft " + gLehr1.kuerzel + " in der selben Schiene (" + nr + ").";
									MapUtils.addToList(_regelTyp_to_verletzungList, 10, beschreibung);
									_regelID_to_verletzungString.put(r.id, beschreibung);
								}
	}

	private void stateRegelvalidierung11_schueler_zusammen_mit_schueler_in_fach(final @NotNull GostBlockungRegel r) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);
		final long idFach = r.parameter.get(2);
		final @NotNull GostFach fach = getFach(idFach);

		if (!_parent.schuelerGetHatFach(idSchueler1, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung =
					getOfSchuelerNameVorname(idSchueler1) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(_regelTyp_to_verletzungList, 11, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}

		if (!_parent.schuelerGetHatFach(idSchueler2, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung =
					getOfSchuelerNameVorname(idSchueler2) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(_regelTyp_to_verletzungList, 11, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}

		if (!_parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2)
					+ " haben nicht die selbe Kursart bei " + fach.kuerzelAnzeige + ".";
			MapUtils.addToList(_regelTyp_to_verletzungList, 11, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}

		if (!getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2)
					+ " sollten gemeinsam in " + fach.kuerzelAnzeige + " sein.";
			MapUtils.addToList(_regelTyp_to_verletzungList, 11, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
		}

	}

	private void stateRegelvalidierung12_schueler_verbieten_mit_schueler_in_fach(final @NotNull GostBlockungRegel r) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);
		final long idFach = r.parameter.get(2);
		final @NotNull GostFach fach = getFach(idFach);

		if (!_parent.schuelerGetHatFach(idSchueler1, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung =
					getOfSchuelerNameVorname(idSchueler1) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", hat aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(_regelTyp_to_verletzungList, 12, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}

		if (!_parent.schuelerGetHatFach(idSchueler2, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung =
					getOfSchuelerNameVorname(idSchueler2) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", hat aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(_regelTyp_to_verletzungList, 12, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}

		if (!_parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler1) + " und SchülerIn " + getOfSchuelerNameVorname(idSchueler2)
					+ " haben nicht die selbe Kursart bei " + fach.kuerzelAnzeige + ".";
			MapUtils.addToList(_regelTyp_to_verletzungList, 12, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}

		if (getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler1) + " und SchülerIn " + getOfSchuelerNameVorname(idSchueler2)
					+ " sollten nicht gemeinsam in " + fach.kuerzelAnzeige + " sein.";
			MapUtils.addToList(_regelTyp_to_verletzungList, 12, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
		}

	}

	private void stateRegelvalidierung13_schueler_zusammen_mit_schueler(final @NotNull GostBlockungRegel r) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);

		for (final @NotNull GostFach fach : _parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2))
			if (!getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				_ergebnis.bewertung.regelVerletzungen.add(r.id);
				final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2)
						+ " sollten gemeinsam in " + fach.kuerzelAnzeige + " sein.";
				MapUtils.addToList(_regelTyp_to_verletzungList, 13, beschreibung);
				_regelID_to_verletzungString.put(r.id, beschreibung);
			}
	}

	private void stateRegelvalidierung14_schueler_verbieten_mit_schueler(final @NotNull GostBlockungRegel r) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);

		for (final @NotNull GostFach fach : _parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2))
			if (getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				_ergebnis.bewertung.regelVerletzungen.add(r.id);
				final @NotNull String beschreibung = getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2)
						+ " sollten nicht gemeinsam in " + fach.kuerzelAnzeige + " sein.";
				MapUtils.addToList(_regelTyp_to_verletzungList, 14, beschreibung);
				_regelID_to_verletzungString.put(r.id, beschreibung);
			}
	}

	private void stateRegelvalidierung15_kurs_maximale_schueleranzahl(final @NotNull GostBlockungRegel r) {
		final long idKurs = r.parameter.get(0);
		final int maxSuS = r.parameter.get(1).intValue();
		DeveloperNotificationException.ifTrue("Regel 15: " + _parent.toStringKurs(idKurs) + " maximale SuS-Anzahl = " + maxSuS + " ist ungültig!",
				(maxSuS < 0) || (maxSuS > 100));
		final int sus = getOfKursAnzahlSchuelerPlusDummy(idKurs);

		if (sus > maxSuS) {
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = "Kurs " + getOfKursName(idKurs) + " hat " + sus + " SuS, sollte aber nicht mehr als " + maxSuS + " haben.";
			MapUtils.addToList(_regelTyp_to_verletzungList, 15, beschreibung);
			_regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private void stateRegelvalidierung18_fach_kursart_maxProSchiene(final @NotNull GostBlockungRegel r) {
		final long idFach = r.parameter.get(0);
		final int kursart = r.parameter.get(1).intValue();
		final int maxProSchiene = r.parameter.get(2).intValue();
		final long idFachart = GostKursart.getFachartID(idFach, kursart);

		for (final long idSchiene : _schienenIDset) {
			final int size = Map2DUtils.getOrCreateArrayList(_schienenID_fachartID_to_kurseList, idSchiene, idFachart).size();
			if (size <= maxProSchiene)
				continue;
			_ergebnis.bewertung.regelVerletzungen.add(r.id);
			final @NotNull String beschreibung = "In " + _parent.toStringSchieneSimple(idSchiene) + " ist die Fachart "
					+ _parent.toStringFachartSimpleByFachartID(idFachart) + " insgesamt " + size
					+ " Mal vertreten, erlaubt sind aber nur " + maxProSchiene + "!";
			MapUtils.addToList(_regelTyp_to_verletzungList, 18, beschreibung);
			final @NotNull String old = MapUtils.getOrDefault(_regelID_to_verletzungString, r.id, "");
			_regelID_to_verletzungString.put(r.id, (old.isEmpty() ? "" : "\n") + beschreibung);
		}
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu und revalidiert nicht den Zustand. <br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, wird der Schüler nicht hinzugefügt.
	 *          Stattdessen wird die ungültige Wahl in einer Map gespeichert.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursHinzufuegenOhneRevalidierung(final long idSchueler, final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs eKurs = getKursE(idKurs);
		eKurs.schueler.add(idSchueler);
	}

	/**
	 * Entfernt den Schüler aus dem Kurs und revalidiert nicht den Zustand. <br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, so wird der Schüler aus der zuvor gespeichert
	 *          Zuordnung aller ungültigen Wahlen gelöscht.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursEntfernenOhneRevalidierung(final long idSchueler, final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs eKurs = getKursE(idKurs);
		eKurs.schueler.remove(idSchueler);
	}

	/**
	 * Fügt den Kurs der Schiene hinzu und revalidiert nicht den Zustand.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private void stateKursSchieneHinzufuegenOhneRegelvalidierung(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(idSchiene);
		kurs.schienen.add(idSchiene);
		schiene.kurse.add(kurs);
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private void stateKursSchieneEntfernenOhneRegelvalidierung(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(idSchiene);
		schiene.kurse.remove(kurs);

		// Muss so entfernt werden, da der transpilierte Code nicht unterscheiden kann, ob Long-Objekt oder Index.
		for (int i = kurs.schienen.size() - 1; i >= 0; i--)
			if (kurs.schienen.get(i) == schiene.id) {
				kurs.schienen.remove(i);
				return;
			}
	}

	// #########################################################################
	// ##########           Allgemeine Anfragen                       ##########
	// #########################################################################

	/**
	 * Liefert die Anzahl an externen SuS.
	 *
	 * @return die Anzahl an externen SuS.
	 */
	public int getAnzahlSchuelerExterne() {
		return ListUtils.getCountFiltered(_parent.daten().schueler, (final @NotNull Schueler schueler) -> getOfSchuelerHatStatusExtern(schueler.id));
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS.
	 *
	 * @return die Anzahl an Dummy-SuS.
	 */
	public int getAnzahlSchuelerDummy() {
		int summe = 0;
		for (final long idKurs : _kursID_to_dummySuS.keySet())
			summe += getOfKursAnzahlSchuelerDummy(idKurs);
		return summe;
	}

	/**
	 * Liefert die Datenbank-ID der Blockungs. Das ist die ID des Elternteils.
	 *
	 * @return die Datenbank-ID der Blockungs. Das ist die ID des Elternteils.
	 */
	public long getBlockungsdatenID() {
		return _ergebnis.blockungID;
	}

	/**
	 * Liefert das Blockungsergebnis inklusive ungültiger Schüler-Kurs-Zuordnungen.
	 *
	 * @return  das Blockungsergebnis inklusive ungültiger Schüler-Kurs-Zuordnungen.
	 */
	public @NotNull GostBlockungsergebnis getErgebnis() {
		return _ergebnis;
	}

	/**
	 * Liefert die Menge (meistens eine) aller Fehlermeldungen.
	 * <br>Falls die Liste nicht leer ist, sollte die GUI den Benutzer warnen, dass die Blockung nicht vollständig geladen wurde!
	 *
	 * @return die Menge (meistens eine) aller Fehlermeldungen.
	 */
	public @NotNull List<String> getFehlermeldungen() {
		return _fehlermeldungen;
	}

	/**
	 * Liefert den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 *
	 * @return den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 */
	public @NotNull GostBlockungsdatenManager getParent() {
		return _parent;
	}

	/**
	 * Liefert eine Güte eines Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 *
	 * @param value   der Wert des Bewertungskriteriums
	 *
	 * @return die Güte des Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	private static double getOfBewertungFarbcodeStatic(final int value) {
		return 1 - (1 / ((0.25 * value) + 1));
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public static int getOfBewertung1WertStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		int summe = 0;
		summe += bewertung.anzahlKurseNichtZugeordnet;
		summe += bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public int getOfBewertung1Wert() {
		return GostBlockungsergebnisManager.getOfBewertung1WertStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param bewertung  die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	private static double getOfBewertung1FarbcodeStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung1WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung1Farbcode() {
		return GostBlockungsergebnisManager.getOfBewertung1FarbcodeStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	private static int getOfBewertung2WertStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		int summe = 0;
		summe += bewertung.anzahlSchuelerNichtZugeordnet;
		summe += bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public int getOfBewertung2Wert() {
		return GostBlockungsergebnisManager.getOfBewertung2WertStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	private static double getOfBewertung2FarbcodeStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung2WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung2Farbcode() {
		return GostBlockungsergebnisManager.getOfBewertung2FarbcodeStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz).
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz).
	 */
	public int getOfBewertung3Wert() {
		return _ergebnis.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz) im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung3Farbcode() {
		final int wert = _ergebnis.bewertung.kursdifferenzMax;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert == 0) ? 0 : (wert - 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart LK.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart LK.
	 */
	public int getOfBewertung3Wert_nur_LK() {
		return _bewertung3_KD_nur_LK;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur LK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur LK) im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung3Farbcode_nur_LK() {
		final int wert = _bewertung3_KD_nur_LK;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert == 0) ? 0 : (wert - 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart GK.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart GK.
	 */
	public int getOfBewertung3Wert_nur_GK() {
		return _bewertung3_KD_nur_GK;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur GK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur GK) im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung3Farbcode_nur_GK() {
		final int wert = _bewertung3_KD_nur_GK;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert == 0) ? 0 : (wert - 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf Kursarten die nicht LK oder GK sind.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf Kursarten die nicht LK oder GK sind.
	 */
	public int getOfBewertung3Wert_nur_REST() {
		return _bewertung3_KD_nur_REST;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, alles außer LK und GK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, alles außer LK und GK) im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung3Farbcode_nur_REST() {
		final int wert = _bewertung3_KD_nur_REST;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert == 0) ? 0 : (wert - 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 * <br>Darin enthalten sind:
	 * <br>- Das Histogramm der ersten 10 Kursdifferenzen (Kursdifferenz 0 bis Kursdifferenz 9).
	 * <br>- Das Histogramm hat eine garantierte Länge von 10.
	 *
	 * @param bewertung  Die Bewertung vom Ergebnis.
	 *
	 * @return den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 */
	private static @NotNull int[] getOfBewertung3HistogrammStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		final @NotNull int[] histo = new int[10];

		for (int i = 0; i < histo.length; i++)
			histo[i] = (bewertung.kursdifferenzHistogramm.length >= histo.length) ? bewertung.kursdifferenzHistogramm[i] : 0;

		return histo;
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 * <br>- Das Histogramm der ersten 10 Kursdifferenzen (Kursdifferenz 0 bis Kursdifferenz 9).
	 * <br>- Das Histogramm hat eine garantierte Länge von 10.
	 *
	 * @return den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 */
	private @NotNull int[] getOfBewertung3Histogramm() {
		return GostBlockungsergebnisManager.getOfBewertung3HistogrammStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	private static int getOfBewertung4WertStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	public int getOfBewertung4Wert() {
		return GostBlockungsergebnisManager.getOfBewertung4WertStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	private static double getOfBewertung4FarbcodeStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung4WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung4Farbcode() {
		return GostBlockungsergebnisManager.getOfBewertung4FarbcodeStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert die Anzahl nicht vollständig verteilter Kurse.<br>
	 * Ein Multikurse der über mehrere Schienen geht und gar nicht zugeteilt wurde, wird mehrfach gezählt.
	 *
	 * @return die Anzahl nicht vollständig verteilter Kurse.
	 */
	public int getOfBewertungAnzahlNichtZugeordneterKurse() {
		return _ergebnis.bewertung.anzahlKurseNichtZugeordnet;
	}

	/**
	 * Liefert die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 *
	 * @return die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 */
	public int getOfBewertungAnzahlNichtzugeordneterFachwahlen() {
		return _ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
	}

	// #########################################################################
	// ##########           Anfragen bezüglich eines Faches.          ##########
	// #########################################################################

	/**
	 * Ermittelt das {@link GostFach} für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.<br>
	 * Wirft eine {@link DeveloperNotificationException} falls die ID unbekannt ist.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return Das {@link GostFach}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostFach getFach(final long idFach) throws DeveloperNotificationException {
		return _parent.faecherManager().getOrException(idFach);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach-ID.<br>
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kurse mit dem angegebenen Fach-ID.
	 * @throws DeveloperNotificationException falls die Fach-ID unbekannt ist.
	 */
	public @NotNull List<GostBlockungsergebnisKurs> getOfFachKursmenge(final long idFach) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_fachID_to_kurseList, idFach);
	}

	// #########################################################################
	// ########## Anfragen bezüglich einer Fachart (=Fach + Kursart). ##########
	// #########################################################################

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört. Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.<br>
	 * <br>Hinweis: Die Kursmenge pro Fachart ist sortiert nach {@link #kursSetSortierungFachKursartNummer()} oder {@link #kursSetSortierungKursartFachNummer()}.
	 * <br>Hinweis: Wirft eine {@link DeveloperNotificationException} falls die Fachart-ID unbekannt ist.
	 *
	 * @param  idFachart  Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursmenge, die zur Fachart gehört.
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public @NotNull List<GostBlockungsergebnisKurs> getOfFachartKursmenge(final long idFachart) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_fachartID_to_kurseList, idFachart);
	}

	/**
	 * Liefert die Kursdifferenz der Fachart und beachtet dabei Dummy-SuS von Kursen.
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * Die Methode beachtet auch Kurse mit Dummy-SuS. <br>
	 * Wirft eine {@link DeveloperNotificationException} falls die Fachart-ID unbekannt ist.
	 *
	 * @param  idFachart  Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursdifferenz der Fachart und beachtet dabei Dummy-SuS von Kursen.
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public int getOfFachartKursdifferenz(final long idFachart) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_fachartID_to_kursdifferenz, idFachart);
	}

	/**
	 * Liefert die Kursdifferenz der Fachart, übergeben als (Fach, Kursart).
	 * Die Methode beachtet auch Kurse mit Dummy-SuS. <br>
	 * Wirft eine {@link DeveloperNotificationException} falls die Fachart-ID unbekannt ist.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Kursdifferenz der Fachart, übergeben als (Fach, Kursart).
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public int getOfFachOfKursartKursdifferenz(final long idFach, final int idKursart) throws DeveloperNotificationException {
		final long idFachart = GostKursart.getFachartID(idFach, idKursart);
		return DeveloperNotificationException.ifMapGetIsNull(_fachartID_to_kursdifferenz, idFachart);
	}

	/**
	 * Liefert den Namen der Fachart, z. B. D-LK.
	 *
	 * @param idFachart  Die ID der Fachart.
	 *
	 * @return den Namen der Fachart, z. B. D-LK.
	 */
	private String getOfFachartName(final long idFachart) {
		final long idFach = GostKursart.getFachID(idFachart);
		final int idKursart = GostKursart.getKursartID(idFachart);
		return _parent.faecherManager().getOrException(idFach).kuerzelAnzeige + "-" + GostKursart.fromID(idKursart).kuerzel;
	}

	/**
	 * Liefert die Menge aller Facharten (Fach + Kursart) sortiert nach der aktuellen Sortiervariante.
	 * <br>Hinweis: Die Sortierung lässt sich mit {@link #kursSetSortierungFachKursartNummer()} und {@link #kursSetSortierungKursartFachNummer()} ändern.
	 *
	 * @return die Menge aller Facharten (Fach + Kursart) sortiert nach der aktuellen Sortiervariante.
	 */
	private @NotNull List<Long> getOfFachartMengeSortiert() {
		return _fachartIDList_sortiert;
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach LK/GK, dann nach der Fachsortierung, zuletzt nach der Kursnummer.
	 */
	public void kursSetSortierungKursartFachNummer() {
		_fachartmenge_sortierung = 1;
		stateRevalidateEverything();
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach der Fachsortierung, dann nach LK/GK, zuletzt nach der Kursnummer.
	 */
	public void kursSetSortierungFachKursartNummer() {
		_fachartmenge_sortierung = 2;
		stateRevalidateEverything();
	}

	// #########################################################################
	// ##########       Anfragen bezüglich eines Schülers.            ##########
	// #########################################################################

	/**
	 * Liefert das {@link Schueler}-Objekt zur übergebenen ID.<br>
	 * Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return das {@link Schueler}-Objekt zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die Schüler-ID unbekannt ist.
	 */
	private @NotNull Schueler getSchuelerG(final long idSchueler) throws DeveloperNotificationException {
		return _parent.schuelerGet(idSchueler);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 *
	 * @param  idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public @NotNull String getOfSchuelerNameVorname(final long idSchueler) {
		final @NotNull Schueler schueler = _parent.schuelerGet(idSchueler);
		return schueler.nachname + ", " + schueler.vorname;
	}

	/**
	 * Liefert die Menge aller Kurse, die dem Schüler zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Schüler zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public @NotNull Set<GostBlockungsergebnisKurs> getOfSchuelerKursmenge(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerID_to_kurseSet, idSchueler);
	}

	/**
	 * Liefert die sortierte Menge aller Kurse, die dem Schüler zugeordnet sind.
	 * <br>Hinweis: Die Sortierung wird mit {@link #kursSetSortierungFachKursartNummer()} und {@link #kursSetSortierungKursartFachNummer()} definiert.
	 * <br>Wirft eine Exception, wenn der ID kein Schüler zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return die sortierte Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	private @NotNull List<GostBlockungsergebnisKurs> getOfSchuelerKursmengeSortiert(final long idSchueler) {
		final List<GostBlockungsergebnisKurs> list = new ArrayList<>();
		list.addAll(DeveloperNotificationException.ifMapGetIsNull(_schuelerID_to_kurseSet, idSchueler));

		if (_fachartmenge_sortierung == 1)
			list.sort(_kursComparator_kursart_fach_kursnummer);
		else
			list.sort(_kursComparator_fach_kursart_kursnummer);

		return list;
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public @NotNull Set<GostBlockungsergebnisKurs> getOfSchuelerKursmengeMitKollisionen(final long idSchueler) {
		// Muss ein Set sein, da ein Multikurs sonst zu Dopplungen führen kann.
		final @NotNull Set<GostBlockungsergebnisKurs> set = new HashSet<>();

		for (final @NotNull GostBlockungSchiene schiene : _parent.schieneGetListe()) {
			final @NotNull Set<GostBlockungsergebnisKurs> kurseDerSchiene =
					_schuelerID_schienenID_to_kurseSet.getOrException(idSchueler, schiene.id);
			if (kurseDerSchiene.size() > 1)
				set.addAll(kurseDerSchiene);
		}

		return set;
	}

	/**
	 * Liefert die Menge aller Fachwahlen eines Schülers, die keinem Kurs zugeordnet sind.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return die Menge aller Fachwahlen eines Schülers, die keinem Kurs zugeordnet sind.
	 */
	public @NotNull List<GostFachwahl> getOfSchuelerFachwahlmengeOhneKurszuordnung(final long idSchueler) {
		final @NotNull List<GostFachwahl> list = _parent.schuelerGetListeOfFachwahlen(idSchueler);
		final @NotNull Predicate<GostFachwahl> filter = (final @NotNull GostFachwahl t) -> (getOfSchuelerOfFachZugeordneterKurs(idSchueler, t.fachID) == null);
		return ListUtils.getCopyFiltered(list, filter);
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 */
	public boolean getOfSchuelerHatNichtwahl(final long idSchueler) {
		final int nIst = DeveloperNotificationException.ifMapGetIsNull(_schuelerID_to_kurseSet, idSchueler).size();
		final int nSoll = _schuelerID_fachID_to_kurs_or_null.getSubMapSizeOrZero(idSchueler);
		return nIst < nSoll;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl (Fach + Kursart) hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart    Die ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl (Fach + Kursart) hat.
	 */
	public boolean getOfSchuelerHatFachwahl(final long idSchueler, final long idFach, final int idKursart) {
		return _parent.schuelerGetHatFachart(idSchueler, idFach, idKursart);
	}


	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach (unabhängig von der Kursart) gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach (unabhängig von der Kursart) gewählt hat.
	 */
	private boolean getOfSchuelerHatFach(final long idSchueler, final long idFach) {
		return _parent.schuelerGetHatFach(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Kollision hat. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Kollision hat.
	 */
	public boolean getOfSchuelerHatKollision(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerID_to_kollisionen, idSchueler) > 0;
	}

	/**
	 * Liefert die Anzahl der Schüler, die den Filterkriterien entsprechen.
	 *
	 * @param  idKurs           falls >= 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach           falls >= 0, werden Schüler mit diesem Fach herausgefiltert.
	 * @param  idKursart        falls >= 0 und idFach >= 0, werden Schüler mit dieser Fach/Kursart Kombination herausgefiltert.
	 * @param  konfliktTyp      falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString        falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 * @param  geschlecht       falls != null, werden die Schüler mit diesem {@link Geschlecht} herausgefiltert.
	 * @param  schriftlichkeit  falls != null, werden die Schüler mit dieser {@link GostSchriftlichkeit} herausgefiltert (isKurs oder idFach/idKursart müssen definiert sein).
	 *
	 * @return die Anzahl der Schüler, die den Filterkriterien entsprechen.
	 */
	public int getOfSchuelerAnzahlGefiltert(final long idKurs, final long idFach, final int idKursart, final int konfliktTyp, final @NotNull String subString,
			final Geschlecht geschlecht, final GostSchriftlichkeit schriftlichkeit) {
		int summe = 0;

		for (final @NotNull Schueler schueler : _parent.schuelerGetListe())
			if (getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, geschlecht, schriftlichkeit))
				summe++;

		return summe;
	}

	/**
	 * Liefert die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idSchiene   Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 */
	private @NotNull Set<GostBlockungsergebnisKurs> getOfSchuelerOfSchieneKursmenge(final long idSchueler, final long idSchiene) {
		return _schuelerID_schienenID_to_kurseSet.getOrException(idSchueler, idSchiene);
	}

	/**
	 * Liefert TRUE, falls der Schüler in der Schiene mehr als einen Kurs belegt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idSchiene   Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Schüler in der Schiene mehr als einen Kurs belegt hat.
	 */
	public boolean getOfSchuelerOfSchieneHatKollision(final long idSchueler, final long idSchiene) {
		return _schuelerID_schienenID_to_kurseSet.getOrException(idSchueler, idSchiene).size() > 1;
	}

	/**
	 * Liefert die zu (idSchueler, idFach) die jeweilige Kursart. <br>
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idFach     Die Datenbank-ID des Faches.
	 *
	 * @return Die zu (idSchueler, idFach) die jeweilige Kursart.
	 */
	public @NotNull GostKursart getOfSchuelerOfFachKursart(final long idSchueler, final long idFach) {
		return _parent.schuelerGetOfFachKursart(idSchueler, idFach);
	}

	/**
	 * Liefert den zu (idSchueler, idFach) passenden Kurs oder NULL.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idFach     Die Datenbank-ID des Faches.
	 *
	 * @return den zu (idSchueler, idFach) passenden Kurs oder NULL.
	 */
	public GostBlockungsergebnisKurs getOfSchuelerOfFachZugeordneterKurs(final long idSchueler, final long idFach) {
		return _schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler, idFach);
	}

	/**
	 * Liefert ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 *
	 * @param idSchueler           Die ID des {@link Schueler}-Objekts.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	private @NotNull SchuelerblockungOutput getOfSchuelerNeuzuordnungMitFixierung(final long idSchueler, final boolean fixiereBelegteKurse) {
		// Erzeuge das SchuelerblockungInput-Objekt für den Algorithmus.

		final @NotNull SchuelerblockungInput input = new SchuelerblockungInput();
		input.schienen = _parent.schieneGetAnzahl();

		for (final @NotNull GostFachwahl fachwahl : _parent.schuelerGetListeOfFachwahlen(idSchueler)) {
			input.fachwahlen.add(fachwahl);
			input.fachwahlenText.add(_parent.fachwahlGetName(fachwahl));
			final long fachartID = GostKursart.getFachartIDByFachwahl(fachwahl);

			// Sammle alle potentiellen Kurse der Fachart des Schülers...
			for (final @NotNull GostBlockungsergebnisKurs kursE : getOfFachartKursmenge(fachartID))
				input.kurse.add(getOfSchuelerNeuzuordnungErzeugeKurs(kursE.id, kursE.fachID, kursE.kursart, idSchueler, fixiereBelegteKurse));
		}

		// Sonderfall: Der Schüler hat 0 Fachwahlen oder alle Fachwahlen haben 0 Kurse.
		if (input.kurse.isEmpty())
			return new SchuelerblockungOutput();

		// Berechne die Zuordnung und gib sie zurück.
		return new SchuelerblockungAlgorithmus().handle(input);
	}

	private @NotNull SchuelerblockungInputKurs getOfSchuelerNeuzuordnungErzeugeKurs(
			final long idKurs,
			final long idFach,
			final int kursart,
			final long idSchueler,
			final boolean fixiereBelegteKurse) {

		final @NotNull SchuelerblockungInputKurs kursS = new SchuelerblockungInputKurs();

		kursS.id = idKurs;
		kursS.fach = idFach;
		kursS.kursart = kursart;
		kursS.schienen = getOfKursSchienenNummern(idKurs);

		// Regel: SCHUELER_VERBIETEN_IN_KURS
		kursS.istGesperrt = getOfSchuelerOfKursIstGesperrt(idSchueler, idKurs);

		// Regel: SCHUELER_FIXIEREN_IN_KURS
		kursS.istFixiert = getOfSchuelerOfKursIstFixiert(idSchueler, idKurs)
				|| (fixiereBelegteKurse && getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs));

		// SuS Anzahl: Kurse in denen der Schüler ist, müssen um 1 reduziert werden.
		kursS.anzahlSuS = getOfKursAnzahlSchuelerPlusDummy(idKurs);
		if (getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs))
			kursS.anzahlSuS--;

		// Regel: KURS_MAXIMALE_SCHUELERANZAHL
		final long maxSuS = getOfKursMaxSuS(idKurs);
		if (kursS.anzahlSuS >= maxSuS)
			kursS.istGesperrt = true;

		// Fehler, aber es wird keine Exception geworfen, sondern die Fixierung hat dann höhere Priorität.
		if (kursS.istGesperrt && kursS.istFixiert)
			kursS.istGesperrt = false;

		// Regel: SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH
		// Regel: SCHUELER_ZUSAMMEN_MIT_SCHUELER
		kursS.anzahlZusammenMitWuensche = getOfSchuelerOfKursAnzahlZusammenWuensche(idSchueler, idKurs);

		// Regel: SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH
		// Regel: SCHUELER_VERBIETEN_MIT_SCHUELER
		kursS.anzahlVerbotenMitWuensche = getOfSchuelerOfKursAnzahlVerbotenWuensche(idSchueler, idKurs);

		return kursS;
	}

	/**
	 * Liefert ein {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, welches für den Schüler eine berechnete Neuzuordnung der Kurse beinhaltet.
	 *
	 * @param idSchueler           Die ID des {@link Schueler}-Objekts.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse beinhaltet.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate getOfSchuelerNeuzuordnung(final long idSchueler, final boolean fixiereBelegteKurse) {
		// Berechne die neue Zuordnung
		final @NotNull SchuelerblockungOutput zuordnung = getOfSchuelerNeuzuordnungMitFixierung(idSchueler, fixiereBelegteKurse);

		// Erzeuge das entsprechende Update-Objekt
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final @NotNull SchuelerblockungOutputFachwahlZuKurs z : zuordnung.fachwahlenZuKurs) {
			// Kurs des Faches 'vorher'.
			final GostBlockungsergebnisKurs kursV = getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);

			// Kurs des Faches 'nachher'.
			final GostBlockungsergebnisKurs kursN = (z.kursID < 0) ? null : getKursE(z.kursID);

			// Bei Ungleichheit wird der Kurs gewechselt.
			if (kursV != kursN) {
				if (kursV != null)
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursV.id, idSchueler));
				if (kursN != null)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursN.id, idSchueler));
			}
		}

		return u;
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 */
	public boolean getOfSchuelerOfKursIstZugeordnet(final long idSchueler, final long idKurs) {
		return MapUtils.getOrCreateHashSet(_kursID_to_schuelerIDSet, idKurs).contains(idSchueler);
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist, aber keine entsprechende Fachwahl hat.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler dem Kurs zugeordnet ist, aber keine entsprechende Fachwahl hat.
	 */
	public boolean getOfSchuelerOfKursIstUngueltig(final long idSchueler, final long idKurs) {
		if (_schuelerID_to_ungueltigeKurseSet.containsKey(idSchueler))
			for (final @NotNull GostBlockungsergebnisKurs kurs : MapUtils.getOrCreateHashSet(_schuelerID_to_ungueltigeKurseSet, idSchueler))
				if (kurs.id == idKurs)
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 */
	private boolean getOfSchuelerOfKursIstFixiert(final long idSchueler, final long idKurs) {
		return _parent.schuelerGetIstFixiertInKurs(idSchueler, idKurs);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als LK gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als LK gewählt hat.
	 */
	private boolean getOfSchuelerOfKursIstLK(final long idSchueler, final long idKurs) {
		final int abiturfach = getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach >= 1) && (abiturfach <= 2);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als AB3 gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als AB3 gewählt hat.
	 */
	private boolean getOfSchuelerOfKursIstAB3(final long idSchueler, final long idKurs) {
		final int abiturfach = getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach == 3);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als LK oder AB3 gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als LK oder AB3 gewählt hat.
	 */
	private boolean getOfSchuelerOfKursIstLKoderAB3(final long idSchueler, final long idKurs) {
		final int abiturfach = getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach >= 1) && (abiturfach <= 3);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als AB4 gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als AB4 gewählt hat.
	 */
	private boolean getOfSchuelerOfKursIstAB4(final long idSchueler, final long idKurs) {
		final int abiturfach = getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach == 4);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 */
	private boolean getOfSchuelerOfKursIstAbiturfach(final long idSchueler, final long idKurs) {
		final int abiturfach = getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return abiturfach >= 1;
	}

	/**
	 * Liefert den Wert (1-4) des Abiturfaches oder 0, falls es kein Abiturfach ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return den Wert (1-4) des Abiturfaches oder 0, falls es kein Abiturfach ist.
	 */
	private int getOfSchuelerOfKursAbiturfach(final long idSchueler, final long idKurs) {
		final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
		return (fachwahl.abiturfach == null) ? 0 : fachwahl.abiturfach;
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 */
	private boolean getOfSchuelerOfKursIstSchriftlich(final long idSchueler, final long idKurs) {
		final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
		return fachwahl.istSchriftlich;
	}


	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 */
	public boolean getOfSchuelerOfKursIstGesperrt(final long idSchueler, final long idKurs) {
		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			final long schuelerID = r.parameter.get(0);
			final long kursID = r.parameter.get(1);
			if ((schuelerID == idSchueler) && (kursID == idKurs))
				return true;
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt (Groß- und Kleinschreibung wird dabei ignoriert).
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param subString   Der zu suchende Sub-String.
	 *
	 * @return TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt (Groß- und Kleinschreibung wird dabei ignoriert).
	 */
	private boolean getOfSchuelerHatImNamenSubstring(final long idSchueler, final @NotNull String subString) {
		final @NotNull Schueler schueler = getSchuelerG(idSchueler);
		final @NotNull String text = subString.toLowerCase();
		return schueler.nachname.toLowerCase().contains(text) || schueler.vorname.toLowerCase().contains(text);
	}

	/**
	 * Liefert das {@link Geschlecht} des Schülers.<br>
	 * Wirft eine Exception, falls das Enum {@link Geschlecht} nicht definiert ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return das {@link Geschlecht} des Schülers.
	 * @throws DeveloperNotificationException falls das Enum {@link Geschlecht} nicht definiert ist.
	 */
	private @NotNull Geschlecht getOfSchuelerGeschlechtOrException(final long idSchueler) throws DeveloperNotificationException {
		final @NotNull Schueler schueler = getSchuelerG(idSchueler);
		final Geschlecht geschlecht = Geschlecht.fromValue(schueler.geschlecht);
		return DeveloperNotificationException.ifNull("Das Geschlecht des Schülers " + _parent.toStringSchueler(idSchueler)
				+ " ist nicht definiert!", geschlecht);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 */
	private boolean getOfSchuelerHatStatusExtern(final @NotNull Long idSchueler) {
		final int idStatus = getSchuelerG(idSchueler).status;
		final SchuelerStatus status = SchuelerStatus.data().getWertByID(idStatus);
		return (status == SchuelerStatus.EXTERN);
	}

	/**
	 * Liefert die Fachwahl des Schüler passend zu den Kurs.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Fachwahl des Schüler passend zu den Kurs.
	 */
	public @NotNull GostFachwahl getOfSchuelerOfKursFachwahl(final long idSchueler, final long idKurs) {
		final long idFach = getKursE(idKurs).fachID;
		return _parent.schuelerGetOfFachFachwahl(idSchueler, idFach);
	}

	/**
	 * Liefert die Fachwahl des Schüler passend zum Fach.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return die Fachwahl des Schüler passend zum Fach.
	 */
	private @NotNull GostFachwahl getOfSchuelerOfFachFachwahl(final long idSchueler, final long idFach) {
		return _parent.schuelerGetOfFachFachwahl(idSchueler, idFach);
	}

	/**
	 * Liefert eine nach Kriterien gefilterte Menge aller Schüler.
	 *
	 * @param  idKurs           falls >= 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach           falls >= 0, werden Schüler mit diesem Fach herausgefiltert.
	 * @param  idKursart        falls >= 0 und idFach >= 0, werden Schüler mit dieser Fach/Kursart Kombination herausgefiltert.
	 * @param  konfliktTyp      falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString        falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 *
	 * @return eine nach Kriterien gefilterte Menge aller Schüler.
	 */
	public @NotNull List<Schueler> getOfSchuelerMengeGefiltert(final long idKurs, final long idFach, final int idKursart, final int konfliktTyp,
			final @NotNull String subString) {
		final @NotNull List<Schueler> menge = new ArrayList<>();

		for (final @NotNull Schueler schueler : _parent.schuelerGetListe())
			if (getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, null, null))
				menge.add(schueler);

		return menge;
	}

	/**
	 * Liefert eine Liste aller Schüler, deren Abiturjahrgang nicht dem der Blockung entspricht.
	 * <br>Hinweis: Das kann passieren, wenn nach der Erstellung eines Blockungsergebnisses, ein Schüler nicht versetzt wird.
	 *
	 * @return eine Liste aller Schüler, deren Abiturjahrgang nicht dem der Blockung entspricht.
	 */
	public @NotNull List<Schueler> getOfSchuelerMengeMitAbweichendemAbijahrgang() {
		final @NotNull List<Schueler> menge = new ArrayList<>();

		for (final @NotNull Schueler schueler : _parent.schuelerGetListe())
			if (schueler.abschlussjahrgang != _parent.daten().abijahrgang)
				// Nur dann den Schüler hinzufügen, wenn es überhaupt eine Kurszuordnung oder eine Regel noch gibt.
				if (!kursSchuelerUpdate_02b_ENTFERNE_SCHUELERMENGE_AUS_ALLEN_KURSEN(SetUtils.create1(schueler.id)).listEntfernen.isEmpty()
						|| !regelupdateCreate_19_SCHUELERMENGE_ENTFERNEN(SetUtils.create1(schueler.id)).listEntfernen.isEmpty())
					menge.add(schueler);

		return menge;
	}

	/**
	 * Liefert TRUE, falls der Schüler alle definierten Kriterien erfüllt.
	 *
	 * @param idSchueler        Die Datenbank-ID des Schülers.
	 * @param idKurs            Falls >= 0, muss der Schüler in dem Kurs sein.
	 * @param idFach            Falls >= 0, muss der Schüler das Fach haben.
	 * @param idKursart         Falls >= 0, und idFach >= muss der Schüler auch die zugehörige Kursart haben.
	 * @param konfliktTyp       Falls > 0 muss der Schüler "1=Kollisionen", "2=Nichtwahlen" oder "3= Kollisionen und Nichtwahlen" haben.
	 * @param subString         Falls length() > 0 muss der Schüler den Substring im Vor- oder Nachnamen haben.
	 * @param geschlecht        Falls != null, muss der Schüler das definierte Geschlecht haben.
	 * @param schriftlichkeit   Falls != null, muss der Schüler das definierte {@link GostSchriftlichkeit} haben.
	 *
	 * @return TRUE, falls der Schüler alle definierten Kriterien erfüllt.
	 */
	private boolean getOfSchuelerErfuelltKriterien(final long idSchueler, final long idKurs, final long idFach, final int idKursart, final int konfliktTyp,
			final @NotNull String subString, final Geschlecht geschlecht, final GostSchriftlichkeit schriftlichkeit) {

		if ((konfliktTyp == 1) && (!getOfSchuelerHatKollision(idSchueler)))
			return false;

		if ((konfliktTyp == 2) && (!getOfSchuelerHatNichtwahl(idSchueler)))
			return false;

		if ((konfliktTyp == 3) && ((!getOfSchuelerHatKollision(idSchueler)) && (!getOfSchuelerHatNichtwahl(idSchueler))))
			return false;

		if ((subString.length() > 0) && (!getOfSchuelerHatImNamenSubstring(idSchueler, subString)))
			return false;

		if ((geschlecht != null) && (getOfSchuelerGeschlechtOrException(idSchueler).id != geschlecht.id))
			return false;

		// Kurs-Filter
		if (idKurs >= 0) {
			if (!getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs))
				return false;

			// Schüler hat den Kurs. Stimmt die Schriftlichkeit ebenfalls?
			if (schriftlichkeit != null) {
				// Prüfe, ob die Kurs-Schüler-Zuordnung gültig ist oder nicht
				final boolean ungueltig = getOfSchuelerOfKursIstUngueltig(idSchueler, idKurs);
				if (ungueltig && (schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH))
					return false;
				if (!ungueltig && (schriftlichkeit.getIstSchriftlichOrException() != getOfSchuelerOfKursFachwahl(idSchueler, idKurs).istSchriftlich))
					return false;
			}
		}

		if (idFach >= 0) {
			if (idKursart >= 0) {
				// Fach/Kursart-Filter
				if (!getOfSchuelerHatFachwahl(idSchueler, idFach, idKursart))
					return false;
			} else {
				// Fach-Filter
				if (!getOfSchuelerHatFach(idSchueler, idFach))
					return false;
			}
			// Schüler hat das Fach. Stimmt die Schriftlichkeit ebenfalls?
			if ((schriftlichkeit != null) && (schriftlichkeit.getIstSchriftlichOrException() != getOfSchuelerOfFachFachwahl(idSchueler, idFach).istSchriftlich))
				return false;
		}

		return true;
	}

	/**
	 * Liefert die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet. <br>
	 * Hinweis 1: Hat ein Schüler keine ungültige Kurse, dann gibt es die ID nicht. <br>
	 * Hinweis 2: Gibt es keine ungültigen Wahlen, so ist die Map leer. <br>
	 *
	 * @return Die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet.
	 */
	public @NotNull Map<Long, Set<GostBlockungsergebnisKurs>> getOfSchuelerMapIDzuUngueltigeKurse() {
		return _schuelerID_to_ungueltigeKurseSet;
	}

	/**
	 * Liefert TRUE, falls der Schüler in einer Schiene des Kurses eine Kollision hat.<br>
	 * Die Methode geht davon aus, dass der Schüler dem Kurs zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler in einer Schiene des Kurses eine Kollision hat.
	 */
	public boolean getOfSchuelerOfKursHatKollision(final long idSchueler, final long idKurs) {
		// Schnelltest, ob der Schüler überhaupt eine Kollision hat.
		if (!getOfSchuelerHatKollision(idSchueler))
			return false;

		// Überprüfe, ob die Schienen des Kurses beim Schüler mehrfachbelegt sind.
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		for (final @NotNull Long idSchiene : kurs.schienen)
			if (getOfSchuelerOfSchieneKursmenge(idSchueler, idSchiene).size() > 1)
				return true;

		return false;
	}


	/**
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach gemeinsam im selben Kurs sind.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return TRUE, falls beide Schüler im bezogen auf das Fach gemeinsam im selben Kurs sind.
	 */
	public boolean getOfSchuelerIstZusammenMitSchuelerInFach(final long idSchueler1, final long idSchueler2, final long idFach) {
		final GostBlockungsergebnisKurs kurs1 = _schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler1, idFach);
		final GostBlockungsergebnisKurs kurs2 = _schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler2, idFach);
		return ((kurs1 != null) && (kurs2 != null)) && (kurs1.id == kurs2.id);
	}

	/**
	 * Liefert die Anzahl an SuS, die mit dem Schüler im Kurs sein wollen.
	 *
	 * @param idS1    Die ID des {@link Schueler}-Objekts.
	 * @param idKurs  Die ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return die Anzahl an SuS, die mit dem Schüler im Kurs sein wollen.
	 */
	public int getOfSchuelerOfKursAnzahlZusammenWuensche(final long idS1, final long idKurs) {
		int anzahl = 0;

		final long idFach = getKursE(idKurs).fachID;
		for (final long idS2 : getKursE(idKurs).schueler) {
			if (idS1 == idS2)
				continue;
			final int typ1 = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
			final int typ2 = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
			if ((_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ1, idS1, idS2 })) != null)
					|| (_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ1, idS2, idS1 })) != null)
					|| (_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ2, idS1, idS2, idFach })) != null)
					|| (_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ2, idS2, idS1, idFach })) != null))
				anzahl++;
		}

		return anzahl;
	}

	/**
	 * Liefert die Anzahl an SuS, die mit dem Schüler nicht im Kurs sein sollen.
	 *
	 * @param idS1    Die ID des {@link Schueler}-Objekts.
	 * @param idKurs  Die ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return die Anzahl an SuS, die mit dem Schüler nicht im Kurs sein sollen.
	 */
	public int getOfSchuelerOfKursAnzahlVerbotenWuensche(final long idS1, final long idKurs) {
		int anzahl = 0;

		final long idFach = getKursE(idKurs).fachID;
		for (final long idS2 : getKursE(idKurs).schueler) {
			if (idS1 == idS2)
				continue;
			final int typ1 = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
			final int typ2 = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
			if ((_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ1, idS1, idS2 })) != null)
					|| (_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ1, idS2, idS1 })) != null)
					|| (_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ2, idS1, idS2, idFach })) != null)
					|| (_parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(new long[] { typ2, idS2, idS1, idFach })) != null))
				anzahl++;
		}

		return anzahl;
	}

	// #########################################################################
	// ##########       Anfragen bezüglich eines Kurses.              ##########
	// #########################################################################

	/**
	 * Liefert den {@link GostBlockungKurs} zur übergebenen ID.<br>
	 * Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den {@link GostBlockungKurs} zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostBlockungKurs getKursG(final long idKurs) throws DeveloperNotificationException {
		return _parent.kursGet(idKurs);
	}

	/**
	 * Liefert den {@link GostBlockungsergebnisKurs} zur übergebenen ID.<br>
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return den {@link GostBlockungsergebnisKurs} zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostBlockungsergebnisKurs getKursE(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_kursID_to_kurs, idKurs);
	}

	/**
	 * Gibt die Menge der {@link GostBlockungsergebnisKurs} zurück.
	 *
	 * @return die Menge der {@link GostBlockungsergebnisKurs}
	 */
	public @NotNull List<GostBlockungsergebnisKurs> getKursmenge() {
		final @NotNull List<GostBlockungsergebnisKurs> result = new ArrayList<>();
		result.addAll(_kursID_to_kurs.values());
		return result;
	}

	/**
	 * Liefert den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 */
	public @NotNull String getOfKursName(final long idKurs) {
		return _parent.kursGetName(idKurs);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene zugeordnet ist.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs der Schiene zugeordnet ist.
	 */
	public boolean getOfKursOfSchieneIstZugeordnet(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(idSchiene);
		final @NotNull Set<GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(idKurs);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene mit der Nummer zugeordnet ist.
	 *
	 * @param  idKurs      Die Datenbank-ID des Kurses.
	 * @param  schienenNr  Die Nummer der Schiene
	 *
	 * @return TRUE, falls der Kurs der Schiene mit der Nummer zugeordnet ist.
	 */
	private boolean getOfKursOfSchienenNrIstZugeordnet(final long idKurs, final int schienenNr) {
		for (final int nr : getOfKursSchienenNummern(idKurs))
			if (nr == schienenNr)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs in der Schiene fixiert ist.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs in der Schiene fixiert ist.
	 */
	private boolean getOfKursOfSchieneIstFixiert(final long idKurs, final long idSchiene) {
		return _parent.kursGetHatFixierungInSchiene(idKurs, idSchiene);
	}

	/**
	 * Liefert zur Kurs-ID die zugehörige Menge aller Schüler-IDs.<br>
	 * Wirft eine Exception, falls der ID kein Kurs zugeordnet ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return zur Kurs-ID die zugehörige Menge aller Schüler-IDs.
	 * @throws DeveloperNotificationException falls der ID kein Kurs zugeordnet ist.
	 */
	private @NotNull Set<Long> getOfKursSchuelerIDmenge(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schuelerIDSet, idKurs);
	}

	/**
	 * Liefert die Menge aller Schüler-Objekte des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-Objekte des Kurses.
	 */
	public @NotNull List<Schueler> getOfKursSchuelermenge(final long idKurs) {
		final @NotNull List<Schueler> list = new ArrayList<>();

		for (final @NotNull Long idSchueler : getKursE(idKurs).schueler)
			list.add(getSchuelerG(idSchueler));

		return list;
	}

	/**
	 * Liefert die Schienenmenge des Kurses.<br>
	 * Wirft eine Exception, falls der ID kein Kurs zugeordnet ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Schienenmenge des Kurses.
	 * @throws DeveloperNotificationException falls der ID kein Kurs zugeordnet ist.
	 */
	public @NotNull Set<GostBlockungsergebnisSchiene> getOfKursSchienenmenge(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schienenSet, idKurs);
	}

	/**
	 * Liefert ein Array aller Schienen-Nummern des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return ein Array aller Schienen-Nummern des Kurses.
	 */
	public @NotNull int[] getOfKursSchienenNummern(final long idKurs) {
		final @NotNull List<@NotNull Long> schienenIDs = getKursE(idKurs).schienen;
		final int[] a = new int[schienenIDs.size()];
		for (int i = 0; i < a.length; i++) {
			final long schienenID = schienenIDs.get(i);
			a[i] = _parent.schieneGet(schienenID).nummer;
		}
		return a;
	}

	/**
	 * Liefert TRUE, falls der Kurs mindestens eine Kollision hat. <br>
	 * Definition: Ein Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mindestens eine Kollision hat.
	 */
	private boolean getOfKursHatKollision(final long idKurs) {
		return getOfKursAnzahlKollisionen(idKurs) > 0;
	}

	/**
	 * Liefert die Anzahl an Schülern des Kurses mit Kollisionen.<br>
	 * Kollision: Der Schüler muss in mindestens einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern des Kurses mit Kollisionen.
	 */
	private int getOfKursAnzahlKollisionen(final long idKurs) {
		return getOfKursSchuelermengeMitKollisionen(idKurs).size();
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 *
	 * @param idKursID  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 */
	private @NotNull Set<Long> getOfKursSchuelermengeMitKollisionen(final long idKursID) {
		final @NotNull Set<Long> set = new HashSet<>();

		for (final @NotNull GostBlockungsergebnisSchiene schiene : getOfKursSchienenmenge(idKursID))
			for (final @NotNull Long idSchueler : getKursE(idKursID).schueler)
				if (getOfSchuelerOfSchieneKursmenge(idSchueler, schiene.id).size() > 1)
					set.add(idSchueler); // Set ist wichtig, da bei Multikursen ein Schüler mehrfach kollidieren kann.

		return set;
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind ohne Dummy SuS.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind ohne Dummy SuS.
	 */
	public int getOfKursAnzahlSchueler(final long idKurs) {
		return getKursE(idKurs).schueler.size();
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind plus potentiell zugeordnete Dummy SuS.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind plus potentiell zugeordnete Dummy SuS.
	 */
	private int getOfKursAnzahlSchuelerPlusDummy(final long idKurs) {
		return getKursE(idKurs).schueler.size() + DeveloperNotificationException.ifMapGetIsNull(_kursID_to_dummySuS, idKurs);
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS des Kurses. Dummy-SuS werden durch die Regel mit dem
	 * Typ {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN} einem Kurs zugeordnet.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Dummy-SuS des Kurses.
	 */
	public int getOfKursAnzahlSchuelerDummy(final long idKurs) {
		return DeveloperNotificationException.ifMapGetIsNull(_kursID_to_dummySuS, idKurs);
	}

	/**
	 * Liefert die Anzahl externer SuS die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl externer SuS die dem Kurs zugeordnet sind.
	 */
	public int getOfKursAnzahlSchuelerExterne(final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(idKurs);
		return ListUtils.getCountFiltered(kursE.schueler, (final @NotNull Long idSchueler) -> getOfSchuelerHatStatusExtern(idSchueler));
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public int getOfKursAnzahlSchuelerSchriftlich(final long idKurs) {
		return getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public int getOfKursAnzahlSchienenIst(final long idKurs) {
		return getOfKursSchienenmenge(idKurs).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public int getOfKursAnzahlSchienenSoll(final long idKurs) {
		return getKursE(idKurs).anzahlSchienen;
	}

	/**
	 * Liefert die Anzahl an Schülern, die den Kurs mit Abiturfach 1 oder 2 gewählt (also LK) haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern, die den Kurs mit Abiturfach 1 oder 2 gewählt (also LK) haben.
	 */
	public int getOfKursAnzahlSchuelerAbiturLK(final long idKurs) {
		int summe = 0;
		for (final @NotNull Long idSchueler : getKursE(idKurs).schueler) {
			try {
				final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
				if ((fachwahl.abiturfach != null) && ((fachwahl.abiturfach == 1) || (fachwahl.abiturfach == 2)))
					summe++;
			} catch (@SuppressWarnings("unused") final DeveloperNotificationException dne) {
				// Für die Kurs-Schüler-Zuordnung existiert keine Fachwahl. Ignoriere diese...
			}
		}
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schülern, die den Kurs mit Abiturfach 3 gewählt haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern, die den Kurs mit Abiturfach 3 gewählt haben.
	 */
	public int getOfKursAnzahlSchuelerAbitur3(final long idKurs) {
		int summe = 0;
		for (final @NotNull Long idSchueler : getKursE(idKurs).schueler) {
			try {
				final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
				if ((fachwahl.abiturfach != null) && (fachwahl.abiturfach == 3))
					summe++;
			} catch (@SuppressWarnings("unused") final DeveloperNotificationException dne) {
				// Für die Kurs-Schüler-Zuordnung existiert keine Fachwahl. Ignoriere diese...
			}
		}
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schülern, die den Kurs mit Abiturfach 4 gewählt haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern, die den Kurs mit Abiturfach 4 gewählt haben.
	 */
	public int getOfKursAnzahlSchuelerAbitur4(final long idKurs) {
		int summe = 0;
		for (final @NotNull Long idSchueler : getKursE(idKurs).schueler) {
			try {
				final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
				if ((fachwahl.abiturfach != null) && (fachwahl.abiturfach == 4))
					summe++;
			} catch (@SuppressWarnings("unused") final DeveloperNotificationException dne) {
				// Für die Kurs-Schüler-Zuordnung existiert keine Fachwahl. Ignoriere diese...
			}
		}
		return summe;
	}

	/**
	 * Liefert die maximale Anzahl an SuS, die in dem Kurs sein dürfen, oder 999 falls es keine Begrenzung gibt.
	 *
	 * @param idKurs  Die ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return die maximale Anzahl an SuS, die in dem Kurs sein dürfen, oder 999 falls es keine Begrenzung gibt.
	 */
	public long getOfKursMaxSuS(final long idKurs) {
		for (final GostBlockungRegel rAlt : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL))
			if (idKurs == rAlt.parameter.get(0))
				return rAlt.parameter.get(1);

		return 999;
	}

	/**
	 * Liefert die Menge aller Schüler eines Kurses, die noch nicht fixiert sind.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler eines Kurses, die noch nicht fixiert sind.
	 */
	private @NotNull List<Schueler> getOfKursMengeAllerNichtFixiertenSchueler(final long idKurs) {
		final @NotNull List<Schueler> list = new ArrayList<>();

		for (final @NotNull Schueler schueler : getOfKursSchuelermenge(idKurs))
			if (!getOfSchuelerOfKursIstFixiert(schueler.id, idKurs))
				list.add(schueler);

		return list;
	}


	/**
	 * Liefert die Menge aller Schüler eines Kurses, die noch nicht fixiert sind und den Kurs als Abiturfach (1, 2, 3 oder 4) gewählt haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler eines Kurses, die noch nicht fixiert sind und den Kurs als Abiturfach (1, 2, 3 oder 4) gewählt haben.
	 */
	private @NotNull List<Schueler> getOfKursMengeAllerNichtFixiertenAbiturSchueler(final long idKurs) {
		final @NotNull List<Schueler> list = new ArrayList<>();

		for (final @NotNull Schueler schueler : getOfKursSchuelermenge(idKurs))
			if ((!getOfSchuelerOfKursIstFixiert(schueler.id, idKurs)) && (getOfSchuelerOfKursIstAbiturfach(schueler.id, idKurs)))
				list.add(schueler);

		return list;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 */
	public @NotNull Map<Long, Set<Long>> getMappingKursIDSchuelerIDs() {
		return _kursID_to_schuelerIDSet;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public @NotNull Map<Long, Set<GostBlockungsergebnisSchiene>> getMappingKursIDSchienenmenge() {
		return _kursID_to_schienenSet;
	}

	/**
	 * Liefert eine Menge aller Kurse mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Kurse mit mindestens einer Kollision.
	 */
	private @NotNull Set<GostBlockungsergebnisKurs> getMengeDerKurseMitKollisionen() {
		final @NotNull Set<GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisKurs kurs : _kursID_to_kurs.values())
			if (getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	private static @NotNull List<GostBlockungKurs> regelGetListeToggleFilteredBetween(final @NotNull List<GostBlockungKurs> list,
			final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB) {
		final @NotNull List<GostBlockungKurs> result = new ArrayList<>();
		boolean foundA = false;
		boolean foundB = false;

		// Alle Elemente zwischen den beiden markierten Kursen kopieren.
		for (final @NotNull GostBlockungKurs kursG : list) {
			if (kursG == kursA)
				foundA = true;
			if (kursG == kursB)
				foundB = true;

			if (foundA || foundB)
				result.add(kursG);

			if (foundA && foundB)
				break;
		}

		return result;
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 *
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 *
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 */
	private @NotNull List<GostBlockungRegel> regelGetListeToggleSperrung(final @NotNull List<GostBlockungKurs> list,
			final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB, final @NotNull GostBlockungSchiene schieneA,
			final @NotNull GostBlockungSchiene schieneB) {
		final int min = Math.min(schieneA.nummer, schieneB.nummer);
		final int max = Math.max(schieneA.nummer, schieneB.nummer);
		final @NotNull List<GostBlockungRegel> regeln = new ArrayList<>();

		for (final @NotNull GostBlockungKurs kursG : GostBlockungsergebnisManager.regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (int nr = min; nr <= max; nr++)
				regeln.add(_parent.regelGetRegelOrDummyKursGesperrtInSchiene(kursG.id, nr));

		return regeln;
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schienen-Fixierung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 *
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 *
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schienen-Fixierung in einem Auswahl-Rechteck ändern soll.
	 */
	private @NotNull List<GostBlockungRegel> regelGetListeToggleKursfixierung(final @NotNull List<GostBlockungKurs> list,
			final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB, final @NotNull GostBlockungSchiene schieneA,
			final @NotNull GostBlockungSchiene schieneB) {
		final int min = Math.min(schieneA.nummer, schieneB.nummer);
		final int max = Math.max(schieneA.nummer, schieneB.nummer);
		final @NotNull List<GostBlockungRegel> regeln = new ArrayList<>();

		for (final @NotNull GostBlockungKurs kursG : GostBlockungsergebnisManager.regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (final @NotNull GostBlockungsergebnisSchiene schieneE : DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schienenSet, kursG.id)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if ((schieneG.nummer >= min) && (schieneG.nummer <= max)) // Kurs im Auswahl-Rechteck?
					regeln.add(_parent.regelGetRegelOrDummyKursFixierungInSchiene(kursG.id, schieneG.nummer));
			}

		return regeln;
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schueler-Fixierung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 * <br>Hinweis: Wenn ein Multi-Kurs zum Teil im Auswahl-Rechteck liegt, wird der Kurs ebenso beachtet.
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 *
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schueler-Fixierung in einem Auswahl-Rechteck ändern soll.
	 */
	private @NotNull List<GostBlockungRegel> regelGetListeToggleSchuelerfixierung(final @NotNull List<GostBlockungKurs> list,
			final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB, final @NotNull GostBlockungSchiene schieneA,
			final @NotNull GostBlockungSchiene schieneB) {
		final int min = Math.min(schieneA.nummer, schieneB.nummer);
		final int max = Math.max(schieneA.nummer, schieneB.nummer);
		final @NotNull List<GostBlockungRegel> regeln = new ArrayList<>();

		for (final @NotNull GostBlockungKurs kursG : GostBlockungsergebnisManager.regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (final @NotNull GostBlockungsergebnisSchiene schieneE : DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schienenSet, kursG.id)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if ((schieneG.nummer >= min) && (schieneG.nummer <= max)) {
					// Kurs gefunden, füge nun seine SuS hinzu.
					final @NotNull GostBlockungsergebnisKurs kursE = getKursE(kursG.id);
					for (final long idSchueler : kursE.schueler)
						regeln.add(_parent.regelGetRegelOrDummySchuelerInKursFixierung(idSchueler, kursE.id));
					// Bei Multikursen dürfen SuS nur einmalig fixiert werden.
					break;
				}
			}

		return regeln;
	}

	/**
	 * Liefert die Regel-Menge aller Kurs-Schienen-Fixierungen eines bestimmten Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel-Menge aller Kurs-Schienen-Fixierungen eines bestimmten Kurses.
	 */
	private @NotNull List<GostBlockungRegel> regelGetMengeAnKursSchienenFixierungenDesKurses(final long idKurs) {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if (regel.parameter.get(0) == idKurs)
				list.add(regel);

		return list;
	}

	/**
	 * Liefert die Regel-Menge aller Kurs-Schienen-Fixierungen einer bestimmten Kursmenge.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Regel-Menge aller Kurs-Schienen-Fixierungen einer bestimmten Kursmenge.
	 */
	private @NotNull List<GostBlockungRegel> regelGetMengeAnKursSchienenFixierungenDerKurse(final @NotNull List<Long> listeDerKursIDs) {
		// List<ID> zu Set<ID>, damit man schnell auf Existenz überprüfen kann.
		final @NotNull Set<Long> setKursIDs = new HashSet<>(listeDerKursIDs);
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if (setKursIDs.contains(regel.parameter.get(0)))
				list.add(regel);

		return list;
	}

	/**
	 * Liefert die Regel-Menge aller Schüler-Kurs-Fixierungen des übergebenen Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel-Menge aller Schüler-Kurs-Fixierungen des übergebenen Kurses.
	 */
	private @NotNull List<GostBlockungRegel> regelGetMengeAllerSchuelerKursFixierungenDesKurses(final long idKurs) {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			if (regel.parameter.get(1) == idKurs)
				list.add(regel);

		return list;
	}

	/**
	 * Liefert die Regel-Menge aller Schüler-Kurs-Fixierungen der übergebenen Kurse.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Regel-Menge aller Schüler-Kurs-Fixierungen der übergebenen Kurse.
	 */
	private @NotNull List<GostBlockungRegel> regelGetMengeAllerSchuelerKursFixierungenDerKurse(final @NotNull List<Long> listeDerKursIDs) {
		// List<ID> zu Set<ID>, damit man schnell auf Existenz überprüfen kann.
		final @NotNull Set<Long> setKursIDs = new HashSet<>(listeDerKursIDs);

		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			if (setKursIDs.contains(regel.parameter.get(1)))
				list.add(regel);

		return list;
	}

	/**
	 * Liefert die Map, welche der verletzten Regel-ID (long) die Beschreibung (String) zuordnet.
	 * <br>Hinweis: Nur verletzte Regel-IDs sind in der KEY-Menge enthalten.
	 *
	 * @return die Map, welche der verletzten Regel-ID (long) die Beschreibung (String) zuordnet.
	 */
	public @NotNull Map<Long, String> regelGetMap_regelID_to_verletzungString() {
		return _regelID_to_verletzungString;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 * <br>Hinweis: Falls ein Kurs bereits fixierte Schienen hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 */
	private @NotNull List<GostBlockungRegel> regelGetDummyMengeAllerKursSchienenFixierungen() {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungsergebnisKurs kurs : _kursID_to_kurs.values())
			for (final @NotNull GostBlockungsergebnisSchiene schiene : getOfKursSchienenmenge(kurs.id))
				if (!getOfKursOfSchieneIstFixiert(kurs.id, schiene.id)) {
					final long schienenNr = _parent.schieneGet(schiene.id).nummer;
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr));
				}

		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller Kurs-Schienen-Fixierungen der übergebenen Kurse.
	 * <br>Hinweis: Falls ein Kurs bereits fixierte Schienen hat, werden dazu keine Regeln erzeugt.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller Kurs-Schienen-Fixierungen der übergebenen Kurse.
	 */
	private @NotNull List<GostBlockungRegel> regelGetDummyMengeAnKursSchienenFixierungen(final @NotNull List<Long> listeDerKursIDs) {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull Long idKurs : listeDerKursIDs)
			for (final @NotNull GostBlockungsergebnisSchiene schiene : getOfKursSchienenmenge(idKurs))
				if (!getOfKursOfSchieneIstFixiert(idKurs, schiene.id)) {
					final long schienenNr = _parent.schieneGet(schiene.id).nummer;
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr));
				}

		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen.
	 */
	private @NotNull List<GostBlockungRegel> regelGetDummyMengeAllerSchuelerKursFixierungen() {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungsergebnisKurs kurs : _kursID_to_kurs.values())
			for (final @NotNull Schueler schueler : getOfKursSchuelermenge(kurs.id))
				if (!getOfSchuelerOfKursIstFixiert(schueler.id, kurs.id))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, kurs.id));

		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen der Abiturkurse.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen der Abiturkurse.
	 */
	private @NotNull List<GostBlockungRegel> regelGetDummyMengeAllerSchuelerAbiturKursFixierungen() {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungsergebnisKurs kurs : _kursID_to_kurs.values())
			for (final @NotNull Schueler schueler : getOfKursSchuelermenge(kurs.id))
				if ((getOfSchuelerOfKursIstAbiturfach(schueler.id, kurs.id)) && (!getOfSchuelerOfKursIstFixiert(schueler.id, kurs.id)))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, kurs.id));

		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge.
	 */
	private @NotNull List<GostBlockungRegel> regelGetDummyMengeAnKursSchuelerFixierungen(final @NotNull List<Long> listeDerKursIDs) {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull Long idKurs : listeDerKursIDs)
			for (final @NotNull Schueler schueler : getOfKursSchuelermenge(idKurs))
				if (!getOfSchuelerOfKursIstFixiert(schueler.id, idKurs))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, idKurs));

		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge, welche als Abiturfach gewählt wurden.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge, welche als Abiturfach gewählt wurden.
	 */
	private @NotNull List<GostBlockungRegel> regelGetDummyMengeAnAbiturKursSchuelerFixierungen(final @NotNull List<Long> listeDerKursIDs) {
		final @NotNull List<GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull Long idKurs : listeDerKursIDs)
			for (final @NotNull Schueler schueler : getOfKursSchuelermenge(idKurs))
				if ((getOfSchuelerOfKursIstAbiturfach(schueler.id, idKurs)) && (!getOfSchuelerOfKursIstFixiert(schueler.id, idKurs)))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, idKurs));

		return list;
	}

	/**
	 * Liefert einen Tooltip für alle Regelverletzungen der definierten Regeln.
	 *
	 * @return einen Tooltip für alle Regelverletzungen der definierten Regeln.
	 */
	final @NotNull String regelGetTooltipFuerRegelverletzungen() {
		return _regelverletzungen_tooltip1_regeln;
	}

	/**
	 * Liefert einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 *
	 * @return einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 */
	final @NotNull String regelGetTooltipFuerFaecherparallelitaet() {
		return _regelverletzungen_tooltip4_faecherparallelitaet;
	}

	/**
	 * Liefert einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 *
	 * @return einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 */
	final @NotNull String regelGetTooltipFuerWahlkonflikte() {
		return _regelverletzungen_tooltip2_wahlkonflikte;
	}

	/**
	 * Liefert einen Tooltip für alle Kursdifferenzen.
	 *
	 * @return einen Tooltip für alle Kursdifferenzen.
	 */
	final @NotNull String regelGetTooltipFuerKursdifferenzen() {
		return _regelverletzungen_tooltip3_kursdifferenzen;
	}

	private static boolean regelupdateIsEqualPair(final long a1, final long a2, final long b1, final long b2) {
		return ((a1 == b1) && (a2 == b2)) || ((a1 == b2) && (a2 == b1));
	}

	private static void regelupdateAppend(final @NotNull GostBlockungRegelUpdate u1, final @NotNull GostBlockungRegelUpdate u2) {
		u1.listEntfernen.addAll(u2.listEntfernen);
		u1.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Sperrung zu setzen.
	 * <br>(1) Wenn ein Kurs der Kursart im Schienen-Bereich liegt und gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn ein Kurs der Kursart im Schienen-Bereich liegt und fixiert ist, wird dies entfernt.
	 * <br>(3a) Wenn die Regel in falscher von/bis-Reihenfolge existiert, wird sie entfernt.
	 * <br>(3b) Wenn die Regel nicht bereits existiert, wird sie hinzugefügt.
	 *
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Sperrung zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_01_KURSART_SPERRE_SCHIENEN_VON_BIS(final int kursart, final int schienenNrVon,
			final int schienenNrBis) {
		final int von = Math.min(schienenNrVon, schienenNrBis);
		final int bis = Math.max(schienenNrVon, schienenNrBis);
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final @NotNull GostBlockungsergebnisKurs kurs : getKursmenge())
			for (int schienenNr = von; schienenNr <= bis; schienenNr++)
				if (kurs.kursart == kursart) { // Kursart und Schienenbereich stimmen.
					// (1)
					final @NotNull LongArrayKey keySperrung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr });
					final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung != null)
						u.listEntfernen.add(regelSperrung);
					// (2)
					final @NotNull LongArrayKey keyFixierung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr });
					final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (regelFixierung != null)
						u.listEntfernen.add(regelFixierung);
				}

		// (3a)
		final @NotNull LongArrayKey keyBisVon =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, bis, von });
		final GostBlockungRegel regelBisVon = _parent.regelGetByLongArrayKeyOrNull(keyBisVon);
		if (regelBisVon != null)
			u.listEntfernen.add(regelBisVon);

		// (3)
		final @NotNull LongArrayKey keyVonBis =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis });
		if (_parent.regelGetByLongArrayKeyOrNull(keyVonBis) == null)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung einer Rechtecks-Auswahl zu realisieren.
	 * <br>(1) Wenn der Kurs markiert ist und eine Sperrung hat, wird die Sperrung entfernt.
	 * <br>(2) Wenn der Kurs markiert ist und keine Fixierung hat, wird eine Fixierung erzeugt.
	 * <br>(3) Fixierungen außerhalb der Kurslage werden vorsichtshalber gelöscht.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller markierten Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung einer Rechtecks-Auswahl zu realisieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(final @NotNull Set<Long> setKursID,
			final @NotNull Set<Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1) (2) --> Markiert + Kurs
			for (final int nr : setSchienenNr)
				if (getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					// (1)
					final @NotNull LongArrayKey kSperrung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nr });
					final GostBlockungRegel rSperrung = _parent.regelGetByLongArrayKeyOrNull(kSperrung);
					if (rSperrung != null)
						u.listEntfernen.add(rSperrung);
					// (2)
					final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr });
					final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr));
				}

			// (3) Fixiert + kein Kurs
			for (int nr = 1; nr <= _schienenNR_to_schiene.size(); nr++)
				if (!getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr });
					final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung != null)
						u.listEntfernen.add(rFixierung);
				}
		}



		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge komplett in ihrer Lage zu fixieren.
	 * <br>(1) Fixierungen innerhalb der Kurslage werden hinzugefügt, falls noch nicht existend.
	 * <br>(2) Fixierungen außerhalb der Kurslage werden gelöscht.
	 *
	 * @param setKursID  Die Kursmenge, die fixiert werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge komplett in ihrer Lage zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (int nr = 1; nr <= _schienenNR_to_schiene.size(); nr++) {
				final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr });
				final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);

				if (getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					// (1)
					if (rFixierung == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr));
				} else {
					// (2)
					if (rFixierung != null)
						u.listEntfernen.add(rFixierung);
				}
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurse komplett in ihrer Lage zu fixieren.
	 * <br>(1) Fixierungen innerhalb der Kurslage werden hinzugefügt, falls noch nicht existend.
	 * <br>(2) Fixierungen außerhalb der Kurslage werden gelöscht.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurse komplett in ihrer Lage zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02c_KURS_FIXIERE_ALLE_IN_IHREN_SCHIENEN() {
		return regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(_parent.kursmengeGetSetDerIDs());
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Fixierung zu realisieren.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich liegt und fixiert ist, wird die Fixierung gelöst.
	 * <br>(2) Wenn der Kurs im Schienen-Bereich liegt und nicht fixiert ist, wird er fixiert
	 * <br>(3) und falls dort eine Sperrung vorliegt, dann wird die Sperrung entfernt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Fixierung zu realisieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02d_KURS_FIXIERE_IN_SCHIENE_TOGGLE(final @NotNull Set<Long> setKursID,
			final @NotNull Set<Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// Beim Fixieren muss man über die Kurse und dann über die Schienen des Kurses iterieren.
		for (final long idKurs : setKursID)
			for (final @NotNull GostBlockungsergebnisSchiene schieneE : DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schienenSet, idKurs)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					final @NotNull LongArrayKey keyFixierung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer });
					final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);

					// (1)
					if (regelFixierung != null) {
						u.listEntfernen.add(regelFixierung);
						continue;
					}

					// (2)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer));

					// (3)
					final @NotNull LongArrayKey keySperrung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer });
					final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung != null)
						u.listEntfernen.add(regelSperrung);
				}
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um einen Kurs in einer Schiene zu fixieren.
	 * <br>(1) Wenn der Kurs in der Schiene eine Sperrung hat, wird diese entfernt.
	 * <br>(2) Wenn der Kurs bereits in der Schiene fixiert ist, passiert nichts weiteres.
	 * <br>(3) Wenn der Kurs bereits vollständig fixiert ist, werden seine alten Fixierungen entfernt.
	 * <br>(4) Andernfalls wird der Kurs in der Schiene fixiert.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param schienenNr  Die Nummer der Schiene, die fixiert werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um einen Kurs in einer Schiene zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02e_KURS_FIXIERE_IN_EINER_SCHIENE(final long idKurs, final int schienenNr) {
		return regelupdateCreate_02e_helper(idKurs, schienenNr, true);
	}

	private @NotNull GostBlockungRegelUpdate regelupdateCreate_02e_helper(final long idKurs, final int schienenNr, final boolean checkErlaubt) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull LongArrayKey kSperrung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr });
		final GostBlockungRegel rSperrung = _parent.regelGetByLongArrayKeyOrNull(kSperrung);
		if (rSperrung != null)
			u.listEntfernen.add(rSperrung);

		// (2)
		final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr });
		final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
		if (rFixierung != null)
			return u;

		// (3)
		if (checkErlaubt && !_parent.kursIstWeitereFixierungErlaubt(idKurs))
			for (int nr = 1; nr <= _schienenNR_to_schiene.size(); nr++) {
				final @NotNull LongArrayKey kFixierungAlt = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr });
				final GostBlockungRegel rFixierungAlt = _parent.regelGetByLongArrayKeyOrNull(kFixierungAlt);
				if (rFixierungAlt != null)
					u.listEntfernen.add(rFixierungAlt);
			}

		// Hinzufügen
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu setzen.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich nicht gesperrt ist und keine Fixierung vorliegt, wird er gesperrt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_03_KURS_SPERRE_IN_SCHIENE(final @NotNull Set<Long> setKursID,
			final @NotNull Set<Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (final int schienenNr : setSchienenNr) {
				// (1)
				final @NotNull LongArrayKey keySperrung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr });
				final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
				final @NotNull LongArrayKey keyFixierung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr });
				final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if ((regelSperrung == null) && (regelFixierung == null))
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr));
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Sperrung zu realisieren.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich liegt und gesperrt ist, wird die Sperrung gelöst.
	 * <br>(2) Wenn der Kurs im Schienen-Bereich liegt und nicht gesperrt ist und keine Fixierung vorliegt, wird er gesperrt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Sperrung zu realisieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_03b_KURS_SPERRE_IN_SCHIENE_TOGGLE(final @NotNull Set<Long> setKursID,
			final @NotNull Set<Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (final @NotNull GostBlockungsergebnisSchiene schieneE : DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schienenSet, idKurs)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					// (1)
					final @NotNull LongArrayKey keySperrung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer });
					final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung != null) {
						u.listEntfernen.add(regelSperrung);
						continue;
					}
					// (2)
					final @NotNull LongArrayKey keyFixierung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer });
					final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (regelFixierung == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer));
				}
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu setzen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler bereits im Kurs fixiert ist, wird dies ignoriert.
	 * <br>(4) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(final @NotNull Set<Long> setSchuelerID,
			final @NotNull Set<Long> setKursID) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID)
				schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu setzen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler bereits im Kurs fixiert ist, wird dies ignoriert.
	 * <br>(4) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1) (2) (3) (4)
			final @NotNull GostBlockungRegelUpdate u2 =
					regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler in ihren aktuellen Kursen zu fixieren.
	 * <br>Die Methode delegiert alles an {@link #regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS}.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler in ihren aktuellen Kursen zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04c_SCHUELER_FIXIEREN_IN_ALLEN_KURSEN() {
		return regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(_kursID_to_kurs.keySet());
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LK-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LK-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK() {
		return regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(_kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle LK-Schüler einer Kursmenge zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle LK-Schüler einer Kursmenge zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(final @NotNull Set<Long> kursIDs) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstLK(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3() {
		return regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(_kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(final @NotNull Set<Long> kursIDs) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstAB3(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3() {
		return regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(_kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(final @NotNull Set<Long> kursIDs) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstLKoderAB3(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4() {
		return regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(_kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(final @NotNull Set<Long> kursIDs) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstAB4(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB() {
		return regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(_kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(final @NotNull Set<Long> kursIDs) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstAbiturfach(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH() {
		return regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(_kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(final @NotNull Set<Long> kursIDs) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstSchriftlich(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Schüler-Kurs-Fixierung.
	 *
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param idSchueler             Die ID des Schülers.
	 * @param idKurs                 Die ID des Kurses.
	 *
	 * @return alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(final long idSchueler, final long idKurs) {
		final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare = new HashSet<>();
		schuelerKursPaare.add(new PairNN<>(idSchueler, idKurs));
		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(schuelerKursPaare);
	}

	/**
	 * Liefert alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 *
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param schuelerKursPaare      Die Menge aller Schüler-Kurs-Paare.
	 *
	 * @return alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURSMENGE(
			final @NotNull Set<PairNN<Long, Long>> schuelerKursPaare) {
		// TODO Wenn der Schüler gar nicht den Kurs wählen kann --> ignorieren.

		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final @NotNull PairNN<Long, Long> pair : schuelerKursPaare) {
			final @NotNull Long idSchueler = pair.a;
			final @NotNull Long idKurs = pair.b;
			final @NotNull GostBlockungKurs kurs1 = _parent.kursGet(idKurs);

			// (1)
			final @NotNull LongArrayKey keySperrung =
					new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs });
			final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
			if (regelSperrung != null)
				u.listEntfernen.add(regelSperrung);

			// (2) (3)
			for (final @NotNull GostBlockungKurs kurs2 : _parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
				final @NotNull LongArrayKey keyFixierung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id });
				final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);

				if (kurs1.id == kurs2.id) {
					if (regelFixierung == null) {
						// (2)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs));
					}
				} else {
					if (regelFixierung != null) {
						// (3)
						u.listEntfernen.add(regelFixierung);
					}
				}
			}
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu setzen.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs gesperrt ist, wird er gesperrt.
	 *
	 * @param setSchuelerID  Die Menge Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(final @NotNull Set<Long> setSchuelerID,
			final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID) {
				// (1)
				final @NotNull LongArrayKey keyFixierung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
				final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if (regelFixierung != null)
					u.listEntfernen.add(regelFixierung);
				// (2)
				final @NotNull LongArrayKey keySperrung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs });
				final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung == null)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs));
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu sperren.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs gesperrt ist, wird er gesperrt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu sperren.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateCreate_05b_SCHUELER_VERBIETEN_IN_DEN_KURSEN(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1) (2)
		for (final long idKurs : setKursID)
			GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs)));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Allein-Zuordnung zu setzen.
	 * <br>(1) Alle Regeln der selben Kursart werden zunächst entfernt, da zwei solcher Regeln sich widersprechen würden.
	 * <br>(2) Wenn eine Kursart-Fixierung im falschen Bereich liegt, wird die Fixierung entfernt.
	 * <br>(3) Wenn eine Kursart-Sperrung im falschen Bereich liegt, wird die Sperrung entfernt.
	 * <br>(4) Zuletzt wird die Regel neu erzeugt und hinzugefügt.
	 *
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Allein-Zuordnung zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(final int kursart, final int schienenNrVon,
			final int schienenNrBis) {
		final int von = Math.min(schienenNrVon, schienenNrBis);
		final int bis = Math.max(schienenNrVon, schienenNrBis);
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		for (final @NotNull GostBlockungRegel rGleicheKursart : _parent
				.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			if (kursart == rGleicheKursart.parameter.get(0))
				u.listEntfernen.add(rGleicheKursart);

		// (2) (3)
		for (final @NotNull GostBlockungsergebnisKurs kurs : getKursmenge())
			for (int schienenNr = 1; schienenNr <= _parent.schieneGetAnzahl(); schienenNr++) {
				final boolean imSchienenBereich = (von <= schienenNr) && (schienenNr <= bis);
				final boolean richtigeKursart = (kurs.kursart == kursart);
				if (imSchienenBereich != richtigeKursart) { // Kursart ist im falschen Bereich.
					// (2)
					final @NotNull LongArrayKey kFixierung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr });
					final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung != null)
						u.listEntfernen.add(rFixierung);
					// (3)
					final @NotNull LongArrayKey kSperrung =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr });
					final GostBlockungRegel rSperrung = _parent.regelGetByLongArrayKeyOrNull(kSperrung);
					if (rSperrung != null)
						u.listEntfernen.add(rSperrung);
				}
			}

		// (4)
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ, kursart, von, bis));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B zusammen (Regel 8) sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel bereits existiert, aber die IDs nicht aufsteigend sind, wird die (MaxKursID, MinKursID)-Kombination entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_07_KURS_VERBIETEN_MIT_KURS(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyZusammen12 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2 });
					final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 != null)
						u.listEntfernen.add(regelZusammen12);
					// (1b)
					final @NotNull LongArrayKey keyZusammen21 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1 });
					final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 != null)
						u.listEntfernen.add(regelZusammen21);
					// (2)
					final @NotNull LongArrayKey keyVerboten21 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1 });
					final GostBlockungRegel regelVerboten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 != null)
						u.listEntfernen.add(regelVerboten21);
					// (3)
					final @NotNull LongArrayKey keyVerboten12 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2 });
					final GostBlockungRegel regelVerboten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2));
				}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B verboten (Regel 7) sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel bereits existiert, aber die IDs nicht aufsteigend sind, wird die (MaxKursID, MinKursID)-Kombination entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_08_KURS_ZUSAMMEN_MIT_KURS(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyVerboten12 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2 });
					final GostBlockungRegel regelVerboten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 != null)
						u.listEntfernen.add(regelVerboten12);
					// (1b)
					final @NotNull LongArrayKey keyVerboten21 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1 });
					final GostBlockungRegel regelVerboten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 != null)
						u.listEntfernen.add(regelVerboten21);
					// (2)
					final @NotNull LongArrayKey keyZusammen21 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1 });
					final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 != null)
						u.listEntfernen.add(regelZusammen21);
					// (3)
					final @NotNull LongArrayKey keyZusammen12 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2 });
					final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2));
				}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Anzahl der Dummy-Schüler eines Kurses zu setzen.
	 * <br>(1) Wenn die Regel bereits existiert, wird sie (zunächst) entfernt.
	 * <br>(2) Wenn danach die Anzahl einen Wert größer 0 hat, wird die Regel hinzugefügt.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 * @param anzahl  Die Anzahl an Dummy-Schülern.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Anzahl der Dummy-Schüler eines Kurses zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN(final long idKurs, final int anzahl) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		for (final GostBlockungRegel rAlt : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (idKurs == rAlt.parameter.get(0))
				u.listEntfernen.add(rAlt);

		// (2)
		if (anzahl > 0)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, idKurs, anzahl));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um "Lehrkräfte beachten" zu aktivieren/deaktivieren.
	 * <br>(1) Wenn erstellen==FALSE und die Regel existiert, wird sie entfernt.
	 * <br>(2) Wenn erstellen==TRUE und die Regel existiert nicht, wird sie erzeugt.
	 *
	 * @param erstellen  Falls TRUE, wird die Regel aktiviert, andernfalls deaktiviert.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um "Lehrkräfte beachten" zu aktivieren/deaktivieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_10_LEHRKRAEFTE_BEACHTEN(final boolean erstellen) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		final @NotNull LongArrayKey keyDummyAlt = new LongArrayKey(new long[] { GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ });
		final GostBlockungRegel regelDummyAlt = _parent.regelGetByLongArrayKeyOrNull(keyDummyAlt);

		// (1)
		if ((!erstellen) && (regelDummyAlt != null))
			u.listEntfernen.add(regelDummyAlt);

		// (2)
		if ((erstellen) && (regelDummyAlt == null))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel0(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zusammen zu setzen.
	 * <br>(1) Wenn beide Schüler-IDs identisch sind, wird die Regel ignoriert.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Verbieten-Regel gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Verbieten-Regel gibt, wird diese entfernt.
	 * <br>(4) Wenn es eine Schüler-Schüler-Zusammen-Regel gibt, wird diese entfernt.
	 * <br>(5a) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel in falscher Schüler-ID-Reihenfolge gibt, wird sie entfernt.
	 * <br>(5b) Wenn es keine Schüler-Schüler-Fach-Zusammen-Regel gibt, wird sie hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zusammen zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(final long idSchueler1, final long idSchueler2,
			final long idFach) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idS1 = Math.min(idSchueler1, idSchueler2);
		final long idS2 = Math.max(idSchueler1, idSchueler2);

		// (1)
		if (idS1 == idS2)
			return u;

		// (2a)
		final @NotNull LongArrayKey keyVerbietenFach12 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach });
		final GostBlockungRegel regelVerbietenFach12 = _parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach12);
		if (regelVerbietenFach12 != null)
			u.listEntfernen.add(regelVerbietenFach12);

		// (2b)
		final @NotNull LongArrayKey keyVerbietenFach21 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach });
		final GostBlockungRegel regelVerbietenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach21);
		if (regelVerbietenFach21 != null)
			u.listEntfernen.add(regelVerbietenFach21);

		// (3a)
		final @NotNull LongArrayKey keyVerbieten12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2 });
		final GostBlockungRegel regelVerbieten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten12);
		if (regelVerbieten12 != null)
			u.listEntfernen.add(regelVerbieten12);

		// (3b)
		final @NotNull LongArrayKey keyVerbieten21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1 });
		final GostBlockungRegel regelVerbieten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten21);
		if (regelVerbieten21 != null)
			u.listEntfernen.add(regelVerbieten21);

		// (4a)
		final @NotNull LongArrayKey keyZusammen12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2 });
		final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
		if (regelZusammen12 != null)
			u.listEntfernen.add(regelZusammen12);

		// (4b)
		final @NotNull LongArrayKey keyZusammen21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1 });
		final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
		if (regelZusammen21 != null)
			u.listEntfernen.add(regelZusammen21);

		// (5a)
		final @NotNull LongArrayKey keyZusammenFach21 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach });
		final GostBlockungRegel regelZusammenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammenFach21);
		if (regelZusammenFach21 != null)
			u.listEntfernen.add(regelZusammenFach21);

		// (5b)
		final @NotNull LongArrayKey keyZusammenFach12 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach });
		final GostBlockungRegel regelZusammenFach12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammenFach12);
		if (regelZusammenFach12 == null)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zu verbieten.
	 * <br>(1) Wenn beide Schüler-IDs identisch sind, wird die Regel ignoriert.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Zusammen-Regel gibt, wird diese entfernt.
	 * <br>(4) Wenn es eine Schüler-Schüler-Verboten-Regel gibt, wird diese entfernt.
	 * <br>(5a) Wenn es eine Schüler-Schüler-Fach-Verboten-Regel in falscher Schüler-ID-Reihenfolge gibt, wird sie entfernt.
	 * <br>(5b) Wenn es keine Schüler-Schüler-Fach-Verboten-Regel gibt, wird sie hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zu verbieten.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(final long idSchueler1, final long idSchueler2,
			final long idFach) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idS1 = Math.min(idSchueler1, idSchueler2);
		final long idS2 = Math.max(idSchueler1, idSchueler2);

		// (1)
		if (idS1 == idS2)
			return u;

		// (2a)
		final @NotNull LongArrayKey keyZusammenFach12 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach });
		final GostBlockungRegel regelZusammenFach12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammenFach12);
		if (regelZusammenFach12 != null)
			u.listEntfernen.add(regelZusammenFach12);

		// (2b)
		final @NotNull LongArrayKey keyZusammenFach21 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach });
		final GostBlockungRegel regelZusammenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammenFach21);
		if (regelZusammenFach21 != null)
			u.listEntfernen.add(regelZusammenFach21);

		// (3a)
		final @NotNull LongArrayKey keyZusammen12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2 });
		final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
		if (regelZusammen12 != null)
			u.listEntfernen.add(regelZusammen12);

		// (3b)
		final @NotNull LongArrayKey keyZusammen21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1 });
		final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
		if (regelZusammen21 != null)
			u.listEntfernen.add(regelZusammen21);

		// (4a)
		final @NotNull LongArrayKey keyVerbieten12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2 });
		final GostBlockungRegel regelVerbieten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten12);
		if (regelVerbieten12 != null)
			u.listEntfernen.add(regelVerbieten12);

		// (4b)
		final @NotNull LongArrayKey keyVerbieten21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1 });
		final GostBlockungRegel regelVerbieten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten21);
		if (regelVerbieten21 != null)
			u.listEntfernen.add(regelVerbieten21);

		// (5a)
		final @NotNull LongArrayKey keyVerbietenFach21 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach });
		final GostBlockungRegel regelVerbietenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach21);
		if (regelVerbietenFach21 != null)
			u.listEntfernen.add(regelVerbietenFach21);

		// (5b)
		final @NotNull LongArrayKey keyVerbietenFach12 =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach });
		final GostBlockungRegel regelVerbietenFach12 = _parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach12);
		if (regelVerbietenFach12 == null)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zusammen zu setzen.
	 * <br>(1) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt (aber später hinzugefügt).
	 * <br>(4) Wenn es eine Schüler-Schüler-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(5) Wenn die Schüler-IDs gültig sind, wird nun die Schüler-Schüler-Zusammen-Regel hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zusammen zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_13_SCHUELER_ZUSAMMEN_MIT_SCHUELER(final long idSchueler1, final long idSchueler2) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idS1 = Math.min(idSchueler1, idSchueler2);
		final long idS2 = Math.max(idSchueler1, idSchueler2);

		// (1)
		for (final @NotNull GostBlockungRegel r11 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r11.parameter.get(0), r11.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r11);

		// (2)
		for (final @NotNull GostBlockungRegel r12 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r12.parameter.get(0), r12.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r12);

		// (3)
		for (final @NotNull GostBlockungRegel r13 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r13.parameter.get(0), r13.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r13);

		// (4)
		for (final @NotNull GostBlockungRegel r14 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r14.parameter.get(0), r14.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r14);

		// (5)
		if ((0 <= idS1) && (idS1 < idS2))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zu verbieten.
	 * <br>(1) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(4) Wenn es eine Schüler-Schüler-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt (aber später hinzugefügt).
	 * <br>(5) Wenn die Schüler-IDs gültig sind, wird nun die Schüler-Schüler-Verbieten-Regel hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zu verbieten.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_14_SCHUELER_VERBIETEN_MIT_SCHUELER(final long idSchueler1, final long idSchueler2) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idS1 = Math.min(idSchueler1, idSchueler2);
		final long idS2 = Math.max(idSchueler1, idSchueler2);

		// (1)
		for (final @NotNull GostBlockungRegel r11 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r11.parameter.get(0), r11.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r11);

		// (2)
		for (final @NotNull GostBlockungRegel r12 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r12.parameter.get(0), r12.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r12);

		// (3)
		for (final @NotNull GostBlockungRegel r13 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r13.parameter.get(0), r13.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r13);

		// (4)
		for (final @NotNull GostBlockungRegel r14 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r14.parameter.get(0), r14.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r14);

		// (5)
		if ((0 <= idS1) && (idS1 < idS2))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Schülern eines Kurses zu setzen.
	 * <br>(1) Wenn die Regel bereits existiert, wird sie (zunächst) entfernt.
	 * <br>(2) Wenn danach die Anzahl einen Wert im Intervall [0;99] hat, wird die Regel hinzugefügt.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 * @param anzahl  Die maximale Anzahl an SuS des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Schülern eines Kurses zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_15_KURS_MAXIMALE_SCHUELERANZAHL(final long idKurs, final int anzahl) {
		// TODO BAR Sind Dummy-SuS im Algorithmus inklusive?
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		for (final GostBlockungRegel rAlt : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL))
			if (idKurs == rAlt.parameter.get(0))
				u.listEntfernen.add(rAlt);

		// (2)
		if ((anzahl >= 0) && (anzahl <= 99))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, idKurs, anzahl));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermenge beim Blocken zu ignorieren.
	 * <br>(1) Wenn diese Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermenge beim Blocken zu ignorieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_16_SCHUELER_IGNORIEREN(final @NotNull Set<Long> setSchuelerID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID) {
			final @NotNull LongArrayKey keySchuelerIgnorieren = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler });
			final GostBlockungRegel regelSchuelerIgnorieren = _parent.regelGetByLongArrayKeyOrNull(keySchuelerIgnorieren);

			// (1)
			if (regelSchuelerIgnorieren == null)
				u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel1(GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler));
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge von Kursdifferenz-Berechnungen auszuschließen.
	 * <br>(1) Wenn diese Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge von Kursdifferenz-Berechnungen auszuschließen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_17_KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			final @NotNull LongArrayKey keyKurs_KD_ignorieren =
					new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs });
			final GostBlockungRegel regelKurs_KD_ignorieren = _parent.regelGetByLongArrayKeyOrNull(keyKurs_KD_ignorieren);

			// (1)
			if (regelKurs_KD_ignorieren == null)
				u.listHinzuzufuegen
						.add(DTOUtils.newGostBlockungRegel1(GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs));
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Kursen einer bestimmten Fachart (in jeder Schiene) zu begrenzen.
	 * <br>(1) Falls die Regel bereits existiert, wird sie zunächst entfernt.
	 * <br>(2) Falls der "maximal"-Wert 1 ist und es Regeln KURS_VERBIETEN_MIT_KURS bei Paaren dieser Fachart gibt, werden die Regeln entfernt.
	 * <br>(3) Zuletzt wird die neue Regel hinzugefügt.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 * @param maximal    Die maximale Kursanzahl dieser Fachart (in jeder Schiene). Gültige Werte sind 1 bis 9.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Kursen einer bestimmten Fachart (in jeder Schiene) zu begrenzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(final long idFach, final int idKursart,
			final int maximal) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		for (final @NotNull GostBlockungRegel r18 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE))
			if ((r18.parameter.get(0) == idFach) && (r18.parameter.get(1) == idKursart))
				u.listEntfernen.add(r18);

		// (2)
		if (maximal == 1)
			for (final @NotNull GostBlockungRegel r7 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS)) {
				final long idKurs1 = r7.parameter.get(0);
				final long idKurs2 = r7.parameter.get(1);
				final @NotNull GostBlockungsergebnisKurs kurs1 = getKursE(idKurs1);
				final @NotNull GostBlockungsergebnisKurs kurs2 = getKursE(idKurs2);
				if ((kurs1.fachID == idFach) && (kurs2.fachID == idFach) && (kurs1.kursart == idKursart) && (kurs2.kursart == idKursart))
					u.listEntfernen.add(r7);
			}

		// (3)
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ,
				idFach, idKursart, maximal));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Regeln einer Schülermenge zu entfernen.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Regeln einer Schülermenge zu entfernen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_19_SCHUELERMENGE_ENTFERNEN(final @NotNull Set<Long> setSchuelerID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final GostBlockungRegel regel : _parent.regelGetListe()) {
			final GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
			for (int i = 0; i < typ.getParamCount(); i++)
				if ((typ.getParamType(i) == GostKursblockungRegelParameterTyp.SCHUELER_ID) && setSchuelerID.contains(regel.parameter.get(i)))
					u.listEntfernen.add(regel);
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt     Die ID der alten zu modifizierenden Regel.
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_01_KURSART_SPERRE_SCHIENEN_VON_BIS(final long idRegelAlt, final int kursart,
			final int schienenNrVon, final int schienenNrBis) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final int von = Math.min(schienenNrVon, schienenNrBis);
		final int bis = Math.max(schienenNrVon, schienenNrBis);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_01_KURSART_SPERRE_SCHIENEN_VON_BIS(kursart, von, bis));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param schienenNr  Die Nummer der Schiene, die fixiert werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_02e_KURS_FIXIERE_IN_EINER_SCHIENE(final long idRegelAlt, final long idKurs,
			final int schienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_02e_helper(idKurs, schienenNr, false));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt     Die ID der alten zu modifizierenden Regel.
	 * @param idKurs         Die ID des Kurses.
	 * @param schienenNr     Die Nummer der Schiene.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_03_KURS_SPERRE_IN_SCHIENE(final long idRegelAlt, final long idKurs, final int schienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_03_KURS_SPERRE_IN_SCHIENE(SetUtils.create1(idKurs), SetUtils.create1(schienenNr)));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler  Die ID des Schülers.
	 * @param idKurs      Die ID des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_04_SCHUELER_FIXIEREN_IN_KURS(final long idRegelAlt, final long idSchueler, final long idKurs) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(idSchueler, idKurs));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler  Die ID des Schülers.
	 * @param idKurs      Die ID des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_05_SCHUELER_VERBIETEN_IN_KURS(final long idRegelAlt, final long idSchueler,
			final long idKurs) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(SetUtils.create1(idSchueler), SetUtils.create1(idKurs)));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt     Die ID der alten zu modifizierenden Regel.
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(final long idRegelAlt, final int kursart,
			final int schienenNrVon, final int schienenNrBis) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final int von = Math.min(schienenNrVon, schienenNrBis);
		final int bis = Math.max(schienenNrVon, schienenNrBis);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ, kursart, von, bis });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(kursart, von, bis));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs1     Die ID des 1. Kurses.
	 * @param idKurs2     Die ID des 2. Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_07_KURS_VERBIETEN_MIT_KURS(final long idRegelAlt, final long idKurs1, final long idKurs2) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idKursMin = Math.min(idKurs1, idKurs2);
		final long idKursMax = Math.max(idKurs1, idKurs2);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKursMin, idKursMax });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_07_KURS_VERBIETEN_MIT_KURS(SetUtils.create2(idKursMin, idKursMax)));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs1     Die ID des 1. Kurses.
	 * @param idKurs2     Die ID des 2. Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_08_KURS_ZUSAMMEN_MIT_KURS(final long idRegelAlt, final long idKurs1, final long idKurs2) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idKursMin = Math.min(idKurs1, idKurs2);
		final long idKursMax = Math.max(idKurs1, idKurs2);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKursMin, idKursMax });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_08_KURS_ZUSAMMEN_MIT_KURS(SetUtils.create2(idKursMin, idKursMax)));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs      Die ID des Kurses.
	 * @param anzahl      Die Anzahl an Dummy-SuS des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN(final long idRegelAlt, final long idKurs, final int anzahl) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, idKurs, anzahl });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKurs, anzahl));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	// regelupdatePatchByID_10 ist nicht nötig, da die Create-Anweisung das bereits beinhaltet.

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(final long idRegelAlt, final long idSchueler1,
			final long idSchueler2, final long idFach) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idSchuelerMin = Math.min(idSchueler1, idSchueler2);
		final long idSchuelerMax = Math.max(idSchueler1, idSchueler2);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idSchuelerMin, idSchuelerMax, idFach });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(idSchuelerMin, idSchuelerMax, idFach));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(final long idRegelAlt, final long idSchueler1,
			final long idSchueler2, final long idFach) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idSchuelerMin = Math.min(idSchueler1, idSchueler2);
		final long idSchuelerMax = Math.max(idSchueler1, idSchueler2);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idSchuelerMin, idSchuelerMax, idFach });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(idSchuelerMin, idSchuelerMax, idFach));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_13_SCHUELER_ZUSAMMEN_MIT_SCHUELER(final long idRegelAlt, final long idSchueler1,
			final long idSchueler2) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idSchuelerMin = Math.min(idSchueler1, idSchueler2);
		final long idSchuelerMax = Math.max(idSchueler1, idSchueler2);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idSchuelerMin, idSchuelerMax });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_13_SCHUELER_ZUSAMMEN_MIT_SCHUELER(idSchuelerMin, idSchuelerMax));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_14_SCHUELER_VERBIETEN_MIT_SCHUELER(final long idRegelAlt, final long idSchueler1,
			final long idSchueler2) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idSchuelerMin = Math.min(idSchueler1, idSchueler2);
		final long idSchuelerMax = Math.max(idSchueler1, idSchueler2);

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idSchuelerMin, idSchuelerMax });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_14_SCHUELER_VERBIETEN_MIT_SCHUELER(idSchuelerMin, idSchuelerMax));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs      Die ID des Kurses.
	 * @param anzahl      Die maximale Anzahl der SuS des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_15_KURS_MAXIMALE_SCHUELERANZAHL(final long idRegelAlt, final long idKurs, final int anzahl) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, idKurs, anzahl });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_15_KURS_MAXIMALE_SCHUELERANZAHL(idKurs, anzahl));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, passiert nichts.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler  Die ID des Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_16_SCHUELER_IGNORIEREN(final long idRegelAlt, final long idSchueler) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_16_SCHUELER_IGNORIEREN(SetUtils.create1(idSchueler)));
		// Falls die Regel bereits durch Kaskaden gelöscht wurde, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Regel von einem zu einem anderen Kurs zu patchen.
	 *
	 * <br>(1) Wenn die alte Regel nicht existiert, passiert nichts.
	 * <br>(2) Wenn die neue Kurs-ID bereits existiert, passiert nichts.
	 * <br>(3) Wenn die alte Kurs-ID der neuen Kurs-ID gleicht, passiert nichts.
	 * <br>(4) Andernfalls wird die alte Regel gelöscht (idKursAlt) und eine neue Regel wird erzeugt (idKursNeu).
	 *
	 * @param idRegelAlt  Die Regel, die modifiziert wird.
	 * @param idKursNeu   Die neue Kurs-ID deren Regel hinzugefügt werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Regel von einem zu einem anderen Kurs zu patchen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdatePatchByID_17_KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(final long idRegelAlt,
			final long idKursNeu) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull GostBlockungRegel rAlt = _parent.regelGet(idRegelAlt);
		if (rAlt.typ != GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ)
			return u;

		// (2)
		final @NotNull LongArrayKey kNeu =
				new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKursNeu });
		final GostBlockungRegel rNeu = _parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu != null)
			return u;

		// (3)
		final long idKursAlt = rAlt.parameter.get(0);
		if (idKursAlt == idKursNeu)
			return u;

		// (4)
		GostBlockungsergebnisManager.regelupdateAppend(u, regelupdateCreate_17_KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(SetUtils.create1(idKursNeu)));
		// Falls die Regel bereits durch Kaskaden gelöscht werden soll, dann entferne sie nicht doppelt.
		if (!u.listEntfernen.contains(rAlt))
			u.listEntfernen.add(rAlt);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Fixierung zu lösen.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich liegt und bereits fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Fixierung zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(final @NotNull Set<Long> setKursID,
			final @NotNull Set<Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// Beim Fixieren muss man über die Kurse und dann über die Schienen des Kurses iterieren.
		for (final long idKurs : setKursID)
			for (final @NotNull GostBlockungsergebnisSchiene schieneE : DeveloperNotificationException.ifMapGetIsNull(_kursID_to_schienenSet, idKurs)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					// (1)
					final @NotNull LongArrayKey keyKursInSchiene =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer });
					final GostBlockungRegel regel = _parent.regelGetByLongArrayKeyOrNull(keyKursInSchiene);
					if (regel != null)
						u.listEntfernen.add(regel);
				}
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Fixierung einer Kursmenge komplett zu lösen.
	 * <br>(1) Alle Fixierungen der Kursmenge werden gelöst.
	 *
	 * @param setKursID  Die Kursmenge, deren Fixierungen gelöst werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Fixierung einer Kursmenge komplett zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (int nr = 1; nr <= _schienenNR_to_schiene.size(); nr++) {
				// (1)
				final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr });
				final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
				if (rFixierung != null)
					u.listEntfernen.add(rFixierung);
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_02c_KURS_FIXIERE_ALLE_IN_IHREN_SCHIENEN() {
		return regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(_parent.kursmengeGetSetDerIDs());
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung zu lösen.
	 * <br>(1) Wenn der Kurs in der Schiene fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param schienenNr  Die Nummer der Schiene, die gelöst werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_02e_KURS_FIXIERE_IN_EINER_SCHIENE(final long idKurs, final int schienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr });
		final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
		if (rFixierung != null)
			u.listEntfernen.add(rFixierung);

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu lösen.
	 * <br>(1) Wenn der Kurs in dem Schienen-Bereich gesperrt ist, wird die Sperrung entfernt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_03_KURS_SPERRE_IN_SCHIENE(final @NotNull Set<Long> setKursID,
			final @NotNull Set<Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (final int schienenNr : setSchienenNr) {
				// (1)
				final @NotNull LongArrayKey keyGesperrt =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr });
				final GostBlockungRegel regelGesperrt = _parent.regelGetByLongArrayKeyOrNull(keyGesperrt);
				if (regelGesperrt != null)
					u.listEntfernen.add(regelGesperrt);
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(final @NotNull Set<Long> setSchuelerID,
			final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID) {
				// (1)
				final @NotNull LongArrayKey keyFixierung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
				final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if (regelFixierung != null)
					u.listEntfernen.add(regelFixierung);
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1)
			final @NotNull GostBlockungRegelUpdate u2 =
					regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen zu lösen.
	 * <br>Die Methode delegiert alles an {@link #regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS}.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_04c_SCHUELER_FIXIEREN_IN_ALLEN_KURSEN() {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		u.listEntfernen.addAll(_parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um ein Schüler-Kurs-Fixierung-Toggle zu realisieren.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 * <br>(2) Wenn der Schüler im Kurs nicht fixiert ist, wird die Fixierung gesetzt.
	 * <br>(3) Potentielle Fixierungen in Nachbar-Kursen werden entfernt, da sie in jedem Fall falsch sind.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um ein Schüler-Kurs-Fixierung-Toggle zu realisieren.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_04d_SCHUELER_FIXIEREN_IN_DEN_KURSEN_TOGGLE(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs1)) {
				final @NotNull LongArrayKey kFixierung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs1 });
				final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
				if (rFixierung != null) {
					// (1)
					u.listEntfernen.add(rFixierung);
				} else {
					// (2)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs1));
				}

				// (3)
				final @NotNull GostBlockungKurs kurs1 = _parent.kursGet(idKurs1);
				for (final @NotNull GostBlockungKurs kurs2 : _parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart))
					if (kurs1.id != kurs2.id) {
						final @NotNull LongArrayKey kFixierung2 =
								new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id });
						final GostBlockungRegel rFixierung2 = _parent.regelGetByLongArrayKeyOrNull(kFixierung2);
						if (rFixierung2 != null)
							u.listEntfernen.add(rFixierung2);
					}
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird die Sperrung entfernt.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu lösen.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateRemove_05_SCHUELER_VERBIETEN_IN_KURS(final @NotNull Set<Long> setSchuelerID,
			final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID) {
				// (1)
				final @NotNull LongArrayKey keySperrung =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs });
				final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung != null)
					u.listEntfernen.add(regelSperrung);
			}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird die Sperrung entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu lösen.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateRemove_05b_SCHUELER_VERBIETEN_IN_DEN_KURSEN(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1)
			final @NotNull GostBlockungRegelUpdate u2 =
					regelupdateRemove_05_SCHUELER_VERBIETEN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}

		return u;
	}


	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu lösen.
	 * <br>(1) Wenn das Kurs-Kurs-Verbot existiert (in beliebiger Permutation), wird es entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu lösen.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateRemove_07_KURS_VERBIETEN_MIT_KURS(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyVerboten12 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2 });
					final GostBlockungRegel regelVerboten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 != null)
						u.listEntfernen.add(regelVerboten12);
					// (1b)
					final @NotNull LongArrayKey keyVerboten21 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1 });
					final GostBlockungRegel regelVerboten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 != null)
						u.listEntfernen.add(regelVerboten21);
				}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Gebote von setKursID (alle Paarungen) zu lösen.
	 * <br>(1) Wenn das Kurs-Kurs-Gebot existiert (in beliebiger Permutation), wird es entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Gebote von setKursID (alle Paarungen) zu lösen.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateRemove_08_KURS_ZUSAMMEN_MIT_KURS(final @NotNull Set<Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyZusammen12 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2 });
					final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 != null)
						u.listEntfernen.add(regelZusammen12);
					// (1b)
					final @NotNull LongArrayKey keyZusammen21 =
							new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1 });
					final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 != null)
						u.listEntfernen.add(regelZusammen21);
				}

		return u;
	}

	/**
	 * Entfernt erst alle Regeln aus {@link GostBlockungRegelUpdate#listEntfernen} und
	 * fügt dann die neuen Regeln aus {@link GostBlockungRegelUpdate#listHinzuzufuegen} hinzu.
	 *
	 * @param update  Das {@link GostBlockungRegelUpdate}-Objekt.
	 */
	public void regelupdateExecute(final @NotNull GostBlockungRegelUpdate update) {
		_parent.regelRemoveListe(update.listEntfernen);
		_parent.regelAddListe(update.listHinzuzufuegen);
		stateRevalidateEverything();
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus den derzeit zugeordneten Kursen zu entfernen.
	 * <br>(1) Wenn ein Schüler in einem Kurs ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn ein Schüler in einem Kurs ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist.
	 *
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus den derzeit zugeordneten Kursen zu entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_01_LEERE_ALLE_KURSE(final boolean entferneAuchFixierte) {
		return kursSchuelerUpdate_01b_LEERE_KURSMENGE(_kursIDset, entferneAuchFixierte);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus der übergebenen Kursmenge zu entfernen.
	 * <br>(1) Wenn ein Schüler in einem Kurs ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn ein Schüler in einem Kurs ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist.
	 *
	 * @param kursIDs               Die Menge der Kurse.
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus der übergebenen Kursmenge zu entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_01b_LEERE_KURSMENGE(final @NotNull Set<Long> kursIDs,
			final boolean entferneAuchFixierte) {
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (entferneAuchFixierte || !_parent.schuelerGetIstFixiertInKurs(idSchueler, idKurs)) {
					// (1) (2)
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus einem Kurs zu entfernen.
	 * <br>(1) Wenn der Schüler dem Kurs zugeordnet ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn der Schüler dem Kurs zugeordnet ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist. Auch die Fixierungs-Regel wird entfernt.
	 *
	 * @param schuelerIDs           Die Menge der Schüler-IDs.
	 * @param idKurs                Die Datenbank-ID des Kurses aus dem die Schüler entfernt werden sollen.
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus einem Kurs zu entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_02a_ENTFERNE_SCHUELERMENGE_AUS_KURS(
			final @NotNull Set<Long> schuelerIDs, final long idKurs, final boolean entferneAuchFixierte) {
		// TODO ungültige Zuordnungen überprüfen.
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		final @NotNull Set<Long> setSchulerOfKurs = getOfKursSchuelerIDmenge(idKurs);
		for (final long idSchueler : schuelerIDs) {
			// Ist der Schüler überhaupt dem Kurs zugeordnet?
			if (!setSchulerOfKurs.contains(idSchueler))
				continue;

			final @NotNull LongArrayKey keyFixiert =
					new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs });
			final GostBlockungRegel regelFixiert = _parent.regelGetByLongArrayKeyOrNull(keyFixiert);

			if (regelFixiert == null) {
				// (1)
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				continue;
			}

			if (entferneAuchFixierte) {
				// (2)
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				u.regelUpdates.listEntfernen.add(regelFixiert);
			}
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus allen Kursen entfernen.
	 * <br>Hinweis: Es werden keine Kurs-Fixierungs-Regeln entfernt. Dafür muss die zugehörige regelupdate-Methode aufgerufen werden.
	 *
	 * @param schuelerIDs  Die Menge der Schüler-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus allen Kursen entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_02b_ENTFERNE_SCHUELERMENGE_AUS_ALLEN_KURSEN(final @NotNull Set<Long> schuelerIDs) {
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final long idSchueler : schuelerIDs)
			for (final GostBlockungsergebnisKurs kurs : getOfSchuelerKursmenge(idSchueler))
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs.id, idSchueler));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 * <br>(1) Wenn der Schüler nicht im Kurs ist, wird er hinzugefügt.
	 * <br>(2) Wenn der Schüler in einem Nachbar-Kurs ist, wird er entfernt.
	 *
	 * <br>Hinweis: Wenn der Schüler den Kurs gar nicht gewählt hat, wird dies trotzdem erlaubt. Denn bei einer Umwahl, kann eine Kurszuordnung ungültig sein!
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche hinzugefügt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(
			final @NotNull Set<GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen) {
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : kursSchuelerZuordnungen) {
			final @NotNull GostBlockungKurs kurs1 = _parent.kursGet(z.idKurs);
			// (1)
			if (!getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				u.listHinzuzufuegen.add(z);

			// (2)
			for (final @NotNull GostBlockungKurs kurs2 : _parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart))
				if ((kurs1.id != kurs2.id) && (getOfSchuelerOfKursIstZugeordnet(z.idSchueler, kurs2.id)))
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs2.id, z.idSchueler));
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 * <br>(1) Wenn der Schüler den Ziel-Kurs nicht wählen darf (falsche Fachwahlen), dann passiert nichts.
	 * <br>(2) Wenn der Schüler aus einem fixierten Kurs verschoben werden soll, dies aber nicht erlaubt ist, dann passiert nichts.
	 * <br>(3) Der Schüler ggf. aus einem alten Kurs entfernt und die Fixier-Regel des alten Kurses wird ggf. entfernt.
	 * <br>(4) Der Schüler wird einem neuen Kurs hinzugefügt und wird ggf. im neuen Kurs fixiert.
	 *
	 * @param kursSchuelerZuordnungen           Alle Kurs-Schüler-Paare, welche hinzugefügt werden sollen.
	 * @param verschiebeFixierteDesQuellkurses  TRUE, dann werden fixierte SuS aus potentiell alten Kursen entfernt.
	 * @param fixiereImZielkurs                 TRUE, dann werden die SuS im Zielkurs fixiert.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 */
	private @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_03a_VERSCHIEBE_SCHUELER_ZU_KURSEN(
			final @NotNull Set<GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen, final boolean verschiebeFixierteDesQuellkurses,
			final boolean fixiereImZielkurs) {
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		// Der Sonderfall von Kurs X zu Kurs X funktioniert auch, da zuerst die "listEntfernen" abgearbeitet wird und dann erst die "listHinzufuegen".

		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : kursSchuelerZuordnungen) {
			final @NotNull GostBlockungKurs kursNeu = _parent.kursGet(z.idKurs);

			// (1)
			if (!getOfSchuelerHatFachwahl(z.idSchueler, kursNeu.fach_id, kursNeu.kursart))
				continue;

			final GostBlockungsergebnisKurs kursAlt = getOfSchuelerOfFachZugeordneterKurs(z.idSchueler, kursNeu.fach_id);
			if (kursAlt != null) {
				final @NotNull LongArrayKey keyFixiertAlt =
						new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, z.idSchueler, kursAlt.id });
				final GostBlockungRegel regelFixiertAlt = _parent.regelGetByLongArrayKeyOrNull(keyFixiertAlt);

				// (2)
				if ((regelFixiertAlt != null) && (!verschiebeFixierteDesQuellkurses))
					continue;

				// (3)
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursAlt.id, z.idSchueler));
				if (regelFixiertAlt != null)
					u.regelUpdates.listEntfernen.add(regelFixiertAlt);
			}

			// (4)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursNeu.id, z.idSchueler));
			if (fixiereImZielkurs)
				u.regelUpdates.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
						z.idSchueler, kursNeu.id));
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 * <br>(1) Wenn der Schüler im Kurs ist, wird er entfernt.
	 * <br>(2) Falls es Schüler-Kurs-Fixierungen gibt, werden diese entfernt.
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche entfernt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_03b_ENTFERNE_KURS_SCHUELER_PAARE(
			final @NotNull Set<GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen) {

		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : kursSchuelerZuordnungen) {
			// (1)
			if (getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				u.listEntfernen.add(z);

			// (2)
			final @NotNull LongArrayKey keyFixiertAlt =
					new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, z.idSchueler, z.idKurs });
			final GostBlockungRegel regelFixiertAlt = _parent.regelGetByLongArrayKeyOrNull(keyFixiertAlt);
			if (regelFixiertAlt != null)
				u.regelUpdates.listEntfernen.add(regelFixiertAlt);
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler-Kerngruppe auf mehrere Kurse zu verteilen.
	 * <br>(1) Die SuS werden entsprechende der Methode {@link #kursSchuelerUpdate_03a_VERSCHIEBE_SCHUELER_ZU_KURSEN} verschoben.
	 * <br>(2) Falls "zielKurseLeeren", werden alle SuS aus dem Ziel-Kurs entfernt, die nicht zur Kerngruppe gehören.
	 *
	 * @param idQuellKurs                       Der Quell-Kurs definiert die Kerngruppe.
	 * @param idZielKurse                       Alle Ziel-Kurs-Schülermengen werden der Kerngruppe angeglichen.
	 * @param verschiebeFixierteDesQuellkurses  Falls TRUE, dann werden Fixierungen im Quell-Kurs gelöst, andernfalls wird der Schüler nicht verschoben.
	 * @param inZielKursenFixieren              Falls TRUE, wird nach der Verschiebung der Schüler im Ziel-Kurs fixiert.
	 * @param zielKurseLeeren                   Falls TRUE, werden alle SuS aus dem Ziel-Kurs entfernt, die nicht zur Kerngruppe gehören.
	 *
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_04_BILDE_KERNGRUPPEN(final long idQuellKurs,
			final @NotNull Set<Long> idZielKurse, final boolean verschiebeFixierteDesQuellkurses, final boolean inZielKursenFixieren,
			final boolean zielKurseLeeren) {
		// Datenkonsistenz überprüfen.
		final @NotNull Set<Long> fachartSet = new HashSet<>();
		for (final long idZielKurs : idZielKurse) {
			final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idZielKurs);
			final long fachartID = GostKursart.getFachartID(kurs.fachID, kurs.kursart);
			if (!fachartSet.add(fachartID)) {
				final String sKursQuelle = _parent.toStringKursSimple(idQuellKurs);
				final String sFachartZiel = _parent.toStringFachartSimpleByFachartID(fachartID);
				throw new UserNotificationException(
						"Die Kerngruppe des Kurses " + sKursQuelle + " kann nicht auf zwei Kurse der Fachart " + sFachartZiel + " verteilt werden!");
			}
		}

		// (1) Verschieben
		final @NotNull Set<Long> idSchuelerKerngruppe = getOfKursSchuelerIDmenge(idQuellKurs);
		final @NotNull Set<GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen = new HashSet<>();
		for (final long idZielKurs : idZielKurse)
			for (final long idSchueler : idSchuelerKerngruppe)
				kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idZielKurs, idSchueler));
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u =
				kursSchuelerUpdate_03a_VERSCHIEBE_SCHUELER_ZU_KURSEN(kursSchuelerZuordnungen, verschiebeFixierteDesQuellkurses, inZielKursenFixieren);

		// (2) Lösche alle SuS aus dem Ziel-Kurs, die nicht zur Kerngruppe gehören.
		// Hinweis: S. werden aus dem Kurs potentiell entfernt, aber NICHT die zugehörigen Regeln,
		//          denn das Erkennen einer Regelverletzung ist hier wichtig.
		if (zielKurseLeeren)
			for (final long idZielKurs : idZielKurse)
				for (final long idSchueler : getOfKursSchuelerIDmenge(idZielKurs))
					if (!idSchuelerKerngruppe.contains(idSchueler))
						u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idZielKurs, idSchueler));

		return u;
	}

	/**
	 * Entfernt erst alle Regeln aus {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate#listEntfernen} und
	 * fügt dann die neuen Regeln aus {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate#listHinzuzufuegen} hinzu.
	 * Macht das selbe für die potentiell enthaltenen RegelUpdates.
	 *
	 * @param update  Das {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt.
	 */
	public void kursSchuelerUpdateExecute(final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate update) {
		// Regeln entfernen.
		_parent.regelRemoveListe(update.regelUpdates.listEntfernen);

		// SuS entfernen.
		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : update.listEntfernen)
			stateSchuelerKursEntfernenOhneRevalidierung(z.idSchueler, z.idKurs);

		// An dieser Stelle darf kein "stateRevalidateEverything".


		// SuS hinzufügen
		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : update.listHinzuzufuegen)
			stateSchuelerKursHinzufuegenOhneRevalidierung(z.idSchueler, z.idKurs);

		// Regeln hinzufügen.
		_parent.regelAddListe(update.regelUpdates.listHinzuzufuegen);

		stateRevalidateEverything();
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse in Schienen zu setzen.
	 * <br>(1) Wenn der Kurs nicht in der Schiene ist, wird er hinzugefügt.
	 *
	 * @param kursSchienenZuordnungen  Alle Kurs-Schienen-Paare, welche gesetzt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse in Schienen zu setzen.
	 */
	public @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(
			final @NotNull Set<GostBlockungsergebnisKursSchienenZuordnung> kursSchienenZuordnungen) {
		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate u = new GostBlockungsergebnisKursSchienenZuordnungUpdate();

		for (final @NotNull GostBlockungsergebnisKursSchienenZuordnung z : kursSchienenZuordnungen) {
			// (1)
			if (!getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene))
				u.listHinzuzufuegen.add(z);
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse aus Schienen zu entfernen.
	 * <br>(1) Wenn der Kurs nicht in der Schiene ist, wird er hinzugefügt.
	 *
	 * @param kursSchienenZuordnungen  Alle Kurs-Schienen-Paare, welche entfernt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse aus Schienen zu entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate kursSchienenUpdate_01b_ENTFERNE_KURS_SCHIENEN_PAARE(
			final @NotNull Set<GostBlockungsergebnisKursSchienenZuordnung> kursSchienenZuordnungen) {
		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate u = new GostBlockungsergebnisKursSchienenZuordnungUpdate();

		for (final @NotNull GostBlockungsergebnisKursSchienenZuordnung z : kursSchienenZuordnungen) {
			// (1)
			if (getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene))
				u.listEntfernen.add(z);
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um einen Kurs von einer Schiene zu einer anderen Schiene zu verschieben.
	 *
	 * <br>(1) Wenn der Kurs nicht in der Quell-Schiene ist, passiert nichts.
	 * <br>(2) Wenn der Kurs bereits in der Ziel-Schiene ist, passiert nichts.
	 * <br>(3) Andernfalls, wird der Kurs aus der Quell-Schiene entfernt und der Ziel-Schiene hinzugefügt.
	 *
	 *
	 * @param idKurs           Die Datenbank-ID des Kurses.
	 * @param idSchieneQuelle  Die Quell-Schiene, aus der der Kurs entfernt wird.
	 * @param idSchieneZiel    Die Ziel-Schiene, zu welcher der Kurs hinzugefügt wird.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um einen Kurs von einer Schiene zu einer anderen Schiene zu verschieben.
	 */
	public @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate kursSchienenUpdate_02a_VERSCHIEBE_KURS_VON_SCHIENE_NACH_SCHIENE(
			final long idKurs,
			final long idSchieneQuelle,
			final long idSchieneZiel) {

		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate u = new GostBlockungsergebnisKursSchienenZuordnungUpdate();

		// (1)
		if (!getOfKursOfSchieneIstZugeordnet(idKurs, idSchieneQuelle))
			return u;

		// (2)
		if (getOfKursOfSchieneIstZugeordnet(idKurs, idSchieneZiel))
			return u;

		u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchieneQuelle));
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchieneZiel));

		return u;
	}

	/**
	 * Entfernt erst alle Regeln aus {@link GostBlockungsergebnisKursSchienenZuordnungUpdate#listEntfernen} und
	 * fügt dann die neuen Regeln aus {@link GostBlockungsergebnisKursSchienenZuordnungUpdate#listHinzuzufuegen} hinzu.
	 * Macht das selbe für die potentiell enthaltenen RegelUpdates.
	 *
	 * @param update  Das {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt.
	 */
	public void kursSchienenUpdateExecute(final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate update) {
		// Regeln entfernen.
		_parent.regelRemoveListe(update.regelUpdates.listEntfernen);

		// Kurse aus Schienen entfernen.
		for (final @NotNull GostBlockungsergebnisKursSchienenZuordnung z : update.listEntfernen)
			stateKursSchieneEntfernenOhneRegelvalidierung(z.idKurs, z.idSchiene);

		// An dieser Stelle darf kein "stateRevalidateEverything", da sonst SuS aus den Kurses entfernt werden.

		// Kurse in Schienen setzen.
		for (final @NotNull GostBlockungsergebnisKursSchienenZuordnung z : update.listHinzuzufuegen)
			stateKursSchieneHinzufuegenOhneRegelvalidierung(z.idKurs, z.idSchiene);

		// Regeln hinzufügen.
		_parent.regelAddListe(update.regelUpdates.listHinzuzufuegen);

		stateRevalidateEverything();
	}

	// #########################################################################
	// ##########       Anfragen bezüglich einer Schiene.             ##########
	// #########################################################################

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungSchiene}-Objekt.<br>
	 * Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.<br>
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public @NotNull GostBlockungSchiene getSchieneG(final long idSchiene) throws DeveloperNotificationException {
		return _parent.schieneGet(idSchiene);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.<br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	private @NotNull GostBlockungsergebnisSchiene getSchieneE(final long idSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_schienenID_to_schiene, idSchiene);
	}

	/**
	 * Liefert das zur Nummer zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.<br>
	 * Wirft eine {@link DeveloperNotificationException} falls eine solche Schiene nicht existiert.
	 *
	 * @param nrSchiene Die Nummer der Schiene.
	 *
	 * @return das zur Nummer zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls eine solche Schiene nicht existiert.
	 */
	private @NotNull GostBlockungsergebnisSchiene getSchieneEmitNr(final int nrSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_schienenNR_to_schiene, nrSchiene);
	}

	/**
	 * Liefert die ID einer Schiene mit einer bestimmten Nummer.
	 *
	 * @param nrSchiene  Die Nummer der Schiene.
	 *
	 * @return die ID einer Schiene mit einer bestimmten Nummer.
	 * @throws DeveloperNotificationException falls eine solche Schiene nicht existiert.
	 */
	public long getOfSchieneID(final int nrSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_schienenNR_to_schiene, nrSchiene).id;
	}

	/**
	 * Liefert die Menge aller Schienen.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public @NotNull List<GostBlockungsergebnisSchiene> getMengeAllerSchienen() {
		return _ergebnis.schienen;
	}

	/**
	 * Liefert eine Menge aller Schienen mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Schienen mit mindestens einer Kollision.
	 */
	private @NotNull Set<GostBlockungsergebnisSchiene> getMengeDerSchienenMitKollisionen() {
		return CollectionUtils.toFilteredHashSet(_schienenID_to_schiene.values(),
				(final @NotNull GostBlockungsergebnisSchiene s) -> getOfSchieneHatKollision(s.id));
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.<br>
	 * Hinweis: Falls ein Schüler mehrfach in der Schiene ist, also mit Kollisionen, wird er mehrfach gezählt!
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.
	 */
	public int getOfSchieneAnzahlSchueler(final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(_schienenID_to_susAnzahl, idSchiene);
	}

	/**
	 * Liefert TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public boolean getOfSchieneHatKollision(final long idSchiene) {
		return getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene.<br>
	 * Hinweis Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schüler-Kollisionen der Schiene.
	 */
	public int getOfSchieneAnzahlSchuelerMitKollisionen(final long idSchiene) {
		return DeveloperNotificationException.ifMapGetIsNull(_schienenID_to_kollisionen, idSchiene);
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public @NotNull Set<Long> getOfSchieneSchuelermengeMitKollisionen(final long idSchiene) {
		final @NotNull Set<Long> set = new HashSet<>();
		for (final @NotNull Long schuelerID : _schuelerID_to_kollisionen.keySet())
			if (getOfSchuelerOfSchieneKursmenge(schuelerID, idSchiene).size() > 1)
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public @NotNull Set<GostBlockungsergebnisKurs> getOfSchieneKursmengeMitKollisionen(final long idSchiene) {
		final @NotNull Set<GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisKurs kurs : getSchieneE(idSchiene).kurse)
			if (getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert die sortierte Menge an Kursen einer bestimmten Schiene.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die sortierte Menge an Kursen einer bestimmten Schiene.
	 */
	public @NotNull List<GostBlockungsergebnisKurs> getOfSchieneKursmengeSortiert(final long idSchiene) {
		return getSchieneE(idSchiene).kurse;
	}

	/**
	 * Liefert einen Tooltip für die Schiene, welche alle Kollisionen pro Kurs-Paarung darstellt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return einen Tooltip für die Schiene, welche alle Kollisionen pro Kurs-Paarung darstellt.
	 */
	private @NotNull String getOfSchieneTooltipKurskollisionen(final long idSchiene) {
		final StringBuilder sbZeilen = new StringBuilder();

		for (final GostBlockungsergebnisKurs kurs1 : getSchieneE(idSchiene).kurse) {
			int summe = 0;
			final StringBuilder sbZeile = new StringBuilder();

			for (final GostBlockungsergebnisKurs kurs2 : getSchieneE(idSchiene).kurse) {
				if (kurs2.id != kurs1.id) {
					final int anzahl = GostBlockungsergebnisManager.getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1, kurs2);
					if (anzahl > 0) {
						summe += anzahl;
						sbZeile.append((sbZeile.isEmpty() ? "" : ", ") + getOfKursName(kurs2.id) + "(" + anzahl + ")");
					}
				}
			}

			if (summe > 0) {
				sbZeilen.append(getOfKursName(kurs1.id) + "(" + summe + "): " + sbZeile.toString() + "\n");
			}
		}

		return sbZeilen.isEmpty() ? "Keine Kollisionen in der Schiene" : sbZeilen.toString();
	}

	/**
	 * Liefert alle Kollisionen einer Schiene, als Liste von Liste von Kurs-Anzahl-Paaren.
	 * <br>Pro innerer Liste gilt: Das erste Paar ist der Kurs, welcher mit allen anderen verglichen wurde, zusammen mit der Kollisions-Summe.
	 * <br>Anschließend folgen alle anderen Kurse mit ihrer Kollisions-Anzahl, falls diese größer 0 ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return alle Kollisionen einer Schiene, als Liste von Liste von Kurs-Anzahl-Paaren.
	 */
	public @NotNull List<List<Pair<GostBlockungsergebnisKurs, Integer>>> getOfSchieneTooltipKurskollisionenAsData(final long idSchiene) {
		final @NotNull List<List<Pair<GostBlockungsergebnisKurs, Integer>>> listOfLists = new ArrayList<>();

		for (final GostBlockungsergebnisKurs kurs1 : getSchieneE(idSchiene).kurse) {
			int summe = 0;
			final @NotNull List<Pair<GostBlockungsergebnisKurs, Integer>> listOfPairs = new ArrayList<>();

			for (final GostBlockungsergebnisKurs kurs2 : getSchieneE(idSchiene).kurse) {
				if (kurs2.id != kurs1.id) {
					final int anzahl = GostBlockungsergebnisManager.getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1, kurs2);
					if (anzahl > 0) {
						listOfPairs.add(new Pair<GostBlockungsergebnisKurs, Integer>(kurs2, anzahl));
						summe += anzahl;
					}
				}
			}

			if (summe > 0) {
				listOfPairs.add(0, new Pair<GostBlockungsergebnisKurs, Integer>(kurs1, summe));
				listOfLists.add(listOfPairs);
			}
		}

		return listOfLists;
	}

	private static int getOfKursOfKursAnzahlGemeinsamerSchueler(final @NotNull GostBlockungsergebnisKurs kurs1,
			final @NotNull GostBlockungsergebnisKurs kurs2) {
		final @NotNull Set<Long> set = new HashSet<>();
		set.addAll(kurs1.schueler);
		set.retainAll(kurs2.schueler);
		return set.size();
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist.<br>
	 * Kriterium: Es dürfen keine Kurse der Schiene zugeordnet sein.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public boolean getOfSchieneRemoveAllowed(final long idSchiene) throws DeveloperNotificationException {
		return getSchieneE(idSchiene).kurse.isEmpty();
	}


	/**
	 * Liefert die maximale Anzahl an Kursen, die es in einer Schiene gibt.
	 *
	 * @return die maximale Anzahl an Kursen, die es in einer Schiene gibt.
	 */
	public int getOfSchieneMaxKursanzahl() {
		int max = 0;
		for (final @NotNull GostBlockungsergebnisSchiene schiene : _ergebnis.schienen)
			max = Math.max(max, schiene.kurse.size());
		return max;
	}

	/**
	 * Liefert die Anzahl an externen SuS der Schiene.
	 * <br>Hinweis: Ist ein Schüler mehrfach in der Schiene (Kollision) wird er auch mehrfach gezählt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an externen SuS der Schiene.
	 */
	public int getOfSchieneAnzahlSchuelerExterne(final long idSchiene) {
		int summe = 0;
		for (final @NotNull GostBlockungsergebnisKurs kurs : getSchieneE(idSchiene).kurse)
			for (final long idSchueler : kurs.schueler)
				if (getOfSchuelerHatStatusExtern(idSchueler))
					summe++;
		return summe;
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS der Schiene.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Dummy-SuS der Schiene.
	 */
	public int getOfSchieneAnzahlSchuelerDummy(final long idSchiene) {
		int summe = 0;

		for (final @NotNull GostBlockungsergebnisKurs kurs : getSchieneE(idSchiene).kurse)
			summe += getOfKursAnzahlSchuelerDummy(kurs.id);

		return summe;
	}

	/**
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddSchieneByID(final long idSchiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Schiene " + _parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager hinzugefügt werden!",
				!_parent.schieneGetExistiert(idSchiene));

		// Bewertungen aktualisieren.
		stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Schiene.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                                         falls die Schiene noch Kurszuordnungen hat.
	 */
	public void setRemoveSchieneByID(final long idSchiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Schiene " + _parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager entfernt werden!",
				_parent.schieneGetExistiert(idSchiene));
		final int nKurse = getSchieneE(idSchiene).kurse.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Schiene " + _parent.toStringSchiene(idSchiene) + " hat noch " + nKurse + " Kurse!",
				nKurse > 0);
		_ergebnis.schienen.remove(getSchieneE(idSchiene));

		// Bewertungen aktualisieren.
		stateRevalidateEverything();
	}

	/**
	 * Fügt den übergebenen Kurs hinzu.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager hinzugefügt wurde.
	 */
	public void setAddKursByID(final long idKurs) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("" + _parent.toStringKurs(idKurs) + " muss erst beim Datenmanager hinzugefügt werden!",
				!_parent.kursGetExistiert(idKurs));
		final @NotNull GostBlockungKurs kurs = _parent.kursGet(idKurs);
		final int nSchienen = _parent.schieneGetAnzahl();
		DeveloperNotificationException.ifTrue("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!",
				nSchienen < kurs.anzahlSchienen);

		// Erst nach der Revalidierung gibt es "eKurs".
		stateRevalidateEverything();

		// Kurs automatisch in die ersten 'kurs.anzahlSchienen' Schienen hinzufügen.
		for (int nr = 1; nr <= kurs.anzahlSchienen; nr++) {
			final @NotNull GostBlockungsergebnisSchiene eSchiene = getSchieneEmitNr(nr);
			stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs, eSchiene.id);
		}

		// Nach dieser Revalidierung gibt ist der Kurs der Schiene zugeordnet.
		stateRevalidateEverything();
	}

	/**
	 * Löscht alle übergebenen Kurse. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.
	 *
	 * @param idKurse  Die Liste der Datenbank-IDs der Kurse.
	 *
	 * @throws DeveloperNotificationException  Falls mindestens einer der Kurse nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public void setRemoveKurseByID(final @NotNull List<Long> idKurse) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen
		for (final long idKurs : idKurse)
			DeveloperNotificationException.ifTrue(_parent.toStringKurs(idKurs) + " muss erst beim Datenmanager entfernt werden!",
					_parent.kursGetExistiert(idKurs));

		// (2) Lösche die Kurse aus der DTO-Datenstruktur (löscht dadurch auch die SuS des Kurses).
		for (final long idKurs : idKurse) {
			final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);

			for (final @NotNull Long schienenID : kurs.schienen) {
				final @NotNull Iterator<GostBlockungsergebnisKurs> i = getSchieneE(schienenID).kurse.iterator();
				while (i.hasNext())
					if (i.next().id == kurs.id)
						i.remove();
			}
		}

		// (3) Bewertungen aktualisieren
		stateRevalidateEverything();
	}

	/**
	 * Verschiebt alles SuS von pKursID2delete nach pKursID1keep und
	 * löscht dann den Kurs mit der ID beim {@link GostBlockungsdatenManager},
	 * anschließend in diesem Manager.
	 *
	 * @param  idKursID1keep    Die Datenbank-ID des Kurses, der erhalten bleibt.
	 * @param  idKursID2delete  Die Datenbank-ID des Kurses, der gelöscht wird.
	 */
	public void setMergeKurseByID(final long idKursID1keep, final long idKursID2delete) {
		// Verschiebe die SuS in der DTO-Datenstruktur
		final @NotNull GostBlockungsergebnisKurs kursDelete = getKursE(idKursID2delete);
		final @NotNull GostBlockungsergebnisKurs kursKeep = getKursE(idKursID1keep);
		kursKeep.schueler.addAll(kursDelete.schueler);

		// Lösche den Kurs aus der DTO-Datenstruktur (löscht dadurch auch SuS aus dem Kurs).
		for (final @NotNull Long schienenID : kursDelete.schienen)
			getSchieneE(schienenID).kurse.remove(kursDelete);

		// Löschen den Kurs beim Parent-Manager.
		_parent.kursMerge(idKursID1keep, idKursID2delete);

		// Bewertungen aktualisieren
		stateRevalidateEverything();
	}

	/**
	 * Erzeugt einen neuen Kurs2 beim {@link GostBlockungsdatenManager},
	 * dann bei diesem Manager und
	 * verschiebt alle SuS des übergebenen Arrays von Kurs1 nach Kurs2.
	 *
	 * @param  kurs1alt     Der Kurs, der gesplittet wird.
	 * @param  kurs2neu     Der Kurs, der neu erzeugt wird.
	 * @param  susVon1nach2 Die Datenbank-IDs der Schüler, die verschoben werden sollen.
	 */
	public void setSplitKurs(final @NotNull GostBlockungKurs kurs1alt, final @NotNull GostBlockungKurs kurs2neu, final @NotNull long[] susVon1nach2) {
		// 1) Kurs2 erzeugen (beim Parent-Manager).
		_parent.kursAdd(kurs2neu);

		// 2) Erst nach der Revalidierung gibt es "eKurs".
		stateRevalidateEverything();

		// 3) Den neuen Kurs in die selben Schienen wie den alten Kurs setzen.
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : getOfKursSchienenmenge(kurs1alt.id))
			stateKursSchieneHinzufuegenOhneRegelvalidierung(kurs2neu.id, eSchiene.id);

		// 4) Verschieben der SuS von Kurs1 nach Kurs2 (in diesem Manager).
		for (final long schuelerID : susVon1nach2) {
			stateSchuelerKursEntfernenOhneRevalidierung(schuelerID, kurs1alt.id);
			stateSchuelerKursHinzufuegenOhneRevalidierung(schuelerID, kurs2neu.id);
		}

		// 5) Erst nach der Revalidierung sind die SuS in den Kursen gewandert.
		stateRevalidateEverything();
	}

	/**
	 * Verändert die Schienenanzahl eines Kurses. Dies ist nur bei einer Blockungsvorlage erlaubt.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 * @param  anzahlSchienenNeu Die neue Schienenanzahl des Kurses.
	 *
	 * @throws DeveloperNotificationException Falls ein unerwarteter Fehler passiert.
	 */
	public void patchOfKursSchienenAnzahl(final long idKurs, final int anzahlSchienenNeu) throws DeveloperNotificationException {
		// Daten holen.
		final @NotNull GostBlockungKurs kursG = getKursG(idKurs);
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(idKurs);
		final int nSchienen = _parent.schieneGetAnzahl();

		// DeveloperNotificationException
		DeveloperNotificationException.ifTrue(
				"Schienenanzahl von KursE (" + kursE.anzahlSchienen + ") ist ungleich der von KursG (" + kursG.anzahlSchienen + ")!",
				kursE.anzahlSchienen != kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Schienenanzahl von " + _parent.toStringKurs(idKurs) + " darf nur bei der Blockungsvorlage verändert werden!",
				!_parent.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue(_parent.toStringKurs(idKurs) + " hat als GostBlockungKurs " + kursG.anzahlSchienen
				+ " Schienen, als GostBlockungsergebnisKurs hingegen " + kursE.anzahlSchienen + " Schienen!", kursE.anzahlSchienen != kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Blockung hat 0 Schienen. Das darf nicht passieren!", nSchienen == 0);
		DeveloperNotificationException.ifTrue(
				_parent.toStringKurs(idKurs) + " muss mindestens einer Schiene zugeordnet sein, statt " + anzahlSchienenNeu + " Schienen!",
				anzahlSchienenNeu <= 0);
		DeveloperNotificationException.ifTrue("Es gibt nur " + nSchienen + " Schienen, somit kann " + _parent.toStringKurs(idKurs) + " nicht "
				+ anzahlSchienenNeu + " Schienen zugeordnet werden!", anzahlSchienenNeu > nSchienen);

		// Die Schienenanzahl erhöhen, ggf. mehrfach.
		while (anzahlSchienenNeu > kursG.anzahlSchienen) {
			boolean hinzugefuegt = false;
			for (int nr = 1; (nr <= _schienenNR_to_schiene.size()) && (!hinzugefuegt); nr++) {
				final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
				if (!kursE.schienen.contains(schiene.id)) {
					hinzugefuegt = true;
					kursG.anzahlSchienen++;
					kursE.anzahlSchienen++;
					stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs, schiene.id);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine freie Schiene für " + _parent.toStringKurs(idKurs) + " gefunden!", !hinzugefuegt);
		}

		// Die Schienenanzahl verringern, ggf. mehrfach.
		while (anzahlSchienenNeu < kursG.anzahlSchienen) {
			boolean entfernt = false;
			for (int nr = _schienenNR_to_schiene.size(); (nr >= 1) && (!entfernt); nr--) {
				final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
				if (kursE.schienen.contains(schiene.id)) {
					entfernt = true;
					kursG.anzahlSchienen--;
					kursE.anzahlSchienen--;
					stateKursSchieneEntfernenOhneRegelvalidierung(idKurs, schiene.id);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine belegte Schiene von " + _parent.toStringKurs(idKurs) + " gefunden!", !entfernt);
		}

		stateRevalidateEverything();
	}

	/**
	 * Informiert den Manager, dass sich bei mindestens einem Kurs die Lehrkraft geändert hat.
	 * Führt zu einer Revalidierung der Bewertung des Ergebnisses.
	 */
	public void patchOfKursLehrkaefteChanged() {
		stateRevalidateEverything();
	}

	private @NotNull Comparator<Long> createComparatorFachartKursartFach() {
		final @NotNull Comparator<Long> comp = (final @NotNull Long a, final @NotNull Long b) -> {
			final long aKursartID = GostKursart.getKursartID(a);
			final long bKursartID = GostKursart.getKursartID(b);
			if (aKursartID < bKursartID)
				return -1;
			if (aKursartID > bKursartID)
				return +1;

			final long aFachID = GostKursart.getFachID(a);
			final long bFachID = GostKursart.getFachID(b);
			final GostFach aFach = _parent.faecherManager().get(aFachID);
			final GostFach bFach = _parent.faecherManager().get(bFachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		};

		return comp;
	}

	private @NotNull Comparator<Long> createComparatorFachartFachKursart() {
		final @NotNull Comparator<Long> comp = (final @NotNull Long a, final @NotNull Long b) -> {
			final long aFachID = GostKursart.getFachID(a);
			final long bFachID = GostKursart.getFachID(b);
			final GostFach aFach = _parent.faecherManager().get(aFachID);
			final GostFach bFach = _parent.faecherManager().get(bFachID);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0)
				return cmpFach;

			final long aKursartID = GostKursart.getKursartID(a);
			final long bKursartID = GostKursart.getKursartID(b);
			if (aKursartID < bKursartID)
				return -1;
			if (aKursartID > bKursartID)
				return +1;
			return 0;
		};
		return comp;
	}

	private @NotNull Comparator<GostBlockungsergebnisKurs> createComparatorKursFachKursartNummer() {
		final @NotNull Comparator<GostBlockungsergebnisKurs> comp =
				(final @NotNull GostBlockungsergebnisKurs a, final @NotNull GostBlockungsergebnisKurs b) -> {
					final GostFach aFach = _parent.faecherManager().get(a.fachID);
					final GostFach bFach = _parent.faecherManager().get(b.fachID);
					final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
					if (cmpFach != 0)
						return cmpFach;

					if (a.kursart < b.kursart)
						return -1;
					if (a.kursart > b.kursart)
						return +1;

					final @NotNull GostBlockungKurs aKurs = _parent.kursGet(a.id);
					final @NotNull GostBlockungKurs bKurs = _parent.kursGet(b.id);
					return Integer.compare(aKurs.nummer, bKurs.nummer);
				};
		return comp;
	}

	private @NotNull Comparator<GostBlockungsergebnisKurs> createComparatorKursKursartFachNummer() {
		final @NotNull Comparator<GostBlockungsergebnisKurs> comp =
				(final @NotNull GostBlockungsergebnisKurs a, final @NotNull GostBlockungsergebnisKurs b) -> {
					if (a.kursart < b.kursart)
						return -1;
					if (a.kursart > b.kursart)
						return +1;

					final GostFach aFach = _parent.faecherManager().get(a.fachID);
					final GostFach bFach = _parent.faecherManager().get(b.fachID);
					final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
					if (cmpFach != 0)
						return cmpFach;

					final @NotNull GostBlockungKurs aKurs = _parent.kursGet(a.id);
					final @NotNull GostBlockungKurs bKurs = _parent.kursGet(b.id);
					return Integer.compare(aKurs.nummer, bKurs.nummer);
				};
		return comp;
	}

	/**
	 * Eine Logger-Ausgabe für Debug-Zwecke.
	 *
	 * @param logger Ein Logger für Debug-Zwecke.
	 */
	public void debug(final @NotNull Logger logger) {
		logger.modifyIndent(+4);
		logger.logLn("----- Kurse sortiert nach Fachart -----");
		for (final @NotNull Long fachartID : _fachartID_to_kurseList.keySet()) {
			logger.logLn("FachartID = " + fachartID + " (KD = " + getOfFachartKursdifferenz(fachartID) + ")");
			for (final @NotNull GostBlockungsergebnisKurs kurs : getOfFachartKursmenge(fachartID)) {
				logger.logLn("    " + getOfKursName(kurs.id) + " : " + kurs.schueler.size() + " SuS");
			}
		}
		logger.logLn("KursdifferenzMax = " + _ergebnis.bewertung.kursdifferenzMax);
		logger.logLn("KursdifferenzHistogramm = " + Arrays.toString(_ergebnis.bewertung.kursdifferenzHistogramm));
		logger.modifyIndent(-4);
	}

	/**
	 * Liefert einen String, der alle Schienen-Fachart-Kurs-Zuordnungen zeigt.
	 *
	 * @return einen String, der alle Schienen-Fachart-Kurs-Zuordnungen zeigt.
	 */
	private @NotNull String debugKursSchienenZuordnungen() {
		final @NotNull StringBuilder sb = new StringBuilder();
		sb.append("\n\nSchienen-Fachart-Kurs-Zuordnungen");

		// Ergänze leere (Schienen, Fachart) Kombinationen
		for (final long idSchiene : _schienenIDset) {
			sb.append("Schiene " + _parent.toStringSchieneSimple(idSchiene) + "\n");

			for (final long idFachart : _schienenID_fachartID_to_kurseList.getKeySetOf(idSchiene)) {
				if (!Map2DUtils.getOrCreateArrayList(_schienenID_fachartID_to_kurseList, idSchiene, idFachart).isEmpty()) {
					sb.append("    Fachart " + _parent.toStringFachartSimpleByFachartID(idFachart) + "\n");
					for (final @NotNull GostBlockungsergebnisKurs eKurs : Map2DUtils.getOrCreateArrayList(_schienenID_fachartID_to_kurseList, idSchiene,
							idFachart)) {
						sb.append("        Kurs " + _parent.toStringKursSimple(eKurs.id) + "\n");
					}
				}
			}
		}

		return sb.toString();

	}


}
