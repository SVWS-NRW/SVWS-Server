import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan} from "~/router/apps/stundenplan/RouteStundenplan";
import { type StundenplanKlasseProps } from "~/components/stundenplan/klasse/SStundenplanKlasseProps";
import { routeApp } from "../RouteApp";

const SStundenplanKlasse = () => import("~/components/stundenplan/klasse/SStundenplanKlasse.vue");

export class RouteStundenplanKlasse extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN ], "stundenplan.klasse", "klasse", SStundenplanKlasse);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanKlasseProps {
		return {
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			patchUnterricht: routeStundenplan.data.patchUnterricht,
			addUnterrichtKlasse: routeStundenplan.data.addUnterrichtKlasse,
			removeUnterrichtKlasse: routeStundenplan.data.removeUnterrichtKlasse,
		};
	}

}

export const routeStundenplanKlasse = new RouteStundenplanKlasse();

