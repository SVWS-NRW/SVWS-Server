import { App } from "../BaseApp";

import { Abiturdaten, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataSchuelerLaufbahndaten extends BaseData<
	Abiturdaten,
	SchuelerListeEintrag
> {
	protected on_update(daten: Partial<Abiturdaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<Abiturdaten> | undefined} Die Daten als Promise
	 */
	public async on_select(): Promise<Abiturdaten | undefined> {
		if (!this.selected_list_item) return super.unselect();
		return await super._select((eintrag: SchuelerListeEintrag) =>
			App.api.getGostSchuelerLaufbahnplanung(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<Abiturdaten>} data Die Daten, die aktualisiert
	 *   werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<Abiturdaten>): Promise<boolean> {
		return !!data;
	}
}
