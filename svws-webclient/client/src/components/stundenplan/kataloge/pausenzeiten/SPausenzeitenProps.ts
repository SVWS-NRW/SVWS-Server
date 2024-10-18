import type { StundenplanPausenzeit } from "@core";

export interface PausenzeitenProps {
	patch: (data : Partial<StundenplanPausenzeit>) => Promise<void>;
	auswahl: StundenplanPausenzeit | undefined;
}
