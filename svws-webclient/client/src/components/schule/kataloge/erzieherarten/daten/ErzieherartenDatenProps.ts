import type { BenutzerKompetenz, Erzieherart } from "@core";
import type { ErzieherartListeManager } from "@ui";

export interface ErzieherartenDatenProps {
	patch: (data: Partial<Erzieherart>) => Promise<void>;
	manager: () => ErzieherartListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
