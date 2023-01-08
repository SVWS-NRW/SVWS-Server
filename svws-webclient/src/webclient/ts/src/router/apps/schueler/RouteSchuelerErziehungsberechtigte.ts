import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { DataKatalogErzieherarten } from "~/apps/schueler/DataKatalogErzieherarten";
import { DataSchuelerErzieherStammdaten } from "~/apps/schueler/DataSchuelerErzieherStammdaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";

const SSchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue");

export class RouteDataSchuelerErziehungsberechtigte {
	item: SchuelerListeEintrag | undefined = undefined;
	daten: DataSchuelerErzieherStammdaten = new DataSchuelerErzieherStammdaten();
	erzieherarten: DataKatalogErzieherarten = new DataKatalogErzieherarten();
}

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte, RouteSchueler> {

	public constructor() {
		super("schueler_erziehungsberechtigte", "erziehungsberechtigte", SSchuelerErziehungsberechtigte, new RouteDataSchuelerErziehungsberechtigte());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erziehungsberechtigte";
	}

	protected onSelect(item?: SchuelerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten.unselect();
			this.data.erzieherarten.unselect();
		} else {
			this.data.item = item;
			this.data.daten.select(this.data.item);
			this.data.erzieherarten.select(undefined);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		let prop: Record<string, any> = routeSchueler.getProps(to);
		this.onSelect(prop.item.value);
		prop.data = this.data.daten;
		prop.erzieherarten = this.data.erzieherarten;
		return prop;
	}

}

export const routeSchuelerErziehungsberechtigte = new RouteSchuelerErziehungsberechtigte();

