import { App } from "~/apps/BaseApp";
import { BaseData } from "~/apps/BaseData";

import {BenutzerDaten, BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, BenutzerManager, Vector} from "@svws-nrw/svws-core-ts";

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
		if (!this.selected_list_item)  {
			this.manager = undefined;
			return super.unselect();
		}
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
		const result = await App.api.addBenutzergruppeBenutzer(bg_ids, App.schema,bg_id) as BenutzergruppeDaten;
		this.manager.addToGruppe(result);
		
		
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
		bgle?.forEach(async (eintrag) =>  {
			if (!this.manager?.IstInGruppe(eintrag.id)) {
				const result = await App.api.addBenutzergruppeBenutzer(benutzer_id, App.schema,eintrag.id) as BenutzergruppeDaten;
				this.manager?.addToGruppe(result);
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
		const result = await App.api.removeBenutzergruppeBenutzer(bg_ids, App.schema,bg_id) as BenutzergruppeDaten;
		this.manager.removeFromGruppe(result);
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
		bgle?.forEach(async (eintrag) =>  {
			if (this.manager?.IstInGruppe(eintrag.id)) {
				const result = await App.api.removeBenutzergruppeBenutzer(benutzer_id, App.schema, eintrag.id) as BenutzergruppeDaten;
				this.manager?.removeFromGruppe(result);
			}
		});
	}

	/**
	 * Fügt die übergebene Kompetenz zu dem Benutzer hinzu
	 * 
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	 public async addKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
		const kid:Vector<Number> = new Vector<Number>();
		kid.add(Number(kompetenz.daten.id));
		if (!this.manager)
			return false;
		if (this.manager.hatKompetenz(kompetenz))
			return false;
		await App.api.addBenutzerKompetenzen(kid, App.schema, this.manager.getID());
		this.manager.addKompetenz(kompetenz);
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenz von diesem Benutzer
	 * 
	 * @param kompetenz   die zu entfernende Kompetenz
	 */
	 public async removeKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
		const kid:Vector<Number> = new Vector<Number>();
		kid.add(Number(kompetenz.daten.id));
		if (!this.manager)
			return false;
		if (!this.manager.hatKompetenz(kompetenz))
			return false;
		await App.api.removeBenutzerKompetenzen(kid, App.schema, this.manager.getID());
		this.manager.removeKompetenz(kompetenz);
		return true;
	}



	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu diesem Benutzer hinzu
	 * 
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	 public async addBenutzerKompetenzGruppe(kompetenzgruppe : BenutzerKompetenzGruppe) : Promise<boolean> {
		const kids : Vector<Number> = new Vector<Number>();
		if (!this.manager)
			return false;
		if (!this.manager.istAdmin()) {
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if(this.manager.getGruppen(komp).size() === 0)
					kids.add(Number(komp.daten.id));
			}
			await App.api.addBenutzerKompetenzen(kids,App.schema,this.manager.getID());
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if(this.manager.getGruppen(komp).size() === 0){
					if (!this.manager?.hatKompetenz(komp))
						this.manager?.addKompetenz(komp);
				}
			}
		}
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von diesem Benutzer
	 * 
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	 */
	 public async removeBenutzerKompetenzGruppe(kompetenzgruppe : BenutzerKompetenzGruppe) : Promise<boolean> {
		const kids : Vector<Number> = new Vector<Number>();
		if (!this.manager)
			return false;
		if (!this.manager.istAdmin()) {
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				if(this.manager.getGruppen(komp).size() === 0)
					kids.add(Number(komp.daten.id));
			await App.api.removeBenutzerKompetenzen(kids,App.schema,this.manager.getID());
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if(this.manager.getGruppen(komp).size() === 0){
					if (this.manager?.hatKompetenz(komp))
						this.manager?.removeKompetenz(komp);
				}		
			}
		}
		return true;
	}
}