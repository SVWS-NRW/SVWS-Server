import type { RouteLocationRaw } from "vue-router";

import { RouteNode } from "@lupo/router/RouteNode";
import { type RouteApp } from "@lupo/router/apps/RouteApp";

const LadeDaten = () => import("@lupo/components/LadeDaten.vue");


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
