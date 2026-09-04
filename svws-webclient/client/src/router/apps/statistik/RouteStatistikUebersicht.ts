import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized } from "vue-router";
import type { StatistikUebersichtProps } from "~/components/statistik/StatistikUebersichtProps";
import { routeStatistik, type RouteStatistik } from "./RouteStatistik";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const StatistikUebersicht = () => import("~/components/statistik/StatistikUebersicht.vue");

export class RouteStatistikUebersicht extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.uebersicht", "uebersicht", StatistikUebersicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Übersicht";
	}

	public getProps(to: RouteLocationNormalized): StatistikUebersichtProps {
		return {
			validatorKontext: () => schuleStateImpl.validatorKontext,
			schuleStammdaten: routeStatistik.data.schuleStammdaten,
			statistikGesamt: routeStatistik.data.statistikGesamt,
		};
	}
}

export const routeStatistikUebersicht = new RouteStatistikUebersicht();
