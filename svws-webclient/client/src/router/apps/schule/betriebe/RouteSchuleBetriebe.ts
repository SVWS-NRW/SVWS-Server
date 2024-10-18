import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { BetriebListeEintrag} from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import type { TabData } from "@ui";
import type { BetriebeAppProps } from "~/components/schule/betriebe/SBetriebeAppProps";
import type { BetriebeAuswahlProps } from "~/components/schule/betriebe/SBetriebeAuswahlProps";
import { RouteDataSchuleBetriebe } from "./RouteDataSchuleBetriebe";
import { routeSchuleBetriebeDaten } from "./RouteSchuleBetriebeDaten";
import { routeSchule } from "../RouteSchule";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SBetriebeAuswahl = () => import("~/components/schule/betriebe/SBetriebeAuswahl.vue")
const SBetriebeApp = () => import("~/components/schule/betriebe/SBetriebeApp.vue")

export class RouteSchuleBetriebe extends RouteNode<RouteDataSchuleBetriebe, RouteApp>{

	public constructor(){
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.betriebe", "schule/betriebe/:id(\\d+)?", SBetriebeApp, new RouteDataSchuleBetriebe());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text="Betriebe";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("submenu", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.setView("liste", SBetriebeAuswahl, (route) => this.getAuswahlProps(route));

		super.children = [
			routeSchuleBetriebeDaten
		];
		super.defaultChild = routeSchuleBetriebeDaten;

	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeListe();
			if (this.data.mapKatalogeintraege.size < 1)
				return;
			let eintrag: BetriebListeEintrag | undefined;
			if ((id === undefined) && this.data.auswahl)
				return this.getRoute(this.data.auswahl.id);
			if (id === undefined) {
				eintrag = this.data.mapKatalogeintraege.get(0);
				return this.getRoute(eintrag?.id);
			}
			else {
				eintrag = this.data.mapKatalogeintraege.get(id);
				if (eintrag === undefined)
					return;
			}
			await this.data.setEintrag(eintrag);
		} catch(e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BetriebeAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			mapBeschaeftigungsarten: this.data.mapBeschaeftigungsarten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): BetriebeAppProps {
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
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
		this.data.setView(node, this.children);
	}


}
export const routeSchuleBetriebe = new RouteSchuleBetriebe();