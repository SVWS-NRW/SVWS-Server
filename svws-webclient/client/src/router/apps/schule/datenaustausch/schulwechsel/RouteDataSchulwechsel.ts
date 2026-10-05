import { routeSchulwechselAbgaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchulwechselAbgaenge";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

type RouteStateSchulwechsel = RouteStateInterface;

const defaultState = <RouteStateSchulwechsel> {
	view: routeSchulwechselAbgaenge,
};

export class RouteDataSchulwechsel extends RouteData<RouteStateSchulwechsel> {

	public constructor() {
		super(defaultState);
	}
}
