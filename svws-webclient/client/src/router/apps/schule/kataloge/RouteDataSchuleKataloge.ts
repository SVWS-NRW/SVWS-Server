import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeKatalogReligionen } from "~/router/apps/schule/kataloge/religionen/RouteKatalogReligionen";

type RouteStateSchuleKataloge = RouteStateInterface

const defaultState: RouteStateSchuleKataloge = {
	view: routeKatalogReligionen,
};

export class RouteDataSchuleKataloge extends RouteData<RouteStateSchuleKataloge> {

	public constructor() {
		super(defaultState);
	}

}
