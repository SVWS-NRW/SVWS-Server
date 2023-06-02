import type { StundenplanZeitraster } from "@svws-nrw/svws-core";

export interface ZeitrasterDatenProps {
	patch: (data : Partial<StundenplanZeitraster>) => Promise<void>;
	data: StundenplanZeitraster;
}