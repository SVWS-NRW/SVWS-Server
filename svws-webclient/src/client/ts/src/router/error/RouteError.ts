import type { RouteLocationRaw, RouteParams } from "vue-router";

import { ServerMode} from "@core";
import { BenutzerKompetenz, Schulform } from "@core";

import { RouteNode } from "~/router/RouteNode";

import SError from "~/components/error/SError.vue";
import type { ErrorProps } from "~/components/error/SErrorProps";


export class RouteError extends RouteNode<unknown, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "error", "/error/:errorcode?", SError);
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

	public getRoute(errorcode : number | undefined): RouteLocationRaw {
		return { name: this.name, params: { errorcode: errorcode } };
	}

	public getProps(): ErrorProps {
		return {
			code: 418,
			message: "Test-Message",
			error: new Error("Test Error")
		}
	}

}

export const routeError = new RouteError();
