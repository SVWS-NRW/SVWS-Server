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
			<div class="app--page h-full w-full overflow-hidden flex flex-col">
				<svws-ui-header>
					<svws-ui-input-wrapper>
						<div class="flex items-center gap-2">
							<span class="icon-xl i-ri-alert-fill icon-ui-danger" />
							<span>{{ errorDescription }}</span>
							<br>
							<span v-if="code !== undefined" class="opacity-40">
								Fehler {{ code }}
							</span>
						</div>
						<svws-ui-button type="primary" @click="copyToClipboard">
							<span class="icon i-ri-file-copy-line" v-if="copied === null" />
							<span class="icon i-ri-error-warning-fill" v-else-if="copied === false" />
							<span class="icon i-ri-check-line icon-ui-brand" v-else /> Fehlermeldung kopieren
						</svws-ui-button>
					</svws-ui-input-wrapper>
				</svws-ui-header>
				<div class="svws-ui-page h-full w-full overflow-hidden" v-if="error !== undefined">
					<div class="svws-ui-tab-content">
						<div class="page page-flex-col overflow-hidden">
							<div v-if="errorSimpleOperationResponse !== null" class="w-full h-1/2 overflow-hidden flex flex-col">
								<div class="text-headline-md mb-4">Fehlermeldung vom SVWS-Server:</div>
								<div class="w-full overflow-y-auto">
									<div v-for="line of errorSimpleOperationResponse.log" :key="line" class="first:font-bold">{{ line.trim() }}</div>
								</div>
							</div>
							<div v-else-if="(errorSimpleOperationResponse === null) && (errorText !== null)" class="w-full h-1/2 overflow-hidden flex flex-col">
								<div class="text-headline-md mb-4">Fehlermeldung vom SVWS-Server:</div>
								<pre class="w-full overflow-y-auto">{{ errorText }}</pre>
							</div>
							<div class="w-full h-1/2 overflow-hidden flex flex-col">
								<div class="text-headline-md mb-4">Fehlermeldung: {{ error.message }}</div>
								<pre class="w-full overflow-y-auto">{{ error.stack }}</pre>
							</div>
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
	import { SimpleOperationResponse} from "@core";
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

	const errorText = computed<string | null>(() => {
		if (props.errortext === undefined)
			return null;
		return props.errortext;
	});

	const errorSimpleOperationResponse = computed<SimpleOperationResponse | null>(() => {
		if (errorText.value === null)
			return null;
		try {
			return SimpleOperationResponse.transpilerFromJSON(errorText.value);
		} catch (e) {
			return null;
		}
	});

	function goBack() {
		window.history.back();
	}

	async function createCapturedError(): Promise<CapturedError> {
		const reason = props.error;
		if (reason === undefined)
			return { id: 0, name: "Unbekannter Fehler", message: "Ein Fehler verhindert den weiteren Ablauf des SVWS-Client, der Fehler ist jedoch unbekannt", stack: "", log: null };
		console.warn(reason);
		const name = errorDescription.value;
		const message = reason.message;
		const log = errorSimpleOperationResponse.value;
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
