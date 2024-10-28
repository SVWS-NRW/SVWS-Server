import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeKurse, type RouteKurse } from "./RouteKurse";
import type { KurseGruppenprozesseProps } from "~/components/kurse/gruppenprozesse/SKurseGruppenprozesseProps";

const SKurseGruppenprozesse = () => import("~/components/kurse/gruppenprozesse/SKurseGruppenprozesse.vue");

export class RouteKurseGruppenprozesse extends RouteNode<any, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "kurse.gruppenprozesse", "gruppenprozesse", SKurseGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): KurseGruppenprozesseProps {
		return {
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			kurseListeManager: () => routeKurse.data.kursListeManager,
			deleteKurse: routeKurse.data.deleteKurse,
			deleteKurseCheck: routeKurse.data.deleteKurseCheck,
		};
	}

}

export const routeKurseGruppenprozesse = new RouteKurseGruppenprozesse();

