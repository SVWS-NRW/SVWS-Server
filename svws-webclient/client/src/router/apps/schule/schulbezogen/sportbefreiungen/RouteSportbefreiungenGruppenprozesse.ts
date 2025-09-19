import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { SportbefreiungenGruppenprozesseProps } from "~/components/schule/schulbezogen/sportbefreiungen/gruppenprozesse/SSportbefreiungenGruppenprozesseProps";
import type { RouteSportbefreiungen } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSportbefreiungen } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungen";

const SSportbefreiungenGruppenprozesse = () => import(
	"~/components/schule/schulbezogen/sportbefreiungen/gruppenprozesse/SSportbefreiungenGruppenprozesse.vue");

export class RouteSportbefreiungenGruppenprozesse extends RouteNode<any, RouteSportbefreiungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.sportbefreiungen.gruppenprozesse" , "gruppenprozesse", SSportbefreiungenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}};
	}

	public getProps(to: RouteLocationNormalized): SportbefreiungenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteSportbefreiungen: routeSportbefreiungen.data.delete,
			manager: () => routeSportbefreiungen.data.manager,
		}
	}
}

export const routeSportbefreiungenGruppenprozesse = new RouteSportbefreiungenGruppenprozesse();
