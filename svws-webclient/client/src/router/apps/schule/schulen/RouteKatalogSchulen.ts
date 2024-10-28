import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogSchuleDaten } from "~/router/apps/schule/schulen/RouteKatalogSchuleDaten";

import type { TabData } from "@ui";
import type { SchulenAppProps } from "~/components/schule/kataloge/schulen/SSchulenAppProps";
import type { SchulenAuswahlProps } from "~/components/schule/kataloge/schulen/SSchulenAuswahlProps";
import { RouteDataKatalogSchulen } from "./RouteDataKatalogSchulen";
import { routeSchule } from "../RouteSchule";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SSchulenAuswahl = () => import("~/components/schule/kataloge/schulen/SSchulenAuswahl.vue");
const SSchulenApp = () => import("~/components/schule/kataloge/schulen/SSchulenApp.vue");

export class RouteKatalogSchulen extends RouteNode<RouteDataKatalogSchulen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.schulen", "schule/schulen/:id(\\d+)?", SSchulenApp, new RouteDataKatalogSchulen());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulen";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.setView("submenu", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.setView("liste", SSchulenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogSchuleDaten,
		];
		super.defaultChild = routeKatalogSchuleDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if ((id === undefined) || isEntering)
				await this.data.ladeListe();
			else {
				const eintrag = this.data.mapKatalogeintraege.get(id);
				if (eintrag === undefined && this.data.auswahl !== undefined) {
					await this.data.ladeListe();
					return this.getRouteDefaultChild();
				}
				else if (eintrag)
					this.data.setEintrag(eintrag);
			}
			if (to.name === this.name && this.data.auswahl !== undefined)
				return this.getRouteDefaultChild();
		} catch (error) {
			return routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.auswahl?.id ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchulenAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () => this.data.mapKatalogeintraege,
			removeEintraege: this.data.removeEintraege,
			addEintrag: this.data.addEintrag,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): SchulenAppProps {
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

export const routeKatalogSchulen = new RouteKatalogSchulen();
