import type { RouteLocationNormalized } from "vue-router";
import type { AbteilungenDatenProps } from "~/components/schule/kataloge/abteilungen/daten/AbteilungenDatenProps";
import type { RouteAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { api } from "~/router/Api";

const AbteilungenDaten = () => import("~/components/schule/kataloge/abteilungen/daten/AbteilungenDaten.vue");

export class RouteAbteilungenDaten extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.abteilungen.daten",
			"daten", AbteilungenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abteilungen";
	}

	public getProps(to: RouteLocationNormalized): AbteilungenDatenProps {
		return {
			goToLehrer: routeAbteilungen.data.goToLehrer,
			manager: () => routeAbteilungen.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeAbteilungen.data.patch,
			deleteKlassenzuordnungen: routeAbteilungen.data.deleteKlassenzuordnungen,
			addKlassenzuordnungen: routeAbteilungen.data.addKlassenzuordnungen,
		};
	}
}

export const routeAbteilungenDaten = new RouteAbteilungenDaten();
