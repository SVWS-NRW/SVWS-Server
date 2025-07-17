package de.svws_nrw.core.utils.kataloge.fahrschuelerarten;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.Fahrschuelerart;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class FahrschuelerartenListeManager extends AuswahlManager<Long, Fahrschuelerart, Fahrschuelerart> {


	private static final @NotNull Function<Fahrschuelerart, Long> _fahrschuelerartToId = (final @NotNull Fahrschuelerart m) -> m.id;

	/** Ein Default-Comparator für den Vergleich von Fahrschülerarten. */
	public static final @NotNull Comparator<Fahrschuelerart> comparator =
			(final @NotNull Fahrschuelerart a, final @NotNull Fahrschuelerart b) -> {
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
	 * @param abschnitte           			  die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param fahrschuelerarten		          die Liste der Fahrschülerarten
	 */
	public FahrschuelerartenListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> abschnitte, final Schulform schulform,
			final @NotNull List<Fahrschuelerart> fahrschuelerarten) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, abschnitte, schulform, fahrschuelerarten, FahrschuelerartenListeManager.comparator,
				_fahrschuelerartToId, _fahrschuelerartToId, List.of());
	}

	@Override
	protected boolean checkFilter(final Fahrschuelerart fahrschuelerart) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Fahrschuelerart a, final @NotNull Fahrschuelerart b) {
		return comparator.compare(a, b);
	}
}
