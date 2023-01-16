import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { DataBetriebsstammdaten } from "~/apps/schueler/DataBetriebsstammdaten";
import { ListSchuelerBetriebsdaten } from "~/apps/schueler/ListSchuelerBetriebsdaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";

const SSchuelerAdressen = () => import("~/components/schueler/adressen/SSchuelerAdressen.vue");

export class RouteDataSchuelerAdressen {
	item: SchuelerListeEintrag | undefined = undefined;
	listSchuelerbetriebe : ListSchuelerBetriebsdaten = new ListSchuelerBetriebsdaten();
	betriebsStammdaten: DataBetriebsstammdaten = new DataBetriebsstammdaten();
}

export class RouteSchuelerAdressen extends RouteNode<RouteDataSchuelerAdressen, RouteSchueler> {

	public constructor() {
		super("schueler_adressen", "adressen", SSchuelerAdressen, new RouteDataSchuelerAdressen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Adressen / Betriebe";
		// TODO Löse diesen Zusammenhang in den Komponente so auf, dass die übliche Vorgehensweise bei den Routen angewandt werden kann und die Folgezeile nicht mehr nötig ist
		this.data.listSchuelerbetriebe.add_data(this.data.betriebsStammdaten);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (routeSchueler.item === undefined)
			return;
		await this.data.listSchuelerbetriebe.update_list(routeSchueler.item.id);
	}

	protected onSelect(item?: SchuelerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = routeSchueler.getProps(to);
		this.onSelect(prop.item.value);
		prop.listSchuelerbetriebe = this.data.listSchuelerbetriebe;
		prop.betriebsStammdaten = this.data.betriebsStammdaten;
		return prop;
	}

}

export const routeSchuelerAdressen = new RouteSchuelerAdressen();

