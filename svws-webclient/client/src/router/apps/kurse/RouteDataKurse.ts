import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
export class RouteDataKurse extends RouteData<RouteStateInterface> {

	public constructor() {
		super({
			view: routeKursDaten,
		});
	}

}
