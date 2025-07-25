import { JavaObject } from '../../java/lang/JavaObject';
import { AttributMitAuswahl } from '../../core/utils/AttributMitAuswahl';
import { HashMap } from '../../java/util/HashMap';
import { Schulform } from '../../asd/types/schule/Schulform';
import { ArrayList } from '../../java/util/ArrayList';
import { SchuljahresabschnittsUtils } from '../../core/utils/schule/SchuljahresabschnittsUtils';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../java/util/Comparator';
import type { JavaFunction } from '../../java/util/function/JavaFunction';
import type { Runnable } from '../../java/lang/Runnable';
import type { Collection } from '../../java/util/Collection';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { Schuljahresabschnitt } from '../../asd/data/schule/Schuljahresabschnitt';
import type { JavaMap } from '../../java/util/JavaMap';
import { Pair } from '../../asd/adt/Pair';

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
	 * Der Schuljahresabschnitt, welcher für die Auswahl genutzt wird
	 */
	protected readonly _schuljahresabschnitt : number;

	/**
	 * Der Schuljahresabschnitt, in dem sich die Schule befindet
	 */
	protected readonly _schuljahresabschnittSchule : number;

	/**
	 * Das Filter-Attribut für die Schuljahresabschnitte - die Filterfunktion wird zur Zeit noch nicht genutzt
	 */
	public readonly schuljahresabschnitte : AttributMitAuswahl<number, Schuljahresabschnitt>;

	private static readonly _schuljahresabschnittToId : JavaFunction<Schuljahresabschnitt, number> = { apply : (sja: Schuljahresabschnitt) => sja.id };

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
	 * Ein Handler für das Ereignis, dass die Liste mit der Mehrfachauswahl angepasst wurde
	 */
	private readonly _eventHandlerListeChanged : Runnable = { run : () => this.onListeChangedInternal() };

	/**
	 * Die Sortier-Ordnung, welche vom Comparator verwendet wird.
	 */
	protected _order : List<Pair<string, boolean>>;

	/**
	 * Gibt an, ob die aktuelle Einzel-Auswahl auch bei dem Filter erlaubt wird oder nicht.
	 */
	protected _filterPermitAuswahl : boolean = false;

	/**
	 * Die Daten aus der vorherigen Auswahl.
	 */
	protected _vorherigeAuswahl : TDaten | null = null;

	/**
	 * Map mit allen selektierten Gruppenprozess CoreDto Objekten
	 */
	protected _listeDaten : JavaMap<TID, TDaten> = new HashMap<TID, TDaten>();


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
	protected constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, values : Collection<TAuswahl>, listComparator : Comparator<TAuswahl>, listeToId : JavaFunction<TAuswahl, TID>, datenToId : JavaFunction<TDaten, TID>, order : List<Pair<string, boolean>>) {
		super();
		this._schuljahresabschnitt = schuljahresabschnitt;
		this._schuljahresabschnittSchule = schuljahresabschnittSchule;
		this.schuljahresabschnitte = new AttributMitAuswahl(schuljahresabschnitte, AuswahlManager._schuljahresabschnittToId, SchuljahresabschnittsUtils.comparator, this._eventHandlerFilterChanged);
		this._schulform = schulform;
		this._order = order;
		this._listeToId = listeToId;
		this._datenToId = datenToId;
		this.liste = new AttributMitAuswahl(values, this._listeToId, listComparator, this._eventHandlerMehrfachauswahlChanged);
		this.liste.setEventHandlerListeGeaendert(this._eventHandlerListeChanged);
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
			if ((this._filterPermitAuswahl && (aktAuswahl !== null) && (this.compareAuswahl(aktAuswahl, eintrag) === 0)) || this.checkFilter(eintrag))
				this._filtered.add(eintrag);
		const comparator : Comparator<TAuswahl> = { compare : (a: TAuswahl, b: TAuswahl) => this.compareAuswahl(a, b) };
		this._filtered.sort(comparator);
		return this._filtered;
	}

	/**
	 * Entfernt den aktuelle Cache für die gefilterte Liste und forciert so eine Neu-Berechnung der gecachten Liste
	 */
	public filterInvalidateCache() : void {
		this._filtered = null;
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
	 * Die Methode wird aufgerufen, wenn eine Änderung an der Liste mit den verfügbaren Daten
	 * eine Änderung vorgenommen wird.
	 */
	private onListeChangedInternal() : void {
		const idAuswahl : TID | null = this.auswahlID();
		if (idAuswahl !== null) {
			if (this.liste.get(idAuswahl) === null)
				this.setDaten(null);
			else
				this.setDaten(this.daten());
		}
		this.onListeChanged();
	}

	/**
	 * Diese Methode kann überschrieben werden.
	 * Sie wird aufgerufen, wenn eine Änderung an der Liste der für die Mehrfachauswahl
	 * zulässigen Werte stattgefunden hat.
	 */
	protected onListeChanged() : void {
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
	 * @return die Sortier-Ordnung
	 */
	public orderGet() : List<Pair<string, boolean>> {
		return new ArrayList<Pair<string, boolean>>(this._order);
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
		const eintrag : Pair<string, boolean> = new Pair<string, boolean>(field, order);
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
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst. Die vorherige Auswahl wird gespeichert.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	public setDaten(daten : TDaten | null) : void {
		this._vorherigeAuswahl = this._daten;
		if (daten === null) {
			this._daten = null;
		} else {
			const eintrag : TAuswahl = this.liste.getOrException(this._datenToId.apply(daten));
			const updateEintrag : boolean = this.onSetDaten(eintrag, daten);
			this._daten = daten;
			if (updateEintrag)
				this.orderSet(this.orderGet());
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
	protected onSetDaten(eintrag : TAuswahl, daten : TDaten) : boolean {
		return false;
	}

	/**
	 * Gibt die ID der Auswahl zurück. Ist keine Auswahl vorhanden, so wird null zurückgegeben.
	 *
	 * @return die ID oder null
	 */
	public auswahlID() : TID | null {
		return (this._daten === null) ? null : this._datenToId.apply(this._daten);
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

	/**
	 * Gibt den Schuljahresabschnitt der Auswahl zurück.
	 *
	 * @return der Schuljahresabschnitt der Auswahl
	 */
	public getSchuljahresabschnittAuswahl() : Schuljahresabschnitt | null {
		return this.schuljahresabschnitte.get(this._schuljahresabschnitt);
	}

	/**
	 * Gibt den Schuljahresabschnitt der Schule zurück.
	 *
	 * @return der Schuljahresabschnitt der Schule
	 */
	public getSchuljahresabschnittSchule() : Schuljahresabschnitt {
		const result : Schuljahresabschnitt | null = this.schuljahresabschnitte.get(this._schuljahresabschnittSchule);
		if (result === null)
			throw new DeveloperNotificationException("Der Schuljahresabschnitt der Schule ist nicht verfügbar.")
		return result;
	}

	/**
	 * Gibt das Schuljahr des Schuljahresabschnitts der Schule zurück.
	 *
	 * @return das Schuljahr
	 */
	public getSchuljahr() : number {
		return this.getSchuljahresabschnittSchule().schuljahr;
	}

	/**
	 * Gibt zurück, ob der Schuljahresabschnitt der Auswahl mit dem aktuellen
	 * Schuljahresabschnitt der Schule übereinstimmt.
	 *
	 * @return true, wenn die Schuljahresabschnitte übereinstimmen
	 */
	public istSchuljahresabschnittAktuell() : boolean {
		const abschnittAuswahl : Schuljahresabschnitt | null = this.getSchuljahresabschnittAuswahl();
		const abschnittSchule : Schuljahresabschnitt | null = this.getSchuljahresabschnittSchule();
		if (abschnittAuswahl === null)
			return false;
		return (abschnittAuswahl.schuljahr === abschnittSchule.schuljahr) && (abschnittAuswahl.abschnitt === abschnittSchule.abschnitt);
	}

	/**
	 * Gibt zurück, ob sich bei dem Schuljahresabschnitt der Auswahl um ein
	 * Abschnitt in Planung handelt, d.h. ob der Schuljahresabschnitt der Auswahl
	 * nach dem aktuellen Schuljahresabschnitt der Schule liegt.
	 *
	 * @return true, wenn der Schuljahresabschnitt der Auswahl ein Planungsabschnitt ist
	 */
	public istSchuljahresabschnittPlanung() : boolean {
		const abschnittAuswahl : Schuljahresabschnitt | null = this.getSchuljahresabschnittAuswahl();
		const abschnittSchule : Schuljahresabschnitt | null = this.getSchuljahresabschnittSchule();
		if (abschnittAuswahl === null)
			return false;
		return (abschnittAuswahl.schuljahr > abschnittSchule.schuljahr) || ((abschnittAuswahl.schuljahr === abschnittSchule.schuljahr) && (abschnittAuswahl.abschnitt > abschnittSchule.abschnitt));
	}

	/**
	 * Gibt zurück, ob sich bei dem Schuljahresabschnitt der Auswahl um einen
	 * Abschnitt in der Vergangengheit handelt, d.h. ob der Schuljahresabschnitt
	 * der Auswahl vor dem aktuellen Schuljahresabschnitt der Schule liegt.
	 *
	 * @return true, wenn der Schuljahresabschnitt der Auswahl ein vergangener Abschnitt ist
	 */
	public istSchuljahresabschnittVergangenheit() : boolean {
		const abschnittAuswahl : Schuljahresabschnitt | null = this.getSchuljahresabschnittAuswahl();
		const abschnittSchule : Schuljahresabschnitt | null = this.getSchuljahresabschnittSchule();
		if (abschnittAuswahl === null)
			return false;
		return (abschnittAuswahl.schuljahr < abschnittSchule.schuljahr) || ((abschnittAuswahl.schuljahr === abschnittSchule.schuljahr) && (abschnittAuswahl.abschnitt < abschnittSchule.abschnitt));
	}

	/**
	 * Gibt die vorherige Auswahl zurück oder <code>null</code>, wenn es keine vorherige Auswahl gibt.
	 *
	 * @return vorherige Auswahl
	 */
	public getVorherigeAuswahl() : TDaten | null {
		return this._vorherigeAuswahl;
	}

	/**
	 * Gibt für den übergebenen Listen-Eintrag die ID zurück.
	 *
	 * @param eintrag   der Listen-Eintrag
	 *
	 * @return die zugehörige ID
	 */
	public getIdByEintrag(eintrag : TAuswahl) : TID {
		return this._listeToId.apply(eintrag);
	}

	/**
	 * Gibt für das übergebene TDaten Objekt die ID zurück.
	 *
	 * @param daten   das TDaten Objekt
	 *
	 * @return die zugehörige ID
	 */
	public getIdByDaten(daten : TDaten) : TID {
		return this._datenToId.apply(daten);
	}

	/**
	 * Setzt Daten vom Typ TDaten der selektierten Gruppenprozesseinträge
	 *
	 * @param listeDaten selektierte Daten
	 */
	public setListeDaten(listeDaten : JavaMap<TID, TDaten>) : void {
		this._listeDaten = listeDaten;
	}

	/**
	 * Gibt Daten vom Typ TDaten der selektierten Gruppenprozesseinträge
	 *
	 * @return selektierte Daten
	 */
	public getListeDaten() : JavaMap<TID, TDaten> {
		return this._listeDaten;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.AuswahlManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager'].includes(name);
	}

	public static class = new Class<AuswahlManager<any, any, any>>('de.svws_nrw.core.utils.AuswahlManager');

}

export function cast_de_svws_nrw_core_utils_AuswahlManager<TID, TAuswahl, TDaten>(obj : unknown) : AuswahlManager<TID, TAuswahl, TDaten> {
	return obj as AuswahlManager<TID, TAuswahl, TDaten>;
}
