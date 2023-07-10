import type { GostBelegpruefungsArt, GostBelegpruefungsErgebnisse, List } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";
import type { Config } from "~/components/Config";

export interface GostLaufbahnfehlerProps {
	config: Config,
	listBelegpruefungsErgebnisse: () => List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: () => GostBelegpruefungsArt;
	setGostBelegpruefungsArt: (value: GostBelegpruefungsArt) => Promise<void>;
	gotoLaufbahnplanung: (d: number) => Promise<void>;
	getPdfWahlbogen: () => Promise<Blob>;
	resetFachwahlenAlle: () => Promise<void>;
	abiturjahr: number;
	apiStatus: ApiStatus;
}
