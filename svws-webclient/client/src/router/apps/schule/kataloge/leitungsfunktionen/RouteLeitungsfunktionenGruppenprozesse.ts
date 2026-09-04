import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteLeitungsfunktionen } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionen";
import { RouteNode } from "~/router/RouteNode";
import { routeLeitungsfunktionen } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionen";
import type { LeitungsfunktionenGruppenprozesseProps } from "~/components/schule/kataloge/leitungsfunktionen/gruppenprozesse/LeitungsfunktionenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

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
			delete: routeLeitungsfunktionen.data.delete,
			deleteCheck: routeLeitungsfunktionen.data.deleteCheck,
			manager: () => routeLeitungsfunktionen.data.manager,
		};
	}
}

export const routeLeitungsfunktionenGruppenprozesse = new RouteLeitungsfunktionenGruppenprozesse();
