import type { StundenplanPausenzeit } from "@svws-nrw/svws-core";

export interface PausenzeitDatenProps {
	patch: (data : Partial<StundenplanPausenzeit>) => Promise<void>;
	data: StundenplanPausenzeit;
}