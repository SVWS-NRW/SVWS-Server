import type { ApiFile, GostBelegpruefungsArt, GostBelegpruefungsErgebnisse, List, SimpleOperationResponse } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";
import type { Config } from "~/components/Config";

export interface GostLaufbahnfehlerProps {
	config: Config,
	listBelegpruefungsErgebnisse: () => List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: () => GostBelegpruefungsArt;
	setGostBelegpruefungsArt: (value: GostBelegpruefungsArt) => Promise<void>;
	gotoLaufbahnplanung: (d: number) => Promise<void>;
	importLaufbahnplanung: (data: FormData) => Promise<SimpleOperationResponse>;
	exportLaufbahnplanung: (schueler: List<number>) => Promise<ApiFile>;
	getPdfLaufbahnplanung: (title: string, list: List<number>, detaillevel: number) => Promise<ApiFile>;
	resetFachwahlenAlle: () => Promise<void>;
	abiturjahr: number;
	apiStatus: ApiStatus;
}
