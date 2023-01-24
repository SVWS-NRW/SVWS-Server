import { App } from "../../BaseApp";

import { JahrgangsDaten, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../../BaseData";

export class DataJahrgang extends BaseData<
	JahrgangsDaten,
	JahrgangsListeEintrag
> {
	protected on_update(daten: Partial<JahrgangsDaten>): void {
		if (!this.selected_list_item) return;
		if (daten.kuerzel) this.selected_list_item.kuerzel = daten.kuerzel;
		if (daten.bezeichnung)
			this.selected_list_item.bezeichnung = daten.bezeichnung;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<JahrgangsDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<JahrgangsDaten | undefined> {
		return super._select((eintrag: JahrgangsListeEintrag) =>
			App.api.getJahrgang(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<JahrgangsDaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<JahrgangsDaten>): Promise<boolean> {
		return !!data;
		// return this._patch(data, () =>
		// 	App.api.setJahrgang(
		// 		data,
		// 		App.schema,
		// 		this._daten?.id
		// 	)
		// );
	}
}
