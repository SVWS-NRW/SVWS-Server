import { routeLogin } from "~/router/RouteLogin";
import { BaseList } from "~/apps/BaseList";

import { BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";

export class ListBenutzer extends BaseList<BenutzerListeEintrag, undefined> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => routeLogin.data.api.getBenutzerliste(routeLogin.data.schema));
	}

}