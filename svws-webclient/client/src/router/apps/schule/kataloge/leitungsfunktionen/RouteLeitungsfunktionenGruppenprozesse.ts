import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteLeitungsfunktionen } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeLeitungsfunktionen } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionen";
import type { LeitungsfunktionenGruppenprozesseProps } from "~/components/schule/kataloge/leitungsfunktionen/gruppenprozesse/LeitungsfunktionenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const LeitungsfunktionenGruppenprozesse = () => import("~/components/schule/kataloge/leitungsfunktionen/gruppenprozesse/LeitungsfunktionenGruppenprozesse.vue");

export class RouteLeitungsfunktionenGruppenprozesse extends RouteNode<any, RouteLeitungsfunktionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.leitungsfunktionen.gruppenprozesse", "gruppenprozesse", LeitungsfunktionenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): LeitungsfunktionenGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			delete: routeLeitungsfunktionen.data.delete,
			deleteCheck: routeLeitungsfunktionen.data.deleteCheck,
			manager: () => routeLeitungsfunktionen.data.manager,
		};
	}
}

export const routeLeitungsfunktionenGruppenprozesse = new RouteLeitungsfunktionenGruppenprozesse();
