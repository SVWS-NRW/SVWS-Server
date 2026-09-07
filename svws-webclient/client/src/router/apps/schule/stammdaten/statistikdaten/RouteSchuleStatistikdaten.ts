import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import { RouteNode } from "~/router/RouteNode";

const SchuleStatistikdaten = () => import("~/components/schule/stammdaten/statistikdaten/SchuleStatistikdaten.vue");

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
