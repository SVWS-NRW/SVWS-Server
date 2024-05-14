import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeKlasseDaten } from "~/router/apps/klassen/RouteKlasseDaten";
import { routeKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplan";
import { RouteDataKlassen } from "~/router/apps/klassen/RouteDataKlassen";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { KlassenAppProps } from "~/components/klassen/SKlassenAppProps";
import type { KlassenAuswahlProps } from "~/components/klassen/SKlassenAuswahlProps";
import { routeError } from "~/router/error/RouteError";
import { routeKlasseGruppenprozesse } from "./RouteKlassenGruppenprozesse";


const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNode<RouteDataKlassen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen", "klassen/:id(\\d+)?", SKlassenApp, new RouteDataKlassen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
		super.setView("liste", SKlassenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKlasseDaten,
			routeKlassenStundenplan,
			routeKlasseGruppenprozesse,
		];
		super.defaultChild = routeKlasseDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		const idSchuljahresabschnitt = RouteNode.getIntParam(to_params, "idSchuljahresabschnitt");
		if (idSchuljahresabschnitt instanceof Error)
			return routeError.getRoute(idSchuljahresabschnitt);
		if (idSchuljahresabschnitt === undefined)
			return routeError.getRoute(new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt."));
		const id = RouteNode.getIntParam(to_params, "id");
		if (id instanceof Error)
			return routeError.getRoute(id);
		if (this.data.idSchuljahresabschnitt !== idSchuljahresabschnitt) {
			const neueID = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
			if (id !== undefined) {
				if (neueID === null)
					return this.getRoute();
				const params = { ... to_params};
				params.id = String(neueID);
				const locationRaw : RouteLocationRaw = {};
				locationRaw.name = to.name;
				locationRaw.params = params;
				return locationRaw;
			}
		}
		const eintrag = (id !== undefined) ? this.data.klassenListeManager.liste.get(id) : null;
		await this.data.setEintrag(eintrag);
		if (!this.data.klassenListeManager.hasDaten()) {
			if (id === undefined) {
				const listFiltered = this.data.klassenListeManager.filtered();
				if (listFiltered.isEmpty())
					return;
				return this.getChildRoute(listFiltered.get(0).id, from);
			}
			return this.getRoute();
		}
		if (to.name === this.name)
			return this.getChildRoute(this.data.klassenListeManager.daten().id, from);
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					this.data.setView(child, this.children);
	}

	public getRoute(id?: number) : RouteLocationRaw {
		if (id === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<any, any>) : RouteLocationRaw {
		if (from !== undefined && (/(\.|^)stundenplan/).test(from.name))
			return { name: routeKlassenStundenplan.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
		const redirect_name: string = (routeKlassen.selectedChild === undefined) ? routeKlasseDaten.name : routeKlassen.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): KlassenAuswahlProps {
		return {
			serverMode: api.mode,
			klassenListeManager: () => this.data.klassenListeManager,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			setFilter: this.data.setFilter,
			setGruppenprozess: this.data.setGruppenprozess,
			setzeDefaultSortierung: this.data.setzeDefaultSortierung,
		};
	}

	public getProps(to: RouteLocationNormalized): KlassenAppProps {
		return {
			klassenListeManager: () => this.data.klassenListeManager,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsGruppenprozesse: this.getTabsGruppenprozesse(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private isGruppenprozess(child : RouteNode<any, any>) : boolean {
		return (child === routeKlasseGruppenprozesse);
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.children) {
			if (!this.isGruppenprozess(c) && c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		}
		return result;
	}

	private getTabsGruppenprozesse(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.children) {
			if (this.isGruppenprozess(c) && c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		}
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.klassenListeManager.auswahlID() } });
		this.data.setView(node, this.children);
	}

}

export const routeKlassen = new RouteKlassen();
