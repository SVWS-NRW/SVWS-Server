import type { RouteLocationNormalized } from "vue-router";
import type { AbteilungenDatenProps } from "~/components/schule/kataloge/abteilungen/daten/AbteilungenDatenProps";
import type { RouteAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { RouteNode } from "~/router/RouteNode";
import { routeAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const AbteilungenDaten = () => import("~/components/schule/kataloge/abteilungen/daten/AbteilungenDaten.vue");

export class RouteAbteilungenDaten extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.abteilungen.daten",
			"daten", AbteilungenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abteilungen";
	}

	public getProps(to: RouteLocationNormalized): AbteilungenDatenProps {
		return {
			goToLehrer: routeAbteilungen.data.goToLehrer,
			manager: () => routeAbteilungen.data.manager,
			isReadonly: routeAbteilungen.data.isReadonly,
			isAbteilungImZukuenftigenAbschnitt: routeAbteilungen.data.isAbteilungImZukuenftigenAbschnitt,
			patch: routeAbteilungen.data.patch,
			deleteKlassenzuordnungen: routeAbteilungen.data.deleteKlassenzuordnungen,
			addKlassenzuordnungen: routeAbteilungen.data.addKlassenzuordnungen,
		};
	}
}

export const routeAbteilungenDaten = new RouteAbteilungenDaten();
