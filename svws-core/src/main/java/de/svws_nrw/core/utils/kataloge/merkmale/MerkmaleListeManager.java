package de.svws_nrw.core.utils.kataloge.merkmale;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class MerkmaleListeManager extends AuswahlManager<Long, Merkmal, Merkmal> {

	private static final @NotNull Function<Merkmal, Long> _merkmalToId = (final @NotNull Merkmal m) -> m.id;

	/** Ein Default-Comparator für den Vergleich von Merkmalen. */
	public static final @NotNull Comparator<Merkmal> comparator =
			(final @NotNull Merkmal a, final @NotNull Merkmal b) -> {
				if ((a.bezeichnung != null) && (b.bezeichnung != null)) {
					int cmp = a.bezeichnung.compareTo(b.bezeichnung);
					if (cmp == 0)
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
	 * @param merkmale     			          die Liste der Merkmale
	 */
	public MerkmaleListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> abschnitte, final Schulform schulform,
			final @NotNull List<Merkmal> merkmale) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, abschnitte, schulform, merkmale, MerkmaleListeManager.comparator,
				_merkmalToId, _merkmalToId, List.of());
	}

	@Override
	protected boolean checkFilter(final Merkmal merkmal) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Merkmal a, final @NotNull Merkmal b) {
		return comparator.compare(a, b);
	}

}
