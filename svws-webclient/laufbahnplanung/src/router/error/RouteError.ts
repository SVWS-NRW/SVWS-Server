import type { RouteLocationRaw, RouteParams } from "vue-router";

import { RouteNode } from "@lupo/router/RouteNode";

import SError from "@lupo/components/error/SError.vue";
import type { ErrorProps } from "@lupo/components/error/SErrorProps";
import { RouteManager } from "@lupo/router/RouteManager";


export class RouteError extends RouteNode<unknown, any> {

	public constructor() {
		super("error", "/error/:errorcode?", SError);
		super.propHandler = (route) => this.getProps();
		super.text = "Fehler";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		if (to_params.error instanceof Array) {
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		}
	}

	public getRoute(error?: Error, errorcode?: number): RouteLocationRaw {
		RouteManager.instance.resetErrorState();
		RouteManager.instance.errorcode = errorcode;
		RouteManager.instance.error = error;
		const params = errorcode === undefined ? {} : { errorcode };
		return { name: this.name, params: params };
	}

	public getProps(): ErrorProps {
		return {
			code: RouteManager.instance.errorcode,
			error: RouteManager.instance.error,
		};
	}

}

export const routeError = new RouteError();
