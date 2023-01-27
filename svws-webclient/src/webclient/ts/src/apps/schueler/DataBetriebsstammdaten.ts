import { App } from "../BaseApp";

import {  BetriebStammdaten, SchuelerBetriebsdaten} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataBetriebsstammdaten extends BaseData<BetriebStammdaten, SchuelerBetriebsdaten> {

	/** Erstellt ein neues Objekt dieser Klasse. */
	public constructor() {
		super();
	}

	protected on_update(
		daten: Partial<BetriebStammdaten>,
		id?: number
	): void {
		console.log("Betrieg_ID");
		console.log(this._daten?.id);
		return void daten;
	}

	public get idfield(): string {
		return "id";
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @param {number} id Die ID des ausgewählten Listeneintrags
	 * @returns {Promise<BetriebStammdaten>} Die Daten als Promise
	 */
	 public async on_select(): Promise<BetriebStammdaten | undefined> {
		return super._select((eintrag: SchuelerBetriebsdaten) =>
			App.api.getBetriebStammdaten(App.schema, eintrag.betrieb_id ?? -1)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<BetriebStammdaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(
		data: Partial<BetriebStammdaten>,
	): Promise<boolean> {
		const daten = this._daten;
		if (!daten)
			return false;
		return this._patch(data, () => App.api.patchBetriebStammdaten(data, App.schema, Number(daten.id)));
	}
}
