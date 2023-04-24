import type { GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp} from "@svws-nrw/svws-core";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { shallowRef} from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostFaecherProps } from "~/components/gost/faecher/SGostFaecherProps";
import { api } from "~/router/Api";
import type { RouteGost} from "~/router/apps/RouteGost";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteNode } from "~/router/RouteNode";

interface RouteStateDataGostFaecher {
	abiturjahr: number | undefined;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination> | undefined;
}
export class RouteDataGostFaecher  {

	private static _defaultState: RouteStateDataGostFaecher = {
		abiturjahr: undefined,
		mapFachkombinationen: undefined,
	}

	private _state = shallowRef(RouteDataGostFaecher._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostFaecher>) {
		this._state.value = Object.assign({ ... RouteDataGostFaecher._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostFaecher>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}
	get abiturjahr(): number {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Unerwarteter Fehler: Jahrgang nicht festgelegt, es können keine Informationen zu den Fächern abgerufen oder eingegeben werden.");
		return this._state.value.abiturjahr;
	}

	public get mapFachkombinationen() : Map<number, GostJahrgangFachkombination> {
		if (this._state.value.mapFachkombinationen === undefined)
			throw new Error("Zugriff auf die Fachkombinationen, bevor diese geladen wurden.");
		return this._state.value.mapFachkombinationen;
	}

	public async setEintrag(abiturjahr?: number) {
		if (abiturjahr === this._state.value.abiturjahr)
			return;
		let mapFachkombinationen = undefined;
		if (abiturjahr !== undefined) {
			const listFachkombinationen = await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, abiturjahr);
			mapFachkombinationen = new Map<number, GostJahrgangFachkombination>();
			for (const fk of listFachkombinationen)
				mapFachkombinationen.set(fk.id, fk);
		}
		this.setPatchedState({abiturjahr, mapFachkombinationen})
	}

	patchFachkombination = async (data: Partial<GostJahrgangFachkombination>, id : number) => {
		const mapFachkombinationen = this.mapFachkombinationen;
		const kombi = mapFachkombinationen.get(id);
		if (kombi === undefined)
			throw new Error("Änderungen an der Fachkombination mit der ID " + id + " nicht möglich, da eine solche Fachkombination nicht bekannt ist.");
		await api.server.patchGostFachkombination(data, api.schema, id);
		Object.assign(kombi, data);
		mapFachkombinationen.set(kombi.id, kombi);
		this.setPatchedState({mapFachkombinationen});
		return true;
	}

	addFachkombination = async (typ: GostLaufbahnplanungFachkombinationTyp) => {
		if (this._state.value.abiturjahr === undefined)
			return undefined;
		const result = await api.server.addGostAbiturjahrgangFachkombination(api.schema, this.abiturjahr, typ.getValue());
		const mapFachkombinationen = this.mapFachkombinationen;
		if (result !== undefined)
			mapFachkombinationen.set(result.id, result);
		this.setPatchedState({mapFachkombinationen});
		return result;
	}

	removeFachkombination = async (id: number) => {
		const result = await api.server.deleteGostFachkombination(api.schema, id);
		const mapFachkombinationen = this.mapFachkombinationen;
		if (result !== undefined)
			mapFachkombinationen.delete(id);
		this.setPatchedState({mapFachkombinationen});
		return result;
	}

}

const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<RouteDataGostFaecher, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.faecher", "faecher", SGostFaecher, new RouteDataGostFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		if (abiturjahr === undefined)
			return routeGost.getRoute();
		return true;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		const id = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		await this.data.setEintrag(id);
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostFaecherProps {
		return {
			faecherManager: () => routeGost.data.faecherManager,
			patchFach: routeGost.data.patchFach,
			patchFachkombination: this.data.patchFachkombination,
			addFachkombination: this.data.addFachkombination,
			removeFachkombination: this.data.removeFachkombination,
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: routeGost.data.jahrgangsdaten,
			mapFachkombinationen: () => this.data.mapFachkombinationen
		};
	}

}

export const routeGostFaecher = new RouteGostFaecher();
