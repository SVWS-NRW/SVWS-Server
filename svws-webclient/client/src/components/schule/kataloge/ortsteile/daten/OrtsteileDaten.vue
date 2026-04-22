<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Ortsteil"
						v-model="model.proxy.ortsteil"
						:validation="() => model.getFehler('ortsteil')"
						@commit="model.patch"
						:max-len="30" required :readonly />
					<ui-select label="Ort"
						v-model="model.ort.value"
						:validation="() => model.getFehler('ort_id')"
						:manager="ortSelectManager"
						searchable required :removable="false" :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@commit="model.patch"
						:min="0" :max="32000"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { OrtsteileDatenProps } from "~/components/schule/kataloge/ortsteile/daten/OrtsteileDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz, type OrtKatalogEintrag } from "@core";
	import { SelectManager } from "@ui";
	import { OrtsteilModelProxy } from "~/components/schule/kataloge/ortsteile/modelproxy/OrtsteilModelProxy";

	const props = defineProps<OrtsteileDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const orteById = computed<Map<number, OrtKatalogEintrag>>(() => props.manager().orteById);
	const orte = computed(() => orteById.value.values());
	const model = new OrtsteilModelProxy(() => props.manager().auswahl(), () => props.manager(), orteById.value, props.patch);

	const ortSelectManager = new SelectManager({
		options: orte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

</script>
