import type { RouteLocationNormalized } from "vue-router";
import type { KindergaertenDatenProps } from "~/components/schule/allgemein/kindergaerten/daten/SKindergaertenDatenProps";
import type { RouteKindergaerten } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaerten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeKindergaerten } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaerten";

const SKindergaertenDaten = () => import("~/components/schule/allgemein/kindergaerten/daten/SKindergaertenDaten.vue")

export class RouteKindergaertenDaten extends RouteNode<any, RouteKindergaerten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.kindergaerten.daten",
			"daten", SKindergaertenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kindergaerten"
	}

	public getProps(to: RouteLocationNormalized): KindergaertenDatenProps {
		return {
			manager: () => routeKindergaerten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeKindergaerten.data.patch,
		}
	}
}

export const routeKindergaertenDaten = new RouteKindergaertenDaten();
