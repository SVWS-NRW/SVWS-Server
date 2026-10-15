import type { Page } from '@playwright/test';

export const KNOWN_NOISE = [/favicon/i, /ResizeObserver/i, /hydrat/i, /ChunkLoadError/i];

export function collectErrors(
	page: Page,
	additionalNoise: RegExp[] = [],
	collectConsoleErrors = true
): string[] {
	const errors: string[] = [];
	const noise = [...KNOWN_NOISE, ...additionalNoise];

	if (collectConsoleErrors) {
		page.on('console', msg => {
			if (msg.type() === 'error') {
				const text = msg.text();
				if (!noise.some(pattern => pattern.test(text))) {
					errors.push(text);
				}
			}
		});
	}

	page.on('pageerror', err => errors.push(err.message));
	return errors;
}
