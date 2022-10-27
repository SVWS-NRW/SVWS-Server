import { App } from "~/apps/BaseApp";
import { BaseList } from "~/apps/BaseList";

import { BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";


export class ListBenutzergruppenBenutzer extends BaseList<BenutzerListeEintrag> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl bei den Benutzergruppen
	 *
	 * @returns {Promise<void>}
	 */
	 public async update_list(gruppen_id: number | undefined): Promise<void> {
		if (!gruppen_id) {
			this.liste = [];
			return;
		}
		await super._update_list(() => App.api.getBenutzerMitGruppenID(App.schema, gruppen_id));
		this.ausgewaehlt = this.liste[0];
	}

}
