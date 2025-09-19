import type { RouteLocationNormalized } from "vue-router";
import type { SportbefreiungenDatenProps } from "~/components/schule/schulbezogen/sportbefreiungen/daten/SSportbefreiungenDatenProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeSportbefreiungen } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungen";
import type { RouteSportbefreiungen } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungen";

const SSportbefreiungenDaten = () => import("~/components/schule/schulbezogen/sportbefreiungen/daten/SSportbefreiungenDaten.vue")

export class RouteSportbefreiungenDaten extends RouteNode<any, RouteSportbefreiungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.sportbefreiungen.daten",
			"daten", SSportbefreiungenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Sportbefreiung"
	}

	public getProps(to: RouteLocationNormalized): SportbefreiungenDatenProps {
		return {
			manager: () => routeSportbefreiungen.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeSportbefreiungen.data.patch,
		}
	}
}

export const routeSportbefreiungenDaten = new RouteSportbefreiungenDaten();
