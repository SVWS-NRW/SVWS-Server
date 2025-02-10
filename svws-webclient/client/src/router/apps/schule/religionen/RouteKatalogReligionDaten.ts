import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogReligionen, type RouteKatalogReligionen } from "~/router/apps/schule/religionen/RouteKatalogReligionen";

import type { ReligionDatenProps } from "~/components/schule/kataloge/religionen/daten/SReligionDatenProps";

const SReligionDaten = () => import("~/components/schule/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<any, RouteKatalogReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.religionen.daten", "daten", SReligionDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogReligionen.data.manager.auswahlID() === null)
			return routeKatalogReligionen.getRoute();
	}

	public getProps(to: RouteLocationNormalized): ReligionDatenProps {
		return {
			religionListeManager: () => routeKatalogReligionen.data.manager,
			patch: routeKatalogReligionen.data.patch,
		};
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

