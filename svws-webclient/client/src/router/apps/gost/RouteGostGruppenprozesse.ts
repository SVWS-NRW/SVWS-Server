import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteGost } from "./RouteGost";
import { routeGost } from "./RouteGost";
import type { GostGruppenprozesseProps } from "~/components/gost/gruppenprozesse/SGostGruppenprozesseProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const SGostGruppenprozesse = () => import("~/components/gost/gruppenprozesse/SGostGruppenprozesse.vue");

export class RoutegostGruppenprozesse extends RouteNode<any, RouteGost> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.OBERSTUFE_ABITURJAHRGAENGE_VERWALTEN], "gost.gruppenprozesse", "gruppenprozesse", SGostGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): GostGruppenprozesseProps {
		return {
			removeAbiturjahrgaenge: routeGost.data.removeAbiturjahrgaenge,
			removeAbiturjahrgaengeCheck: routeGost.data.removeAbiturjahrgaengeCheck,
		};
	}

}

export const routeGostGruppenprozesse = new RoutegostGruppenprozesse();

