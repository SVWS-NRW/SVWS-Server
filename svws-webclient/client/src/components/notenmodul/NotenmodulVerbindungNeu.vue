<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper>
			<svws-ui-text-input class="contentFocusField" v-model.trim="data.url" type="text" placeholder="URL" url />
			<svws-ui-text-input v-model.trim="data.bezeichnung" type="text" placeholder="Bezeichnung" />
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
	import { ENMServerConnection } from "@core";
	import type { NotenmodulVerbindungNeuProps } from "./NotenmodulVerbindungNeuProps";

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

	const validateAll = () => isValid.value = (data.value.url !== "");

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function updateCredentials() {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = false;
		isLoading.value = true;
		const { url, bezeichnung } = data.value;
		await props.addCredentials({ url: `https://${url}`, bezeichnung: bezeichnung === "" ? null : bezeichnung });
		isLoading.value = false;
	}

</script>
