package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.set.AVLSet;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.StringUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Daten eines Stundenplanes. Die Daten werden aus vier DTO-Objekten aggregiert.
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanManager {

	// StundenplanAufsichtsbereich
	private final @NotNull List<@NotNull StundenplanAufsichtsbereich> _list_aufsichtsbereiche = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanAufsichtsbereich> _map_idAufsichtsbereich_zu_aufsichtsbereich = new HashMap<>();
	private static final @NotNull Comparator<@NotNull StundenplanAufsichtsbereich> _compAufsichtsbereich = (final @NotNull StundenplanAufsichtsbereich a, final @NotNull StundenplanAufsichtsbereich b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};

	// StundenplanFach
	private final @NotNull List<@NotNull StundenplanFach> _list_faecher = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanFach> _map_idFach_zu_fach = new HashMap<>();
	private static final @NotNull Comparator<@NotNull StundenplanFach> _compFach = (final @NotNull StundenplanFach a, final @NotNull StundenplanFach b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};

	// StundenplanJahrgang
	private final @NotNull List<@NotNull StundenplanJahrgang> _list_jahrgaenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanJahrgang> _map_idJahrgang_zu_jahrgang = new HashMap<>();

	// StundenplanKalenderwochenzuordnung
	private final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> _list_kwz = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKalenderwochenzuordnung> _map_idKWZ_zu_kwz = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanKalenderwochenzuordnung> _map2d_jahr_kw_zu_kwz = new HashMap2D<>();
	private static final @NotNull Comparator<@NotNull StundenplanKalenderwochenzuordnung> _compKWZ = (final @NotNull StundenplanKalenderwochenzuordnung a, final @NotNull StundenplanKalenderwochenzuordnung b) -> {
		if (a.jahr < b.jahr) return -1;
		if (a.jahr > b.jahr) return +1;
		if (a.kw < b.kw) return -1;
		if (a.kw > b.kw) return +1;
		if (a.wochentyp < b.wochentyp) return -1;
		if (a.wochentyp > b.wochentyp) return +1;
		return Long.compare(a.id, b.id);
	};

	// StundenplanKlasse
	private final @NotNull List<@NotNull StundenplanKlasse> _list_klassen = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKlasse> _map_idKlasse_zu_klasse = new HashMap<>();

	// StundenplanKurs
	private final @NotNull List<@NotNull StundenplanKurs> _list_kurse = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKurs> _map_idKurs_zu_kurs = new HashMap<>();

	// StundenplanLehrer
	private final @NotNull List<@NotNull StundenplanLehrer> _list_lehrer = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanLehrer> _map_idLehrer_zu_lehrer = new HashMap<>();

	// StundenplanPausenaufsicht
	private final @NotNull List<@NotNull StundenplanPausenaufsicht> _list_pausenaufsichten = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenaufsicht> _map_pausenaufsichtID_zu_pausenaufsicht = new HashMap<>();
	private final @NotNull Comparator<@NotNull StundenplanPausenaufsicht> _compPausenaufsicht = (final @NotNull StundenplanPausenaufsicht a, final @NotNull StundenplanPausenaufsicht b) -> Long.compare(a.id, b.id);

	// StundenplanPausenzeit
	private final @NotNull List<@NotNull StundenplanPausenzeit> _list_pausenzeiten = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenzeit> _map_idPausenzeit_zu_pausenzeit = new HashMap<>();
	private static final @NotNull Comparator<@NotNull StundenplanPausenzeit> _compPausenzeit = (final @NotNull StundenplanPausenzeit a, final @NotNull StundenplanPausenzeit b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;
		final int beginnA = a.beginn == null ? -1 : a.beginn;
		final int beginnB = b.beginn == null ? -1 : b.beginn;
		if (beginnA < beginnB) return -1;
		if (beginnA > beginnB) return +1;
		return Long.compare(a.id, b.id);
	};

	// StundenplanRaum
	private final @NotNull List<@NotNull StundenplanRaum> _list_raeume = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanRaum> _map_idRaum_zu_raum = new HashMap<>();
	private static final @NotNull Comparator<@NotNull StundenplanRaum> _compRaum = (final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};

	// StundenplanSchiene
	private final @NotNull List<@NotNull StundenplanSchiene> _list_schienen = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchiene> _map_idSchiene_zu_schiene = new HashMap<>();

	// StundenplanSchueler
	private final @NotNull List<@NotNull StundenplanSchueler> _list_schueler = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchueler> _map_schuelerID_zu_schueler = new HashMap<>();

	// StundenplanUnterricht
	private final @NotNull List<@NotNull StundenplanUnterricht> _list_unterricht = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanUnterricht> _map_idUnterricht_zu_unterricht = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_idKurs_zu_unterrichtmenge = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull StundenplanUnterricht>> _map2d_idZeitraster_wochentyp_zu_unterrichtmenge = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_idZeitraster_zu_unterrichtmenge = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanLehrer>> _map_idUnterricht_zu_lehrermenge = new HashMap<>();

	// StundenplanZeitraster
	private final @NotNull List<@NotNull StundenplanZeitraster> _list_zeitraster = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanZeitraster> _map_idZeitraster_zu_zeitraster = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanZeitraster> _map2d_wochentag_stunde_zu_zeitraster = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _map_wochentag_zu_zeitrastermenge = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _map_stunde_zu_zeitrastermenge = new HashMap<>();
	private int _zeitrasterWochentagMin = Wochentag.MONTAG.id;
	private int _zeitrasterWochentagMax = Wochentag.MONTAG.id;
	private int _zeitrasterStundeMin = 1;
	private int _zeitrasterStundeMax = 1;
	private @NotNull int @NotNull [] _zeitrasterStundenRange = new int[] {1};
	private @NotNull Wochentag @NotNull [] _zeitrasterWochentageAlsEnumRange = new Wochentag[] {Wochentag.MONTAG};
	private boolean _unterrichtHatMultiWochen = false;
	private static final @NotNull Comparator<@NotNull StundenplanZeitraster> _compZeitraster = (final @NotNull StundenplanZeitraster a, final @NotNull StundenplanZeitraster b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;
		if (a.unterrichtstunde < b.unterrichtstunde) return -1;
		if (a.unterrichtstunde > b.unterrichtstunde) return +1;
		return Long.compare(a.id, b.id);
	};

	// Weiteres
	private final long stundenplanID;
	private final int stundenplanWochenTypModell;
	private final long stundenplanSchuljahresAbschnittID;
	private final @NotNull String stundenplanGueltigAb;
	private final @NotNull String stundenplanGueltigBis;
	private final @NotNull String stundenplanBezeichnung;

	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grund-Daten des Stundenplanes.
	 * @param unterrichte           liefert die Informationen zu allen {@link StundenplanUnterricht} im Stundenplan. Die Liste darf leer sein.
	 * @param pausenaufsichten      liefert die Informationen zu allen {@link StundenplanPausenaufsicht} im Stundenplan. Die Liste darf leer sein.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans. Darf NULL sein.
	 */
	public StundenplanManager(final @NotNull Stundenplan daten, final @NotNull List<@NotNull StundenplanUnterricht> unterrichte, final @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichten, final StundenplanUnterrichtsverteilung unterrichtsverteilung) {
		stundenplanID = daten.id;
		stundenplanWochenTypModell = daten.wochenTypModell;
		stundenplanSchuljahresAbschnittID = daten.idSchuljahresabschnitt;
		stundenplanGueltigAb = daten.gueltigAb;
		stundenplanGueltigBis = daten.gueltigBis;
		stundenplanBezeichnung = daten.bezeichnungStundenplan;

		// Spezialfall: "unterrichtsverteilung" ist NULL
		StundenplanUnterrichtsverteilung uv = unterrichtsverteilung;
		if (uv == null) {
			uv = new StundenplanUnterrichtsverteilung();
			uv.id = stundenplanID;
		}

		// Spezielle Prüfungen.
		DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", daten.id != uv.id);

		// Initialisierungen der Maps und Prüfung der Integrität.
		initAll(daten.kalenderwochenZuordnung,
				uv.faecher,
				daten.jahrgaenge,
				daten.zeitraster,
				daten.raeume,
				daten.pausenzeiten,
				daten.aufsichtsbereiche,
				uv.lehrer,
				uv.schueler,
				daten.schienen,
				uv.klassen,
				pausenaufsichten,
				uv.kurse,
				unterrichte);

	}

	/**
	 * Dieser Manager baut mit Hilfe des {@link StundenplanKomplett}-Objektes eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param stundenplanKomplett  Beinhaltet alle relevanten Daten für einen Stundenplan.
	 */
	public StundenplanManager(final @NotNull StundenplanKomplett stundenplanKomplett) {
		stundenplanID = stundenplanKomplett.daten.id;
		stundenplanWochenTypModell = stundenplanKomplett.daten.wochenTypModell;
		stundenplanSchuljahresAbschnittID = stundenplanKomplett.daten.idSchuljahresabschnitt;
		stundenplanGueltigAb = stundenplanKomplett.daten.gueltigAb;
		stundenplanGueltigBis = stundenplanKomplett.daten.gueltigBis;
		stundenplanBezeichnung = stundenplanKomplett.daten.bezeichnungStundenplan;

		// Spezielle Prüfungen.
		DeveloperNotificationException.ifTrue("Stundenplan.id != StundenplanUnterrichtsverteilung.id", stundenplanKomplett.daten.id != stundenplanKomplett.unterrichtsverteilung.id);

		initAll(stundenplanKomplett.daten.kalenderwochenZuordnung,
				stundenplanKomplett.unterrichtsverteilung.faecher,
				stundenplanKomplett.daten.jahrgaenge,
				stundenplanKomplett.daten.zeitraster,
				stundenplanKomplett.daten.raeume,
				stundenplanKomplett.daten.pausenzeiten,
				stundenplanKomplett.daten.aufsichtsbereiche,
				stundenplanKomplett.unterrichtsverteilung.lehrer,
				stundenplanKomplett.unterrichtsverteilung.schueler,
				stundenplanKomplett.daten.schienen,
				stundenplanKomplett.unterrichtsverteilung.klassen,
				stundenplanKomplett.pausenaufsichten,
				stundenplanKomplett.unterrichtsverteilung.kurse,
				stundenplanKomplett.unterrichte);
	}

	private void initAll(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ,
			             final @NotNull List<@NotNull StundenplanFach> listFach,
			             final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang,
			             final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster,
			             final @NotNull List<@NotNull StundenplanRaum> listRaum,
			             final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit,
			             final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich,
			             final @NotNull List<@NotNull StundenplanLehrer> listLehrer,
			             final @NotNull List<@NotNull StundenplanSchueler> listSchueler,
			             final @NotNull List<@NotNull StundenplanSchiene> listSchiene,
			             final @NotNull List<@NotNull StundenplanKlasse> listKlasse,
			             final @NotNull List<@NotNull StundenplanPausenaufsicht> listPausenaufsicht,
			             final @NotNull List<@NotNull StundenplanKurs> listKurs,
			             final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {

		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell < 0", stundenplanWochenTypModell < 0);
		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell == 1", stundenplanWochenTypModell == 1);

		kalenderwochenzuordnungAddAll(listKWZ);        // ✔, referenziert ---
		fachAddAll(listFach);                          // ✔, referenziert ---
		jahrgangAddAll(listJahrgang);                  // ✔, referenziert ---
		zeitrasterAddAll(listZeitraster);              // ✔, referenziert ---
		raumAddAll(listRaum);                          // ✔, referenziert ---
		pausenzeitAddAll(listPausenzeit);              // ✔, referenziert ---
		aufsichtsbereichAddAll(listAufsichtsbereich);  // ✔, referenziert ---
		klasseAddAll(listKlasse);                      // ✔, referenziert [Jahrgang], es gibt auch jahrgangsübergreifende Klassen!
		lehrerAddAll(listLehrer);                      // ✔, referenziert [Fach]
		schuelerAddAll(listSchueler);                  // ✔, referenziert Klasse
		schieneAddAll(listSchiene);                    // ✔, referenziert Jahrgang
		pausenaufsichtAddAll(listPausenaufsicht);      // ✔, referenziert Lehrer, Pausenzeit, [Aufsichtsbereich]
		kursAddAll(listKurs);                          // ✔, referenziert [Schienen], [Jahrgang], [Schüler]
		unterrichtAddAll(listUnterricht);              // ✔, referenziert Zeitraster, Kurs, Fach, [Lehrer], [Klasse], [Raum], [Schiene]
	}

	// #####################################################################
	// #################### StundenplanAufsichtsbereich ####################
	// #####################################################################

	private void aufsichtsbereichAddOhneUpdate(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		// Prüfen
		DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsichtsbereich.id);
		DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsichtsbereich.kuerzel);
		// aufsicht.beschreibung darf "blank" sein

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idAufsichtsbereich_zu_aufsichtsbereich, aufsichtsbereich.id, aufsichtsbereich);
		DeveloperNotificationException.ifListAddsDuplicate("_list_aufsichtsbereiche", _list_aufsichtsbereiche, aufsichtsbereich);
	}

	/**
	 * Fügt ein {@link StundenplanAufsichtsbereich}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param aufsichtsbereich  Das {@link StundenplanAufsichtsbereich}-Objekt, welches hinzugefügt werden soll.
	 */
	public void aufsichtsbereichAdd(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		aufsichtsbereichAddOhneUpdate(aufsichtsbereich);
		aufsichtsbereichUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanAufsichtsbereich}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param listAufsichtsbereich  Die Menge der {@link StundenplanAufsichtsbereich}-Objekte, welche hinzugefügt werden soll.
	 */
	public void aufsichtsbereichAddAll(final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich) {
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : listAufsichtsbereich)
			aufsichtsbereichAddOhneUpdate(aufsichtsbereich);
		aufsichtsbereichUpdate();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public @NotNull StundenplanAufsichtsbereich aufsichtsbereichGetByIdOrException(final long idAufsichtsbereich) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idAufsichtsbereich_zu_aufsichtsbereich, idAufsichtsbereich);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanAufsichtsbereich> aufsichtsbereichGetMengeAsList() {
		return _list_aufsichtsbereiche;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanAufsichtsbereich}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param aufsichtsbereich  Das neue {@link StundenplanAufsichtsbereich}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void aufsichtsbereichPatch(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		aufsichtsbereichRemoveOhneUpdate(aufsichtsbereich.id);
		aufsichtsbereichAddOhneUpdate(aufsichtsbereich);
		aufsichtsbereichUpdate();
	}

	private void aufsichtsbereichRemoveOhneUpdate(final long idAufsichtsbereich) {
		// Get
		final @NotNull StundenplanAufsichtsbereich a = DeveloperNotificationException.ifMapGetIsNull(_map_idAufsichtsbereich_zu_aufsichtsbereich, idAufsichtsbereich);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idAufsichtsbereich_zu_aufsichtsbereich, a.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_aufsichtsbereiche", _list_aufsichtsbereiche, a);
	}

	/**
	 * Entfernt ein {@link StundenplanAufsichtsbereich}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanAufsichtsbereich|), da aufsichtsbereichUpdate() aufgerufen wird.
	 *
	 * @param idAufsichtsbereich  Die Datenbank-ID des {@link StundenplanAufsichtsbereich}-Objekts, welches entfernt werden soll.
	 */
	public void aufsichtsbereichRemove(final long idAufsichtsbereich) {
		aufsichtsbereichRemoveOhneUpdate(idAufsichtsbereich);
		aufsichtsbereichUpdate();
	}

	private void aufsichtsbereichUpdate() {
		// Überprüfe, ob doppelte StundenplanAufsichtsbereich-Kürzel vorhanden sind.
		final @NotNull HashSet<@NotNull String> setAufsichtKuerzel = new HashSet<>();
		for (final @NotNull StundenplanAufsichtsbereich aufsicht : _list_aufsichtsbereiche)
			DeveloperNotificationException.ifSetAddsDuplicate("setAufsichtKuerzel", setAufsichtKuerzel, aufsicht.kuerzel);

		// Sortieren
		_list_aufsichtsbereiche.sort(_compAufsichtsbereich);
	}

	private void fachAddOhneUpdate(final @NotNull StundenplanFach fach) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idFach_zu_fach, fach.id, fach);
		DeveloperNotificationException.ifListAddsDuplicate("_list_faecher", _list_faecher, fach);
	}

	/**
	 * Fügt ein {@link StundenplanFach}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param fach  Das {@link StundenplanFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public void fachAdd(final @NotNull StundenplanFach fach) {
		fachAddOhneUpdate(fach);
		fachUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanFach}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param listFach  Die Menge der {@link StundenplanFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public void fachAddAll(final @NotNull List<@NotNull StundenplanFach> listFach) {
		for (final @NotNull StundenplanFach fach : listFach)
			fachAddOhneUpdate(fach);
		fachUpdate();
	}

	/**
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 */
	public @NotNull StundenplanFach fachGetByIdOrException(final long idFach) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idFach_zu_fach, idFach);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanFach}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanFach}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanFach> fachGetMengeAsList() {
		return _list_faecher;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanFach}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param fach  Das neue {@link StundenplanFach}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void fachPatch(final @NotNull StundenplanFach fach) {
		fachRemoveOhneUpdate(fach.id);
		fachAddOhneUpdate(fach);
		fachUpdate();
	}

	private void fachRemoveOhneUpdate(final long idFach) {
		// Get
		final @NotNull StundenplanFach f = DeveloperNotificationException.ifMapGetIsNull(_map_idFach_zu_fach, idFach);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idFach_zu_fach, f.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_faecher", _list_faecher, f);
	}

	/**
	 * Entfernt ein {@link StundenplanFach}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanFach|), da fachUpdate() aufgerufen wird.
	 *
	 * @param idFach  Die Datenbank-ID des {@link StundenplanFach}-Objekts, welches entfernt werden soll.
	 */
	public void fachRemove(final long idFach) {
		fachRemoveOhneUpdate(idFach);
		fachUpdate();
	}

	private void fachUpdate() {
		// Überprüfe, ob doppelte StundenplanFach-Kürzel vorhanden sind.
		final @NotNull HashSet<@NotNull String> setFachKuerzel = new HashSet<>();
		for (final @NotNull StundenplanFach fach: _list_faecher)
			DeveloperNotificationException.ifSetAddsDuplicate("setFachKuerzel", setFachKuerzel, fach.kuerzel);

		// Sortieren
		_list_faecher.sort(_compFach);
	}

	private void jahrgangAddOhneUpdate(final @NotNull StundenplanJahrgang jahrgang) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idJahrgang_zu_jahrgang, jahrgang.id, jahrgang);
		DeveloperNotificationException.ifListAddsDuplicate("_list_jahrgaenge", _list_jahrgaenge, jahrgang);
	}

	/**
	 * Fügt ein {@link StundenplanJahrgang}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param jahrgang  Das {@link StundenplanJahrgang}-Objekt, welches hinzugefügt werden soll.
	 */
	public void jahrgangAdd(final @NotNull StundenplanJahrgang jahrgang) {
		jahrgangAddOhneUpdate(jahrgang);
		jahrgangUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanJahrgang}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param listJahrgang  Die Menge der {@link StundenplanJahrgang}-Objekte, welche hinzugefügt werden soll.
	 */
	public void jahrgangAddAll(final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang) {
		for (final @NotNull StundenplanJahrgang jahrgang : listJahrgang)
			jahrgangAddOhneUpdate(jahrgang);
		jahrgangUpdate();
	}

	/**
	 * Liefert das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts.
	 *
	 * @return das {@link StundenplanJahrgang}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanJahrgang jahrgangGetByIdOrException(final long idJahrgang) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idJahrgang_zu_jahrgang, idJahrgang);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanJahrgang}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanJahrgang}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanJahrgang> jahrgangGetMengeAsList() {
		return _list_jahrgaenge;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanJahrgang}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param jahrgang  Das neue {@link StundenplanJahrgang}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void jahrgangPatch(final @NotNull StundenplanJahrgang jahrgang) {
		jahrgangRemoveOhneUpdate(jahrgang.id);
		jahrgangAddOhneUpdate(jahrgang);
		jahrgangUpdate();
	}

	private void jahrgangRemoveOhneUpdate(final long idJahrgang) {
		// Get
		final @NotNull StundenplanJahrgang j = DeveloperNotificationException.ifMapGetIsNull(_map_idJahrgang_zu_jahrgang, idJahrgang);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idJahrgang_zu_jahrgang, j.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_jahrgaenge", _list_jahrgaenge, j);
	}

	/**
	 * Entfernt ein {@link StundenplanJahrgang}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanJahrgang|), da jahrgangUpdate() aufgerufen wird.
	 *
	 * @param idJahrgang  Die Datenbank-ID des {@link StundenplanJahrgang}-Objekts, welches entfernt werden soll.
	 */
	public void jahrgangRemove(final long idJahrgang) {
		jahrgangRemoveOhneUpdate(idJahrgang);
		jahrgangUpdate();
	}

	private void jahrgangUpdate() {
		// Überprüfe, ob doppelte StundenplanJahrgang-Kürzel vorhanden sind.
		final @NotNull HashSet<@NotNull String> setJahrgangKuerzel = new HashSet<>();
		for (final @NotNull StundenplanJahrgang jahrgang : _list_jahrgaenge)
			DeveloperNotificationException.ifSetAddsDuplicate("setJahrgangKuerzel", setJahrgangKuerzel, jahrgang.kuerzel);
	}

	private void kalenderwochenzuordnungAddOhneUpdate(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("kw.id", kwz.id);
		DeveloperNotificationException.ifTrue("(kwz.jahr < 2000) || (kwz.jahr > 3000)", (kwz.jahr < 2000) || (kwz.jahr > 3000));
		DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > 53)", (kwz.kw < 1) || (kwz.kw > 53));
		DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("kwz.wochentyp <= 0", kwz.wochentyp <= 0);

		// Hinzufügen
		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, kwz);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKWZ_zu_kwz, kwz.id, kwz);
		_list_kwz.add(kwz);
	}

	/**
	 * Fügt ein {@link StundenplanKalenderwochenzuordnung}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung| * log ), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt, welches hinzugefügt werden soll.
	 */
	public void kalenderwochenzuordnungAdd(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungAddOhneUpdate(kwz);
		kalenderwochenzuordnungUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanKalenderwochenzuordnung}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung| * log ), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param listKWZ  Die Menge der {@link StundenplanKalenderwochenzuordnung}-Objekte, welche hinzugefügt werden soll.
	 */
	public void kalenderwochenzuordnungAddAll(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ) {
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : listKWZ)
			kalenderwochenzuordnungAddOhneUpdate(kwz);
		kalenderwochenzuordnungUpdate();
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param idKWZ Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 */
	public @NotNull StundenplanKalenderwochenzuordnung kalenderwochenzuordnungGetByIdOrException(final long idKWZ) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idKWZ_zu_kwz, idKWZ);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> kalenderwochenzuordnungGetMengeAsList() {
		return _list_kwz;
	}

	/**
	 * Liefert eine String-Darstellung der Kalenderwoche.
	 * <br>Beispiel: Jahr 2023, KW  5 --> "30.01.2023 - 05.02.2023 (KW 2023.5)"
	 * <br>Beispiel: Jahr 2025, KW  1 --> "30.12.2024 - 05.01.2025 (KW 2025.1)"
	 * <br>Beispiel: Jahr 2026, KW 53 --> "28.12.2026 - 03.01.2027 (KW 2026.53)"
	 * <br>Laufzeit: O(1)

	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @return eine String-Darstellung der Kalenderwoche.
	 */
	public @NotNull String kalenderwochenzuordnungGetWocheAsString(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		final @NotNull String sMo = DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		final @NotNull String sSo = DateUtils.gibDatumDesSonntagsOfJahrAndKalenderwoche(kwz.jahr, kwz.kw);
		final @NotNull String sMoGer = DateUtils.gibDatumGermanFormat(sMo);
		final @NotNull String sSoGer = DateUtils.gibDatumGermanFormat(sSo);
		final @NotNull String sJahrKW = "(KW " + kwz.jahr + "." + kwz.kw + ")";
		return sMoGer + " - " + sSoGer + " " + sJahrKW;
	}

	/**
	 * Liefert den zugeordneten Wochentyp, oder den Default-Wochentyp.
	 *
	 * @param jahr          Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return den zugeordneten Wochentyp, oder den Default-Wochentyp.
	 */
	public int kalenderwochenzuordnungGetWochentypOrDefault(final int jahr, final int kalenderwoche) {
		// Überprüfen
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));

		// Fall: Das globale Modell hat keine Multiwochen.
		if (stundenplanWochenTypModell == 0)
			return 0;

		// Fall: Eine Zuordnung ist definiert.
		final StundenplanKalenderwochenzuordnung z = _map2d_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z != null)
			return z.wochentyp;

		// Default: Der Wert berechnet sich modulo der Kalenderwoche.
		final int wochentyp = kalenderwoche % stundenplanWochenTypModell;
		return wochentyp == 0 ? stundenplanWochenTypModell : wochentyp; // 0 wird zu stundenplanWochenTypModell rotiert!
	}

	/**
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" auf "Kalenderwoche" verwendet wird.
	 *
	 * @param jahr          Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" auf "Kalenderwoche" verwendet wird.
	 */
	public boolean kalenderwochenzuordnungGetWochentypUsesMapping(final int jahr, final int kalenderwoche) {
		// Überprüfen
		DeveloperNotificationException.ifSmaller("jahr", jahr, DateUtils.MIN_GUELTIGES_JAHR);
		DeveloperNotificationException.ifGreater("jahr", jahr, DateUtils.MAX_GUELTIGES_JAHR);
		DeveloperNotificationException.ifSmaller("kalenderwoche", kalenderwoche, 1);
		DeveloperNotificationException.ifGreater("kalenderwoche", kalenderwoche, DateUtils.gibKalenderwochenOfJahr(jahr));

		// Berechnen
		final StundenplanKalenderwochenzuordnung z = _map2d_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		return (stundenplanWochenTypModell >= 2) && (z != null);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanKalenderwochenzuordnung}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param kwz Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void kalenderwochenzuordnungPatch(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungRemoveOhneUpdate(kwz.id);
		kalenderwochenzuordnungAddOhneUpdate(kwz);
		kalenderwochenzuordnungUpdate();
	}

	private void kalenderwochenzuordnungRemoveOhneUpdate(final long idKWZ) {
		// Get
		final @NotNull StundenplanKalenderwochenzuordnung k = DeveloperNotificationException.ifMapGetIsNull(_map_idKWZ_zu_kwz, idKWZ);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKWZ_zu_kwz, k.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_jahr_kw_zu_kwz, k.jahr, k.kw);
		DeveloperNotificationException.ifListRemoveFailes("_list_kalenderwochenzuordnungen", _list_kwz, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung|), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param idKWZ  Die Datenbank-ID des {@link StundenplanKalenderwochenzuordnung}-Objekts, welches entfernt werden soll.
	 */
	public void kalenderwochenzuordnungRemove(final long idKWZ) {
		jahrgangRemoveOhneUpdate(idKWZ);
		jahrgangUpdate();
	}

	/**
	 * Aktualisiert verschiedene Werte nachdem sich die Menge der {@link StundenplanKalenderwochenzuordnung} verändert hat.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung| * log)
	 */
	private void kalenderwochenzuordnungUpdate() {
		// Sortieren.
		_list_kwz.sort(_compKWZ);
	}

	private void klasseAddOhneUpdate(final @NotNull StundenplanKlasse klasse) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
		// klasse.bezeichnung darf "blank" sein

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKlasse_zu_klasse, klasse.id, klasse);
		_list_klassen.add(klasse);
	}

	/**
	 * Fügt ein {@link StundenplanKlasse}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanKlasse|), da klasseUpdate() aufgerufen wird.
	 *
	 * @param klasse  Das {@link StundenplanKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public void klasseAdd(final @NotNull StundenplanKlasse klasse) {
		klasseAddOhneUpdate(klasse);
		klasseUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanKlasse}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanKlasse|), da klasseUpdate() aufgerufen wird.
	 *
	 * @param listKlasse  Die Menge der {@link StundenplanKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public void klasseAddAll(final @NotNull List<@NotNull StundenplanKlasse> listKlasse) {
		for (final @NotNull StundenplanKlasse klasse : listKlasse)
			klasseAddOhneUpdate(klasse);
		klasseUpdate();
	}

	/**
	 * Liefert das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts.
	 *
	 * @return das {@link StundenplanKlasse}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanKlasse klasseGetByIdOrException(final long idKlasse) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klasse, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlasse}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKlasse}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKlasse> klasseGetMengeAsList() {
		return _list_klassen;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanKlasse}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param klasse  Das neue {@link StundenplanKlasse}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void klassePatch(final @NotNull StundenplanKlasse klasse) {
		klasseRemoveOhneUpdate(klasse.id);
		klasseAddOhneUpdate(klasse);
		klasseUpdate();
	}

	private void klasseRemoveOhneUpdate(final long idKlasse) {
		// Get
		final @NotNull StundenplanKlasse k = DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klasse, idKlasse);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKlasse_zu_klasse, k.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_klasse", _list_klassen, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKlasse}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanKlasse|), da klasseUpdate() aufgerufen wird.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts, welches entfernt werden soll.
	 */
	public void klasseRemove(final long idKlasse) {
		klasseRemoveOhneUpdate(idKlasse);
		klasseUpdate();
	}

	private void klasseUpdate() {
		// Überprüfe, ob doppelte StundenplanKlasse-Kürzel vorhanden sind.
		final @NotNull HashSet<@NotNull String> setKlasseKuerzel = new HashSet<>();
		for (final @NotNull StundenplanKlasse klasse : _list_klassen) {
			DeveloperNotificationException.ifSetAddsDuplicate("setKlasseKuerzel", setKlasseKuerzel, klasse.kuerzel);

			// Überprüfen, ob die Klasse doppelte Jahrgänge hat.
			final @NotNull HashSet<@NotNull Long> setJahrgaengeDerKlasse = new HashSet<>();
			for (final @NotNull Long idJahrgangDerKlasse : klasse.jahrgaenge) {
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_idJahrgang_zu_jahrgang, idJahrgangDerKlasse);
				DeveloperNotificationException.ifSetAddsDuplicate("setJahrgaengeDerKlasse", setJahrgaengeDerKlasse, idJahrgangDerKlasse);
			}
		}
	}

	private void kursAddOhneUpdate(final @NotNull StundenplanKurs kurs) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
		for (final @NotNull Long idSchieneDesKurses : kurs.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", _map_idSchiene_zu_schiene, idSchieneDesKurses);
		for (final @NotNull Long idJahrgangDesKurses : kurs.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_idJahrgang_zu_jahrgang, idJahrgangDesKurses);
		for (final @NotNull Long idSchuelerDesKurses : kurs.schueler)
			DeveloperNotificationException.ifMapNotContains("_map_schuelerID_zu_schueler", _map_schuelerID_zu_schueler, idSchuelerDesKurses);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKurs_zu_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKurs_zu_unterrichtmenge, kurs.id, new ArrayList<>());
		DeveloperNotificationException.ifListAddsDuplicate("_list_kurse", _list_kurse, kurs);
	}

	/**
	 * Fügt ein {@link StundenplanKurs}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanKurs|), da kursUpdate() aufgerufen wird.
	 *
	 * @param kurs  Das {@link StundenplanKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public void kursAdd(final @NotNull StundenplanKurs kurs) {
		kursAddOhneUpdate(kurs);
		kursUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanKurs}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanKurs|), da kursUpdate() aufgerufen wird.
	 *
	 * @param listKurs  Die Menge der {@link StundenplanKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public void kursAddAll(final @NotNull List<@NotNull StundenplanKurs> listKurs) {
		for (final @NotNull  StundenplanKurs kurs : listKurs)
			kursAddOhneUpdate(kurs);
		kursUpdate();
	}

	/**
	 * Liefert das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return das {@link StundenplanKurs}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanKurs kursGetByIdOrException(final long idKurs) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_kurs, idKurs);
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs am (Wochentyp / Wochentag / Unterrichtsstunde) hat.
	 *
	 * @param idKurs            Die Datenbank-ID des Kurses.
	 * @param wochentyp         Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag         Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde  Die gewünschte Unterrichtsstunde.
	 *
	 * @return TRUE, falls der übergebene Kurs am (wochentyp / wochentag / Unterrichtsstunde) hat.
	 */
	public boolean kursGetHatUnterrichtAm(final long idKurs, final int wochentyp, final @NotNull Wochentag wochentag, final int unterrichtstunde) {
		for (final @NotNull StundenplanUnterricht u : unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp)) {
			final @NotNull StundenplanZeitraster z =  zeitrasterGetByIdOrException(u.idZeitraster);
			if ((z.wochentag == wochentag.id) && (z.unterrichtstunde == unterrichtstunde))
				return true;
		}
		return false;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeAsList() {
		return _list_kurse;
	}


	/**
	 * Liefert gefilterte Kurs-IDs, deren Unterricht zu (Wochentyp / Wochentag / Unterrichtsstunde) passt.
	 *
	 * @param idsKurs          Die Liste aller Kurs-IDs.
	 * @param wochentyp        Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag        Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return gefilterte Kurs-IDs, deren Unterricht zu (Wochentyp / Wochentag / Unterrichtsstunde) passt.
	 */
	public @NotNull List<@NotNull Long> kursGetMengeGefiltertByWochentypAndWochentagAndStunde(final @NotNull List<@NotNull Long> idsKurs, final int wochentyp, final @NotNull Wochentag wochentag, final int unterrichtstunde) {
		return CollectionUtils.toFilteredArrayList(idsKurs, (final @NotNull Long idKurs) -> kursGetHatUnterrichtAm(idKurs, wochentyp, wochentag, unterrichtstunde));
	}

	/**
	 * Liefert gefilterte Kurs-IDs, deren Unterricht zu (Datum / Unterrichtsstunde) passt.
	 *
	 * @param idsKurs          Die Liste aller Kurs-IDs.
	 * @param datumISO8601     Das Datum. Daraus ergibt sich (Wochentyp / Wochentag).
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return gefilterte Kurs-IDs, deren Unterricht zu (Datum / Unterrichtsstunde) passt.
	 */
	public @NotNull List<@NotNull Long> kursGetMengeGefiltertByDatumAndStunde(final @NotNull List<@NotNull Long> idsKurs, final @NotNull String datumISO8601, final int unterrichtstunde) {
		final int[] e = DateUtils.extractFromDateISO8601(datumISO8601);
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(e[6], e[5]); // 6 = kalenderwochenjahr, 5 = kalenderwoche
		final @NotNull Wochentag wochentag = Wochentag.fromIDorException(e[3]); // 3 = tagInWoche
		return kursGetMengeGefiltertByWochentypAndWochentagAndStunde(idsKurs, wochentyp, wochentag, unterrichtstunde);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanKurs}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param kurs  Das neue {@link StundenplanKurs}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void kursPatch(final @NotNull StundenplanKurs kurs) {
		kursRemoveOhneUpdate(kurs.id);
		kursAddOhneUpdate(kurs);
		kursUpdate();
	}

	private void kursRemoveOhneUpdate(final long idKurs) {
		// Get
		final @NotNull StundenplanKurs k = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_kurs, idKurs);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_zu_kurs, k.id);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_zu_unterrichtmenge, k.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_kurse", _list_kurse, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKurs}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanKurs|), da kursUpdate() aufgerufen wird.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts, welches entfernt werden soll.
	 */
	public void kursRemove(final long idKurs) {
		kursRemoveOhneUpdate(idKurs);
		kursUpdate();
	}

	private void kursUpdate() {
		// ...
	}

	private void schieneAddOhneUpdate(final @NotNull StundenplanSchiene schiene) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
		DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
		DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_idJahrgang_zu_jahrgang, schiene.idJahrgang);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idSchiene_zu_schiene, schiene.id, schiene);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schienen", _list_schienen, schiene);
	}

	/**
	 * Fügt ein {@link StundenplanSchiene}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanSchiene|), da schienenUpdate() aufgerufen wird.
	 *
	 * @param schiene  Das {@link StundenplanSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schieneAdd(final @NotNull StundenplanSchiene schiene) {
		schieneAddOhneUpdate(schiene);
		schieneUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanSchiene}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanSchiene|), da schienenUpdate() aufgerufen wird.
	 *
	 * @param listSchiene  Die Menge der {@link StundenplanSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schieneAddAll(final @NotNull List<@NotNull StundenplanSchiene> listSchiene) {
		for (final @NotNull StundenplanSchiene schiene : listSchiene)
			schieneAddOhneUpdate(schiene);
		schieneUpdate();
	}

	private void schieneUpdate() {
		// ...
	}

	private void schuelerAddOhneUpdate(final @NotNull StundenplanSchueler schueler) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
		DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
		DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
		DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", _map_idKlasse_zu_klasse, schueler.idKlasse);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_zu_schueler, schueler.id, schueler);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schueler", _list_schueler, schueler);
	}

	/**
	 * Fügt ein {@link StundenplanSchueler}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanSchueler|), da schuelerUpdate() aufgerufen wird.
	 *
	 * @param schueler  Das {@link StundenplanSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schuelerAdd(final @NotNull StundenplanSchueler schueler) {
		schuelerAddOhneUpdate(schueler);
		schuelerUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanSchueler}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanSchueler|), da schuelerUpdate() aufgerufen wird.
	 *
	 * @param listSchueler  Die Menge der {@link StundenplanSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schuelerAddAll(final @NotNull List<@NotNull StundenplanSchueler> listSchueler) {
		for (final @NotNull StundenplanSchueler schueler : listSchueler)
			schuelerAddOhneUpdate(schueler);
		schuelerUpdate();
	}

	private void schuelerUpdate() {
		// noch leer, später O(|StundenplanSchueler|) Checks.
	}

	private void lehrerAddOhneUpdate(final @NotNull StundenplanLehrer lehrer) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
		DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
		DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idLehrer_zu_lehrer, lehrer.id, lehrer);
		DeveloperNotificationException.ifListAddsDuplicate("_list_lehrer", _list_lehrer, lehrer);
	}

	/**
	 * Fügt ein {@link StundenplanLehrer}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanLehrer|), da lehrerUpdate() aufgerufen wird.
	 *
	 * @param lehrer  Das {@link StundenplanLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lehrerAdd(final @NotNull StundenplanLehrer lehrer) {
		lehrerAddOhneUpdate(lehrer);
		lehrerUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanLehrer}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanLehrer|), da lehrerUpdate() aufgerufen wird.
	 *
	 * @param listLehrer  Die Menge der {@link StundenplanLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lehrerAddAll(final @NotNull List<@NotNull StundenplanLehrer> listLehrer) {
		for (final @NotNull StundenplanLehrer lehrer : listLehrer)
			lehrerAddOhneUpdate(lehrer);
		lehrerUpdate();
	}

	private void lehrerUpdate() {
		final @NotNull HashSet<@NotNull String> setLehrerKuerzel = new HashSet<>();
		for (final @NotNull StundenplanLehrer lehrer : _list_lehrer) {
			DeveloperNotificationException.ifSetAddsDuplicate("setLehrerKuerzel", setLehrerKuerzel, lehrer.kuerzel);

			// Konsistenz der Fächer der Lehrkraft überprüfen.
			final @NotNull HashSet<@NotNull Long> setFaecherDerLehrkraft = new HashSet<>();
			for (final @NotNull Long idFachDerLehrkraft : lehrer.faecher) {
				DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", _map_idFach_zu_fach, idFachDerLehrkraft);
				DeveloperNotificationException.ifSetAddsDuplicate("setFaecherDerLehrkraft", setFaecherDerLehrkraft, idFachDerLehrkraft);
			}
		}
	}

	private void pausenzeitAddOhneUpdate(final @NotNull StundenplanPausenzeit pausenzeit) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
		Wochentag.fromIDorException(pausenzeit.wochentag);
		if ((pausenzeit.beginn != null) && (pausenzeit.ende != null)) {
			final int beginn = pausenzeit.beginn;
			final int ende = pausenzeit.ende;
			DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
		}

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idPausenzeit_zu_pausenzeit, pausenzeit.id, pausenzeit);
		_list_pausenzeiten.add(pausenzeit);
	}

	/**
	 * Fügt ein {@link StundenplanPausenzeit}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenzeit|), da pausenzeitUpdate() aufgerufen wird.
	 *
	 * @param pausenzeit  Das {@link StundenplanPausenzeit}-Objekt, welches hinzugefügt werden soll.
	 */
	public void pausenzeitAdd(final @NotNull StundenplanPausenzeit pausenzeit) {
		pausenzeitAddOhneUpdate(pausenzeit);
		pausenzeitUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanPausenzeit}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenzeit|), da pausenzeitUpdate() aufgerufen wird.
	 *
	 * @param listPausenzeit  Die Menge der {@link StundenplanPausenzeit}-Objekte, welche hinzugefügt werden soll.
	 */
	public void pausenzeitAddAll(final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit) {
		for (final @NotNull StundenplanPausenzeit pausenzeit : listPausenzeit)
			pausenzeitAddOhneUpdate(pausenzeit);
		pausenzeitUpdate();
	}

	private void pausenzeitUpdate() {
		// Sortieren
		_list_pausenzeiten.sort(_compPausenzeit);

	}

	private void raumAddOhneUpdate(final @NotNull StundenplanRaum raum) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
		// raum.beschreibung darf "blank" sein!
		DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idRaum_zu_raum, raum.id, raum);
		_list_raeume.add(raum);
	}

	/**
	 * Fügt ein {@link StundenplanRaum}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanRaum|), da raumUpdate() aufgerufen wird.
	 *
	 * @param raum  Das {@link StundenplanRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public void raumAdd(final @NotNull StundenplanRaum raum) {
		raumAddOhneUpdate(raum);
		raumUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanRaum}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanRaum|), da raumUpdate() aufgerufen wird.
	 *
	 * @param listRaum  Die Menge der {@link StundenplanRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public void raumAddAll(final @NotNull List<@NotNull StundenplanRaum> listRaum) {
		for (final @NotNull StundenplanRaum raum : listRaum)
			raumAddOhneUpdate(raum);
		raumUpdate();
	}

	private void raumUpdate() {
		// Überprüfe, ob doppelte StundenplanRaum-Kürzel vorhanden sind.
		final @NotNull HashSet<@NotNull String> setRaumKuerzel = new HashSet<>();
		for (final @NotNull StundenplanRaum raum : _list_raeume)
			DeveloperNotificationException.ifSetAddsDuplicate("setRaumKuerzel", setRaumKuerzel, raum.kuerzel);

		// Sortieren
		_list_raeume.sort(_compRaum);
	}

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public long getIDSchuljahresabschnitt() {
		return stundenplanSchuljahresAbschnittID;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigAb() {
		return stundenplanGueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigBis() {
		return stundenplanGueltigBis;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public @NotNull String getBezeichnungStundenplan() {
		return stundenplanBezeichnung;
	}

	/**
	 * Liefert das Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Kein gültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 *
	 * @return das Modell für die Wochen des Stundenplans.
	 */
	public int getWochenTypModell() {
		return stundenplanWochenTypModell;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanRaum> getListRaum() {
		return _list_raeume;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> getListPausenzeit() {
		return _list_pausenzeiten;
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur raumID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public @NotNull StundenplanRaum getRaum(final long idRaum) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idRaum_zu_raum, idRaum);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public @NotNull StundenplanPausenzeit getPausenzeit(final long idPausenzeit) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, idPausenzeit);
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanRaum} hinzu.
	 *
	 * @param raum Der Raum, der hinzugefügt werden soll.
	 */
	public void addRaum(final @NotNull StundenplanRaum raum) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_idRaum_zu_raum, raum.id, raum);
		_list_raeume.add(raum);
		_list_raeume.sort(_compRaum);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenzeit} hinzu.
	 *
	 * @param pausenzeit Die Pausenzeit, die hinzugefügt werden soll.
	 */
	public void addPausenzeit(final @NotNull StundenplanPausenzeit pausenzeit) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_idPausenzeit_zu_pausenzeit, pausenzeit.id, pausenzeit);
		_list_pausenzeiten.add(pausenzeit);
		_list_pausenzeiten.sort(_compPausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierenden {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public void removeRaum(final long idRaum) {
		final @NotNull StundenplanRaum raum = DeveloperNotificationException.ifNull("_map_raumID_zu_raum.get(" + idRaum + ")", _map_idRaum_zu_raum.get(idRaum));
		_map_idRaum_zu_raum.remove(idRaum);
		_list_raeume.remove(raum); // Neusortierung nicht nötig.
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public void removePausenzeit(final long idPausenzeit) {
		final @NotNull StundenplanPausenzeit pausenzeit = DeveloperNotificationException.ifNull("_map_pausenzeitID_zu_pausenzeit.get(" + idPausenzeit + ")", _map_idPausenzeit_zu_pausenzeit.get(idPausenzeit));
		_map_idPausenzeit_zu_pausenzeit.remove(idPausenzeit);
		_list_pausenzeiten.remove(pausenzeit); // Neusortierung nicht nötig.
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanRaum}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param raum Das neue {@link StundenplanRaum}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void patchRaum(final @NotNull StundenplanRaum raum) {
		removeRaum(raum.id);
		addRaum(raum);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenzeit}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param pausenzeit Das neue {@link StundenplanPausenzeit}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void patchPausenzeit(final @NotNull StundenplanPausenzeit pausenzeit) {
		removePausenzeit(pausenzeit.id);
		addPausenzeit(pausenzeit);
	}





	// ############################################################
	// #################### REFACTORED FROM HERE ##################
	// ############################################################

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @deprecated use {@link #pausenaufsichtGetMenge()}
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	@Deprecated (forRemoval = true)
	public @NotNull List<@NotNull StundenplanPausenaufsicht> getListPausenaufsicht() {
		return pausenaufsichtGetMenge();
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenaufsicht} hinzu.
	 *
	 * @deprecated use {@link #pausenaufsichtAdd(StundenplanPausenaufsicht)}
	 * @param pausenaufsicht Die StundenplanPausenaufsicht, die hinzugefügt werden soll.
	 */
	@Deprecated (forRemoval = true)
	public void addPausenaufsicht(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtAdd(pausenaufsicht);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @deprecated use {@link #pausenaufsichtRemove(long)}
	 * @param idPausenaufsicht Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	@Deprecated (forRemoval = true)
	public void removePausenaufsicht(final long idPausenaufsicht) {
		pausenaufsichtRemove(idPausenaufsicht);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @deprecated use {@link #pausenaufsichtGet(long)}
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	@Deprecated (forRemoval = true)
	public @NotNull StundenplanPausenaufsicht getPausenaufsicht(final long idPausenaufsicht) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenaufsicht}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @deprecated use {@link #pausenaufsichtPatch(StundenplanPausenaufsicht)}
	 * @param pausenaufsicht Das neue {@link StundenplanPausenaufsicht}-Objekt, welches das alte Objekt ersetzt.
	 */
	@Deprecated (forRemoval = true)
	public void patchPausenaufsicht(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtPatch(pausenaufsicht);
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @deprecated use {@link #zeitrasterGetStundeMin()}
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	@Deprecated (forRemoval = true)
	public int getZeitrasterStundeMin() {
		return zeitrasterGetStundeMin();
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @deprecated use {@link #zeitrasterGetStundeMax()}
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	@Deprecated (forRemoval = true)
	public int getZeitrasterStundeMax() {
		return zeitrasterGetStundeMax();
	}


	/**
	 * Liefert die ID des Stundenplans.
	 *
	 * @deprecated use {@link #stundenplanGetID()}
	 * @return die ID des Stundenplans.
	 */
	@Deprecated (forRemoval = true)
	public long getID() {
		return stundenplanID;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getListZeitraster() {
		return _list_zeitraster;
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einem bestimmten Wochentag.
	 *
	 * @param wochentag der Wochentag der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zum übergeben Wochentag.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getListZeitrasterZuWochentag(final @NotNull Wochentag wochentag) {
		return CollectionUtils.toFilteredArrayList(_list_zeitraster, (final @NotNull StundenplanZeitraster z) -> (wochentag.id == z.wochentag));
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berühren.
	 *
	 * @param zeitrasterStart    Das {@link StundenplanZeitraster} zu dem es startet.
	 * @param minutenVerstrichen Die verstrichene Zeit (in Minuten) seit der "startzeit" .
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getZeitrasterByStartVerstrichen(final @NotNull StundenplanZeitraster zeitrasterStart, final int minutenVerstrichen) {
		final Wochentag wochentag = Wochentag.fromIDorException(zeitrasterStart.wochentag);
		final int startzeit = DeveloperNotificationException.ifNull("zeitrasterStart.stundenbeginn ist NULL", zeitrasterStart.stundenbeginn);
		return getZeitrasterByWochentagStartVerstrichen(wochentag, startzeit, minutenVerstrichen);
	}

	/**
	 * Liefert die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Zeit-Intervall berühren.<br>
	 *
	 * @param wochentag          Der {@link Wochentag} des Zeit-Intervalls.
	 * @param beginn             Der Beginn des Zeit-Intervalls.
	 * @param minutenVerstrichen Daraus ergibt sich das Ende des Zeit-Intervalls.
	 *
	 * @return die passende Menge an {@link StundenplanZeitraster}-Objekten, welche das Intervall berührt.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getZeitrasterByWochentagStartVerstrichen(final @NotNull Wochentag wochentag, final int beginn, final int minutenVerstrichen) {
		final int ende = beginn + minutenVerstrichen;
		return CollectionUtils.toFilteredArrayList(_list_zeitraster, (final @NotNull StundenplanZeitraster z) -> (wochentag.id == z.wochentag) &&  testIntervalleSchneidenSich(beginn, ende, z.stundenbeginn, z.stundenende));
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 * @deprecated use {@link #zeitrasterGetByIdOrException(long)}
	 */
	@Deprecated (forRemoval = true)
	public @NotNull StundenplanZeitraster getZeitraster(final long idZeitraster) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_zeitraster, idZeitraster);
	}

	/**
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int getZeitrasterMinutenMin() {
		for (final @NotNull StundenplanZeitraster z :_list_zeitraster)
			if (z.stundenbeginn != null) {
				int min = z.stundenbeginn;
				for (final @NotNull StundenplanZeitraster z2 :_list_zeitraster)
					if ((z2.stundenbeginn != null) && (z2.stundenbeginn < min))
						min = z2.stundenbeginn;
				return min;
			}
		return 480;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int getZeitrasterMinutenMax() {
		for (final @NotNull StundenplanZeitraster z :_list_zeitraster)
			if (z.stundenende != null) {
				int max = z.stundenende;
				for (final @NotNull StundenplanZeitraster z2 :_list_zeitraster)
					if ((z2.stundenende != null) && (z2.stundenende > max))
						max = z2.stundenende;
				return max;
			}
		return 480;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 *
	 * @return den kleinsten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 * @deprecated use {@link #zeitrasterGetWochentagMinEnum()}
	 */
	@Deprecated (forRemoval = true)
	public @NotNull Wochentag getZeitrasterWochentagMin() {
		return zeitrasterGetWochentagMinEnum();
	}

	/**
	 * Liefert den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 *
	 * @return den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 * @deprecated use {@link #zeitrasterGetWochentagMaxEnum()}
	 */
	@Deprecated (forRemoval = true)
	public @NotNull Wochentag getZeitrasterWochentagMax() {
		return zeitrasterGetWochentagMaxEnum();
	}

	/**
	 * Liefert TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 * @deprecated use {@link #zeitrasterExistsByWochentagAndStunde(int, int)}
	 */
	@Deprecated (forRemoval = true)
	public boolean testZeitrasterByWochentagStunde(final @NotNull Wochentag wochentag, final int stunde) {
		return zeitrasterExistsByWochentagAndStunde(wochentag.id, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 * @deprecated use {@link #zeitrasterGetByWochentagAndStundeOrException(int, int)}
	 */
	@Deprecated (forRemoval = true)
	public @NotNull StundenplanZeitraster getZeitrasterByWochentagStunde(final @NotNull Wochentag wochentag, final int stunde) {
		return zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde);
	}

	/**
	 * Liefert das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 *
	 * @param zeitraster Das aktuelle {@link StundenplanZeitraster}-Objekt.
	 *
	 * @return das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 */
	public @NotNull StundenplanZeitraster getZeitrasterNext(final @NotNull StundenplanZeitraster zeitraster) {
		return _map2d_wochentag_stunde_zu_zeitraster.getNonNullOrException(zeitraster.wochentag, zeitraster.unterrichtstunde + 1);
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2 Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2   Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 * @deprecated use {@link #zeitrasterGetSchneidenSich(int, int, Integer, Integer)}
	 */
	@Deprecated (forRemoval = true)
	public boolean testIntervalleSchneidenSich(final int beginn1, final int ende1, final Integer iBeginn2, final Integer iEnde2) {
		return zeitrasterGetSchneidenSich(beginn1, ende1, iBeginn2, iEnde2);
	}

	/**
	 * Fügt dem Stundenplan eine neues {@link StundenplanZeitraster} hinzu.
	 *
	 * @param zeitraster Das StundenplanZeitraster, das hinzugefügt werden soll.
	 * @deprecated use {@link #zeitrasterAdd(StundenplanZeitraster)}
	 */
	@Deprecated (forRemoval = true)
	public void addZeitraster(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterAdd(zeitraster);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des {@link StundenplanZeitraster}-Objekts.
	 * @deprecated use {@link #zeitrasterRemove(long)}
	 */
	@Deprecated (forRemoval = true)
	public void removeZeitraster(final long idZeitraster) {
		zeitrasterRemove(idZeitraster);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanZeitraster}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param zeitraster Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 * @deprecated use {@link #zeitrasterPatch(StundenplanZeitraster)}
	 */
	@Deprecated (forRemoval = true)
	public void patchZeitraster(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterPatch(zeitraster);
	}

	private void pausenaufsichtAddOhneUpdate(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("aufsicht.id", pausenaufsicht.id);
		DeveloperNotificationException.ifMapNotContains("_map_idPausenzeit_zu_pausenzeit", _map_idPausenzeit_zu_pausenzeit, pausenaufsicht.idPausenzeit);
		DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", _map_idLehrer_zu_lehrer, pausenaufsicht.idLehrer);
		DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell)", (pausenaufsicht.wochentyp > 0) && (pausenaufsicht.wochentyp > stundenplanWochenTypModell));

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenaufsichten", _list_pausenaufsichten, pausenaufsicht);
	}

	/**
	 * Fügt ein {@link StundenplanPausenaufsicht}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param pausenaufsicht  Das {@link StundenplanPausenaufsicht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void pausenaufsichtAdd(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtAddOhneUpdate(pausenaufsicht);
		pausenaufsichtUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanPausenaufsicht}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param listPausenaufsicht  Die Menge der {@link StundenplanPausenaufsicht}-Objekte, welche hinzugefügt werden soll.
	 */
	private void pausenaufsichtAddAll(final @NotNull List<@NotNull StundenplanPausenaufsicht> listPausenaufsicht) {
		for (final @NotNull StundenplanPausenaufsicht pausenaufsicht : listPausenaufsicht)
			pausenaufsichtAddOhneUpdate(pausenaufsicht);
		pausenaufsichtUpdate();
	}

	private void pausenaufsichtUpdate() {
		// Überprüfe, ob eine StundenplanPausenaufsicht Dopplungen hat.
		for (final @NotNull StundenplanPausenaufsicht pausenaufsicht : _list_pausenaufsichten) {
			final @NotNull HashSet<@NotNull Long> setBereicheDieserAufsicht = new HashSet<>();
			for (final @NotNull Long idAufsichtsbereich : pausenaufsicht.bereiche) {
				DeveloperNotificationException.ifMapNotContains("_map_aufsichtsbereichID_zu_aufsichtsbereich", _map_idAufsichtsbereich_zu_aufsichtsbereich, idAufsichtsbereich);
				DeveloperNotificationException.ifSetAddsDuplicate("setBereicheDieserAufsicht", setBereicheDieserAufsicht, idAufsichtsbereich);
			}
		}

		// Sortieren
		_list_pausenaufsichten.sort(_compPausenaufsicht);

	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMenge() {
		return _list_pausenaufsichten;
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public void pausenaufsichtRemove(final long idPausenaufsicht) {
		final @NotNull StundenplanPausenaufsicht pa = DeveloperNotificationException.ifNull("_map_pausenaufsichtID_zu_pausenaufsicht.get(" + idPausenaufsicht + ")", _map_pausenaufsichtID_zu_pausenaufsicht.get(idPausenaufsicht));
		_map_pausenaufsichtID_zu_pausenaufsicht.remove(idPausenaufsicht);
		_list_pausenaufsichten.remove(pa); // Neusortierung nicht nötig.
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public @NotNull StundenplanPausenaufsicht pausenaufsichtGet(final long idPausenaufsicht) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenaufsicht}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param pausenaufsicht Das neue {@link StundenplanPausenaufsicht}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void pausenaufsichtPatch(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtRemove(pausenaufsicht.id);
		pausenaufsichtAdd(pausenaufsicht);
	}

	/**
	 * Liefert die Beginn-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "09:30" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID des {@link StundenplanPausenzeit}.
	 *
	 * @return die Beginn-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String pausenzeitGetByIdStringOfUhrzeitBeginn(final long idPausenzeit) {
		final @NotNull StundenplanPausenzeit pausenzeit =  DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, idPausenzeit);
		return (pausenzeit.beginn == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.beginn);
	}

	/**
	 * Liefert die End-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "10:15" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idPausenzeit  Die Datenbank-ID des {@link StundenplanPausenzeit}.
	 *
	 * @return die End-Uhrzeit der {@link StundenplanPausenzeit} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String pausenzeitGetByIdStringOfUhrzeitEnde(final long idPausenzeit) {
		final @NotNull StundenplanPausenzeit pausenzeit =  DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, idPausenzeit);
		return (pausenzeit.ende == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(pausenzeit.ende);
	}

	/**
	 * Liefert die Datenbank-ID des Schülers.<br>
	 * Wirft eine Exception, falls in den Daten nicht genau ein Schüler geladen wurde.
	 *
	 * @return die Datenbank-ID des Schülers.
	 */
	public long schuelerGetIDorException() {
		final int size = _list_schueler.size();
		DeveloperNotificationException.ifTrue("getSchuelerID() geht nicht bei " + size + " Schülern!", size != 1);
		return _list_schueler.get(0).id;
	}

	/**
	 * Liefert das globale WochenTyp-Modell (0: alle Wochen sehen gleich aus, 2 und größer: Die Wochen sehen verschieden aus).
	 * <br>Laufzeit: O(1)
	 *
	 * @return das globale WochenTyp-Modell.
	 */
	public int stundenplanGetWochenTypModell() {
		return stundenplanWochenTypModell;
	}

	/**
	 * Liefert zum übergebenen Wochentyp einen passenden String.
	 * <br>Beispiel: 0 -> "Alle", 1 -> "A-Woche", ...
	 * <br>Laufzeit: O(1)
	 *
	 * @param wochenTyp  Der umzuwandelnde Wochentyp.
	 *
	 * @return zum übergebenen Wochentyp einen passenden String.
	 */
	public @NotNull String stundenplanGetWochenTypAsString(final int wochenTyp) {
		if (wochenTyp <= 0)
			return "Alle";

		final int zahl = wochenTyp - 1; // 0-basierter Wochentyp
		final int z2 = zahl / 26;       // 2. Stelle im 26. System
		final int z1 = zahl - z2 * 26;  // 1. Stelle im 26. System

		return StringUtils.numberToLetterIndex1(z2) + StringUtils.numberToLetterIndex0(z1) + "-Woche";
	}

	/**
	 * Liefert die Datenbank-ID des Stundenplans.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die Datenbank-ID des Stundenplans.
	 */
	public long stundenplanGetID() {
		return stundenplanID;
	}

	private void unterrichtAddOhneUpdate(final @NotNull StundenplanUnterricht u) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("u.id", u.id);
		DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", _map_idZeitraster_zu_zeitraster, u.idZeitraster);
		DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0); // 0 ist erlaubt!

		// Ist es Kurs-Unterricht?
		if (u.idKurs != null) {
			DeveloperNotificationException.ifMapNotContains("_map_kursID_zu_kurs", _map_idKurs_zu_kurs, u.idKurs);
			final @NotNull List<@NotNull StundenplanUnterricht> unterrichtDesKurses = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListAddsDuplicate("unterrichtDesKurses", unterrichtDesKurses, u);
		}

		DeveloperNotificationException.ifMapNotContains("_map_idFach_zu_fach", _map_idFach_zu_fach, u.idFach);
		for (final @NotNull Long idLehrkraftDesUnterrichts : u.lehrer)
			DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", _map_idLehrer_zu_lehrer, idLehrkraftDesUnterrichts);
		for (final @NotNull Long idKlasseDesUnterrichts : u.klassen)
			DeveloperNotificationException.ifMapNotContains("_map_idKlasse_zu_klasse", _map_idKlasse_zu_klasse, idKlasseDesUnterrichts);
		for (final @NotNull Long idRaumDesUnterrichts : u.raeume)
			DeveloperNotificationException.ifMapNotContains("_map_idRaum_zu_raum", _map_idRaum_zu_raum, idRaumDesUnterrichts);
		for (final @NotNull Long idSchieneDesUnterrichts : u.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_idSchiene_zu_schiene", _map_idSchiene_zu_schiene, idSchieneDesUnterrichts);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idUnterricht_zu_unterricht, u.id, u);
		MapUtils.getOrCreateArrayList(_map_idZeitraster_zu_unterrichtmenge, u.idZeitraster).add(u);
		Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, u.idZeitraster, u.wochentyp).add(u);
		for (final @NotNull Long idLehrkraftDesUnterrichts : u.lehrer) {
			final @NotNull StundenplanLehrer lehrer = DeveloperNotificationException.ifMapGetIsNull(_map_idLehrer_zu_lehrer, idLehrkraftDesUnterrichts);
			MapUtils.getOrCreateArrayList(_map_idUnterricht_zu_lehrermenge, u.id).add(lehrer);
		}
		_list_unterricht.add(u);
	}

	/**
	 * Fügt ein {@link StundenplanUnterricht}-Objekt hinzu.
	 * <br>Laufzeit: O(|StundenplanUnterricht|), da unterrichtUpdate() aufgerufen wird.
	 *
	 * @param unterricht  Das {@link StundenplanUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void unterrichtAdd(final @NotNull StundenplanUnterricht unterricht) {
		unterrichtAddOhneUpdate(unterricht);
		unterrichtUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanUnterricht}-Objekte hinzu.
	 * <br>Laufzeit: O(|StundenplanUnterricht|), da unterrichtUpdate() aufgerufen wird.
	 *
	 * @param listUnterricht  Die Menge der {@link StundenplanUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public void unterrichtAddAll(final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {
		for (final @NotNull StundenplanUnterricht unterricht : listUnterricht)
			unterrichtAddOhneUpdate(unterricht);
		unterrichtUpdate();
	}

	/**
	 * Liefert TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 * <br>Laufzeit: O(1)
	 *
	 * @deprecated Sollte nicht benutzt werden. Stattdessen sollte man pro Zeitraster (Zelle) arbeiten.
	 * @return TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 */
	@Deprecated (forRemoval = true)
	public boolean unterrichtHatMultiWochen() {
		return _unterrichtHatMultiWochen;
	}

	/**
	 * Liefert die Anzahl an Wochentypen.
	 * <br>Laufzeit: O(1)
	 *
	 * @deprecated Sollte nicht benutzt werden.  Stattdessen sollte man pro Zeitraster (Zelle) arbeiten.
	 * @return die Anzahl an Wochentypen.
	 */
	@Deprecated (forRemoval = true)
	public int unterrichtGetAnzahlWochentypen() {
		return unterrichtHatMultiWochen() ? stundenplanWochenTypModell : 1;
	}

	/**
	 * Liefert das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 * <br>Laufzeit: O(1)
	 * <br>Hinweis: Unnötige Methode, denn man bekommt die Objekte über Zeitraster-Abfragen.
	 *
	 * @param idUnterricht  Die Datenbank-ID des Unterrichts.
	 *
	 * @return das {@link StundenplanUnterricht}-Objekt zur übergebenen ID.
	 */
	public @NotNull StundenplanUnterricht unterrichtGetByIdOrException(final long idUnterricht) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idUnterricht_zu_unterricht, idUnterricht);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param idkurs     Die ID des Kurses.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdAndWochentyp(final long idkurs, final int wochentyp) {
		// Überprüfen
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > stundenplanWochenTypModell);

		// Filtern
		final @NotNull List<@NotNull StundenplanUnterricht> list = MapUtils.getOrCreateArrayList(_map_idKurs_zu_unterrichtmenge, idkurs);
		return CollectionUtils.toFilteredArrayList(list, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param idsKurs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdsAndWochentyp(final @NotNull long[] idsKurs, final int wochentyp) {
		// Daten filtern.
		final @NotNull ArrayList<@NotNull StundenplanUnterricht> result = new ArrayList<>();
		for (final long idKurs : idsKurs)
			result.addAll(unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp));
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param idKurs        Die ID des Kurses.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdAndJahrAndKW(final long idKurs, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKursIdAndWochentyp(idKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 *
	 * @param idsKurs       Die IDs aller Kurse.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKursIdsAndJahrAndKW(final @NotNull long[] idsKurs, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKursIdsAndWochentyp(idsKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergebenen Zeitraster liegen.
	 * <br>Hinweis: Diese Methode sollte der Client nicht benutzen, denn dann müsste er den Wochentyp parsen.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergebenen Zeitraster liegen.
	 * @deprecated Sollte nicht benutzt werden.  Stattdessen sollte man pro Zeitraster (Zelle) und Wochentyp arbeiten.
	 */
	@Deprecated (forRemoval = true)
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByZeitrasterIdOrEmptyList(final long idZeitraster) {
		return MapUtils.getOrCreateArrayList(_map_idZeitraster_zu_unterrichtmenge, idZeitraster);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(final long idZeitraster, final int wochentyp) {
		return Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, idZeitraster, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekt, die im übergeben Zeitraster und Wochentyp liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(final @NotNull Wochentag wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster zeitraster = _map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);
		return (zeitraster == null) ?  new ArrayList<>() : unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(zeitraster.id, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekten, die im übergebenen Zeitraster und Wochentyp liegen.
	 * Falls der Parameter inklWoche0 TRUE ist, wird Unterricht des Wochentyps 0 hinzugefügt.
	 * <br>Hinweis: Diese Methode sollte der Client nicht benutzen. Der Client sollte sich nach dem Wochentyp des Zeitrasters informieren
	 *              und dann entsprechend {@link #unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(long, int)} aufrufen.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 * @param wochentyp     Der Wochentyp
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, die im übergebenen Zeitraster und Wochentyp liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(final long idZeitraster, final int wochentyp, final boolean inklWoche0) {
		if ((wochentyp == 0) || (!inklWoche0))
			return unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp);

		final @NotNull List<@NotNull StundenplanUnterricht> list = new ArrayList<>();
		list.addAll(unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, wochentyp));
		list.addAll(unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(idZeitraster, 0));
		return list;
	}


	/**
	 * Liefert die Wochentypen ohne Typ 0 zurück, außer es gibt nur Typ 0.
	 * <br>Laufzeit: O(1)
	 *
	 * @deprecated Sollte nicht benutzt werden.  Stattdessen sollte man pro Zeitraster (Zelle) arbeiten.
	 * @return die Wochentypen ohne Typ 0 zurück, außer es gibt nur Typ 0.
	 */
	@Deprecated (forRemoval = true)
	public @NotNull List<@NotNull Integer> unterrichtGetWochentypenMenge() {
		final List<@NotNull Integer> list = new ArrayList<>();

		if (_unterrichtHatMultiWochen) {
			for (int i = 1; i <= stundenplanWochenTypModell; i++)
				list.add(i);
		} else {
			list.add(0);
		}

		return list;
	}

	/**
	 * Liefert eine String-Repräsentation des das Fach- oder Kurs-Kürzel eines {@link StundenplanUnterricht}.
	 * <br>Beispiel: "M-LK1-Suffix" bei Kursen und "M" Fachkürzel bei Klassenunterricht.
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation des das Fach- oder Kurs-Kürzel eines {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDStringOfFachOderKursKuerzel(final long idUnterricht) {
		final @NotNull StundenplanUnterricht unterricht =  DeveloperNotificationException.ifMapGetIsNull(_map_idUnterricht_zu_unterricht, idUnterricht);

		// Klassenunterricht?
		if (unterricht.idKurs == null) {
			final @NotNull StundenplanFach fach =  DeveloperNotificationException.ifMapGetIsNull(_map_idFach_zu_fach, unterricht.idFach);
			return fach.kuerzel;
		}

		// Kursunterricht
		final @NotNull StundenplanKurs kurs =  DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_kurs, unterricht.idKurs);
		return kurs.bezeichnung;
	}

	/**
	 * Liefert eine String-Repräsentation der Klassenmenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "5a" bei einer Klasse und "7a,7b" bei mehreren (z.B. Französisch...)
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Klassenmenge des {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDStringOfKlassen(final long idUnterricht) {
		final @NotNull StundenplanUnterricht unterricht =  DeveloperNotificationException.ifMapGetIsNull(_map_idUnterricht_zu_unterricht, idUnterricht);

		// Klassenkürzel sammeln und sortieren.
		final @NotNull AVLSet<@NotNull String> kuerzel = new AVLSet<>();
		for (final @NotNull Long idKlasse : unterricht.klassen) {
			final @NotNull StundenplanKlasse klasse =  DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klasse, idKlasse);
			kuerzel.add(klasse.kuerzel);
		}

		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 * <br>Beispiel: "1.01" bei einem Raum und "T1, T2" bei mehreren (z.B. Sporthallen...)
	 * <br>Laufzeit: O(1)
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDStringOfRaeume(final long idUnterricht) {
		final @NotNull StundenplanUnterricht unterricht =  DeveloperNotificationException.ifMapGetIsNull(_map_idUnterricht_zu_unterricht, idUnterricht);

		// Klassenkürzel sammeln und sortieren.
		final @NotNull AVLSet<@NotNull String> kuerzel = new AVLSet<>();
		for (final @NotNull Long idRaum : unterricht.raeume) {
			final @NotNull StundenplanRaum raum =  DeveloperNotificationException.ifMapGetIsNull(_map_idRaum_zu_raum, idRaum);
			kuerzel.add(raum.kuerzel);
		}

		return StringUtils.collectionToCommaSeparatedString(kuerzel);
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht}.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht}.
	 */
	public @NotNull List<@NotNull StundenplanLehrer> unterrichtGetByIDLehrerMenge(final long idUnterricht) {
		return MapUtils.getOrCreateArrayList(_map_idUnterricht_zu_lehrermenge, idUnterricht);
	}

	/**
	 * Liefert die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return die Menge aller {@link StundenplanLehrer} des {@link StundenplanUnterricht} als kommaseparierter String.
	 */
	public @NotNull String unterrichtGetByIDLehrerMengeAsString(final long idUnterricht) {
		final @NotNull List<@NotNull StundenplanLehrer> lehrkraefteDesUnterrichts = MapUtils.getOrCreateArrayList(_map_idUnterricht_zu_lehrermenge, idUnterricht);

		final @NotNull List<@NotNull String> listeDerKuerzel = new ArrayList<>();
		for (final @NotNull StundenplanLehrer lehkraft : lehrkraefteDesUnterrichts)
			listeDerKuerzel.add(lehkraft.kuerzel);

		return StringUtils.collectionToCommaSeparatedString(listeDerKuerzel);
	}

	/**
	 * Liefert die erste {@link StundenplanLehrer} des {@link StundenplanUnterricht} oder NULL falls nicht existent.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public StundenplanLehrer unterrichtGetByIDLehrerFirstOrNull(final long idUnterricht) {
		final @NotNull List<@NotNull StundenplanLehrer> lehrerDesUnterrichts = MapUtils.getOrCreateArrayList(_map_idUnterricht_zu_lehrermenge, idUnterricht);
		return lehrerDesUnterrichts.isEmpty() ? null : DeveloperNotificationException.ifListGetFirstFailes("lehrerDesUnterrichts.first", lehrerDesUnterrichts);
	}

	/**
	 * Liefert die erste {@link StundenplanLehrer} des {@link StundenplanUnterricht} oder NULL falls nicht existent.
	 * <br>Laufzeit: O(|Ergebnis|)
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}.
	 *
	 * @return eine String-Repräsentation der Raummenge des {@link StundenplanUnterricht}.
	 */
	public @NotNull String unterrichtGetByIDLehrerFirstAsStringOrEmpty(final long idUnterricht) {
		final StundenplanLehrer lehrkraft = unterrichtGetByIDLehrerFirstOrNull(idUnterricht);
		return lehrkraft == null ? "" : lehrkraft.kuerzel;
	}

	/**
	 * Aktualisiert verschiedene Werte nachdem sich die {@link StundenplanUnterricht}-Menge verändert hat.
	 */
	private void unterrichtUpdate() {
		_unterrichtHatMultiWochen = false;

		for (final @NotNull StundenplanUnterricht u : _list_unterricht)
			if (u.wochentyp > 0)
				_unterrichtHatMultiWochen = true;
	}

	private void zeitrasterAddOhneUpdate(final @NotNull StundenplanZeitraster zeitraster) {
		// Prüfen.
		DeveloperNotificationException.ifInvalidID("zeit.id", zeitraster.id);
		Wochentag.fromIDorException(zeitraster.wochentag);
		DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeitraster.unterrichtstunde < 0) || (zeitraster.unterrichtstunde > 29));
		if ((zeitraster.stundenbeginn != null) && (zeitraster.stundenende != null)) {
			final int beginn = zeitraster.stundenbeginn;
			final int ende = zeitraster.stundenende;
			DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
		}
		// Hinzufügen.
		DeveloperNotificationException.ifMapPutOverwrites(_map_idZeitraster_zu_zeitraster, zeitraster.id, zeitraster);
		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_wochentag_stunde_zu_zeitraster, zeitraster.wochentag, zeitraster.unterrichtstunde, zeitraster);
		MapUtils.getOrCreateArrayList(_map_wochentag_zu_zeitrastermenge, zeitraster.wochentag).add(zeitraster);
		MapUtils.getOrCreateArrayList(_map_stunde_zu_zeitrastermenge, zeitraster.unterrichtstunde).add(zeitraster);
		_list_zeitraster.add(zeitraster);
	}

	/**
	 * Fügt ein {@link StundenplanZeitraster}-Objekt hinzu.
	 * <br>Laufzeit: O(|Zeitraster| * log ), da zeitrasterUpdate() aufgerufen wird.
	 *
	 * @param zeitraster  Das {@link StundenplanZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public void zeitrasterAdd(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterAddOhneUpdate(zeitraster);
		zeitrasterUpdate();
	}

	/**
	 * Fügt alle {@link StundenplanZeitraster}-Objekte hinzu.
	 * <br>Laufzeit: O(|Zeitraster| * log ), da zeitrasterUpdate() aufgerufen wird.
	 *
	 * @param listZeitraster  Die Menge der {@link StundenplanZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public void zeitrasterAddAll(final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster) {
		for (final @NotNull StundenplanZeitraster zeitraster : listZeitraster)
			zeitrasterAddOhneUpdate(zeitraster);
		zeitrasterUpdate();
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMin() {
		return _zeitrasterStundeMin;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMax() {
		return _zeitrasterStundeMax;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetWochentagMin() {
		return _zeitrasterWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public @NotNull Wochentag zeitrasterGetWochentagMinEnum() {
		return Wochentag.fromIDorException(_zeitrasterWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetWochentagMax() {
		return _zeitrasterWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public @NotNull Wochentag zeitrasterGetWochentagMaxEnum() {
		return Wochentag.fromIDorException(_zeitrasterWochentagMax);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public @NotNull StundenplanZeitraster zeitrasterGetByIdOrException(final long idZeitraster) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_zeitraster, idZeitraster);
	}

	/**
	 * Liefert die Beginn-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "09:30" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}.
	 *
	 * @return die Beginn-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String zeitrasterGetByIdStringOfUhrzeitBeginn(final long idZeitraster) {
		final @NotNull StundenplanZeitraster zeitraster =  DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_zeitraster, idZeitraster);
		return (zeitraster.stundenbeginn == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
	}

	/**
	 * Liefert die End-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 * <br>Beispiel: "10:15" oder ""
	 * <br>Laufzeit: O(1)
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}.
	 *
	 * @return die End-Uhrzeit des {@link StundenplanZeitraster} oder den leeren String, falls diese NULL ist.
	 */
	public @NotNull String zeitrasterGetByIdStringOfUhrzeitEnde(final long idZeitraster) {
		final @NotNull StundenplanZeitraster zeitraster =  DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_zeitraster, idZeitraster);
		return (zeitraster.stundenende == null) ? "" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 */
	public @NotNull StundenplanZeitraster zeitrasterGetByWochentagAndStundeOrException(final int wochentag, final int stunde) {
		return _map2d_wochentag_stunde_zu_zeitraster.getNonNullOrException(wochentag, stunde);
	}

	/**
	 * Liefert die Liste aller {@link StundenplanZeitraster}-Objekte des Wochentags.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag}.
	 *
	 * @deprecated Sollte nicht benutzt werden, stattdessen {@link #zeitrasterGetByWochentagAndStundeOrException(int, int)}
	 * @return die Liste aller {@link StundenplanZeitraster}-Objekte des Wochentags.
	 */
	@Deprecated (forRemoval = true)
	public @NotNull List<@NotNull StundenplanZeitraster> zeitrasterGetMengeByWochentag(final int wochentag) {
		return MapUtils.getOrCreateArrayList(_map_wochentag_zu_zeitrastermenge, wochentag);
	}

	/**
	 * Liefert die Liste aller {@link StundenplanZeitraster}-Objekte der Unterrichtsstunde.
	 *
	 * @param stunde  Die Unterrichtsstunde.
	 *
	 * @deprecated Sollte nicht benutzt werden, stattdessen {@link #zeitrasterGetByWochentagAndStundeOrException(int, int)}
	 * @return die Liste aller {@link StundenplanZeitraster}-Objekte der Unterrichtsstunde.
	 */
	@Deprecated (forRemoval = true)
	public @NotNull List<@NotNull StundenplanZeitraster> zeitrasterGetMengeByStunde(final int stunde) {
		return MapUtils.getOrCreateArrayList(_map_stunde_zu_zeitrastermenge, stunde);
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 *
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2 Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2   Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 */
	public boolean zeitrasterGetSchneidenSich(final int beginn1, final int ende1, final Integer iBeginn2, final Integer iEnde2) {
		final int beginn2 = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn ist NULL!", iBeginn2);
		final int ende2 = DeveloperNotificationException.ifNull("zeitraster.stundenende ist NULL!", iEnde2);
		DeveloperNotificationException.ifTrue("beginn1 < 0", beginn1 < 0);
		DeveloperNotificationException.ifTrue("beginn2 < 0", beginn2 < 0);
		DeveloperNotificationException.ifTrue("beginn1 > ende1", beginn1 > ende1);
		DeveloperNotificationException.ifTrue("beginn2 > ende2", beginn2 > ende2);
		return !((ende1 < beginn2) || (ende2 < beginn1));
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMin()} bis {@link #zeitrasterGetStundeMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der {@link StundenplanZeitraster}.
	 */
	public @NotNull int @NotNull [] zeitrasterGetStundenRange() {
		return _zeitrasterStundenRange;
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #zeitrasterGetWochentagMin} bis {@link #zeitrasterGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 */
	public @NotNull Wochentag @NotNull [] zeitrasterGetWochentageAlsEnumRange() {
		return _zeitrasterWochentageAlsEnumRange;
	}

	/**
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0(final long idZeitraster) {
		return !Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, idZeitraster, 0).isEmpty();
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.

	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 0 gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(final @NotNull Wochentag wochentag, final int stunde) {
		final StundenplanZeitraster zeitraster = _map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);
		return (zeitraster != null) && zeitrasterHatUnterrichtMitWochentyp0(zeitraster.id);
	}

	/**
	 * Liefert TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des Zeitrasters.
	 *
	 * @return TRUE, falls es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisN(final long idZeitraster) {
		for (int wochentyp = 1; wochentyp <= stundenplanWochenTypModell; wochentyp++)
			if (!Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, idZeitraster, wochentyp).isEmpty())
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.

	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 *
	 * @return TRUE, falls das Zeitraster existiert und es mindestens einen Unterricht im Zeitraster mit einem einen Wochentyp 1 bis N gibt.
	 */
	public boolean zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(final @NotNull Wochentag wochentag, final int stunde) {
		final StundenplanZeitraster zeitraster = _map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);
		return (zeitraster != null) && zeitrasterHatUnterrichtMitWochentyp1BisN(zeitraster.id);
	}

	/**
	 * Liefert TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 *
	 * @param wochentag  Der ENUM-ID des {@link Wochentag} des Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des Zeitrasters.
	 *
	 * @return TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 */
	public boolean zeitrasterExistsByWochentagAndStunde(final int wochentag, final int stunde) {
		return _map2d_wochentag_stunde_zu_zeitraster.contains(wochentag, stunde);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanZeitraster}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void zeitrasterPatch(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterRemoveOhneUpdate(zeitraster.id);
		zeitrasterAddOhneUpdate(zeitraster);
		zeitrasterUpdate();
	}

	private void zeitrasterRemoveOhneUpdate(final long idZeitraster) {
		// Get
		final @NotNull StundenplanZeitraster z = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + idZeitraster + ")", _map_idZeitraster_zu_zeitraster.get(idZeitraster));

		// Remove
		DeveloperNotificationException.ifMapRemoveFailes(_map_idZeitraster_zu_zeitraster, idZeitraster);
		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_wochentag_stunde_zu_zeitraster, z.wochentag, z.unterrichtstunde);
		MapUtils.removeFromListAndTrimOrException(_map_wochentag_zu_zeitrastermenge, z.wochentag, z);
		MapUtils.removeFromListAndTrimOrException(_map_stunde_zu_zeitrastermenge, z.unterrichtstunde, z);
		DeveloperNotificationException.ifListRemoveFailes("_list_zeitraster", _list_zeitraster, z);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public void zeitrasterRemove(final long idZeitraster) {
		zeitrasterRemoveOhneUpdate(idZeitraster);
		zeitrasterUpdate();
	}


	/**
	 * Aktualisiert verschiedene Werte nachdem sich die Menge der Zeitraster verändert hat.
	 * <br>Laufzeit: O(|Zeitraster| * log)
	 */
	private void zeitrasterUpdate() {

		if (_list_zeitraster.isEmpty()) {
			_zeitrasterWochentagMin = Wochentag.MONTAG.id;
			_zeitrasterWochentagMax = Wochentag.MONTAG.id;
			_zeitrasterStundeMin = 1;
			_zeitrasterStundeMax = 1;
			_zeitrasterStundenRange = new int[] {1};
			_zeitrasterWochentageAlsEnumRange = new Wochentag[] {Wochentag.MONTAG};
		} else {
			_list_zeitraster.sort(_compZeitraster);
			final @NotNull StundenplanZeitraster first = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", _list_zeitraster);
			int minWT = first.wochentag;
			int maxWT = first.wochentag;
			int minSt = first.unterrichtstunde;
			int maxSt = first.unterrichtstunde;
			_unterrichtHatMultiWochen = false;
			for (final @NotNull StundenplanZeitraster z :_list_zeitraster) {
				minWT = Math.min(minWT, z.wochentag);
				maxWT = Math.max(maxWT, z.wochentag);
				minSt = Math.min(minSt, z.unterrichtstunde);
				maxSt = Math.max(maxSt, z.unterrichtstunde);
			}
			_zeitrasterWochentagMin =  minWT;
			_zeitrasterWochentagMax =  maxWT;
			_zeitrasterStundeMin = minSt;
			_zeitrasterStundeMax = maxSt;

			_zeitrasterStundenRange = new int[maxSt - minSt + 1];
			for (int i = 0; i < _zeitrasterStundenRange.length; i++)
				_zeitrasterStundenRange[i] = minSt + i;

			_zeitrasterWochentageAlsEnumRange = new Wochentag[maxWT - minWT + 1];
			for (int i = 0; i < _zeitrasterWochentageAlsEnumRange.length; i++)
				_zeitrasterWochentageAlsEnumRange[i] = Wochentag.fromIDorException(minWT + i);
		}
	}

	/**
	 * Liefert eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 * <br>Beispiel:  "08:00-8:45", falls sie nicht abweichen.
	 * <br>Beispiel:  "Mo.-Mi. 08:00-8:45", "Do. 07:55-8:40", "Fr. 07:40-8:25", falls sie abweichen.
	 *
	 * @param stunde  Die Nr. der Unterrichtsstunde.
	 *
	 * @return eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 */
	public @NotNull List<@NotNull String> unterrichtsstundeGetUhrzeitenAsStrings(final int stunde) {
		final @NotNull List<@NotNull String> listUhrzeit = new ArrayList<>();
		final @NotNull List<@NotNull String> listWochentagVon = new ArrayList<>();
		final @NotNull List<@NotNull String> listWochentagBis = new ArrayList<>();

		for (int wochentag = _zeitrasterWochentagMin; wochentag <= _zeitrasterWochentagMax; wochentag++) {
			final @NotNull String sUhrzeit = unterrichtsstundeGetUhrzeitAsString(wochentag, stunde);
			final @NotNull String sWochentag = Wochentag.fromIDorException(wochentag).kuerzel;

			if (listUhrzeit.isEmpty()) {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
				continue;
			}

			final @NotNull String sUhrzeitDavor = DeveloperNotificationException.ifListGetLastFailes("listUhrzeit", listUhrzeit);

			if (sUhrzeitDavor.equals(sUhrzeit)) {
				listWochentagBis.set(listWochentagBis.size() - 1, sWochentag);
			} else {
				listUhrzeit.add(sUhrzeit);
				listWochentagVon.add(sWochentag);
				listWochentagBis.add(sWochentag);
			}

		}

		// Fall: Alle Zeiten sind identisch.
		if (listUhrzeit.size() <= 1)
			return listUhrzeit;

		// Fall: Unterschiedliche Zeiten benötigen als Prefix den Wochentag.
		for (int i = 0; i < listUhrzeit.size(); i++) {
			final @NotNull String sUhrzeit = listUhrzeit.get(i);
			final @NotNull String sWochentagVon = listWochentagVon.get(i);
			final @NotNull String sWochentagBis = listWochentagBis.get(i);
			if (sWochentagVon.equals(sWochentagBis))
				listUhrzeit.set(i, sWochentagVon + ". " + sUhrzeit);
			else
				listUhrzeit.set(i, sWochentagVon + ".-" + sWochentagBis + ". " + sUhrzeit);
		}

		return listUhrzeit;
	}

	private @NotNull String unterrichtsstundeGetUhrzeitAsString(final int wochentag, final int stunde) {
		final StundenplanZeitraster zeitraster = _map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag, stunde);

		if (zeitraster == null)
			return "???";

		final @NotNull String sBeginn = (zeitraster.stundenbeginn == null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenbeginn);
		final @NotNull String sEnde = (zeitraster.stundenende == null) ? "??:??" : DateUtils.getStringOfUhrzeitFromMinuten(zeitraster.stundenende);

		return sBeginn + " - " + sEnde + " Uhr";
	}

}
