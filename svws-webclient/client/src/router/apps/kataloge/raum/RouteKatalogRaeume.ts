import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { Raum } from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogRaumDaten } from "~/router/apps/kataloge/raum/RouteKatalogRaumDaten";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RaeumeAppProps } from "~/components/kataloge/raeume/SRaeumeAppProps";
import type { RaeumeAuswahlProps } from "~/components/kataloge/raeume/SRaeumeAuswahlProps";
import { RouteDataKatalogRaeume } from "./RouteDataKatalogRaeume";



const SRaeumeAuswahl = () => import("~/components/kataloge/raeume/SRaeumeAuswahl.vue")
const SRaeumeApp = () => import("~/components/kataloge/raeume/SRaeumeApp.vue")

export class RouteKatalogRaeume extends RouteNode<RouteDataKatalogRaeume, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.raeume", "kataloge/raeume/:id(\\d+)?", SRaeumeApp, new RouteDataKatalogRaeume());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
		super.setView("liste", SRaeumeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogRaumDaten
		];
		super.defaultChild = routeKatalogRaumDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.stundenplanManager.raumGetMengeAsList().isEmpty())
			return;
		let eintrag: Raum | null = null;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.stundenplanManager.raumGetMengeAsList().getFirst();
			return this.getRoute(eintrag?.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.stundenplanManager.raumGetByIdOrException(id);
		}
		if (eintrag !== null)
			await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		const name = (this.data.auswahl === undefined && id === undefined) ? this.name : this.defaultChild!.name;
		return { name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): RaeumeAuswahlProps {
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
			setKatalogRaeumeImportJSON: this.data.setKatalogRaeumeImportJSON,
			stundenplanManager: () => this.data.stundenplanManager,
		};
	}

	public getProps(to: RouteLocationNormalized): RaeumeAppProps {
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

export const routeKatalogRaeume = new RouteKatalogRaeume();
