import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteKatalogAufsichtsbereiche} from "../RouteKatalogAufsichtsbereiche";
import type { AufsichtsbereichDatenProps } from "~/components/kataloge/aufsichtsbereiche/daten/SAufsichtsbereichDatenProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogAufsichtsbereiche } from "../RouteKatalogAufsichtsbereiche";

const SAufsichtsbereichDaten = () => import("~/components/kataloge/aufsichtsbereiche/daten/SAufsichtsbereichDaten.vue");

export class RouteKatalogAufsichtsbereichDaten extends RouteNode<unknown, RouteKatalogAufsichtsbereiche> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.aufsichtsbereiche.daten", "daten", SAufsichtsbereichDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (routeKatalogAufsichtsbereiche.data.auswahl === undefined)
			return routeKatalogAufsichtsbereiche.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): AufsichtsbereichDatenProps {
		return {
			patch: routeKatalogAufsichtsbereiche.data.patch,
			data: routeKatalogAufsichtsbereiche.data.daten,
		};
	}

}

export const routeKatalogAufsichtsbereichDaten = new RouteKatalogAufsichtsbereichDaten();

