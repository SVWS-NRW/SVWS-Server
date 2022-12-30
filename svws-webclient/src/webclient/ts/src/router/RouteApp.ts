import { routeSchule } from "~/router/apps/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";
import { routeKataloge } from "~/router/apps/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligion";
import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import { routeKlassen } from "~/router/apps/RouteKlassen";
import { routeKurse } from "~/router/apps/RouteKurse";
import { routeGost } from "~/router/apps/RouteGost";
import { routeStatistik } from "~/router/apps/RouteStatistik";
import { RouteNode } from "~/router/RouteNode";

import SApp from "~/components/SApp.vue";


export class RouteApp extends RouteNode<unknown> {

	protected defaultChildNode = undefined;

	public constructor() {
		super("app", "/", SApp);
		super.text = "SVWS-Client";
		super.children = [
            routeSchule,
            routeSchuleBenutzer,
            routeSchuleBenutzergruppe,
            routeKataloge,
            routeKatalogFaecher,
            routeKatalogReligion,
            routeKatalogJahrgaenge,
            routeSchueler,
            routeLehrer,
            routeKlassen,
            routeKurse,
            routeGost,
            routeStatistik
        ];
		super.menu = [
            routeSchule,
            routeKataloge,
            routeSchueler,
            routeLehrer,
            routeKlassen,
            routeKurse,
            routeGost,
            routeStatistik
		];
	}

}

export const routeApp = new RouteApp();
