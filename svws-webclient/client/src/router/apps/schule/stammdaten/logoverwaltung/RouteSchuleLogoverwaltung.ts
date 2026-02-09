import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";

const SchuleLogoverwaltung =
	() => import("~/components/schule/stammdaten/logoverwaltung/SchuleLogoverwaltung.vue");

export class RouteSchuleLogoverwaltung extends RouteNode<any, RouteSchuleStammdaten> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten.logoverwaltung",
			"logoverwaltung",
			SchuleLogoverwaltung
		);
		super.mode = ServerMode.DEV;
		super.text = "Logoverwaltung";
	}

}

export const routeSchuleLogoverwaltung = new RouteSchuleLogoverwaltung();
