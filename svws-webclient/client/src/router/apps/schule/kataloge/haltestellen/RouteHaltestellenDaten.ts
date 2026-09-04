import type { HaltestellenDatenProps } from "~/components/schule/kataloge/haltestellen/daten/HaltestellenDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { RouteNode } from "~/router/RouteNode";
import { routeHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const HaltestelleDaten = () => import("~/components/schule/kataloge/haltestellen/daten/HaltestellenDaten.vue");

export class RouteHaltestellenDaten extends RouteNode<any, RouteHaltestellen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.haltestellen.daten",
			"daten", HaltestelleDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Haltestelle";
	}

	public getProps(to: RouteLocationNormalized): HaltestellenDatenProps {
		return {
			manager: () => routeHaltestellen.data.manager,
			patch: routeHaltestellen.data.patch,
		};
	}
}

export const routeHaltestellenDaten = new RouteHaltestellenDaten();
