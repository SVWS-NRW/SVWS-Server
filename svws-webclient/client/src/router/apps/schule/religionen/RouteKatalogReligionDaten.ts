import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogReligionen, type RouteKatalogReligionen } from "~/router/apps/schule/religionen/RouteKatalogReligionen";

import type { ReligionDatenProps } from "~/components/schule/kataloge/religionen/daten/SReligionDatenProps";
import { routeApp } from "~/router/apps/RouteApp";

const SReligionDaten = () => import("~/components/schule/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<any, RouteKatalogReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.religionen.daten", "daten", SReligionDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogReligionen.data.religionListeManager.auswahlID() === null)
			return routeKatalogReligionen.getRoute(undefined);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): ReligionDatenProps {
		return {
			religionListeManager: () => routeKatalogReligionen.data.religionListeManager,
			patch: routeKatalogReligionen.data.patch,
		};
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

