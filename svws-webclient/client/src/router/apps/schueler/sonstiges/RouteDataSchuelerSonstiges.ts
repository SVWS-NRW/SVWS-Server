import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuelerVermerke } from "../vermerke/RouteSchuelerVermerke";

const defaultState = <RouteStateInterface> {
	view: routeSchuelerVermerke,
};

export class RouteDataSchuelerLernabschnitte extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

}
