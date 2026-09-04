import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import type { FloskelgruppenListeManager } from "@ui/ui/manager/kataloge/FloskelgruppenListeManager";

export interface FloskelgruppenDatenProps {
	manager: () => FloskelgruppenListeManager;
	patch: (data: Partial<Floskelgruppe>) => Promise<boolean>;
}
