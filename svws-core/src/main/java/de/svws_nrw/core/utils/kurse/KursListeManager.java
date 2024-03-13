package de.svws_nrw.core.utils.kurse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.fach.FachUtils;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.lehrer.LehrerUtils;
import de.svws_nrw.core.utils.schueler.SchuelerUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Kurs-Listen.
 */
public final class KursListeManager extends AuswahlManager<@NotNull Long, @NotNull KursDaten, @NotNull KursDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<@NotNull KursDaten, @NotNull Long> _kursToId = (final @NotNull KursDaten k) -> k.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<@NotNull Boolean, @NotNull Long, @NotNull KursDaten> _mapKursIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KursDaten> _mapKursInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KursDaten> _mapKursHatSchueler = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KursDaten> _mapLehrerInKurs = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull KursDaten> _mapKursHatFach = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull String, @NotNull Long, @NotNull KursDaten> _mapKursInSchulgliederung = new HashMap2D<>();
	private final @NotNull HashMap<@NotNull String, @NotNull KursDaten> _mapKursByKuerzel = new HashMap<>();

	/** Das Filter-Attribut für die Jahrgänge */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull JahrgangsDaten> jahrgaenge;
	private static final @NotNull Function<@NotNull JahrgangsDaten, @NotNull Long> _jahrgangToId = (final @NotNull JahrgangsDaten jg) -> jg.id;

	/** Das Filter-Attribut für die Lehrer */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull LehrerListeEintrag> lehrer;
	private static final @NotNull Function<@NotNull LehrerListeEintrag, @NotNull Long> _lehrerToId = (final @NotNull LehrerListeEintrag l) -> l.id;

	/** Das Filter-Attribut für die Fächer */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull FachDaten> faecher;
	private static final @NotNull Function<@NotNull FachDaten, @NotNull Long> _fachToId = (final @NotNull FachDaten f) -> f.id;

	/** Das Filter-Attribut für die Schüler */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull SchuelerListeEintrag> schueler;
	private static final @NotNull Function<@NotNull SchuelerListeEintrag, @NotNull Long> _schuelerToId = (final @NotNull SchuelerListeEintrag s) -> s.id;
	private @NotNull List<@NotNull Schueler> schuelerListe = new ArrayList<@NotNull Schueler>();

	/** Das Filter-Attribut für die Schulgliederungen */
	public final @NotNull AttributMitAuswahl<@NotNull String, @NotNull Schulgliederung> schulgliederungen;
	private static final @NotNull Function<@NotNull Schulgliederung, @NotNull String> _schulgliederungToId = (final @NotNull Schulgliederung sg) -> sg.daten.kuerzel;
	private static final @NotNull Comparator<@NotNull Schulgliederung> _comparatorSchulgliederung = (final @NotNull Schulgliederung a, final @NotNull Schulgliederung b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut für den Schüler-Status */
	public final @NotNull AttributMitAuswahl<@NotNull Integer, @NotNull SchuelerStatus> schuelerstatus;
	private static final @NotNull Function<@NotNull SchuelerStatus, @NotNull Integer> _schuelerstatusToId = (final @NotNull SchuelerStatus s) -> s.id;
	private static final @NotNull Comparator<@NotNull SchuelerStatus> _comparatorSchuelerStatus = (final @NotNull SchuelerStatus a, final @NotNull SchuelerStatus b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut auf nur sichtbare Kurse */
	private boolean _filterNurSichtbar = true;



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Kursauswahl bezieht
	 * @param schulform     die Schulform der Schule
	 * @param kurse         die Liste der Kurse
	 * @param schueler      die Liste der Schüler
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 * @param faecher       die Liste der Fächer
	 */
	public KursListeManager(final long schuljahresabschnitt, final Schulform schulform,
			final @NotNull List<@NotNull KursDaten> kurse,
			final @NotNull List<@NotNull SchuelerListeEintrag> schueler,
			final @NotNull List<@NotNull JahrgangsDaten> jahrgaenge,
			final @NotNull List<@NotNull LehrerListeEintrag> lehrer,
			final @NotNull List<@NotNull FachDaten> faecher) {
		super(schuljahresabschnitt, schulform, kurse, KursUtils.comparator, _kursToId, _kursToId,
				Arrays.asList(new Pair<>("kurse", true), new Pair<>("schueleranzahl", true)));
		this.schuelerstatus = new AttributMitAuswahl<>(Arrays.asList(SchuelerStatus.values()), _schuelerstatusToId, _comparatorSchuelerStatus, _eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl<>(schueler, _schuelerToId, SchuelerUtils.comparator, _eventSchuelerAuswahlChanged);
		this.jahrgaenge = new AttributMitAuswahl<>(jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator, _eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(lehrer, _lehrerToId, LehrerUtils.comparator, _eventHandlerFilterChanged);
		this.faecher = new AttributMitAuswahl<>(faecher, _fachToId, FachUtils.comparator, _eventHandlerFilterChanged);
		final @NotNull List<@NotNull Schulgliederung> gliederungen = (schulform == null) ? Arrays.asList(Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl<>(gliederungen, _schulgliederungToId, _comparatorSchulgliederung, _eventHandlerFilterChanged);
		initKurse();
		this.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
	}


	private void initKurse() {
		for (final @NotNull KursDaten k : this.liste.list()) {
			this._mapKursIstSichtbar.put(k.istSichtbar, k.id, k);
			for (final long idJahrgang : k.idJahrgaenge) {
				this._mapKursInJahrgang.put(idJahrgang, k.id, k);
				final JahrgangsDaten j = this.jahrgaenge.getOrException(idJahrgang);
				if (j.kuerzelSchulgliederung != null) {
					final Schulgliederung gliederung = this.schulgliederungen.get(j.kuerzelSchulgliederung);
					if (gliederung != null)
						this._mapKursInSchulgliederung.put(j.kuerzelSchulgliederung, k.id, k);
				}
			}
			for (final Schueler s : k.schueler)
				this._mapKursHatSchueler.put(s.id, k.id, k);
			if (k.lehrer != null)
				this._mapLehrerInKurs.put(k.lehrer, k.id, k);
			// TODO Zusatzkräfte ergänzen
			this._mapKursHatFach.put(k.idFach, k.id, k);
			if (k.kuerzel != null)
				this._mapKursByKuerzel.put(k.kuerzel, k);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull KursDaten eintrag, final @NotNull KursDaten daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Kursliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		// TODO Liste der Kurslehrer?
		schuelerListe = (daten != null) ? filterSchueler(daten) : new ArrayList<@NotNull Schueler>();
		return updateEintrag;
	}


	/**
	 * Gibt die Schulgliederungen für den aktuell ausgewählten Kurs zurück.
	 *
	 * @return die Schulgliederungen des Kurses
	 */
	public @NotNull List<@NotNull Schulgliederung> datenGetSchulgliederung() {
		final @NotNull List<@NotNull Schulgliederung> result = new ArrayList<>();
		if ((this._daten == null) || (this._daten.idJahrgaenge.isEmpty()))
			return result;
		for (final long idJahrgang : this._daten.idJahrgaenge) {
			final JahrgangsDaten j = this.jahrgaenge.getOrException(idJahrgang);
			if (j.kuerzelSchulgliederung != null)
				result.add(this.schulgliederungen.get(j.kuerzelSchulgliederung));
		}
		return result;
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
	 * Vergleicht zwei Kurslisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull KursDaten a, final @NotNull KursDaten b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kurse".equals(field)) {
				cmp = KursUtils.comparator.compare(a, b);
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
	protected boolean checkFilter(final @NotNull KursDaten eintrag) {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		if (this.faecher.auswahlExists() && !this.faecher.auswahlHasKey(eintrag.idFach))
			return false;
		if (this.jahrgaenge.auswahlExists()) {
			boolean hatEinenJahrgang = false;
			for (final long idJahrgang : eintrag.idJahrgaenge)
				if (this.jahrgaenge.auswahlHasKey(idJahrgang))
					hatEinenJahrgang = true;
			if (!hatEinenJahrgang)
				return false;
		}
		if (this.lehrer.auswahlExists()) {
			final boolean hatEinenLehrer = (eintrag.lehrer != null) && (this.lehrer.auswahlHasKey(eintrag.lehrer));
			/* TODO Zusatzkräfte
			for (final long idLehrer : eintrag.zusatzkraefte)
				if (this.lehrer.auswahlHasKey(idLehrer))
					hatEinenLehrer = true;
			*/
			if (!hatEinenLehrer)
				return false;
		}
		if (this.schulgliederungen.auswahlExists()) {
			if (eintrag.idJahrgaenge.isEmpty())
				return false;
			boolean hatEineSchulglierung = false;
			for (final long idJahrgang : eintrag.idJahrgaenge) {
				final JahrgangsDaten j = this.jahrgaenge.getOrException(idJahrgang);
				if ((j.kuerzelSchulgliederung != null) && (this.schulgliederungen.auswahlHasKey(j.kuerzelSchulgliederung))) {
					hatEineSchulglierung = true;
					break;
				}
			}
			if (!hatEineSchulglierung)
				return false;
		}
		if (this.schuelerstatus.auswahlExists())
			schuelerListe = filterSchueler(_daten);
		return true;
	}


	protected @NotNull List<@NotNull Schueler> filterSchueler(final KursDaten daten) {
		final @NotNull List<@NotNull Schueler> result = new ArrayList<>();
		if (daten != null)
			for (final @NotNull Schueler s : daten.schueler)
				if (!schuelerstatus.auswahlExists() || schuelerstatus.auswahlHasKey(s.status))
					result.add(s);
		return result;
	}


	/**
	 * Gibt die Schülerliste des aktuell ausgewählten Kurses zurück. Ist
	 * kein Kurs ausgewählt, so wird eine leere Liste zurückgegeben.
	 *
	 * @return die Liste der Schüler
	 */
	public @NotNull List<@NotNull Schueler> getSchuelerListe() {
		return this.schuelerListe;
	}


	protected final @NotNull Runnable _eventSchuelerAuswahlChanged = () -> {
		// TODO erstmal nichts zu tun ... wird später implementiert, wenn eine Checkbox zum Hinzufügen von Schülern zu einem Kurs verwendet wird
	};


	/**
	 * Gibt die Kursdaten anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Kursdaten oder null
	 */
	public KursDaten getByKuerzelOrNull(final @NotNull String kuerzel) {
		return this._mapKursByKuerzel.get(kuerzel);
	}

}
