<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<div>
						<svws-ui-text-input	:valid="validatorErzieherartBezeichnung" v-model="erzieherart.bezeichnung" type="text" placeholder="Bezeichnung" />
						<div v-if="!validatorErzieherartBezeichnung(erzieherart.bezeichnung) && (erzieherart.bezeichnung.length > 0)" class="flex my-auto">
							<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
							<p> Diese Bezeichnung wird bereits verwendet </p>
						</div>
					</div>
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button><svws-ui-button @click="addErzieherart"
						:disabled="((!validatorErzieherartBezeichnung(erzieherart.bezeichnung)) || isLoading || (erzieherart.bezeichnung === ''))">
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
	import { Erzieherart } from "@core";
	import type { SErzieherartenNeuProps } from "~/components/schule/kataloge/erzieherarten/SErzieherartenNeuProps";

	const props = defineProps<SErzieherartenNeuProps>();
	const erzieherart = ref(new Erzieherart());
	const isLoading = ref<boolean>(false);

	function validatorErzieherartBezeichnung(value: string | null): boolean {
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

	async function addErzieherart() {
		if (isLoading.value === true)
			return;
		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add({
			bezeichnung: erzieherart.value.bezeichnung.trim(),
		});
		erzieherart.value = new Erzieherart();
		isLoading.value = false;
	}
</script>
