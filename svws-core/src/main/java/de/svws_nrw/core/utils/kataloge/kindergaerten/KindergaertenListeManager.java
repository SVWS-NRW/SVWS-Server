package de.svws_nrw.core.utils.kataloge.kindergaerten;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.Kindergarten;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class KindergaertenListeManager extends AuswahlManager<Long, Kindergarten, Kindergarten> {

	private static final @NotNull Function<Kindergarten, Long> _kindergartenToId = (final @NotNull Kindergarten k) -> k.id;

	/** Ein Default-Comparator für den Vergleich von Kindergaärten */
	public static final @NotNull Comparator<Kindergarten> comparator =
			(final @NotNull Kindergarten a, final @NotNull Kindergarten b) -> {
				int cmp = Integer.compare(a.sortierung, b.sortierung);
				if (cmp != 0)
					return cmp;

				if ((a.bezeichnung != null) && (b.bezeichnung != null)) {
					cmp = a.bezeichnung.compareTo(b.bezeichnung);
					if (cmp != 0)
						return cmp;
				}

				return Long.compare(a.id, b.id);
			};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param kindergaerten     			  die Liste der Kindergärten
	 */
	public KindergaertenListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<Kindergarten> kindergaerten) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, kindergaerten, KindergaertenListeManager.comparator,
				_kindergartenToId, _kindergartenToId, List.of());
	}

	@Override
	protected boolean checkFilter(final Kindergarten eintrag) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Kindergarten a, final @NotNull Kindergarten b) {
		return comparator.compare(a, b);
	}

}
