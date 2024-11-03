import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";

import type { SchuleDatenaustauschUntisStundenplanProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisStundenplanProps";
import { type RouteSchuleDatenaustauschUntis, routeSchuleDatenaustauschUntis } from "./RouteSchuleDatenaustauschUntis";

const SSchuleDatenaustauschUntisStundenplan = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisStundenplan.vue");

export class RouteSchuleDatenaustauschUntisStundenplan extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.untis.stundenplan", "stundenplan", SSchuleDatenaustauschUntisStundenplan);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisStundenplanProps {
		return {
			importUntisStundenplanGPU001: routeSchuleDatenaustauschUntis.data.importUntisStundenplanGPU001,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(true),
		};
	}
}

export const routeSchuleDatenaustauschUntisStundenplan = new RouteSchuleDatenaustauschUntisStundenplan();

