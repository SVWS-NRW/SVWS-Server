import type { RouteLocationNormalized, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { StundenplanZeitrasterPausenzeitProps } from "~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeitProps";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { RouteNode } from "~/router/RouteNode";

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
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			listLehrer: routeStundenplan.data.listLehrer,
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
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

