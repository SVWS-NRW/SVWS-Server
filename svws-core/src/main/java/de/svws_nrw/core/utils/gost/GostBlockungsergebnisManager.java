package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.Map.Entry;

import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.PairNN;
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
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.kursblockung.SchuelerblockungAlgorithmus;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.SetUtils;
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

	// ############################## UPDATE ##############################

	/** Liste aller Fehlermeldungen. */
	private final @NotNull List<@NotNull String> _fehlermeldungen = new ArrayList<>();

	/** Set aller Schienen-IDs. */
	private final @NotNull Set<@NotNull Long> _schienenIDset = new HashSet<@NotNull Long>();


	// ####################################################################

	/** Schienen-Nummer --> GostBlockungsergebnisSchiene */
	private final @NotNull Map<@NotNull Integer, @NotNull GostBlockungsergebnisSchiene> _map_schienenNr_schiene = new HashMap<>();

	/** Schienen-ID --> GostBlockungSchiene */
	private final @NotNull Map<@NotNull Long, @NotNull GostBlockungsergebnisSchiene> _map_schienenID_schiene = new HashMap<>();

	/** Schienen-ID --> Integer = Anzahl SuS */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_schienenID_schuelerAnzahl = new HashMap<>();

	/** Schienen-ID --> Anzahl Kollisionen */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_schienenID_kollisionen = new HashMap<>();

	/** (Schienen-ID, Fachart-ID) --> ArrayList<Kurse> = Alle Kurse in der Schiene mit der Fachart. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>> _map2D_schienenID_fachartID_kurse = new HashMap2D<>();

	/** Schüler-ID --> Set<GostBlockungsergebnisKurs> */
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> _map_schuelerID_kurse = new HashMap<>();

	/** Schüler-ID --> Set<GostBlockungsergebnisKurs> = Kurse des Schüler, die aufgrund der aktuellen Fachwahlen ungültig sind.*/
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> _map_schuelerID_ungueltige_kurse = new HashMap<>();

	/** Schüler-ID --> Integer (Kollisionen) */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_schuelerID_kollisionen = new HashMap<>();

	/** (Schüler-ID, Fach-ID) --> GostBlockungsergebnisKurs = Die aktuelle Wahl des Schülers in dem Fach.*/
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, GostBlockungsergebnisKurs> _map2D_schuelerID_fachID_kurs = new HashMap2D<>();

	/** Schüler-ID --> Schienen-ID --> Set<GostBlockungsergebnisKurs> = Alle Kurse des Schülers in der Schiene.*/
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> _map2D_schuelerID_schienenID_kurse = new HashMap2D<>();

	/** Kurs-ID --> Set<GostBlockungsergebnisSchiene> */
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisSchiene>> _map_kursID_schienen = new HashMap<>();

	/** Kurs-ID --> GostBlockungsergebnisKurs */
	private final @NotNull Map<@NotNull Long, @NotNull GostBlockungsergebnisKurs> _map_kursID_kurs = new HashMap<>();

	/** Kurs-ID --> Anzahl an Dummy-SuS */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_kursID_dummySuS = new HashMap<>();

	/** Kurs-ID --> Maximale Anzahl an SuS */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_kursID_maxSuS = new HashMap<>();

	/** Kurs-ID --> Set<SchuelerID> */
	private final @NotNull Map<@NotNull Long, @NotNull Set<@NotNull Long>> _map_kursID_schuelerIDs = new HashMap<>();

	/** Fach-ID --> ArrayList<GostBlockungsergebnisKurs> */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>> _map_fachID_kurse = new HashMap<>();

	/** Fachart-ID --> Integer = Kursdifferenz der Fachart. */
	private final @NotNull Map<@NotNull Long, @NotNull Integer> _map_fachartID_kursdifferenz = new HashMap<>();

	/** Menge aller Fachart-IDs sortiert nach der aktuellen Sortiervariante. */
	private final @NotNull List<@NotNull Long> _fachartmenge_sortiert = new ArrayList<>();

	/** Entscheidet, welcher Comparator verwendet wird mit 1 = (KURSART, FACH) andernfalls (FACH, KURSART). */
	private int _fachartmenge_sortierung = 1;

	/** Comparator für die Facharten nach (KURSART, FACH). */
	private final @NotNull Comparator<@NotNull Long> _fachartComparator_kursart_fach;

	/** Comparator für die Facharten nach (FACH, KURSART). */
	private final @NotNull Comparator<@NotNull Long> _fachartComparator_fach_kursart;

	/** Fachart-ID --> ArrayList<GostBlockungsergebnisKurs> = Alle Kurse der selben Fachart. */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull GostBlockungsergebnisKurs>> _map_fachartID_kurse = new HashMap<>();

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<@NotNull GostBlockungsergebnisKurs> _kursComparator_kursart_fach_kursnummer;

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<@NotNull GostBlockungsergebnisKurs> _kursComparator_fach_kursart_kursnummer;

	/** Regeltyp --> Liste aller Regelverletzungen. */
	private final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> _map_regelID_verletzungen = new HashMap<>();

	/** Liste aller Regeltypen, die mindestens eine Regelverletzung haben. */
	private final @NotNull List<@NotNull GostKursblockungRegelTyp> _list_verletzte_regeltypen_sortiert = new ArrayList<>();

	/** Textuelle Darstellung aller Regelverletzungen der Fächerparallelität. */
	private @NotNull String _regelverletzungen_der_faecherparallelitaet = "";

	/** Textuelle Darstellung aller Regelverletzungen der Wahlkonflikte. */
	private @NotNull String _regelverletzungen_der_wahlkonflikte = "";

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
		stateClear(new GostBlockungsergebnis(), pGostBlockungsergebnisID);
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
		stateClear(pErgebnis, pErgebnis.id);
	}

	private @NotNull Comparator<@NotNull Long> createComparatorFachartKursartFach() {
		final @NotNull Comparator<@NotNull Long> comp = (final @NotNull Long a, final @NotNull Long b) -> {
			final long aKursartID = GostKursart.getKursartID(a);
			final long bKursartID = GostKursart.getKursartID(b);
			if (aKursartID < bKursartID) return -1;
			if (aKursartID > bKursartID) return +1;

			final long aFachID = GostKursart.getFachID(a);
			final long bFachID = GostKursart.getFachID(b);
			final @NotNull GostFach aFach = _parent.faecherManager().getOrException(aFachID);
			final @NotNull GostFach bFach = _parent.faecherManager().getOrException(bFachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		};

		return comp;
	}

	private @NotNull Comparator<@NotNull Long> createComparatorFachartFachKursart() {
		final @NotNull Comparator<@NotNull Long> comp = (final @NotNull Long a, final @NotNull Long b) -> {
			final long aFachID = GostKursart.getFachID(a);
			final long bFachID = GostKursart.getFachID(b);
			final @NotNull GostFach aFach = _parent.faecherManager().getOrException(aFachID);
			final @NotNull GostFach bFach = _parent.faecherManager().getOrException(bFachID);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;

			final long aKursartID = GostKursart.getKursartID(a);
			final long bKursartID = GostKursart.getKursartID(b);
			if (aKursartID < bKursartID) return -1;
			if (aKursartID > bKursartID) return +1;
			return 0;
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostBlockungsergebnisKurs> createComparatorKursFachKursartNummer() {
		final @NotNull Comparator<@NotNull GostBlockungsergebnisKurs> comp = (final @NotNull GostBlockungsergebnisKurs a, final @NotNull GostBlockungsergebnisKurs b) -> {
			final @NotNull GostFach aFach = _parent.faecherManager().getOrException(a.fachID);
			final @NotNull GostFach bFach = _parent.faecherManager().getOrException(b.fachID);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;

			if (a.kursart < b.kursart) return -1;
			if (a.kursart > b.kursart) return +1;

			final @NotNull GostBlockungKurs aKurs = _parent.kursGet(a.id);
			final @NotNull GostBlockungKurs bKurs = _parent.kursGet(b.id);
			return Integer.compare(aKurs.nummer, bKurs.nummer);
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostBlockungsergebnisKurs> createComparatorKursKursartFachNummer() {
		final @NotNull Comparator<@NotNull GostBlockungsergebnisKurs> comp = (final @NotNull GostBlockungsergebnisKurs a, final @NotNull GostBlockungsergebnisKurs b) -> {
			if (a.kursart < b.kursart) return -1;
			if (a.kursart > b.kursart) return +1;

			final @NotNull GostFach aFach = _parent.faecherManager().getOrException(a.fachID);
			final @NotNull GostFach bFach = _parent.faecherManager().getOrException(b.fachID);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;

			final @NotNull GostBlockungKurs aKurs = _parent.kursGet(a.id);
			final @NotNull GostBlockungKurs bKurs = _parent.kursGet(b.id);
			return Integer.compare(aKurs.nummer, bKurs.nummer);
		};
		return comp;
	}

	/**
	 * Baut alle Datenstrukturen neu auf.
	 */
	private void stateRevalidateEverything() {
		stateClear(_ergebnis, _ergebnis.id);
	}

	private void stateClear(final @NotNull GostBlockungsergebnis pOld, final long pGostBlockungsergebnisID) {
		// clear maps
		_map_schienenNr_schiene.clear();
		_map_schienenID_schiene.clear();
		_map_schienenID_schuelerAnzahl.clear();
		_map_schienenID_kollisionen.clear();
		_map2D_schienenID_fachartID_kurse.clear();
		_map_schuelerID_kurse.clear();
		_map_schuelerID_ungueltige_kurse.clear();
		_map_schuelerID_kollisionen.clear();
		_map2D_schuelerID_fachID_kurs.clear();
		_map2D_schuelerID_schienenID_kurse.clear();
		_map_kursID_schienen.clear();
		_map_kursID_kurs.clear();
		_map_kursID_schuelerIDs.clear();
		_map_kursID_dummySuS.clear();
		_map_kursID_maxSuS.clear();
		_map_fachID_kurse.clear();
		_map_fachartID_kurse.clear();
		_map_fachartID_kursdifferenz.clear();
		_regelverletzungen_der_faecherparallelitaet = "";
		_regelverletzungen_der_wahlkonflikte = "";

		// copy attributes
		_ergebnis = new GostBlockungsergebnis();
		_ergebnis.id = pGostBlockungsergebnisID;
		_ergebnis.blockungID = _parent.getID();
		_ergebnis.name = pOld.name;
		_ergebnis.gostHalbjahr = _parent.daten().gostHalbjahr;
		_ergebnis.istAktiv = pOld.istAktiv;

		// Bewertungskriterium 3a und 3b (Kursdifferenzen)
		_ergebnis.bewertung.kursdifferenzMax = 0;
		_ergebnis.bewertung.kursdifferenzHistogramm = new int[_parent.schuelerGetAnzahl() + 1];

		// Bewertungskriterium 2a (Nicht zugeordnete Fachwahlen)
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet = _parent.daten().fachwahlen.size();

		// Update Prozess.
		_fehlermeldungen.clear();
		if (update_schienenIDset())
			return;


		// Schienen von '_parent' kopieren und hinzufügen.
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
			// GostBlockungSchiene --> GostBlockungsergebnisSchiene
			final @NotNull GostBlockungsergebnisSchiene eSchiene = new GostBlockungsergebnisSchiene();
			eSchiene.id = gSchiene.id;
			// Hinzufügen.
			_ergebnis.schienen.add(eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenNr_schiene, gSchiene.nummer, eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenID_schiene, gSchiene.id, eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenID_schuelerAnzahl, gSchiene.id, 0);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schienenID_kollisionen, gSchiene.id, 0);
		}

		// Kurse von '_parent' kopieren und hinzufügen. Fachart-IDs erzeugen.
		for (final @NotNull GostBlockungKurs gKurs : _parent.daten().kurse) {
			// GostBlockungKurs --> GostBlockungsergebnisKurs
			final @NotNull GostBlockungsergebnisKurs eKurs = new GostBlockungsergebnisKurs();
			eKurs.id = gKurs.id;
			eKurs.fachID = gKurs.fach_id;
			eKurs.kursart = gKurs.kursart;
			eKurs.anzahlSchienen = gKurs.anzahlSchienen;
			_ergebnis.bewertung.anzahlKurseNichtZugeordnet += eKurs.anzahlSchienen;

			// Map: kursID --> Kurs-Objekt
			DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_kurs, eKurs.id, eKurs);

			// Map: kursID --> Schienen des Kurses
			DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_schienen, eKurs.id, new HashSet<@NotNull GostBlockungsergebnisSchiene>());

			// Map: kursID --> Schüler des Kurses
			DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_schuelerIDs, eKurs.id, new HashSet<@NotNull Long>());

			// Map: fachID --> Kursliste
			MapUtils.getOrCreateArrayList(_map_fachID_kurse, eKurs.fachID).add(eKurs);

			// Map: fachartID --> Kursliste
			final long fachartID = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			MapUtils.getOrCreateArrayList(_map_fachartID_kurse, fachartID).add(eKurs);

			// Map: fachartID --> Kursdifferenz
			if (!_map_fachartID_kursdifferenz.containsKey(fachartID)) {
				_map_fachartID_kursdifferenz.put(fachartID, 0);
				_ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
		}

		// Fachwahlen zu denen es keinen Kurs gibt der Map '_map_fachartID_kurse' und '_map_fachID_kurse' hinzufügen.
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen) {
			MapUtils.getOrCreateArrayList(_map_fachartID_kurse, GostKursart.getFachartIDByFachwahl(gFachwahl));
			MapUtils.getOrCreateArrayList(_map_fachID_kurse, gFachwahl.fachID);
		}

		// Map: (schienenID, fachartID) --> Kursmenge = Alle Kurse einer Fachart pro Schiene
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen)
			for (final @NotNull Long fachartID : _map_fachartID_kursdifferenz.keySet())
				DeveloperNotificationException.ifMap2DPutOverwrites(_map2D_schienenID_fachartID_kurse, gSchiene.id, fachartID, new ArrayList<>());

		// Schüler kopieren und hinzufügen.
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler) {
			// Map: schuelerID --> Kurs-Set
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_kurse, gSchueler.id, new HashSet<@NotNull GostBlockungsergebnisKurs>());
			// Map: schuelerID --> Anzahl Kollisionen
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_kollisionen, gSchueler.id, 0);
			// Map: schuelerID --> (fachID --> Kurs)
			// _map2D_schuelerID_fachID_kurs nicht nötig
			// _map2D_schuelerID_schienenID_kurse nicht nötig --> wird später in der Schüler-Schienen-Schleife initialisiert.
		}

		// _map2D_schuelerID_fachID_kurs: Fachwahlen kopieren und hinzufügen
		for (final @NotNull GostFachwahl gFachwahl : _parent.daten().fachwahlen)
			DeveloperNotificationException.ifMap2DPutOverwrites(_map2D_schuelerID_fachID_kurs,  gFachwahl.schuelerID, gFachwahl.fachID, null);

		// Map: schuelerID --> (schienenID --> Kurse) = Alle Kurse des Schülers pro Schiene
		for (final @NotNull Schueler gSchueler : _parent.daten().schueler)
			for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen) {
				final HashSet<@NotNull GostBlockungsergebnisKurs> newSet = new HashSet<>();
				DeveloperNotificationException.ifMap2DPutOverwrites(_map2D_schuelerID_schienenID_kurse, gSchueler.id, gSchiene.id, newSet);
			}

		// Zuordnungen kopieren (diese können leer sein).
		final HashSet<@NotNull Long> kursBearbeitet = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisSchiene schieneOld : pOld.schienen)
			for (final @NotNull GostBlockungsergebnisKurs kursOld : schieneOld.kurse) {
				stateKursSchieneHinzufuegenOhneRegelvalidierung(kursOld.id, schieneOld.id);
				if (kursBearbeitet.add(kursOld.id)) // Bei Multikursen dürfen SuS nicht doppelt hinzugefügt werden.
					for (final @NotNull Long schuelerID : kursOld.schueler)
						stateSchuelerKursHinzufuegenOhneRevalidierung(schuelerID, kursOld.id);
			}

		// Wenn ein Schüler ignoriert werden soll, dann bewerte nicht zugeordnete Kurse als zugeordnet.
		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			final long idSchueler = regel.parameter.get(0);
			for (@NotNull final GostFachwahl gFachwahl : _parent.schuelerGetListeOfFachwahlen(idSchueler))
				if (getOfSchuelerOfFachZugeordneterKurs(idSchueler, gFachwahl.fachID) == null)
					_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		}


		// _fachartmenge_sortiert erzeugen.
		_fachartmenge_sortiert.addAll(_map_fachartID_kurse.keySet());

		// Regelverletzungen überprüfen.
		stateRegelvalidierung();
	}

	private boolean update_schienenIDset() {
		_schienenIDset.clear();

		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen)
			if (!_schienenIDset.add(gSchiene.id)) {
				_fehlermeldungen.add("Die Schienen-ID " + gSchiene.id + " ist doppelt!");
				return true;
			}

		return false;
	}

	private void stateRegelvalidierung() {
		// Clear
		final @NotNull List<@NotNull Long> regelVerletzungen = _ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();
		_map_kursID_dummySuS.clear(); // Wird mit Regel 9 gefüllt.
		_map_kursID_maxSuS.clear(); // Wird mit Regel 15 gefüllt.
		_map_regelID_verletzungen.clear();
		_list_verletzte_regeltypen_sortiert.clear();
		_regelverletzungen_der_faecherparallelitaet = stateRegelvalidierungTooltip4();
		_regelverletzungen_der_wahlkonflikte = stateRegelvalidierungTooltip2();

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			stateRegelvalidierung2_kurs_fixieren_in_schiene(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE))
			stateRegelvalidierung3_kurs_sperren_in_schiene(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			stateRegelvalidierung4_schueler_fixieren_in_kurs(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS))
			stateRegelvalidierung5_schueler_verbieten_in_kurs(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS))
			stateRegelvalidierung7_kurs_verbieten_mit_kurs(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS))
			stateRegelvalidierung8_kurs_zusammen_mit_kurs(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(r); // keine Regel, die verletzt werden kann.

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN))
			stateRegelvalidierung10_lehrkraefte_beachten(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH))
			stateRegelvalidierung11_schueler_zusammen_mit_schueler_in_fach(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			stateRegelvalidierung12_schueler_verbieten_mit_schueler_in_fach(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			stateRegelvalidierung13_schueler_zusammen_mit_schueler(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			stateRegelvalidierung14_schueler_verbieten_mit_schueler(r, regelVerletzungen, _map_regelID_verletzungen);

		for (final @NotNull GostBlockungRegel r : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL))
			stateRegelvalidierung15_kurs_maximale_schueleranzahl(r, regelVerletzungen, _map_regelID_verletzungen);

		// stateRegelvalidierung16 ist nicht nötig
		// stateRegelvalidierung17 ist nicht nötig

		// Fülle die Liste der verletzten Regeltypen in einer bestimmten Sortierung (kann später geändert werden).
		final @NotNull int[] regeltypSortierung = new int[] {1, 6, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14, 15};
		for (final int regeltyp : regeltypSortierung)
			if (_map_regelID_verletzungen.containsKey(regeltyp))
				_list_verletzte_regeltypen_sortiert.add(GostKursblockungRegelTyp.fromTyp(regeltyp));

		// Die Bewertung im DatenManager aktualisieren.
		_parent.ergebnisUpdateBewertung(_ergebnis);

		updateAll();
	}

	private @NotNull String stateRegelvalidierungTooltip2() {
		final @NotNull StringBuilder sb = new StringBuilder();

		// Nichtwahlen des Schülers.
		int wahlkonflikte = 0;
		int wahlkonflikte_ignored = 0;
		for (final long idSchueler : _map2D_schuelerID_fachID_kurs.getKeySet())
			for (final @NotNull Entry<@NotNull Long, GostBlockungsergebnisKurs> e : _map2D_schuelerID_fachID_kurs.getSubMapOrException(idSchueler).entrySet())
				if (e.getValue() == null) {
					if (wahlkonflikte < 10) {
						final long idFach = e.getKey();
						final int kursart = _parent.schuelerGetOfFachFachwahl(idSchueler, idFach).kursartID;
						sb.append(_parent.toStringSchuelerSimple(idSchueler) + " ist im Fach " + _parent.toStringFachartSimple(idFach, kursart) + " keinem Kurs zugeordnet.\n");
					} else {
						wahlkonflikte_ignored++;
					}
					wahlkonflikte++;
				}

		// Kollisionen des Schülers.
		for (final long idSchueler : _map2D_schuelerID_schienenID_kurse.getKeySet())
			for (final @NotNull Entry<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> e : _map2D_schuelerID_schienenID_kurse.getSubMapOrException(idSchueler).entrySet()) {
				final Set<@NotNull GostBlockungsergebnisKurs> set = e.getValue();
				if (set == null)
					continue;
				if (set.size() <= 1)
					continue;

				final @NotNull ArrayList<@NotNull GostBlockungsergebnisKurs> list = new ArrayList<@NotNull GostBlockungsergebnisKurs>(set);

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

		return "Wahlkonflikte = " + wahlkonflikte + "\n" + sb.toString() + (wahlkonflikte_ignored == 0 ? "" : "+" + wahlkonflikte_ignored + " weitere Konflikte.");
	}

	private @NotNull String stateRegelvalidierungTooltip4() {
		final @NotNull StringBuilder sb = new StringBuilder();

		for (int nr = 1; nr <= _map_schienenNr_schiene.size(); nr++) {
			final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneEmitNr(nr);
			final @NotNull String proSchiene = stateRegelvalidierungTooltip4proSchiene(schiene.id);
			if (!proSchiene.isEmpty())
				sb.append("Schiene " + nr + ":\n" + proSchiene);
		}

		return sb.toString();
	}

	private @NotNull String stateRegelvalidierungTooltip4proSchiene(final long idSchiene) {
		final @NotNull StringBuilder sb = new StringBuilder();

		for (final long idFachart : _fachartmenge_sortiert) {
			final @NotNull String proFachart = stateRegelvalidierungTooltip4proSchieneUndFachart(idSchiene, idFachart);
			if (!proFachart.isEmpty())
				sb.append(proFachart + "\n");
		}

		return sb.toString();
	}

	private @NotNull String stateRegelvalidierungTooltip4proSchieneUndFachart(final long idSchiene, final long idFachart) {
		final @NotNull StringBuilder sb = new StringBuilder();

		if (_map2D_schienenID_fachartID_kurse.contains(idSchiene, idFachart)) {
			final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursGruppe = _map2D_schienenID_fachartID_kurse.getNonNullOrException(idSchiene, idFachart);
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

	private void stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		for (int schienenNr = r.parameter.get(1).intValue(); schienenNr <= r.parameter.get(2).intValue(); schienenNr++)
			for (final GostBlockungsergebnisKurs eKurs : getSchieneEmitNr(schienenNr).kurse)
				if (eKurs.kursart == r.parameter.get(0).intValue()) {
					regelVerletzungen.add(r.id);
					MapUtils.addToList(mapRegelVerletzungen, 1, "Kursart " + getOfKursName(eKurs.id) + " sollte nicht auf Schiene " + schienenNr + " liegen.");
				}
	}

	private void stateRegelvalidierung2_kurs_fixieren_in_schiene(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idKurs = r.parameter.get(0);
		final int schienenNr = r.parameter.get(1).intValue();
		if (!getOfKursSchienenmenge(idKurs).contains(getSchieneEmitNr(schienenNr))) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 2, "Kurs " + getOfKursName(idKurs) + " sollte fixiert sein in Schiene " + schienenNr + ".");
		}
	}

	private void stateRegelvalidierung3_kurs_sperren_in_schiene(final @NotNull GostBlockungRegel r, final  @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idKurs = r.parameter.get(0);
		final int schienenNr = r.parameter.get(1).intValue();
		if (getOfKursSchienenmenge(idKurs).contains(getSchieneEmitNr(schienenNr))) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 3, "Kurs " + getOfKursName(idKurs) + " sollte gesperrt sein in Schiene " + schienenNr + ".");
		}
	}

	private void stateRegelvalidierung4_schueler_fixieren_in_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idSchueler = r.parameter.get(0);
		final long idKurs = r.parameter.get(1);
		if (!getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 4, getOfSchuelerNameVorname(idSchueler) + " sollte fixiert sein in Kurs " + getOfKursName(idKurs) + ".");
		}
	}

	private void stateRegelvalidierung5_schueler_verbieten_in_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idSchueler = r.parameter.get(0);
		final long idKurs = r.parameter.get(1);
		if (getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 5, getOfSchuelerNameVorname(idSchueler) + " sollte verboten sein in Kurs " + getOfKursName(idKurs) + ".");
		}
	}

	private void stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		for (final GostBlockungsergebnisKurs eKurs : _map_kursID_kurs.values())
			for (final @NotNull Long eSchieneID : eKurs.schienen) {
				final int nr  = getSchieneG(eSchieneID).nummer;
				final int kursart = r.parameter.get(0).intValue();
				final int schienenNrVon  = r.parameter.get(1).intValue();
				final int schienenNrBis  = r.parameter.get(2).intValue();

				final boolean b1 = eKurs.kursart == kursart;
				final boolean b2 = (schienenNrVon <= nr) && (nr <= schienenNrBis);
				if (b1 != b2) {
					regelVerletzungen.add(r.id);
					MapUtils.addToList(mapRegelVerletzungen, 6, "Kursart von " + getOfKursName(eKurs.id) + " sollte innerhalb der Schienen " +  schienenNrVon + " bis " + schienenNrBis + " sein.");
				}
			}
	}

	private void stateRegelvalidierung7_kurs_verbieten_mit_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idKurs1 = r.parameter.get(0);
		final long idKurs2 = r.parameter.get(1);
		for (final @NotNull GostBlockungsergebnisSchiene schiene1 : getOfKursSchienenmenge(idKurs1))
			for (final @NotNull GostBlockungsergebnisSchiene schiene2 : getOfKursSchienenmenge(idKurs2))
				if (schiene1 == schiene2) {
					regelVerletzungen.add(r.id);
					final int nr  = getSchieneG(schiene1.id).nummer;
					MapUtils.addToList(mapRegelVerletzungen, 7, "Kurs " + getOfKursName(idKurs1) + " und Kurs " +  getOfKursName(idKurs2) + " sollten nicht gemeinsam in einer Schiene (" + nr + ") sein.");
				}
	}

	private void stateRegelvalidierung8_kurs_zusammen_mit_kurs(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idKurs1 = r.parameter.get(0);
		final long idKurs2 = r.parameter.get(1);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> set1 = getOfKursSchienenmenge(idKurs1);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> set2 = getOfKursSchienenmenge(idKurs2);
		if (set1.size() < set2.size()) {
			// "set1" muss in "set2" enthalten sein (da es dass kleinere ist).
			for (final @NotNull GostBlockungsergebnisSchiene schiene1 : set1)
				if (!set2.contains(schiene1)) {
					regelVerletzungen.add(r.id);
					MapUtils.addToList(mapRegelVerletzungen, 8, "Kurs " + getOfKursName(idKurs1) + " und Kurs " +  getOfKursName(idKurs2) + " sollten gemeinsam in einer Schiene sein.");
				}
		} else {
			// "set2" muss in "set1" enthalten sein, da es dass kleinere ist.
			for (final @NotNull GostBlockungsergebnisSchiene schiene2 : set2)
				if (!set1.contains(schiene2)) {
					regelVerletzungen.add(r.id);
					MapUtils.addToList(mapRegelVerletzungen, 8, "Kurs " + getOfKursName(idKurs1) + " und Kurs " +  getOfKursName(idKurs2) + " sollten gemeinsam in einer Schiene sein.");
				}
		}
	}

	private void stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(final @NotNull GostBlockungRegel r) {
		final long idKurs = r.parameter.get(0);
		final int anzahl = r.parameter.get(1).intValue();
		DeveloperNotificationException.ifTrue("Regel 9: " + _parent.toStringKurs(idKurs) + " mit SuS-Anzahl " + anzahl + " ist ungültig!", (anzahl < 1) || (anzahl > 99));
		DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_dummySuS, idKurs, anzahl); // map wird vorher geleert
	}

	private void stateRegelvalidierung10_lehrkraefte_beachten(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _map_schienenID_schiene.values())
			for (final @NotNull GostBlockungsergebnisKurs eKurs1 : eSchiene.kurse)
				for (final @NotNull GostBlockungsergebnisKurs eKurs2 : eSchiene.kurse)
					if (eKurs1.id < eKurs2.id)
						for (final @NotNull GostBlockungKursLehrer gLehr1 : getKursG(eKurs1.id).lehrer)
							for (final @NotNull GostBlockungKursLehrer gLehr2 : getKursG(eKurs2.id).lehrer)
								if (gLehr1.id == gLehr2.id) {
									regelVerletzungen.add(r.id);
									final int nr  = getSchieneG(eSchiene.id).nummer;
									MapUtils.addToList(mapRegelVerletzungen, 10, "Kurs " + getOfKursName(eKurs1.id) + " und Kurs " + getOfKursName(eKurs2.id) + " haben die Lehrkraft " + gLehr1.kuerzel + " in der selben Schiene (" + nr + ").");
								}
	}

	private void stateRegelvalidierung11_schueler_zusammen_mit_schueler_in_fach(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);
		final long idFach = r.parameter.get(2);
		final @NotNull GostFach fach = getFach(idFach);

		if (!_parent.schuelerGetHatFach(idSchueler1, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 11, getOfSchuelerNameVorname(idSchueler1) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", aber eine Regel, die das Fach definiert.");
			return;
		}

		if (!_parent.schuelerGetHatFach(idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 11, getOfSchuelerNameVorname(idSchueler2) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", aber eine Regel, die das Fach definiert.");
			return;
		}

		if (!_parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 11, getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2) + " haben nicht die selbe Kursart bei " + fach.kuerzelAnzeige + ".");
			return;
		}

		if (!getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 11, getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2) + " sollten gemeinsam in " + fach.kuerzelAnzeige + " sein.");
		}

	}

	private void stateRegelvalidierung12_schueler_verbieten_mit_schueler_in_fach(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);
		final long idFach = r.parameter.get(2);
		final @NotNull GostFach fach = getFach(idFach);

		if (!_parent.schuelerGetHatFach(idSchueler1, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 12, getOfSchuelerNameVorname(idSchueler1) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", hat aber eine Regel, die das Fach definiert.");
			return;
		}

		if (!_parent.schuelerGetHatFach(idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 12, getOfSchuelerNameVorname(idSchueler2) + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", hat aber eine Regel, die das Fach definiert.");
			return;
		}

		if (!_parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 12, getOfSchuelerNameVorname(idSchueler1) + " und SchülerIn " + getOfSchuelerNameVorname(idSchueler2) + " haben nicht die selbe Kursart bei " + fach.kuerzelAnzeige + ".");
			return;
		}

		if (getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 12, getOfSchuelerNameVorname(idSchueler1) + " und SchülerIn " + getOfSchuelerNameVorname(idSchueler2) + " sollten nicht gemeinsam in " + fach.kuerzelAnzeige + " sein.");
		}

	}

	private void stateRegelvalidierung13_schueler_zusammen_mit_schueler(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);

		for (final @NotNull GostFach fach : _parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2))
			if (!getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				regelVerletzungen.add(r.id);
				MapUtils.addToList(mapRegelVerletzungen, 13, getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2) + " sollten gemeinsam in " + fach.kuerzelAnzeige + " sein.");
			}
	}

	private void stateRegelvalidierung14_schueler_verbieten_mit_schueler(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idSchueler1 = r.parameter.get(0);
		final long idSchueler2 = r.parameter.get(1);

		for (final @NotNull GostFach fach : _parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2))
			if (getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				regelVerletzungen.add(r.id);
				MapUtils.addToList(mapRegelVerletzungen, 14, getOfSchuelerNameVorname(idSchueler1) + " und " + getOfSchuelerNameVorname(idSchueler2) + " sollten nicht gemeinsam in " + fach.kuerzelAnzeige + " sein.");
			}
	}

	private void stateRegelvalidierung15_kurs_maximale_schueleranzahl(final @NotNull GostBlockungRegel r, final @NotNull List<@NotNull Long> regelVerletzungen, final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>> mapRegelVerletzungen) {
		final long idKurs = r.parameter.get(0);
		final int maxSuS = r.parameter.get(1).intValue();
		DeveloperNotificationException.ifTrue("Regel 15: " + _parent.toStringKurs(idKurs) + " maximale SuS-Anzahl = " + maxSuS + " ist ungültig!", (maxSuS < 0) || (maxSuS > 100));
		DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_maxSuS, idKurs, maxSuS); // map wird vorher geleert
		final int sus = getOfKursAnzahlSchuelerPlusDummy(idKurs);

		if (sus > maxSuS) {
			regelVerletzungen.add(r.id);
			MapUtils.addToList(mapRegelVerletzungen, 15, "Kurs " + getOfKursName(idKurs) + " hat " + sus + " SuS, sollte aber nicht mehr als " + maxSuS + " haben.");
		}
	}


	/**
	 * Fügt den Schüler dem Kurs hinzu.<br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, wird der Schüler nicht hinzugefügt.
	 *          Stattdessen wird die ungültige Wahl in einer Map gespeichert.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursHinzufuegen(final long idSchueler, final long idKurs) {
		stateSchuelerKursHinzufuegenOhneRevalidierung(idSchueler, idKurs);
		stateRegelvalidierung();
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
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final long fachID = kurs.fachID;

		// Ungültige Wahlen extra behandeln.
		if (!getOfSchuelerHatFachwahl(idSchueler, fachID, kurs.kursart)) {
			stateSchuelerKursUngueltigeWahlHinzufuegen(idSchueler, kurs);
			return;
		}

		// Wurde das Fach bereits einem anderen Kurs zugeordnet?
		if (getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID) != null) // wirft ggf. Exception
			return;

		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(idSchueler);
		final @NotNull Set<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(idKurs);
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.add(idSchueler); // Data-Objekt aktualisieren
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(idSchueler);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		_map2D_schuelerID_fachID_kurs.put(idSchueler, fachID, kurs);
		stateKursdifferenzUpdate(fachartID);
		for (final @NotNull Long schieneID : kurs.schienen)
			stateSchuelerSchieneHinzufuegenOhneRegelvalidierung(idSchueler, schieneID, kurs);
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.<br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, so wird der Schüler aus der zuvor gespeichert
	 *          Zuordnung aller ungültigen Wahlen gelöscht.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private void stateSchuelerKursEntfernen(final long idSchueler, final long idKurs) {
		stateSchuelerKursEntfernenOhneRevalidierung(idSchueler, idKurs);
		stateRegelvalidierung();
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
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final long fachID = kurs.fachID;

		// Ungültige Wahlen extra behandeln.
		if (!getOfSchuelerHatFachwahl(idSchueler, fachID, kurs.kursart)) {
			stateSchuelerKursUngueltigeWahlEntfernen(idSchueler, kurs);
			return;
		}

		// Der Kurs ist derzeit gar nicht zugeordnet!
		if (getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID) != kurs) // wirft ggf. Exception
			return;

		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(idSchueler);
		final @NotNull Set<@NotNull Long> schuelerIDsOfKurs = getOfKursSchuelerIDmenge(idKurs);
		final long fachartID = GostKursart.getFachartID(fachID, kurs.kursart);

		// Hinzufügen
		kurs.schueler.remove(idSchueler); // Data-Objekt aktualisieren
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(idSchueler);
		_ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		_map2D_schuelerID_fachID_kurs.put(idSchueler, fachID, null);
		stateKursdifferenzUpdate(fachartID);
		for (final @NotNull Long schieneID : kurs.schienen)
			stateSchuelerSchieneEntfernenOhneRegelvalidierung(idSchueler, schieneID, kurs);
	}

	private void stateSchuelerKursUngueltigeWahlHinzufuegen(final long idSchueler, final @NotNull GostBlockungsergebnisKurs kurs) {
		MapUtils.getOrCreateHashSet(_map_schuelerID_ungueltige_kurse, idSchueler).add(kurs);
	}

	private void stateSchuelerKursUngueltigeWahlEntfernen(final long idSchueler, final @NotNull GostBlockungsergebnisKurs kurs) {
	    final @NotNull Set<@NotNull GostBlockungsergebnisKurs> set = DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_ungueltige_kurse, idSchueler);
		set.remove(kurs);
		if (set.isEmpty())
			_map_schuelerID_ungueltige_kurse.remove(idSchueler);
	}

	/**
	 * Fügt den Kurs der Schiene hinzu.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private void stateKursSchieneHinzufuegen(final long idKurs, final long idSchiene) {
		stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs, idSchiene);
		stateRegelvalidierung();
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
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> setSchienenOfKurs = getOfKursSchienenmenge(idKurs);
		final long idFach = kurs.fachID;
		final long idFachart = GostKursart.getFachartID(idFach, kurs.kursart);
		final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursGruppe = _map2D_schienenID_fachartID_kurse.getNonNullOrException(idSchiene, idFachart);

		// Hinzufügen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListAddsDuplicate("kurs.schienen", kurs.schienen, schiene.id); // Data-Objekt aktualisieren
		DeveloperNotificationException.ifListAddsDuplicate("schiene.kurse", schiene.kurse, kurs);       // Data-Objekt aktualisieren
		DeveloperNotificationException.ifSetAddsDuplicate("setSchienenOfKurs", setSchienenOfKurs, schiene);
		for (final @NotNull Long schuelerID : kurs.schueler)
			stateSchuelerSchieneHinzufuegenOhneRegelvalidierung(schuelerID, schiene.id, kurs);
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += kursGruppe.isEmpty() ? 0 : 1;
		DeveloperNotificationException.ifListAddsDuplicate("kursGruppe", kursGruppe, kurs);  // Muss nach der Bewertung passieren.
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene Die Datenbank-ID der Schiene.
	 */
	private void stateKursSchieneEntfernen(final long idKurs, final long idSchiene) {
		stateKursSchieneEntfernenOhneRegelvalidierung(idKurs, idSchiene);
		stateRegelvalidierung();
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene Die Datenbank-ID der Schiene.
	 */
	private void stateKursSchieneEntfernenOhneRegelvalidierung(final long idKurs, final long idSchiene) {
		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);
		final @NotNull GostBlockungsergebnisSchiene schiene = getSchieneE(idSchiene);
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> setSchienenOfKurs = getOfKursSchienenmenge(idKurs);
		final long idFach = kurs.fachID;
		final long idFachart = GostKursart.getFachartID(idFach, kurs.kursart);
		final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursGruppe = _map2D_schienenID_fachartID_kurse.getNonNullOrException(idSchiene, idFachart);

		// Entfernen
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListRemoveFailes("kurs.schienen", kurs.schienen, schiene.id); // Data-Objekt aktualisieren
		DeveloperNotificationException.ifListRemoveFailes("schiene.kurse", schiene.kurse, kurs);       // Data-Objekt aktualisieren
		DeveloperNotificationException.ifSetRemoveFailes("setSchienenOfKurs", setSchienenOfKurs, schiene);
		for (final @NotNull Long schuelerID : kurs.schueler)
			stateSchuelerSchieneEntfernenOhneRegelvalidierung(schuelerID, schiene.id, kurs);
		_ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListRemoveFailes("kursGruppe", kursGruppe, kurs); // Muss vor der Bewertung passieren.
		_ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene -= kursGruppe.isEmpty() ? 0 : 1;
	}

	private void stateSchuelerSchieneHinzufuegenOhneRegelvalidierung(final long idSchueler, final long idSchiene, final @NotNull GostBlockungsergebnisKurs kurs) {
		// Schiene --> Integer (erhöhen)
		final int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(idSchiene);
		_map_schienenID_schuelerAnzahl.put(idSchiene, schieneSchuelerzahl + 1);

		// Schiene --> Schüler --> Kurse (erhöhen)
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kursmenge = _map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene);
		kursmenge.add(kurs);

		// Kollisionen erhöhen?
		if (kursmenge.size() > 1) {
			// Kollisionen der Schiene.
			final int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene);
			_map_schienenID_kollisionen.put(idSchiene, schieneKollisionen + 1);

			// Kollisionen des Schülers.
			final int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(idSchueler);
			_map_schuelerID_kollisionen.put(idSchueler, schuelerKollisionen + 1);

			// Kollisionen insgesamt.
			_ergebnis.bewertung.anzahlSchuelerKollisionen++;
		}
	}

	private void stateSchuelerSchieneEntfernenOhneRegelvalidierung(final long idSchueler, final long idSchiene, final @NotNull GostBlockungsergebnisKurs kurs) {
		// // Schiene --> Integer (verringern)
		final int schieneSchuelerzahl = getOfSchieneAnzahlSchueler(idSchiene);
		DeveloperNotificationException.ifTrue(_parent.toStringSchueler(idSchueler) + " entfernen aus " + _parent.toStringKurs(kurs.id) + " / " + _parent.toStringSchiene(idSchiene) +  " unmöglich, da Schienen-SuS-Anzahl = " + schieneSchuelerzahl + "!", schieneSchuelerzahl <= 0);
		_map_schienenID_schuelerAnzahl.put(idSchiene, schieneSchuelerzahl - 1);

		// Schiene --> Schüler --> Integer (verringern)
		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kursmenge = _map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene);
		kursmenge.remove(kurs);

		// Kollisionen verringern?
		if (!kursmenge.isEmpty()) {
			// Kollisionen der Schiene.
			final int schieneKollisionen = getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene);
			DeveloperNotificationException.ifTrue(_parent.toStringSchiene(idSchiene) + " hat " + schieneKollisionen + " Kollisionen --> Entfernen unmöglich!", schieneKollisionen <= 0);
			_map_schienenID_kollisionen.put(idSchiene, schieneKollisionen - 1);

			// Kollisionen des Schülers.
			final int schuelerKollisionen = getOfSchuelerAnzahlKollisionen(idSchueler);
			DeveloperNotificationException.ifTrue(_parent.toStringSchueler(idSchiene) + " hat " + schuelerKollisionen + " Kollisionen --> Entfernen unmöglich!", schuelerKollisionen <= 0);
			_map_schuelerID_kollisionen.put(idSchueler, schuelerKollisionen - 1);

			// Kollisionen insgesamt.
			DeveloperNotificationException.ifTrue("Gesamtkollisionen = " + _ergebnis.bewertung.anzahlSchuelerKollisionen + " --> Entfernen unmöglich!", _ergebnis.bewertung.anzahlSchuelerKollisionen <= 0);
			_ergebnis.bewertung.anzahlSchuelerKollisionen--;
		}
	}

	private void stateKursdifferenzUpdate(final long idFachart) {
		// Den ersten Kurs der Fachart holen.
		final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursmenge = getOfFachartKursmenge(idFachart);

		// Neue Kursdifferenz berechnen
		int min = _parent.fachwahlGetListeOfFachart(idFachart).size();
		int max = 0;
		for (final @NotNull GostBlockungsergebnisKurs kurs : kursmenge) {
			// Wichtig: Kurse die zu ignorieren sind, müssen beachtet werden!
			final @NotNull LongArrayKey keyIgnoreID = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, kurs.id});
			if (_parent.regelGetByLongArrayKeyOrNull(keyIgnoreID) != null)
				continue;

			// Wichtig: DummySuS müssen beachtet werden!
			final int size = kurs.schueler.size() + getOfKursAnzahlSchuelerDummy(kurs.id);
			min = Math.min(min, size);
			max = Math.max(max, size);
		}
		int newKD = max - min;

		// Sonderfall: Falls es alle Kursdifferenzen ignoriert werden sollen.
		if (newKD < 0)
			newKD = 0;

		// Kursdifferenz ändert sich nicht.
		final int oldKD = getOfFachartKursdifferenz(idFachart);
		if (newKD == oldKD)
			return;

		// Kursdifferenz-Map aktualisieren.
		_map_fachartID_kursdifferenz.put(idFachart, newKD);

		// Kursdifferenz-Histogramm aktualisieren.
		final int[] kursdifferenzen = _ergebnis.bewertung.kursdifferenzHistogramm;
		kursdifferenzen[oldKD]--;
		kursdifferenzen[newKD]++;

		// Kursdifferenz-Max aktualisieren.
		if (oldKD == _ergebnis.bewertung.kursdifferenzMax) {
			if (newKD > oldKD) {
				// Neues Maximum
				_ergebnis.bewertung.kursdifferenzMax = newKD;
			} else {
				// Neues Minimum
				if (kursdifferenzen[oldKD] == 0)
					_ergebnis.bewertung.kursdifferenzMax = newKD;
			}
		}
	}

	// #########################################################################
	// ##########           Allgemeine Anfragen                       ##########
	// #########################################################################

	private void updateAll() {
		// _fachartmenge_sortiert sortieren.
		if (_fachartmenge_sortierung == 1) {
			_fachartmenge_sortiert.sort(_fachartComparator_kursart_fach);
		} else {
			_fachartmenge_sortiert.sort(_fachartComparator_fach_kursart);
		}

		// _map_fachartID_kurse: Zugeordnete Listen sortieren.
		for (final @NotNull Long idFachart : _map_fachartID_kurse.keySet()) {
			final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursmenge = DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kurse, idFachart);
			if (_fachartmenge_sortierung == 1) {
				kursmenge.sort(_kursComparator_kursart_fach_kursnummer);
			} else {
				kursmenge.sort(_kursComparator_fach_kursart_kursnummer);
			}
		}

		// Kursmenge pro Schiene sortieren.
		for (@NotNull final GostBlockungsergebnisSchiene schiene : _ergebnis.schienen) {
			final @NotNull List<@NotNull GostBlockungsergebnisKurs> kursmenge =  schiene.kurse;
			if (_fachartmenge_sortierung == 1) {
				kursmenge.sort(_kursComparator_kursart_fach_kursnummer);
			} else {
				kursmenge.sort(_kursComparator_fach_kursart_kursnummer);
			}
		}

	}

	/**
	 * Liefert die Anzahl an externen SuS.
	 *
	 * @return die Anzahl an externen SuS.
	 */
	public int getAnzahlSchuelerExterne() {
		return ListUtils.getCountFiltered(_parent.daten().schueler,  (final @NotNull Schueler schueler) -> getOfSchuelerHatStatusExtern(schueler.id));
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS.
	 *
	 * @return die Anzahl an Dummy-SuS.
	 */
	public int getAnzahlSchuelerDummy() {
		int summe = 0;
		for (final long idKurs : _map_kursID_dummySuS.keySet())
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
	 * Liefert das Blockungsergebnis ohne ungültige Schüler-Kurs-Zuordnungen.
	 * <br>Hinweis: Siehe auch {@link #getErgebnisInklusiveUngueltigerWahlen()}.
	 *
	 * @return das Blockungsergebnis ohne ungültige Schüler-Kurs-Zuordnungen.
	 */
	public @NotNull GostBlockungsergebnis getErgebnis() {
		return _ergebnis;
	}


	/**
	 * Liefert das Blockungsergebnis inklusive ungültiger Schüler-Kurs-Zuordnungen.
	 * <br>Hinweis: Siehe auch {@link #getErgebnis()}.
	 *
	 * @return  das Blockungsergebnis inklusive ungültiger Schüler-Kurs-Zuordnungen.
	 */
	public @NotNull GostBlockungsergebnis getErgebnisInklusiveUngueltigerWahlen() {
		final @NotNull GostBlockungsergebnis copy = deepCopyErgebnis(_ergebnis);

		// Ungültige Zuordnungen hinzufügen
		for (final @NotNull Entry<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> entry : _map_schuelerID_ungueltige_kurse.entrySet())
			for (final @NotNull @NotNull GostBlockungsergebnisKurs kurs1 : entry.getValue())
				for (final @NotNull GostBlockungsergebnisSchiene schiene : copy.schienen)
					for (final @NotNull GostBlockungsergebnisKurs kurs2 : schiene.kurse)
						if (kurs1.id == kurs2.id)
							kurs2.schueler.add(entry.getKey());

		return copy;
	}

	/**
	 * Liefert die Menge (meistens eine) aller Fehlermeldungen.
	 * <br>Falls die Liste nicht leer ist, sollte die GUI den Benutzer warnen, dass die Blockung nicht vollständig geladen wurde!
	 *
	 * @return die Menge (meistens eine) aller Fehlermeldungen.
	 */
	public @NotNull List<@NotNull String> getFehlermeldungen() {
		return _fehlermeldungen;
	}

	/**
	 * Liefert den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 *
	 * @return den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 */
	public GostBlockungsdatenManager getParent() {
		return _parent;
	}

	/**
	 * Liefert eine tiefe Kopie des Blockungsergebnisses.
	 *
	 * @param e  Das zu kopierende GostBlockungsergebnis.
	 *
	 * @return eine tiefe Kopie des Blockungsergebnisses.
	 */
	public static @NotNull GostBlockungsergebnis deepCopyErgebnis(final @NotNull GostBlockungsergebnis e) {
		// Normale Kopie
		final @NotNull GostBlockungsergebnis copy = new GostBlockungsergebnis();
		copy.id = e.id;
		copy.blockungID = e.blockungID;
		copy.name = e.name;
		copy.gostHalbjahr = e.gostHalbjahr;
		copy.istAktiv = e.istAktiv;
		for (final @NotNull GostBlockungsergebnisSchiene schiene : e.schienen)
			copy.schienen.add(deepCopyBewertung(schiene)); // deep copy
		copy.bewertung = deepCopyBewertung(e.bewertung);  // deep copy
		return copy;
	}

	private static @NotNull GostBlockungsergebnisBewertung deepCopyBewertung(final @NotNull GostBlockungsergebnisBewertung b) {
		final @NotNull GostBlockungsergebnisBewertung copy = new GostBlockungsergebnisBewertung();
		copy.regelVerletzungen.addAll(b.regelVerletzungen); // copy Long-Values
		copy.anzahlKurseNichtZugeordnet = b.anzahlKurseNichtZugeordnet;
		copy.anzahlSchuelerNichtZugeordnet = b.anzahlSchuelerNichtZugeordnet;
		copy.anzahlSchuelerKollisionen = b.anzahlSchuelerKollisionen;
		copy.kursdifferenzMax = b.kursdifferenzMax;
		copy.kursdifferenzHistogramm = deepCopyArray(b.kursdifferenzHistogramm); // deep copy
		copy.anzahlKurseMitGleicherFachartProSchiene = b.anzahlKurseMitGleicherFachartProSchiene;
		return copy;
	}

	private static @NotNull int[] deepCopyArray(final @NotNull int[] a) {
		final int[] copy = new int[a.length];
		System.arraycopy(a, 0, copy, 0, a.length);
		return copy;
	}

	private static @NotNull GostBlockungsergebnisSchiene deepCopyBewertung(final @NotNull GostBlockungsergebnisSchiene s) {
		final @NotNull GostBlockungsergebnisSchiene copy = new GostBlockungsergebnisSchiene();
		copy.id = s.id;
		for (final @NotNull GostBlockungsergebnisKurs kurs : s.kurse)
			copy.kurse.add(deepCopyKurs(kurs)); // deep copy
		return copy;
	}

	private static @NotNull GostBlockungsergebnisKurs deepCopyKurs(final @NotNull GostBlockungsergebnisKurs k) {
		final @NotNull GostBlockungsergebnisKurs copy = new GostBlockungsergebnisKurs();
		copy.id = k.id;
		copy.fachID = k.fachID;
		copy.kursart = k.kursart;
		copy.anzahlSchienen = k.anzahlSchienen;
		copy.schueler.addAll(k.schueler); // copy Long-Values
		copy.schienen.addAll(k.schienen); // copy Long-Values
		return copy;
	}

	/**
	 * Liefert eine Güte eines Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 *
	 * @param value   der Wert des Bewertungskriteriums
	 *
	 * @return die Güte des Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public static double getOfBewertungFarbcodeStatic(final int value) {
		return 1 - 1 / (0.25 * value + 1);
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
		return getOfBewertung1WertStatic(_ergebnis.bewertung);
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
	public static double getOfBewertung1FarbcodeStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return getOfBewertungFarbcodeStatic(getOfBewertung1WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung1Farbcode() {
		return getOfBewertung1FarbcodeStatic(_ergebnis.bewertung);
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
	public static int getOfBewertung2WertStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
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
		return getOfBewertung2WertStatic(_ergebnis.bewertung);
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
	public static double getOfBewertung2FarbcodeStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return getOfBewertungFarbcodeStatic(getOfBewertung2WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung2Farbcode() {
		return getOfBewertung2FarbcodeStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public static int getOfBewertung3WertStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public int getOfBewertung3Wert() {
		return getOfBewertung3WertStatic(_ergebnis.bewertung);
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
	public static @NotNull int[] getOfBewertung3HistogrammStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		final @NotNull int[] histo  = new int[10];

		for (int i = 0; i < histo.length; i++)
			histo[i] = bewertung.kursdifferenzHistogramm.length >= histo.length ? bewertung.kursdifferenzHistogramm[i] : 0;

		return histo;
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 * <br>- Das Histogramm der ersten 10 Kursdifferenzen (Kursdifferenz 0 bis Kursdifferenz 9).
	 * <br>- Das Histogramm hat eine garantierte Länge von 10.
	 *
	 * @return den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 */
	public @NotNull int[] getOfBewertung3Histogramm() {
		return getOfBewertung3HistogrammStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public static double getOfBewertung3FarbcodeStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		int wert = getOfBewertung3WertStatic(bewertung);
		if (wert > 0)
			wert--; // Jede Kursdifferenz wird um 1 reduziert, außer die 0.
		return getOfBewertungFarbcodeStatic(wert);
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public double getOfBewertung3Farbcode() {
		return getOfBewertung3FarbcodeStatic(_ergebnis.bewertung);
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
	public static int getOfBewertung4WertStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
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
		return getOfBewertung4WertStatic(_ergebnis.bewertung);
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
	public static double getOfBewertung4FarbcodeStatic(final @NotNull GostBlockungsergebnisBewertung bewertung) {
		return getOfBewertungFarbcodeStatic(getOfBewertung4WertStatic(bewertung));
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
		return getOfBewertung4FarbcodeStatic(_ergebnis.bewertung);
	}

	/**
	 * Liefert die Anzahl an Schülerkollisionen.<br>
	 * Ist ein Schüler x mal in einer Schiene und ist x > 1, dann wird dies als x-1 Kollisionen gezählt.
	 *
	 * @return die Anzahl an Schülerkollisionen.
	 */
	public int getOfBewertungAnzahlKollisionen() {
		return _ergebnis.bewertung.anzahlSchuelerKollisionen;
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
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfFachKursmenge(final long idFach) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachID_kurse, idFach);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getOfFachAnzahlSchuelerMaennlich(final long idFach) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getOfFachAnzahlSchuelerWeiblich(final long idFach) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getOfFachAnzahlSchuelerDivers(final long idFach) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getOfFachAnzahlSchuelerOhneAngabe(final long idFach) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public int getOfFachAnzahlSchuelerSchriftlich(final long idFach) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 */
	public int getOfFachAnzahlSchuelerMuendlich(final long idFach) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", null, GostSchriftlichkeit.MUENDLICH);
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
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfFachartKursmenge(final long idFachart) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kurse, idFachart);
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kursdifferenz, idFachart);
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachartID_kursdifferenz, idFachart);
	}

	/**
	 * Liefert den Namen der Fachart, z. B. D-LK.
	 *
	 * @param idFachart  Die ID der Fachart.
	 *
	 * @return den Namen der Fachart, z. B. D-LK.
	 */
	public String getOfFachartName(final long idFachart) {
		final long idFach = GostKursart.getFachID(idFachart);
		final int idKursart = GostKursart.getKursartID(idFachart);
		return _parent.faecherManager().getOrException(idFach).kuerzelAnzeige + "-" + GostKursart.fromID(idKursart).kuerzel;
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getOfFachartAnzahlSchuelerMaennlich(final long idFach, final int idKursart) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getOfFachartAnzahlSchuelerWeiblich(final long idFach, final int idKursart) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getOfFachartAnzahlSchuelerDivers(final long idFach, final int idKursart) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getOfFachartAnzahlSchuelerOhneAngabe(final long idFach, final int idKursart) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public int getOfFachartAnzahlSchuelerSchriftlich(final long idFach, final int idKursart) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 */
	public int getOfFachartAnzahlSchuelerMuendlich(final long idFach, final int idKursart) {
		return getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", null, GostSchriftlichkeit.MUENDLICH);
	}

	/**
	 * Liefert die Menge aller Facharten (Fach + Kursart) sortiert nach der aktuellen Sortiervariante.
	 * <br>Hinweis: Die Sortierung lässt sich mit {@link #kursSetSortierungFachKursartNummer()} und {@link #kursSetSortierungKursartFachNummer()} ändern.
	 *
	 * @return die Menge aller Facharten (Fach + Kursart) sortiert nach der aktuellen Sortiervariante.
	 */
	public @NotNull List<@NotNull Long> getOfFachartMengeSortiert() {
		return _fachartmenge_sortiert;
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach LK/GK, dann nach der Fachsortierung, zuletzt nach der Kursnummer.
	 */
	public void kursSetSortierungKursartFachNummer() {
		_fachartmenge_sortierung = 1;
		updateAll();
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach der Fachsortierung, dann nach LK/GK, zuletzt nach der Kursnummer.
	 */
	public void kursSetSortierungFachKursartNummer() {
		_fachartmenge_sortierung = 2;
		updateAll();
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
	public @NotNull Schueler getSchuelerG(final long idSchueler) throws DeveloperNotificationException {
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
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmenge(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_kurse, idSchueler);
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
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmengeSortiert(final long idSchueler) {
		final List<@NotNull GostBlockungsergebnisKurs> list = new ArrayList<>();
		list.addAll(DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_kurse, idSchueler));

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
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchuelerKursmengeMitKollisionen(final long idSchueler) {
		// Muss ein Set sein, da ein Multikurs sonst zu Dopplungen führen kann.
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();

		for (final @NotNull GostBlockungSchiene schiene : _parent.schieneGetListe()) {
			final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseDerSchiene = _map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, schiene.id);
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
	public @NotNull List<@NotNull GostFachwahl> getOfSchuelerFachwahlmengeOhneKurszuordnung(final long idSchueler) {
		final @NotNull List<@NotNull GostFachwahl> list = _parent.schuelerGetListeOfFachwahlen(idSchueler);
		final @NotNull Predicate<@NotNull GostFachwahl> filter = (final @NotNull GostFachwahl t) -> (getOfSchuelerOfFachZugeordneterKurs(idSchueler, t.fachID) == null);
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
		final int nIst = DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_kurse, idSchueler).size();
		final int nSoll = _map2D_schuelerID_fachID_kurs.getSubMapSizeOrZero(idSchueler);
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
	public boolean getOfSchuelerHatFach(final long idSchueler, final long idFach) {
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_kollisionen, idSchueler) > 0;
	}

	/**
	 * Liefert die Anzahl an Kollisionen des Schülers.<br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return die Anzahl an Kollisionen des Schülers.
	 */
	public int getOfSchuelerAnzahlKollisionen(final long idSchueler) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_kollisionen, idSchueler);
	}

	/**
	 * Liefert die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 *
	 * @return Die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 */
	public int getOfSchuelerAnzahlMitKollisionenOderNichtwahlen() {
		return getOfSchuelerAnzahlGefiltert(-1, -1, -1, 3, "", null, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getOfSchuelerAnzahlMaennlich() {
		return getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getOfSchuelerAnzahlWeiblich() {
		return getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getOfSchuelerAnzahlDivers() {
		return getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getOfSchuelerAnzahlOhneAngabe() {
		return getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.X, null);
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
	public int getOfSchuelerAnzahlGefiltert(final long idKurs, final long idFach, final int idKursart, final int konfliktTyp, final @NotNull String subString, final Geschlecht geschlecht, final GostSchriftlichkeit schriftlichkeit) {
		int summe = 0;

		for (final @NotNull Schueler schueler : _parent.schuelerGetListe())
			if (getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, geschlecht, schriftlichkeit))
				summe++;

		return summe;
	}

	/**
	 * Liefert TRUE, falls sämtliche Fachwahlen aller SuS noch nicht zugeordnet sind.
	 *
	 * @return TRUE, falls sämtliche Fachwahlen aller SuS noch nicht zugeordnet sind.
	 */
	public boolean getOfSchuelerAlleFachwahlenNichtZugeordnet() {
		return _ergebnis.bewertung.anzahlSchuelerNichtZugeordnet == _parent.daten().fachwahlen.size();
	}

	/**
	 * Liefert die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idSchiene   Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchuelerOfSchieneKursmenge(final long idSchueler, final long idSchiene) {
		return _map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene);
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
		return _map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene).size() > 1;
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
		return _map2D_schuelerID_fachID_kurs.getOrNull(idSchueler, idFach);
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schuelerIDs, idKurs).contains(idSchueler);
//		final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs)
//		final @NotNull Set<@NotNull GostBlockungsergebnisKurs> kurseOfSchueler = getOfSchuelerKursmenge(idSchueler)
//		return kurseOfSchueler.contains(kurs)
	}

	/**
	 * Liefert ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	private @NotNull SchuelerblockungOutput getOfSchuelerNeuzuordnungMitFixierung(final long idSchueler, final boolean fixiereBelegteKurse) {
		System.out.println("getOfSchuelerNeuzuordnung " + idSchueler + ", " + fixiereBelegteKurse);
		// SCHUELER_FIXIEREN_IN_KURS, SCHUELER_VERBIETEN_IN_KURS
		// SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH, SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH
		// SCHUELER_ZUSAMMEN_MIT_SCHUELER, SCHUELER_VERBIETEN_MIT_SCHUELER
		// KURS_MAXIMALE_SCHUELERANZAHL
		// Konstruiere die Eingabedaten "input".
		final @NotNull SchuelerblockungInput input = new SchuelerblockungInput();
		input.schienen = _parent.schieneGetAnzahl();

		// Sammle alle Facharten des Schülers...
		for (final @NotNull GostFachwahl fachwahl : _parent.schuelerGetListeOfFachwahlen(idSchueler)) {
			input.fachwahlen.add(fachwahl);
			input.fachwahlenText.add(_parent.fachwahlGetName(fachwahl));
			final long fachartID = GostKursart.getFachartIDByFachwahl(fachwahl);

			// Sammle alle potentiellen Kurse der Fachart des Schülers...
			for (final @NotNull GostBlockungsergebnisKurs kursE : getOfFachartKursmenge(fachartID)) {
				final @NotNull SchuelerblockungInputKurs kursS = new SchuelerblockungInputKurs();
				final long idKurs = kursE.id;
				kursS.id = idKurs;
				kursS.fach = kursE.fachID;
				kursS.kursart = kursE.kursart;
				kursS.istGesperrt = getOfSchuelerOfKursIstGesperrt(idSchueler, idKurs);
				kursS.istFixiert = getOfSchuelerOfKursIstFixiert(idSchueler, idKurs)
	                               || (fixiereBelegteKurse && getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs));
				DeveloperNotificationException.ifTrue(_parent.toStringKurs(idKurs) + " von " + _parent.toStringSchueler(idSchueler) + " ist gesperrt und fixiert zugleich!", kursS.istGesperrt && kursS.istFixiert);
				kursS.anzahlSuS = getOfKursAnzahlSchueler(idKurs);
				kursS.schienen = getOfKursSchienenNummern(idKurs);
				input.kurse.add(kursS);
			}
		}

		// Sonderfall: Der Schüler hat 0 Fachwahlen oder alle Fachwahlen haben 0 Kurse.
		if (input.kurse.isEmpty())
			return new SchuelerblockungOutput();

		// Berechne die Zuordnung und gib sie zurück.
		return new SchuelerblockungAlgorithmus().handle(input);
	}

	/**
	 * Liefert ein {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse beinhaltet.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse beinhaltet.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate getOfSchuelerNeuzuordnung(final long idSchueler, final boolean fixiereBelegteKurse) {
		final @NotNull SchuelerblockungOutput zuordnung = getOfSchuelerNeuzuordnungMitFixierung(idSchueler, fixiereBelegteKurse);

		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final @NotNull SchuelerblockungOutputFachwahlZuKurs z : zuordnung.fachwahlenZuKurs) {
			// Kurs des Faches 'vorher'.
			final GostBlockungsergebnisKurs kursV = getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);
			// Kurs des Faches 'nachher'.
			final GostBlockungsergebnisKurs kursN = z.kursID < 0 ? null : getKursE(z.kursID);
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
	 * Liefert TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 */
	public boolean getOfSchuelerOfKursIstFixiert(final long idSchueler, final long idKurs) {
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
	public boolean getOfSchuelerOfKursIstLK(final long idSchueler, final long idKurs) {
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
	public boolean getOfSchuelerOfKursIstAB3(final long idSchueler, final long idKurs) {
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
	public boolean getOfSchuelerOfKursIstLKoderAB3(final long idSchueler, final long idKurs) {
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
	public boolean getOfSchuelerOfKursIstAB4(final long idSchueler, final long idKurs) {
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
	public boolean getOfSchuelerOfKursIstAbiturfach(final long idSchueler, final long idKurs) {
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
	public boolean getOfSchuelerOfKursIstSchriftlich(final long idSchueler, final long idKurs) {
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
	public boolean getOfSchuelerHatImNamenSubstring(final long idSchueler, final @NotNull String subString) {
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
	public @NotNull Geschlecht getOfSchuelerGeschlechtOrException(final long idSchueler) throws DeveloperNotificationException {
		final @NotNull Schueler schueler = getSchuelerG(idSchueler);
		final Geschlecht geschlecht = Geschlecht.fromValue(schueler.geschlecht);
		return DeveloperNotificationException.ifNull("Das Geschlecht des Schülers " + _parent.toStringSchueler(idSchueler) + " ist nicht definiert!", geschlecht);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 */
	public boolean getOfSchuelerHatStatusExtern(final @NotNull Long idSchueler) {
		return getSchuelerG(idSchueler).status == SchuelerStatus.EXTERN.id;
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
	public @NotNull GostFachwahl getOfSchuelerOfFachFachwahl(final long idSchueler, final long idFach) {
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
	public @NotNull List<@NotNull Schueler> getOfSchuelerMengeGefiltert(final long idKurs, final long idFach, final int idKursart, final int konfliktTyp, final @NotNull String subString) {
		final @NotNull List<@NotNull Schueler> menge = new ArrayList<>();

		for (final @NotNull Schueler schueler : _parent.schuelerGetListe())
			if (getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, null, null))
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
	public boolean getOfSchuelerErfuelltKriterien(final long idSchueler, final long idKurs, final long idFach, final int idKursart, final int konfliktTyp, @NotNull final String subString, final Geschlecht geschlecht, final GostSchriftlichkeit schriftlichkeit) {

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
			if ((schriftlichkeit != null) && (schriftlichkeit.getIstSchriftlichOrException() != getOfSchuelerOfKursFachwahl(idSchueler, idKurs).istSchriftlich))
				return false;
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
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisKurs>> getOfSchuelerMapIDzuUngueltigeKurse() {
		return _map_schuelerID_ungueltige_kurse;
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
		final GostBlockungsergebnisKurs kurs1 = _map2D_schuelerID_fachID_kurs.getOrNull(idSchueler1, idFach);
		final GostBlockungsergebnisKurs kurs2 = _map2D_schuelerID_fachID_kurs.getOrNull(idSchueler2, idFach);
		return (kurs1 == null) || (kurs2 == null) ? false : kurs1.id == kurs2.id;
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_kursID_kurs, idKurs);
	}

	/**
	 * Gibt die Menge der {@link GostBlockungsergebnisKurs} zurück.
	 *
	 * @return die Menge der {@link GostBlockungsergebnisKurs}
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getKursmenge() {
		final @NotNull List<@NotNull GostBlockungsergebnisKurs> result = new ArrayList<>();
		result.addAll(_map_kursID_kurs.values());
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
		final @NotNull Set<@NotNull GostBlockungsergebnisSchiene> schienenOfKurs = getOfKursSchienenmenge(idKurs);
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
	public boolean getOfKursOfSchienenNrIstZugeordnet(final long idKurs, final int schienenNr) {
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
	public boolean getOfKursOfSchieneIstFixiert(final long idKurs, final long idSchiene) {
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
	public @NotNull Set<@NotNull Long> getOfKursSchuelerIDmenge(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schuelerIDs, idKurs);
	}

	/**
	 * Liefert die Menge aller Schüler-Objekte des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-Objekte des Kurses.
	 */
	public @NotNull List<@NotNull Schueler> getOfKursSchuelermenge(final long idKurs) {
		final @NotNull List<@NotNull Schueler> list = new ArrayList<>();

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
	public @NotNull Set<@NotNull GostBlockungsergebnisSchiene> getOfKursSchienenmenge(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schienen, idKurs);
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
	public boolean getOfKursHatKollision(final long idKurs) {
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
	public int getOfKursAnzahlKollisionen(final long idKurs) {
		return getOfKursSchuelermengeMitKollisionen(idKurs).size();
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 *
	 * @param idKursID  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 */
	public @NotNull Set<@NotNull Long> getOfKursSchuelermengeMitKollisionen(final long idKursID) {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();

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
	public int getOfKursAnzahlSchuelerPlusDummy(final long idKurs) {
		return getKursE(idKurs).schueler.size() + MapUtils.getOrDefault(_map_kursID_dummySuS, idKurs, 0);
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
		return MapUtils.getOrDefault(_map_kursID_dummySuS, idKurs, 0);
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
		return ListUtils.getCountFiltered(kursE.schueler,  (final @NotNull Long idSchueler) -> getOfSchuelerHatStatusExtern(idSchueler));
	}

	/**
	 * Liefert die Anzahl nicht externer SuS die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl nicht externer SuS die dem Kurs zugeordnet sind.
	 */
	public int getOfKursAnzahlSchuelerNichtExtern(final long idKurs) {
		final @NotNull GostBlockungsergebnisKurs kursE = getKursE(idKurs);
		return ListUtils.getCountFiltered(kursE.schueler, (final @NotNull Long idSchueler) -> !getOfSchuelerHatStatusExtern(idSchueler));
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public int getOfKursAnzahlSchuelerMaennlich(final long idKurs) {
		return getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public int getOfKursAnzahlSchuelerWeiblich(final long idKurs) {
		return getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public int getOfKursAnzahlSchuelerDivers(final long idKurs) {
		return getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public int getOfKursAnzahlSchuelerOhneAngabe(final long idKurs) {
		return getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.X, null);
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
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn mündlich belegt haben.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn mündlich belegt haben.
	 */
	public int getOfKursAnzahlSchuelerMuendlich(final long idKurs) {
		return getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH);
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
			final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
			if ((fachwahl.abiturfach != null) && ((fachwahl.abiturfach == 1) || (fachwahl.abiturfach == 2)))
				summe++;
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
			final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
			if ((fachwahl.abiturfach != null) && (fachwahl.abiturfach == 3))
				summe++;
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
			final @NotNull GostFachwahl fachwahl = getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
			if ((fachwahl.abiturfach != null) && (fachwahl.abiturfach == 4))
				summe++;
		}
		return summe;
	}

	/**
	 * Liefert TRUE, falls der Kurs keine Schüler enthält und somit ein Löschen des Kurses erlaubt ist.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs keine Schüler enthält und somit ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public boolean getOfKursRemoveAllowed(final long idKurs) throws DeveloperNotificationException {
		return getOfKursAnzahlSchueler(idKurs) == 0;
	}

	/**
	 * Liefert die Menge aller Schüler eines Kurses, die noch nicht fixiert sind.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler eines Kurses, die noch nicht fixiert sind.
	 */
	public @NotNull List<@NotNull Schueler> getOfKursMengeAllerNichtFixiertenSchueler(final long idKurs) {
		final @NotNull List<@NotNull Schueler> list = new ArrayList<>();

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
	public @NotNull List<@NotNull Schueler> getOfKursMengeAllerNichtFixiertenAbiturSchueler(final long idKurs) {
		final @NotNull List<@NotNull Schueler> list = new ArrayList<>();

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
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull Long>> getMappingKursIDSchuelerIDs() {
		return _map_kursID_schuelerIDs;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public @NotNull Map<@NotNull Long, @NotNull Set<@NotNull GostBlockungsergebnisSchiene>> getMappingKursIDSchienenmenge() {
		return _map_kursID_schienen;
	}

	/**
	 * Liefert eine Menge aller Kurse mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Kurse mit mindestens einer Kollision.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getMengeDerKurseMitKollisionen() {
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
		for (final @NotNull GostBlockungsergebnisKurs kurs : _map_kursID_kurs.values())
			if (getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	private static @NotNull List<@NotNull GostBlockungKurs> regelGetListeToggleFilteredBetween(final @NotNull List<@NotNull GostBlockungKurs> list, final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB) {
		final @NotNull List<@NotNull GostBlockungKurs> result = new ArrayList<>();
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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetListeToggleSperrung(final @NotNull List<@NotNull GostBlockungKurs> list, final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB, final @NotNull GostBlockungSchiene schieneA, final @NotNull GostBlockungSchiene schieneB) {
		final int min = Math.min(schieneA.nummer, schieneB.nummer);
		final int max = Math.max(schieneA.nummer, schieneB.nummer);
		final @NotNull List<@NotNull GostBlockungRegel> regeln = new ArrayList<>();

		for (final @NotNull GostBlockungKurs kursG : regelGetListeToggleFilteredBetween(list, kursA, kursB))
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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetListeToggleKursfixierung(final @NotNull List<@NotNull GostBlockungKurs> list, final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB, final @NotNull GostBlockungSchiene schieneA, final @NotNull GostBlockungSchiene schieneB) {
		final int min = Math.min(schieneA.nummer, schieneB.nummer);
		final int max = Math.max(schieneA.nummer, schieneB.nummer);
		final @NotNull List<@NotNull GostBlockungRegel> regeln = new ArrayList<>();

		for (final @NotNull GostBlockungKurs kursG : regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (final @NotNull GostBlockungsergebnisSchiene schieneE :  DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schienen, kursG.id)) {
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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetListeToggleSchuelerfixierung(final @NotNull List<@NotNull GostBlockungKurs> list, final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB, final @NotNull GostBlockungSchiene schieneA, final @NotNull GostBlockungSchiene schieneB) {
		final int min = Math.min(schieneA.nummer, schieneB.nummer);
		final int max = Math.max(schieneA.nummer, schieneB.nummer);
		final @NotNull List<@NotNull GostBlockungRegel> regeln = new ArrayList<>();

		for (final @NotNull GostBlockungKurs kursG : regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (final @NotNull GostBlockungsergebnisSchiene schieneE :  DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schienen, kursG.id)) {
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
	 * Liefert die Regel-Menge aller aktuellen Kurs-Schienen-Fixierungen.
	 *
	 * @return die Regel-Menge aller aktuellen Kurs-Schienen-Fixierungen.
	 */
	public @NotNull List<@NotNull GostBlockungRegel> regelGetMengeAllerKursSchienenFixierungen() {
		return new ArrayList<>(_parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE));
	}

	/**
	 * Liefert die Regel-Menge aller Kurs-Schienen-Fixierungen eines bestimmten Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel-Menge aller Kurs-Schienen-Fixierungen eines bestimmten Kurses.
	 */
	public @NotNull List<@NotNull GostBlockungRegel> regelGetMengeAnKursSchienenFixierungenDesKurses(final long idKurs) {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetMengeAnKursSchienenFixierungenDerKurse(final @NotNull List<@NotNull Long> listeDerKursIDs) {
		// List<ID> zu Set<ID>, damit man schnell auf Existenz überprüfen kann.
		final @NotNull HashSet<@NotNull Long> setKursIDs = new HashSet<>(listeDerKursIDs);
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if (setKursIDs.contains(regel.parameter.get(0)))
				list.add(regel);

		return list;
	}

	/**
	 * Liefert die Regel-Menge aller Schüler-Kurs-Fixierungen.
	 *
	 * @return die Regel-Menge aller Schüler-Kurs-Fixierungen.
	 */
	public @NotNull List<@NotNull GostBlockungRegel> regelGetMengeAllerSchuelerKursFixierungen() {
		return new ArrayList<>(_parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS));
	}

	/**
	 * Liefert die Regel-Menge aller Schüler-Kurs-Fixierungen des übergebenen Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel-Menge aller Schüler-Kurs-Fixierungen des übergebenen Kurses.
	 */
	public @NotNull List<@NotNull GostBlockungRegel> regelGetMengeAllerSchuelerKursFixierungenDesKurses(final long idKurs) {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetMengeAllerSchuelerKursFixierungenDerKurse(final @NotNull List<@NotNull Long> listeDerKursIDs) {
		// List<ID> zu Set<ID>, damit man schnell auf Existenz überprüfen kann.
		final @NotNull HashSet<@NotNull Long> setKursIDs = new HashSet<>(listeDerKursIDs);

		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungRegel regel : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			if (setKursIDs.contains(regel.parameter.get(1)))
				list.add(regel);

		return list;
	}

	/**
	 * Liefert die Menge aller {@link GostKursblockungRegelTyp}, welche mindestens eine Regelverletzung haben.
	 *
	 * @return die Menge aller {@link GostKursblockungRegelTyp}, welche mindestens eine Regelverletzung haben.
	 */
	final @NotNull List<@NotNull GostKursblockungRegelTyp> regelGetMengeVerletzterTypen() {
		return _list_verletzte_regeltypen_sortiert;
	}

	/**
	 * Liefert zu einem {@link GostKursblockungRegelTyp} die Menge aller Verletzungen als textuelle Beschreibung.
	 *
	 * @param regeltyp  Das {@link GostKursblockungRegelTyp}-Objekt.
	 *
	 * @return zu einem {@link GostKursblockungRegelTyp} die Menge aller Verletzungen als textuelle Beschreibung.
	 */
	public @NotNull List<@NotNull String> regelGetMengeAnVerletzungen(final @NotNull GostKursblockungRegelTyp regeltyp) {
		return MapUtils.getOrCreateArrayList(_map_regelID_verletzungen, regeltyp.typ);
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 * <br>Hinweis: Falls ein Kurs bereits fixierte Schienen hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 */
	public @NotNull List<@NotNull GostBlockungRegel> regelGetDummyMengeAllerKursSchienenFixierungen() {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungsergebnisKurs kurs : _map_kursID_kurs.values())
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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetDummyMengeAnKursSchienenFixierungen(final @NotNull List<@NotNull Long> listeDerKursIDs) {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetDummyMengeAllerSchuelerKursFixierungen() {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungsergebnisKurs kurs : _map_kursID_kurs.values())
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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetDummyMengeAllerSchuelerAbiturKursFixierungen() {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull GostBlockungsergebnisKurs kurs : _map_kursID_kurs.values())
			for (final @NotNull Schueler schueler : getOfKursSchuelermenge(kurs.id))
				if ((getOfSchuelerOfKursIstAbiturfach(schueler.id, kurs.id)) &&  (!getOfSchuelerOfKursIstFixiert(schueler.id, kurs.id)))
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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetDummyMengeAnKursSchuelerFixierungen(final @NotNull List<@NotNull Long> listeDerKursIDs) {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

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
	public @NotNull List<@NotNull GostBlockungRegel> regelGetDummyMengeAnAbiturKursSchuelerFixierungen(final @NotNull List<@NotNull Long> listeDerKursIDs) {
		final @NotNull List<@NotNull GostBlockungRegel> list = new ArrayList<>();

		for (final @NotNull Long idKurs : listeDerKursIDs)
			for (final @NotNull Schueler schueler : getOfKursSchuelermenge(idKurs))
				if ((getOfSchuelerOfKursIstAbiturfach(schueler.id, idKurs)) &&  (!getOfSchuelerOfKursIstFixiert(schueler.id, idKurs)))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, idKurs));

		return list;
	}

	/**
	 * Liefert einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 *
	 * @return einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 */
	final @NotNull String regelGetTooltipFuerFaecherparallelitaet() {
		return _regelverletzungen_der_faecherparallelitaet;
	}

	/**
	 * Liefert einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 *
	 * @return einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 */
	final @NotNull String regelGetTooltipFuerWahlkonflikte() {
		return _regelverletzungen_der_wahlkonflikte;
	}

	private static boolean regelupdateIsEqualPair(final long a1, final long a2, final long b1, final long b2) {
		return ((a1 == b1) && (a2 == b2)) || ((a1 == b2) && (a2 == b1));
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_01_KURSART_SPERRE_SCHIENEN_VON_BIS(final int kursart, final int schienenNrVon, final int schienenNrBis) {
		final int von = Math.min(schienenNrVon, schienenNrBis);
		final int bis = Math.max(schienenNrVon, schienenNrBis);
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final @NotNull GostBlockungsergebnisKurs kurs : getKursmenge())
			for (int schienenNr = von; schienenNr <= bis; schienenNr++)
				if (kurs.kursart == kursart) { // Kursart und Schienenbereich stimmen.
					// (1)
					final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr});
					final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung != null)
						u.listEntfernen.add(regelSperrung);
					// (2)
					final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr});
					final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (regelFixierung != null)
						u.listEntfernen.add(regelFixierung);
				}

		// (3a)
		final @NotNull LongArrayKey keyBisVon = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, bis, von});
		final GostBlockungRegel regelBisVon = _parent.regelGetByLongArrayKeyOrNull(keyBisVon);
		if (regelBisVon != null)
			u.listEntfernen.add(regelBisVon);

		// (3)
		final @NotNull LongArrayKey keyVonBis = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis});
		if (_parent.regelGetByLongArrayKeyOrNull(keyVonBis) == null)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis));

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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(final int kursart, final int schienenNrVon, final int schienenNrBis) {
		final int von = Math.min(schienenNrVon, schienenNrBis);
		final int bis = Math.max(schienenNrVon, schienenNrBis);
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		for (final @NotNull @NotNull GostBlockungRegel rGleicheKursart : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			if (kursart == rGleicheKursart.parameter.get(0))
				u.listEntfernen.add(rGleicheKursart);

		// (2) (3)
		for (final @NotNull GostBlockungsergebnisKurs kurs : getKursmenge())
			for (int schienenNr = 1; schienenNr <= _parent.schieneGetAnzahl(); schienenNr++) {
				final boolean imSchienenBereich = (von <= schienenNr) && (schienenNr <= bis);
				final boolean richtigeKursart = (kurs.kursart == kursart);
				if (imSchienenBereich != richtigeKursart) { // Kursart ist im falschen Bereich.
					// (2)
					final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr});
					final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung != null)
						u.listEntfernen.add(rFixierung);
					// (3)
					final @NotNull LongArrayKey kSperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr});
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(final @NotNull Set<@NotNull Long> setKursID, final @NotNull Set<@NotNull Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1) (2) --> Markiert + Kurs
			for (final int nr : setSchienenNr)
				if (getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					// (1)
					final @NotNull LongArrayKey kSperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nr});
					final GostBlockungRegel rSperrung = _parent.regelGetByLongArrayKeyOrNull(kSperrung);
					if (rSperrung != null)
						u.listEntfernen.add(rSperrung);
					// (2)
					final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr});
					final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr));
				}

			// (3) Fixiert + kein Kurs
			for (int nr = 1; nr <= _map_schienenNr_schiene.size(); nr++)
				if (!getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr});
					final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung != null)
						u.listEntfernen.add(rFixierung);
				}
		}



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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(final @NotNull Set<@NotNull Long> setKursID, final @NotNull Set<@NotNull Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// Beim Fixieren muss man über die Kurse und dann über die Schienen des Kurses iterieren.
		for (final long idKurs : setKursID)
			for (final @NotNull GostBlockungsergebnisSchiene schieneE : DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schienen, idKurs)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					// (1)
					final @NotNull LongArrayKey keyKursInSchiene = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer});
					final GostBlockungRegel regel = _parent.regelGetByLongArrayKeyOrNull(keyKursInSchiene);
					if (regel != null)
						u.listEntfernen.add(regel);
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (int nr = 1; nr <= _map_schienenNr_schiene.size(); nr++) {
				final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr});
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
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Fixierung einer Kursmenge komplett zu lösen.
	 * <br>(1) Alle Fixierungen der Kursmenge werden gelöst.
	 *
	 * @param setKursID  Die Kursmenge, deren Fixierungen gelöst werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Fixierung einer Kursmenge komplett zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (int nr = 1; nr <= _map_schienenNr_schiene.size(); nr++) {
				// (1)
				final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr});
				final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
				if (rFixierung != null)
					u.listEntfernen.add(rFixierung);
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
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_02c_KURS_FIXIERE_ALLE_IN_IHREN_SCHIENEN() {
		return regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(_parent.kursmengeGetSetDerIDs());
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_02d_KURS_FIXIERE_IN_SCHIENE_TOGGLE(final @NotNull Set<@NotNull Long> setKursID, final @NotNull Set<@NotNull Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// Beim Fixieren muss man über die Kurse und dann über die Schienen des Kurses iterieren.
		for (final long idKurs : setKursID)
			for (final @NotNull GostBlockungsergebnisSchiene schieneE :  DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schienen, idKurs)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer});
					final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);

					// (1)
					if (regelFixierung != null) {
						u.listEntfernen.add(regelFixierung);
						continue;
					}

					// (2)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer));

					// (3)
					final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer});
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
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		// (1)
		final @NotNull LongArrayKey kSperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr});
		final GostBlockungRegel rSperrung = _parent.regelGetByLongArrayKeyOrNull(kSperrung);
		if (rSperrung != null)
			u.listEntfernen.add(rSperrung);

		// (2)
		final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr});
		final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
		if (rFixierung != null)
			return u;

		// (3)
		if (!_parent.kursIstWeitereFixierungErlaubt(idKurs))
			for (int nr = 1; nr <= _map_schienenNr_schiene.size(); nr++) {
				final @NotNull LongArrayKey kFixierungAlt = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr});
				final GostBlockungRegel rFixierungAlt = _parent.regelGetByLongArrayKeyOrNull(kFixierungAlt);
				if (rFixierungAlt != null)
					u.listEntfernen.add(rFixierungAlt);
			}

		// Hinzufügen
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr));

		return u;
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
		final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr});
		final GostBlockungRegel rFixierung = _parent.regelGetByLongArrayKeyOrNull(kFixierung);
		if (rFixierung != null)
			u.listEntfernen.add(rFixierung);

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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_03_KURS_SPERRE_IN_SCHIENE(final @NotNull Set<@NotNull Long> setKursID, final @NotNull Set<@NotNull Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (final int schienenNr : setSchienenNr) {
				// (1)
				final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr});
				final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
				final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr});
				final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if ((regelSperrung == null) && (regelFixierung == null))
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr));
			}

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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_03_KURS_SPERRE_IN_SCHIENE(final @NotNull Set<@NotNull Long> setKursID, final @NotNull Set<@NotNull Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (final int schienenNr : setSchienenNr) {
				// (1)
				final @NotNull LongArrayKey keyGesperrt = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr});
				final GostBlockungRegel regelGesperrt = _parent.regelGetByLongArrayKeyOrNull(keyGesperrt);
				if (regelGesperrt != null)
					u.listEntfernen.add(regelGesperrt);
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_03b_KURS_SPERRE_IN_SCHIENE_TOGGLE(final @NotNull Set<@NotNull Long> setKursID, final @NotNull Set<@NotNull Integer> setSchienenNr) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID)
			for (final @NotNull GostBlockungsergebnisSchiene schieneE :  DeveloperNotificationException.ifMapGetIsNull(_map_kursID_schienen, idKurs)) {
				final @NotNull GostBlockungSchiene schieneG = getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					// (1)
					final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer});
					final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung != null) {
						u.listEntfernen.add(regelSperrung);
						continue;
					}
					// (2)
					final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer});
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
	 * TODO Wenn der Schüler gar nicht den Kurs wählen kann --> ignorieren.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(final @NotNull Set<@NotNull Long> setSchuelerID, final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID) {
				final @NotNull GostBlockungKurs kurs1 = _parent.kursGet(idKurs);

				// (1)
				final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs});
				final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung != null)
					u.listEntfernen.add(regelSperrung);

				// Ist da eine Fixierung (in den Kursen der selben Fachart)?
				for (final @NotNull GostBlockungKurs kurs2 : _parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
					final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id});
					final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);

					if (kurs1.id == kurs2.id) {
						if (regelFixierung == null) {
							// (2)
							u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs));
						} // ELSE (3)
					} else {
						if (regelFixierung != null) // (4)
							u.listEntfernen.add(regelFixierung);
					}
				}
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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(final @NotNull Set<@NotNull Long> setSchuelerID, final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID) {
				// (1)
				final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs});
				final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if (regelFixierung != null)
					u.listEntfernen.add(regelFixierung);
			}

		return u;
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1) (2) (3) (4)
			final @NotNull GostBlockungRegelUpdate u2 = regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1)
			final @NotNull GostBlockungRegelUpdate u2 = regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
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
		return regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(_map_kursID_kurs.keySet());
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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_04d_SCHUELER_FIXIEREN_IN_DEN_KURSEN_TOGGLE(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs1)) {
				final @NotNull LongArrayKey kFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs1});
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
						final @NotNull LongArrayKey kFixierung2 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id});
						final GostBlockungRegel rFixierung2 = _parent.regelGetByLongArrayKeyOrNull(kFixierung2);
						if (rFixierung2 != null)
							u.listEntfernen.add(rFixierung2);
					}
			}

		return u;
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
		return regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(_map_kursID_schuelerIDs.keySet());
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(final @NotNull Set<@NotNull Long> kursIDs) {
		final @NotNull HashSet<@NotNull PairNN<@NotNull Long, @NotNull Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstLK(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<@NotNull Long, @NotNull Long>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
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
		return regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(_map_kursID_schuelerIDs.keySet());
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(final @NotNull Set<@NotNull Long> kursIDs) {
		final @NotNull HashSet<@NotNull PairNN<@NotNull Long, @NotNull Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstAB3(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<@NotNull Long, @NotNull Long>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
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
		return regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(_map_kursID_schuelerIDs.keySet());
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(final @NotNull Set<@NotNull Long> kursIDs) {
		final @NotNull HashSet<@NotNull PairNN<@NotNull Long, @NotNull Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstLKoderAB3(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<@NotNull Long, @NotNull Long>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
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
		return regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(_map_kursID_schuelerIDs.keySet());
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(final @NotNull Set<@NotNull Long> kursIDs) {
		final @NotNull HashSet<@NotNull PairNN<@NotNull Long, @NotNull Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstAB4(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<@NotNull Long, @NotNull Long>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
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
		return regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(_map_kursID_schuelerIDs.keySet());
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(final @NotNull Set<@NotNull Long> kursIDs) {
		final @NotNull HashSet<@NotNull PairNN<@NotNull Long, @NotNull Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstAbiturfach(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<@NotNull Long, @NotNull Long>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
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
		return regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(_map_kursID_schuelerIDs.keySet());
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(final @NotNull Set<@NotNull Long> kursIDs) {
		final @NotNull HashSet<@NotNull PairNN<@NotNull Long, @NotNull Long>> schuelerKursPaare = new HashSet<>();

		for (final long idKurs : kursIDs)
			for (final long idSchueler : getOfKursSchuelerIDmenge(idKurs))
				if (getOfSchuelerOfKursIstSchriftlich(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<@NotNull Long, @NotNull Long>(idSchueler, idKurs));

		return regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
	}

	/**
	 * Liefert alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 *
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param schuelerKursPaare  Die Menge aller Schüler-Kurs-Paare.
	 *
	 * @return alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 */
	private @NotNull GostBlockungRegelUpdate regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(final @NotNull Set<@NotNull PairNN<@NotNull Long, @NotNull Long>> schuelerKursPaare) {
		// TODO Wenn der Schüler gar nicht den Kurs wählen kann --> ignorieren.
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final @NotNull PairNN<@NotNull Long, @NotNull Long> pair : schuelerKursPaare) {
			final @NotNull Long idSchueler = pair.a;
			final @NotNull Long idKurs = pair.b;
			final @NotNull GostBlockungKurs kurs1 = _parent.kursGet(idKurs);

			// (1)
			final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs});
			final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
			if (regelSperrung != null)
				u.listEntfernen.add(regelSperrung);

			// (2) (3)
			for (final @NotNull GostBlockungKurs kurs2 : _parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
				final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id});
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(final @NotNull Set<@NotNull Long> setSchuelerID, final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID) {
				// (1)
				final @NotNull LongArrayKey keyFixierung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs});
				final GostBlockungRegel regelFixierung = _parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if (regelFixierung != null)
					u.listEntfernen.add(regelFixierung);
				// (2)
				final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs});
				final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung == null)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs));
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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_05_SCHUELER_VERBIETEN_IN_KURS(final @NotNull Set<@NotNull Long> setSchuelerID, final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID)
			for (final long idKurs : setKursID) {
				// (1)
				final @NotNull LongArrayKey keySperrung = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs});
				final GostBlockungRegel regelSperrung = _parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung != null)
					u.listEntfernen.add(regelSperrung);
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_05b_SCHUELER_VERBIETEN_IN_DEN_KURSEN(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1) (2)
			final @NotNull GostBlockungRegelUpdate u2 = regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_05b_SCHUELER_VERBIETEN_IN_DEN_KURSEN(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			// (1)
			final @NotNull GostBlockungRegelUpdate u2 = regelupdateRemove_05_SCHUELER_VERBIETEN_IN_KURS(getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}

		return u;
	}


	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B zusammen sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel mit bereits existiert, aber die IDs nicht aufsteigend sind, wird dies entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_07_KURS_VERBIETEN_MIT_KURS(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyZusammen12 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2});
					final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 != null)
						u.listEntfernen.add(regelZusammen12);
					// (1b)
					final @NotNull LongArrayKey keyZusammen21 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1});
					final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 != null)
						u.listEntfernen.add(regelZusammen21);
					// (2)
					final @NotNull LongArrayKey keyVerboten21 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1});
					final GostBlockungRegel regelVerboten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 != null)
						u.listEntfernen.add(regelVerboten21);
					// (3)
					final @NotNull LongArrayKey keyVerboten12 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2});
					final GostBlockungRegel regelVerboten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2));
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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_07_KURS_VERBIETEN_MIT_KURS(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyVerboten12 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2});
					final GostBlockungRegel regelVerboten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 != null)
						u.listEntfernen.add(regelVerboten12);
					// (1b)
					final @NotNull LongArrayKey keyVerboten21 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1});
					final GostBlockungRegel regelVerboten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 != null)
						u.listEntfernen.add(regelVerboten21);
				}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B verboten sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel mit bereits existiert, aber die IDs nicht aufsteigend sind, wird dies entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 */
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_08_KURS_ZUSAMMEN_MIT_KURS(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyVerboten12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2 });
					final GostBlockungRegel regelVerboten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 != null)
						u.listEntfernen.add(regelVerboten12);
					// (1b)
					final @NotNull LongArrayKey keyVerboten21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1 });
					final GostBlockungRegel regelVerboten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 != null)
						u.listEntfernen.add(regelVerboten21);
					// (2)
					final @NotNull LongArrayKey keyZusammen21 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1});
					final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 != null)
						u.listEntfernen.add(regelZusammen21);
					// (3)
					final @NotNull LongArrayKey keyZusammen12 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2});
					final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 == null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2));
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
	public @NotNull GostBlockungRegelUpdate regelupdateRemove_08_KURS_ZUSAMMEN_MIT_KURS(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs1 : setKursID)
			for (final long idKurs2 : setKursID)
				if (idKurs1 < idKurs2) {
					// (1a)
					final @NotNull LongArrayKey keyZusammen12 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2});
					final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 != null)
						u.listEntfernen.add(regelZusammen12);
					// (1b)
					final @NotNull LongArrayKey keyZusammen21 = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1});
					final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 != null)
						u.listEntfernen.add(regelZusammen21);
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

		final @NotNull LongArrayKey keyDummyAlt = new LongArrayKey(new long[] { GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ});
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(final long idSchueler1, final long idSchueler2, final long idFach) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idS1 = Math.min(idSchueler1, idSchueler2);
		final long idS2 = Math.max(idSchueler1, idSchueler2);

		// (1)
		if (idS1 == idS2)
			return u;

		// (2a)
		final @NotNull LongArrayKey keyVerbietenFach12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach});
		final GostBlockungRegel regelVerbietenFach12 = _parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach12);
		if (regelVerbietenFach12 != null)
			u.listEntfernen.add(regelVerbietenFach12);

		// (2b)
		final @NotNull LongArrayKey keyVerbietenFach21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach});
		final GostBlockungRegel regelVerbietenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach21);
		if (regelVerbietenFach21 != null)
			u.listEntfernen.add(regelVerbietenFach21);

		// (3a)
		final @NotNull LongArrayKey keyVerbieten12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2});
		final GostBlockungRegel regelVerbieten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten12);
		if (regelVerbieten12 != null)
			u.listEntfernen.add(regelVerbieten12);

		// (3b)
		final @NotNull LongArrayKey keyVerbieten21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1});
		final GostBlockungRegel regelVerbieten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten21);
		if (regelVerbieten21 != null)
			u.listEntfernen.add(regelVerbieten21);

		// (4a)
		final @NotNull LongArrayKey keyZusammen12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2});
		final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
		if (regelZusammen12 != null)
			u.listEntfernen.add(regelZusammen12);

		// (4b)
		final @NotNull LongArrayKey keyZusammen21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1});
		final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
		if (regelZusammen21 != null)
			u.listEntfernen.add(regelZusammen21);

		// (5a)
		final @NotNull LongArrayKey keyZusammenFach21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach});
		final GostBlockungRegel regelZusammenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammenFach21);
		if (regelZusammenFach21 != null)
			u.listEntfernen.add(regelZusammenFach21);

		// (5b)
		final @NotNull LongArrayKey keyZusammenFach12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach});
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(final long idSchueler1, final long idSchueler2, final long idFach) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();
		final long idS1 = Math.min(idSchueler1, idSchueler2);
		final long idS2 = Math.max(idSchueler1, idSchueler2);

		// (1)
		if (idS1 == idS2)
			return u;

		// (2a)
		final @NotNull LongArrayKey keyZusammenFach12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach});
		final GostBlockungRegel regelZusammenFach12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammenFach12);
		if (regelZusammenFach12 != null)
			u.listEntfernen.add(regelZusammenFach12);

		// (2b)
		final @NotNull LongArrayKey keyZusammenFach21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach});
		final GostBlockungRegel regelZusammenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammenFach21);
		if (regelZusammenFach21 != null)
			u.listEntfernen.add(regelZusammenFach21);

		// (3a)
		final @NotNull LongArrayKey keyZusammen12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2});
		final GostBlockungRegel regelZusammen12 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
		if (regelZusammen12 != null)
			u.listEntfernen.add(regelZusammen12);

		// (3b)
		final @NotNull LongArrayKey keyZusammen21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1});
		final GostBlockungRegel regelZusammen21 = _parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
		if (regelZusammen21 != null)
			u.listEntfernen.add(regelZusammen21);

		// (4a)
		final @NotNull LongArrayKey keyVerbieten12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2});
		final GostBlockungRegel regelVerbieten12 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten12);
		if (regelVerbieten12 != null)
			u.listEntfernen.add(regelVerbieten12);

		// (4b)
		final @NotNull LongArrayKey keyVerbieten21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1});
		final GostBlockungRegel regelVerbieten21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbieten21);
		if (regelVerbieten21 != null)
			u.listEntfernen.add(regelVerbieten21);

		// (5a)
		final @NotNull LongArrayKey keyVerbietenFach21 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach});
		final GostBlockungRegel regelVerbietenFach21 = _parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach21);
		if (regelVerbietenFach21 != null)
			u.listEntfernen.add(regelVerbietenFach21);

		// (5b)
		final @NotNull LongArrayKey keyVerbietenFach12 = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach});
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
			if (regelupdateIsEqualPair(r11.parameter.get(0), r11.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r11);

		// (2)
		for (final @NotNull GostBlockungRegel r12 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			if (regelupdateIsEqualPair(r12.parameter.get(0), r12.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r12);

		// (3)
		for (final @NotNull GostBlockungRegel r13 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			if (regelupdateIsEqualPair(r13.parameter.get(0), r13.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r13);

		// (4)
		for (final @NotNull GostBlockungRegel r14 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			if (regelupdateIsEqualPair(r14.parameter.get(0), r14.parameter.get(1), idS1, idS2))
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
			if (regelupdateIsEqualPair(r11.parameter.get(0), r11.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r11);

		// (2)
		for (final @NotNull GostBlockungRegel r12 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			if (regelupdateIsEqualPair(r12.parameter.get(0), r12.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r12);

		// (3)
		for (final @NotNull GostBlockungRegel r13 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			if (regelupdateIsEqualPair(r13.parameter.get(0), r13.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r13);

		// (4)
		for (final @NotNull GostBlockungRegel r14 : _parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			if (regelupdateIsEqualPair(r14.parameter.get(0), r14.parameter.get(1), idS1, idS2))
				u.listEntfernen.add(r14);

		// (5)
		if ((0 <= idS1) && (idS1 < idS2))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2));

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Schülern eines Kurses zu setzen.
	 * <br>(1) Wenn die Regel bereits existiert, wird sie (zunächst) entfernt.
	 * <br>(2) Wenn danach die Anzahl einen Wert im Intervall [0;99], wird die Regel hinzugefügt.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 * @param anzahl  Die Anzahl an Dummy-Schülern.
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_16_SCHUELER_IGNORIEREN(final @NotNull Set<@NotNull Long> setSchuelerID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idSchueler : setSchuelerID) {
			final @NotNull LongArrayKey keySchuelerIgnorieren = new LongArrayKey(new long[] { GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler});
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
	public @NotNull GostBlockungRegelUpdate regelupdateCreate_17_KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(final @NotNull Set<@NotNull Long> setKursID) {
		final @NotNull GostBlockungRegelUpdate u = new GostBlockungRegelUpdate();

		for (final long idKurs : setKursID) {
			final @NotNull LongArrayKey keyKurs_KD_ignorieren = new LongArrayKey(new long[] { GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs});
			final GostBlockungRegel regelKurs_KD_ignorieren = _parent.regelGetByLongArrayKeyOrNull(keyKurs_KD_ignorieren);

			// (1)
			if (regelKurs_KD_ignorieren == null)
				u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel1(GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs));
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
		DeveloperNotificationException.ifTrue("Ein RegelUpdate ist nur bei einer Blockungsvorlage erlaubt!", !_parent.getIstBlockungsVorlage());
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
		return kursSchuelerUpdate_01b_LEERE_KURSMENGE(_map_kursID_schuelerIDs.keySet(), entferneAuchFixierte);
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
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_01b_LEERE_KURSMENGE(final @NotNull Set<@NotNull Long> kursIDs, final boolean entferneAuchFixierte) {
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
	 * <br>(2) Wenn der Schüler dem Kurs zugeordnet ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist.
	 *
	 * @param schuelerIDs  Die Menge der Schüler-IDs.
	 * @param idKurs       Die Datenbank-ID des Kurses aus dem die Schüler entfernt werden sollen.
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus einem Kurs zu entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_02a_ENTFERNE_SCHUELERMENGE_AUS_KURS(final @NotNull Set<@NotNull Long> schuelerIDs, final long idKurs, final boolean entferneAuchFixierte) {
		// TODO ungültige Zuordnungen überprüfen.
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		final @NotNull Set<@NotNull Long> setSchulerOfKurs = getOfKursSchuelerIDmenge(idKurs);
		for (final long idSchueler : schuelerIDs)
			if (setSchulerOfKurs.contains(idSchueler))
				if (entferneAuchFixierte || !_parent.schuelerGetIstFixiertInKurs(idSchueler, idKurs)) {
					// (1) (2)
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 * <br>(1) Wenn der Schüler den Kurs gar nicht gewählt hat, wird dies ignoriert.
	 * <br>(2) Wenn der Schüler nicht im Kurs ist, wird er hinzugefügt.
	 * <br>(3) Wenn der Schüler in einem Nachbar-Kurs ist, wird er entfernt.
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche hinzugefügt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(final @NotNull Set<@NotNull GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen) {
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : kursSchuelerZuordnungen) {
			final @NotNull GostBlockungKurs kurs1 = _parent.kursGet(z.idKurs);
			// (1)
			if (!getOfSchuelerHatFachwahl(z.idSchueler, kurs1.fach_id, kurs1.kursart))
				continue;
			// (2)
			if (!getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				u.listHinzuzufuegen.add(z);
			// (3)
			for (final @NotNull GostBlockungKurs kurs2 : _parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart))
				if ((kurs1.id != kurs2.id) && (getOfSchuelerOfKursIstZugeordnet(z.idSchueler, kurs2.id)))
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs2.id, z.idSchueler));
		}

		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 * <br>(1) Wenn der Schüler im Kurs ist, wird er entfernt.
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche entfernt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 */
	public @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate kursSchuelerUpdate_03b_ENTFERNE_KURS_SCHUELER_PAARE(final @NotNull Set<@NotNull GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen) {
		final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate u = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();

		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : kursSchuelerZuordnungen) {
			// (1)
			if (getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				u.listEntfernen.add(z);
		}

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
		// System.out.println("kursSchuelerUpdateExecute ksz-" + update.listEntfernen.size() + ", ksz+" + update.listHinzuzufuegen.size())

		// Regeln entfernen.
		if (_parent.getIstBlockungsVorlage())
			_parent.regelRemoveListe(update.regelUpdates.listEntfernen);

		// SuS entfernen.
		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : update.listEntfernen)
			if (getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs)) // vorsichtshalber!
				stateSchuelerKursEntfernenOhneRevalidierung(z.idSchueler, z.idKurs);

		// SuS hinzufügen
		for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung z : update.listHinzuzufuegen)
			if (!getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs)) // vorsichtshalber!
				stateSchuelerKursHinzufuegenOhneRevalidierung(z.idSchueler, z.idKurs);

		// Regeln hinzufügen.
		if (_parent.getIstBlockungsVorlage())
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
	public @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(final @NotNull Set<@NotNull GostBlockungsergebnisKursSchienenZuordnung> kursSchienenZuordnungen) {
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
	public @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate kursSchienenUpdate_01b_ENTFERNE_KURS_SCHIENEN_PAARE(final @NotNull Set<@NotNull GostBlockungsergebnisKursSchienenZuordnung> kursSchienenZuordnungen) {
		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate u = new GostBlockungsergebnisKursSchienenZuordnungUpdate();

		for (final @NotNull GostBlockungsergebnisKursSchienenZuordnung z : kursSchienenZuordnungen) {
			// (1)
			if (getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene))
				u.listEntfernen.add(z);
		}

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
		if (_parent.getIstBlockungsVorlage())
			_parent.regelRemoveListe(update.regelUpdates.listEntfernen);

		// Kurse aus Schienen entfernen.
		for (final @NotNull GostBlockungsergebnisKursSchienenZuordnung z : update.listEntfernen)
			if (getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene)) // vorsichtshalber!
				stateKursSchieneEntfernenOhneRegelvalidierung(z.idKurs, z.idSchiene);

		// Kurse in Schienen setzen.
		for (final @NotNull GostBlockungsergebnisKursSchienenZuordnung z : update.listHinzuzufuegen)
			if (!getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene)) // vorsichtshalber!
				stateKursSchieneHinzufuegenOhneRegelvalidierung(z.idKurs, z.idSchiene);

		// Regeln hinzufügen.
		if (_parent.getIstBlockungsVorlage())
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
	public @NotNull GostBlockungsergebnisSchiene getSchieneE(final long idSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_schiene, idSchiene);
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
	public @NotNull GostBlockungsergebnisSchiene getSchieneEmitNr(final int nrSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenNr_schiene, nrSchiene);
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenNr_schiene, nrSchiene).id;
	}

	/**
	 * Liefert die Menge aller Schienen.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnisSchiene> getMengeAllerSchienen() {
		return _ergebnis.schienen;
	}

	/**
	 * Liefert eine Menge aller Schienen mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Schienen mit mindestens einer Kollision.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisSchiene> getMengeDerSchienenMitKollisionen() {
	    return CollectionUtils.toFilteredHashSet(_map_schienenID_schiene.values(), (@NotNull final GostBlockungsergebnisSchiene s) -> getOfSchieneHatKollision(s.id));
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_schuelerAnzahl, idSchiene);
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public int getOfSchieneAnzahl() {
		return _map_schienenID_schiene.size();
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
		return DeveloperNotificationException.ifMapGetIsNull(_map_schienenID_kollisionen, idSchiene);
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public @NotNull Set<@NotNull Long> getOfSchieneSchuelermengeMitKollisionen(final long idSchiene) {
		final @NotNull HashSet<@NotNull Long> set = new HashSet<>();
		for (final @NotNull Long schuelerID : _map_schuelerID_kollisionen.keySet())
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
	public int getOfSchieneAnzahlKursmengeMitKollisionen(final long idSchiene) {
		return getOfSchieneKursmengeMitKollisionen(idSchiene).size();
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public @NotNull Set<@NotNull GostBlockungsergebnisKurs> getOfSchieneKursmengeMitKollisionen(final long idSchiene) {
		final @NotNull HashSet<@NotNull GostBlockungsergebnisKurs> set = new HashSet<>();
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
	public @NotNull List<@NotNull GostBlockungsergebnisKurs> getOfSchieneKursmengeSortiert(final long idSchiene) {
		return getSchieneE(idSchiene).kurse;
	}

	/**
	 * Liefert einen Tooltip für die Schiene, welche alle Kollisionen pro Kurs-Paarung darstellt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return einen Tooltip für die Schiene, welche alle Kollisionen pro Kurs-Paarung darstellt.
	 */
	public @NotNull String getOfSchieneTooltipKurskollisionen(final long idSchiene) {
		final StringBuilder sbZeilen = new StringBuilder();

		for (final GostBlockungsergebnisKurs kurs1 : getSchieneE(idSchiene).kurse) {
			int summe = 0;
			final StringBuilder sbZeile = new StringBuilder();

			for (final GostBlockungsergebnisKurs kurs2 : getSchieneE(idSchiene).kurse) {
				if (kurs2.id != kurs1.id) {
					final int anzahl = getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1, kurs2);
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
	public @NotNull List<@NotNull List<@NotNull Pair<@NotNull GostBlockungsergebnisKurs, @NotNull Integer>>> getOfSchieneTooltipKurskollisionenAsData(final long idSchiene) {
		final @NotNull List<@NotNull List<@NotNull Pair<@NotNull GostBlockungsergebnisKurs, @NotNull Integer>>> listOfLists = new ArrayList<>();

		for (final GostBlockungsergebnisKurs kurs1 : getSchieneE(idSchiene).kurse) {
			int summe = 0;
			final @NotNull List<@NotNull Pair<@NotNull GostBlockungsergebnisKurs, @NotNull Integer>> listOfPairs = new ArrayList<>();

			for (final GostBlockungsergebnisKurs kurs2 : getSchieneE(idSchiene).kurse) {
				if (kurs2.id != kurs1.id) {
					final int anzahl = getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1, kurs2);
					if (anzahl > 0) {
						listOfPairs.add(new Pair<@NotNull GostBlockungsergebnisKurs, @NotNull Integer>(kurs2, anzahl));
						summe += anzahl;
					}
				}
			}

			if (summe > 0) {
				listOfPairs.add(0, new Pair<@NotNull GostBlockungsergebnisKurs, @NotNull Integer>(kurs1, summe));
				listOfLists.add(listOfPairs);
			}
		}

		return listOfLists;
	}

	private static int getOfKursOfKursAnzahlGemeinsamerSchueler(final @NotNull GostBlockungsergebnisKurs kurs1, final @NotNull GostBlockungsergebnisKurs kurs2) {
		final @NotNull HashSet<Long> set = new HashSet<>();
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
	 * Verknüpft einen Kurs mit einer Schiene.
	 * Die Schiene wird anhand ihrer Nummer (nicht anhand der Datenbank-ID) identifiziert.
	 *
	 * @deprecated  Diese Methode muss in Zukunft über Kurs-Schienen-Updates erfolgen.
	 *
	 * @param  kursID      Die Datenbank-ID des Kurses.
	 * @param  schienenNr  Die Nummer der Schiene (nicht die Datenbank-ID).
	 *
	 * @throws DeveloperNotificationException falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	@Deprecated(forRemoval = true)
	public void setKursSchienenNr(final long kursID, final int schienenNr) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnisSchiene eSchiene = DeveloperNotificationException.ifMapGetIsNull(_map_schienenNr_schiene, schienenNr);
		stateKursSchieneHinzufuegen(kursID, eSchiene.id);
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene oder hebt die Verknüpfung auf.
	 *
	 * @deprecated  Diese Methode muss in Zukunft über Kurs-Schienen-Updates erfolgen.
	 *
	 * @param  idKurs                    Die Datenbank-ID des Kurses.
	 * @param  idSchiene                 Die Datenbank-ID der Schiene.
	 * @param  hinzufuegenOderEntfernen  TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @throws DeveloperNotificationException falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	@Deprecated(forRemoval = true)
	public void setKursSchiene(final long idKurs, final long idSchiene, final boolean hinzufuegenOderEntfernen) throws DeveloperNotificationException {
		if (hinzufuegenOderEntfernen)
			stateKursSchieneHinzufuegen(idKurs, idSchiene);
		else
			stateKursSchieneEntfernen(idKurs, idSchiene);
	}

	/**
	 * Verknüpft einen Schüler mit einem Kurs oder hebt die Verknüpfung auf.
	 *
	 * @deprecated  Die Kurszuordnungen sollen über Update-Objekte erfolgen.
	 *
	 * @param  idSchueler                Die Datenbank-ID des Schülers.
	 * @param  idKurs                    Die Datenbank-ID des Kurses.
	 * @param  hinzufuegenOderEntfernen  TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @throws DeveloperNotificationException  falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	@Deprecated(forRemoval = true)
	public void setSchuelerKurs(final long idSchueler, final long idKurs, final boolean hinzufuegenOderEntfernen) throws DeveloperNotificationException {
		if (hinzufuegenOderEntfernen)
			stateSchuelerKursHinzufuegen(idSchueler, idKurs);
		else
			stateSchuelerKursEntfernen(idSchueler, idKurs);
	}

	/**
	 * Geht die übergebene Fach-Zuordnungen (Fach --> Kurs) eines Schülers durch und
	 * setzt aktualisiert Veränderung die Kurs-Schüler-Zuordnung.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param zuordnung   Die gewünschte Zuordnung.
	 */
	public void setSchuelerNeuzuordnung(final long idSchueler, final @NotNull SchuelerblockungOutput zuordnung) {

		for (final @NotNull SchuelerblockungOutputFachwahlZuKurs z : zuordnung.fachwahlenZuKurs) {
			// Kurs des Faches 'vorher'.
			final GostBlockungsergebnisKurs kursV = getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);
			// Kurs des Faches 'nachher'.
			final GostBlockungsergebnisKurs kursN = z.kursID < 0 ? null : getKursE(z.kursID);
			// Bei Ungleichheit wird der Kurs gewechselt.
			if (kursV != kursN) {
				if (kursV != null)
					setSchuelerKurs(idSchueler, kursV.id, false);
				if (kursN != null)
					setSchuelerKurs(idSchueler, kursN.id, true);
			}
		}

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
		DeveloperNotificationException.ifTrue("Die Schiene " + _parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager hinzugefügt werden!", !_parent.schieneGetExistiert(idSchiene));

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
		DeveloperNotificationException.ifTrue("Die Schiene " + _parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager entfernt werden!", _parent.schieneGetExistiert(idSchiene));
		final int nKurse = getSchieneE(idSchiene).kurse.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Schiene " + _parent.toStringSchiene(idSchiene) + " hat noch " + nKurse + " Kurse!", nKurse > 0);

		// Bewertungen aktualisieren.
		stateRevalidateEverything();
	}

	/**
	 * Fügt die übergebene Regel hinzu.
	 *
	 * @param  idRegel  Die Datenbank-ID der Regel.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddRegelByID(final long idRegel) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Regel " + _parent.toStringRegel(idRegel) + " muss erst beim Datenmanager hinzugefügt werden!", !_parent.regelGetExistiert(idRegel));

		// Bewertungen aktualisieren
		stateRevalidateEverything();
	}

	/**
	 * Fügt die übergebenen Regeln hinzu.
	 *
	 * @param regelmenge  Die Menge der Regeln, welche hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddRegelmenge(final @NotNull List<@NotNull GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			DeveloperNotificationException.ifTrue("Die Regel " + _parent.toStringRegel(regel.id) + " muss erst beim Datenmanager hinzugefügt werden!", !_parent.regelGetExistiert(regel.id));

		// Bewertungen aktualisieren
		stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Regel.
	 *
	 * @param  idRegel  Die Datenbank-ID der Regel.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public void setRemoveRegelByID(final long idRegel) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Regel " + _parent.toStringRegel(idRegel) + " muss erst beim Datenmanager entfernt werden!", _parent.regelGetExistiert(idRegel));

		// Bewertungen aktualisieren
		stateRevalidateEverything();
	}

	/**
	 * Entfernt die übergebenen Regeln.
	 *
	 * @param regelmenge  Die Menge der Regeln, welche entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst im Datenmanager entfernt wurde.
	 */
	public void setRemoveRegelmenge(final @NotNull List<@NotNull GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			DeveloperNotificationException.ifTrue("Die Regel " + _parent.toStringRegel(regel.id) + " muss erst beim Datenmanager entfernt werden!", _parent.regelGetExistiert(regel.id));

		// Bewertungen aktualisieren
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
		DeveloperNotificationException.ifTrue("" + _parent.toStringKurs(idKurs) + " muss erst beim Datenmanager hinzugefügt werden!", !_parent.kursGetExistiert(idKurs));
		final @NotNull GostBlockungKurs kurs = _parent.kursGet(idKurs);
		final int nSchienen = _parent.schieneGetAnzahl();
		DeveloperNotificationException.ifTrue("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!", nSchienen < kurs.anzahlSchienen);

		// Muss vor 'setKursSchienenNr' aufgerufen werden.
		stateRevalidateEverything();

		// Kurs automatisch in die ersten 'kurs.anzahlSchienen' Schienen hinzufügen.
		for (int nr = 1; nr <= kurs.anzahlSchienen; nr++)
			setKursSchienenNr(idKurs, nr);
	}

	/**
	 * Löscht den übergebenen Kurs. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public void setRemoveKursByID(final long idKurs) throws DeveloperNotificationException {
		setRemoveKurseByID(ListUtils.create1(idKurs));
	}

	/**
	 * Löscht alle übergebenen Kurse. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.

	 * @param kurse  Die Liste der {@link GostBlockungsergebnisKurs}-Objekte.
	 *
	 * @throws DeveloperNotificationException  Falls mindestens einer der Kurse nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public void setRemoveKurse(final @NotNull List<@NotNull GostBlockungsergebnisKurs> kurse) throws DeveloperNotificationException {
		// Kopieren der IDs.
		final @NotNull List<@NotNull Long> idKurse = new ArrayList<>();
		for (final @NotNull GostBlockungsergebnisKurs kursExtern : kurse)
			idKurse.add(kursExtern.id);

		// Delegieren an die andere Methode.
		setRemoveKurseByID(idKurse);
	}

	/**
	 * Löscht alle übergebenen Kurse. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.

	 * @param idKurse  Die Liste der Datenbank-IDs der Kurse.
	 *
	 * @throws DeveloperNotificationException  Falls mindestens einer der Kurse nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public void setRemoveKurseByID(final @NotNull List<@NotNull Long> idKurse) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen
		for (final long idKurs : idKurse)
			DeveloperNotificationException.ifTrue(_parent.toStringKurs(idKurs) + " muss erst beim Datenmanager entfernt werden!", _parent.kursGetExistiert(idKurs));

		// (2) Lösche die Kurse aus der DTO-Datenstruktur (löscht dadurch auch die SuS des Kurses).
		for (final long idKurs : idKurse) {
			final @NotNull GostBlockungsergebnisKurs kurs = getKursE(idKurs);

			for (final @NotNull Long schienenID : kurs.schienen) {
				final @NotNull Iterator<@NotNull GostBlockungsergebnisKurs> i = getSchieneE(schienenID).kurse.iterator();
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

		// 2) Kurs2 erzeugen (in diesem Manager).
		setAddKursByID(kurs2neu.id);

		// 3) Verschieben der SuS von Kurs1 nach Kurs2 (in diesem Manager).
		for (final long schuelerID : susVon1nach2) {
			stateSchuelerKursEntfernen(schuelerID, kurs1alt.id);
			stateSchuelerKursHinzufuegen(schuelerID, kurs2neu.id);
		}
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
		DeveloperNotificationException.ifTrue("Schienenanzahl von KursE (" + kursE.anzahlSchienen + ") ist ungleich der von KursG (" + kursG.anzahlSchienen + ")!", kursE.anzahlSchienen != kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Schienenanzahl von " + _parent.toStringKurs(idKurs) + " darf nur bei der Blockungsvorlage verändert werden!", !_parent.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue(_parent.toStringKurs(idKurs) + " hat als GostBlockungKurs " + kursG.anzahlSchienen + " Schienen, als GostBlockungsergebnisKurs hingegen " + kursE.anzahlSchienen + " Schienen!", kursE.anzahlSchienen != kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Blockung hat 0 Schienen. Das darf nicht passieren!", nSchienen == 0);
		DeveloperNotificationException.ifTrue(_parent.toStringKurs(idKurs) + " muss mindestens einer Schiene zugeordnet sein, statt " + anzahlSchienenNeu + " Schienen!", anzahlSchienenNeu <= 0);
		DeveloperNotificationException.ifTrue("Es gibt nur " + nSchienen + " Schienen, somit kann " + _parent.toStringKurs(idKurs) + " nicht " + anzahlSchienenNeu + " Schienen zugeordnet werden!", anzahlSchienenNeu > nSchienen);

		// Die Schienenanzahl erhöhen, ggf. mehrfach.
		while (anzahlSchienenNeu > kursG.anzahlSchienen) {
			boolean hinzugefuegt = false;
			for (int nr = 1; (nr <= _map_schienenNr_schiene.size()) && (!hinzugefuegt); nr++) {
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
			for (int nr = _map_schienenNr_schiene.size(); (nr >= 1) && (!entfernt); nr--) {
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

	/**
	 * Eine Logger-Ausgabe für Debug-Zwecke.
	 *
	 * @param logger Ein Logger für Debug-Zwecke.
	 */
	public void debug(final @NotNull Logger logger) {
		logger.modifyIndent(+4);
		logger.logLn("----- Kurse sortiert nach Fachart -----");
		for (final @NotNull Long fachartID : _map_fachartID_kurse.keySet()) {
			logger.logLn("FachartID = " + fachartID + " (KD = " + getOfFachartKursdifferenz(fachartID) + ")");
			for (final @NotNull GostBlockungsergebnisKurs kurs : getOfFachartKursmenge(fachartID)) {
				logger.logLn("    " + getOfKursName(kurs.id) + " : " + kurs.schueler.size() + " SuS");
			}
		}
		logger.logLn("KursdifferenzMax = " + _ergebnis.bewertung.kursdifferenzMax);
		logger.logLn("KursdifferenzHistogramm = " + Arrays.toString(_ergebnis.bewertung.kursdifferenzHistogramm));
		logger.modifyIndent(-4);
	}

}
