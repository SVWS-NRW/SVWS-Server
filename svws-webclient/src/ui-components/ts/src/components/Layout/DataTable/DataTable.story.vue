<script setup lang="ts">
	import { logEvent } from "histoire/client";
	import { ref } from "vue";
	import type { DataTableItem, DataTableSortingOrder } from "./types";

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
		},
		{
			id: 5,
			name: "A",
			email: "test@web.de",
			age: 16,
			isActive: false,
		},
		{
			id: 6,
			name: "B",
			email: "bla@gmx.de",
			age: 31,
			isActive: true,
		},
		{
			id: 7,
			name: "C",
			email: "hallo@gmail.com",
			age: 49,
			isActive: true,
		},
		{
			id: 8,
			name: "D",
			email: "hallo@gmail.com",
			age: 49,
			isActive: true,
		},
		{
			id: 9,
			name: "A",
			email: "test@web.de",
			age: 16,
			isActive: false,
		},
		{
			id: 10,
			name: "B",
			email: "bla@gmx.de",
			age: 31,
			isActive: true,
		},
		{
			id: 11,
			name: "C",
			email: "hallo@gmail.com",
			age: 49,
			isActive: true,
		},
		{
			id: 12,
			name: "D",
			email: "hallo@gmail.com",
			age: 49,
			isActive: true,
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
	]);
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
	]);
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
</script>

<template>
	<Story title="SVWS UI/Layout/DataTable" icon="ri:table-2">
		<Variant title="All Features">
			<section style="max-height: 50vh" class="flex flex-col">
				<DataTable v-model="selectedRows3"
						   :items="data"
						   :columns="columns"
						   selectable
						   v-model:sort-by="sortBy"
						   v-model:sorting-order="sortingOrder"
						   v-model:clicked="clickedRow"
						   clickable
						   count
						   :rowActions="actions"
						   :rowExecute="execute"
				>
					<template #footerActions>
						<Button type="transparent">
							Button
						</Button>
						<Button type="trash">
							Button
						</Button>
						<Button type="icon">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</Button>
					</template>
				</DataTable>
			</section>
			<section class="mt-8">
				<code><strong>Sort By:</strong> {{ sortBy }}</code><br/>
				<code><strong>Sorting Order:</strong> {{ sortingOrder }}</code><br/>
				<code><strong>Clicked Row:</strong> {{ clickedRow }}</code><br/>
				<code><strong>Selected Rows:</strong> {{ selectedRows3 }}</code>
			</section>
		</Variant>
		<Variant title="Basic">
			<DataTable :items="data" />
		</Variant>
		<Variant title="Basic Iterable">
			<DataTable :items="set" />
		</Variant>
		<Variant title="Simple Row Click Selection">
			<DataTable v-model:clicked="clickedRow" :items="data" clickable />
			<p>Clicked row: {{ clickedRow }}</p>
		</Variant>
		<Variant title="Selection Multi">
			<DataTable v-model="selectedRows2"
				:items="data"
				selectable
				v-model:clicked="clickedRow"
				clickable />
			<p>Clicked row: {{ clickedRow }}</p>
			<p>Selected Rows: {{ selectedRows2 }}</p>
		</Variant>
		<Variant title="Selection Multi Iterable">
			<DataTable v-model="selectedRows2"
				:items="set"
				selectable
				v-model:clicked="clickedRow"
				clickable />
			<p>Clicked row: {{ clickedRow }}</p>
			<p>Selected Rows: {{ selectedRows2 }}</p>
		</Variant>
		<Variant title="Selection Multi reactive-Iterable">
			<DataTable v-model="selectedRows2"
				:items="setReactive"
				selectable
				v-model:clicked="clickedRow"
				clickable />
			<p>Clicked row: {{ clickedRow }}</p>
			<p>Selected Rows: {{ selectedRows4 }}</p>
		</Variant>
		<Variant title="Selection Multi + Footer Action">
			<DataTable v-model="selectedRows2" :items="data" selectable>
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
			</DataTable>
		</Variant>
		<Variant title="Custom everything">
			<DataTable :items="data">
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
			</DataTable>
		</Variant>
		<Variant title="Sorting + Selection + Header Labels">
			<DataTable v-model="selectedRows3"
				:items="data"
				:columns="columns"
				selectable
				v-model:sort-by="sortBy"
				v-model:sorting-order="sortingOrder" />
			<p>Sort By: {{ sortBy }}</p>
			<p>Sorting Order: {{ sortingOrder }}</p>
		</Variant>
		<Variant title="Basic + Custom Head Cell">
			<DataTable :items="data">
				<template #header(age)="{ column: { label } }">
					<div class="flex">
						<Icon>
							<i-ri-alarm-line />
						</Icon>
						{{ label }} 🥳
					</div>
				</template>
			</DataTable>
		</Variant>
		<Variant title="Basic + Custom Cell Content">
			<DataTable :items="data" :columns="columns">
				<template #cell(email)="{ value }">
					{{ value }}
				</template>
				<template #cell(age)="{ value }">
					<Icon v-if="value < 18">
						<i-ri-battery-low-fill />
					</Icon>
					<Icon v-else>
						<i-ri-battery-fill />
					</Icon>
				</template>
			</DataTable>
		</Variant>
		<Variant title="Basic + Inline Editing">
			<DataTable :items="data" :columns="columns2">
				<template #cell(name)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<TextInput v-else v-model="rowData.name" />
				</template>
				<template #cell(email)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<TextInput v-else v-model="rowData.email" />
				</template>
				<template #cell(age)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<TextInput v-else v-model="rowData.name.age" />
				</template>
				<template #cell(actions)="{ rowData }">
					<Popover :hover="false"
						placement="left-end"
						:disable-click-away="false">
						<template #trigger>
							<Button class="action-button">
								<Icon>
									<i-ri-more-2-fill />
								</Icon>
							</Button>
						</template>
						<template #content>
							<div class="action-items">
								<div v-for="action in actions"
									:key="action.action">
									<Button class="action-item"
										type="transparent"
										@click="execute(action.action, rowData)">
										{{ action.label }}
									</Button>
								</div>
							</div>
						</template>
					</Popover>
				</template>
			</DataTable>
		</Variant>
		<Variant title="Headless Inputs">
			<DataTable :items="data" :columns="columns2">
				<template #cell(name)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<TextInput v-else v-model="rowData.name" headless />
				</template>
				<template #cell(email)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<TextInput v-else v-model="rowData.email" headless />
				</template>
				<template #cell(age)="{ value, rowData }">
					<span class="data-table__td-content" v-if="!rowData.isEditing">{{ value }}</span>
					<TextInput v-else v-model="rowData.age" headless />
				</template>
				<template #cell(actions)="{ rowData }">
					<Popover :hover="false"
						placement="left-end"
						:disable-click-away="false">
						<template #trigger>
							<Button class="action-button">
								<Icon>
									<i-ri-more-2-fill />
								</Icon>
							</Button>
						</template>
						<template #content>
							<div class="action-items">
								<div v-for="action in actions"
									:key="action.action">
									<Button class="action-item"
										type="transparent"
										@click="execute(action.action, rowData)">
										{{ action.label }}
									</Button>
								</div>
							</div>
						</template>
					</Popover>
				</template>
			</DataTable>
		</Variant>
	</Story>
</template>
