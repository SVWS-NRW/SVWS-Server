import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { AbteilungenGruppenprozesseProps } from "~/components/schule/kataloge/abteilungen/gruppenprozesse/AbteilungenGruppenprozesseProps";
import type { RouteAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { RouteNode } from "~/router/RouteNode";
import { routeAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const AbteilungenGruppenprozesse = () => import("~/components/schule/kataloge/abteilungen/gruppenprozesse/AbteilungenGruppenprozesse.vue");

export class RouteAbteilungenGruppenprozesse extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.abteilungen.gruppenprozesse", "gruppenprozesse", AbteilungenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): AbteilungenGruppenprozesseProps {
		return {
			manager: () => routeAbteilungen.data.manager,
			delete: routeAbteilungen.data.delete,
		};
	};
}

export const routeAbteilungenGruppenprozesse = new RouteAbteilungenGruppenprozesse();
