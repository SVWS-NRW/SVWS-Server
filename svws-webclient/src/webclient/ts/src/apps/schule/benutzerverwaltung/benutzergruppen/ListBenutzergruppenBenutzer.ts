import { BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";
import { App } from "~/apps/BaseApp";
import { BaseList } from "~/apps/BaseList";


export class ListBenutzergruppenBenutzer extends BaseList<BenutzerListeEintrag> {

	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Lehrerauswahl bei den Betriebsdaten
	 *
	 * @returns {Promise<void>}
	 */
	 public async update_list(gruppen_id: number): Promise<void> {
		await super._update_list(() => App.api.getBenutzerMitGruppenID(App.schema, gruppen_id));
	}

}
