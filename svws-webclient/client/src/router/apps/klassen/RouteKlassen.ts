import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

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


const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNode<RouteDataKlassen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen", "/klassen/:id(\\d+)?", SKlassenApp, new RouteDataKlassen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
		super.setView("liste", SKlassenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKlasseDaten,
			routeKlassenStundenplan
		];
		super.defaultChild = routeKlasseDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.setSchuljahresabschnitt(routeApp.data.aktAbschnitt.value.id);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from?: RouteNode<unknown, any>) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		const eintrag = (id !== undefined) ? this.data.klassenListeManager.liste.get(id) : null;
		await this.data.setEintrag(eintrag);
		if (!this.data.klassenListeManager.hasDaten()) {
			if (to.name === this.name) {
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
					await this.data.setView(child);
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<unknown, any>) : RouteLocationRaw {
		if (from !== undefined && (/(\.|^)stundenplan/).test(from.name))
			return { name: routeKlassenStundenplan.name, params: { id } };
		const redirect_name: string = (routeKlassen.selectedChild === undefined) ? routeKlasseDaten.name : routeKlassen.selectedChild.name;
		return { name: redirect_name, params: { id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): KlassenAuswahlProps {
		return {
			klassenListeManager: () => this.data.klassenListeManager,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			setFilter: this.data.setFilter,
		};
	}

	public getProps(to: RouteLocationNormalized): KlassenAppProps {
		return {
			klassenListeManager: () => this.data.klassenListeManager,
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
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.klassenListeManager.auswahlID() } });
		await this.data.setView(node);
	}

}

export const routeKlassen = new RouteKlassen();
