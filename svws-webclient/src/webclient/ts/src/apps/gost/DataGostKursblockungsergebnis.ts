import { App } from "../BaseApp";

import {
	GostBlockungKurs,
	GostBlockungSchiene,
	GostBlockungsergebnis,
	GostBlockungsergebnisKurs,
	GostBlockungsergebnisListeneintrag,
	GostBlockungsergebnisManager,
	GostFach,
	List,
	Schueler,
	Vector
} from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { Ref, ref } from "vue";

export class DataGostKursblockungsergebnis extends BaseData<
	GostBlockungsergebnis,
	GostBlockungsergebnisListeneintrag,
	GostBlockungsergebnisManager
> {

	public active_kurs: Ref<GostBlockungsergebnisKurs | undefined> = ref(undefined);

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
		if (!this.selected_list_item) return super.unselect();
		const ergebnis: GostBlockungsergebnis | undefined = await super._select(
			(eintrag: GostBlockungsergebnisListeneintrag) =>
				App.api.getGostBlockungsergebnis(App.schema, eintrag.id)
		);
		if (ergebnis === undefined) {
			this.manager = undefined;
			return ergebnis;
		}
		// Hole die Daten für den Manager
		const schueler: Vector<Schueler> = new Vector<Schueler>();
		App.apps.gost?.listAbiturjahrgangSchueler.liste.forEach(s => {
			const entry = new Schueler();
			entry.id = s.id;
			entry.nachname = s.nachname;
			entry.vorname = s.vorname;
			entry.geschlecht = 0;
			schueler.add(entry);
		});
		const faecher: Vector<GostFach> = new Vector<GostFach>();
		if (App.apps.gost?.dataFaecher.daten) faecher.addAll(App.apps.gost.dataFaecher.daten)
		// App.apps.gost?.dataFaecher.daten?.forEach(f => faecher.add(f));
		const schienen: List<GostBlockungSchiene> | undefined =
			App.apps.gost?.dataKursblockung.daten?.schienen;
		const kurse: List<GostBlockungKurs> | undefined =
			App.apps.gost?.dataKursblockung.daten?.kurse;
		if (
			!ergebnis ||
			schueler.size() === 0 ||
			faecher.size() === 0 ||
			!schienen ||
			!kurse
		)
			return undefined;
		this.manager = (!App.apps.gost.dataKursblockung.manager) ? undefined : new GostBlockungsergebnisManager(
			App.apps.gost.dataKursblockung.manager,
			ergebnis,
			schueler
		);
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
		if (!daten || daten.id === null) return false;
		const id = daten.id;
		console.log("patch", data, id);
		// TODO
		//return this._patch(data, () => App.api.patchGostBlockungsergebnis(data as GostBlockungsergebnis, App.schema, id));
		return false;
	}

	public async assignKursSchiene(
		kursid: number,
		schienenid: number
	): Promise<boolean> {
		// TODO implement methods on server side and call api methods here - see assignSchuelerKurs
		this.manager?.assignKursSchiene(kursid, schienenid);
		return true;
	}

	public async assignSchuelerKurs(
		schuelerid: number,
		kursid: number,
		undo: boolean
	): Promise<boolean> {
		const ergebnisid: number | undefined = this._daten?.id;
		if (!ergebnisid) return false;
		if (undo) {
			await App.api.deleteGostBlockungsergebnisKursSchuelerZuordnung(
				App.schema,
				ergebnisid,
				schuelerid,
				kursid
			);
		} else {
			await App.api.createGostBlockungsergebnisKursSchuelerZuordnung(
				App.schema,
				ergebnisid,
				schuelerid,
				kursid
			);
		}
		this.manager?.assignSchuelerKurs(schuelerid, kursid, undo);
		return true;
	}
}
