import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import { routeOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import type { OrteGruppenprozesseProps } from "~/components/schule/kataloge/orte/gruppenprozesse/OrteGruppenprozesseProps";

const OrteGruppenprozesse = () => import("~/components/schule/kataloge/orte/gruppenprozesse/OrteGruppenprozesse.vue");

export class RouteOrteGruppenprozesse extends RouteNode<any, RouteOrte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.orte.gruppenprozesse", "gruppenprozesse", OrteGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(_: RouteLocationNormalized): OrteGruppenprozesseProps {
		return {
			serverMode: api.mode,
			manager: () => routeOrte.data.manager,
			delete: routeOrte.data.delete,
			deleteCheck: routeOrte.data.deleteCheck,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeOrteGruppenprozesse = new RouteOrteGruppenprozesse();

