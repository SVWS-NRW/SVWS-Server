import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { DataFoerderschwerpunkt } from "~/apps/kataloge/foerderschwerpunkt/DataFoerderschwerpunkt";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogFoerderschwerpunkte } from "../RouteKatalogFoerderschwerpunkte";

const SFoerderschwerpunktDaten = () => import("~/components/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunktDaten.vue");

export class RouteDataKatalogFoerderschwerpunkteDaten {
	item: FoerderschwerpunktEintrag | undefined = undefined;
	daten: DataFoerderschwerpunkt = new DataFoerderschwerpunkt();
}

export class RouteKatalogFoerderschwerpunkteDaten extends RouteNode<RouteDataKatalogFoerderschwerpunkteDaten> {

	public constructor() {
		super("foerderschwerpunkte_daten", "daten", SFoerderschwerpunktDaten, new RouteDataKatalogFoerderschwerpunkteDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	protected onSelect(item?: FoerderschwerpunktEintrag) {
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
		let prop: any = RouteNodeListView.getPropsByAuswahlID(to, routeKatalogFoerderschwerpunkte.data.auswahl);
		this.onSelect(prop.item as FoerderschwerpunktEintrag | undefined);
		prop.data = this.data.daten;
		return prop;
	}


}

export const routeKatalogFoerderschwerpunkteDaten = new RouteKatalogFoerderschwerpunkteDaten();

