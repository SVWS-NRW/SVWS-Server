package de.svws_nrw.core.utils.raum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.schule.Raum;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.utils.AuswahlManager;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Raum-Listen.
 */
public final class RaumListeManager extends AuswahlManager<Long, Raum, Raum> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<Raum, Long> _raumToId = (final @NotNull Raum f) -> f.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap<String, Raum> _mapRaumByKuerzel = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    		der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte			die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform						die Schulform der Schule
	 * @param raeume						die Liste der Raum
	 */
	public RaumListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<Raum> raeume) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, raeume, RaumUtils.comparator, _raumToId, _raumToId,
				Arrays.asList());
		onMehrfachauswahlChanged();
	}


	@Override
	protected void onMehrfachauswahlChanged() {
		this._mapRaumByKuerzel.clear();
		for (final @NotNull Raum f : this.liste.list()) {
			if (f.kuerzel != null)
				this._mapRaumByKuerzel.put(f.kuerzel, f);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull Raum eintrag, final @NotNull Raum daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Raumliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!daten.beschreibung.equals(eintrag.beschreibung)) {
			eintrag.beschreibung = daten.beschreibung;
			updateEintrag = true;
		}
		return updateEintrag;
	}


	/**
	 * Vergleicht zwei Raumlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull Raum a, final @NotNull Raum b) {
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kuerzel".equals(field)) {
				cmp = a.kuerzel.compareTo(b.kuerzel);
			} else if ("beschreibung".equals(field)) {
				cmp = a.beschreibung.compareTo(b.beschreibung);
			} else if ("groesse".equals(field)) {
				cmp = Long.compare(a.groesse, b.groesse);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return RaumUtils.comparator.compare(a, b);
	}


	@Override
	protected boolean checkFilter(final @NotNull Raum eintrag) {
		return true;
	}


	/**
	 * Gibt die Raum anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Raum oder null
	 */
	public Raum getByKuerzelOrNull(final @NotNull String kuerzel) {
		return this._mapRaumByKuerzel.get(kuerzel);
	}

	// public void useFilter(final @NotNull RaumListeManager srcManager) {
	// 	return;
	// }

}
