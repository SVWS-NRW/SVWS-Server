import type { HaltestellenGruppenprozesseProps } from "~/components/schule/allgemein/haltestellen/gruppenprozesse/SHaltestellenGruppenprozesseProps";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteHaltestellen } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp} from "~/router/apps/RouteApp";
import { routeHaltestellen } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellen";

const SHaltestellenGruppenprozesse = () => import(
	"~/components/schule/allgemein/haltestellen/gruppenprozesse/SHaltestellenGruppenprozesse.vue");

export class RouteHaltestellenGruppenprozesse extends RouteNode<any, RouteHaltestellen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.haltestellen.gruppenprozesse" , "gruppenprozesse", SHaltestellenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse"
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}}
	}

	public getProps(to: RouteLocationNormalized): HaltestellenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteHaltestellen: routeHaltestellen.data.delete,
			manager: () => routeHaltestellen.data.manager,
		}
	}
}

export const routeHaltestellenGruppenprozesse = new RouteHaltestellenGruppenprozesse();
