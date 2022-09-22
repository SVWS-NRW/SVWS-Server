<template>
	<table class="table">
		<thead class="table--header">
			<tr>
				<td
					v-if="multiSelect"
					class="table--cell table--cell-padded w-1"
				></td>
				<td
					v-for="col in cols"
					:key="col.id"
					class="table--cell table--cell-padded"
					:width="col.width"
					@click="changeSort(col)"
				>
					<div class="table--header-col">
						<span class="table--header-col--text">
							{{ col.title }}
						</span>
						<span v-if="col.sortable">
							<Icon v-show="col.id !== sorting.column"
								><i-ri-arrow-up-down-line
							/></Icon>
							<Icon
								v-show="
									sorting.asc && col.id === sorting.column
								"
								><i-ri-sort-asc
							/></Icon>
							<Icon
								v-show="
									!sorting.asc && col.id === sorting.column
								"
								><i-ri-sort-desc
							/></Icon>
						</span>
					</div>
				</td>
				<td
					v-if="actions && actions.length > 0"
					class="table--cell table--cell-padded"
				></td>
			</tr>
		</thead>
		<tbody>
			<template v-for="item in items" :key="item.data.id">
				<tr
					:id="'row_' + (items.indexOf(item) + 1)"
					:tabindex="items.indexOf(item) + 1"
					class="table--row"
					:class="{
						'table--row-selected':
							current.data.id === item.data.id
					}"
					@keydown.space="onKeyDownSpace"
					@keydown.down="onKeyDown"
					@keydown.up="onKeyUp"
				>
					<td
						v-if="multiSelect"
						class="table--cell table--cell-padded"
						:class="{ 'table--border': border }"
					>
						<Checkbox
							:model-value="item.selected"
							@change="toggleSelect(item)"
						/>
					</td>
					<td
						v-for="col in cols"
						:key="item.data[col.id]"
						class="table--cell table--cell-padded"
						:class="{ 'table--border': border }"
						:width="col.width"
						@click="mousePressed(item)"
					>
						<div class="flex">
							<template
								v-if="
									current === item &&
									arrows &&
									col === cols[0]
								"
							>
								<i-ri-arrow-down-s-line
									v-if="slot_open"
									class="inline-flex"
									@click="open_slot"
								></i-ri-arrow-down-s-line>
								<i-ri-arrow-right-s-line
									v-else
									class="inline-flex"
									@click="open_slot"
								></i-ri-arrow-right-s-line>
							</template>
							{{ item.data[col.id] }}
						</div>
					</td>
					<td
						v-if="actions && actions.length > 0"
						class="table--cell"
						:class="{ 'table--border': border }"
					>
						<Popover
							:hover="false"
							placement="left-end"
							:disable-click-away="false"
						>
							<template #trigger>
								<button class="table--action-button">
									<Icon
										><i-ri-more--2-fill
									/></Icon>
								</button>
							</template>
							<template #content>
								<div class="table--action-items">
									<div
										v-for="action in actions"
										:key="action"
									>
										<Button
											class="table--action-item"
											type="transparent"
											@click="
												$emit('action', [
													action.action,
													item
												])
											"
											>{{ action.label }}</Button
										>
									</div>
								</div>
							</template>
						</Popover>
					</td>
				</tr>
				<slot v-if="current === item && slot_open" name="tr"> </slot>
			</template>
		</tbody>
		<tfoot>
			<tr
				v-if="multiSelect || footer"
				class="table--footer-wrapper"
			>
				<td class="table--footer-row" colspan="1000">
					<div class="table--footer">
						<Checkbox
							v-if="multiSelect"
							:model-value="selectedItems === items"
							@change="selectAll()"
						/>
						<div class="table--footer--actions">
							<slot v-if="footer" name="footer" />
						</div>
					</div>
				</td>
			</tr>
		</tfoot>
	</table>
</template>

<script lang="ts">
	import { defineComponent } from "vue";

	export default defineComponent({
		name: "SvwsUiTable",
		props: {
			arrows: {
				type: Boolean,
				default: false
			},
			border: {
				type: Boolean,
				default: true
			},
			multiSelect: {
				type: Boolean,
				default: false
			},
			cols: {
				type: Array,
				default: () => []
			},
			rows: {
				type: Array,
				default: () => []
			},
			actions: {
				type: Array,
				default: () => []
			},
			selected: {
				type: Object
			},
			footer: {
				type: Boolean,
				default: false
			}
		},
		emits: [
			"update:sort",
			"update:asc",
			"update:selected",
			"action",
			"update:selectedItems"
		],
		data() {
			return {
				sort: "",
				asc: true,
				sorting: {
					column: "",
					asc: false
				},
				current: {
					data: {},
					selected: false
				},
				items: {
					data: {},
					selected: false
				},
				selectedItems: [],
				slot_open: false,
			};
		},
		computed: {
		},
		watch: {
			sort() {
				this.sorting.column = this.sort;
			},
			asc() {
				this.sorting.asc = this.asc;
			},
			items: {
				deep: true,
				handler() {
					if (
						this.current.data.id === undefined &&
						this.items[0] !== undefined
					) {
						this.changeCurrent(this.items[0]);
					}
				}
			},
			rows: {
				deep: true,
				handler() {
					this.updateData();
				}
			},
			sorting: {
				deep: true,
				handler() {
					this.doSort();
				}
			}
		},
		created() {
			this.updateData();
		},
		mounted() {
			document.getElementById("row_1")?.focus();
		},
		methods: {
			open_slot() {
				this.slot_open = !this.slot_open;
			},
			changeSort(column) {
				if (column.sortable) {
					if (this.sorting.column === column.id) {
						this.sorting.asc = !this.sorting.asc;
						this.$emit("update:asc", this.sorting.asc);
					} else {
						this.sorting.column = column.id;
						this.sorting.asc = true;
						this.$emit("update:sort", this.sorting.column);
						this.$emit("update:asc", this.sorting.asc);
					}
				}
			},
			doSort() {
				if (this.sorting.column) {
					if (this.sorting.asc) {
						this.items.sort((a, b) =>
							b.data[this.sorting.column]
								.toString()
								.localeCompare(
									a.data[this.sorting.column].toString(),
									"de-DE"
								)
						);
					} else {
						this.items.sort((a, b) =>
							a.data[this.sorting.column]
								.toString()
								.localeCompare(
									b.data[this.sorting.column].toString(),
									"de-DE"
								)
						);
					}
				}
			},
			selectAll() {
				if (
					this.selectedItems.length >= 0 &&
					this.selectedItems.length < this.items.length
				) {
					this.items.forEach(item => (item.selected = true));
					this.selectedItems = this.items;
				} else if (this.selectedItems.length === this.items.length) {
					this.items.forEach(item => (item.selected = false));
					this.selectedItems = [];
				}
				this.$emit("update:selectedItems", this.selectedItems);
			},
			mousePressed(item) {
				this.changeCurrent(item);
			},
			toggleSelect(item) {
				item.selected = !item.selected;
				if (item.selected) {
					this.selectedItems.push(item);
				} else {
					this.selectedItems = this.removeFromArray(
						this.selectedItems,
						item
					);
				}
				this.$emit("update:selectedItems", this.selectedItems);
			},
			updateData() {
				this.items = this.rows.map(item => ({
					data: item,
					selected: false
				}));
			},
			removeFromArray(arr, val) {
				return arr.filter(function (ele) {
					return ele != val;
				});
			},
			changeCurrent(item) {
				if (!item) {
					return undefined;
				}
				this.current = item;
				this.$emit("update:selected", item.data);
				return item.data;
			},
			/* KEYBOARD NAVIGATION */
			onKeyDown() {
				event?.preventDefault();
				let element = null;
				const index = this.items.indexOf(this.current);
				if (index + 1 === this.items.length) {
					this.changeCurrent(this.items[0]);
					element = document.getElementById("row_1");
				} else {
					this.changeCurrent(this.items[index + 1]);
					element = document.getElementById("row_" + (index + 1));
				}
				this.focusAndScroll(element);
			},
			onKeyDownSpace() {
				event?.preventDefault();
				if (this.multiSelect) {
					this.toggleSelect(this.current);
				}
			},
			onKeyUp() {
				event?.preventDefault();
				let element = null;
				const index = this.items.indexOf(this.current);
				if (index === 0) {
					this.changeCurrent(this.items[this.items.length - 1]);
					element = document.getElementById(
						"row_" + this.items.length
					);
				} else {
					this.changeCurrent(this.items[index - 1]);
					element = document.getElementById("row_" + index);
				}
				this.focusAndScroll(element);
			},
			focusAndScroll(element) {
				element.focus();
				element.scrollIntoView({
					behavior: "smooth",
					block: "center",
					inline: "nearest"
				});
			}
		}
	});
</script>

<style>
	.table--header {
		@apply sticky top-px left-0 z-10 bg-white;
		@apply shadow-border-b shadow-dark-20;
		position: -webkit-sticky;
	}

	.table--header-col {
		@apply inline-flex flex-row items-center;
		@apply select-none;
		@apply space-x-2;
		@apply text-button font-bold text-black;
	}

	.table--row {
		@apply text-button text-black;
	}

	.table--row:hover {
		@apply cursor-pointer;
	}

	.table--row:focus {
		@apply outline-none;
	}

	.table--row-selected {
		@apply font-bold text-primary;
	}

	.table--row-selected .checkbox {
		@apply font-normal;
	}

	.table--row-selected
		.checkbox
		.checkbox--indicator {
		@apply border-primary;
	}

	.table--cell {
		@apply bg-white;
		@apply border-dark-20;
	}

	.table--border {
		@apply border;
	}

	.table--cell-padded {
		@apply px-3 py-1;
	}

	.table {
		width: calc(100% - 1px);
	}

	.table--action-button {
		@apply h-6 w-6;
	}

	.table--action-items {
		@apply bg-white;
		@apply flex flex-col;
		@apply px-2 py-1;
		@apply ring-1;
		@apply ring-black ring-opacity-5;
		@apply rounded-md;
		@apply shadow-lg;
		@apply w-48;
	}

	.table--action-item {
		@apply block;
		@apply w-full;
	}

	.table--action-items:focus {
		@apply outline-none;
	}

	.table--action-button:focus {
		@apply outline-none ring-2 ring-inset ring-primary;
	}

	.table--footer {
		@apply flex justify-between;
		@apply w-full;
	}

	.table--footer-wrapper {
		@apply sticky bottom-0 left-0 z-10  bg-white;
		@apply shadow-border-t shadow-dark-20;
		position: -webkit-sticky;
	}

	.table--footer-row {
		@apply bg-white;
		@apply py-2 px-3;
	}

	.table--footer--actions {
		@apply flex flex-row items-center space-x-2;
	}
</style>
