import { RouteNode } from "~/router/RouteNode";
import { BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";
import { ListBenutzer } from "~/apps/schule/benutzerverwaltung/ListBenutzer";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteSchuleBenutzergruppe, routeSchuleBenutzergruppe } from "../RouteSchuleBenutzergruppe";
import { routeLogin } from "~/router/RouteLogin";
import { router } from "~/router/RouteManager";

const SBenutzergruppe = () => import("~/components/schule/benutzergruppen/daten/SBenutzergruppe.vue");

export class RouteDataSchuleBenutzergruppeDaten {
	item: BenutzergruppeListeEintrag | undefined = undefined;
	daten: DataBenutzergruppe = new DataBenutzergruppe();
	listBenutzer: ListBenutzer = new ListBenutzer();
	
	/**
	 * Setzt die Bezeichnung der Benutzergruppe
	 *
	 * @param {string} bezeichnung
	 *
	 * @returns {Promise<void>}
	 */
	setBezeichnung = async (bezeichnung: string) => {
		if (!this.daten.manager)
			return;
		await routeLogin.data.api.setBenutzergruppeBezeichnung(bezeichnung, routeLogin.data.schema, this.daten.manager.getID());
		this.daten.manager.setBezeichnung(bezeichnung);
		for(const index in routeSchuleBenutzergruppe.liste.liste){
			if(routeSchuleBenutzergruppe.liste.liste[index].id ===this.daten.daten?.id)
			routeSchuleBenutzergruppe.liste.liste[index].bezeichnung=bezeichnung;
		}
	}

	/**
	 * Setzt, ob die Benutzergruppe eine administrative Gruppe ist oder nicht
	 *
	 * @param {boolean} istAdmin
	 *
	 * @returns {Promise<void>}
	 */
	 setIstAdmin = async (istAdmin: boolean) => {
		if (!this.daten.manager)
			return;
		if(istAdmin)
			await routeLogin.data.api.addBenutzergruppeAdmin(routeLogin.data.schema, this.daten.manager.getID());
		else
			await routeLogin.data.api.removeBenutzergruppeAdmin(routeLogin.data.schema, this.daten.manager.getID());
		this.daten.manager.setAdmin(istAdmin);
	}


	/**
	 * Fügt die übergebene Kompetenz zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	addKompetenz = async (kompetenz : BenutzerKompetenz) => {
		const kid = new Vector<number>();
		kid.add(kompetenz.daten.id);
		if (!this.daten.manager)
			return false;
		if (this.daten.manager.hatKompetenz(kompetenz))
			return false;
		await routeLogin.data.api.addBenutzergruppeKompetenzen(kid, routeLogin.data.schema, this.daten.manager.getID());
		this.daten.manager.addKompetenz(kompetenz);
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenz aus dieser Benutzergruppe
	 *
	 * @param kompetenz   die zu entfernende Kompetenz
	 */
	 removeKompetenz = async (kompetenz : BenutzerKompetenz) => {
		const kid = new Vector<number>();
		kid.add(kompetenz.daten.id);
		if (!this.daten.manager)
			return false;
		if (!this.daten.manager.hatKompetenz(kompetenz))
			return false;
		await routeLogin.data.api.removeBenutzergruppeKompetenzen(kid, routeLogin.data.schema, this.daten.manager.getID());
		this.daten.manager.removeKompetenz(kompetenz);
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	 addBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids  = new Vector<number>();
		if (!this.daten.manager)
			return false;
		if (!this.daten.manager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				kids.add(komp.daten.id);
			}
			await routeLogin.data.api.addBenutzergruppeKompetenzen(kids,routeLogin.data.schema,this.daten.manager.getID());
			const benutzergruppendaten = await routeLogin.data.api.getBenutzergruppeDaten(routeLogin.data.schema, this.daten.manager.getID())
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (!this.daten.manager?.hatKompetenz(komp))
					this.daten.manager?.addKompetenz(komp);
			}
		}
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von dieser Benutzergruppe
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	 */
	 removeBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new Vector<number>();
		if (!this.daten.manager)
			return false;
		if (!this.daten.manager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				kids.add(komp.daten.id);
			await routeLogin.data.api.removeBenutzergruppeKompetenzen(kids,routeLogin.data.schema,this.daten.manager.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.daten.manager?.hatKompetenz(komp))
					this.daten.manager?.removeKompetenz(komp);
			}
		}
		return true;
	}

	/**
	 * Erstellt eine neue Benutzergruppe
	 * @param bezeichnung die Bezichnung der neuen Benutzergruppe
	 * @param istAdmin    True, wenn die neue Benutzrgruppe administrativ ist.
	 */
	create = async ( bezeichnung : string, istAdmin : boolean) => {
		const bg = new BenutzergruppeDaten();
		bg.bezeichnung = bezeichnung;
		bg.istAdmin = istAdmin;
		const result = await routeLogin.data.api.createBenutzergruppe(bg,routeLogin.data.schema);
		const bgle = new BenutzergruppeListeEintrag();
		bgle.id = result.id;
		bgle.bezeichnung = result.bezeichnung;
		bgle.istAdmin = result.istAdmin;
		routeSchuleBenutzergruppe.liste.liste.push(bgle);
		routeSchuleBenutzergruppe.liste.ausgewaehlt = result;
		await router.push("/schule/benutzerverwaltung/"+bgle.id+"/benutzergruppe");
	}

	/**
	 * Entfernt die ausgewählten Benutzergruppen
	 */
	deleteBenutzergruppe_n = async () => {
		const benutzer =routeSchuleBenutzergruppe.liste.ausgewaehlt_gruppe;
		console.log(benutzer);
		const bids = new Vector<number>();
		for ( const b of benutzer){
			bids.add(b.id)
		}
		console.log(bids);
		await routeLogin.data.api.removeBenutzerGruppe(bids,routeLogin.data.schema);
		routeSchuleBenutzergruppe.liste.ausgewaehlt_gruppe = [];
		for(const b of benutzer) {
			routeSchuleBenutzergruppe.liste.liste = routeSchuleBenutzergruppe.liste.ausgewaehlt_gruppe.filter(item => item.id !== b.id);
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
	addBenutzergruppeBenutzer = async  (benutzer: BenutzerListeEintrag) => {
		if (!this.daten.manager)
			return;
		const b_ids = new Vector<number>();
		b_ids.add(benutzer.id);
		await routeLogin.data.api.addBenutzergruppeBenutzer(b_ids, routeLogin.data.schema,this.daten.manager.getID()) as BenutzergruppeDaten;
		this.listBenutzer.liste.push(benutzer);

	}

	/**
	 * Entfernt einen Benutzer aus der Gruppe
	 *
	 * @param {BenutzerListeEintrag} benutzer
	 *
	 * @returns {Promise<void>}
	 */
	removeBenutzergruppeBenutzer = async (benutzer: BenutzerListeEintrag) => {
		if (!this.daten.manager)
			return;
		const bg_ids = new Vector<number>();
		bg_ids.add(benutzer.id);
		const result = await routeLogin.data.api.removeBenutzergruppeBenutzer(bg_ids, routeLogin.data.schema,this.daten.manager.getID()) as BenutzergruppeDaten;
		this.listBenutzer.liste = this.listBenutzer.liste.filter(item => item.id !== benutzer.id);
	}




}

export class RouteSchuleBenutzergruppeDaten extends RouteNode<RouteDataSchuleBenutzergruppeDaten, RouteSchuleBenutzergruppe> {

	public constructor() {
		super("benutzergruppe_daten", "daten", SBenutzergruppe, new RouteDataSchuleBenutzergruppeDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.data.listBenutzer.update_list();
			await this.onSelect(this.parent!.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: BenutzergruppeListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.daten.unselect();
		} else {
			this.data.item = item;
			await this.data.daten.select(this.data.item);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeSchuleBenutzergruppe.getProps(to),
			data: this.data.daten,
			benutzer: this.data.listBenutzer.liste,
			manager : this.data.daten.manager,
			setBezeichnung : this.data.setBezeichnung,
			setIstAdmin : this.data.setIstAdmin,
			addKompetenz : this.data.addKompetenz,
			removeKompetenz : this.data.removeKompetenz,
			addBenutzerKompetenzGruppe : this.data.addBenutzerKompetenzGruppe,
			removeBenutzerKompetenzGruppe : this.data.removeBenutzerKompetenzGruppe,
			create : this.data.create,
			deleteBenutzergruppe_n : this.data.deleteBenutzergruppe_n,
			addBenutzergruppeBenutzer : this.data.addBenutzergruppeBenutzer,
			removeBenutzergruppeBenutzer : this.data.removeBenutzergruppeBenutzer


		};
	}

}

export const routeSchuleBenutzergruppeDaten = new RouteSchuleBenutzergruppeDaten();

