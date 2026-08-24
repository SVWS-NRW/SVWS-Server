import { HashMap } from '../../../../core/src/java/util/HashMap';
import type { Schulform } from '../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../core/src/java/util/ArrayList';
import { SchuljahresabschnittsUtils } from '../../../../core/src/core/utils/schule/SchuljahresabschnittsUtils';
import { DeveloperNotificationException } from '../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../core/src/java/util/Comparator';
import type { Collection } from '../../../../core/src/java/util/Collection';
import type { List } from '../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { JavaMap } from '../../../../core/src/java/util/JavaMap';

import type { AuswahlManagerSortierOrdnung as SortierOrdnung } from "./AuswahlManagerSortierOrdnung";
import { ListeMitAuswahl } from './ListeMitAuswahl';

/**
 * Abstrakte Basisklasse für Auswahl-Manager, die im Client für die Verwaltung von
 * Auswahllisten eingesetzt werden.
 *
 * @abstract
 * @typeParam TID 				- der primitive Typ (number|string) der ID des Auswahl-Elementes
 * @typeParam TAuswahl		- der Typ der Auswahl-Einträge
 * @typeParam TDaten			- der Typ der mit der aktuellen Auswahl verknüpften Daten
 */
export abstract class AuswahlManager<TID extends number | string, TAuswahl, TDaten> {

	/** Ein Liste (z.B. eines Attributs) für Mehrfachauswahl - nicht für eine Filterung verwendet */
	public readonly liste: ListeMitAuswahl<TID, TAuswahl>;

	/** Funktion für das Mapping von einem Auswahl-Objekt auf dessen ID */
	private readonly _listeToId: (eintrag: TAuswahl) => TID;

	/** Funktion für das Mapping von einem Daten-Objekt auf dessen ID */
	private readonly _datenToId: (daten: TDaten) => TID;

	/** Die Schulform der Schule */
	protected readonly _schulform: Schulform | null;

	/** Der Schuljahresabschnitt, welcher für die Auswahl genutzt wird */
	protected readonly _schuljahresabschnitt: number;

	/** Der Schuljahresabschnitt, in dem sich die Schule befindet */
	protected readonly _schuljahresabschnittSchule: number;

	/** Das Filter-Attribut für die Schuljahresabschnitte -
	 * die Filterfunktion wird zur Zeit noch nicht genutzt */
	public readonly schuljahresabschnitte: ListeMitAuswahl<number, Schuljahresabschnitt>;

	/** Funktion für das Mapping von Schuljahresabschnitt zu ID */
	private static readonly _schuljahresabschnittToId = (sja: Schuljahresabschnitt) => sja.id;

	/** Die gefilterte Liste, sofern sie schon berechnet wurde */
	protected _filtered: List<TAuswahl> | null = null;

	/** Die Daten, sofern eine Auswahl vorhanden ist */
	protected _daten: TDaten | null = null;

	/** Ein Handler für das Ereignis, dass der Filter angepasst wurde */
	protected readonly _eventHandlerFilterChanged = () => {
		this.onFilterChanged();
		this._filtered = null;
	};

	/** Ein Handler für das Ereignis, dass die Mehrfachauswahl angepasst wurde */
	private readonly _eventHandlerMehrfachauswahlChanged = () => this.onMehrfachauswahlChanged();

	/** Ein Handler für das Ereignis, dass die Liste mit der Mehrfachauswahl angepasst wurde */
	private readonly _eventHandlerListeChanged = () => this.onListeChangedInternal();

	/** Die Sortier-Ordnung, welche vom {@link Comparator} verwendet wird */
	protected _order: Array<SortierOrdnung>;

	/** Gibt an, ob die aktuelle Einzel-Auswahl auch bei dem Filter erlaubt wird oder nicht */
	protected _filterPermitAuswahl: boolean = false;

	/** Die Daten aus der vorherigen Auswahl */
	protected _vorherigeAuswahl: TDaten | null = null;

	/** Map der aktuell für den Gruppenprozess selektierten Einträge, indexiert nach ID */
	protected _listeDaten: JavaMap<TID, TDaten> = new HashMap<TID, TDaten>();


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
	protected constructor(
		schuljahresabschnitt: number,
		schuljahresabschnittSchule: number,
		schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null,
		values: Collection<TAuswahl>,
		listComparator: Comparator<TAuswahl>,
		listeToId: (eintrag: TAuswahl) => TID,
		datenToId: (daten: TDaten) => TID,
		order: Array<SortierOrdnung>
	) {
		this._schuljahresabschnitt = schuljahresabschnitt;
		this._schuljahresabschnittSchule = schuljahresabschnittSchule;
		this.schuljahresabschnitte = new ListeMitAuswahl(schuljahresabschnitte, AuswahlManager._schuljahresabschnittToId, SchuljahresabschnittsUtils.comparator, this._eventHandlerFilterChanged);
		this._schulform = schulform;
		this._order = order;
		this._listeToId = listeToId;
		this._datenToId = datenToId;
		this.liste = new ListeMitAuswahl(values, this._listeToId, listComparator, this._eventHandlerMehrfachauswahlChanged);
		this.liste.setEventHandlerListeGeaendert(this._eventHandlerListeChanged);
		this._filterPermitAuswahl = true;
	}

	/**
	 * Gibt eine gefilterte Auswahl-Liste zurück. Für die Filterung
	 * muss der Manager die Methode onFilter überschreiben.
	 *
	 * @return die gefilterte Liste
	 */
	public filtered(): List<TAuswahl> {
		if (this._filtered !== null) {
			return this._filtered;
		}

		this._filtered = new ArrayList();

		const aktAuswahl: TAuswahl | null = (this._daten === null) ? null : this.auswahl();

		for (const eintrag of this.liste.list()) {
			const keepAktuelleAuswahlInFiltered = this._filterPermitAuswahl && (aktAuswahl !== null) && (this.compareAuswahl(aktAuswahl, eintrag) === 0);

			if (keepAktuelleAuswahlInFiltered || this.checkFilter(eintrag)) {
				this._filtered.add(eintrag);
			}
		}

		this._filtered.sort({ compare: (a: TAuswahl, b: TAuswahl) => this.compareAuswahl(a, b) });
		return this._filtered;
	}

	/**
	 * Entfernt den aktuellen Cache für die gefilterte Liste und forciert so eine Neu-Berechnung der gecachten Liste
	 */
	public filterInvalidateCache(): void {
		this._filtered = null;
	}

	/**
	 * Prüft, ob der angegebene Eintrag durch den Filter durchgelassen wird oder nicht.
	 *
	 * @param eintrag          der zu prüfende Eintrag
	 *
	 * @return true, wenn der Eintrag den Filter passiert, und ansonsten false
	 */
	protected abstract checkFilter(eintrag: TAuswahl): boolean;

	/**
	 * Vergleicht zwei Einträge der Auswahl miteinander.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return ein negativer Wert, 0 oder ein positiver Wert, wenn der erste Eintrag
	 *         kleiner, gleich oder größer ist als der zweite Eintrag
	 */
	protected abstract compareAuswahl(a: TAuswahl, b: TAuswahl): number;

	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an einem Filter stattgefunden hat. Das
	 * Ereignis tritt auf bevor die alte gefilterte Liste ungültig wird.
	 */
	protected onFilterChanged(): void {
		// empty block
	}

	/**
	 * Die Methode wird aufgerufen, wenn eine Änderung an der Liste mit den verfügbaren Daten
	 * eine Änderung vorgenommen wird.
	 */
	private onListeChangedInternal(): void {
		const idAuswahl: TID | null = this.auswahlID();
		if (idAuswahl !== null) {
			if (this.liste.get(idAuswahl) === null) {
				this.setDaten(null);
			} else {
				this.setDaten(this.daten());
			}
		}
		this.onListeChanged();
	}

	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an der Liste der für die Mehrfachauswahl
	 * zulässigen Werte stattgefunden hat.
	 */
	protected onListeChanged(): void {
		// empty block
	}

	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an der Mehrfachauswahl stattgefunden hat.
	 */
	protected onMehrfachauswahlChanged(): void {
		// empty block
	}

	/**
	 * Setzt die Sortier-Ordnung (Array). Jeder Eintrag beschreibt ein Sortierfeld (als String) und
	 * die zugehörige Richtung: aufsteigend (`true`) oder absteigend (`false`).
	 *
	 * @param order   die neue Sortier-Ordnung
	 */
	public orderSet(order: Array<SortierOrdnung>): void {
		this._order = [...order];
		this._filtered = null;
	}

	/**
	 * Gibt die aktuelle Sortier-Ordnung (Array) zurück. Jeder Eintrag beschreibt ein Sortierfeld (als String)
	 * und die zugehörige Richtung: aufsteigend (`true`) oder absteigend (`false`).
	 *
	 * @return die aktuellen Sortier-Ordnung (als Kopie)
	 */
	public orderGet(): Array<SortierOrdnung> {
		return [...this._order];
	}

	/**
	 * Aktualisiert die Reihenfolge bei der Sortierung für das angegebene Feld.
	 *
	 * Vorhandene Feld-Einträge werden angepasst oder bei null entfernt.
	 * Nicht vorhandene Feld-Einträge werden ergänzt, sofern eine Reihenfolge definiert wird.
	 * Geänderte oder neue Einträge werden an den Anfang der Sortier-Ordnung gesetzt.
	 *
	 * @param field   das Feld
	 * @param order   die Reihenfolge für dieses Feld (ascending: true, descending: false, deaktivieren: null)
	 */
	public orderUpdate(field: string, order: boolean | null): void {
		const index = this._order.findIndex(e => e.field === field);

		if (order === null) {
			if (index !== -1) {
				this._order.splice(index, 1);
				this._filtered = null;
			}
			return;
		}

		if (index !== -1) {
			if (this._order[index].ascending === order) {
				return;
			}
			this._order.splice(index, 1);
		}

		this._order.unshift({ field, ascending: order });
		this._filtered = null;
	}

	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @return die Schulform der Schule
	 */
	public schulform(): Schulform {
		if (this._schulform === null) {
			throw new DeveloperNotificationException("Der Auswahl-Manager sollte nur mit einer korrekt gesetzten Schulform verwendet werden.");
		}
		return this._schulform;
	}

	/**
	 * Gibt zurück, ob eine Auswahl und damit auch Daten vorliegen.
	 *
	 * @return true, wenn eine Auswahl und Daten vorliegen, und ansonsten false
	 */
	public hasDaten(): boolean {
		return this._daten !== null;
	}

	/**
	 * Gibt die Daten der aktuellen Auswahl zurück.
	 *
	 * @return die Daten
	 */
	public daten(): TDaten {
		if (this._daten === null) {
			throw new DeveloperNotificationException("Es exisiert derzeit keine Auswahl und damit auch keine Daten");
		}
		return this._daten;
	}

	/**
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst. Die vorherige Auswahl wird gespeichert.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	public setDaten(daten: TDaten | null): void {
		this._vorherigeAuswahl = this._daten;
		if (daten === null) {
			this._daten = null;
		} else {
			const eintrag: TAuswahl = this.liste.getOrException(this._datenToId(daten));
			const updateEintrag: boolean = this.onSetDaten(eintrag, daten);
			this._daten = daten;
			if (updateEintrag) {
				this.orderSet(this.orderGet());
			}
		}
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
	protected onSetDaten(eintrag: TAuswahl, daten: TDaten): boolean {
		return false;
	}

	/**
	 * Gibt die ID der Auswahl zurück. Ist keine Auswahl vorhanden, so wird null zurückgegeben.
	 *
	 * @return die ID oder null
	 */
	public auswahlID(): TID | null {
		return (this._daten === null) ? null : this._datenToId(this._daten);
	}

	/**
	 * Gibt den Eintrag der aktuellen Auswahl in der Liste zurück. Hiefür muss eine
	 * gültige Auswahl vorliegen. Dies kann ggf. vorher über hasDaten geprüft werden.
	 *
	 * @return der Eintrag in der Auswahlliste
	 *
	 * @throws DeveloperNotificationException wenn keine gültige Auswahl vorliegt
	 */
	public auswahl(): TAuswahl {
		if (this._daten === null) {
			throw new DeveloperNotificationException("Für den Aufruf dieser Methode muss zuvor eine Auswahl vorliegen.");
		}
		return this.liste.getOrException(this._datenToId(this._daten));
	}

	/**
	 * Gibt zurück, ob die aktuelle Auswahl beim Filter erlaubt bleibt oder nicht.
	 *
	 * @return true, falls die aktuelle Auswahl beim Filtern erlaubt bleibt oder nicht.
	 */
	public isFilterAuswahlPermitted(): boolean {
		return this._filterPermitAuswahl;
	}

	/**
	 * Setzt, ob die aktuelle Auswahl beim Filter erlaubt bleibt oder nicht.
	 *
	 * @param value   der neue boolean-Wert
	 */
	public setFilterAuswahlPermitted(value: boolean): void {
		this._filterPermitAuswahl = value;
	}

	/**
	 * Gibt die vorherige Auswahl zurück oder <code>null</code>, wenn es keine vorherige Auswahl gibt.
	 *
	 * @return vorherige Auswahl
	 */
	public getVorherigeAuswahl(): TDaten | null {
		return this._vorherigeAuswahl;
	}

	/**
	 * Gibt für den übergebenen Listen-Eintrag die ID zurück.
	 *
	 * @param eintrag   der Listen-Eintrag
	 *
	 * @return die zugehörige ID
	 */
	public getIdByEintrag(eintrag: TAuswahl): TID {
		return this._listeToId(eintrag);
	}

	/**
	 * Gibt für das übergebene TDaten Objekt die ID zurück.
	 *
	 * @param daten   das TDaten Objekt
	 *
	 * @return die zugehörige ID
	 */
	public getIdByDaten(daten: TDaten): TID {
		return this._datenToId(daten);
	}

	/**
	 * Setzt die Map der für den Gruppenprozess selektierten Einträge.
	 *
	 * @param listeDaten   Map der selektierten Einträge, indexiert nach ihrer ID
	 */
	public setListeDaten(listeDaten: JavaMap<TID, TDaten>): void {
		this._listeDaten = listeDaten;
	}

	/**
	 * Gibt den Eintrag zu dem übergebenen Schlüssel zurück. Ist dieser nicht vorhanden, so wird als Default
	 * der erste Wert der gefilterten Liste zurückgegeben. Ist diese leer, so wird null zurückgegeben.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag
	 */
	public getEintragOrDefault(id: TID): TAuswahl | null {
		if (this.liste.has(id)) {
			return this.liste.get(id);
		}
		return this.filtered().isEmpty() ? null : this.filtered().get(0);
	}

	/**
	 * Gibt Daten vom Typ TDaten der selektierten Gruppenprozesseinträge
	 *
	 * @return selektierte Daten
	 */
	public getListeDaten(): JavaMap<TID, TDaten> {
		return this._listeDaten;
	}
}
