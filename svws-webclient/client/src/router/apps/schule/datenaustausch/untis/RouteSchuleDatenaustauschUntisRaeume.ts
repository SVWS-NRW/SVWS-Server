import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";

import type { SchuleDatenaustauschUntisRaeumeProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisRaeumeProps";

const SSchuleDatenaustauschUntisRaeume = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisRaeume.vue");

export class RouteSchuleDatenaustauschUntisRaeume extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.untis.raeume", "raeume", SSchuleDatenaustauschUntisRaeume);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisRaeumeProps {
		return {
			importUntisRaeumeGPU005: routeSchuleDatenaustauschUntis.data.importUntisRaeumeGPU005,
		};
	}

}

export const routeSchuleDatenaustauschUntisRaeume = new RouteSchuleDatenaustauschUntisRaeume();

