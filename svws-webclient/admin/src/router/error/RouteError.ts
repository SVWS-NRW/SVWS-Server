import type { RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { ErrorProps } from "~/components/error/SErrorProps";
import { routerManager } from "~/router/RouteManager";
import { ServerMode } from "@core/core/types/ServerMode";

const SError = () => import("~/components/error/SError.vue")

export class RouteError extends RouteNode<unknown, any> {

	public constructor() {
		super("error", "/error/:errorcode?", SError);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Fehler";
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.error instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
	}

	public getRoute(error?: Error, errorcode? : number): RouteLocationRaw {
		routerManager.resetErrorState();
		routerManager.errorcode = errorcode;
		routerManager.error = error;
		const params = errorcode === undefined ? {} : { errorcode };
		return { name: this.name, params: params };
	}

	public getProps(): ErrorProps {
		return {
			code: routerManager.errorcode,
			error: routerManager.error,
		}
	}

}

export const routeError = new RouteError();
