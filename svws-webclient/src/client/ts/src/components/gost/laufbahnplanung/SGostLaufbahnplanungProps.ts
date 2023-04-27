import type { GostBelegpruefungsArt, GostBelegpruefungsErgebnisse, List } from "@svws-nrw/svws-core";
import type { Config } from "~/components/Config";

export interface GostLaufbahnplanungProps {
	config: Config,
	listBelegpruefungsErgebnisse: List<GostBelegpruefungsErgebnisse>;
	belegpruefungsart: GostBelegpruefungsArt;
	setBelegpruefungsart: (belegpruefungsart: GostBelegpruefungsArt) => Promise<void>;
}
