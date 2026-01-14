import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeEinwilligungsarten, type RouteEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import type { EinwilligungsartenDatenProps } from "~/components/schule/kataloge/einwilligungsarten/daten/EinwilligungsartenDatenProps";
import { api } from "~/router/Api";

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
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulform: api.schulform,
			schuljahr: api.abschnitt.schuljahr,
		};
	}

}

export const routeEinwilligungsartenDaten = new RouteEinwilligungsartenDaten();
