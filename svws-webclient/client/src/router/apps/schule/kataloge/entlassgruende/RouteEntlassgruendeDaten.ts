import type { RouteLocationNormalized } from "vue-router";
import type { EntlassgruendeDatenProps } from "~/components/schule/kataloge/entlassgruende/daten/EntlassgruendeDatenProps";
import type { RouteEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruende";
import { RouteNode } from "~/router/RouteNode";
import { routeEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruende";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const EntlassgruendeDaten = () => import("~/components/schule/kataloge/entlassgruende/daten/EntlassgruendeDaten.vue");

export class RouteEntlassgruendeDaten extends RouteNode<any, RouteEntlassgruende> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.entlassgruende.daten",
			"daten", EntlassgruendeDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Entlassgründe";
	}

	public getProps(to: RouteLocationNormalized): EntlassgruendeDatenProps {
		return {
			manager: () => routeEntlassgruende.data.manager,
			patch: routeEntlassgruende.data.patch,
		};
	}
}

export const routeEntlassgruendeDaten = new RouteEntlassgruendeDaten();
