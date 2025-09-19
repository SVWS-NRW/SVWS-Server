import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteKatalogTelefonArten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArten";
import { routeKatalogTelefonArten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArten";
import type { STelefonArtenGruppenprozesseProps } from "~/components/schule/allgemein/telefonarten/gruppenprozesse/STelefonArtenGruppenprozesseProps";

const STelefonArtenGruppenprozesse = () => import("~/components/schule/allgemein/telefonarten/gruppenprozesse/STelefonArtenGruppenprozesse.vue");

export class RouteKatalogTelefonArtenGruppenprozesse extends RouteNode<any, RouteKatalogTelefonArten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.telefonarten.gruppenprozesse", "gruppenprozesse", STelefonArtenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): STelefonArtenGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeKatalogTelefonArten.data.manager,
			delete: routeKatalogTelefonArten.data.delete,
		};
	}
}

export const routeKatalogTelefonArtenGruppenprozesse = new RouteKatalogTelefonArtenGruppenprozesse();

