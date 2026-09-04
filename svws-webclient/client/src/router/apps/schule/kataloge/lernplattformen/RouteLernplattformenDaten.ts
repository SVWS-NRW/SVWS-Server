import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import { routeLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import type { LernplattformenDatenProps } from "~/components/schule/kataloge/lernplattformen/daten/LernplattformenDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const LernplattformenDaten = () =>
	import("~/components/schule/kataloge/lernplattformen/daten/LernplattformenDaten.vue");

export class RouteLernplattformenDaten extends RouteNode<any, RouteLernplattformen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.lernplattformen.daten", "daten", LernplattformenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattform";
	}

	public getProps(to: RouteLocationNormalized): LernplattformenDatenProps {
		return {
			patch: routeLernplattformen.data.patch,
			manager: () => routeLernplattformen.data.manager,
		};
	}

}

export const routeLernplattformenDaten = new RouteLernplattformenDaten();
