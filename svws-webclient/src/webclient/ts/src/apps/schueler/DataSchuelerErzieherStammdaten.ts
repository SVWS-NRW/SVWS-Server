import { App } from "../BaseApp";

import {
	ErzieherStammdaten,
	List,
	SchuelerListeEintrag
} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataSchuelerErzieherStammdaten extends BaseData<
	List<ErzieherStammdaten>,
	SchuelerListeEintrag
> {
	protected on_update(daten: Partial<ErzieherStammdaten>): void {
		return void daten;
	}
	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<ErzieherStammdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<List<ErzieherStammdaten> | undefined> {
		return super._select(
			async (
				eintrag: SchuelerListeEintrag
			): Promise<List<ErzieherStammdaten>> => {
				const result = await App.api.getSchuelerErzieher(
					App.schema,
					eintrag.id
				);
				return result;
			}
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<ErzieherStammdaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(
		data: Partial<ErzieherStammdaten>,
		erzieher: ErzieherStammdaten
	): Promise<boolean> {
		if (!erzieher || !this._daten) return false;
		return this._patchElement(
			data,
			() =>
				App.api.patchErzieherStammdaten(
					data as ErzieherStammdaten,
					App.schema,
					erzieher.id || -1
				),
			this._daten.indexOf(erzieher)
		);
	}
}
