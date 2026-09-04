import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { RouteNode } from "~/router/RouteNode";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import type { BeschaeftigungsartenGruppenprozesseProps } from "~/components/schule/kataloge/beschaeftigungsarten/gruppenprozesse/BeschaeftigungsartenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const BeschaeftigungsartenGruppenprozesse = () => import(
	"~/components/schule/kataloge/beschaeftigungsarten/gruppenprozesse/BeschaeftigungsartenGruppenprozesse.vue");

export class RouteBeschaeftigungsartenGruppenprozesse extends RouteNode<any, RouteBeschaeftigungsarten> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.beschaeftigungsarten.gruppenprozesse", "gruppenprozesse", BeschaeftigungsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): BeschaeftigungsartenGruppenprozesseProps {
		return {
			delete: routeBeschaeftigungsarten.data.delete,
			deleteCheck: routeBeschaeftigungsarten.data.deleteCheck,
			manager: () => routeBeschaeftigungsarten.data.manager,
		};
	}
}

export const routeBeschaeftigungsartenGruppenprozesse = new RouteBeschaeftigungsartenGruppenprozesse();
