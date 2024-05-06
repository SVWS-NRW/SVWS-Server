import type { VermerkartEintrag } from "@core";

export interface VermerkartDatenProps {
	patch: (data : Partial<VermerkartEintrag>) => Promise<void>;
	auswahl: VermerkartEintrag;
}
