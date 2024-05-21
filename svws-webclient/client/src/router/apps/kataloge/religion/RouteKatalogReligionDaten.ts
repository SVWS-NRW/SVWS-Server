import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogReligion, type RouteKatalogReligionen } from "~/router/apps/kataloge/religion/RouteKatalogReligionen";

import type { ReligionDatenProps } from "~/components/kataloge/religionen/daten/SReligionDatenProps";
import { routeApp } from "../../RouteApp";

const SReligionDaten = () => import("~/components/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<any, RouteKatalogReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.religionen.daten", "daten", SReligionDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogReligion.data.religionListeManager.auswahlID() === null)
			return routeKatalogReligion.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): ReligionDatenProps {
		return {
			religionListeManager: () => routeKatalogReligion.data.religionListeManager,
			patch: routeKatalogReligion.data.patch,
		};
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

