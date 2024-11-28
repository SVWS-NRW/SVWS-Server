import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import type { FoerderschwerpunktEintrag } from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogFoerderschwerpunktDaten } from "~/router/apps/schule/foerderschwerpunkte/RouteKatalogFoerderschwerpunktDaten";

import type { TabData } from "@ui";
import type { FoerderschwerpunkteAppProps } from "~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAppProps";
import type { FoerderschwerpunkteAuswahlProps } from "~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahlProps";
import { RouteDataKatalogFoerderschwerpunkte } from "./RouteDataKatalogFoerderschwerpunkte";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";

const SFoerderschwerpunkteAuswahl = () => import("~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahl.vue");
const SFoerderschwerpunkteApp = () => import("~/components/schule/kataloge/foerderschwerpunkte/SFoerderschwerpunkteApp.vue");

export class RouteKatalogFoerderschwerpunkte extends RouteNode<RouteDataKatalogFoerderschwerpunkte, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.foerderschwerpunkte", "schule/foerderschwerpunkte/:id(\\d+)?", SFoerderschwerpunkteApp, new RouteDataKatalogFoerderschwerpunkte());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SFoerderschwerpunkteAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogFoerderschwerpunktDaten,
		];
		super.defaultChild = routeKatalogFoerderschwerpunktDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeListe();
			if (this.data.mapKatalogeintraege.size < 1)
				return;
			let eintrag: FoerderschwerpunktEintrag | undefined;
			if ((id === undefined) && this.data.auswahl)
				return this.getRouteDefaultChild({ id: this.data.auswahl.id });
			if (id === undefined) {
				eintrag = this.data.mapKatalogeintraege.get(0);
				return this.getRouteDefaultChild({ id: eintrag?.id });
			}
			eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined)
				return;
			await this.data.setEintrag(eintrag);
			if (to.name === this.name)
				return this.getRouteDefaultChild();
		} catch (error) {
			return routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.auswahl?.id ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): FoerderschwerpunkteAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteAppProps {
		return {
			auswahl: this.data.auswahl,
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

export const routeKatalogFoerderschwerpunkte = new RouteKatalogFoerderschwerpunkte();
