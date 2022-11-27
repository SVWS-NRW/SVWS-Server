import { App } from "../BaseApp";

import {
	SchuelerListeEintrag,
	SchuelerStammdaten
} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataSchuelerStammdaten extends BaseData<
	SchuelerStammdaten,
	SchuelerListeEintrag
> {
	protected on_update(daten: Partial<SchuelerStammdaten>): void {
		if (!this.selected_list_item || !this._daten) return;
		if (daten.nachname) this.selected_list_item.nachname = daten.nachname;
		if (daten.vorname) this.selected_list_item.vorname = daten.vorname;
		if (daten.wohnortID) this._daten.ortsteilID = -1;
		if(daten.status) this.selected_list_item.status = daten.status;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<SchuelerStammdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<SchuelerStammdaten | undefined> {
		return super._select((eintrag: SchuelerListeEintrag) =>
			App.api.getSchuelerStammdaten(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<SchuelerStammdaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<SchuelerStammdaten>): Promise<boolean> {
		const daten = this._daten;
		if (!daten) return false;
		return this._patch(data, () =>
			App.api.patchSchuelerStammdaten(data, App.schema, daten.id)
		);
	}
}
