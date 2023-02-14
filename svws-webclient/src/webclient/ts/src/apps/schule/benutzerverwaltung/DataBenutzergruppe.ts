import { routeLogin } from "~/router/RouteLogin";
import { BaseData } from "~/apps/BaseData";

import { ListBenutzergruppenBenutzer } from "./ListBenutzergruppenBenutzer";

import { BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
import { router } from "~/router/RouteManager";
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
			routeLogin.data.api.getBenutzergruppeDaten(routeLogin.data.schema, eintrag.id)
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
		await routeLogin.data.api.setBenutzergruppeBezeichnung(bezeichnung, routeLogin.data.schema, this.manager.getID());
		this.manager.setBezeichnung(bezeichnung);
		for(const index in App.apps.benutzergruppe.auswahl.liste){
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
			await routeLogin.data.api.addBenutzergruppeAdmin(routeLogin.data.schema, this.manager.getID());
		else
			await routeLogin.data.api.removeBenutzergruppeAdmin(routeLogin.data.schema, this.manager.getID());
		this.manager.setAdmin(istAdmin);
	}


	/**
	 * Fügt die übergebene Kompetenz zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	public async addKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
		const kid = new Vector<number>();
		kid.add(kompetenz.daten.id);
		if (!this.manager)
			return false;
		if (this.manager.hatKompetenz(kompetenz))
			return false;
		await routeLogin.data.api.addBenutzergruppeKompetenzen(kid, routeLogin.data.schema, this.manager.getID());
		this.manager.addKompetenz(kompetenz);
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenz aus dieser Benutzergruppe
	 *
	 * @param kompetenz   die zu entfernende Kompetenz
	 */
	 public async removeKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
		const kid = new Vector<number>();
		kid.add(kompetenz.daten.id);
		if (!this.manager)
			return false;
		if (!this.manager.hatKompetenz(kompetenz))
			return false;
		await routeLogin.data.api.removeBenutzergruppeKompetenzen(kid, routeLogin.data.schema, this.manager.getID());
		this.manager.removeKompetenz(kompetenz);
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	 public async addBenutzerKompetenzGruppe(kompetenzgruppe : BenutzerKompetenzGruppe) : Promise<boolean> {
		const kids  = new Vector<number>();
		if (!this.manager)
			return false;
		if (!this.manager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				kids.add(komp.daten.id);
			}
			await routeLogin.data.api.addBenutzergruppeKompetenzen(kids,routeLogin.data.schema,this.manager.getID());
			const benutzergruppendaten = await routeLogin.data.api.getBenutzergruppeDaten(routeLogin.data.schema, this.manager.getID())
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
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
		const kids = new Vector<number>();
		if (!this.manager)
			return false;
		if (!this.manager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				kids.add(komp.daten.id);
			await routeLogin.data.api.removeBenutzergruppeKompetenzen(kids,routeLogin.data.schema,this.manager.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
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
		const bg = new BenutzergruppeDaten();
		bg.bezeichnung = bezeichnung;
		bg.istAdmin = istAdmin;
		const result = await routeLogin.data.api.createBenutzergruppe(bg,routeLogin.data.schema);
		const bgle = new BenutzergruppeListeEintrag();
		bgle.id = result.id;
		bgle.bezeichnung = result.bezeichnung;
		bgle.istAdmin = result.istAdmin;
		App.apps.benutzergruppe.auswahl.liste.push(bgle);
		App.apps.benutzergruppe.auswahl.ausgewaehlt = result;
		await router.push("/schule/benutzerverwaltung/"+bgle.id+"/benutzergruppe");
	}

	/**
	 * Entfernt die ausgewählten Benutzergruppen
	 */
	public async deleteBenutzergruppe_n(){
		const benutzer = App.apps.benutzergruppe.auswahl.ausgewaehlt_gruppe;
		console.log(benutzer);
		const bids = new Vector<number>();
		for ( const b of benutzer){
			bids.add(b.id)
		}
		console.log(bids);
		await routeLogin.data.api.removeBenutzerGruppe(bids,routeLogin.data.schema);
		App.apps.benutzergruppe.auswahl.ausgewaehlt_gruppe = [];
		for(const b of benutzer) {
			App.apps.benutzergruppe.auswahl.liste = App.apps.benutzergruppe.auswahl.liste.filter(item => item.id !== b.id);
		}
		alert("Benutzergruppe gelöscht!!!");
		await router.push({ name: routeSchuleBenutzergruppe.name});
	}

	/**
	 * Fügt den Benutzer in die Benutzergruppe ein
	 *
	 * @param {BenutzerListeEintrag} benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async addBenutzergruppeBenutzer(benutzer: BenutzerListeEintrag): Promise<void> {
		if (!this.manager)
			return;
		const b_ids = new Vector<number>();
		b_ids.add(benutzer.id);
		await routeLogin.data.api.addBenutzergruppeBenutzer(b_ids, routeLogin.data.schema,this.manager.getID()) as BenutzergruppeDaten;
		App.apps.benutzergruppe.dataBenutzergruppe.listBenutzergruppenBenutzer.liste.push(benutzer);

	}

	/**
	 * Entfernt einen Benutzer aus der Gruppe
	 *
	 * @param {BenutzerListeEintrag} benutzer
	 *
	 * @returns {Promise<void>}
	 */
	public async removeBenutzergruppeBenutzer(benutzer: BenutzerListeEintrag): Promise<void> {
		if (!this.manager)
			return;
		const bg_ids = new Vector<number>();
		bg_ids.add(benutzer.id);
		const result = await routeLogin.data.api.removeBenutzergruppeBenutzer(bg_ids, routeLogin.data.schema,this.manager.getID()) as BenutzergruppeDaten;
		App.apps.benutzergruppe.dataBenutzergruppe.listBenutzergruppenBenutzer.liste = App.apps.benutzergruppe.dataBenutzergruppe.listBenutzergruppenBenutzer.liste.filter(item => item.id !== benutzer.id);
	}

}
