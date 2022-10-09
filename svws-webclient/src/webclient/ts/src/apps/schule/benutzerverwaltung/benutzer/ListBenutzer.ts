import { BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";
import { App } from "../../../BaseApp";
import { BaseList } from "../../../BaseList";

export class ListBenutzer extends BaseList<BenutzerListeEintrag, undefined> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		const t: BenutzerListeEintrag = new BenutzerListeEintrag();
		//console.log(t.ane)
		await super._update_list(() => App.api.getBenutzerliste(App.schema));
	}
}