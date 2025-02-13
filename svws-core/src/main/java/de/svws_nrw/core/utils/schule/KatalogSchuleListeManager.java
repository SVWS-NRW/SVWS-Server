package de.svws_nrw.core.utils.schule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.core.data.schule.SchulenKatalogEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AuswahlManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der SchulEintrag Katalogliste.
 */
public final class KatalogSchuleListeManager extends AuswahlManager<Long, SchulEintrag, SchulEintrag> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<SchulEintrag, Long> _schuleToId = (final @NotNull SchulEintrag schulEintrag) -> schulEintrag.id;

	/** die Liste der Schulen aus Gesamt-NRW */
	private final @NotNull List<SchulenKatalogEintrag> schulenKatalogEintraege;

	/** Ein Default-Comparator für den Vergleich von Schulen im Schule Katalog. */
	public static final @NotNull Comparator<SchulEintrag> _defaultComparator = (final @NotNull SchulEintrag a, final @NotNull SchulEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;

		if ((a.kuerzel == null) && (b.kuerzel != null)) {
			return 1;
		} else if ((a.kuerzel != null) && (b.kuerzel == null)) {
			return -1;
		} else if ((a.kuerzel != null)) {
			cmp = a.kuerzel.compareTo(b.kuerzel);
		}
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt			 der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte          die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule     der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform						 die Schulform der Schule
	 * @param schulen						 die Liste der Schulen
	 * @param schulenKatalogEintraege        die Liste der Schulen aus Gesamt-NRW
	 */
	public KatalogSchuleListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform, final @NotNull List<SchulEintrag> schulen,
			final @NotNull List<SchulenKatalogEintrag> schulenKatalogEintraege) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, schulen, _defaultComparator, _schuleToId,
				_schuleToId,  Arrays.asList());
		this.schulenKatalogEintraege = schulenKatalogEintraege;
	}

	/**
	 * Getter der Gesamtliste der Schulen aus NRW
	 *
	 * @return die Liste der Schulen aus Gesamt-NRW
	 */
	public @NotNull List<SchulenKatalogEintrag> getSchulenKatalogEintraege() {
		return new ArrayList<>(this.schulenKatalogEintraege);
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull SchulEintrag eintrag, final @NotNull SchulEintrag daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Jahrgangsliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!daten.kurzbezeichnung.equals(eintrag.kurzbezeichnung)) {
			eintrag.kurzbezeichnung = daten.kurzbezeichnung;
			updateEintrag = true;
		}

		return updateEintrag;
	}


	/**
	 * Vergleicht zwei SchulEintrag Objekten, anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull SchulEintrag a, final @NotNull SchulEintrag b) {
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kuerzel".equals(field)) {
				if ((a.kuerzel == null) && (b.kuerzel != null)) {
					cmp = 1;
				} else if ((a.kuerzel != null) && (b.kuerzel == null)) {
					cmp = -1;
				} else if ((a.kuerzel != null)) {
					cmp = a.kuerzel.compareTo(b.kuerzel);
				}
			} else if ("kurzbezeichnung".equals(field)) {
				if ((a.kurzbezeichnung == null) && (b.kurzbezeichnung != null)) {
					cmp = 1;
				} else if ((a.kurzbezeichnung != null) && (b.kurzbezeichnung == null)) {
					cmp = -1;
				} else if ((a.kurzbezeichnung != null)) {
					cmp = a.kurzbezeichnung.compareTo(b.kurzbezeichnung);
				}
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return _defaultComparator.compare(a, b);
	}

	@Override
	protected boolean checkFilter(final @NotNull SchulEintrag eintrag) {
		return true;
	}

}
