import type { RouteLocationNormalized } from "vue-router";
import type { EntlassgruendeDatenProps } from "~/components/schule/schulbezogen/entlassgruende/daten/SEntlassgruendeDatenProps";
import type { RouteEntlassgruende } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteEntlassgruende";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeEntlassgruende } from "~/router/apps/schule/schulbezogen/entlassgruende/RouteEntlassgruende";
import { api } from "~/router/Api";

const SEntlassgruendeDaten = () => import("~/components/schule/schulbezogen/entlassgruende/daten/SEntlassgruendeDaten.vue")

export class RouteEntlassgruendeDaten extends RouteNode<any, RouteEntlassgruende> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.entlassgruende.daten",
			"daten", SEntlassgruendeDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Entlassgründe"
	}

	public getProps(to: RouteLocationNormalized): EntlassgruendeDatenProps {
		return {
			manager: () => routeEntlassgruende.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeEntlassgruende.data.patch,
		}
	}
}

export const routeEntlassgruendeDaten = new RouteEntlassgruendeDaten();
