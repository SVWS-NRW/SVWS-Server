import { routeSchuelerVermerke } from "../vermerke/RouteSchuelerVermerke";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

const defaultState = <RouteStateInterface> {
	view: routeSchuelerVermerke,
};

export class RouteDataSchuelerLernabschnitte extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

}
