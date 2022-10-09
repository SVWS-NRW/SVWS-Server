import { BenutzergruppeListeEintrag} from "@svws-nrw/svws-core-ts";
import { App } from "../../../BaseApp";
import { BaseList } from "../../../BaseList";
import { ListBenutzergruppenBenutzer } from "./ListBenutzergruppenBenutzer";

export class ListBenutzergruppe extends BaseList<BenutzergruppeListeEintrag, undefined> {
	protected _filter = undefined;
	protected _ready = true;

	public lehrer : ListBenutzergruppenBenutzer = new ListBenutzergruppenBenutzer();

	/**
	 * Aktualisiert die Liste für die Auswahl der Benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() => App.api.getBenutzergruppenliste(App.schema));
	}
}