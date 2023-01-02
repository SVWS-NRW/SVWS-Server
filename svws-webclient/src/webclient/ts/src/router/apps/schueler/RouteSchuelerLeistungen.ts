import { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchueler } from "../RouteSchueler";
import { routeSchuelerLeistungenDaten } from "./leistungsdaten/RouteSchuelerLeistungenDaten";


const SSchuelerLeistungen = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungen.vue");

export class RouteSchuelerLeistungen extends RouteNode<unknown> {

	public constructor() {
		super("schueler_leistungen", "leistungsdaten", SSchuelerLeistungen);
		super.propHandler = (route) => routeSchueler.getProps(route);
		super.text = "Leistungsdaten";
		super.children = [
			routeSchuelerLeistungenDaten
		];
	}

    public async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): Promise<any> {
		return { name: routeSchuelerLeistungenDaten.name, params: to.params };
    }

}

export const routeSchuelerLeistungen = new RouteSchuelerLeistungen();

