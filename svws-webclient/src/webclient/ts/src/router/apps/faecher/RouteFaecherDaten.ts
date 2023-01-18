import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { DataFach } from "~/apps/faecher/DataFach";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFaecher, RouteKatalogFaecher } from "../RouteKatalogFaecher";

export class RouteDataFaecherDaten {
	item: FaecherListeEintrag | undefined = undefined;
	daten: DataFach = new DataFach();
}

const SFachDaten = () => import("~/components/faecher/daten/SFachDaten.vue");

export class RouteFaecherDaten extends RouteNode<RouteDataFaecherDaten, RouteKatalogFaecher> {

	public constructor() {
		super("faecher_daten", "daten", SFachDaten, new RouteDataFaecherDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			this.onSelect(this.parent!.liste.gefiltert.find(f => f.id === id));
		}
	}

	protected onSelect(item?: FaecherListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten.unselect();
		} else {
			this.data.item = item;
			this.data.daten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeKatalogFaecher.getProps(to),
			data: this.data.daten
		};
	}

}

export const routeFaecherDaten = new RouteFaecherDaten();

