import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeKatalogReligionen } from "~/router/apps/kataloge/religion/RouteKatalogReligionen";

type RouteStateKataloge = RouteStateInterface

const defaultState: RouteStateKataloge = {
	view: routeKatalogReligionen,
};

export class RouteDataKataloge extends RouteData<RouteStateKataloge> {

	public constructor() {
		super(defaultState);
	}

}
