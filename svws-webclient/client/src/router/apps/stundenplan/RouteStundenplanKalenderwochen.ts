import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { RouteLocationNormalized, RouteParams } from "vue-router";
import type { StundenplanKalenderwochenProps } from "~/components/stundenplan/kalenderwochen/SStundenplanKalenderwochenProps";
import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

const SStundenplanKalenderwochen = () => import("~/components/stundenplan/kalenderwochen/SStundenplanKalenderwochen.vue");

export class RouteStundenplanKalenderwochen extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.kalenderwochen", "kalenderwochen", SStundenplanKalenderwochen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalenderwochen";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(false, this, params);
	}

	public getProps(to: RouteLocationNormalized): StundenplanKalenderwochenProps {
		return {
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			patchKalenderwochenzuordnungen: routeStundenplan.data.patchKalenderwochenzuordnungen,
			deleteKalenderwochenzuordnungen: routeStundenplan.data.deleteKalenderwochenzuordnungen,
		};
	}

}

export const routeStundenplanKalenderwochen = new RouteStundenplanKalenderwochen();

