import { App } from "../../../BaseApp";

import {BenutzerDaten, BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../../../BaseData";

export class DataBenutzer extends BaseData<BenutzerDaten, BenutzerListeEintrag> {

	protected on_update(daten: Partial<BenutzerDaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<BenutzerDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<BenutzerDaten | undefined> {
		return super._select((eintrag: BenutzerListeEintrag) =>
			App.api.getBenutzerDaten(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<BenutzerDaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	 public async patch(data: Partial<BenutzerDaten>,
	 ): Promise<boolean> {
		 return false;
		// const daten = this._daten;
		// if (!daten)
		// 	return false;
		// return this._patch(data, () => App.api.patchReligion(data, App.schema, Number(daten.id)));
	}
}