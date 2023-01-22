import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";

export class RouteDataGostKursplanung  {
	item: GostJahrgang | undefined = undefined;
	dataFachkombinationen: DataGostFachkombinationen = new DataGostFachkombinationen();
}

const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<RouteDataGostKursplanung, RouteGost> {

	public constructor() {
		super("gost_faecher", "faecher", SGostFaecher, new RouteDataGostKursplanung());
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

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeGost.getProps(to),
			dataFachkombinationen: this.data.dataFachkombinationen
		};
	}

}

export const routeGostFaecher = new RouteGostFaecher();
