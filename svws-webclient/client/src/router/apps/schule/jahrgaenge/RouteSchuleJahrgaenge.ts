import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchuleJahrgaengeDaten } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaengeDaten";

import type { TabData } from "@ui";
import type { JahrgaengeAppProps } from "~/components/schule/jahrgaenge/SJahrgaengeAppProps";
import type { JahrgaengeAuswahlProps } from "~/components/schule/jahrgaenge/SJahrgaengeAuswahlProps";
import { RouteDataSchuleJahrgaenge } from "./RouteDataSchuleJahrgaenge";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";
import { api } from "~/router/Api";
import { ViewType } from "@ui";
import { routeSchuleJahrgangNeu } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangNeu";
import { routeSchuleJahrgangGruppenprozesse } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangGruppenprozesse";

const SJahrgaengeAuswahl = () => import("~/components/schule/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/schule/jahrgaenge/SJahrgaengeApp.vue")

export class RouteSchuleJahrgaenge extends RouteNode<RouteDataSchuleJahrgaenge, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN ], "schule.jahrgaenge", "schule/jahrgaenge/:id(\\d+)?", SJahrgaengeApp, new RouteDataSchuleJahrgaenge());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgänge";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SJahrgaengeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleJahrgaengeDaten,
			routeSchuleJahrgangNeu,
			routeSchuleJahrgangGruppenprozesse,
		];
		super.defaultChild = routeSchuleJahrgaengeDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");

			if (isEntering && (to.types.has(ViewType.GRUPPENPROZESSE) || to.types.has(ViewType.HINZUFUEGEN)))
				return this.data.view.getRoute({ id: id ?? '' });
			// Lade neuen Schuljahresabschnitt, falls er geändert wurde und schreibe ggf. die Route auf die neue Klassen ID um
			const idNeu = await this.data.ladeDaten(isEntering);
			if ((idNeu !== null) && (idNeu !== id))
				return routeSchuleJahrgaengeDaten.getRoute({ id: idNeu });

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
				if (this.data.jahrgangListeManager.hasDaten())
					return this.getRouteDefaultChild({ id: this.data.jahrgangListeManager.daten().id });
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
		return { id : this.data.jahrgangListeManager.auswahlID() ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): JahrgaengeAuswahlProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			jahrgangListeManager: () => this.data.jahrgangListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoDefaultView: this.data.gotoDefaultView,
			gotoGruppenprozessView: this.data.gotoGruppenprozessView,
			gotoHinzufuegenView: this.data.gotoHinzufuegenView,
			activeViewType: this.data.activeViewType,
		};
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeAppProps {
		return {
			jahrgangListeManager: () => this.data.jahrgangListeManager,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
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

export const routeSchuleJahrgaenge = new RouteSchuleJahrgaenge();
