<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						:model-value="manager().auswahl().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :min-len="1" :max-len="30" required :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().auswahl().sortierung"
						@change="patchSortierung"
						:min="0" :max="32000" :readonly="!hatKompetenzUpdate" />
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

	import type { FahrschuelerartenDatenProps } from "~/components/schule/kataloge/fahrschuelerarten/daten/FahrschuelerartenDatenProps";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";
	import { isUniqueInList, mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<FahrschuelerartenDatenProps>();
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

	async function patchSortierung(value: number | null): Promise<void> {
		if (sortierungIsValid(value)) {
			await props.patch({ sortierung: value === null ? 32000 : value });
		}
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });
	}

	function bezeichnungIsValid(value: string | null) {
		return mandatoryInputIsValid(value, 30)
			&& isUniqueInList(value, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(value: number | null): boolean {
		return numberIsValid(value, true, 0, 32000);
	}

</script>
