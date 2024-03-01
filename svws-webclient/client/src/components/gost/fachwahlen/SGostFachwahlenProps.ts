import type { List, GostStatistikFachwahl, GostHalbjahr } from "@core";

export interface GostFachwahlenProps {
	fachwahlstatistik: List<GostStatistikFachwahl>;
	doSelect: (idFach: number | undefined, bereich: string | undefined, halbjahr?: GostHalbjahr) => Promise<void>;
	selected: () => { idFach?: number, bereich: string };
}