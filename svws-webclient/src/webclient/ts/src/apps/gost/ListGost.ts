import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListGost extends BaseList<GostJahrgang> {

	protected _filter = undefined;

	public constructor() {
		super();
	}

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(async () => App.api.getGostAbiturjahrgaenge(App.schema));
		if (this.ausgewaehlt === undefined) 
		 	this.ausgewaehlt = this.liste[0];
	}

}
