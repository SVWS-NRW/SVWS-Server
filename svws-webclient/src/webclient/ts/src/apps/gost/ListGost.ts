import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { api } from "~/router/Api";
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
		await super._update_list(async () => api.server.getGostAbiturjahrgaenge(api.schema));
		if (this.ausgewaehlt === undefined)
		 	this.ausgewaehlt = this.liste[0];
	}

}
