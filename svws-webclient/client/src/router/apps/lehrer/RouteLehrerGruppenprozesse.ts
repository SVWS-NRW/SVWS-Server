import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import type { LehrerGruppenprozesseProps } from "~/components/lehrer/gruppenprozesse/SLehrerGruppenprozesseProps";

const SLehrerGruppenprozesse = () => import("~/components/lehrer/gruppenprozesse/SLehrerGruppenprozesse.vue");

export class RouteLehrerGruppenprozesse extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_AENDERN ], "lehrer.gruppenprozesse", "gruppenprozesse", SLehrerGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : "" };
	}

	public getProps(to: RouteLocationNormalized): LehrerGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			lehrerListeManager: () => routeLehrer.data.lehrerListeManager,
			deleteLehrer: routeLehrer.data.deleteLehrer,
			deleteLehrerCheck: routeLehrer.data.deleteLehrerCheck,
		};
	}

}

export const routeLehrerGruppenprozesse = new RouteLehrerGruppenprozesse();