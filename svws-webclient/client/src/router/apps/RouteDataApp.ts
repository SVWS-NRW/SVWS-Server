import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";

export interface RouteStateApp extends RouteStateInterface {
	view: any;
}

const defaultState = <RouteStateApp>{
	view: routeSchueler,
};

export class RouteDataApp extends RouteData<RouteStateApp> {

	public constructor() {
		super(defaultState);
	}

	public async leave() {
		this._state.value = this._defaultState;
	}

}
