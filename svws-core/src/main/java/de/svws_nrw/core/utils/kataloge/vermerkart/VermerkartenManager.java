package de.svws_nrw.core.utils.kataloge.vermerkart;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.schueler.SchuelerVermerkartZusammenfassung;
import de.svws_nrw.core.data.schule.VermerkartEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.schueler.SchuelerUtils;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Ein Manager zum Verwalten der VermerkartEintrag-Listen.
 */
public final class VermerkartenManager extends AuswahlManager<Long, VermerkartEintrag, VermerkartEintrag> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<VermerkartEintrag, Long> _vermerkArtToId = (final @NotNull VermerkartEintrag vme) -> vme.id;

	private @NotNull AttributMitAuswahl<Long, SchuelerVermerkartZusammenfassung> listSchuelerVermerkartZusammenfassung;
	private static final @NotNull Function<SchuelerVermerkartZusammenfassung, @NotNull Long> _schuelerToId =
			(final @NotNull SchuelerVermerkartZusammenfassung s) -> s.id;

	protected static final @NotNull Runnable _dummyEvent = () -> { /* do nothing */	};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param listVermerkArtEintrag     					die Liste der Vermerkarten
	 * @param listSchuelerVermerkartZusammenfassung     	die Liste der SchuelerVermerkartZusammenfassung
	 */
	public VermerkartenManager(final @NotNull List<VermerkartEintrag> listVermerkArtEintrag,
			final @NotNull List<SchuelerVermerkartZusammenfassung> listSchuelerVermerkartZusammenfassung) {
		super(-1L, -1L, new ArrayList<>(), null, listVermerkArtEintrag, VermerkArtUtils.comparator, _vermerkArtToId, _vermerkArtToId,
				Arrays.asList(new Pair<>("VermerkArt", true), new Pair<>("schueleranzahl", true)));
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl<>(listSchuelerVermerkartZusammenfassung, _schuelerToId,
				SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, _dummyEvent);
	}

	/**
	 * Setzt die Liste der SchülervermerkartZusammenfassungen.
	 *
	 * @param listSchuelerVermerkartZusammenfassung Eine Liste von SchülervermerkartZusammenfassungen
	 */
	public void setListSchuelerVermerkartZusammenfassung(final @NotNull List<SchuelerVermerkartZusammenfassung> listSchuelerVermerkartZusammenfassung) {
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl<>(listSchuelerVermerkartZusammenfassung, _schuelerToId,
				SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, _dummyEvent);
	}

	/**
	 * Gibt die Liste der SchülervermerkartZusammenfassungen zurück.
	 *
	 * @return Eine Instanz von AttributMitAuswahl, die eine Liste von SchülervermerkartZusammenfassungen enthält.
	 */
	public @NotNull AttributMitAuswahl<Long, SchuelerVermerkartZusammenfassung> getListSchuelerVermerkartZusammenfassung() {
		return listSchuelerVermerkartZusammenfassung;
	}

	/**
	 * Vergleicht zwei VermerkArtEinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull VermerkartEintrag a, final @NotNull VermerkartEintrag b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("VermerkArt".equals(field)) {
				cmp = VermerkArtUtils.comparator.compare(a, b);
			} else if ("schueleranzahl".equals(field)) {
				cmp = Integer.compare(a.anzahlVermerke, b.anzahlVermerke);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	@Override
	protected boolean checkFilter(final @NotNull VermerkartEintrag eintrag) {
		return true;
	}

}
