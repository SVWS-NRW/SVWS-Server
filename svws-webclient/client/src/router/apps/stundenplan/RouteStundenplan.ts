import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";

import type {  DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode, StundenplanKonfiguration } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";

import { routeStundenplanDaten } from "~/router/apps/stundenplan/RouteStundenplanDaten";
import { routeStundenplanKalenderwochen } from "./RouteStundenplanKalenderwochen";
import { routeStundenplanPausen } from "~/router/apps/stundenplan/RouteStundenplanPausen";
import { routeStundenplanZeitrasterPausenzeit } from "./RouteStundenplanZeitrasterPausenzeit";
import { routeStundenplanKlasse } from "~/router/apps/stundenplan/RouteStundenplanKlasse";
import { routeStundenplanUnterrichte } from "./RouteStundenplanUnterrichte";
import { routeKatalogPausenzeiten } from "./kataloge/RouteKatalogPausenzeiten";
import { routeKatalogAufsichtsbereiche } from "./kataloge/RouteKatalogAufsichtsbereiche";
import { routeKatalogRaeume } from "./kataloge/RouteKatalogRaeume";
import { routeKatalogZeitraster } from "./kataloge/RouteKatalogZeitraster";
import { RouteDataStundenplan } from "~/router/apps/stundenplan/RouteDataStundenplan";
import { routeStundenplanRaum } from "./RouteStundenplanRaum";
import { ConfigElement } from "../../../../../ui/src/utils/Config";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { AppMenuGroup } from "@ui";
import type { StundenplanListeManager } from "@ui";
import { routeStundenplanNeu } from "./RouteStundenplanNeu";
import { routeStundenplanGruppenprozesse } from "./RouteStundenplanGruppenprozesse";
import { routeError } from "~/router/error/RouteError";

const SStundenplanAuswahl = () => import("~/components/stundenplan/SStundenplanAuswahl.vue")
const SStundenplanApp = () => import("~/components/stundenplan/SStundenplanApp.vue")

export class RouteStundenplan extends RouteAuswahlNode<StundenplanListeManager, RouteDataStundenplan, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "stundenplan", "stundenplan/:id(-?\\d+)?", SStundenplanApp, SStundenplanAuswahl, new RouteDataStundenplan());
		super.mode = ServerMode.STABLE;
		super.text = "Stundenplan";
		super.children = [
			routeStundenplanDaten,
			routeStundenplanKalenderwochen,
			routeStundenplanPausen,
			routeStundenplanZeitrasterPausenzeit,
			routeStundenplanKlasse,
			routeStundenplanUnterrichte,
			routeStundenplanRaum,
			routeKatalogZeitraster,
			routeKatalogPausenzeiten,
			routeKatalogAufsichtsbereiche,
			routeKatalogRaeume,
			routeStundenplanNeu,
			routeStundenplanGruppenprozesse,
		];
		super.defaultChild = routeStundenplanDaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-calendar-event-line";
		const stundenplanConfig = new StundenplanKonfiguration();
		api.config.addElements([
			new ConfigElement("stundenplan.settings.defaults", "user", StundenplanKonfiguration.transpilerToJSON(stundenplanConfig)),
		]);
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};

	public static katalogeCheckHidden(isKatalog: boolean, node: RouteNode<any, any>, params?: RouteParams ) {
		if (params === undefined)
			return false;
		try {
			const { id } = RouteNode.getIntParams(params, ["id"] );
			if (isKatalog && id !== -1)
				return { name: routeStundenplanDaten.name, params };
			else if (!isKatalog && id === -1)
				return { name: routeKatalogZeitraster.name, params };
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

}

export const routeStundenplan = new RouteStundenplan();
