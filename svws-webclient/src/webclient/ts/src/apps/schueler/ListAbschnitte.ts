import { SchuelerLernabschnittListeEintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListAbschnitte extends BaseList<SchuelerLernabschnittListeEintrag> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(id: number): Promise<void> {
		if (!id) return;
		await super._update_list(() =>
			App.api.getSchuelerLernabschnittsliste(App.schema, id)
		);
	}
}
