import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { StundenplanGruppenprozesseProps } from "~/components/stundenplan/gruppenprozesse/SStundenplanGruppenprozesseProps";
import { type RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { RouteNode } from "~/router/RouteNode";

const SStundenplanGruppenprozesse = () => import("~/components/stundenplan/gruppenprozesse/SStundenplanGruppenprozesse.vue");

export class RouteStundenplanGruppenprozesse extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "stundenplan.gruppenprozesse", "gruppenprozesse", SStundenplanGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): StundenplanGruppenprozesseProps {
		return {
			stundenplanListeManager: () => routeStundenplan.data.manager,
			deleteStundenplan: routeStundenplan.data.delete,
		};
	}

}

export const routeStundenplanGruppenprozesse = new RouteStundenplanGruppenprozesse();

