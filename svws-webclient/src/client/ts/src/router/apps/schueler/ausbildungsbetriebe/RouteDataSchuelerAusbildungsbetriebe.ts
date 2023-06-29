import { shallowRef} from "vue";

import type { BetriebAnsprechpartner, BetriebListeEintrag, BetriebStammdaten, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten} from "@core";
import { ArrayList } from "@core";

import { api } from "~/router/Api";


interface RouteStateDataSchuelerAusbildungsbetriebe {
	daten: BetriebStammdaten | undefined;
	idSchueler: number | undefined;
	betrieb: SchuelerBetriebsdaten | undefined;
	listSchuelerbetriebe : List<SchuelerBetriebsdaten>;
	mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapBetriebe: Map<number, BetriebListeEintrag>;
	listAnsprechpartner: List<BetriebAnsprechpartner>;
	mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
}

export class RouteDataSchuelerAusbildungsbetriebe {

	private static _defaultState: RouteStateDataSchuelerAusbildungsbetriebe = {
		daten: undefined,
		idSchueler: undefined,
		betrieb: undefined,
		listSchuelerbetriebe : new ArrayList(),
		mapBeschaeftigungsarten: new Map(),
		mapLehrer: new Map(),
		mapBetriebe: new Map(),
		listAnsprechpartner: new ArrayList(),
		mapAnsprechpartner: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerAusbildungsbetriebe._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerAusbildungsbetriebe>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerAusbildungsbetriebe._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerAusbildungsbetriebe>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get daten(): BetriebStammdaten | undefined {
		return this._state.value.daten;
	}

	get listSchuelerbetriebe(): List<SchuelerBetriebsdaten> {
		return this._state.value.listSchuelerbetriebe;
	}

	get mapBeschaeftigungsarten(): Map<number, KatalogEintrag> {
		return this._state.value.mapBeschaeftigungsarten;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapBetriebe(): Map<number, BetriebListeEintrag> {
		return this._state.value.mapBetriebe;
	}

	get listAnsprechpartner(): List<BetriebAnsprechpartner> {
		return this._state.value.listAnsprechpartner;
	}

	get mapAnsprechpartner(): Map<number, BetriebAnsprechpartner> {
		return this._state.value.mapAnsprechpartner;
	}

	get betrieb(): SchuelerBetriebsdaten | undefined {
		return this._state.value.betrieb;
	}

	public async ladeListe() {
		const listBeschaeftigungsarten = await api.server.getKatalogBeschaeftigungsart(api.schema);
		const mapBeschaeftigungsarten = new Map<number, KatalogEintrag>();
		for (const ba of listBeschaeftigungsarten)
			mapBeschaeftigungsarten.set(ba.id, ba);
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		const listBetriebe = await api.server.getBetriebe(api.schema);
		const mapBetriebe = new Map<number, BetriebListeEintrag>();
		for (const b of listBetriebe)
			mapBetriebe.set(b.id, b);
		const listAnsprechpartner = await api.server.getBetriebeAnsprechpartner(api.schema);
		this.setPatchedDefaultState({mapBeschaeftigungsarten, mapLehrer, mapBetriebe, listAnsprechpartner});
	}

	public async setSchueler(idSchueler?: number) {
		if (idSchueler === undefined || idSchueler === this._state.value.idSchueler)
			return;
		const listSchuelerbetriebe = await api.server.getSchuelerBetriebe(api.schema, idSchueler);
		this.setPatchedState({idSchueler, listSchuelerbetriebe});
	}

	public setSchuelerBetrieb = async (betriebsdaten?: SchuelerBetriebsdaten) => {
		let betrieb, daten;
		const mapAnsprechpartner = new Map();
		if (this.listSchuelerbetriebe.size() > 0) {
			betrieb = betriebsdaten || this.listSchuelerbetriebe.get(0);
			daten = await api.server.getBetriebStammdaten(api.schema, betrieb.betrieb_id);
			console.log(betrieb, this.listAnsprechpartner)
			for (const a of this.listAnsprechpartner)
				if (a.betrieb_id === betrieb.betrieb_id)
					mapAnsprechpartner.set(a.id, a);
		}
		this.setPatchedState({daten, betrieb, mapAnsprechpartner});
	}

	patchBetrieb = async (data : Partial<BetriebStammdaten>, id : number) => {
		await api.server.patchBetriebStammdaten(data, api.schema, id);
	}

	patchSchuelerBetriebsdaten = async (data : Partial<SchuelerBetriebsdaten>, id : number) => {
		await api.server.patchSchuelerBetriebsdaten(data, api.schema, id);
	}

	patchAnsprechpartner = async (data : Partial<BetriebAnsprechpartner>, id : number) => {
		await api.server.patchBetriebanpsrechpartnerdaten(data, api.schema, id);
	}

	createAnsprechpartner = async (data: BetriebAnsprechpartner) => {
		if (this.daten === undefined)
			throw new Error("Es ist kein gültiger Betrieb für das Anlegen eines Ansprechpartners ausgewählt.")
		const ansprechpartner = await api.server.createBetriebansprechpartner(data, api.schema, this.daten.id);
		const listAnsprechpartner = this.listAnsprechpartner;
		listAnsprechpartner.add(ansprechpartner);
		const mapAnsprechpartner = this.mapAnsprechpartner;
		mapAnsprechpartner.set(ansprechpartner.id, ansprechpartner);
		this.setPatchedState({listAnsprechpartner, mapAnsprechpartner})
	}

	createSchuelerBetriebsdaten = async (data: SchuelerBetriebsdaten) => {
		const betrieb = await api.server.createSchuelerbetrieb(data, api.schema, data.schueler_id, data.betrieb_id);
		const listSchuelerbetriebe = this.listSchuelerbetriebe;
		listSchuelerbetriebe.add(betrieb);
		const mapAnsprechpartner = new Map();
		this.setPatchedState({listSchuelerbetriebe, mapAnsprechpartner})
	}
}

