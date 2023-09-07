import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan} from "~/router/apps/stundenplan/RouteStundenplan";
import { type StundenplanKlasseProps } from "~/components/stundenplan/klasse/SStundenplanKlasseProps";

const SStundenplanKlasse = () => import("~/components/stundenplan/klasse/SStundenplanKlasse.vue");

export class RouteStundenplanKlasse extends RouteNode<unknown, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.klasse", "klasse", SStundenplanKlasse);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klasse";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
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

