import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";

import type { SchuleDatenaustauschUntisRaeumeProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisRaeumeProps";

const SSchuleDatenaustauschUntisRaeume = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisRaeume.vue");

export class RouteSchuleDatenaustauschUntisRaeume extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.untis.raeume", "raeume", SSchuleDatenaustauschUntisRaeume);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisRaeumeProps {
		return {
			importUntisRaeumeGPU005: routeSchuleDatenaustauschUntis.data.importUntisRaeumeGPU005,
		};
	}

}

export const routeSchuleDatenaustauschUntisRaeume = new RouteSchuleDatenaustauschUntisRaeume();

