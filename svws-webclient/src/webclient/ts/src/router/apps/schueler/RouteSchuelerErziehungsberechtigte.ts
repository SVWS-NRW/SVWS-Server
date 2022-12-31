import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { mainApp } from "~/apps/Main";
import { DataSchuelerErzieherStammdaten } from "~/apps/schueler/DataSchuelerErzieherStammdaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";

const SSchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue");

export class RouteDataSchuelerErziehungsberechtigte {
	item: SchuelerListeEintrag | undefined = undefined;
	daten: DataSchuelerErzieherStammdaten = new DataSchuelerErzieherStammdaten();
}

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte> {

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
		} else {
			this.data.item = item;
			this.data.daten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		let prop: Record<string, any> = RouteNodeListView.getPropsByAuswahlID(to, mainApp.apps.schueler.auswahl);
		this.onSelect(prop.item as SchuelerListeEintrag | undefined);
		prop.data = this.data.daten;
		return prop;
	}

}

export const routeSchuelerErziehungsberechtigte = new RouteSchuelerErziehungsberechtigte();

