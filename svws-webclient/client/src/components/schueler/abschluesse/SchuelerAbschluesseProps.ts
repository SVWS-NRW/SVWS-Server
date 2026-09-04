import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { List } from "@core/java/util/List";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerAbschluesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
}
