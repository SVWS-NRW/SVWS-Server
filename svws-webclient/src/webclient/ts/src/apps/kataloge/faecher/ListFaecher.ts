import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "~/router/RouteLogin";
import { BaseList } from "../../BaseList";

export class ListFaecher extends BaseList<FaecherListeEintrag, undefined> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => routeLogin.data.api.getFaecher(routeLogin.data.schema));
	}

}
