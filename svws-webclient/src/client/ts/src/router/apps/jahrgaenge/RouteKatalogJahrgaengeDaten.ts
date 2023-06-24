import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { JahrgangDatenProps } from "~/components/kataloge/jahrgaenge/daten/SJahrgangDatenProps";
import type { RouteKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { RouteNode } from "~/router/RouteNode";

const SJahrgangDaten = () => import("~/components/kataloge/jahrgaenge/daten/SJahrgangDaten.vue");

export class RouteKatalogJahrgaengeDaten extends RouteNode<unknown, RouteKatalogJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.jahrgaenge.daten", "daten", SJahrgangDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKatalogJahrgaenge.data.auswahl === undefined)
			return routeKatalogJahrgaenge.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): JahrgangDatenProps {
		return {
			patch: routeKatalogJahrgaenge.data.patch,
			data: routeKatalogJahrgaenge.data.daten,
			mapKatalogeintraege: routeKatalogJahrgaenge.data.mapKatalogeintraege
		};
	}

}

export const routeKatalogJahrgaengeDaten = new RouteKatalogJahrgaengeDaten();

