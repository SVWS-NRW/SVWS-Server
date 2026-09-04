import type { ConfigAppProps } from "@admin/components/config/SConfigAppProps";
import { api } from "@admin/router/Api";
import { RouteNode } from "@admin/router/RouteNode";
import { ServerMode } from "@core/core/types/ServerMode";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteApp } from "../RouteApp";
import { routeApp } from "../RouteApp";
import { RouteDataConfig } from "./RouteDataConfig";

const SConfigApp = () => import("@admin/components/config/SConfigApp.vue");


export class RouteConfig extends RouteNode<RouteDataConfig, RouteApp> {

	public constructor() {
		super("config", "/config", SConfigApp, new RouteDataConfig());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration";
		super.children = [];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		// Prüfe, ob diese Route von den Rechten des angemeldeten Benutzers überhaupt angesteuert werden darf
		if (!api.isServerAdmin) {
			return routeApp.getRoute();
		}
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
			getCert: this.data.getCert,
			createCert: this.data.createCert,
			uploadCert: this.data.uploadCert,
			apiStatus: api.status,
		};
	}

}

export const routeConfig = new RouteConfig();
