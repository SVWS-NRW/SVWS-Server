import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import type { KlassenGruppenprozesseProps } from "~/components/klassen/gruppenprozesse/SKlassenGruppenprozesseProps";
import { routeApp } from "../RouteApp";

const SKlassenGruppenprozesse = () => import("~/components/klassen/gruppenprozesse/SKlassenGruppenprozesse.vue");

export class RouteKlasseGruppenprozesse extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen.gruppenprozesse", "gruppenprozesse", SKlassenGruppenprozesse);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): KlassenGruppenprozesseProps {
		return {
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			klassenListeManager: () => routeKlassen.data.klassenListeManager,
			deleteKlassen: routeKlassen.data.deleteKlassen,
			deleteKlassenCheck: routeKlassen.data.deleteKlassenCheck
		};
	}

}

export const routeKlasseGruppenprozesse = new RouteKlasseGruppenprozesse();

