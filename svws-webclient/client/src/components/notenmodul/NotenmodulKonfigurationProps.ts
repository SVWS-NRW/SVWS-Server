import type { SimpleOperationResponse, JavaMap, ENMServerConfigElement, ENMServerConnection } from "@core";
import type { WenomAuswahlListeManager } from "@ui";
import type { MapLeistungenTabelleSpaltenanzeige, MapTeilleistungenTabelleSpaltenanzeige } from "~/router/apps/notenmodul/RouteDataNotenmodulAdministration";

export interface NotenmodulKonfigurationProps {
	manager: () => WenomAuswahlListeManager;
	serverConfig: () => JavaMap<string, string>;
	setServerConfigElement: (config: ENMServerConfigElement) => Promise<SimpleOperationResponse>;
	updateServerConnection: (data: Partial<ENMServerConnection>) => Promise<void>;
	mapLeistungenTabelleSpaltenanzeige: () => MapLeistungenTabelleSpaltenanzeige;
	setMapLeistungenTabelleSpaltenanzeige: (value: MapLeistungenTabelleSpaltenanzeige) => Promise<void>;
	mapTeilleistungenTabelleSpaltenanzeige: () => MapTeilleistungenTabelleSpaltenanzeige;
	setMapTeilleistungenTabelleSpaltenanzeige: (value: MapTeilleistungenTabelleSpaltenanzeige) => Promise<void>;
}
