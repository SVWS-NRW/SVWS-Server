import type { ApiFile, BenutzerKompetenz, GostBelegpruefungsArt, GostBelegpruefungsErgebnisse, GostJahrgangsdaten, List, Schulform, ServerMode, SimpleOperationResponse } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface GostLaufbahnfehlerProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenAbiturjahrgaenge: Set<number>;
	listBelegpruefungsErgebnisse: () => List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: () => GostBelegpruefungsArt;
	setGostBelegpruefungsArt: (value: GostBelegpruefungsArt) => Promise<void>;
	gotoLaufbahnplanung: (d: number) => Promise<RoutingStatus>;
	gotoSprachenfolge: (d: number) => Promise<RoutingStatus>;
	importLaufbahnplanung: (data: FormData) => Promise<void>;
	exportLaufbahnplanung: (schueler: List<number>) => Promise<ApiFile>;
	getPdfLaufbahnplanung: (title: string, list: List<number>, detaillevel: number, einzelpdfs: boolean) => Promise<ApiFile>;
	resetFachwahlenAlle: () => Promise<void>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	apiStatus: ApiStatus;
	filterFehler: () => boolean;
	setFilterFehler: (value: boolean) => Promise<void>;
	filterExterne: () => boolean;
	setFilterExterne: (value: boolean) => Promise<void>;
}
