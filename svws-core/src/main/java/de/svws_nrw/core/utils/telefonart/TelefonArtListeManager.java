package de.svws_nrw.core.utils.telefonart;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.TelefonArt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager für die Telefonarten.
 */
public final class TelefonArtListeManager extends AuswahlManager<Long, TelefonArt, TelefonArt> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<TelefonArt, Long> _telefonArtenToId = (final @NotNull TelefonArt ta) -> ta.id;

	/** Sets mit Listen zur aktuellen Auswahl */
	private final @NotNull HashSet<Long> setTelefonArtIDsMitPersonen = new HashSet<>();

	/** Ein Default-Comparator für den Vergleich von Telefonarten in Telefonartlisten. */
	public static final @NotNull Comparator<TelefonArt> comparator =
			(final @NotNull TelefonArt a, final @NotNull TelefonArt b) -> {
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
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Telefonart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param listTelefonArt     	       die Liste der Telefonart
	 */
	public TelefonArtListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform, final @NotNull List<TelefonArt> listTelefonArt) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listTelefonArt, TelefonArtListeManager.comparator,
				_telefonArtenToId, _telefonArtenToId, Arrays.asList(new Pair<>("telefonArt", true)));
	}

	/** Gibt das Set mit den TelefonArtIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Telefonarten, die Schüler haben
	 */
	public @NotNull Set<Long> getTelefonArtIDsMitPersonen() {
		return this.setTelefonArtIDsMitPersonen;
	}

	@Override
	protected boolean onSetDaten(final @NotNull TelefonArt eintrag, final @NotNull TelefonArt daten) {
		boolean updateEintrag = false;
		if (!daten.bezeichnung.equals(eintrag.bezeichnung)) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	@Override
	protected void onMehrfachauswahlChanged() {
		this.setTelefonArtIDsMitPersonen.clear();
		for (final @NotNull TelefonArt t : this.liste.auswahl())
			if (t.anzahlTelefonnummern != 0)
				this.setTelefonArtIDsMitPersonen.add(t.id);
	}

	@Override
	protected int compareAuswahl(final @NotNull TelefonArt a, final @NotNull TelefonArt b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("telefonArt".equals(field)) {
				cmp = TelefonArtListeManager.comparator.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	@Override
	protected boolean checkFilter(final @NotNull TelefonArt eintrag) {
		return true;
	}
}
