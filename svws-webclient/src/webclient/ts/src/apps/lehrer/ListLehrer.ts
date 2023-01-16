import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListLehrer extends BaseList<LehrerListeEintrag> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Lehrerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getLehrer(App.schema));
		if (!this.ausgewaehlt)
			this.ausgewaehlt = this.gefiltert[0]
	}

}
