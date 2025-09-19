import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteFoerderschwerpunkte } from "~/router/apps/schule/schulbezogen/foerderschwerpunkte/RouteFoerderschwerpunkte";
import type { FoerderschwerpunkteGruppenprozesseProps }
	from "~/components/schule/schulbezogen/foerderschwerpunkte/gruppenprozesse/SFoerderschwerpunkteGruppenprozesseProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/schulbezogen/foerderschwerpunkte/RouteFoerderschwerpunkte";

const SFoerderschwerpunkteGruppenprozesse = () => import(
	"~/components/schule/schulbezogen/foerderschwerpunkte/gruppenprozesse/SFoerderschwerpunkteGruppenprozesse.vue");

export class RouteFoerderschwerpunkteGruppenprozesse extends RouteNode<any, RouteFoerderschwerpunkte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.foerderschwerpunkte.gruppenprozesse" , "gruppenprozesse", SFoerderschwerpunkteGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse"
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}}
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteFoerderschwerpunkte: routeFoerderschwerpunkte.data.delete,
			manager: () => routeFoerderschwerpunkte.data.manager,
		}
	}
}

export const routeFoerderschwerpunkteGruppenprozesse = new RouteFoerderschwerpunkteGruppenprozesse();
