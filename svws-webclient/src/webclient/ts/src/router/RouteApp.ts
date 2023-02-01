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
import type { RouteLocationRaw, RouteParams } from "vue-router";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { type List, OrtKatalogEintrag, type OrtsteilKatalogEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { App } from "~/apps/BaseApp";

export class RouteDataApp {
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	orte: List<OrtKatalogEintrag> = new Vector();
	ortsteile: List<OrtsteilKatalogEintrag> = new Vector();
}
export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super("app", "/", SApp, new RouteDataApp());
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
		await this.data.schule.select(true);    // TODO Kein Data-Objekt, sondern Handhabung des aktuellen Abschnitts, etc. in dieser Route (RouteDataApp-Objekt)
		this.data.orte = await App.api.getOrte(App.schema);
		this.data.ortsteile = await App.api.getOrtsteile(App.schema);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await this.data.schule.unselect();
		this.data.orte = new Vector<OrtKatalogEintrag>();
		this.data.ortsteile = new Vector<OrtsteilKatalogEintrag>();
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getProps(): Record<string, any> {
		return {
			schule: this.data.schule,
			orte: this.data.orte,
			ortsteile: this.data.ortsteile
		};
	}
}

export const routeApp = new RouteApp();
