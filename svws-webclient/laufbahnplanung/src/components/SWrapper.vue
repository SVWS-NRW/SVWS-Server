<template>
	<router-view />
	<ui-color-mode headless />
	<svws-ui-notification type="error" v-if="!browser()">
		<template #header>
			Browser veraltet
		</template>
		Bitte aktualisieren Sie Ihren Browser, diese Version kann keine Laufbahndaten laden.
	</svws-ui-notification>
	<svws-ui-notifications v-if="errors.size > 0">
		<div v-if="errors.size > 1" class="bg-ui-100">
			<svws-ui-button @click="errors.clear()" type="transparent" class="pointer-events-auto ml-auto rounded-lg bg-contr0 border-ui-25 fixed right-6 left-0 top-5 z-50 w-[29rem] max-w-[75vw] justify-center">Alle {{ errors.size }} Meldungen schließen</svws-ui-button>
			<div class="min-h-[1.85rem]" />
		</div>
		<template v-for="error of [...errors.values()].reverse().slice(0, 20)" :key="error.id">
			<svws-ui-notification type="error" :id="error.id" @click="id => errors.delete(id)" :to-copy="copyString(error)">
				<template #header>
					{{ error.name }}
				</template>
				{{ error.message }}
				<template v-if="error.log !== null">
					<p v-for="log in error.log.log" :key="log || ''" v-text="log" />
				</template>
				<template #stack v-if="error.stack !== ''">
					<pre v-html="error.stack" />
				</template>
			</svws-ui-notification>
		</template>
	</svws-ui-notifications>
</template>

<script setup lang="ts">

	import { onErrorCaptured, ref } from 'vue';
	import { githash } from '../../githash';
	import { version } from '../../version';
	import type { SimpleOperationResponse } from '@core/core/data/SimpleOperationResponse';
	import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
	import { UserNotificationException } from '@core/core/exceptions/UserNotificationException';
	import { OpenApiError } from '@core/api/OpenApiError';

	type CapturedError = {
		id: number;
		name: string;
		message: string;
		stack: string | string[];
		log: SimpleOperationResponse | null;
	};

	const counter = ref(0);
	const errors = ref<Map<number, CapturedError>>(new Map());

	function copyString(error: CapturedError) {
		const json = JSON.stringify({ env: { version: version, "Commit": githash }, error }, null, 2);
		return "```json\n"+json+"\n```";
	}

	function errorHandler(event: ErrorEvent | PromiseRejectionEvent) {
		event.preventDefault();
		if (event instanceof ErrorEvent)
			void createCapturedError(event.error);
		if (event instanceof PromiseRejectionEvent)
			void createCapturedError(event.reason);
	}

	// Dieser Listener gilt nur für Promises
	window.addEventListener("unhandledrejection", errorHandler);

	// Dieser Listener fängt alle anderen Fehler ab
	window.addEventListener("error", errorHandler);

	onErrorCaptured((reason) => {
		if (reason.name === 'resetAllErrors')
			errors.value.clear();
		else
			void createCapturedError(reason);
		return false;
	});

	async function createCapturedError(reason: Error) {
		console.warn(reason);
		counter.value++;
		let name = `Fehler ${reason.name !== 'Error' ? ': ' + reason.name : ''}`;
		let message = reason.message;
		let log = null;
		if (reason instanceof DeveloperNotificationException)
			name = "Programmierfehler: Bitte melden Sie diesen Fehler."
		else if (reason instanceof UserNotificationException)
			name = "Nutzungsfehler: Dieser Fehler wurde durch eine nicht vorgesehene Nutzung der verwendeten Funktion hervorgerufen, z.B. durch unmögliche Kombinationen etc.";
		else if (reason instanceof OpenApiError) {
			name = "API-Fehler: Dieser Fehler wird durch eine fehlerhafte Kommunikation mit dem Server verursacht. In der Regel bedeutet das, dass die verschickten Daten nicht den Vorgaben entsprechen."
			if (reason.response instanceof Response) {
				const text = await reason.response.text();
				try {
					const res = JSON.parse(text)
					if (('log' in res) && ('success' in res))
						log = res satisfies SimpleOperationResponse;
				} catch {
					if (text.length > 0)
						message = text;
					else
						message += ` - Status: ${reason.response.status}`;
				}
			}
		}
		const newError: CapturedError = {
			id: counter.value,
			name,
			message,
			stack: reason.stack?.split("\n") || '',
			log,
		}
		errors.value.set(newError.id, newError);
	}

	const browser = () => {
		try {
			const dc = new DecompressionStream("gzip");
			return true;
		} catch (e) {
			return false;
		}
	}

</script>

