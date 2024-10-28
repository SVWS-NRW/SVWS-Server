import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import type { JahrgangsDaten } from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleJahrgaengeDaten } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaengeDaten";

import type { TabData } from "@ui";
import type { JahrgaengeAppProps } from "~/components/schule/jahrgaenge/SJahrgaengeAppProps";
import type { JahrgaengeAuswahlProps } from "~/components/schule/jahrgaenge/SJahrgaengeAuswahlProps";
import { RouteDataSchuleJahrgaenge } from "./RouteDataSchuleJahrgaenge";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SJahrgaengeAuswahl = () => import("~/components/schule/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/schule/jahrgaenge/SJahrgaengeApp.vue")

export class RouteSchuleJahrgaenge extends RouteNode<RouteDataSchuleJahrgaenge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.jahrgaenge", "schule/jahrgaenge/:id(\\d+)?", SJahrgaengeApp, new RouteDataSchuleJahrgaenge());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgänge";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("submenu", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.setView("liste", SJahrgaengeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleJahrgaengeDaten,
		];
		super.defaultChild = routeSchuleJahrgaengeDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeListe();
			if (this.data.mapKatalogeintraege.size < 1)
				return;
			let eintrag: JahrgangsDaten | undefined;
			if ((id === undefined) && (this.data.auswahl !== undefined))
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

	public getAuswahlProps(to: RouteLocationNormalized): JahrgaengeAuswahlProps {
		return {
			auswahl: () => this.data.auswahl,
			mapKatalogeintraege: () => this.data.mapKatalogeintraege,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeAppProps {
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

export const routeSchuleJahrgaenge = new RouteSchuleJahrgaenge();
