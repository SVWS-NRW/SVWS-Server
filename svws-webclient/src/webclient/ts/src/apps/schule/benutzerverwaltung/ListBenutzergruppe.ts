import { App } from "~/apps/BaseApp";
import { BaseList } from "~/apps/BaseList";

import { BenutzergruppeListeEintrag} from "@svws-nrw/svws-core-ts";


export class ListBenutzergruppe extends BaseList<BenutzergruppeListeEintrag, undefined> {

	protected _filter = undefined;

	/**
	 * Prüfe vor der Auswahl der Benutzergruppe, ob bereits zuvor ein Benutzer ausgewählt wurde.
	 * Ist dies der Fall, so entferne die Auswahl des Benutzers.
	 * 
	 * @param eintrag   die neue Auswahl
	 */
	public async before_select(eintrag: BenutzergruppeListeEintrag | undefined): Promise<void> {
		if ((eintrag !== undefined) && (App.apps.benutzer.auswahl.ausgewaehlt !== undefined))
			App.apps.benutzer.auswahl.ausgewaehlt = undefined;
	}

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getBenutzergruppenliste(App.schema));
	}

}