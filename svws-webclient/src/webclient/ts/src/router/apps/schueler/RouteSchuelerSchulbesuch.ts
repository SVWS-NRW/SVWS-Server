import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

export class RouteDataSchuelerSchulbesuch {
	item: SchuelerListeEintrag | undefined = undefined;
	daten: DataSchuelerSchulbesuchsdaten = new DataSchuelerSchulbesuchsdaten();
}

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super("schueler_schulbesuch", "schulbesuch", SSchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
	}

	protected onSelect(item?: SchuelerListeEintrag) {
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
		let prop: Record<string, any> = routeSchueler.getProps(to);
		this.onSelect(prop.item.value);
		prop.data = this.data.daten;
		return prop;
	}

}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();

