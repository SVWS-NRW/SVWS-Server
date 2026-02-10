import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { FaecherDatenProps } from "~/components/schule/kataloge/faecher/daten/FaecherDatenProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeFaecher, type RouteFaecher } from "~/router/apps/schule/kataloge/faecher/RouteFaecher";
import { api } from "~/router/Api";

const FaecherDaten = () => import("~/components/schule/kataloge/faecher/daten/FaecherDaten.vue");

export class RouteFaecherDaten extends RouteNode<any, RouteFaecher> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.faecher.daten", "daten", FaecherDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		if (routeFaecher.data.manager.auswahlID() === null) {
			return routeFaecher.getRoute();
		}
	}

	public getProps(to: RouteLocationNormalized): FaecherDatenProps {
		return {
			schuljahr: api.abschnitt.schuljahr,
			patch: routeFaecher.data.patch,
			manager: () => routeFaecher.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
			schulform: api.schulform,
		};
	}

}

export const routeFaecherDaten = new RouteFaecherDaten();

