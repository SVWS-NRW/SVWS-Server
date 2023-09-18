import type { List, GostStatistikFachwahl, GostJahrgangsFachwahlenManager, SchuelerListeEintrag, GostFaecherManager } from "@core";

export interface GostFachwahlenAbiturProps {
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
}