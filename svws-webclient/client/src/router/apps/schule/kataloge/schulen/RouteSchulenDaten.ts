import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeSchulen, type RouteSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import type { SchulenDatenProps } from "~/components/schule/kataloge/schulen/daten/SchulenDatenProps";
import { api } from "~/router/Api";

const SchulenDaten = () => import("~/components/schule/kataloge/schulen/daten/SchulenDaten.vue");

export class RouteSchulenDaten extends RouteNode<any, RouteSchulen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulen.daten", "daten", SchulenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
	}

	public getProps(to: RouteLocationNormalized): SchulenDatenProps {
		return {
			schuljahr: api.abschnitt.schuljahr,
			manager: () => routeSchulen.data.manager,
			patch: routeSchulen.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulform: api.schulform,
		};
	}

}

export const routeSchulenDaten = new RouteSchulenDaten();

