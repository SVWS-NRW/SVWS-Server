import type { RouteParamsRawGeneric } from "vue-router";

import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import type { RouteNode } from "~/router/RouteNode";

interface RouteStateKlassen extends RouteStateInterface {
	oldView?: RouteNode<any, any>;
}

const defaultState: RouteStateKlassen = {
	view: routeKlassenDaten,
	oldView: undefined,
};

export class RouteDataKlassen extends RouteData<RouteStateKlassen> {

	public constructor() {
		super(defaultState);
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

}
