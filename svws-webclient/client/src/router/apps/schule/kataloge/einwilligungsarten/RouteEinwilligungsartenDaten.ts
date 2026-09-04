import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeEinwilligungsarten, type RouteEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import type { EinwilligungsartenDatenProps } from "~/components/schule/kataloge/einwilligungsarten/daten/EinwilligungsartenDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const EinwilligungsartenDaten = () => import("~/components/schule/kataloge/einwilligungsarten/daten/EinwilligungsartenDaten.vue");

export class RouteEinwilligungsartenDaten extends RouteNode<any, RouteEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.einwilligungsarten.daten", "daten", EinwilligungsartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsart";
	}

	public getProps(to: RouteLocationNormalized): EinwilligungsartenDatenProps {
		return {
			patch: routeEinwilligungsarten.data.patch,
			manager: () => routeEinwilligungsarten.data.manager,
		};
	}

}

export const routeEinwilligungsartenDaten = new RouteEinwilligungsartenDaten();
