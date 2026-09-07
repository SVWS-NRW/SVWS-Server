import { routeSchuleDatenaustauschSchulwechselAbgaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechselAbgaenge";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

type RouteStateDatenaustauschSchulwechsel = RouteStateInterface;

const defaultState = <RouteStateDatenaustauschSchulwechsel> {
	view: routeSchuleDatenaustauschSchulwechselAbgaenge,
};

export class RouteDataSchuleDatenaustauschSchulwechsel extends RouteData<RouteStateDatenaustauschSchulwechsel> {

	public constructor() {
		super(defaultState);
	}
}
