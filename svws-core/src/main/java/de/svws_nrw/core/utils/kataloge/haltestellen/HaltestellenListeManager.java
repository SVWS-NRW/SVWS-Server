package de.svws_nrw.core.utils.kataloge.haltestellen;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.Haltestelle;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class HaltestellenListeManager extends AuswahlManager<Long, Haltestelle, Haltestelle> {

	private static final @NotNull Function<Haltestelle, Long> _haltestelleToId = (final @NotNull Haltestelle h) -> h.id;

	/** Ein Default-Comparator für den Vergleich von Haltestellen. */
	public static final @NotNull Comparator<Haltestelle> comparator =
			(final @NotNull Haltestelle a, final @NotNull Haltestelle b) -> {
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
	 * @param haltestellen	     			  die Liste der Haltestellen
	 */
	public HaltestellenListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<Haltestelle> haltestellen) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, haltestellen,
				HaltestellenListeManager.comparator, _haltestelleToId, _haltestelleToId, List.of());
	}

	@Override
	protected boolean checkFilter(final Haltestelle haltestelle) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Haltestelle a, final @NotNull Haltestelle b) {
		return comparator.compare(a, b);
	}
}
