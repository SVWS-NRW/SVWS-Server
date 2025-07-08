import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { FahrschuelerartenGruppenprozesseProps } from "~/components/schule/kataloge/fahrschuelerarten/gruppenprozesse/SFahrschuelerartenGruppenprozesseProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/fahrschuelerarten/RouteFahrschuelerarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeFahrschuelerarten } from "~/router/apps/schule/fahrschuelerarten/RouteFahrschuelerarten";

const SFahrschuelerartenGruppenprozesse = () => import(
	"~/components/schule/kataloge/fahrschuelerarten/gruppenprozesse/SFahrschuelerartenGruppenprozesse.vue");

export class RouteFahrschuelerartenGruppenprozesse extends RouteNode<any, RouteFahrschuelerarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.fahrschuelerarten.gruppenprozesse" , "gruppenprozesse", SFahrschuelerartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: {idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: ""}}
	}

	public getProps(to: RouteLocationNormalized): FahrschuelerartenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			deleteFahrschuelerarten: routeFahrschuelerarten.data.delete,
			manager: () => routeFahrschuelerarten.data.manager,
		}
	}
}

export const routeFahrschuelerartenGruppenprozesse = new RouteFahrschuelerartenGruppenprozesse();
