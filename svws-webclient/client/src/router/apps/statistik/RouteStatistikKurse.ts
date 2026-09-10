import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { routeKurse } from "../kurse/RouteKurse";
import type { StatistikKurseProps } from "~/components/statistik/StatistikKurseProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteStatistik, routeStatistik } from "./RouteStatistik";

const StatistikKurse = () => import("~/components/statistik/StatistikKurse.vue");
export class RouteStatistikKurse extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.kurse", "kurse", StatistikKurse);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
	}

	public getProps(to: RouteLocationNormalized): StatistikKurseProps {
		return {
			// statistik
			gotoKurs: routeStatistik.data.gotoKurs,
			zeigeAlles: false,
			gotoSchueler: routeStatistik.data.gotoSchueler,
			// lehrer
			patch: routeKurse.data.patch,
			setFilter: routeKurse.data.setFilter,
			addKursLehrer: routeKurse.data.addKurLehrer,
			patchKursLehrer: routeKurse.data.patchKursLehrer,
			deleteKursLehrer: routeKurse.data.deleteKursLehrer,
		};
	}
}

export const routeStatistikKurse = new RouteStatistikKurse();
