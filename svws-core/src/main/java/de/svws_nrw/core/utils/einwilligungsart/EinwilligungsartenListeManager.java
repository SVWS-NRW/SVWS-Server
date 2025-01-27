package de.svws_nrw.core.utils.einwilligungsart;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schueler.SchuelerEinwilligungsartenZusammenfassung;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Einwilligungsarten.
 */
public final class EinwilligungsartenListeManager extends AuswahlManager<Long, Einwilligungsart, Einwilligungsart> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<Einwilligungsart, Long> _einwilligungsArtToId = (final @NotNull Einwilligungsart ea) -> ea.id;

	/** Liste der Schülereinwilligungsarten-Zusammenfassungen */
	private @NotNull AttributMitAuswahl<Long, SchuelerEinwilligungsartenZusammenfassung> listSchuelerEinwilligungsartenZusammenfassung;
	private static final @NotNull Function<SchuelerEinwilligungsartenZusammenfassung, @NotNull Long> _schuelerToId =
			(final @NotNull SchuelerEinwilligungsartenZusammenfassung s) -> s.id;

	/** Das Filter-Attribut auf nur sichtbare Einwilligungsarten */
	private boolean _filterNurSichtbar = true;

	/** Ein Dummy-Event. */
	protected static final @NotNull Runnable _dummyEvent = () -> { /* do nothing */	};

	/** Sets mit Listen zur aktuellen Auswahl */
	private final @NotNull HashSet<Long> setEinwilligungsartenIDsMitSchuelern = new HashSet<>();

	/** Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten. */
	public static final @NotNull Comparator<Einwilligungsart> comparator =
			(final @NotNull Einwilligungsart a, final @NotNull Einwilligungsart b) -> {
				int cmp = a.sortierung - b.sortierung;
				if (cmp != 0)
					return cmp;
				if ((a.bezeichnung == null) || (b.bezeichnung == null))
					return Long.compare(a.id, b.id);
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/** Ein Default-Comparator für den Vergleich von Schülern in Schuelerlisten. */
	public static final @NotNull Comparator<SchuelerEinwilligungsartenZusammenfassung> comparatorSchuelerEinwilligungsartenZusammenfassung =
			(final @NotNull SchuelerEinwilligungsartenZusammenfassung a, final @NotNull SchuelerEinwilligungsartenZusammenfassung b) -> {
				int cmp = a.nachname.compareTo(b.nachname);
				if (cmp != 0)
					return cmp;
				cmp = a.vorname.compareTo(b.vorname);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param listEinwilligungsart     						      die Liste der Einwilligungsarten
	 * @param listSchuelerEinwilligungsartenZusammenfassung         die Liste der SchuelerEinwilligungsartZusammenfassung
	 */
	public EinwilligungsartenListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform, final @NotNull List<Einwilligungsart> listEinwilligungsart,
			final @NotNull List<SchuelerEinwilligungsartenZusammenfassung> listSchuelerEinwilligungsartenZusammenfassung) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listEinwilligungsart, EinwilligungsartenListeManager.comparator, _einwilligungsArtToId, _einwilligungsArtToId,
				Arrays.asList(new Pair<>("einwilligungsart", true), new Pair<>("schueleranzahl", true)));
		this.listSchuelerEinwilligungsartenZusammenfassung = new AttributMitAuswahl<>(listSchuelerEinwilligungsartenZusammenfassung, _schuelerToId,
				comparatorSchuelerEinwilligungsartenZusammenfassung, _dummyEvent);
	}

	/**
	 * Setzt die Liste der SchülereinwilligungsartZusammenfassungen.
	 *
	 * @param listSchuelerEinwilligungsartenZusammenfassung Eine Liste von SchülereinwilligungsartZusammenfassungen
	 */
	public void setListSchuelerEinwilligungsartenZusammenfassung(final @NotNull List<SchuelerEinwilligungsartenZusammenfassung> listSchuelerEinwilligungsartenZusammenfassung) {
		this.listSchuelerEinwilligungsartenZusammenfassung = new AttributMitAuswahl<>(listSchuelerEinwilligungsartenZusammenfassung, _schuelerToId,
				comparatorSchuelerEinwilligungsartenZusammenfassung, _dummyEvent);
	}


	/** Gibt das Set mit den EinwilligungsartenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Einwilligungsarten, die Schüler haben
	 */
	public @NotNull Set<Long> getEinwilligungsartenIDsMitSchuelern() {
		return this.setEinwilligungsartenIDsMitSchuelern;
	}

	/**
	 * Gibt die Liste der SchülereinwilligungsartZusammenfassungen zurück.
	 *
	 * @return Eine Instanz von AttributMitAuswahl, die eine Liste von SchülereinwilligungsartZusammenfassungen enthält.
	 */
	public @NotNull AttributMitAuswahl<Long, SchuelerEinwilligungsartenZusammenfassung> getListSchuelerEinwilligungsartenZusammenfassung() {
		return listSchuelerEinwilligungsartenZusammenfassung;
	}

	@Override
	protected boolean onSetDaten(final @NotNull Einwilligungsart eintrag, final @NotNull Einwilligungsart daten) {
		boolean updateEintrag = false;
		if (!daten.bezeichnung.equals(eintrag.bezeichnung)) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Einwilligungsarten zurück.
	 *
	 * @return true, wenn nur sichtbare Einwilligungsarten angezeigt werden und ansonsten false
	 */
	public boolean filterNurSichtbar() {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Einwilligungsarten.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurSichtbar(final boolean value) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	@Override
	protected void onMehrfachauswahlChanged() {
		this.setEinwilligungsartenIDsMitSchuelern.clear();
		for (final @NotNull Einwilligungsart k : this.liste.auswahl())
			if (k.anzahlEinwilligungen != 0)
				this.setEinwilligungsartenIDsMitSchuelern.add(k.id);
	}

	/**
	 * Vergleicht zwei EinwilligungArtEinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull Einwilligungsart a, final @NotNull Einwilligungsart b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("einwilligungsart".equals(field)) {
				cmp = EinwilligungsartenListeManager.comparator.compare(a, b);
			} else if ("schueleranzahl".equals(field)) {
				cmp = Integer.compare(a.anzahlEinwilligungen, b.anzahlEinwilligungen);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	@Override
	protected boolean checkFilter(final @NotNull Einwilligungsart eintrag) {
		return !(this._filterNurSichtbar && !eintrag.istSichtbar);
	}

}
