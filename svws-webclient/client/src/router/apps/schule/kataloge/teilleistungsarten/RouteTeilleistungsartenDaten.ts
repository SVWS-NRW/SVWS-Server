import { RouteNode } from "~/router/RouteNode";
import { routeTeilleistungsarten, type RouteTeilleistungsarten } from "./RouteTeilleistungsarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized } from "vue-router";
import type { TeilleistungsartenDatenProps } from "~/components/schule/kataloge/teilleistungsarten/daten/TeilleistungsartenDatenProps";
import { api } from "~/router/Api";

const TeilleistungsartenDaten = () =>
	import("~/components/schule/kataloge/teilleistungsarten/daten/TeilleistungsartenDaten.vue");

class RouteTeilleistungsartenDaten extends RouteNode<any, RouteTeilleistungsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.teilleistungsarten.daten",
			"daten", TeilleistungsartenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Teilleistungsarten";
	}

	public getProps(to: RouteLocationNormalized): TeilleistungsartenDatenProps {
		return {
			patch: routeTeilleistungsarten.data.patch,
			manager: () => routeTeilleistungsarten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeTeilleistungsartenDaten = new RouteTeilleistungsartenDaten();
