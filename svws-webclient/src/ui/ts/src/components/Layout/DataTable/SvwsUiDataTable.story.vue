<script setup lang="ts">
	import { logEvent } from "histoire/client";
	import type {Ref} from "vue";
	import { reactive, ref} from "vue";
	import type { DataTableColumnSource, DataTableItem, DataTableSortingOrder } from "./types";

	const data = ref([
		{
			"id": "6435185e4d6c9c5ebdb14a65",
			"email": "shermanjoseph@barkarama.com",
			"name": "Nicholson",
			"age": 32,
			"isActive": true
		},
		{
			"id": "6435185e4199f940e786d831",
			"email": "nicholsonjoseph@barkarama.com",
			"name": "Lorraine",
			"age": 30,
			"isActive": false
		},
		{
			"id": "6435185e0ad9a9f269d37a10",
			"email": "lorrainejoseph@barkarama.com",
			"name": "Berta",
			"age": 28,
			"isActive": true
		},
		{
			"id": "6435185e8f64be794ee58fe9",
			"email": "bertajoseph@barkarama.com",
			"name": "Neal",
			"age": 36,
			"isActive": false
		},
		{
			"id": "6435185e01443306bc959b8a",
			"email": "nealjoseph@barkarama.com",
			"name": "Clare",
			"age": 30,
			"isActive": true
		},
		{
			"id": "6435185ef54b710b63a6d99e",
			"email": "clarejoseph@barkarama.com",
			"name": "Blanche",
			"age": 35,
			"isActive": false
		},
		{
			"id": "6435185eedc4d9388d160e95",
			"email": "blanchejoseph@barkarama.com",
			"name": "Perry",
			"age": 31,
			"isActive": false
		},
		{
			"id": "6435185e5f76ec01a66e8482",
			"email": "perryjoseph@barkarama.com",
			"name": "Callahan",
			"age": 24,
			"isActive": true
		},
		{
			"id": "6435185ebcb463d402aef799",
			"email": "callahanjoseph@barkarama.com",
			"name": "Tonia",
			"age": 31,
			"isActive": true
		},
		{
			"id": "6435185ee74583db924c081f",
			"email": "toniajoseph@barkarama.com",
			"name": "Roy",
			"age": 20,
			"isActive": true
		},
		{
			"id": "6435185e943d4c71e8b24be7",
			"email": "royjoseph@barkarama.com",
			"name": "Cummings",
			"age": 27,
			"isActive": false
		},
		{
			"id": "6435185e39a422abe50804e7",
			"email": "cummingsjoseph@barkarama.com",
			"name": "Mason",
			"age": 36,
			"isActive": true
		},
		{
			"id": "6435185e3ba677291de5e885",
			"email": "masonjoseph@barkarama.com",
			"name": "Merritt",
			"age": 20,
			"isActive": false
		},
		{
			"id": "6435185ecd5bf8d1689fac1c",
			"email": "merrittjoseph@barkarama.com",
			"name": "Mullins",
			"age": 27,
			"isActive": false
		},
		{
			"id": "6435185e01d97098df50b992",
			"email": "mullinsjoseph@barkarama.com",
			"name": "Nona",
			"age": 25,
			"isActive": false
		},
		{
			"id": "6435185ef3da44fb935a003d",
			"email": "nonajoseph@barkarama.com",
			"name": "Lydia",
			"age": 26,
			"isActive": false
		},
		{
			"id": "6435185ef2ed2f7560ab6d6b",
			"email": "lydiajoseph@barkarama.com",
			"name": "Klein",
			"age": 36,
			"isActive": false
		},
		{
			"id": "6435185ebe5094e68cc2b73d",
			"email": "kleinjoseph@barkarama.com",
			"name": "Sloan",
			"age": 37,
			"isActive": false
		},
		{
			"id": "6435185e910a3bee912069ff",
			"email": "sloanjoseph@barkarama.com",
			"name": "Mcdonald",
			"age": 32,
			"isActive": true
		},
		{
			"id": "6435185e5e200ebd6fd073ca",
			"email": "mcdonaldjoseph@barkarama.com",
			"name": "Cindy",
			"age": 25,
			"isActive": false
		},
		{
			"id": "6435185e33f27433c3fd2b0c",
			"email": "cindyjoseph@barkarama.com",
			"name": "Johns",
			"age": 31,
			"isActive": true
		},
		{
			"id": "6435185eee682bb5a20b02ce",
			"email": "johnsjoseph@barkarama.com",
			"name": "Potter",
			"age": 40,
			"isActive": true
		},
		{
			"id": "6435185ea4822b8213cd99df",
			"email": "potterjoseph@barkarama.com",
			"name": "Francesca",
			"age": 31,
			"isActive": false
		},
		{
			"id": "6435185ebefe8ed62a414003",
			"email": "francescajoseph@barkarama.com",
			"name": "Addie",
			"age": 34,
			"isActive": false
		},
		{
			"id": "6435185e6e982e2c8590fd9e",
			"email": "addiejoseph@barkarama.com",
			"name": "Shields",
			"age": 33,
			"isActive": false
		},
		{
			"id": "6435185e8a759ad1effb47a0",
			"email": "shieldsjoseph@barkarama.com",
			"name": "Rice",
			"age": 25,
			"isActive": true
		},
		{
			"id": "6435185eb1cf99ac826b269e",
			"email": "ricejoseph@barkarama.com",
			"name": "Thornton",
			"age": 37,
			"isActive": false
		},
		{
			"id": "6435185e340497a2b2f1e06a",
			"email": "thorntonjoseph@barkarama.com",
			"name": "Lucinda",
			"age": 20,
			"isActive": true
		},
		{
			"id": "6435185e3ddcd5200d6b9287",
			"email": "lucindajoseph@barkarama.com",
			"name": "Verna",
			"age": 32,
			"isActive": true
		},
		{
			"id": "6435185ee8fb73f2e4140d04",
			"email": "vernajoseph@barkarama.com",
			"name": "Gomez",
			"age": 39,
			"isActive": false
		},
		{
			"id": "6435185e189beec86164b4a0",
			"email": "gomezjoseph@barkarama.com",
			"name": "Bender",
			"age": 24,
			"isActive": true
		},
		{
			"id": "6435185ebc7f058ff1179f45",
			"email": "benderjoseph@barkarama.com",
			"name": "Holloway",
			"age": 40,
			"isActive": true
		},
		{
			"id": "6435185e959a6b5f8dbb5221",
			"email": "hollowayjoseph@barkarama.com",
			"name": "Tammi",
			"age": 28,
			"isActive": true
		},
		{
			"id": "6435185e0d57aed234dd295f",
			"email": "tammijoseph@barkarama.com",
			"name": "Yvette",
			"age": 28,
			"isActive": false
		},
		{
			"id": "6435185e3550ce13c0c9e1e4",
			"email": "yvettejoseph@barkarama.com",
			"name": "Simpson",
			"age": 40,
			"isActive": true
		},
		{
			"id": "6435185eb8e0cd05d2316349",
			"email": "simpsonjoseph@barkarama.com",
			"name": "Gibson",
			"age": 29,
			"isActive": false
		},
		{
			"id": "6435185e8eaebf8089ef6d4d",
			"email": "gibsonjoseph@barkarama.com",
			"name": "Moran",
			"age": 37,
			"isActive": true
		},
		{
			"id": "6435185e5a8633746f8907c1",
			"email": "moranjoseph@barkarama.com",
			"name": "Louella",
			"age": 24,
			"isActive": true
		},
		{
			"id": "6435185edd98e2770ebd9f6e",
			"email": "louellajoseph@barkarama.com",
			"name": "Dunlap",
			"age": 22,
			"isActive": false
		},
		{
			"id": "6435185e3c2b646609432164",
			"email": "dunlapjoseph@barkarama.com",
			"name": "Deidre",
			"age": 25,
			"isActive": false
		},
		{
			"id": "6435185e9f9cf05d628b7c27",
			"email": "deidrejoseph@barkarama.com",
			"name": "Guadalupe",
			"age": 20,
			"isActive": false
		},
		{
			"id": "6435185e6455ddff03029246",
			"email": "guadalupejoseph@barkarama.com",
			"name": "Adriana",
			"age": 28,
			"isActive": true
		},
		{
			"id": "6435185e9ab71ac00a7e8fa8",
			"email": "adrianajoseph@barkarama.com",
			"name": "Penny",
			"age": 26,
			"isActive": true
		},
		{
			"id": "6435185e90502fd227c6fde7",
			"email": "pennyjoseph@barkarama.com",
			"name": "Carney",
			"age": 23,
			"isActive": false
		},
		{
			"id": "6435185ed708fa83a012c554",
			"email": "carneyjoseph@barkarama.com",
			"name": "Rosie",
			"age": 19,
			"isActive": false
		}
	]);
	const dataCollapsible = ref([
		{
			column: 1,
			content: 2,
		}
	]);
	const set = new Set<DataTableItem>();
	set.add({
		"id": "643517f82c0533f1cc096130",
		"email": "perezjoseph@barkarama.com",
		"name": "Kathie",
		"age": 38,
		"isActive": false
	});
	set.add({
		"id": "643517f86d23cd36ad9e1d1f",
		"email": "kathiejoseph@barkarama.com",
		"name": "Cherry",
		"age": 34,
		"isActive": false
	});
	const setReactive = reactive(new Set<DataTableItem>());
	setReactive.add({
		"id": "643517f82c0533f1cc096130",
		"email": "perezjoseph@barkarama.com",
		"name": "Kathie",
		"age": 38,
		"isActive": false
	});
	setReactive.add({
		"id": "643517f86d23cd36ad9e1d1f",
		"email": "kathiejoseph@barkarama.com",
		"name": "Cherry",
		"age": 34,
		"isActive": false
	});
	const columns = ref([
		{
			key: "id",
			label: "ID",
			sortable: true,
			fixedWidth: 4,
		},
		{
			key: "name",
			label: "Überschriebener Name",
			sortable: true,
			span: 2,
		},
		{
			key: "email",
			label: "Digitale Postadresse",
			disabled: true,
		},
		{
			key: "age",
			label: "Wie alt?",
			sortable: true,
		} as const,
	]) as Ref<DataTableColumnSource[]>;
	const varcol = ref(columns);
	const columns_ohne_id = ref([
		{
			key: "name",
			label: "Überschriebener Name",
			sortable: true,
			span: 2,
		},
		{
			key: "email",
			label: "Digitale Postadresse",
		},
		{
			key: "age",
			label: "Wie alt?",
			sortable: true,
		} as const,
	]) as Ref<DataTableColumnSource[]>;
	const columns2 = ref([
		{
			key: "name",
			label: "Überschriebener Name",
			sortable: true,
		},
		{
			key: "email",
			label: "Digitale Postadresse",
		},
		{
			key: "age",
			label: "Wie alt?",
			sortable: true,
		} as const,
		{
			key: "isActive",
			label: "Is active?",
		},
		{
			key: "actions",
			label: "",
		},
	]) as Ref<DataTableColumnSource[]>;
	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" },
		{ label: "Edit", action: "edit" },
	];

	const clickedRow = ref(data.value[data.value.length - 1]);
	const selectedRows2 = ref([]);
	const selectedRows3 = ref([]);
	const selectedRows4 = ref([]);
	const sortBy = ref("age");
	const sortingOrder = ref<DataTableSortingOrder>("desc");

	function execute(action: string, row: DataTableItem) {
		logEvent(action, row);

		if (action === "edit") {
			if (row?.isEditing) {
				row.isEditing = !row.isEditing;
			} else {
				row.isEditing = true;
			}
		}
	}

	const items = reactive([
		{ id: 1, text: "item1" },
		{ id: 2, text: "item2" },
		{ id: 3, text: "item3" },
		{ id: 3, text: "item4" },
		{ id: 3, text: "item5" },
		{ id: 3, text: "item6" }
	]);

	const modelValueComboBox = ref([items[0]]);
	const modelValueComboBox1 = ref();
	const modelValueComboBox2 = ref();
	const modelValueComboBox3 = ref();
	const modelValueComboBox4 = ref();

	function onInput(event: Event) {
		logEvent("input", event);
	}

	const collapsed = ref([
		false,
		true,
		true,
		true,
		true,
	]);
</script>

<template>
	<Story title="Data Table" icon="ri:table-2">
		<Variant title="Basic">
			<section class="flex flex-col h-[50vh]">
				<svws-ui-data-table v-model="selectedRows3"
					:items="data"
					:columns="columns"
					selectable
					v-model:sort-by="sortBy"
					v-model:sorting-order="sortingOrder"
					v-model:clicked="clickedRow"
					clickable
					count
					:row-actions="actions"
					:row-execute="execute"
					:filter="true">
					<template #search>
						<svws-ui-text-input type="search" placeholder="Suche" />
					</template>
					<template #filter>
						<svws-ui-multi-select v-model="modelValueComboBox3"
							autocomplete
							title="Filter 1"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
						<svws-ui-multi-select v-model="modelValueComboBox4"
							autocomplete
							title="Filter 2"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
					</template>
					<template #footerActions>
						<svws-ui-button type="transparent">
							Button
						</svws-ui-button>
						<svws-ui-button type="trash">
							Button
						</svws-ui-button>
						<svws-ui-button type="icon">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</svws-ui-button>
					</template>
				</svws-ui-data-table>
			</section>
			<section class="mt-8">
				<code><strong>Sort By:</strong> {{ sortBy }}</code><br>
				<code><strong>Sorting Order:</strong> {{ sortingOrder }}</code><br>
				<code><strong>Clicked Row:</strong> {{ clickedRow }}</code><br>
				<code><strong>Selected Rows:</strong> {{ selectedRows3 }}</code>
			</section>
		</Variant>
		<Variant title="Collapsible">
			<section class="flex flex-col">
				<svws-ui-data-table :items="dataCollapsible" collapsible>
					<template #header />
					<template #body>
						<svws-ui-table-row>
							<svws-ui-table-cell>
								<div class="flex items-center gap-1">
									<svws-ui-button type="icon" size="small" @click="collapsed[0] = !collapsed[0]">
										<i-ri-arrow-right-s-line v-if="collapsed[0]" />
										<i-ri-arrow-down-s-line v-else />
									</svws-ui-button>
									Collapsible Row
								</div>
							</svws-ui-table-cell>
							<svws-ui-table-cell>
								Content
							</svws-ui-table-cell>
						</svws-ui-table-row>
						<svws-ui-table-row :depth="1" :collapsed="collapsed[0]" :expanded="!collapsed[0]">
							<svws-ui-table-cell>
								<div class="flex items-center gap-1">
									<svws-ui-button type="icon" size="small" @click="collapsed[1] = !collapsed[1]">
										<i-ri-arrow-right-s-line v-if="collapsed[1]" />
										<i-ri-arrow-down-s-line v-else />
									</svws-ui-button>
									<svws-ui-checkbox />
									Collapsible Row (Depth 1)
								</div>
							</svws-ui-table-cell>
							<svws-ui-table-cell>
								Content
							</svws-ui-table-cell>
						</svws-ui-table-row>
						<svws-ui-table-row :depth="2" :collapsed="collapsed[0] || collapsed[1]" :expanded="!collapsed[1]">
							<svws-ui-table-cell span="full">
								<div class="flex items-center gap-1">
									<svws-ui-checkbox />
									Normal Row
								</div>
							</svws-ui-table-cell>
						</svws-ui-table-row>
						<svws-ui-table-row :depth="2" :collapsed="collapsed[0] || collapsed[1]" :expanded="!collapsed[1]">
							<svws-ui-table-cell span="full">
								Normal Row
							</svws-ui-table-cell>
						</svws-ui-table-row>
						<svws-ui-table-row :depth="2" :collapsed="collapsed[0] || collapsed[1]" :expanded="!collapsed[1]">
							<svws-ui-table-cell span="full">
								<div class="flex items-center gap-1">
									<svws-ui-button type="icon" size="small" @click="collapsed[2] = !collapsed[2]">
										<i-ri-arrow-right-s-line v-if="collapsed[2]" />
										<i-ri-arrow-down-s-line v-else />
									</svws-ui-button>
									Collapsible Row (Depth 2)
								</div>
							</svws-ui-table-cell>
						</svws-ui-table-row>
						<svws-ui-table-row :depth="3" :collapsed="collapsed[0] || collapsed[1] || collapsed[2]" :expanded="!collapsed[2]">
							<svws-ui-table-cell>
								<div class="flex items-center gap-1">
									<svws-ui-button type="icon" size="small" @click="collapsed[3] = !collapsed[3]">
										<i-ri-arrow-right-s-line v-if="collapsed[3]" />
										<i-ri-arrow-down-s-line v-else />
									</svws-ui-button>
									Collapsible Row (Depth 3)
								</div>
							</svws-ui-table-cell>
							<svws-ui-table-cell>
								Content
							</svws-ui-table-cell>
						</svws-ui-table-row>
						<svws-ui-table-row :depth="4" :collapsed="collapsed[0] || collapsed[1] || collapsed[2] || collapsed[3]" :expanded="!collapsed[3]">
							<svws-ui-table-cell>
								Collapsible Row (Depth 4)
							</svws-ui-table-cell>
							<svws-ui-table-cell>
								Content
							</svws-ui-table-cell>
						</svws-ui-table-row>
					</template>
				</svws-ui-data-table>
			</section>
		</Variant>
		<Variant title="Drag-n-Drop">
			<section class="flex flex-col">
				<svws-ui-data-table v-model="selectedRows3"
					:items="data"
					:columns="columns"
					selectable
					v-model:sort-by="sortBy"
					v-model:sorting-order="sortingOrder"
					v-model:clicked="clickedRow"
					clickable
					count
					:row-actions="actions"
					:row-execute="execute"
					:filter="true">
					<template #search>
						<svws-ui-text-input type="search" placeholder="Suche" />
					</template>
					<template #filter>
						<svws-ui-multi-select v-model="modelValueComboBox3"
							autocomplete
							title="Filter 1"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
						<svws-ui-multi-select v-model="modelValueComboBox4"
							autocomplete
							title="Filter 2"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
					</template>
					<template #footerActions>
						<svws-ui-button type="transparent">
							Button
						</svws-ui-button>
						<svws-ui-button type="trash">
							Button
						</svws-ui-button>
						<svws-ui-button type="icon">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</svws-ui-button>
					</template>
				</svws-ui-data-table>
			</section>
			<section class="mt-8">
				<code><strong>Sort By:</strong> {{ sortBy }}</code><br>
				<code><strong>Sorting Order:</strong> {{ sortingOrder }}</code><br>
				<code><strong>Clicked Row:</strong> {{ clickedRow }}</code><br>
				<code><strong>Selected Rows:</strong> {{ selectedRows3 }}</code>
			</section>
		</Variant>
		<Variant title="Complex Filters">
			<section style="max-height: 50vh" class="flex flex-col complex-filters">
				<svws-ui-data-table v-model="selectedRows3"
					:items="data"
					:columns="columns"
					selectable
					v-model:sort-by="sortBy"
					v-model:sorting-order="sortingOrder"
					v-model:clicked="clickedRow"
					clickable
					count
					:row-actions="actions"
					:row-execute="execute"
					:filter="true"
					:filter-open="true">
					<template #search>
						<svws-ui-text-input type="search" placeholder="Suche" />
					</template>
					<template #filter>
						<svws-ui-multi-select v-model="modelValueComboBox"
							title="Filter"
							:items="items"
							tags
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput"
							class="col-span-full mb-3" />
						<svws-ui-multi-select v-model="modelValueComboBox1"
							autocomplete
							title="Filter 1"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
						<svws-ui-multi-select v-model="modelValueComboBox2"
							autocomplete
							title="Filter 2 Long Title"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
						<svws-ui-multi-select v-model="modelValueComboBox3"
							autocomplete
							title="Filter 3"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
						<svws-ui-multi-select v-model="modelValueComboBox4"
							autocomplete
							title="Filter 4"
							:items="items"
							:item-text="item => item?.text || ''"
							:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
							@input="onInput" />
					</template>
					<template #footerActions>
						<svws-ui-button type="transparent">
							Button
						</svws-ui-button>
						<svws-ui-button type="trash">
							Button
						</svws-ui-button>
						<svws-ui-button type="icon">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</svws-ui-button>
					</template>
				</svws-ui-data-table>
			</section>
			<section class="mt-8">
				<code><strong>Sort By:</strong> {{ sortBy }}</code><br>
				<code><strong>Sorting Order:</strong> {{ sortingOrder }}</code><br>
				<code><strong>Clicked Row:</strong> {{ clickedRow }}</code><br>
				<code><strong>Selected Rows:</strong> {{ selectedRows3 }}</code>
			</section>
		</Variant>
		<Variant title="Simple">
			<svws-ui-data-table :items="data" />
		</Variant>
		<Variant title="Simple mit zuschaltbaren Columns">
			<div class="flex gap-3 mb-3">
				<svws-ui-button @click="varcol=columns">Columns</svws-ui-button>
				<svws-ui-button @click="varcol=columns_ohne_id">Columns ohne ID</svws-ui-button>
			</div>
			<svws-ui-data-table :items="data" :columns="varcol" />
		</Variant>
		<Variant title="Simple Iterable">
			<svws-ui-data-table :items="set" />
		</Variant>
		<Variant title="Simple Row Click Selection">
			<svws-ui-data-table v-model:clicked="clickedRow" :items="data" clickable />
			<p>Clicked row: {{ clickedRow }}</p>
		</Variant>
		<Variant title="Selection Multi">
			<svws-ui-data-table v-model="selectedRows2"
				:items="data"
				selectable
				v-model:clicked="clickedRow"
				clickable />
			<p>Clicked row: {{ clickedRow }}</p>
			<p>Selected Rows: {{ selectedRows2 }}</p>
		</Variant>
		<Variant title="Selection Multi Iterable">
			<svws-ui-data-table v-model="selectedRows2"
				:items="set"
				selectable
				v-model:clicked="clickedRow"
				clickable />
			<p>Clicked row: {{ clickedRow }}</p>
			<p>Selected Rows: {{ selectedRows2 }}</p>
		</Variant>
		<Variant title="Selection Multi reactive-Iterable">
			<svws-ui-data-table v-model="selectedRows2"
				:items="setReactive"
				selectable
				v-model:clicked="clickedRow"
				clickable />
			<p>Clicked row: {{ clickedRow }}</p>
			<p>Selected Rows: {{ selectedRows4 }}</p>
		</Variant>
		<Variant title="Selection Multi + Footer Action">
			<svws-ui-data-table v-model="selectedRows2" :items="data" selectable>
				<template #footerActions>
					<button class="button button--icon">
						<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
					</button>
					<button class="button button--icon">
						<svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
					</button>
					<button class="button button--icon">
						<svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
					</button>
				</template>
			</svws-ui-data-table>
		</Variant>
		<Variant title="Custom everything">
			<svws-ui-data-table :items="data">
				<template #header>
					<tr class="data-table__tr">
						<th class="data-table__th">Fruit</th>
						<th class="data-table__th">More Fruit</th>
					</tr>
					<tr class="data-table__tr">
						<th class="data-table__th">Banana</th>
						<th class="data-table__th">Apple</th>
					</tr>
				</template>
				<template #body="{ rows }">
					<tr class="data-table__tr"
						v-for="(row, index) in rows"
						:key="index">
						<td class="data-table__td">{{ row.source.name }}</td>
						<td class="data-table__td">{{ row.source.email }}</td>
					</tr>
				</template>
			</svws-ui-data-table>
		</Variant>
		<Variant title="Sorting + Selection + Header Labels">
			<svws-ui-data-table v-model="selectedRows3"
				:items="data"
				:columns="columns"
				selectable
				v-model:sort-by="sortBy"
				v-model:sorting-order="sortingOrder" />
			<p>Sort By: {{ sortBy }}</p>
			<p>Sorting Order: {{ sortingOrder }}</p>
		</Variant>
		<Variant title="Basic + Custom Head Cell">
			<svws-ui-data-table :items="data">
				<template #header(age)="{ column: { label } }">
					<div class="flex">
						<svws-ui-icon>
							<i-ri-alarm-line />
						</svws-ui-icon>
						{{ label }} 🥳
					</div>
				</template>
			</svws-ui-data-table>
		</Variant>
		<Variant title="Basic + Custom Cell Content">
			<svws-ui-data-table :items="data" :columns="columns">
				<template #cell(email)="{ value }">
					{{ value }}
				</template>
				<template #cell(age)="{ value }">
					<svws-ui-icon v-if="value < 18">
						<i-ri-battery-low-fill />
					</svws-ui-icon>
					<svws-ui-icon v-else>
						<i-ri-battery-fill />
					</svws-ui-icon>
				</template>
			</svws-ui-data-table>
		</Variant>
		<Variant title="Basic + Inline Editing">
			<svws-ui-data-table :items="data" :columns="columns2">
				<template #cell(name)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<svws-ui-text-input v-else v-model="rowData.name" />
				</template>
				<template #cell(email)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<svws-ui-text-input v-else v-model="rowData.email" />
				</template>
				<template #cell(age)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<svws-ui-text-input v-else v-model="rowData.name.age" />
				</template>
				<template #cell(actions)="{ rowData }">
					<!-- <svws-ui-popover :hover="false"
						placement="left-end"
						:disable-click-away="false">
						<template #trigger>
							<svws-ui-button class="action-button">
								<svws-ui-icon>
									<i-ri-more-2-fill />
								</svws-ui-icon>
							</svws-ui-button>
						</template>
						<template #content>
							<div class="action-items">
								<div v-for="action in actions"
									:key="action.action">
									<svws-ui-button class="action-item"
										type="transparent"
										@click="execute(action.action, rowData)">
										{{ action.label }}
									</svws-ui-button>
								</div>
							</div>
						</template>
					</svws-ui-popover> -->
				</template>
			</svws-ui-data-table>
		</Variant>
		<Variant title="Headless Inputs">
			<svws-ui-data-table :items="data" :columns="columns2">
				<template #cell(name)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<svws-ui-text-input v-else v-model="rowData.name" headless />
				</template>
				<template #cell(email)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<svws-ui-text-input v-else v-model="rowData.email" headless />
				</template>
				<template #cell(age)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<svws-ui-text-input v-else v-model="rowData.age" headless />
				</template>
				<template #cell(actions)="{ rowData }">
					<!-- <svws-ui-popover :hover="false"
						placement="left-end"
						:disable-click-away="false">
						<template #trigger>
							<svws-ui-button class="action-button">
								<svws-ui-icon>
									<i-ri-more-2-fill />
								</svws-ui-icon>
							</svws-ui-button>
						</template>
						<template #content>
							<div class="action-items">
								<div v-for="action in actions"
									:key="action.action">
									<svws-ui-button class="action-item"
										type="transparent"
										@click="execute(action.action, rowData)">
										{{ action.label }}
									</svws-ui-button>
								</div>
							</div>
						</template>
					</svws-ui-popover> -->
				</template>
			</svws-ui-data-table>
		</Variant>
	</Story>
</template>
