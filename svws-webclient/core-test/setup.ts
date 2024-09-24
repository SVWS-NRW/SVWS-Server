import { beforeAll } from "vitest";
import { JsonCoreTypeReader } from "../../svws-webclient/client/src/router/JsonCoreTypeReader";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
const targetHost: string | undefined | null = process.env.targetHost;
const reader = new JsonCoreTypeReader(`https://${targetHost ?? "localhost"}`);

beforeAll(async () => {
	// Lade die JSON Daten für die Core Types vom Filesystem
	await reader.loadAllFromFileSystem();

	// initiiere alle Core Types

	reader.readAll();
});
