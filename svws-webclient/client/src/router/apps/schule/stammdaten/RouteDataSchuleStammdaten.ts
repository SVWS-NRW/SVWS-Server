import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuleAdressdaten } from "~/router/apps/schule/stammdaten/adressdaten/RouteSchuleAdressdaten";
import { ViewType } from "@ui";

type RouteStateSchuleStammdaten = RouteStateInterface;

const defaultState = <RouteStateSchuleStammdaten> {
	view: routeSchuleAdressdaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataSchuleStammdaten extends RouteData<RouteStateSchuleStammdaten> {

	public constructor() {
		super(defaultState);
	}
}
