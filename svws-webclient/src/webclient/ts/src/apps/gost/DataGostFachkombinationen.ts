import { routeLogin } from "~/router/RouteLogin";

import { List, GostFach, ZulaessigesFach, GostJahrgang, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataGostFachkombinationen extends BaseData<List<GostJahrgangFachkombination>, GostJahrgang, unknown> {

	protected on_update(daten: Partial<GostJahrgangFachkombination>, id?: number): void {
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<GostJahrgangFachkombination[]> | undefined} Die Daten als Promise
	 */
	public async on_select(): Promise<List<GostJahrgangFachkombination> | undefined> {
		const getter = (eintrag: GostJahrgang | undefined)  => {
			const abiturjahr = eintrag?.abiturjahr || -1;
			return routeLogin.data.api.getGostAbiturjahrgangFachkombinationen(routeLogin.data.schema, abiturjahr);
		};
		const res = await super._select(getter);
		return res;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<GostJahrgangFachkombination>} data   Die Daten, die aktualisiert werden sollen
	 *
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<GostJahrgangFachkombination>, fachkombi: GostJahrgangFachkombination, abiturjahr?: number): Promise<boolean> {
		const daten = this._daten;
		if (!daten)
			return false;
		return this._patchElement(data, () => routeLogin.data.api.patchGostFachkombination(data, routeLogin.data.schema, fachkombi.id), daten.indexOf(fachkombi));
	}


	/**
	 * Gibt den Farbcode für das Fach zurück
	 *
	 * @param {GostFach} fach
	 * @returns {string}
	 */
	public getBgColor(fach: GostFach): string {
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getHMTLFarbeRGB();
	}

}
