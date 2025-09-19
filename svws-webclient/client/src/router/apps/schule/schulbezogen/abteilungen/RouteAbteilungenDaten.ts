import type { RouteLocationNormalized } from "vue-router";
import type { AbteilungenDatenProps } from "~/components/schule/schulbezogen/abteilungen/daten/SAbteilungenDatenProps";
import type { RouteAbteilungen } from "~/router/apps/schule/schulbezogen/abteilungen/RouteAbteilungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeAbteilungen } from "~/router/apps/schule/schulbezogen/abteilungen/RouteAbteilungen";
import { RouteManager } from "~/router/RouteManager";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { api } from "~/router/Api";

const SAbteilungenDaten = () => import("~/components/schule/schulbezogen/abteilungen/daten/SAbteilungenDaten.vue");

export class RouteAbteilungenDaten extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.abteilungen.daten",
			"daten", SAbteilungenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abteilungen";
	}

	goToLehrer = async (idLehrer : number) => {
		await RouteManager.doRoute(routeLehrer.getRoute({id : idLehrer}));
	}

	public getProps(to: RouteLocationNormalized): AbteilungenDatenProps {
		return {
			goToLehrer: this.goToLehrer,
			manager: () => routeAbteilungen.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeAbteilungen.data.patch,
			deleteKlassenzuordnungen: routeAbteilungen.data.deleteKlassenzuordnungen,
			addKlassenzuordnungen: routeAbteilungen.data.addKlassenzuordnungen,
		}
	}
}

export const routeAbteilungenDaten = new RouteAbteilungenDaten();
