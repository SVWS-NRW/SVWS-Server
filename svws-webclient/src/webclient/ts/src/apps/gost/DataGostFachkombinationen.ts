import { App } from "../BaseApp";

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
			return App.api.getGostAbiturjahrgangFachkombinationen(App.schema, abiturjahr.valueOf());
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
		return this._patchElement(data, () => App.api.patchGostFachkombination(data, App.schema, fachkombi.id), daten.indexOf(fachkombi));
	}


	/**
	 * Fügt eine neue Regel für eine Fachkombination von dem angegebenen Typ hinzu
	 * 
	 * @param {GostLaufbahnplanungFachkombinationTyp} typ
	 * 
	 * @returns {Promise<GostJahrgangFachkombination | undefined>} Ein Kursobjekt bei Erfolg
	 */
	 public async add(typ: GostLaufbahnplanungFachkombinationTyp): Promise<GostJahrgangFachkombination | undefined> {
		const abijahr = App.apps.gost.auswahl.ausgewaehlt?.abiturjahr?.valueOf();
		if (abijahr === undefined)
			return undefined;
		const result = await App.api.addGostAbiturjahrgangFachkombination(App.schema, abijahr, typ.getValue());
		if (result !== undefined)
			this._daten?.add(result);
		return result;
	}


	/**
	 * Entfernt die Regel für eine Fachkombination mit der angegebenen ID.
	 * 
	 * @param {number} id
	 * 
	 * @returns {Promise<GostJahrgangFachkombination | undefined>} Ein Kursobjekt bei Erfolg
	 */
	 public async delete(id: number): Promise<GostJahrgangFachkombination | undefined> {
		const result = await App.api.deleteGostFachkombination(App.schema, id);
		if ((result !== undefined) && (this._daten !== undefined))
			for (let i : number = this._daten.size() - 1; i >= 0; i--)
				if (this._daten.get(i).id === id)
					this._daten.remove(i);
		return result;
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
