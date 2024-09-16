import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { StundenplanKatalogeAuswahlProps } from "~/components/stundenplan/kataloge/SStundenplanKatalogeAuswahlProps";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";

import { RouteDataStundenplanKataloge } from "./RouteDataStundenplanKataloge";
import { routeKatalogPausenzeiten } from "./pausenzeit/RouteKatalogPausenzeiten";
import { routeKatalogAufsichtsbereiche } from "./aufsichtsbereich/RouteKatalogAufsichtsbereiche";
import { routeKatalogRaeume } from "./raum/RouteKatalogRaeume";
import { routeKatalogZeitraster } from "./zeitraster/RouteKatalogZeitraster";
import { routeStundenplan } from "../RouteStundenplan";

const SStundenplanKatalogeAuswahl = () => import("~/components/stundenplan/kataloge/SStundenplanKatalogeAuswahl.vue")
const SStundenplanKatalogeApp = () => import("~/components/stundenplan/kataloge/SStundenplanKatalogeApp.vue")

export class RouteStundenplanKataloge extends RouteNode<RouteDataStundenplanKataloge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "stundenplan.kataloge", "stundenplan/kataloge", SStundenplanKatalogeApp, new RouteDataStundenplanKataloge());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Kataloge";
		super.setView("liste", SStundenplanKatalogeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
		super.menu = [
			routeKatalogAufsichtsbereiche,
			routeKatalogPausenzeiten,
			routeKatalogRaeume,
			routeKatalogZeitraster,
		];
		super.defaultChild = undefined;
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): StundenplanKatalogeAuswahlProps {
		return {
			returnToStundenplan: routeStundenplan.returnToStundenplan,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
			childrenHidden: this.children_hidden().value,
		};
	}

	private getChild(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getChildData(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.menu)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: AuswahlChildData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, this.menu);
	}

	returnToKataloge = async () => await RouteManager.doRoute({ name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
}

export const routeStundenplanKataloge = new RouteStundenplanKataloge();
