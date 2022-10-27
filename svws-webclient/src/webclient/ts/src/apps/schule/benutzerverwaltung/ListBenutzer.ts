import { App } from "~/apps/BaseApp";
import { BaseList } from "~/apps/BaseList";

import { BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";

export class ListBenutzer extends BaseList<BenutzerListeEintrag, undefined> {

	protected _filter = undefined;

	public async on_select(): Promise<void> {
		if (this.ausgewaehlt !== undefined)
			return;
		if (App.apps.benutzergruppe.auswahl.ausgewaehlt !== undefined)
			App.apps.benutzergruppe.auswahl.ausgewaehlt = undefined;
		return;
	}

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getBenutzerliste(App.schema));
	}

}