package de.svws_nrw.core.utils.jahrgang;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Jahrgangs-Listen.
 */
public final class JahrgangListeManager extends AuswahlManager<Long, JahrgangsDaten, JahrgangsDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<JahrgangsDaten, Long> _jahrgangToId = (final @NotNull JahrgangsDaten j) -> j.id;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param jahrgaenge       die Liste der Jahrgänge
	 */
	public JahrgangListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform, final @NotNull List<JahrgangsDaten> jahrgaenge) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, jahrgaenge, JahrgangsUtils.comparator, _jahrgangToId,
				_jahrgangToId,  Arrays.asList());
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull JahrgangsDaten eintrag, final @NotNull JahrgangsDaten daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Jahrgangsliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!daten.bezeichnung.equals(eintrag.bezeichnung)) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		if (!daten.anzahlRestabschnitte.equals(eintrag.anzahlRestabschnitte)) {
			eintrag.anzahlRestabschnitte = daten.anzahlRestabschnitte;
			updateEintrag = true;
		}

		return updateEintrag;
	}


	/**
	 * Vergleicht zwei JahrgangsDaten Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull JahrgangsDaten a, final @NotNull JahrgangsDaten b) {
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kuerzel".equals(field)) {
				if ((a.kuerzel == null) && (b.kuerzel != null)) {
					cmp = -1;
				} else if ((a.kuerzel != null) && (b.kuerzel == null)) {
					cmp = 1;
				} else if ((a.kuerzel != null) && (b.kuerzel != null)) {
					cmp = a.kuerzel.compareTo(b.kuerzel);
				}
			} else if ("bezeichnung".equals(field)) {
				cmp = a.bezeichnung.compareTo(b.bezeichnung);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JahrgangsUtils.comparator.compare(a, b);
	}


	@Override
	protected boolean checkFilter(final @NotNull JahrgangsDaten eintrag) {
		return true;
	}

}
