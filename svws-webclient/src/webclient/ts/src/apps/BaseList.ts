import { reactive } from "vue";
import { BaseData } from "./BaseData";
import { List } from "@svws-nrw/svws-core-ts";

class BaseListReactiveState<T> {
	/** Der Katalog für die Auswahlliste */
	liste: Array<T> = [];

	/** Die gefilterte Liste, sofern ein Filter vorgesehen ist */
	gefiltert: Array<T> = [];

	/** Der aktuell in der Liste ausgewählte Eintrag */
	ausgewaehlt: T | undefined = undefined;

	/** Die aktuelle Auswahl, sofern eine Mehrfachauswahl genutzt wird */
	ausgewaehlt_gruppe: Array<T> = [];

	/** Der aktuelle Ladestatus der Liste */
	pending = false;
}

/**
 * Diese Klasse führt die Auswahlliste der jeweiligen App und welcher Eintrag
 * momentan ausgewählt ist.
 */
export abstract class BaseList<ListenEintragTyp, ListenFilterTyp = undefined> {
	/** Der reaktive Status dieser Auswahlliste */
	protected _state = reactive(
		new BaseListReactiveState<ListenEintragTyp>()
	) as BaseListReactiveState<ListenEintragTyp>;

	/** Die zur App gehörenden Daten wie z.B. Stammdaten, Schulbesuchsdaten etc */
	public daten: BaseData<unknown, ListenEintragTyp>[] = reactive([]);

	/** Der Status der aktuellen Auswahl. Ein Array mit Promises der Daten bzw. deren Ladezustand */
	private _pending: Promise<unknown | Array<unknown>>[] = [];

	/** Array mit allen Listen, die bei Bedarf dann z.B. aktualisiert werden können bei Abschnittsauswahl */
	static all: BaseList<unknown, unknown>[] = [];

	protected abstract _filter: ListenFilterTyp;

	public constructor() {
		BaseList.all.push(this);
	}

	/**
	 * Diese Methode wird vor der Auswahl eines Listeneintrags aufgerufen.
	 * Diese Methode arbeitet synchron.
	 * Für ein anderes Verhalten sollte diese Methode überschrieben
	 * werden.
	 *
	 * @returns {Promise<void>} ein Promise
	 */
	public before_select(eintrag: ListenEintragTyp | undefined): void {
		return void eintrag;
	}

	/**
	 * Diese Methode wird bei der Auswahl eines Listeneintrags aufgerufen.
	 * Als default liefert die Klasse BaseList ein Promise
	 * zurück. Für ein anderes Verhalten sollte diese Methode überschrieben
	 * werden.
	 *
	 * @returns {Promise<void>} ein Promise
	 */
	public async on_select(): Promise<void> {
		return
	}

	/**
	 * Liste aller Daten-Instanzen, die bei einem Select aktualisiert werden sollen
	 *
	 * @param {BaseData<unknown, ListenEintragTyp> | BaseData<unknown, ListenEintragTyp>[]} daten Dateninstanzen entweder
	 *   alleine oder als Array
	 */
	public add_data(
		daten:
			| BaseData<unknown, ListenEintragTyp>
			| BaseData<unknown, ListenEintragTyp>[]
	): void {
		if (Array.isArray(daten)) {
			this.daten = this.daten.concat(daten);
		} else {
			this.daten.push(daten);
		}
	}

	/**
	 * Methode, die von allen Subklassen implementiert werden muss.
	 * Sie ruft _update_list auf und übergibt den Getter für die Listendaten.
	 */
	public abstract update_list(...args: unknown[]): Promise<void>

	protected async _update_list(
		getter: () => Promise<List<ListenEintragTyp>>
	): Promise<void> {
		this._state.pending = true;
		try {
			const result = await getter();
			if (result.size() === 0)
				this.ausgewaehlt = undefined;
			this.liste = result.toArray(new Array<ListenEintragTyp>());
		} catch (error) {
			throw error;
			// console.log( "Fehler, die Auswahlliste konnte nicht geladen werden: ", error);
			// this.liste = [];
		} finally {
			this._state.pending = false;
		}
	}

	/**
	 * Diese Methode filtert die Daten der Auswahlliste und wird von dem
	 * AppState-Objekt dieser Klasse beim Laden des Katalogs automatisch aufgerufen.
	 *
	 * @returns Void
	 */
	protected filter_liste(): void {
		this.gefiltert = this.liste;
	}

	get liste(): Array<ListenEintragTyp> {
		return this._state.liste;
	}

	set liste(liste: Array<ListenEintragTyp>) {
		this._state.liste = liste;
		this.filter_liste();
	}

	get gefiltert(): Array<ListenEintragTyp> {
		return this._state.gefiltert;
	}

	set gefiltert(gefiltert: Array<ListenEintragTyp>) {
		this._state.gefiltert = gefiltert;
	}

	get ausgewaehlt_gruppe(): Array<ListenEintragTyp> {
		return this._state.ausgewaehlt_gruppe;
	}

	set ausgewaehlt_gruppe(gruppe: Array<ListenEintragTyp>) {
		this._state.ausgewaehlt_gruppe = gruppe;
	}

	/**
	 * Der aktuell ausgewählte Listeneintrag
	 *
	 * @returns {ListenEintragTyp | undefined}
	 */
	get ausgewaehlt(): ListenEintragTyp | undefined {
		return this._state.ausgewaehlt;
	}

	/**
	 * Setze einen neuen Listeneintrag
	 *
	 * @param {ListenEintragTyp | undefined} eintrag
	 */
	set ausgewaehlt(eintrag: ListenEintragTyp | undefined) {
		try {
			this.before_select(eintrag);
		} catch (e) {
			return;
		}
		this._state.pending = true;
		this._state.ausgewaehlt = eintrag;
		this._pending = [];
		if (eintrag)
			this._pending.concat(this.daten.map(d => { return d.select(eintrag); }));
		else
			this._pending.concat(this.daten.map(d => d.unselect()));
		Promise.allSettled(this._pending).then(()=> this.on_select()).then(() => this._state.pending = false);
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

	public get pending(): boolean {
		return this._state.pending;
	}
}
