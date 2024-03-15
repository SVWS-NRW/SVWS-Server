package de.svws_nrw.core.utils.klassen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.lehrer.LehrerUtils;
import de.svws_nrw.core.utils.schueler.SchuelerUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Klassen-Listen.
 */
public final class KlassenListeManager extends AuswahlManager<@NotNull Long, @NotNull KlassenDaten, @NotNull KlassenDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<@NotNull KlassenDaten, @NotNull Long> _klasseToId = (final @NotNull KlassenDaten k) -> k.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<@NotNull Boolean, @NotNull Long, @NotNull KlassenDaten> _mapKlasseIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KlassenDaten> _mapKlasseInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KlassenDaten> _mapKlasseHatSchueler = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KlassenDaten> _mapKlassenlehrerInKlasse = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull String, @NotNull Long, @NotNull KlassenDaten> _mapKlasseInSchulgliederung = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull String, @NotNull KlassenDaten> _mapKlasseByKuerzel = new HashMap<>();

	/** Das Filter-Attribut für die Jahrgänge */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull JahrgangsDaten> jahrgaenge;
	private static final @NotNull Function<@NotNull JahrgangsDaten, @NotNull Long> _jahrgangToId = (final @NotNull JahrgangsDaten jg) -> jg.id;

	/** Das Filter-Attribut für die Lehrer */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull LehrerListeEintrag> lehrer;
	private static final @NotNull Function<@NotNull LehrerListeEintrag, @NotNull Long> _lehrerToId = (final @NotNull LehrerListeEintrag l) -> l.id;

	/** Das Filter-Attribut für die Schüler */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull SchuelerListeEintrag> schueler;
	private static final @NotNull Function<@NotNull SchuelerListeEintrag, @NotNull Long> _schuelerToId = (final @NotNull SchuelerListeEintrag s) -> s.id;
	private List<@NotNull Schueler> _filteredSchuelerListe = null;

	/** Das Filter-Attribut für die Schulgliederungen */
	public final @NotNull AttributMitAuswahl<@NotNull String, @NotNull Schulgliederung> schulgliederungen;
	private static final @NotNull Function<@NotNull Schulgliederung, @NotNull String> _schulgliederungToId = (final @NotNull Schulgliederung sg) -> sg.daten.kuerzel;
	private static final @NotNull Comparator<@NotNull Schulgliederung> _comparatorSchulgliederung = (final @NotNull Schulgliederung a, final @NotNull Schulgliederung b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut für den Schüler-Status */
	public final @NotNull AttributMitAuswahl<@NotNull Integer, @NotNull SchuelerStatus> schuelerstatus;
	private static final @NotNull Function<@NotNull SchuelerStatus, @NotNull Integer> _schuelerstatusToId = (final @NotNull SchuelerStatus s) -> s.id;
	private static final @NotNull Comparator<@NotNull SchuelerStatus> _comparatorSchuelerStatus = (final @NotNull SchuelerStatus a, final @NotNull SchuelerStatus b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut auf nur sichtbare Klassen */
	private boolean _filterNurSichtbar = true;



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schulform     die Schulform der Schule
	 * @param klassen       die Liste der Klassen
	 * @param schueler      die Liste der Schüler
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 */
	public KlassenListeManager(final long schuljahresabschnitt, final Schulform schulform,
			final @NotNull List<@NotNull KlassenDaten> klassen,
			final @NotNull List<@NotNull SchuelerListeEintrag> schueler,
			final @NotNull List<@NotNull JahrgangsDaten> jahrgaenge,
			final @NotNull List<@NotNull LehrerListeEintrag> lehrer) {
		super(schuljahresabschnitt, schulform, klassen, KlassenUtils.comparator, _klasseToId, _klasseToId,
				Arrays.asList(new Pair<>("klassen", true), new Pair<>("schueleranzahl", true)));
		this.schuelerstatus = new AttributMitAuswahl<>(Arrays.asList(SchuelerStatus.values()), _schuelerstatusToId, _comparatorSchuelerStatus, _eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl<>(schueler, _schuelerToId, SchuelerUtils.comparator, _eventSchuelerAuswahlChanged);
		this.jahrgaenge = new AttributMitAuswahl<>(jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator, _eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(lehrer, _lehrerToId, LehrerUtils.comparator, _eventHandlerFilterChanged);
		final @NotNull List<@NotNull Schulgliederung> gliederungen = (schulform == null) ? Arrays.asList(Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl<>(gliederungen, _schulgliederungToId, _comparatorSchulgliederung, _eventHandlerFilterChanged);
		initKlassen();
		this.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
	}


	private void initKlassen() {
		for (final @NotNull KlassenDaten k : this.liste.list()) {
			this._mapKlasseIstSichtbar.put(k.istSichtbar, k.id, k);
			if (k.idJahrgang != null) {
				this._mapKlasseInJahrgang.put(k.idJahrgang, k.id, k);
				final JahrgangsDaten j = this.jahrgaenge.getOrException(k.idJahrgang);
				if (j.kuerzelSchulgliederung != null) {
					final Schulgliederung gliederung = this.schulgliederungen.get(j.kuerzelSchulgliederung);
					if (gliederung != null)
						this._mapKlasseInSchulgliederung.put(j.kuerzelSchulgliederung, k.id, k);
				}
			}
			for (final Schueler s : k.schueler)
				this._mapKlasseHatSchueler.put(s.id, k.id, k);
			for (final Long l : k.klassenLeitungen)
				this._mapKlassenlehrerInKlasse.put(l, k.id, k);
			if (k.kuerzel != null)
				this._mapKlasseByKuerzel.put(k.kuerzel, k);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull KlassenDaten eintrag, final @NotNull KlassenDaten daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Klassenliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		// TODO Liste der Klassenlehrer?
		_filteredSchuelerListe = null;
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
		final JahrgangsDaten j = this.jahrgaenge.getOrException(this._daten.idJahrgang);
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
	@Override
	protected int compareAuswahl(final @NotNull KlassenDaten a, final @NotNull KlassenDaten b) {
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


	@Override
	protected boolean checkFilter(final @NotNull KlassenDaten eintrag) {
		this._filteredSchuelerListe = null;
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang == null) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang))))
			return false;
		if (this.lehrer.auswahlExists()) {
			boolean hatEinenLehrer = false;
			for (final long idLehrer : eintrag.klassenLeitungen)
				if (this.lehrer.auswahlHasKey(idLehrer))
					hatEinenLehrer = true;
			if (!hatEinenLehrer)
				return false;
		}
		if (this.schulgliederungen.auswahlExists()) {
			if (eintrag.idJahrgang == null)
				return false;
			final JahrgangsDaten j = this.jahrgaenge.getOrException(eintrag.idJahrgang);
			if ((j.kuerzelSchulgliederung == null) || ((j.kuerzelSchulgliederung != null) && (!this.schulgliederungen.auswahlHasKey(j.kuerzelSchulgliederung))))
				return false;
		}
		return true;
	}


	/**
	 * Gibt die Schülerliste der aktuelle ausgewählten Klasse zurück. Ist
	 * keine Klasse ausgewählt, so wird eine leere Liste zurückgegeben.
	 *
	 * @return die Liste der Schüler
	 */
	public @NotNull List<@NotNull Schueler> getSchuelerListe() {
		if (_filteredSchuelerListe == null) {
			_filteredSchuelerListe = new ArrayList<>();
			if (_daten != null)
				for (final @NotNull Schueler s : _daten.schueler)
					if (!schuelerstatus.auswahlExists() || schuelerstatus.auswahlHasKey(s.status))
						_filteredSchuelerListe.add(s);
		}
		return this._filteredSchuelerListe;
	}


	protected final @NotNull Runnable _eventSchuelerAuswahlChanged = () -> {
		// TODO erstmal nichts zu tun ... wird später implementiert, wenn eine Checkbox zum Hinzufügen von Schülern zu einer Klasse verwendet wird
	};


	/**
	 * Gibt die Klassendaten anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Klassendaten oder null
	 */
	public KlassenDaten getByKuerzelOrNull(final @NotNull String kuerzel) {
		return this._mapKlasseByKuerzel.get(kuerzel);
	}

}
