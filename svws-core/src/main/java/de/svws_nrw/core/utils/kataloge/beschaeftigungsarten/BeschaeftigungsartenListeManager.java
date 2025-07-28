package de.svws_nrw.core.utils.kataloge.beschaeftigungsarten;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.betrieb.Beschaeftigungsart;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class BeschaeftigungsartenListeManager extends AuswahlManager<Long, Beschaeftigungsart, Beschaeftigungsart> {

	private static final @NotNull Function<Beschaeftigungsart, Long> _beschaeftigungsartToId = (final @NotNull Beschaeftigungsart a) -> a.id;

	/** Ein Default-Comparator für den Vergleich von Beschäftigungsarten. */
	public static final @NotNull Comparator<Beschaeftigungsart> comparator =
			(final @NotNull Beschaeftigungsart a, final @NotNull Beschaeftigungsart b) -> {
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
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param beschaeftigungsarten				die Liste der Beschäftigungsarten
	 */
	public BeschaeftigungsartenListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<Beschaeftigungsart> beschaeftigungsarten) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, beschaeftigungsarten, BeschaeftigungsartenListeManager.comparator,
				_beschaeftigungsartToId, _beschaeftigungsartToId, List.of());
	}

	@Override
	protected boolean checkFilter(final Beschaeftigungsart eintrag) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Beschaeftigungsart a, final @NotNull Beschaeftigungsart b) {
		return comparator.compare(a, b);
	}

}
