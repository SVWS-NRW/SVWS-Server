<script setup lang='ts'>
	import { hstEvent } from 'histoire/client';
	import { ref } from 'vue';
	import { type DataTableItem } from './types';

	const data = ref([
		{
			name: 'A',
			email: 'test@web.de',
			age: 16,
			isActive: false,
		},
		{
			name: 'B',
			email: 'bla@gmx.de',
			age: 31,
			isActive: true,
		},
		{
			name: 'C',
			email: 'hallo@gmail.com',
			age: 49,
			isActive: true,
		}
	]);
	const columns = ref([
		{
			key: 'name',
			label: 'Überschriebener NAme',
			sortable: true,
		},
		{
			key: 'email',
			label: 'Digitale Postadresse',
		},
		{
			key: 'age',
			label: 'Wie alt?',
			sortable: true,
			defaultSort: 'desc'
		} as const
	]);
	const columns2 = ref([
		{
			key: 'name',
			label: 'Überschriebener NAme',
			sortable: true,
		},
		{
			key: 'email',
			label: 'Digitale Postadresse',
		},
		{
			key: 'age',
			label: 'Wie alt?',
			sortable: true,
			defaultSort: 'desc'
		} as const,
		{
			key: 'isActive',
			label: 'Is active?',
		}
	]);
	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" },
		{ label: "Edit", action: "edit" }
	];

	const clickedRow = ref(data.value[0]);
	const selectedRows2 = ref([data.value[0]])
	const selectedRows3 = ref([data.value[0]])

	function updateSelectedRow() {
		clickedRow.value = data.value[2];
	}

	function execute(action: string, row: DataTableItem) {
		hstEvent(action, row);

		if (action === 'edit') {
			row.isEditing = !row.isEditing;
		}
	}

	function manipulateData() {
		data.value.splice(0,1);
	}
</script>

<template>
	<Story title="SVWS UI/Layout/Table (Deprecated)" icon="ri:alert-line" icon-color="#ccc">
		<Variant title="Basic">
			<Table :data="data" />
		</Variant>
		<Variant title="Simple Row Click Selection">
			<Table v-model="clickedRow" :data="data" />
			<br>
			<svws-ui-button @click="updateSelectedRow">Update Selection</svws-ui-button>
			<br>
			<strong>Clicked:</strong>
			<div v-if="Object.keys(clickedRow).length === 0">No row clickd</div>
			<span v-else>{{ clickedRow }}</span>
		</Variant>
		<Variant title="Selection Multi">
			<Table v-model:selection="selectedRows2" v-model="clickedRow" :data="data" is-multi-select />
			<br>
			<svws-ui-button @click="manipulateData">Manipulate data prop</svws-ui-button>
			<br>
			<strong>Selected:</strong>
			<div v-if="selectedRows2.length === 0">No rows selected</div>
			<span v-else>{{ selectedRows2 }}</span>
			<div><strong>Clicked:</strong></div>
			<div v-if="Object.keys(clickedRow).length === 0">No row clicked</div>
			<span v-else>{{ clickedRow }}</span>
		</Variant>
		<Variant title="Selection Multi + Footer Action">
			<Table v-model:selection="selectedRows2" v-model="clickedRow" :data="data" is-multi-select :footer="true">
				<template #footer>
					<button
						class="button button--icon">
						<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
					</button>
					<button class="button button--icon">
						<svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
					</button>
					<button class="button button--icon">
						<svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
					</button>
				</template>
			</Table>
			<br>
			<strong>Selected:</strong>
			<div v-if="selectedRows2.length === 0">No rows selected</div>
			<span v-else>{{ selectedRows2 }}</span>
			<div><strong>Clicked:</strong></div>
			<div v-if="Object.keys(clickedRow).length === 0">No row clicked</div>
			<span v-else>{{ clickedRow }}</span>
		</Variant>
		<Variant title="Custom everything">
			<Table :data="data">
				<template #head>
					<tr><th>Fruit</th><th>More Fruit</th></tr>
					<tr><th>Banana</th><th>Apple</th></tr>
				</template>
				<template #body="{rows}">
					<tr v-for="row, index in rows" :key="index"><td>{{ row.name }}</td><td>{{ row.email }}</td></tr>
				</template>
			</Table>
		</Variant>
		<Variant title="Sorting + Selection + Header Labels">
			<Table v-model:selection="selectedRows3" :data="data" :columns="columns" is-multi-select />
			<br>
			<strong>Selected:</strong>
			<div v-if="selectedRows3.length === 0">No rows selected</div>
			<span v-else>{{ selectedRows3 }}</span>
		</Variant>
		<Variant title="Basic + Custom Head Cell">
			<Table :data="data">
				<template #head-age="{ column }">
					<th>
						<svws-ui-icon>
							<i-ri-alarm-line />
						</svws-ui-icon>
						{{ column.label }} 🥳
					</th>
				</template>
			</Table>
		</Variant>
		<Variant title="Basic + Custom Cell Content">
			<Table :data="data" :columns="columns">
				<template #cell-email="{ row }">
					<svws-ui-text-input v-model="row.email" />
				</template>
				<template #cell-age="{ row }">
					<svws-ui-icon v-if="row.age < 18">
						<i-ri-battery-low-fill />
					</svws-ui-icon>
					<svws-ui-icon v-else>
						<i-ri-battery-fill />
					</svws-ui-icon>
				</template>
			</Table>
		</Variant>
		<Variant title="Basic + Inline Editing">
			<Table :data="data" :columns="columns2">
				<template #cell-name="{ row }">
					<span v-if="!row.isEditing">{{ row.name }}</span>
					<svws-ui-text-input v-else v-model="row.name" />
				</template>
				<template #cell-email="{ row }">
					<span v-if="!row.isEditing">{{ row.email }}</span>
					<svws-ui-text-input v-else v-model="row.email" />
				</template>
				<template #cell-age="{ row }">
					<span v-if="!row.isEditing">{{ row.age }}</span>
					<svws-ui-text-input v-else v-model="row.age" />
				</template>
				<template #cell-actions="{ row }">
					<svws-ui-popover :hover="false" placement="left-end" :disable-click-away="false">
						<template #trigger>
							<svws-ui-button class="action-button">
								<svws-ui-icon>
									<i-ri-more-2-fill />
								</svws-ui-icon>
							</svws-ui-button>
						</template>
						<template #content>
							<div class="action-items">
								<div v-for="action in actions" :key="action.action">
									<svws-ui-button class="action-item" type="transparent" @click="execute(action.action, row)">
										{{ action.label
										}}
									</svws-ui-button>
								</div>
							</div>
						</template>
					</svws-ui-popover>
				</template>
			</Table>
		</Variant>
		<Variant title="Headless Inputs">
			<Table :data="data" :columns="columns2">
				<template #cell-name="{ row }">
					<span v-if="!row.isEditing" class="table--cell-content">{{ row.name }}</span>
					<svws-ui-text-input v-else v-model="row.name" headless />
				</template>
				<template #cell-email="{ row }">
					<span v-if="!row.isEditing" class="table--cell-content">{{ row.email }}</span>
					<svws-ui-text-input v-else v-model="row.email" headless />
				</template>
				<template #cell-age="{ row }">
					<span v-if="!row.isEditing" class="table--cell-content">{{ row.age }}</span>
					<MultiSelect v-else v-model="row.age" :items="[{id: 1, text: '< 10',}, {id: 2, text: '11-20'}, {id: 3, text: '> 20'} ]" headless />
				</template>
				<template #cell-isActive="{ row }">
					<span v-if="!row.isEditing" class="table--cell-content">{{ row.isActive }}</span>
					<Toggle v-else v-model="row.isActive" headless />
				</template>
				<template #cell-actions="{ row }">
					<svws-ui-popover :hover="false" placement="left-end" :disable-click-away="false">
						<template #trigger>
							<svws-ui-button class="action-button">
								<svws-ui-icon>
									<i-ri-more-2-fill />
								</svws-ui-icon>
							</svws-ui-button>
						</template>
						<template #content>
							<div class="action-items">
								<div v-for="action in actions" :key="action.action">
									<svws-ui-button class="action-item" type="transparent" @click="execute(action.action, row)">
										{{ action.label
										}}
									</svws-ui-button>
								</div>
							</div>
						</template>
					</svws-ui-popover>
				</template>
			</Table>
		</Variant>
	</Story>
</template>
