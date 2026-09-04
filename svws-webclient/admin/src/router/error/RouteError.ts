import type { RouteLocationRaw, RouteParams } from "vue-router";
import { ServerMode } from "@core/core/types/ServerMode";
import type { ErrorProps } from "@admin/components/error/SErrorProps";
import { RouteManager } from "../RouteManager";
import { RouteNode } from "../RouteNode";

const SError = () => import("@admin/components/error/SError.vue");

export class RouteError extends RouteNode<unknown, any> {

	public constructor() {
		super("error", "/error/:errorcode?", SError);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Fehler";
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
