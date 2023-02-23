import { GostJahrgang, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp, Schulform } from "@svws-nrw/svws-core-ts";
import { Ref, ref } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { api } from "~/router/Api";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { RouteNode } from "~/router/RouteNode";

export class RouteDataGostFaecher  {

	item: GostJahrgang | undefined = undefined;
	private _mapFachkombinationen: Ref<Map<number, GostJahrgangFachkombination> | undefined> = ref(undefined);

	public async onSelect(item?: GostJahrgang) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
			this._mapFachkombinationen.value = undefined;
		} else {
			this.item = item;
			await this.ladeFachkombinationen(item);
		}
	}

	public get mapFachkombinationen() : Map<number, GostJahrgangFachkombination> {
		if (this._mapFachkombinationen.value === undefined)
			throw new Error("Zugriff auf die Fachkombinationen, bevor diese geladen wurden.");
		return this._mapFachkombinationen.value;
	}

	public async ladeFachkombinationen(item: GostJahrgang) {
		const listfachkombinationen = await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, item.abiturjahr);
		const mapFachkombinationen = new Map<number, GostJahrgangFachkombination>();
		for (const fk of listfachkombinationen)
			mapFachkombinationen.set(fk.id, fk);
		this._mapFachkombinationen.value = mapFachkombinationen;
	}

	patchFachkombination = async (data: Partial<GostJahrgangFachkombination>, id : number) => {
		const kombi = this.mapFachkombinationen.get(id);
		if (kombi === undefined)
			throw new Error("Änderungen an der Fachkombination mit der ID " + id + " nicht möglich, da eine solche Fachkombination nicht bekannt ist.");
		await api.server.patchGostFachkombination(data, api.schema, id);
		Object.assign(kombi, data);
		return true;
	}

	addFachkombination = async (typ: GostLaufbahnplanungFachkombinationTyp) => {
		if (this.item === undefined)
			return undefined;
		const result = await api.server.addGostAbiturjahrgangFachkombination(api.schema, this.item.abiturjahr, typ.getValue());
		if (result !== undefined)
			this.mapFachkombinationen.set(result.id, result);
		return result;
	}

	removeFachkombination = async (id: number) => {
		const result = await api.server.deleteGostFachkombination(api.schema, id);
		if (result !== undefined)
			this.mapFachkombinationen.delete(id);
		return result;
	}

}

const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<RouteDataGostFaecher, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), "gost.faecher", "faecher", SGostFaecher, new RouteDataGostFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.abiturjahr as string);
			await this.data.onSelect(this.parent!.liste.liste.find(s => s.abiturjahr === tmp));
		}
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			getFaecherManager: routeGost.data.getFaecherManager,
			patchFach: routeGost.data.patchFach,
			patchFachkombination: this.data.patchFachkombination,
			addFachkombination: this.data.addFachkombination,
			removeFachkombination: this.data.removeFachkombination,
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: routeGost.data.jahrgangsdaten.value,
			mapFachkombinationen: this.data.mapFachkombinationen
		};
	}

}

export const routeGostFaecher = new RouteGostFaecher();
