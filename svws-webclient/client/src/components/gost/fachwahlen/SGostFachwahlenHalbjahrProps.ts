import type { GostStatistikFachwahl } from "@core/core/data/gost/GostStatistikFachwahl";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import type { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import type { GostJahrgangsFachwahlenManager } from "@core/core/utils/gost/GostJahrgangsFachwahlenManager";
import type { List } from "@core/java/util/List";

export interface GostFachwahlenHalbjahrProps {
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	fachwahlstatistik: List<GostStatistikFachwahl>;
	fachwahlenManager: GostJahrgangsFachwahlenManager;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
	halbjahr: GostHalbjahr;
}
