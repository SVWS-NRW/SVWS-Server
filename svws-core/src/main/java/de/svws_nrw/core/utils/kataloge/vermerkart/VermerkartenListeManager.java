package de.svws_nrw.core.utils.kataloge.vermerkart;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schueler.SchuelerVermerkartZusammenfassung;
import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.schueler.SchuelerUtils;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Ein Manager zum Verwalten der VermerkartEintrag-Listen.
 */
public final class VermerkartenListeManager extends AuswahlManager<Long, VermerkartEintrag, VermerkartEintrag> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<VermerkartEintrag, Long> _vermerkartToId = (final @NotNull VermerkartEintrag v) -> v.id;

	private @NotNull AttributMitAuswahl<Long, SchuelerVermerkartZusammenfassung> listSchuelerVermerkartZusammenfassung;
	private static final @NotNull Function<SchuelerVermerkartZusammenfassung, @NotNull Long> _schuelerToId =
			(final @NotNull SchuelerVermerkartZusammenfassung s) -> s.id;

	/** Das Filter-Attribut auf nur sichtbare Vermerkarten */
	private boolean _filterNurSichtbar = true;

	/** Ein Dummy-Event. */
	protected static final @NotNull Runnable _dummyEvent = () -> { /* do nothing */	};

	/** Sets mit Listen zur aktuellen Auswahl */
	private final @NotNull HashSet<Long> setVermerkartenIDsMitSchuelern = new HashSet<>();

	/** Ein Default-Comparator für den Vergleich von Vermerkarten in Vermerkartenlisten. */
	public static final @NotNull Comparator<VermerkartEintrag> comparator =
			(final @NotNull VermerkartEintrag a, final @NotNull VermerkartEintrag b) -> {
				int cmp = a.sortierung - b.sortierung;
				if (cmp != 0)
					return cmp;
				if ((a.bezeichnung == null) || (b.bezeichnung == null))
					return Long.compare(a.id, b.id);
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param vermerkarten     					die Liste der Vermerkarten
	 * @param listSchuelerVermerkartZusammenfassung     	die Liste der SchuelerVermerkartZusammenfassung
	 */
	public VermerkartenListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform, final @NotNull List<VermerkartEintrag> vermerkarten,
			final @NotNull List<SchuelerVermerkartZusammenfassung> listSchuelerVermerkartZusammenfassung) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, vermerkarten, VermerkartenListeManager.comparator, _vermerkartToId, _vermerkartToId,
				Arrays.asList(new Pair<>("Vermerkart", true), new Pair<>("schueleranzahl", true)));
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl<>(listSchuelerVermerkartZusammenfassung, _schuelerToId,
				SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, _dummyEvent);
	}

	/**
	 * Setzt die Liste der SchülervermerkartZusammenfassungen.
	 *
	 * @param listSchuelerVermerkartZusammenfassung Eine Liste von SchülervermerkartZusammenfassungen
	 */
	public void setListSchuelerVermerkartZusammenfassung(final @NotNull List<SchuelerVermerkartZusammenfassung> listSchuelerVermerkartZusammenfassung) {
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl<>(listSchuelerVermerkartZusammenfassung, _schuelerToId,
				SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, _dummyEvent);
	}

	/** Gibt das Set mit den VermerkartenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Vermerkarten, die Schüler haben
	*/
	public @NotNull Set<Long> getVermerkartenIDsMitSchuelern() {
		return this.setVermerkartenIDsMitSchuelern;
	}
	/**
	 * Gibt die Liste der SchülervermerkartZusammenfassungen zurück.
	 *
	 * @return Eine Instanz von AttributMitAuswahl, die eine Liste von SchülervermerkartZusammenfassungen enthält.
	 */
	public @NotNull AttributMitAuswahl<Long, SchuelerVermerkartZusammenfassung> getListSchuelerVermerkartZusammenfassung() {
		return listSchuelerVermerkartZusammenfassung;
	}

	@Override
	protected boolean onSetDaten(final @NotNull VermerkartEintrag eintrag, final @NotNull VermerkartEintrag daten) {
		boolean updateEintrag = false;
		if (!daten.bezeichnung.equals(eintrag.bezeichnung)) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}


	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Vermerkarten zurück.
	 *
	 * @return true, wenn nur sichtbare Vermerkarten angezeigt werden und ansonsten false
	 */
	public boolean filterNurSichtbar() {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Vermerkarten.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurSichtbar(final boolean value) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}


	@Override
	protected void onMehrfachauswahlChanged() {
		this.setVermerkartenIDsMitSchuelern.clear();
		for (final @NotNull VermerkartEintrag k : this.liste.auswahl())
			if (k.anzahlVermerke != 0)
				this.setVermerkartenIDsMitSchuelern.add(k.id);
	}

	/**
	 * Vergleicht zwei VermerkArtEinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull VermerkartEintrag a, final @NotNull VermerkartEintrag b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("Vermerkart".equals(field)) {
				cmp = VermerkartenListeManager.comparator.compare(a, b);
			} else if ("schueleranzahl".equals(field)) {
				cmp = Integer.compare(a.anzahlVermerke, b.anzahlVermerke);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	@Override
	protected boolean checkFilter(final @NotNull VermerkartEintrag eintrag) {
		return !(this._filterNurSichtbar && !eintrag.istSichtbar);
	}

	public void useFilter(final @NotNull VermerkartenListeManager srcManager) {
		this.setFilterNurSichtbar(srcManager.filterNurSichtbar());
	}

}
