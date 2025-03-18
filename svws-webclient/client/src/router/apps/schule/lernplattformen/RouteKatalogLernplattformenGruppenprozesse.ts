import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { ViewType } from "@ui";
import type { RouteKatalogLernplattformen } from "~/router/apps/schule/lernplattformen/RouteKatalogLernplattformen";
import { routeKatalogLernplattformen } from "~/router/apps/schule/lernplattformen/RouteKatalogLernplattformen";
import type { SLernplattformenGruppenprozesseProps } from "~/components/schule/kataloge/lernplattformen/gruppenprozesse/SLernplattformenGruppenprozesseProps";

const SLernplattformenGruppenprozesse = () => import("~/components/schule/kataloge/lernplattformen/gruppenprozesse/SLernplattformenGruppenprozesse.vue");

export class RouteKatalogLernplattformenGruppenprozesse extends RouteNode<any, RouteKatalogLernplattformen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.lernplattformen.gruppenprozesse", "gruppenprozesse", SLernplattformenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SLernplattformenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulgliederungen: api.schulgliederungen,
			manager: () => routeKatalogLernplattformen.data.manager,
			delete: routeKatalogLernplattformen.data.delete,
		};
	}

}

export const routeKatalogLernplattformenGruppenprozesse = new RouteKatalogLernplattformenGruppenprozesse();

