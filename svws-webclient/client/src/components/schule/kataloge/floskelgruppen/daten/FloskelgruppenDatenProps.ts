import type { Floskelgruppe } from "@core";
import type { FloskelgruppenListeManager } from "@ui";

export interface FloskelgruppenDatenProps {
	manager: () => FloskelgruppenListeManager;
	patch: (data: Partial<Floskelgruppe>) => Promise<boolean>;
}
