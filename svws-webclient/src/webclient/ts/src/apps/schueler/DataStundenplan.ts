import { App } from "../BaseApp";

import { StundenplanListeEintrag, SchuelerStundenplan } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { routeSchueler } from "~/router/apps/RouteSchueler";

export class DataStundenplan extends BaseData<
	SchuelerStundenplan,
	StundenplanListeEintrag
> {
	protected on_update(daten: Partial<SchuelerStundenplan>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<StundenplanZeitraster>} Die Daten als Promise
	 */
	public async on_select(): Promise<SchuelerStundenplan | undefined> {
		const schueler = routeSchueler.liste.ausgewaehlt;
		if (!schueler)
			return;
		return super._select((eintrag: StundenplanListeEintrag) => App.api.getSchuelerStundenplan(App.schema, eintrag.id, schueler.id));
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<SchuelerStundenplan>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<SchuelerStundenplan>): Promise<boolean> {
		void data;
		throw new Error("Unsupported Patch");
	}
}
