import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../RouteApp";
import { ViewType } from "@ui";
import type { RouteGost } from "./RouteGost";
import { routeGost } from "./RouteGost";
import type { GostGruppenprozesseProps } from "~/components/gost/gruppenprozesse/SGostGruppenprozesseProps";

const SGostGruppenprozesse = () => import("~/components/gost/gruppenprozesse/SGostGruppenprozesse.vue");

export class RoutegostGruppenprozesse extends RouteNode<any, RouteGost> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.OBERSTUFE_ABITURJAHRGAENGE_VERWALTEN ], "gost.gruppenprozesse", "gruppenprozesse", SGostGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): GostGruppenprozesseProps {
		return {
			removeAbiturjahrgaenge: routeGost.data.removeAbiturjahrgaenge,
			removeAbiturjahrgaengeCheck: routeGost.data.removeAbiturjahrgaengeCheck,
		};
	}

}

export const routeGostGruppenprozesse = new RoutegostGruppenprozesse();

