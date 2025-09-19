import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteKatalogVermerkarten} from "./RouteKatalogVermerkarten";
import type { SchuleVermerkartenGruppenprozesseProps } from "~/components/schule/schulbezogen/vermerkarten/gruppenprozesse/SVermerkartenGruppenprozesseProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { routeKatalogVermerkarten } from "./RouteKatalogVermerkarten";

const SVermerkartenGruppenprozesse = () => import("~/components/schule/schulbezogen/vermerkarten/gruppenprozesse/SVermerkartenGruppenprozesse.vue");

export class RouteKatalogVermerkartenGruppenprozesse extends RouteNode<any, RouteKatalogVermerkarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ], "schule.vermerkarten.gruppenprozesse", "gruppenprozesse", SVermerkartenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }};
	}

	public getProps(to: RouteLocationNormalized): SchuleVermerkartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulgliederungen: api.schulgliederungen,
			manager: () => routeKatalogVermerkarten.data.manager,
			delete: routeKatalogVermerkarten.data.delete,
		};
	}

}

export const routeKatalogVermerkartenGruppenprozesse = new RouteKatalogVermerkartenGruppenprozesse();

