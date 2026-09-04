import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import type { StundenplanGruppenprozesseProps } from "~/components/stundenplan/gruppenprozesse/SStundenplanGruppenprozesseProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

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

