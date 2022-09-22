import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListFaecher extends BaseList<FaecherListeEintrag, undefined> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getFaecher(App.schema));
	}
}
