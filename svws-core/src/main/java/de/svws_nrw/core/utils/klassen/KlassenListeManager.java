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
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.lehrer.LehrerUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Klassen-Listen.
 */
public class KlassenListeManager {

	/** Ein Filter-Attribut für die Klassenliste. Dieses wird nicht für das Filtern der Klassen verwendet, sondern für eine Mehrfachauswahl */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KlassenListeEintrag> klassen;
	private static final @NotNull Function<@NotNull KlassenListeEintrag, @NotNull Long> _klasseToId = (final @NotNull KlassenListeEintrag k) -> k.id;
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

	/** Die gefilterte Klassen-Liste, sofern sie schon berechnet wurde */
	private List<@NotNull KlassenListeEintrag> _filtered = null;

	/** Ein Handler für das Ereignis, dass der Klassen-Filter angepasst wurde */
	private final @NotNull Runnable _eventHandlerFilterChanged = () -> this._filtered = null;

	/** Ein Handler für das Ereignis, dass die Klassenauswahl angepasst wurde */
	private static final @NotNull Runnable _eventHandlerKlassenAuswahlChanged = () -> { /* nicht zu tun */ };

	/** Die Sortier-Ordnung, welche vom Comparator verwendet wird. */
	private @NotNull List<@NotNull Pair<@NotNull String, @NotNull Boolean>> _order = Arrays.asList(new Pair<>("klassen", true), new Pair<>("schueleranzahl", true));

	/** Die Schulform der Schule */
	private final Schulform _schulform;

	/** Die Daten der Klasse, sofern eine Klasse ausgewählt ist. */
	private KlassenDaten _daten = null;


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
		this._schulform = schulform;
		this.klassen = new AttributMitAuswahl<>(klassen, _klasseToId, KlassenUtils.comparator, _eventHandlerKlassenAuswahlChanged);
		this.jahrgaenge = new AttributMitAuswahl<>(jahrgaenge, _jahrgangToId, JahrgangsUtils.comparator, _eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl<>(lehrer, _lehrerToId, LehrerUtils.comparator, _eventHandlerFilterChanged);
		final @NotNull List<@NotNull Schulgliederung> gliederungen = (schulform == null) ? Arrays.asList(Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl<>(gliederungen, _schulgliederungToId, _comparatorSchulgliederung, _eventHandlerFilterChanged);
		initKlassen();
	}


	private void initKlassen() {
		for (final @NotNull KlassenListeEintrag k : this.klassen.list()) {
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
	 * Setzt die Sortier-Ordnung für die gefilterten Listen. Hier wird eine Menge von Paaren angegeben,
	 * welche das zu sortierende Feld als String angebenen und als boolean ob es aufsteigend (true)
	 * oder absteigend (false) sortiert werden soll.
	 *
	 * @param order   die Sortier-Ordnung
	 */
	public void orderSet(final @NotNull List<@NotNull Pair<@NotNull String, @NotNull Boolean>> order) {
		this._order = order;
		this._filtered = null;
	}


	/**
	 * Gibt die Sortier-Ordnung für die gefilterten Listen zurück als eine Menge von Paaren,
	 * welche das zu sortierende Feld als String angebenen und als boolean ob es aufsteigend (true)
	 * oder absteigend (false) sortiert werden soll.
	 *
	 * @return   die Sortier-Ordnung
	 */
	public final @NotNull List<@NotNull Pair<@NotNull String, @NotNull Boolean>> orderGet() {
		return new ArrayList<>(this._order);
	}


	/**
	 * Aktualisiert die Reihenfolge bei der Sortierung für das angegebene Feld. Dabei
	 * werden vorhande Feld-Eintrage angepasst oder bei null entfernt. Nicht vorhande
	 * Feld-Einträge werden ergänzt, sofern eine Reihenfolge definiert wird.
	 *
	 * @param field   das Feld
	 * @param order   die Reihenfolge für dieses Feld (ascending: true, descending: false, deaktivieren: null)
	 */
	public void orderUpdate(@NotNull final String field, final Boolean order) {
		// Prüfe, ob der Feld-Eintrag entfernt werden soll
		if (order == null) {
			for (int i = 0; i < this._order.size(); i++) {
				final @NotNull Pair<@NotNull String, @NotNull Boolean> eintrag = this._order.get(i);
				if (eintrag.a.equals(field)) {
					this._order.remove(eintrag);
					this._filtered = null;
					return;
				}
			}
			return;
		}
		// Prüfe, ob bereits ein Eintrag vorhanden ist und passe diesen ggf an
		for (int i = 0; i < this._order.size(); i++) {
			final @NotNull Pair<@NotNull String, @NotNull Boolean> eintrag = this._order.get(i);
			if (eintrag.a.equals(field)) {
				if (eintrag.b == order)
					return;
				this._order.remove(eintrag);
				eintrag.b = order;
				this._order.add(0, eintrag);
				this._filtered = null;
				return;
			}
		}
		// Füge einen neuen Eintrag vorne in der Liste hinzu
		final @NotNull Pair<@NotNull String, @NotNull Boolean> eintrag = new Pair<>(field, order);
		this._order.add(0, eintrag);
		this._filtered = null;
	}


	/**
	 * Vergleicht zwei Klassenlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	private int compare(final @NotNull KlassenListeEintrag a, final @NotNull KlassenListeEintrag b) {
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
	 * Gibt eine gefilterte Liste der Klassen zurück. Als Filter werden dabei
	 * die Jahrgänge, die Klassenlehrer und ein Filter auf nur sichtbare Klassen beachtet.
	 *
	 * @return die gefilterte Liste
	 */
	public @NotNull List<@NotNull KlassenListeEintrag> filtered() {
		if (_filtered != null)
			return _filtered;
		final @NotNull List<@NotNull KlassenListeEintrag> tmpList = new ArrayList<>();
		for (final @NotNull KlassenListeEintrag eintrag : this.klassen.list()) {
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
				if ((j.kuerzelStatistik == null) || ((j.kuerzelStatistik != null) && (!this.schulgliederungen.auswahlHasKey(j.kuerzelStatistik))))
					continue;
			}
			tmpList.add(eintrag);
		}
		final @NotNull Comparator<@NotNull KlassenListeEintrag> comparator = (final @NotNull KlassenListeEintrag a, final @NotNull KlassenListeEintrag b) -> this.compare(a, b);
		tmpList.sort(comparator);
		_filtered = tmpList;
		return _filtered;
	}


	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @return die Schulform der Schule
	 */
	public @NotNull Schulform schulform() {
		if (this._schulform == null)
			throw new DeveloperNotificationException("Der Klassenlisten-Manager sollte nur mit einer korrekt gesetzten Schulform verwendet werden.");
		return this._schulform;
	}


	/**
	 * Gibt zurück, ob eine Klassen ausgewählt ist und Daten vorliegen.
	 *
	 * @return true, wenn Daten vorliegen, und ansonsten false
	 */
	public boolean hasDaten() {
		return this._daten != null;
	}


	/**
	 * Gibt die Daten der aktuell ausgewählten Klassen zurück.
	 *
	 * @return die Daten
	 */
	public @NotNull KlassenDaten daten() {
		if (this._daten == null)
			throw new DeveloperNotificationException("Es exitsiert derzeit keine Klassenauswahl");
		return this._daten;
	}


	/**
	 * Setzt die Daten der Klasse. Dabei wird ggf. die Auswahl angepasst.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Klasse nicht in der Liste der Klassen vorhanden ist
	 */
	public void setDaten(final KlassenDaten daten) throws DeveloperNotificationException {
		// Die Auswahl wird zurückgesetzt und es ist keine Klasse mehr ausgewählt
		if (daten == null) {
			this._daten = null;
			return;
		}
		// Bestimme den Klassen-Eintrag. Dieser sollte immer vorhanden sein. Wenn nicht, dann liegt ein Fehler beim Aufruf vor...
		final @NotNull KlassenListeEintrag eintrag = this.klassen.getOrException(daten.id);
		// Passe ggf. die Daten in der Klassenliste an ... (beim Patchen der Daten)
		boolean updateEintrag = false;
		if (!daten.kuerzel.equals(eintrag.kuerzel)) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		// TODO Liste der Klassenlehrer?
		// TODO Liste der Schüler?
		// ... und setze die neue Daten
		this._daten = daten;
		// ... und berechne ggf. die Sortierung der Klassenliste neu
		if (updateEintrag)
			this.orderSet(this.orderGet());
	}


	/**
	 * Gibt die ID der Auswahl zurück. Ist keine Auswahl vorhanden, so wird null zurückgegeben.
	 *
	 * @return die ID oder null
	 */
	public Long auswahlID() {
		return this._daten == null ? null : this._daten.id;
	}


	/**
	 * Gibt den Eintrag der aktuellen Klassenauswahl in der Liste zurück. Hiefür muss eine
	 * gültige Auswahl vorliegen. Dies kann ggf. vorher über hasDaten geprüft werden.
	 *
	 * @return der Eintrag in der Klassenliste
	 *
	 * @throws DeveloperNotificationException wenn keine gültige Auswahl vorliegt
	 */
	public @NotNull KlassenListeEintrag auswahl() {
		if (this._daten == null)
			throw new DeveloperNotificationException("Für den Aufruf dieser Methode muss zuvor eine Klassen-Auswahl vorliegen.");
		return this.klassen.getOrException(this._daten.id);
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

}
