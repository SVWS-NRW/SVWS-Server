import {
	SchuelerListeEintrag,
} from "@svws-nrw/svws-api-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListSchueler extends BaseList<SchuelerListeEintrag> {
	_filter: any
	
	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(
			() => App.api.getSchuelerAktuell(App.schema)
		);
	}
}
