import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import type { BeschaeftigungsartenGruppenprozesseProps } from "~/components/schule/kataloge/beschaeftigungsarten/gruppenprozesse/BeschaeftigungsartenGruppenprozesseProps";

const BeschaeftigungsartenGruppenprozesse = () => import(
	"~/components/schule/kataloge/beschaeftigungsarten/gruppenprozesse/BeschaeftigungsartenGruppenprozesse.vue");

export class RouteBeschaeftigungsartenGruppenprozesse extends RouteNode<any, RouteBeschaeftigungsarten> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.beschaeftigungsarten.gruppenprozesse", "gruppenprozesse", BeschaeftigungsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): BeschaeftigungsartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			delete: routeBeschaeftigungsarten.data.delete,
			deleteCheck: routeBeschaeftigungsarten.data.deleteCheck,
			manager: () => routeBeschaeftigungsarten.data.manager,
		};
	}
}

export const routeBeschaeftigungsartenGruppenprozesse = new RouteBeschaeftigungsartenGruppenprozesse();
