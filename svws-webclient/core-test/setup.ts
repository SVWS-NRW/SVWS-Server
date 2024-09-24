import { beforeAll } from "vitest";
import { JsonCoreTypeReaderNode } from "./JsonCoreTypeReaderNode";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
const reader = new JsonCoreTypeReaderNode();

beforeAll(async () => {
	// Lade die JSON Daten für die Core Types vom Filesystem
	await reader.loadAllFromFileSystem();
	// initiiere alle Core Types
	reader.readAll();
});
