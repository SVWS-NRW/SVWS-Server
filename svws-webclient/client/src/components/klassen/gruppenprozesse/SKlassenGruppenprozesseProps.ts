import type { Schulform, KlassenListeManager, List, Schulgliederung } from "@core";

export interface KlassenGruppenprozesseProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	klassenListeManager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, List<string | null>]>;
	deleteKlassenCheck: () => [boolean, List<string>];
}
