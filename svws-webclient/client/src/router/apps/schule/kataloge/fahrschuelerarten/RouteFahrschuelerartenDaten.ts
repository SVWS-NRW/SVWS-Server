import type { RouteLocationNormalized } from "vue-router";
import type { FahrschuelerartenDatenProps } from "~/components/schule/kataloge/fahrschuelerarten/daten/SFahrschuelerartenDatenProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";

const SFahrschuelerartenDaten = () => import("~/components/schule/kataloge/fahrschuelerarten/daten/SFahrschuelerartenDaten.vue");

export class RouteFahrschuelerartenDaten extends RouteNode<any, RouteFahrschuelerarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.fahrschuelerarten.daten",
			"daten", SFahrschuelerartenDaten);
		super.mode = ServerMode.DEV;
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
