import type { GostJahrgang, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp} from "@svws-nrw/svws-core";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import type { Ref} from "vue";
import { ref } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostFaecherProps } from "~/components/gost/faecher/SGostFaecherProps";
import { api } from "~/router/Api";
import type { RouteGost} from "~/router/apps/RouteGost";
import { routeGost } from "~/router/apps/RouteGost";
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
		if (to_params.abiturjahr === undefined) {
			await this.data.onSelect(undefined);
			return routeGost.getRoute();
		}
		const id = parseInt(to_params.abiturjahr);
		await this.data.onSelect(this.parent.data.mapAbiturjahrgaenge.get(id));
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostFaecherProps {
		return {
			faecherManager: routeGost.data.faecherManager,
			patchFach: routeGost.data.patchFach,
			patchFachkombination: this.data.patchFachkombination,
			addFachkombination: this.data.addFachkombination,
			removeFachkombination: this.data.removeFachkombination,
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: routeGost.data.jahrgangsdaten,
			mapFachkombinationen: this.data.mapFachkombinationen
		};
	}

}

export const routeGostFaecher = new RouteGostFaecher();
