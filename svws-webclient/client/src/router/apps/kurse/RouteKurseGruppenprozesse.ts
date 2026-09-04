import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeKurse, type RouteKurse } from "./RouteKurse";
import type { KurseGruppenprozesseProps } from "~/components/kurse/gruppenprozesse/SKurseGruppenprozesseProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const SKurseGruppenprozesse = () => import("~/components/kurse/gruppenprozesse/SKurseGruppenprozesse.vue");

export class RouteKurseGruppenprozesse extends RouteNode<any, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "kurse.gruppenprozesse", "gruppenprozesse", SKurseGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): KurseGruppenprozesseProps {
		return {
			apiStatus: api.status,
			manager: () => routeKurse.data.manager,
			deleteKurse: routeKurse.data.delete,
		};
	}

}

export const routeKurseGruppenprozesse = new RouteKurseGruppenprozesse();

