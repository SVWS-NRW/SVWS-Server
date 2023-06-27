import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteKatalogRaeume} from "../RouteKatalogRaeume";
import type { PausenzeitDatenProps } from "~/components/kataloge/pausenzeiten/daten/SPausenzeitDatenProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogPausenzeiten } from "../RouteKatalogPausenzeiten";

const SPausenzeitDaten = () => import("~/components/kataloge/pausenzeiten/daten/SPausenzeitDaten.vue");

export class RouteKatalogPausenzeitDaten extends RouteNode<unknown, RouteKatalogRaeume> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.pausenzeiten.daten", "daten", SPausenzeitDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausenzeit";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogPausenzeiten.data.auswahl === undefined)
			return routeKatalogPausenzeiten.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): PausenzeitDatenProps {
		return {
			patch: routeKatalogPausenzeiten.data.patch,
			data: routeKatalogPausenzeiten.data.daten,
		};
	}

}

export const routeKatalogPausenzeitDaten = new RouteKatalogPausenzeitDaten();

