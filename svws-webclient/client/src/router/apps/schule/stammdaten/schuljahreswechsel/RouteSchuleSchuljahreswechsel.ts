import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";

const SchuleSchuljahreswechsel =
	() => import("~/components/schule/stammdaten/schuljahreswechsel/SchuleSchuljahreswechsel.vue");

export class RouteSchuleSchuljahreswechsel extends RouteNode<any, RouteSchuleStammdaten> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten.schuljahreswechsel",
			"schuljahreswechsel",
			SchuleSchuljahreswechsel
		);
		super.mode = ServerMode.DEV;
		super.text = "Schuljahreswechsel";
	}

}

export const routeSchuleSchuljahreswechsel = new RouteSchuleSchuljahreswechsel();
