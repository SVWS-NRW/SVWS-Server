import type { BetriebAnsprechpartner, BetriebStammdaten, KatalogEintrag } from "@core";
import { ArrayList, BetriebListeEintrag, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeSchuleBetriebe } from "./RouteSchuleBetriebe";
import { routeSchuleBetriebeDaten } from "./RouteSchuleBetriebeDaten";

interface RouteStateSchuleBetriebe extends RouteStateInterface {
	auswahl: BetriebListeEintrag | undefined;
	daten: BetriebStammdaten | undefined;
	mapKatalogeintraege: Map<number,BetriebListeEintrag>;
	mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
}

const defaultState = <RouteStateSchuleBetriebe> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	mapAnsprechpartner: new Map(),
	mapBeschaeftigungsarten: new Map(),
	view: routeSchuleBetriebeDaten,
};


export class RouteDataSchuleBetriebe extends RouteData<RouteStateSchuleBetriebe> {

	public constructor() {
		super(defaultState);
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
			throw new DeveloperNotificationException("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getBetriebe(api.schema);
		const mapKatalogeintraege = new Map();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);

		const listBeschaeftigungsarten = await api.server.getKatalogBetriebsart(api.schema);
		const mapBeschaeftigungsarten = new Map();
		for (const l of listBeschaeftigungsarten)
			mapBeschaeftigungsarten.set(l.id, l)

		const mapAnsprechpartner = new Map();
		if (auswahl !== undefined) {
			const listAnsprechpartner = await api.server.getBetriebAnsprechpartner(api.schema, auswahl.id);
			for( const l of listAnsprechpartner)
				mapAnsprechpartner.set(l.id, l);
		}

		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, mapAnsprechpartner, mapBeschaeftigungsarten })
	}

	setEintrag = async (auswahl: BetriebListeEintrag) => {
		const daten = await api.server.getBetriebStammdaten(api.schema, auswahl.id)
		const listAnsprechpartner = await api.server.getBetriebAnsprechpartner(api.schema, auswahl.id);
		const mapAnsprechpartner = new Map();
		for( const l of listAnsprechpartner)
			mapAnsprechpartner.set(l.id, l);
		this.setPatchedState({ auswahl, daten , mapAnsprechpartner})
	}

	gotoEintrag = async (eintrag: BetriebListeEintrag | undefined) => {
		await RouteManager.doRoute(routeSchuleBetriebe.getRoute({ id: eintrag?.id }));
	}

	addEintrag =async (eintrag: BetriebStammdaten) => {
		const res = await api.server.createBetrieb(eintrag, api.schema);
		const auswahl = new BetriebListeEintrag();
		auswahl.name1 = res.name1;
		auswahl.id = res.id;
		auswahl.adressArt = res.adressArt;
		auswahl.branche = res.branche;
		this.mapKatalogeintraege.set(auswahl.id,auswahl);
		const mapKatalogeintraege = new Map();
		this.mapKatalogeintraege.forEach((value) => mapKatalogeintraege.set(value.id, value));
		const mapAnsprechpartner = new Map();
		const listAnsprechpartner = await api.server.getBetriebAnsprechpartner(api.schema,auswahl.id);
		for( const l of listAnsprechpartner)
			mapAnsprechpartner.set(l.id, l);
		this.setPatchedState({ mapKatalogeintraege, mapAnsprechpartner, auswahl });
		await this.gotoEintrag(auswahl);
	}

	patch = async (data : Partial<BetriebStammdaten>) => {
		await api.server.patchBetriebStammdaten(data, api.schema, this.daten.id)
		const daten = await api.server.getBetriebStammdaten(api.schema, this.daten.id)
		const auswahl = new BetriebListeEintrag();
		auswahl.name1 = daten.name1;
		auswahl.id = daten.id;
		auswahl.adressArt = daten.adressArt;
		auswahl.branche = daten.branche;
		this.mapKatalogeintraege.set(auswahl.id, auswahl);
		const mapKatalogeintraege = new Map();
		this.mapKatalogeintraege.forEach((value) => mapKatalogeintraege.set(value.id, value));
		this.setPatchedState({ daten, auswahl, mapKatalogeintraege })
	}

	deleteEintraege = async (eintraege: BetriebListeEintrag[]) => {
		const bids = new ArrayList<number>();
		let istAuswahlMitGeloescht : boolean = false;
		for ( const betrieb of eintraege)
			bids.add(betrieb.id);
		await api.server.removeBetrieb(bids, api.schema);
		const mapKatalogeintraege = new Map();
		for ( const betrieb of eintraege) {
			this.mapKatalogeintraege.delete(betrieb.id);
			if( this.auswahl)
				if(this.auswahl.id === betrieb.id)
					istAuswahlMitGeloescht = true;
		}
		const auswahl = istAuswahlMitGeloescht ? this.mapKatalogeintraege.values().next().value : this.auswahl;
		this.mapKatalogeintraege.forEach((value) => mapKatalogeintraege.set(value.id, value));
		this.setPatchedState({ mapKatalogeintraege, auswahl });
		await this.gotoEintrag(auswahl);
	}

	patchBetriebAnsprechpartner = async (data : Partial<BetriebAnsprechpartner>, id: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException('Kein gültiger Betreib ausgewählt');
		await api.server.patchBetriebanpsrechpartnerdaten(data,api.schema,id);
	}

	addBetriebAnsprechpartner = async (data : BetriebAnsprechpartner) => {
		const id = this._state.value.auswahl?.id;
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
