import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteDataSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteDataSchuelerIndividualdatenGruppenprozesse";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";

const SSchuelerIndividualdatenGruppenprozesse = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdatenGruppenprozesse.vue");


export class RouteSchuelerIndividualdatenGruppenprozesse extends RouteNode<RouteDataSchuelerIndividualdatenGruppenprozesse, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.gruppenprozesse.daten", "gruppenprozesse/daten", SSchuelerIndividualdatenGruppenprozesse, new RouteDataSchuelerIndividualdatenGruppenprozesse());
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.DEV;
		super.text = "Individualdaten";
	}

}

export const routeSchuelerIndividualdatenGruppenprozesse = new RouteSchuelerIndividualdatenGruppenprozesse();

