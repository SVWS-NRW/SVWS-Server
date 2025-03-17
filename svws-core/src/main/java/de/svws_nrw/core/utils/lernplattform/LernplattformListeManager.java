package de.svws_nrw.core.utils.lernplattform;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Lernplattformen.
 */
public final class LernplattformListeManager extends AuswahlManager<Long, Lernplattform, Lernplattform> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<Lernplattform, Long> _lernplattformenToId = (final @NotNull Lernplattform ea) -> ea.id;

	/** Ein Default-Comparator für den Vergleich von Lernplattformen in Lernplattformlisten. */
	public static final @NotNull Comparator<Lernplattform> comparator =
			(final @NotNull Lernplattform a, final @NotNull Lernplattform b) -> {
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
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Lernplattform bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param listLernplattform     	   die Liste der Lernplattform
	 */
	public LernplattformListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform, final @NotNull List<Lernplattform> listLernplattform) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listLernplattform, LernplattformListeManager.comparator,
				_lernplattformenToId, _lernplattformenToId,
				Arrays.asList(new Pair<>("lernplattform", true)));
	}

	@Override
	protected boolean onSetDaten(final @NotNull Lernplattform eintrag, final @NotNull Lernplattform daten) {
		boolean updateEintrag = false;
		if (!daten.bezeichnung.equals(eintrag.bezeichnung)) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	@Override
	protected int compareAuswahl(final @NotNull Lernplattform a, final @NotNull Lernplattform b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("lernplattform".equals(field)) {
				cmp = LernplattformListeManager.comparator.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	@Override
	protected boolean checkFilter(final @NotNull Lernplattform eintrag) {
		return true;
	}

}
