import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";

import type { RaeumeProps } from "~/components/stundenplan/kataloge/raeume/SRaeumeProps";
import type { RaeumeAuswahlProps } from "~/components/stundenplan/kataloge/raeume/SRaeumeAuswahlProps";
import { routeError } from "~/router/error/RouteError";
import { RouteDataKatalogRaeume } from "./RouteDataKatalogRaeume";
import type { RouteStundenplan } from "../RouteStundenplan";



const SRaeumeAuswahl = () => import("~/components/stundenplan/kataloge/raeume/SRaeumeAuswahl.vue")
const SRaeume = () => import("~/components/stundenplan/kataloge/raeume/SRaeume.vue")

export class RouteKatalogRaeume extends RouteNode<RouteDataKatalogRaeume, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.raeume", "raeume/:id(\\d+)?", SRaeume, new RouteDataKatalogRaeume());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
		super.setView("eintraege", SRaeumeAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering)
				await this.data.ladeListe();
			const { idSchuljahresabschnitt, id } = RouteNode.getIntParams(to_params, [ "idSchuljahresabschnitt", "id" ]);
			if (idSchuljahresabschnitt === undefined)
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			const eintrag = (id !== undefined) ? this.data.raumListeManager.liste.get(id) : null;
			this.data.setEintrag(eintrag);
			if (!this.data.raumListeManager.hasDaten()) {
				if (id === undefined) {
					if (this.data.raumListeManager.filtered().isEmpty())
						return;
					return this.getRoute({ id: this.data.raumListeManager.filtered().get(0).id });
				}
				return;
			}
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.raumListeManager.auswahlID() ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): RaeumeAuswahlProps {
		return {
			raumListeManager: () => this.data.raumListeManager,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			setKatalogRaeumeImportJSON: this.data.setKatalogRaeumeImportJSON,
		};
	}

	public getProps(to: RouteLocationNormalized): RaeumeProps {
		return {
			patch: routeKatalogRaeume.data.patch,
			auswahl: routeKatalogRaeume.data.raumListeManager.hasDaten() ? routeKatalogRaeume.data.raumListeManager.auswahl() : undefined,
		};
	}

}

export const routeKatalogRaeume = new RouteKatalogRaeume();
