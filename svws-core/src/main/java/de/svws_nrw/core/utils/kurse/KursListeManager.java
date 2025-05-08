package de.svws_nrw.core.utils.kurse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.lehrer.LehrerUtils;
import de.svws_nrw.core.utils.schueler.SchuelerUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Kurs-Listen.
 */
public final class KursListeManager extends AuswahlManager<Long, KursDaten, KursDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<KursDaten, Long> _kursToId = (final @NotNull KursDaten k) -> k.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<Boolean, Long, KursDaten> _mapKursIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, KursDaten> _mapKursInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, KursDaten> _mapKursHatSchueler = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, KursDaten> _mapLehrerInKurs = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, KursDaten> _mapKursHatFach = new HashMap2D<>();
	private final @NotNull HashMap2D<String, Long, KursDaten> _mapKursInSchulgliederung = new HashMap2D<>();
	private final @NotNull HashMap2D<String, Long, KursDaten> _mapKursByKuerzelAndJahrgang = new HashMap2D<>();

	/** Das Filter-Attribut für die Jahrgänge */
	public final @NotNull AttributMitAuswahl<Long, JahrgangsDaten> jahrgaenge;
	private static final @NotNull Function<JahrgangsDaten, Long> _jahrgangToId = (final @NotNull JahrgangsDaten jg) -> jg.id;

	/** Das Filter-Attribut für die Lehrer */
	public final @NotNull AttributMitAuswahl<Long, LehrerListeEintrag> lehrer;
	private static final @NotNull Function<LehrerListeEintrag, Long> _lehrerToId = (final @NotNull LehrerListeEintrag l) -> l.id;

	/** Das Filter-Attribut für die Fächer */
	public final @NotNull AttributMitAuswahl<Long, FaecherListeEintrag> faecher;
	private static final @NotNull Function<FaecherListeEintrag, Long> _fachToId = (final @NotNull FaecherListeEintrag f) -> f.id;

	/** Das Filter-Attribut für die Schüler */
	public final @NotNull AttributMitAuswahl<Long, SchuelerListeEintrag> schueler;
	private static final @NotNull Function<SchuelerListeEintrag, Long> _schuelerToId = (final @NotNull SchuelerListeEintrag s) -> s.id;
	private List<Schueler> _filteredSchuelerListe = null;

	/** Das Filter-Attribut für die Schulgliederungen */
	public final @NotNull AttributMitAuswahl<String, Schulgliederung> schulgliederungen;
	private final @NotNull Function<Schulgliederung, String> _schulgliederungToId = (final @NotNull Schulgliederung sg) -> {
		final SchulgliederungKatalogEintrag sglke = sg.daten(getSchuljahr());
		if (sglke == null)
			throw new IllegalArgumentException("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.".formatted(sg.name(), getSchuljahr()));
		return sglke.kuerzel;
	};
	private static final @NotNull Comparator<Schulgliederung> _comparatorSchulgliederung =
			(final @NotNull Schulgliederung a, final @NotNull Schulgliederung b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut für den Schüler-Status */
	public final @NotNull AttributMitAuswahl<Integer, SchuelerStatus> schuelerstatus;
	private final @NotNull Function<SchuelerStatus, Integer> _schuelerstatusToId = (final @NotNull SchuelerStatus s) -> {
		final SchuelerStatusKatalogEintrag sske = s.daten(getSchuljahr());
		if (sske == null)
			throw new IllegalArgumentException("Der Schülerstatus %s ist in dem Schuljahr %d nicht gültig.".formatted(s.name(), getSchuljahr()));
		return Integer.parseInt(sske.kuerzel);
	};
	private static final @NotNull Comparator<SchuelerStatus> _comparatorSchuelerStatus =
			(final @NotNull SchuelerStatus a, final @NotNull SchuelerStatus b) -> a.ordinal() - b.ordinal();

	/** Ein Default-Comparator für den Vergleich von Fächern in Fächerlisten. */
	public static final @NotNull Comparator<FaecherListeEintrag> comparatorFaecherListe = (final @NotNull FaecherListeEintrag a, final @NotNull FaecherListeEintrag b) -> {
		int cmp = a.sortierung - b.sortierung;
		if (cmp != 0)
			return cmp;
		cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/** Das Filter-Attribut auf nur sichtbare Kurse */
	private boolean _filterNurSichtbar = true;



	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Kursauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param kurse         die Liste der Kurse
	 * @param schueler      die Liste der Schüler
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 * @param faecher       die Liste der Fächer
	 */
	public KursListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<KursDaten> kurse,
			final @NotNull List<SchuelerListeEintrag> schueler,
			final @NotNull List<JahrgangsDaten> jahrgaenge,
			final @NotNull List<LehrerListeEintrag> lehrer,
			final @NotNull List<FaecherListeEintrag> faecher) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, kurse, KursUtils.comparator, _kursToId, _kursToId,
				Arrays.asList(new Pair<>("idJahrgaenge", true), new Pair<>("kuerzel", true)));
		this.schuelerstatus =
				new AttributMitAuswahl<>(Arrays.asList(SchuelerStatus.values()), _schuelerstatusToId, _comparatorSchuelerStatus, _eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl<>(schueler, _schuelerToId, SchuelerUtils.comparator, _eventHandlerFilterChanged);
		this.jahrgaenge = new AttributMitAuswahl<>(jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator, _eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(lehrer, _lehrerToId, LehrerUtils.comparator, _eventHandlerFilterChanged);
		this.faecher = new AttributMitAuswahl<>(faecher, _fachToId, comparatorFaecherListe, _eventHandlerFilterChanged);
		final @NotNull List<Schulgliederung> gliederungen =
				(schulform == null) ? Arrays.asList(Schulgliederung.values()) : Schulgliederung.getBySchuljahrAndSchulform(getSchuljahr(), schulform);
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
				for (final long idJahrgang : k.idJahrgaenge)
					this._mapKursByKuerzelAndJahrgang.put(k.kuerzel, idJahrgang, k);
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
		_filteredSchuelerListe = null;
		return updateEintrag;
	}


	/**
	 * Gibt die Schulgliederungen für den aktuell ausgewählten Kurs zurück.
	 *
	 * @return die Schulgliederungen des Kurses
	 */
	public @NotNull List<Schulgliederung> datenGetSchulgliederung() {
		final @NotNull List<Schulgliederung> result = new ArrayList<>();
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
		for (final Pair<String, Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("kuerzel".equals(field)) {
				cmp = KursUtils.comparator.compare(a, b);
			} else if ("lehrer".equals(field)) {
				if ((a.lehrer == null) && (b.lehrer == null)) {
					cmp = 0;
				} else if (a.lehrer == null) {
					cmp = -1;
				} else if (b.lehrer == null) {
					cmp = 1;
				} else {
					final LehrerListeEintrag la = this.lehrer.get(a.lehrer);
					final LehrerListeEintrag lb = this.lehrer.get(b.lehrer);
					if ((la == null) && (lb == null))
						cmp = 0;
					else if (la == null)
						cmp = -1;
					else if (lb == null)
						cmp = 1;
					else
						cmp = LehrerUtils.comparator.compare(la, lb);
				}
			} else if ("idJahrgaenge".equals(field)) {
				// TODO Unterstützung für Kurse mit mehreren Jahrgängen
				if ((a.idJahrgaenge.isEmpty()) && (b.idJahrgaenge.isEmpty())) {
					cmp = 0;
				} else if (a.idJahrgaenge.isEmpty()) {
					cmp = -1;
				} else if (b.idJahrgaenge.isEmpty()) {
					cmp = 1;
				} else {
					final JahrgangsDaten ja = this.jahrgaenge.get(a.idJahrgaenge.get(0));
					final JahrgangsDaten jb = this.jahrgaenge.get(b.idJahrgaenge.get(0));
					if ((ja == null) && (jb == null))
						cmp = 0;
					else if (ja == null)
						cmp = -1;
					else if (jb == null)
						cmp = 1;
					else
						cmp = JahrgangsUtils.comparator.compare(ja, jb);
				}
			} else if ("schueler".equals(field)) {
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
		this._filteredSchuelerListe = null;
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
		if (this.schueler.auswahlExists()) {
			boolean hatEinenSchueler = false;
			for (final Schueler s : eintrag.schueler)
				if (this.schueler.auswahlHasKey(s.id))
					hatEinenSchueler = true;
			if (!hatEinenSchueler)
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
		return true;
	}


	/**
	 * Gibt die Schülerliste des aktuell ausgewählten Kurses zurück. Ist
	 * kein Kurs ausgewählt, so wird eine leere Liste zurückgegeben.
	 *
	 * @return die Liste der Schüler
	 */
	public @NotNull List<Schueler> getSchuelerListe() {
		if (_filteredSchuelerListe == null) {
			_filteredSchuelerListe = new ArrayList<>();
			if (_daten != null)
				for (final @NotNull Schueler s : _daten.schueler)
					if (!schuelerstatus.auswahlExists() || schuelerstatus.auswahlHasKey(s.status))
						_filteredSchuelerListe.add(s);
		}
		return this._filteredSchuelerListe;
	}


	/**
	 * Trigger, wenn eine Checkbox zum Hinzufügen von Schülern zu einem Kurs verwendet wird.
	 */
	protected final @NotNull Runnable _eventSchuelerAuswahlChanged = () -> {
		// TODO erstmal nichts zu tun ... wird später implementiert, wenn eine Checkbox zum Hinzufügen von Schülern zu einem Kurs verwendet wird
	};


	/**
	 * Gibt die Kursdaten anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel      das Kürzel
	 * @param idJahrgang   die ID des Jahrgangs
	 *
	 * @return die Kursdaten oder null
	 */
	public KursDaten getByKuerzelAndJahrgangOrNull(final @NotNull String kuerzel, final long idJahrgang) {
		return this._mapKursByKuerzelAndJahrgang.getOrNull(kuerzel, idJahrgang);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link KursListeManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public void useFilter(final @NotNull KursListeManager srcManager) {
		this.jahrgaenge.setAuswahl(srcManager.jahrgaenge);
		this.lehrer.setAuswahl(srcManager.lehrer);
		this.schulgliederungen.setAuswahl(srcManager.schulgliederungen);
		this.faecher.setAuswahl(srcManager.faecher);
		this.schueler.setAuswahl(srcManager.schueler);
		this.setFilterNurSichtbar(srcManager._filterNurSichtbar);
	}

}
