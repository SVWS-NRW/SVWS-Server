import { App } from "~/apps/BaseApp";
import { BaseList } from "~/apps/BaseList";

import { BenutzerListeEintrag} from "@svws-nrw/svws-core-ts";

export class ListBenutzer extends BaseList<BenutzerListeEintrag, undefined> {

	protected _filter = undefined;

	/**
	 * Prüfe vor der Auswahl des Benutzers, ob bereits zuvor eine Benutzergruppe ausgewählt wurde.
	 * Ist dies der Fall, so entferne die Auswahl der Benutzergruppe.
	 * 
	 * @param eintrag   die neue Auswahl
	 */
	 public async before_select(eintrag: BenutzerListeEintrag | undefined): Promise<void> {
		if ((eintrag !== undefined) && (App.apps.benutzergruppe.auswahl.ausgewaehlt !== undefined))
			App.apps.benutzergruppe.auswahl.ausgewaehlt = undefined;
	}

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getBenutzerliste(App.schema));
	}

}