import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

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
import { routeSchule } from "../RouteSchule";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";

const SEinwilligungsartenAuswahl = () => import("~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenAuswahl.vue")
const SEinwilligungsartenApp = () => import("~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenApp.vue")

export class RouteKatalogEinwilligungsarten extends RouteNode<RouteDataKatalogEinwilligungsarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schule.einwilligungsarten", "schule/einwilligungsarten/:id(\\d+)?", SEinwilligungsartenApp, new RouteDataKatalogEinwilligungsarten());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsarten";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SEinwilligungsartenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogEinwilligungsartenDaten
		];
		super.defaultChild = routeKatalogEinwilligungsartenDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.ladeListe();
		} else {
			const id = parseInt(to_params.id);
			const eintrag = this.data.mapKatalogeintraege.get(id);
			if ((eintrag === undefined) && (this.data.auswahl !== undefined)) {
				await this.data.ladeListe();
				return this.getRoute(this.data.auswahl.id);
			} else if (eintrag)
				this.data.setEintrag(eintrag);
		}
		if ((to.name === this.name) && (this.data.auswahl !== undefined))
			return this.getRoute(this.data.auswahl.id);
	}

	public getRoute(id: number | undefined): RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SEinwilligungsartenAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			auswahl: () => this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			gotoEintrag: this.data.gotoEintrag,
			gotoSchule: routeSchule.gotoSchule,
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
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
		this.data.setView(node, this.children);
	}
}

export const routeKatalogEinwilligungsarten = new RouteKatalogEinwilligungsarten();
