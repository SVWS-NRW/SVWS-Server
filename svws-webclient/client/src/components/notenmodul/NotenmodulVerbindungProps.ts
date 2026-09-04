import type { ENMServerConnection } from "@core/core/data/enm/ENMServerConnection";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";

export interface NotenmodulVerbindungProps {
	manager: () => WenomAuswahlListeManager;
	connected: boolean;
	connect: (id: number) => Promise<void>;
	trustCertificate: (value: boolean) => Promise<void>;
	updateServerConnection: (data: Partial<ENMServerConnection>) => Promise<void>;
}
