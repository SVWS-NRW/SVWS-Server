import { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { ViewType } from "@ui";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";
import type { RouteAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import { routeAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import type { AnkreuzkompetenzenGruppenprozesseProps } from "~/components/schule/kataloge/ankreuzkompetenzen/gruppenprozesse/AnkreuzkompetenzenGruppenprozesseProps";

const AnkreuzkompetenzenGruppenprozesse = () => import(
	"~/components/schule/kataloge/ankreuzkompetenzen/gruppenprozesse/AnkreuzkompetenzenGruppenprozesse.vue");

class RouteAnkreuzkompetenzenGruppenprozesse extends RouteNode<any, RouteAnkreuzkompetenzen> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.ankreuzkompetenzen.gruppenprozesse", "gruppenprozesse", AnkreuzkompetenzenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): AnkreuzkompetenzenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeAnkreuzkompetenzen.data.manager,
			delete: routeAnkreuzkompetenzen.data.delete,
			deleteCheck: routeAnkreuzkompetenzen.data.deleteCheck,
			gotoDefaultView: routeAnkreuzkompetenzen.data.gotoDefaultView,
		};
	}
}

export const routeAnkreuzkompetenzenGruppenprozesse = new RouteAnkreuzkompetenzenGruppenprozesse();

