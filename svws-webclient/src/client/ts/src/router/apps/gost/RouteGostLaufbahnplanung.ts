import type { List} from "@svws-nrw/svws-core";
import { BenutzerKompetenz, Schulform, ArrayList } from "@svws-nrw/svws-core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ConfigElement } from "~/components/Config";
import type { GostLaufbahnplanungProps } from "~/components/gost/laufbahnplanung/SGostLaufbahnplanungProps";
import { api } from "~/router/Api";
import type { RouteGost} from "~/router/apps/RouteGost";
import { RouteNode } from "~/router/RouteNode";


interface RouteStateDataGostLaufbahnplanung {
	laufbahnplanungsergebnisse: List<any>;
}
export class RouteDataGostLaufbahnplanung  {

	private static _defaultState: RouteStateDataGostLaufbahnplanung = {
		laufbahnplanungsergebnisse: new ArrayList(),
	}

	private _state = shallowRef(RouteDataGostLaufbahnplanung._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostLaufbahnplanung>) {
		this._state.value = Object.assign({ ... RouteDataGostLaufbahnplanung._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostLaufbahnplanung>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get laufbahnplanungsergebnisse(): List<any> {
		return this._state.value.laufbahnplanungsergebnisse;
	}

	public async setEintrag(id?: number) {
		//
	}

	public async ladeListe() {
		// const listlaufbahnplanungsergebnisseSchueler = await api.server.
		// this.setPatchedDefaultState({laufbahnplanungsergebnisse});
	}
}

const SGostLaufbahnplanung = () => import("~/components/gost/laufbahnplanung/SGostLaufbahnplanung.vue");

export class RouteGostLaufbahnplanung extends RouteNode<RouteDataGostLaufbahnplanung, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.laufbahnplanung", "laufbahnplanung", SGostLaufbahnplanung, new RouteDataGostLaufbahnplanung());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		api.config.addElements([
			new ConfigElement("gost.laufbahnplanung.filterFehler", "user", "true")
		]);
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? undefined : parseInt(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to.name === this.name) {
			if (this.parent === undefined)
				throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			const abiturjahr = parseInt(to_params.abiturjahr);
			if (abiturjahr === undefined || abiturjahr === -1)
				return { name: this.parent.defaultChild!.name, params: { abiturjahr: this.parent.data.mapAbiturjahrgaenge.values().next().value.abiturjahr }};
		}
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		await this.data.setEintrag(abiturjahr);
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostLaufbahnplanungProps {
		return {
			config: api.config,
			laufbahnplanungsergebnisse: this.data.laufbahnplanungsergebnisse,
		};
	}

}

export const routeGostLaufbahnplanung = new RouteGostLaufbahnplanung();
