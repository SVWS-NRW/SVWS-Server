import { GenericAuswahlStateRoutingAdapter } from "../GenericAuswahlStateRoutingAdapter";
import { routeSchuelerAllgemeinesGruppenprozesse } from "~/router/apps/schueler/allgemeines/RouteSchuelerAllgemeinesGruppenprozesse";
import { routeSchuelerNeu } from "~/router/apps/schueler/neu/RouteSchuelerNeu";
import { routeSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteSchuelerSchnelleingabe";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";

export const schuelerAuswahlStateRoutingAdapter = new GenericAuswahlStateRoutingAdapter({
	route: routeSchueler.data,
	gruppenprozesse: routeSchuelerAllgemeinesGruppenprozesse,
	hinzufuegen: routeSchuelerNeu,
	schnelleingabe: routeSchuelerSchnelleingabe,
	addID: (param, id) => param.id = id,
});
