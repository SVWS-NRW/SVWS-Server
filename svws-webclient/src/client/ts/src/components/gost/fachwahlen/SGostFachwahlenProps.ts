import type { List, GostStatistikFachwahl, GostHalbjahr } from "@core";

export interface GostFachwahlenProps {
	fachwahlen: List<GostStatistikFachwahl>;
	doSelect: (idFach: number | undefined, bereich: string | undefined, halbjahr?: GostHalbjahr) => Promise<void>;
}