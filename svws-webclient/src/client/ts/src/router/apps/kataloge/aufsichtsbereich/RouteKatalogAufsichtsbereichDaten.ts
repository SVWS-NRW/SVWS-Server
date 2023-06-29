import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { routeKatalogAufsichtsbereiche, type RouteKatalogAufsichtsbereiche } from "~/router/apps/kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereiche";

import type { AufsichtsbereichDatenProps } from "~/components/kataloge/aufsichtsbereiche/daten/SAufsichtsbereichDatenProps";

const SAufsichtsbereichDaten = () => import("~/components/kataloge/aufsichtsbereiche/daten/SAufsichtsbereichDaten.vue");

export class RouteKatalogAufsichtsbereichDaten extends RouteNode<unknown, RouteKatalogAufsichtsbereiche> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.aufsichtsbereiche.daten", "daten", SAufsichtsbereichDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Aufsichtsbereich";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
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

