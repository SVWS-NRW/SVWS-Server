import { App } from "../BaseApp";

import { List, GostJahrgang, GostStatistikFachwahl, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";

export class DataGostSchuelerFachwahlen extends BaseData<List<GostStatistikFachwahl>, GostJahrgang> {

	protected on_update(daten: Partial<GostStatistikFachwahl>): void {
		return void daten;
	}

	/**
	 * Gibt den Farbcode für das Fach zurück
	 *
	 * @param {GostStatistikFachwahl} f
	 * @returns {string}
	 */
	public getBgColor(f: GostStatistikFachwahl): string {
		return ZulaessigesFach.getByKuerzelASD(f.kuerzel).getHMTLFarbeRGB().valueOf();
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<List<GostStatistikFachwahl>>} Die Daten als Promise
	 */
	public async on_select(): Promise<List<GostStatistikFachwahl> | undefined> {
		if ((!this.selected_list_item?.abiturjahr) || (this.selected_list_item?.abiturjahr == -1))
			return super.unselect();
		return super._select((eintrag: GostJahrgang) =>
			App.api.getGostAbiturjahrgangFachwahlstatistik(App.schema, eintrag.abiturjahr?.valueOf() || -1)
		);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<GostStatistikFachwahl>} data Die Daten, die aktualisiert
	 *   werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<GostStatistikFachwahl>): Promise<boolean> {
		return !!data;
	}

}
