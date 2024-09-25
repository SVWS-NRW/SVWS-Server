import { beforeAll } from "vitest";
import { JsonCoreTypeReaderStatic } from "../core/src/JsonCoreTypeReaderStatic";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
const reader = new JsonCoreTypeReaderStatic();

beforeAll(async () => {
	// Lade die JSON Daten für die Core Types vom Filesystem
	await reader.loadAll();
	// initiiere alle Core Types
	reader.readAll();
});
