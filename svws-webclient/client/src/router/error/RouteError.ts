import type { RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import SError from "~/components/error/SError.vue";
import type { ErrorProps } from "~/components/error/SErrorProps";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { OpenApiError } from "@core/api/OpenApiError";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";


export class RouteError extends RouteNode<any, any> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "error", "/error/:errorcode?", SError);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Fehler";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (Array.isArray(to_params.error)) {
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		}
	}

	public async getErrorRoute(error?: Error, errorcode?: number): Promise<RouteLocationRaw> {
		RouteManager.instance.resetErrorState();
		RouteManager.instance.errorcode = errorcode;
		RouteManager.instance.error = error;
		RouteManager.instance.errortext = undefined;
		if ((error instanceof OpenApiError) && (error.response !== null)) {
			RouteManager.instance.errorcode = error.response.status;
			RouteManager.instance.errortext = await error.response.text();
		}
		const params = errorcode === undefined ? {} : { errorcode };
		return { name: this.name, params: params };
	}

	public getSimpleErrorRoute(error?: Error, errorcode?: number): RouteLocationRaw {
		RouteManager.instance.resetErrorState();
		RouteManager.instance.errorcode = errorcode;
		RouteManager.instance.error = error;
		const params = errorcode === undefined ? {} : { errorcode };
		return { name: this.name, params: params };
	}

	public getProps(): ErrorProps {
		console.error(RouteManager.instance.error);
		return {
			code: RouteManager.instance.errorcode,
			error: RouteManager.instance.error,
			errortext: RouteManager.instance.errortext,
			api: api,
		};
	}

}

export const routeError = new RouteError();
