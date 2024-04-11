import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { StundenplanPausenzeit } from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogPausenzeitDaten } from "~/router/apps/kataloge/pausenzeit/RouteKatalogPausenzeitDaten";

import type { PausenzeitenAuswahlProps } from "~/components/kataloge/pausenzeiten/SPausenzeitenAuswahlProps";
import type { PausenzeitenAppProps } from "~/components/kataloge/pausenzeiten/SPausenzeitenAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import { RouteDataKatalogPausenzeiten } from "./RouteDataKatalogPausenzeiten";

const SPausenzeitenAuswahl = () => import("~/components/kataloge/pausenzeiten/SPausenzeitenAuswahl.vue")
const SPausenzeitenApp = () => import("~/components/kataloge/pausenzeiten/SPausenzeitenApp.vue")

export class RouteKatalogPausenzeiten extends RouteNode<RouteDataKatalogPausenzeiten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.pausenzeiten", "kataloge/pausenzeiten/:id(\\d+)?", SPausenzeitenApp, new RouteDataKatalogPausenzeiten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausenzeiten";
		super.setView("liste", SPausenzeitenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogPausenzeitDaten
		];
		super.defaultChild = routeKatalogPausenzeitDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.stundenplanManager.pausenzeitGetMengeAsList().isEmpty())
			return;
		let eintrag: StundenplanPausenzeit | null = null;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.stundenplanManager.pausenzeitGetMengeAsList().getFirst();
			return this.getRoute(eintrag?.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.stundenplanManager.pausenzeitGetByIdOrException(id);
		}
		if (eintrag !== undefined)
			await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		const name = (this.data.stundenplanManager.pausenzeitGetMengeAsList().isEmpty()) ? this.name : this.defaultChild!.name;
		return { name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): PausenzeitenAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			returnToKataloge: routeKataloge.returnToKataloge,
			setKatalogPausenzeitenImportJSON: this.data.setKatalogRaeumeImportJSON,
			stundenplanManager: () => this.data.stundenplanManager,
		};
	}

	public getProps(to: RouteLocationNormalized): PausenzeitenAppProps {
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
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
		this.data.setView(node, this.children);
	}

}

export const routeKatalogPausenzeiten = new RouteKatalogPausenzeiten();
