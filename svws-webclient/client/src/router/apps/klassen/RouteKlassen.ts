import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

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
import { RouteType } from "~/router/RouteType";
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

			if (isEntering && (to.types.has(RouteType.GRUPPENPROZESSE) || to.types.has(RouteType.HINZUFUEGEN)))
				return this.data.view.getRoute(id);
			// Lade neuen Schuljahresabschnitt, falls er geändert wurde und schreibe ggf. die Route auf die neue Klassen ID um
			const idNeu = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
			if ((idNeu !== null) && (idNeu !== id))
				return routeKlassenDaten.getRoute(idNeu);

			// Wenn die Route für Gruppenprozesse aufgerufen wird, wird hier sichergestellt, dass die Klassen ID nicht gesetzt ist
			if (to.types.has(RouteType.GRUPPENPROZESSE) && (id !== undefined))
				return routeKlasseGruppenprozesse.getRoute();
			else if (to.types.has(RouteType.HINZUFUEGEN) && (id !== undefined))
				return routeKlassenNeu.getRoute();

			if (to.types.has(RouteType.GRUPPENPROZESSE))
				await this.data.gotoGruppenprozess(false);
			else if (to.types.has(RouteType.HINZUFUEGEN))
				await this.data.gotoCreationMode(false);
			else
				await this.data.gotoEintrag(id);

			if (to.name === this.name) {
				if (this.data.klassenListeManager.hasDaten())
					return this.getChildRoute(this.data.klassenListeManager.daten().id, from);
				return;
			}
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams) : Promise<void> {
		this.data.reset();
	}

	public getRoute(id?: number) : RouteLocationRaw {
		if (id === undefined)
			return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getChildRoute(id: number | undefined, from?: RouteNode<any, any>) : RouteLocationRaw {
		if (from !== undefined && (/(\.|^)stundenplan/).test(from.name))
			return { name: routeKlassenStundenplan.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
		return { name: this.data.view.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): KlassenAuswahlProps {
		return {
			serverMode: api.mode,
			klassenListeManager: () => this.data.klassenListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			gotoEintrag: this.data.gotoEintrag,
			setFilter: this.data.setFilter,
			gotoGruppenprozess: this.data.gotoGruppenprozess,
			gotoCreationMode: this.data.gotoCreationMode,
			setzeDefaultSortierung: this.data.setzeDefaultSortierung,
			creationModeEnabled: this.data.creationModeEnabled,
		};
	}

	public getProps(to: RouteLocationNormalized): KlassenAppProps {
		return {
			klassenListeManager: () => this.data.klassenListeManager,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab, this.getType()),
			gruppenprozesseEnabled: this.data.gruppenprozesseEnabled,
			creationModeEnabled: this.data.creationModeEnabled,
		};
	}

	private getType() : RouteType {
		if (this.data.gruppenprozesseEnabled)
			return RouteType.GRUPPENPROZESSE;
		if (this.data.creationModeEnabled)
			return RouteType.HINZUFUEGEN;
		return RouteType.DEFAULT;
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		const result = await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.klassenListeManager.auswahlID() } });
		if (result === RoutingStatus.SUCCESS)
			this.data.setView(node, this.children);
	}

}

export const routeKlassen = new RouteKlassen();
