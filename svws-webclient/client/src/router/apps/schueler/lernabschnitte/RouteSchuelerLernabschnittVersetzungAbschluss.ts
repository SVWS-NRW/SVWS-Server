import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittVersetzungAbschlussProps } from "~/components/schueler/lernabschnitte/versetzung/SSchuelerLernabschnittVersetzungAbschlussProps";
import { api } from "~/router/Api";

const SSchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/versetzung/SSchuelerLernabschnittVersetzungAbschluss.vue");

export class RouteSchuelerLernabschnittVersetzungAbschluss extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN ], "schueler.lernabschnitt.versetzung", "versetzung", SSchuelerLernabschnittAllgmein);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Versetzung/Abschluss";
		super.children = [
		];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittVersetzungAbschlussProps {
		return {
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchBemerkungen: routeSchuelerLernabschnitte.data.patchBemerkungen
		};
	}

}

export const routeSchuelerLernabschnittVersetzungAbschluss = new RouteSchuelerLernabschnittVersetzungAbschluss();

