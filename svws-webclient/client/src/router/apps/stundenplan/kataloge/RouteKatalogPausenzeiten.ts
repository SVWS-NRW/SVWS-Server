import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import type { StundenplanPausenzeit , DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";

import type { PausenzeitenAuswahlProps } from "~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenAuswahlProps";
import type { PausenzeitenProps } from "~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenProps";
import { RouteDataKatalogPausenzeiten } from "./RouteDataKatalogPausenzeiten";
import { routeError } from "~/router/error/RouteError";
import { RouteStundenplan, routeStundenplan } from "../RouteStundenplan";

const SPausenzeitenAuswahl = () => import("~/components/stundenplan/kataloge/pausenzeiten/SPausenzeitenAuswahl.vue");
const SPausenzeiten = () => import("~/components/stundenplan/kataloge/pausenzeiten/SPausenzeiten.vue");

export class RouteKatalogPausenzeiten extends RouteNode<RouteDataKatalogPausenzeiten, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.pausenzeiten", "pausenzeiten/:idPausenzeiten(\\d+)?", SPausenzeiten, new RouteDataKatalogPausenzeiten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausenzeiten";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(true, this, params);
		super.setView("eintraege", SPausenzeitenAuswahl, (route) => this.getAuswahlProps(route));
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { idPausenzeiten } = RouteNode.getIntParams(to_params, ["idPausenzeiten"]);
			if (isEntering)
				await this.data.ladeListe();
			if (this.data.stundenplanManager.pausenzeitGetMengeAsList().isEmpty())
				return;
			let eintrag: StundenplanPausenzeit | null = null;
			if ((idPausenzeiten === undefined) && this.data.auswahl)
				return this.getRoute();
			if (idPausenzeiten === undefined) {
				eintrag = this.data.stundenplanManager.pausenzeitGetMengeAsList().getFirst();
				return this.getRoute({ id: eintrag.id });
			} else {
				eintrag = this.data.stundenplanManager.pausenzeitGetByIdOrException(idPausenzeiten);
			}
			await this.data.setEintrag(eintrag);
		} catch (error) {
			return await routeError.getErrorRoute(error as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { idPausenzeiten : this.data.auswahl?.id ?? undefined };
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
			setSettingsDefaults: routeStundenplan.data.setSettingsDefaults,
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
