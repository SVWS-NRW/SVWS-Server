import { App } from "~/apps/BaseApp";
import { BaseList } from "~/apps/BaseList";

import { BenutzergruppeListeEintrag} from "@svws-nrw/svws-core-ts";


export class ListBenutzergruppe extends BaseList<BenutzergruppeListeEintrag, undefined> {

	protected _filter = undefined;

	public async on_select(): Promise<void> {
		if (this.ausgewaehlt !== undefined)
			return;
		if (App.apps.benutzer.auswahl.ausgewaehlt !== undefined)
			App.apps.benutzer.auswahl.ausgewaehlt = undefined;
		return;
	}

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getBenutzergruppenliste(App.schema));
	}

}