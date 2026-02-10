<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" span="2"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')" :min-len="1" :max-len="30" :disabled required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzAdd" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!hatKompetenzAdd">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addErzieherart" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { ErzieherartenNeuProps } from "~/components/schule/kataloge/erzieherarten/ErzieherartenNeuProps";
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Erzieherart } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<ErzieherartenNeuProps>();
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const data = ref<Erzieherart>(Object.assign(new Erzieherart(), { istSichtbar: true, sortierung: 32000 }));

	const isLoading = ref<boolean>(false);

	function bezeichnungIsValid(value: string | null) {
		return mandatoryInputIsValid(value, 30)
			&& isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	function sortierungIsValid(sortierung: number): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Erzieherart));
	});

	const fieldIsValid = (field: keyof Erzieherart): boolean => {
		switch (field) {
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	async function addErzieherart() {
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
