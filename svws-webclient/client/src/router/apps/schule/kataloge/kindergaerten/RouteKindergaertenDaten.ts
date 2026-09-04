import type { RouteLocationNormalized } from "vue-router";
import type { KindergaertenDatenProps } from "~/components/schule/kataloge/kindergaerten/daten/KindergaertenDatenProps";
import type { RouteKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { RouteNode } from "~/router/RouteNode";
import { routeKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

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
			patch: routeKindergaerten.data.patch,
		};
	}
}

export const routeKindergaertenDaten = new RouteKindergaertenDaten();
