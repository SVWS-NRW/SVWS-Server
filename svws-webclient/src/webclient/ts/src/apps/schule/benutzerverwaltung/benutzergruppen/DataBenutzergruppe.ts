import { App } from "../../../BaseApp";

import {BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../../../BaseData";
import { ListBenutzergruppenBenutzer } from "./ListBenutzergruppenBenutzer";

export class DataBenutzergruppe extends BaseData<BenutzergruppeDaten, BenutzergruppeListeEintrag> {


	public listBenutzergruppenBenutzer!: ListBenutzergruppenBenutzer = new ListBenutzergruppenBenutzer();
	protected on_update(daten: Partial<BenutzergruppeDaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<BenutzergruppeDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<BenutzergruppeDaten | undefined> {
		console.log("DatenBenutzergruppe->on_select")
		const response = await super._select((eintrag: BenutzergruppeListeEintrag) =>
			App.api.getBenutzergruppeDaten(App.schema, eintrag.id)
		);
		if(this._daten?.id)
			await this.listBenutzergruppenBenutzer.update_list(this._daten?.id);
		console.log(this.listBenutzergruppenBenutzer.liste);
		return response;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<BenutzergruppeDaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	 public async patch(data: Partial<BenutzergruppeDaten>,
	 ): Promise<boolean> {
		 return false;
		// const daten = this._daten;
		// if (!daten)
		// 	return false;
		// return this._patch(data, () => App.api.patchReligion(data, App.schema, Number(daten.id)));
	}
}