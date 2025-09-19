import type { HaltestellenDatenProps } from "~/components/schule/allgemein/haltestellen/daten/SHaltestellenDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteHaltestellen } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeHaltestellen } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellen";

const SHaltestelleDaten = () => import("~/components/schule/allgemein/haltestellen/daten/SHaltestellenDaten.vue")

export class RouteHaltestellenDaten extends RouteNode<any, RouteHaltestellen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.haltestellen.daten",
			"daten", SHaltestelleDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Haltestelle"
	}

	public getProps(to: RouteLocationNormalized): HaltestellenDatenProps {
		return {
			manager: () => routeHaltestellen.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeHaltestellen.data.patch,
		}
	}
}

export const routeHaltestellenDaten = new RouteHaltestellenDaten();
