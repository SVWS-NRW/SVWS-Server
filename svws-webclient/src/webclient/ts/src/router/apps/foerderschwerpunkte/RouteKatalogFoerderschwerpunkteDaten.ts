import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { DataFoerderschwerpunkt } from "~/apps/kataloge/foerderschwerpunkt/DataFoerderschwerpunkt";
import { RouteNode } from "~/router/RouteNode";
import { RouteKatalogFoerderschwerpunkte, routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";

export class RouteDataKatalogFoerderschwerpunkteDaten {
	item: FoerderschwerpunktEintrag | undefined = undefined;
	daten: DataFoerderschwerpunkt = new DataFoerderschwerpunkt();
}

const SFoerderschwerpunktDaten = () => import("~/components/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunktDaten.vue");

export class RouteKatalogFoerderschwerpunkteDaten extends RouteNode<RouteDataKatalogFoerderschwerpunkteDaten, RouteKatalogFoerderschwerpunkte> {

	public constructor() {
		super("foerderschwerpunkte_daten", "daten", SFoerderschwerpunktDaten, new RouteDataKatalogFoerderschwerpunkteDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: FoerderschwerpunktEintrag) {
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

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeKatalogFoerderschwerpunkte.getProps(to),
			data: this.data.daten
		};
	}

}

export const routeKatalogFoerderschwerpunkteDaten = new RouteKatalogFoerderschwerpunkteDaten();

