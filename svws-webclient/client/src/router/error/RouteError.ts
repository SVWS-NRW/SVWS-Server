import type { RouteLocationRaw, RouteParams } from "vue-router";

import { DeveloperNotificationException, OpenApiError, ServerMode} from "@core";
import { BenutzerKompetenz, Schulform } from "@core";

import { RouteNode } from "~/router/RouteNode";

import SError from "~/components/error/SError.vue";
import type { ErrorProps } from "~/components/error/SErrorProps";
import { routerManager } from "~/router/RouteManager";
import { api } from "../Api";


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

	public async getErrorRoute(error?: Error, errorcode? : number): Promise<RouteLocationRaw> {
		routerManager.resetErrorState();
		routerManager.errorcode = errorcode;
		routerManager.error = error;
		routerManager.errortext = undefined;
		if ((error instanceof OpenApiError) && (error.response !== null)) {
			routerManager.errorcode = error.response.status;
			routerManager.errortext = await error.response.text();
		}
		const params = errorcode === undefined ? {} : { errorcode };
		return { name: this.name, params: params };
	}

	public getSimpleErrorRoute(error?: Error, errorcode? : number): RouteLocationRaw {
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
			errortext: routerManager.errortext,
			api: api,
			benutzerKompetenzen: api.benutzerKompetenzen,
		}
	}

}

export const routeError = new RouteError();
