package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
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
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Daten eines Stundenplanes. Die Daten werden aus vier DTO-Objekten aggregiert.
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanManager {

	private final @NotNull Stundenplan _daten;
	private final @NotNull List<@NotNull StundenplanUnterricht> _datenU;
	private final @NotNull StundenplanUnterrichtsverteilung _datenUV;

	// Mappings von DTO-StundenplanUnterrichtsverteilung.
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanFach> _map_fachID_zu_fach = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKlasse> _map_klasseID_zu_klasse = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull Long>> _map_klasseID_zu_jahrgangIDs = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanJahrgang> _map_jahrgangID_zu_jahrgang = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanLehrer> _map_lehrerID_zu_lehrer = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull Long>> _map_lehrerID_zu_faecherIDs = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchueler> _map_schuelerID_zu_schueler = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKurs> _map_kursID_zu_kurs = new HashMap<>();

	// Mappings von DTO-Stundenplan
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanZeitraster> _map_zeitrasterID_zu_zeitraster = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanRaum> _map_raumID_zu_raum = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchiene> _map_schieneID_zu_schiene = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanPausenzeit> _map_pausenzeitID_zu_pausenzeit = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanAufsichtsbereich> _map_aufsichtID_zu_aufsicht = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKalenderwochenzuordnung> _map_kwzID_zu_kwz = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanKalenderwochenzuordnung> _map_jahr_kw_zu_kwz = new HashMap2D<>();

	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_kursID_zu_unterrichte = new HashMap<>();

	/**
	 * Der {@link StundenplanManager} benötigt vier data-Objekte und baut damit eine Datenstruktur für schnelle Zugriffe auf.
	 *
	 * @param daten                 liefert die Grunddaten des Stundenplanes.
	 * @param unterrichte           liefert die Informationen zu allen {@link StundenplanUnterricht} im Stundenplan. Die Liste darf leer sein.
	 * @param pausenaufsichten      liefert die Informationen zu allen {@link StundenplanPausenaufsicht} im Stundenplan. Die Liste darf leer sein.
	 * @param unterrichtsverteilung liefert die Informationen zu der Unterrichtsverteilung eines Stundenplans. Darf NULL sein.
	 */
	public StundenplanManager(final @NotNull Stundenplan daten, final @NotNull List<@NotNull StundenplanUnterricht> unterrichte, final @NotNull List<@NotNull StundenplanPausenaufsicht> pausenaufsichten, final StundenplanUnterrichtsverteilung unterrichtsverteilung) {
		_daten = daten;
		_datenU = unterrichte;
		if (unterrichtsverteilung == null) {
			_datenUV = new StundenplanUnterrichtsverteilung();
			_datenUV.id = daten.id;
		} else {
			_datenUV = unterrichtsverteilung;
		}

		checkWochentypenKonsistenz();

		// Maps: DTO-StundenplanUnterrichtsverteilung.
		initMapFach();         // hat ---
		initMapJahrgang();     // hat ---
		initMapLehrer();       // hat "{Fach}"
		initMapKlasse();       // hat "{Jahrgang}", es gibt auch jahrgangsübergreifende Klassen!
		initMapSchueler();     // hat "Klasse"
		initMapKurs();         // hat "{Schienen}", "{Jahrgang}", "{Schüler}"

		// Maps: DTO-Stundenplan.
		initMapZeitraster();   // hat ---
		initMapRaum();         // hat ---
		initMapSchiene();      // hat "Jahrgang"
		initMapPausenzeit();   // hat ---
		initMapAufsicht();     // hat ---
		initMapKWZuordnung();


		// Maps: DTO-StundenplanUnterricht (DTO-StundenplanUnterrichtsverteilung muss vorher geladen werden)
		initMapKursZuUnterrichte();

	}

	private void checkWochentypenKonsistenz() {
		// Der globale Wochentyp darf nur 0 oder >= 2 sein.
		final int wochentyp = _daten.wochenTypModell;
		DeveloperNotificationException.ifTrue("_daten.wochenTypModell < 0", wochentyp < 0);
		DeveloperNotificationException.ifTrue("_daten.wochenTypModell == 1", wochentyp == 1);

		for (@NotNull final StundenplanKalenderwochenzuordnung z : _daten.kalenderwochenZuordnung) {
			// Der Wochentyp einer KW-Zuordnung muss >= 1 sein.
			DeveloperNotificationException.ifTrue("z.wochentyp <= 0", z.wochentyp <= 0);
			// Liegt die Zuordnung in einer bestimmten Woche, muss es so viele Wochen auch global geben.
			DeveloperNotificationException.ifTrue("z.wochentyp > wochentyp", z.wochentyp > wochentyp);
		}

		for (@NotNull final StundenplanUnterricht u : _datenU) {
			// Der Wochentyp eines Unterrichts muss >= 0 sein.
			DeveloperNotificationException.ifTrue("u.wochentyp < 0", u.wochentyp < 0);
			// Liegt der Unterricht in einer bestimmten Woche, muss es so viele Wochen auch global geben.
			DeveloperNotificationException.ifTrue("u.wochentyp > wochentyp", u.wochentyp > wochentyp);
		}
	}

	private void initMapFach() {
		_map_fachID_zu_fach.clear();
		for (final @NotNull StundenplanFach fach: _datenUV.faecher) {
			DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
			DeveloperNotificationException.ifTrue("fach.bezeichnung.isBlank()", fach.bezeichnung.isBlank());
			DeveloperNotificationException.ifTrue("fach.kuerzel.isBlank()", fach.kuerzel.isBlank());
			DeveloperNotificationException.ifMapContains("_map_fachID_zu_fach", _map_fachID_zu_fach, fach.id);
			_map_fachID_zu_fach.put(fach.id, fach);
		}
	}

	private void initMapKlasse() {
		_map_klasseID_zu_klasse.clear();
		_map_klasseID_zu_jahrgangIDs.clear();
		for (final @NotNull StundenplanKlasse klasse: _datenUV.klassen) {
			DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
			DeveloperNotificationException.ifTrue("klasse.bezeichnung.isBlank()", klasse.bezeichnung.isBlank());
			DeveloperNotificationException.ifTrue("klasse.kuerzel.isBlank()", klasse.kuerzel.isBlank());
			DeveloperNotificationException.ifMapContains("_map_klasseID_zu_klasse", _map_klasseID_zu_klasse, klasse.id);
			_map_klasseID_zu_klasse.put(klasse.id, klasse);

			// Jahrgänge der Klasse hinzufügen.
			@NotNull final ArrayList<@NotNull Long> listJ = new ArrayList<>();
			for (final @NotNull Long jahrgangID : klasse.jahrgaenge) {
				DeveloperNotificationException.ifTrue("!_map_jahrgangID_zu_jahrgang.containsKey(jahrgangID)", !_map_jahrgangID_zu_jahrgang.containsKey(jahrgangID));
				DeveloperNotificationException.ifTrue("jahrgaenge.contains(jahrgangID)", listJ.contains(jahrgangID));
				listJ.add(jahrgangID);
			}
			_map_klasseID_zu_jahrgangIDs.put(klasse.id, listJ);
		}
	}

	private void initMapJahrgang() {
		_map_jahrgangID_zu_jahrgang.clear();
		for (final @NotNull StundenplanJahrgang jahrgang: _datenUV.jahrgaenge) {
			DeveloperNotificationException.ifInvalidID("jahrgang.id", jahrgang.id);
			DeveloperNotificationException.ifTrue("jahrgang.bezeichnung.isBlank()", jahrgang.bezeichnung.isBlank());
			DeveloperNotificationException.ifTrue("jahrgang.kuerzel.isBlank()", jahrgang.kuerzel.isBlank());
			DeveloperNotificationException.ifMapContains("_map_jahrgangID_zu_jahrgang", _map_jahrgangID_zu_jahrgang, jahrgang.id);
			_map_jahrgangID_zu_jahrgang.put(jahrgang.id, jahrgang);
		}
	}

	private void initMapLehrer() {
		_map_lehrerID_zu_lehrer.clear();
		_map_lehrerID_zu_faecherIDs.clear();
		for (final @NotNull StundenplanLehrer lehrer : _datenUV.lehrer) {
			DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
			DeveloperNotificationException.ifTrue("zeit.kuerzel.isBlank()", lehrer.kuerzel.isBlank());
			DeveloperNotificationException.ifTrue("zeit.nachname.isBlank()", lehrer.nachname.isBlank());
			DeveloperNotificationException.ifTrue("zeit.vorname.isBlank()", lehrer.vorname.isBlank());
			DeveloperNotificationException.ifMapContains("_map_lehrerID_zu_lehrer", _map_lehrerID_zu_lehrer, lehrer.id);
			_map_lehrerID_zu_lehrer.put(lehrer.id, lehrer);

			// Fächer der Lehrkraft hinzufügen.
			@NotNull final ArrayList<@NotNull Long> listF = new ArrayList<>();
			for (final @NotNull Long fachID : lehrer.faecher) {
				DeveloperNotificationException.ifTrue("!_map_fachID_zu_fach.containsKey(fachID)", !_map_fachID_zu_fach.containsKey(fachID));
				DeveloperNotificationException.ifTrue("faecher.contains(fachID)", listF.contains(fachID));
				listF.add(fachID);
			}
			_map_lehrerID_zu_faecherIDs.put(lehrer.id, listF);
		}
	}

	private void initMapSchueler() {
		_map_schuelerID_zu_schueler.clear();
		for (final @NotNull StundenplanSchueler schueler : _datenUV.schueler) {
			DeveloperNotificationException.ifInvalidID("schueler.id", schueler.id);
			DeveloperNotificationException.ifInvalidID("schueler.idKlasse", schueler.idKlasse);
			DeveloperNotificationException.ifTrue("!_map_klasseID_zu_klasse.containsKey(schueler.idKlasse)", !_map_klasseID_zu_klasse.containsKey(schueler.idKlasse));
			DeveloperNotificationException.ifTrue("schueler.nachname.isBlank()", schueler.nachname.isBlank());
			DeveloperNotificationException.ifTrue("schueler.vorname.isBlank()", schueler.vorname.isBlank());
			DeveloperNotificationException.ifMapContains("_map_schuelerID_zu_schueler", _map_schuelerID_zu_schueler, schueler.id);
			_map_schuelerID_zu_schueler.put(schueler.id, schueler);
		}
	}

	private void initMapKurs() {
		DeveloperNotificationException.ifTrue("_daten.id != _datenUV.id", _daten.id != _datenUV.id);

		_map_kursID_zu_kurs.clear();
		for (final @NotNull StundenplanKurs kurs : _datenUV.kurse) {
			DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
			DeveloperNotificationException.ifTrue("kurs.bezeichnung.isBlank()", kurs.bezeichnung.isBlank());
			DeveloperNotificationException.ifMapContains("_map_kursID_zu_kurs", _map_kursID_zu_kurs, kurs.id);
			_map_kursID_zu_kurs.put(kurs.id, kurs);
		}
	}

	private void initMapZeitraster() {
		_map_zeitrasterID_zu_zeitraster.clear();
		for (final @NotNull StundenplanZeitraster zeit : _daten.zeitraster) {
			DeveloperNotificationException.ifInvalidID("zeit.id", zeit.id);
			DeveloperNotificationException.ifTrue("zeit.stundenbeginn.isBlank()", zeit.stundenbeginn.isBlank());
			DeveloperNotificationException.ifTrue("zeit.stundenende.isBlank()", zeit.stundenende.isBlank());
			DeveloperNotificationException.ifTrue("zeit.unterrichtstunde <= 0", zeit.unterrichtstunde <= 0);
			DeveloperNotificationException.ifTrue("(zeit.wochentag < 1) || (zeit.wochentag > 7)", (zeit.wochentag < 1) || (zeit.wochentag > 7));
			DeveloperNotificationException.ifTrue("_map_zeitrasterID_zu_zeitraster.containsKey(zeit.id)", _map_zeitrasterID_zu_zeitraster.containsKey(zeit.id));
			_map_zeitrasterID_zu_zeitraster.put(zeit.id, zeit);
		}
	}

	private void initMapRaum() {
		_map_raumID_zu_raum.clear();
		for (final @NotNull StundenplanRaum raum : _daten.raeume) {
			DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
			DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
			DeveloperNotificationException.ifTrue("raum.kuerzel.isBlank()", raum.kuerzel.isBlank());
			DeveloperNotificationException.ifMapContains("_map_raumID_zu_raum", _map_raumID_zu_raum, raum.id);
			_map_raumID_zu_raum.put(raum.id, raum);
		}

	}

	private void initMapSchiene() {
		_map_schieneID_zu_schiene.clear();
		for (final @NotNull StundenplanSchiene schiene : _daten.schienen) {
			DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
			DeveloperNotificationException.ifInvalidID("schiene.idJahrgang", schiene.idJahrgang);
			DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
			DeveloperNotificationException.ifTrue("schiene.bezeichnung.isBlank()", schiene.bezeichnung.isBlank());
			DeveloperNotificationException.ifMapContains("_map_schieneID_zu_schiene", _map_schieneID_zu_schiene, schiene.id);
			_map_schieneID_zu_schiene.put(schiene.id, schiene);
		}
	}

	private void initMapPausenzeit() {
		_map_pausenzeitID_zu_pausenzeit.clear();
		for (final @NotNull StundenplanPausenzeit pause : _daten.pausenzeiten) {
			DeveloperNotificationException.ifInvalidID("pause.id", pause.id);
			DeveloperNotificationException.ifTrue("pause.beginn.isBlank()", pause.beginn.isBlank());
			DeveloperNotificationException.ifTrue("pause.ende.isBlank()", pause.ende.isBlank());
			DeveloperNotificationException.ifTrue("(pause.wochentag < 1) || (pause.wochentag > 7)", (pause.wochentag < 1) || (pause.wochentag > 7));
			DeveloperNotificationException.ifMapContains("_map_pausenzeitID_zu_pausenzeit", _map_pausenzeitID_zu_pausenzeit, pause.id);
			_map_pausenzeitID_zu_pausenzeit.put(pause.id, pause);
		}
	}

	private void initMapAufsicht() {
		_map_aufsichtID_zu_aufsicht.clear();
		for (final @NotNull StundenplanAufsichtsbereich aufsicht : _daten.aufsichtsbereiche) {
			DeveloperNotificationException.ifInvalidID("aufsicht.id", aufsicht.id);
			DeveloperNotificationException.ifTrue("aufsicht.kuerzel.isBlank()", aufsicht.kuerzel.isBlank());
			DeveloperNotificationException.ifMapContains("_map_aufsichtID_zu_aufsicht", _map_aufsichtID_zu_aufsicht, aufsicht.id);
			_map_aufsichtID_zu_aufsicht.put(aufsicht.id, aufsicht);
		}
	}

	private void initMapKWZuordnung() {
		_map_kwzID_zu_kwz.clear();
		_map_jahr_kw_zu_kwz.clear();
		for (final @NotNull StundenplanKalenderwochenzuordnung kwz : _daten.kalenderwochenZuordnung) {
			DeveloperNotificationException.ifInvalidID("kw.id", kwz.id);
			DeveloperNotificationException.ifTrue("(kwz.jahr < 2000) || (kwz.jahr > 3000)", (kwz.jahr < 2000) || (kwz.jahr > 3000));
			DeveloperNotificationException.ifTrue("(kwz.kw < 1) || (kwz.kw > 53)", (kwz.kw < 1) || (kwz.kw > 53));
			DeveloperNotificationException.ifTrue("kwz.wochentyp > _daten.wochenTypModell", kwz.wochentyp > _daten.wochenTypModell);
			DeveloperNotificationException.ifTrue("kwz.wochentyp == 0", kwz.wochentyp == 0);
			_map_jahr_kw_zu_kwz.put(kwz.jahr, kwz.kw, kwz);
			_map_kwzID_zu_kwz.put(kwz.id, kwz);
		}
	}

	private void initMapKursZuUnterrichte() {
		_map_kursID_zu_unterrichte.clear();
		for (final @NotNull StundenplanUnterricht u : _datenU) {
			// Ignoriere, falls es kein Kurs-Unterricht ist.
			if (u.idKurs == null)
				continue;
			// Wurde der Kurs in der Unterrichtsverteilung definiert?
			DeveloperNotificationException.ifTrue("!_map_kursID_zu_kurs.containsKey(u.idKurs)", !_map_kursID_zu_kurs.containsKey(u.idKurs));
			// Liste des Kurses holen.
			List<@NotNull StundenplanUnterricht> listU = _map_kursID_zu_unterrichte.get(u.idKurs); // Kann NULL sein!
			if (listU == null) {
				listU = new ArrayList<>();
				_map_kursID_zu_unterrichte.put(u.idKurs, listU);
			}
			DeveloperNotificationException.ifTrue("listU.contains(u)", listU.contains(u));
			// Hinzufügen
			listU.add(u);
		}
	}

	/**
	 * Liefert die ID des Stundenplans.
	 *
	 * @return die ID des Stundenplans.
	 */
	public long getID() {
		return _daten.id;
	}

	/**
	 * Liefert die ID des Schuljahresabschnitts des Stundenplans.
	 *
	 * @return die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public long getIDSchuljahresabschnitt() {
		return _daten.idSchuljahresabschnitt;
	}

	/**
	 * Liefert das Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return das Datum, ab dem der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigAb() {
		return _daten.gueltigAb;
	}

	/**
	 * Liefert das Datum, bis wann der Stundenplan gültig ist.
	 *
	 * @return das Datum, bis wann der Stundenplan gültig ist.
	 */
	public @NotNull String getGueltigBis() {
		return _daten.gueltigBis;
	}

	/**
	 * Liefert die textuelle Beschreibung des Stundenplans.
	 *
	 * @return die textuelle Beschreibung des Stundenplans.
	 */
	public @NotNull String getBezeichnungStundenplan() {
		return _daten.bezeichnungStundenplan;
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
		return _daten.wochenTypModell;
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
		if (_daten.wochenTypModell == 0)
			return 0;
		final StundenplanKalenderwochenzuordnung z = _map_jahr_kw_zu_kwz.getOrNull(jahr, kalenderwoche);
		if (z != null)
			return z.wochentyp;
		final int wochentyp = kalenderwoche % _daten.wochenTypModell;
		return wochentyp == 0 ? _daten.wochenTypModell : wochentyp;
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
		return (_daten.wochenTypModell >= 2) && (z != null);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses mit einem bestimmten Wochentyp.
	 *
	 * @param kursID    Die ID des Kurses.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDesKursesByWochentyp(final long kursID, final int wochentyp) {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("wochentyp > _daten.wochenTypModell", wochentyp > _daten.wochenTypModell);
		@NotNull final List<@NotNull StundenplanUnterricht> list = DeveloperNotificationException.ifNull("_map_kursID_zu_unterrichte.get(kursID)==NULL", _map_kursID_zu_unterrichte.get(kursID));

		// Daten filtern.
		final @NotNull ArrayList<@NotNull StundenplanUnterricht> result = new ArrayList<>();
		for (final StundenplanUnterricht u : list)
			if ((u.wochentyp == 0) || (u.wochentyp == wochentyp))
				result.add(u);

		return result;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param kursID        Die ID des Kurses.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDesKursesByKW(final long kursID, final int jahr, final int kalenderwoche) {
		final int wochentyp = getWochentypOrDefault(jahr, kalenderwoche);
		return getUnterrichtDesKursesByWochentyp(kursID, wochentyp);
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 *
	 * @param kursIDs   Die IDs aller Kurse.
	 * @param wochentyp Der gewünschten Wochentyp. Der Wert 0 ist nur dann erlaubt, wenn wochenTypModell ebenfalls 0 ist.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge mit einem bestimmten Wochentyp.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDerKurseByWochentyp(final @NotNull long[] kursIDs, final int wochentyp) {
		// Daten filtern.
		final @NotNull ArrayList<@NotNull StundenplanUnterricht> result = new ArrayList<>();
		for (final long kursID : kursIDs)
			result.addAll(getUnterrichtDesKursesByWochentyp(kursID, wochentyp));
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 *
	 * @param kursIDs       Die IDs aller Kurse.
	 * @param jahr          Das Jahr der Kalenderwoche (muss zwischen 2000 und 3000 liegen).
	 * @param kalenderwoche Die gewünschten Kalenderwoche (muss zwischen 1 und 53 liegen).
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} einer Kursmenge in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDerKurseByKW(final @NotNull long[] kursIDs, final int jahr, final int kalenderwoche) {
		final int wochentyp = getWochentypOrDefault(jahr, kalenderwoche);
		return getUnterrichtDerKurseByWochentyp(kursIDs, wochentyp);
	}

	/**
	 * Liefert ein Map der Räume {@link StundenplanRaum} für den aktuell ausgewählten Stundenplan.
	 *
	 * @return ein Map der Räume {@link StundenplanRaum}
	 */
	public @NotNull Map<@NotNull Long, @NotNull StundenplanRaum> getMapRaeume() {
		return _map_raumID_zu_raum;
	}

	/**
	 * Liefert ein Map der Pausenzeiten {@link StundenplanPausenzeit} für den aktuell ausgewählten Stundenplan.
	 *
	 * @return ein Map der Pausenzeiten {@link StundenplanPausenzeit}
	 */
	public @NotNull Map<@NotNull Long, @NotNull StundenplanPausenzeit> getMapPausenzeiten() {
		return _map_pausenzeitID_zu_pausenzeit;
	}

	/**
	 * Liefert ein Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich} für den aktuell ausgewählten Stundenplan.
	 *
	 * @return ein Map der Aufsichtsbereiche {@link StundenplanAufsichtsbereich}
	 */
	public @NotNull Map<@NotNull Long, @NotNull StundenplanAufsichtsbereich> getMapAufsichtsbereich() {
		return _map_aufsichtID_zu_aufsicht;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanRaum}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanRaum}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanRaum> getListRaum() {
		return _daten.raeume;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanPausenzeit}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanPausenzeit> getListPausenzeit() {
		return _daten.pausenzeiten;
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 *
	 * @return eine Liste aller {@link StundenplanAufsichtsbereich}-Objekte.
	 */
	public @NotNull List<@NotNull StundenplanAufsichtsbereich> getListAufsichtbereich() {
		return _daten.aufsichtsbereiche;
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanRaum}-Objekt.
	 *
	 * @param raumID Die ID des angefragten-Objektes.
	 *
	 * @return das zur raumID zugehörige {@link StundenplanRaum}-Objekt.
	 */
	public @NotNull StundenplanRaum getRaum(final long raumID) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_raumID_zu_raum, raumID);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 *
	 * @param pausenzeitID Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanPausenzeit}-Objekt.
	 */
	public @NotNull StundenplanPausenzeit getPausenzeit(final long pausenzeitID) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_pausenzeitID_zu_pausenzeit, pausenzeitID);
	}

	/**
	 * Liefert das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 *
	 * @param aufsichtsbereichID Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link StundenplanAufsichtsbereich}-Objekt.
	 */
	public @NotNull StundenplanAufsichtsbereich getAufsichtsbereich(final long aufsichtsbereichID) {
		return DeveloperNotificationException.ifMapGetIsNull(_map_aufsichtID_zu_aufsicht, aufsichtsbereichID);
	}

	/**
	 * Fügt dem Stundenplan einen neuen Raum hinzu.
	 *
	 * @param raum Der Raum, der hinzugefügt werden soll.
	 */
	public void addRaum(final @NotNull StundenplanRaum raum) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_raumID_zu_raum, raum.id, raum);
		_daten.raeume.add(raum); // TODO BAR Sortiere die Liste
	}

	/**
	 * Fügt dem Stundenplan eine neue Pausenzeit hinzu.
	 *
	 * @param pausenzeit Die Pausenzeit, die hinzugefügt werden soll.
	 */
	public void addPausenzeit(final @NotNull StundenplanPausenzeit pausenzeit) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_pausenzeitID_zu_pausenzeit, pausenzeit.id, pausenzeit);
		_daten.pausenzeiten.add(pausenzeit); // TODO BAR Sortiere die Liste
	}

	/**
	 * Fügt dem Stundenplan einen neuen Aufsichtsbereich hinzu.
	 *
	 * @param aufsichtsbereich Der Aufsichtsbereich, der hinzugefügt werden soll.
	 */
	public void addAufsichtsbereich(final @NotNull StundenplanAufsichtsbereich aufsichtsbereich) {
		DeveloperNotificationException.ifMapPutOverwrites(_map_aufsichtID_zu_aufsicht, aufsichtsbereich.id, aufsichtsbereich);
		_daten.aufsichtsbereiche.add(aufsichtsbereich); // TODO BAR Sortiere die Liste
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierenden {@link StundenplanRaum}.
	 *
	 * @param raumID Die ID des Raumes, der entfernt werden soll.
	 */
	public void removeRaum(final long raumID) {
		final @NotNull StundenplanRaum raum = DeveloperNotificationException.ifNull("_map_raumID_zu_raum.get(" + raumID + ")", _map_raumID_zu_raum.get(raumID));
		_map_raumID_zu_raum.remove(raumID);
		_daten.raeume.remove(raum);
	}

	/**
	 * Entfernt aus dem Stundenplan eine existierende {@link StundenplanPausenzeit}.
	 *
	 * @param pausenzeitID Die ID der Pausenzeit, die entfernt werden soll.
	 */
	public void removePausenzeit(final long pausenzeitID) {
		final @NotNull StundenplanPausenzeit pausenzeit = DeveloperNotificationException.ifNull("_map_pausenzeitID_zu_pausenzeit.get(" + pausenzeitID + ")", _map_pausenzeitID_zu_pausenzeit.get(pausenzeitID));
		_map_pausenzeitID_zu_pausenzeit.remove(pausenzeitID);
		_daten.pausenzeiten.remove(pausenzeit);
	}

	/**
	 * Entfernt aus dem Stundenplan einen existierenden {@link StundenplanAufsichtsbereich}.
	 *
	 * @param aufsichtsbereichID Die ID des Aufsichtsbereich, der entfernt werden soll.
	 */
	public void removeAufsichtsbereich(final long aufsichtsbereichID) {
		final @NotNull StundenplanAufsichtsbereich aufsichtsbereich = DeveloperNotificationException.ifNull("_map_aufsichtID_zu_aufsicht.get(" + aufsichtsbereichID + ")", _map_aufsichtID_zu_aufsicht.get(aufsichtsbereichID));
		_map_aufsichtID_zu_aufsicht.remove(aufsichtsbereichID);
		_daten.aufsichtsbereiche.remove(aufsichtsbereich);
	}

}
