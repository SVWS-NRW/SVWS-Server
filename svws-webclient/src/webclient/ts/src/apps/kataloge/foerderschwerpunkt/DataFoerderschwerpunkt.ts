import { App } from "../../BaseApp";

import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../../BaseData";

export class DataFoerderschwerpunkt extends BaseData<FoerderschwerpunktEintrag, FoerderschwerpunktEintrag> {

	protected on_update(daten: Partial<FoerderschwerpunktEintrag>): void {
		if (!this.selected_list_item) return;
		if (daten.kuerzel) this.selected_list_item.kuerzel = daten.kuerzel;
		if (daten.text)
			this.selected_list_item.text = daten.text;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<FoerderschwerpunktEintrag>} Die Daten als Promise
	 */
	public async on_select(): Promise<FoerderschwerpunktEintrag | undefined> {
		return super._select((eintrag: FoerderschwerpunktEintrag) =>
			App.api.getSchuelerFoerderschwerpunkt(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<FachDaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<FoerderschwerpunktEintrag>): Promise<boolean> {
		return !!data;
		// TODO
		// return this._patch(data, () =>
		// 	App.api.patchSchuelerFoerderschwerpunkt(
		// 		data,
		// 		App.schema,
		// 		this._daten?.id
		// 	)
		// );
	}
}
