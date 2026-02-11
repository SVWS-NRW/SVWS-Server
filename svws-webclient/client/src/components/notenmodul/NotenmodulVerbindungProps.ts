import type { ENMServerConnection } from "@core";
import type { WenomAuswahlListeManager } from "@ui";

export interface NotenmodulVerbindungProps {
	manager: () => WenomAuswahlListeManager;
	connected: boolean;
	connect: (id: number) => Promise<void>;
	trustCertificate: (value: boolean) => Promise<void>;
	updateServerConnection: (data: Partial<ENMServerConnection>) => Promise<void>;
}
