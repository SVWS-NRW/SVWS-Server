import { RouteNode } from "~/router/RouteNode";
import { RouteKurse, routeKurse } from "~/router/apps/RouteKurse";
import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
import { DataKurs } from "~/apps/kurse/DataKurs";
import { RouteLocationNormalized } from "vue-router";

const SKursDaten = () => import("~/components/kurse/daten/SKursDaten.vue");

export class RouteDataKurseDaten {
	item: KursListeEintrag | undefined = undefined;
	daten: DataKurs = new DataKurs();
}


export class RouteKurseDaten extends RouteNode<RouteDataKurseDaten, RouteKurse> {

	public constructor() {
		super("kurse_daten", "daten", SKursDaten, new RouteDataKurseDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	protected onSelect(item?: KursListeEintrag) {
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
		let prop: Record<string, any> = routeKurse.getProps(to);
		this.onSelect(prop.item);
		prop.data = this.data.daten;
		return prop;
	}

}

export const routeKurseDaten = new RouteKurseDaten();

