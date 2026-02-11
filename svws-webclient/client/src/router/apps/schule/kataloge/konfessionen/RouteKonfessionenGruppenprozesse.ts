import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { routeKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";
import type { RouteKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";
import type { KonfessionenGruppenprozesseProps } from "~/components/schule/kataloge/konfessionen/gruppenprozesse/KonfessionenGruppenprozesseProps";

const KonfessionenGruppenprozesse = () => import("~/components/schule/kataloge/konfessionen/gruppenprozesse/KonfessionenGruppenprozesse.vue");

export class RouteKonfessionenGruppenprozesse extends RouteNode<any, RouteKonfessionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.konfessionen.gruppenprozesse", "gruppenprozesse", KonfessionenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KonfessionenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			delete: routeKonfessionen.data.delete,
			deleteCheck: routeKonfessionen.data.deleteCheck,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeKonfessionen.data.manager,
			gotoDefaultView: routeKonfessionen.data.gotoDefaultView,
		};
	}

}

export const routeKonfessionenGruppenprozesse = new RouteKonfessionenGruppenprozesse();

