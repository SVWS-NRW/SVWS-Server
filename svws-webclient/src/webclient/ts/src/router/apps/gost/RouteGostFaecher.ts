import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
import { GostJahrgang, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeLogin } from "~/router/RouteLogin";

export class RouteDataGostFaecher  {

	item: GostJahrgang | undefined = undefined;
	dataFachkombinationen: DataGostFachkombinationen = new DataGostFachkombinationen();

	patchFachkombination = async (data: Partial<GostJahrgangFachkombination>, id : number) => {
		if (this.dataFachkombinationen.daten === undefined)
			return false;
		await routeLogin.data.api.patchGostFachkombination(data, routeLogin.data.schema, id);
		return true;
	}

	addFachkombination = async (typ: GostLaufbahnplanungFachkombinationTyp) => {
		if ((this.item === undefined) || (this.dataFachkombinationen.daten === undefined))
			return undefined;
		const result = await routeLogin.data.api.addGostAbiturjahrgangFachkombination(routeLogin.data.schema, this.item.abiturjahr, typ.getValue());
		if (result !== undefined)
			this.dataFachkombinationen.daten.add(result);
		return result;
	}

	removeFachkombination = async (id: number) => {
		const result = await routeLogin.data.api.deleteGostFachkombination(routeLogin.data.schema, id);
		if ((result !== undefined) && (this.dataFachkombinationen.daten !== undefined))
			for (let i : number = this.dataFachkombinationen.daten.size() - 1; i >= 0; i--) {
				const element = this.dataFachkombinationen.daten.get(i);
				if (element.id === id) {
					this.dataFachkombinationen.daten.remove(element);
					break;
				}
			}
		return result;
	}

}

const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<RouteDataGostFaecher, RouteGost> {

	public constructor() {
		super("gost.faecher", "faecher", SGostFaecher, new RouteDataGostFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.abiturjahr as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.abiturjahr === tmp));
		}
	}

	protected async onSelect(item?: GostJahrgang) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.dataFachkombinationen.unselect();
		} else {
			this.data.item = item;
			await this.data.dataFachkombinationen.select(this.data.item);
		}
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patchFach: routeGost.data.patchFach,
			patchFachkombination: this.data.patchFachkombination,
			addFachkombination: this.data.addFachkombination,
			removeFachkombination: this.data.removeFachkombination,
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			item: routeGost.data.item.value,
			schule: routeGost.data.schule,
			jahrgangsdaten: routeGost.data.jahrgangsdaten.value,
			faecherManager: routeGost.data.faecherManager,
			listJahrgaenge: routeGost.data.listJahrgaenge,
			dataFachkombinationen: this.data.dataFachkombinationen
		};
	}

}

export const routeGostFaecher = new RouteGostFaecher();
