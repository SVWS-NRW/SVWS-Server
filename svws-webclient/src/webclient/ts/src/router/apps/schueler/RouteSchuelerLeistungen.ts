import { RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";
import { routeSchuelerLeistungenDaten } from "~/router/apps/schueler/leistungsdaten/RouteSchuelerLeistungenDaten";


const SSchuelerLeistungen = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungen.vue");

export class RouteSchuelerLeistungen extends RouteNode<unknown, RouteSchueler> {

	public constructor() {
		super("schueler_leistungen", "leistungsdaten", SSchuelerLeistungen);
		super.propHandler = (route) => routeSchueler.getProps(route);
		super.text = "Leistungsdaten";
		super.children = [
			routeSchuelerLeistungenDaten
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return { name: routeSchuelerLeistungenDaten.name, params: to_params };
	}

}

export const routeSchuelerLeistungen = new RouteSchuelerLeistungen();

