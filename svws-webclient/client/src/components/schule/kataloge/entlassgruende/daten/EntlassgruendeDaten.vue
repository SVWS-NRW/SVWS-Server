<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="manager().daten().bezeichnung"
						required readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { EntlassgruendeDatenProps } from "~/components/schule/kataloge/entlassgruende/daten/EntlassgruendeDatenProps";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";
	import { numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<EntlassgruendeDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const istSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void patchSichtbar(v),
	});

	async function patchSortierung(value: number | null): Promise<void> {
		if (sortierungIsValid(value)) {
			await props.patch({ sortierung: value === null ? 32000 : value });
		}
	}

	function sortierungIsValid(value: number | null): boolean {
		return numberIsValid(value, true, 0, 32000);
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });
	}

</script>
