import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittVersetzungAbschlussProps } from "~/components/schueler/lernabschnitte/versetzung/SchuelerLernabschnittVersetzungAbschlussProps";

const SchuelerLernabschnittAllgemein = () => import("~/components/schueler/lernabschnitte/versetzung/SchuelerLernabschnittVersetzungAbschluss.vue");

export class RouteSchuelerLernabschnittVersetzungAbschluss extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.versetzung", "versetzung", SchuelerLernabschnittAllgemein);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Versetzung/Abschluss";
		super.children = [
		];
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittVersetzungAbschlussProps {
		return {
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchBemerkungen: routeSchuelerLernabschnitte.data.patchBemerkungen,
		};
	}

}

export const routeSchuelerLernabschnittVersetzungAbschluss = new RouteSchuelerLernabschnittVersetzungAbschluss();
