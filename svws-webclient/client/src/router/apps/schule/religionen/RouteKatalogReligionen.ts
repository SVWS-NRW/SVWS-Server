import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogReligionDaten } from "~/router/apps/schule/religionen/RouteKatalogReligionDaten";

import type { TabData } from "@ui";
import type { ReligionenAppProps } from "~/components/schule/kataloge/religionen/SReligionenAppProps";
import type { ReligionenAuswahlProps } from "~/components/schule/kataloge/religionen/SReligionenAuswahlPops";
import { RouteDataKatalogReligionen } from "./RouteDataKatalogReligionen";
import { routeSchule } from "../RouteSchule";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SReligionenAuswahl = () => import("~/components/schule/kataloge/religionen/SReligionenAuswahl.vue");
const SReligionenApp = () => import("~/components/schule/kataloge/religionen/SReligionenApp.vue");

export class RouteKatalogReligionen extends RouteNode<RouteDataKatalogReligionen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.religionen", "schule/religion/:id(\\d+)?", SReligionenApp, new RouteDataKatalogReligionen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religionen";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.setView("submenu", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.setView("liste", SReligionenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogReligionDaten,
		];
		super.defaultChild = routeKatalogReligionDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeListe();
			if (id === undefined) {
				await this.data.ladeListe();
			} else {
				const eintrag = this.data.religionListeManager.liste.get(id);
				if ((eintrag === null) && (this.data.religionListeManager.auswahlID() !== null)) {
					await this.data.ladeListe();
					return this.getRouteDefaultChild();
				} else if (eintrag !== null)
					this.data.setEintrag(eintrag);
			}
			if ((to.name === this.name) && (this.data.religionListeManager.auswahlID() !== null))
				return this.getRouteDefaultChild();
		} catch (error) {
			return routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.religionListeManager.auswahlID() ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): ReligionenAuswahlProps {
		return {
			religionListeManager: () => this.data.religionListeManager,
			setFilter: this.data.setFilter,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): ReligionenAppProps {
		return {
			religionListeManager: () => this.data.religionListeManager,
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

export const routeKatalogReligionen = new RouteKatalogReligionen();
