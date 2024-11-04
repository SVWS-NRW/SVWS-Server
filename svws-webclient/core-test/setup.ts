import { beforeAll } from "vitest";
import { JsonCoreTypeReaderStatic } from "../core/src/asd/utils/JsonCoreTypeReaderStatic";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
const reader = new JsonCoreTypeReaderStatic();

beforeAll(async () => {
	reader.readAll();
});
