package de.svws_nrw.core.utils.gost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link GostBlockungsdaten}.<br>
 * Hierbei werden auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class GostBlockungsdatenManager {

	/** Die Blockungsdaten, die im Manager vorhanden sind. */
	private final @NotNull GostBlockungsdaten _daten;

	/** Der Fächermanager mit den Fächern der gymnasialen Oberstufe. */
	private final @NotNull GostFaecherManager _faecherManager;


	/** Ein Comparator für Kurse der Blockung. Dieser vergleicht nur die Kursnummern! */
	private static final @NotNull Comparator<@NotNull GostBlockungKurs> _compKursnummer = (final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für Schienen der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungSchiene> _compSchiene = (final @NotNull GostBlockungSchiene a, final @NotNull GostBlockungSchiene b) -> Integer.compare(a.nummer, b.nummer);

	/** Ein Comparator für Regeln der Blockung */
	private static final @NotNull Comparator<@NotNull GostBlockungRegel> _compRegel = (final @NotNull GostBlockungRegel a, final @NotNull GostBlockungRegel b) -> {
		final int result = Integer.compare(a.typ, b.typ);
		if (result != 0)
			return result;
		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die Lehrkräfte eines Kurses */
	private static final @NotNull Comparator<@NotNull GostBlockungKursLehrer> _compLehrkraefte = (final @NotNull GostBlockungKursLehrer a, final @NotNull GostBlockungKursLehrer b) -> {
		final int result = Integer.compare(a.reihenfolge, b.reihenfolge);
		if (result != 0)
			return result;
		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die Schüler. */
	private static final @NotNull Comparator<@NotNull Schueler> _compSchueler = (final @NotNull Schueler a, final @NotNull Schueler b) -> {
		final int cNachname = a.nachname.compareTo(b.nachname);
		if (cNachname != 0) return cNachname;

		final int cVorname = a.vorname.compareTo(b.vorname);
		if (cVorname != 0) return cVorname;

		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART) */
	private final @NotNull Comparator<@NotNull GostFachwahl> _compFachwahlen;

	/** Ein Comparator für die {@link GostBlockungsergebnis} nach ihrer Bewertung. */
	private final @NotNull Comparator<@NotNull GostBlockungsergebnis> _compErgebnisse = new GostBlockungsergebnisComparator();

	/** Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER) */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_kursart_fach_kursnummer;

	/** Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER). */
	private final @NotNull Comparator<@NotNull GostBlockungKurs> _compKurs_fach_kursart_kursnummer;

	/** Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungKurs> _map_idKurs_kurs = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Listen der Kurse, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull GostBlockungKurs>> _map2d_idFach_idKursart_kurse = new HashMap2D<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Listen der Fachwahlen, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull GostFachwahl>> _map2d_idFach_idKursart_fachwahlen = new HashMap2D<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungSchiene> _map_idSchiene_schiene = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungRegel> _map_idRegel_regel = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}. */
	private final @NotNull Map<@NotNull GostKursblockungRegelTyp, @NotNull List<@NotNull GostBlockungRegel>> _map_regeltyp_regeln = new ArrayMap<>(GostKursblockungRegelTyp.values());

	/** Eine interne Hashmap zum Multi-Key-Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}. */
	private final @NotNull HashMap<@NotNull LongArrayKey, @NotNull GostBlockungRegel> _map_multikey_regeln = new HashMap<>();

	/** Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID. */
	private final @NotNull HashMap<@NotNull Long, @NotNull Schueler> _map_idSchueler_schueler = new HashMap<>();

	/** Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull GostFachwahl>> _map_idSchueler_fachwahlen = new HashMap<>();

	/** (Schüler-ID, Fach-ID) --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet. */
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull GostFachwahl> _map2d_idSchueler_idFach_fachwahl = new HashMap2D<>();

	/** Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart. */
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull GostFachwahl>> _map_idFachart_fachwahlen = new HashMap<>();

	/** Ergebnis-ID --> {@link GostBlockungsergebnis} */
	private final @NotNull HashMap<@NotNull Long, @NotNull GostBlockungsergebnis> _map_idErgebnis_Ergebnis = new HashMap<>();

	/** Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER). */
	private final @NotNull List<@NotNull GostBlockungKurs> _list_kurse_sortiert_fach_kursart_kursnummer = new ArrayList<>();

	/** Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER) */
	private final @NotNull List<@NotNull GostBlockungKurs> _list_kurse_sortiert_kursart_fach_kursnummer = new ArrayList<>();

	/** Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf. */
	private long _maxTimeMillis = 1000;

	/**
	 * Erstellt einen neuen Manager mit leeren Blockungsdaten und einem leeren Fächer-Manager.
	 */
	public GostBlockungsdatenManager() {
		_faecherManager = new GostFaecherManager();
		_daten = new GostBlockungsdaten();
		_daten.gostHalbjahr = GostHalbjahr.EF1.id;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();
	}

	/** Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 *
	 * @param daten           die Blockungsdaten
	 * @param faecherManager  der Fächer-Manager
	 */
	public GostBlockungsdatenManager(final @NotNull GostBlockungsdaten daten, final @NotNull GostFaecherManager faecherManager) {
		_faecherManager = faecherManager;
		_compKurs_fach_kursart_kursnummer = createComparatorKursFachKursartNummer();
		_compKurs_kursart_fach_kursnummer = createComparatorKursKursartFachNummer();
		_compFachwahlen = createComparatorFachwahlen();

		// Tiefe Kopie (deep copy) der GostBlockungsdaten.
		_daten = new GostBlockungsdaten();
		_daten.id = daten.id;
		_daten.name = daten.name;
		_daten.abijahrgang = daten.abijahrgang;
		_daten.gostHalbjahr = daten.gostHalbjahr;
		_daten.istAktiv = daten.istAktiv;

		// Kopieren und Mappings aufbauen.
		schieneAddListe(daten.schienen); // Muss vor den Kursen erzeugt werden.
		regelAddListe(daten.regeln);
		kursAddListe(daten.kurse);
		schuelerAddListe(daten.schueler);
		fachwahlAddListe(daten.fachwahlen);
		ergebnisAddListe(daten.ergebnisse);
	}

	private @NotNull Comparator<@NotNull GostBlockungKurs> createComparatorKursFachKursartNummer() {
		final @NotNull Comparator<@NotNull GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> {
			final @NotNull GostFach aFach = _faecherManager.getOrException(a.fach_id);
			final @NotNull GostFach bFach = _faecherManager.getOrException(b.fach_id);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;

			if (a.kursart < b.kursart) return -1;
			if (a.kursart > b.kursart) return +1;

			return Integer.compare(a.nummer, b.nummer);
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostBlockungKurs> createComparatorKursKursartFachNummer() {
		final @NotNull Comparator<@NotNull GostBlockungKurs> comp = (final @NotNull GostBlockungKurs a, final @NotNull GostBlockungKurs b) -> {
			if (a.kursart < b.kursart) return -1;
			if (a.kursart > b.kursart) return +1;

			final @NotNull GostFach aFach = _faecherManager.getOrException(a.fach_id);
			final @NotNull GostFach bFach = _faecherManager.getOrException(b.fach_id);
			final int cmpFach = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach != 0) return cmpFach;

			return Integer.compare(a.nummer, b.nummer);
		};
		return comp;
	}

	private @NotNull Comparator<@NotNull GostFachwahl> createComparatorFachwahlen() {
		final @NotNull Comparator<@NotNull GostFachwahl> comp = (final @NotNull GostFachwahl a, final @NotNull GostFachwahl b) -> {
			if (a.schuelerID < b.schuelerID) return -1;
			if (a.schuelerID > b.schuelerID) return +1;

			if (a.kursartID < b.kursartID) return -1;
			if (a.kursartID > b.kursartID) return +1;

			final @NotNull GostFach aFach = _faecherManager.getOrException(a.fachID);
			final @NotNull GostFach bFach = _faecherManager.getOrException(b.fachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		};
		return comp;
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param ergebnis Das {@link GostBlockungsergebnis}-Objekt, welches hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public void ergebnisAdd(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		ergebnisAddListe(ListUtils.create1(ergebnis));
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnis} hinzu.
	 *
	 * @param ergebnismenge Die Menge an Ergebnissen.
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public void ergebnisAddListe(final @NotNull List<@NotNull GostBlockungsergebnis> ergebnismenge) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		for (final @NotNull GostBlockungsergebnis ergebnis : ergebnismenge) {
			DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
			DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
			DeveloperNotificationException.ifNull("GostHalbjahr.fromID(" + ergebnis.gostHalbjahr + ")", GostHalbjahr.fromID(ergebnis.gostHalbjahr));
			DeveloperNotificationException.ifMapContains("_map_idErgebnis_Ergebnis", _map_idErgebnis_Ergebnis, ergebnis.id);
		}

		// Hinzufügen
		for (final @NotNull GostBlockungsergebnis ergebnis : ergebnismenge) {
			DeveloperNotificationException.ifMapPutOverwrites(_map_idErgebnis_Ergebnis, ergebnis.id, ergebnis);
			_daten.ergebnisse.add(ergebnis);
		}

		// Sortieren
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	// #########################################################################
	// ##########                 Kurs-Anfragen                       ##########
	// #########################################################################

	/**
	 * Liefert einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public @NotNull GostBlockungsergebnis ergebnisGet(final long idErgebnis) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("Es wurde kein Listeneintrag mit ID(" + idErgebnis + ") gefunden!", _map_idErgebnis_Ergebnis.get(idErgebnis));
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 */
	public @NotNull List<@NotNull GostBlockungsergebnis> ergebnisGetListeSortiertNachBewertung() {
		final @NotNull List<@NotNull GostBlockungsergebnis> result = new ArrayList<>(_daten.ergebnisse);
		return result;
	}

	private void ergebnisRemoveListeByIDs(final @NotNull List<@NotNull Long> listeDerErgebnisIDs) throws DeveloperNotificationException {
		// Überprüfen
		for (final long idErgebnis : listeDerErgebnisIDs)
			DeveloperNotificationException.ifMapNotContains("_map_idErgebnis_Ergebnis", _map_idErgebnis_Ergebnis, idErgebnis);

		// Entfernen des Ergebnisses.
		for (final long idErgebnis : listeDerErgebnisIDs) {
			final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
			_daten.ergebnisse.remove(e);
			_map_idErgebnis_Ergebnis.remove(e.id);
		}

		// Neusortierung nicht nötig.
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param idErgebnis  Die Datenbank-ID des zu entfernenden Ergebnisses.
	 *
	 * @throws DeveloperNotificationException Falls es kein Eregbnis mit dieser ID gibt.
	 */
	public void ergebnisRemoveByID(final long idErgebnis) throws DeveloperNotificationException {
		ergebnisRemoveListeByIDs(ListUtils.create1(idErgebnis));
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param ergebnis  Das zu entfernende Ergebnis.
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public void ergebnisRemove(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		ergebnisRemoveListeByIDs(ListUtils.create1(ergebnis.id));
	}

	/**
	 * Entfernt die Menge an Ergebnissen {@link GostBlockungsergebnis} hinzu.
	 *
	 * @param ergebnismenge Die Menge an Ergebnissen.
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public void ergebnisRemoveListe(final @NotNull List<@NotNull GostBlockungsergebnis> ergebnismenge) throws DeveloperNotificationException {
		// ID kopieren, da Löschen über Objektidentität nicht funktioniert!
		final @NotNull List<@NotNull Long> listIDs = new ArrayList<>();
		for (final @NotNull GostBlockungsergebnis e : ergebnismenge)
			listIDs.add(e.id);

		ergebnisRemoveListeByIDs(listIDs);
	}

	/**
	 * Aktualisiert die Bewertung im {@link GostBlockungsdatenManager} mit der aus dem {@link GostBlockungsergebnis}. <br>
	 * Wirft eine Exception, falls kein  {@link GostBlockungsergebnis} mit der ID gefunden wurde.
	 *
	 * @param ergebnis  Das Ergebnis mit der neuen Bewertung.
	 *
	 * @throws DeveloperNotificationException Falls kein  {@link GostBlockungsergebnis} mit der ID gefunden wurde.
	 */
	public void ergebnisUpdateBewertung(final @NotNull GostBlockungsergebnis ergebnis) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);

		// Bewertung aktualisieren.
		for (final @NotNull GostBlockungsergebnis eintrag : _daten.ergebnisse)
			if (eintrag.id == ergebnis.id)
				eintrag.bewertung = ergebnis.bewertung;

		// Ergebnisse sortieren.
		_daten.ergebnisse.sort(_compErgebnisse);
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung1Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		int summe = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung1Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final double summe = ergebnisGetBewertung1Wert(idErgebnis);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung2Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		int summe = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung2Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final double summe = ergebnisGetBewertung2Wert(idErgebnis);
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung3Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung3Intervall(final long idErgebnis) throws DeveloperNotificationException {
		int wert = ergebnisGetBewertung3Wert(idErgebnis);
		if (wert > 0)
			wert--; // Jede Kursdifferenz wird um 1 reduziert, außer die 0.
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public int ergebnisGetBewertung4Wert(final long idErgebnis) throws DeveloperNotificationException {
		final @NotNull GostBlockungsergebnis e = ergebnisGet(idErgebnis);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public double ergebnisGetBewertung4Intervall(final long idErgebnis) throws DeveloperNotificationException {
		final int wert = ergebnisGetBewertung4Wert(idErgebnis);
		return 1 - 1 / (0.25 * wert + 1);
	}

	// TODO BAR trennen in "Überprüfen" und "Hinzufügen"
	private void kursAddKursOhneSortierung(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		final int nSchienen = schieneGetAnzahl();

		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifInvalidID("pKurs.id", kurs.id);
		DeveloperNotificationException.ifNull("_faecherManager.get(pKurs.fach_id)", _faecherManager.get(kurs.fach_id));
		DeveloperNotificationException.ifNull("GostKursart.fromIDorNull(pKurs.kursart)", GostKursart.fromIDorNull(kurs.kursart));
		DeveloperNotificationException.ifSmaller("pKurs.wochenstunden", kurs.wochenstunden, 0);
		DeveloperNotificationException.ifSmaller("pKurs.anzahlSchienen", kurs.anzahlSchienen, 1);
		DeveloperNotificationException.ifGreater("pKurs.anzahlSchienen", kurs.anzahlSchienen, nSchienen);
		DeveloperNotificationException.ifSmaller("pKurs.nummer", kurs.nummer, 1);

		// Hinzufügen des Kurses.
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKurs_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_fach_kursart_kursnummer", _list_kurse_sortiert_fach_kursart_kursnummer, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_kursart_fach_kursnummer", _list_kurse_sortiert_kursart_fach_kursnummer, kurs);
		final List<@NotNull GostBlockungKurs> liste = Map2DUtils.getOrCreateArrayList(_map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart);
		liste.add(kurs);
		liste.sort(_compKursnummer);
		_daten.kurse.add(kurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param kurs  Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException falls die Daten des Kurses inkonsistent sind.
	 */
	public void kursAdd(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		kursAddListe(ListUtils.create1(kurs));
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param kursmenge Die Menge an Kursen.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public void kursAddListe(final @NotNull List<@NotNull GostBlockungKurs> kursmenge) throws DeveloperNotificationException {
		// Hinzufügen der Kurse.
		for (final @NotNull GostBlockungKurs gKurs : kursmenge)
			kursAddKursOhneSortierung(gKurs);

		// Sortieren der Kursmengen.
		_list_kurse_sortiert_fach_kursart_kursnummer.sort(_compKurs_fach_kursart_kursnummer);
		_list_kurse_sortiert_kursart_fach_kursnummer.sort(_compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public boolean kursGetExistiert(final long idKurs) {
		return _map_idKurs_kurs.get(idKurs) != null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return Die Anzahl an Kursen.
	 */
	public int kursGetAnzahl() {
		return _map_idKurs_kurs.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return Den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][Suffix], beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull String kursGetName(final long idKurs) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull GostFach gFach = _faecherManager.getOrException(kurs.fach_id);
		final @NotNull String sSuffix = kurs.suffix.equals("") ? "" : ("-" + kurs.suffix);
		return gFach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer + sSuffix;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull String kursGetNameOhneSuffix(final long idKurs) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull GostFach gFach = _faecherManager.getOrException(kurs.fach_id);
		return gFach.kuerzelAnzeige + "-" + GostKursart.fromID(kurs.kursart).kuerzel + kurs.nummer;
	}

	/**
	 * Liefert das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses
	 *
	 * @return das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungKurs kursGet(final long idKurs) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_kurs, idKurs);
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return die Lehrkraft des Kurses, welche die angegebene Nummer hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer kursGetLehrkraftMitNummer(final long idKurs, final int reihenfolgeNr) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge == reihenfolgeNr)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + idKurs + " keine Lehrkraft mit ReihenfolgeNr. " + reihenfolgeNr + "!");
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return Die Lehrkraft des Kurses, welche die angegebene ID hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public GostBlockungKursLehrer kursGetLehrkraftMitID(final long idKurs, final int idLehrkraft) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id == idLehrkraft)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + idKurs + " keine Lehrkraft mit ID " + idLehrkraft + "!");
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<@NotNull GostBlockungKurs> kursGetListeSortiertNachFachKursartNummer() {
		return _list_kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public @NotNull List<@NotNull GostBlockungKurs> kursGetListeSortiertNachKursartFachNummer() {
		return _list_kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 * Liefert eine nach Kursnummer sortiere Liste der Kurse für das angegebenen Fach und die angegebene Kursart.
	 *
	 * @param idFach      die ID des Fachs
	 * @param idKursart   die ID der Kursart
	 *
	 * @return die sortiere Liste der Kurse für das Fach und die Kursart
	 */
	public @NotNull List<@NotNull GostBlockungKurs> kursGetListeByFachUndKursart(final long idFach, final int idKursart) {
		final List<@NotNull GostBlockungKurs> liste = _map2d_idFach_idKursart_kurse.getOrNull(idFach, idKursart);
		if (liste == null)
			return new ArrayList<>();
		liste.sort(_compKursnummer);
		return liste;
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public @NotNull List<@NotNull GostBlockungKursLehrer> kursGetLehrkraefteSortiert(final long idKurs) throws DeveloperNotificationException {
		return kursGet(idKurs).lehrer;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public boolean kursGetIsRemoveAllowed(final long idKurs) throws DeveloperNotificationException {
		return (_map_idKurs_kurs.get(idKurs) != null) && getIstBlockungsVorlage();
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public boolean kursGetIstVerbotenInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		if (kursGetHatSperrungInSchiene(idKurs, idSchiene))
			return true;

		final int nummer = schieneGet(idSchiene).nummer;
		final int kursart = kursGet(idKurs).kursart;

		// KURSART_ALLEIN_IN_SCHIENEN_VON_BIS
		// Zugriff ist O(1) durch "regelGetListeOfTyp" und da jede Regel dieses Typs getestet werden muss.
		for (final @NotNull GostBlockungRegel regel :  regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			if (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)) { // Schiene im Intervall?
				if (regel.parameter.get(0) != kursart)
					return true;
			} else {
				if (regel.parameter.get(0) == kursart)
					return true;
			}

		// KURSART_SPERRE_SCHIENEN_VON_BIS
		// Zugriff ist O(1) durch "regelGetListeOfTyp" und da jede Regel dieses Typs getestet werden muss.
		for (final @NotNull GostBlockungRegel regel :  regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			if ((nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2))  // Schiene im Intervall?
				&& (regel.parameter.get(0) == kursart))
					return true;

		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public boolean kursGetHatSperrungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene});
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel kursGetRegelGesperrtInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene});
		return DeveloperNotificationException.ifNull("Kurs " + idKurs + " ist nicht gesperrt in Schiene " + idSchiene + "!", _map_multikey_regeln.get(key));
	}

	/**
	 * Liefert die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 */
	public GostBlockungRegel kursGetRegelDummySchuelerOrNull(final long idKurs) {
		for (final @NotNull GostBlockungRegel regel : regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (regel.parameter.get(0) == idKurs)
				return regel;
		return null;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public boolean kursGetHatFixierungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene});
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert hat.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel kursGetRegelFixierungInSchiene(final long idKurs, final long idSchiene) throws DeveloperNotificationException {
		final int nrSchiene = schieneGet(idSchiene).nummer;
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene});
		return DeveloperNotificationException.ifNull("Kurs " + idKurs + " ist nicht fixiert in Schiene " + idSchiene + "!", _map_multikey_regeln.get(key));
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param idKurs  Die Datenbank-ID des zu entfernenden Kurses.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public void kursRemoveByID(final long idKurs) throws DeveloperNotificationException {
		kurseRemoveByID(ListUtils.create1(idKurs));
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.<br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert.
	 *
	 * @param kurs  Der zu entfernende Kurs.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public void kursRemove(final @NotNull GostBlockungKurs kurs) throws DeveloperNotificationException {
		kursRemoveByID(kurs.id);
	}

	/**
	 * Entfernt alleKurse mit den übergebenen IDs aus der Blockung.
	 *
	 * @param idKurse  Die Datenbank-IDs der zu entfernenden Kurse.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void kurseRemoveByID(final @NotNull List<@NotNull Long> idKurse) throws DeveloperNotificationException {
		// (1) Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		for (final long idKurs : idKurse)
			DeveloperNotificationException.ifMapNotContains("_map_idKurs_kurs", _map_idKurs_kurs, idKurs);

		// (2) Entfernen des Kurses.
		for (final long idKurs : idKurse) {
			final @NotNull GostBlockungKurs kurs = this.kursGet(idKurs);
			_list_kurse_sortiert_fach_kursart_kursnummer.remove(kurs); // Neusortierung nicht nötig.
			_list_kurse_sortiert_kursart_fach_kursnummer.remove(kurs); // Neusortierung nicht nötig.
			Map2DUtils.removeFromListAndTrimOrException(_map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart, kurs);
			DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_kurs, idKurs);
			_daten.kurse.remove(kurs);
		}
	}

	/**
	 * Entfernt alleKurse mit den übergebenen IDs aus der Blockung.
	 *
	 * @param kurse Die zu entfernenden {{@link GostBlockungKurs} GostBlockungKurs-Objekte.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void kurseRemove(final @NotNull List<@NotNull GostBlockungKurs> kurse) throws DeveloperNotificationException {
		// Kopieren der IDs.
		final @NotNull List<@NotNull Long> idKurse = new ArrayList<>();
		for (final @NotNull GostBlockungKurs kursExtern : kurse)
			idKurse.add(kursExtern.id);

		// Delegieren an die andere Methode.
		kurseRemoveByID(idKurse);
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param neueLehrkraft  Das {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 */
	public void kursAddLehrkraft(final long idKurs, final @NotNull GostBlockungKursLehrer neueLehrkraft) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull List<@NotNull GostBlockungKursLehrer> listOfLehrer = kurs.lehrer;
		for (final @NotNull GostBlockungKursLehrer lehrkraft : listOfLehrer) {
			DeveloperNotificationException.ifTrue("kursAddLehrkraft: Der Kurs hat bereits eine Lehrkraft mit ID " + lehrkraft.id, lehrkraft.id == neueLehrkraft.id);
			DeveloperNotificationException.ifTrue("kursAddLehrkraft: Der Kurs hat bereits eine Lehrkraft mit Reihenfolge " + lehrkraft.reihenfolge, lehrkraft.reihenfolge == neueLehrkraft.reihenfolge);
		}

		// Hinzufügen
		listOfLehrer.add(neueLehrkraft);
		listOfLehrer.sort(_compLehrkraefte);
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft. <br>
	 * Wirft eine DeveloperNotificationException, falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 *
	 * @param idKurs           Die Datenbank-ID des Kurses.
	 * @param idAlteLehrkraft  Die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public void kursRemoveLehrkraft(final long idKurs, final long idAlteLehrkraft) throws DeveloperNotificationException {
		final @NotNull GostBlockungKurs kurs = kursGet(idKurs);
		final @NotNull List<@NotNull GostBlockungKursLehrer> listOfLehrer = kurs.lehrer;
		for (int i = 0; i < listOfLehrer.size(); i++)
			if (listOfLehrer.get(i).id == idAlteLehrkraft) {
				listOfLehrer.remove(listOfLehrer.get(i));
				return;
			}
		throw new DeveloperNotificationException("kursRemoveLehrkraft: Kurs (" + idKurs + ") hat keine Lehrkraft (" + idAlteLehrkraft + ")!");
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 */
	public boolean kursGetLehrkraftMitNummerExists(final long idKurs, final int reihenfolgeNr) {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge == reihenfolgeNr)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 */
	public boolean kursGetLehrkraftMitIDExists(final long idKurs, final int idLehrkraft) {
		for (final @NotNull GostBlockungKursLehrer lehrkraft : kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id == idLehrkraft)
				return true;
		return false;
	}

	/**
	 * Setzt den Suffix des Kurses.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 * @param  suffix  Der neue Suffix des Kurses.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht in der Blockung existiert.
	 */
	public void kursSetSuffix(final long idKurs, final @NotNull String suffix) throws DeveloperNotificationException {
		kursGet(idKurs).suffix = suffix;
	}

	// #########################################################################
	// ##########                Schiene-Anfragen                     ##########
	// #########################################################################

	// TODO BAR check / add trennen
	private void schieneAddOhneSortierung(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifInvalidID("GostBlockungSchiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("GostBlockungSchiene.bezeichnung darf nicht leer sein!", "".equals(schiene.bezeichnung));
		DeveloperNotificationException.ifSmaller("GostBlockungSchiene.nummer", schiene.nummer, 1);
		DeveloperNotificationException.ifSmaller("GostBlockungSchiene.wochenstunden", schiene.wochenstunden, 1);
		DeveloperNotificationException.ifMapContains("mapSchienen", _map_idSchiene_schiene, schiene.id);

		// Hinzufügen der Schiene.
		_map_idSchiene_schiene.put(schiene.id, schiene);
		_daten.schienen.add(schiene);
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 *
	 * @param schiene  Die hinzuzufügende Schiene.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void schieneAdd(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		schieneAddListe(ListUtils.create1(schiene));
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 *
	 * @param schienenmenge  Die Menge an Schienen.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public void schieneAddListe(final @NotNull List<@NotNull GostBlockungSchiene> schienenmenge) throws DeveloperNotificationException {
		// Hinzufügen der Schienen.
		for (final @NotNull GostBlockungSchiene schiene : schienenmenge)
			schieneAddOhneSortierung(schiene);

		// Sortieren der Schienenmenge.
		_daten.schienen.sort(_compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return Das zugehörige {@link GostBlockungSchiene} Objekt.
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungSchiene schieneGet(final long idSchiene) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("_mapSchienen.get(" + idSchiene + ")", _map_idSchiene_schiene.get(idSchiene));
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public boolean schieneGetExistiert(final long idSchiene) {
		return _map_idSchiene_schiene.get(idSchiene) != null;
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen.
	 * Das ist die interne Referenz zur Liste der Schienen im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach der Schienen-Nummer.
	 *
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public @NotNull List<@NotNull GostBlockungSchiene> schieneGetListe() {
		return new ArrayList<>(_daten.schienen);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Die Schiene muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public boolean schieneGetIsRemoveAllowed(final long idSchiene) throws DeveloperNotificationException {
		return (schieneGet(idSchiene) != null) && getIstBlockungsVorlage();
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#bezeichnung} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene    Die Datenbank-ID der Schiene.
	 * @param bezeichnung  Die neue Bezeichnung.
	 */
	public void schienePatchBezeichnung(final long idSchiene, final @NotNull String bezeichnung) {
		schieneGet(idSchiene).bezeichnung = bezeichnung;
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#wochenstunden} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene      Die Datenbank-ID der Schiene.
	 * @param wochenstunden  Die neuen Wochenstunden.
	 */
	public void schienePatchWochenstunden(final long idSchiene, final int wochenstunden) {
		schieneGet(idSchiene).wochenstunden = wochenstunden;
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein, sonst Exception.
	 * (2) Die Schienen werden neu nummeriert. <br>
	 * (3) Die Konsistenz der sortierten Schienen muss überprüft werden. <br>
	 * (4) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param idSchiene  Die Datenbank-ID der zu entfernenden Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void schieneRemoveByID(final long idSchiene) throws DeveloperNotificationException {
		// (1)
		DeveloperNotificationException.ifTrue("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());

		// (2)
		final @NotNull GostBlockungSchiene schieneR = this.schieneGet(idSchiene);
		_map_idSchiene_schiene.remove(idSchiene);
		_daten.schienen.remove(schieneR);
		for (final @NotNull GostBlockungSchiene schiene : _daten.schienen)
			if (schiene.nummer > schieneR.nummer)
				schiene.nummer--;

		// (3)
		for (int index = 0; index < _daten.schienen.size(); index++)
			DeveloperNotificationException.ifTrue("Schiene am Index " + index + " hat nicht Nr. " + (index + 1) + "!", _daten.schienen.get(index).nummer != index + 1);

		// (4)
		final Iterator<@NotNull GostBlockungRegel> iRegel = _daten.regeln.iterator();
		if (iRegel == null)
			return;
		while (iRegel.hasNext()) {
			final @NotNull GostBlockungRegel r = iRegel.next();
			final long[] a = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a == null)
				iRegel.remove();
			else
				for (int i = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}

	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung.
	 *
	 * @param schiene  Die zu entfernende Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public void schieneRemove(final @NotNull GostBlockungSchiene schiene) throws DeveloperNotificationException {
		schieneRemoveByID(schiene.id);
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public int schieneGetAnzahl() {
		return _map_idSchiene_schiene.size();
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param  halbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 *
	 * @return Die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 */
	public static int schieneGetDefaultAnzahl(final @NotNull GostHalbjahr halbjahr) {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param regel  Die hinzuzufügende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public void regelAdd(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		regelAddListe(ListUtils.create1(regel));
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param regelmenge  Die Menge an Regeln.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void regelAddListe(final @NotNull List<@NotNull GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		// Regeln überprüfen
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			regelCheck(regel);

		// Regeln hinzufügen.
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			regelAddOhneSortierung(regel);

		// Sortieren der Regelmenge.
		_daten.regeln.sort(_compRegel);
	}

	private void regelCheck(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		// Ist die ID gültig?
		DeveloperNotificationException.ifInvalidID("Regel.id", regel.id);

		// Ist der Regel-Typ gültig?
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifTrue("Der Typ(" + regel.typ + ") der Regel(" + regel.id + ") ist unbekannt!", typ == GostKursblockungRegelTyp.UNDEFINIERT);

		// Existiert bereits exakt die selbe Regel?
		final @NotNull LongArrayKey multikey = regelToMultikey(regel);
		if (_map_multikey_regeln.containsKey(multikey)) {
			final @NotNull StringBuilder sb = new StringBuilder();
			sb.append("Die Regel (ID=" + regel.id + ") vom Typ " + typ.bezeichnung + " existiert bereits mit den Parametern: ");
			for (int i = 0; i < regel.parameter.size(); i++)
				sb.append("[" + (i == 0 ? "" : ", ") + regel.parameter.get(i) + "]");
			System.out.println("WARNUNG: " + sb.toString());
		}
	}

	private void regelAddOhneSortierung(final @NotNull GostBlockungRegel regel) throws DeveloperNotificationException {
		final @NotNull LongArrayKey multikey = regelToMultikey(regel);
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idRegel_regel, regel.id, regel);
		MapUtils.getOrCreateArrayList(_map_regeltyp_regeln, typ).add(regel);
		_map_multikey_regeln.put(multikey, regel);
		_daten.regeln.add(regel);
	}

	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return Die Anzahl an Regeln.
	 */
	public int regelGetAnzahl() {
		return _map_idRegel_regel.size();
	}

	/**
	 * Gibt die Regel der Blockung anhand von deren ID zurück.
	 *
	 * @param idRegel  Die Datenbank-ID der Regel.
	 *
	 * @return Das {@link GostBlockungRegel} Objekt.
	 * @throws DeveloperNotificationException Falls die Regel nicht in der Blockung existiert.
	 */
	public @NotNull GostBlockungRegel regelGet(final long idRegel) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("_mapRegeln.get(" + idRegel + ")", _map_idRegel_regel.get(idRegel));
	}

	/**
	 * Liefert die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 *
	 * @param key  Der {@link LongArrayKey}-Schlüssel.
	 *
	 * @return die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 */
	public GostBlockungRegel regelGetByLongArrayKeyOrNull(@NotNull final LongArrayKey key) {
		return _map_multikey_regeln.get(key);
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public @NotNull List<@NotNull GostBlockungRegel> regelGetListe() {
		return _daten.regeln;
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 *
	 * @param typ Der {@link GostKursblockungRegelTyp}.
	 *
	 * @return die aktuelle Menge aller  Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	public @NotNull List<@NotNull GostBlockungRegel> regelGetListeOfTyp(final @NotNull GostKursblockungRegelTyp typ) {
		return MapUtils.getOrCreateArrayList(_map_regeltyp_regeln, typ);
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 *
	 * @deprecated      Die Methode ist in den Ergebnismanager gewandert.
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 */
	@Deprecated(forRemoval = true)
	public @NotNull List<@NotNull GostBlockungRegel> regelGetListeToggleSperrung(final @NotNull List<@NotNull GostBlockungKurs> list, final @NotNull GostBlockungKurs kursA, final @NotNull GostBlockungKurs kursB, final @NotNull GostBlockungSchiene schieneA, final @NotNull GostBlockungSchiene schieneB) {
		final @NotNull List<@NotNull GostBlockungRegel> regeln = new ArrayList<>();
		boolean aktiv = false;
		final int min = Math.min(schieneA.nummer, schieneB.nummer);
		final int max = Math.max(schieneA.nummer, schieneB.nummer);
		for (final @NotNull GostBlockungKurs kurs : list) {
			// Aktive Auswahl erkennen.
			if ((kurs == kursA) || (kurs == kursB))
				aktiv = !aktiv;

			// Aktuelle Zeile ignorieren?
			if (!aktiv)
				continue;

			// Aktive Auswahl: Scanne die Spalten
			for (int nr = min; nr <= max; nr++)
				regeln.add(regelGetRegelOrDummyKursGesperrtInSchiene(kurs.id, nr));
		}

		return regeln;
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param nrSchiene  Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummyKursGesperrtInSchiene(final long idKurs, final int nrSchiene) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene});

		final GostBlockungRegel regel = _map_multikey_regeln.get(key);
		if (regel != null)
			return regel;

		final @NotNull GostBlockungRegel regelDummy = new GostBlockungRegel();
		regelDummy.id = -1;
		regelDummy.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regelDummy.parameter.add(idKurs);
		regelDummy.parameter.add((long) nrSchiene);

		return regelDummy;
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param nrSchiene  Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummyKursFixierungInSchiene(final long idKurs, final int nrSchiene) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene});

		final GostBlockungRegel regel = _map_multikey_regeln.get(key);
		if (regel != null)
			return regel;

		final @NotNull GostBlockungRegel regelDummy = new GostBlockungRegel();
		regelDummy.id = -1;
		regelDummy.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regelDummy.parameter.add(idKurs);
		regelDummy.parameter.add((long) nrSchiene);

		return regelDummy;
	}


	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public @NotNull GostBlockungRegel regelGetRegelOrDummySchuelerInKursFixierung(final long idSchueler, final long idKurs) {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs});

		final GostBlockungRegel regel = _map_multikey_regeln.get(key);
		if (regel != null)
			return regel;

		final @NotNull GostBlockungRegel regelDummy = new GostBlockungRegel();
		regelDummy.id = -1;
		regelDummy.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regelDummy.parameter.add(idSchueler);
		regelDummy.parameter.add(idKurs);

		return regelDummy;
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param idRegel  Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public boolean regelGetExistiert(final long idRegel) {
		return _map_idRegel_regel.get(idRegel) != null;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist. <br>
	 * Kriterium: Die Regel muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  idRegel Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls ein Löschen der Regel erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public boolean regelGetIsRemoveAllowed(final long idRegel) throws DeveloperNotificationException {
		return _map_idRegel_regel.containsKey(idRegel) && getIstBlockungsVorlage();
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 * Wirft eine Exception, falls es sich nicht um eine Blockungsvorlage handelt.
	 *
	 * @param idRegel Die Datenbank-ID der zu entfernenden Regel.
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void regelRemoveByID(final long idRegel) throws DeveloperNotificationException, UserNotificationException {
		// Datenkonsistenz überprüfen.
		UserNotificationException.ifTrue("Ein Löschen einer Regel ist nur bei einer Blockungsvorlage erlaubt!", !getIstBlockungsVorlage());
		final @NotNull GostBlockungRegel regel = this.regelGet(idRegel);
		final @NotNull GostKursblockungRegelTyp typ = GostKursblockungRegelTyp.fromTyp(regel.typ);
		final @NotNull LongArrayKey multikey = regelToMultikey(regel);

		// Regel entfernen.
		_map_idRegel_regel.remove(idRegel);
		MapUtils.getOrCreateArrayList(_map_regeltyp_regeln, typ).remove(regel);
		_map_multikey_regeln.remove(multikey);
		_daten.regeln.remove(regel);
	}

	/**
	 * Entfernt eine Menge von Regeln.
	 *
	 * @param regelmenge  Die Menge an Regeln, die entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public void regelRemoveListe(final @NotNull List<@NotNull GostBlockungRegel> regelmenge) throws DeveloperNotificationException {
		// Alle Regeln löschen.
		for (final @NotNull GostBlockungRegel regel : regelmenge)
			regelRemoveByID(regel.id);
	}

	private static @NotNull LongArrayKey regelToMultikey(final @NotNull GostBlockungRegel regel) {
		final int size = regel.parameter.size();
		final long[] keys = new long[size + 1];
		keys[0] = regel.typ;
		for (int i = 1; i <= size; i++)
			keys[i] = regel.parameter.get(i - 1);
		return new LongArrayKey(keys);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param regel  Die zu entfernende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 * @throws UserNotificationException Falls es sich nicht um eine Blockungsvorlage handelt.
	 */
	public void regelRemove(final @NotNull GostBlockungRegel regel)  throws DeveloperNotificationException, UserNotificationException {
		regelRemoveByID(regel.id);
	}


	/**
	 * Liefert die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 */
	public @NotNull List<@NotNull GostKursart> fachGetMengeKursarten(final long idFach)  {
		final @NotNull HashSet<@NotNull Integer> idKursarten = new HashSet<>();

		if (_map2d_idFach_idKursart_kurse.containsKey1(idFach))
			idKursarten.addAll(_map2d_idFach_idKursart_kurse.getKeySetOf(idFach));

		if (_map2d_idFach_idKursart_fachwahlen.containsKey1(idFach))
			idKursarten.addAll(_map2d_idFach_idKursart_fachwahlen.getKeySetOf(idFach));

		final @NotNull List<@NotNull GostKursart> list = new ArrayList<>();
		for (final @NotNull GostKursart kursart : GostKursart.values())
			if (idKursarten.contains(kursart.id))
				list.add(kursart);

		return list;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 * <br>Wirft eine Exception, falls die Fachwahl-Daten inkonsistent sind.
	 *
	 * @param fachwahl  Die Fachwahl, die hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void fachwahlAdd(final @NotNull GostFachwahl fachwahl) throws DeveloperNotificationException {
		fachwahlAddListe(ListUtils.create1(fachwahl));
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param fachwahlmenge  Die Menge an Fachwahlen.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public void fachwahlAddListe(final @NotNull List<@NotNull GostFachwahl> fachwahlmenge) throws DeveloperNotificationException {
		// check
		for (final @NotNull GostFachwahl fachwahl : fachwahlmenge)
			GostKursart.fromFachwahlOrException(fachwahl);

		// add
		for (final @NotNull GostFachwahl fachwahl : fachwahlmenge) {
			// _map_schulerID_fachID_fachwahl
			DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_idSchueler_idFach_fachwahl, fachwahl.schuelerID, fachwahl.fachID, fachwahl);

			// _map_schuelerID_fachwahlen
			@NotNull final List<@NotNull GostFachwahl> fachwahlenDesSchuelers = MapUtils.getOrCreateArrayList(_map_idSchueler_fachwahlen, fachwahl.schuelerID);
			fachwahlenDesSchuelers.add(fachwahl);
			fachwahlenDesSchuelers.sort(_compFachwahlen);

			// _map_fachartID_fachwahlen
			final long fachartID = GostKursart.getFachartIDByFachwahl(fachwahl);
			fachwahlGetListeOfFachart(fachartID).add(fachwahl);

			// _map2d_idFach_idKursart_kurse
			Map2DUtils.getOrCreateArrayList(_map2d_idFach_idKursart_fachwahlen, fachwahl.fachID, fachwahl.kursartID).add(fachwahl);

			// fachwahlen
			_daten.fachwahlen.add(fachwahl);
		}
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return Die Anzahl an Fachwahlen.
	 */
	public int fachwahlGetAnzahl() {
		return _daten.fachwahlen.size();
	}

	/**
	 * Liefert den Namen (Fach-Kursart) der Fachwahl, beispielsweise 'M-GK'.
	 *
	 * @param fachwahl  Das Fachwahl-Objekt.
	 *
	 * @return den Namen (Fach-Kursart) der Fachwahl, beispielsweise 'M-GK'.
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public @NotNull String fachwahlGetName(final @NotNull GostFachwahl fachwahl) throws DeveloperNotificationException {
		final @NotNull GostFach gFach = _faecherManager.getOrException(fachwahl.fachID);
		final @NotNull GostKursart gKursart = GostKursart.fromID(fachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID. <br>
	 * Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen.
	 *
	 * @param idFachart Die Fachart-ID berechnet aus Fach-ID und Kursart-ID.
	 *
	 * @return Die Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 */
	public @NotNull List<@NotNull GostFachwahl> fachwahlGetListeOfFachart(final long idFachart) {
		return MapUtils.getOrCreateArrayList(_map_idFachart_fachwahlen, idFachart);
	}

	/**
	 * Liefert die Anzahl verschiedenen Kursarten. Dies passiert indem über alle Fachwahlen summiert wird.
	 *
	 * @return Die Anzahl verschiedenen Kursarten.
	 */
	public int fachwahlGetAnzahlVerwendeterKursarten() {
		final @NotNull HashSet<@NotNull Integer> setKursartenIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler  Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	private void schuelerAddOhneSortierung(final @NotNull Schueler schueler) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifInvalidID("pSchueler.id", schueler.id);
		DeveloperNotificationException.ifSmaller("pSchueler.geschlecht", schueler.geschlecht, 0);

		// _map_schuelerID_schueler
		DeveloperNotificationException.ifMapPutOverwrites(_map_idSchueler_schueler, schueler.id, schueler);

		// _map_schuelerID_fachwahlen
		if (!_map_idSchueler_fachwahlen.containsKey(schueler.id))
			_map_idSchueler_fachwahlen.put(schueler.id, new ArrayList<>());

		// _map2d_schuelerID_fachID_fachwahl: Der Teil-Pfad muss nicht erzeugt werden.

		// _daten.schueler
		_daten.schueler.add(schueler);
	}

	/**
	 * Fügt einen Schüler hinzu.
	 * <br>Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler  Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public void schuelerAdd(final @NotNull Schueler schueler) throws DeveloperNotificationException {
		schuelerAddListe(ListUtils.create1(schueler));
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param schuelermenge  Die Menge an Schülern.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	public void schuelerAddListe(final @NotNull List<@NotNull Schueler> schuelermenge) throws DeveloperNotificationException {
		// überprüfen
		for (final @NotNull Schueler schueler : schuelermenge)
			DeveloperNotificationException.ifInvalidID(schueler.id + "", schueler.id);

		// hinzufügen
		for (final @NotNull Schueler schueler : schuelermenge) {
			schuelerAddOhneSortierung(schueler);
		}

		// sortieren
		_daten.schueler.sort(_compSchueler);
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public int schuelerGetAnzahlMitMindestensEinerFachwahl() {
		final HashSet<@NotNull Long> setSchuelerIDs = new HashSet<>();
		for (final @NotNull GostFachwahl fachwahl : _daten.fachwahlen)
			setSchuelerIDs.add(fachwahl.schuelerID);
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 *
	 * @return Die Anzahl an Schülern.
	 */
	public int schuelerGetAnzahl() {
		return _daten.schueler.size();
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Wirft eine DeveloperNotificationException, falls die Schüler-ID unbekannt ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return Das zugehörige {@link Schueler}-Objekt.
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public @NotNull Schueler schuelerGet(final long idSchueler) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("_map_id_schueler.get(" + idSchueler + ")", _map_idSchueler_schueler.get(idSchueler));
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return Die aktuelle Menge aller Schüler.
	 */
	public @NotNull List<@NotNull Schueler> schuelerGetListe() {
		return _daten.schueler;
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostKursart schuelerGetOfFachKursart(final long idSchueler, final long idFach) throws DeveloperNotificationException {
		final @NotNull GostFachwahl fachwahl = schuelerGetOfFachFachwahl(idSchueler, idFach);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Wirft eine Exception, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws DeveloperNotificationException Falls der Schüler das Fach nicht gewählt hat.
	 */
	public @NotNull GostFachwahl schuelerGetOfFachFachwahl(final long idSchueler, final long idFach) throws DeveloperNotificationException {
		return _map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank.ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public boolean schuelerGetHatFach(final long idSchueler, final long idFach) throws DeveloperNotificationException {
		return _map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach);
	}


	/**
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception, falls zum Fach keine Fachwahl existiert.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception, falls zum Fach keine Fachwahl existiert.
	 */
	public boolean schuelerGetHatDieSelbeKursartMitSchuelerInFach(final long idSchueler1, final long idSchueler2, final long idFach) {
		final @NotNull GostFachwahl fachwahl1 = _map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler1, idFach);
		final @NotNull GostFachwahl fachwahl2 = _map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler2, idFach);
		return fachwahl1.kursartID == fachwahl2.kursartID;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart   Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public boolean schuelerGetHatFachart(final long idSchueler, final long idFach, final int idKursart) throws DeveloperNotificationException {
		if (!_map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach))
			return false;
		return _map2d_idSchueler_idFach_fachwahl.getNonNullOrException(idSchueler, idFach).kursartID == idKursart;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller {@link GostFachwahl} des Schülers.
	 * @throws DeveloperNotificationException Falls die Schüler-ID unbekannt ist.
	 */
	public @NotNull List<@NotNull GostFachwahl> schuelerGetListeOfFachwahlen(final long pSchuelerID) throws DeveloperNotificationException {
		return DeveloperNotificationException.ifNull("_map_schuelerID_fachwahlen.get(" + pSchuelerID + ")", _map_idSchueler_fachwahlen.get(pSchuelerID));
	}

	/**
	 * Liefert eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 */
	public @NotNull List<@NotNull GostFach> schuelerGetFachListeGemeinsamerFacharten(final long idSchueler1, final long idSchueler2) {
		final @NotNull List<@NotNull GostFach> temp = new ArrayList<>();

		for (final @NotNull GostFachwahl fachwahl1 :  schuelerGetListeOfFachwahlen(idSchueler1))
			if (schuelerGetHatFachart(idSchueler2, fachwahl1.fachID, fachwahl1.kursartID))
				temp.add(_faecherManager.getOrException(fachwahl1.fachID));

		return temp;
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public boolean schuelerGetIstVerbotenInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs});
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs verbietet.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs verbietet.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel schuelerGetRegelVerbotenInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs});
		return DeveloperNotificationException.ifNull("Schüler " + idSchueler + " ist nicht verboten in Kurs " + idKurs + "!", _map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public boolean schuelerGetIstFixiertInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs});
		return _map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public @NotNull GostBlockungRegel schuelerGetRegelFixiertInKurs(final long idSchueler, final long idKurs) throws DeveloperNotificationException {
		final @NotNull LongArrayKey key = new LongArrayKey(new long[] {GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs});
		return DeveloperNotificationException.ifNull("Schüler " + idSchueler + " ist nicht fixiert in Kurs " + idKurs + "!", _map_multikey_regeln.get(key));
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 *
	 * @return die ID der Blockung
	 */
	public long getID() {
		return _daten.id;
	}

	/**
	 * Setzt die ID der Blockung.
	 *
	 * @param pBlockungsID die ID, welche der Blockung zugewiesen wird.
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig bzw. negativ ist.
	 */
	public void setID(final long pBlockungsID) throws DeveloperNotificationException {
		DeveloperNotificationException.ifInvalidID("pBlockungsID", pBlockungsID);
		_daten.id = pBlockungsID;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden.
	 *
	 * @return Die maximale Blockungszeit in Millisekunden.
	 */
	public long getMaxTimeMillis() {
		return _maxTimeMillis;
	}

	/**
	 * Setzt die maximale Blockungszeit in Millisekunden.
	 *
	 * @param pZeit die maximale Blockungszeit in Millisekunden.
	 */
	public void setMaxTimeMillis(final long pZeit) {
		_maxTimeMillis = pZeit;
	}

	/**
	 * Gibt den Namen der Blockung zurück.
	 *
	 * @return der Name der Blockung
	 */
	public @NotNull String getName() {
		return _daten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 *
	 * @param pName der Name, welcher der Blockung zugewiesen wird.
	 * @throws UserNotificationException Falls der übergebene String leer ist.
	 */
	public void setName(final @NotNull String pName) throws UserNotificationException {
		UserNotificationException.ifTrue("Ein leerer Name ist für die Blockung nicht zulässig.", "".equals(pName));
		_daten.name = pName;
	}

	/**
	 * Gibt das Halbjahr der gymnasialen Oberstufe zurück, für welches die Blockung angelegt wurde.
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe
	 */
	public @NotNull GostHalbjahr getHalbjahr() {
		return GostHalbjahr.fromIDorException(_daten.gostHalbjahr);
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @param pHalbjahr das Halbjahr der gymnasialen Oberstufe
	 */
	public void setHalbjahr(final @NotNull GostHalbjahr pHalbjahr) {
		_daten.gostHalbjahr = pHalbjahr.id;
	}

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 */
	public boolean getIstBlockungsVorlage() {
		return _daten.ergebnisse.size() == 1;
	}

	/**
	 * Liefert die Anzahl an Fächern.
	 *
	 * @return Die Anzahl an Fächern.
	 */
	public int getFaecherAnzahl() {
		return _faecherManager.faecher().size();
	}

	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 *
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public @NotNull GostFaecherManager faecherManager() {
		return this._faecherManager;
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 *
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public @NotNull GostBlockungsdaten daten() {
		return this._daten;
	}

	/**
	 * Liefert den Kurs-Comparator der nach (KURSART, FACH, KURSNUMMER) sortiert.
	 *
	 * @return den Kurs-Comparator der nach (KURSART, FACH, KURSNUMMER) sortiert.
	 */
	public @NotNull Comparator<@NotNull GostBlockungKurs> getComparatorKurs_kursart_fach_kursnummer() {
		return _compKurs_kursart_fach_kursnummer;
	}

	/**
	 * Liefert den Kurs-Comparator der nach (FACH, KURSART, KURSNUMMER) sortiert.
	 *
	 * @return den Kurs-Comparator der nach (FACH, KURSART, KURSNUMMER) sortiert.
	 */
	public @NotNull Comparator<@NotNull GostBlockungKurs> getComparatorKurs_fach_kursart_kursnummer() {
		return _compKurs_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine String-Representation vieler Daten.
	 *
	 * @return eine String-Representation vieler Daten.
	 */
	public @NotNull String getDebugString() {
		final @NotNull StringBuilder sb = new StringBuilder();

		sb.append("\nSchülermenge = " + schuelerGetAnzahl() + "\n");
		for (final @NotNull Schueler s : schuelerGetListe()) {
			sb.append("    " + s.id + ", " + s.nachname + ", " + s.vorname + "\n");
		}

		sb.append("\nKurse = " + kursGetAnzahl() + "\n");
		for (final @NotNull GostBlockungKurs k : kursGetListeSortiertNachFachKursartNummer()) {
			sb.append("    " + k.id + ", " + k.fach_id + ", " + k.kursart + ", " + k.nummer + "\n");
		}

		sb.append("\nFachwahlen = " + fachwahlGetAnzahl() + "\n");
		for (final long idFach : _map2d_idFach_idKursart_fachwahlen.getKeySet()) {
			for (final int idKursart : _map2d_idFach_idKursart_fachwahlen.getKeySetOf(idFach)) {
				final int nKurse = _map2d_idFach_idKursart_kurse.getNonNullOrException(idFach, idKursart).size();
				sb.append("    Fach = " + idFach + ", Kursart = " + idKursart + " (" + nKurse + " Kurse)\n");
				final @NotNull List<@NotNull GostFachwahl> list = _map2d_idFach_idKursart_fachwahlen.getNonNullOrException(idFach, idKursart);

				for (final @NotNull  GostFachwahl fachwahl : list) {
					sb.append("        " + fachwahl.schuelerID + "\n");
				}
			}
		}

		return sb.toString();
	}

}
