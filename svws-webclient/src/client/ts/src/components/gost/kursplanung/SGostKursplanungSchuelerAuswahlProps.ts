import type { SchuelerListeEintrag, GostBlockungsergebnisManager, GostFaecherManager } from "@svws-nrw/svws-core";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

export interface KursplanungSchuelerAuswahlProps {
	setSchueler: (schueler: SchuelerListeEintrag) => Promise<void>;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	schueler: SchuelerListeEintrag | undefined;
	schuelerFilter: GostKursplanungSchuelerFilter;
	faecherManager: GostFaecherManager;
}