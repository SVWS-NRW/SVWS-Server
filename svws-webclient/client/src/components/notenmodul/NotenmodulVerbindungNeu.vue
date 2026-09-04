<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper>
			<svws-ui-text-input class="contentFocusField" v-model.trim="data.url" type="text" placeholder="URL" />
			<svws-ui-text-input v-model.trim="data.bezeichnung" type="text" placeholder="Bezeichnung" />
			<div v-if="Boolean(data.url) !== Boolean(data.bezeichnung)" class="p-2 rounded-md bg-ui-caution text-ui-oncaution mt-2">Die eingegebenen Daten entsprechen noch nicht den Vorgaben. Geben Sie eine gültige URL und eine Bezeichnung für die Verbindung an.</div>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button type="primary" @click="updateCredentials" :disabled="!isValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import { ref, watch } from "vue";
	import type { NotenmodulVerbindungNeuProps } from "./NotenmodulVerbindungNeuProps";
	import { ENMServerConnection } from "@core/core/data/enm/ENMServerConnection";

	const props = defineProps<NotenmodulVerbindungNeuProps>();

	const data = ref<ENMServerConnection>(new ENMServerConnection());
	const isLoading = ref<boolean>(false);
	const isValid = ref<boolean>(false);

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
		validateAll();
	}, { immediate: false, deep: true });

	const validateAll = () => isValid.value = (data.value.url !== "") && (data.value.bezeichnung !== "");

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function updateCredentials() {
		if (isLoading.value) {
			return;
		}
		try {
			props.checkpoint.active = false;
			isLoading.value = true;
			const { url, bezeichnung } = data.value;
			let address: URL;
			if (url.startsWith("https://")) {
				address = new URL(url);
			} else if (url.startsWith("http://")) {
				isValid.value = false;
				return;
			} else {
				address = new URL(`https://${url}`);
			}
			await props.addCredentials({ url: address.href, bezeichnung: bezeichnung === "" ? null : bezeichnung, clientID: "1", clientSecret: "" });
		} catch {
			isValid.value = false;
		} finally {
			isLoading.value = false;
		}
	}

</script>
