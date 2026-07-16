import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import type { StundenplanUnterrichteProps } from "~/components/stundenplan/unterrichte/SStundenplanUnterrichteProps";

const SStundenplanUnterrichte = () => import("~/components/stundenplan/unterrichte/SStundenplanUnterrichte.vue");

export class RouteStundenplanUnterrichte extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.unterrichte", "unterrichte", SStundenplanUnterrichte);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Unterrichte";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(false, this, params);
	}

	public getProps(to: RouteLocationNormalized): StundenplanUnterrichteProps {
		return {
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			stundenplanUnterrichtListeManager: () => routeStundenplan.data.stundenplanUnterrichtListeManager,
			setFilter: routeStundenplan.data.setFilter,
			patchUnterricht: routeStundenplan.data.patchUnterrichte,
		};
	}

}

export const routeStundenplanUnterrichte = new RouteStundenplanUnterrichte();

