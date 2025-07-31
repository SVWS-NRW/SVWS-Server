import type { Erzieherart } from "@core";
import type { ErzieherartListeManager } from "@ui";

export interface ErzieherartenDatenProps {
	patch: (data : Partial<Erzieherart>) => Promise<void>;
	erzieherartListeManager: () => ErzieherartListeManager,
}
