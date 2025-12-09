import type { RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import { routeLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import type { LernplattformenGruppenprozesseProps } from "~/components/schule/kataloge/lernplattformen/gruppenprozesse/LernplattformenGruppenprozesseProps";

const LernplattformenGruppenprozesse = () =>
	import("~/components/schule/kataloge/lernplattformen/gruppenprozesse/LernplattformenGruppenprozesse.vue");

export class RouteLernplattformenGruppenprozesse extends RouteNode<any, RouteLernplattformen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.lernplattformen.gruppenprozesse", "gruppenprozesse", LernplattformenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(): LernplattformenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeLernplattformen.data.manager,
			delete: routeLernplattformen.data.delete,
			deleteCheck: routeLernplattformen.data.deleteCheck,
			gotoDefaultView: routeLernplattformen.data.gotoDefaultView,
		};
	}

}

export const routeLernplattformenGruppenprozesse = new RouteLernplattformenGruppenprozesse();

