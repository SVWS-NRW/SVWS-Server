import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { StatistikKlassenProps } from "~/components/statistik/StatistikKlassenProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteStatistik, routeStatistik } from "./RouteStatistik";

const StatistikKlassen = () => import("~/components/statistik/StatistikKlassen.vue");
export class RouteStatistikKlassen extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.klassen", "klassen", StatistikKlassen);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
	}

	public getProps(to: RouteLocationNormalized): StatistikKlassenProps {
		return {
			// statistik
			gotoKlasse: routeStatistik.data.gotoKlasse,
		};
	}
}

export const routeStatistikKlassen = new RouteStatistikKlassen();
