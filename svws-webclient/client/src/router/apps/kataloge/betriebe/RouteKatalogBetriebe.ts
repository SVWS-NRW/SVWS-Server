import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { BetriebListeEintrag} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogBetriebeDaten } from "~/router/apps/kataloge/betriebe/RouteKatalogBetriebeDaten";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { BetriebeAppProps } from "~/components/kataloge/betriebe/SBetriebeAppProps";
import type { BetriebeAuswahlProps } from "~/components/kataloge/betriebe/SBetriebeAuswahlProps";
import { RouteDataKatalogBetriebe } from "./RouteDataKatalogBetriebe";


const SBetriebeAuswahl = () => import("~/components/kataloge/betriebe/SBetriebeAuswahl.vue")
const SBetriebeApp = () => import("~/components/kataloge/betriebe/SBetriebeApp.vue")

export class RouteKatalogBetriebe extends RouteNode<RouteDataKatalogBetriebe, RouteApp>{

	public constructor(){
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.betriebe", "/kataloge/betriebe/:id(\\d+)?", SBetriebeApp, new RouteDataKatalogBetriebe());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text="Betriebe";
		super.setView("liste", SBetriebeAuswahl, (route) => this.getAuswahlProps(route));

		super.children = [
			routeKatalogBetriebeDaten
		];
		super.defaultChild = routeKatalogBetriebeDaten;

	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: BetriebListeEintrag | undefined;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.mapKatalogeintraege.get(0);
			return this.getRoute(eintrag?.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined) {
				return;
			}
		}
		if (eintrag !== undefined)
			await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BetriebeAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			mapBeschaeftigungsarten: this.data.mapBeschaeftigungsarten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			gotoEintrag: this.data.gotoEintrag,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): BetriebeAppProps {
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
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
		await this.data.setView(node, this.children);
	}


}
export const routeKatalogBetriebe = new RouteKatalogBetriebe();