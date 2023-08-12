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
import de.svws_nrw.core.utils.BlockungsUtils;
import de.svws_nrw.core.utils.CollectionUtils;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.ListUtils;
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

	// Static
	private static final int MINUTEN_INF_POS = 24 * 60 + 1;
	private static final int MINUTEN_INF_NEG = -1;
	private static final int WOCHENTAG_INF_POS = Wochentag.SONNTAG.id + 1;
	private static final int WOCHENTAG_INF_NEG  = Wochentag.MONTAG.id - 1;
	private static final int STUNDE_INF_POS = -1;
	private static final int STUNDE_INF_NEG = -1;

	// StundenplanAufsichtsbereich
	private static final @NotNull Comparator<@NotNull StundenplanAufsichtsbereich> _compAufsichtsbereich = (final @NotNull StundenplanAufsichtsbereich a, final @NotNull StundenplanAufsichtsbereich b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanAufsichtsbereich> _list_aufsichtsbereiche = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanAufsichtsbereich> _map_idAufsichtsbereich_zu_aufsichtsbereich = new HashMap<>();

	// StundenplanFach
	private static final @NotNull Comparator<@NotNull StundenplanFach> _compFach = (final @NotNull StundenplanFach a, final @NotNull StundenplanFach b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanFach> _list_faecher = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanFach> _map_idFach_zu_fach = new HashMap<>();

	// StundenplanJahrgang
	private final @NotNull List<@NotNull StundenplanJahrgang> _list_jahrgaenge = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanJahrgang> _map_idJahrgang_zu_jahrgang = new HashMap<>();

	// StundenplanKalenderwochenzuordnung
	private static final @NotNull Comparator<@NotNull StundenplanKalenderwochenzuordnung> _compKWZ = (final @NotNull StundenplanKalenderwochenzuordnung a, final @NotNull StundenplanKalenderwochenzuordnung b) -> {
		if (a.jahr < b.jahr) return -1;
		if (a.jahr > b.jahr) return +1;
		if (a.kw < b.kw) return -1;
		if (a.kw > b.kw) return +1;
		if (a.wochentyp < b.wochentyp) return -1;
		if (a.wochentyp > b.wochentyp) return +1;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> _list_kwz = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKalenderwochenzuordnung> _map_idKWZ_zu_kwz = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanKalenderwochenzuordnung> _map2d_jahr_kw_zu_kwz = new HashMap2D<>();

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
	private static final @NotNull Comparator<@NotNull StundenplanPausenzeit> _compPausenzeit = (final @NotNull StundenplanPausenzeit a, final @NotNull StundenplanPausenzeit b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;
		final int beginnA = a.beginn == null ? -1 : a.beginn;
		final int beginnB = b.beginn == null ? -1 : b.beginn;
		if (beginnA < beginnB) return -1;
		if (beginnA > beginnB) return +1;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanPausenzeit> _list_pausenzeiten = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenzeit> _map_idPausenzeit_zu_pausenzeit = new HashMap<>();
	private int _pausenzeitMinutenMin = 480;
	private int _pausenzeitMinutenMax = 480;
	private int _pausenzeitUndZeitrasterMinutenMin = 480;
	private int _pausenzeitUndZeitrasterMinutenMax = 480;
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanPausenzeit>> _pausenzeitMapByWochentag = new HashMap<>();

	// StundenplanRaum
	private static final @NotNull Comparator<@NotNull StundenplanRaum> _compRaum = (final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanRaum> _list_raeume = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanRaum> _map_idRaum_zu_raum = new HashMap<>();

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
	private boolean _unterrichtHatMultiWochen = false;

	// StundenplanZeitraster
	private static final @NotNull Comparator<@NotNull StundenplanZeitraster> _compZeitraster = (final @NotNull StundenplanZeitraster a, final @NotNull StundenplanZeitraster b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;
		if (a.unterrichtstunde < b.unterrichtstunde) return -1;
		if (a.unterrichtstunde > b.unterrichtstunde) return +1;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanZeitraster> _list_zeitraster = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanZeitraster> _map_idZeitraster_zu_zeitraster = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanZeitraster> _map2d_wochentag_stunde_zu_zeitraster = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _map_wochentag_zu_zeitrastermenge = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _map_stunde_zu_zeitrastermenge = new HashMap<>();
	private @NotNull int @NotNull [] _zeitrasterStundenRange = new int[] {1};
	private final @NotNull HashMap<@NotNull Integer, Integer> _zeitrasterMinutenMinByStunde = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, Integer> _zeitrasterMinutenMaxByStunde = new HashMap<>();
	private @NotNull Wochentag @NotNull [] _zeitrasterWochentageAlsEnumRange = new Wochentag[] {Wochentag.MONTAG};
	private int _zeitrasterWochentagMin = Wochentag.MONTAG.id;
	private int _zeitrasterWochentagMax = Wochentag.MONTAG.id;
	private int _zeitrasterStundeMin = 1;
	private int _zeitrasterStundeMax = 1;
	private int _zeitrasterMinutenMin = 480;
	private int _zeitrasterMinutenMax = 480;

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
		kalenderwochenzuordnungErzeugePseudoMenge();   // ✔, referenziert Kalenderwochenzuordnung (wg. Default-Wochentyp-Bestimmung)
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
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param aufsichtsbereich  Das neue {@link StundenplanAufsichtsbereich}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void aufsichtsbereichPatch(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		aufsichtsbereichRemoveOhneUpdateById(aufsichtsbereich.id);
		aufsichtsbereichAddOhneUpdate(aufsichtsbereich);
		aufsichtsbereichUpdate();
	}

	private void aufsichtsbereichRemoveOhneUpdateById(final long idAufsichtsbereich) {
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
	public void aufsichtsbereichRemoveById(final long idAufsichtsbereich) {
		aufsichtsbereichRemoveOhneUpdateById(idAufsichtsbereich);
		aufsichtsbereichUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @param listAufsichtsbereich  Die Liste der zu entfernenden {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public void aufsichtsbereichRemoveAll(final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich) {
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : listAufsichtsbereich)
			aufsichtsbereichRemoveOhneUpdateById(aufsichtsbereich.id);
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
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param fach  Das neue {@link StundenplanFach}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void fachPatch(final @NotNull StundenplanFach fach) {
		fachRemoveOhneUpdateById(fach.id);
		fachAddOhneUpdate(fach);
		fachUpdate();
	}

	private void fachRemoveOhneUpdateById(final long idFach) {
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
	public void fachRemoveById(final long idFach) {
		fachRemoveOhneUpdateById(idFach);
		fachUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanFach}-Objekte.
	 *
	 * @param listFach  Die Liste der zu entfernenden {@link StundenplanFach}-Objekte.
	 */
	public void fachRemoveAll(final @NotNull List<@NotNull StundenplanFach> listFach) {
		for (final @NotNull StundenplanFach fach : listFach)
			fachRemoveOhneUpdateById(fach.id);
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
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param jahrgang  Das neue {@link StundenplanJahrgang}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void jahrgangPatch(final @NotNull StundenplanJahrgang jahrgang) {
		jahrgangRemoveOhneUpdateById(jahrgang.id);
		jahrgangAddOhneUpdate(jahrgang);
		jahrgangUpdate();
	}

	private void jahrgangRemoveOhneUpdateById(final long idJahrgang) {
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
	public void jahrgangRemoveById(final long idJahrgang) {
		jahrgangRemoveOhneUpdateById(idJahrgang);
		jahrgangUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanJahrgang}-Objekte.
	 *
	 * @param listJahrgang  Die Liste der zu entfernenden {@link StundenplanJahrgang}-Objekte.
	 */
	public void jahrgangRemoveAll(final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang) {
		for (final @NotNull StundenplanJahrgang jahrgang : listJahrgang)
			jahrgangRemoveOhneUpdateById(jahrgang.id);
		jahrgangUpdate();
	}

	private void jahrgangUpdate() {
		// Überprüfe, ob doppelte StundenplanJahrgang-Kürzel vorhanden sind.
		final @NotNull HashSet<@NotNull String> setJahrgangKuerzel = new HashSet<>();
		for (final @NotNull StundenplanJahrgang jahrgang : _list_jahrgaenge)
			DeveloperNotificationException.ifSetAddsDuplicate("setJahrgangKuerzel", setJahrgangKuerzel, jahrgang.kuerzel);
	}

	private void kalenderwochenzuordnungErzeugePseudoMenge() {
		final @NotNull int[] infoVon = DateUtils.extractFromDateISO8601(stundenplanGueltigAb);
		final @NotNull int[] infoBis = DateUtils.extractFromDateISO8601(stundenplanGueltigBis);
		final int jahrVon = infoVon[6]; // 6 = kalenderwochenjahr
		final int jahrBis = infoBis[6]; // 6 = kalenderwochenjahr
		final int kwVon = infoVon[5]; // 5 = kalenderwoche
		final int kwBis = infoBis[5]; // 5 = kalenderwoche
		DeveloperNotificationException.ifTrue("jahrVon > jahrBis", jahrVon > jahrBis);
		DeveloperNotificationException.ifTrue("(jahrVon == jahrBis) && (kwVon > kwBis)", (jahrVon == jahrBis) && (kwVon > kwBis));

		final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listNeueKWZ = new ArrayList<>();

		for (int jahr = jahrVon; jahr <= jahrBis; jahr++) {
			final int von = (jahr == jahrVon) ? kwVon : 1;
			final int bis = (jahr == jahrBis) ? kwBis : DateUtils.gibKalenderwochenOfJahr(jahr);
			for (int kw = von; kw <= bis; kw++)
				if (!_map2d_jahr_kw_zu_kwz.contains(jahr, kw)) { // Überschreibe Objekte der DB nicht!
					final @NotNull StundenplanKalenderwochenzuordnung kwz = new StundenplanKalenderwochenzuordnung();
					kwz.id = -1;
					kwz.jahr = jahr;
					kwz.kw = kw;
					kwz.wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kw);
					listNeueKWZ.add(kwz);
				}
		}

		kalenderwochenzuordnungAddAll(listNeueKWZ);
	}

	private void kalenderwochenzuordnungAddOhneUpdate(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		// Überprüfen
		DeveloperNotificationException.ifTrue("kwz.id < -1", kwz.id < -1);
		DeveloperNotificationException.ifTrue("(kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR)", (kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR));
		DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr))", (kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr)));
		DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("kwz.wochentyp < 0", kwz.wochentyp < 0); // // kwz.wochentyp darf 0 sein, wegen der Pseudomenge!

		// Hinzufügen
		if (kwz.id != -1)
			DeveloperNotificationException.ifMapPutOverwrites(_map_idKWZ_zu_kwz, kwz.id, kwz);
		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, kwz);
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
	 * Liefert das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche.
	 *
	 * @return das dem Jahr und der Kalenderwoche zugeordnete {@link StundenplanKalenderwochenzuordnung}-Objekt der Auswahl-Menge.
	 */
	public @NotNull StundenplanKalenderwochenzuordnung kalenderwochenzuordnungGetByJahrAndKWOrException(final int jahr, final int kalenderwoche) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_map2d_jahr_kw_zu_kwz, jahr, kalenderwoche);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 * <br>Hinweis: Einige Objekte dieser Menge können die ID = -1 haben, falls sie erzeugt wurden und nicht aus der DB stammen.
	 *
	 * @return eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> kalenderwochenzuordnungGetMengeAsList() {
		return _list_kwz;
	}

	/**
	 * Liefert eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 * <br>Beispiel: Jahr 2023, KW  5 --> "30.01.2023 - 05.02.2023 (KW 2023.5)"
	 * <br>Beispiel: Jahr 2025, KW  1 --> "30.12.2024 - 05.01.2025 (KW 2025.1)"
	 * <br>Beispiel: Jahr 2026, KW 53 --> "28.12.2026 - 03.01.2027 (KW 2026.53)"
	 * <br>Laufzeit: O(1)

	 * @param kwz  Das {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @return eine String-Darstellung der Kalenderwoche des {@link StundenplanKalenderwochenzuordnung}-Objekts.
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
	 * Liefert den zugeordneten Wochentyp, oder den Default-Wochentyp, welcher sich aus der Kalenderwoche berechnet.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return den zugeordneten Wochentyp, oder den Default-Wochentyp, welcher sich aus der Kalenderwoche berechnet.
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
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
	 * <br>Hinweis: Das Mapping muss existieren UND {@link #stundenplanWochenTypModell} muss mindestens 2 sein.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche. Es muss zwischen {@link DateUtils#MIN_GUELTIGES_JAHR} und {@link DateUtils#MAX_GUELTIGES_JAHR} liegen.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche. Es muss zwischen 1 und {@link DateUtils#gibKalenderwochenOfJahr(int)} liegen.
	 *
	 * @return TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
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
	 * Entfernt anhand das alte {@link StundenplanKalenderwochenzuordnung}-Objekt anhand der ID und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Ein patchen der ID ist nicht erlaubt. Hierfür muss die
	 * {@link #kalenderwochenzuordnungReplace(StundenplanKalenderwochenzuordnung, StundenplanKalenderwochenzuordnung)}-Methode verwendet werden.
	 *
	 * @param kwz Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void kalenderwochenzuordnungPatch(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungRemoveOhneUpdateById(kwz.id);
		kalenderwochenzuordnungAddOhneUpdate(kwz);
		kalenderwochenzuordnungUpdate();
	}

	private void kalenderwochenzuordnungRemoveOhneUpdateById(final long idKWZ) {
		// Holen
		final @NotNull StundenplanKalenderwochenzuordnung k = DeveloperNotificationException.ifMapGetIsNull(_map_idKWZ_zu_kwz, idKWZ);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKWZ_zu_kwz, k.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_jahr_kw_zu_kwz, k.jahr, k.kw);
		DeveloperNotificationException.ifListRemoveFailes("_list_kwz", _list_kwz, k);
	}

	private void kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(final int jahr, final int kalenderwoche) {
		// Holen
		final @NotNull StundenplanKalenderwochenzuordnung k = DeveloperNotificationException.ifMap2DGetIsNull(_map2d_jahr_kw_zu_kwz, jahr, kalenderwoche);

		// Entfernen
		if (k.id != -1)
			DeveloperNotificationException.ifMapRemoveFailes(_map_idKWZ_zu_kwz, k.id);
		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_jahr_kw_zu_kwz, k.jahr, k.kw);
		DeveloperNotificationException.ifListRemoveFailes("_list_kwz", _list_kwz, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand seiner Datenbank-ID.
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung|), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param idKWZ  Die Datenbank-ID des {@link StundenplanKalenderwochenzuordnung}-Objekts, welches entfernt werden soll.
	 */
	public void kalenderwochenzuordnungRemoveById(final long idKWZ) {
		kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ);
		kalenderwochenzuordnungUpdate();
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand der Parameter (jahr, kalenderwoche).
	 * <br>Laufzeit: O(|StundenplanKalenderwochenzuordnung|), da kalenderwochenzuordnungUpdate() aufgerufen wird.
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche.
	 */
	public void kalenderwochenzuordnungRemoveByJahrAndKW(final int jahr, final int kalenderwoche) {
		kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(jahr, kalenderwoche);
		kalenderwochenzuordnungUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @param listKWZ  Die Liste der zu entfernenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public void kalenderwochenzuordnungRemoveAll(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ) {
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : listKWZ)
			kalenderwochenzuordnungRemoveOhneUpdateById(kwz.id);
		kalenderwochenzuordnungUpdate();
	}

	/**
	 * Ersetzt das alte {@link StundenplanKalenderwochenzuordnung}-Objekt durch das neue Objekt.
	 *
	 * @param kwzAlt  Das alte {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 * @param kwzNeu  Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void kalenderwochenzuordnungReplace(final @NotNull StundenplanKalenderwochenzuordnung kwzAlt, final @NotNull StundenplanKalenderwochenzuordnung kwzNeu) {
		kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(kwzAlt.jahr, kwzAlt.kw);
		kalenderwochenzuordnungAddOhneUpdate(kwzNeu);
		kalenderwochenzuordnungUpdate();
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
		DeveloperNotificationException.ifListAddsDuplicate("_list_klassen", _list_klassen, klasse);
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
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param klasse  Das neue {@link StundenplanKlasse}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void klassePatch(final @NotNull StundenplanKlasse klasse) {
		klasseRemoveOhneUpdateById(klasse.id);
		klasseAddOhneUpdate(klasse);
		klasseUpdate();
	}

	private void klasseRemoveOhneUpdateById(final long idKlasse) {
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
	public void klasseRemoveById(final long idKlasse) {
		klasseRemoveOhneUpdateById(idKlasse);
		klasseUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanKlasse}-Objekte.
	 *
	 * @param listKlasse  Die Liste der zu entfernenden {@link StundenplanKlasse}-Objekte.
	 */
	public void klasseRemoveAll(final @NotNull List<@NotNull StundenplanKlasse> listKlasse) {
		for (final @NotNull StundenplanKlasse klasse : listKlasse)
			klasseRemoveOhneUpdateById(klasse.id);
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
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param kurs  Das neue {@link StundenplanKurs}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void kursPatch(final @NotNull StundenplanKurs kurs) {
		kursRemoveOhneUpdateById(kurs.id);
		kursAddOhneUpdate(kurs);
		kursUpdate();
	}

	private void kursRemoveOhneUpdateById(final long idKurs) {
		// Get
		final @NotNull StundenplanKurs kurs = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_kurs, idKurs);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_zu_kurs, kurs.id);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_zu_unterrichtmenge, kurs.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_kurse", _list_kurse, kurs);
	}

	/**
	 * Entfernt ein {@link StundenplanKurs}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanKurs|), da kursUpdate() aufgerufen wird.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts, welches entfernt werden soll.
	 */
	public void kursRemoveById(final long idKurs) {
		kursRemoveOhneUpdateById(idKurs);
		kursUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanKurs}-Objekte.
	 *
	 * @param listKurs  Die Liste der zu entfernenden {@link StundenplanKurs}-Objekte.
	 */
	public void kursRemoveAll(final @NotNull List<@NotNull StundenplanKurs> listKurs) {
		for (final @NotNull StundenplanKurs kurs : listKurs)
			kursRemoveOhneUpdateById(kurs.id);
		kursUpdate();
	}

	private void kursUpdate() {
		// ...
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

	/**
	 * Liefert das {@link StundenplanLehrer}-Objekt mit der übergebenen ID.
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts.
	 *
	 * @return das {@link StundenplanLehrer}-Objekt mit der übergebenen ID.
	 */
	public @NotNull StundenplanLehrer lehrerGetByIdOrException(final long idLehrer) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idLehrer_zu_lehrer, idLehrer);
	}


	/**
	 * Liefert eine Liste aller {@link StundenplanLehrer}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanLehrer}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanLehrer> lehrerGetMengeAsList() {
		return _list_lehrer;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanLehrer}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param lehrer  Das neue {@link StundenplanLehrer}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void lehrerPatch(final @NotNull StundenplanLehrer lehrer) {
		lehrerRemoveOhneUpdateById(lehrer.id);
		lehrerAddOhneUpdate(lehrer);
		lehrerUpdate();
	}

	private void lehrerRemoveOhneUpdateById(final long idLehrer) {
		// Get
		final @NotNull StundenplanLehrer lehrer = DeveloperNotificationException.ifMapGetIsNull(_map_idLehrer_zu_lehrer, idLehrer);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idLehrer_zu_lehrer, lehrer.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_lehrer", _list_lehrer, lehrer);
	}

	/**
	 * Entfernt ein {@link StundenplanLehrer}-Objekt anhand seiner ID.
	 * <br>Laufzeit: O(|StundenplanLehrer|), da lehrerUpdate() aufgerufen wird.
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts, welches entfernt werden soll.
	 */
	public void lehrerRemoveById(final long idLehrer) {
		lehrerRemoveOhneUpdateById(idLehrer);
		lehrerUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanLehrer}-Objekte.
	 *
	 * @param listLehrer  Die Liste der zu entfernenden {@link StundenplanLehrer}-Objekte.
	 */
	public void lehrerRemoveAll(final @NotNull List<@NotNull StundenplanLehrer> listLehrer) {
		for (final @NotNull StundenplanLehrer lehrer : listLehrer)
			lehrerRemoveOhneUpdateById(lehrer.id);
		lehrerUpdate();
	}

	private void lehrerUpdate() {
		final @NotNull HashSet<@NotNull String> setLehrerKuerzel = new HashSet<>();
		for (final @NotNull StundenplanLehrer lehrer : _list_lehrer) {
			// Überprüfe, ob ein Lehrer-Kürzel doppelt vorkommt.
			DeveloperNotificationException.ifSetAddsDuplicate("setLehrerKuerzel", setLehrerKuerzel, lehrer.kuerzel);

			// Überprüfe, ob die Lehrkraft Fächer doppelt hat.
			final @NotNull HashSet<@NotNull Long> setFaecherDerLehrkraft = new HashSet<>();
			for (final @NotNull Long idFachDerLehrkraft : lehrer.faecher) {
				DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", _map_idFach_zu_fach, idFachDerLehrkraft);
				DeveloperNotificationException.ifSetAddsDuplicate("setFaecherDerLehrkraft", setFaecherDerLehrkraft, idFachDerLehrkraft);
			}
		}
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

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public @NotNull StundenplanPausenaufsicht pausenaufsichtGetByIdOrException(final long idPausenaufsicht) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeAsList() {
		return _list_pausenaufsichten;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenaufsicht}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param pausenaufsicht Das neue {@link StundenplanPausenaufsicht}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void pausenaufsichtPatch(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtRemoveOhneUpdateById(pausenaufsicht.id);
		pausenaufsichtAddOhneUpdate(pausenaufsicht);
		pausenaufsichtUpdate();
	}

	private void pausenaufsichtRemoveOhneUpdateById(final long idPausenaufsicht) {
		// Get
		final @NotNull StundenplanPausenaufsicht pausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(_map_pausenaufsichtID_zu_pausenaufsicht, idPausenaufsicht);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_pausenaufsichten", _list_pausenaufsichten, pausenaufsicht);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenaufsicht}-Objekt.
	 * <br>Laufzeit: O(|StundenplanPausenaufsicht|), da pausenaufsichtUpdate() aufgerufen wird.
	 *
	 * @param idPausenaufsicht  Die ID des {@link StundenplanPausenaufsicht}-Objekts.
	 */
	public void pausenaufsichtRemoveById(final long idPausenaufsicht) {
		pausenaufsichtRemoveOhneUpdateById(idPausenaufsicht);
		pausenaufsichtUpdate();
	}

	private void pausenaufsichtUpdate() {
		// Prüfen
		for (final @NotNull StundenplanPausenaufsicht pausenaufsicht : _list_pausenaufsichten) {
			// Überprüfe, ob der StundenplanPausenaufsicht einen doppelten Aufsichtsbereich hat.
			final @NotNull HashSet<@NotNull Long> setBereicheDieserAufsicht = new HashSet<>();
			for (final @NotNull Long idAufsichtsbereich : pausenaufsicht.bereiche) {
				DeveloperNotificationException.ifMapNotContains("_map_aufsichtsbereichID_zu_aufsichtsbereich", _map_idAufsichtsbereich_zu_aufsichtsbereich, idAufsichtsbereich);
				DeveloperNotificationException.ifSetAddsDuplicate("setBereicheDieserAufsicht", setBereicheDieserAufsicht, idAufsichtsbereich);
			}
		}

		// Sortieren
		_list_pausenaufsichten.sort(_compPausenaufsicht);
	}

	private void pausenzeitAddOhneUpdate(final @NotNull StundenplanPausenzeit pausenzeit) {
		// Überprüfen
		DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
		Wochentag.fromIDorException(pausenzeit.wochentag);
		if ((pausenzeit.beginn != null) && (pausenzeit.ende != null))
			DeveloperNotificationException.ifTrue("pausenzeit.beginn >= pausenzeit.ende", pausenzeit.beginn >= pausenzeit.ende);

		// Hinzufügen
		DeveloperNotificationException.ifMapPutOverwrites(_map_idPausenzeit_zu_pausenzeit, pausenzeit.id, pausenzeit);
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenzeiten", _list_pausenzeiten, pausenzeit);
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

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public @NotNull StundenplanPausenzeit pausenzeitGetByIdOrException(final long idPausenzeit) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, idPausenzeit);
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
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeAsList() {
		return _list_pausenzeiten;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 * <br> Laufzeit: O(1)
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByWochentagOrEmptyList(final int wochentag) {
		return MapUtils.getOrCreateArrayList(_pausenzeitMapByWochentag, wochentag);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanPausenzeit}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param pausenzeit Das neue {@link StundenplanPausenzeit}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void pausenzeitPatch(final @NotNull StundenplanPausenzeit pausenzeit) {
		pausenzeitRemoveOhneUpdateById(pausenzeit.id);
		pausenzeitAddOhneUpdate(pausenzeit);
		pausenzeitUpdate();
	}

	private void pausenzeitRemoveOhneUpdateById(final long idPausenzeit) {
		// Get
		final @NotNull StundenplanPausenzeit pausenzeit = DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, idPausenzeit);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idPausenzeit_zu_pausenzeit, pausenzeit.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_pausenzeiten", _list_pausenzeiten, pausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 * <br>Laufzeit: O(|StundenplanPausenzeit|), da pausenzeitUpdate() aufgerufen wird.
	 *
	 * @param idPausenzeit  Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public void pausenzeitRemoveById(final long idPausenzeit) {
		pausenzeitRemoveOhneUpdateById(idPausenzeit);
		pausenzeitUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @param listPausenzeit  Die Liste der zu entfernenden {@link StundenplanPausenzeit}-Objekte.
	 */
	public void pausenzeitRemoveAll(final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit) {
		for (final @NotNull StundenplanPausenzeit pausenzeit : listPausenzeit)
			pausenzeitRemoveOhneUpdateById(pausenzeit.id);
		pausenzeitUpdate();
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitGetMinutenMin() {
		return _pausenzeitMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitGetMinutenMax() {
		return _pausenzeitMinutenMax;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMin() {
		return _pausenzeitUndZeitrasterMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMax() {
		return _pausenzeitUndZeitrasterMinutenMax;
	}


	private void pausenzeitUpdate() {
		// Sortieren
		_list_pausenzeiten.sort(_compPausenzeit);

		// Initialisierungen
		_pausenzeitMinutenMin = MINUTEN_INF_POS;               // Ungültiger Dummy-Wert
		_pausenzeitMinutenMax = MINUTEN_INF_NEG;               // Ungültiger Dummy-Wert
		_pausenzeitUndZeitrasterMinutenMin = MINUTEN_INF_POS;  // Ungültiger Dummy-Wert
		_pausenzeitUndZeitrasterMinutenMax = MINUTEN_INF_NEG;  // Ungültiger Dummy-Wert
		_pausenzeitMapByWochentag.clear();

		// Iterieren
		for (final @NotNull StundenplanPausenzeit p : _list_pausenzeiten) { // Wichtig: Pausenzeiten sind sortiert!
			_pausenzeitMinutenMin = BlockungsUtils.minVI(_pausenzeitMinutenMin, p.beginn);
			_pausenzeitMinutenMax = BlockungsUtils.maxVI(_pausenzeitMinutenMax, p.ende);
			_pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(_pausenzeitUndZeitrasterMinutenMin, p.beginn);
			_pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(_pausenzeitUndZeitrasterMinutenMax, p.ende);
			MapUtils.getOrCreateArrayList(_pausenzeitMapByWochentag, p.wochentag).add(p);
		}
		for (final @NotNull StundenplanZeitraster z : _list_zeitraster) {
			_pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(_pausenzeitUndZeitrasterMinutenMin, z.stundenbeginn);
			_pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(_pausenzeitUndZeitrasterMinutenMax, z.stundenende);
		}

		// Dummy-Werte durch Default-Werte ersetzen.
		_pausenzeitMinutenMin = (_pausenzeitMinutenMin == MINUTEN_INF_POS) ? 480 : _pausenzeitMinutenMin;
		_pausenzeitMinutenMax = (_pausenzeitMinutenMax == MINUTEN_INF_NEG) ? 480 : _pausenzeitMinutenMax;
		_pausenzeitUndZeitrasterMinutenMin = (_pausenzeitUndZeitrasterMinutenMin == MINUTEN_INF_POS) ? 480 : _pausenzeitUndZeitrasterMinutenMin;
		_pausenzeitUndZeitrasterMinutenMax = (_pausenzeitUndZeitrasterMinutenMax == MINUTEN_INF_NEG) ? 480 : _pausenzeitUndZeitrasterMinutenMax;
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

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public @NotNull StundenplanRaum raumGetByIdOrException(final long idRaum) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idRaum_zu_raum, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanRaum> raumGetMengeAsList() {
		return _list_raeume;
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanRaum}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param raum Das neue {@link StundenplanRaum}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void raumPatch(final @NotNull StundenplanRaum raum) {
		raumRemoveOhneUpdateById(raum.id);
		raumAddOhneUpdate(raum);
		raumUpdate();
	}

	private void raumRemoveOhneUpdateById(final long idRaum) {
		// Get
		final @NotNull StundenplanRaum raum = DeveloperNotificationException.ifMapGetIsNull(_map_idRaum_zu_raum, idRaum);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idRaum_zu_raum, raum.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_raeume", _list_raeume, raum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanRaum}-Objekt.
	 * <br>Laufzeit: O(|StundenplanRaum|), da raumUpdate() aufgerufen wird.
	 *
	 * @param idRaum  Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public void raumRemoveById(final long idRaum) {
		raumRemoveOhneUpdateById(idRaum);
		raumUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum  Die Liste der zu entfernenden {@link StundenplanRaum}-Objekte.
	 */
	public void raumRemoveAll(final @NotNull List<@NotNull StundenplanRaum> listRaum) {
		for (final @NotNull StundenplanRaum raum : listRaum)
			raumRemoveOhneUpdateById(raum.id);
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
		// Ist es Kurs-Unterricht?
		if (u.idKurs != null) {
			DeveloperNotificationException.ifMapNotContains("_map_kursID_zu_kurs", _map_idKurs_zu_kurs, u.idKurs);
			final @NotNull List<@NotNull StundenplanUnterricht> unterrichtDesKurses = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListAddsDuplicate("unterrichtDesKurses", unterrichtDesKurses, u);
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
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekten, die in der Stundenplanzelle "wochentag, stunde" und "wochentyp" liegen.
	 * Falls der Parameter inklWoche0 TRUE ist, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @param wochentag     Der {@link Wochentag}-ENUM.
	 * @param stunde        Die Unterrichtsstunde.
	 * @param wochentyp     Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 * @param inklWoche0    falls TRUE, wird Unterricht des Wochentyps 0 hinzugefügt.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekten, die in der Stundenplanzelle "wochentag, stunde" und "wochentyp" liegen.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(final @NotNull Wochentag wochentag, final int stunde, final int wochentyp, final boolean inklWoche0) {
		final long idZeitraster = zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde).id;
		return unterrichtGetMengeByZeitrasterIdAndWochentypAndInklusiveOrEmptyList(idZeitraster, wochentyp, inklWoche0);
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
	 * Liefert TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 * <br>Laufzeit: O(1)
	 *
	 * @return TRUE, falls es {@link StundenplanUnterricht} gibt, der einen Wochentyp > 0 hat.
	 */
	public boolean unterrichtHatMultiWochen() {
		return _unterrichtHatMultiWochen;
	}

	private void unterrichtRemoveByIdOhneUpdate(final long idUnterricht) {
		// Get
		final @NotNull StundenplanUnterricht u = DeveloperNotificationException.ifNull("_map_idUnterricht_zu_unterricht.get(" + idUnterricht + ")", _map_idUnterricht_zu_unterricht.get(idUnterricht));

		// Remove
		DeveloperNotificationException.ifMapRemoveFailes(_map_idUnterricht_zu_unterricht, u.id);
		MapUtils.getOrCreateArrayList(_map_idZeitraster_zu_unterrichtmenge, u.idZeitraster).remove(u);
		Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, u.idZeitraster, u.wochentyp).remove(u);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idUnterricht_zu_lehrermenge, u.id);
		// Ist es Kurs-Unterricht?
		if (u.idKurs != null) {
			DeveloperNotificationException.ifMapNotContains("_map_kursID_zu_kurs", _map_idKurs_zu_kurs, u.idKurs);
			final @NotNull List<@NotNull StundenplanUnterricht> unterrichtDesKurses = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListRemoveFailes("unterrichtDesKurses", unterrichtDesKurses, u);
		}
		_list_unterricht.remove(u);
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanUnterricht}-Objekt.
	 *
	 * @param idUnterricht  Die Datenbank-ID des {@link StundenplanUnterricht}-Objekts.
	 */
	public void unterrichtRemoveById(final long idUnterricht) {
		unterrichtRemoveByIdOhneUpdate(idUnterricht);
		unterrichtUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanUnterricht}-Objekte.
	 *
	 * @param listUnterricht  Die Liste der zu entfernenden {@link StundenplanUnterricht}-Objekte.
	 */
	public void unterrichtRemoveAll(final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {
		for (final @NotNull StundenplanUnterricht unterricht : listUnterricht)
			unterrichtRemoveByIdOhneUpdate(unterricht.id);
		unterrichtUpdate();
	}

	/**
	 * Aktualisiert verschiedene Werte nachdem sich die {@link StundenplanUnterricht}-Menge verändert hat.
	 */
	private void unterrichtUpdate() {
		_unterrichtHatMultiWochen = false;

		for (final @NotNull StundenplanUnterricht u : _list_unterricht)
			if (u.wochentyp > 0) {
				_unterrichtHatMultiWochen = true;
				break;
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
		return CollectionUtils.toFilteredArrayList(_list_zeitraster, (final @NotNull StundenplanZeitraster z) -> (wochentag.id == z.wochentag) &&  zeitrasterGetSchneidenSich(beginn, ende, z.stundenbeginn, z.stundenende));
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
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int zeitrasterGetMinutenMin() {
		return _zeitrasterMinutenMin;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Minimum gesucht wird.
	 *
	 * @return das Minimum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int zeitrasterGetMinutenMinDerStunde(final int stunde) {
		final Integer min = _zeitrasterMinutenMinByStunde.get(stunde); // Beide Fälle von NULL können auftreten!
		return (min == null) ? 480 : min;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int zeitrasterGetMinutenMax() {
		return _zeitrasterMinutenMax;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @param stunde  Die Unterrichtsstunde, deren Maximum gesucht wird.
	 *
	 * @return das Maximum aller {@link StundenplanZeitraster#stundenbeginn}-Objekte einer bestimmten Unterrichtsstunde, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int zeitrasterGetMinutenMaxDerStunde(final int stunde) {
		final Integer max = _zeitrasterMinutenMaxByStunde.get(stunde); // Beide Fälle von NULL können auftreten!
		return (max == null) ? 480 : max;
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
	 * Liefert TRUE, falls es in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" gibt.
	 *
	 * @param wochentag  Der {@link Wochentag}-ENUM.
	 * @param stunde     Die Unterrichtsstunde.
	 * @param wochentyp  Der Wochentyp (0 jede Woche, 1 nur Woche A, 2 nur Woche B, ...)
	 *
	 * @return TRUE, falls es in der Stundenplanzelle "wochtag, stunde" Unterricht eines "wochentyps" gibt.
	 */
	public boolean zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(final @NotNull Wochentag wochentag, final int stunde, final int wochentyp) {
		final StundenplanZeitraster zeitraster = _map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag.id, stunde);

		if (zeitraster == null)
			return false;

		return !Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, zeitraster.id, wochentyp).isEmpty();
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
	 * Liefert TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit dem Wochentag existiert.
	 *
	 * @param wochentag  Der Wochentag, deren Zeitrastermenge überprüft wird.
	 *
	 * @return TRUE, falls ein {@link StundenplanZeitraster}-Objekt mit dem Wochentag existiert.
	 */
	public boolean zeitrasterExistsByWochentag(final int wochentag) {
		return !MapUtils.getOrCreateArrayList(_map_wochentag_zu_zeitrastermenge, wochentag).isEmpty();
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanZeitraster}-Objekt und fügt dann das neue Objekt hinzu.
	 * <br>Hinweis: Die ID darf nicht gepatch werden!
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void zeitrasterPatch(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterRemoveOhneUpdate(zeitraster.id);
		zeitrasterAddOhneUpdate(zeitraster);
		zeitrasterUpdate();
	}

	private void zeitrasterRemoveOhneUpdate(final long idZeitraster) {
		// Kaskade: StundenplanUnterricht
		final @NotNull List<@NotNull StundenplanUnterricht> listU = MapUtils.getOrCreateArrayList(_map_idZeitraster_zu_unterrichtmenge, idZeitraster);
		for (final @NotNull StundenplanUnterricht u : listU)
			unterrichtRemoveByIdOhneUpdate(u.id);

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
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param idZeitraster  Die Datenbank-ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public void zeitrasterRemoveById(final long idZeitraster) {
		zeitrasterRemoveOhneUpdate(idZeitraster);
		zeitrasterUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte aus dem Stundenplan.
	 * <br> Hinweis: Kaskadierend werden auch alle {@link StundenplanUnterricht}-Objekte gelöscht.
	 *
	 * @param listZeitraster  Die {@link StundenplanZeitraster}-Objekte, die entfernt werden sollen.
	 */
	public void zeitrasterRemoveAll(final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster) {
		for (final @NotNull StundenplanZeitraster zeitraster : listZeitraster)
			zeitrasterRemoveOhneUpdate(zeitraster.id);
		zeitrasterUpdate();
	}

	/**
	 * Entfernt alle {@link StundenplanZeitraster}-Objekte, die einen bestimmten Wochentag haben.
	 *
	 * @param wochentagEnumID  Die ID des {@link Wochentag}.
	 */
	public void zeitrasterRemoveByWochentag(final int wochentagEnumID) {
		final @NotNull List<@NotNull StundenplanZeitraster> list = ListUtils.getCopyFiltered(_list_zeitraster, (final @NotNull StundenplanZeitraster z) -> z.wochentag == wochentagEnumID);
		zeitrasterRemoveAll(list);
	}

	/**
	 * Aktualisiert verschiedene Werte nachdem sich die Menge der Zeitraster verändert hat.
	 * <br>Laufzeit: O(|Zeitraster| + |Unterricht|)
	 */
	private void zeitrasterUpdate() {
		// Kaskade: StundenplanUnterricht
		unterrichtUpdate();

		// Sortieren
		_list_zeitraster.sort(_compZeitraster);

		// Initialisierungen
		_pausenzeitUndZeitrasterMinutenMin = MINUTEN_INF_POS; // Ungültiger Dummy-Wert
		_pausenzeitUndZeitrasterMinutenMax = MINUTEN_INF_NEG; // Ungültiger Dummy-Wert
		_zeitrasterMinutenMin = MINUTEN_INF_POS;              // Ungültiger Dummy-Wert
		_zeitrasterMinutenMax = MINUTEN_INF_NEG;              // Ungültiger Dummy-Wert
		_zeitrasterWochentagMin = WOCHENTAG_INF_POS;          // Ungültiger Dummy-Wert
		_zeitrasterWochentagMax = WOCHENTAG_INF_NEG;          // Ungültiger Dummy-Wert
		_zeitrasterStundeMin = STUNDE_INF_POS;                // Ungültiger Dummy-Wert
		_zeitrasterStundeMax = STUNDE_INF_NEG;                // Ungültiger Dummy-Wert
		_zeitrasterMinutenMinByStunde.clear();
		_zeitrasterMinutenMaxByStunde.clear();

		// Iterieren
		for (final @NotNull StundenplanZeitraster z :_list_zeitraster) {
			_pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(_pausenzeitUndZeitrasterMinutenMin, z.stundenbeginn);
			_pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(_pausenzeitUndZeitrasterMinutenMax, z.stundenende);
			_zeitrasterMinutenMin = BlockungsUtils.minVI(_zeitrasterMinutenMin, z.stundenbeginn);
			_zeitrasterMinutenMax = BlockungsUtils.maxVI(_zeitrasterMinutenMax, z.stundenende);
			_zeitrasterWochentagMin = BlockungsUtils.minVI(_zeitrasterWochentagMin, z.wochentag);
			_zeitrasterWochentagMax = BlockungsUtils.maxVI(_zeitrasterWochentagMax, z.wochentag);
			_zeitrasterStundeMin = BlockungsUtils.minVI(_zeitrasterStundeMin, z.unterrichtstunde);
			_zeitrasterStundeMax = BlockungsUtils.maxVI(_zeitrasterStundeMax, z.unterrichtstunde);
			_zeitrasterMinutenMinByStunde.put(z.unterrichtstunde, BlockungsUtils.minII(_zeitrasterMinutenMinByStunde.get(z.unterrichtstunde), z.stundenbeginn));
			_zeitrasterMinutenMaxByStunde.put(z.unterrichtstunde, BlockungsUtils.maxII(_zeitrasterMinutenMaxByStunde.get(z.unterrichtstunde), z.stundenende));
		}
		for (final @NotNull StundenplanPausenzeit p :_list_pausenzeiten) {
			_pausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(_pausenzeitUndZeitrasterMinutenMin, p.beginn);
			_pausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(_pausenzeitUndZeitrasterMinutenMax, p.ende);
		}

		// Dummy-Werte mit Default-Werten ersetzen
		_pausenzeitUndZeitrasterMinutenMin = (_pausenzeitUndZeitrasterMinutenMin == MINUTEN_INF_POS) ? 480 : _pausenzeitUndZeitrasterMinutenMin;
		_pausenzeitUndZeitrasterMinutenMax = (_pausenzeitUndZeitrasterMinutenMax == MINUTEN_INF_NEG) ? 480 : _pausenzeitUndZeitrasterMinutenMax;
		_zeitrasterMinutenMin = (_zeitrasterMinutenMin == MINUTEN_INF_POS) ? 480 : _zeitrasterMinutenMin;
		_zeitrasterMinutenMax = (_zeitrasterMinutenMax == MINUTEN_INF_NEG) ? 480 : _zeitrasterMinutenMax;
		_zeitrasterWochentagMin = (_zeitrasterWochentagMin == WOCHENTAG_INF_POS) ? Wochentag.MONTAG.id : _zeitrasterWochentagMin;
		_zeitrasterWochentagMax = (_zeitrasterWochentagMax == WOCHENTAG_INF_NEG) ? Wochentag.MONTAG.id : _zeitrasterWochentagMax;
		_zeitrasterStundeMin = (_zeitrasterStundeMin == STUNDE_INF_POS) ? 1 : _zeitrasterStundeMin;
		_zeitrasterStundeMax = (_zeitrasterStundeMax == STUNDE_INF_NEG) ? 1 : _zeitrasterStundeMax;

		// _zeitrasterStundenRange
		_zeitrasterStundenRange = new int[_zeitrasterStundeMax - _zeitrasterStundeMin + 1];
		for (int i = 0; i < _zeitrasterStundenRange.length; i++)
			_zeitrasterStundenRange[i] = _zeitrasterStundeMin + i;

		// _zeitrasterWochentageAlsEnumRange
		_zeitrasterWochentageAlsEnumRange = new Wochentag[_zeitrasterWochentagMax - _zeitrasterWochentagMin + 1];
		for (int i = 0; i < _zeitrasterWochentageAlsEnumRange.length; i++)
			_zeitrasterWochentageAlsEnumRange[i] = Wochentag.fromIDorException(_zeitrasterWochentagMin + i);
	}

}
