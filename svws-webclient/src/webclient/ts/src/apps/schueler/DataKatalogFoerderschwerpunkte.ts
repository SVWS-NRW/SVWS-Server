import { App } from "../BaseApp";

import { FoerderschwerpunktEintrag, List } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataKatalogFoerderschwerpunkte extends BaseData<List<FoerderschwerpunktEintrag>, unknown> {

	/**
	 * Holt den Katalog vom SVWS-Server.
	 *
	 * @returns {Promise<List<KatalogEintrag>} Die Katalog.Daten als Promise
	 */
	public async on_select(): Promise<List<FoerderschwerpunktEintrag> | undefined> {
		try {
			this._daten = await App.api.getSchuelerFoerderschwerpunkte(App.schema);
			return this._daten;
		} catch (error) {
			console.log(`Fehler: ${error}`);
			return this.unselect();
		}
	}

	protected on_update(daten: Partial<List<FoerderschwerpunktEintrag>>): void {
		return void daten;
	}

	public async patch(data: Partial<List<FoerderschwerpunktEintrag>>): Promise<boolean> {
		return false;
	}
}
