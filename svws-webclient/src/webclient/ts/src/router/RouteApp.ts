import { routeSchule } from "~/router/apps/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";
import { routeKataloge } from "~/router/apps/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
import { routeKatalogReligion } from "~/router/apps/RouteKatalogReligionen";
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
import type { AppProps } from "~/components/SAppProps";
import type { RouteLocationRaw, RouteParams } from "vue-router";
import { type List, OrtKatalogEintrag, type OrtsteilKatalogEintrag, Vector, Schuljahresabschnitt, Schulform, BenutzerKompetenz } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "./RouteLogin";
import { computed, WritableComputedRef } from "vue";
import { api } from "./Api";
import { ConfigElement } from "~/components/Config";

export class RouteDataApp {

	orte: List<OrtKatalogEintrag> = new Vector();
	mapOrte: Map<number, OrtKatalogEintrag> = new Map();
	ortsteile: List<OrtsteilKatalogEintrag> = new Vector();
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag> = new Map();

	aktAbschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => {
			let abschnitt = api.config.getObjectValue("app.akt_abschnitt", Schuljahresabschnitt.transpilerFromJSON);
			if (abschnitt === null) {
				void api.config.setObjectValue("app.akt_abschnitt", api.abschnitt, Schuljahresabschnitt.transpilerToJSON);
				abschnitt = api.abschnitt;
			}
			return abschnitt;
		},
		set: (abschnitt: Schuljahresabschnitt) => {
			void api.config.setObjectValue("app.akt_abschnitt", abschnitt, Schuljahresabschnitt.transpilerToJSON);
			// TODO was tun, wenn das akt Halbjahr neu gesetzt wurde?
		}
	})

	setAbschnitt = (abschnitt: Schuljahresabschnitt): void => {
		this.aktAbschnitt.value = abschnitt;
	}
}
export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "app", "/", SApp, new RouteDataApp());
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
		api.config.addElements([
			new ConfigElement("app.akt_abschnitt", "user", "")
		]);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<any> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		// Lade den Katalog der Orte
		this.data.orte = await api.server.getOrte(api.schema);
		this.data.mapOrte = new Map();
		for (const o of this.data.orte)
			this.data.mapOrte.set(o.id, o);
		// Lade den Katalog der Ortsteile
		this.data.ortsteile = await api.server.getOrtsteile(api.schema);
		this.data.mapOrtsteile = new Map();
		for (const o of this.data.ortsteile)
			this.data.mapOrtsteile.set(o.id, o);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		this.data.orte = new Vector<OrtKatalogEintrag>();
		this.data.mapOrte = new Map();
		this.data.ortsteile = new Vector<OrtsteilKatalogEintrag>();
		this.data.mapOrtsteile = new Map();
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getProps(): AppProps {
		return {
			logout: routeLogin.logout,
			username: api.username,
			schulform: api.schulform,
			schuleStammdaten: api.schuleStammdaten,
		};
	}
}

export const routeApp = new RouteApp();
