import type { HaltestellenDatenProps } from "~/components/schule/kataloge/haltestellen/daten/HaltestellenDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";

const HaltestelleDaten = () => import("~/components/schule/kataloge/haltestellen/daten/HaltestellenDaten.vue");

export class RouteHaltestellenDaten extends RouteNode<any, RouteHaltestellen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.haltestellen.daten",
			"daten", HaltestelleDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Haltestelle";
	}

	public getProps(to: RouteLocationNormalized): HaltestellenDatenProps {
		return {
			manager: () => routeHaltestellen.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeHaltestellen.data.patch,
		};
	}
}

export const routeHaltestellenDaten = new RouteHaltestellenDaten();
