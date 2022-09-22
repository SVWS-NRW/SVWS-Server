import { App } from "../BaseApp";

import { SchuelerListeEintrag, SchuelerStammdaten } from "@svws-nrw/svws-api-ts";
import { BaseData } from "../BaseData";

export class DataSchuelerStammdaten extends BaseData<SchuelerStammdaten, SchuelerListeEintrag> {
	protected on_update(daten: Partial<SchuelerStammdaten>): void {
		void daten
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<SchuelerStammdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<SchuelerStammdaten | undefined> {
		return super._select(
			(eintrag: SchuelerListeEintrag) => App.api.getSchuelerStammdaten(App.schema, eintrag.id)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 */
	public async patch(data: Partial<SchuelerStammdaten>): Promise<boolean> {
		return !!data
	}
}
