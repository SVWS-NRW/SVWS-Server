import type { ENMServerConfigElement } from "@core/core/data/enm/ENMServerConfigElement";
import type { ENMServerConnection } from "@core/core/data/enm/ENMServerConnection";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";

export interface NotenmodulMailProps {
	manager: () => WenomAuswahlListeManager;
	serverConfig: () => JavaMap<string, string>;
	setServerConfigElement: (config: ENMServerConfigElement) => Promise<SimpleOperationResponse>;
	updateServerConnection: (data: Partial<ENMServerConnection>) => Promise<void>;
}
