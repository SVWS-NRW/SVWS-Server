import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteKatalogErzieherarten } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherarten";
import { routeKatalogErzieherarten } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherarten";
import type { ErzieherartenDatenProps } from "~/components/schule/allgemein/erzieherarten/daten/SErzieherartenDatenProps";
import { api } from "~/router/Api";

const SErzieherartenDaten = () => import("~/components/schule/allgemein/erzieherarten/daten/SErzieherartenDaten.vue");

export class RouteKatalogErzieherartenDaten extends RouteNode<any, RouteKatalogErzieherarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.erzieherarten.daten", "daten", SErzieherartenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erzieherarten";
	}

	public getProps(to: RouteLocationNormalized): ErzieherartenDatenProps {
		return {
			patch: routeKatalogErzieherarten.data.patch,
			manager: () => routeKatalogErzieherarten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeKatalogErzieherartenDaten = new RouteKatalogErzieherartenDaten();
