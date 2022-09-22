import { GostBlockungsergebnisListeneintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListKursblockungsergebnisse extends BaseList<GostBlockungsergebnisListeneintrag> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl der Blockungsergebnisse
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(blockung_id: number | undefined): Promise<void> {
		if (!blockung_id) {
			this.liste = [];
			return;
		}
		await super._update_list(() =>
			App.api.getGostBlockungsergebnisliste(App.schema, blockung_id)
		);
	}
}
