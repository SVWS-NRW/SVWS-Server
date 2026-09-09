import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { RouteNode } from "~/router/RouteNode";

import type { RouteStatistik } from "./RouteStatistik";


const StatistikUebersicht = () => import("~/components/statistik/StatistikUebersicht.vue");

export class RouteStatistikUebersicht extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.uebersicht", "uebersicht", StatistikUebersicht);
		super.mode = ServerMode.DEV;
		super.text = "Übersicht";
	}

}

export const routeStatistikUebersicht = new RouteStatistikUebersicht();
