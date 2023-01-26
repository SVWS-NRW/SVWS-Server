import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataFach } from "~/apps/kataloge/faecher/DataFach";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFaecher, RouteKatalogFaecher } from "../RouteKatalogFaecher";

export class RouteDataFaecherDaten {
	item: FaecherListeEintrag | undefined = undefined;
	daten: DataFach = new DataFach();
}

const SFachDaten = () => import("~/components/kataloge/faecher/daten/SFachDaten.vue");

export class RouteFaecherDaten extends RouteNode<RouteDataFaecherDaten, RouteKatalogFaecher> {

	public constructor() {
		super("faecher_daten", "daten", SFachDaten, new RouteDataFaecherDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.gefiltert.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: FaecherListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.daten.unselect();
		} else {
			this.data.item = item;
			await this.data.daten.select(this.data.item);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeKatalogFaecher.getProps(to),
			data: this.data.daten
		};
	}

}

export const routeFaecherDaten = new RouteFaecherDaten();

