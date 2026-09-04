import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";

export interface PausenzeitenProps {
	patch: (data: Partial<StundenplanPausenzeit>) => Promise<void>;
	auswahl: StundenplanPausenzeit | undefined;
}
