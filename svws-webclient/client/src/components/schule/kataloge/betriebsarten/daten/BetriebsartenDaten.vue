<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="manager().auswahl().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :readonly required :min-len="1" :max-len="30" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().auswahl().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :readonly="!hatKompetenzUpdate" :removable="false" :min="1" :max="32000" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { BetriebsartenDatenProps } from './BetriebsartenDatenProps';
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<BetriebsartenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const istSichtbar = computed<boolean>({
		get: () => props.manager().auswahl().istSichtbar,
		set: (v: boolean) => void patchIstSichtbar(v),
	});

	// patch
	async function patchBezeichnung(value: string | null) {
		if (bezeichnungIsValid(value)) {
			await props.patch({ bezeichnung: value?.trim() ?? undefined });
		}
	}

	async function patchSortierung(value: number | null) {
		if (sortierungIsValid(value)) {
			await props.patch({ sortierung: value === null ? 3200 : value });
		}
	}
	async function patchIstSichtbar(value: boolean) {
		await props.patch({ istSichtbar: value });
	}

	// validate
	function bezeichnungIsValid(value: string | null) {
		return mandatoryInputIsValid(value, 30)
			&& isUniqueInList(value, props.manager().liste.list(), 'bezeichnung', 'id', props.manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

</script>
