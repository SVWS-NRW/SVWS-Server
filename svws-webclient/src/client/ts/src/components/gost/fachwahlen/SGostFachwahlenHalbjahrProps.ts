import type { List, GostStatistikFachwahl, GostJahrgangsFachwahlenManager, SchuelerListeEintrag, GostFaecherManager, GostHalbjahr } from "@core";

export interface GostFachwahlenHalbjahrProps {
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
	halbjahr: GostHalbjahr;
}
