import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogSchuleDaten } from "~/router/apps/kataloge/schulen/RouteKatalogSchuleDaten";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { SchulenAppProps } from "~/components/kataloge/schulen/SSchulenAppProps";
import type { SchulenAuswahlProps } from "~/components/kataloge/schulen/SSchulenAuswahlProps";
import { RouteDataKatalogSchulen } from "./RouteDataKatalogSchulen";

const SSchulenAuswahl = () => import("~/components/kataloge/schulen/SSchulenAuswahl.vue")
const SSchulenApp = () => import("~/components/kataloge/schulen/SSchulenApp.vue")

export class RouteKatalogSchulen extends RouteNode<RouteDataKatalogSchulen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.schulen", "/kataloge/schulen/:id(\\d+)?", SSchulenApp, new RouteDataKatalogSchulen());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulen";
		super.setView("liste", SSchulenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogSchuleDaten
		];
		super.defaultChild = routeKatalogSchuleDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.ladeListe();
		} else {
			const id = parseInt(to_params.id);
			const eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined && this.data.auswahl !== undefined) {
				await this.data.ladeListe();
				return this.getRoute(this.data.auswahl.id);
			}
			else if (eintrag)
				this.data.setEintrag(eintrag);
		}
		if (to.name === this.name && this.data.auswahl !== undefined)
			return this.getRoute(this.data.auswahl.id);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchulenAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () => this.data.mapKatalogeintraege,
			removeEintraege: this.data.removeEintraege,
			addEintrag: this.data.addEintrag,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): SchulenAppProps {
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
		this.data.setView(node, this.children);
	}
}

export const routeKatalogSchulen = new RouteKatalogSchulen();
