import { BenutzerDaten, BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, BenutzerManager, Credentials, List, Vector } from "@svws-nrw/svws-core-ts";
import { Ref, ref, ShallowRef, shallowRef, triggerRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager, router } from "~/router/RouteManager";
import { routeSchuleBenutzer } from "../schule/RouteSchuleBenutzer";
import { routeSchuleBenutzerDaten } from "./RouteSchuleBenutzerDaten";

export class RouteDataSchuleBenutzer {
	auswahl: ShallowRef<BenutzerListeEintrag | undefined> = shallowRef(undefined);
	auswahlGruppe: ShallowRef<BenutzerListeEintrag[]> = shallowRef([]);
	listBenutzer: List<BenutzerListeEintrag> = new Vector();
	mapBenutzer: Map<number, BenutzerListeEintrag> = new Map();
	benutzerManager: ShallowRef<BenutzerManager> = shallowRef(new BenutzerManager(new BenutzerDaten()));
	listBenutzergruppen: Ref<List<BenutzergruppeListeEintrag>> = ref(new Vector());
	_daten: BenutzerDaten | undefined = undefined;

	public async ladeListe() {
		this.listBenutzer = await api.server.getBenutzerliste(api.schema);
		const mapBenutzer = new Map<number, BenutzerListeEintrag>();
		for (const l of this.listBenutzer)
			mapBenutzer.set(l.id, l);
		this.mapBenutzer = mapBenutzer;
	}

	public async setBenutzer(item?: BenutzerListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
			this._daten = undefined;
			this.benutzerManager.value = new BenutzerManager(new BenutzerDaten());
		} else {
			this.auswahl.value = item;
			this._daten = await api.server.getBenutzerDaten(api.schema, item.id);
			this.benutzerManager.value = new BenutzerManager(this._daten);
		}
	}

	gotoBenutzer = async (value: BenutzerListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeSchuleBenutzer.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchuleBenutzer.selectedChild === undefined) ? routeSchuleBenutzerDaten.name : routeSchuleBenutzer.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}
	setAuswahlGruppe = (value: BenutzerListeEintrag[]) => this.auswahlGruppe.value = value;

	get hatDaten(): boolean {
		return this._daten !== undefined;
	}

	get daten(): BenutzerDaten {
		if(this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: BenutzerDaten | undefined) {
		this._daten = value;
	}

	getBenutzerManager = () => {
		return this.benutzerManager.value;
	}
	/**
	 * Setzt den Anzeigenamen eines Benutzernamens
	 *
	 * @param {string} anzeigename
	 *
	 * @returns {Promise<void>}
	 */
	setAnzeigename = async (anzeigename : string) => {
		if (!this.getBenutzerManager())
			return;
		await api.server.setAnzeigename(anzeigename,api.schema,this.getBenutzerManager().getID());
		for (const benutzer of routeSchuleBenutzer.data.listBenutzer){
			if (benutzer.id === this.daten.id)
				benutzer.anzeigename = anzeigename;
		}
		this.getBenutzerManager().setAnzeigename(anzeigename);
		triggerRef(this.benutzerManager);
	}

	/**
	 * Setzt den Anmeldenamen eines Benutzernamens
	 *
	 * @param {string} anzeigename
	 *
	 * @returns {Promise<void>}
	 */
	setAnmeldename =  async (anmeldename: string)=> {
		if (!this.getBenutzerManager())
			return;
		await api.server.setAnmeldename(anmeldename, api.schema, this.getBenutzerManager().getID());
		for (const benutzer of routeSchuleBenutzer.data.listBenutzer){
			if (benutzer.id === this.daten.id)
				benutzer.name = anmeldename;
		}
		this.getBenutzerManager().setAnmeldename(anmeldename);
		triggerRef(this.benutzerManager);
	}

	/**
	 * Setzt, ob die Benutzergruppe eine administrative Gruppe ist oder nicht
	 *
	 * @param {boolean} istAdmin
	 *
	 * @returns {Promise<void>}
	 */
	setIstAdmin = async (istAdmin: boolean) => {
		if (!this.getBenutzerManager())
			return;
		if(istAdmin)
			await api.server.addBenutzerAdmin(api.schema, this.getBenutzerManager().getID());
		else
			await api.server.removeBenutzerAdmin(api.schema, this.getBenutzerManager().getID());
		this.getBenutzerManager().setAdmin(istAdmin);
		triggerRef(this.benutzerManager);
	}

	/**
	 * Setzt das neue Passwort
	 *
	 * @passwort das neue Passwort
	 */

	setPassword = async( passwort : string ) => {
		if (!this.getBenutzerManager())
			return false;
		await api.server.setBenutzerPasswort(passwort,api.schema,this.getBenutzerManager().getID());
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
			if (!this.getBenutzerManager())
				return;
			const bg_ids = new Vector<number>();
			bg_ids.add(this.getBenutzerManager().getID());
			const result = await api.server.addBenutzergruppeBenutzer(bg_ids, api.schema,bg_id) as BenutzergruppeDaten;
			this.getBenutzerManager().addToGruppe(result);
			triggerRef(this.benutzerManager);
		}else{
			const benutzer_id = new Vector<number>();
			benutzer_id.add(this.getBenutzerManager()?.getID() ?? null);
			for(const bg of this.listBenutzergruppen.value){
				if (!this.getBenutzerManager()?.IstInGruppe(bg.id)) {
					api.server.addBenutzergruppeBenutzer(benutzer_id, api.schema,bg.id)
						.then((result: any) => { this.getBenutzerManager()?.addToGruppe(result)
							triggerRef(this.benutzerManager);})
						.catch((e: any) => {throw e});
				}
			}
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
		if (!this.getBenutzerManager())
			return;
		const ids = new Vector<number>();
		ids.add(this.getBenutzerManager().getID());
		if (bg_id !== -1) {
			const result = await api.server.removeBenutzergruppeBenutzer(ids, api.schema,bg_id) as BenutzergruppeDaten;
			this.getBenutzerManager().removeFromGruppe(result);
			triggerRef(this.benutzerManager);
		} else {
			for (const eintrag of this.listBenutzergruppen.value) {
				if (this.getBenutzerManager()?.IstInGruppe(eintrag.id)) {
					const result = await api.server.removeBenutzergruppeBenutzer(ids, api.schema,eintrag.id);
					this.getBenutzerManager()?.removeFromGruppe(result);
				}
			}
			triggerRef(this.benutzerManager);
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
		if (!this.getBenutzerManager())
			return false;
		if (this.getBenutzerManager().hatKompetenz(kompetenz))
			return false;
		await api.server.addBenutzerKompetenzen(kid, api.schema, this.getBenutzerManager().getID());
		this.getBenutzerManager().addKompetenz(kompetenz);
		triggerRef(this.benutzerManager);
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
		if (!this.getBenutzerManager())
			return false;
		if (!this.getBenutzerManager().hatKompetenz(kompetenz))
			return false;
		await api.server.removeBenutzerKompetenzen(kid, api.schema, this.getBenutzerManager().getID());
		this.getBenutzerManager().removeKompetenz(kompetenz);
		triggerRef(this.benutzerManager);
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu diesem Benutzer hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	addBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new Vector<number>();
		if (!this.getBenutzerManager())
			return false;
		if (!this.getBenutzerManager().istAdmin()) {
			//Es werden nur die IDs der Kompetenzen in kids gespreichert, welche dem Benutzer direkt zugordnet sind.
			// Sie sind also nicht über Benutzergruppen vererbt.
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.getBenutzerManager().getGruppen(komp).size() === 0)
					kids.add(komp.daten.id);
			}
			await api.server.addBenutzerKompetenzen(kids,api.schema,this.getBenutzerManager().getID());
			//Den obigen Schritten entsprechende Anpassung des Client-Objekts mithilfe des Managers
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.getBenutzerManager().getGruppen(komp).size() === 0) {
					if (!this.getBenutzerManager()?.hatKompetenz(komp))
						this.getBenutzerManager()?.addKompetenz(komp);
				}
			}
			triggerRef(this.benutzerManager);
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
		if (!this.getBenutzerManager())
			return false;
		if (!this.getBenutzerManager().istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				if (this.getBenutzerManager().getGruppen(komp).size() === 0)
					kids.add(komp.daten.id);
			await api.server.removeBenutzerKompetenzen(kids,api.schema,this.getBenutzerManager().getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.getBenutzerManager().getGruppen(komp).size() === 0) {
					if (this.getBenutzerManager()?.hatKompetenz(komp))
						this.getBenutzerManager()?.removeKompetenz(komp);
				}
			}
			triggerRef(this.benutzerManager);
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
		routeSchuleBenutzer.data.listBenutzer.add(ble);
		routeSchuleBenutzer.data.auswahl.value = result;
		await router.push({ name: routeSchuleBenutzer.name, params: { id : ble.id } });
	}

	/**
	 * Entfernt die ausgewählten Benutzer
	 */
	deleteBenutzerAllgemein = async () => {
		const benutzer = routeSchuleBenutzer.data.auswahlGruppe;
		const bids = new Vector<number>();
		for (const b of benutzer.value) {
			bids.add(b.id)
		}
		await api.server.removeBenutzerAllgemein(bids,api.schema);
		routeSchuleBenutzer.data.auswahlGruppe.value = [];
		for (const b of routeSchuleBenutzer.data.listBenutzer) {
			for(const i of bids){
				if(b.id === i)
					routeSchuleBenutzer.data.listBenutzer.remove(b);
			}
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
		if (this.getBenutzerManager()?.getGruppen(kompetenz)) {
			for (const bg of this.getBenutzerManager().getGruppen(kompetenz)) {
				if (i !== 0)
					text += ", ";
				text += bg.bezeichnung;
				i = -2;
			}
		}
		return text;
	}

}
