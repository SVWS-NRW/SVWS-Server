package de.svws_nrw.core.utils.kataloge.abteilungen;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Ein Manager zum Verwalten der Listen von Abteilungen.
 */
public final class AbteilungenListeManager extends AuswahlManager<Long, Abteilung, Abteilung> {


	private static final @NotNull Function<Abteilung, Long> _abteilungToId = (final @NotNull Abteilung a) -> a.id;

	private final @NotNull Map<Long, LehrerListeEintrag> _lehrer;

	/** Ein Default-Comparator für den Vergleich von Abteilungen. */
	public static final @NotNull Comparator<Abteilung> comparator =
			(final @NotNull Abteilung a, final @NotNull Abteilung b) -> {
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
	 * @param idSchuljahresabschnittAuswahl    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param abteilungen     				  die Liste der Abteilungen
	 * @param lehrer     					  die Liste der Lehrer
	 */
	public AbteilungenListeManager(final long idSchuljahresabschnittAuswahl, final long idSchuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<Abteilung> abteilungen, final @NotNull List<LehrerListeEintrag> lehrer) {
		super(idSchuljahresabschnittAuswahl, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, abteilungen, AbteilungenListeManager.comparator,
				_abteilungToId, _abteilungToId, Arrays.asList());
		this._lehrer = mapLehrer(lehrer);
	}

	private static @NotNull Map<Long, LehrerListeEintrag> mapLehrer(final @NotNull List<LehrerListeEintrag> lehrerListe) {
		final Map<Long, LehrerListeEintrag> result = new HashMap<>();
		for (final LehrerListeEintrag v : lehrerListe)
			result.put(v.id, v);
		return result;
	}

	/**
	 * Ein Getter für die Liste der Lehrer
	 *
	 * @return lehrer
	 */
	public @NotNull Map<Long, LehrerListeEintrag> getLehrer() {
		return _lehrer;
	}

	/**
	 * Gibt einen Lehrer anhand der gegebenen ID zurück
	 *
	 * @param id	id des Lehrers
	 *
	 * @return		lehrer
	 */
	public LehrerListeEintrag getLehrerById(final long id) {
		return _lehrer.get(id);
	}

	@Override
	protected boolean checkFilter(final Abteilung eintrag) {
		return true;
	}

	@Override
	protected int compareAuswahl(final @NotNull Abteilung a, final @NotNull Abteilung b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("bezeichnung".equals(field))
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
			else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return comparator.compare(a, b);
	}

	// public void useFilter(final @NotNull AbteilungenListeManager srcManager) {
	// 	return;
	// }

}
