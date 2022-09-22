import { BetriebListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListSchuelerBetriebsdatenBetriebe extends BaseList<BetriebListeEintrag> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Lehrerauswahl bei den Betriebsdaten
	 *
	 * @returns {Promise<void>}
	 */
	 public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getBetriebe(App.schema));
	}

}
