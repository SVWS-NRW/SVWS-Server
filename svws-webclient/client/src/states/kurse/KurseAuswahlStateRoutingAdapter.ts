import { GenericAuswahlStateRoutingAdapter } from "../GenericAuswahlStateRoutingAdapter";
import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeKurseGruppenprozesse } from "~/router/apps/kurse/RouteKurseGruppenprozesse";
import { routeKurseNeu } from "~/router/apps/kurse/RouteKurseNeu";

export const kurseAuswahlStateRoutingAdapter = new GenericAuswahlStateRoutingAdapter({
	route: routeKurse.data,
	gruppenprozesse: routeKurseGruppenprozesse,
	hinzufuegen: routeKurseNeu,
	addID: (param, id) => param.id = id,
});
