package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
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
	private final StundenplanUnterrichtsverteilung _datenUV;

	// Simple Mappings von "Stundenplan"
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanZeitraster> _map_zeitrasterID_zu_zeitraster = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanRaum> _map_raumID_zu_raum = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanSchiene> _map_schieneID_zu_schiene = new HashMap<>();

	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKurs> _map_kursID_zu_kurs = new HashMap<>();

	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_kursID_zu_unterrichte = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Integer, @NotNull StundenplanKalenderwochenzuordnung> _map_jahr_kw_zu_wochtentyp = new HashMap2D<>();

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
		_datenUV = unterrichtsverteilung;
		checkWochentypenKonsistenz();

		initMapZeitrasterIDZuZeitraster();
		initMapRaumIDZuRaum();
		initMapSchieneIDZuSchiene();

		initMapJahrUndKwZuWochentyp();
		initMapKursIDZuKurs();
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

	private void initMapZeitrasterIDZuZeitraster() {
		_map_zeitrasterID_zu_zeitraster.clear();
		for (final @NotNull StundenplanZeitraster zeit : _daten.zeitraster) {
			DeveloperNotificationException.ifTrue("zeit.id <= 0", zeit.id <= 0);
			DeveloperNotificationException.ifTrue("zeit.stundenbeginn.length() == 0", zeit.stundenbeginn.length() == 0); // TODO Transpiler: Mit isBlank() ersetzen
			DeveloperNotificationException.ifTrue("zeit.stundenende.length() == 0", zeit.stundenende.length() == 0); // TODO Transpiler: Mit isBlank() ersetzen
			DeveloperNotificationException.ifTrue("zeit.unterrichtstunde <= 0", zeit.unterrichtstunde <= 0);
			DeveloperNotificationException.ifTrue("(zeit.wochentag < 1) || (zeit.wochentag > 7)", (zeit.wochentag < 1) || (zeit.wochentag > 7));
			DeveloperNotificationException.ifTrue("_map_zeitrasterID_zu_zeitraster.containsKey(zeit.id)", _map_zeitrasterID_zu_zeitraster.containsKey(zeit.id));
			_map_zeitrasterID_zu_zeitraster.put(zeit.id, zeit);
		}
	}

	private void initMapRaumIDZuRaum() {
		_map_raumID_zu_raum.clear();
		for (final @NotNull StundenplanRaum raum : _daten.raeume) {
			DeveloperNotificationException.ifTrue("raum.id <= 0", raum.id <= 0);
			DeveloperNotificationException.ifTrue("raum.groesse < 0", raum.groesse < 0);
			DeveloperNotificationException.ifTrue("raum.kuerzel.length() == 0", raum.kuerzel.length() == 0); // TODO Transpiler: Mit isBlank() ersetzen
			DeveloperNotificationException.ifTrue("_map_raumID_zu_raum.containsKey(raum.id)", _map_raumID_zu_raum.containsKey(raum.id));
			_map_raumID_zu_raum.put(raum.id, raum);
		}

	}

	private void initMapSchieneIDZuSchiene() {
		_map_schieneID_zu_schiene.clear();
		for (final @NotNull StundenplanSchiene schiene : _daten.schienen) {
			DeveloperNotificationException.ifTrue("schiene.id <= 0", schiene.id <= 0);
			DeveloperNotificationException.ifTrue("schiene.idJahrgang <= 0", schiene.idJahrgang <= 0);
			DeveloperNotificationException.ifTrue("schiene.nummer <= 0", schiene.nummer <= 0);
			DeveloperNotificationException.ifTrue("schiene.bezeichnung.length() == 0", schiene.bezeichnung.length() == 0); // TODO Transpiler: Mit isBlank() ersetzen
			DeveloperNotificationException.ifTrue("_map_schieneID_zu_schiene.containsKey(schiene.id)", _map_schieneID_zu_schiene.containsKey(schiene.id));
			_map_schieneID_zu_schiene.put(schiene.id, schiene);
		}
	}

	private void initMapJahrUndKwZuWochentyp() {
		_map_jahr_kw_zu_wochtentyp.clear();
		for (final @NotNull StundenplanKalenderwochenzuordnung skwz : _daten.kalenderwochenZuordnung)
			_map_jahr_kw_zu_wochtentyp.put(skwz.jahr, skwz.kw, skwz);
	}

	private void initMapKursIDZuKurs() {
		if (_datenUV == null)
			return;

		_map_kursID_zu_kurs.clear();
		for (final @NotNull StundenplanKurs k : _datenUV.kurse) {
			DeveloperNotificationException.ifTrue("_map_kursID_zu_kurs.containsKey(k.id)", _map_kursID_zu_kurs.containsKey(k.id));
			_map_kursID_zu_kurs.put(k.id, k);
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
			List<@NotNull StundenplanUnterricht> listU = _map_kursID_zu_unterrichte.get(u.idKurs);
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
		final StundenplanKalenderwochenzuordnung z = _map_jahr_kw_zu_wochtentyp.getOrNull(jahr, kalenderwoche);
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
		final StundenplanKalenderwochenzuordnung z = _map_jahr_kw_zu_wochtentyp.getOrNull(jahr, kalenderwoche);
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

}
