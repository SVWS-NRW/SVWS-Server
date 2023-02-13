import { KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { routeApp } from "~/router/RouteApp";
import { routeLogin } from "~/router/RouteLogin";
import { BaseList } from "../BaseList";

export class ListKlassen extends BaseList<KlassenListeEintrag> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => routeLogin.data.api.getKlassenFuerAbschnitt(routeLogin.data.schema, routeApp.data.aktAbschnitt.value.id));
	}

}
