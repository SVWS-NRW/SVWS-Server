import type { Leitungsfunktion } from "@core";
import type { LeitungsfunktionenListeManager } from "@ui";

export interface LeitungsfunktionenDatenProps {
	manager: () => LeitungsfunktionenListeManager;
	patch: (data: Partial<Leitungsfunktion>) => Promise<boolean>;
}