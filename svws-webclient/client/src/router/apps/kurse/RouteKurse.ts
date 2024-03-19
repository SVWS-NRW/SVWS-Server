import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { type KursDaten , BenutzerKompetenz, Schulform, ServerMode, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { RouteDataKurse } from "~/router/apps/kurse/RouteDataKurse";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { KurseAppProps } from "~/components/kurse/SKurseAppProps";
import type { KurseAuswahlProps } from "~/components/kurse/SKurseAuswahlProps";
import { routeError } from "~/router/error/RouteError";


const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue")
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue")

export class RouteKurse extends RouteNode<RouteDataKurse, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kurse", "kurse/:id(\\d+)?", SKurseApp, new RouteDataKurse());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
		super.setView("liste", SKurseAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKursDaten
		];
		super.defaultChild = routeKursDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from?: RouteNode<unknown, any>) : Promise<void | Error | RouteLocationRaw> {
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
		const eintrag = (id !== undefined) ? this.data.kursListeManager.liste.get(id) : null;
		await this.data.setEintrag(eintrag);
		if (!this.data.kursListeManager.hasDaten()) {
			if (id === undefined) {
				const listFiltered = this.data.kursListeManager.filtered();
				if (listFiltered.isEmpty())
					return;
				return this.getChildRoute(listFiltered.get(0).id, from);
			}
			return this.getRoute();
		}
		if (to.name === this.name)
			return this.getChildRoute(this.data.kursListeManager.daten().id, from);
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

	public getChildRoute(id: number | undefined, from?: RouteNode<unknown, any>) : RouteLocationRaw {
		const redirect_name: string = (routeKurse.selectedChild === undefined) ? routeKursDaten.name : routeKurse.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): KurseAuswahlProps {
		return {
			serverMode: api.mode,
			kursListeManager: () => this.data.kursListeManager,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			setFilter: this.data.setFilter,
		};
	}

	public getProps(to: RouteLocationNormalized): KurseAppProps {
		return {
			kursListeManager: () => this.data.kursListeManager,
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
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.kursListeManager.auswahlID() } });
		this.data.setView(node, this.children);
	}

}

export const routeKurse = new RouteKurse();
