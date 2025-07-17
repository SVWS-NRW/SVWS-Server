package de.svws_nrw.core.utils.kataloge.entlassgruende;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class EntlassgruendeListeManager extends AuswahlManager<Long, KatalogEntlassgrund, KatalogEntlassgrund> {

	private static final @NotNull Function<KatalogEntlassgrund, Long> _entlassgrundToId = (final @NotNull KatalogEntlassgrund a) -> a.id;

	/** Ein Default-Comparator für den Vergleich von Abteilungen. */
	public static final @NotNull Comparator<KatalogEntlassgrund> comparator =
			(final @NotNull KatalogEntlassgrund a, final @NotNull KatalogEntlassgrund b) -> {
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
	 * @param entlassgruende     			  die Liste der Entlassgründe
	 */
	public EntlassgruendeListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<KatalogEntlassgrund> entlassgruende) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, entlassgruende, EntlassgruendeListeManager.comparator,
				_entlassgrundToId, _entlassgrundToId, List.of());
	}

	@Override
	protected boolean checkFilter(final KatalogEntlassgrund eintrag) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull KatalogEntlassgrund a, final @NotNull KatalogEntlassgrund b) {
		return comparator.compare(a, b);
	}
}
