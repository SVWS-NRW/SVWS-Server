package de.svws_nrw.core.utils.erzieherart;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.erzieher.Erzieherart;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Erzieherarten.
 */
public final class ErzieherartListeManager extends AuswahlManager<Long, Erzieherart, Erzieherart> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<Erzieherart, Long> _erzieherartenToId = (final @NotNull Erzieherart ea) -> ea.id;

	/** Sets mit Listen zur aktuellen Auswahl */
	private final @NotNull HashSet<Long> setErzieherartIDsMitPersonen = new HashSet<>();

	/** Ein Default-Comparator für den Vergleich von Erzieherarten in Erzieherartlisten. */
	public static final @NotNull Comparator<Erzieherart> comparator =
			(final @NotNull Erzieherart a, final @NotNull Erzieherart b) -> {
				int cmp = (int) (a.id - b.id);
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
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Erzieherart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param listErzieherart     	       die Liste der Erzieherart
	 */
	public ErzieherartListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform, final @NotNull List<Erzieherart> listErzieherart) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listErzieherart, ErzieherartListeManager.comparator,
				_erzieherartenToId, _erzieherartenToId, Arrays.asList(new Pair<>("erzieherart", true)));
	}

	/** Gibt das Set mit den ErzieherartIds zurück, die in der Auswahl sind und Erziehungsberechtigte beinhalten
	 *
	 * @return Das Set mit IDs von Erzieherarten, die Schüler haben
	 */
	public @NotNull Set<Long> getErzieherartIDsMitPersonen() {
		return this.setErzieherartIDsMitPersonen;
	}

	@Override
	protected void onMehrfachauswahlChanged() {
		this.setErzieherartIDsMitPersonen.clear();
		for (final @NotNull Erzieherart e : this.liste.auswahl())
			if (e.anzahlErziehungsberechtigte != 0)
				this.setErzieherartIDsMitPersonen.add(e.id);
	}

	@Override
	protected int compareAuswahl(final @NotNull Erzieherart a, final @NotNull Erzieherart b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("erzieherart".equals(field)) {
				cmp = ErzieherartListeManager.comparator.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	@Override
	protected boolean checkFilter(final @NotNull Erzieherart eintrag) {
		return true;
	}
}
