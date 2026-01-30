import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import type { RouteFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { routeFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import type { FloskelgruppenGruppenprozesseProps } from "~/components/schule/kataloge/floskelgruppen/gruppenprozesse/FloskelgruppenGruppenprozesseProps";

const FloskelgruppenGruppenprozesse = () => import(
	"~/components/schule/kataloge/floskelgruppen/gruppenprozesse/FloskelgruppenGruppenprozesse.vue");

export class RouteFloskelgruppenGruppenprozesse extends RouteNode<any, RouteFloskelgruppen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.floskelgruppen.gruppenprozesse", "gruppenprozesse", FloskelgruppenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FloskelgruppenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			delete: routeFloskelgruppen.data.delete,
			deleteCheck: routeFloskelgruppen.data.deleteCheck,
			manager: () => routeFloskelgruppen.data.manager,
		};
	}
}

export const routeFloskelgruppenGruppenprozesse = new RouteFloskelgruppenGruppenprozesse();
