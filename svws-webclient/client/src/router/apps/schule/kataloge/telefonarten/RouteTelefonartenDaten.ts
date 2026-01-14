import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import type { TelefonartenDatenProps } from "~/components/schule/kataloge/telefonarten/daten/TelefonartenDatenProps";
import { api } from "~/router/Api";

const TelefonartenDaten = () => import("~/components/schule/kataloge/telefonarten/daten/TelefonartenDaten.vue");

export class RouteTelefonartenDaten extends RouteNode<any, RouteTelefonarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.telefonarten.daten", "daten", TelefonartenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Telefonarten";
	}

	public getProps(to: RouteLocationNormalized): TelefonartenDatenProps {
		return {
			patch: routeTelefonarten.data.patch,
			manager: () => routeTelefonarten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeTelefonartenDaten = new RouteTelefonartenDaten();
