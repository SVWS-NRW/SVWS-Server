import { beforeAll } from "vitest";
import { JsonCoreTypeReaderStatic } from "../core/src/JsonCoreTypeReaderStatic";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
const reader = new JsonCoreTypeReaderStatic();

beforeAll(async () => {
	// initiiere alle Core Types
	reader.readAll();
});
