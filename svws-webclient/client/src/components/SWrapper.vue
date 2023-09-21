<template>
	<router-view />
	<svws-ui-notifications v-if="errors.length">
		<template v-if="errors.length > 1">
			<svws-ui-button @click="errors = []" type="transparent" class="pointer-events-auto ml-auto rounded-lg bg-white border-light fixed right-6 left-0 top-5 z-50 w-[29rem] max-w-[75vw] justify-center">Alle {{ errors.length }} Meldungen schließen</svws-ui-button>
			<div class="min-h-[1.85rem]" />
		</template>
		<template v-for="error of errors.reverse().slice(0, 20)" :key="error.message">
			<svws-ui-notification type="error">
				<template #header>
					{{ error.name }}
					<template v-if="error instanceof DeveloperNotificationException">
						<br>Programmierfehler: Bitte melden Sie diesen Fehler.
					</template>
					<template v-if="error instanceof UserNotificationException">
						<br>Nutzungsfehler: Dieser Fehler wurde durch eine nicht vorgesehene Nutzung der verwendeten Funktion hervorgerufen, z.B. durch unmögliche Kombinationen etc.
					</template>
					<template v-if="(error instanceof OpenApiError)">
						<br>API-Fehler: Dieser Fehler wird durch eine fehlerhafte Kommunikation mit dem Server verursacht. In der Regel bedeutet das, dass die verschickten Daten nicht den Vorgaben entsprechen.
					</template>
				</template>
				{{ error.message }}
				<template #stack v-if="error.stack">
					<pre v-html="error.stack" />
				</template>
			</svws-ui-notification>
		</template>
	</svws-ui-notifications>
</template>

<script setup lang="ts">
	import { DeveloperNotificationException, OpenApiError, UserNotificationException } from '@core';
	import { onErrorCaptured, ref } from 'vue';
	import { api } from '~/router/Api';

	const errors = ref<Error[]>([]);
	window.addEventListener("unhandledrejection", function (event) {
		api.status.stop();
		errors.value.push(new Error(event.reason))
		event.preventDefault();
	});

	onErrorCaptured((e) => {
		if (e.name === 'resetAllErrors')
			errors.value = [];
		else {
			console.warn(e)
			errors.value.push(e);
		}
		api.status.stop();
		return false;
	});

</script>
