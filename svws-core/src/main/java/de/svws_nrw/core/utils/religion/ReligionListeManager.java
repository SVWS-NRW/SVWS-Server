package de.svws_nrw.core.utils.religion;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.schule.Schulform;
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

	/** Ein Default-Comparator für den Vergleich von Religion-Einträgen. */
	public static final @NotNull Comparator<ReligionEintrag> _comparatorKuerzel =
		(final @NotNull ReligionEintrag a, final @NotNull ReligionEintrag b) -> {
			int cmp = a.sortierung - b.sortierung;
			if (cmp != 0)
				return cmp;
			if ((a.kuerzel == null) || (b.kuerzel == null)) {
				if ((a.kuerzel == null) && (b.kuerzel == null))
					return 0;
				return (a.kuerzel == null) ? -1 : 1;
			}
			cmp = a.kuerzel.compareTo(b.kuerzel);
			return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
		};

	/** Ein Comparator für den Vergleich von Religion-Einträgen anhand ihres Textes. */
	public static final @NotNull Comparator<ReligionEintrag> _comparatorText =
		(final @NotNull ReligionEintrag a, final @NotNull ReligionEintrag b) -> {
			if ((a.bezeichnung == null) || (b.bezeichnung == null)) {
				if ((a.bezeichnung == null) && (b.bezeichnung == null))
					return 0;
				return (a.bezeichnung == null) ? -1 : 1;
			}
			final int cmp = a.bezeichnung.compareTo(b.bezeichnung);
			return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
		};


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
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, religionen, _comparatorKuerzel, _religionToId,
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
				cmp = _comparatorKuerzel.compare(a, b);
			} else if ("text".equals(field)) {
				cmp = _comparatorText.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return _comparatorKuerzel.compare(a, b);
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

	public void useFilter(final @NotNull ReligionListeManager srcManager) {
		this.setFilterNurSichtbar(srcManager.filterNurSichtbar());
	}

}
