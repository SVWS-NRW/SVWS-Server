import path from "node:path";
import { existsSync } from "node:fs";
import { readFile } from 'node:fs/promises';
import assert from 'node:assert';

// Pfad zur primären Konfigurationsdatei
const configPath = '../../config/tests/config.json';

// Pfad zur Standard-Konfigurationsdatei (Fallback)
const defaultConfigPath = '../../config/tests/config_default.json';

export interface TestConfig {
	localTestRunBackendURL: string,
	localTestRunFrontendURL: string,
	localTestRunENMURL: string,
	localTestRunDBSchema: string
}

/**
 * Lädt eine Konfigurationsdatei.
 *
 * Diese Funktion versucht zuerst, die primäre Konfigurationsdatei zu laden.
 * Falls diese nicht existiert, wird stattdessen die Standardkonfiguration geladen.
 *
 * @returns Ein Promise, das die geladene Konfiguration als Objekt zurückgibt.
 */
export async function loadConfig(): Promise<TestConfig> {
	const primaryPath = path.resolve(configPath);
	const fallbackPath = path.resolve(defaultConfigPath);

	const _configPath = existsSync(primaryPath) ? primaryPath : fallbackPath;

	const fileContent = await readFile(_configPath, 'utf-8');
	const _config = JSON.parse(fileContent);

	assert(_config.localTestRunBackendURL !== undefined);
	assert(_config.localTestRunFrontendURL !== undefined);
	assert(_config.localTestRunENMURL !== undefined);
	assert(_config.localTestRunDBSchema !== undefined);

	return {
		localTestRunBackendURL: _config.localTestRunBackendURL,
		localTestRunFrontendURL: _config.localTestRunFrontendURL,
		localTestRunENMURL: _config.localTestRunENMURL,
		localTestRunDBSchema: _config.localTestRunDBSchema,
	} as TestConfig;

}
