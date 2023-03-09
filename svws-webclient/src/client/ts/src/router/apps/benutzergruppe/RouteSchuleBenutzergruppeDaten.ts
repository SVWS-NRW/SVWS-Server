import { BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core";
import { ref, Ref, shallowRef, ShallowRef, triggerRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { api } from "~/router/Api";
import { router } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchuleBenutzergruppe, routeSchuleBenutzergruppe } from "../schule/RouteSchuleBenutzergruppe";



const SBenutzergruppe = () => import("~/components/schule/benutzergruppen/daten/SBenutzergruppe.vue");

export class RouteDataSchuleBenutzergruppeDaten {
	item: BenutzergruppeListeEintrag | undefined = undefined;
	benutzergruppenManager: ShallowRef<BenutzergruppenManager> = shallowRef(new BenutzergruppenManager(new BenutzergruppeDaten()));
	listBenutzerAlle: Ref<List<BenutzerListeEintrag>> = ref(new Vector());
	listBenutzergruppenBenutzer: Ref<List<BenutzerListeEintrag>> = ref( new Vector());

	private _daten: BenutzergruppeDaten | undefined = undefined;

	get daten(): BenutzergruppeDaten {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: BenutzergruppeDaten | undefined) {
		this._daten = value;
	}

	getBenutzergruppenManager = () => {
		return this.benutzergruppenManager.value;
	}

	/**
	 * Setzt die Bezeichnung der Benutzergruppe
	 *
	 * @param {string} bezeichnung
	 *
	 * @returns {Promise<void>}
	 */
	setBezeichnung = async (bezeichnung: string) => {
		if (!this.benutzergruppenManager.value)
			return;
		await api.server.setBenutzergruppeBezeichnung(bezeichnung, api.schema, this.benutzergruppenManager.value.getID());
		this.benutzergruppenManager.value.setBezeichnung(bezeichnung);
		triggerRef(this.benutzergruppenManager);
		for(const index in routeSchuleBenutzergruppe.liste.liste){
			if(routeSchuleBenutzergruppe.liste.liste[index].id ===this.daten.id)
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
		if (!this.benutzergruppenManager.value)
			return;
		if(istAdmin)
			await api.server.addBenutzergruppeAdmin(api.schema, this.benutzergruppenManager.value.getID());
		else
			await api.server.removeBenutzergruppeAdmin(api.schema,this.benutzergruppenManager.value.getID());
		this.benutzergruppenManager.value.setAdmin(istAdmin);
		triggerRef(this.benutzergruppenManager);
	}


	/**
	 * Fügt die übergebene Kompetenz zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	addKompetenz = async (kompetenz : BenutzerKompetenz) => {
		const kid = new Vector<number>();
		kid.add(kompetenz.daten.id);
		if (!this.benutzergruppenManager.value)
			return false;
		if (this.benutzergruppenManager.value.hatKompetenz(kompetenz))
			return false;
		await api.server.addBenutzergruppeKompetenzen(kid, api.schema, this.benutzergruppenManager.value.getID());
		this.benutzergruppenManager.value.addKompetenz(kompetenz);
		triggerRef(this.benutzergruppenManager);
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
		if (!this.benutzergruppenManager.value)
			return false;
		if (!this.benutzergruppenManager.value.hatKompetenz(kompetenz))
			return false;
		await api.server.removeBenutzergruppeKompetenzen(kid, api.schema, this.benutzergruppenManager.value.getID());
		this.benutzergruppenManager.value.removeKompetenz(kompetenz);
		triggerRef(this.benutzergruppenManager);
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	 addBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids  = new Vector<number>();
		if (!this.benutzergruppenManager.value)
			return false;
		if (!this.benutzergruppenManager.value.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				kids.add(komp.daten.id);
			}
			await api.server.addBenutzergruppeKompetenzen(kids,api.schema,this.benutzergruppenManager.value.getID());
			const benutzergruppendaten = await api.server.getBenutzergruppeDaten(api.schema, this.benutzergruppenManager.value.getID())
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (!this.benutzergruppenManager.value?.hatKompetenz(komp))
					this.benutzergruppenManager.value?.addKompetenz(komp);
			}
		}
		triggerRef(this.benutzergruppenManager);
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von dieser Benutzergruppe
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	 */
	 removeBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new Vector<number>();
		if (!this.benutzergruppenManager.value)
			return false;
		if (!this.benutzergruppenManager.value.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				kids.add(komp.daten.id);
			await api.server.removeBenutzergruppeKompetenzen(kids,api.schema,this.benutzergruppenManager.value.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.benutzergruppenManager.value?.hatKompetenz(komp))
					this.benutzergruppenManager.value?.removeKompetenz(komp);
			}
		}
		triggerRef(this.benutzergruppenManager);
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
		const result = await api.server.createBenutzergruppe(bg,api.schema);
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
		await api.server.removeBenutzerGruppe(bids,api.schema);
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
	addBenutzerToBenutzergruppe = async  (benutzer: BenutzerListeEintrag) => {
		if (!this.benutzergruppenManager.value)
			return;
		const b_ids = new Vector<number>();
		b_ids.add(benutzer.id);
		await api.server.addBenutzergruppeBenutzer(b_ids, api.schema,this.benutzergruppenManager.value.getID()) as BenutzergruppeDaten;
		this.listBenutzergruppenBenutzer.value.add(benutzer);
		this.listBenutzerAlle.value.remove(benutzer);

	}

	/**
	 * Entfernt einen Benutzer aus der Gruppe
	 *
	 * @param {BenutzerListeEintrag} benutzer
	 *
	 * @returns {Promise<void>}
	 */
	removeBenutzerFromBenutzergruppe = async (benutzer: BenutzerListeEintrag) => {
		if (!this.benutzergruppenManager.value)
			return;
		const bg_ids = new Vector<number>();
		bg_ids.add(benutzer.id);
		const result = await api.server.removeBenutzergruppeBenutzer(bg_ids, api.schema,this.benutzergruppenManager.value.getID()) as BenutzergruppeDaten;
		this.listBenutzergruppenBenutzer.value.remove(benutzer);
		this.listBenutzerAlle.value.add(benutzer);
	}




}

export class RouteSchuleBenutzergruppeDaten extends RouteNode<RouteDataSchuleBenutzergruppeDaten, RouteSchuleBenutzergruppe> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzergruppe_daten", "daten", SBenutzergruppe, new RouteDataSchuleBenutzergruppeDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: BenutzergruppeListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
			this.data.benutzergruppenManager.value = new BenutzergruppenManager(new BenutzergruppeDaten());
			this.data.listBenutzergruppenBenutzer.value = new Vector();
			this.data.listBenutzerAlle.value = new Vector();
		} else {
			this.data.item = item;
			this.data.daten = await api.server.getBenutzergruppeDaten(api.schema, item.id);
			this.data.benutzergruppenManager.value = new BenutzergruppenManager(this.data.daten);
			this.data.listBenutzerAlle.value = await api.server.getBenutzerliste(api.schema);
			this.data.listBenutzergruppenBenutzer.value = await api.server.getBenutzerMitGruppenID(api.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: routeSchuleBenutzergruppe._item,
			data: this.data.daten,
			listBenutzerAlle: this.data.listBenutzerAlle.value,
			listBenutzergruppenBenutzer: this.data.listBenutzergruppenBenutzer.value,
			getBenutzergruppenManager : this.data.getBenutzergruppenManager,
			setBezeichnung : this.data.setBezeichnung,
			setIstAdmin : this.data.setIstAdmin,
			addKompetenz : this.data.addKompetenz,
			removeKompetenz : this.data.removeKompetenz,
			addBenutzerKompetenzGruppe : this.data.addBenutzerKompetenzGruppe,
			removeBenutzerKompetenzGruppe : this.data.removeBenutzerKompetenzGruppe,
			create : this.data.create,
			deleteBenutzergruppe_n : this.data.deleteBenutzergruppe_n,
			addBenutzerToBenutzergruppe : this.data.addBenutzerToBenutzergruppe,
			removeBenutzerFromBenutzergruppe : this.data.removeBenutzerFromBenutzergruppe


		};
	}

}

export const routeSchuleBenutzergruppeDaten = new RouteSchuleBenutzergruppeDaten();

