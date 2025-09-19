import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteBeschaeftigungsarten } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsarten";
import type { BeschaeftigungsartenGruppenprozesseProps } from "~/components/schule/allgemein/beschaeftigungsarten/gruppenprozesse/SBeschaeftigungsartenGruppenprozesseProps";

const SBeschaeftigungsartenGruppenprozesse = () => import(
	"~/components/schule/allgemein/beschaeftigungsarten/gruppenprozesse/SBeschaeftigungsartenGruppenprozesse.vue");

export class RouteBeschaeftigungsartenGruppenprozesse extends RouteNode<any, RouteBeschaeftigungsarten> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.beschaeftigungsarten.gruppenprozesse" , "gruppenprozesse", SBeschaeftigungsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}};
	}

	public getProps(to: RouteLocationNormalized): BeschaeftigungsartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteBeschaeftigungsarten: routeBeschaeftigungsarten.data.delete,
			manager: () => routeBeschaeftigungsarten.data.manager,
		}
	}
}

export const routeBeschaeftigungsartenGruppenprozesse = new RouteBeschaeftigungsartenGruppenprozesse();
