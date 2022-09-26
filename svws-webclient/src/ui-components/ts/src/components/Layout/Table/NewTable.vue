<script setup lang='ts'>
import useColumns from './features/use-columns';
import type { DataTableItem, DataTableColumn } from './types';

const {
  data = [],
  columns = [],
  isMultiSelect = false,
  modelValue = {},
  selection = [],
  footer = false,
} = defineProps<{
  data: Array<DataTableItem>,
  columns?: Array<DataTableColumn>,
  isMultiSelect?: boolean,
  selection?: Array<DataTableItem>,
  modelValue?: DataTableItem,
  footer?: boolean
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', selection: DataTableItem): void
  (e: 'update:selection', selection: Array<DataTableItem>): void
}>();

const { columnsComputed } = useColumns(data, columns);
const tableRef = ref();
const clickedRow = ref(modelValue);

function proxyUpdate(tableFn: () => void) {
  tableFn();
  emit('update:selection', tableRef.value?.tableState?.selectedRows ?? []);
}

function updateClickedRow(row: DataTableItem) {
  if(clickedRow.value === row) {
    clickedRow.value = {};
  } else {
    clickedRow.value = row;
  }
  emit('update:modelValue', clickedRow.value);
}

onMounted(() => tableRef.value.selectRows(selection));
watch(() => selection, (newVal) => tableRef.value.selectRows(newVal));
watch(() => modelValue, (newVal) => clickedRow.value = newVal);
</script>
  
<template>
  <VTable
    ref="tableRef" :data="data" :selection-mode="isMultiSelect ? 'multiple' : null" :select-on-click="false"
    hide-sort-icons>
    <template #head="{ allRowsSelected, toggleAllRows }">
      <tr>
        <th v-if="isMultiSelect" class="w-1">
          <span class="table__head-content">
            <Checkbox
              v-if="isMultiSelect" :model-value="allRowsSelected"
              @update:model-value="proxyUpdate(toggleAllRows)" />
          </span>
        </th>
        <template v-for="(column, index) in columnsComputed" :key="`head-${index}`">
          <slot :name="`head-${column.key}`" :column="column">
            <VTh
              v-if="column.sortable" v-slot="{ sortOrder }" :sort-key="column.key"
              :default-sort="column.defaultSort">
              <div>
                <span>{{ column.label }}</span>
                <span>
                  <Icon v-show="sortOrder === 0">
                    <i-ri-arrow-up-down-line />
                  </Icon>
                  <Icon v-show=" sortOrder === 1 ">
                    <i-ri-sort-asc />
                  </Icon>
                  <Icon v-show=" sortOrder === -1 ">
                    <i-ri-sort-desc />
                  </Icon>
                </span>
              </div>
            </VTh>
            <th v-else><span>{{ column.label }}</span></th>
          </slot>
        </template>
      </tr>
    </template>
    <template #body="{ rows }">
      <VTr v-for="(row, index) in rows" :key="`row-${index}`" v-slot="{ isSelected, toggle }" :row="row" :class="{'vt-clicked': row === clickedRow}" @click="updateClickedRow(row)">
        <td v-if="isMultiSelect">
          <span class="table__cell-content">
            <Checkbox :model-value="isSelected === row" @click.stop @update:model-value="proxyUpdate(toggle)" />
          </span>
        </td>
        <td v-for="(column, columnIndex) in columnsComputed" :key="`row-column-${column.key}-${columnIndex}`">
          <slot :name="`cell-${column.key}`" :column="column" :row="row">
            <span class="table__cell-content">
              {{ row[column.key] }}
            </span>
          </slot>
        </td>
      </VTr>
    </template>
    <template v-if="isMultiSelect || footer" #foot="{ allRowsSelected, toggleAllRows }">
      <tr>
        <td colspan="1000">
          <div class="v-table__footer">
            <Checkbox v-if="isMultiSelect" :model-value="allRowsSelected" @change="toggleAllRows" />
            <div class="v-table__footer--actions">
              <slot v-if="footer" name="footer" />
            </div>
          </div>
        </td>
      </tr>
    </template>
  </VTable>
</template>
  
<style>
.v-table {
  width: calc(100% - 1px);

  thead {
    @apply sticky top-px left-0 z-10 bg-white;
    @apply shadow-border-b shadow-dark-20;
    position: -webkit-sticky;

    th {
      @apply bg-white;
      @apply border-dark-20;

      &.v-th {
        div {
          @apply inline-flex flex-row items-center;
          @apply select-none;
          @apply space-x-2;
          @apply text-button font-bold text-black;
        }
      }
    }
  }

  tbody {
    tr {
      @apply text-button text-black;

      &:hover {
        @apply cursor-pointer;
      }

      &:focus {
        @apply outline-none;
      }

      &.vt-clicked {
        @apply font-bold text-primary;
      }

      &.vt-selected {
        .checkbox {
          .checkbox--indicator {
            @apply border-primary;
          }
        }
      }

      td {
        @apply bg-white;
        @apply border-dark-20;
        @apply border;
      }
    }
  }

  tfoot {
    tr {
      @apply sticky bottom-0 left-0 z-10 bg-white;
      @apply shadow-border-t shadow-dark-20;
      position: -webkit-sticky;

      td {
        @apply bg-white;
        @apply py-2 px-3;
      }
    }
  }
}

.v-table__footer {
  @apply flex justify-between;
  @apply w-full;

  &--actions {
    @apply flex flex-row items-center space-x-2;
  }
}

.table__cell-content {
  @apply px-3 py-1;
}
</style>