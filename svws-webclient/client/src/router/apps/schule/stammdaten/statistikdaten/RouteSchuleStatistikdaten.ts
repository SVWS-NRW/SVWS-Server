import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";

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
