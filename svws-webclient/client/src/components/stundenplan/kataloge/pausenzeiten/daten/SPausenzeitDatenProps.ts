import type { StundenplanPausenzeit } from "@core";

export interface PausenzeitDatenProps {
	patch: (data : Partial<StundenplanPausenzeit>) => Promise<void>;
	auswahl: StundenplanPausenzeit | undefined;
}