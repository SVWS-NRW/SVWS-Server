import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
import { routeApp } from "~/router/RouteApp";
import { routeLogin } from "~/router/RouteLogin";
import { BaseList } from "../BaseList";

export class ListKurse extends BaseList<KursListeEintrag> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() =>
			routeLogin.data.api.getKurseFuerAbschnitt(routeLogin.data.schema, routeApp.data.aktAbschnitt.value.id)
		);
	}
}
