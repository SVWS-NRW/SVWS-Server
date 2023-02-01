import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataBetriebsstammdaten } from "~/apps/schueler/DataBetriebsstammdaten";
import { ListSchuelerBetriebsdaten } from "~/apps/schueler/ListSchuelerBetriebsdaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";
import { routeApp } from "~/router/RouteApp";

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
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === tmp));
		}
	}

	protected async onSelect(item?: SchuelerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.betriebsStammdaten.unselect();
		} else {
			this.data.item = item;
			await this.data.listSchuelerbetriebe.update_list(this.data.item.id);
			await this.data.betriebsStammdaten.select(this.data.listSchuelerbetriebe.ausgewaehlt);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeSchueler.getProps(to),
			orte: routeApp.data.orte,
			ortsteile: routeApp.data.ortsteile,
			listSchuelerbetriebe: this.data.listSchuelerbetriebe,
			betriebsStammdaten: this.data.betriebsStammdaten
		};
	}

}

export const routeSchuelerAdressen = new RouteSchuelerAdressen();

