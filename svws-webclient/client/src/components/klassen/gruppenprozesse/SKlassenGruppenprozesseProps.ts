import type { Schulform,  KlassenListeManager, List, Schulgliederung } from "@core";

export interface KlassenGruppenprozesseProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	klassenListeManager: () => KlassenListeManager;
}