import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { api } from "~/router/Api";
import { BaseList } from "../../BaseList";

export class ListJahrgaenge extends BaseList<JahrgangsListeEintrag> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		if (this.liste.length) return;
		await super._update_list(() => api.server.getJahrgaenge(api.schema));
	}
}
