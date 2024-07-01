package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.schule.SchuljahresabschnittsUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse stellt grundlegende Funktionalitäten für die Verwaltung einer
 * Auswahl mit einer Manager-Klasse zur Verfügung. Ein solche Manager sollte
 * von dieser Klasse abgeleitet werden.
 *
 * @param <TID>        der gemeinsame Typ der ID der Auswahl- und Daten-Klassen
 * @param <TAuswahl>   der Typ der Auswahl-Listen-Objekte
 * @param <TDaten>     der Typ der Datenobjekte für eine Auswahl
 */
public abstract class AuswahlManager<TID, TAuswahl, TDaten> {

	/** Ein Auswahl-Attribut für die Auswahliste. Dieses wird nicht für eine Filterung verwendet, sondern für eine Mehrfachauswahl */
	public final @NotNull AttributMitAuswahl<TID, TAuswahl> liste;

	/** Ein Lambda-Ausdruck für das Mapping von einem Auswahl-Objekt auf dessen ID */
	private final @NotNull Function<TAuswahl, TID> _listeToId;

	/** Ein Lambda-Ausdruck für das Mapping von einem Daten-Objekt auf dessen ID */
	private final @NotNull Function<TDaten, TID> _datenToId;

	/** Die Schulform der Schule */
	protected final Schulform _schulform;

	/** Der Schuljahresabschnitt, welcher für die Auswahl genutzt wird */
	protected final long _schuljahresabschnitt;

	/** Der Schuljahresabschnitt, in dem sich die Schule befindet */
	protected final long _schuljahresabschnittSchule;

	/** Das Filter-Attribut für die Schuljahresabschnitte - die Filterfunktion wird zur Zeit noch nicht genutzt */
	public final @NotNull AttributMitAuswahl<Long, Schuljahresabschnitt> schuljahresabschnitte;
	private static final @NotNull Function<Schuljahresabschnitt, Long> _schuljahresabschnittToId = (final @NotNull Schuljahresabschnitt sja) -> sja.id;


	/** Die gefilterte Liste, sofern sie schon berechnet wurde */
	protected List<TAuswahl> _filtered = null;

	/** Die Daten, sofern eine Auswahl vorhanden ist. */
	protected TDaten _daten = null;

	/** Ein Handler für das Ereignis, dass der Filter angepasst wurde */
	protected final @NotNull Runnable _eventHandlerFilterChanged = () -> {
		this.onFilterChanged();
		this._filtered = null;
	};

	/** Ein Handler für das Ereignis, dass die Mehrfachauswahl angepasst wurde */
	private final @NotNull Runnable _eventHandlerMehrfachauswahlChanged = () -> this.onMehrfachauswahlChanged();

	/** Ein Handler für das Ereignis, dass die Liste mit der Mehrfachauswahl angepasst wurde */
	private final @NotNull Runnable _eventHandlerListeChanged = () -> this.onListeChangedInternal();

	/** Die Sortier-Ordnung, welche vom Comparator verwendet wird. */
	protected @NotNull List<Pair<String, Boolean>> _order;

	/** Gibt an, ob die aktuelle Einzel-Auswahl auch bei dem Filter erlaubt wird oder nicht. */
	protected boolean _filterPermitAuswahl;

	/** Die Daten aus der vorherigen Auswahl. */
	protected TDaten _vorherigeAuswahl = null;


	/**
	 * Initialisiert die Auswahl-Manager-Instanz
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, für welchen die Auswahl bereitgestellt wird.
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schulform                    die Schulform, für welche die Auswahl bereitgestellt wird.
	 * @param values                       die Werte für die Auswahlliste
	 * @param listComparator               ein comparator für das Vergleichen von Auswahl-Werten
	 * @param listeToId                    eine Funktion für das Mappen eines Auswahl-Objektes auf seine ID
	 * @param datenToId                    eine Funktion für das Mappen eines Daten-Objektes auf seine ID
	 * @param order                        die Default-Sortierung für die Auswahl-Liste
	 */
	protected AuswahlManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull Collection<TAuswahl> values, final @NotNull Comparator<TAuswahl> listComparator,
			final @NotNull Function<TAuswahl, TID> listeToId, final @NotNull Function<TDaten, TID> datenToId,
			final @NotNull List<Pair<String, Boolean>> order) {
		this._schuljahresabschnitt = schuljahresabschnitt;
		this._schuljahresabschnittSchule = schuljahresabschnittSchule;
		this.schuljahresabschnitte =
				new AttributMitAuswahl<>(schuljahresabschnitte, _schuljahresabschnittToId, SchuljahresabschnittsUtils.comparator, _eventHandlerFilterChanged);
		this._schulform = schulform;
		this._order = order;
		this._listeToId = listeToId;
		this._datenToId = datenToId;
		this.liste = new AttributMitAuswahl<>(values, _listeToId, listComparator, _eventHandlerMehrfachauswahlChanged);
		this.liste.setEventHandlerListeGeaendert(_eventHandlerListeChanged);
		this._filterPermitAuswahl = true;
	}


	/**
	 * Gibt eine gefilterte Auswahl-Liste zurück. Für die Filterung
	 * muss der Manager die Methode onFilter überschreiben.
	 *
	 * @return die gefilterte Liste
	 */
	public @NotNull List<TAuswahl> filtered() {
		if (_filtered != null)
			return _filtered;
		_filtered = new ArrayList<>();
		final TAuswahl aktAuswahl = (this._daten == null) ? null : this.auswahl();
		for (final @NotNull TAuswahl eintrag : this.liste.list())
			// Lasse die aktuelle Auswahl immer durch den Filter falls _filterPermitAuswahl gesetzt ist
			if ((this._filterPermitAuswahl && (aktAuswahl != null) && (this.compareAuswahl(aktAuswahl, eintrag) == 0)) || checkFilter(eintrag))
				_filtered.add(eintrag);
		final @NotNull Comparator<TAuswahl> comparator = (final @NotNull TAuswahl a, final @NotNull TAuswahl b) -> this.compareAuswahl(a, b);
		_filtered.sort(comparator);
		return _filtered;
	}


	/**
	 * Entfernt den aktuelle Cache für die gefilterte Liste und forciert so eine Neu-Berechnung der gecachten Liste
	 */
	public void filterInvalidateCache() {
		this._filtered = null;
	}


	/**
	 * Prüft, ob der angegebene Eintrag durch den Filter durchgelassen wird oder nicht.
	 *
	 * @param eintrag          der zu prüfende Eintrag
	 *
	 * @return true, wenn der Eintrag den Filter passiert, und ansonsten false
	 */
	protected abstract boolean checkFilter(@NotNull TAuswahl eintrag);


	/**
	 * Vergleicht zwei Einträge der Auswahl miteinander.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return ein negativer Wert, 0 oder ein positiver Werte, wenn der erste Eintrag
	 *         kleiner, gleich oder größer ist als der zweite Eintrag
	 */
	protected abstract int compareAuswahl(@NotNull TAuswahl a, @NotNull TAuswahl b);


	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an einem Filter stattgefunden hat. Das
	 * Ereignis tritt auf bevor die alte gefilterte Liste ungültig wird.
	 */
	protected void onFilterChanged() {
		// die Methode kann abgeleitet werden, um auf Filter-Änderungen zu reagieren.
	}


	/**
	 * Die Methode wird aufgerufen, wenn eine Änderung an der Liste mit den verfügbaren Daten
	 * eine Änderung vorgenommen wird.
	 */
	private void onListeChangedInternal() {
		final TID idAuswahl = auswahlID();
		if (idAuswahl != null) {
			if (this.liste.get(idAuswahl) == null)
				setDaten(null);
			else
				setDaten(daten());
		}
		this.onListeChanged();
	}


	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an der Liste der für die Mehrfachauswahl
	 * zulässigen Werte stattgefunden hat.
	 */
	protected void onListeChanged() {
		// die Methode kann abgeleitet werden, um auf Änderungen an der Liste zu reagieren.
	}


	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an der Mehrfachauswahl stattgefunden hat.
	 */
	protected void onMehrfachauswahlChanged() {
		// die Methode kann abgeleitet werden, um auf Änderungen an der Mehrfachauswahl zu reagieren.
	}


	/**
	 * Setzt die Sortier-Ordnung für die gefilterten Listen. Hier wird eine Menge von Paaren angegeben,
	 * welche das zu sortierende Feld als String angebenen und als boolean ob es aufsteigend (true)
	 * oder absteigend (false) sortiert werden soll.
	 *
	 * @param order   die Sortier-Ordnung
	 */
	public void orderSet(final @NotNull List<Pair<String, Boolean>> order) {
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
	public final @NotNull List<Pair<String, Boolean>> orderGet() {
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
				final @NotNull Pair<String, Boolean> eintrag = this._order.get(i);
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
			final @NotNull Pair<String, Boolean> eintrag = this._order.get(i);
			if (eintrag.a.equals(field)) {
				if (eintrag.b.equals(order))
					return;
				this._order.remove(eintrag);
				eintrag.b = order;
				this._order.add(0, eintrag);
				this._filtered = null;
				return;
			}
		}
		// Füge einen neuen Eintrag vorne in der Liste hinzu
		final @NotNull Pair<String, Boolean> eintrag = new Pair<>(field, order);
		this._order.add(0, eintrag);
		this._filtered = null;
	}


	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @return die Schulform der Schule
	 */
	public @NotNull Schulform schulform() {
		if (this._schulform == null)
			throw new DeveloperNotificationException("Der Auswahl-Manager sollte nur mit einer korrekt gesetzten Schulform verwendet werden.");
		return this._schulform;
	}


	/**
	 * Gibt zurück, ob eine Auswahl und damit auch Daten vorliegen.
	 *
	 * @return true, wenn eine Auswahl und Daten vorliegen, und ansonsten false
	 */
	public boolean hasDaten() {
		return this._daten != null;
	}


	/**
	 * Gibt die Daten der aktuellen Auswahl zurück.
	 *
	 * @return die Daten
	 */
	public @NotNull TDaten daten() {
		if (this._daten == null)
			throw new DeveloperNotificationException("Es exitsiert derzeit keine Auswahl und damit auch keine Daten");
		return this._daten;
	}


	/**
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst. Die vorherige Auswahl wird gespeichert.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	public void setDaten(final TDaten daten) throws DeveloperNotificationException {
		// vorherige Auswahl speichern, um diese ggf. wiederherstellen zu können
		this._vorherigeAuswahl = this._daten;

		if (daten == null) {
			// Die Daten werden zurückgesetzt und damit ist keine Auswahl mehr vorhanden
			this._daten = null;
		} else {
			// Bestimme den Auswahl-Eintrag. Dieser sollte immer vorhanden sein. Wenn nicht, dann liegt ein Fehler beim Aufruf vor...
			final @NotNull TAuswahl eintrag = this.liste.getOrException(this._datenToId.apply(daten));
			// Passe ggf. die Daten in der Auswahlliste an ... (beim Patchen der Daten)
			final boolean updateEintrag = this.onSetDaten(eintrag, daten);
			// ... und setze die neue Daten
			this._daten = daten;
			// ... und berechne ggf. die Sortierung der Liste neu
			if (updateEintrag)
				this.orderSet(this.orderGet());
		}
		// ... und triggere, dass der Filter neu angewendet wird
		this._filtered = null;
	}


	/**
	 * Diese Methode wird aufgerufen, wenn neue Daten gesetzt werden. Hierüber kann
	 * ein Manager noch nötige Anpassungen an der Auswahlliste durchführen.
	 * Wurde die Auswahlliste so angepasst, dass Änderungen an der Sortierung
	 * daraus resultieren können, so ist true zurückzugeben.
	 *
	 * @param eintrag   der Eintrag in der Auswahlliste
	 * @param daten     die neuen Daten für den Eintrag in der Auswahlliste
	 *
	 * @return gibt an, ob Anpassungen an der Auswahlliste vorgenommen wurden.
	 */
	protected boolean onSetDaten(final @NotNull TAuswahl eintrag, final @NotNull TDaten daten) {
		return false;
	}


	/**
	 * Gibt die ID der Auswahl zurück. Ist keine Auswahl vorhanden, so wird null zurückgegeben.
	 *
	 * @return die ID oder null
	 */
	public TID auswahlID() {
		return this._daten == null ? null : this._datenToId.apply(this._daten);
	}


	/**
	 * Gibt den Eintrag der aktuellen Auswahl in der Liste zurück. Hiefür muss eine
	 * gültige Auswahl vorliegen. Dies kann ggf. vorher über hasDaten geprüft werden.
	 *
	 * @return der Eintrag in der Auswahlliste
	 *
	 * @throws DeveloperNotificationException wenn keine gültige Auswahl vorliegt
	 */
	public @NotNull TAuswahl auswahl() {
		if (this._daten == null)
			throw new DeveloperNotificationException("Für den Aufruf dieser Methode muss zuvor eine Auswahl vorliegen.");
		return this.liste.getOrException(this._datenToId.apply(this._daten));
	}


	/**
	 * Gibt zurück, ob die aktuelle Auswahl beim Filter erlaubt bleibt oder nicht.
	 *
	 * @return true, falls die aktuelle Auswahl beim Filtern erlaubt bleibt oder nicht.
	 */
	public boolean isFilterAuswahlPermitted() {
		return this._filterPermitAuswahl;
	}


	/**
	 * Setzt, ob die aktuelle Auswahl beim Filter erlaubt bleibt oder nicht.
	 *
	 * @param value   der neue boolean-Wert
	 */
	public void setFilterAuswahlPermitted(final boolean value) {
		this._filterPermitAuswahl = value;
	}


	/**
	 * Gibt den Schuljahresabschnitt der Auswahl zurück.
	 *
	 * @return der Schuljahresabschnitt der Auswahl
	 */
	public Schuljahresabschnitt getSchuljahresabschnittAuswahl() {
		return this.schuljahresabschnitte.get(this._schuljahresabschnitt);
	}


	/**
	 * Gibt den Schuljahresabschnitt der Schule zurück.
	 *
	 * @return der Schuljahresabschnitt der Schule
	 */
	public Schuljahresabschnitt getSchuljahresabschnittSchule() {
		return this.schuljahresabschnitte.get(this._schuljahresabschnittSchule);
	}


	/**
	 * Gibt zurück, ob der Schuljahresabschnitt der Auswahl mit dem aktuellen
	 * Schuljahresabschnitt der Schule übereinstimmt.
	 *
	 * @return true, wenn die Schuljahresabschnitte übereinstimmen
	 */
	public boolean istSchuljahresabschnittAktuell() {
		final Schuljahresabschnitt abschnittAuswahl = this.getSchuljahresabschnittAuswahl();
		final Schuljahresabschnitt abschnittSchule = this.getSchuljahresabschnittSchule();
		if ((abschnittAuswahl == null) || (abschnittSchule == null))
			return false;
		return (abschnittAuswahl.schuljahr == abschnittSchule.schuljahr) && (abschnittAuswahl.abschnitt == abschnittSchule.abschnitt);
	}


	/**
	 * Gibt zurück, ob sich bei dem Schuljahresabschnitt der Auswahl um ein
	 * Abschnitt in Planung handelt, d.h. ob der Schuljahresabschnitt der Auswahl
	 * nach dem aktuellen Schuljahresabschnitt der Schule liegt.
	 *
	 * @return true, wenn der Schuljahresabschnitt der Auswahl ein Planungsabschnitt ist
	 */
	public boolean istSchuljahresabschnittPlanung() {
		final Schuljahresabschnitt abschnittAuswahl = this.getSchuljahresabschnittAuswahl();
		final Schuljahresabschnitt abschnittSchule = this.getSchuljahresabschnittSchule();
		if ((abschnittAuswahl == null) || (abschnittSchule == null))
			return false;
		return (abschnittAuswahl.schuljahr > abschnittSchule.schuljahr)
				|| ((abschnittAuswahl.schuljahr == abschnittSchule.schuljahr) && (abschnittAuswahl.abschnitt > abschnittSchule.abschnitt));
	}


	/**
	 * Gibt zurück, ob sich bei dem Schuljahresabschnitt der Auswahl um einen
	 * Abschnitt in der Vergangengheit handelt, d.h. ob der Schuljahresabschnitt
	 * der Auswahl vor dem aktuellen Schuljahresabschnitt der Schule liegt.
	 *
	 * @return true, wenn der Schuljahresabschnitt der Auswahl ein vergangener Abschnitt ist
	 */
	public boolean istSchuljahresabschnittVergangenheit() {
		final Schuljahresabschnitt abschnittAuswahl = this.getSchuljahresabschnittAuswahl();
		final Schuljahresabschnitt abschnittSchule = this.getSchuljahresabschnittSchule();
		if ((abschnittAuswahl == null) || (abschnittSchule == null))
			return false;
		return (abschnittAuswahl.schuljahr < abschnittSchule.schuljahr)
				|| ((abschnittAuswahl.schuljahr == abschnittSchule.schuljahr) && (abschnittAuswahl.abschnitt < abschnittSchule.abschnitt));
	}


	/**
	 * Gibt die vorherige Auswahl zurück oder <code>null</code>, wenn es keine vorherige Auswahl gibt.
	 *
	 * @return vorherige Auswahl
	 */
	public TDaten getVorherigeAuswahl() {
		return this._vorherigeAuswahl;
	}

}
