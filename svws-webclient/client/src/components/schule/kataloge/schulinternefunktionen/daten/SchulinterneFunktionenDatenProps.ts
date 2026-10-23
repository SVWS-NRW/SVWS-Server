import type { SchulinterneFunktion } from "@core/core/data/schule/SchulinterneFunktion";
import type { SchulinterneFunktionenListeManager } from "@ui/ui/manager/kataloge/SchulinterneFunktionenListeManager";

export interface SchulinterneFunktionenDatenProps {
	manager: () => SchulinterneFunktionenListeManager;
	patch: (data: Partial<SchulinterneFunktion>) => Promise<boolean>;
}