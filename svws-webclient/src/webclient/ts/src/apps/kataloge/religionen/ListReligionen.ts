import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "~/router/RouteLogin";
import { BaseList } from "../../BaseList";

export class ListReligionen extends BaseList<ReligionEintrag, undefined> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl der Förderschwerpunkte
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => routeLogin.data.api.getReligionen(routeLogin.data.schema));
	}
}