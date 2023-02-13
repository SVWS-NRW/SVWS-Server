import { routeLogin } from "~/router/RouteLogin";

import { KlassenDaten, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataKlasse extends BaseData<KlassenDaten, KlassenListeEintrag> {
	protected on_update(daten: Partial<KlassenDaten>): void {
		if (!this.selected_list_item) return;
		if (daten.kuerzel) this.selected_list_item.kuerzel = daten.kuerzel;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<KlassenDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<KlassenDaten | undefined> {
		return super._select((eintrag: KlassenListeEintrag) =>
			routeLogin.data.api.getKlasse(routeLogin.data.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<KlassenDaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<KlassenDaten>): Promise<boolean> {
		return !!data;
		// return this._patch(data, () =>
		// 	routeLogin.data.api.setKlasse(
		// 		routeLogin.data.schema,
		// 		data,
		// 		this._daten?.id
		// 	)
		// );
	}
}
