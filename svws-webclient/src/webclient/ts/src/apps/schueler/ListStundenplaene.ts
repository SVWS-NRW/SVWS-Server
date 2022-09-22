import { StundenplanListeEintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListStundenplaene extends BaseList<StundenplanListeEintrag> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Stundenpläne
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		const abschnitt = App.akt_abschnitt;
		if (!abschnitt) 
			return;
		await super._update_list(() => App.api.getStundenplanlisteFuerAbschnitt(App.schema, abschnitt.id));
	}
}
