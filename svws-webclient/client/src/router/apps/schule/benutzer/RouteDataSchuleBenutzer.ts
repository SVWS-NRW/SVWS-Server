import { shallowRef } from "vue";

import type { BenutzergruppeListeEintrag, BenutzerKompetenzGruppe, List} from "@core";
import { BenutzerDaten, BenutzerKompetenz, BenutzerListeEintrag, BenutzerManager, BenutzerAllgemeinCredentials, ArrayList } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";
import { routeSchuleBenutzerDaten } from "~/router/apps/schule/benutzer/RouteSchuleBenutzerDaten";


interface RouteStateSchuleBenutzer {
	auswahl: BenutzerListeEintrag | undefined;
	listBenutzer: List<BenutzerListeEintrag> ;
	mapBenutzer: Map<number, BenutzerListeEintrag>;
	benutzerManager: BenutzerManager;
	listBenutzergruppen: List<BenutzergruppeListeEintrag>;
	daten: BenutzerDaten | undefined;
	view: RouteNode<any, any>;
}

export class RouteDataSchuleBenutzer {

	private static _defaultState : RouteStateSchuleBenutzer = {
		auswahl: undefined,
		listBenutzer: new ArrayList(),
		mapBenutzer: new Map<number, BenutzerListeEintrag>,
		benutzerManager: new BenutzerManager(new BenutzerDaten()),
		listBenutzergruppen: new ArrayList(),
		daten: undefined,
		view: routeSchuleBenutzerDaten,
	}

	private _state = shallowRef<RouteStateSchuleBenutzer>(RouteDataSchuleBenutzer._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuleBenutzer>) {
		this._state.value = Object.assign({ ... RouteDataSchuleBenutzer._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuleBenutzer>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	private firstBenutzer(mapBenutzer: Map<number, BenutzerListeEintrag>): BenutzerListeEintrag | undefined {
		if (mapBenutzer.size === 0)
			return undefined;
		return mapBenutzer.values().next().value;
	}

	private async ladeBenutzerDaten(eintrag: BenutzerListeEintrag | undefined): Promise<BenutzerDaten | undefined> {
		if (eintrag === undefined)
			return undefined;
		const result = await api.server.getBenutzerDaten(api.schema, eintrag.id);
		return result;
	}

	public async ladeListe() {
		this._state.value.listBenutzer = await api.server.getBenutzerliste(api.schema);
		const mapBenutzer = new Map<number, BenutzerListeEintrag>();
		for (const l of this.listBenutzer)
			mapBenutzer.set(l.id, l);
		this.mapBenutzer = mapBenutzer;
		this.setPatchedState({
			mapBenutzer : this._state.value.mapBenutzer,
			listBenutzer: this._state.value.listBenutzer
		})
	}

	public async setBenutzer(benutzer: BenutzerListeEintrag | undefined ) {
		if (benutzer?.id === this._state.value.auswahl?.id)
		 	return;
		if ((benutzer === undefined) || (this.mapBenutzer.size === 0)) {
			this.setPatchedDefaultState({
				auswahl: undefined,
				listBenutzer: new ArrayList(),
				mapBenutzer: new Map(),
				benutzerManager: new BenutzerManager(new BenutzerDaten()),
				listBenutzergruppen: new ArrayList(),
				daten: undefined,

			})
			await this.ladeListe();
		}
		const neueAuswahl = benutzer === undefined ? this.firstBenutzer(this.mapBenutzer) : benutzer;
		const daten = await this.ladeBenutzerDaten(neueAuswahl);
		const listBenutzergruppen = await api.server.getBenutzergruppenliste(api.schema);
		const benutzerManager = daten=== undefined ? undefined : new BenutzerManager(daten);
		this.setPatchedState({
			auswahl: neueAuswahl,
			benutzerManager: benutzerManager,
			listBenutzergruppen: listBenutzergruppen,
			daten: daten
		})
	}

	gotoBenutzer = async (value: BenutzerListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeSchuleBenutzer.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchuleBenutzer.selectedChild === undefined) ? routeSchuleBenutzerDaten.name : routeSchuleBenutzer.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

	getBenutzerManager = () => {
		return this._state.value.benutzerManager;
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchule.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für Schule gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): BenutzerListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	set auswahl(value: BenutzerListeEintrag | undefined) {
		this._state.value.auswahl = value;
	}

	get listBenutzer(): List<BenutzerListeEintrag>{
		return this._state.value.listBenutzer;
	}

	set listBenutzer(value: List<BenutzerListeEintrag>) {
		this._state.value.listBenutzer = value;
	}

	get hatDaten(): boolean {
		return this._state.value.daten !== undefined;
	}

	get daten(): BenutzerDaten {
		if(this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	set daten(value: BenutzerDaten | undefined) {
		this._state.value.daten = value;
	}

	get benutzerManager(): BenutzerManager {
		if(this._state.value.benutzerManager === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.benutzerManager
	}

	set benutzerManager(value: BenutzerManager) {
		this._state.value.benutzerManager = value;
	}

	get listBenutzergruppen(): List<BenutzergruppeListeEintrag> {
		if(this._state.value.listBenutzergruppen === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.listBenutzergruppen
	}

	set listBenutzergruppen(value: List<BenutzergruppeListeEintrag>) {
		this._state.value.listBenutzergruppen = value;
	}

	get mapBenutzer(): Map<number, BenutzerListeEintrag> {
		if(this._state.value.mapBenutzer === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.mapBenutzer
	}

	set mapBenutzer(value: Map<number, BenutzerListeEintrag>) {
		this._state.value.mapBenutzer = value;
	}


	/**
	 * Setzt den Anzeigenamen eines Benutzers
	 *
	 * @param {string} anzeigename   der Anzeigename
	 *
	 * @returns {Promise<void>}
	 */
	setAnzeigename = async (anzeigename : string): Promise<void> => {
		if (this.benutzerManager === undefined)
			return;
		await api.server.setAnzeigename(anzeigename, api.schema, this.benutzerManager.getID());
		for (const benutzer of this.listBenutzer)
			if (benutzer.id === this.daten.id)
				benutzer.anzeigename = anzeigename;
		this.benutzerManager.setAnzeigename(anzeigename);
		this.commit();
	}

	/**
	 * Setzt den Benutzernamens für die Anmeldung
	 *
	 * @param {string} anzeigename
	 *
	 * @returns {Promise<void>}
	 */
	setAnmeldename = async (anmeldename: string): Promise<void> => {
		if (this.benutzerManager === undefined)
			return;
		await api.server.setBenutzername(anmeldename, api.schema, this.benutzerManager.getID());
		this.benutzerManager.setAnmeldename(anmeldename);
		const neueAuswahl = this.mapBenutzer.get(this.daten.id);
		this.mapBenutzer.set(this.daten.id,this.daten);
		this.setPatchedState({
			auswahl: neueAuswahl,
			mapBenutzer : this.mapBenutzer,
			benutzerManager : this.benutzerManager
		})
	}

	/**
	 * Setzt, ob der Benutzer ein administrativer Benutzer ist oder nicht
	 *
	 * @param {boolean} istAdmin
	 *
	 * @returns {Promise<void>}
	 */
	setIstAdmin = async (istAdmin: boolean): Promise<void> => {
		if (this.benutzerManager === undefined)
			return;
		if (istAdmin)
			await api.server.addBenutzerAdmin(api.schema, this.benutzerManager.getID());
		else
			await api.server.removeBenutzerAdmin(api.schema, this.benutzerManager.getID());
		this.benutzerManager.setAdmin(istAdmin);
		this.commit();
	}

	/**
	 * Setzt ein neues Kennwort für den aktuell ausgewählten Benutzer
	 *
	 * @passwort das neue Kennwort
	 */
	setPassword = async (passwort : string) => {
		if (this.benutzerManager === undefined)
			return false;
		await api.server.setBenutzerPasswort(passwort, api.schema, this.benutzerManager.getID());
		setTimeout(() => alert("Das Kennwort wurde erfolgreich geändert."), 300);
	}

	/**
	 * Fügt den Benutzer in die Benutzergruppe mit der übergebenen ID ein.
	 * Ist diese ID -1, so wird der Benutzer in alle Gruppen eingefügt.
	 *
	 * @param {number} idGroup   die ID der Benutzergruppe
	 *
	 * @returns {Promise<void>}
	 */
	addBenutzerToBenutzergruppe = async (idGroup: number): Promise<void> => {
		if (this.benutzerManager === undefined)
			return;
		if (idGroup != -1) {
			const bg_ids = new ArrayList<number>();
			bg_ids.add(this.benutzerManager.getID());
			const result = await api.server.addBenutzergruppeBenutzer(bg_ids, api.schema, idGroup);
			this.benutzerManager.addToGruppe(result);
		} else {
			const benutzer_id = new ArrayList<number>();
			benutzer_id.add(this.benutzerManager.getID());
			for (const bg of this.listBenutzergruppen) {
				if (!this.benutzerManager.istInGruppe(bg.id)) {
					const result = await api.server.addBenutzergruppeBenutzer(benutzer_id, api.schema, bg.id);
					this.benutzerManager.addToGruppe(result);
				}
			}
		}
		this.setPatchedState({
			benutzerManager: this._state.value.benutzerManager,
			listBenutzergruppen: this._state.value.listBenutzergruppen
		});
	}

	/**
	 * Entfernt den Benutzer aus der Benutzergruppe mit der übergebenen ID.
	 * Ist diese ID -1, so wird der Benutzer aus allen Gruppen entfernt.
	 *
	 * @param {number} idGroup   die ID der Benutzergruppe
	 *
	 * @returns {Promise<void>}
	 */
	removeBenutzerFromBenutzergruppe = async (idGroup: number): Promise<void> => {
		if (this.benutzerManager === undefined)
			return;
		const ids = new ArrayList<number>();
		ids.add(this.benutzerManager.getID());
		if (idGroup !== -1) {
			const result = await api.server.removeBenutzergruppeBenutzer(ids, api.schema, idGroup);
			this.benutzerManager.removeFromGruppe(result);
		} else {
			for (const eintrag of this.listBenutzergruppen) {
				if (this.benutzerManager.istInGruppe(eintrag.id)) {
					const result = await api.server.removeBenutzergruppeBenutzer(ids, api.schema, eintrag.id);
					this.benutzerManager.removeFromGruppe(result);
				}
			}
		}
		// TODO Durch eine entpsrechende Gruppenmitgliedschaft wird ein Benutzer administrativ und das wird in BenutzerView festgehalt.
		// Die Entfernung dieser Mitgliedschaft wird im BenutzerManager nicht richtig umgesetzt. Die Gruppe wird zwar entfernt, jedoch muss auch im
		// verwalteten Obejkt istAdmin Attribut angepasst werden.
		const daten = await this.ladeBenutzerDaten(this._state.value.daten);
		const benutzerManager = (daten === undefined) ? undefined : new BenutzerManager(daten);
		this.setPatchedState({
			benutzerManager: benutzerManager,
			listBenutzergruppen: this._state.value.listBenutzergruppen
		})
	}

	/**
	 * Fügt die übergebene Kompetenz zu dem Benutzer hinzu
	 *
	 * @param kompetenz   die hinzuzufügende Kompetenz
	 */
	addKompetenz = async (kompetenz : BenutzerKompetenz) => {
		const kid = new ArrayList<number>();
		kid.add(kompetenz.daten.id);
		if (this.benutzerManager === undefined)
			return false;
		if (this.benutzerManager.hatKompetenz(kompetenz))
			return false;
		await api.server.addBenutzerKompetenzen(kid, api.schema, this.benutzerManager.getID());
		this.benutzerManager.addKompetenz(kompetenz);
		this.setPatchedState({
			benutzerManager: this._state.value.benutzerManager,
		})
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenz von diesem Benutzer
	 *
	 * @param kompetenz   die zu entfernende Kompetenz
	 */
	 removeKompetenz = async (kompetenz : BenutzerKompetenz) => {
		const kid = new ArrayList<number>();
		kid.add(kompetenz.daten.id);
		if (this.benutzerManager === undefined)
			return false;
		if (!this.benutzerManager.hatKompetenz(kompetenz))
			return false;
		await api.server.removeBenutzerKompetenzen(kid, api.schema, this.benutzerManager.getID());
		this.benutzerManager.removeKompetenz(kompetenz);
		this.setPatchedState({
			benutzerManager: this._state.value.benutzerManager,
		})
		return true;
	}

	/**
	 * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu diesem Benutzer hinzu
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	 */
	addBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new ArrayList<number>();
		if (this.benutzerManager === undefined)
			return false;
		if (!this.benutzerManager.istAdmin()) {
			//Es werden nur die IDs der Kompetenzen in kids gespreichert, welche dem Benutzer direkt zugordnet sind.
			// Sie sind also nicht über Benutzergruppen vererbt.
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.benutzerManager.getGruppen(komp).size() === 0)
					kids.add(komp.daten.id);
			}
			await api.server.addBenutzerKompetenzen(kids, api.schema, this.benutzerManager.getID());
			//Den obigen Schritten entsprechende Anpassung des Client-Objekts mithilfe des Managers
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.benutzerManager.getGruppen(komp).size() === 0) {
					if (!this.benutzerManager.hatKompetenz(komp))
						this.benutzerManager.addKompetenz(komp);
				}
			}
		}
		this.setPatchedState({
			benutzerManager: this._state.value.benutzerManager,
		})
		return true;
	}

	/**
	 * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von diesem Benutzer
	 *
	 * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	 */
	 removeBenutzerKompetenzGruppe = async (kompetenzgruppe : BenutzerKompetenzGruppe) => {
		const kids = new ArrayList<number>();
		if (this.benutzerManager === undefined)
			return false;
		if (!this.benutzerManager.istAdmin()) {
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
				if (this.benutzerManager.getGruppen(komp).size() === 0)
					kids.add(komp.daten.id);
			await api.server.removeBenutzerKompetenzen(kids, api.schema, this.benutzerManager.getID());
			for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
				if (this.benutzerManager.getGruppen(komp).size() === 0) {
					if (this.benutzerManager.hatKompetenz(komp))
						this.benutzerManager.removeKompetenz(komp);
				}
			}
		}
		this.setPatchedState({
			benutzerManager: this._state.value.benutzerManager,
		})
		return true;
	}

	/**
	 * Erstellt einen neuen Benutzer
	 *
	 * @param anzeigename    der Name des Benutzer für die Anzeige - kann vom Benutzernamen abweichen
	 * @param benutzername   der Name des Benutzers
	 * @param password       das Kennwort des Benutzers
	 */
	createBenutzerAllgemein = async (anzeigename: string, benutzername: string, passwort: string) => {
		const credential = new BenutzerAllgemeinCredentials();
		credential.anzeigename = anzeigename;
		credential.benutzername = benutzername;
		credential.password = passwort;
		const result = await api.server.createBenutzerAllgemein(credential, api.schema);
		const ble = new BenutzerListeEintrag();
		ble.id = result.id;
		ble.anzeigename = result.anzeigename;
		ble.name = result.name;
		ble.istAdmin= result.istAdmin;
		ble.idCredentials = result.idCredentials;
		this.listBenutzer.add(ble);
		this.mapBenutzer.set(ble.id, ble);
		this.auswahl = ble;
		this.commit();
		await this.gotoBenutzer(ble);
	}

	/**
	 * Entfernt die ausgewählten Benutzer
	 */
	deleteBenutzerAllgemein = async (selectedItems: BenutzerListeEintrag[]): Promise<void> => {
		const bids = new ArrayList<number>();
		let auswahl_gewaehlt = false;
		if (this.auswahl !== undefined)
			auswahl_gewaehlt = selectedItems.includes(this.auswahl);
		for (const b of selectedItems)
			bids.add(b.id)
		await api.server.removeBenutzerAllgemein(bids, api.schema);
		for (const i of bids)
			this.mapBenutzer.delete(i);
		// TODO Der Benutzer wird in der Auswahl nicht entfernt, erst beim Reload.
		this.setPatchedState({
			mapBenutzer: this._state.value.mapBenutzer,
		})
		if (auswahl_gewaehlt)
			 await this.gotoBenutzer(this.listBenutzer.get(0));
	}

	/**
	 * Liefert zur einer Kompetenz die Gruppenzugehörigkeiten
	 *
	 * @kompetenz die Kompetenz
	 */
	getGruppen4Kompetenz = (kompetenz : BenutzerKompetenz) : string => {
		if (this.benutzerManager === undefined)
			return "";
		let text = "";
		let i = 0;
		if (this.benutzerManager.getGruppen(kompetenz)) {
			for (const bg of this.benutzerManager.getGruppen(kompetenz)) {
				if (i !== 0)
					text += ", ";
				text += bg.bezeichnung;
				i = -2;
			}
		}
		return text;
	}

}
