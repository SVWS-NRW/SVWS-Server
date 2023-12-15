import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerStundenplan } from "./stundenplan/RouteLehrerStundenplan";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";

import { RouteDataLehrer } from "~/router/apps/lehrer/RouteDataLehrer";

import type { LehrerAppProps } from "~/components/lehrer/SLehrerAppProps";
import type { LehrerAuswahlProps } from "~/components/lehrer/SLehrerAuswahlProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNode<RouteDataLehrer, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "lehrer", "/lehrkraefte/:id(\\d+)?", SLehrerApp, new RouteDataLehrer());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrkräfte";
		super.setView("liste", SLehrerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerStundenplan,
			routeLehrerUnterrichtsdaten
		];
		super.defaultChild = routeLehrerIndividualdaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.setSchuljahresabschnitt(routeApp.data.aktAbschnitt.value.id);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from?: RouteNode<unknown, any>) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idLehrer = !to_params.id ? undefined : parseInt(to_params.id);
		const eintrag = (idLehrer !== undefined) ? this.data.lehrerListeManager.liste.get(idLehrer) : null;
		await this.data.setLehrer(eintrag);
		if (!this.data.lehrerListeManager.hasDaten()) {
			if (to.name === this.name) {
				const listFiltered = this.data.lehrerListeManager.filtered();
				if (listFiltered.isEmpty())
					return;
				return this.getChildRoute(listFiltered.get(0).id, from);
			}
			return this.getRoute();
		}
		if (to.name === this.name)
			return this.getChildRoute(this.data.lehrerListeManager.daten().id, from);
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					this.data.setView(child, this.children);
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<unknown, any>) : RouteLocationRaw {
		if (from !== undefined && (/(\.|^)stundenplan/).test(from.name))
			return { name: routeLehrerStundenplan.name, params: { id } };
		const redirect_name: string = (routeLehrer.selectedChild === undefined) ? routeLehrerIndividualdaten.name : routeLehrer.selectedChild.name;
		return { name: redirect_name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): LehrerAuswahlProps {
		return {
			lehrerListeManager: () => this.data.lehrerListeManager,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoLehrer: this.data.gotoEintrag,
			setFilter: this.data.setFilter,
		};
	}

	public getProps(to: RouteLocationNormalized): LehrerAppProps {
		return {
			lehrerListeManager: () => this.data.lehrerListeManager,
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
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.lehrerListeManager.auswahlID() } });
		this.data.setView(node, this.children);
	}

}

export const routeLehrer = new RouteLehrer();