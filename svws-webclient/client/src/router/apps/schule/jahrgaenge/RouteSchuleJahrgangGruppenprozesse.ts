import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { ViewType } from "@ui";
import { type RouteSchuleJahrgaenge, routeSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";
import type { SchuleJahrgangGruppenprozesseProps } from "~/components/schule/jahrgaenge/gruppenprozesse/SSchuleJahrgangGruppenprozesseProps";

const SSchuleJahrgangGruppenprozesse = () => import("~/components/schule/jahrgaenge/gruppenprozesse/SSchuleJahrgangGruppenprozesse.vue");

export class RouteSchuleJahrgangGruppenprozesse extends RouteNode<any, RouteSchuleJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN ], "schule.jahrgaenge.gruppenprozesse", "gruppenprozesse", SSchuleJahrgangGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SchuleJahrgangGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			jahrgangListeManager: () => routeSchuleJahrgaenge.data.jahrgangListeManager,
			deleteJahrgaenge: routeSchuleJahrgaenge.data.deleteJahrgaenge,
			deleteJahrgaengeCheck: routeSchuleJahrgaenge.data.deleteJahrgaengeCheck,
		};
	}

}

export const routeSchuleJahrgangGruppenprozesse = new RouteSchuleJahrgangGruppenprozesse();

