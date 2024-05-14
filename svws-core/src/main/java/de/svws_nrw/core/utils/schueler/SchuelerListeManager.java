package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.schueler.SchuelerListe;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.klassen.KlassenUtils;
import de.svws_nrw.core.utils.kurse.KursUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Manager zum Verwalten der Schüler-Listen.
 */
public final class SchuelerListeManager extends AuswahlManager<@NotNull Long, @NotNull SchuelerListeEintrag, @NotNull SchuelerStammdaten> {

	/** Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ */
	private static final @NotNull Function<@NotNull SchuelerListeEintrag, @NotNull Long> _schuelerToId = (final @NotNull SchuelerListeEintrag s) -> s.id;
	private static final @NotNull Function<@NotNull SchuelerStammdaten, @NotNull Long> _stammdatenToId = (final @NotNull SchuelerStammdaten s) -> s.id;

	/** Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können */
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerMitStatus = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInKlasse = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInKurs = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInSchuljahresabschnitt = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInAbiturjahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull String, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInSchulgliederung = new HashMap2D<>();

	/** Das Filter-Attribut für die Jahrgänge */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull JahrgangsDaten> jahrgaenge;
	private static final @NotNull Function<@NotNull JahrgangsDaten, @NotNull Long> _jahrgangToId = (final @NotNull JahrgangsDaten jg) -> jg.id;

	/** Das Filter-Attribut für die Klassen */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KlassenDaten> klassen;
	private static final @NotNull Function<@NotNull KlassenDaten, @NotNull Long> _klasseToId = (final @NotNull KlassenDaten k) -> k.id;
	private final @NotNull Map<@NotNull Long, @NotNull KlassenDaten> _mapKlassenAlle = new HashMap<>();

	/** Das Filter-Attribut für die Kurse */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KursDaten> kurse;
	private static final @NotNull Function<@NotNull KursDaten, @NotNull Long> _kursToId = (final @NotNull KursDaten k) -> k.id;

	/** Das Filter-Attribut für die Abiturjahrgänge - die Filterfunktion wird zur Zeit noch nicht genutzt */
	public final @NotNull AttributMitAuswahl<@NotNull Integer, @NotNull GostJahrgang> abiturjahrgaenge;
	private static final @NotNull Function<@NotNull GostJahrgang, @NotNull Integer> _abiturjahrgangToId = (final @NotNull GostJahrgang a) -> a.abiturjahr;

	/** Das Filter-Attribut für die Schulgliederungen */
	public final @NotNull AttributMitAuswahl<@NotNull String, @NotNull Schulgliederung> schulgliederungen;
	private static final @NotNull Function<@NotNull Schulgliederung, @NotNull String> _schulgliederungToId = (final @NotNull Schulgliederung sg) -> sg.daten.kuerzel;
	private static final @NotNull Comparator<@NotNull Schulgliederung> _comparatorSchulgliederung = (final @NotNull Schulgliederung a, final @NotNull Schulgliederung b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut für den Schüler-Status */
	public final @NotNull AttributMitAuswahl<@NotNull Integer, @NotNull SchuelerStatus> schuelerstatus;
	private static final @NotNull Function<@NotNull SchuelerStatus, @NotNull Integer> _schuelerstatusToId = (final @NotNull SchuelerStatus s) -> s.id;
	private static final @NotNull Comparator<@NotNull SchuelerStatus> _comparatorSchuelerStatus = (final @NotNull SchuelerStatus a, final @NotNull SchuelerStatus b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut auf Schüler mit einem Lernabschnitt in dem Schuljahresabschnitt */
	private boolean _filterNurMitLernabschitt = true;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform                    die Schulform der Schule
	 * @param daten                        die Informationen zur Schüler-Auswahlliste
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in dem sich die Schule aktuell befindet
	 */
	public SchuelerListeManager(final Schulform schulform, final @NotNull SchuelerListe daten, final @NotNull List<@NotNull Schuljahresabschnitt> schuljahresabschnitte,
			final long schuljahresabschnittSchule) {
		super(daten.idSchuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, daten.schueler, SchuelerUtils.comparator,
			_schuelerToId, _stammdatenToId, Arrays.asList(new Pair<>("klassen", true), new Pair<>("nachname", true), new Pair<>("vorname", true)));
		// Erstelle eine Map für den Zugriff auf Klassen über die ID und
		// erstelle für den Filter eine Liste der Klassen reduziert auf alle Klassen des Schuljahresabschnittes der Auswahl
		final @NotNull List<@NotNull KlassenDaten> aktuelleKlassen = new ArrayList<>();
		for (final @NotNull KlassenDaten klasse : daten.klassen) {
			_mapKlassenAlle.put(klasse.id, klasse);
			if (klasse.idSchuljahresabschnitt == daten.idSchuljahresabschnitt)
				aktuelleKlassen.add(klasse);
		}
		this.klassen = new AttributMitAuswahl<>(aktuelleKlassen, _klasseToId, KlassenUtils.comparator, _eventHandlerFilterChanged);
		this.jahrgaenge = new AttributMitAuswahl<>(daten.jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator, _eventHandlerFilterChanged);
		this.kurse = new AttributMitAuswahl<>(daten.kurse, _kursToId, KursUtils.comparator, _eventHandlerFilterChanged);
		this.abiturjahrgaenge = new AttributMitAuswahl<>(daten.jahrgaengeGost, _abiturjahrgangToId, GostAbiturjahrUtils.comparator, _eventHandlerFilterChanged);
		final @NotNull List<@NotNull Schulgliederung> gliederungen = (schulform == null) ? Arrays.asList(Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl<>(gliederungen, _schulgliederungToId, _comparatorSchulgliederung, _eventHandlerFilterChanged);
		this.schuelerstatus = new AttributMitAuswahl<>(Arrays.asList(SchuelerStatus.values()), _schuelerstatusToId, _comparatorSchuelerStatus, _eventHandlerFilterChanged);
		initSchueler();
	}


	private void initSchueler() {
		for (final @NotNull SchuelerListeEintrag s : this.liste.list()) {
			this._mapSchuelerMitStatus.put(s.status, s.id, s);
			if (s.idJahrgang >= 0)
				this._mapSchuelerInJahrgang.put(s.idJahrgang, s.id, s);
			if (s.idKlasse >= 0)
				this._mapSchuelerInKlasse.put(s.idKlasse, s.id, s);
			for (final long idKurs : s.kurse)
				this._mapSchuelerInKurs.put(idKurs, s.id, s);
			if (s.idSchuljahresabschnitt >= 0)
				this._mapSchuelerInSchuljahresabschnitt.put(s.idSchuljahresabschnitt, s.id, s);
			if (s.abiturjahrgang != null)
				this._mapSchuelerInAbiturjahrgang.put(s.abiturjahrgang, s.id, s);
			if (!s.schulgliederung.isBlank())
				this._mapSchuelerInSchulgliederung.put(s.schulgliederung, s.id, s);
		}
	}


	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull SchuelerListeEintrag eintrag, final @NotNull SchuelerStammdaten daten) {
		// Passe ggf. die Daten in der Schülerliste an ... (beim Patchen der Daten)
		boolean updateEintrag = false;
		if (!daten.vorname.equals(eintrag.vorname)) {
			eintrag.vorname = daten.vorname;
			updateEintrag = true;
		}
		if (!daten.nachname.equals(eintrag.nachname)) {
			eintrag.nachname = daten.nachname;
			updateEintrag = true;
		}
		if (daten.status != eintrag.status) {
			eintrag.status = daten.status;
			updateEintrag = true;
		}
		return updateEintrag;
	}


	/**
	 * Gibt die aktuelle Filtereinstellung auf Schüler mit Lernabschnitt
	 * im ausgewählten Schuljahresabschnitt zurück.
	 *
	 * @return true, wenn nur Schüler mit Lernabschnitt angezeigt werden und ansonsten false
	 */
	public boolean filterNurMitLernabschitt() {
		return this._filterNurMitLernabschitt;
	}


	/**
	 * Setzt die Filtereinstellung auf Schüler mit Lernabschnitt im ausgewählten
	 * Schuljahresabschnitt.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public void setFilterNurMitLernabschitt(final boolean value) {
		this._filterNurMitLernabschitt = value;
		this._eventHandlerFilterChanged.run();
	}


	/**
	 * Aktualisiert die Klassen-ID bei dem Schüler
	 *
	 * @param idKlasse   die ID der Klasse
	 *
	 * @throws DeveloperNotificationException   falls kein Schüler ausgewählt ist oder die Klassen-ID nicht zulässig ist
	 */
	public void updateKlassenID(final Long idKlasse) throws DeveloperNotificationException {
		// Prüfe, ob überhaupt eine Schüler-Auswahl vorliegt ...
		if (this._daten == null)
			throw new DeveloperNotificationException("Für das Setzen der Klassen-ID %d muss ein Schüler ausgewählt sein.".formatted(idKlasse));
		// Prüfe, ob die angebene Klassen-ID überhaupt gültig ist ...
		if ((idKlasse != null) && (idKlasse >= 0) && (!this.klassen.has(idKlasse)))
			throw new DeveloperNotificationException("Die Klassen-ID %d muss zu dem aktuell ausgewählten Schuljahresabschnitt passen.".formatted(idKlasse));
		// Bestimme den Listen-Eintrag, passe diesen an und aktualisiere dann ggf. die Sortierung ...
		final @NotNull SchuelerListeEintrag eintrag = this.liste.getOrException(this._daten.id);
		eintrag.idKlasse = ((idKlasse == null) || (idKlasse < 0)) ? -1 : idKlasse;
		this.orderSet(this.orderGet());
	}


	/**
	 * Vergleicht zwei Schülerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull SchuelerListeEintrag a, final @NotNull SchuelerListeEintrag b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp = 0;
			if ("klassen".equals(field)) {
				final KlassenDaten aKlasse = this.klasseGetOrNull(a.idKlasse);
				final KlassenDaten bKlasse = this.klasseGetOrNull(b.idKlasse);
				if ((aKlasse == null) && (bKlasse == null)) {
					cmp = 0;
				} else if (aKlasse == null) {
					cmp = -1;
				} else if (bKlasse == null) {
					cmp = 1;
				} else {
					cmp = KlassenUtils.comparator.compare(aKlasse, bKlasse);
				}
			} else if ("nachname".equals(field)) {
				cmp = a.nachname.compareTo(b.nachname);
			} else if ("vorname".equals(field)) {
				cmp = a.vorname.compareTo(b.vorname);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}


	@Override
	protected boolean checkFilter(final @NotNull SchuelerListeEintrag eintrag) {
		// Filtere nun anhand der Filterkriterien
		if (this._filterNurMitLernabschitt && (!this.istSchuljahresabschnittAktuell()) && (eintrag.idSchuljahresabschnitt != this._schuljahresabschnitt))
			return false;
		if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang < 0) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang))))
			return false;
		if (this.klassen.auswahlExists() && ((eintrag.idKlasse < 0) || (eintrag.idSchuljahresabschnitt != this._schuljahresabschnitt) || (!this.klassen.auswahlHasKey(eintrag.idKlasse))))
			return false;
		if (this.kurse.auswahlExists()) {
			boolean hatEinenKurs = false;
			for (final long idKurs : eintrag.kurse)
				if (this.kurse.auswahlHasKey(idKurs))
					hatEinenKurs = true;
			if (!hatEinenKurs)
				return false;
		}
		if (this.schulgliederungen.auswahlExists() && ((eintrag.schulgliederung.isBlank()) || (!this.schulgliederungen.auswahlHasKey(eintrag.schulgliederung))))
			return false;
		if (this.schuelerstatus.auswahlExists() && (this.istSchuljahresabschnittAktuell()) && (!this.schuelerstatus.auswahlHasKey(eintrag.status)))
			return false;
		return true;
	}


	/**
	 * Gibt für die angegebene Klassen-ID die Daten zur Klasse zurück.
	 * Sollte die ID ungültig sein, so wird null zurückgegeben.
	 *
	 * @param idKlasse    die ID der Klasse
	 *
	 * @return die Daten der Klasse
	 */
	public KlassenDaten klasseGetOrNull(final long idKlasse) {
		return this._mapKlassenAlle.get(idKlasse);
	}


	/**
	 * Gibt zurück, ob der Schüler mit der angebenen ID einen Lernabschnitt in
	 * dem Schuljahresabschnitt dieser Auswahl hat.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return true, fall ein Lernabschnitt vorhanden ist und ansonsten false
	 */
	public boolean schuelerIstImSchuljahresabschnitt(final long idSchueler) {
		final SchuelerListeEintrag schueler = this.liste.get(idSchueler);
		return (schueler != null) && (schueler.idSchuljahresabschnitt == this._schuljahresabschnitt);
	}


	/**
	 * Gibt den Schuljahresabschnitt des Schülers als String zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return der Schuljahresabschnitt als String
	 */
	public @NotNull String schuelerSchuljahresabschnittAsString(final long idSchueler) {
		final SchuelerListeEintrag schueler = this.liste.get(idSchueler);
		if (schueler == null)
			return "----.-";
		final Schuljahresabschnitt schuljahresabschnitt = this.schuljahresabschnitte.get(schueler.idSchuljahresabschnitt);
		if (schuljahresabschnitt == null)
			return "----.-";
		return schuljahresabschnitt.schuljahr + "." + schuljahresabschnitt.abschnitt;
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link SchuelerListeManager}
	 *
	 * @param srcManager Manager aus dem die Filterinformationen übernommen werden
	 */
	public void useFilter(final @NotNull SchuelerListeManager srcManager) {
		this.klassen.setAuswahl(srcManager.klassen);
		this.kurse.setAuswahl(srcManager.kurse);
		this.jahrgaenge.setAuswahl(srcManager.jahrgaenge);
		this.schulgliederungen.setAuswahl(srcManager.schulgliederungen);
	}

}
