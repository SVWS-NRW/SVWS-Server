import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogVermerkartenDaten } from "~/router/apps/schule/vermerke/RouteKatalogVermerkartenDaten";

import type { TabData } from "@ui";
import type { VermerkeAppProps } from "~/components/schule/kataloge/vermerke/SVermerkeAppProps";
import type { VermerkeAuswahlProps } from "~/components/schule/kataloge/vermerke/SVermerkeAuswahlProps";
import { RouteDataKatalogVermerke } from "./RouteDataKatalogVermerke";

import { routeError } from "~/router/error/RouteError";
import { routeSchule } from "../RouteSchule";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";

const SVermerkAuswahl = () => import("~/components/schule/kataloge/vermerke/SVermerkeAuswahl.vue")
const SVermerkApp = () => import("~/components/schule/kataloge/vermerke/SVermerkeApp.vue")

export class RouteKatalogVermerkarten extends RouteNode<RouteDataKatalogVermerke, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.vermerkarten", "schule/vermerkarten/:id(\\d+)?", SVermerkApp, new RouteDataKatalogVermerke());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkarten";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SVermerkAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogVermerkartenDaten
		];
		super.defaultChild = routeKatalogVermerkartenDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idVermerkart } = RouteNode.getIntParams(to_params, ["idVermerkart"]);
			if (idVermerkart === undefined) {
				await this.data.ladeListe();
			} else {
				if (!this.data.vermerkartenManager.hasDaten())
					await this.data.ladeListe();
				const eintrag = this.data.vermerkartenManager.liste.get(idVermerkart)
				if (eintrag) {
					return await this.data.setEintrag(eintrag);
				}
			}
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}

		if ((to.name === this.name)) {
			const route = this.getRoute(this.data.vermerkartenManager.auswahl().id)
			return route;
		}
	}

	public getRoute(id: number|undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): VermerkeAuswahlProps {
		return {
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			gotoEintrag: this.data.gotoEintrag,
			gotoSchule: routeSchule.gotoSchule,
			vermerkartenManager: () => this.data.vermerkartenManager,
			commit: this.data.enforceCommit,
		};
	}

	public getProps(to: RouteLocationNormalized): VermerkeAppProps {
		return {
			vermerkartenManager: () => this.data.vermerkartenManager,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		const manager = this.data.vermerkartenManager;
		const id = (manager.auswahlID() === null) ? undefined : manager.auswahlID.toString();
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id } });
		this.data.setView(node, this.children);
	}
}

export const routeKatalogVermerkarten = new RouteKatalogVermerkarten();
