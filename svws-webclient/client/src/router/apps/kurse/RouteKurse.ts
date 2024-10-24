import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { RouteDataKurse } from "~/router/apps/kurse/RouteDataKurse";

import type { TabData } from "@ui";
import type { KurseAppProps } from "~/components/kurse/SKurseAppProps";
import type { KurseAuswahlProps } from "~/components/kurse/SKurseAuswahlProps";
import { routeError } from "~/router/error/RouteError";


const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue")
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue")

export class RouteKurse extends RouteNode<RouteDataKurse, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "kurse", "kurse/:id(\\d+)?", SKurseApp, new RouteDataKurse());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
		super.setView("liste", SKurseAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKursDaten,
		];
		super.defaultChild = routeKursDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from?: RouteNode<any, any>) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
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
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public getRoute(id?: number) : RouteLocationRaw {
		if (id === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<any, any>) : RouteLocationRaw {
		const redirect_name: string = (routeKurse.selectedChild === undefined) ? routeKursDaten.name : routeKurse.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): KurseAuswahlProps {
		return {
			serverMode: api.mode,
			kursListeManager: () => this.data.kursListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			gotoEintrag: this.data.gotoEintrag,
			setFilter: this.data.setFilter,
		};
	}

	public getProps(to: RouteLocationNormalized): KurseAppProps {
		return {
			kursListeManager: () => this.data.kursListeManager,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.kursListeManager.auswahlID() } });
		this.data.setView(node, this.children);
	}

}

export const routeKurse = new RouteKurse();
