import { App } from "../BaseApp";

import { Erzieherart, List } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataKatalogErzieherarten extends BaseData<List<Erzieherart>, unknown> {

	/**
	 * Holt den Katalog vom SVWS-Server.
	 *
	 * @returns {Promise<List<Erzieherart>} Die Katalog.Daten als Promise
	 */
	public async on_select(): Promise<List<Erzieherart> | undefined> {
		return super._select(() => App.api.getErzieherArten(App.schema));
	}

	protected on_update(daten: Partial<List<Erzieherart>>): void {
		return void daten;
	}

	public async patch(data: Partial<List<Erzieherart>>): Promise<boolean> {
		return false;
	}
}
