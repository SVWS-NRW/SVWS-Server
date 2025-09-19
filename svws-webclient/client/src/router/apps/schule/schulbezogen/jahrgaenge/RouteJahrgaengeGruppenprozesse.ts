import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { type RouteJahrgaenge, routeJahrgaenge } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaenge";
import type { SchuleJahrgangGruppenprozesseProps } from "~/components/schule/schulbezogen/jahrgaenge/gruppenprozesse/SJahrgaengeGruppenprozesseProps";

const SJahrgaengeGruppenprozesse = () => import("~/components/schule/schulbezogen/jahrgaenge/gruppenprozesse/SJahrgaengeGruppenprozesse.vue");

export class RouteJahrgaengeGruppenprozesse extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.jahrgaenge.gruppenprozesse", "gruppenprozesse", SJahrgaengeGruppenprozesse);
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
			manager: () => routeJahrgaenge.data.manager,
			delete: routeJahrgaenge.data.delete,
			deleteCheck: routeJahrgaenge.data.deleteCheck,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeJahrgaengeGruppenprozesse = new RouteJahrgaengeGruppenprozesse();

