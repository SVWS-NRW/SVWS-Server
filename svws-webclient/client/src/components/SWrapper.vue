<template>
	<router-view />
	<svws-ui-notifications v-if="errors.size > 0">
		<template v-if="errors.size > 1">
			<svws-ui-button @click="errors.clear()" type="transparent" class="pointer-events-auto ml-auto rounded-lg bg-white border-light fixed right-6 left-0 top-5 z-50 w-[29rem] max-w-[75vw] justify-center">Alle {{ errors.size }} Meldungen schließen</svws-ui-button>
			<div class="min-h-[1.85rem]" />
		</template>
		<template v-for="error of [...errors.values()].reverse().slice(0, 20)" :key="error.id">
			<svws-ui-notification type="error" :id="error.id" @click="id => errors.delete(id)">
				<template #header>
					{{ error.name }}
				</template>
				{{ error.message }}
				<template v-if="error.log !== null">
					<p v-for="log in error.log.log" :key="log || ''" v-html="log" />
				</template>
				<template #stack v-if="error.stack !== ''">
					<pre v-html="error.stack" />
				</template>
			</svws-ui-notification>
		</template>
	</svws-ui-notifications>
</template>

<script setup lang="ts">

	import type { SimpleOperationResponse } from '@core';
	import { DeveloperNotificationException, OpenApiError, UserNotificationException } from '@core';
	import { onErrorCaptured, ref } from 'vue';
	import { api } from '~/router/Api';

	type CapturedError = {
		id: number;
		name: string;
		message: string;
		stack: string;
		log: SimpleOperationResponse | null;
	};

	const counter = ref(0);
	const errors = ref<Map<number, CapturedError>>(new Map());

	window.addEventListener("unhandledrejection", function (event) {
		api.status.stop();
		const reason: Error = event.reason;
		void createCapturedError(reason);
		event.preventDefault();
	});

	onErrorCaptured((reason) => {
		api.status.stop();
		if (reason.name === 'resetAllErrors') {
			errors.value.clear();
		} else {
			void createCapturedError(reason)
		}
		return false;
	});

	async function createCapturedError(reason: Error) {
		console.warn(reason)
		counter.value++;
		let name = "Fehler";
		let message = reason.message;
		let log = null;
		if (reason instanceof DeveloperNotificationException)
			name = "Programmierfehler: Bitte melden Sie diesen Fehler."
		else if (reason instanceof UserNotificationException)
			name = "Nutzungsfehler: Dieser Fehler wurde durch eine nicht vorgesehene Nutzung der verwendeten Funktion hervorgerufen, z.B. durch unmögliche Kombinationen etc.";
		else if (reason instanceof OpenApiError) {
			name = "API-Fehler: Dieser Fehler wird durch eine fehlerhafte Kommunikation mit dem Server verursacht. In der Regel bedeutet das, dass die verschickten Daten nicht den Vorgaben entsprechen."
			if (reason.response instanceof Response) {
				try {
					const res = await reason.response.json();
					if ('log' in res && 'success' in res)
						log = res as SimpleOperationResponse;
					else if (res.length > 0)
						message = res;
				} catch(e) { void e }
			}
		}
		const newError: CapturedError = {
			id: counter.value,
			name,
			message,
			stack: reason.stack || '',
			log,
		}
		errors.value.set(newError.id, newError);
	}

</script>
