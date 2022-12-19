import { App } from "~/apps/BaseApp";
import { BaseList } from "~/apps/BaseList";

import { BenutzergruppeListeEintrag} from "@svws-nrw/svws-core-ts";


export class ListBenutzergruppe extends BaseList<BenutzergruppeListeEintrag, undefined> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getBenutzergruppenliste(App.schema));
	}

}