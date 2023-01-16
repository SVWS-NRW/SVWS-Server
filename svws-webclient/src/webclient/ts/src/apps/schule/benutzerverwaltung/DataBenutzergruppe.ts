import { App } from "~/apps/BaseApp";
import { BaseData } from "~/apps/BaseData";

import { ListBenutzergruppenBenutzer } from "./ListBenutzergruppenBenutzer";

import { BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, List, Vector } from "@svws-nrw/svws-core-ts";
import { router } from "~/router";
import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";


export class DataBenutzergruppe extends BaseData<BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager> {

	public listBenutzergruppenBenutzer: ListBenutzergruppenBenutzer = new ListBenutzergruppenBenutzer();

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<BenutzergruppeDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<BenutzergruppeDaten | undefined> {
		if (!this.selected_list_item) {
			this.manager = undefined;
			return super.unselect();
		}
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
		for(var index in App.apps.benutzergruppe.auswahl.liste){
			if(App.apps.benutzergruppe.auswahl.liste[index].id === App.apps.benutzergruppe.dataBenutzergruppe.daten?.id)
			App.apps.benutzergruppe.auswahl.liste[index].bezeichnung=bezeichnung;
		}
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
		if(istAdmin)
			await App.api.addBenutzergruppeAdmin(App.schema, this.manager.getID());
		else
			await App.api.removeBenutzergruppeAdmin(App.schema, this.manager.getID());
		this.manager.setAdmin(istAdmin);
	}


	/**
	 * Fügt die übergebene Kompetenz zu dieser Benutzergruppe hinzu
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
		await App.api.addBenutzergruppeKompetenzen(kid, App.schema, this.manager.getID());
		this.manager.addKompetenz(kompetenz);
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenz aus dieser Benutzergruppe
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
		await App.api.removeBenutzergruppeKompetenzen(kid, App.schema, this.manager.getID());
		this.manager.removeKompetenz(kompetenz);
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu dieser Benutzergruppe hinzu
	 * 
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	 public async addBenutzerKompetenzGruppe(kompetenzgruppe : BenutzerKompetenzGruppe) : Promise<boolean> {
		const kids : Vector<Number> = new Vector<Number>();
		if (!this.manager)
			return false;
		if (!this.manager.istAdmin()) {
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				kids.add(Number(komp.daten.id));
			}
			await App.api.addBenutzergruppeKompetenzen(kids,App.schema,this.manager.getID());
			const benutzergruppendaten = await App.api.getBenutzergruppeDaten(App.schema, this.manager.getID())
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (!this.manager?.hatKompetenz(komp))
					this.manager?.addKompetenz(komp);
			}
		}
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von dieser Benutzergruppe 
	 * 
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	 */
	 public async removeBenutzerKompetenzGruppe(kompetenzgruppe : BenutzerKompetenzGruppe) : Promise<boolean> {
		const kids : Vector<Number> = new Vector<Number>();
		if (!this.manager)
			return false;
		if (!this.manager.istAdmin()) {
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				kids.add(Number(komp.daten.id));
			await App.api.removeBenutzergruppeKompetenzen(kids,App.schema,this.manager.getID());
			for (let komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.manager?.hatKompetenz(komp))
					this.manager?.removeKompetenz(komp);
			}
		}
		return true;
	}

	/**
	 * Erstellt eine neue Benutzergruppe
	 * @param bezeichnung die Bezichnung der neuen Benutzergruppe
	 * @param istAdmin    True, wenn die neue Benutzrgruppe administrativ ist.
	 */
	public async create( bezeichnung : string, istAdmin : boolean){
		const bg : BenutzergruppeDaten = new BenutzergruppeDaten();
		bg.bezeichnung = bezeichnung;
		bg.istAdmin = istAdmin;
		const result = await App.api.createBenutzergruppe(bg,App.schema);
		const bgle: BenutzergruppeListeEintrag = new BenutzergruppeListeEintrag();
		bgle.id = result.id;
		bgle.bezeichnung = result.bezeichnung;
		bgle.istAdmin = result.istAdmin;
		App.apps.benutzergruppe.auswahl.liste.push(bgle);
		App.apps.benutzergruppe.auswahl.ausgewaehlt = result;
		router.push("/schule/benutzerverwaltung/"+bgle.id+"/benutzergruppe");
	}

	/**
	 * Entfernt die ausgewählten Benutzer 
	 */
	public async deleteBenutzergruppe_n(){
		const benutzer : BenutzergruppeListeEintrag[] = App.apps.benutzergruppe.auswahl.ausgewaehlt_gruppe;
		console.log(benutzer);
		const bids : Vector<Number> = new Vector<Number>();
		for ( let b of benutzer){
			bids.add(b.id)
		}
		console.log(bids);
		await App.api.removeBenutzerGruppe(bids,App.schema);
		App.apps.benutzergruppe.auswahl.ausgewaehlt_gruppe = [];
		for(let b of benutzer) {
			App.apps.benutzergruppe.auswahl.liste = App.apps.benutzergruppe.auswahl.liste.filter(item => item.id !== b.id);
		}
		alert("Benutzergruppe gelöscht!!!");
		router.push({ name: routeSchuleBenutzergruppe.name});
	}

}
