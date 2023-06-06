import { RouteNode } from "~/router/RouteNode";
import type { RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeSchuelerStundenplanDaten } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplanDaten";
import type { RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform } from "@core";

const SSchuelerStundenplan = () => import("~/components/schueler/stundenplan/SSchuelerStundenplan.vue");

export class RouteSchuelerStundenplan extends RouteNode<unknown, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.stundenplan", "stundenplan", SSchuelerStundenplan);
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeSchuelerStundenplanDaten
		];
		super.defaultChild = routeSchuelerStundenplanDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return { name: routeSchuelerStundenplanDaten.name, params: to_params };
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

}

export const routeSchuelerStundenplan = new RouteSchuelerStundenplan();

