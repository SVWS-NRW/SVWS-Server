import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteDataSchuelerKAoA";

import type { SchuelerKAoAProps } from "~/components/schueler/kaoa/SSchuelerKaoaProps";


const SSchuelerKaoa = () => import("~/components/schueler/kaoa/SSchuelerKaoa.vue");

export class RouteSchuelerKAoA extends RouteNode<RouteDataSchuelerKAoA, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f=>!f.equals(Schulform.G)), [ BenutzerKompetenz.KEINE ], "schueler.kaoa", "kaoa", SSchuelerKaoa, new RouteDataSchuelerKAoA());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "KAoA";
		super.isHidden = (params?: RouteParams) => routeSchueler.data.auswahl === undefined;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (this.parent === undefined)
			return routeError.getRoute(new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert"));
		if (to_params.id === undefined) {
			await this.data.ladeDaten(undefined);
		} else {
			const id = parseInt(to_params.id);
			try {
				await this.data.ladeDaten(this.parent.data.mapSchueler.get(id));
			} catch(error) {
				return routeSchueler.getRoute(id);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerKAoAProps {
		return {
			data: this.data.data,
			patch: this.data.patch,
		 };
	}

}

export const routeSchuelerKAoA = new RouteSchuelerKAoA();

