import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeError } from "~/router/error/RouteError";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchuelerAusbildungsbetriebe } from "~/router/apps/schueler/ausbildungsbetriebe/RouteSchuelerAusbildungsbetriebe";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/erziehungsberechtigte/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplan";
import { routeSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteSchuelerKAoA";
import { routeSchuelerVermerke } from "~/router/apps/schueler/vermerke/RouteSchuelerVermerke";

import { RouteDataSchueler } from "~/router/apps/schueler/RouteDataSchueler";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { SchuelerAppProps } from "~/components/schueler/SSchuelerAppProps";
import type { SchuelerAuswahlProps } from "~/components/schueler/SSchuelerAuswahlProps";
import { routeSchuelerSprachen } from "./sprachen/RouteSchuelerSprachen";
import { routeSchuelerAbschluesse } from "./abschluesse/RouteSchuelerAbschluesse";

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue")
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue")


export class RouteSchueler extends RouteNode<RouteDataSchueler, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler", "schueler/:id(\\d+)?", SSchuelerApp, new RouteDataSchueler());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler";
		super.setView("liste", SSchuelerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerVermerke,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerAusbildungsbetriebe,
			routeSchuelerKAoA,
			routeSchuelerSchulbesuch,
			routeSchuelerLernabschnitte,
			routeSchuelerAbschluesse,
			routeSchuelerSprachen,
			routeSchuelerLaufbahnplanung,
			routeSchuelerStundenplan
		];
		super.defaultChild = routeSchuelerIndividualdaten;
	}


	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any>, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");

			// Daten zum ausgewählten Schuljahresabschnitt und Schüler laden
			await this.data.reload(idSchuljahresabschnitt, id, isEntering);

			if (!this.data.schuelerListeManager.hasDaten()) {
				if (id === undefined) {
					const listFiltered = this.data.schuelerListeManager.filtered();
					if (listFiltered.isEmpty())
						return;
					return this.getChildRoute(listFiltered.get(0).id, from);
				}
				return this.getRoute();
			}

			if (to.name === this.name)
				return this.getChildRoute(this.data.schuelerListeManager.daten().id, from);

			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}


	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<any, any>) : RouteLocationRaw {
		if (from !== undefined && (/(\.|^)stundenplan/).test(from.name))
			return { name: routeSchuelerStundenplan.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
		const redirect_name: string = (routeSchueler.selectedChild === undefined) ? routeSchuelerIndividualdaten.name : routeSchueler.selectedChild.name;
		return { name: redirect_name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerAuswahlProps {
		return {
			serverMode: api.mode,
			schuelerListeManager: () => this.data.schuelerListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			gotoSchueler: this.data.gotoSchueler,
			setFilter: this.data.setFilter,
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAppProps {
		return {
			schuelerListeManager: () => this.data.schuelerListeManager,
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
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		this.data.autofocus = true;
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.schuelerListeManager.auswahlID() ?? undefined } });
		this.data.setView(node, this.children);
		this.data.autofocus = false;
	}

}

export const routeSchueler = new RouteSchueler();
