import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import type { RouteOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import { routeOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import type { OrtsteileDatenProps } from "~/components/schule/kataloge/ortsteile/daten/OrtsteileDatenProps";

const OrtsteileDaten = () => import("~/components/schule/kataloge/ortsteile/daten/OrtsteileDaten.vue");

export class RouteOrtsteileDaten extends RouteNode<any, RouteOrtsteile> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.ortsteile.daten", "daten", OrtsteileDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ortsteil";
	}

	public getProps(to: RouteLocationNormalized): OrtsteileDatenProps {
		return {
			manager: () => routeOrtsteile.data.manager,
			patch: routeOrtsteile.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeOrtsteileDaten = new RouteOrtsteileDaten();

