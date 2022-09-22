import { reactive } from "vue";
import { BaseData } from "./BaseData";
import {List} from "@svws-nrw/svws-api-ts";


class BaseListReactiveState<T> {

	/** Der Katalog für die Auswahlliste */
	liste : Array<T> = [];

	/** Die gefilterte Liste, sofern ein Filter vorgesehen ist */
	gefiltert: Array<T> = [];

	/** Der aktuell in der Liste ausgewählte Eintrag */
	ausgewaehlt : T|undefined = undefined;

	/** Die aktuelle Auswahl, sofern eine Mehrfachauswahl genutzt wird */
	ausgewaehlt_gruppe: Array<T> = [];

	/** Der aktuelle Ladestatus der Liste */
	pending = false;

}


/**
 * Diese Klasse führt die Auswahlliste der jeweiligen App und welcher Eintrag
 * momentan ausgewählt ist.
 */
export abstract class BaseList<ListenEintragTyp, ListenFilterTyp=undefined> {

	/** Der reaktive Status dieser Auswahlliste */
	protected _state = reactive(new BaseListReactiveState<ListenEintragTyp>()) as BaseListReactiveState<ListenEintragTyp>;

	/** Die zur App gehörenden Daten wie z.B. Stammdaten, Schulbesuchsdaten etc */
	public daten: BaseData<any, any>[] = reactive([]);

	/** Der Status der aktuellen Auswahl. Ein Array mit Promises der Daten bzw. deren Ladezustand */
	private _pending: Promise<unknown | Array<unknown>>[] = [];

	/** Array mit allen Listen, die bei Bedarf dann z.B. aktualisiert werden können bei Abschnittsauswahl */
	static all: BaseList<any, any>[] = [];

	protected abstract _filter: ListenFilterTyp;

	public constructor() {
		BaseList.all.push(this);
	}

	/**
	 * Diese Methode wird bei der Auswahl eines Listeneintrags aufgerufen.
	 * Als default liefert die Klasse BaseList eine erfüllte Promise
	 * zurück. Für ein anderes Verhalten sollte diese Methode überschrieben
	 * werden.
	 * 
	 * @returns eine Promise
	 */
	public async on_select(): Promise<void> {
		return Promise.resolve();
	}

	/**
	 * Liste aller Daten-Instanzen, die bei einem Select aktualisiert werden sollen
	 *
	 * @param {Daten<unknown> | Daten<unknown>[]} daten Dateninstanzen entweder
	 *   alleine oder als Array
	 */
	public add_data(daten: BaseData<any, ListenEintragTyp> | BaseData<any, ListenEintragTyp>[]): void {
		if (Array.isArray(daten)) {
			this.daten = this.daten.concat(daten);
		} else {
			this.daten.push(daten);
		}
	}


	protected async _update_list(getter: () => Promise<List<ListenEintragTyp>>): Promise<void> {
		this._state.pending = true;
		try {
			const result = await getter();
			this.liste = result.toArray(new Array<ListenEintragTyp>());
		} catch (error) {
			console.log(
				"Fehler, die Auswahlliste konnte nicht geladen werden: ",
				error
			);
			this.liste = [];
		}
		this._state.pending = false;
	}


	/**
	 * Diese Methode filtert die Daten der Auswahlliste und wird von dem
	 * AppState-Objekt dieser Klasse beim Laden des Katalogs automatisch aufgerufen. s*
	 *
	 * @returns Void
	 */
	protected filter_liste(): void {
		this.gefiltert = this.liste;
	}


	get liste(): Array<ListenEintragTyp> {
		return this._state.liste as Array<ListenEintragTyp>;
	}


	set liste(liste : Array<ListenEintragTyp>) {
		this._state.liste = liste;
		this.filter_liste();
	}


	get gefiltert(): Array<ListenEintragTyp> {
		return this._state.gefiltert as Array<ListenEintragTyp>;
	}


	set gefiltert(gefiltert : Array<ListenEintragTyp>) {
		this._state.gefiltert = gefiltert;
	}



	get ausgewaehlt_gruppe(): Array<ListenEintragTyp> {
		return this._state.ausgewaehlt_gruppe as Array<ListenEintragTyp>;
	}


	set ausgewaehlt_gruppe(gruppe : Array<ListenEintragTyp>) {
		this._state.ausgewaehlt_gruppe = gruppe;
	}


	/**
	 * Der aktuell ausgewählte Listeneintrag
	 *
	 * @returns {ListenEintragTyp | undefined}
	 */
	get ausgewaehlt(): ListenEintragTyp | undefined {
		if (!this._state.ausgewaehlt)
			return undefined;
		return this._state.ausgewaehlt as ListenEintragTyp;
	}


	/**
	 * Setze einen neuen Listeneintrag
	 *
	 * @param {ListenEintragTyp | undefined} eintrag
	 */
	set ausgewaehlt(eintrag: ListenEintragTyp | undefined) {
		this._state.ausgewaehlt = eintrag;
		if (eintrag)
			this._pending = this.daten.map(d => {
				return d.select(eintrag);
			});
		else this._pending = this.daten.map(d => d.unselect());
		this._pending.push(this.on_select());
		Promise.allSettled(this._pending).then(() => (this._state.pending = false));
	}


	/** Getter für den Schnellfilter */
	public get filter(): ListenFilterTyp {
		return this._filter;
	}

	/** Setter für den Schnellfilter */
	public set filter(value: ListenFilterTyp) {
		this._filter = value;
		this.filter_liste();
	}
}
