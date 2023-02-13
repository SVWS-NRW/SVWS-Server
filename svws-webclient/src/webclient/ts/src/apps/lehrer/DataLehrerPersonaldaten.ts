import { routeLogin } from "~/router/RouteLogin";

import { LehrerListeEintrag, LehrerPersonaldaten } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataLehrerPersonaldaten extends BaseData<
	LehrerPersonaldaten,
	LehrerListeEintrag
> {
	protected on_update(daten: Partial<LehrerPersonaldaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<LehrerPersonaldaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<LehrerPersonaldaten | undefined> {
		return super._select((eintrag: LehrerListeEintrag) =>
			routeLogin.data.api.getLehrerPersonaldaten(routeLogin.data.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<LehrerPersonaldaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<LehrerPersonaldaten>): Promise<boolean> {
		const daten = this._daten;
		if (!daten) return false;
		return this._patch(data, () =>
			routeLogin.data.api.patchLehrerPersonaldaten(data, routeLogin.data.schema, daten.id)
		);
	}
}
