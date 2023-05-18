package de.svws_nrw.core.utils.stundenplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author Benjamin A. Bartsch
 */
public class StundenplanManager {

	private final @NotNull Stundenplan _daten;
	private final @NotNull List<@NotNull StundenplanUnterricht> _datenU;
	private final StundenplanUnterrichtsverteilung _datenUV;

	private final @NotNull HashMap<@NotNull Long, @NotNull List<@NotNull StundenplanUnterricht>> _map_kursID_zu_unterrichte = new HashMap<>();
	private final @NotNull HashMap<@NotNull Long, @NotNull StundenplanKurs> _map_kursID_zu_kurs = new HashMap<>();
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

	private void initMapJahrUndKwZuWochentyp() {
		_map_jahr_kw_zu_wochtentyp.clear();
		for (final @NotNull StundenplanKalenderwochenzuordnung z : _daten.kalenderwochenZuordnung)
			_map_jahr_kw_zu_wochtentyp.put(z.jahr, z.kw, z);
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
	 * Liefert den zugeordneten Wochentyp, oder den Default-Wochentyp.
	 *
	 * @param jahr          Das Jahr der Kalenderwoche.
	 * @param kalenderwoche Die gewünschten Kalenderwoche. Der Wert darf nicht 0 sein.
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
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param kursID    Die ID des Kurses.
	 * @param wochentyp Die gewünschten Kalenderwoche. Der Wert darf nicht 0 sein.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDesKurses(final int kursID, final int wochentyp) {

		return new ArrayList<>();
	}

	/**
	 * Liefert eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 *
	 * @param kursID        Die ID des Kurses.
	 * @param jahr          Das Jahr der Kalenderwoche.
	 * @param kalenderwoche Die gewünschten Kalenderwoche. Der Wert darf nicht 0 sein.
	 *
	 * @return eine Liste aller {@link StundenplanUnterricht} eines Kurses in einer bestimmten Kalenderwoche.
	 */
	public @NotNull List<@NotNull StundenplanUnterricht> getUnterrichtDesKurses(final int kursID, final int jahr, final int kalenderwoche) {
		final int wochentyp = getWochentypOrDefault(jahr, kalenderwoche);
		return getUnterrichtDesKurses(kursID, wochentyp);
	}

}
