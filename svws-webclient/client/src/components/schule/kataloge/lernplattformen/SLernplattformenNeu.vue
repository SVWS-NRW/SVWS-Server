<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<div>
						<svws-ui-text-input	:valid="validatorLernplattformBezeichnung" v-model="lernplattform.bezeichnung" type="text" placeholder="Bezeichnung" :readonly />
						<div v-if="!validatorLernplattformBezeichnung(lernplattform.bezeichnung) && (lernplattform.bezeichnung.length > 0)" class="flex my-auto">
							<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
							<p> Diese Bezeichnung wird bereits verwendet </p>
						</div>
					</div>
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button><svws-ui-button @click="addEinwilligungsart"
						:disabled="((!validatorLernplattformBezeichnung(lernplattform.bezeichnung)) || isLoading || (lernplattform.bezeichnung === '') || (!hatKompetenzUpdate))">
						Speichern <svws-ui-spinner :spinning="isLoading" />
					</svws-ui-button>
				</div>
			</svws-ui-content-card>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import { BenutzerKompetenz, Lernplattform } from "@core";
	import type { SLernplattformenNeuProps } from "~/components/schule/kataloge/lernplattformen/SLernplattformenNeuProps";

	const props = defineProps<SLernplattformenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const lernplattform = ref(new Lernplattform());
	const isLoading = ref<boolean>(false);

	function validatorLernplattformBezeichnung(value: string | null): boolean {
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

	async function addEinwilligungsart() {
		if (isLoading.value === true)
			return;
		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add({
			bezeichnung: lernplattform.value.bezeichnung.trim(),
		});
		lernplattform.value = new Lernplattform();
		isLoading.value = false;
	}
</script>
