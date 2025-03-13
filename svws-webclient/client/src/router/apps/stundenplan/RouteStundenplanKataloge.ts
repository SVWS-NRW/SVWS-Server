import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { TabData } from "@ui";
import type { StundenplanKatalogeAppProps } from "~/components/stundenplan/SStundenplanKatalogeAppProps";
import type { StundenplanAuswahlProps } from "~/components/stundenplan/SStundenplanAuswahlProps";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";

import { routeKatalogPausenzeiten } from "./kataloge/RouteKatalogPausenzeiten";
import { routeKatalogAufsichtsbereiche } from "./kataloge/RouteKatalogAufsichtsbereiche";
import { routeKatalogRaeume } from "./kataloge/RouteKatalogRaeume";
import { routeKatalogZeitraster } from "./kataloge/RouteKatalogZeitraster";
import type { RouteStundenplan } from "./RouteStundenplan";
import { api } from "~/router/Api";
import { RouteDataStundenplanKataloge } from "./RouteDataStundenplanKataloge";

const SStundenplanAuswahl = () => import("~/components/stundenplan/SStundenplanAuswahl.vue")
const SStundenplanKatalogeApp = () => import("~/components/stundenplan/SStundenplanKatalogeApp.vue")

export class RouteStundenplanKataloge extends RouteNode<RouteDataStundenplanKataloge, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "stundenplan.kataloge", "stundenplan/kataloge", SStundenplanKatalogeApp, new RouteDataStundenplanKataloge());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kataloge";
		super.setView("liste", SStundenplanAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogZeitraster,
			routeKatalogPausenzeiten,
			routeKatalogAufsichtsbereiche,
			routeKatalogRaeume,
		];
		super.defaultChild = routeKatalogZeitraster;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		const { idSchuljahresabschnitt } = RouteNode.getIntParams(to_params, [ "idSchuljahresabschnitt" ]);
		if (idSchuljahresabschnitt === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
		await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
		if (to.name === this.name)
			return this.getRouteDefaultChild();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public getAuswahlProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			serverMode: api.mode,
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () => this.data.mapKatalogeintraege,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			removeEintraege: this.data.removeEintraege,
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanKatalogeAppProps {
		return {
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(node.getRoute());
		this.data.setView(node, this.children);
	}

}

export const routeStundenplanKataloge = new RouteStundenplanKataloge();
