import type { RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import SError from "~/components/error/SError.vue";
import type { ErrorProps } from "~/components/error/SErrorProps";
import { routerManager } from "~/router/RouteManager";
import { api } from "../Api";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";


export class RouteError extends RouteNode<any, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "error", "/error/:errorcode?", SError);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Fehler";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.error instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
	}

	public getErrorRoute(error?: Error, errorcode? : number): RouteLocationRaw {
		routerManager.resetErrorState();
		routerManager.errorcode = errorcode;
		routerManager.error = error;
		const params = errorcode === undefined ? {} : { errorcode };
		return { name: this.name, params: params };
	}

	public getProps(): ErrorProps {
		console.error(routerManager.error);
		return {
			code: routerManager.errorcode,
			error: routerManager.error,
			api: api,
		}
	}

}

export const routeError = new RouteError();
