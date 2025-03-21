<template>
	<Story title="Select New" id="ui-select" icon="ri:expand-up-down-line" auto-props-disabled source="nix" :layout="{ type: 'grid', width: '45%'}">
		<Variant title="Single Selection">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					<ui-select label="SimpleSelectManager mit String" :select-manager="sStringSelectManager()" :searchable="state.filter"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" />
					<ui-select label="SimpleSelectManager mit Number" :select-manager="sNumberSelectManager()" :searchable="state.filter"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" />
					<ui-select label="CoreTypeSelectManager" :select-manager="sCoreTypeSelectManager()" :searchable="state.filter" :disabled="state.disabled"
						:statistics="state.statistics" :removable="state.removable" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<template #controls>
				<HstCheckbox v-model="state.filter" title="Filter" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
			</template>
		</Variant>
		<Variant title="Multi Selection">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					<ui-select label="SimpleSelectManager mit String" :select-manager="mStringSelectManager()" :searchable="state.filter"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :min-options="state.minOptions"
						:max-options="state.maxOptions" />
					<ui-select label="SimpleSelectManager mit Number" :select-manager="mNumberSelectManager()" :searchable="state.filter"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :min-options="state.minOptions"
						:max-options="state.maxOptions" />
					<ui-select label="CoreTypeSelectManager" :select-manager="mCoreTypeSelectManager()" :searchable="state.filter" :disabled="state.disabled"
						:statistics="state.statistics" :removable="state.removable" :min-options="state.minOptions" :max-options="state.maxOptions" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<template #controls>
				<HstCheckbox v-model="state.filter" title="Filter" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
				<HstNumber v-model="state.minOptions" title="minOptions" />
				<HstNumber v-model="state.maxOptions" title="maxOptions" />
			</template>
		</Variant>
	</Story>
</template>

  <script setup lang="ts">
	import { reactive } from "vue";
	import { SimpleSelectManager } from "./SimpleSelectManager";
	import { CoreTypeSelectManager } from "./CoreTypeSelectManager";
	import { LehrerRechtsverhaeltnis, Schulform } from "../../../../../core/src";

	const state = reactive({
		filter: false,
		disabled: false,
		statistics: false,
		removable: true,
		minOptions: undefined as number | undefined,
		maxOptions: undefined as number | undefined,
	});

	const stringItems: string[] = ["Ananas", "Aprikose", "Banane", "Birne", "Apfelsine", "Brombeere", "Clementine", "Granatapfel", "Himbeere",
		"Ich will gleich den ganzen Obstkorb haben und am liebsten alles doppelt und dreifach, nur damit dieses Item einen langen Text zur Vorschau hat.",
		"Kirsche", "Kiwi", "Lemon", "Litschi", "Melone", "Orange", "Papaya", "Pfirsich", "Pflaume", "Rote Johannisbeere", "Zitronenmelisse",
	];
	const numberItems: number[] = [ 1990, 1991, 1992, 1993, 1994, 2000, 2001, 2002, 2003, 2004, 2010, 2011, 2012, 2013, 2014, 2020, 2021, 2022, 2023, 2024 ];



	// Arrow Functions sind bei den Managern notwendig! Beim Wechseln zwischen Stories führt Histoire structuredClone() aus und das führt bei komplexen Objekten
	// zu Problemen.
	const sStringSelectManager = () => new SimpleSelectManager(false, stringItems);
	const sNumberSelectManager = () => new SimpleSelectManager(false, numberItems);
	const sCoreTypeSelectManager = () => new CoreTypeSelectManager(false, LehrerRechtsverhaeltnis.class, 2018, Schulform.GY, (item) => `${item.id}: ${item.text}`, 'kuerzelText');

	const mStringSelectManager = () => new SimpleSelectManager(true, stringItems);
	const mNumberSelectManager = () => new SimpleSelectManager(true, numberItems);
	const mCoreTypeSelectManager = () => new CoreTypeSelectManager(true, LehrerRechtsverhaeltnis.class, 2018, Schulform.GY, 'text', 'kuerzelText');

  </script>
