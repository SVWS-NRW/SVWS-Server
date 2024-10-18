import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { StundenplanZeitrasterPausenzeitProps } from "~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeitProps";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { api } from "~/router/Api";
import { routeApp } from "../RouteApp";

const SStundenplanZeitrasterPausenzeit = () => import("~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeit.vue");

export class RouteStundenplanZeitrasterPausenzeit extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_AENDERN,
		], "stundenplan.zeitrasterpausenzeit", "zeitrasterpausenzeit", SStundenplanZeitrasterPausenzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanZeitrasterPausenzeitProps {
		return {
			schulform: api.schulform,
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
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

