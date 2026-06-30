import type { RouteLocationRaw } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import { type RouteApp } from "~/router/apps/RouteApp";

const LadeDaten = () => import("~/components/LadeDaten.vue");


export class RouteLadeDaten extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("load", "/load", LadeDaten, null);
		super.text = "Laden";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { } };
	}

}

export const routeLadeDaten = new RouteLadeDaten();
