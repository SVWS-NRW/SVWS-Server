package de.svws_nrw.core.utils.religion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.AuswahlManager;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten des Kataloges der Religionen.
 */
public final class ReligionListeManager extends AuswahlManager<Long, ReligionEintrag, ReligionEintrag> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<ReligionEintrag, Long> _religionToId = (final @NotNull ReligionEintrag r) -> r.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<Boolean, Long, ReligionEintrag> _mapReligionEintragIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap<String, ReligionEintrag> _mapReligionEintragByKuerzel = new HashMap<>();

	/** Das Filter-Attribut auf nur sichtbare ReligionEintragen */
	private boolean _filterNurSichtbar = true;



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param religionen                   die Liste der Katalog-Einträge
	 */
	public ReligionListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<ReligionEintrag> religionen) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, religionen, ReligionUtils.comparator, _religionToId,
				_religionToId, Arrays.asList());
		initEintrage();
	}


	private void initEintrage() {
		for (final @NotNull ReligionEintrag r : this.liste.list()) {
			this._mapReligionEintragIstSichtbar.put(r.istSichtbar, r.id, r);
			if (r.kuerzel != null)
				this._mapReligionEintragByKuerzel.put(r.kuerzel, r);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull ReligionEintrag eintrag, final @NotNull ReligionEintrag daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Fächerliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!daten.text.equals(eintrag.text)) {
			eintrag.text = daten.text;
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
	 * Vergleicht zwei Religionslisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull ReligionEintrag a, final @NotNull ReligionEintrag b) {
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kuerzel".equals(field)) {
				cmp = ReligionUtils.comparator.compare(a, b);
			} else if ("text".equals(field)) {
				cmp = ReligionUtils.comparatorText.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return ReligionUtils.comparator.compare(a, b);
	}


	@Override
	protected boolean checkFilter(final @NotNull ReligionEintrag eintrag) {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		return true;
	}


	/**
	 * Gibt die Religion anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Religion oder null
	 */
	public ReligionEintrag getByKuerzelOrNull(final @NotNull String kuerzel) {
		return this._mapReligionEintragByKuerzel.get(kuerzel);
	}

}
