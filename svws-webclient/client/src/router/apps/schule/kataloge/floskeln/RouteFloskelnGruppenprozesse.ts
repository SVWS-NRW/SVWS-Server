import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import type { RouteFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import { routeFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import type { FloskelnGruppenprozesseProps } from "~/components/schule/kataloge/floskeln/gruppenprozesse/FloskelnGruppenprozesseProps";

const FloskelnGruppenprozesse = () => import(
	"~/components/schule/kataloge/floskeln/gruppenprozesse/FloskelnGruppenprozesse.vue");

export class RouteFloskelnGruppenprozesse extends RouteNode<any, RouteFloskeln> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.floskeln.gruppenprozesse", "gruppenprozesse", FloskelnGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FloskelnGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			delete: routeFloskeln.data.delete,
			manager: () => routeFloskeln.data.manager,
		};
	}
}

export const routeFloskelnGruppenprozesse = new RouteFloskelnGruppenprozesse();
