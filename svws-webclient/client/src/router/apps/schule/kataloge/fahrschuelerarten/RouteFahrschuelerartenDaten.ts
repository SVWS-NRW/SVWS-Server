import type { RouteLocationNormalized } from "vue-router";
import type { FahrschuelerartenDatenProps } from "~/components/schule/kataloge/fahrschuelerarten/daten/FahrschuelerartenDatenProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";

const FahrschuelerartenDaten = () => import("~/components/schule/kataloge/fahrschuelerarten/daten/FahrschuelerartenDaten.vue");

export class RouteFahrschuelerartenDaten extends RouteNode<any, RouteFahrschuelerarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.fahrschuelerarten.daten",
			"daten", FahrschuelerartenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fahrschülerart";
	}

	public getProps(to: RouteLocationNormalized): FahrschuelerartenDatenProps {
		return {
			manager: () => routeFahrschuelerarten.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeFahrschuelerarten.data.patch,
		};
	}
}

export const routeFahrschuelerartenDaten = new RouteFahrschuelerartenDaten();
