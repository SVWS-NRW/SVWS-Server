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
  <Story title="SVWS UI/Layout/Table">
    <Variant title="Basic">
      <Table :data="data" />
    </Variant>
    <Variant title="Simple Row Click Selection">
      <Table v-model="clickedRow" :data="data" />
      <br />
      <Button @click="updateSelectedRow">Update Selection</Button>
      <br />
      <strong>Clicked:</strong>
      <div v-if="Object.keys(clickedRow).length === 0">No row clickd</div>
      <span v-else>{{ clickedRow }}</span>
    </Variant>
    <Variant title="Selection Multi">
      <Table v-model:selection="selectedRows2" v-model="clickedRow" :data="data" is-multi-select />
      <br />
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
					class="button button--icon"
				>
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
      <br />
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
          <tr v-for="row in rows"><td>{{ row.name }}</td><td>{{ row.email }}</td></tr>
        </template>
      </Table>
    </Variant>
    <Variant title="Sorting + Selection + Header Labels">
      <Table v-model:selection="selectedRows3" :data="data" :columns="columns" is-multi-select />
      <br />
      <strong>Selected:</strong>
      <div v-if="selectedRows3.length === 0">No rows selected</div>
      <span v-else>{{ selectedRows3 }}</span>
    </Variant>
    <Variant title="Basic + Custom Head Cell">
      <Table :data="data">
        <template #head-age="{ column }">
          <th>
            <Icon>
              <i-ri-alarm-line />
            </Icon>
            {{ column.label }} 🥳
          </th>
        </template>
      </Table>
    </Variant>
    <Variant title="Basic + Custom Cell Content">
      <Table :data="data" :columns="columns">
        <template #cell-email="{ row }">
          <TextInput v-model="row.email" />
        </template>
        <template #cell-age="{ row }">
          <Icon v-if="row.age < 18">
            <i-ri-battery-low-fill />
          </Icon>
          <Icon v-else>
            <i-ri-battery-fill />
          </Icon>
        </template>
      </Table>
    </Variant>
    <Variant title="Basic + Inline Editing">
      <Table :data="data" :columns="columns2">
        <template #cell-name="{ row }">
          <span v-if="!row.isEditing">{{ row.name }}</span>
          <TextInput v-else v-model="row.name" />
        </template>
        <template #cell-email="{ row }">
          <span v-if="!row.isEditing">{{ row.email }}</span>
          <TextInput v-else v-model="row.email" />
        </template>
        <template #cell-age="{ row }">
          <span v-if="!row.isEditing">{{ row.age }}</span>
          <TextInput v-else v-model="row.age" />
        </template>
        <template #cell-actions="{ row }">
          <Popover :hover="false" placement="left-end" :disable-click-away="false">
            <template #trigger>
              <Button class="action-button">
                <Icon>
                  <i-ri-more-2-fill />
                </Icon>
              </Button>
            </template>
            <template #content>
              <div class="action-items">
                <div v-for="action in actions" :key="action.action">
                  <Button class="action-item" type="transparent" @click="execute(action.action, row)">{{ action.label
                  }}</Button>
                </div>
              </div>
            </template>
          </Popover>
        </template>
      </Table>
    </Variant>
    <Variant title="Headless Inputs">
      <Table :data="data" :columns="columns2">
        <template #cell-name="{ row }">
          <span v-if="!row.isEditing" class="table--cell-content">{{ row.name }}</span>
          <TextInput v-else v-model="row.name" headless />
        </template>
        <template #cell-email="{ row }">
          <span v-if="!row.isEditing" class="table--cell-content">{{ row.email }}</span>
          <TextInput v-else v-model="row.email" headless />
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
          <Popover :hover="false" placement="left-end" :disable-click-away="false">
            <template #trigger>
              <Button class="action-button">
                <Icon>
                  <i-ri-more-2-fill />
                </Icon>
              </Button>
            </template>
            <template #content>
              <div class="action-items">
                <div v-for="action in actions" :key="action.action">
                  <Button class="action-item" type="transparent" @click="execute(action.action, row)">{{ action.label
                  }}</Button>
                </div>
              </div>
            </template>
          </Popover>
        </template>
      </Table>
    </Variant>
  </Story>
</template>

<style>
.action-button {
  @apply h-6 w-6;
}

.action-items {
  @apply bg-white;
  @apply flex flex-col;
  @apply px-2 py-1;
  @apply ring-1;
  @apply ring-black ring-opacity-5;
  @apply rounded-md;
  @apply shadow-lg;
  @apply w-48;
}

.action-item {
  @apply block;
  @apply w-full;
}

.action-items:focus {
  @apply outline-none;
}

.action-button:focus {
  @apply outline-none ring-2 ring-inset ring-primary;
}
</style>
