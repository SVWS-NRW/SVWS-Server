import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { StundenplanPausenzeit , DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import type { PausenzeitenAuswahlProps } from "~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenAuswahlProps";
import type { PausenzeitenProps } from "~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenProps";
import { RouteDataKatalogPausenzeiten } from "./RouteDataKatalogPausenzeiten";
import { routeError } from "~/router/error/RouteError";

const SPausenzeitenAuswahl = () => import("~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenAuswahl.vue");
const SPausenzeiten = () => import("~/components/stundenplan/kataloge/pausenzeiten/SPausenzeiten.vue");

export class RouteKatalogPausenzeiten extends RouteNode<RouteDataKatalogPausenzeiten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.pausenzeiten", "pausenzeiten/:id(\\d+)?", SPausenzeiten, new RouteDataKatalogPausenzeiten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausenzeiten";
		super.setView("eintraege", SPausenzeitenAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeListe();
			if (this.data.stundenplanManager.pausenzeitGetMengeAsList().isEmpty())
				return;
			let eintrag: StundenplanPausenzeit | null = null;
			if ((id === undefined) && this.data.auswahl)
				return this.getRoute(this.data.auswahl.id);
			if (id === undefined) {
				eintrag = this.data.stundenplanManager.pausenzeitGetMengeAsList().getFirst();
				return this.getRoute(eintrag.id);
			} else {
				eintrag = this.data.stundenplanManager.pausenzeitGetByIdOrException(id);
			}
			await this.data.setEintrag(eintrag);
		} catch (error) {
			return routeError.getRoute(error as DeveloperNotificationException);
		}
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name : this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): PausenzeitenAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			addPausenzeiten: this.data.addPausenzeiten,
			deleteEintraege: this.data.deleteEintraege,
			setKatalogPausenzeitenImportJSON: this.data.setKatalogRaeumeImportJSON,
			stundenplanManager: () => this.data.stundenplanManager,
		};
	}

	public getProps(to: RouteLocationNormalized): PausenzeitenProps {
		return {
			patch: routeKatalogPausenzeiten.data.patch,
			auswahl: this.data.auswahl,
		};
	}

}

export const routeKatalogPausenzeiten = new RouteKatalogPausenzeiten();
