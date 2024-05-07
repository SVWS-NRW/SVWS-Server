import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";

import type { SchuleDatenaustauschUntisStundenplanProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisStundenplanProps";
import { api } from "~/router/Api";
import { type RouteSchuleDatenaustauschUntis, routeSchuleDatenaustauschUntis } from "./RouteSchuleDatenaustauschUntis";

const SSchuleDatenaustauschUntisStundenplan = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisStundenplan.vue");

export class RouteSchuleDatenaustauschUntisStundenplan extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.untis.stundenplan", "stundenplan", SSchuleDatenaustauschUntisStundenplan);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisStundenplanProps {
		return {
			importUntisStundenplanGPU001: routeSchuleDatenaustauschUntis.data.importUntisStundenplanGPU001,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
		};
	}
}

export const routeSchuleDatenaustauschUntisStundenplan = new RouteSchuleDatenaustauschUntisStundenplan();

