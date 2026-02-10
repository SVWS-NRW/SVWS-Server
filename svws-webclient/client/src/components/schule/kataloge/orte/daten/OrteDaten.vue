<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="PLZ" class="contentFocusField"
						:model-value="manager().daten().plz"
						@change="patchPlz"
						:valid="plzIsValid" :min-len="1" :max-len="10" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Ortsname"
						:model-value="manager().daten().ortsname"
						@change="patchOrtsname"
						:valid="ortsnameIsValid" :min-len="1" :max-len="50" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Kreis"
						:model-value="manager().daten().kreis"
						@change="patchKreis"
						:valid="v => optionalInputIsValid(v, 3)" :max-len="3" :disabled="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Land"
						:model-value="manager().daten().kuerzelBundesland"
						@change="patchBundesland"
						:valid="v => optionalInputIsValid(v, 2)" :max-len="2" :disabled="!hatKompetenzUpdate" />
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

	import type { OrteDatenProps } from "~/components/schule/kataloge/orte/daten/OrteDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";
	import { mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<OrteDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const selectedIsSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void props.patch({ 'istSichtbar': v }),
	});

	async function patchPlz(v: string | null) {
		if (plzIsValid(v) && ortsnameIsValid(props.manager().daten().ortsname)) {
			await props.patch({ plz: v.trim() });
		}
	}

	async function patchOrtsname(v: string | null) {
		if (ortsnameIsValid(v)) {
			await props.patch({ ortsname: v.trim() });
		}
	}

	async function patchKreis(v: string | null) {
		if (optionalInputIsValid(v, 3)) {
			await props.patch({ kreis: v?.trim() ?? null });
		}
	}

	async function patchBundesland(v: string | null) {
		if (optionalInputIsValid(v, 2)) {
			await props.patch({ kuerzelBundesland: v?.trim() ?? null });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung });
		}
	}

	// validierung
	function plzIsValid(v: string | null): v is string {
		return mandatoryInputIsValid(v, 10);
	}

	function ortsnameIsValid(v: string | null): v is string {
		if (!mandatoryInputIsValid(v, 50)) {
			return false;
		}
		for (const ort of props.manager().liste.list()) {
			if ((ort.id !== props.manager().daten().id)
				&& (ort.plz === props.manager().daten().plz)
				&& (ort.ortsname !== null)
				&& (ort.ortsname.toLowerCase() === v.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	function sortierungIsValid(sortierung: number | null): sortierung is number {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

</script>

