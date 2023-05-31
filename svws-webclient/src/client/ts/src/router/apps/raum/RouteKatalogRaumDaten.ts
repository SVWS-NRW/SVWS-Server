import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteKatalogRaeume} from "../RouteKatalogRaeume";
import type { RaumDatenProps } from "~/components/kataloge/raeume/daten/SRaumDatenProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogRaeume } from "../RouteKatalogRaeume";

const SRaumDaten = () => import("~/components/kataloge/raeume/daten/SRaumDaten.vue");

export class RouteKatalogRaumDaten extends RouteNode<unknown, RouteKatalogRaeume> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.raeume.daten", "daten", SRaumDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKatalogRaeume.data.auswahl === undefined)
			return routeKatalogRaeume.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): RaumDatenProps {
		return {
			patch: routeKatalogRaeume.data.patch,
			data: routeKatalogRaeume.data.daten,
		};
	}

}

export const routeKatalogRaumDaten = new RouteKatalogRaumDaten();

