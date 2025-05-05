<template>
	<Story title="Select New" id="ui-select" icon="ri:expand-up-down-line" auto-props-disabled :layout="{ type: 'grid', width: '45%'}">
		<Variant title="Single Selection">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					<ui-select label="SimpleSelectManager mit String" :select-manager="sStringSelectManager()" :searchable="state.searchable"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :headless="state.headless"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
					<ui-select label="SimpleSelectManager mit Number" :select-manager="sNumberSelectManager()" :searchable="state.searchable"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :headless="state.headless"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
					<ui-select label="CoreTypeSelectManager" :select-manager="sCoreTypeSelectManager()" :searchable="state.searchable" :disabled="state.disabled"
						:statistics="state.statistics" :removable="state.removable" :headless="state.headless"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
					<ui-select label="ObjectSelectManager" :select-manager="sObjectSelectManager()" :searchable="state.searchable"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :headless="state.headless"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<template #controls>
				<HstCheckbox v-model="state.searchable" title="Searchable" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
				<HstCheckbox v-model="state.headless" title="Headless" />
				<span class="text-headline-md">Farben</span>
				<HstRadio v-model="state.bgColor" title="Hintergrund" :options="[
					{ label: 'keine', value: '' },
					{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
					{ label: 'bg-ui-success', value: 'bg-ui-success' },
					{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
				]" />
				<HstRadio v-model="state.textColor" title="Text" :options="[
					{ label: 'keine', value: '' },
					{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
					{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
					{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
				]" />
				<HstRadio v-model="state.iconColor" title="Icon" :options="[
					{ label: 'keine', value: '' },
					{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
					{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
					{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
				]" />
				<HstRadio v-model="state.borderColor" title="Border" :options="[
					{ label: 'keine', value: '' },
					{ label: 'border-ui-brand', value: 'border-ui-brand' },
					{ label: 'border-ui-success', value: 'border-ui-success' },
					{ label: 'border-ui-danger', value: 'border-ui-danger' },
				]" />
			</template>
		</Variant>
		<Variant title="Multi Selection">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					<ui-select label="SimpleSelectManager mit String" :select-manager="mStringSelectManager()" :searchable="state.searchable"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :headless="state.headless"
						:min-options="state.minOptions" :max-options="state.maxOptions"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
					<ui-select label="SimpleSelectManager mit Number" :select-manager="mNumberSelectManager()" :searchable="state.searchable"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :headless="state.headless"
						:min-options="state.minOptions" :max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
					<ui-select label="CoreTypeSelectManager" :select-manager="mCoreTypeSelectManager()" :searchable="state.searchable" :disabled="state.disabled"
						:statistics="state.statistics" :removable="state.removable" :headless="state.headless" :min-options="state.minOptions"
						:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
					<ui-select label="ObjectSelectManager" :select-manager="mObjectSelectManager()" :searchable="state.searchable" :disabled="state.disabled"
						:statistics="state.statistics" :removable="state.removable" :headless="state.headless" :min-options="state.minOptions"
						:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</Variant>
		<Variant title="Filter">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					In diesem Beispiel werden zwei Filter an das Select übergeben. Jeder Filter hat 2 Fachgruppen, die ausgewählt werden können. Solange kein
					Filter gesetzt ist, werden alle Optionen angezeigt. Sobald ein Filter gesetzt wird, werden nur noch dazu passende Optionen zur Verfügung
					gestellt. Gesetzte Fachgruppen in einem Filter ergänzen sich dabei. Wird jedoch in beiden Filtern eine Fachruppe gesetzt, dann werden nur
					Optionen angezeigt, die zu beiden Fachgruppen passen.
					<strong>Filter 1</strong>
					<svws-ui-checkbox v-model="filterState1.fremdsprache">
						Fremdsprachen
					</svws-ui-checkbox>
					<svws-ui-checkbox v-model="filterState1.musikUndKunst">
						Musik und Kunst
					</svws-ui-checkbox>
					<strong>Filter 2</strong>
					<svws-ui-checkbox v-model="filterState2.deutsch">
						Deustch
					</svws-ui-checkbox>
					<svws-ui-checkbox v-model="filterState2.musikUndKunst">
						Musik und Kunst
					</svws-ui-checkbox>
					<ui-select label="CoreTypeSelectManager Fach abhängig von Fachgruppe" :select-manager="sFachSelectManager()" :searchable="state.searchable"
						:disabled="state.disabled" :statistics="state.statistics" :removable="state.removable" :headless="state.headless"
						:min-options="state.minOptions" :max-options="state.maxOptions"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</Variant>
		<Variant title="Deep Search">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					Das folgende Select verwendet Deep Search und lässt auch die Suche nach Attributen zu, die nicht im Optiontext dargestellt werden. So ergibt
					die Suche nach "2006" oder "2008" ebenfalls jeweils ein Auto, da die Optionen folgende sind:
					<pre class="bg-ui-neutral border border-ui-neutral rounded w-fit whitespace-normal p-2">
						<code>
							[<br>
								{ marke: "BMW", color: "blue", baujahr: 2006 },<br>
								{ marke: "Audi", color: "red", baujahr: 2008 }<br>
							]
						</code>
					</pre>
					<ui-select label="Deep Search ObjectSelectManager" :select-manager="deepSearchObjectSelectManager()" :searchable="true" :disabled="state.disabled"
						:statistics="state.statistics" :removable="state.removable" :headless="state.headless" :min-options="state.minOptions"
						:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</Variant>
		<template #source>
			{{ getSourceString() }}
		</template>
		<template #controls>
			<HstCheckbox v-model="state.searchable" title="Searchable" />
			<HstCheckbox v-model="state.disabled" title="Disabled" />
			<HstCheckbox v-model="state.statistics" title="Statistik" />
			<HstCheckbox v-model="state.removable" title="Removable" />
			<HstCheckbox v-model="state.headless" title="Headless" />
			<HstNumber v-model="state.minOptions" title="minOptions" />
			<HstNumber v-model="state.maxOptions" title="maxOptions" />
			<span class="text-headline-md">Farben</span>
			<HstRadio v-model="state.bgColor" title="Hintergrund" :options="[
				{ label: 'keine', value: '' },
				{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
				{ label: 'bg-ui-success', value: 'bg-ui-success' },
				{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
			]" />
			<HstRadio v-model="state.textColor" title="Text" :options="[
				{ label: 'keine', value: '' },
				{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
				{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
				{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
			]" />
			<HstRadio v-model="state.iconColor" title="Icon" :options="[
				{ label: 'keine', value: '' },
				{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
				{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
				{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
			]" />
			<HstRadio v-model="state.borderColor" title="Border" :options="[
				{ label: 'keine', value: '' },
				{ label: 'border-ui-onbrand', value: 'border-ui-onbrand' },
				{ label: 'border-ui-onsuccess', value: 'border-ui-onsuccess' },
				{ label: 'border-ui-ondanger', value: 'border-ui-ondanger' },
			]" />
		</template>
	</Story>
</template>

<script setup lang="ts">

	import { reactive, ref, watchEffect } from "vue";
	import { SimpleSelectManager } from "./selectManager/SimpleSelectManager";
	import { CoreTypeSelectManager } from "./selectManager/CoreTypeSelectManager";
	import { FachSelectFilter as FachSelectFilter } from "./filter/FachSelectFilter";
	import { ArrayList, Fach, Fachgruppe, LehrerRechtsverhaeltnis, Schulform } from "../../../../../core/src";
	import { ObjectSelectManager } from "./selectManager/ObjectSelectManager";



	const state = reactive({
		searchable: false,
		disabled: false,
		statistics: false,
		removable: true,
		headless: false,
		minOptions: undefined as number | undefined,
		maxOptions: undefined as number | undefined,
		bgColor: "",
		textColor: "",
		iconColor: "",
		borderColor: "",
	});

	// Filter lassen sich wegen der structuredClone() Methode von histoire nicht als computed umsetzen, daher dieser Workaround. Andernfalls tauchen entsprechende
	// Warnings in der Konsole auf.

	const filterState1 = reactive({
		fremdsprache: false,
		musikUndKunst: false,
	})

	const filter = ref<Fachgruppe[]>([])

	watchEffect(() => {
		const list = new ArrayList<Fachgruppe>()
		if (filterState1.fremdsprache)
			list.add(Fachgruppe.FG_FS)
		if (filterState1.musikUndKunst)
			list.add(Fachgruppe.FG_MS)
		filter.value = [...list] // in plain array umgewandelt
	})

	const filterState2 = reactive({
		deutsch: false,
		musikUndKunst: false,
	})

	const filter2 = ref<Fachgruppe[]>([])

	watchEffect(() => {
		const list = new ArrayList<Fachgruppe>()
		if (filterState2.deutsch)
			list.add(Fachgruppe.FG_D)
		if (filterState2.musikUndKunst)
			list.add(Fachgruppe.FG_MS)
		filter2.value = [...list]
	})

	const stringItems: string[] = ["Ananas", "Aprikose", "Banane", "Birne", "Apfelsine", "Brombeere", "Clementine", "Granatapfel", "Himbeere",
		"Ich will gleich den ganzen Obstkorb haben und am liebsten alles doppelt und dreifach, nur damit dieses Item einen langen Text zur Vorschau hat.",
		"Kirsche", "Kiwi", "Lemon", "Litschi", "Melone", "Orange", "Papaya", "Pfirsich", "Pflaume", "Rote Johannisbeere", "Zitronenmelisse",
	];
	const numberItems: number[] = [ 1990, 1991, 1992, 1993, 1994, 2000, 2001, 2002, 2003, 2004, 2010, 2011, 2012, 2013, 2014, 2020, 2021, 2022, 2023, 2024 ];
	const carItems: { marke: string, color: string, baujahr: number }[] = [{ marke: "BMW", color: "blue", baujahr: 2006 }, { marke: "Audi", color: "red", baujahr: 2008}];



	// Arrow Functions sind bei den Managern notwendig! Beim Wechseln zwischen Stories führt Histoire structuredClone() aus und das führt bei komplexen Objekten
	// zu Problemen.
	const sStringSelectManager = () => new SimpleSelectManager(false, stringItems);
	const sNumberSelectManager = () => new SimpleSelectManager(false, numberItems);
	const sCoreTypeSelectManager = () => new CoreTypeSelectManager(false, LehrerRechtsverhaeltnis.class, 2018, Schulform.GY, 'text', 'kuerzelText');
	const sObjectSelectManager = () => new ObjectSelectManager(false, carItems,
		(option : { marke: string, color: string }) => option.marke, (option : { marke: string, color: string }) => `${option.marke} - ${option.color}`);
	const deepSearchObjectSelectManager = () => {
		const manager = new ObjectSelectManager(false, carItems,
			(option : { marke: string, color: string, baujahr: number }) => option.marke, (option : { marke: string, color: string, baujahr: number }) => `${option.marke} - ${option.color}`);
		manager.setDeepSearchAttributes(["marke", "color", "baujahr"]);
		return manager;
	}
	const sFachSelectManager = () => {
		const manager = new CoreTypeSelectManager(false, Fach.class, 2020, Schulform.GY, 'text', 'kuerzelText');
		manager.addFilter(new FachSelectFilter("fachgruppe1", filter.value, 2020));
		manager.addFilter(new FachSelectFilter("fachgruppe2", filter2.value, 2020));
		return manager;
	};

	const mStringSelectManager = () => new SimpleSelectManager(true, stringItems);
	const mNumberSelectManager = () => new SimpleSelectManager(true, numberItems);
	const mCoreTypeSelectManager = () => new CoreTypeSelectManager(true, LehrerRechtsverhaeltnis.class, 2018, Schulform.GY, 'text', 'kuerzelText');
	const mObjectSelectManager = () => new ObjectSelectManager(true, carItems,
		(option : { marke: string, color: string }) => option.marke, (option : { marke: string, color: string }) => `${option.marke} - ${option.color}`);


	function getSourceString (multi = false) {
		return `<ui-select
        label="..."
        :select-manager="..."
        ${state.searchable ? 'searchable' : ''}
        ${state.disabled ? 'disabled' : ''}
        ${state.statistics ? 'statistics' : ''}
        ${state.removable ? '' : ':removable="false"'}
        ${state.headless ? 'headless' : ''}
        ${multi ? `:min-options="${state.minOptions}"` : ''}
        ${multi ? `:max-options="${state.maxOptions}"` : ''}
		/>
      `.split('\n').filter(line => line.trim() !== '').join('\n');

	};
</script>
