import { beforeAll } from "vitest";

import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";

const reader = new JsonCoreTypeReaderStatic();

beforeAll(async () => {
	reader.readAll();
});
