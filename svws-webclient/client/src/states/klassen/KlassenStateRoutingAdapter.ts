import { GenericAuswahlStateRoutingAdapter } from "../GenericAuswahlStateRoutingAdapter";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKlasseGruppenprozesse } from "~/router/apps/klassen/RouteKlassenGruppenprozesse";
import { routeKlassenNeu } from "~/router/apps/klassen/RouteKlassenNeu";

export const klassenStateRoutingAdapter = new GenericAuswahlStateRoutingAdapter({
	route: routeKlassen.data,
	gruppenprozesse: routeKlasseGruppenprozesse,
	hinzufuegen: routeKlassenNeu,
	addID: (param, id) => param.id = id,
});
