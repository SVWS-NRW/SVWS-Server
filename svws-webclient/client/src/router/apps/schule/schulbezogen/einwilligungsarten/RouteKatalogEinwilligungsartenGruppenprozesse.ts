import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteKatalogEinwilligungsarten} from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteKatalogEinwilligungsarten";
import {routeKatalogEinwilligungsarten} from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteKatalogEinwilligungsarten";
import type {
	SchuleEinwilligungsartenGruppenprozesseProps,
} from "~/components/schule/schulbezogen/einwilligungsarten/gruppenprozesse/SEinwilligungsartenGruppenprozesseProps";

const SEinwilligungsartenGruppenprozesse = () => import("~/components/schule/schulbezogen/einwilligungsarten/gruppenprozesse/SEinwilligungsartenGruppenprozesse.vue");

export class RouteKatalogEinwilligungsartenGruppenprozesse extends RouteNode<any, RouteKatalogEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.einwilligungsarten.gruppenprozesse", "gruppenprozesse", SEinwilligungsartenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SchuleEinwilligungsartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulgliederungen: api.schulgliederungen,
			manager: () => routeKatalogEinwilligungsarten.data.manager,
			delete: routeKatalogEinwilligungsarten.data.delete,
		};
	}

}

export const routeKatalogEinwilligungsartenGruppenprozesse = new RouteKatalogEinwilligungsartenGruppenprozesse();

