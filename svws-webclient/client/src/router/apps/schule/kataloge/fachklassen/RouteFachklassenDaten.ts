import { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassen";
import { routeFachklassen } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassen";
import type { FachklassenDatenProps } from "~/components/schule/kataloge/fachklassen/daten/FachklassenDatenProps";

const FachklassenDaten = () => import("~/components/schule/kataloge/fachklassen/daten/FachklassenDaten.vue");

class RouteFachklassenDaten extends RouteNode<any, RouteFachklassen> {
	public constructor() {
		super([Schulform.BK, Schulform.SB],
			[BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.fachklassen.daten",
			"daten",
			FachklassenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Fachklassen";
	}

	public getProps(): FachklassenDatenProps {
		return {
			patch: routeFachklassen.data.patch,
			manager: () => routeFachklassen.data.manager,
		};
	}
}

export const routeFachklassenDaten = new RouteFachklassenDaten();
