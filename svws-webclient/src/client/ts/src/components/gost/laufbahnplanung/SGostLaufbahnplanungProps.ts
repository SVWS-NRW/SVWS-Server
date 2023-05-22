import type { GostBelegpruefungsErgebnisse, List } from "@svws-nrw/svws-core";
import type { Config } from "~/components/Config";

export interface GostLaufbahnplanungProps {
	config: Config,
	listBelegpruefungsErgebnisse: () => List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt';
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt') => Promise<void>;
	gotoLaufbahnplanung: (d: number) => Promise<void>;
}
