import type { BenutzerKompetenz, Erzieherart } from "@core";
import type { ErzieherartListeManager } from "@ui";

export interface ErzieherartenDatenProps {
	patch: (data: Partial<Erzieherart>) => Promise<boolean>;
	manager: () => ErzieherartListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
