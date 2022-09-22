import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListSchuelerBetriebsdatenLehrer extends BaseList<LehrerListeEintrag> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Lehrerauswahl bei den Betriebsdaten
	 *
	 * @returns {Promise<void>}
	 */
	 public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getLehrer(App.schema));
	}

}
