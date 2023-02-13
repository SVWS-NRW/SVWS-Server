import { routeLogin } from "~/router/RouteLogin";

import { KatalogEintrag, List } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataKatalogFahrschuelerarten extends BaseData<List<KatalogEintrag>, unknown> {

	/**
	 * Holt den Katalog vom SVWS-Server.
	 *
	 * @returns {Promise<List<KatalogEintrag>} Die Katalog.Daten als Promise
	 */
	public async on_select(): Promise<List<KatalogEintrag> | undefined> {
		try {
			this._daten = await routeLogin.data.api.getSchuelerFahrschuelerarten(routeLogin.data.schema);
			return this._daten;
		} catch (error) {
			console.log(`Fehler: ${error}`);
			return this.unselect();
		}
	}

	protected on_update(daten: Partial<List<KatalogEintrag>>): void {
		return void daten;
	}

	public async patch(data: Partial<List<KatalogEintrag>>): Promise<boolean> {
		return false;
	}
}
