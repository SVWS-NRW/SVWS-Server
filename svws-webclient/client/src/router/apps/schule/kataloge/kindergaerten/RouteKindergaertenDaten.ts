import type { RouteLocationNormalized } from "vue-router";
import type { KindergaertenDatenProps } from "~/components/schule/kataloge/kindergaerten/daten/KindergaertenDatenProps";
import type { RouteKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";

const KindergaertenDaten = () => import("~/components/schule/kataloge/kindergaerten/daten/KindergaertenDaten.vue");

export class RouteKindergaertenDaten extends RouteNode<any, RouteKindergaerten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.kindergaerten.daten",
			"daten", KindergaertenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kindergaerten";
	}

	public getProps(to: RouteLocationNormalized): KindergaertenDatenProps {
		return {
			manager: () => routeKindergaerten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeKindergaerten.data.patch,
		};
	}
}

export const routeKindergaertenDaten = new RouteKindergaertenDaten();
