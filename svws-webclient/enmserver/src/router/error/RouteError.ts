import type { RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "@wenom/router/RouteNode";
import SError from "@wenom/components/error/SError.vue";
import type { ErrorProps } from "@wenom/components/error/SErrorProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { RouteManager } from "../RouteManager";


export class RouteError extends RouteNode<any, any> {

	public constructor() {
		super(Schulform.values(), "error", "/error/:errorcode?", SError);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Fehler";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (Array.isArray(to_params.error)) {
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		}
	}

	public getErrorRoute(error?: Error, errorcode?: number): RouteLocationRaw {
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
		};
	}

}

export const routeError = new RouteError();
