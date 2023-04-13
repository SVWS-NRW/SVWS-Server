import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { FachDatenProps } from "~/components/kataloge/faecher/daten/SFachDatenProps";
import { RouteNode } from "~/router/RouteNode";
import type { RouteKatalogFaecher } from "../RouteKatalogFaecher";
import { routeKatalogFaecher } from "../RouteKatalogFaecher";

const SFachDaten = () => import("~/components/kataloge/faecher/daten/SFachDaten.vue");

export class RouteKatalogFachDaten extends RouteNode<unknown, RouteKatalogFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.faecher.daten", "daten", SFachDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKatalogFaecher.data.auswahl === undefined)
			return routeKatalogFaecher.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): FachDatenProps {
		return {
			patch: routeKatalogFaecher.data.patch,
			data: routeKatalogFaecher.data.daten,
			mapKatalogeintraege: routeKatalogFaecher.data.mapKatalogeintraege
		};
	}

}

export const routeKatalogFachDaten = new RouteKatalogFachDaten();

