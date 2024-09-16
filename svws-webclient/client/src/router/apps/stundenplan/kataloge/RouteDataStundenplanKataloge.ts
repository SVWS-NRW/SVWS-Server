import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeKatalogAufsichtsbereiche } from "./aufsichtsbereich/RouteKatalogAufsichtsbereiche";

type RouteStateStundenplanKataloge = RouteStateInterface

const defaultState: RouteStateStundenplanKataloge = {
	view: routeKatalogAufsichtsbereiche,
};

export class RouteDataStundenplanKataloge extends RouteData<RouteStateStundenplanKataloge> {

	public constructor() {
		super(defaultState);
	}

}
