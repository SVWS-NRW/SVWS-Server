package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.set.AVLSet;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht;
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
	private static final @NotNull Comparator<@NotNull StundenplanJahrgang> _compJahrgang = (final @NotNull StundenplanJahrgang a, final @NotNull StundenplanJahrgang b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
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
	private static final @NotNull Comparator<@NotNull StundenplanKlasse> _compKlasse = (final @NotNull StundenplanKlasse a, final @NotNull StundenplanKlasse b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanKlasse> _list_klassen = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKlasse> _map_idKlasse_zu_klasse = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _map_idKlasse_zu_klassenunterricht = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_idKlasse_zu_unterrichtmenge = new HashMap<>();

	// StundenplanKlassenunterricht
	private static final @NotNull Comparator<@NotNull StundenplanKlassenunterricht> _compKlassenunterricht = (final @NotNull StundenplanKlassenunterricht a, final @NotNull StundenplanKlassenunterricht b) -> {
		if (a.idKlasse < b.idKlasse) return -1;
		if (a.idKlasse > b.idKlasse) return +1;
		if (a.idFach < b.idFach) return -1;
		if (a.idFach > b.idFach) return +1;
		if (a.wochenstunden < b.wochenstunden) return -1;
		if (a.wochenstunden > b.wochenstunden) return +1;
		return a.bezeichnung.compareTo(b.bezeichnung);
	};
	private final @NotNull List<@NotNull StundenplanKlassenunterricht> _list_klassenunterricht = new ArrayList<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull StundenplanKlassenunterricht> _map2d_idKlasse_idFach_zu_klassenunterricht = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map2d_idKlasse_idFach_zu_unterrichtmenge = new HashMap2D<>();

	// StundenplanKurs
	private static final @NotNull Comparator<@NotNull StundenplanKurs> _compKurs = (final @NotNull StundenplanKurs a, final @NotNull StundenplanKurs b) -> Long.compare(a.id, b.id);
	private final @NotNull List<@NotNull StundenplanKurs> _list_kurse = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKurs> _map_idKurs_zu_kurs = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_idKurs_zu_unterrichtmenge = new HashMap<>();

	// StundenplanLehrer
	private static final @NotNull Comparator<@NotNull StundenplanLehrer> _compLehrer = (final @NotNull StundenplanLehrer a, final @NotNull StundenplanLehrer b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanLehrer> _list_lehrer = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanLehrer> _map_idLehrer_zu_lehrer = new HashMap<>();

	// StundenplanPausenaufsicht
	private static final @NotNull Comparator<@NotNull StundenplanPausenaufsicht> _compPausenaufsicht = (final @NotNull StundenplanPausenaufsicht a, final @NotNull StundenplanPausenaufsicht b) -> Long.compare(a.id, b.id);
	private final @NotNull List<@NotNull StundenplanPausenaufsicht> _list_pausenaufsichten = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenaufsicht> _map_idPausenaufsicht_zu_pausenaufsicht = new HashMap<>();

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
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanPausenaufsicht>> _map_idPausenzeit_zu_pausenaufsichtmenge = new HashMap<>();

	// StundenplanRaum
	private static final @NotNull Comparator<@NotNull StundenplanRaum> _compRaum = (final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanRaum> _list_raeume = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanRaum> _map_idRaum_zu_raum = new HashMap<>();

	// StundenplanSchiene
	private static final @NotNull Comparator<@NotNull StundenplanSchiene> _compSchiene = (final @NotNull StundenplanSchiene a, final @NotNull StundenplanSchiene b) -> {
		if (a.idJahrgang < b.idJahrgang) return -1;
		if (a.idJahrgang > b.idJahrgang) return +1;
		if (a.nummer < b.nummer) return -1;
		if (a.nummer > b.nummer) return +1;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanSchiene> _list_schienen = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchiene> _map_idSchiene_zu_schiene = new HashMap<>();

	// StundenplanSchueler
	private static final @NotNull Comparator<@NotNull StundenplanSchueler> _compSchueler = (final @NotNull StundenplanSchueler a, final @NotNull StundenplanSchueler b) -> {
		if (a.idKlasse < b.idKlasse) return -1;
		if (a.idKlasse > b.idKlasse) return +1;
		final int cmpNachname = a.nachname.compareTo(b.nachname);
		if (cmpNachname != 0) return cmpNachname;
		final int cmpVorname = a.vorname.compareTo(b.vorname);
		if (cmpVorname != 0) return cmpVorname;
		return Long.compare(a.id, b.id);
	};
	private final @NotNull List<@NotNull StundenplanSchueler> _list_schueler = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchueler> _map_schuelerID_zu_schueler = new HashMap<>();

	// StundenplanUnterricht
	private static final @NotNull Comparator<@NotNull StundenplanUnterricht> _compUnterricht = (final @NotNull StundenplanUnterricht a, final @NotNull StundenplanUnterricht b) -> Long.compare(a.id, b.id);
	private final @NotNull List<@NotNull StundenplanUnterricht> _list_unterricht = new ArrayList<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanUnterricht> _map_idUnterricht_zu_unterricht = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Integer, @NotNull List<@NotNull StundenplanUnterricht>> _map2d_idZeitraster_wochentyp_zu_unterrichtmenge = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanLehrer>> _map_idUnterricht_zu_lehrermenge = new HashMap<>();
	private boolean _uUnterrichtHatMultiWochen = false;

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
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_idZeitraster_zu_unterrichtmenge = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanZeitraster> _map2d_wochentag_stunde_zu_zeitraster = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _map_wochentag_zu_zeitrastermenge = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanZeitraster>> _map_stunde_zu_zeitrastermenge = new HashMap<>();

	// Weiteres
	private final long _stundenplanID;
	private final int _stundenplanWochenTypModell;
	private final long _stundenplanSchuljahresAbschnittID;
	private final @NotNull String _stundenplanGueltigAb;
	private final @NotNull String _stundenplanGueltigBis;
	private final @NotNull String _stundenplanBezeichnung;

	// Attribute, die durch update() aktualisiert werden.
	private @NotNull int @NotNull [] _uZeitrasterStundenRange = new int[] {1};
	private @NotNull int @NotNull [] _uZeitrasterStundenRangeOhneLeere = new int[] {1};
	private final @NotNull HashMap<@NotNull Integer, Integer> _uZeitrasterMinutenMinByStunde = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, Integer> _uZeitrasterMinutenMaxByStunde = new HashMap<>();
	private @NotNull Wochentag @NotNull [] _uZeitrasterWochentageAlsEnumRange = new Wochentag[] {Wochentag.MONTAG};
	private int _uZeitrasterWochentagMin = Wochentag.MONTAG.id;
	private int _uZeitrasterWochentagMax = Wochentag.MONTAG.id;
	private int _uZeitrasterStundeMin = 1;
	private int _uZeitrasterStundeMax = 1;
	private int _uZeitrasterStundeMinOhneLeere = 1;
	private int _uZeitrasterStundeMaxOhneLeere = 1;
	private int _uZeitrasterMinutenMin = 480;
	private int _uZeitrasterMinutenMax = 480;
	private int _uPausenzeitMinutenMin = 480;
	private int _uPausenzeitMinutenMax = 480;
	private int _uPausenzeitUndZeitrasterMinutenMin = 480;
	private int _uPausenzeitUndZeitrasterMinutenMax = 480;
	private int _uPausenzeitUndZeitrasterMinutenMinOhneLeere = 480;
	private int _uPausenzeitUndZeitrasterMinutenMaxOhneLeere = 480;
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanPausenzeit>> _uPausenzeitMapByWochentag = new HashMap<>();
	private final @NotNull HashMap<@NotNull Integer, @NotNull List<@NotNull StundenplanPausenaufsicht>> _uPausenaufsichtMapByWochentag = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _uKursMapByKlasseId = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _uKursMapByLehrerId = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKurs>> _uKursMapBySchuelerId = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _uKlassenunterrichtByLehrerId = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanKlassenunterricht>> _uKlassenunterrichtBySchuelerId = new HashMap<>();
	private final @NotNull List<@NotNull StundenplanPausenzeit> _uPausenzeitListNichtLeere = new ArrayList<>();


	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grund-Daten des Stundenplanes.
	 * @param unterrichte           liefert die Informationen zu allen {@link StundenplanUnterricht} im Stundenplan. Die Liste darf leer sein.
	 * @param pausenaufsichten      liefert die Informationen zu allen {@link StundenplanPausenaufsicht} im Stundenplan. Die Liste darf leer sein.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans. Darf NULL sein.
	 */
	public StundenplanManager(final @NotNull Stundenplan daten, final @NotNull List<@NotNull StundenplanUnterricht> unterrichte, final @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichten, final StundenplanUnterrichtsverteilung unterrichtsverteilung) {
		_stundenplanID = daten.id;
		_stundenplanWochenTypModell = daten.wochenTypModell;
		_stundenplanSchuljahresAbschnittID = daten.idSchuljahresabschnitt;
		_stundenplanGueltigAb = daten.gueltigAb;
		_stundenplanGueltigBis = daten.gueltigBis;
		_stundenplanBezeichnung = daten.bezeichnungStundenplan;

		// Spezialfall: "unterrichtsverteilung" ist NULL
		StundenplanUnterrichtsverteilung uv = unterrichtsverteilung;
		if (uv == null) {
			uv = new StundenplanUnterrichtsverteilung();
			uv.id = _stundenplanID;
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
				uv.klassenunterricht,
				pausenaufsichten,
				uv.kurse,
				unterrichte
				);

	}

	/**
	 * Dieser Manager baut mit Hilfe des {@link StundenplanKomplett}-Objektes eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param stundenplanKomplett  Beinhaltet alle relevanten Daten für einen Stundenplan.
	 */
	public StundenplanManager(final @NotNull StundenplanKomplett stundenplanKomplett) {
		_stundenplanID = stundenplanKomplett.daten.id;
		_stundenplanWochenTypModell = stundenplanKomplett.daten.wochenTypModell;
		_stundenplanSchuljahresAbschnittID = stundenplanKomplett.daten.idSchuljahresabschnitt;
		_stundenplanGueltigAb = stundenplanKomplett.daten.gueltigAb;
		_stundenplanGueltigBis = stundenplanKomplett.daten.gueltigBis;
		_stundenplanBezeichnung = stundenplanKomplett.daten.bezeichnungStundenplan;

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
				stundenplanKomplett.unterrichtsverteilung.klassenunterricht,
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
			             final @NotNull List<@NotNull StundenplanKlassenunterricht> listKlassenunterricht,
			             final @NotNull List<@NotNull StundenplanPausenaufsicht> listPausenaufsicht,
			             final @NotNull List<@NotNull StundenplanKurs> listKurs,
			             final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {

		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell < 0", _stundenplanWochenTypModell < 0);
		DeveloperNotificationException.ifTrue("stundenplanWochenTypModell == 1", _stundenplanWochenTypModell == 1);

		kalenderwochenzuordnungAddAll(listKWZ);          // ✔, referenziert ---
		kalenderwochenzuordnungErzeugePseudoMenge();     // ✔, referenziert Kalenderwochenzuordnung (wg. Default-Wochentyp-Bestimmung)
		fachAddAll(listFach);                            // ✔, referenziert ---
		jahrgangAddAll(listJahrgang);                    // ✔, referenziert ---
		zeitrasterAddAll(listZeitraster);                // ✔, referenziert ---
		raumAddAll(listRaum);                            // ✔, referenziert ---
		pausenzeitAddAll(listPausenzeit);                // ✔, referenziert ---
		aufsichtsbereichAddAll(listAufsichtsbereich);    // ✔, referenziert ---
		klasseAddAll(listKlasse);                        // ✔, referenziert [Jahrgang], es gibt auch jahrgangsübergreifende Klassen!
		lehrerAddAll(listLehrer);                        // ✔, referenziert [Fach]
		schuelerAddAll(listSchueler);                    // ✔, referenziert Klasse
		schieneAddAll(listSchiene);                      // ✔, referenziert Jahrgang
		klassenunterrichtAddAll(listKlassenunterricht);  // ✔, referenziert Klasse, [Jahrgang], [Schienen]
		pausenaufsichtAddAll(listPausenaufsicht);        // ✔, referenziert Lehrer, Pausenzeit, [Aufsichtsbereich]
		kursAddAll(listKurs);                            // ✔, referenziert [Schienen], [Jahrgang], [Schüler]
		unterrichtAddAll(listUnterricht);                // ✔, referenziert Zeitraster, Kurs, Fach, [Lehrer], [Klasse], [Raum], [Schiene]
	}

	private void update() {
		updateIteriereKlassenunterricht();
		updateIteriereKurs();
		updateIteriereUnterricht();

		// Initialisierungen
		_uPausenzeitMinutenMin = MINUTEN_INF_POS;                       // Ungültiger Dummy-Wert
		_uPausenzeitMinutenMax = MINUTEN_INF_NEG;                       // Ungültiger Dummy-Wert
		_uPausenzeitUndZeitrasterMinutenMin = MINUTEN_INF_POS;          // Ungültiger Dummy-Wert
		_uPausenzeitUndZeitrasterMinutenMax = MINUTEN_INF_NEG;          // Ungültiger Dummy-Wert
		_uPausenzeitUndZeitrasterMinutenMinOhneLeere = MINUTEN_INF_POS; // Ungültiger Dummy-Wert
		_uPausenzeitUndZeitrasterMinutenMaxOhneLeere = MINUTEN_INF_NEG; // Ungültiger Dummy-Wert
		_uPausenzeitMapByWochentag.clear();
		_uPausenaufsichtMapByWochentag.clear();
		_uPausenzeitListNichtLeere.clear();

		_uZeitrasterMinutenMin = MINUTEN_INF_POS;                       // Ungültiger Dummy-Wert
		_uZeitrasterMinutenMax = MINUTEN_INF_NEG;                       // Ungültiger Dummy-Wert
		_uZeitrasterWochentagMin = WOCHENTAG_INF_POS;                   // Ungültiger Dummy-Wert
		_uZeitrasterWochentagMax = WOCHENTAG_INF_NEG;                   // Ungültiger Dummy-Wert
		_uZeitrasterStundeMin = STUNDE_INF_POS;                         // Ungültiger Dummy-Wert
		_uZeitrasterStundeMax = STUNDE_INF_NEG;                         // Ungültiger Dummy-Wert
		_uZeitrasterStundeMinOhneLeere = STUNDE_INF_POS;                // Ungültiger Dummy-Wert
		_uZeitrasterStundeMaxOhneLeere = STUNDE_INF_NEG;                // Ungültiger Dummy-Wert
		_uZeitrasterMinutenMinByStunde.clear();
		_uZeitrasterMinutenMaxByStunde.clear();



		// Iterieren über Pausenaufsichten
		for (final @NotNull StundenplanPausenaufsicht a : _list_pausenaufsichten) {
			final @NotNull StundenplanPausenzeit p = DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, a.idPausenzeit);
			MapUtils.getOrCreateArrayList(_uPausenaufsichtMapByWochentag, p.wochentag).add(a);
		}

		// Iterieren über Pausenzeiten
		for (final @NotNull StundenplanPausenzeit p : _list_pausenzeiten) { // Wichtig: Pausenzeiten sind sortiert!
			MapUtils.getOrCreateArrayList(_uPausenzeitMapByWochentag, p.wochentag).add(p);
			_uPausenzeitMinutenMin = BlockungsUtils.minVI(_uPausenzeitMinutenMin, p.beginn);
			_uPausenzeitMinutenMax = BlockungsUtils.maxVI(_uPausenzeitMinutenMax, p.ende);
			_uPausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(_uPausenzeitUndZeitrasterMinutenMin, p.beginn);
			_uPausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(_uPausenzeitUndZeitrasterMinutenMax, p.ende);
			// Nicht leere Zeitraster
			final @NotNull List<@NotNull StundenplanPausenaufsicht> listPA = DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenaufsichtmenge, p.id);
			if (!listPA.isEmpty()) {
				_uPausenzeitUndZeitrasterMinutenMinOhneLeere = BlockungsUtils.minVI(_uPausenzeitUndZeitrasterMinutenMinOhneLeere, p.beginn);
				_uPausenzeitUndZeitrasterMinutenMaxOhneLeere = BlockungsUtils.maxVI(_uPausenzeitUndZeitrasterMinutenMaxOhneLeere, p.ende);
				_uPausenzeitListNichtLeere.add(p);
			}
		}

		// Iterieren über Zeitraster
		for (final @NotNull StundenplanZeitraster z :_list_zeitraster) {
			_uZeitrasterWochentagMin = BlockungsUtils.minVI(_uZeitrasterWochentagMin, z.wochentag);
			_uZeitrasterWochentagMax = BlockungsUtils.maxVI(_uZeitrasterWochentagMax, z.wochentag);
			_uZeitrasterStundeMin = BlockungsUtils.minVI(_uZeitrasterStundeMin, z.unterrichtstunde);
			_uZeitrasterStundeMax = BlockungsUtils.maxVI(_uZeitrasterStundeMax, z.unterrichtstunde);
			_uZeitrasterMinutenMinByStunde.put(z.unterrichtstunde, BlockungsUtils.minII(_uZeitrasterMinutenMinByStunde.get(z.unterrichtstunde), z.stundenbeginn));
			_uZeitrasterMinutenMaxByStunde.put(z.unterrichtstunde, BlockungsUtils.maxII(_uZeitrasterMinutenMaxByStunde.get(z.unterrichtstunde), z.stundenende));
			_uZeitrasterMinutenMin = BlockungsUtils.minVI(_uZeitrasterMinutenMin, z.stundenbeginn);
			_uZeitrasterMinutenMax = BlockungsUtils.maxVI(_uZeitrasterMinutenMax, z.stundenende);
			_uPausenzeitUndZeitrasterMinutenMin = BlockungsUtils.minVI(_uPausenzeitUndZeitrasterMinutenMin, z.stundenbeginn);
			_uPausenzeitUndZeitrasterMinutenMax = BlockungsUtils.maxVI(_uPausenzeitUndZeitrasterMinutenMax, z.stundenende);
			// Nicht leere Zeitraster
			final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_unterrichtmenge, z.id);
			if (!listU.isEmpty()) {
				_uPausenzeitUndZeitrasterMinutenMinOhneLeere = BlockungsUtils.minVI(_uPausenzeitUndZeitrasterMinutenMinOhneLeere, z.stundenbeginn);
				_uPausenzeitUndZeitrasterMinutenMaxOhneLeere = BlockungsUtils.maxVI(_uPausenzeitUndZeitrasterMinutenMaxOhneLeere, z.stundenende);
				_uZeitrasterStundeMinOhneLeere = BlockungsUtils.minVI(_uZeitrasterStundeMinOhneLeere, z.unterrichtstunde);
				_uZeitrasterStundeMaxOhneLeere = BlockungsUtils.maxVI(_uZeitrasterStundeMaxOhneLeere, z.unterrichtstunde);
			}
		}

		// Dummy-Werte mit Default-Werten ersetzen
		_uPausenzeitMinutenMin = (_uPausenzeitMinutenMin == MINUTEN_INF_POS) ? 480 : _uPausenzeitMinutenMin;
		_uPausenzeitMinutenMax = (_uPausenzeitMinutenMax == MINUTEN_INF_NEG) ? 480 : _uPausenzeitMinutenMax;
		_uPausenzeitUndZeitrasterMinutenMin = (_uPausenzeitUndZeitrasterMinutenMin == MINUTEN_INF_POS) ? 480 : _uPausenzeitUndZeitrasterMinutenMin;
		_uPausenzeitUndZeitrasterMinutenMax = (_uPausenzeitUndZeitrasterMinutenMax == MINUTEN_INF_NEG) ? 480 : _uPausenzeitUndZeitrasterMinutenMax;
		_uPausenzeitUndZeitrasterMinutenMinOhneLeere = (_uPausenzeitUndZeitrasterMinutenMinOhneLeere == MINUTEN_INF_POS) ? 480 : _uPausenzeitUndZeitrasterMinutenMinOhneLeere;
		_uPausenzeitUndZeitrasterMinutenMaxOhneLeere = (_uPausenzeitUndZeitrasterMinutenMaxOhneLeere == MINUTEN_INF_NEG) ? 480 : _uPausenzeitUndZeitrasterMinutenMaxOhneLeere;
		_uZeitrasterMinutenMin = (_uZeitrasterMinutenMin == MINUTEN_INF_POS) ? 480 : _uZeitrasterMinutenMin;
		_uZeitrasterMinutenMax = (_uZeitrasterMinutenMax == MINUTEN_INF_NEG) ? 480 : _uZeitrasterMinutenMax;
		_uZeitrasterWochentagMin = (_uZeitrasterWochentagMin == WOCHENTAG_INF_POS) ? Wochentag.MONTAG.id : _uZeitrasterWochentagMin;
		_uZeitrasterWochentagMax = (_uZeitrasterWochentagMax == WOCHENTAG_INF_NEG) ? Wochentag.MONTAG.id : _uZeitrasterWochentagMax;
		_uZeitrasterStundeMin = (_uZeitrasterStundeMin == STUNDE_INF_POS) ? 1 : _uZeitrasterStundeMin;
		_uZeitrasterStundeMax = (_uZeitrasterStundeMax == STUNDE_INF_NEG) ? 1 : _uZeitrasterStundeMax;
		_uZeitrasterStundeMinOhneLeere = (_uZeitrasterStundeMinOhneLeere == STUNDE_INF_POS) ? 1 : _uZeitrasterStundeMinOhneLeere;
		_uZeitrasterStundeMaxOhneLeere = (_uZeitrasterStundeMaxOhneLeere == STUNDE_INF_NEG) ? 1 : _uZeitrasterStundeMaxOhneLeere;

		// _zeitrasterStundenRange
		_uZeitrasterStundenRange = new int[_uZeitrasterStundeMax - _uZeitrasterStundeMin + 1];
		for (int i = 0; i < _uZeitrasterStundenRange.length; i++)
			_uZeitrasterStundenRange[i] = _uZeitrasterStundeMin + i;

		// _uZeitrasterStundenRangeOhneLeere
		_uZeitrasterStundenRangeOhneLeere = new int[_uZeitrasterStundeMaxOhneLeere - _uZeitrasterStundeMinOhneLeere + 1];
		for (int i = 0; i < _uZeitrasterStundenRangeOhneLeere.length; i++)
			_uZeitrasterStundenRangeOhneLeere[i] = _uZeitrasterStundeMinOhneLeere + i;

		// _zeitrasterWochentageAlsEnumRange
		_uZeitrasterWochentageAlsEnumRange = new Wochentag[_uZeitrasterWochentagMax - _uZeitrasterWochentagMin + 1];
		for (int i = 0; i < _uZeitrasterWochentageAlsEnumRange.length; i++)
			_uZeitrasterWochentageAlsEnumRange[i] = Wochentag.fromIDorException(_uZeitrasterWochentagMin + i);
	}

	private void updateIteriereKlassenunterricht() {
		_uKlassenunterrichtByLehrerId.clear();
		_uKlassenunterrichtBySchuelerId.clear();

		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : _list_klassenunterricht) {
			// Der Klassenunterricht des Lehrer.
			for (final @NotNull Long idLehrer : klassenunterricht.lehrer)
				MapUtils.getOrCreateArrayList(_uKlassenunterrichtByLehrerId, idLehrer).add(klassenunterricht);

			// Der Klassenunterricht des Schülers
			for (final @NotNull Long idSchueler : klassenunterricht.schueler)
				MapUtils.getOrCreateArrayList(_uKlassenunterrichtBySchuelerId, idSchueler).add(klassenunterricht);
		}

	}

	private void updateIteriereKurs() {
		_uKursMapBySchuelerId.clear();
		_uKursMapByLehrerId.clear();
		_uKursMapByKlasseId.clear();

		for (final @NotNull StundenplanKurs kurs : _list_kurse) {
			// Die Kurse des Lehrers.
			for (final @NotNull Long idLehrer : kurs.lehrer)
				MapUtils.getOrCreateArrayList(_uKursMapByLehrerId, idLehrer).add(kurs);

			// Die Kurse des Schülers.
			for (final @NotNull Long idSchueler : kurs.schueler) {
				MapUtils.getOrCreateArrayList(_uKursMapBySchuelerId, idSchueler).add(kurs);
				// Die Kurse der Klasse, aggregiert über die Klasse des Schülers.
				final @NotNull StundenplanSchueler schueler = DeveloperNotificationException.ifMapGetIsNull(_map_schuelerID_zu_schueler, idSchueler);
				if ((schueler.idKlasse > 0) && (!MapUtils.getOrCreateArrayList(_uKursMapByKlasseId, schueler.idKlasse).contains(kurs)))
					MapUtils.getOrCreateArrayList(_uKursMapByKlasseId, schueler.idKlasse).add(kurs);
			}
		}

	}

	private void updateIteriereUnterricht() {
		// Initialisierungen
		_uUnterrichtHatMultiWochen = false;

		for (final @NotNull StundenplanUnterricht u : _list_unterricht)
			if (u.wochentyp > 0) {
				_uUnterrichtHatMultiWochen = true;
				break;
			}

	}

	// #####################################################################
	// #################### StundenplanAufsichtsbereich ####################
	// #####################################################################

	private void aufsichtsbereichAddOhneUpdate(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		aufsichtsbereichCheck(aufsichtsbereich);

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
		_list_aufsichtsbereiche.sort(_compAufsichtsbereich);
		update();
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
		_list_aufsichtsbereiche.sort(_compAufsichtsbereich);
		update();
	}

	// #####################################################################
	// #################### StundenplanAufsichtsbereich ####################
	// #####################################################################

	private static void aufsichtsbereichCheck(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsichtsbereich.id);
		DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsichtsbereich.kuerzel);
		// aufsicht.beschreibung darf "blank" sein
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
	 * Aktualisiert das vorhandene {@link StundenplanAufsichtsbereich}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanAufsichtsbereich#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanAufsichtsbereich#beschreibung}
	 * <br>{@link StundenplanAufsichtsbereich#kuerzel}
	 *
	 * @param aufsichtsbereich  Das neue {@link StundenplanAufsichtsbereich}-Objekt, dessen Attribute kopiert werden.
	 */
	public void aufsichtsbereichPatchAttributes(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		aufsichtsbereichCheck(aufsichtsbereich);

		final @NotNull StundenplanAufsichtsbereich old = DeveloperNotificationException.ifMapGetIsNull(_map_idAufsichtsbereich_zu_aufsichtsbereich, aufsichtsbereich.id);
		old.beschreibung = aufsichtsbereich.beschreibung;
		old.kuerzel = aufsichtsbereich.kuerzel;

		_list_aufsichtsbereiche.sort(_compAufsichtsbereich);
		update();
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
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @param listAufsichtsbereich  Die Liste der zu entfernenden {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public void aufsichtsbereichRemoveAll(final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich) {
		for (final @NotNull StundenplanAufsichtsbereich aufsichtsbereich : listAufsichtsbereich)
			aufsichtsbereichRemoveOhneUpdateById(aufsichtsbereich.id);
		update();
	}

	private void fachAddOhneUpdate(final @NotNull StundenplanFach fach) {
		fachCheck(fach);

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
		_list_faecher.sort(_compFach);
		update();
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
		_list_faecher.sort(_compFach);
		update();
	}

	private static void fachCheck(final @NotNull StundenplanFach fach) {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
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

	private void jahrgangAddOhneUpdate(final @NotNull StundenplanJahrgang jahrgang) {
		jahrgangCheck(jahrgang);

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
		_list_jahrgaenge.sort(_compJahrgang);
		update();
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
		_list_jahrgaenge.sort(_compJahrgang);
		update();
	}

	private static void jahrgangCheck(final @NotNull StundenplanJahrgang jahrgang) {
		DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
		DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
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
	 * Aktualisiert das vorhandene {@link StundenplanJahrgang}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanJahrgang#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanJahrgang#bezeichnung}
	 * <br>{@link StundenplanJahrgang#kuerzel}
	 *
	 * @param jahrgang  Das neue {@link StundenplanJahrgang}-Objekt, dessen Attribute kopiert werden.
	 */
	public void jahrgangPatchAttributes(final @NotNull StundenplanJahrgang jahrgang) {
		jahrgangCheck(jahrgang);

		final @NotNull StundenplanJahrgang old = DeveloperNotificationException.ifMapGetIsNull(_map_idJahrgang_zu_jahrgang, jahrgang.id);
		old.bezeichnung = jahrgang.bezeichnung;
		old.kuerzel = jahrgang.kuerzel;

		_list_jahrgaenge.sort(_compJahrgang);
		update();
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
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanJahrgang}-Objekte.
	 *
	 * @param listJahrgang  Die Liste der zu entfernenden {@link StundenplanJahrgang}-Objekte.
	 */
	public void jahrgangRemoveAll(final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang) {
		for (final @NotNull StundenplanJahrgang jahrgang : listJahrgang)
			jahrgangRemoveOhneUpdateById(jahrgang.id);
		update();
	}

	private void kalenderwochenzuordnungAddOhneUpdate(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungCheck(kwz);

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
		_list_kwz.sort(_compKWZ);
		update();
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
		_list_kwz.sort(_compKWZ);
		update();
	}

	private void kalenderwochenzuordnungCheck(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		DeveloperNotificationException.ifTrue("kwz.id < -1", kwz.id < -1);
		DeveloperNotificationException.ifTrue("(kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR)", (kwz.jahr < DateUtils.MIN_GUELTIGES_JAHR) || (kwz.jahr > DateUtils.MAX_GUELTIGES_JAHR));
		DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr))", (kwz.kw < 1) || (kwz.kw > DateUtils.gibKalenderwochenOfJahr(kwz.jahr)));
		DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > _stundenplanWochenTypModell);
		DeveloperNotificationException.ifTrue("kwz.wochentyp < 0", kwz.wochentyp < 0); // // kwz.wochentyp darf 0 sein, wegen der Pseudomenge!
	}

	private void kalenderwochenzuordnungErzeugePseudoMenge() {
		final @NotNull int[] infoVon = DateUtils.extractFromDateISO8601(_stundenplanGueltigAb);
		final @NotNull int[] infoBis = DateUtils.extractFromDateISO8601(_stundenplanGueltigBis);
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
		if (_stundenplanWochenTypModell == 0)
			return 0;

		// Fall: Eine Zuordnung ist definiert.
		final StundenplanKalenderwochenzuordnung z = _map2d_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z != null)
			return z.wochentyp;

		// Default: Der Wert berechnet sich modulo der Kalenderwoche.
		final int wochentyp = kalenderwoche % _stundenplanWochenTypModell;
		return wochentyp == 0 ? _stundenplanWochenTypModell : wochentyp; // 0 wird zu stundenplanWochenTypModell rotiert!
	}

	/**
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" den Wochentyp verwendet wird.
	 * <br>Hinweis: Das Mapping muss existieren UND {@link #_stundenplanWochenTypModell} muss mindestens 2 sein.
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
		return (_stundenplanWochenTypModell >= 2) && (z != null);
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKalenderwochenzuordnung}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKalenderwochenzuordnung#jahr}
	 * <br>{@link StundenplanKalenderwochenzuordnung#kw}
	 * <br>{@link StundenplanKalenderwochenzuordnung#wochentyp}
	 *
	 * @param kwz  Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, dessen Attribute kopiert werden.
	 */
	public void kalenderwochenzuordnungPatchAttributes(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		kalenderwochenzuordnungCheck(kwz);

		final @NotNull StundenplanKalenderwochenzuordnung old = DeveloperNotificationException.ifMapGetIsNull(_map_idKWZ_zu_kwz, kwz.id);

		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_jahr_kw_zu_kwz, old.jahr, old.kw);
		old.jahr = kwz.jahr;
		old.kw = kwz.kw;
		old.wochentyp = kwz.wochentyp;
		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, old);

		_list_kwz.sort(_compKWZ);
		update();
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
	 *
	 * @param idKWZ  Die Datenbank-ID des {@link StundenplanKalenderwochenzuordnung}-Objekts, welches entfernt werden soll.
	 */
	public void kalenderwochenzuordnungRemoveById(final long idKWZ) {
		kalenderwochenzuordnungRemoveOhneUpdateById(idKWZ);
		update();
	}

	/**
	 * Entfernt ein {@link StundenplanKalenderwochenzuordnung}-Objekt anhand der Parameter (jahr, kalenderwoche).
	 *
	 * @param jahr           Das Jahr der Kalenderwoche.
	 * @param kalenderwoche  Die gewünschten Kalenderwoche.
	 */
	public void kalenderwochenzuordnungRemoveByJahrAndKW(final int jahr, final int kalenderwoche) {
		kalenderwochenzuordnungRemoveOhneUpdateByJahrAndKW(jahr, kalenderwoche);
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @param listKWZ  Die Liste der zu entfernenden {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public void kalenderwochenzuordnungRemoveAll(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ) {
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : listKWZ)
			kalenderwochenzuordnungRemoveOhneUpdateById(kwz.id);
		update();
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
		update();
	}

	private void klasseAddOhneUpdate(final @NotNull StundenplanKlasse klasse) {
		klasseCheck(klasse);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idKlasse_zu_klasse, klasse.id, klasse);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKlasse_zu_klassenunterricht, klasse.id, new ArrayList<>());
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKlasse_zu_unterrichtmenge, klasse.id, new ArrayList<>());
		DeveloperNotificationException.ifListAddsDuplicate("_list_klassen", _list_klassen, klasse);
	}

	/**
	 * Fügt ein {@link StundenplanKlasse}-Objekt hinzu.
	 *
	 * @param klasse  Das {@link StundenplanKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public void klasseAdd(final @NotNull StundenplanKlasse klasse) {
		klasseAddOhneUpdate(klasse);
		_list_klassen.sort(_compKlasse);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanKlasse}-Objekte hinzu.
	 *
	 * @param listKlasse  Die Menge der {@link StundenplanKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public void klasseAddAll(final @NotNull List<@NotNull StundenplanKlasse> listKlasse) {
		for (final @NotNull StundenplanKlasse klasse : listKlasse)
			klasseAddOhneUpdate(klasse);
		_list_klassen.sort(_compKlasse);
		update();
	}

	private static void klasseCheck(final @NotNull StundenplanKlasse klasse) {
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
		// klasse.bezeichnung darf "blank" sein
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
	 * Aktualisiert das vorhandene {@link StundenplanKlasse}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKlasse#id}
	 * <br>{@link StundenplanKlasse#jahrgaenge}
	 * <br>{@link StundenplanKlasse#schueler}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKlasse#bezeichnung}
	 * <br>{@link StundenplanKlasse#kuerzel}
	 *
	 * @param klasse  Das neue {@link StundenplanKlasse}-Objekt, dessen Attribute kopiert werden.
	 */
	public void klassePatchAttributes(final @NotNull StundenplanKlasse klasse) {
		klasseCheck(klasse);

		final @NotNull StundenplanKlasse old = DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klasse, klasse.id);
		old.bezeichnung = klasse.bezeichnung;
		old.kuerzel = klasse.kuerzel;

		_list_klassen.sort(_compKlasse);
		update();
	}

	private void klasseRemoveOhneUpdateById(final long idKlasse) {
		// Kaskade: StundenplanUnterricht (des Kurses)
		final @NotNull List<@NotNull StundenplanKlassenunterricht> listKU = DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klassenunterricht, idKlasse);
		final @NotNull List<@NotNull StundenplanKlassenunterricht> listKU2 = new ArrayList<>(listKU); // Wichtig, wegen "concurrent modifications"
		for (final @NotNull StundenplanKlassenunterricht u : listKU2)
			klassenunterrichtRemoveOhneUpdateById(u.idKlasse, u.idFach);

		// Get
		final @NotNull StundenplanKlasse k = DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klasse, idKlasse);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKlasse_zu_klasse, idKlasse);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKlasse_zu_klassenunterricht, idKlasse);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKlasse_zu_unterrichtmenge, idKlasse);
		DeveloperNotificationException.ifListRemoveFailes("_list_klasse", _list_klassen, k);
	}

	/**
	 * Entfernt ein {@link StundenplanKlasse}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID des {@link StundenplanKlasse}-Objekts, welches entfernt werden soll.
	 */
	public void klasseRemoveById(final long idKlasse) {
		klasseRemoveOhneUpdateById(idKlasse);
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanKlasse}-Objekte.
	 *
	 * @param listKlasse  Die Liste der zu entfernenden {@link StundenplanKlasse}-Objekte.
	 */
	public void klasseRemoveAll(final @NotNull List<@NotNull StundenplanKlasse> listKlasse) {
		for (final @NotNull StundenplanKlasse klasse : listKlasse)
			klasseRemoveOhneUpdateById(klasse.id);
		update();
	}

	private void klassenunterrichtAddOhneUpdate(final @NotNull StundenplanKlassenunterricht klassenunterricht) {
		klassenunterrichtCheck(klassenunterricht);

		DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klassenunterricht, klassenunterricht.idKlasse).add(klassenunterricht);
		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_idKlasse_idFach_zu_klassenunterricht, klassenunterricht.idKlasse, klassenunterricht.idFach, klassenunterricht);
		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_idKlasse_idFach_zu_unterrichtmenge, klassenunterricht.idKlasse, klassenunterricht.idFach, new ArrayList<>());
		DeveloperNotificationException.ifListAddsDuplicate("_list_klassenunterricht", _list_klassenunterricht, klassenunterricht);
	}

	/**
	 * Fügt ein {@link StundenplanKlassenunterricht}-Objekt hinzu.
	 *
	 * @param klassenunterricht  Das {@link StundenplanKlassenunterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void klassenunterrichtAdd(final @NotNull StundenplanKlassenunterricht klassenunterricht) {
		klassenunterrichtAddOhneUpdate(klassenunterricht);
		_list_klassenunterricht.sort(_compKlassenunterricht);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanKlassenunterricht}-Objekte hinzu.
	 *
	 * @param listKlassenunterricht  Die Menge der {@link StundenplanKlassenunterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	private void klassenunterrichtAddAll(@NotNull final List<@NotNull StundenplanKlassenunterricht> listKlassenunterricht) {
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : listKlassenunterricht)
			klassenunterrichtAddOhneUpdate(klassenunterricht);
		_list_klassenunterricht.sort(_compKlassenunterricht);
		update();
	}

	private void klassenunterrichtCheck(final @NotNull StundenplanKlassenunterricht klassenunterricht) {
		DeveloperNotificationException.ifMapNotContains("_map_idKlasse_zu_klasse", _map_idKlasse_zu_klasse, klassenunterricht.idKlasse);
		DeveloperNotificationException.ifMapNotContains("_map_idFach_zu_fach", _map_idFach_zu_fach, klassenunterricht.idFach);
		for (final @NotNull Long idSchiene : klassenunterricht.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_idSchiene_zu_schiene", _map_idSchiene_zu_schiene, idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeAsList() {
		return _list_klassenunterricht;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte der Klasse.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeByKlasseIdAsList(final long idKlasse) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klassenunterricht, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Lehrers.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param idLehrer  Die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Lehrers.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeByLehrerIdAsList(final long idLehrer) {
		return MapUtils.getOrCreateArrayList(_uKlassenunterrichtByLehrerId, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Schülers.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanKlassenunterricht}-Objekte des Schülers.
	 */
	public @NotNull List<@NotNull StundenplanKlassenunterricht> klassenunterrichtGetMengeBySchuelerIdAsList(final long idSchueler) {
		return MapUtils.getOrCreateArrayList(_uKlassenunterrichtBySchuelerId, idSchueler);
	}

	/**
	 * Liefert die IST-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 * <br>Hinweis: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Laufzeit: O(|Unterrichte des Klassenunterricht|)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die IST-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 */
	public double klassenunterrichtGetWochenstundenIst(final long idKlasse, final long idFach) {
		double summe = 0;
		final double faktor = (_stundenplanWochenTypModell == 0) ? 1 : _stundenplanWochenTypModell;

		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMap2DGetIsNull(_map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		for (final @NotNull  StundenplanUnterricht u : listU)
			summe += (u.wochentyp == 0) ? faktor : 1;

		return summe / faktor;
	}

	/**
	 * Liefert die SOLL-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 *
	 * @return die SOLL-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 */
	public int klassenunterrichtGetWochenstundenSoll(final long idKlasse, final long idFach) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_map2d_idKlasse_idFach_zu_klassenunterricht, idKlasse, idFach).wochenstunden;
	}

	private void klassenunterrichtRemoveOhneUpdateById(final long idKlasse, final long idFach) {
		// Kaskade: StundenplanUnterricht (des Klassenunterrichts)
		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMap2DGetIsNull(_map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		final @NotNull List<@NotNull StundenplanUnterricht> listU2 = new ArrayList<>(listU); // Wichtig, wegen "concurrent modifications"
		for (final @NotNull StundenplanUnterricht u : listU2)
			unterrichtRemoveByIdOhneUpdate(u.id);

		// Get
		final @NotNull StundenplanKlassenunterricht klassenunterricht = DeveloperNotificationException.ifMap2DGetIsNull(_map2d_idKlasse_idFach_zu_klassenunterricht, idKlasse, idFach);

		// Entfernen
		DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_klassenunterricht, idKlasse).remove(klassenunterricht);
		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_idKlasse_idFach_zu_klassenunterricht, idKlasse, idFach);
		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		DeveloperNotificationException.ifListRemoveFailes("_list_klassenunterricht", _list_klassenunterricht, klassenunterricht);
	}

	/**
	 * Entfernt ein {@link StundenplanKlassenunterricht}-Objekt anhand seiner ID.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 * @param idFach    Die Datenbank-ID des Faches.
	 */
	public void klassenunterrichtRemoveById(final long idKlasse, final long idFach) {
		klassenunterrichtRemoveOhneUpdateById(idKlasse, idFach);
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanKlassenunterricht}-Objekte.
	 *
	 * @param listKlassenunterricht  Die Liste der zu entfernenden {@link StundenplanKlassenunterricht}-Objekte.
	 */
	public void klassenunterrichtRemoveAll(final @NotNull List<@NotNull StundenplanKlassenunterricht> listKlassenunterricht) {
		for (final @NotNull StundenplanKlassenunterricht klassenunterricht : listKlassenunterricht)
			klassenunterrichtRemoveOhneUpdateById(klassenunterricht.idKlasse, klassenunterricht.idFach);
		update();
	}

	private void kursAddOhneUpdate(final @NotNull StundenplanKurs kurs) {
		kursCheck(kurs);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idKurs_zu_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idKurs_zu_unterrichtmenge, kurs.id, new ArrayList<>());
		DeveloperNotificationException.ifListAddsDuplicate("_list_kurse", _list_kurse, kurs);
	}

	/**
	 * Fügt ein {@link StundenplanKurs}-Objekt hinzu.
	 *
	 * @param kurs  Das {@link StundenplanKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public void kursAdd(final @NotNull StundenplanKurs kurs) {
		kursAddOhneUpdate(kurs);
		_list_kurse.sort(_compKurs);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanKurs}-Objekte hinzu.
	 *
	 * @param listKurs  Die Menge der {@link StundenplanKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public void kursAddAll(final @NotNull List<@NotNull StundenplanKurs> listKurs) {
		for (final @NotNull  StundenplanKurs kurs : listKurs)
			kursAddOhneUpdate(kurs);
		_list_kurse.sort(_compKurs);
		update();
	}

	private void kursCheck(final @NotNull StundenplanKurs kurs) {
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
		for (final @NotNull Long idSchieneDesKurses : kurs.schienen)
			DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", _map_idSchiene_zu_schiene, idSchieneDesKurses);
		for (final @NotNull Long idJahrgangDesKurses : kurs.jahrgaenge)
			DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_idJahrgang_zu_jahrgang, idJahrgangDesKurses);
		for (final @NotNull Long idSchuelerDesKurses : kurs.schueler)
			DeveloperNotificationException.ifMapNotContains("_map_schuelerID_zu_schueler", _map_schuelerID_zu_schueler, idSchuelerDesKurses);
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
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param idKlasse  Die Datenbank-ID der Klasse.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte der Klasse.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeByKlasseIdAsList(final long idKlasse) {
		return MapUtils.getOrCreateArrayList(_uKursMapByKlasseId, idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param idLehrer  Die Datenbank-ID des Lehrers.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte des Lehrers.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeByLehrerIdAsList(final long idLehrer) {
		return MapUtils.getOrCreateArrayList(_uKursMapByLehrerId, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKurs}-Objekte des Schülers.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Liste aller {@link StundenplanKurs}-Objekte des Schülers.
	 */
	public @NotNull List<@NotNull StundenplanKurs> kursGetMengeBySchuelerIdAsList(final long idSchueler) {
		return MapUtils.getOrCreateArrayList(_uKursMapBySchuelerId, idSchueler);
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
	 * Liefert die IST-Wochenstunden des {@link StundenplanKurs}.
	 * <br>Hinweis: Durch AB-Wochen, ist der Rückgabewert eine Kommazahl, da nur Stundenanteile gesetzt sein können.
	 * <br>Laufzeit: O(|Unterrichte des Kurses|)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die IST-Wochenstunden des {@link StundenplanKlassenunterricht}.
	 */
	public double kursGetWochenstundenIst(final long idKurs) {
		double summe = 0;
		final double faktor = (_stundenplanWochenTypModell == 0) ? 1 : _stundenplanWochenTypModell;

		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, idKurs);
		for (final @NotNull  StundenplanUnterricht u : listU)
			summe += (u.wochentyp == 0) ? faktor : 1;

		return summe / faktor;
	}

	/**
	 * Liefert die Wochenstunden des Kurses.
	 * <br>Laufzeit: O(1)
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Wochenstunden des Kurses.
	 */
	public int kursGetWochenstundenSoll(final long idKurs) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_kurs, idKurs).wochenstunden;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanKurs}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanKurs#id}
	 * <br>{@link StundenplanKurs#jahrgaenge}
	 * <br>{@link StundenplanKurs#schienen}
	 * <br>{@link StundenplanKurs#schueler}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanKurs#bezeichnung}
	 * <br>{@link StundenplanKurs#wochenstunden}
	 *
	 * @param kurs  Das neue {@link StundenplanKurs}-Objekt, dessen Attribute kopiert werden.
	 */
	public void kursPatchAttributtes(final @NotNull StundenplanKurs kurs) {
		kursCheck(kurs);

		final @NotNull StundenplanKurs old = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_kurs, kurs.id);
		old.bezeichnung = kurs.bezeichnung;
		old.wochenstunden = kurs.wochenstunden;

		_list_kurse.sort(_compKurs);
		update();
	}

	private void kursRemoveOhneUpdateById(final long idKurs) {
		// Kaskade: StundenplanUnterricht (des Kurses)
		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, idKurs);
		final @NotNull List<@NotNull StundenplanUnterricht> listU2 = new ArrayList<>(listU); // Wichtig, wegen "concurrent modifications"
		for (final @NotNull StundenplanUnterricht u : listU2)
			unterrichtRemoveByIdOhneUpdate(u.id);

		// Get
		final @NotNull StundenplanKurs kurs = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_kurs, idKurs);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_zu_kurs, kurs.id);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idKurs_zu_unterrichtmenge, kurs.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_kurse", _list_kurse, kurs);
	}

	/**
	 * Entfernt ein {@link StundenplanKurs}-Objekt anhand seiner ID.
	 *
	 * @param idKurs  Die Datenbank-ID des {@link StundenplanKurs}-Objekts, welches entfernt werden soll.
	 */
	public void kursRemoveById(final long idKurs) {
		kursRemoveOhneUpdateById(idKurs);
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanKurs}-Objekte.
	 *
	 * @param listKurs  Die Liste der zu entfernenden {@link StundenplanKurs}-Objekte.
	 */
	public void kursRemoveAll(final @NotNull List<@NotNull StundenplanKurs> listKurs) {
		for (final @NotNull StundenplanKurs kurs : listKurs)
			kursRemoveOhneUpdateById(kurs.id);
		update();
	}

	private void lehrerAddOhneUpdate(final @NotNull StundenplanLehrer lehrer) {
		lehrerCheck(lehrer);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idLehrer_zu_lehrer, lehrer.id, lehrer);
		DeveloperNotificationException.ifListAddsDuplicate("_list_lehrer", _list_lehrer, lehrer);
	}

	/**
	 * Fügt ein {@link StundenplanLehrer}-Objekt hinzu.
	 *
	 * @param lehrer  Das {@link StundenplanLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lehrerAdd(final @NotNull StundenplanLehrer lehrer) {
		lehrerAddOhneUpdate(lehrer);
		_list_lehrer.sort(_compLehrer);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanLehrer}-Objekte hinzu.
	 *
	 * @param listLehrer  Die Menge der {@link StundenplanLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lehrerAddAll(final @NotNull List<@NotNull StundenplanLehrer> listLehrer) {
		for (final @NotNull StundenplanLehrer lehrer : listLehrer)
			lehrerAddOhneUpdate(lehrer);
		_list_lehrer.sort(_compLehrer);
		update();
	}

	private static void lehrerCheck(final @NotNull StundenplanLehrer lehrer) {
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
		DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
		DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
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
	 * Aktualisiert das vorhandene {@link StundenplanLehrer}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanLehrer#id}
	 * <br>{@link StundenplanLehrer#faecher}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanLehrer#kuerzel}
	 * <br>{@link StundenplanLehrer#nachname}
	 * <br>{@link StundenplanLehrer#vorname}
	 *
	 * @param lehrer  Das neue {@link StundenplanLehrer}-Objekt, dessen Attribute kopiert werden.
	 */
	public void lehrerPatchAttributes(final @NotNull StundenplanLehrer lehrer) {
		lehrerCheck(lehrer);

		final @NotNull StundenplanLehrer old = DeveloperNotificationException.ifMapGetIsNull(_map_idLehrer_zu_lehrer, lehrer.id);
		old.kuerzel = lehrer.kuerzel;
		old.nachname = lehrer.nachname;
		old.vorname = lehrer.vorname;

		_list_lehrer.sort(_compLehrer);
		update();
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
	 *
	 * @param idLehrer  Die Datenbank-ID des {@link StundenplanLehrer}-Objekts, welches entfernt werden soll.
	 */
	public void lehrerRemoveById(final long idLehrer) {
		lehrerRemoveOhneUpdateById(idLehrer);
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanLehrer}-Objekte.
	 *
	 * @param listLehrer  Die Liste der zu entfernenden {@link StundenplanLehrer}-Objekte.
	 */
	public void lehrerRemoveAll(final @NotNull List<@NotNull StundenplanLehrer> listLehrer) {
		for (final @NotNull StundenplanLehrer lehrer : listLehrer)
			lehrerRemoveOhneUpdateById(lehrer.id);
		update();
	}

	private void pausenaufsichtAddOhneUpdate(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtCheck(pausenaufsicht);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idPausenaufsicht_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenaufsichtmenge, pausenaufsicht.idPausenzeit).add(pausenaufsicht);
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenaufsichten", _list_pausenaufsichten, pausenaufsicht);
	}

	/**
	 * Fügt ein {@link StundenplanPausenaufsicht}-Objekt hinzu.
	 *
	 * @param pausenaufsicht  Das {@link StundenplanPausenaufsicht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void pausenaufsichtAdd(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtAddOhneUpdate(pausenaufsicht);
		_list_pausenaufsichten.sort(_compPausenaufsicht);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanPausenaufsicht}-Objekte hinzu.
	 *
	 * @param listPausenaufsicht  Die Menge der {@link StundenplanPausenaufsicht}-Objekte, welche hinzugefügt werden soll.
	 */
	private void pausenaufsichtAddAll(final @NotNull List<@NotNull StundenplanPausenaufsicht> listPausenaufsicht) {
		for (final @NotNull StundenplanPausenaufsicht pausenaufsicht : listPausenaufsicht)
			pausenaufsichtAddOhneUpdate(pausenaufsicht);
		_list_pausenaufsichten.sort(_compPausenaufsicht);
		update();
	}

	private void pausenaufsichtCheck(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		DeveloperNotificationException.ifInvalidID("pausenaufsicht.id", pausenaufsicht.id);
		DeveloperNotificationException.ifMapNotContains("_map_idLehrer_zu_lehrer", _map_idLehrer_zu_lehrer, pausenaufsicht.idLehrer);
		DeveloperNotificationException.ifMapNotContains("_map_idPausenzeit_zu_pausenzeit", _map_idPausenzeit_zu_pausenzeit, pausenaufsicht.idPausenzeit);
		DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell)", (pausenaufsicht.wochentyp > 0) && (pausenaufsicht.wochentyp > _stundenplanWochenTypModell));
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 *
	 * @param idPausenaufsicht Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenaufsicht}-Objekt.
	 */
	public @NotNull StundenplanPausenaufsicht pausenaufsichtGetByIdOrException(final long idPausenaufsicht) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_idPausenaufsicht_zu_pausenaufsicht, idPausenaufsicht);
	}

	/**
	 * Liefert eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @return eine sortierte Liste aller {@link StundenplanPausenaufsicht}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeAsList() {
		return _list_pausenaufsichten;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenaufsicht}-Objekte eines bestimmten Wochentages.
	 */
	public @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichtGetMengeByWochentagOrEmptyList(final int wochentag) {
		return MapUtils.getOrCreateArrayList(_uPausenaufsichtMapByWochentag, wochentag);
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanPausenaufsicht}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanPausenaufsicht#id}
	 * <br>{@link StundenplanPausenaufsicht#bereiche}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanPausenaufsicht#idLehrer}
	 * <br>{@link StundenplanPausenaufsicht#idPausenzeit}
	 * <br>{@link StundenplanPausenaufsicht#wochentyp}
	 *
	 * @param pausenaufsicht  Das neue {@link StundenplanPausenaufsicht}-Objekt, dessen Attribute kopiert werden.
	 */
	public void pausenaufsichtPatchAttributes(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		pausenaufsichtCheck(pausenaufsicht);

		final @NotNull StundenplanPausenaufsicht old = DeveloperNotificationException.ifMapGetIsNull(_map_idPausenaufsicht_zu_pausenaufsicht, pausenaufsicht.id);
		old.idLehrer = pausenaufsicht.idLehrer;
		old.idPausenzeit = pausenaufsicht.idPausenzeit;
		old.wochentyp = pausenaufsicht.wochentyp;

		_list_pausenaufsichten.sort(_compPausenaufsicht);
		update();
	}

	private void pausenaufsichtRemoveOhneUpdateById(final long idPausenaufsicht) {
		// Get
		final @NotNull StundenplanPausenaufsicht pausenaufsicht = DeveloperNotificationException.ifMapGetIsNull(_map_idPausenaufsicht_zu_pausenaufsicht, idPausenaufsicht);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idPausenaufsicht_zu_pausenaufsicht, pausenaufsicht.id);
		DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenaufsichtmenge, pausenaufsicht.idPausenzeit).remove(pausenaufsicht);
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
		update();
	}

	private void pausenzeitAddOhneUpdate(final @NotNull StundenplanPausenzeit pausenzeit) {
		pausenzeitCheck(pausenzeit);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idPausenzeit_zu_pausenzeit, pausenzeit.id, pausenzeit);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idPausenzeit_zu_pausenaufsichtmenge, pausenzeit.id, new ArrayList<>());
		DeveloperNotificationException.ifListAddsDuplicate("_list_pausenzeiten", _list_pausenzeiten, pausenzeit);
	}

	/**
	 * Fügt ein {@link StundenplanPausenzeit}-Objekt hinzu.
	 *
	 * @param pausenzeit  Das {@link StundenplanPausenzeit}-Objekt, welches hinzugefügt werden soll.
	 */
	public void pausenzeitAdd(final @NotNull StundenplanPausenzeit pausenzeit) {
		pausenzeitAddOhneUpdate(pausenzeit);
		_list_pausenzeiten.sort(_compPausenzeit);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanPausenzeit}-Objekte hinzu.
	 *
	 * @param listPausenzeit  Die Menge der {@link StundenplanPausenzeit}-Objekte, welche hinzugefügt werden soll.
	 */
	public void pausenzeitAddAll(final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit) {
		for (final @NotNull StundenplanPausenzeit pausenzeit : listPausenzeit)
			pausenzeitAddOhneUpdate(pausenzeit);
		_list_pausenzeiten.sort(_compPausenzeit);
		update();
	}

	private static void pausenzeitCheck(final @NotNull StundenplanPausenzeit pausenzeit) {
		DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
		Wochentag.fromIDorException(pausenzeit.wochentag);
		if ((pausenzeit.beginn != null) && (pausenzeit.ende != null))
			DeveloperNotificationException.ifTrue("pausenzeit.beginn >= pausenzeit.ende", pausenzeit.beginn >= pausenzeit.ende);
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
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @param wochentag  Die ID des ENUMS {@link Wochentag}.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte eines bestimmten Wochentages, oder eine leere Liste.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeByWochentagOrEmptyList(final int wochentag) {
		return MapUtils.getOrCreateArrayList(_uPausenzeitMapByWochentag, wochentag);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 * <br> Laufzeit: O(1), da Referenz zu einer Liste.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte, die mindestens eine {@link StundenplanPausenaufsicht} beinhalten.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> pausenzeitGetMengeNichtLeereAsList() {
		return _uPausenzeitListNichtLeere;
	}

	/**
	 * Aktualisiert das vorhandene {@link StundenplanPausenzeit}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanPausenzeit#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanPausenzeit#beginn}
	 * <br>{@link StundenplanPausenzeit#bezeichnung}
	 * <br>{@link StundenplanPausenzeit#ende}
	 * <br>{@link StundenplanPausenzeit#wochentag}
	 *
	 * @param pausenzeit  Das neue {@link StundenplanPausenzeit}-Objekt, dessen Attribute kopiert werden.
	 */
	public void pausenzeitPatchAttributes(final @NotNull StundenplanPausenzeit pausenzeit) {
		pausenzeitCheck(pausenzeit);

		final @NotNull StundenplanPausenzeit old = DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, pausenzeit.id);
		old.beginn = pausenzeit.beginn;
		old.bezeichnung = pausenzeit.bezeichnung;
		old.ende = pausenzeit.ende;
		old.wochentag = pausenzeit.wochentag;

		_list_pausenzeiten.sort(_compPausenzeit);
		update();
	}

	private void pausenzeitRemoveOhneUpdateById(final long idPausenzeit) {
		// Get
		final @NotNull StundenplanPausenzeit pausenzeit = DeveloperNotificationException.ifMapGetIsNull(_map_idPausenzeit_zu_pausenzeit, idPausenzeit);

		// Entfernen
		DeveloperNotificationException.ifMapRemoveFailes(_map_idPausenzeit_zu_pausenzeit, pausenzeit.id);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idPausenzeit_zu_pausenaufsichtmenge, pausenzeit.id);
		DeveloperNotificationException.ifListRemoveFailes("_list_pausenzeiten", _list_pausenzeiten, pausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit  Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public void pausenzeitRemoveById(final long idPausenzeit) {
		pausenzeitRemoveOhneUpdateById(idPausenzeit);
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @param listPausenzeit  Die Liste der zu entfernenden {@link StundenplanPausenzeit}-Objekte.
	 */
	public void pausenzeitRemoveAll(final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit) {
		for (final @NotNull StundenplanPausenzeit pausenzeit : listPausenzeit)
			pausenzeitRemoveOhneUpdateById(pausenzeit.id);
		update();
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitGetMinutenMin() {
		return _uPausenzeitMinutenMin;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitGetMinutenMax() {
		return _uPausenzeitMinutenMax;
	}

	/**
	 * Liefert das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMin() {
		return _uPausenzeitUndZeitrasterMinutenMin;
	}

	/**
	 * Liefert das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Minimum aller nicht leeren {@link StundenplanPausenzeit#beginn}-Objekte und aller {@link StundenplanZeitraster#stundenbeginn}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMinOhneLeere() {
		return _uPausenzeitUndZeitrasterMinutenMinOhneLeere;
	}

	/**
	 * Liefert das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMax() {
		return _uPausenzeitUndZeitrasterMinutenMax;
	}

	/**
	 * Liefert das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 * <br>Laufzeit: O(1)
	 *
	 * @return das Maximum aller nicht leeren {@link StundenplanPausenzeit#ende}-Objekte und aller {@link StundenplanZeitraster#stundenende}-Objekte, oder 480 (8 Uhr) falls keines vorhanden ist.
	 */
	public int pausenzeitUndZeitrasterGetMinutenMaxOhneLeere() {
		return _uPausenzeitUndZeitrasterMinutenMaxOhneLeere;
	}

	private void raumAddOhneUpdate(final @NotNull StundenplanRaum raum) {
		raumCheck(raum);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idRaum_zu_raum, raum.id, raum);
		DeveloperNotificationException.ifListAddsDuplicate("_list_raeume", _list_raeume, raum);
	}

	/**
	 * Fügt ein {@link StundenplanRaum}-Objekt hinzu.
	 *
	 * @param raum  Das {@link StundenplanRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public void raumAdd(final @NotNull StundenplanRaum raum) {
		raumAddOhneUpdate(raum);
		_list_raeume.sort(_compRaum);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanRaum}-Objekte hinzu.
	 *
	 * @param listRaum  Die Menge der {@link StundenplanRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public void raumAddAll(final @NotNull List<@NotNull StundenplanRaum> listRaum) {
		for (final @NotNull StundenplanRaum raum : listRaum)
			raumAddOhneUpdate(raum);
		_list_raeume.sort(_compRaum);
		update();
	}

	private static void raumCheck(final @NotNull StundenplanRaum raum) {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
		// raum.beschreibung darf "blank" sein!
		DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
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
	 * Aktualisiert das vorhandene {@link StundenplanRaum}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanRaum#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanRaum#beschreibung}
	 * <br>{@link StundenplanRaum#groesse}
	 * <br>{@link StundenplanRaum#kuerzel}
	 *
	 * @param raum  Das neue {@link StundenplanRaum}-Objekt, dessen Attribute kopiert werden.
	 */
	public void raumPatchAttributes(final @NotNull StundenplanRaum raum) {
		raumCheck(raum);

		final @NotNull StundenplanRaum old = DeveloperNotificationException.ifMapGetIsNull(_map_idRaum_zu_raum, raum.id);
		old.beschreibung = raum.beschreibung;
		old.groesse = raum.groesse;
		old.kuerzel = raum.kuerzel;

		_list_raeume.sort(_compRaum);
		update();
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
	 *
	 * @param idRaum  Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public void raumRemoveById(final long idRaum) {
		raumRemoveOhneUpdateById(idRaum);
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum  Die Liste der zu entfernenden {@link StundenplanRaum}-Objekte.
	 */
	public void raumRemoveAll(final @NotNull List<@NotNull StundenplanRaum> listRaum) {
		for (final @NotNull StundenplanRaum raum : listRaum)
			raumRemoveOhneUpdateById(raum.id);
		update();
	}

	private void schieneAddOhneUpdate(final @NotNull StundenplanSchiene schiene) {
		schieneCheck(schiene);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idSchiene_zu_schiene, schiene.id, schiene);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schienen", _list_schienen, schiene);
	}

	/**
	 * Fügt ein {@link StundenplanSchiene}-Objekt hinzu.
	 *
	 * @param schiene  Das {@link StundenplanSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schieneAdd(final @NotNull StundenplanSchiene schiene) {
		schieneAddOhneUpdate(schiene);
		_list_schienen.sort(_compSchiene);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanSchiene}-Objekte hinzu.
	 *
	 * @param listSchiene  Die Menge der {@link StundenplanSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schieneAddAll(final @NotNull List<@NotNull StundenplanSchiene> listSchiene) {
		for (final @NotNull StundenplanSchiene schiene : listSchiene)
			schieneAddOhneUpdate(schiene);
		_list_schienen.sort(_compSchiene);
		update();
	}

	private void schieneCheck(final @NotNull StundenplanSchiene schiene) {
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
		DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
		DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_idJahrgang_zu_jahrgang, schiene.idJahrgang);
	}

	private void schuelerAddOhneUpdate(final @NotNull StundenplanSchueler schueler) {
		schuelerCheck(schueler);

		DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_zu_schueler, schueler.id, schueler);
		DeveloperNotificationException.ifListAddsDuplicate("_list_schueler", _list_schueler, schueler);
	}

	/**
	 * Fügt ein {@link StundenplanSchueler}-Objekt hinzu.
	 *
	 * @param schueler  Das {@link StundenplanSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schuelerAdd(final @NotNull StundenplanSchueler schueler) {
		schuelerAddOhneUpdate(schueler);
		_list_schueler.sort(_compSchueler);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanSchueler}-Objekte hinzu.
	 *
	 * @param listSchueler  Die Menge der {@link StundenplanSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schuelerAddAll(final @NotNull List<@NotNull StundenplanSchueler> listSchueler) {
		for (final @NotNull StundenplanSchueler schueler : listSchueler)
			schuelerAddOhneUpdate(schueler);
		_list_schueler.sort(_compSchueler);
		update();
	}

	private void schuelerCheck(final @NotNull StundenplanSchueler schueler) {
		DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
		DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
		DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
		DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", _map_idKlasse_zu_klasse, schueler.idKlasse);
	}

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public long getIDSchuljahresabschnitt() {
		return _stundenplanSchuljahresAbschnittID;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigAb() {
		return _stundenplanGueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigBis() {
		return _stundenplanGueltigBis;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public @NotNull String getBezeichnungStundenplan() {
		return _stundenplanBezeichnung;
	}

	/**
	 * Liefert das (globale) Wochentyp-Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Ungültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 * <br>Laufzeit: O(1)
	 *
	 * @return das (globale) Wochentyp-Modell für die Wochen des Stundenplans.
	 */
	public int getWochenTypModell() {
		return _stundenplanWochenTypModell;
	}

	/**
	 * Liefert die Datenbank-ID des Schülers.<br>
	 * Wirft eine Exception, falls in den Daten nicht genau ein Schüler geladen wurde.
	 *
	 * @return  Die Datenbank-ID des Schülers.
	 */
	public long schuelerGetIDorException() {
		final int size = _list_schueler.size();
		DeveloperNotificationException.ifTrue("getSchuelerID() geht nicht bei " + size + " Schülern!", size != 1);
		return _list_schueler.get(0).id;
	}

	/**
	 * Liefert das (globale) Wochentyp-Modell für die Wochen des Stundenplans. <br>
	 * 0: Stundenplan gilt jede Woche. <br>
	 * 1: Ungültiger Wert. <br>
	 * N: Stundenplan wiederholt sich alle N Wochen. <br>
	 * <br>Laufzeit: O(1)
	 *
	 * @return das (globale) Wochentyp-Modell für die Wochen des Stundenplans.
	 */
	public int stundenplanGetWochenTypModell() {
		return _stundenplanWochenTypModell;
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
		return _stundenplanID;
	}

	private void unterrichtAddOhneUpdate(final @NotNull StundenplanUnterricht u) {
		unterrichtCheck(u);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idUnterricht_zu_unterricht, u.id, u);
		DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_unterrichtmenge, u.idZeitraster).add(u);
		Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, u.idZeitraster, u.wochentyp).add(u);
		for (final @NotNull Long idLehrkraftDesUnterrichts : u.lehrer) {
			final @NotNull StundenplanLehrer lehrer = DeveloperNotificationException.ifMapGetIsNull(_map_idLehrer_zu_lehrer, idLehrkraftDesUnterrichts);
			MapUtils.getOrCreateArrayList(_map_idUnterricht_zu_lehrermenge, u.id).add(lehrer);
		}
		if (u.idKurs != null) {
			// Kursunterricht
			final @NotNull List<@NotNull StundenplanUnterricht> unterrichtKurs = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListAddsDuplicate("unterrichtKurs", unterrichtKurs, u);
		} else {
			// Klassenunterricht
			for (final @NotNull Long idKlasse : u.klassen) {
				final @NotNull List<@NotNull StundenplanUnterricht> unterrichtKlasseFach = Map2DUtils.getOrCreateArrayList(_map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, u.idFach);
				DeveloperNotificationException.ifListAddsDuplicate("unterrichtKlasseFach", unterrichtKlasseFach, u);
				final @NotNull List<@NotNull StundenplanUnterricht> unterrichtKlasse = DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_unterrichtmenge, idKlasse);
				DeveloperNotificationException.ifListAddsDuplicate("unterrichtKL", unterrichtKlasse, u);
			}
		}
		_list_unterricht.add(u);
	}

	/**
	 * Fügt ein {@link StundenplanUnterricht}-Objekt hinzu.
	 *
	 * @param unterricht  Das {@link StundenplanUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void unterrichtAdd(final @NotNull StundenplanUnterricht unterricht) {
		unterrichtAddOhneUpdate(unterricht);
		_list_unterricht.sort(_compUnterricht);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanUnterricht}-Objekte hinzu.
	 *
	 * @param listUnterricht  Die Menge der {@link StundenplanUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public void unterrichtAddAll(final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {
		for (final @NotNull StundenplanUnterricht unterricht : listUnterricht)
			unterrichtAddOhneUpdate(unterricht);
		_list_unterricht.sort(_compUnterricht);
		update();
	}

	private void unterrichtCheck(final @NotNull StundenplanUnterricht u) {
		DeveloperNotificationException.ifInvalidID("u.id", u.id);
		DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", _map_idZeitraster_zu_zeitraster, u.idZeitraster);
		DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > _stundenplanWochenTypModell);
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
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndWochentyp(final long idKlasse, final int wochentyp) {
		// Überprüfen
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > _stundenplanWochenTypModell);

		// Filtern
		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_unterrichtmenge, idKlasse);
		return CollectionUtils.toFilteredArrayList(listU, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse in einer bestimmten Kalenderwoche.
	 *
	 * @param idKlasse       Die Datenbank-ID der Klasse.
	 * @param jahr           Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche  Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte einer Klasse in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndJahrAndKW(final long idKlasse, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKlasseIdAndWochentyp(idKlasse, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) mit einem bestimmten Wochentyp.
	 *
	 * @param idKlasse   Die Datenbank-ID der Klasse.
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param wochentyp  Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(final long idKlasse, final long idFach, final int wochentyp) {
		// Überprüfen
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > _stundenplanWochenTypModell);

		// Filtern
		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMap2DGetIsNull(_map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, idFach);
		return CollectionUtils.toFilteredArrayList(listU, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) in einer bestimmten Kalenderwoche.
	 *
	 * @param idKlasse       Die Datenbank-ID der Klasse.
	 * @param idFach         Die Datenbank-ID des Faches.
	 * @param jahr           Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche  Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht}-Objekte eines Klassenunterrichts (Klasse, Fach) in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeByKlasseIdAndFachIdAndJahrAndKW(final long idKlasse, final long idFach, final int jahr, final int kalenderwoche) {
		final int wochentyp = kalenderwochenzuordnungGetWochentypOrDefault(jahr, kalenderwoche);
		return unterrichtGetMengeByKlasseIdAndFachIdAndWochentyp(idKlasse, idFach, wochentyp);
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
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > _stundenplanWochenTypModell);

		// Filtern
		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, idkurs);
		return CollectionUtils.toFilteredArrayList(listU, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
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
		return _uUnterrichtHatMultiWochen;
	}

	private void unterrichtRemoveByIdOhneUpdate(final long idUnterricht) {
		// Get
		final @NotNull StundenplanUnterricht u = DeveloperNotificationException.ifNull("_map_idUnterricht_zu_unterricht.get(" + idUnterricht + ")", _map_idUnterricht_zu_unterricht.get(idUnterricht));

		// Remove
		DeveloperNotificationException.ifMapRemoveFailes(_map_idUnterricht_zu_unterricht, u.id);
		DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_unterrichtmenge, u.idZeitraster).remove(u);
		Map2DUtils.getOrCreateArrayList(_map2d_idZeitraster_wochentyp_zu_unterrichtmenge, u.idZeitraster, u.wochentyp).remove(u);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idUnterricht_zu_lehrermenge, u.id);
		if (u.idKurs != null) {
			// Kursunterricht
			final @NotNull List<@NotNull StundenplanUnterricht> unterrichtKurs = DeveloperNotificationException.ifMapGetIsNull(_map_idKurs_zu_unterrichtmenge, u.idKurs);
			DeveloperNotificationException.ifListRemoveFailes("unterrichtKurs", unterrichtKurs, u);
		} else {
			// Klassenunterricht
			for (final @NotNull Long idKlasse : u.klassen) {
				final @NotNull List<@NotNull StundenplanUnterricht> unterrichtKlasseFach = DeveloperNotificationException.ifMap2DGetIsNull(_map2d_idKlasse_idFach_zu_unterrichtmenge, idKlasse, u.idFach);
				DeveloperNotificationException.ifListRemoveFailes("unterrichtKlasseFach", unterrichtKlasseFach, u);
				final @NotNull List<@NotNull StundenplanUnterricht> unterrichtKlasse = DeveloperNotificationException.ifMapGetIsNull(_map_idKlasse_zu_unterrichtmenge, idKlasse);
				DeveloperNotificationException.ifListRemoveFailes("unterrichtKL", unterrichtKlasse, u);
			}
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
		update();
	}

	/**
	 * Entfernt alle {@link StundenplanUnterricht}-Objekte.
	 *
	 * @param listUnterricht  Die Liste der zu entfernenden {@link StundenplanUnterricht}-Objekte.
	 */
	public void unterrichtRemoveAll(final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {
		for (final @NotNull StundenplanUnterricht unterricht : listUnterricht)
			unterrichtRemoveByIdOhneUpdate(unterricht.id);
		update();
	}


	/**
	 * Liefert eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 * <br>Beispiel:  "08:00-8:45", falls sie nicht abweichen.
	 * <br>Beispiel:  "Mo-Mi 08:00-8:45", "Do 07:55-8:40", "Fr. 07:40-8:25", falls sie abweichen.
	 *
	 * @param stunde  Die Nr. der Unterrichtsstunde.
	 *
	 * @return eine String-Menge aller Uhrzeiten der Zeitraster einer bestimmten Unterrichtsstunde. Dabei werden identische Uhrzeiten zusammengefasst.
	 */
	public @NotNull List<@NotNull String> unterrichtsstundeGetUhrzeitenAsStrings(final int stunde) {
		final @NotNull List<@NotNull String> listUhrzeit = new ArrayList<>();
		final @NotNull List<@NotNull String> listWochentagVon = new ArrayList<>();
		final @NotNull List<@NotNull String> listWochentagBis = new ArrayList<>();

		for (int wochentag = _uZeitrasterWochentagMin; wochentag <= _uZeitrasterWochentagMax; wochentag++) {
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
				listUhrzeit.set(i, sWochentagVon + " " + sUhrzeit);
			else
				listUhrzeit.set(i, sWochentagVon + "-" + sWochentagBis + " " + sUhrzeit);
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
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zum übergebenen Wochentag.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getListZeitrasterZuWochentag(final @NotNull Wochentag wochentag) {
		return CollectionUtils.toFilteredArrayList(_list_zeitraster, (final @NotNull StundenplanZeitraster z) -> (wochentag.id == z.wochentag));
	}

	/**
	 * Liefert eine Liste der {@link StundenplanZeitraster}-Objekte zu einer bestimmten Unterrichtsstunde.
	 *
	 * @param unterrichtstunde   die Unterrichtsstunde der gewünschten Zeitraster-Objekte
	 *
	 * @return eine Liste aller {@link StundenplanZeitraster}-Objekte zur übergebenen Unterrichtsstunde.
	 */
	public @NotNull List<@NotNull StundenplanZeitraster> getListZeitrasterZuStunde(final int unterrichtstunde) {
		return CollectionUtils.toFilteredArrayList(_list_zeitraster, (final @NotNull StundenplanZeitraster z) -> (unterrichtstunde == z.unterrichtstunde));
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
		zeitrasterCheck(zeitraster);

		DeveloperNotificationException.ifMapPutOverwrites(_map_idZeitraster_zu_zeitraster, zeitraster.id, zeitraster);
		DeveloperNotificationException.ifMapPutOverwrites(_map_idZeitraster_zu_unterrichtmenge, zeitraster.id, new ArrayList<>());
		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_wochentag_stunde_zu_zeitraster, zeitraster.wochentag, zeitraster.unterrichtstunde, zeitraster);
		MapUtils.getOrCreateArrayList(_map_wochentag_zu_zeitrastermenge, zeitraster.wochentag).add(zeitraster);
		MapUtils.getOrCreateArrayList(_map_stunde_zu_zeitrastermenge, zeitraster.unterrichtstunde).add(zeitraster);
		_list_zeitraster.add(zeitraster);
	}

	/**
	 * Fügt ein {@link StundenplanZeitraster}-Objekt hinzu.
	 *
	 * @param zeitraster  Das {@link StundenplanZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public void zeitrasterAdd(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterAddOhneUpdate(zeitraster);
		_list_zeitraster.sort(_compZeitraster);
		update();
	}

	/**
	 * Fügt alle {@link StundenplanZeitraster}-Objekte hinzu.
	 *
	 * @param listZeitraster  Die Menge der {@link StundenplanZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public void zeitrasterAddAll(final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster) {
		for (final @NotNull StundenplanZeitraster zeitraster : listZeitraster)
			zeitrasterAddOhneUpdate(zeitraster);
		_list_zeitraster.sort(_compZeitraster);
		update();
	}

	private static void zeitrasterCheck(final @NotNull StundenplanZeitraster zeitraster) {
		DeveloperNotificationException.ifInvalidID("zeit.id", zeitraster.id);
		Wochentag.fromIDorException(zeitraster.wochentag);
		DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeitraster.unterrichtstunde < 0) || (zeitraster.unterrichtstunde > 29));
		if ((zeitraster.stundenbeginn != null) && (zeitraster.stundenende != null)) {
			final int beginn = zeitraster.stundenbeginn;
			final int ende = zeitraster.stundenende;
			DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
		}
	}

	/**
	 * Liefert den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int zeitrasterGetMinutenMin() {
		return _uZeitrasterMinutenMin;
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
		final Integer min = _uZeitrasterMinutenMinByStunde.get(stunde); // Beide Fälle von NULL können auftreten!
		return (min == null) ? 480 : min;
	}

	/**
	 * Liefert den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten Minuten-Wert aller Zeitraster, oder 480 (8 Uhr).
	 */
	public int zeitrasterGetMinutenMax() {
		return _uZeitrasterMinutenMax;
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
		final Integer max = _uZeitrasterMinutenMaxByStunde.get(stunde); // Beide Fälle von NULL können auftreten!
		return (max == null) ? 480 : max;
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMin() {
		return _uZeitrasterStundeMin;
	}

	/**
	 * Liefert die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die kleinste nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMinOhneLeere() {
		return _uZeitrasterStundeMinOhneLeere;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMax() {
		return _uZeitrasterStundeMax;
	}

	/**
	 * Liefert die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die größte nicht leere Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMaxOhneLeere() {
		return _uZeitrasterStundeMaxOhneLeere;
	}

	/**
	 * Liefert die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetWochentagMin() {
		return _uZeitrasterWochentagMin;
	}

	/**
	 * Liefert den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den kleinsten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public @NotNull Wochentag zeitrasterGetWochentagMinEnum() {
		return Wochentag.fromIDorException(_uZeitrasterWochentagMin);
	}

	/**
	 * Liefert die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return die ID des größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetWochentagMax() {
		return _uZeitrasterWochentagMax;
	}

	/**
	 * Liefert den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 * <br>Laufzeit: O(1)
	 *
	 * @return den größten {@link Wochentag} oder den Montag falls es keine Zeitraster gibt.
	 */
	public @NotNull Wochentag zeitrasterGetWochentagMaxEnum() {
		return Wochentag.fromIDorException(_uZeitrasterWochentagMax);
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
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 * @throws DeveloperNotificationException falls kein Zeitraster-Eintrag existiert
	 */
	public @NotNull StundenplanZeitraster zeitrasterGetByWochentagAndStundeOrException(final int wochentag, final int stunde) {
		return _map2d_wochentag_stunde_zu_zeitraster.getNonNullOrException(wochentag, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 *
	 * @param wochentag  Die ENUM-ID des {@link Wochentag} des gesuchten Zeitrasters.
	 * @param stunde     Die Unterrichtsstunde des gesuchten Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt, falls es existiert, sonst NULL.
	 */
	public StundenplanZeitraster zeitrasterGetByWochentagAndStundeOrNull(final int wochentag, final int stunde) {
		return _map2d_wochentag_stunde_zu_zeitraster.getOrNull(wochentag, stunde);
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
		return _uZeitrasterStundenRange;
	}

	/**
	 * Liefert alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle Zahlen von {@link #zeitrasterGetStundeMinOhneLeere()} bis {@link #zeitrasterGetStundeMaxOhneLeere()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten Unterrichtsstunden der nicht leeren {@link StundenplanZeitraster}.
	 */
	public @NotNull int @NotNull [] zeitrasterGetStundenRangeOhneLeere() {
		return _uZeitrasterStundenRangeOhneLeere;
	}

	/**
	 * Liefert alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 * Das Array beinhaltet alle {@link Wochentag}-Objekte von {@link #zeitrasterGetWochentagMin} bis {@link #zeitrasterGetWochentagMax()}.
	 * <br>Laufzeit: O(1), da Referenz auf ein Array.
	 *
	 * @return alle verwendeten sortierten {@link Wochentag}-Objekte der {@link StundenplanZeitraster}.
	 */
	public @NotNull Wochentag @NotNull [] zeitrasterGetWochentageAlsEnumRange() {
		return _uZeitrasterWochentageAlsEnumRange;
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
		for (int wochentyp = 1; wochentyp <= _stundenplanWochenTypModell; wochentyp++)
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
	 * Aktualisiert das vorhandene {@link StundenplanZeitraster}-Objekt durch das neue Objekt.
	 * <br>Die folgenden Attribute werden nicht aktualisiert:
	 * <br>{@link StundenplanZeitraster#id}
	 * <br>
	 * <br>Die folgenden Attribute werden kopiert:
	 * <br>{@link StundenplanZeitraster#stundenbeginn}
	 * <br>{@link StundenplanZeitraster#stundenende}
	 * <br>{@link StundenplanZeitraster#unterrichtstunde}
	 * <br>{@link StundenplanZeitraster#wochentag}
	 *
	 * @param zeitraster  Das neue {@link StundenplanZeitraster}-Objekt, dessen Attribute kopiert werden.
	 */
	public void zeitrasterPatchAttributes(final @NotNull StundenplanZeitraster zeitraster) {
		zeitrasterCheck(zeitraster);

		final @NotNull StundenplanZeitraster old = DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_zeitraster, zeitraster.id);

		DeveloperNotificationException.ifMap2DRemoveFailes(_map2d_wochentag_stunde_zu_zeitraster, old.wochentag, old.unterrichtstunde);
		MapUtils.removeFromListAndTrimOrException(_map_wochentag_zu_zeitrastermenge, old.wochentag, old);
		MapUtils.removeFromListAndTrimOrException(_map_stunde_zu_zeitrastermenge, old.unterrichtstunde, old);

		old.stundenbeginn = zeitraster.stundenbeginn;
		old.stundenende = zeitraster.stundenende;
		old.unterrichtstunde = zeitraster.unterrichtstunde;
		old.wochentag = zeitraster.wochentag;

		DeveloperNotificationException.ifMap2DPutOverwrites(_map2d_wochentag_stunde_zu_zeitraster, old.wochentag, old.unterrichtstunde, old);
		MapUtils.getOrCreateArrayList(_map_wochentag_zu_zeitrastermenge, old.wochentag).add(old);
		MapUtils.getOrCreateArrayList(_map_stunde_zu_zeitrastermenge, old.unterrichtstunde).add(old);

		_list_zeitraster.sort(_compZeitraster);
		update();
	}


	private void zeitrasterRemoveOhneUpdate(final long idZeitraster) {
		// Kaskade: StundenplanUnterricht
		final @NotNull List<@NotNull StundenplanUnterricht> listU = DeveloperNotificationException.ifMapGetIsNull(_map_idZeitraster_zu_unterrichtmenge, idZeitraster);
		final @NotNull List<@NotNull StundenplanUnterricht> listU2 = new ArrayList<>(listU); // Wichtig, wegen "concurrent modification".
		for (final @NotNull StundenplanUnterricht u : listU2)
			unterrichtRemoveByIdOhneUpdate(u.id);

		// Get
		final @NotNull StundenplanZeitraster z = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + idZeitraster + ")", _map_idZeitraster_zu_zeitraster.get(idZeitraster));

		// Remove
		DeveloperNotificationException.ifMapRemoveFailes(_map_idZeitraster_zu_zeitraster, idZeitraster);
		DeveloperNotificationException.ifMapRemoveFailes(_map_idZeitraster_zu_unterrichtmenge, idZeitraster);
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
		update();
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
		update();
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

}
