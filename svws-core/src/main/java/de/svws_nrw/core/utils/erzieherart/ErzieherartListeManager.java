package de.svws_nrw.core.utils.erzieherart;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
