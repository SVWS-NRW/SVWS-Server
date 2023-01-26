import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";
import { RouteLocationRaw } from "vue-router";


const SSchuelerAbschnitt = () => import("~/components/schueler/abschnitt/SSchuelerAbschnitt.vue");

export class RouteSchuelerAbschnitt extends RouteNode<unknown, RouteSchueler> {

	public constructor() {
		super("schueler_abschnitt", "abschnitt", SSchuelerAbschnitt);
		super.propHandler = (route) => routeSchueler.getProps(route);
		super.text = "Aktueller Abschnitt";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

}

export const routeSchuelerAbschnitt = new RouteSchuelerAbschnitt();

