import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogZeitraster, type RouteKatalogZeitraster } from "~/router/apps/kataloge/zeitraster/RouteDataKatalogZeitraster";

import type { ZeitrasterDatenProps } from "~/components/kataloge/zeitraster/daten/SZeitrasterDatenProps";

const SZeitrasterDaten = () => import("~/components/kataloge/zeitraster/daten/SZeitrasterDaten.vue");

export class RouteKatalogZeitrasterDaten extends RouteNode<unknown, RouteKatalogZeitraster> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster.daten", "daten", SZeitrasterDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterDatenProps {
		return {
			stundenplanManager: () => routeKatalogZeitraster.data.stundenplanManager,
			patchZeitraster: routeKatalogZeitraster.data.patchZeitraster,
			addZeitraster: routeKatalogZeitraster.data.addZeitraster,
			removeZeitraster: routeKatalogZeitraster.data.removeZeitraster,
		};
	}

}

export const routeKatalogZeitrasterDaten = new RouteKatalogZeitrasterDaten();

