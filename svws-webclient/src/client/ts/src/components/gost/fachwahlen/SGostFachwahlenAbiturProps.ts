import type { List, GostStatistikFachwahl, GostJahrgangsFachwahlenManager, SchuelerListeEintrag, GostFaecherManager } from "@core";

export interface GostFachwahlenAbiturProps {
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
}