import { ViewType } from "@ui/ui/nav/ViewType";

import { routeUvGrunddatenRaeume } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenRaeume";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

interface RouteStateUvGrunddatenRaeumeGruppen extends RouteStateInterface {
}

const defaultState = <RouteStateUvGrunddatenRaeumeGruppen> {
	view: routeUvGrunddatenRaeume,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataUvGrunddatenRaeumeGruppen extends RouteData<RouteStateUvGrunddatenRaeumeGruppen> {

	public constructor() {
		super(defaultState);
	}

}
