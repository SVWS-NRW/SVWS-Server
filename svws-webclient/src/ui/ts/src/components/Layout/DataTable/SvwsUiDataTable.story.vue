<script setup lang="ts">
	import { logEvent } from "histoire/client";
	import {Ref, reactive, ref} from "vue";
	import type { DataTableColumnSource, DataTableItem, DataTableSortingOrder } from "./types";

	const data = ref([
		{
			id: 1,
			name: "A",
			email: "test@web.de",
			age: 16,
			isActive: false,
		},
		{
			id: 2,
			name: "B",
			email: "bla@gmx.de",
			age: 31,
			isActive: true,
		},
		{
			id: 3,
			name: "C",
			email: "hallo@gmail.com",
			age: 49,
			isActive: true,
		},
		{
			id: 4,
			name: "D",
			email: "hallo@gmail.com",
			age: 49,
			isActive: true,
		}
	]);
	const dataCollapsible = ref([
		{
			id: 1,
		}
	]);
	const set = new Set<DataTableItem>();
	set.add({ id: 1, name: "A", email: "test@web.de", age: 16, isActive: false });
	set.add({ id: 2, name: "B", email: "bla@gmx.de", age: 31, isActive: true });
	const setReactive = reactive(new Set<DataTableItem>());
	setReactive.add({ id: 1, name: "A", email: "test@web.de", age: 16, isActive: false });
	setReactive.add({ id: 2, name: "B", email: "bla@gmx.de", age: 31, isActive: true });
	const columns = ref([
		{
			key: "id",
			label: "ID",
			sortable: true,
			fixedWidth: 4,
			align: "right",
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

	const clickedRow = ref(data.value[0]);
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

	const collapsed = ref(false);
</script>

<template>
	<Story title="SVWS UI/Layout/DataTable" icon="ri:table-2">
		<Variant title="Basic">
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
		<Variant title="Collapsible">
			<section class="flex flex-col">
				<svws-ui-data-table :items="dataCollapsible" collapsible>
					<template #header >
					</template>
					<template #body>
						<div role="row" class="data-table__tr data-table__tbody__tr">
							<div role="row" class="data-table__tr data-table__tbody__tr">
								<div role="cell" class="data-table__td">
									<div class="flex items-center gap-1">
										<svws-ui-button type="icon" size="small" @click="collapsed = !collapsed">
											<i-ri-arrow-right-s-line v-if="collapsed" />
											<i-ri-arrow-down-s-line v-else />
										</svws-ui-button>
										Collapsed Cell Content
									</div>
								</div>
							</div>
							<div role="row" class="data-table__tr data-table__tbody__tr" :class="{'data-table__tr__collapsed': collapsed, 'data-table__tr__expanded': !collapsed}">
								<div role="cell" class="data-table__td">
									<div class="flex items-center gap-1">
										Collapsed Row
									</div>
								</div>
							</div>
						</div>
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
					<svws-ui-popover :hover="false"
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
					</svws-ui-popover>
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
					<svws-ui-popover :hover="false"
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
					</svws-ui-popover>
				</template>
			</svws-ui-data-table>
		</Variant>
	</Story>
</template>
