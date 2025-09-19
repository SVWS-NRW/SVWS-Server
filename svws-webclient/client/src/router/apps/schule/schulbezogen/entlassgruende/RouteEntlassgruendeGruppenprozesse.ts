import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { EntlassgruendeGruppenprozesseProps } from "~/components/schule/schulbezogen/entlassgruende/gruppenprozesse/SEntlassgruendeGruppenprozesseProps";
import type { RouteEntlassgruende } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteEntlassgruende";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeEntlassgruende } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteEntlassgruende";

const SEntlassgruendeGruppenprozesse = () => import(
	"~/components/schule/schulbezogen/entlassgruende/gruppenprozesse/SEntlassgruendeGruppenprozesse.vue");

export class RouteEntlassgruendeGruppenprozesse extends RouteNode<any, RouteEntlassgruende> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.entlassgruende.gruppenprozesse" , "gruppenprozesse", SEntlassgruendeGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse"
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}}
	}

	public getProps(to: RouteLocationNormalized): EntlassgruendeGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteEntlassgruende: routeEntlassgruende.data.delete,
			manager: () => routeEntlassgruende.data.manager,
		}
	}

}

export const routeEntlassgruendeGruppenprozesse = new RouteEntlassgruendeGruppenprozesse();
