import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaFunction } from '../../java/util/function/JavaFunction';
import { AttributMitAuswahl } from '../../core/utils/AttributMitAuswahl';
import { Schulform } from '../../core/types/schule/Schulform';
import type { Runnable } from '../../java/lang/Runnable';
import { ArrayList } from '../../java/util/ArrayList';
import type { Collection } from '../../java/util/Collection';
import type { List } from '../../java/util/List';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Pair } from '../../core/adt/Pair';
import type { Comparator } from '../../java/util/Comparator';

export abstract class AuswahlManager<TID, TAuswahl, TDaten> extends JavaObject {

	/**
	 * Ein Auswahl-Attribut für die Auswahliste. Dieses wird nicht für eine Filterung verwendet, sondern für eine Mehrfachauswahl
	 */
	public readonly liste : AttributMitAuswahl<TID, TAuswahl>;

	/**
	 * Ein Lambda-Ausdruck für das Mapping von einem Auswahl-Objekt auf dessen ID
	 */
	private readonly _listeToId : JavaFunction<TAuswahl, TID>;

	/**
	 * Ein Lambda-Ausdruck für das Mapping von einem Daten-Objekt auf dessen ID
	 */
	private readonly _datenToId : JavaFunction<TDaten, TID>;

	/**
	 * Die Schulform der Schule
	 */
	protected readonly _schulform : Schulform | null;

	/**
	 * Der Schuljahresabschnitt
	 */
	protected readonly _schuljahresabschnitt : number;

	/**
	 * Die gefilterte Liste, sofern sie schon berechnet wurde
	 */
	protected _filtered : List<TAuswahl> | null = null;

	/**
	 * Die Daten, sofern eine Auswahl vorhanden ist.
	 */
	protected _daten : TDaten | null = null;

	/**
	 * Ein Handler für das Ereignis, dass der Filter angepasst wurde
	 */
	protected readonly _eventHandlerFilterChanged : Runnable = { run : () => {
		this.onFilterChanged();
		this._filtered = null;
	} };

	/**
	 * Ein Handler für das Ereignis, dass die Mehrfachauswahl angepasst wurde
	 */
	private readonly _eventHandlerMehrfachauswahlChanged : Runnable = { run : () => this.onMehrfachauswahlChanged() };

	/**
	 * Die Sortier-Ordnung, welche vom Comparator verwendet wird.
	 */
	protected _order : List<Pair<string, boolean>>;

	/**
	 * Gibt an, ob die aktuelle Einzel-Auswahl auch bei dem Filter erlaubt wird oder nicht.
	 */
	protected _filterPermitAuswahl : boolean = false;


	/**
	 * Initialisiert die Auswahl-Manager-Instanz
	 *
	 * @param schuljahresabschnitt   der Schuljahresabschnitt, für welchen die Auswahl bereitgestellt wird.
	 * @param schulform              die Schulform, für welche die Auswahl bereitgestellt wird.
	 * @param values                 die Werte für die Auswahlliste
	 * @param listComparator         ein comparator für das Vergleichen von Auswahl-Werten
	 * @param listeToId              eine Funktion für das Mappen eines Auswahl-Objektes auf seine ID
	 * @param datenToId              eine Funktion für das Mappen eines Daten-Objektes auf seine ID
	 * @param order                  die Default-Sortierung für die Auswahl-Liste
	 */
	protected constructor(schuljahresabschnitt : number, schulform : Schulform | null, values : Collection<TAuswahl>, listComparator : Comparator<TAuswahl>, listeToId : JavaFunction<TAuswahl, TID>, datenToId : JavaFunction<TDaten, TID>, order : List<Pair<string, boolean>>) {
		super();
		this._schuljahresabschnitt = schuljahresabschnitt;
		this._schulform = schulform;
		this._order = order;
		this._listeToId = listeToId;
		this._datenToId = datenToId;
		this.liste = new AttributMitAuswahl(values, this._listeToId, listComparator, this._eventHandlerMehrfachauswahlChanged);
		this._filterPermitAuswahl = true;
	}

	/**
	 * Gibt eine gefilterte Auswahl-Liste zurück. Für die Filterung
	 * muss der Manager die Methode onFilter überschreiben.
	 *
	 * @return die gefilterte Liste
	 */
	public filtered() : List<TAuswahl> {
		if (this._filtered !== null)
			return this._filtered;
		this._filtered = new ArrayList();
		const aktAuswahl : TAuswahl | null = (this._daten === null) ? null : this.auswahl();
		for (const eintrag of this.liste.list())
			if ((this._filterPermitAuswahl && (aktAuswahl !== null) && this.compareAuswahl(aktAuswahl, eintrag) === 0) || this.checkFilter(eintrag))
				this._filtered.add(eintrag);
		const comparator : Comparator<TAuswahl> = { compare : (a: TAuswahl, b: TAuswahl) => this.compareAuswahl(a, b) };
		this._filtered.sort(comparator);
		return this._filtered;
	}

	/**
	 * Prüft, ob der angegebene Eintrag durch den Filter durchgelassen wird oder nicht.
	 *
	 * @param eintrag          der zu prüfende Eintrag
	 *
	 * @return true, wenn der Eintrag den Filter passiert, und ansonsten false
	 */
	protected abstract checkFilter(eintrag : TAuswahl) : boolean;

	/**
	 * Vergleicht zwei Einträge der Auswahl miteinander.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return ein negativer Wert, 0 oder ein positiver Werte, wenn der erste Eintrag
	 *         kleiner, gleich oder größer ist als der zweite Eintrag
	 */
	protected abstract compareAuswahl(a : TAuswahl, b : TAuswahl) : number;

	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an einem Filter stattgefunden hat. Das
	 * Ereignis tritt auf bevor die alte gefilterte Liste ungültig wird.
	 */
	protected onFilterChanged() : void {
		// empty block
	}

	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an der Mehrfachauswahl stattgefunden hat.
	 */
	protected onMehrfachauswahlChanged() : void {
		// empty block
	}

	/**
	 * Setzt die Sortier-Ordnung für die gefilterten Listen. Hier wird eine Menge von Paaren angegeben,
	 * welche das zu sortierende Feld als String angebenen und als boolean ob es aufsteigend (true)
	 * oder absteigend (false) sortiert werden soll.
	 *
	 * @param order   die Sortier-Ordnung
	 */
	public orderSet(order : List<Pair<string, boolean>>) : void {
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
	public orderGet() : List<Pair<string, boolean>> {
		return new ArrayList(this._order);
	}

	/**
	 * Aktualisiert die Reihenfolge bei der Sortierung für das angegebene Feld. Dabei
	 * werden vorhande Feld-Eintrage angepasst oder bei null entfernt. Nicht vorhande
	 * Feld-Einträge werden ergänzt, sofern eine Reihenfolge definiert wird.
	 *
	 * @param field   das Feld
	 * @param order   die Reihenfolge für dieses Feld (ascending: true, descending: false, deaktivieren: null)
	 */
	public orderUpdate(field : string, order : boolean | null) : void {
		if (order === null) {
			for (let i : number = 0; i < this._order.size(); i++) {
				const eintrag : Pair<string, boolean> = this._order.get(i);
				if (JavaObject.equalsTranspiler(eintrag.a, (field))) {
					this._order.remove(eintrag);
					this._filtered = null;
					return;
				}
			}
			return;
		}
		for (let i : number = 0; i < this._order.size(); i++) {
			const eintrag : Pair<string, boolean> = this._order.get(i);
			if (JavaObject.equalsTranspiler(eintrag.a, (field))) {
				if (JavaObject.equalsTranspiler(eintrag.b, (order)))
					return;
				this._order.remove(eintrag);
				eintrag.b = order;
				this._order.add(0, eintrag);
				this._filtered = null;
				return;
			}
		}
		const eintrag : Pair<string, boolean> = new Pair(field, order);
		this._order.add(0, eintrag);
		this._filtered = null;
	}

	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @return die Schulform der Schule
	 */
	public schulform() : Schulform {
		if (this._schulform === null)
			throw new DeveloperNotificationException("Der Auswahl-Manager sollte nur mit einer korrekt gesetzten Schulform verwendet werden.")
		return this._schulform;
	}

	/**
	 * Gibt zurück, ob eine Auswahl und damit auch Daten vorliegen.
	 *
	 * @return true, wenn eine Auswahl und Daten vorliegen, und ansonsten false
	 */
	public hasDaten() : boolean {
		return this._daten !== null;
	}

	/**
	 * Gibt die Daten der aktuellen Auswahl zurück.
	 *
	 * @return die Daten
	 */
	public daten() : TDaten {
		if (this._daten === null)
			throw new DeveloperNotificationException("Es exitsiert derzeit keine Auswahl und damit auch keine Daten")
		return this._daten;
	}

	/**
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	public setDaten(daten : TDaten | null) : void {
		if (daten === null) {
			this._daten = null;
			return;
		}
		const eintrag : TAuswahl = this.liste.getOrException(this._datenToId.apply(daten));
		const updateEintrag : boolean = this.onSetDaten(eintrag, daten);
		this._daten = daten;
		if (updateEintrag)
			this.orderSet(this.orderGet());
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
	protected onSetDaten(eintrag : TAuswahl, daten : TDaten) : boolean {
		return false;
	}

	/**
	 * Gibt die ID der Auswahl zurück. Ist keine Auswahl vorhanden, so wird null zurückgegeben.
	 *
	 * @return die ID oder null
	 */
	public auswahlID() : TID | null {
		return this._daten === null ? null : this._datenToId.apply(this._daten);
	}

	/**
	 * Gibt den Eintrag der aktuellen Auswahl in der Liste zurück. Hiefür muss eine
	 * gültige Auswahl vorliegen. Dies kann ggf. vorher über hasDaten geprüft werden.
	 *
	 * @return der Eintrag in der Auswahlliste
	 *
	 * @throws DeveloperNotificationException wenn keine gültige Auswahl vorliegt
	 */
	public auswahl() : TAuswahl {
		if (this._daten === null)
			throw new DeveloperNotificationException("Für den Aufruf dieser Methode muss zuvor eine Auswahl vorliegen.")
		return this.liste.getOrException(this._datenToId.apply(this._daten));
	}

	/**
	 * Gibt zurück, ob die aktuelle Auswahl beim Filter erlaubt bleibt oder nicht.
	 *
	 * @return true, falls die aktuelle Auswahl beim Filtern erlaubt bleibt oder nicht.
	 */
	public isFilterAuswahlPermitted() : boolean {
		return this._filterPermitAuswahl;
	}

	/**
	 * Setzt, ob die aktuelle Auswahl beim Filter erlaubt bleibt oder nicht.
	 *
	 * @param value   der neue boolean-Wert
	 */
	public setFilterAuswahlPermitted(value : boolean) : void {
		this._filterPermitAuswahl = value;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.AuswahlManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_AuswahlManager<TID, TAuswahl, TDaten>(obj : unknown) : AuswahlManager<TID, TAuswahl, TDaten> {
	return obj as AuswahlManager<TID, TAuswahl, TDaten>;
}
