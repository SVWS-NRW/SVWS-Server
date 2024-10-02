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
			routeKatalogAufsichtsbereiche,
			routeKatalogPausenzeiten,
			routeKatalogRaeume,
			routeKatalogZeitraster,
		];
		super.defaultChild = routeKatalogAufsichtsbereiche;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		const { idSchuljahresabschnitt } = RouteNode.getIntParams(to_params, [ "idSchuljahresabschnitt" ]);
		if (idSchuljahresabschnitt === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
		await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
		if (to.name === this.name)
			return this.getRoute();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined) : RouteLocationRaw {
		const redirect_name = (routeStundenplanKataloge.selectedChild === undefined) ? routeKatalogAufsichtsbereiche.name : routeStundenplanKataloge.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
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
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): TabData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): TabData[] {
		const result: TabData[] = [];
		for (const c of super.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, this.children);
	}

}

export const routeStundenplanKataloge = new RouteStundenplanKataloge();
