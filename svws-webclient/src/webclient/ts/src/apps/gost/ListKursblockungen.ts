import { GostBlockungListeneintrag } from "@svws-nrw/svws-core-ts";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";

export class ListKursblockungen extends BaseList<GostBlockungListeneintrag> {
	protected _filter = undefined;

	/**
	 * Aktualisiert die Liste für die Schülerauswahl und wählt direkt das zuletzt
	 * angelegte Element aus.
	 *
	 * @param {number} abiturjahr Das für die Liste notwendige Abiturjahr
	 * @param {number} halbjahr Die ID des Halbjahres
	 * @returns {Promise<void>}
	 */
	public async update_list(abiturjahr: number, halbjahr: number, neue_blockung?: boolean): Promise<void> {
		// 0 (number) wird bei !halbjahr als true gewertet, da 0 zu boolean konvertiert false ist.
		if (!abiturjahr || halbjahr === undefined) {
			console.error(`Fehler beim Update der ListKursblockungen! abiturjahr: ${abiturjahr}, halbjahr: ${halbjahr}`);
			return;
		}
		const id = this.ausgewaehlt?.id;
		await super._update_list(() => App.api.getGostAbiturjahrgangBlockungsliste(App.schema, abiturjahr, halbjahr));
		let blockung
		if (neue_blockung)
			blockung = this.liste[this.liste.length - 1];
		else
			blockung = this.liste.find(e => e.id === id) || this.liste[this.liste.length - 1];
		if (this.liste.length)
			this.ausgewaehlt = blockung;
		else this.ausgewaehlt = undefined;
	}
}
