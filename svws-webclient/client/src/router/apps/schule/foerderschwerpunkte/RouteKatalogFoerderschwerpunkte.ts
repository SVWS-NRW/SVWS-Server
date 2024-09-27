import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { FoerderschwerpunktEintrag} from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogFoerderschwerpunktDaten } from "~/router/apps/schule/foerderschwerpunkte/RouteKatalogFoerderschwerpunktDaten";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { FoerderschwerpunkteAppProps } from "~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAppProps";
import type { FoerderschwerpunkteAuswahlProps } from "~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahlProps";
import { RouteDataKatalogFoerderschwerpunkte } from "./RouteDataKatalogFoerderschwerpunkte";
import { routeSchule } from "../RouteSchule";



const SFoerderschwerpunkteAuswahl = () => import("~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahl.vue")
const SFoerderschwerpunkteApp = () => import("~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteApp.vue")

export class RouteKatalogFoerderschwerpunkte extends RouteNode<RouteDataKatalogFoerderschwerpunkte, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.foerderschwerpunkte", "schule/foerderschwerpunkte/:id(\\d+)?", SFoerderschwerpunkteApp, new RouteDataKatalogFoerderschwerpunkte());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.setView("liste", SFoerderschwerpunkteAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogFoerderschwerpunktDaten
		];
		super.defaultChild = routeKatalogFoerderschwerpunktDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: FoerderschwerpunktEintrag | undefined;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.mapKatalogeintraege.get(0);
			return this.getRoute(eintrag?.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined)
				return this.getRoute(undefined);
		}
		if (eintrag !== undefined)
			await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): FoerderschwerpunkteAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			gotoSchule: routeSchule.gotoSchule,
		};
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteAppProps {
		return {
			auswahl: this.data.auswahl,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
		this.data.setView(node, this.children);
	}
}

export const routeKatalogFoerderschwerpunkte = new RouteKatalogFoerderschwerpunkte();
