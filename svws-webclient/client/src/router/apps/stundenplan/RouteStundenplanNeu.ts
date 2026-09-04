import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { StundenplanNeuProps } from "~/components/stundenplan/SStundenplanNeuProps";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const SStundenplanNeu = () => import("~/components/stundenplan/SStundenplanNeu.vue");

export class RouteStundenplanNeu extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.STUNDENPLAN_AENDERN], "stundenplan.neu", "neu", SStundenplanNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): StundenplanNeuProps {
		return {
			manager: () => routeStundenplan.data.manager,
			add: routeStundenplan.data.add,
			addAsCopy: routeStundenplan.data.addAsCopy,
			loadAfterAdd: routeStundenplan.data.loadAfterAdd,
			gotoDefaultView: routeStundenplan.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			getStundenplanListeEintragVorgaengerabschnitt: routeStundenplan.data.getStundenplanListeEintragVorgaengerabschnitt,
		};
	}

}

export const routeStundenplanNeu = new RouteStundenplanNeu();
