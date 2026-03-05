import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteLeitungsfunktionen } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionen";
import { BenutzerKompetenz, Schulform } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeLeitungsfunktionen } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionen";
import type { LeitungsfunktionenGruppenprozesseProps } from "~/components/schule/kataloge/leitungsfunktionen/gruppenprozesse/LeitungsfunktionenGruppenprozesseProps";

const LeitungsfunktionenGruppenprozesse = () => import("~/components/schule/kataloge/leitungsfunktionen/gruppenprozesse/LeitungsfunktionenGruppenprozesse.vue");

export class RouteLeitungsfunktionenGruppenprozesse extends RouteNode<any, RouteLeitungsfunktionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.leitungsfunktionen.gruppenprozesse", "gruppenprozesse", LeitungsfunktionenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): LeitungsfunktionenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			delete: routeLeitungsfunktionen.data.delete,
			deleteCheck: routeLeitungsfunktionen.data.deleteCheck,
			manager: () => routeLeitungsfunktionen.data.manager,
		};
	}
}

export const routeLeitungsfunktionenGruppenprozesse = new RouteLeitungsfunktionenGruppenprozesse();
