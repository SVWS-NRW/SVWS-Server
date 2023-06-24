import type { GostStatistikFachwahl, List} from "@core";
import { BenutzerKompetenz, Schulform, ArrayList, ServerMode } from "@core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostFachwahlenProps } from "~/components/gost/fachwahlen/SGostFachwahlenProps";
import { api } from "~/router/Api";
import type { RouteGost} from "~/router/apps/RouteGost";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteNode } from "~/router/RouteNode";


interface RouteStateDataGostFachwahlen {
	fachwahlen: List<GostStatistikFachwahl>;
}
export class RouteDataGostFachwahlen  {

	private static _defaultState: RouteStateDataGostFachwahlen = {
		fachwahlen: new ArrayList<GostStatistikFachwahl>(),
	}

	private _state = shallowRef(RouteDataGostFachwahlen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostFachwahlen>) {
		this._state.value = Object.assign({ ... RouteDataGostFachwahlen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostFachwahlen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public get fachwahlen(): List<GostStatistikFachwahl> {
		return this._state.value.fachwahlen;
	}

	public async setEintrag(abiturjahr?: number) {
		const fachwahlen = await api.server.getGostAbiturjahrgangFachwahlstatistik(api.schema, abiturjahr || -1);
		this.setPatchedState({fachwahlen})
	}

}

const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<RouteDataGostFachwahlen, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen", "fachwahlen", SGostFachwahlen, new RouteDataGostFachwahlen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
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

	public getProps(to: RouteLocationNormalized): GostFachwahlenProps {
		return {
			fachwahlen: this.data.fachwahlen,
		};
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
