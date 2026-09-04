import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { FaecherDatenProps } from "~/components/schule/kataloge/faecher/daten/FaecherDatenProps";
import { RouteNode } from "~/router/RouteNode";
import { routeFaecher, type RouteFaecher } from "~/router/apps/schule/kataloge/faecher/RouteFaecher";

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
			patch: routeFaecher.data.patch,
			manager: () => routeFaecher.data.manager,
		};
	}

}

export const routeFaecherDaten = new RouteFaecherDaten();

