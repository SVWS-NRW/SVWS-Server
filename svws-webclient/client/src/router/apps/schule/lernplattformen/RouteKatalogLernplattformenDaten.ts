import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteKatalogLernplattformen } from "~/router/apps/schule/lernplattformen/RouteKatalogLernplattformen";
import { routeKatalogLernplattformen } from "~/router/apps/schule/lernplattformen/RouteKatalogLernplattformen";
import type { LernplattformenDatenProps } from "~/components/schule/kataloge/lernplattformen/daten/SLernplattformenDatenProps";
import { api } from "~/router/Api";

const SLernplattformenDaten = () => import("~/components/schule/kataloge/lernplattformen/daten/SLernplattformenDaten.vue");

export class RouteKatalogLernplattformenDaten extends RouteNode<any, RouteKatalogLernplattformen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.lernplattformen.daten", "daten", SLernplattformenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattform";
	}

	public getProps(to: RouteLocationNormalized): LernplattformenDatenProps {
		return {
			patch: routeKatalogLernplattformen.data.patch,
			lernplattformListeManager: () => routeKatalogLernplattformen.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeKatalogLernplattformenDaten = new RouteKatalogLernplattformenDaten();
