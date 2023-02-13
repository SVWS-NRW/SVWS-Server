import { routeLogin } from "~/router/RouteLogin";

import {
	SchuelerLeistungsdaten,
	SchuelerLernabschnittListeEintrag,
	SchuelerLernabschnittsdaten
} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataSchuelerAbschnittsdaten extends BaseData<
	SchuelerLernabschnittsdaten,
	SchuelerLernabschnittListeEintrag
> {
	protected on_update(daten: Partial<SchuelerLernabschnittsdaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<SchuelerLernabschnittsdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<SchuelerLernabschnittsdaten | undefined> {
		if (!this.selected_list_item?.schuelerID) return super.unselect();
		return super._select((eintrag: SchuelerLernabschnittListeEintrag) =>
			routeLogin.data.api.getSchuelerLernabschnittsdaten(
				routeLogin.data.schema,
				eintrag.schuelerID,
				eintrag.schuljahresabschnitt
			)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<SchuelerLernabschnittsdaten>} data Die Daten, die
	 *   aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<SchuelerLernabschnittsdaten>): Promise<boolean> {
		const daten = this._daten;
		if (!daten)
			return false;
		// TODO richtige Patch-Methode suchen
		// return this._patch(data, () => routeLogin.data.api.patchSchuelerLernabschnittsdaten(data as SchuelerLernabschnittsdaten, routeLogin.data.schema, daten.id));
		return true;
	}


	public async patchLeistung(data: Partial<SchuelerLeistungsdaten>, idLeistung: number): Promise<boolean> {
		const daten = this._daten;
		if (!daten)
			return false;
		// TODO Patch-Methode aufrufen: await routeLogin.data.api.patchSchuelerLeistungsdaten(data, routeLogin.data.schema, idLeistung)
		// TODO Daten anpassen
		return true;
	}

}
