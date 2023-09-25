import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, ReligionEintrag, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogReligion, type RouteKatalogReligionen } from "~/router/apps/kataloge/religion/RouteKatalogReligionen";

import type { ReligionDatenProps } from "~/components/kataloge/religionen/daten/SReligionDatenProps";

const SReligionDaten = () => import("~/components/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<unknown, RouteKatalogReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.religionen.daten", "daten", SReligionDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogReligion.data.auswahl === undefined)
			return routeKatalogReligion.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): ReligionDatenProps {
		return {
			patch: routeKatalogReligion.data.patch,
			auswahl: routeKatalogReligion.data.auswahl || new ReligionEintrag(),
		};
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

