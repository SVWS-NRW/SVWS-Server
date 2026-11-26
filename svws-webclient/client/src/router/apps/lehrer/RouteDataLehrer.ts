import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdaten";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

export class RouteDataLehrer extends RouteData<RouteStateInterface> {

	public constructor() {
		super({
			view: routeLehrerIndividualdaten,
		});
	}

}
