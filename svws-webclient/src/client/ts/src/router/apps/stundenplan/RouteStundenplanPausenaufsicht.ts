import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteStundenplan} from "~/router/apps/RouteStundenplan";

const SStundenplanPausenaufsicht = () => import("~/components/stundenplan/pausenaufsicht/SStundenplanPausenaufsicht.vue");

export class RouteStundenplanPausenaufsicht extends RouteNode<unknown, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.pausenaufsicht", "pausenaufsicht", SStundenplanPausenaufsicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausenaufsicht";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
		};
	}

}

export const routeStundenplanPausenaufsicht = new RouteStundenplanPausenaufsicht();

