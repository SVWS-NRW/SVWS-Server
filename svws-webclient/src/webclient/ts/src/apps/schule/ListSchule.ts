import { BaseList } from "../BaseList";

export class ListSchule extends BaseList<undefined> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		return void undefined;
	}
}
