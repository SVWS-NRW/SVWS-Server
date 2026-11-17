import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { RouteNode } from "~/router/RouteNode";

import type { RouteStatistik } from "./RouteStatistik";

const StatistikZusatzerhebungen = () => import("~/components/statistik/StatistikZusatzerhebungen.vue");

export class RouteStatistikZusatzerhebungen extends RouteNode<any, RouteStatistik> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik.zusatzerhebungen", "zusatzerhebungen", StatistikZusatzerhebungen);
		super.mode = ServerMode.DEV;
		super.text = "Zusatzerhebungen";
	}

}

export const routeStatistikZusatzerhebungen = new RouteStatistikZusatzerhebungen();
