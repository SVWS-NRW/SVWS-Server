<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Ortsteil"
						:model-value="manager().daten().ortsteil"
						@change="patchOrtsteil"
						:valid="ortsteilIsValid" :min-len="1" :max-len="30" required :disabled="!hatKompetenzUpdate" />
					<ui-select label="Wohnort"
						v-model="selectedOrt"
						:manager="ortSelectManager"
						:readonly="!hatKompetenzUpdate" searchable required :removable="false" />
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
					<svws-ui-checkbox v-model="selectedIstSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { OrtsteileDatenProps } from "~/components/schule/kataloge/ortsteile/daten/OrtsteileDatenProps";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";
	import { computed } from "vue";
	import { BenutzerKompetenz, type OrtKatalogEintrag } from "@core";
	import { SelectManager } from "@ui";

	const props = defineProps<OrtsteileDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const orteById = computed<Map<number, OrtKatalogEintrag>>(() => props.manager().orteById);
	const orte = computed(() => orteById.value.values());
	const ortSelectManager = new SelectManager({
		options: orte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

	const selectedOrt = computed<OrtKatalogEintrag | null>({
		get: () => orteById.value.get(props.manager().daten().ort_id ?? -1) ?? null,
		set: (v: OrtKatalogEintrag | null) => patchOrtData(v),
	});

	// notwendig, damit plz und ortsname nach patchen in der Auswahlliste angezeigt werden
	function patchOrtData(v: OrtKatalogEintrag | null) {
		const ort = orteById.value.get(v?.id ?? -1) ?? null;
		if (ort !== null) {
			void props.patch({ ort_id: ort.id, bezeichnungOrt: ort.ortsname, plzOrt: ort.plz });
		}
	}

	const selectedIstSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void props.patch({ 'istSichtbar': v }),
	});

	async function patchOrtsteil(v: string | null) {
		if (ortsteilIsValid(v)) {
			await props.patch({ ortsteil: v?.trim() ?? null });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung ?? -1 });
		}
	}

	function ortsteilIsValid(bezeichnung: string | null): boolean {
		return mandatoryInputIsValid(bezeichnung, 30)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "ortsteil", "id", props.manager().auswahlID() ?? undefined);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung) && numberIsValid(sortierung, true, 0, 32000);
	}


</script>
