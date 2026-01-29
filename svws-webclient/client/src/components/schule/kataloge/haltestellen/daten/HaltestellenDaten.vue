<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="manager().auswahl().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :min-len="1" :max-len="30" required :readonly="!hatKompetenzUpdate" />
					<svws-ui-input-number placeholder="Entfernung zur Schule"
						:model-value="manager().auswahl().entfernungSchule"
						@change="patchEntfernungSchule"
						:valid="entfernungSchuleIsValid" :min="0" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().auswahl().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
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

	import type { HaltestellenDatenProps } from "~/components/schule/kataloge/haltestellen/daten/HaltestellenDatenProps";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<HaltestellenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const istSichtbar = computed<boolean>({
		get: () => props.manager().auswahl().istSichtbar,
		set: (v: boolean) => void patchSichtbar(v),
	});

	async function patchBezeichnung(bezeichnung: string | null) {
		if (bezeichnungIsValid(bezeichnung)) {
			await props.patch({ bezeichnung: bezeichnung ?? '' });
		}
	}

	async function patchEntfernungSchule(value: number | null): Promise<void> {
		if (entfernungSchuleIsValid(value)) {
			await props.patch({ entfernungSchule: value });
		}
	}

	async function patchSortierung(value: number | null): Promise<void> {
		if (sortierungIsValid(value)) {
			await props.patch({ sortierung: value === null ? 32000 : value });
		}
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });
	}

	// ---validate---
	function bezeichnungIsValid(bezeichnung: string | null) {
		return mandatoryInputIsValid(bezeichnung, 30)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	function entfernungSchuleIsValid(entfernung: number | null): boolean {
		return !numberHasDecimals(entfernung)
			&& numberIsValid(entfernung, true, 0);
	}

</script>
