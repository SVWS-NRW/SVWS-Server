import { App } from "../BaseApp";

import { LehrerListeEintrag, LehrerStammdaten } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataLehrerStammdaten extends BaseData<
	LehrerStammdaten,
	LehrerListeEintrag
> {
	protected on_update(daten: Partial<LehrerStammdaten>): void {
		if (!this.selected_list_item || !this._daten) return;
		if (daten.nachname) this.selected_list_item.nachname = daten.nachname;
		if (daten.vorname) this.selected_list_item.vorname = daten.vorname;
		if (daten.kuerzel) this.selected_list_item.kuerzel = daten.kuerzel;
		if (daten.wohnortID) this._daten.ortsteilID = -1;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<LehrerStammdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<LehrerStammdaten | undefined> {
		return super._select((eintrag: LehrerListeEintrag) =>
			App.api.getLehrerStammdaten(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<LehrerStammdaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<LehrerStammdaten>): Promise<boolean> {
		const daten = this._daten;
		if (!daten) return false;
		return this._patch(data, () =>
			App.api.patchLehrerStammdaten(data, App.schema, daten.id)
		);
	}
}
