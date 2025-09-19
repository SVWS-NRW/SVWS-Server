import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { KindergaertenGruppenprozesseProps } from "~/components/schule/allgemein/kindergaerten/gruppenprozesse/SKindergaertenGruppenprozesseProps";
import type { RouteKindergaerten } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaerten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKindergaerten } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaerten";

const SKindergaertenGruppenprozesse = () => import(
	"~/components/schule/allgemein/kindergaerten/gruppenprozesse/SKindergaertenGruppenprozesse.vue");

export class RouteKindergaertenGruppenprozesse extends RouteNode<any, RouteKindergaerten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.kindergaerten.gruppenprozesse" , "gruppenprozesse", SKindergaertenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse"
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}}
	}

	public getProps(to: RouteLocationNormalized): KindergaertenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteKindergaerten: routeKindergaerten.data.delete,
			manager: () => routeKindergaerten.data.manager,
		}
	}
}

export const routeKindergaertenGruppenprozesse = new RouteKindergaertenGruppenprozesse();
