import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform, BenutzerKompetenz, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";

import { RouteDataApp } from "~/router/apps/RouteDataApp";

import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/schule/benutzergruppen/RouteSchuleBenutzergruppe";
import { routeSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogFaecher } from "~/router/apps/kataloge/faecher/RouteKatalogFaecher";
import { routeKatalogBetriebe } from "~/router/apps/kataloge/betriebe/RouteKatalogBetriebe";
import { routeKatalogReligion } from "~/router/apps/kataloge/religion/RouteKatalogReligionen";
import { routeKatalogJahrgaenge } from "~/router/apps/kataloge/jahrgaenge/RouteKatalogJahrgaenge";
import { routeKatalogFoerderschwerpunkte } from "~/router/apps/kataloge/foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeKatalogRaeume } from "~/router/apps/kataloge/raum/RouteKatalogRaeume";
import { routeKatalogAufsichtsbereiche } from "~/router/apps/kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereiche";
import { routeKatalogPausenzeiten } from "~/router/apps/kataloge/pausenzeit/RouteKatalogPausenzeiten";
import { routeKatalogZeitraster } from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitraster";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import { routeKlassen } from "~/router/apps/RouteKlassen";
import { routeKurse } from "~/router/apps/RouteKurse";
import { routeGost } from "~/router/apps/gost/RouteGost";
import { routeStatistik } from "~/router/apps/RouteStatistik";
import { routeStundenplan } from "~/router/apps/RouteStundenplan";
import { routeLogin } from "~/router/login/RouteLogin";

import { ConfigElement } from "~/components/Config";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

import type { AppProps } from "~/components/SAppProps";
import SApp from "~/components/SApp.vue";


export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "app", "/", SApp, new RouteDataApp());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS-Client";
		super.children = [
			routeSchule,
			routeSchuleBenutzer,
			routeSchuleBenutzergruppe,
			routeSchuleDatenaustausch,
			routeKataloge,
			routeKatalogFaecher,
			routeKatalogReligion,
			routeKatalogBetriebe,
			routeKatalogJahrgaenge,
			routeKatalogFoerderschwerpunkte,
			routeKatalogRaeume,
			routeKatalogAufsichtsbereiche,
			routeKatalogPausenzeiten,
			routeKatalogZeitraster,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik,
			routeStundenplan,
		];
		super.menu = [
			routeSchule,
			routeKataloge,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik,
			routeStundenplan,
		];
		super.defaultChild = routeSchueler;
		api.config.addElements([
			new ConfigElement("app.akt_abschnitt", "user", "")
		]);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.setSchuljahresabschnitt(this.data.aktAbschnitt.value.id);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		let cur: RouteNode<unknown, any> = to;
		while (cur.parent !== this)
		  cur = cur.parent;
		if (cur !== this.data.view)
			await this.data.setView(cur);
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await this.data.setSchuljahresabschnitt();
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
			// Props für die Navigation
			setApp: this.setApp,
			app: this.getApp(),
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
		};
	}

	private getApp(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getApps(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.menu) {
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		}
		return result;
	}

	private setApp = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {  } });
		await this.data.setView(node);
	}

}

export const routeApp = new RouteApp();
