import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";

import { type RouteApp } from "~/router/apps/RouteApp";
import { RouteDataSchule } from "~/router/apps/schule/RouteDataSchule";
import { routeSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import { RouteNode } from "~/router/RouteNode";

export class RouteSchule extends RouteNode<RouteDataSchule, RouteApp> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule",
			"schule",
			undefined,
			new RouteDataSchule()
		);
		super.text = "Schule";
		super.mode = ServerMode.STABLE;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-school-line";
	}

	protected async update(to: RouteNode<any, any>) {
		if (to.name === this.name) {
			return routeSchuleStammdaten.getRoute();
		}
	}

}

export const routeSchule = new RouteSchule();
