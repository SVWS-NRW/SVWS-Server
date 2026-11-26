import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

const defaultState = <RouteStateInterface> {
	view: routeSchuelerIndividualdaten,
};

export class RouteDataSchueler extends RouteData<RouteStateInterface> {

	public constructor() {
		super(defaultState);
	}

}
