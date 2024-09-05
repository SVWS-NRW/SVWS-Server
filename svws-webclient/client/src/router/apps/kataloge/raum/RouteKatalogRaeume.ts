import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

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
import { routeRaumStundenplan } from "./stundenplan/RouteRaumStundenplan";
import { routeError } from "~/router/error/RouteError";



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
			routeKatalogRaumDaten,
			routeRaumStundenplan,
		];
		super.defaultChild = routeKatalogRaumDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering)
				await this.data.ladeListe();
			const { id, idSchuljahresabschnitt } = RouteNode.getIntParams(to_params, ["id", "idSchuljahresabschnitt"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			const eintrag = (id !== undefined) ? this.data.raumListeManager.liste.get(id) : null;
			this.data.setEintrag(eintrag);
			if (!this.data.raumListeManager.hasDaten()) {
				if (id === undefined) {
					if (this.data.raumListeManager.filtered().isEmpty())
						return;
					return this.getRoute(this.data.raumListeManager.filtered().get(0).id);
				}
				return this.getRoute();
			}
			if (to.name === this.name)
				return this.getChildRoute(this.data.raumListeManager.daten().id, from);
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}


	public getRoute(id?: number) : RouteLocationRaw {
		if (id === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<any, any>) : RouteLocationRaw {
		if (from !== undefined && (/(\.|^)stundenplan/).test(from.name))
			return { name: routeRaumStundenplan.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
		const redirect_name: string = (this.selectedChild === undefined) ? routeKatalogRaumDaten.name : this.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): RaeumeAuswahlProps {
		return {
			raumListeManager: () => this.data.raumListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			returnToKataloge: routeKataloge.returnToKataloge,
			setKatalogRaeumeImportJSON: this.data.setKatalogRaeumeImportJSON,
		};
	}

	public getProps(to: RouteLocationNormalized): RaeumeAppProps {
		return {
			raumListeManager: () => this.data.raumListeManager,
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
		const id = this.data.raumListeManager.auswahlID();
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } });
		this.data.setView(node, this.children);
	}
}

export const routeKatalogRaeume = new RouteKatalogRaeume();
