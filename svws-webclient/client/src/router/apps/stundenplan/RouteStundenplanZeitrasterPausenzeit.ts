import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { StundenplanZeitrasterPausenzeitProps } from "~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeitProps";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

const SStundenplanZeitrasterPausenzeit = () => import("~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeit.vue");

export class RouteStundenplanZeitrasterPausenzeit extends RouteNode<unknown, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.zeitrasterpausenzeit", "zeitrasterpausenzeit", SStundenplanZeitrasterPausenzeit);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanZeitrasterPausenzeitProps {
		return {
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			listLehrer: routeStundenplan.data.listLehrer,
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			addPausenzeit: routeStundenplan.data.addPausenzeit,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			importPausenzeiten: routeStundenplan.data.importPausenzeiten,
			patchZeitraster: routeStundenplan.data.patchZeitraster,
			addZeitraster: routeStundenplan.data.addZeitraster,
			removeZeitraster: routeStundenplan.data.removeZeitraster,
			importZeitraster: routeStundenplan.data.importZeitraster,
			addAufsichtUndBereich: routeStundenplan.data.addAufsichtUndBereich,
		};
	}

}

export const routeStundenplanZeitrasterPausenzeit = new RouteStundenplanZeitrasterPausenzeit();

