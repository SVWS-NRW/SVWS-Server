import type { StundenplanZeitraster } from "@core";

export interface ZeitrasterDatenProps {
	patch: (data : Partial<StundenplanZeitraster>) => Promise<void>;
	data: StundenplanZeitraster;
}