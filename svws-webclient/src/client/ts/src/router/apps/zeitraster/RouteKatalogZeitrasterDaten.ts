import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteKatalogZeitraster} from "../RouteKatalogZeitraster";
import type { ZeitrasterDatenProps } from "~/components/kataloge/zeitraster/daten/SZeitrasterDatenProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogZeitraster } from "../RouteKatalogZeitraster";

const SZeitrasterDaten = () => import("~/components/kataloge/zeitraster/daten/SZeitrasterDaten.vue");

export class RouteKatalogZeitrasterDaten extends RouteNode<unknown, RouteKatalogZeitraster> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster.daten", "daten", SZeitrasterDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKatalogZeitraster.data.auswahl === undefined)
			return routeKatalogZeitraster.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterDatenProps {
		return {
			patch: routeKatalogZeitraster.data.patch,
			data: routeKatalogZeitraster.data.daten,
		};
	}

}

export const routeKatalogZeitrasterDaten = new RouteKatalogZeitrasterDaten();

