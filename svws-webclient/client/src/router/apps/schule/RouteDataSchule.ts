import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";

interface RouteStateSchule extends RouteStateInterface {
}

const defaultState = <RouteStateSchule> {
	view: routeSchuleBenutzer,
};

export class RouteDataSchule extends RouteData<RouteStateSchule> {

	public constructor() {
		super(defaultState);
	}

}
