import { App } from "../../BaseApp";

import {ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../../BaseData";

export class DataReligion extends BaseData<ReligionEintrag , ReligionEintrag> {

	protected on_update(daten: Partial<ReligionEintrag>): void {
		if (!this.selected_list_item) return;
		if (daten.kuerzel) this.selected_list_item.kuerzel = daten.kuerzel;
		if (daten.text)
			this.selected_list_item.text = daten.text;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<ReligionEintrag>} Die Daten als Promise
	 */
	public async on_select(): Promise<ReligionEintrag | undefined> {
		return super._select((eintrag: ReligionEintrag) =>
			App.api.getReligion(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<ReligionEintrag>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	 public async patch(data: Partial<ReligionEintrag>,
	): Promise<boolean> {
		const daten = this._daten;
		if (!daten)
			return false;
		return this._patch(data, () => App.api.patchReligion(data, App.schema, Number(daten.id)));
	}
}