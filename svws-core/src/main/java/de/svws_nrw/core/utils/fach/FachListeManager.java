package de.svws_nrw.core.utils.fach;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.AuswahlManager;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Fächer-Listen.
 */
public final class FachListeManager extends AuswahlManager<Long, FachDaten, FachDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<FachDaten, Long> _fachToId = (final @NotNull FachDaten f) -> f.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<Boolean, Long, FachDaten> _mapFachIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap<String, FachDaten> _mapFachByKuerzel = new HashMap<>();

	/** Das Filter-Attribut auf nur sichtbare Fächer */
	private boolean _filterNurSichtbar = true;



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param faecher       die Liste der Fächer
	 */
	public FachListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<FachDaten> faecher) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, faecher, FachUtils.comparator, _fachToId, _fachToId,
				Arrays.asList());
		initFaecher();
	}


	private void initFaecher() {
		for (final @NotNull FachDaten f : this.liste.list()) {
			this._mapFachIstSichtbar.put(f.istSichtbar, f.id, f);
			if (f.kuerzel != null)
				this._mapFachByKuerzel.put(f.kuerzel, f);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull FachDaten eintrag, final @NotNull FachDaten daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Fächerliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!daten.bezeichnung.equals(eintrag.bezeichnung)) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}


	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Fächer zurück.
	 *
	 * @return true, wenn nur sichtbare Fächer angezeigt werden und ansonsten false
	 */
	public boolean filterNurSichtbar() {
		return this._filterNurSichtbar;
	}


	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Fächer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurSichtbar(final boolean value) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}


	/**
	 * Vergleicht zwei Fächerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull FachDaten a, final @NotNull FachDaten b) {
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kuerzel".equals(field)) {
				cmp = a.kuerzel.compareTo(b.kuerzel);
			} else if ("bezeichnung".equals(field)) {
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return FachUtils.comparator.compare(a, b);
	}


	@Override
	protected boolean checkFilter(final @NotNull FachDaten eintrag) {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		return true;
	}


	/**
	 * Gibt die FachDaten anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die FachDaten oder null
	 */
	public FachDaten getByKuerzelOrNull(final @NotNull String kuerzel) {
		return this._mapFachByKuerzel.get(kuerzel);
	}

}
