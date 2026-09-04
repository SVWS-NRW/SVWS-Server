import { RouteNode } from "~/router/RouteNode";
import type { RouteLocation, RouteLocationNormalized, RouteParams } from "vue-router";
import type { RouteSchuelerLernabschnitte } from "./RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittFoerderempfehlungenProps } from "~/components/schueler/lernabschnitte/foerderempfehlungen/SchuelerLernabschnittFoerderempfehlungenProps";
import { RouteDataSchuelerLernabschnittFoerderempfehlungen } from "./RouteDataSchuelerLernabschnittFoerderempfehlungen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const SchuelerLernabschnittFoerderempfehlungen = () => import("~/components/schueler/lernabschnitte/foerderempfehlungen/SchuelerLernabschnittFoerderempfehlungen.vue");

class RouteSchuelerLernabschnittFoerderempfehlungen extends RouteNode<RouteDataSchuelerLernabschnittFoerderempfehlungen, RouteSchuelerLernabschnitte> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.foerderempfehlungen", "foerderempfehlungen", SchuelerLernabschnittFoerderempfehlungen, new RouteDataSchuelerLernabschnittFoerderempfehlungen);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderempfehlungen";
		super.children = [];
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocation> {
		await this.data.ladeFoerderempfehlungen();
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittFoerderempfehlungenProps {
		return {
			foerderempfehlungen: () => this.data.listFoerderempfehlungen,
			add: this.data.addFoerderempfehlung,
			patch: this.data.patchFoerderempfehlung,
			delete: this.data.deleteFoerderempfehlungen,
		};
	}
}

export const routeSchuelerLernabschnittFoerderempfehlungen = new RouteSchuelerLernabschnittFoerderempfehlungen();
