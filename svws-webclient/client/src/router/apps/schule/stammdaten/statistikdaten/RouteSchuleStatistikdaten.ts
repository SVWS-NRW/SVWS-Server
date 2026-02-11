import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";

const SchuleStatistikdaten =
	() => import("~/components/schule/stammdaten/statistikdaten/SchuleStatistikdaten.vue");

export class RouteSchuleStatikstikdaten extends RouteNode<any, RouteSchuleStammdaten> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten.statistikdaten",
			"statistikdaten",
			SchuleStatistikdaten
		);
		super.mode = ServerMode.DEV;
		super.text = "Statistikdaten";
	}

}

export const routeSchuleStatistikdaten = new RouteSchuleStatikstikdaten();
