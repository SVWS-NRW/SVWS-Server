import type { Schulform,  KlassenListeManager, List, Schulgliederung } from "@core";
import {ArrayList} from "@core";

export interface KlassenGruppenprozesseProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	klassenListeManager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, ArrayList<string | null>]>
}
