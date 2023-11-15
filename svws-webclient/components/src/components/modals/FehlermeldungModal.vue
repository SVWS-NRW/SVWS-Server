<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" type="danger" class="hidden">
		<template #modalTitle><slot name="title">Fehler</slot></template>
		<template #modalContent>
			<slot name="content">
				{{ error.message }}
				<template v-if="error.log !== null">
					<p v-for="log in error.log.log" :key="log || ''" v-html="log" />
				</template>
				<!-- <template v-if="error.stack !== ''">
					<pre v-html="error.stack" />
				</template> -->
			</slot>
		</template>
		<template #modalActions>
			<svws-ui-button type="danger" @click="showModal().value = false"> Schließen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { SimpleOperationResponse } from '@core';
	import { DeveloperNotificationException, UserNotificationException, OpenApiError } from '@core';
	import { ref } from "vue";

	type CapturedError = {
		id: number;
		name: string;
		message: string;
		stack: string;
		log: SimpleOperationResponse | null;
	};

	const error = ref<CapturedError>({
		id: 1,
		name: 'Fehler',
		message: '',
		stack: '',
		log: null,
	});

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const openModal = (error?: string | Error) => {
		if (error === undefined)
			return;
		const reason = (typeof error === 'string') ? new Error(error) : error;
		createCapturedError(reason).then(()=> showModal().value = true).catch(e => e);
	}

	async function createCapturedError(reason: Error) {
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
			id: 1,
			name,
			message,
			stack: reason.stack || '',
			log,
		}
		error.value = newError;
	}
	defineExpose<{ openModal: (error?: string | Error) => void }>({openModal})

</script>
