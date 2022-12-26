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
  id = undefined
} = defineProps<{
  data: Array<DataTableItem>,
  columns?: Array<DataTableColumn>,
  isMultiSelect?: boolean,
  selection?: Array<DataTableItem>,
  modelValue?: DataTableItem,
  footer?: boolean,
  id?: keyof DataTableColumn
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', selection: DataTableItem): void
  (e: 'update:selection', selection: Array<DataTableItem>): void
}>();
const { columnsComputed } = useColumns(data, columns);
const tableRef = ref();
// const clickedRow = ref(modelValue);

function proxyUpdate(tableFn: () => void) {
  tableFn();
  emit('update:selection', tableRef.value?.tableState?.selectedRows ?? []);
}

/** prüft, ob ein Attribut zum Vergleich mitgegeben wurde, ob `id` vorhanden ist oder die beiden Objekte gleich sind */
function check_same(row: DataTableItem): boolean {
  if (id) return row[id] === modelValue[id]
  else if (row.id) return row.id === modelValue.id
  else return row === modelValue
}

// function updateClickedRow(row: DataTableItem) {
//   if(clickedRow.value === row) {
//     clickedRow.value = {};
//   } else {
//     clickedRow.value = row;
//   }
//   emit('update:modelValue', clickedRow.value);
// }

onMounted(() => tableRef.value.selectRows(selection));
watch(() => selection, (newVal) => tableRef.value.selectRows(newVal));
// watch(() => modelValue, (newVal) => clickedRow.value = newVal);
</script>

<template>
  <VTable
    ref="tableRef" :data="data" :selection-mode="isMultiSelect ? 'multiple' : null" :select-on-click="false"
    hide-sort-icons>
    <template #head="{ allRowsSelected, toggleAllRows }">
      <tr>
        <th v-if="isMultiSelect" class="column--checkbox">
          <span class="table--head-content">
            <Checkbox
              v-if="isMultiSelect" :model-value="allRowsSelected"
              @update:model-value="proxyUpdate(toggleAllRows)" />
          </span>
        </th>
        <template v-for="(column, index) in columnsComputed" :key="`head-${index}`">
          <slot :name="`head-${column.key}`" :column="column">
            <VTh
              v-if="column.sortable" v-slot="{ sortOrder }" :sort-key="column.key"
			  :style="`flex-grow: ${column.span};`"
              :default-sort="column.defaultSort">
              <div class="w-full">
                <span class="column--title" :title="column.label">{{ column.label }}</span>
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
            <th v-else :style="`flex-grow: ${column.span};`"><span class="column--title" :title="column.label">{{ column.label }}</span></th>
          </slot>
        </template>
      </tr>
    </template>
    <template #body="{ rows }">
      <slot name="body" :rows="rows">
        <VTr v-for="(row, index) in rows" :key="`row-${index}`" v-slot="{ isSelected, toggle }" :row="row" :class="{'vt-clicked': check_same(row)}" @click="emit('update:modelValue', row)">
          <td v-if="isMultiSelect" class="column--checkbox">
            <span class="table--cell-content">
              <Checkbox :model-value="isSelected === row" @click.stop @update:model-value="proxyUpdate(toggle)" />
            </span>
          </td>
          <td v-for="(column, columnIndex) in columnsComputed" :key="`row-column-${column.key}-${columnIndex}`" :style="`flex-grow: ${column.span};`">
            <slot :name="`cell-${column.key}`" :column="column" :row="row">
              <span class="table--cell-content" :title="row[column.key]">
                {{ row[column.key] }}
              </span>
            </slot>
          </td>
        </VTr>
      </slot>
    </template>
    <template v-if="isMultiSelect || footer" #foot="{ allRowsSelected, toggleAllRows }">
      <tr class="table--footer">
        <td class="table--footer-checkbox column--checkbox">
			<Checkbox v-if="isMultiSelect" :model-value="allRowsSelected" @change="toggleAllRows" />
        </td>
		  <td class="table--footer--actions" :class="{ 'table--footer--has-actions': footer}">
			  <slot v-if="footer" name="footer" />
		  </td>
      </tr>
    </template>
  </VTable>
</template>

<style lang="postcss">
.v-table {
	@apply w-full border-l border-dark-20 bg-white flex flex-col;

	tr {
		@apply flex items-center w-full border-b border-dark-20 relative overflow-hidden;
	}

	th, td {
		@apply flex items-center grow w-0 border-r border-dark-20 leading-none h-full overflow-hidden;
		padding: 0.3rem 0.5rem 0.25rem;
		line-height: 1.25;
	}

	td:not(.column--checkbox) {
		@apply overflow-visible;

		.table--cell-content {
			@apply overflow-hidden;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			word-break: break-all;

		&:hover {
			 @apply block overflow-visible whitespace-nowrap bg-white z-10 relative;
		 }
		}
	}

	thead {
		@apply sticky top-0 left-0 z-10 bg-white flex w-full;
		@apply border-dark-20 border-t shadow shadow-dark-20 text-sm-bold uppercase;
		position: -webkit-sticky;

		tr {
			@apply border-b-0;
		}

		th {
			@apply text-left;
			padding: 0.8rem 0.5rem;

			> div, > div > span {
				@apply w-full;
			}

			&.v-th {
				div {
					@apply inline-flex flex-row items-center;
					@apply select-none cursor-pointer;
					@apply space-x-1;
					@apply text-sm-bold;
				}
			}
		}
	}

	tbody {
		tr {
			@apply bg-white;
			height: 1.72rem;

			&:hover {
				@apply cursor-pointer;
			}

			&:focus {
				@apply outline-none;
			}

			&.vt-clicked {
				@apply font-bold text-primary bg-primary bg-opacity-5;;
			}

			 &:last-child {
				@apply border-b-0;
			  }
		}

		td {
			@apply justify-between;

			.button {
				@apply border;
				font-size: 0.833rem;
				padding: 0.1em 0.7em;
			}
		}

		.text-input-component input {
			@apply border-0 p-0 h-auto w-full;
		}
	}

	tfoot {
		@apply sticky bottom-0 left-0 z-20 bg-white flex w-full;
		@apply border-t border-dark-20;
		position: -webkit-sticky;
		box-shadow: 0 -6px 12px -12px rgba(var(--color-dark-20),0.5);
	}

	.column--checkbox,
	.table--footer-checkbox {
		@apply p-0 flex justify-center items-center;
		flex-grow: 0.25;
		min-width: 2rem;
		max-width: 2rem;
	}

	.table--footer-checkbox {
		@apply border-r-0;
	}

	tr.table--footer {
		@apply flex justify-between;
		@apply w-full;
		min-height: 2rem;

		@apply overflow-visible;
	}

	.table--footer--actions {
		@apply flex flex-row items-center justify-end space-x-1 overflow-visible;

		.button--icon {
			@apply p-2 w-8 h-8;
		}

		.dropdown--button {
			@apply border-0;
		}
	}

	.table--footer--has-actions {
		min-height: 2.7rem;
	}

}

.column--title {
	@apply text-ellipsis overflow-hidden;
	max-width: calc(100% - 1.25rem);
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 1;
	word-break: break-all;
	min-width: 1.6rem;
	line-height: 1.25;
}

.secondary-menu--content .v-table {
	@apply border-l-0;
}

.table--row-indent {
	@apply pl-2;

	&:before {
		@apply block bg-dark-20 w-4;
		content: '';
		height: 1px;
	 }
}

.v-table--complex {
	@apply w-full border;
	--table-border-color: rgba(0,0,0,0.25);
	border-color: var(--table-border-color);

	tr {
		@apply border;
		border-color: var(--table-border-color);
	}

	th, td {
		@apply h-full whitespace-nowrap border px-2;
		line-height: 1.25;
		border-color: var(--table-border-color);
	}

	thead {
		@apply sticky top-0 left-0 z-10;
		@apply shadow shadow-dark-20 text-sm-bold uppercase;
		position: -webkit-sticky;

		tr {
			height: 1.72rem;
		}

		th {
			@apply text-left;
			padding: 0.15rem 0.5rem 0.1rem;
		}
	}

	tbody {
		tr {
			@apply w-full;
			height: 1.72rem;

			&.row--kursdifferenz {
				@apply hidden;
			}

			&:focus {
				@apply outline-none;
			}
		}

		td {
			.button {
				font-size: 0.833rem;
				padding: 0.1em 0.7em;
			}
		}

		.text-input-component input {
			@apply border-0 p-0 h-auto w-full;
		}
	}
}

/*.table--highlight-rows tr:hover,
.table--highlight-rows tr:hover ~ tr:not(.blockung--kursdifferenz) {
	filter: brightness(0.8) saturate(200%);
}

.table--highlight-rows tr:hover ~ tr.blockung--kursdifferenz ~ tr {
	filter: none;
}*/
</style>
