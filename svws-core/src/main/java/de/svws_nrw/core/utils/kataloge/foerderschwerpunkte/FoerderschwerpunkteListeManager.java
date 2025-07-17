package de.svws_nrw.core.utils.kataloge.foerderschwerpunkte;

import de.svws_nrw.asd.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class FoerderschwerpunkteListeManager extends AuswahlManager<Long, FoerderschwerpunktEintrag, FoerderschwerpunktEintrag> {

	private static final @NotNull Function<FoerderschwerpunktEintrag, Long> _foerderschwerpunktToId = (final @NotNull FoerderschwerpunktEintrag a) -> a.id;


	/** Ein Default-Comparator für den Vergleich von Förderschwerpunkten */
	public static final @NotNull Comparator<FoerderschwerpunktEintrag> comparator =
			(final @NotNull FoerderschwerpunktEintrag a, final @NotNull FoerderschwerpunktEintrag b) -> {
				int cmp;
				cmp = Integer.compare(a.sortierung, b.sortierung);
				if (cmp != 0)
					return cmp;

				if ((a.kuerzel != null) && (b.kuerzel != null)) {
					cmp = a.kuerzel.compareTo(b.kuerzel);
					if (cmp != 0)
						return cmp;
				}

				if ((a.text != null) && (b.text != null)) {
					cmp = a.text.compareTo(b.text);
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
	 * @param foerderschwerpunkte     			  die Liste der Foerderschwerpunkte
	 */
	public FoerderschwerpunkteListeManager(final long idSchuljahresabschnitt, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<FoerderschwerpunktEintrag> foerderschwerpunkte) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, foerderschwerpunkte,
				FoerderschwerpunkteListeManager.comparator, _foerderschwerpunktToId, _foerderschwerpunktToId, List.of());
	}


	/**
	 * Gibt die Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte zurück
	 *
	 * @return Liste der bislang nicht für diese Schule erstellten Förderschwerpunkte
	 */
	public @NotNull List<Foerderschwerpunkt> getAvailableFoerderschwerpunkte() {
		final @NotNull List<Foerderschwerpunkt> result = new ArrayList<>();

		for (final Foerderschwerpunkt f : Foerderschwerpunkt.getBySchuljahrAndSchulform(this.getSchuljahr(), schulform())) {
			boolean alreadyInList = false;
			for (final FoerderschwerpunktEintrag fe : this.liste.list()) {
				final FoerderschwerpunktKatalogEintrag daten = f.daten(this.getSchuljahr());
				if (daten == null)
					continue;
				if (daten.schluessel.equals(fe.kuerzelStatistik)) {
					alreadyInList = true;
					break;
				}
			}
			if (!alreadyInList)
				result.add(f);
		}
		return result;
	}

	@Override
	protected boolean checkFilter(final FoerderschwerpunktEintrag eintrag) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull FoerderschwerpunktEintrag a, final @NotNull FoerderschwerpunktEintrag b) {
		return comparator.compare(a, b);
	}

}


