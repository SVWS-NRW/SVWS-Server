import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleFachDaten } from "~/router/apps/schule/faecher/RouteSchuleFachDaten";

import { ViewType, type TabData } from "@ui";
import type { FaecherAppProps } from "~/components/schule/faecher/SFaecherAppProps";
import type { FaecherAuswahlProps } from "~/components/schule/faecher/SFaecherAuswahlProps";
import { RouteDataSchuleFaecher } from "./RouteDataSchuleFaecher";
import { routeError } from "~/router/error/RouteError";
import { routeFachStundenplan } from "./stundenplan/RouteFachStundenplan";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeSchuleFachGruppenprozesse } from "./RouteSchuleFachGruppenprozesse";
import { routeSchuleFachNeu } from "./RouteSchuleFachNeu";
import { api } from "~/router/Api";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SFaecherAuswahl = () => import("~/components/schule/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/schule/faecher/SFaecherApp.vue")

export class RouteSchuleFaecher extends RouteNode<RouteDataSchuleFaecher, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.faecher", "schule/faecher/:id(\\d+)?", SFaecherApp, new RouteDataSchuleFaecher());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SFaecherAuswahl, (route) => this.getAuswahlProps(route));
		super.setView("submenu", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.children = [
			routeSchuleFachDaten,
			routeFachStundenplan,
			routeSchuleFachGruppenprozesse,
			routeSchuleFachNeu,
		];
		super.defaultChild = routeSchuleFachDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");

			if (isEntering && (to.types.has(ViewType.GRUPPENPROZESSE) || to.types.has(ViewType.HINZUFUEGEN)))
				return this.data.view.getRoute({ id: id ?? '' });
			// Lade neuen Schuljahresabschnitt, falls er geändert wurde und schreibe ggf. die Route auf die neue Klassen ID um
			const idNeu = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id))
				return routeSchuleFachDaten.getRoute({ id: idNeu });

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined))
				return this.getRouteView(to, { id: '' })

			if (to.types.has(ViewType.GRUPPENPROZESSE))
				await this.data.gotoGruppenprozessView(false);
			else if (to.types.has(ViewType.HINZUFUEGEN))
				await this.data.gotoHinzufuegenView(false);
			else
				await this.data.gotoDefaultView(id);

			if (to.name === this.name) {
				if (this.data.fachListeManager.hasDaten())
					return this.getRouteDefaultChild({ id: this.data.fachListeManager.daten().id });
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

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.fachListeManager.auswahlID() ?? undefined };
	}


	public getAuswahlProps(to: RouteLocationNormalized): FaecherAuswahlProps {
		return {
			mode: api.mode,
			fachListeManager: () => this.data.fachListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			setFilter: this.data.setFilter,
			setzeDefaultSortierungSekII: this.data.setzeDefaultSortierungSekII,
			gotoDefaultView: this.data.gotoDefaultView,
			gotoGruppenprozessView: this.data.gotoGruppenprozessView,
			gotoHinzufuegenView: this.data.gotoHinzufuegenView,
			activeViewType: this.data.activeViewType,
		};
	}

	public getProps(to: RouteLocationNormalized): FaecherAppProps {
		return {
			fachListeManager: () => this.data.fachListeManager,
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
		await RouteManager.doRoute(node.getRoute());
		this.data.setView(node, this.children);
	}
}

export const routeSchuleFaecher = new RouteSchuleFaecher();
