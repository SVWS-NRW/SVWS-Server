import { RouteData, type RouteStateInterface } from "~/router/RouteData";

const defaultState = <RouteStateInterface> {
};


export class RouteDataSchuleReporting extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

}
