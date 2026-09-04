import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import type { TelefonartenDatenProps } from "~/components/schule/kataloge/telefonarten/daten/TelefonartenDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const TelefonartenDaten = () => import("~/components/schule/kataloge/telefonarten/daten/TelefonartenDaten.vue");

export class RouteTelefonartenDaten extends RouteNode<any, RouteTelefonarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.telefonarten.daten", "daten", TelefonartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Telefonarten";
	}

	public getProps(to: RouteLocationNormalized): TelefonartenDatenProps {
		return {
			patch: routeTelefonarten.data.patch,
			manager: () => routeTelefonarten.data.manager,
		};
	}
}

export const routeTelefonartenDaten = new RouteTelefonartenDaten();
