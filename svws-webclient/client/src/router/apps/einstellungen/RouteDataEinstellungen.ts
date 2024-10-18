import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeEinstellungenBenutzer } from "./benutzer/RouteEinstellungenBenutzer";

interface RouteStateEinstellungen extends RouteStateInterface {
}

const defaultState = <RouteStateEinstellungen> {
	view: routeEinstellungenBenutzer,
};

export class RouteDataEinstellungen extends RouteData<RouteStateEinstellungen> {

	public constructor() {
		super(defaultState);
	}

}
