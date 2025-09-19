import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteKatalogErzieherarten } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherarten";
import { routeKatalogErzieherarten } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherarten";
import type { SErzieherartenGruppenprozesseProps } from "~/components/schule/allgemein/erzieherarten/gruppenprozesse/SErzieherartenGruppenprozesseProps";

const SErzieherartenGruppenprozesse = () => import("~/components/schule/allgemein/erzieherarten/gruppenprozesse/SErzieherartenGruppenprozesse.vue");

export class RouteKatalogErzieherartenGruppenprozesse extends RouteNode<any, RouteKatalogErzieherarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.erzieherarten.gruppenprozesse", "gruppenprozesse", SErzieherartenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SErzieherartenGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeKatalogErzieherarten.data.manager,
			delete: routeKatalogErzieherarten.data.delete,
		};
	}
}

export const routeKatalogErzieherartenGruppenprozesse = new RouteKatalogErzieherartenGruppenprozesse();

