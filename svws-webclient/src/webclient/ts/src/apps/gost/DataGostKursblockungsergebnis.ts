import { App } from "../BaseApp";

import { GostBlockungsergebnis, GostBlockungsergebnisKurs, GostBlockungsergebnisListeneintrag, GostBlockungsergebnisManager, } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { Ref, ref } from "vue";
import { DataGostKursblockung } from "./DataGostKursblockung";

export class DataGostKursblockungsergebnis extends BaseData<GostBlockungsergebnis, GostBlockungsergebnisListeneintrag, GostBlockungsergebnisManager> {

	public active_kurs: Ref<GostBlockungsergebnisKurs | undefined> = ref(undefined);

	private _dataKursblockung: DataGostKursblockung | undefined;

	public get dataKursblockung(): DataGostKursblockung {
		if (this._dataKursblockung === undefined)
			throw new Error("Zugriff auf die Daten der Kursblockung vor der Zuweisung.");
		return this._dataKursblockung;
	}

	public set dataKursblockung(value: DataGostKursblockung) {
		this._dataKursblockung = value;
	}

	protected on_update(daten: Partial<GostBlockungsergebnis>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<GostBlockungsergebnis> | undefined} Die Daten als Promise
	 */
	public async on_select(): Promise<GostBlockungsergebnis | undefined> {
		if (!this.selected_list_item)
			return super.unselect();
		this.pending = true;
		const ergebnis: GostBlockungsergebnis | undefined = await super._select(
			(eintrag: GostBlockungsergebnisListeneintrag) => App.api.getGostBlockungsergebnis(App.schema, eintrag.id));
		if (ergebnis === undefined) {
			this.pending = false;
			return ergebnis;
		}
		this.dataKursblockung.ergebnismanager = (this.dataKursblockung.datenmanager !== undefined)
			? new GostBlockungsergebnisManager(this.dataKursblockung.datenmanager, ergebnis)
			: undefined;
		this.dataKursblockung.commit();
		this.pending = false;
		return ergebnis;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<GostBlockungsergebnis>} data Die Daten, die aktualisiert
	 *   werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<GostBlockungsergebnis>): Promise<boolean> {
		const daten = this._daten;
		if (!daten || daten.id === null)
			return false;
		const id = daten.id;
		console.log("patch", data, id);
		// TODO
		//return this._patch(data, () => App.api.patchGostBlockungsergebnis(data as GostBlockungsergebnis, App.schema, id));
		return false;
	}

	public async assignKursSchiene(kursid: number, schienenid: number, schienenidneu: number): Promise<boolean> {
		if (!this._daten?.id)
			return false;
		this.pending = true;
		await App.api.updateGostBlockungsergebnisKursSchieneZuordnung(App.schema, this._daten.id, schienenid, kursid, schienenidneu);
		this.dataKursblockung.ergebnismanager?.setKursSchiene(kursid, schienenid, false);
		this.dataKursblockung.ergebnismanager?.setKursSchiene(kursid, schienenidneu, true);
		this.dataKursblockung.commit();
		this.pending = false;
		return true;
	}

	public async removeSchuelerKurs(schuelerid: number, kursid: number): Promise<boolean> {
		const ergebnisid = this._daten?.id;
		if (ergebnisid === undefined || this.dataKursblockung.ergebnismanager === undefined)
			return false;
		this.pending = true;
		await App.api.deleteGostBlockungsergebnisKursSchuelerZuordnung(App.schema, ergebnisid, schuelerid, kursid);
		this.dataKursblockung.ergebnismanager.setSchuelerKurs(schuelerid, kursid, false);
		this.dataKursblockung.commit();
		this.pending = false;
		return true;
	}

	public async assignSchuelerKurs(schuelerid: number, kursid_neu: number, kursid_alt?: number): Promise<boolean> {
		const ergebnisid = this._daten?.id;
		if (!ergebnisid)
			return false;
		this.pending = true;
		if (kursid_alt) {
			await App.api.deleteGostBlockungsergebnisKursSchuelerZuordnung(App.schema, ergebnisid, schuelerid, kursid_alt);
			await App.api.createGostBlockungsergebnisKursSchuelerZuordnung(App.schema, ergebnisid, schuelerid, kursid_neu);
			this.dataKursblockung.ergebnismanager?.setSchuelerKurs(schuelerid, kursid_alt, false);
		} else {
			await App.api.createGostBlockungsergebnisKursSchuelerZuordnung(App.schema, ergebnisid, schuelerid, kursid_neu);
		}
		this.dataKursblockung.ergebnismanager?.setSchuelerKurs(schuelerid, kursid_neu, true);
		this.dataKursblockung.commit();
		this.pending = false;
		return true;
	}

	public async multiAssignSchuelerKurs(schuelerid: number) {
		const ergebnismanager = this.dataKursblockung.ergebnismanager;
		const ergebnisid = this._daten?.id;
		if (ergebnismanager === undefined || schuelerid === undefined || ergebnisid === undefined)
			return;
		const zuordnungen = ergebnismanager.getOfSchuelerNeuzuordnung(schuelerid);
		this.pending = true;
		for (const z of zuordnungen.fachwahlenZuKurs) {
			const kursV = ergebnismanager.getOfSchuelerOfFachZugeordneterKurs(schuelerid, z.fachID);
			const kursN = z.kursID < 0 ? null : ergebnismanager.getKursE(z.kursID);
			if (kursV !== kursN) {
				if (kursV !== null) {
					await App.api.deleteGostBlockungsergebnisKursSchuelerZuordnung(App.schema, ergebnisid, schuelerid, kursV.id);
					ergebnismanager.setSchuelerKurs(schuelerid, kursV.id, false);
				}
				if (kursN !== null) {
					await App.api.createGostBlockungsergebnisKursSchuelerZuordnung(App.schema, ergebnisid, schuelerid, kursN.id);
					ergebnismanager.setSchuelerKurs(schuelerid, kursN.id, true);
				}
			}
		}
		this.dataKursblockung.commit();
		this.pending = false;
	}

	public async activate_blockungsergebnis(): Promise<boolean> {
		if (!this.selected_list_item)
			return false;
		await App.api.activateGostBlockungsergebnis(App.schema, this.selected_list_item.id);
		return true;
	}

}
