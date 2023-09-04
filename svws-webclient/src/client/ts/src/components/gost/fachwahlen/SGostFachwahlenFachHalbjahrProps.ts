import type { List, GostStatistikFachwahl, GostJahrgangsFachwahlenManager, SchuelerListeEintrag, GostFaecherManager, GostHalbjahr } from "@core";

export interface GostFachwahlenFachHalbjahrProps {
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
	fachID: number;
	halbjahr: GostHalbjahr;
}
