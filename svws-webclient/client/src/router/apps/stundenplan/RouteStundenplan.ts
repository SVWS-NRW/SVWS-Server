import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
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
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeStundenplanNeu } from "./RouteStundenplanNeu";
import { routeStundenplanGruppenprozesse } from "./RouteStundenplanGruppenprozesse";
import { routeError } from "~/router/error/RouteError";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { StundenplanKonfiguration } from "@core/core/data/stundenplan/StundenplanKonfiguration";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { StundenplanListeManager } from "@ui/ui/manager/stundenplan/StundenplanListeManager";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { RouteNode } from "~/router/RouteNode";
import { ConfigElement } from "@ui/utils/Config";

const SStundenplanAuswahl = () => import("~/components/stundenplan/SStundenplanAuswahl.vue");
const SStundenplanApp = () => import("~/components/stundenplan/SStundenplanApp.vue");

export class RouteStundenplan extends RouteAuswahlNode<StundenplanListeManager, RouteDataStundenplan, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "stundenplan", String.raw`stundenplan/:id(-?\d+)?`, SStundenplanApp, SStundenplanAuswahl, new RouteDataStundenplan());
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
		configStateImpl.config.addElements([
			new ConfigElement("stundenplan.settings.defaults", "user", StundenplanKonfiguration.transpilerToJSON(stundenplanConfig)),
		]);
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

	public static katalogeCheckHidden(isKatalog: boolean, node: RouteNode<any, any>, params?: RouteParams) {
		if (params === undefined) {
			return false;
		}
		try {
			const { id } = RouteNode.getIntParams(params, ["id"]);
			if (isKatalog && id !== -1) {
				return { name: routeStundenplanDaten.name, params };
			} else if (!isKatalog && id === -1) {
				return { name: routeKatalogZeitraster.name, params };
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

}

export const routeStundenplan = new RouteStundenplan();
