import type { GostStatistikFachwahl } from "@core/core/data/gost/GostStatistikFachwahl";
import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import type { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import type { List } from "@core/java/util/List";

export interface GostFachwahlenProps {
	fachwahlstatistik: List<GostStatistikFachwahl>;
	faecherManager: GostFaecherManager;
	doSelect: (idFach: number | undefined, bereich: string | undefined, halbjahr?: GostHalbjahr) => Promise<void>;
	selected: () => { idFach?: number, bereich: string };
}