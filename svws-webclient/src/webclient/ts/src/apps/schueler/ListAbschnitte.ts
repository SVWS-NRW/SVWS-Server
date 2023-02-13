import { SchuelerLernabschnittListeEintrag } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "~/router/RouteLogin";
import { BaseList } from "../BaseList";

export class ListAbschnitte extends BaseList<SchuelerLernabschnittListeEintrag> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(id: number): Promise<void> {
		if (!id)
			return;
		await super._update_list(() =>
			routeLogin.data.api.getSchuelerLernabschnittsliste(routeLogin.data.schema, id)
		);
	}
}
