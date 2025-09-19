import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { FachDatenProps } from "~/components/schule/schulbezogen/faecher/daten/SFachDatenProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "~/router/apps/schule/schulbezogen/faecher/RouteSchuleFaecher";
import { api } from "~/router/Api";

const SFachDaten = () => import("~/components/schule/schulbezogen/faecher/daten/SFachDaten.vue");

export class RouteSchuleFachDaten extends RouteNode<any, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.faecher.daten", "daten", SFachDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeSchuleFaecher.data.manager.auswahlID() === null)
			return routeSchuleFaecher.getRoute();
	}

	public getProps(to: RouteLocationNormalized): FachDatenProps {
		return {
			patch: routeSchuleFaecher.data.patch,
			manager: () => routeSchuleFaecher.data.manager,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeSchuleFachDaten = new RouteSchuleFachDaten();

