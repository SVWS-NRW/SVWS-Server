import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import { RouteNode } from "~/router/RouteNode";

const SchuleSchuljahreswechsel = () => import("~/components/schule/stammdaten/schuljahreswechsel/SchuleSchuljahreswechsel.vue");

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
