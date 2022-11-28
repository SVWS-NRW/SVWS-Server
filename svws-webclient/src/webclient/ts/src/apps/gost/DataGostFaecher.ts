import { App } from "../BaseApp";

import { List, GostFach, Fachgruppe, ZulaessigesFach, GostJahrgang, GostFaecherManager, Jahrgaenge } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { reactive } from "vue";

class DataGostFaecherReactiveState {
	/**
	 * Die Fächer der gymnasialen Oberstufe des aktuell ausgewählten Jahrgangs,
	 * aber ohne Vertiefungs- und Projektkursfächer
	 */
	public faecherOhnePJKundVTF: Array<GostFach> = [];
}

export class DataGostFaecher extends BaseData<List<GostFach>, GostJahrgang, GostFaecherManager> {

	protected _data = reactive(new DataGostFaecherReactiveState());

	get faecherOhnePJKundVTF(): Array<GostFach> {
		return this._data.faecherOhnePJKundVTF;
	}

	protected on_update(daten: Partial<GostFach>, id?: number): void {
		if (!this._daten) 
			return;
		if (!this.selected_list_item || !id)
			return;
		let fach: GostFach | undefined = undefined;
		for (const f of this._daten) {
			if (!this.istVertiefungsOderProjektkursfach(f))
				this._data.faecherOhnePJKundVTF.push(f);
			if (f.id === id) 
				fach = f;
		}
		// const fach = this._daten.find(f => f.id === id);
		if (!fach)
			return;
		if (daten.projektKursLeitfach1ID) {
			// Aktualisiere bei den Projektkursen auch das Kürzel für die Ansicht!
			if (!daten.projektKursLeitfach1ID) {
				fach["projektKursLeitfach1Kuerzel"] = "";
			} else {
				const leitfach = this._data.faecherOhnePJKundVTF.find(f => daten.id == f.id);
				if (!leitfach) {
					// TODO error
				} else {
					fach["projektKursLeitfach1Kuerzel"] = leitfach.kuerzel;
				}
			}
		}
		if (daten.projektKursLeitfach2ID) {
			// Aktualisiere bei den Projektkursen auch das Kürzel für die Ansicht!
			if (!daten.projektKursLeitfach2ID) {
				fach["projektKursLeitfach2Kuerzel"] = "";
			} else {
				const leitfach = this._data.faecherOhnePJKundVTF.find(f => daten.id == f.id);
				if (!leitfach) {
					// TODO error
				} else {
					fach["projektKursLeitfach2Kuerzel"] = leitfach.kuerzel;
				}
			}
		}
	}

	/**
	 * Prüft, ob es sich bei dem übergebenen Fach um ein Vertiefungskurs- oder
	 * Projektkursfach handelt oder nicht
	 *
	 * @param {GostFach} fach Das zu prüfende Fach
	 * @returns {boolean} True, falls es sich im ein Vertiefungskurs- oder
	 *   Projektkursfach handelt, ansonsten false //--helfer TODO
	 */
	public istVertiefungsOderProjektkursfach(fach: GostFach): boolean {
		if (!fach.kuerzel) 
			return false;
		const fg = ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe();
		return fg == Fachgruppe.FG_VX || fg == Fachgruppe.FG_PX;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<GostFach[]>} Die Daten als Promise
	 */
	public async on_select(): Promise<List<GostFach> | undefined> {
		const getter = (eintrag: GostJahrgang | undefined) => {
			const abiturjahr = eintrag?.abiturjahr || -1;
			return App.api.getGostAbiturjahrgangFaecher(App.schema, abiturjahr.valueOf());
		};
		const res = await super._select(getter);
		const daten = this._daten;
		this._data.faecherOhnePJKundVTF = [];
		if (daten) {
			for (const f of daten) {
				if (!this.istVertiefungsOderProjektkursfach(f))
					this._data.faecherOhnePJKundVTF.push(f);
			}
		}
		if (res) {
			this.manager = new GostFaecherManager(res);
		}
		return res;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<GostFach>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<GostFach>, fach: GostFach, abiturjahr?: number): Promise<boolean> {
		const daten = this._daten;
		if ((!daten) || (abiturjahr === undefined))
			return false;
		return this._patchElement(data, () => App.api.patchGostAbiturjahrgangFach(data, App.schema, abiturjahr, fach.id), daten.indexOf(fach));
	}

	/**
	 * Gibt den Farbcode für das Fach zurück
	 *
	 * @param {GostFach} row
	 * @returns {string}
	 */
	public getBgColor(row: GostFach): string {
		return ZulaessigesFach.getByKuerzelASD(row.kuerzel).getHMTLFarbeRGB().valueOf();
	}

	public ist_PJK(fach: GostFach): boolean {
		if (!fach.kuerzel) return false;
		const fg = ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe();
		return fg === Fachgruppe.FG_PX;
	}

	hatLeitfach1(fach: GostFach): boolean {
		const f = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
		return (
			f.getFachgruppe() == Fachgruppe.FG_VX ||
			f.getFachgruppe() == Fachgruppe.FG_PX
		);
	}
	/**
	 * Gibt an, ob die Fachwahl in der EF1 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getEF1Moeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return !(
			fach.getFachgruppe() == Fachgruppe.FG_ME ||
			fach.getFachgruppe() == Fachgruppe.FG_PX
		);
	}
	/**
	 * Gibt an, ob die Fachwahl in der EF2 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getEF2Moeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return !(
			fach.getFachgruppe() == Fachgruppe.FG_ME ||
			fach.getFachgruppe() == Fachgruppe.FG_PX
		);
	}
	/**
	 * Gibt an, ob die Fachwahl in der Q11 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ11Moeglich(row: GostFach): boolean {
		void row;
		// var fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return true;
	}
	/**
	 * Gibt an, ob die Fachwahl in der Q12 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ12Moeglich(row: GostFach): boolean {
		void row;
		// var fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return true;
	}
	/**
	 * Gibt an, ob die Fachwahl in der Q12 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ21Moeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return !(fach.getFachgruppe() == Fachgruppe.FG_ME);
	}
	/**
	 * Gibt an, ob die Fachwahl in der Q22 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ22Moeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return !(fach.getFachgruppe() == Fachgruppe.FG_ME);
	}
	/**
	 * Gibt an, ob die Fachwahl als Grundkurs möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getAbiGKMoeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return !(
			fach.getFachgruppe() == Fachgruppe.FG_ME ||
			fach.getFachgruppe() == Fachgruppe.FG_VX ||
			fach.getFachgruppe() == Fachgruppe.FG_PX
		);
	}
	/**
	 * Gibt an, ob die Fachwahl als LK möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getAbiLKMoeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		return !(
			fach.getFachgruppe() == Fachgruppe.FG_ME ||
			fach.getFachgruppe() == Fachgruppe.FG_VX ||
			fach.getFachgruppe() == Fachgruppe.FG_PX ||
			fach.getJahrgangAb() == Jahrgaenge.JG_EF ||
			(row.biliSprache != null && row.biliSprache != "D")
		);
	}
}
