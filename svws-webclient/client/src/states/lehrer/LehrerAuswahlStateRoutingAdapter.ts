import { GenericAuswahlStateRoutingAdapter } from "../GenericAuswahlStateRoutingAdapter";
import { routeLehrerAllgemeinesGruppenprozesse } from "~/router/apps/lehrer/allgemeines/RouteLehrerAllgemeinesGruppenprozesse";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";

export const lehrerAuswahlStateRoutingAdapter = new GenericAuswahlStateRoutingAdapter({
	route: routeLehrer.data,
	gruppenprozesse: routeLehrerAllgemeinesGruppenprozesse, // routeLehrerIndividualdatenGruppenprozesse,
	hinzufuegen: routeLehrerNeu,
	addID: (param, id) => param.id = id,
});
