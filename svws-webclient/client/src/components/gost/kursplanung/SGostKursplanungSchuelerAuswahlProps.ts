import type { SchuelerListeEintrag, GostBlockungsergebnisManager, GostFaecherManager, GostBlockungsdatenManager } from "@core";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
import type { Config } from "~/components/Config";

export interface KursplanungSchuelerAuswahlProps {
	setSchueler: (schueler: SchuelerListeEintrag) => Promise<void>;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	getDatenmanager: () => GostBlockungsdatenManager;
	schueler: SchuelerListeEintrag | undefined;
	schuelerFilter: () => GostKursplanungSchuelerFilter;
	faecherManager: GostFaecherManager;
	config: Config;
}