import type { RouteLocationNormalized } from "vue-router";
import type { FahrschuelerartenDatenProps } from "~/components/schule/kataloge/fahrschuelerarten/daten/FahrschuelerartenDatenProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { RouteNode } from "~/router/RouteNode";
import { routeFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

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
			patch: routeFahrschuelerarten.data.patch,
		};
	}
}

export const routeFahrschuelerartenDaten = new RouteFahrschuelerartenDaten();
