import { routeLogin } from "~/router/RouteLogin";

import { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungListeneintrag, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdaten,
	GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostBlockungsergebnisManager, List, Vector } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { ShallowReactive, shallowReactive } from "vue";
import { routeGost } from "~/router/apps/RouteGost";

interface DataGostKursblockungManagerContainer {
	manager: {
		daten: GostBlockungsdatenManager|undefined;
		ergebnis: GostBlockungsergebnisManager|undefined;
	}
}

export class DataGostKursblockung extends BaseData<GostBlockungsdaten, GostBlockungListeneintrag, unknown> {

	public manager_container : ShallowReactive<DataGostKursblockungManagerContainer> = shallowReactive({manager: {daten: undefined, ergebnis: undefined}});

	public constructor() {
		super();
	}

	protected on_update(daten: Partial<GostBlockungsdaten>): void {
		return void daten;
	}

	public commit() {
		this.manager_container.manager = {...this.manager_container.manager};
	}

	public get datenmanager() : GostBlockungsdatenManager | undefined {
		return this.manager_container.manager.daten;
	}

	public set datenmanager(man : GostBlockungsdatenManager | undefined) {
		this.manager_container.manager.daten = man;
	}
	public get ergebnismanager() : GostBlockungsergebnisManager | undefined {
		return this.manager_container.manager.ergebnis;
	}

	public set ergebnismanager(man : GostBlockungsergebnisManager | undefined) {
		this.manager_container.manager.ergebnis = man;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<GostBlockungsdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<GostBlockungsdaten | undefined> {
		if (!this.selected_list_item)
			return super.unselect();
		this.pending = true;
		const blockungsdaten = await super._select((eintrag: GostBlockungListeneintrag) =>
			routeLogin.data.api.getGostBlockung(routeLogin.data.schema, eintrag.id));
		if (blockungsdaten && routeGost.data.dataFaecher.manager){
			this.ergebnismanager = undefined;
			this.datenmanager = new GostBlockungsdatenManager(blockungsdaten, routeGost.data.dataFaecher.manager);
			this.commit();
		}
		this.pending = false;
		return blockungsdaten;
	}

	public ergebnisse(): List<GostBlockungsergebnisListeneintrag> {
		return this.datenmanager?.getErgebnisseSortiertNachBewertung() || new Vector<GostBlockungsergebnisListeneintrag>()
	}

	/**
	 * Setzt die Daten auf die Default-Werte zurück.
	 *
	 * @returns {Promise<undefined>} Die Daten als Promise
	 */
	public async unselect(): Promise<undefined> {
		this._daten = undefined;
		this.datenmanager = undefined;
		this.ergebnismanager = undefined;
		this.commit();
		return undefined;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<GostBlockungsdaten>} data Die Daten, die aktualisiert
	 *   werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<GostBlockungsdaten>): Promise<boolean> {
		const daten = this._daten;
		if (!daten || daten.id === null) return false;
		const id = daten.id;
		console.log("patch", data, id);
		return this._patch(data, () => routeLogin.data.api.patchGostBlockung(data, routeLogin.data.schema, id));
	}

	/**Ergänzt einen Kurs in der Blockung für das angegebene fach_id
	 * @param {number} fach_id
	 * @param {number} kursart_id
	 * @returns {Promise<GostBlockungKurs|undefined>} Ein Kursobjekt bei Erfolg
	 */
	public async add_blockung_kurse(fach_id: number, kursart_id: number): Promise<GostBlockungKurs | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const kurs = await routeLogin.data.api.addGostBlockungKurs(routeLogin.data.schema, this.daten.id, fach_id, kursart_id);
		if (!kurs) {
			this.pending = false;
			return
		}
		this.datenmanager?.addKurs(kurs)
		this.ergebnismanager?.setAddKursByID(kurs.id)
		this.commit()
		this.pending = false;
		return kurs
	}

	/**Löscht einen Kurs in der Blockung für das angegebene fach_id
	 * @param {number} fach_id
	 * @param {number} kursart_id
	 * @returns {Promise<GostBlockungKurs|undefined>} Ein Kursobjekt bei Erfolg
	 */
	public async del_blockung_kurse(fach_id: number, kursart_id: number): Promise<GostBlockungKurs | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const kurs = await routeLogin.data.api.deleteGostBlockungKurs(routeLogin.data.schema, this.daten.id, fach_id, kursart_id);
		if (!kurs) {
			this.pending = false;
			return
		}
		this.datenmanager?.removeKurs(kurs)
		this.ergebnismanager?.setRemoveKursByID(kurs.id)
		this.commit()
		this.pending = false;
		return kurs
	}

	/** passt einen Kurs an */
	public async patch_kurs(kurs_id: number, data: Partial<GostBlockungKurs>): Promise<void> {
		await routeLogin.data.api.patchGostBlockungKurs(data, routeLogin.data.schema, kurs_id);
		if (data.suffix !== undefined)
			this.datenmanager?.setSuffixOfKurs(kurs_id, data.suffix);
	}

	/**Ergänzt eine Regel in der Blockung
	 * @param {GostBlockungRegel} regel die neue Regel
	 * @returns {Promise<GostBlockungRegel|undefined>} Ein Kursobjekt bei Erfolg
	 */
	public async add_blockung_regel(r: GostBlockungRegel): Promise<GostBlockungRegel | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const regel = await routeLogin.data.api.addGostBlockungRegel(r.parameter, routeLogin.data.schema, this.daten.id, r.typ);
		if (!regel) {
			this.pending = false;
			return;
		}
		this.datenmanager?.addRegel(regel);
		this.ergebnismanager?.setAddRegelByID(regel.id);
		this.commit();
		this.pending = false;
		return regel;
	}

	/**Löscht eine Regel in der Blockung mit der jeweiligen id
	 * @param {number} regel_id
	 * @returns {Promise<GostBlockungRegel|undefined>} Ein Kursobjekt bei Erfolg
	 */
	public async del_blockung_regel(regel_id: number): Promise<GostBlockungRegel | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const regel = await routeLogin.data.api.deleteGostBlockungRegelByID(routeLogin.data.schema, regel_id);
		if (!regel) {
			this.pending = false;
			return
		}
		this.datenmanager?.removeRegel(regel);
		this.ergebnismanager?.setRemoveRegelByID(regel.id);
		this.commit();
		this.pending = false;
		return regel
	}

	/**Patcht eine Regel in der Blockung mit der jeweiligen id
	 * @param {number} regel_id
	 */
	public async patch_blockung_regel(data: GostBlockungRegel) {
		this.pending = true;
		await routeLogin.data.api.patchGostBlockungRegel(data, routeLogin.data.schema, data.id);
		this.pending = false;
	}

	/**Ergänzt eine Schiene in der Blockung für das angegebene
	 * @returns {Promise<GostBlockungSchiene|undefined>} Ein Schienenobjekt bei Erfolg
	 */
	public async add_blockung_schiene(): Promise<GostBlockungSchiene | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const schiene = await routeLogin.data.api.addGostBlockungSchiene(routeLogin.data.schema, this.daten.id);
		if (!schiene) {
			this.pending = false;
			return
		}
		this.datenmanager?.addSchiene(schiene);
		this.ergebnismanager?.setAddSchieneByID(schiene.id)
		this.commit();
		this.pending = false;
		return schiene
	}

	/** Entfernt eine Schiene aus der Blockung */
	public async del_blockung_schiene(schiene: GostBlockungSchiene): Promise<GostBlockungSchiene|undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const s = await routeLogin.data.api.deleteGostBlockungSchieneByID(routeLogin.data.schema, schiene.id);
		if (!s) {
			this.pending = false;
			return
		}
		this.datenmanager?.removeSchieneByID(s.id);
		this.ergebnismanager?.setRemoveSchieneByID(s.id)
		this.commit();
		this.pending = false;
		return s;
	}

	/** passt eine Schiene an */
	public async patch_schiene(data: Partial<GostBlockungSchiene>, id : number): Promise<void> {
		await routeLogin.data.api.patchGostBlockungSchiene(data, routeLogin.data.schema, id);
		//TODO, der Manager sollte was tun..
	}

	/**Ergänzt einen Lehrer in der Blockung zum angegebenen Kurs
	 * @returns {Promise<GostBlockungKursLehrer|undefined>} Ein Schienenobjekt bei Erfolg
	 */
	public async add_blockung_lehrer(kursid: number, lehrerid: number): Promise<GostBlockungKursLehrer | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const lehrer = await routeLogin.data.api.addGostBlockungKurslehrer(routeLogin.data.schema, kursid, lehrerid);
		if (!lehrer) {
			this.pending = false;
			return
		}
		// this.ergebnismanager?.  addSchiene(schiene);
		// this.ergebnismanager?.setAddSchieneByID(schiene.id)
		// this.commit();
		this.pending = false;
		return lehrer;
	}

	/** Entfernt eine Lehrerin aus der Kurszuordnung */
	public async del_blockung_lehrer(kursid: number, lehrerid: number): Promise<void> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		await routeLogin.data.api.deleteGostBlockungKurslehrer(routeLogin.data.schema, kursid, lehrerid)
		this.pending = false;
	}

}
