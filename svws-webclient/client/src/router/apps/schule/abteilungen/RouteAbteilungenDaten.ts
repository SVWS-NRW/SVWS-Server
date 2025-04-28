import type { RouteLocationNormalized } from "vue-router";
import type { AbteilungenDatenProps } from "~/components/schule/kataloge/abteilungen/daten/SAbteilungenDatenProps";
import type { RouteAbteilungen } from "~/router/apps/schule/abteilungen/RouteAbteilungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeAbteilungen } from "~/router/apps/schule/abteilungen/RouteAbteilungen";
import {RouteManager} from "~/router/RouteManager";
import {routeLehrer} from "~/router/apps/lehrer/RouteLehrer";

const SAbteilungenDaten = () => import("~/components/schule/kataloge/abteilungen/daten/SAbteilungenDaten.vue");

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
			patch: routeAbteilungen.data.patch,
		}
	}
}

export const routeAbteilungenDaten = new RouteAbteilungenDaten();
