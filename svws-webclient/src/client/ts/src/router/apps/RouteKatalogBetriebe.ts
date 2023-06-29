import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { BetriebStammdaten , BetriebAnsprechpartner, KatalogEintrag } from "@core";
import { ArrayList, BetriebListeEintrag, DeveloperNotificationException, ServerMode, Schulform, BenutzerKompetenz } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/RouteKataloge";
import { routeKatalogBetriebeDaten } from "~/router/apps/betriebe/RouteKatalogBetriebeDaten";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { BetriebeAppProps } from "~/components/kataloge/betriebe/SBetriebeAppProps";
import type { BetriebeAuswahlProps } from "~/components/kataloge/betriebe/SBetriebeAuswahlProps";

interface RouteStateKatalogBetriebe{
    auswahl: BetriebListeEintrag | undefined;
    daten: BetriebStammdaten | undefined;
	mapKatalogeintraege: Map<number,BetriebListeEintrag>;
	mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
    view: RouteNode<any,any>;
}

export class RouteDataKatalogBetriebe {
	private static _defaultState: RouteStateKatalogBetriebe = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		mapAnsprechpartner: new Map(),
		mapBeschaeftigungsarten: new Map(),
		view: routeKatalogBetriebeDaten,
	}
	private _state = shallowRef(RouteDataKatalogBetriebe._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteDataKatalogBetriebe>) {
		this._state.value = Object.assign({ ... RouteDataKatalogBetriebe._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteDataKatalogBetriebe>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogBetriebe.children.includes(view))
			this.setPatchedState({ view: view})
		else
			throw new Error("Diese für die Fächer gewählte Ansicht wird nicht unterstütz.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): BetriebListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapAnsprechpartner(): Map<number, BetriebAnsprechpartner>{
		return this._state.value.mapAnsprechpartner;
	}

	get mapKatalogeintraege(): Map<number, BetriebListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}
	get mapBeschaeftigungsarten(): Map<number, KatalogEintrag> {
		return this._state.value.mapBeschaeftigungsarten;
	}

	get daten(): BetriebStammdaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getBetriebe(api.schema);
		const mapKatalogeintraege = new Map<number, BetriebListeEintrag>;
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);

		const listBeschaeftigungsarten = await api.server.getKatalogBetriebsart(api.schema);
		const mapBeschaeftigungsarten = new Map<number, KatalogEintrag>();
		for (const l of listBeschaeftigungsarten)
			mapBeschaeftigungsarten.set(l.id, l)

		const listAnsprechpartner = await api.server.getBetriebAnsprechpartner(api.schema, auswahl?.id);
		const mapAnsprechpartner = new Map<number, BetriebAnsprechpartner>();
		for( const l of listAnsprechpartner)
			mapAnsprechpartner.set(l.id, l);

		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, mapAnsprechpartner, mapBeschaeftigungsarten })
	}

	setEintrag = async (auswahl: BetriebListeEintrag) => {
		const daten = await api.server.getBetriebStammdaten(api.schema, auswahl.id)
		const listAnsprechpartner = await api.server.getBetriebAnsprechpartner(api.schema, auswahl?.id);
		const mapAnsprechpartner = new Map<number, BetriebAnsprechpartner>();
		for( const l of listAnsprechpartner)
			mapAnsprechpartner.set(l.id, l);
		this.setPatchedState({ auswahl, daten , mapAnsprechpartner})
	}

	gotoEintrag = async (eintrag: BetriebListeEintrag) => {
		await RouteManager.doRoute(routeKatalogBetriebe.getRoute(eintrag.id));
	}

	addEintrag =async (eintrag: BetriebStammdaten) => {
		const res = await api.server.createBetrieb(eintrag, api.schema);
		const ble = new BetriebListeEintrag();
		ble.name1 = res.name1;
		ble.id = res.id;
		ble.adressArt = res.adressArt;
		ble.branche = res.branche;
		this.mapKatalogeintraege.set(ble.id,ble);
		const mapKT:Map<number, BetriebListeEintrag> = new Map<number, BetriebListeEintrag>();
		this.mapKatalogeintraege.forEach((value) => mapKT.set(value.id, value));
		const mapAnsprechpartner = new Map<number, BetriebAnsprechpartner>();
		const listAnsprechpartner = await api.server.getBetriebAnsprechpartner(api.schema,ble.id);
		for( const l of listAnsprechpartner)
			mapAnsprechpartner.set(l.id, l);
		this.setPatchedState({
			mapKatalogeintraege : mapKT,
			mapAnsprechpartner,
			auswahl: ble });
		await this.gotoEintrag(ble);
	}

	patch = async (data : Partial<BetriebStammdaten>) => {
		if(this.daten === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.")
		await api.server.patchBetriebStammdaten(data, api.schema, this.daten.id)
		const daten = await api.server.getBetriebStammdaten(api.schema, this.daten.id)
		const ble = new BetriebListeEintrag();
		ble.name1 = daten.name1;
		ble.id = daten.id;
		ble.adressArt = daten.adressArt;
		ble.branche = daten.branche;
		this.mapKatalogeintraege.set(ble.id, ble);
		const mapKT:Map<number, BetriebListeEintrag> = new Map<number, BetriebListeEintrag>();
		this.mapKatalogeintraege.forEach((value) => mapKT.set(value.id, value));
		this.setPatchedState({
			daten, auswahl: ble, mapKatalogeintraege : mapKT
		 })
	}

	deleteEintraege = async (eintraege: BetriebListeEintrag[]) => {
		const bids = new ArrayList<number>();
		let istAuswahlMitGeloescht : boolean  = false;
		for ( const betrieb of eintraege){
			bids.add(betrieb.id);
		}
		await api.server.removeBetrieb(bids, api.schema);
		const mapKT:Map<number, BetriebListeEintrag> = new Map<number, BetriebListeEintrag>();
		for ( const betrieb of eintraege){
			this.mapKatalogeintraege.delete(betrieb.id);
			if( this.auswahl){
				if(this.auswahl.id === betrieb.id)
					istAuswahlMitGeloescht = true;
			}
		}
		const _auswahl : BetriebListeEintrag = istAuswahlMitGeloescht ? this.mapKatalogeintraege.values().next().value : this.auswahl;
		this.mapKatalogeintraege.forEach((value) => mapKT.set(value.id, value));
		this.setPatchedState({ mapKatalogeintraege : mapKT , auswahl : _auswahl});
		await this.gotoEintrag(_auswahl);
	}

	patchBetriebAnsprechpartner = async (data : Partial<BetriebAnsprechpartner>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Betreib ausgewählt');
		await api.server.patchBetriebanpsrechpartnerdaten(data,api.schema,id);
	}

	addBetriebAnsprechpartner = async (data : BetriebAnsprechpartner) => {
		const id = this._state.value.auswahl?.id;
		console.log(id);
		if (id === undefined)
			throw new DeveloperNotificationException('Kein gültiger Betrieb ausgewählt');
		data.betrieb_id = id;
		const ansprechpartner = await api.server.createBetriebansprechpartner(data,api.schema, id);
		const mapAnsprechpartner = new Map<number, BetriebAnsprechpartner>();
		this.mapAnsprechpartner.forEach((value) => mapAnsprechpartner.set(value.id, value));
		mapAnsprechpartner.set(ansprechpartner.id, ansprechpartner);
		this.setPatchedState({ mapAnsprechpartner })
	}

	removeBetriebAnsprechpartner = async (daten : BetriebAnsprechpartner[]) => {
		const bids = new ArrayList<number>();
		for ( const ansprechpartner of daten){
			bids.add(ansprechpartner.id);
		}
		await api.server.removeBetriebansprechpartner(bids, api.schema);
		const mapAnsprechpartner = new Map<number, BetriebAnsprechpartner>();
		for ( const ansprechpartner of daten){
			this.mapAnsprechpartner.delete(ansprechpartner.id);
		}
		this.mapAnsprechpartner.forEach((value) => mapAnsprechpartner.set(value.id, value));
		this.setPatchedState({ mapAnsprechpartner })
	}
}

const SBetriebeAuswahl = () => import("~/components/kataloge/betriebe/SBetriebeAuswahl.vue")
const SBetriebeApp = () => import("~/components/kataloge/betriebe/SBetriebeApp.vue")

export class RouteKatalogBetriebe extends RouteNode<RouteDataKatalogBetriebe, RouteApp>{

	public constructor(){
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.betriebe", "/kataloge/betriebe/:id(\\d+)?", SBetriebeApp, new RouteDataKatalogBetriebe());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text="Betriebe";
		super.setView("liste", SBetriebeAuswahl, (route) => this.getAuswahlProps(route));

		super.children = [
			routeKatalogBetriebeDaten
		];
		super.defaultChild = routeKatalogBetriebeDaten;

	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		console.log("ente_routekatalogtest")
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		console.log("update_routekatalogtest");
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: BetriebListeEintrag | undefined;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.mapKatalogeintraege.get(0);
			return this.getRoute(eintrag?.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined) {
				return;
			}
		}
		if (eintrag !== undefined)
			await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BetriebeAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			mapBeschaeftigungsarten: this.data.mapBeschaeftigungsarten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			gotoEintrag: this.data.gotoEintrag,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): BetriebeAppProps {
		return {
			auswahl: this.data.auswahl,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
		await this.data.setView(node);
	}


}
export const routeKatalogBetriebe = new RouteKatalogBetriebe();