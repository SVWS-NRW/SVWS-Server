import { GenericAuswahlStateRoutingAdapter } from "../GenericAuswahlStateRoutingAdapter";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import { routeSchuelerNeu } from "~/router/apps/schueler/neu/RouteSchuelerNeu";
import { routeSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteSchuelerSchnelleingabe";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";

export const schuelerAuswahlStateRoutingAdapter = new GenericAuswahlStateRoutingAdapter({
	route: routeSchueler.data,
	gruppenprozesse: routeSchuelerIndividualdatenGruppenprozesse,
	hinzufuegen: routeSchuelerNeu,
	schnelleingabe: routeSchuelerSchnelleingabe,
	addID: (param, id) => param.id = id,
});
