import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import { routeKatalogFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeKatalogReligionen } from "~/router/apps/schule/kataloge/religionen/RouteKatalogReligionen";
import { routeKatalogVermerkarten } from "~/router/apps/schule/kataloge/vermerke/RouteKatalogVermerkarten";
import { RouteDataSchuleKataloge } from "~/router/apps/schule/kataloge/RouteDataSchuleKataloge";

import type { KatalogeAuswahlProps } from "~/components/schule/kataloge/SKatalogeAuswahlProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import { routeKatalogSchulen } from "./schulen/RouteKatalogSchulen";
import { routeKatalogEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteKatalogEinwilligungsarten";
import { routeSchule } from "../RouteSchule";


const SKatalogeAuswahl = () => import("~/components/schule/kataloge/SKatalogeAuswahl.vue")
const SKatalogeApp = () => import("~/components/schule/kataloge/SKatalogeApp.vue")

export class RouteSchuleKataloge extends RouteNode<RouteDataSchuleKataloge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.kataloge", "schule/kataloge", SKatalogeApp, new RouteDataSchuleKataloge());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "weitere Kataloge";
		super.setView("liste", SKatalogeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
		super.menu = [
			routeKatalogReligionen,
			routeKatalogEinwilligungsarten,
			routeKatalogVermerkarten,
			routeKatalogFoerderschwerpunkte,
			routeKatalogSchulen,
		];
		super.defaultChild = undefined;
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): KatalogeAuswahlProps {
		return {
			returnToSchule: routeSchule.gotoSchule,
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

export const routeSchuleKataloge = new RouteSchuleKataloge();
