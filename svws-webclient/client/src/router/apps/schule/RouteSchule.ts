import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { type RouteApp } from "~/router/apps/RouteApp";

import { RouteDataSchule } from "~/router/apps/schule/schulbezogen/stammdaten/RouteDataSchule";
import { routeSchuleStammdaten } from "./schulbezogen/stammdaten/RouteSchuleStammdaten";
import { AppMenuGroup } from "@ui";


export class RouteSchule extends RouteNode<RouteDataSchule, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule", "schule", undefined, new RouteDataSchule());
		super.text = "Schule";
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-school-line";
	}

	protected async update(to: RouteNode<any, any>) {
		if (to.name === this.name)
			return routeSchuleStammdaten.getRoute();
	}

	public async leave(): Promise<void> {
		await this.data.entferneDaten();
	}

}

export const routeSchule = new RouteSchule();
