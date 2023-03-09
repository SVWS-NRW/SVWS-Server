import { BaseList } from "~/apps/BaseList";

import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core";
import { api } from "~/router/Api";


export class ListBenutzergruppe extends BaseList<BenutzergruppeListeEintrag, undefined> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => api.server.getBenutzergruppenliste(api.schema));
	}

}