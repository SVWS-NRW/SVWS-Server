import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { KonfessionenDatenProps } from "~/components/schule/kataloge/konfessionen/daten/KonfessionenDatenProps";
import { api } from "~/router/Api";
import { routeKonfessionen, type RouteKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";

const KonfessionenDaten = () => import("~/components/schule/kataloge/konfessionen/daten/KonfessionenDaten.vue");

export class RouteKonfessionenDaten extends RouteNode<any, RouteKonfessionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.konfessionen.daten",
			"daten", KonfessionenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfessionen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		if (routeKonfessionen.data.manager.auswahlID() === null) {
			return routeKonfessionen.getRoute();
		}
	}

	public getProps(to: RouteLocationNormalized): KonfessionenDatenProps {
		return {
			manager: () => routeKonfessionen.data.manager,
			patch: routeKonfessionen.data.patch,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulform: api.schulform,
		};
	}

}

export const routeKonfessionenDaten = new RouteKonfessionenDaten();

