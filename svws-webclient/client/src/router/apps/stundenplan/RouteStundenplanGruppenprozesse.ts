import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import type { StundenplanGruppenprozesseProps } from "~/components/stundenplan/gruppenprozesse/SStundenplanGruppenprozesseProps";
import { ViewType } from "@ui";

const SStundenplanGruppenprozesse = () => import("~/components/stundenplan/gruppenprozesse/SStundenplanGruppenprozesse.vue");

export class RouteStundenplanGruppenprozesse extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "stundenplan.gruppenprozesse", "gruppenprozesse", SStundenplanGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): StundenplanGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			stundenplanListeManager: () => routeStundenplan.data.manager,
			deleteStundenplan: routeStundenplan.data.delete,
		};
	}

}

export const routeStundenplanGruppenprozesse = new RouteStundenplanGruppenprozesse();

