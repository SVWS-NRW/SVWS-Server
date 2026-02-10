import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import { RouteDataSchuleAdressdaten } from "~/router/apps/schule/stammdaten/adressdaten/RouteDataSchuleAdressdaten";
import type { RouteLocationNormalized } from "vue-router";

import type { SchuleAdressdatenProps } from "~/components/schule/stammdaten/adressdaten/SchuleAdressdatenProps";
import { api } from "~/router/Api";
import { routeSchule } from "~/router/apps/schule/RouteSchule";

const SchuleAdressdaten =
	() => import("~/components/schule/stammdaten/adressdaten/SchuleAdressdaten.vue");

export class RouteSchuleAdressdaten extends RouteNode<any, RouteSchuleStammdaten> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten.adressdaten",
			"adressdaten",
			SchuleAdressdaten,
			new RouteDataSchuleAdressdaten()
		);
		super.mode = ServerMode.STABLE;
		super.text = "Adressdaten";
		super.propHandler = (route) => this.getProps(route);
	}

	public getProps(to: RouteLocationNormalized): SchuleAdressdatenProps {
		return {
			schule: () => api.schuleStammdaten,
			patch: routeSchuleAdressdaten.data.patch,
			smptServerKonfiguration: () => routeSchule.data.smtpServerKonfiguration,
			patchSMTPServerKonfiguration: routeSchule.data.patchSMTServerKonfiguration,
			benutzerIstAdmin: api.benutzerIstAdmin,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeSchuleAdressdaten = new RouteSchuleAdressdaten();
