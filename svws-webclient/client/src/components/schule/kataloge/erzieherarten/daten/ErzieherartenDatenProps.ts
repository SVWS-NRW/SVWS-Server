import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import type { ErzieherartListeManager } from "@ui/ui/manager/kataloge/ErzieherartListeManager";

export interface ErzieherartenDatenProps {
	patch: (data: Partial<Erzieherart>) => Promise<boolean>;
	manager: () => ErzieherartListeManager;
}
