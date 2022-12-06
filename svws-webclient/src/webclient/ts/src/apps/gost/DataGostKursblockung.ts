import { App } from "../BaseApp";

import {
	GostBlockungKurs,
	GostBlockungListeneintrag,
	GostBlockungRegel,
	GostBlockungSchiene,
	GostBlockungsdaten,
	GostBlockungsdatenManager,
	GostBlockungsergebnisListeneintrag,
	GostBlockungsergebnisManager,
	List,
	Vector,
} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { ListKursblockungsergebnisse } from "./ListKursblockungsergebnisse";
import { ShallowReactive, shallowReactive } from "vue";

interface DataGostKursblockungManagerContainer {
	manager: {
		daten: GostBlockungsdatenManager|undefined;
		ergebnis: GostBlockungsergebnisManager|undefined;
	}
}

export class DataGostKursblockung extends BaseData<
	GostBlockungsdaten,
	GostBlockungListeneintrag,
	unknown
> {
	protected listKursblockungsergebnisse: ListKursblockungsergebnisse;

	public manager_container : ShallowReactive<DataGostKursblockungManagerContainer> = shallowReactive({manager: {daten: undefined, ergebnis: undefined}});

	public constructor(listKursblockungsergebnisse: ListKursblockungsergebnisse) {
		super();
		this.listKursblockungsergebnisse = listKursblockungsergebnisse;
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
			App.api.getGostBlockung(App.schema, eintrag.id));
		if (blockungsdaten && App.apps.gost.dataFaecher.manager){
			App.apps.gost.listAbiturjahrgangSchueler.reset_filter();
			this.ergebnismanager = undefined;
			this.datenmanager = new GostBlockungsdatenManager(blockungsdaten, App.apps.gost.dataFaecher.manager);
			this.commit();
			await this.listKursblockungsergebnisse.update_list(blockungsdaten.id);
			if (this.listKursblockungsergebnisse.liste.length) {
				let ergebnis = this.listKursblockungsergebnisse.liste[0];
				if (blockungsdaten.istAktiv)
					for (const e of this.listKursblockungsergebnisse.liste)
						if (e.istVorlage) {
							ergebnis = e;
							break;
						}
				this.listKursblockungsergebnisse.ausgewaehlt = ergebnis;
			}
		}
		this.pending = false;
		return blockungsdaten;
	}

	public async ergebnisse(): Promise<List<GostBlockungsergebnisListeneintrag>> {
		return this.datenmanager?.getErgebnisseSortiertNachBewertung() || new Vector<GostBlockungsergebnisListeneintrag>()
	}

	/**
	 * Setzt die Daten auf die Default-Werte zurück.
	 *
	 * @returns {Promise<undefined>} Die Daten als Promise
	 */
	public async unselect(): Promise<undefined> {
		this._daten = undefined;
		await this.listKursblockungsergebnisse.update_list(undefined);
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
		return this._patch(data, () => App.api.patchGostBlockung(data, App.schema, id));
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
		const kurs = await App.api.addGostBlockungKurs(App.schema, this.daten.id, fach_id, kursart_id);
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
		const kurs = await App.api.deleteGostBlockungKurs(App.schema, this.daten.id, fach_id, kursart_id);
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
	public async patch_kurs(data: GostBlockungKurs): Promise<void> {
		await App.api.patchGostBlockungKurs(data, App.schema, data.id);
	}

	/**Ergänzt eine Regel in der Blockung
	 * @param {number} regel_typ
	 * @returns {Promise<GostBlockungRegel|undefined>} Ein Kursobjekt bei Erfolg
	 */
	public async add_blockung_regel(regel_typ: number): Promise<GostBlockungRegel | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const regel = await App.api.addGostBlockungRegel(App.schema, this.daten.id, regel_typ);
		if (!regel) {
			this.pending = false;
			return
		}
		this.datenmanager?.addRegel(regel)
		this.ergebnismanager?.setAddRegelByID(regel.id)
		this.commit()
		this.pending = false;
		return regel
	}

	/**Löscht eine Regel in der Blockung mit der jeweiligen id
	 * @param {number} regel_id
	 * @returns {Promise<GostBlockungRegel|undefined>} Ein Kursobjekt bei Erfolg
	 */
	public async del_blockung_regel(regel_id: number): Promise<GostBlockungRegel | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const regel = await App.api.deleteGostBlockungRegelByID(App.schema, regel_id);
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

	/**passt die Regel einer Blockung an */
	public async patch_blockung_regel(data: GostBlockungRegel): Promise<GostBlockungRegel | undefined> {
		await App.api.patchGostBlockungRegel(data, App.schema, data.id);
		const regel = this.datenmanager?.getRegel(data.id)
		if (!regel)
			return
		this.datenmanager?.removeRegel(regel);
		this.ergebnismanager?.setRemoveRegelByID(regel.id);
		this.datenmanager?.addRegel(data);
		this.ergebnismanager?.setAddRegelByID(data.id)
		this.commit();
		return regel;
	}

	/**Ergänzt eine Schiene in der Blockung für das angegebene
	 * @returns {Promise<GostBlockungSchiene|undefined>} Ein Schienenobjekt bei Erfolg
	 */	
	public async add_blockung_schiene(): Promise<GostBlockungSchiene | undefined> {
		if (!this.daten?.id)
			return;
		this.pending = true;
		const schiene = await App.api.addGostBlockungSchiene(App.schema, this.daten.id);
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
		const s = await App.api.deleteGostBlockungSchieneByID(App.schema, schiene.id);
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
	public async patch_schiene(data: GostBlockungSchiene): Promise<void> {
		await App.api.patchGostBlockungSchiene(data, App.schema, data.id);
		//TODO, der Manager sollte was tun..
	}
}
