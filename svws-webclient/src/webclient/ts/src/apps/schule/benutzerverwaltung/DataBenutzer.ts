import { App } from "~/apps/BaseApp";
import { BaseData } from "~/apps/BaseData";

import {BenutzerDaten, BenutzergruppeListeEintrag, BenutzerListeEintrag, BenutzerManager, Vector} from "@svws-nrw/svws-core-ts";

export class DataBenutzer extends BaseData<BenutzerDaten, BenutzerListeEintrag, BenutzerManager> {
 
	protected on_update(daten: Partial<BenutzerDaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<BenutzerDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<BenutzerDaten | undefined> {
		this.manager = undefined;
		if (!this.selected_list_item) 
			return super.unselect();
		const benutzerdaten = await super._select((eintrag: BenutzerListeEintrag) =>
			App.api.getBenutzerDaten(App.schema, eintrag.id)
		);
		if (benutzerdaten)
			this.manager = new BenutzerManager(benutzerdaten);
		return benutzerdaten;	
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<BenutzerDaten>} data Die Daten, die aktualisiert werden sollen
	 * 
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	 public async patch(data: Partial<BenutzerDaten>): Promise<boolean> {
		 return false;
	}

	/**
	 * Setzt den Anzeigenamen eines Benutzernamens
	 * 
	 * @param {string} anzeigename
	 * 
	 * @returns {Promise<void>}
	 */
	 public async setAnzeigename(anzeigename: string): Promise<void> {
		if (!this.manager) 
			return;
		console.log(anzeigename);
		await App.api.setAnzeigename(anzeigename,App.schema,this.manager.getID());
		for (var index in App.apps.benutzer.auswahl.liste) {
			if (App.apps.benutzer.auswahl.liste[index].id === App.apps.benutzer.dataBenutzer.daten?.id)
				App.apps.benutzer.auswahl.liste[index].anzeigename = anzeigename;
		}
		this.manager.setAnzeigename(anzeigename);
	}

	/**
	 * Setzt den Anmeldenamen eines Benutzernamens
	 * 
	 * @param {string} anzeigename
	 * 
	 * @returns {Promise<void>}
	 */
	 public async setAnmeldename(anmeldename: string): Promise<void> {
		if (!this.manager) 
			return;
		console.log(anmeldename);
		await App.api.setAnmeldename(anmeldename, App.schema, this.manager.getID());
		for (var index in App.apps.benutzer.auswahl.liste){
			if (App.apps.benutzer.auswahl.liste[index].id === App.apps.benutzer.dataBenutzer.daten?.id)
				App.apps.benutzer.auswahl.liste[index].name = anmeldename;
		}
		this.manager.setAnmeldename(anmeldename);
	}

	/**
	 * Fügt den Benutzer in eine Benutzergruppe ein
	 * 
	 * @param {number} bg_id
	 * 
	 * @returns {Promise<void>}
	 */
	 public async addBenutzergruppeBenutzer(bg_id: number): Promise<void> {
		if (!this.manager) 
			return;
		const bg_ids:Vector<Number> = new Vector<Number>();
		bg_ids.add(Number(this.manager.getID()));	
		await App.api.addBenutzergruppeBenutzer(bg_ids, App.schema,bg_id);
		this.manager.addToGruppe(bg_id);
		
	}

	/**
	 * Fügt den Benutzer in alle Benutzergruppe ein
	 * 
	 * @param {} bg_id
	 * 
	 * @returns {Promise<void>}
	 */
	 public async addBenutzergruppenBenutzer(bgle: BenutzergruppeListeEintrag[] | undefined): Promise<void> {
		const benutzer_id:Vector<Number> = new Vector<Number>();
		benutzer_id.add(Number(this.manager?.getID()));
		bgle?.forEach((eintrag) =>  {
			if (!this.manager?.IstInGruppe(eintrag.id)) {
				App.api.addBenutzergruppeBenutzer(benutzer_id, App.schema,eintrag.id);
				this.manager?.addToGruppe(eintrag.id);
			}
		});
	}

	/**
	 * Entfernt den Benutzer aus einer Gruppe
	 * 
	 * @param {number} bg_id
	 * 
	 * @returns {Promise<void>}
	 */
	 public async removeBenutzergruppeBenutzer(bg_id: number): Promise<void> {
		if (!this.manager) 
			return;
		const bg_ids:Vector<Number> = new Vector<Number>();
		bg_ids.add(this.manager.getID());	
		await App.api.removeBenutzergruppeBenutzer(bg_ids, App.schema,bg_id);
		this.manager.removeFromGruppe(bg_id);
	}

	/**
	 * Entfernt den Benutzer aus allen Benutzergruppen.
	 * 
	 * @param {bgle} Benutzergruppen
	 * 
	 * @returns {Promise<void>}
	 */
	 public async removeBenutzergruppenBenutzer(bgle: BenutzergruppeListeEintrag[] | undefined): Promise<void> {
		const benutzer_id:Vector<Number> = new Vector<Number>();
		benutzer_id.add(Number(this.manager?.getID()));	
		bgle?.forEach((eintrag) =>  {
			if (this.manager?.IstInGruppe(eintrag.id)) {
				App.api.removeBenutzergruppeBenutzer(benutzer_id, App.schema, eintrag.id);
				this.manager?.removeFromGruppe(eintrag.id);
			}
		});
	}

}