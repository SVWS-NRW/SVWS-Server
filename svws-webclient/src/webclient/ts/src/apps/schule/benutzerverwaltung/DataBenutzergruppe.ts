import { App } from "~/apps/BaseApp";
import { BaseData } from "~/apps/BaseData";

import { ListBenutzergruppenBenutzer } from "./ListBenutzergruppenBenutzer";

import { BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerKompetenz } from "@svws-nrw/svws-core-ts";


export class DataBenutzergruppe extends BaseData<BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager> {

	public listBenutzergruppenBenutzer: ListBenutzergruppenBenutzer = new ListBenutzergruppenBenutzer();

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<BenutzergruppeDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<BenutzergruppeDaten | undefined> {
		this.manager = undefined;
		if (!this.selected_list_item) 
			return super.unselect();
		const gruppendaten = await super._select((eintrag: BenutzergruppeListeEintrag) =>
			App.api.getBenutzergruppeDaten(App.schema, eintrag.id)
		);
		if (gruppendaten)
			this.manager = new BenutzergruppenManager(gruppendaten);
		await this.listBenutzergruppenBenutzer.update_list(gruppendaten?.id);
		return gruppendaten;
	}


	/**
	 * Setzt die Daten auf die Default-Werte zurück.
	 *
	 * @returns {Promise<undefined>} Die Daten als Promise
	 */
	 public async unselect(): Promise<undefined> {
		this.manager = undefined;
		this._daten = undefined;
		await this.listBenutzergruppenBenutzer.update_list(undefined);
		return undefined;
	}


	protected on_update(daten: Partial<BenutzergruppeDaten>): void {
		return void daten;
	}


	public async patch(data: Partial<BenutzergruppeDaten>): Promise<boolean> {
		return false;
	}


	/**
	 * Setzt die Bezeichnung der Benutzergruppe
	 * 
	 * @param {string} bezeichnung
	 * 
	 * @returns {Promise<void>}
	 */
	 public async setBezeichnung(bezeichnung: string): Promise<void> {
		if (!this.manager) 
			return;
		await App.api.setBenutzergruppeBezeichnung(bezeichnung, App.schema, this.manager.getID());
		this.manager.setBezeichnung(bezeichnung);
	}


	/**
	 * Setzt, ob die Benutzergruppe eine administrative Gruppe ist oder nicht
	 * 
	 * @param {boolean} istAdmin
	 * 
	 * @returns {Promise<void>}
	 */
	 public async setIstAdmin(istAdmin: boolean): Promise<void> {
		if (!this.manager) 
			return;
		await App.api.setBenutzergruppeAdmin(istAdmin, App.schema, this.manager.getID());
		this.manager.setAdmin(istAdmin);
	}


	/**
	 * Fügt die übergebene Kompetenz zu dieser Benutzergruppe hinzu
	 * 
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	public async addKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
		if (!this.manager)
			return false;
		if (this.manager.hatKompetenz(kompetenz))
			return false;
		await App.api.setBenutzergruppeKompetenz(true, App.schema, this.manager.getID(), kompetenz.daten.id);
		return true;
	}


	/**
	 * Entfernt die übergebene Kompetenz aus dieser Benutzergruppe
	 * 
	 * @param kompetenz   die zu entfernende Kompetenz
	 */
	 public async removeKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
		if (!this.manager)
			return false;
		if (!this.manager.hatKompetenz(kompetenz))
			return false;
		await App.api.setBenutzergruppeKompetenz(false, App.schema, this.manager.getID(), kompetenz.daten.id);
		return true;
	}

}
