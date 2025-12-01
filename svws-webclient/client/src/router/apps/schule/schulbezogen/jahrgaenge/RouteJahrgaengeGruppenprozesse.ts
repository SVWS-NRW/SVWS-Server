import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { type RouteJahrgaenge, routeJahrgaenge } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaenge";
import type { JahrgaengeGruppenprozesseProps } from "~/components/schule/schulbezogen/jahrgaenge/gruppenprozesse/JahrgaengeGruppenprozesseProps";

const JahrgaengeGruppenprozesse = () => import("~/components/schule/schulbezogen/jahrgaenge/gruppenprozesse/JahrgaengeGruppenprozesse.vue");

export class RouteJahrgaengeGruppenprozesse extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.jahrgaenge.gruppenprozesse", "gruppenprozesse", JahrgaengeGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeGruppenprozesseProps {
		return {
			serverMode: api.mode,
			manager: () => routeJahrgaenge.data.manager,
			delete: routeJahrgaenge.data.delete,
			deleteCheck: routeJahrgaenge.data.deleteCheck,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeJahrgaengeGruppenprozesse = new RouteJahrgaengeGruppenprozesse();

