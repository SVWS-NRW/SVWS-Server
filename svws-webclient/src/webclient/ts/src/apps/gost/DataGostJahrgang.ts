import { App } from "../BaseApp";

import { GostJahrgang, GostJahrgangsdaten } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataGostJahrgang extends BaseData<GostJahrgangsdaten, GostJahrgang> {

	protected on_update(daten: Partial<GostJahrgangsdaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<GostJahrgangsdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<GostJahrgangsdaten | undefined> {
		if (!this.selected_list_item?.abiturjahr)
			return await super.unselect();
		try {
			this.pending = true;
			const res = await super._select((eintrag: GostJahrgang) => App.api.getGostAbiturjahrgang(App.schema, eintrag.abiturjahr?.valueOf() || -1));
			this.pending = false;
			return res;
		} catch (e) {
			return undefined;
		}
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<GostJahrgangsdaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<GostJahrgangsdaten>): Promise<boolean> {
		const daten = this._daten;
		if (!daten || daten.abiturjahr === null)
			return false;
		const abijahr = daten.abiturjahr.valueOf();
		return await this._patch(data, () => App.api.patchGostAbiturjahrgang(data as GostJahrgangsdaten, App.schema, abijahr));
	}

	/** Erstellt einen neuen Abiturjahrgang für die Gost mittels übergebener Jahrgang-ID
	 * @param {number} id Die ID des GostJahrgangsdaten
	 * @returns {Promise<number>} Gibt das Abiturjahr zurück als Promise
	 */
	public async post_jahrgang(id: number): Promise<number> {
		const abiturjahr = await App.api.createGostAbiturjahrgang(App.schema, id);
		return abiturjahr.valueOf();
	}

}
