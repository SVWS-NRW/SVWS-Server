import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeLeistungen } from "~/router/apps/RouteLeistungen";


const defaultState = <RouteStateInterface>{
	view: routeLeistungen,
};

export class RouteDataApp extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

}

