import { RouteNode } from "~/router/RouteNode";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { routeSchuelerStundenplanDaten } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplanDaten";
import { RouteLocationNormalized, RouteParams } from "vue-router";

const SSchuelerStundenplan = () => import("~/components/schueler/stundenplan/SSchuelerStundenplan.vue");

export class RouteSchuelerStundenplan extends RouteNode<unknown> {

	public constructor() {
		super("schueler_stundenplan", "stundenplan", SSchuelerStundenplan);
		super.propHandler = (route) => routeSchueler.getProps(route);
		super.text = "Stundenplan";
		super.children = [
			routeSchuelerStundenplanDaten
		];
	}

    public async beforeEach(to: RouteNode<unknown>, to_params: RouteParams, from: RouteNode<unknown> | undefined, from_params: RouteParams): Promise<any> {
		return { name: routeSchuelerStundenplanDaten.name, params: to_params };
    }

}

export const routeSchuelerStundenplan = new RouteSchuelerStundenplan();

