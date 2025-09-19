import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { MerkmaleGruppenprozesseProps } from "~/components/schule/schulbezogen/merkmale/gruppenprozesse/SMerkmaleGruppenprozesseProps";
import type { RouteMerkmale } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmale";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeMerkmale } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmale";

const SMerkmaleGruppenprozesse = () => import(
	"~/components/schule/schulbezogen/merkmale/gruppenprozesse/SMerkmaleGruppenprozesse.vue");

export class RouteMerkmaleGruppenprozesse extends RouteNode<any, RouteMerkmale> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.merkmale.gruppenprozesse" , "gruppenprozesse", SMerkmaleGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse"
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}}
	}

	public getProps(to: RouteLocationNormalized): MerkmaleGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteMerkmale: routeMerkmale.data.delete,
			manager: () => routeMerkmale.data.manager,
		}
	}
}

export const routeMerkmaleGruppenprozesse = new RouteMerkmaleGruppenprozesse();
