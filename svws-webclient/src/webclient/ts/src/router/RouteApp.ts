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
import { RouteLocationRaw, RouteParams } from "vue-router";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
import { App } from "~/apps/BaseApp";


export class RouteApp extends RouteNode<unknown, any> {
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();

	public constructor() {
		super("app", "/", SApp);
		super.propHandler = (route) => this.getProps();
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

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.schule.select(true);
		const id = this.schule.daten?.idSchuljahresabschnitt;
		const a = this.schule.daten?.abschnitte
			.toArray(new Array<Schuljahresabschnitt>())
			.find(e => e.id === id);
		if (a) App.akt_abschnitt = a;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getProps(): Record<string, any> {
		// TODO
		return {
			schule: this.schule,
		};
	}
}

export const routeApp = new RouteApp();
