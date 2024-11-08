import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
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
import { ViewType } from "@ui";
import { routeError } from "~/router/error/RouteError";
import { routeLehrerGruppenprozesse } from "~/router/apps/lehrer/RouteLehrerGruppenprozesse";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";
import type { RouteTabProps } from "~/router/RouteTabNode";
import { RouteTabNode } from "~/router/RouteTabNode";


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue");
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue");


export class RouteLehrer extends RouteTabNode<RouteDataLehrer, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_ANSEHEN ], "lehrer", "lehrkraefte/:id(\\d+)?", SLehrerApp, new RouteDataLehrer());
		super.mode = ServerMode.STABLE;
		super.text = "Lehrkräfte";
		super.setView("liste", SLehrerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerStundenplan,
			routeLehrerUnterrichtsdaten,
			routeLehrerGruppenprozesse,
			routeLehrerNeu,
		];
		super.defaultChild = routeLehrerIndividualdaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");

			if (isEntering && (to.types.has(ViewType.GRUPPENPROZESSE) || to.types.has(ViewType.HINZUFUEGEN)))
				return this.getRouteView(this.data.view, { id: id ?? '' });
			// Lade neuen Schuljahresabschnitt, falls er geändert wurde und schreibe ggf. die Route auf die neue Klassen ID um
			const idNeu = await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id))
				return routeLehrerIndividualdaten.getRoute({ id: idNeu });

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
				if (this.data.lehrerListeManager.hasDaten()) {
					if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name))
						return this.getRouteView(routeLehrerStundenplan);
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
		return { id : this.data.lehrerListeManager.auswahlID() ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): LehrerAuswahlProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			lehrerListeManager: () => this.data.lehrerListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
			gotoDefaultView: this.data.gotoDefaultView,
			gotoGruppenprozessView: this.data.gotoGruppenprozessView,
			gotoHinzufuegenView: this.data.gotoHinzufuegenView,
			setFilter: this.data.setFilter,
			activeViewType: this.data.activeViewType
		};
	}

	public getProps(props: RouteTabProps): LehrerAppProps {
		return {
			...props,
			lehrerListeManager: () => this.data.lehrerListeManager,
		};
	}

}

export const routeLehrer = new RouteLehrer();
