import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import { routeLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import type { LernplattformenDatenProps } from "~/components/schule/kataloge/lernplattformen/daten/LernplattformenDatenProps";
import { api } from "~/router/Api";

const LernplattformenDaten = () =>
	import("~/components/schule/kataloge/lernplattformen/daten/LernplattformenDaten.vue");

export class RouteLernplattformenDaten extends RouteNode<any, RouteLernplattformen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.lernplattformen.daten", "daten", LernplattformenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattform";
	}

	public getProps(to: RouteLocationNormalized): LernplattformenDatenProps {
		return {
			patch: routeLernplattformen.data.patch,
			manager: () => routeLernplattformen.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeLernplattformenDaten = new RouteLernplattformenDaten();
