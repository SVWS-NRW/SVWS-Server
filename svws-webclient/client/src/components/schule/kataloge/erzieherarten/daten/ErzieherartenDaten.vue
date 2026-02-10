<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="manager().daten().bezeichnung"
						readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="selectedIsSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { ErzieherartenDatenProps } from "~/components/schule/kataloge/erzieherarten/daten/ErzieherartenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";
	import { numberHasDecimals, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<ErzieherartenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	// patching these entries is not aloud according to SchILDzentral
	const idsOfNonPatchableEntries = new Set([1, 2, 3, 4, 5]);
	const readonly = computed(() => (!hatKompetenzUpdate.value) || (idsOfNonPatchableEntries.has(props.manager().auswahl().id)));
	const selectedIsSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void props.patch({ 'istSichtbar': v }),
	});

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung ?? -1 });
		}
	}


	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung) && numberIsValid(sortierung, true, 0, 32000);
	}


</script>
