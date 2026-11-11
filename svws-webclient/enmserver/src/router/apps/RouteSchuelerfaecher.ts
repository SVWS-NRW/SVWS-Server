import { type RouteApp, routeApp } from "@wenom/router/apps/RouteApp";
import { RouteNode } from "@wenom/router/RouteNode";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";

const EnmSchuelerFaecher = () => import("@ui/components/enm/EnmSchuelerFaecher.vue");

export class RouteSchuelerfaecher extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), "schuelerfaecher", "schuelerfaecher", EnmSchuelerFaecher);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Notenübersicht";
	}

	public getProps() {
		return { ladeManagerKlassenleitung: () => routeApp.data.ladeManagerKlassenleitung() };
	}

}

export const routeSchuelerfaecher = new RouteSchuelerfaecher();
