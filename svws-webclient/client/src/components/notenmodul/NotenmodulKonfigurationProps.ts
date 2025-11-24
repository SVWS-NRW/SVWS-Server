import type { SimpleOperationResponse, JavaMap, ENMServerConfigElement, ENMServerConnection, ENMTeilleistungsart } from "@core";
import type { WenomAuswahlListeManager } from "@ui";
import type { MapLeistungenTabelleSpaltenanzeige, MapTeilleistungenTabelleSpaltenanzeige, Spalte } from "~/router/apps/notenmodul/RouteDataNotenmodulAdministration";

export interface NotenmodulKonfigurationProps {
	manager: () => WenomAuswahlListeManager;
	serverConfig: () => JavaMap<string, string>;
	setServerConfigElement: (config: ENMServerConfigElement) => Promise<SimpleOperationResponse>;
	updateServerConnection: (data: Partial<ENMServerConnection>) => Promise<void>;
	mapLeistungenTabelleSpaltenanzeige: () => MapLeistungenTabelleSpaltenanzeige;
	setMapLeistungenTabelleSpaltenanzeige: (key: Spalte, value: boolean) => Promise<void>;
	mapTeilleistungenTabelleSpaltenanzeige: () => MapTeilleistungenTabelleSpaltenanzeige;
	setMapTeilleistungenTabelleSpaltenanzeige: (key: number, value: boolean) => Promise<void>;
	mapTeilleistungsarten: JavaMap<number, ENMTeilleistungsart>;
}
