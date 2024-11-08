import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { routeKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplan";
import { RouteDataKlassen } from "~/router/apps/klassen/RouteDataKlassen";

import type { TabData } from "@ui";
import type { KlassenAppProps } from "~/components/klassen/SKlassenAppProps";
import type { KlassenAuswahlProps } from "~/components/klassen/SKlassenAuswahlProps";
import { routeError } from "~/router/error/RouteError";
import { routeKlasseGruppenprozesse } from "./RouteKlassenGruppenprozesse";
import { RoutingStatus } from "~/router/RoutingStatus";
import { ViewType } from "@ui";
import { routeKlassenNeu } from "./RouteKlassenNeu";


const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNode<RouteDataKlassen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "klassen", "klassen/:id(-?\\d+)?", SKlassenApp, new RouteDataKlassen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
		super.setView("liste", SKlassenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKlassenDaten,
			routeKlassenNeu,
			routeKlassenStundenplan,
			routeKlasseGruppenprozesse,
		];
		super.defaultChild = routeKlassenDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");

			if (isEntering && (to.types.has(ViewType.GRUPPENPROZESSE) || to.types.has(ViewType.HINZUFUEGEN)))
				return this.data.view.getRoute({ id: id ?? '' });
			// Lade neuen Schuljahresabschnitt, falls er geändert wurde und schreibe ggf. die Route auf die neue Klassen ID um
			const idNeu = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
			if ((idNeu !== null) && (idNeu !== id))
				return routeKlassenDaten.getRoute({ id: idNeu });

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
				if (this.data.klassenListeManager.hasDaten()) {
					if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name))
						return this.getRouteView(routeKlassenStundenplan);
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
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		if (!this.data.hatKlassenListeManagerManager)
			return {};
		return { id : this.data.klassenListeManager.auswahlID() ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): KlassenAuswahlProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			klassenListeManager: () => this.data.klassenListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			setFilter: this.data.setFilter,
			gotoDefaultView: this.data.gotoDefaultView,
			gotoGruppenprozessView: this.data.gotoGruppenprozessView,
			gotoHinzufuegenView: this.data.gotoHinzufuegenView,
			setzeDefaultSortierung: this.data.setzeDefaultSortierung,
			activeViewType: this.data.activeViewType,
		};
	}

	public getProps(to: RouteLocationNormalized): KlassenAppProps {
		return {
			klassenListeManager: () => this.data.klassenListeManager,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab, this.data.activeViewType),
			activeViewType: this.data.activeViewType,
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		const result = await RouteManager.doRoute(this.getRouteView(node));
		if (result === RoutingStatus.SUCCESS)
			this.data.setView(node, this.children);
	}

}

export const routeKlassen = new RouteKlassen();
