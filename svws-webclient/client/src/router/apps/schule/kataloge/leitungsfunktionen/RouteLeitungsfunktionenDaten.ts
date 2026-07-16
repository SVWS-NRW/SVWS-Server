import type { LeitungsfunktionenDatenProps } from "~/components/schule/kataloge/leitungsfunktionen/daten/LeitungsfunktionenDatenProps";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteLeitungsfunktionen } from "./RouteLeitungsfunktionen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeLeitungsfunktionen } from "./RouteLeitungsfunktionen";

const LeitungsfunktionenDaten = () => import("~/components/schule/kataloge/leitungsfunktionen/daten/LeitungsfunktionenDaten.vue");

export class RouteLeitungsfunktionenDaten extends RouteNode<any, RouteLeitungsfunktionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.leitungsfunktionen.daten",
			"daten", LeitungsfunktionenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leitungsfunktion";
	}

	public getProps(to: RouteLocationNormalized): LeitungsfunktionenDatenProps {
		return {
			manager: () => routeLeitungsfunktionen.data.manager,
			patch: routeLeitungsfunktionen.data.patch,
		};
	}
}

export const routeLeitungsfunktionenDaten = new RouteLeitungsfunktionenDaten();
