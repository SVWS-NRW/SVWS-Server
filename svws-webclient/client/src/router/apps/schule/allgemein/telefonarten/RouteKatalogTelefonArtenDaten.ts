import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteKatalogTelefonArten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArten";
import { routeKatalogTelefonArten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArten";
import type { TelefonArtenDatenProps } from "~/components/schule/allgemein/telefonarten/daten/STelefonArtenDatenProps";
import { api } from "~/router/Api";

const STelefonArtenDaten = () => import("~/components/schule/allgemein/telefonarten/daten/STelefonArtenDaten.vue");

export class RouteKatalogTelefonArtenDaten extends RouteNode<any, RouteKatalogTelefonArten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.telefonarten.daten", "daten", STelefonArtenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Telefonarten";
	}

	public getProps(to: RouteLocationNormalized): TelefonArtenDatenProps {
		return {
			patch: routeKatalogTelefonArten.data.patch,
			telefonArtListeManager: () => routeKatalogTelefonArten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeKatalogTelefonArtenDaten = new RouteKatalogTelefonArtenDaten();
