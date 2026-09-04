import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import type { LeitungsfunktionenListeManager } from "@ui/ui/manager/kataloge/LeitungsfunktionenListeManager";

export interface LeitungsfunktionenDatenProps {
	manager: () => LeitungsfunktionenListeManager;
	patch: (data: Partial<Leitungsfunktion>) => Promise<boolean>;
}