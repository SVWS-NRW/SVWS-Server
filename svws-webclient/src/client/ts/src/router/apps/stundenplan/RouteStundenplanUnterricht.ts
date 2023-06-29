import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteStundenplan} from "~/router/apps/stundenplan/RouteStundenplan";

const SStundenplanUnterricht = () => import("~/components/stundenplan/unterricht/SStundenplanUnterricht.vue");

export class RouteStundenplanUnterricht extends RouteNode<unknown, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.unterricht", "unterricht", SStundenplanUnterricht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Unterricht";
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

export const routeStundenplanUnterricht = new RouteStundenplanUnterricht();

