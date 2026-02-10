import { RouteNode } from "~/router/RouteNode";
import { routeBetriebsarten, type RouteBetriebsarten } from "./RouteBetriebsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { routeApp } from "~/router/apps/RouteApp";
import type { BetriebsartenGruppenprozesseProps } from "~/components/schule/kataloge/betriebsarten/gruppenprozesse/BetriebsartenGruppenprozesseProps";
import { api } from "~/router/Api";

const BetriebsartenGruppenprozesse = () => import(
	"~/components/schule/kataloge/betriebsarten/gruppenprozesse/BetriebsartenGruppenprozesse.vue");

class RouteBetriebsartenGruppenprozesse extends RouteNode<any, RouteBetriebsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.betriebsarten.gruppenprozesse", "gruppenprozesse", BetriebsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): BetriebsartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeBetriebsarten.data.manager,
			delete: routeBetriebsarten.data.delete,
			deleteCheck: routeBetriebsarten.data.deleteCheck,
			gotoDefaultView: routeBetriebsarten.data.gotoDefaultView,
		};
	}
}

export const routeBetriebsartenGruppenprozesse = new RouteBetriebsartenGruppenprozesse();

