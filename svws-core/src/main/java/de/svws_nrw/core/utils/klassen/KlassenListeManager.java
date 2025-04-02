package de.svws_nrw.core.utils.klassen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.lehrer.LehrerUtils;
import de.svws_nrw.core.utils.schueler.SchuelerUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Klassen-Listen.
 */
public final class KlassenListeManager extends AuswahlManager<Long, KlassenDaten, KlassenDaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<KlassenDaten, Long> _klasseToId = (final @NotNull KlassenDaten k) -> k.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<Boolean, Long, KlassenDaten> _mapKlasseIstSichtbar = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, KlassenDaten> _mapKlasseInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, KlassenDaten> _mapKlasseHatSchueler = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, KlassenDaten> _mapKlassenlehrerInKlasse = new HashMap2D<>();
	private final @NotNull HashMap2D<String, Long, KlassenDaten> _mapKlasseInSchulgliederung = new HashMap2D<>();
	private final @NotNull HashMap<String, KlassenDaten> _mapKlasseByKuerzel = new HashMap<>();

	/** Sets mit Listen zur aktuellen Auswahl */
	private final @NotNull HashSet<Long> setKlassenIDsMitSchuelern = new HashSet<>();

	/** Die ausgewählte Klassenleitung */
	public LehrerListeEintrag auswahlKlassenLeitung = null;

	/** Das Filter-Attribut für die Jahrgänge */
	public final @NotNull AttributMitAuswahl<Long, JahrgangsDaten> jahrgaenge;
	private static final @NotNull Function<JahrgangsDaten, Long> _jahrgangToId = (final @NotNull JahrgangsDaten jg) -> jg.id;

	/** Das Filter-Attribut für die Lehrer */
	public final @NotNull AttributMitAuswahl<Long, LehrerListeEintrag> lehrer;
	private static final @NotNull Function<LehrerListeEintrag, Long> _lehrerToId = (final @NotNull LehrerListeEintrag l) -> l.id;

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

	/** Das Filter-Attribut auf nur sichtbare Klassen */
	private boolean _filterNurSichtbar = false;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param klassen       die Liste der Klassen
	 * @param schueler      die Liste der Schüler
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 */
	public KlassenListeManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<KlassenDaten> klassen,
			final @NotNull List<SchuelerListeEintrag> schueler,
			final @NotNull List<JahrgangsDaten> jahrgaenge,
			final @NotNull List<LehrerListeEintrag> lehrer) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, klassen, KlassenUtils.comparator, _klasseToId, _klasseToId,
				Arrays.asList(new Pair<>("klassen", true), new Pair<>("schueleranzahl", true)));
		this.schuelerstatus =
				new AttributMitAuswahl<>(Arrays.asList(SchuelerStatus.values()), _schuelerstatusToId, _comparatorSchuelerStatus, _eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl<>(schueler, _schuelerToId, SchuelerUtils.comparator, _eventSchuelerAuswahlChanged);
		this.jahrgaenge = new AttributMitAuswahl<>(jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator, _eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(lehrer, _lehrerToId, LehrerUtils.comparator, _eventHandlerFilterChanged);
		final @NotNull List<Schulgliederung> gliederungen =
				(schulform == null) ? Arrays.asList(Schulgliederung.values()) : Schulgliederung.getBySchuljahrAndSchulform(this.getSchuljahr(), schulform);
		this.schulgliederungen = new AttributMitAuswahl<>(gliederungen, _schulgliederungToId, _comparatorSchulgliederung, _eventHandlerFilterChanged);
		initKlassen();
		this.auswahlKlassenLeitung = null;
		this.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.NEUAUFNAHME);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.WARTELISTE);
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


	@Override
	protected boolean onSetDaten(final @NotNull KlassenDaten eintrag, final @NotNull KlassenDaten daten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten in der Klassenliste an ... (beim Patchen der Daten)
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}

		// Setze Klassenleitungs-Auswahl zurück, falls vorhanden
		if (auswahlKlassenLeitung != null) {
			auswahlKlassenLeitung = null;
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
	 * @param value   true, wenn der Filter aktiviert werden soll und ansonsten false
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
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull KlassenDaten a, final @NotNull KlassenDaten b) {
		for (final Pair<String, Boolean> criteria : _order) {
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
	protected void onMehrfachauswahlChanged() {
		this.setKlassenIDsMitSchuelern.clear();
		for (final @NotNull KlassenDaten k : this.liste.auswahl())
			if (!k.schueler.isEmpty())
				this.setKlassenIDsMitSchuelern.add(k.id);
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

	/** Gibt das Set mit den KlassenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Klassen, die Schüler haben
	*/
	public @NotNull Set<Long> getKlassenIDsMitSchuelern() {
		return this.setKlassenIDsMitSchuelern;
	}

	/**
	 * Gibt die ausgewählte Klassenleitung zurück
	 *
	 * @return die ausgewählte Klassenleitung
	 */
	public LehrerListeEintrag getAuswahlKlassenLeitung() {
		return this.auswahlKlassenLeitung;
	}

	/**
	 * Setzt die angegebene Lehrkraft zur ausgewählten Klassenleitung
	 *
	 * @param klassenLeitung neue ausgewählte Klassenleitung
	 */
	public void setAuswahlKlassenLeitung(final LehrerListeEintrag klassenLeitung) {
		this.auswahlKlassenLeitung = klassenLeitung;
	}

	/**
	 * Trigger, wenn eine Checkbox zum Hinzufügen von Schülern zu einer Klasse verwendet wird.
	 */
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


	/**
	 * Erhöht, bzw. senkt die Position der Klassenleitung mit der angegebenen Lehrer-ID auf der lokalen Klassenleitungs-Liste.
	 * Dabei wird der Reihenfolgen-Wert zwischen dem nächstgrößeren (bzw. nächskleineren) Eintrag
	 * und dem angegebenen Eintrag getauscht.
	 *
	 * @param klassenleitungen   die Liste der Klassenleitungen
	 * @param lehrerId           Lehrer-ID der zu höher- bzw. tieferstellenden Klassenleitung
	 * @param erhoehe            true, falls die Klassenleitung eine höhere Position auf der Klassenleitungs-Liste haben soll,
	 *                           false, wenn sie eine tiefere Position auf der Klassenleitungs-Liste haben soll.
	 *
	 * @return true, falls Änderungen durchgeführt wurden und ansonsten false
	 *
	 * @throws DeveloperNotificationException wenn die Klassen-Daten oder die übergebene Lehrer-ID ungültig sind
	 */
	public static boolean updateReihenfolgeKlassenleitung(final @NotNull List<Long> klassenleitungen, final long lehrerId, final boolean erhoehe)
			throws DeveloperNotificationException {
		// Ist diese Klassenleitung die einzige in der Klasse, dann wird keine Änderung durchgeführt...
		if (klassenleitungen.size() == 1)
			return false;

		// Bestimmt den Index des Lehrers in der Liste der Klassenleitungen
		final int posLehrer = klassenleitungen.indexOf(lehrerId);
		if (posLehrer < 0)
			throw new DeveloperNotificationException("Es wurde keine Klassenleitung mit der angegebenen Klassen- und Lehrer-ID gefunden.");

		if (erhoehe) {
			// Prüfe, ob die Klassenleitung bereits an oberster Stelle steht. Ist dies der Fall, so ist keine Änderung nötig...
			if (posLehrer == 0)
				return false;

			// Dreieckstausch mit der darüberstehenden Klassenleitung
			final long lehrerIdVorgaenger = klassenleitungen.get(posLehrer - 1);
			klassenleitungen.set(posLehrer, lehrerIdVorgaenger);
			klassenleitungen.set(posLehrer - 1, lehrerId);
			return true;

		}

		// Prüfe, ob die Klassenleitung bereits an unterster Stelle steht. Ist dies der Fall, so ist keine Änderung nötig...
		if ((posLehrer + 1) >= klassenleitungen.size())
			return false;

		// Dreieckstausch mit der darunterstehenden Klassenleitung
		final long lehrerIdNachfolger = klassenleitungen.get(posLehrer + 1);
		klassenleitungen.set(posLehrer, lehrerIdNachfolger);
		klassenleitungen.set(posLehrer + 1, lehrerId);
		return true;
	}

	/**
	 * Wenn das Kürzel nicht leer, für den Schuljahresabschnitt einzigartig und zwischen 1 und 15 Zeichen lang ist,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param kuerzel das Kürzel der Klasse
	 *
	 * @return <code>true</code> wenn Kürzel der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateKuerzel(final String kuerzel) {
		if ((kuerzel == null) || kuerzel.isBlank() || (kuerzel.trim().length() > 15))
			return false;

		for (final KlassenDaten klasse : this.liste.list())
			if ((this.auswahlID() != klasse.id) && klasse.kuerzel.equals(kuerzel.trim()))
				return false;

		return true;
	}

	/**
	 * Die Beschreibung ist optional und darf maximal 150 Zeichen lang sein.
	 *
	 * @param beschreibung die Beschreibung der Klasse
	 *
	 * @return <code>true</code> wenn Beschreibung der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateBeschreibung(final String beschreibung) {
		if (beschreibung == null)
			return true;
		return beschreibung.trim().length() <= 150;
	}

	/**
	 * Der Sortierungsindex darf nicht <code>null</code> sein und muss größer gleich 0 sein.
	 *
	 * @param sortierung der Sortierungsindex der Klasse
	 *
	 * @return <code>true</code> wenn Sortierung der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public boolean validateSortierung(final Integer sortierung) {
		return (sortierung != null) && (sortierung >= 0);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link KlassenListeManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public void useFilter(final @NotNull KlassenListeManager srcManager) {
		this.jahrgaenge.setAuswahl(srcManager.jahrgaenge);
		this.lehrer.setAuswahl(srcManager.lehrer);
		this.schulgliederungen.setAuswahl(srcManager.schulgliederungen);
	}

}
