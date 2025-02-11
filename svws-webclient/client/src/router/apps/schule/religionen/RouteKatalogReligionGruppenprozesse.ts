import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { ViewType } from "@ui";
import { type RouteSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";
import type { KatalogReligionGruppenprozesseProps } from "~/components/schule/kataloge/religionen/gruppenprozesse/SKatalogReligionGruppenprozesseProps";
import { routeKatalogReligionen } from "~/router/apps/schule/religionen/RouteKatalogReligionen";

const SKatalogReligionGruppenprozesse = () => import("~/components/schule/kataloge/religionen/gruppenprozesse/SKatalogReligionGruppenprozesse.vue");

export class RouteKatalogReligionGruppenprozesse extends RouteNode<any, RouteSchuleJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.religionen.gruppenprozesse", "gruppenprozesse", SKatalogReligionGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): KatalogReligionGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			schulgliederungen: api.schulgliederungen,
			religionListeManager: () => routeKatalogReligionen.data.manager,
			delete: routeKatalogReligionen.data.delete,
			checkBeforeDelete: routeKatalogReligionen.data.checkBeforeDelete,
		};
	}

}

export const routeKatalogReligionGruppenprozesse = new RouteKatalogReligionGruppenprozesse();

