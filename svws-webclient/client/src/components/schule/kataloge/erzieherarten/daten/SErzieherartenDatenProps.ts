import type { Erzieherart, ErzieherartListeManager } from "@core";

export interface ErzieherartenDatenProps {
	patch: (data : Partial<Erzieherart>) => Promise<void>;
	erzieherartListeManager: () => ErzieherartListeManager,
}
