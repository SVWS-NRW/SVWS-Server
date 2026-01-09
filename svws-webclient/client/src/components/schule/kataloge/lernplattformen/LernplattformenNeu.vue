<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung"
					v-model="data.bezeichnung"
					:valid="bezeichnungIsValid" :min-len="1" :max-len="255" :disabled="!hatKompetenzUpdate" required />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addLernplatfform" :disabled="!bezeichnungIsValid(data.bezeichnung)">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Lernplattform } from "@core";
	import type { LernplattformenNeuProps } from "~/components/schule/kataloge/lernplattformen/LernplattformenNeuProps";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<LernplattformenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const data = ref(new Lernplattform());
	const isLoading = ref<boolean>(false);

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 255)) {
			return false;
		}

		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	async function addLernplatfform() {
		if (isLoading.value === true) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
