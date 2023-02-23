import { BenutzergruppeDaten, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, Credentials, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";
import { ListBenutzergruppe } from "~/apps/schule/benutzerverwaltung/ListBenutzergruppe";
import { api } from "~/router/Api";
import { routeSchuleBenutzer, RouteSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { router } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

const SBenutzer = () => import("~/components/schule/benutzer/daten/SBenutzer.vue");

export class RouteDataSchuleBenutzerDaten {
	item: BenutzerListeEintrag | undefined = undefined;
	daten: DataBenutzer = new DataBenutzer();
	listBenutzergruppen: ListBenutzergruppe = new ListBenutzergruppe();

	/**
	 * Setzt den Anzeigenamen eines Benutzernamens
	 *
	 * @param {string} anzeigename
	 *
	 * @returns {Promise<void>}
	 */
	setAnzeigename = async (anzeigename : string) => {
		if (!this.daten.manager)
			return;
		console.log("Es hat geklappt!!");
		await api.server.setAnzeigename(anzeigename,api.schema,this.daten.manager.getID());
		for (const index in routeSchuleBenutzer.liste.liste) {
			if (routeSchuleBenutzer.liste.liste[index].id === routeSchuleBenutzerDaten.data.daten.daten?.id)
				routeSchuleBenutzer.liste.liste[index].anzeigename = anzeigename;
		}
		this.daten.manager.setAnzeigename(anzeigename);
	}

	/**
	 * Setzt den Anmeldenamen eines Benutzernamens
	 *
	 * @param {string} anzeigename
	 *
	 * @returns {Promise<void>}
	 */
	setAnmeldename =  async (anmeldename: string)=> {
		if (!this.daten.manager)
			return;
		await api.server.setAnmeldename(anmeldename, api.schema, this.daten.manager.getID());
		for (const index in routeSchuleBenutzer.liste.liste){
			if (routeSchuleBenutzer.liste.liste[index].id === routeSchuleBenutzerDaten.data.daten.daten?.id)
				routeSchuleBenutzer.liste.liste[index].name = anmeldename;
		}
		this.daten.manager.setAnmeldename(anmeldename);
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
			await api.server.addBenutzerAdmin(api.schema, this.daten.manager.getID());
		else
			await api.server.removeBenutzerAdmin(api.schema, this.daten.manager.getID());
		this.daten.manager.setAdmin(istAdmin);
	}

	/**
	 * Setzt das neue Passwort
	 *
	 * @passwort das neue Passwort
	 */

	setPassword = async( passwort : string ) => {
		if (!this.daten.manager)
			return false;
		await api.server.setBenutzerPasswort(passwort,api.schema,this.daten.manager.getID());
		setTimeout( function ( ) { alert( "Das Kennwort wurde erfolgreich geändert!!" ); }, 300 );
	}

	/**
	 * Fügt den Benutzer in eine Benutzergruppe ein
	 * Bei bg_id = -1 wird der Benutzer in alle Gruppen eingefügt.
	 *
	 * @param {number} bg_id
	 *
	 * @returns {Promise<void>}
	 */
	addBenutzerToBenutzergruppe = async(bg_id: number) => {
		if(bg_id != -1){
			if (!this.daten.manager)
				return;
			const bg_ids = new Vector<number>();
			bg_ids.add(this.daten.manager.getID());
			const result = await api.server.addBenutzergruppeBenutzer(bg_ids, api.schema,bg_id) as BenutzergruppeDaten;
			this.daten.manager.addToGruppe(result);
		}else{
			const benutzer_id = new Vector<number>();
			benutzer_id.add(this.daten.manager?.getID() ?? null);
			this.listBenutzergruppen.liste.forEach(eintrag =>  {
				if (!this.daten.manager?.IstInGruppe(eintrag.id)) {
					api.server.addBenutzergruppeBenutzer(benutzer_id, api.schema,eintrag.id)
						.then((result: any) => this.daten.manager?.addToGruppe(result))
						.catch((e: any) => {throw e});
				}
			});
		}
	}

	/**
	 * Entfernt den Benutzer aus einer Gruppe mit bg_id
	 * Bei bg_id = -1 wird der Benutzer aus allen Gruppen eingefügt.
	 *
	 * @param {number} bg_id
	 *
	 * @returns {Promise<void>}
	 */
	removeBenutzerFromBenutzergruppe = async (bg_id: number): Promise<void> => {
		if (!this.daten.manager)
			return;
		const ids = new Vector<number>();
		ids.add(this.daten.manager.getID());
		if (bg_id !== -1) {
			const result = await api.server.removeBenutzergruppeBenutzer(ids, api.schema,bg_id) as BenutzergruppeDaten;
			this.daten.manager.removeFromGruppe(result);
		} else {
			for (const eintrag of this.listBenutzergruppen.liste) {
				if (this.daten.manager?.IstInGruppe(eintrag.id)) {
					const result = await api.server.removeBenutzergruppeBenutzer(ids, api.schema,eintrag.id);
					this.daten.manager?.removeFromGruppe(result);
				}
			}
		}
	}

	/**
	 * Fügt die übergebene Kompetenz zu dem Benutzer hinzu
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
		await api.server.addBenutzerKompetenzen(kid, api.schema, this.daten.manager.getID());
		this.daten.manager.addKompetenz(kompetenz);
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenz von diesem Benutzer
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
		await api.server.removeBenutzerKompetenzen(kid, api.schema, this.daten.manager.getID());
		this.daten.manager.removeKompetenz(kompetenz);
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu diesem Benutzer hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	addBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new Vector<number>();
		if (!this.daten.manager)
			return false;
		if (!this.daten.manager.istAdmin()) {
			//Es werden nur die IDs der Kompetenzen in kids gespreichert, welche dem Benutzer direkt zugordnet sind.
			// Sie sind also nicht über Benutzergruppen vererbt.
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.daten.manager.getGruppen(komp).size() === 0)
					kids.add(komp.daten.id);
			}
			await api.server.addBenutzerKompetenzen(kids,api.schema,this.daten.manager.getID());
			//Den obigen Schritten entsprechende Anpassung des Client-Objekts mithilfe des Managers
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.daten.manager.getGruppen(komp).size() === 0) {
					if (!this.daten.manager?.hatKompetenz(komp))
						this.daten.manager?.addKompetenz(komp);
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
	 removeBenutzerKompetenzGruppe = async(kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new Vector<number>();
		if (!this.daten.manager)
			return false;
		if (!this.daten.manager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				if (this.daten.manager.getGruppen(komp).size() === 0)
					kids.add(komp.daten.id);
			await api.server.removeBenutzerKompetenzen(kids,api.schema,this.daten.manager.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.daten.manager.getGruppen(komp).size() === 0) {
					if (this.daten.manager?.hatKompetenz(komp))
						this.daten.manager?.removeKompetenz(komp);
				}
			}
		}
		return true;
	}

	/**
	 * Erstellt eine neue Benutzergruppe
	 * @param bezeichnung die Bezichnung der neuen Benutzergruppe
	 * @param istAdmin    True, wenn die neue Benutzrgruppe administrativ ist.
	 */
	createBenutzerAllgemein = async (anmeldename: string, benutzername: string, passwort: string) => {
		const credential = new Credentials();
		credential.benutzername = benutzername;
		credential.password = passwort;
		const result = await api.server.createBenutzerAllgemein(credential,api.schema,anmeldename);
		const ble = new BenutzerListeEintrag();
		ble.id = result.id;
		ble.anzeigename = result.anzeigename;
		ble.name = result.name;
		ble.istAdmin= result.istAdmin;
		ble.idCredentials = result.idCredentials;
		routeSchuleBenutzer.liste.liste.push(ble);
		routeSchuleBenutzer.liste.ausgewaehlt = result;
		await router.push({ name: routeSchuleBenutzer.name, params: { id : ble.id } });
	}

	/**
	 * Entfernt die ausgewählten Benutzer
	 */
	deleteBenutzerAllgemein = async () => {
		const benutzer = routeSchuleBenutzer.liste.ausgewaehlt_gruppe;
		const bids = new Vector<number>();
		for (const b of benutzer) {
			bids.add(b.id)
		}
		await api.server.removeBenutzerAllgemein(bids,api.schema);
		routeSchuleBenutzer.liste.ausgewaehlt_gruppe = [];
		for (const b of benutzer) {
			routeSchuleBenutzer.liste.liste = routeSchuleBenutzer.liste.liste.filter(item => item.id !== b.id);
		}
		alert("Benutzer gelöscht!!!");
		await router.push({ name: routeSchuleBenutzer.name});
	}

	/**
	 * Liefert zur einer Kompetenz die Gruppenzugehörigkeiten
	 *
	 * @kompetenz die Kompetenz
	 */
	getGruppen4Kompetenz = ( kompetenz : BenutzerKompetenz ) : string =>{
		let text="";
		let i = 0;
		if (this.daten.manager?.getGruppen(kompetenz)) {
			for (const bg of this.daten.manager.getGruppen(kompetenz)) {
				if (i !== 0)
					text += ", ";
				text += bg.bezeichnung;
				i = -2;
			}
		}
		return text;
	}
}

export class RouteSchuleBenutzerDaten extends RouteNode<RouteDataSchuleBenutzerDaten, RouteSchuleBenutzer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzer_daten", "daten", SBenutzer, new RouteDataSchuleBenutzerDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.data.listBenutzergruppen.update_list();
			await this.onSelect(this.parent!.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: BenutzerListeEintrag) {
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
			...routeSchuleBenutzer.getProps(to),
			benutzergruppen: this.data.listBenutzergruppen.liste,
			manager : this.data.daten.manager,
			setAnzeigename : this.data.setAnzeigename,
			setAnmeldename : this.data.setAnmeldename,
			setIstAdmin : this.data.setIstAdmin,
			setPassword : this.data.setPassword,
			addBenutzerToBenutzergruppe : this.data.addBenutzerToBenutzergruppe,
			removeBenutzerFromBenutzergruppe : this.data.removeBenutzerFromBenutzergruppe,
			addKompetenz : this.data.addKompetenz,
			removeKompetenz : this.data.removeKompetenz,
			addBenutzerKompetenzGruppe : this.data.addBenutzerKompetenzGruppe,
			removeBenutzerKompetenzGruppe : this.data.removeBenutzerKompetenzGruppe,
			getGruppen4Kompetenz : this.data.getGruppen4Kompetenz
		};
	}

}

export const routeSchuleBenutzerDaten = new RouteSchuleBenutzerDaten();

