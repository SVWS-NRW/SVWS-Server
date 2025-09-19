import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { routeKatalogSchulen, type RouteKatalogSchulen } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchulen";
import type { KatalogSchuleGruppenprozesseProps } from "~/components/schule/allgemein/schulen/gruppenprozesse/SKatalogSchuleGruppenprozesseProps";

const SKatalogSchuleGruppenprozesse = () => import("~/components/schule/allgemein/schulen/gruppenprozesse/SKatalogSchuleGruppenprozesse.vue");

export class RouteKatalogSchuleGruppenprozesse extends RouteNode<any, RouteKatalogSchulen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.schulen.gruppenprozesse", "gruppenprozesse", SKatalogSchuleGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): KatalogSchuleGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			schuleListeManager: () => routeKatalogSchulen.data.manager,
			delete: routeKatalogSchulen.data.delete,
		};
	}

}

export const routeKatalogSchuleGruppenprozesse = new RouteKatalogSchuleGruppenprozesse();

