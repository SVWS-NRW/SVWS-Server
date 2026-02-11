import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import type { RouteOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import { routeOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import type { OrteDatenProps } from "~/components/schule/kataloge/orte/daten/OrteDatenProps";

const OrteDaten = () => import("~/components/schule/kataloge/orte/daten/OrteDaten.vue");

export class RouteOrteDaten extends RouteNode<any, RouteOrte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.orte.daten", "daten", OrteDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ort";
	}

	public getProps(to: RouteLocationNormalized): OrteDatenProps {
		return {
			manager: () => routeOrte.data.manager,
			patch: routeOrte.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeOrteDaten = new RouteOrteDaten();

