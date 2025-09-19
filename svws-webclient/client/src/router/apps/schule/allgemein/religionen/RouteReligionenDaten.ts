import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { ReligionenDatenProps } from "~/components/schule/allgemein/religionen/daten/SReligionenDatenProps";
import { api } from "~/router/Api";
import { routeReligionen, type RouteReligionen } from "~/router/apps/schule/allgemein/religionen/RouteReligionen";

const SReligionenDaten = () => import("~/components/schule/allgemein/religionen/daten/SReligionenDaten.vue");

export class RouteReligionenDaten extends RouteNode<any, RouteReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.religionen.daten", "daten", SReligionenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religionen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeReligionen.data.manager.auswahlID() === null)
			return routeReligionen.getRoute();
	}

	public getProps(to: RouteLocationNormalized): ReligionenDatenProps {
		return {
			manager: () => routeReligionen.data.manager,
			patch: routeReligionen.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeReligionenDaten = new RouteReligionenDaten();

