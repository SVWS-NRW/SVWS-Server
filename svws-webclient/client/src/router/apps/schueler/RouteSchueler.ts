import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

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

import { type TabData, ViewType } from "@ui";
import type { SchuelerAppProps } from "~/components/schueler/SSchuelerAppProps";
import type { SchuelerAuswahlProps } from "~/components/schueler/SSchuelerAuswahlProps";
import { routeSchuelerSprachen } from "./sprachen/RouteSchuelerSprachen";
import { routeSchuelerAbschluesse } from "./abschluesse/RouteSchuelerAbschluesse";
import { routeSchuelerGruppenprozesse } from "~/router/apps/schueler/RouteSchuelerGruppenprozesse";
import { routeSchuelerNeu } from "./RouteSchuelerNeu";
import { ConfigElement } from "~/components/Config";

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
			routeSchuelerStundenplan,
			routeSchuelerNeu,
			routeSchuelerGruppenprozesse,
		];
		super.defaultChild = routeSchuelerIndividualdaten;
		api.nonPersistentConfig.addElements([
			new ConfigElement(`${this.name}.auswahl.id`, "user", ""),
		]);
	}


	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id: paramId } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			let id = paramId;
			if ((paramId === undefined) && isEntering) {
				const lastId = parseInt(api.nonPersistentConfig.getValue(`${this.name}.auswahl.id`));
				if (!isNaN(lastId))
					id = lastId;
			}
			if (isEntering && (to.types.has(ViewType.GRUPPENPROZESSE) || to.types.has(ViewType.HINZUFUEGEN)))
				return this.getRouteView(this.data.view, { id: id ?? '' });
			// Daten zum ausgewählten Schuljahresabschnitt und Schüler laden
			const idNeu = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id))
				return routeSchuelerIndividualdaten.getRoute({ id: idNeu });

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined))
				return this.getRouteView(to, { id: '' })

			if (to.hasType(ViewType.GRUPPENPROZESSE))
				await this.data.gotoGruppenprozessView(false);
			else if (to.hasType(ViewType.HINZUFUEGEN))
				await this.data.gotoHinzufuegenView(false);
			else
				await this.data.gotoDefaultView(id);

			if (to.name === this.name) {
				if (this.data.schuelerListeManager.hasDaten()) {
					if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name))
						return this.getRouteView(routeSchuelerStundenplan);
					return this.getRouteSelectedChild();
				}
				return;
			}
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams) : Promise<void> {
		this.data.reset();
		const { id } = RouteNode.getStringParams(from_params, ["id"]);
		await api.nonPersistentConfig.setValue(`${this.name}.auswahl.id`, id ?? "");
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.schuelerListeManager.auswahlID() ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerAuswahlProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schuelerListeManager: () => this.data.schuelerListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			gotoDefaultView: this.data.gotoDefaultView,
			gotoHinzufuegenView: this.data.gotoHinzufuegenView,
			gotoGruppenprozessView: this.data.gotoGruppenprozessView,
			setFilter: this.data.setFilter,
			activeViewType: this.data.activeViewType,
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAppProps {
		return {
			patch: this.data.patch,
			schuelerListeManager: () => this.data.schuelerListeManager,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab, this.data.activeViewType),
			activeViewType: this.data.activeViewType,
		};
	}

	private setTab = async (value: TabData) => {
		this.data.autofocus = true;
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(this.getRouteView(node));
		this.data.setView(node, this.children);
		this.data.autofocus = false;
	}

}

export const routeSchueler = new RouteSchueler();
