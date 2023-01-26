import { routeSchule } from "~/router/apps/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";
import { routeKataloge } from "~/router/apps/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligion";
import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { routeKatalogFoerderschwerpunkte } from "./apps/RouteKatalogFoerderschwerpunkte";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import { routeKlassen } from "~/router/apps/RouteKlassen";
import { routeKurse } from "~/router/apps/RouteKurse";
import { routeGost } from "~/router/apps/RouteGost";
import { routeStatistik } from "~/router/apps/RouteStatistik";
import { RouteNode } from "~/router/RouteNode";

import SApp from "~/components/SApp.vue";
import { RouteLocationRaw } from "vue-router";


export class RouteApp extends RouteNode<unknown, any> {

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
			routeKatalogFoerderschwerpunkte,
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
		super.defaultChild = routeSchueler;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

}

export const routeApp = new RouteApp();
