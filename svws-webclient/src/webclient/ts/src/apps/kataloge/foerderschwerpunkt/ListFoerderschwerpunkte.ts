import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
import { api } from "~/router/Api";
import { BaseList } from "../../BaseList";

export class ListFoerderschwerpunkte extends BaseList<FoerderschwerpunktEintrag, undefined> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl der Förderschwerpunkte
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => api.server.getSchuelerFoerderschwerpunkte(api.schema));
	}
}
