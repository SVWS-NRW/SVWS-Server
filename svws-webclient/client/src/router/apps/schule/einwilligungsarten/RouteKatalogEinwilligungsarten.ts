import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogEinwilligungsartenDaten } from "~/router/apps/schule/einwilligungsarten/RouteKatalogEinwilligungsartenDaten";

import type { TabData } from "@ui";
import type { SEinwilligungsartenAppProps } from "~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenAppProps";
import type { SEinwilligungsartenAuswahlProps } from "~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenAuswahlProps";
import { RouteDataKatalogEinwilligungsarten } from "./RouteDataKatalogEinwilligungsarten";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";

const SEinwilligungsartenAuswahl = () => import("~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenAuswahl.vue");
const SEinwilligungsartenApp = () => import("~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenApp.vue");

export class RouteKatalogEinwilligungsarten extends RouteNode<RouteDataKatalogEinwilligungsarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.einwilligungsarten", "schule/einwilligungsarten/:id(\\d+)?", SEinwilligungsartenApp, new RouteDataKatalogEinwilligungsarten());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsarten";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SEinwilligungsartenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogEinwilligungsartenDaten,
		];
		super.defaultChild = routeKatalogEinwilligungsartenDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined || isEntering)
				await this.data.ladeListe();
			else {
				const eintrag = this.data.mapKatalogeintraege.get(id);
				if ((eintrag === undefined) && (this.data.auswahl !== undefined)) {
					await this.data.ladeListe();
					return this.getRouteDefaultChild();
				} else if (eintrag)
					this.data.setEintrag(eintrag);
			}
			if ((to.name === this.name) && (this.data.auswahl !== undefined))
				return this.getRouteDefaultChild();
		} catch (error) {
			return routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.auswahl?.id ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SEinwilligungsartenAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			auswahl: () => this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): SEinwilligungsartenAppProps {
		return {
			auswahl: () => this.data.auswahl,
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

export const routeKatalogEinwilligungsarten = new RouteKatalogEinwilligungsarten();
