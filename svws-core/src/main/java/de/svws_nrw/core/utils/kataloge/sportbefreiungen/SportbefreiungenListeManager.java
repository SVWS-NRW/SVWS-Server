package de.svws_nrw.core.utils.kataloge.sportbefreiungen;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.Sportbefreiung;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class SportbefreiungenListeManager extends AuswahlManager<Long, Sportbefreiung, Sportbefreiung> {

	private static final @NotNull Function<Sportbefreiung, Long> _sportbefreiungToId = (final @NotNull Sportbefreiung h) -> h.id;

	/** Ein Default-Comparator für den Vergleich von Sportbefreiungen. */
	public static final @NotNull Comparator<Sportbefreiung> comparator =
			(final @NotNull Sportbefreiung a, final @NotNull Sportbefreiung b) -> {
				int cmp;
				cmp = Integer.compare(a.sortierung, b.sortierung);
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
	 * @param sportbefreiungen				  die Liste der Sportbefreiungen
	 */
	public SportbefreiungenListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<Sportbefreiung> sportbefreiungen) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, sportbefreiungen,
				SportbefreiungenListeManager.comparator, _sportbefreiungToId, _sportbefreiungToId, List.of());
	}


	@Override
	protected boolean checkFilter(final Sportbefreiung sportbefreiung) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Sportbefreiung a, final @NotNull Sportbefreiung b) {
		return comparator.compare(a, b);
	}

}
