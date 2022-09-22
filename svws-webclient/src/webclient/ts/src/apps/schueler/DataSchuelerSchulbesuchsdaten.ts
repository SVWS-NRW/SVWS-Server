import { App } from "../BaseApp";

import {
	SchuelerListeEintrag,
	SchuelerSchulbesuchsdaten
} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataSchuelerSchulbesuchsdaten extends BaseData<
	SchuelerSchulbesuchsdaten,
	SchuelerListeEintrag
> {
	protected on_update(daten: Partial<SchuelerSchulbesuchsdaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<SchuelerSchulbesuchsdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<SchuelerSchulbesuchsdaten | undefined> {
		return super._select((eintrag: SchuelerListeEintrag) =>
			App.api.getSchuelerSchulbesuch(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<SchuelerSchulbesuchsdaten>} data Die Daten, die
	 *   aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(
		data: Partial<SchuelerSchulbesuchsdaten>
	): Promise<boolean> {
		const daten = this._daten;
		if (!daten) return false;
		return this._patch(data, () =>
			App.api.patchSchuelerSchulbesuch(data, App.schema, daten.id)
		);
	}
}
