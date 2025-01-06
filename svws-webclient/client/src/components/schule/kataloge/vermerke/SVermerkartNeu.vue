<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input	:valid="validatorVermerkartBezeichnung" v-model="vermerkart.bezeichnung" type="text" placeholder="Bezeichnung" />
					<div v-if="!validatorVermerkartBezeichnung(vermerkart.bezeichnung) && (vermerkart.bezeichnung.length > 0)" class="flex my-auto">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Diese Bezeichnung wird bereits verwendet </p>
					</div>
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addVermerkart" :disabled="!validatorVermerkartBezeichnung(vermerkart.bezeichnung) || isLoading || (vermerkart.bezeichnung === '')">
						Speichern <svws-ui-spinner :spinning="isLoading" />
					</svws-ui-button>
				</div>
			</svws-ui-content-card>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	import type { SchuleVermerkartNeuProps } from "./SVermerkartNeuProps";
	import { VermerkartEintrag } from "@core";

	const props = defineProps<SchuleVermerkartNeuProps>();
	const vermerkart = ref(new VermerkartEintrag());
	const isLoading = ref<boolean>(false);

	function validatorVermerkartBezeichnung(value: string | null): boolean {
		if (value === null)
			return true;
		for (const eintrag of props.manager().liste.list())
			if (eintrag.bezeichnung === value.trim())
				return false;
		return true;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addVermerkart() {
		if (isLoading.value === true)
			return;
		isLoading.value = true;
		props.checkpoint.active = false;
		const v: Partial<VermerkartEintrag> = vermerkart.value;
		delete v.id;
		delete v.anzahlVermerke;
		await props.add(v);
		vermerkart.value = new VermerkartEintrag();
		isLoading.value = false;
	}
</script>
