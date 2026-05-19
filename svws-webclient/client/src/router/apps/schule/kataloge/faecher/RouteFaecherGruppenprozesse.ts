import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeFaecher, type RouteFaecher } from "./RouteFaecher";
import type { FaecherGruppenprozesseProps } from "~/components/schule/kataloge/faecher/gruppenprozesse/FaecherGruppenprozesseProps";
import { abschnittState } from "~/states/AbschnittStateImpl";

const FaecherGruppenprozesse = () => import("~/components/schule/kataloge/faecher/gruppenprozesse/FaecherGruppenprozesse.vue");

export class RouteFaecherGruppenprozesse extends RouteNode<any, RouteFaecher> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.faecher.gruppenprozesse", "gruppenprozesse", FaecherGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittState.auswahl.id, id: "" } };
	}

	public getProps(_: RouteLocationNormalized): FaecherGruppenprozesseProps {
		return {
			getPDF: routeFaecher.data.getPDF,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeFaecher.data.manager,
			delete: routeFaecher.data.delete,
			deleteCheck: routeFaecher.data.deleteCheck,
			sortFaecher: routeFaecher.data.sortFaecher,
		};
	}

}

export const routeFaecherGruppenprozesse = new RouteFaecherGruppenprozesse();

