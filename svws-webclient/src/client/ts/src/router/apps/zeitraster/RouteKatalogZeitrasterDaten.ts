import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteKatalogZeitraster} from "../RouteKatalogZeitraster";
import type { ZeitrasterDatenProps } from "~/components/kataloge/zeitraster/daten/SZeitrasterDatenProps";
import { BenutzerKompetenz, Schulform } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogZeitraster } from "../RouteKatalogZeitraster";

const SZeitrasterDaten = () => import("~/components/kataloge/zeitraster/daten/SZeitrasterDaten.vue");

export class RouteKatalogZeitrasterDaten extends RouteNode<unknown, RouteKatalogZeitraster> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster.daten", "daten", SZeitrasterDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterDatenProps {
		return {
			listKatalogeintraege: () => routeKatalogZeitraster.data.listKatalogeintraege,
			patchZeitraster: routeKatalogZeitraster.data.patchZeitraster,
			addZeitraster: routeKatalogZeitraster.data.addZeitraster,
			removeZeitraster: routeKatalogZeitraster.data.removeZeitraster,
		};
	}

}

export const routeKatalogZeitrasterDaten = new RouteKatalogZeitrasterDaten();

