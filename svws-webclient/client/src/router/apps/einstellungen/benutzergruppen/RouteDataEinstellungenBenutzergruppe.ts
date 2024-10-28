import type { BenutzerKompetenzGruppe, BenutzerListeEintrag, List} from "@core";
import { BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerKompetenz, ArrayList, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeEinstellungenBenutzergruppeDaten } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppeDaten";
import { routeEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppe";
import { routeApp } from "../../RouteApp";
import { RouteNode } from "~/router/RouteNode";


interface RoutStateEinstellungenBenutzergruppe extends RouteStateInterface {
	auswahl: BenutzergruppeListeEintrag | undefined;
	mapBenutzergruppe: Map<number, BenutzergruppeListeEintrag>;
	benutzergruppenManager: BenutzergruppenManager;
	listBenutzergruppe: List<BenutzergruppeListeEintrag>;
	daten: BenutzergruppeDaten | undefined;
	listBenutzerAlle: List<BenutzerListeEintrag>;
	listBenutzergruppenBenutzer: List<BenutzerListeEintrag>;
}

const defaultState = <RoutStateEinstellungenBenutzergruppe> {
	auswahl: undefined,
	listBenutzergruppe: new ArrayList(),
	mapBenutzergruppe: new Map<number, BenutzergruppeListeEintrag>,
	benutzergruppenManager: new BenutzergruppenManager(new BenutzergruppeDaten()),
	listBenutzerAlle: new ArrayList(),
	listBenutzergruppenBenutzer: new ArrayList(),
	daten: undefined,
	view: routeEinstellungenBenutzergruppeDaten,
};

export class RouteDataEinstellungenBenutzergruppe extends RouteData<RoutStateEinstellungenBenutzergruppe> {

	public constructor() {
		super(defaultState);
	}

	private firstBenutzer(mapBenutzergruppe: Map<number, BenutzergruppeListeEintrag>): BenutzergruppeListeEintrag | undefined {
		if (mapBenutzergruppe.size === 0)
			return undefined;
		return mapBenutzergruppe.values().next().value;
	}

	private async ladeBenutzergruppenDaten(eintrag: BenutzergruppeListeEintrag | undefined): Promise<BenutzergruppeDaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		const result = await api.server.getBenutzergruppeDaten(api.schema, eintrag.id);
		return result;
	}

	public async ladeListe() {
		const listBenutzergruppe = await api.server.getBenutzergruppenliste(api.schema);
		const mapBenutzergruppe = new Map<number, BenutzergruppeListeEintrag>();
		for (const l of listBenutzergruppe)
			mapBenutzergruppe.set(l.id, l);
		const listBenutzerAlle = await api.server.getBenutzerliste(api.schema);
		this.setPatchedState({
			listBenutzergruppe: listBenutzergruppe,
			mapBenutzergruppe : mapBenutzergruppe,
			listBenutzerAlle: listBenutzerAlle,
		})
	}
	public async setBenutzergruppe(benutzerGruppe: BenutzergruppeListeEintrag | undefined ) {
		if (benutzerGruppe?.id === this._state.value.auswahl?.id && this.hatDaten)
			return;
		if ((benutzerGruppe === undefined) || (this.mapBenutzergruppe.size === 0)) {
			this.setPatchedDefaultState({
				auswahl: undefined,
				listBenutzergruppe: new ArrayList(),
				mapBenutzergruppe: new Map(),
				benutzergruppenManager: new BenutzergruppenManager(new BenutzergruppeDaten()),
				listBenutzerAlle: new ArrayList(),
				daten: undefined,

			})
			await this.ladeListe();
		}
		const neueAuswahl = benutzerGruppe === undefined ? this.firstBenutzer(this.mapBenutzergruppe) : benutzerGruppe;
		const daten = await this.ladeBenutzergruppenDaten(neueAuswahl);
		const benutzergruppenManager = daten=== undefined ? undefined : new BenutzergruppenManager(daten);
		const listBenutzergruppenBenutzer = neueAuswahl === undefined ? undefined : await api.server.getBenutzerMitGruppenID(api.schema, neueAuswahl.id);
		this.setPatchedState({
			auswahl: neueAuswahl,
			benutzergruppenManager: benutzergruppenManager,
			listBenutzergruppenBenutzer: listBenutzergruppenBenutzer,
			daten: daten
		})
	}

	gotoBenutzer = async (b_id: number) => {
		const node = RouteNode.getNodeByName("einstellungen.benutzer.daten");
		if (node !== undefined)
			await RouteManager.doRoute(node.getRoute({ id: b_id }));
	}

	gotoBenutzergruppe = async (value: BenutzergruppeListeEintrag | undefined) => {
		const route = (value === undefined) ? routeEinstellungenBenutzergruppe.getRoute() : routeEinstellungenBenutzergruppe.getRouteSelectedChild({ id: value.id });
		await RouteManager.doRoute(route);
	}

	getBenutzergruppenManager = () => {
		return this._state.value.benutzergruppenManager;
	}

	get benutzergruppenManager(): BenutzergruppenManager {
		return this._state.value.benutzergruppenManager
	}

	set benutzergruppenManager(value: BenutzergruppenManager) {
		this._state.value.benutzergruppenManager = value;
	}

	get auswahl(): BenutzergruppeListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	set auswahl(value: BenutzergruppeListeEintrag | undefined) {
		this._state.value.auswahl = value;
	}

	get listBenutzergruppe(): List<BenutzergruppeListeEintrag>{
		return this._state.value.listBenutzergruppe;
	}

	set listBenutzergruppe(value: List<BenutzergruppeListeEintrag>) {
		this._state.value.listBenutzergruppe = value;
	}

	get listBenutzerAlle(): List<BenutzerListeEintrag>{
		return this._state.value.listBenutzerAlle;
	}

	set listBenutzerAlle(value: List<BenutzerListeEintrag>) {
		this._state.value.listBenutzerAlle = value;
	}

	get listBenutzergruppenBenutzer(): List<BenutzerListeEintrag>{
		return this._state.value.listBenutzergruppenBenutzer
	}

	set listBenutzergruppenBenutzer(value: List<BenutzerListeEintrag>) {
		this._state.value.listBenutzergruppenBenutzer = value;
	}


	get hatDaten(): boolean {
		return this._state.value.daten !== undefined;
	}

	get daten(): BenutzergruppeDaten {
		if(this._state.value.daten === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	set daten(value: BenutzergruppeDaten | undefined) {
		this._state.value.daten = value;
	}

	get mapBenutzergruppe(): Map<number, BenutzergruppeListeEintrag> {
		return this._state.value.mapBenutzergruppe
	}

	set mapBenutzergruppe(value: Map<number, BenutzergruppeListeEintrag>) {
		this._state.value.mapBenutzergruppe = value;
	}


	/**
	 * Setzt die Bezeichnung der Benutzergruppe
	 *
	 * @param {string} bezeichnung
	 *
	 * @returns {Promise<void>}
	 */
	setBezeichnung = async (bezeichnung: string | null) => {
		if (bezeichnung === null)
			return;
		await api.server.setBenutzergruppeBezeichnung(bezeichnung, api.schema, this.benutzergruppenManager.getID());
		this.benutzergruppenManager.setBezeichnung(bezeichnung);
		const neueAuswahl = this.mapBenutzergruppe.get(this.daten.id);
		this.mapBenutzergruppe.set(this.daten.id,this.daten);
		this.setPatchedState({
			auswahl: neueAuswahl,
			mapBenutzergruppe : this.mapBenutzergruppe,
			benutzergruppenManager : this.benutzergruppenManager
		})
	}

	/**
	 * Setzt, ob die Benutzergruppe eine administrative Gruppe ist oder nicht
	 *
	 * @param {boolean} istAdmin
	 *
	 * @returns {Promise<void>}
	 */
	setIstAdmin = async (istAdmin: boolean) => {
		if(istAdmin)
			await api.server.addBenutzergruppeAdmin(api.schema, this.benutzergruppenManager.getID());
		else
			await api.server.removeBenutzergruppeAdmin(api.schema,this.benutzergruppenManager.getID());
		this.benutzergruppenManager.setAdmin(istAdmin);
		this.setPatchedState({
			benutzergruppenManager : this.benutzergruppenManager
		})
	}


	/**
	 * Fügt die übergebene Kompetenz zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	addKompetenz = async (kompetenz : BenutzerKompetenz) => {
		const kid = new ArrayList<number>();
		kid.add(kompetenz.daten.id);
		if (this.benutzergruppenManager.hatKompetenz(kompetenz))
			return false;
		await api.server.addBenutzergruppeKompetenzen(kid, api.schema, this.benutzergruppenManager.getID());
		this.benutzergruppenManager.addKompetenz(kompetenz);
		this.setPatchedState({
			benutzergruppenManager : this.benutzergruppenManager
		})
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenz aus dieser Benutzergruppe
	 *
	 * @param kompetenz   die zu entfernende Kompetenz
	 */
	removeKompetenz = async (kompetenz : BenutzerKompetenz) => {
		const kid = new ArrayList<number>();
		kid.add(kompetenz.daten.id);
		if (!this.benutzergruppenManager.hatKompetenz(kompetenz))
			return false;
		await api.server.removeBenutzergruppeKompetenzen(kid, api.schema, this.benutzergruppenManager.getID());
		this.benutzergruppenManager.removeKompetenz(kompetenz);
		this.setPatchedState({
			benutzergruppenManager : this.benutzergruppenManager
		})
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu dieser Benutzergruppe hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	addBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new ArrayList<number>();
		if (!this.benutzergruppenManager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				kids.add(komp.daten.id);
			}
			await api.server.addBenutzergruppeKompetenzen(kids,api.schema,this.benutzergruppenManager.getID());
			//const benutzergruppendaten = await api.server.getBenutzergruppeDaten(api.schema, this.benutzergruppenManager.getID())
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (!this.benutzergruppenManager.hatKompetenz(komp))
					this.benutzergruppenManager.addKompetenz(komp);
			}
		}
		this.setPatchedState({
			benutzergruppenManager : this.benutzergruppenManager
		})
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von dieser Benutzergruppe
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	 */
	removeBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new ArrayList<number>();
		if (!this.benutzergruppenManager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				kids.add(komp.daten.id);
			await api.server.removeBenutzergruppeKompetenzen(kids,api.schema,this.benutzergruppenManager.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.benutzergruppenManager.hatKompetenz(komp))
					this.benutzergruppenManager.removeKompetenz(komp);
			}
		}
		this.setPatchedState({
			benutzergruppenManager : this.benutzergruppenManager
		})
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
		this.listBenutzergruppe.add(bgle);
		this.mapBenutzergruppe.set(bgle.id,bgle);
		this.setPatchedState({
			benutzergruppenManager : this.benutzergruppenManager,
			listBenutzergruppe: this.listBenutzergruppe,
			mapBenutzergruppe: this.mapBenutzergruppe

		})
		await this.gotoBenutzergruppe(bgle);
	}

	/**
	 * Entfernt die ausgewählten Benutzergruppen
	 */
	deleteBenutzergruppen = async (selectedItems: BenutzergruppeListeEintrag[]) => {
		const bids = new ArrayList<number>();
		for ( const b of selectedItems){
			bids.add(b.id)
		}
		let auswahl_gewaehlt = false;
		if(this.auswahl !== undefined)
			auswahl_gewaehlt= selectedItems.includes(this.auswahl);
		await api.server.removeBenutzerGruppe(bids,api.schema);
		for (const i of bids) {
			this.mapBenutzergruppe.delete(i);
		}
		for(const b of selectedItems) {
			this.listBenutzergruppe.remove(b);
		}
		alert("Benutzergruppe gelöscht.");
		if(auswahl_gewaehlt)
			await this.gotoBenutzergruppe(this.listBenutzergruppe.get(0));
		this.setPatchedState({
			listBenutzergruppe: this.listBenutzergruppe,
			mapBenutzergruppe: this.mapBenutzergruppe

		})
	}

	/**
	 * Fügt den Benutzer in die Benutzergruppe ein
	 *
	 * @param {BenutzerListeEintrag} benutzer
	 *
	 * @returns {Promise<void>}
	 */
	addBenutzerToBenutzergruppe = async (benutzer: BenutzerListeEintrag): Promise<void> => {
		const b_ids = new ArrayList<number>();
		b_ids.add(benutzer.id);
		await api.server.addBenutzergruppeBenutzer(b_ids, api.schema,this.benutzergruppenManager.getID());
		this._state.value.listBenutzergruppenBenutzer.add(benutzer);
		const temp = this.listBenutzergruppenBenutzer;
		const liste_benutzer_ids = new Set();
		const temp_listBenutzerAlle = new ArrayList<BenutzerListeEintrag>();

		for(const b of this.listBenutzergruppenBenutzer){
			liste_benutzer_ids.add(b.id)
		}
		for(const b of this.listBenutzerAlle){
			if(!liste_benutzer_ids.has(b.id))
				temp_listBenutzerAlle.add(b);
		}
		this.setPatchedState({
			listBenutzergruppenBenutzer : temp,
			listBenutzerAlle: temp_listBenutzerAlle
		})

	}

	/**
	 * Entfernt einen Benutzer aus der Gruppe
	 *
	 * @param {BenutzerListeEintrag} benutzer
	 *
	 * @returns {Promise<void>}
	 */
	removeBenutzerFromBenutzergruppe = async (benutzer: BenutzerListeEintrag): Promise<void> => {
		const bg_ids = new ArrayList<number>();
		bg_ids.add(benutzer.id);
		await api.server.removeBenutzergruppeBenutzer(bg_ids, api.schema,this.benutzergruppenManager.getID());
		this.listBenutzergruppenBenutzer.remove(benutzer);
		this.listBenutzerAlle.add(benutzer);
		this.setPatchedState({
			listBenutzergruppe: this.listBenutzergruppe,
			mapBenutzergruppe: this.mapBenutzergruppe
		})
	}

	aktualisiereListeBenutzerGruppenBenutzer = async (benutzer: BenutzerListeEintrag) => {
		this.listBenutzergruppenBenutzer.add(benutzer);
	}

}