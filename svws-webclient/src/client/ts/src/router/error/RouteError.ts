import type { RouteLocationRaw, RouteParams } from "vue-router";

import { ServerMode} from "@core";
import { BenutzerKompetenz, Schulform } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteDataError } from "~/router/error/RouteDataError";

import SError from "~/components/error/SError.vue";
import type { ErrorProps } from "~/components/error/SErrorProps";


export class RouteError extends RouteNode<RouteDataError, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "error", "/error/:errorcode?", SError, new RouteDataError());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Fehler";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.error instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
	}

	public getRoute(error?: Error, errorcode? : number): RouteLocationRaw {
		this.data.reset();
		this.data.code = errorcode;
		this.data.error = error;
		const params = errorcode === undefined ? {} : { errorcode };
		return { name: this.name, params: params };
	}

	public getProps(): ErrorProps {
		return {
			code: this.data.code,
			error: this.data.error
		}
	}

}

export const routeError = new RouteError();
