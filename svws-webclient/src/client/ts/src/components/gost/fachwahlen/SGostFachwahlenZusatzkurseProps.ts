import type { List, GostStatistikFachwahl, GostJahrgangsFachwahlenManager, SchuelerListeEintrag, GostFaecherManager } from "@core";

export interface GostFachwahlenZusatzkurseProps {
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
}