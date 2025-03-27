import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { StundenplanZeitrasterPausenzeitProps } from "~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeitProps";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { api } from "~/router/Api";

const SStundenplanZeitrasterPausenzeit = () => import("~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeit.vue");

export class RouteStundenplanZeitrasterPausenzeit extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_AENDERN,
		], "stundenplan.zeitrasterpausenzeit", "zeitrasterpausenzeit", SStundenplanZeitrasterPausenzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(false, this, params);
	}

	public getProps(to: RouteLocationNormalized): StundenplanZeitrasterPausenzeitProps {
		return {
			schulform: api.schulform,
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			listLehrer: routeStundenplan.data.listLehrer,
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			importPausenzeiten: routeStundenplan.data.importPausenzeiten,
			patchZeitraster: routeStundenplan.data.patchZeitraster,
			addZeitraster: routeStundenplan.data.addZeitraster,
			removeZeitraster: routeStundenplan.data.removeZeitraster,
			importZeitraster: routeStundenplan.data.importZeitraster,
			selected: routeStundenplan.data.selected,
			setSelection: routeStundenplan.data.setSelection,
			setSettingsDefaults: routeStundenplan.data.setSettingsDefaults,
		};
	}

}

export const routeStundenplanZeitrasterPausenzeit = new RouteStundenplanZeitrasterPausenzeit();

