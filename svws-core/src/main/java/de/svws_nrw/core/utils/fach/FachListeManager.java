package de.svws_nrw.core.utils.fach;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Fächer-Listen.
 */
public final class FachListeManager extends AuswahlManager<Long, FaecherListeEintrag, FachDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<FaecherListeEintrag, Long> _fachToId = (final @NotNull FaecherListeEintrag f) -> f.id;
	private static final @NotNull Function<FachDaten, Long> _fachDatenToId = (final @NotNull FachDaten f) -> f.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<Boolean, Long, FaecherListeEintrag> _mapFachIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap<String, FaecherListeEintrag> _mapFachByKuerzel = new HashMap<>();

	/** Das Filter-Attribut auf nur sichtbare Fächer */
	private boolean _filterNurSichtbar = true;

	/** Sets mit Listen zur aktuellen Auswahl */
	private final @NotNull HashSet<Long> idsReferenzierterFaecher = new HashSet<>();

	/** Ein Default-Comparator für den Vergleich von Fächern in Fächerlisten. */
	public static final @NotNull Comparator<FaecherListeEintrag> comparator = (final @NotNull FaecherListeEintrag a, final @NotNull FaecherListeEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

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
			final @NotNull List<FaecherListeEintrag> faecher) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, faecher, comparator, _fachToId, _fachDatenToId,
				Arrays.asList());
		initFaecher();
	}


	private void initFaecher() {
		for (final @NotNull FaecherListeEintrag f : this.liste.list()) {
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
	protected boolean onSetDaten(final @NotNull FaecherListeEintrag eintrag, final @NotNull FachDaten daten) {
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
	protected int compareAuswahl(final @NotNull FaecherListeEintrag a, final @NotNull FaecherListeEintrag b) {
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
		return comparator.compare(a, b);
	}

	@Override
	protected void onMehrfachauswahlChanged() {
		this.idsReferenzierterFaecher.clear();
		for (final @NotNull FaecherListeEintrag f : this.liste.auswahl())
			if ((f.referenziertInAnderenTabellen != null) && f.referenziertInAnderenTabellen)
				this.idsReferenzierterFaecher.add(f.id);
	}

	/** Gibt das Set mit den FächerIds zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Fächern, die in anderen Datenbanktabellen referenziert werden
	 */
	public @NotNull Set<Long> getIdsReferenzierterFaecher() {
		return this.idsReferenzierterFaecher;
	}

	@Override
	protected boolean checkFilter(final @NotNull FaecherListeEintrag eintrag) {
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
	public FaecherListeEintrag getByKuerzelOrNull(final @NotNull String kuerzel) {
		return this._mapFachByKuerzel.get(kuerzel);
	}

	/**
	 * Wenn das Kürzel nicht leer, bislang nicht verwendet und zwischen 1 und 20 Zeichen lang ist,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param kuerzel   das gegebene Kürzel
	 *
	 * @return <code>true</code> wenn das Kürzel gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateKuerzel(final String kuerzel) {
		if ((kuerzel == null) || kuerzel.isBlank() || (kuerzel.trim().length() > 20))
			return false;

		for (final FaecherListeEintrag listeEintrag : this.liste.list()) {
			if (listeEintrag.kuerzel.equals(kuerzel))
				return false;
		}
		return true;
	}

	/**
	 * Wenn die Bezeichnung nicht leer, bislang nicht verwendet und zwischen 1 und 255 Zeichen lang ist,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param bezeichnung   die gegebene Bezeichnung
	 *
	 * @return <code>true</code> wenn die Bezeichnung gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateBezeichnung(final String bezeichnung) {
		if ((bezeichnung == null) || bezeichnung.isBlank() || (bezeichnung.trim().length() > 255))
			return false;

		for (final FaecherListeEintrag listeEintrag : this.liste.list()) {
			if (listeEintrag.bezeichnung.equals(bezeichnung))
				return false;
		}
		return true;
	}

	/**
	 * Wenn die maxZeichenInFachbemerkungen leer oder einen positiven Wert hat,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param maxZeichenInFachbemerkungen   die gegebene maxZeichenInFachbemerkungen
	 *
	 * @return <code>true</code> wenn die maxZeichenInFachbemerkungen gültig sind, ansonsten <code>false</code>
	 */
	public boolean validateMaxZeichenInFachbemerkungen(final Integer maxZeichenInFachbemerkungen) {
		if (maxZeichenInFachbemerkungen == null)
			return true;
		return (maxZeichenInFachbemerkungen > 0);
	}

	/**
	 * Wenn die Sortierung einen positiven Wert hat,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param sortierung   die gegebene sortierung
	 *
	 * @return <code>true</code> wenn die sortierung gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateSortierung(final Integer sortierung) {
		if (sortierung == null)
			return false;
		return (sortierung > 0) && (sortierung < Integer.MAX_VALUE);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link AuswahlManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public void useFilter(final @NotNull FachListeManager srcManager) {
		this.setFilterAuswahlPermitted(srcManager.isFilterAuswahlPermitted());
		this.setFilterNurSichtbar(srcManager.filterNurSichtbar());
	}

}
