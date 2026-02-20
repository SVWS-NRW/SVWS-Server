<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-text-input placeholder="Bezeichnung"
						class="contentFocusField"
						span="2"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')"
						:min-len="1" :max-len="50" :disabled required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')"
						:min="0" :max="32000" :disabled :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>

				<svws-ui-button :disabled="!formIsValid || !hatKompetenzUpdate" @click="addSchwerpunkt">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>

		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { SchuelerSchwerpunkt as Schwerpunkt } from "@core";
	import { BenutzerKompetenz } from "@core";
	import type { SchwerpunkteNeuProps } from './SchwerpunkteNeuProps';
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";

	interface SchwerpunktNeu extends Pick<Schwerpunkt, "bezeichnung" | "sortierung" | "istSichtbar"> {}

	const props = defineProps<SchwerpunkteNeuProps>();

	const data = ref<SchwerpunktNeu>({ bezeichnung: null, istSichtbar: true, sortierung: 32000 });

	const isLoading = ref<boolean>(false);

	const hatKompetenzUpdate = computed<boolean>(() => {
		return props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	});

	const disabled = computed(() => !hatKompetenzUpdate.value);

	// validate
	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Schwerpunkt));
	});

	function bezeichnungIsValid(value: string | null) {
		return mandatoryInputIsValid(value, 50)
			&& isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	function fieldIsValid(field: keyof Schwerpunkt | null): boolean {
		switch (field) {
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	}

	// util
	async function addSchwerpunkt() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;

		await props.add(data.value);

		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	// watchers
	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
