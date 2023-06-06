import type { BetriebAnsprechpartner, BetriebListeEintrag, BetriebStammdaten, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten} from "@core";
import { BenutzerKompetenz, Schulform, ArrayList } from "@core";
import { shallowRef} from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerAdressenProps } from "~/components/schueler/adressen/SSChuelerAdressenProps";
import { api } from "~/router/Api";
import type { RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";

const SSchuelerAdressen = () => import("~/components/schueler/adressen/SSchuelerAdressen.vue");

interface RouteStateDataSchuelerAdressen {
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
export class RouteDataSchuelerAdressen {

	private static _defaultState: RouteStateDataSchuelerAdressen = {
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

	private _state = shallowRef(RouteDataSchuelerAdressen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerAdressen>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerAdressen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerAdressen>) {
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
		const listAnsprechpartner = await api.server.getBetriebAnsprechpartner(api.schema);
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

export class RouteSchuelerAdressen extends RouteNode<RouteDataSchuelerAdressen, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.ausbildungsbetriebe", "ausbildungsbetriebe", SSchuelerAdressen, new RouteDataSchuelerAdressen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ausbildungsbetriebe";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = to_params.id === undefined ? undefined : parseInt(to_params.id);
		await this.data.setSchueler(id);
		await this.data.setSchuelerBetrieb();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAdressenProps {
		return {
			patchBetrieb: this.data.patchBetrieb,
			patchSchuelerBetriebsdaten: this.data.patchSchuelerBetriebsdaten,
			patchAnsprechpartner: this.data.patchAnsprechpartner,
			setSchuelerBetrieb: this.data.setSchuelerBetrieb,
			createAnsprechpartner: this.data.createAnsprechpartner,
			createSchuelerBetriebsdaten: this.data.createSchuelerBetriebsdaten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			idSchueler: routeSchueler.data.stammdaten.id,
			listSchuelerbetriebe: this.data.listSchuelerbetriebe,
			betrieb: this.data.betrieb,
			betriebsStammdaten: this.data.daten,
			mapBeschaeftigungsarten: this.data.mapBeschaeftigungsarten,
			mapLehrer: this.data.mapLehrer,
			mapBetriebe: this.data.mapBetriebe,
			mapAnsprechpartner: this.data.mapAnsprechpartner,
		};
	}

}

export const routeSchuelerAdressen = new RouteSchuelerAdressen();

