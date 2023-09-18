import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import { routeKatalogFaecher } from "~/router/apps/kataloge/faecher/RouteKatalogFaecher";
import { routeKatalogFoerderschwerpunkte } from "~/router/apps/kataloge/foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeKatalogJahrgaenge } from "~/router/apps/kataloge/jahrgaenge/RouteKatalogJahrgaenge";
import { routeKatalogReligion } from "~/router/apps/kataloge/religion/RouteKatalogReligionen";
import { routeKatalogRaeume } from "~/router/apps/kataloge/raum/RouteKatalogRaeume";
import { routeKatalogAufsichtsbereiche } from "~/router/apps/kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereiche";
import { routeKatalogPausenzeiten } from "~/router/apps/kataloge/pausenzeit/RouteKatalogPausenzeiten";
import { routeKatalogZeitraster } from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitraster";
import { routeKatalogBetriebe } from "~/router/apps/kataloge/betriebe/RouteKatalogBetriebe";

import { RouteDataKataloge } from "~/router/apps/kataloge/RouteDataKataloge";

import type { KatalogeAuswahlProps } from "~/components/kataloge/SKatalogeAuswahlProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";


const SKatalogeAuswahl = () => import("~/components/kataloge/SKatalogeAuswahl.vue")
const SKatalogeApp = () => import("~/components/kataloge/SKatalogeApp.vue")

export class RouteKataloge extends RouteNode<RouteDataKataloge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge", "/kataloge", SKatalogeApp, new RouteDataKataloge());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Kataloge";
		super.setView("liste", SKatalogeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
		super.menu = [
			routeKatalogFaecher,
			routeKatalogReligion,
			routeKatalogJahrgaenge,
			routeKatalogFoerderschwerpunkte,
			routeKatalogRaeume,
			routeKatalogAufsichtsbereiche,
			routeKatalogPausenzeiten,
			routeKatalogZeitraster,
			routeKatalogBetriebe,
			// TODO { title: "Haltestellen", value: "haltestellen" },
		];
		super.defaultChild = undefined;
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): KatalogeAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
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
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {} });
		await this.data.setView(node);
	}

	returnToKataloge = async () => await RouteManager.doRoute({ name: this.name, params: {} });
}

export const routeKataloge = new RouteKataloge();
