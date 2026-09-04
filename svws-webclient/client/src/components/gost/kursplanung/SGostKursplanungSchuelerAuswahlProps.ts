import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { GostBlockungRegelUpdate } from "@core/core/data/gost/GostBlockungRegelUpdate";
import type { GostBlockungsdatenManager } from "@core/core/utils/gost/GostBlockungsdatenManager";
import type { GostBlockungsergebnisManager } from "@core/core/utils/gost/GostBlockungsergebnisManager";
import type { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

export interface KursplanungSchuelerAuswahlProps {
	hatBlockung: boolean,
	hatErgebnis: boolean,
	setSchueler: (schueler: Schueler) => Promise<void>;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	getDatenmanager: () => GostBlockungsdatenManager;
	schueler: Schueler | undefined;
	schuelerFilter: () => GostKursplanungSchuelerFilter;
	faecherManager: GostFaecherManager;
	regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	isSchuelerFilterOpen: () => boolean;
	setIsSchuelerFilterOpen: (value: boolean) => Promise<void>;
	showGeschlecht: () => boolean;
	setShowGeschlecht: (value: boolean) => Promise<void>;
}