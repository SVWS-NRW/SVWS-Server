import { App } from "../BaseApp";

import { List, GostFach, ZulaessigesFach, GostJahrgang, GostJahrgangFachkombinationen } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataGostFachkombinationen extends BaseData<List<GostJahrgangFachkombinationen>, GostJahrgang, unknown> {

	protected on_update(daten: Partial<GostJahrgangFachkombinationen>, id?: number): void {
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<GostJahrgangFachkombinationen[]> | undefined} Die Daten als Promise
	 */
	public async on_select(): Promise<List<GostJahrgangFachkombinationen> | undefined> {
		const getter = (eintrag: GostJahrgang | undefined) => {
			const abiturjahr = eintrag?.abiturjahr;
			return abiturjahr
				? App.api.getGostAbiturjahrgangFachkombinationen(App.schema, abiturjahr.valueOf())
				: App.api.getGostFachkombinationen(App.schema);
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
	 * @param {Partial<GostJahrgangFachkombinationen>} data   Die Daten, die aktualisiert werden sollen
	 * 
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<GostJahrgangFachkombinationen>, fachkombi: GostJahrgangFachkombinationen, abiturjahr?: number): Promise<boolean> {
		// const daten = this._daten;
		// if (!daten) return false;
		// return this._patchElement(data,
		// 	abiturjahr
		// 		? () => App.api.patchGostAbiturjahrgangFachkombinationen(data, App.schema, abiturjahr, fachkombi.id)
		// 		: () => App.api.patchGostFachkombinationen(data, App.schema, fachkombi.id),
		// 	daten.indexOf(fachkombi)
		// );
		return false; // TODO...
	}

	/**
	 * Gibt den Farbcode für das Fach zurück
	 *
	 * @param {GostFach} fach
	 * @returns {string}
	 */
	public getBgColor(fach: GostFach): string {
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getHMTLFarbeRGB().valueOf();
	}

}
