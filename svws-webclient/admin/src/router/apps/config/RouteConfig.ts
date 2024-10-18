import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";


import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";

import { RouteDataConfig } from "~/router/apps/config/RouteDataConfig";

import type { ConfigAppProps } from "~/components/config/SConfigAppProps";
import { api } from "~/router/Api";
import { ServerMode } from "../../../../../core/src/core/types/ServerMode";

const SConfigApp = () => import("~/components/config/SConfigApp.vue")


export class RouteConfig extends RouteNode<RouteDataConfig, RouteApp> {

	public constructor() {
		super("config", "/config", SConfigApp, new RouteDataConfig());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration";
		super.children = [ ];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe, ob diese Route von den Rechten des angemeldeten Benutzers überhaupt angesteuert werden darf
		if (!api.isServerAdmin)
			return routeApp.getRoute();
		// TODO
	}

	protected async leaveBefore(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		// TODO Aufräumen der Konfigurationsdaten, damit diese beim Abmelden nicht erhalten bleiben!
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { } };
	}

	public getProps(to: RouteLocationNormalized): ConfigAppProps {
		return {
		};
	}

}

export const routeConfig = new RouteConfig();
