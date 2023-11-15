package de.svws_nrw.core.utils.klassen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.jahrgang.JahrgangsListeEintrag;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.lehrer.LehrerUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Klassen-Listen.
 */
public class KlassenListeManager extends AuswahlManager<@NotNull Long, @NotNull KlassenListeEintrag, @NotNull KlassenDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<@NotNull KlassenListeEintrag, @NotNull Long> _klasseToId = (final @NotNull KlassenListeEintrag k) -> k.id;
	private static final @NotNull Function<@NotNull KlassenDaten, @NotNull Long> _klassenDatenToId = (final @NotNull KlassenDaten k) -> k.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<@NotNull Boolean, @NotNull Long, @NotNull KlassenListeEintrag> _mapKlasseIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KlassenListeEintrag> _mapKlasseInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KlassenListeEintrag> _mapKlasseHatSchueler = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KlassenListeEintrag> _mapKlassenlehrerInKlasse = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull String, @NotNull Long, @NotNull KlassenListeEintrag> _mapKlasseInSchulgliederung = new HashMap2D<>();

	/** Das Filter-Attribut für die Jahrgänge */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull JahrgangsListeEintrag> jahrgaenge;
	private static final @NotNull Function<@NotNull JahrgangsListeEintrag, @NotNull Long> _jahrgangToId = (final @NotNull JahrgangsListeEintrag jg) -> jg.id;

	/** Das Filter-Attribut für die Lehrer */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull LehrerListeEintrag> lehrer;
	private static final @NotNull Function<@NotNull LehrerListeEintrag, @NotNull Long> _lehrerToId = (final @NotNull LehrerListeEintrag l) -> l.id;

	/** Das Filter-Attribut für die Schulgliederungen */
	public final @NotNull AttributMitAuswahl<@NotNull String, @NotNull Schulgliederung> schulgliederungen;
	private static final @NotNull Function<@NotNull Schulgliederung, @NotNull String> _schulgliederungToId = (final @NotNull Schulgliederung sg) -> sg.daten.kuerzel;
	private static final @NotNull Comparator<@NotNull Schulgliederung> _comparatorSchulgliederung = (final @NotNull Schulgliederung a, final @NotNull Schulgliederung b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut auf nur sichtbare Klassen */
	private boolean _filterNurSichtbar = true;



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform     die Schulform der Schule
	 * @param klassen       die Liste der Klassen
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 */
	public KlassenListeManager(final Schulform schulform,
			final @NotNull List<@NotNull KlassenListeEintrag> klassen,
			final @NotNull List<@NotNull JahrgangsListeEintrag> jahrgaenge,
			final @NotNull List<@NotNull LehrerListeEintrag> lehrer) {
		super(schulform, klassen, KlassenUtils.comparator, _klasseToId, _klassenDatenToId,
				Arrays.asList(new Pair<>("klassen", true), new Pair<>("schueleranzahl", true)));
		this.jahrgaenge = new AttributMitAuswahl<>(jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator, _eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(lehrer, _lehrerToId, LehrerUtils.comparator, _eventHandlerFilterChanged);
		final @NotNull List<@NotNull Schulgliederung> gliederungen = (schulform == null) ? Arrays.asList(Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl<>(gliederungen, _schulgliederungToId, _comparatorSchulgliederung, _eventHandlerFilterChanged);
		initKlassen();
	}


	private void initKlassen() {
		for (final @NotNull KlassenListeEintrag k : this.liste.list()) {
			this._mapKlasseIstSichtbar.put(k.istSichtbar, k.id, k);
			if (k.idJahrgang != null) {
				this._mapKlasseInJahrgang.put(k.idJahrgang, k.id, k);
				final JahrgangsListeEintrag j = this.jahrgaenge.getOrException(k.idJahrgang);
				if (j.kuerzelSchulgliederung != null) {
					final Schulgliederung gliederung = this.schulgliederungen.get(j.kuerzelSchulgliederung);
					if (gliederung != null)
						this._mapKlasseInSchulgliederung.put(j.kuerzelSchulgliederung, k.id, k);
				}
			}
			for (final Schueler s : k.schueler)
				this._mapKlasseHatSchueler.put(s.id, k.id, k);
			for (final Long l : k.klassenLehrer)
				this._mapKlassenlehrerInKlasse.put(l, k.id, k);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull KlassenListeEintrag eintrag, final @NotNull KlassenDaten daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Klassenliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		// TODO Liste der Klassenlehrer?
		// TODO Liste der Schüler?
		return updateEintrag;
	}


	/**
	 * Gibt die Schulgliederung für die aktuell ausgewählte Klasse zurück.
	 *
	 * @return die Schulgliederung der Klasse
	 */
	public Schulgliederung datenGetSchulgliederung() {
		if ((this._daten == null) || (this._daten.idJahrgang == null))
			return null;
		final JahrgangsListeEintrag j = this.jahrgaenge.getOrException(this._daten.idJahrgang);
		return (j.kuerzelSchulgliederung == null) ? null : this.schulgliederungen.get(j.kuerzelSchulgliederung);
	}


	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Klassen zurück.
	 *
	 * @return true, wenn nur nichtbare Klassen angezeigt werden und ansonsten false
	 */
	public boolean filterNurSichtbar() {
		return this._filterNurSichtbar;
	}


	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Klassen.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurSichtbar(final boolean value) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}


	/**
	 * Vergleicht zwei Klassenlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected int compare(final @NotNull KlassenListeEintrag a, final @NotNull KlassenListeEintrag b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("klassen".equals(field)) {
				cmp = KlassenUtils.comparator.compare(a, b);
			} else if ("schueleranzahl".equals(field)) {
				cmp = Integer.compare(a.schueler.size(), b.schueler.size());
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}


	/**
	 * Gibt eine gefilterte Liste der Klassen zurück. Als Filter werden dabei
	 * die Jahrgänge, die Klassenlehrer und ein Filter auf nur sichtbare Klassen beachtet.
	 *
	 * @return die gefilterte Liste
	 */
	@Override
	protected @NotNull List<@NotNull KlassenListeEintrag> onFilter() {
		final @NotNull List<@NotNull KlassenListeEintrag> tmpList = new ArrayList<>();
		for (final @NotNull KlassenListeEintrag eintrag : this.liste.list()) {
			if (this._filterNurSichtbar && !eintrag.istSichtbar)
				continue;
			if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang == null) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang))))
				continue;
			if (this.lehrer.auswahlExists()) {
				boolean hatEinenLehrer = false;
				for (final long idLehrer : eintrag.klassenLehrer)
					if (this.lehrer.auswahlHasKey(idLehrer))
						hatEinenLehrer = true;
				if (!hatEinenLehrer)
					continue;
			}
			if (this.schulgliederungen.auswahlExists()) {
				if (eintrag.idJahrgang == null)
					continue;
				final JahrgangsListeEintrag j = this.jahrgaenge.getOrException(eintrag.idJahrgang);
				if ((j.kuerzelSchulgliederung == null) || ((j.kuerzelSchulgliederung != null) && (!this.schulgliederungen.auswahlHasKey(j.kuerzelSchulgliederung))))
					continue;
			}
			tmpList.add(eintrag);
		}
		final @NotNull Comparator<@NotNull KlassenListeEintrag> comparator = (final @NotNull KlassenListeEintrag a, final @NotNull KlassenListeEintrag b) -> this.compare(a, b);
		tmpList.sort(comparator);
		return tmpList;
	}


}
