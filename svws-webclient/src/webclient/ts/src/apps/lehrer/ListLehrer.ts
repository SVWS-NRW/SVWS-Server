import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "~/router/RouteLogin";
import { BaseList } from "../BaseList";

export class ListLehrer extends BaseList<LehrerListeEintrag> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Lehrerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => routeLogin.data.api.getLehrer(routeLogin.data.schema));
		if (!this.ausgewaehlt)
			this.ausgewaehlt = this.gefiltert[0]
	}

}
