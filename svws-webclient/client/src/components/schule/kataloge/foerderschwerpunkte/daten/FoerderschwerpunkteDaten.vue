<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Förderschwerpunkt ASD-Kürzel"
						v-model="model.foerderschwerpunkt.value"
						:manager="foerderschwerpunktKuerzelManager"
						searchable required :removable="false" statistics />
					<ui-select label="Förderschwerpunkt ASD-Text" class="contentFocusField"
						v-model="model.foerderschwerpunkt.value"
						:manager="foerderschwerpunktTextManager"
						searchable :removable="false" statistics required />
					<svws-ui-text-input placeholder="Interne Bezeichnung" span="2"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@commit="model.patch"
						:max-len="50" required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@commit="model.patch"
						:min="0" :max="32000" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { Foerderschwerpunkt } from "@core";
	import type { FoerderschwerpunkteDatenProps } from "~/components/schule/kataloge/foerderschwerpunkte/daten/FoerderschwerpunkteDatenProps";
	import { CoreTypeSelectManager } from "@ui";
	import { FoerderschwerpunkteModelProxy } from "~/components/schule/kataloge/foerderschwerpunkte/modelproxy/FoerderschwerpunkteModelProxy";

	const props = defineProps<FoerderschwerpunkteDatenProps>();
	const model = new FoerderschwerpunkteModelProxy(() => props.manager().daten(), props.manager, props.schuljahr, props.patch);

	const foerderschwerpunktKuerzelManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const foerderschwerpunktTextManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>
