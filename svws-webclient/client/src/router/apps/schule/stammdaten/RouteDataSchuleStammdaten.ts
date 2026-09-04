import { ViewType } from "@ui/ui/nav/ViewType";
import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import { routeSchuleAdressdaten } from "./adressdaten/RouteSchuleAdressdaten";

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
