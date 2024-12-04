<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<svws-ui-menu-item :active="false" @click="goBack">
					<template #label>
						Zurück
					</template>
					<template #icon>
						<span class="inline-block icon-lg i-ri-arrow-go-back-line" />
					</template>
				</svws-ui-menu-item>
				<svws-ui-menu-item :active="false" @click="reloadClient">
					<template #label>
						Neu laden
					</template>
					<template #icon>
						<span class="inline-block icon-lg i-ri-restart-line" />
					</template>
				</svws-ui-menu-item>
			</svws-ui-menu>
		</template>
		<template #main>
			<div class="app--page">
				<svws-ui-header>
					<svws-ui-input-wrapper>
						<div class="flex items-center gap-2">
							<span class="icon-xl i-ri-alert-fill icon-error" />
							<span>{{ errorDescription }}</span>
							<br>
							<span v-if="code !== undefined" class="opacity-40">
								Fehler {{ code }}
							</span>
						</div>
						<svws-ui-button type="primary" @click="copyToClipboard">
							<span class="icon i-ri-file-copy-line" v-if="copied === null" />
							<span class="icon i-ri-error-warning-fill" v-else-if="copied === false" />
							<span class="icon i-ri-check-line icon-primary" v-else /> Fehlermeldung kopieren
						</svws-ui-button>
					</svws-ui-input-wrapper>
				</svws-ui-header>
				<div class="svws-ui-page" v-if="error !== undefined">
					<div class="svws-ui-tab-content">
						<div class="page--content">
							<svws-ui-content-card :title="error.message">
								<pre>{{ error.stack }}</pre>
							</svws-ui-content-card>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>


<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { ErrorProps } from "./SErrorProps";
	import type { SimpleOperationResponse} from "@core";
	import { DeveloperNotificationException, OpenApiError, UserNotificationException } from "@core";

	type CapturedError = {
		id: number;
		name: string;
		message: string;
		stack: string | string[];
		log: SimpleOperationResponse | null;
	};

	const props = defineProps<ErrorProps>();
	const copied = ref<boolean|null>(null);

	const errorDescription = computed(() => {
		if (props.error instanceof DeveloperNotificationException)
			return "Programmierfehler: Bitte melden Sie diesen Fehler."
		else if (props.error instanceof UserNotificationException)
			return "Nutzungsfehler: Dieser Fehler wurde durch eine nicht vorgesehene Nutzung der verwendeten Funktion hervorgerufen, z.B. durch unmögliche Kombinationen etc.";
		else if (props.error instanceof OpenApiError)
			return "API-Fehler: Dieser Fehler wird durch eine fehlerhafte Kommunikation mit dem Server verursacht. In der Regel bedeutet das, dass die verschickten Daten nicht den Vorgaben entsprechen.";
		return "Unbekannter Fehler";
	})

	function goBack() {
		window.history.back();
	}

	async function createCapturedError(): Promise<CapturedError> {
		const reason = props.error;
		if (reason === undefined)
			return { id: 0, name: "Unbekannter Fehler", message: "Ein Fehler verhindert den weiteren Ablauf des SVWS-Client, der Fehler ist jedoch unbekannt", stack: "", log: null };
		console.warn(reason);
		const name = errorDescription.value;
		let message = reason.message;
		let log = null;
		if (reason instanceof OpenApiError) {
			if (reason.response instanceof Response) {
				try {
					let res;
					if (reason.response.headers.get('content-type') === 'application/json') {
						res = await reason.response.json();
						if ('log' in res && 'success' in res)
							log = res satisfies SimpleOperationResponse;
					}
					else
						res = await reason.response.text();
					if (res.length > 0)
						message = res;
					else
						message += ' - Status: '+reason.response.status;
				} catch(e) { void e }
			}
		}
		return { id: 0, name, message, stack: reason.stack?.split("\n") || '', log }
	}

	async function copyToClipboard() {
		const capturedError = await createCapturedError();
		const json = JSON.stringify({ env: { mode: props.api.mode.text, version: props.api.version, commit: props.api.githash, kompetenzen: props.benutzerKompetenzen.values().toArray().toString(), userAgent: window.navigator.userAgent }, capturedError }, null, 2);
		try {
			await navigator.clipboard.writeText("```json\n"+json+"\n```");
		} catch(e) {
			copied.value = false;
		}
		copied.value = true;
	}

	function reloadClient() {
		window.location.href = window.location.origin;
	}

</script>
