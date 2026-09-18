import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { StatistikLehrerProps } from "~/components/statistik/StatistikLehrerProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteStatistik, routeStatistik } from "./RouteStatistik";

const StatistikLehrer = () => import("~/components/statistik/StatistikLehrer.vue");
export class RouteStatistikLehrer extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.lehrer", "lehrer", StatistikLehrer);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrer";
	}

	public getProps(to: RouteLocationNormalized): StatistikLehrerProps {
		return {
			zeigeAlles: false,
			gotoLehrer: routeStatistik.data.gotoLehrer,
		};
	}
}

export const routeStatistikLehrer = new RouteStatistikLehrer();
