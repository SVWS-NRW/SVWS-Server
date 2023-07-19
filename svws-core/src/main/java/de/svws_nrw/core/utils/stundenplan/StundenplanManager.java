package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
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
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Daten eines Stundenplanes. Die Daten werden aus vier DTO-Objekten aggregiert.
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanManager {

	/** Ein Comparator für die Räume. */
	private static final @NotNull Comparator<@NotNull StundenplanRaum> _compRaum = (final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;

		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die Pausenzeiten. */
	private static final @NotNull Comparator<@NotNull StundenplanPausenzeit> _compPausenzeit = (final @NotNull StundenplanPausenzeit a, final @NotNull StundenplanPausenzeit b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;

		final int beginnA = a.beginn == null ? -1 : a.beginn;
		final int beginnB = b.beginn == null ? -1 : b.beginn;
		if (beginnA < beginnB) return -1;
		if (beginnA > beginnB) return +1;

		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die Aufsichtsbereiche. */
	private static final @NotNull Comparator<@NotNull StundenplanAufsichtsbereich> _compAufsichtsbereich = (final @NotNull StundenplanAufsichtsbereich a, final @NotNull StundenplanAufsichtsbereich b) -> {
		final int result = a.kuerzel.compareTo(b.kuerzel);
		if (result != 0) return result;

		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die Pausenaufsichten. */
	private final @NotNull Comparator<@NotNull StundenplanPausenaufsicht> _compPausenaufsicht = (final @NotNull StundenplanPausenaufsicht a, final @NotNull StundenplanPausenaufsicht b) -> Long.compare(a.id, b.id);

	/** Ein Comparator für die Zeitraster. */
	private static final @NotNull Comparator<@NotNull StundenplanZeitraster> _compZeitraster = (final @NotNull StundenplanZeitraster a, final @NotNull StundenplanZeitraster b) -> {
		if (a.wochentag < b.wochentag) return -1;
		if (a.wochentag > b.wochentag) return +1;

		if (a.unterrichtstunde < b.unterrichtstunde) return -1;
		if (a.unterrichtstunde > b.unterrichtstunde) return +1;

		return Long.compare(a.id, b.id);
	};

	/** Ein Comparator für die StundenplanKalenderwochenzuordnung. */
	private static final @NotNull Comparator<@NotNull StundenplanKalenderwochenzuordnung> _compKWZ = (final @NotNull StundenplanKalenderwochenzuordnung a, final @NotNull StundenplanKalenderwochenzuordnung b) -> {
		if (a.jahr < b.jahr) return -1;
		if (a.jahr > b.jahr) return +1;

		if (a.kw < b.kw) return -1;
		if (a.kw > b.kw) return +1;

		if (a.wochentyp < b.wochentyp) return -1;
		if (a.wochentyp > b.wochentyp) return +1;

		return Long.compare(a.id, b.id);
	};

	// Maps
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanFach> _map_fachID_zu_fach = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKlasse> _map_klasseID_zu_klasse = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanJahrgang> _map_jahrgangID_zu_jahrgang = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanLehrer> _map_lehrerID_zu_lehrer = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchueler> _map_schuelerID_zu_schueler = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKurs> _map_kursID_zu_kurs = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanZeitraster> _map_zeitrasterID_zu_zeitraster = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanZeitraster> _map_wochentag_stunde_zu_zeitraster = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenzeit> _map_pausenzeitID_zu_pausenzeit = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanRaum> _map_raumID_zu_raum = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchiene> _map_schieneID_zu_schiene = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanAufsichtsbereich> _map_aufsichtsbereichID_zu_aufsichtsbereich = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKalenderwochenzuordnung> _map_kwzID_zu_kwz = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanKalenderwochenzuordnung> _map_jahr_kw_zu_kwz = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_kursID_zu_unterrichte = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenaufsicht> _map_pausenaufsichtID_zu_pausenaufsicht = new HashMap<>();

	// Listen
	private final @NotNull List<@NotNull StundenplanPausenaufsicht> _list_pausenaufsichten = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanRaum> _list_raeume = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanZeitraster> _list_zeitraster = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanPausenzeit> _list_pausenzeiten = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanAufsichtsbereich> _list_aufsichtsbereiche = new ArrayList<>();
	private final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> _list_kalenderwochenzuordnungen = new ArrayList<>();

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

		// Spezialfall: unterrichtsverteilung ist NULL
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

		initKalenderwochenZuordnungen(listKWZ);        // ✔, referenziert ---
		initFaecher(listFach);                         // ✔, referenziert ---
		initJahrgaenge(listJahrgang);                  // ✔, referenziert ---
		initZeitraster(listZeitraster);                // ✔, referenziert ---
		initRaeume(listRaum);                          // ✔, referenziert ---
		initPausenzeiten(listPausenzeit);              // ✔, referenziert ---
		initAufsichtsbereiche(listAufsichtsbereich);   // ✔, referenziert ---
		initKlassen(listKlasse);                       // ✔, referenziert [Jahrgang], es gibt auch jahrgangsübergreifende Klassen!
		initLehrer(listLehrer);                        // ✔, referenziert [Fach]
		initSchueler(listSchueler);                    // ✔, referenziert Klasse
		initSchienen(listSchiene);                     // ✔, referenziert Jahrgang
		initPausenaufsichten(listPausenaufsicht);      // ✔, referenziert Lehrer, Pausenzeit, [Aufsichtsbereich]
		initKurse(listKurs);                           // ✔, referenziert [Schienen], [Jahrgang], [Schüler]
		initUnterrichte(listUnterricht);               // ✔, referenziert Zeitraster, Kurs, Fach, [Lehrer], [Klasse], [Raum], [Schiene]

	}

	private void initKalenderwochenZuordnungen(final @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> listKWZ) {
		// Löschen.
		_map_kwzID_zu_kwz.clear();
		_map_jahr_kw_zu_kwz.clear();
		_list_kalenderwochenzuordnungen.clear();

		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : listKWZ) {
			// Prüfen.
			DeveloperNotificationException.ifInvalidID("kw.id", kwz.id);
			DeveloperNotificationException.ifTrue("(kwz.jahr < 2000) || (kwz.jahr > 3000)", (kwz.jahr < 2000) || (kwz.jahr > 3000));
			DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > 53)", (kwz.kw < 1) || (kwz.kw > 53));
			DeveloperNotificationException.ifTrue("kwz.wochentyp > stundenplanWochenTypModell", kwz.wochentyp > stundenplanWochenTypModell);
			DeveloperNotificationException.ifTrue("kwz.wochentyp <= 0", kwz.wochentyp <= 0);

			// Hinzufügen.
			DeveloperNotificationException.ifMap2DPutOverwrites(_map_jahr_kw_zu_kwz, kwz.jahr, kwz.kw, kwz);
			DeveloperNotificationException.ifMapPutOverwrites(_map_kwzID_zu_kwz, kwz.id, kwz);
			_list_kalenderwochenzuordnungen.add(kwz);
		}

		// Sortieren.
		_list_kalenderwochenzuordnungen.sort(_compKWZ);
	}

	private void initFaecher(final @NotNull List<@NotNull StundenplanFach> listFach) {
		_map_fachID_zu_fach.clear();
		final @NotNull HashSet<@NotNull String> setFachKuerzel = new HashSet<>();
		for (final @NotNull StundenplanFach fach: listFach) {
			DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
			DeveloperNotificationException.ifStringIsBlank("fach.bezeichnung", fach.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("fach.kuerzel", fach.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setFachKuerzel", setFachKuerzel, fach.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(_map_fachID_zu_fach, fach.id, fach);
		}
	}

	private void initJahrgaenge(final @NotNull List<@NotNull StundenplanJahrgang> listJahrgang) {
		_map_jahrgangID_zu_jahrgang.clear();
		final @NotNull HashSet<@NotNull String> setJahrgangKuerzel = new HashSet<>();
		for (final @NotNull StundenplanJahrgang jahrgang : listJahrgang) {
			DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.bezeichnung", jahrgang.bezeichnung);
			DeveloperNotificationException.ifStringIsBlank("jahrgang.kuerzel", jahrgang.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setJahrgangKuerzel", setJahrgangKuerzel, jahrgang.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(_map_jahrgangID_zu_jahrgang, jahrgang.id, jahrgang);
		}
	}

	private void initLehrer(final @NotNull List<@NotNull StundenplanLehrer> listLehrer) {
		_map_lehrerID_zu_lehrer.clear();
		final @NotNull HashSet<@NotNull String> setLehrerKuerzel = new HashSet<>();
		for (final @NotNull StundenplanLehrer lehrer : listLehrer) {
			DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
			DeveloperNotificationException.ifStringIsBlank("lehrer.kuerzel", lehrer.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setLehrerKuerzel", setLehrerKuerzel, lehrer.kuerzel);
			DeveloperNotificationException.ifStringIsBlank("lehrer.nachname", lehrer.nachname);
			DeveloperNotificationException.ifStringIsBlank("lehrer.vorname", lehrer.vorname);
			DeveloperNotificationException.ifMapPutOverwrites(_map_lehrerID_zu_lehrer, lehrer.id, lehrer);

			// Konsistenz der Fächer der Lehrkraft überprüfen.
			final @NotNull HashSet<@NotNull Long> setFaecherDerLehrkraft = new HashSet<>();
			for (final @NotNull Long idFachDerLehrkraft : lehrer.faecher) {
				DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", _map_fachID_zu_fach, idFachDerLehrkraft);
				DeveloperNotificationException.ifSetAddsDuplicate("setFaecherDerLehrkraft", setFaecherDerLehrkraft, idFachDerLehrkraft);
			}
		}
	}

	private void initKlassen(final @NotNull List<@NotNull StundenplanKlasse> listKlasse) {
		_map_klasseID_zu_klasse.clear();
		final @NotNull HashSet<@NotNull String> setKlasseKuerzel = new HashSet<>();
		for (final @NotNull StundenplanKlasse klasse: listKlasse) {
			DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
			DeveloperNotificationException.ifStringIsBlank("klasse.kuerzel", klasse.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setKlasseKuerzel", setKlasseKuerzel, klasse.kuerzel);
			DeveloperNotificationException.ifMapPutOverwrites(_map_klasseID_zu_klasse, klasse.id, klasse);
			// klasse.bezeichnung darf "blank" sein

			// Konsistenz der Jahrgänge der Klasse überprüfen.
			final @NotNull HashSet<@NotNull Long> setJahrgaengeDerKlasse = new HashSet<>();
			for (final @NotNull Long idJahrgangDerKlasse : klasse.jahrgaenge) {
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_jahrgangID_zu_jahrgang, idJahrgangDerKlasse);
				DeveloperNotificationException.ifSetAddsDuplicate("setJahrgaengeDerKlasse", setJahrgaengeDerKlasse, idJahrgangDerKlasse);
			}
		}
	}

	private void initSchueler(final @NotNull List<@NotNull StundenplanSchueler> listSchueler) {
		_map_schuelerID_zu_schueler.clear();
		for (final @NotNull StundenplanSchueler schueler : listSchueler) {
			DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
			DeveloperNotificationException.ifStringIsBlank("schueler.nachname", schueler.nachname);
			DeveloperNotificationException.ifStringIsBlank("schueler.vorname", schueler.vorname);
			DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", _map_klasseID_zu_klasse, schueler.idKlasse);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schuelerID_zu_schueler, schueler.id, schueler);
		}
	}

	private void initSchienen(final @NotNull List<@NotNull StundenplanSchiene> listSchiene) {
		_map_schieneID_zu_schiene.clear();
		for (final @NotNull StundenplanSchiene schiene : listSchiene) {
			DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
			DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
			DeveloperNotificationException.ifStringIsBlank("schiene.bezeichnung", schiene.bezeichnung);
			DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_jahrgangID_zu_jahrgang, schiene.idJahrgang);
			DeveloperNotificationException.ifMapPutOverwrites(_map_schieneID_zu_schiene, schiene.id, schiene);
		}
	}

	private void initKurse(final @NotNull List<@NotNull StundenplanKurs> listKurs) {
		_map_kursID_zu_kurs.clear();
		for (final @NotNull StundenplanKurs kurs : listKurs) {
			DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
			DeveloperNotificationException.ifStringIsBlank("kurs.bezeichnung", kurs.bezeichnung);
			DeveloperNotificationException.ifMapPutOverwrites(_map_kursID_zu_kurs, kurs.id, kurs);

			// Konsistenz der Referenzen überprüfen.
			for (final @NotNull Long idSchieneDesKurses : kurs.schienen)
				DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", _map_schieneID_zu_schiene, idSchieneDesKurses);
			for (final @NotNull Long idJahrgangDesKurses : kurs.jahrgaenge)
				DeveloperNotificationException.ifMapNotContains("_map_jahrgangID_zu_jahrgang", _map_jahrgangID_zu_jahrgang, idJahrgangDesKurses);
			for (final @NotNull Long idSchuelerDesKurses : kurs.schueler)
				DeveloperNotificationException.ifMapNotContains("_map_schuelerID_zu_schueler", _map_schuelerID_zu_schueler, idSchuelerDesKurses);
		}
	}

	private void initZeitraster(final @NotNull List<@NotNull StundenplanZeitraster> listZeitraster) {
		// Löschen.
		_map_zeitrasterID_zu_zeitraster.clear();
		_map_wochentag_stunde_zu_zeitraster.clear();
		_list_zeitraster.clear();

		for (final @NotNull StundenplanZeitraster zeit : listZeitraster) {
			// Prüfen.
			DeveloperNotificationException.ifInvalidID("zeit.id", zeit.id);
			Wochentag.fromIDorException(zeit.wochentag);
			DeveloperNotificationException.ifTrue("(zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29)", (zeit.unterrichtstunde < 0) || (zeit.unterrichtstunde > 29));
			if ((zeit.stundenbeginn != null) && (zeit.stundenende != null)) {
				final int beginn = zeit.stundenbeginn;
				final int ende = zeit.stundenende;
				DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
			}
			// Hinzufügen
			DeveloperNotificationException.ifMapPutOverwrites(_map_zeitrasterID_zu_zeitraster, zeit.id, zeit);
			DeveloperNotificationException.ifMap2DPutOverwrites(_map_wochentag_stunde_zu_zeitraster, zeit.wochentag, zeit.unterrichtstunde, zeit);
			_list_zeitraster.add(zeit);
		}

		// Sortieren.
		_list_zeitraster.sort(_compZeitraster);
	}

	private void initRaeume(final @NotNull List<@NotNull StundenplanRaum> listRaum) {
		// Löschen.
		_map_raumID_zu_raum.clear();
		_list_raeume.clear();

		final @NotNull HashSet<@NotNull String> setRaumKuerzel = new HashSet<>();
		for (final @NotNull StundenplanRaum raum : listRaum) {
			// Prüfen.
			DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
			DeveloperNotificationException.ifStringIsBlank("raum.kuerzel", raum.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setRaumKuerzel", setRaumKuerzel, raum.kuerzel);
			// raum.beschreibung darf "blank" sein!
			DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);

			// Hinzufügen.
			DeveloperNotificationException.ifMapPutOverwrites(_map_raumID_zu_raum, raum.id, raum);
			_list_raeume.add(raum);
		}

		// Sortieren.
		_list_raeume.sort(_compRaum);
	}

	private void initPausenzeiten(final @NotNull List<@NotNull StundenplanPausenzeit> listPausenzeit) {
		// Löschen.
		_map_pausenzeitID_zu_pausenzeit.clear();
		_list_pausenzeiten.clear();

		for (final @NotNull StundenplanPausenzeit pausenzeit : listPausenzeit) {
			// Prüfen.
			DeveloperNotificationException.ifInvalidID("pause.id", pausenzeit.id);
			Wochentag.fromIDorException(pausenzeit.wochentag);
			if ((pausenzeit.beginn != null) && (pausenzeit.ende != null)) {
				final int beginn = pausenzeit.beginn;
				final int ende = pausenzeit.ende;
				DeveloperNotificationException.ifTrue("beginn >= ende", beginn >= ende);
			}
			// Hinzufügen.
			DeveloperNotificationException.ifMapPutOverwrites(_map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
			_list_pausenzeiten.add(pausenzeit);
		}

		// Sortieren.
		_list_pausenzeiten.sort(_compPausenzeit);
	}

	private void initAufsichtsbereiche(final @NotNull List<@NotNull StundenplanAufsichtsbereich> listAufsichtsbereich) {
		// Löschen.
		_map_aufsichtsbereichID_zu_aufsichtsbereich.clear();
		_list_aufsichtsbereiche.clear();

		final @NotNull HashSet<@NotNull String> setAufsichtKuerzel = new HashSet<>();
		for (final @NotNull StundenplanAufsichtsbereich aufsicht : listAufsichtsbereich) {
			// Prüfen.
			DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsicht.id);
			DeveloperNotificationException.ifStringIsBlank("aufsicht.kuerzel", aufsicht.kuerzel);
			DeveloperNotificationException.ifSetAddsDuplicate("setAufsichtKuerzel", setAufsichtKuerzel, aufsicht.kuerzel);
			// aufsicht.beschreibung darf "blank" sein

			// Hinzufügen.
			DeveloperNotificationException.ifMapPutOverwrites(_map_aufsichtsbereichID_zu_aufsichtsbereich, aufsicht.id, aufsicht);
			_list_aufsichtsbereiche.add(aufsicht);
		}

		// Sortieren.
		_list_aufsichtsbereiche.sort(_compAufsichtsbereich);
	}

	private void initUnterrichte(final @NotNull List<@NotNull StundenplanUnterricht> listUnterricht) {
		// Leere Listen pro Kurs-ID zuordnen.
		_map_kursID_zu_unterrichte.clear();
		for (final @NotNull Long idKurs : _map_kursID_zu_kurs.keySet())
			_map_kursID_zu_unterrichte.put(idKurs, new ArrayList<>());

		for (final @NotNull StundenplanUnterricht u : listUnterricht) {
			// Konsistenz der Attribute überprüfen.
			DeveloperNotificationException.ifInvalidID("u.id", u.id);
			DeveloperNotificationException.ifMapNotContains("_map_zeitrasterID_zu_zeitraster", _map_zeitrasterID_zu_zeitraster, u.idZeitraster);
			DeveloperNotificationException.ifTrue("u.wochentyp > stundenplanWochenTypModell", u.wochentyp > stundenplanWochenTypModell);
			DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0); // 0 ist erlaubt!

			// Ist es Kurs-Unterricht?
			if (u.idKurs != null) {
				DeveloperNotificationException.ifMapNotContains("_map_kursID_zu_kurs", _map_kursID_zu_kurs, u.idKurs);
				final @NotNull List<@NotNull StundenplanUnterricht> listDerUnterrichte = DeveloperNotificationException.ifMapGetIsNull(_map_kursID_zu_unterrichte, u.idKurs);
				DeveloperNotificationException.ifListAddsDuplicate("listDerUnterrichte", listDerUnterrichte, u);
			}

			DeveloperNotificationException.ifMapNotContains("_map_fachID_zu_fach", _map_fachID_zu_fach, u.idFach);
			for (final @NotNull Long idLehrkraftDesUnterrichts : u.lehrer)
				DeveloperNotificationException.ifMapNotContains("_map_lehrerID_zu_lehrer", _map_lehrerID_zu_lehrer, idLehrkraftDesUnterrichts);
			for (final @NotNull Long idKlasseDesUnterrichts : u.klassen)
				DeveloperNotificationException.ifMapNotContains("_map_klasseID_zu_klasse", _map_klasseID_zu_klasse, idKlasseDesUnterrichts);
			for (final @NotNull Long idRaumDesUnterrichts : u.raeume)
				DeveloperNotificationException.ifMapNotContains("_map_raumID_zu_raum", _map_raumID_zu_raum, idRaumDesUnterrichts);
			for (final @NotNull Long idSchieneDesUnterrichts : u.schienen)
				DeveloperNotificationException.ifMapNotContains("_map_schieneID_zu_schiene", _map_schieneID_zu_schiene, idSchieneDesUnterrichts);
		}
	}

	private void initPausenaufsichten(final @NotNull List<@NotNull StundenplanPausenaufsicht> listPausenaufsicht) {
		// Löschen.
		_map_pausenaufsichtID_zu_pausenaufsicht.clear();
		_list_pausenaufsichten.clear();

		for (final @NotNull StundenplanPausenaufsicht pa : listPausenaufsicht) {
			// Prüfen.
			DeveloperNotificationException.ifInvalidID("aufsicht.id", pa.id);
			DeveloperNotificationException.ifMapNotContains("_map_pausenzeitID_zu_pausenzeit", _map_pausenzeitID_zu_pausenzeit, pa.idPausenzeit);
			DeveloperNotificationException.ifMapNotContains("_map_lehrerID_zu_lehrer", _map_lehrerID_zu_lehrer, pa.idLehrer);
			DeveloperNotificationException.ifTrue("(pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell)", (pa.wochentyp > 0) && (pa.wochentyp > stundenplanWochenTypModell));

			final @NotNull HashSet<@NotNull Long> setBereicheDieserAufsicht = new HashSet<>();
			for (final @NotNull Long idAufsichtsbereich : pa.bereiche) {
				DeveloperNotificationException.ifMapNotContains("_map_aufsichtsbereichID_zu_aufsichtsbereich", _map_aufsichtsbereichID_zu_aufsichtsbereich, idAufsichtsbereich);
				DeveloperNotificationException.ifSetAddsDuplicate("setBereicheDieserAufsicht", setBereicheDieserAufsicht, idAufsichtsbereich);
			}

			// Hinzufügen.
			DeveloperNotificationException.ifMapPutOverwrites(_map_pausenaufsichtID_zu_pausenaufsicht, pa.id, pa);
			_list_pausenaufsichten.add(pa);
		}

		// Sortieren.
		_list_pausenaufsichten.sort(_compPausenaufsicht);
	}


	/**
	 * Liefert die ID des Stundenplans.
	 *
	 * @return die ID des Stundenplans.
	 */
	public long getID() {
		return stundenplanID;
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
	 * Liefert den zugeordneten Wochentyp, oder den Default-Wochentyp.
	 *
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return den zugeordneten Wochentyp, oder den Default-Wochentyp.
	 */
	public int getWochentypOrDefault(final int jahr, final int kalenderwoche) {
		DeveloperNotificationException.ifTrue("(jahr < 2000) || (jahr > 3000)", (jahr < 2000) || (jahr > 3000));
		DeveloperNotificationException.ifTrue("(kalenderwoche < 1) || (kalenderwoche > 53)", (kalenderwoche < 1) || (kalenderwoche > 53));
		if (stundenplanWochenTypModell == 0)
			return 0;
		final StundenplanKalenderwochenzuordnung z = _map_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z != null)
			return z.wochentyp;
		final int wochentyp = kalenderwoche % stundenplanWochenTypModell;
		return wochentyp == 0 ? stundenplanWochenTypModell : wochentyp;
	}

	/**
	 * Liefert TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" auf "Kalenderwoche" verwendet wird.
	 *
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return TRUE, falls intern ein Mapping von "Jahr, Kalenderwoche" auf "Kalenderwoche" verwendet wird.
	 */
	public boolean getWochentypUsesMapping(final int jahr, final int kalenderwoche) {
		DeveloperNotificationException.ifTrue("(jahr < 2000) || (jahr > 3000)", (jahr < 2000) || (jahr > 3000));
		DeveloperNotificationException.ifTrue("(kalenderwoche < 1) || (kalenderwoche > 53)", (kalenderwoche < 1) || (kalenderwoche > 53));
		final StundenplanKalenderwochenzuordnung z = _map_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		return (stundenplanWochenTypModell >= 2) && (z != null);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param idKkurs   Die ID des Kurses.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDesKursesByWochentyp(final long idKkurs, final int wochentyp) {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("wochentyp > stundenplanWochenTypModell", wochentyp > stundenplanWochenTypModell);
		final @NotNull List<@NotNull StundenplanUnterricht> list = DeveloperNotificationException.ifNull("_map_kursID_zu_unterrichte.get(kursID)==NULL", _map_kursID_zu_unterrichte.get(idKkurs));
		// Daten filtern.
		return CollectionUtils.toFilteredArrayList(list, (final @NotNull StundenplanUnterricht u) -> (u.wochentyp == 0) || (u.wochentyp == wochentyp));
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
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDesKursesByKW(final long idKurs, final int jahr, final int kalenderwoche) {
		final int wochentyp = getWochentypOrDefault(jahr, kalenderwoche);
		return getUnterrichtDesKursesByWochentyp(idKurs, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param idsKurs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDerKurseByWochentyp(final @NotNull long[] idsKurs, final int wochentyp) {
		// Daten filtern.
		final @NotNull ArrayList<@NotNull StundenplanUnterricht> result = new ArrayList<>();
		for (final long idKurs : idsKurs)
			result.addAll(getUnterrichtDesKursesByWochentyp(idKurs, wochentyp));
		return result;
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
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDerKurseByKW(final @NotNull long[] idsKurs, final int jahr, final int kalenderwoche) {
		final int wochentyp = getWochentypOrDefault(jahr, kalenderwoche);
		return getUnterrichtDerKurseByWochentyp(idsKurs, wochentyp);
	}

	/**
	 * Liefert alle Unterrichte des Schülers bezüglich einer bestimmten Stundenplanzelle.<br>
	 * Die Liste sollte 0 oder 1 Element (in der Regel) enthalten.
	 *
	 * @param idSchueler        Die Datenbank-ID des Schülers.
	 * @param wochentyp         Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag         Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde  Die gewünschte Unterrichtsstunde.
	 *
	 * @return alle Unterrichte des Schülers bezüglich einer bestimmten Stundenplanzelle.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> unterrichtGetMengeBySchuelerIDWochentypWochentagUnterrichtsstunde(final long idSchueler, final int wochentyp, final @NotNull Wochentag wochentag, final int unterrichtstunde) {
		return new ArrayList<>();
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
	public @NotNull List<@NotNull Long> getKurseGefiltert(final @NotNull List<@NotNull Long> idsKurs, final int wochentyp, final @NotNull Wochentag wochentag, final int unterrichtstunde) {
		return CollectionUtils.toFilteredArrayList(idsKurs, (final @NotNull Long idKurs) -> testKursHatUnterrichtAm(idKurs, wochentyp, wochentag, unterrichtstunde));
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
	public @NotNull List<@NotNull Long> getKurseGefiltertByDatum(final @NotNull List<@NotNull Long> idsKurs, final @NotNull String datumISO8601, final int unterrichtstunde) {
		final int[] e = DateUtils.extractFromDateISO8601(datumISO8601);
		final int wochentyp = getWochentypOrDefault(e[6], e[5]); // kalenderwochenjahr, kalenderwoche
		final @NotNull Wochentag wochentag = Wochentag.fromIDorException(e[3]); // tagInWoche
		return getKurseGefiltert(idsKurs, wochentyp, wochentag, unterrichtstunde);
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs am (Wochentyp / Wochentag / Unterrichtsstunde)  hat.
	 *
	 * @param idKurs           Die ID des Kurses.
	 * @param wochentyp        Der Typ der Woche (beispielsweise bei AB-Wochen).
	 * @param wochentag        Der gewünschte {@link Wochentag}.
	 * @param unterrichtstunde Die gewünschte Unterrichtsstunde.
	 *
	 * @return TRUE, falls der übergebene Kurs am (wochentyp / wochentag / Unterrichtsstunde)  hat.
	 */
	public boolean testKursHatUnterrichtAm(final long idKurs, final int wochentyp, final @NotNull Wochentag wochentag, final int unterrichtstunde) {
		for (final @NotNull StundenplanUnterricht u : getUnterrichtDesKursesByWochentyp(idKurs, wochentyp)) {
			final @NotNull StundenplanZeitraster z =  getZeitraster(u.idZeitraster);
			if ((z.wochentag == wochentag.id) && (z.unterrichtstunde == unterrichtstunde))
				return true;
		}
		return false;
	}

	/**
	 * Liefert eine Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich} für den aktuell ausgewählten Stundenplan.
	 *
	 * @return eine Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich}
	 */
	public @NotNull Map<@NotNull Long, @NotNull StundenplanAufsichtsbereich> getMapAufsichtsbereich() {
		return _map_aufsichtsbereichID_zu_aufsichtsbereich;
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
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanAufsichtsbereich> getListAufsichtbereich() {
		return _list_aufsichtsbereiche;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanKalenderwochenzuordnung}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanKalenderwochenzuordnung> getListKalenderwochenzuordnung() {
		return _list_kalenderwochenzuordnungen;
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
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur raumID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public @NotNull StundenplanRaum getRaum(final long idRaum) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_raumID_zu_raum, idRaum);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public @NotNull StundenplanPausenzeit getPausenzeit(final long idPausenzeit) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_pausenzeitID_zu_pausenzeit, idPausenzeit);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public @NotNull StundenplanAufsichtsbereich getAufsichtsbereich(final long idAufsichtsbereich) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_aufsichtsbereichID_zu_aufsichtsbereich, idAufsichtsbereich);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param idKWZ Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 */
	public @NotNull StundenplanKalenderwochenzuordnung getKalenderwochenzuordnung(final long idKWZ) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_kwzID_zu_kwz, idKWZ);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public @NotNull StundenplanZeitraster getZeitraster(final long idZeitraster) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_zeitrasterID_zu_zeitraster, idZeitraster);
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
	 */
	public @NotNull Wochentag getZeitrasterWochentagMin() {
		if (_list_zeitraster.isEmpty())
			return Wochentag.MONTAG;

		int min = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", _list_zeitraster).wochentag;
		for (@NotNull final StundenplanZeitraster z :_list_zeitraster)
			min = Math.min(min, z.wochentag);

		return Wochentag.fromIDorException(min);
	}

	/**
	 * Liefert den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 *
	 * @return den größten {@link Wochentag}, oder den Montag falls es keine Zeitraster gibt.
	 */
	public @NotNull Wochentag getZeitrasterWochentagMax() {
		if (_list_zeitraster.isEmpty())
			return Wochentag.MONTAG;

		int max = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", _list_zeitraster).wochentag;
		for (@NotNull final StundenplanZeitraster z :_list_zeitraster)
			max = Math.max(max, z.wochentag);

		return Wochentag.fromIDorException(max);
	}

	/**
	 * Liefert TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return TRUE, falls zu (wochentag, stunde) ein zugehöriges {@link StundenplanZeitraster}-Objekt existiert.
	 */
	public boolean testZeitrasterByWochentagStunde(final @NotNull Wochentag wochentag, final int stunde) {
		return _map_wochentag_stunde_zu_zeitraster.contains(wochentag.id, stunde);
	}

	/**
	 * Liefert das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param wochentag Der {@link Wochentag} des Zeitrasters.
	 * @param stunde    Die Unterrichtsstunde Zeitrasters.
	 *
	 * @return das zu (wochentag, stunde) zugehörige {@link StundenplanZeitraster}-Objekt.
	 */
	public @NotNull StundenplanZeitraster getZeitrasterByWochentagStunde(final @NotNull Wochentag wochentag, final int stunde) {
		return _map_wochentag_stunde_zu_zeitraster.getNonNullOrException(wochentag.id, stunde);
	}

	/**
	 * Liefert das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 *
	 * @param zeitraster Das aktuelle {@link StundenplanZeitraster}-Objekt.
	 *
	 * @return das {@link StundenplanZeitraster}-Objekt der nächsten Stunde am selben Wochentag.
	 */
	public @NotNull StundenplanZeitraster getZeitrasterNext(final @NotNull StundenplanZeitraster zeitraster) {
		return _map_wochentag_stunde_zu_zeitraster.getNonNullOrException(zeitraster.wochentag, zeitraster.unterrichtstunde + 1);
	}

	/**
	 * Liefert TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 * @param beginn1  Der Anfang (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param ende1    Das Ende (inklusive) des ersten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iBeginn2 Der Anfang (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 * @param iEnde2   Das Ende (inklusive) des zweiten Intervalls (in Minuten) seit 0 Uhr.
	 *
	 * @return TRUE, falls die Intervalle [beginn1, ende1] und [beginn2, ende2] sich schneiden.
	 */
	public boolean testIntervalleSchneidenSich(final int beginn1, final int ende1, final Integer iBeginn2, final Integer iEnde2) {
		final int beginn2 = DeveloperNotificationException.ifNull("zeitraster.stundenbeginn ist NULL!", iBeginn2);
		final int ende2 = DeveloperNotificationException.ifNull("zeitraster.stundenende ist NULL!", iEnde2);
		DeveloperNotificationException.ifTrue("beginn1 < 0", beginn1 < 0);
		DeveloperNotificationException.ifTrue("beginn2 < 0", beginn2 < 0);
		DeveloperNotificationException.ifTrue("beginn1 > ende1", beginn1 > ende1);
		DeveloperNotificationException.ifTrue("beginn2 > ende2", beginn2 > ende2);
		return !((ende1 < beginn2) || (ende2 < beginn1));
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanRaum} hinzu.
	 *
	 * @param raum Der Raum, der hinzugefügt werden soll.
	 */
	public void addRaum(final @NotNull StundenplanRaum raum) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_raumID_zu_raum, raum.id, raum);
		_list_raeume.add(raum);
		_list_raeume.sort(_compRaum);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenzeit} hinzu.
	 *
	 * @param pausenzeit Die Pausenzeit, die hinzugefügt werden soll.
	 */
	public void addPausenzeit(final @NotNull StundenplanPausenzeit pausenzeit) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
		_list_pausenzeiten.add(pausenzeit);
		_list_pausenzeiten.sort(_compPausenzeit);
	}

	/**
	 * Fügt dem Stundenplan einen neuen {@link StundenplanAufsichtsbereich} hinzu.
	 *
	 * @param aufsichtsbereich Der Aufsichtsbereich, der hinzugefügt werden soll.
	 */
	public void addAufsichtsbereich(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_aufsichtsbereichID_zu_aufsichtsbereich, aufsichtsbereich.id, aufsichtsbereich);
		_list_aufsichtsbereiche.add(aufsichtsbereich);
		_list_aufsichtsbereiche.sort(_compAufsichtsbereich);
	}

	/**
	 * Fügt dem Stundenplan eine neue {@link StundenplanKalenderwochenzuordnung} hinzu.
	 *
	 * @param kwz Die Kalenderwochenzuordnung, die hinzugefügt werden soll.
	 */
	public void addKalenderwochenzuordnung(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_kwzID_zu_kwz, kwz.id, kwz);
		_list_kalenderwochenzuordnungen.add(kwz);
		_list_kalenderwochenzuordnungen.sort(_compKWZ);
	}

	/**
	 * Fügt dem Stundenplan eine neues {@link StundenplanZeitraster} hinzu.
	 *
	 * @param zeitraster Das StundenplanZeitraster, das hinzugefügt werden soll.
	 */
	public void addZeitraster(final @NotNull StundenplanZeitraster zeitraster) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_zeitrasterID_zu_zeitraster, zeitraster.id, zeitraster);
		DeveloperNotificationException.ifMap2DPutOverwrites(_map_wochentag_stunde_zu_zeitraster, zeitraster.wochentag, zeitraster.unterrichtstunde, zeitraster);
		_list_zeitraster.add(zeitraster);
		_list_zeitraster.sort(_compZeitraster);
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierenden {@link StundenplanRaum}-Objekt.
	 *
	 * @param idRaum Die ID des {@link StundenplanRaum}-Objekts.
	 */
	public void removeRaum(final long idRaum) {
		final @NotNull StundenplanRaum raum = DeveloperNotificationException.ifNull("_map_raumID_zu_raum.get(" + idRaum + ")", _map_raumID_zu_raum.get(idRaum));
		_map_raumID_zu_raum.remove(idRaum);
		_list_raeume.remove(raum); // Neusortierung nicht nötig.
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param idPausenzeit Die ID des {@link StundenplanPausenzeit}-Objekts.
	 */
	public void removePausenzeit(final long idPausenzeit) {
		final @NotNull StundenplanPausenzeit pausenzeit = DeveloperNotificationException.ifNull("_map_pausenzeitID_zu_pausenzeit.get(" + idPausenzeit + ")", _map_pausenzeitID_zu_pausenzeit.get(idPausenzeit));
		_map_pausenzeitID_zu_pausenzeit.remove(idPausenzeit);
		_list_pausenzeiten.remove(pausenzeit); // Neusortierung nicht nötig.
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierendes {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param idAufsichtsbereich Die ID des {@link StundenplanAufsichtsbereich}-Objekts.
	 */
	public void removeAufsichtsbereich(final long idAufsichtsbereich) {
		final @NotNull StundenplanAufsichtsbereich aufsichtsbereich = DeveloperNotificationException.ifNull("_map_aufsichtID_zu_aufsicht.get(" + idAufsichtsbereich + ")", _map_aufsichtsbereichID_zu_aufsichtsbereich.get(idAufsichtsbereich));
		_map_aufsichtsbereichID_zu_aufsichtsbereich.remove(idAufsichtsbereich);
		_list_aufsichtsbereiche.remove(aufsichtsbereich); // Neusortierung nicht nötig.
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierendes {@link StundenplanKalenderwochenzuordnung}-Objekt.
	 *
	 * @param idKWZ Die ID des {@link StundenplanKalenderwochenzuordnung}-Objekts.
	 */
	public void removeKalenderwochenzuordnung(final long idKWZ) {
		final @NotNull StundenplanKalenderwochenzuordnung kwz = DeveloperNotificationException.ifNull("_map_kwzID_zu_kwz.get(" + idKWZ + ")", _map_kwzID_zu_kwz.get(idKWZ));
		_map_kwzID_zu_kwz.remove(idKWZ);
		_list_kalenderwochenzuordnungen.remove(kwz); // Neusortierung nicht nötig.
	}

	/**
	 * Entfernt aus dem Stundenplan ein existierendes {@link StundenplanZeitraster}-Objekt.
	 *
	 * @param idZeitraster Die ID des {@link StundenplanZeitraster}-Objekts.
	 */
	public void removeZeitraster(final long idZeitraster) {
		final @NotNull StundenplanZeitraster zr = DeveloperNotificationException.ifNull("_map_zeitrasterID_zu_zeitraster.get(" + idZeitraster + ")", _map_zeitrasterID_zu_zeitraster.get(idZeitraster));
		_map_wochentag_stunde_zu_zeitraster.removeOrException(zr.wochentag, zr.unterrichtstunde);
		_map_zeitrasterID_zu_zeitraster.remove(idZeitraster);
		_list_zeitraster.remove(zr);  // Neusortierung nicht nötig.
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

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanAufsichtsbereich}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param aufsichtsbereich Das neue {@link StundenplanAufsichtsbereich}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void patchAufsichtsbereich(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		removeAufsichtsbereich(aufsichtsbereich.id);
		addAufsichtsbereich(aufsichtsbereich);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanKalenderwochenzuordnung}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param kwz Das neue {@link StundenplanKalenderwochenzuordnung}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void patchKalenderwochenzuordnung(final @NotNull StundenplanKalenderwochenzuordnung kwz) {
		removeKalenderwochenzuordnung(kwz.id);
		addKalenderwochenzuordnung(kwz);
	}

	/**
	 * Entfernt anhand der ID das alte {@link StundenplanZeitraster}-Objekt und fügt dann das neue Objekt hinzu.
	 *
	 * @param zeitraster Das neue {@link StundenplanZeitraster}-Objekt, welches das alte Objekt ersetzt.
	 */
	public void patchZeitraster(final @NotNull StundenplanZeitraster zeitraster) {
		removeZeitraster(zeitraster.id);
		addZeitraster(zeitraster);
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
	 * Liefert das Fach mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return  das Fach mit der übergebenen ID.
	 */
	public @NotNull StundenplanFach fachGet(final long idFach) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_fachID_zu_fach, idFach);
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
	 * Fügt dem Stundenplan eine neue {@link StundenplanPausenaufsicht} hinzu und sortiert die Liste der Pausenaufsichten.
	 *
	 * @param pausenaufsicht Die StundenplanPausenaufsicht, die hinzugefügt werden soll.
	 */
	public void pausenaufsichtAdd(final @NotNull StundenplanPausenaufsicht pausenaufsicht) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_pausenaufsichtID_zu_pausenaufsicht, pausenaufsicht.id, pausenaufsicht);
		_list_pausenaufsichten.add(pausenaufsicht);
		_list_pausenaufsichten.sort(_compPausenaufsicht);
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
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int stundenplanGetWochenTypModell() {
		return stundenplanWochenTypModell;
	}

	/**
	 * Liefert die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @return die kleinste Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMin() {
		if (_list_zeitraster.isEmpty())
			return 1;

		int min = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", _list_zeitraster).unterrichtstunde;
		for (final @NotNull StundenplanZeitraster z :_list_zeitraster)
			min = Math.min(min, z.unterrichtstunde);

		return min;
	}

	/**
	 * Liefert die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 *
	 * @return die größte Stunde aller Zeitraster, oder 1 falls es keine Zeitraster gibt.
	 */
	public int zeitrasterGetStundeMax() {
		if (_list_zeitraster.isEmpty())
			return 1;

		int max = DeveloperNotificationException.ifListGetFirstFailes("_list_zeitraster", _list_zeitraster).unterrichtstunde;
		for (final @NotNull StundenplanZeitraster z :_list_zeitraster)
			max = Math.max(max, z.unterrichtstunde);

		return max;
	}

}
