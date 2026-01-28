import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import { routeFaecher, type RouteFaecher } from "./RouteFaecher";
import type { FaecherGruppenprozesseProps } from "~/components/schule/kataloge/faecher/gruppenprozesse/FaecherGruppenprozesseProps";

const FaecherGruppenprozesse = () => import("~/components/schule/kataloge/faecher/gruppenprozesse/FaecherGruppenprozesse.vue");

export class RouteFaecherGruppenprozesse extends RouteNode<any, RouteFaecher> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.faecher.gruppenprozesse", "gruppenprozesse", FaecherGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		await routeFaecher.data.updateMapStundenplaene();
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FaecherGruppenprozesseProps {
		return {
			getPDF: routeFaecher.data.getPDF,
			stundenplaeneById: routeFaecher.data.stundenplaeneById,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeFaecher.data.manager,
			deleteFaecher: routeFaecher.data.delete,
			deleteFaecherCheck: routeFaecher.data.deleteFaecherCheck,
			sortFaecher: routeFaecher.data.sortFaecher,
			schuljahr: api.abschnitt.schuljahr,
		};
	}

}

export const routeFaecherGruppenprozesse = new RouteFaecherGruppenprozesse();

