import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { StundenplanPausenzeit } from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import type { PausenzeitenAuswahlProps } from "~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenAuswahlProps";
import type { PausenzeitenProps } from "~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenProps";
import { RouteDataKatalogPausenzeiten } from "./RouteDataKatalogPausenzeiten";

const SPausenzeitenAuswahl = () => import("~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenAuswahl.vue")
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
		if (isEntering)
			await this.data.ladeListe();
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.stundenplanManager.pausenzeitGetMengeAsList().isEmpty())
			return;
		let eintrag: StundenplanPausenzeit | null = null;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.stundenplanManager.pausenzeitGetMengeAsList().getFirst();
			return this.getRoute(eintrag.id);
		} else {
			const id = parseInt(to_params.id);
			eintrag = this.data.stundenplanManager.pausenzeitGetByIdOrException(id);
		}
		await this.data.setEintrag(eintrag);
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
