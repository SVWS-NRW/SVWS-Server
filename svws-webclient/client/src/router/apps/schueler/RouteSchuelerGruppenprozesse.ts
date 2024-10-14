import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "../RouteApp";
import { RouteType } from "~/router/RouteType";
import type { SchuelerGruppenprozesseProps } from "~/components/schueler/gruppenprozesse/SSchuelerGruppenprozesseProps";
import { api } from "~/router/Api";

const SSchuelerGruppenprozesse = () => import("~/components/schueler/gruppenprozesse/SSchuelerGruppenprozesse.vue");

export class RouteSchuelerGruppenprozesse extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN ], "schueler.gruppenprozesse", "gruppenprozesse", SSchuelerGruppenprozesse);
		super.types = new Set([ RouteType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			schuelerListeManager: () => routeSchueler.data.schuelerListeManager,
			deleteSchueler: routeSchueler.data.deleteSchueler,
			deleteSchuelerCheck: routeSchueler.data.deleteSchuelerCheck,
		};
	}

}

export const routeSchuelerGruppenprozesse = new RouteSchuelerGruppenprozesse();

