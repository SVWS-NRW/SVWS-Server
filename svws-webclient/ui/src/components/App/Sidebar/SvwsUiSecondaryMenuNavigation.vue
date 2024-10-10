<template>
	<div class="secondary-menu--navigation">
		<template v-for="tabgroup of tabManager().tabgroups" :key="tabgroup">
			<table class="svws-ui-table" role="table" aria-label="Tabelle">
				<thead class="svws-ui-thead cursor-pointer mb-1" role="rowgroup" aria-label="Tabellenkopf">
					<tr class="svws-ui-tr" role="row" @click="toggle(tabgroup)">
						<td class="svws-ui-td" role="columnheader">
							<span v-if="isCollapsed.has(tabgroup)" class="icon i-ri-arrow-right-s-line" />
							<span v-else class="icon i-ri-arrow-down-s-line" />
							{{ tabgroup }}
						</td>
					</tr>
				</thead>
				<tbody v-if="!isCollapsed.has(tabgroup)" class="svws-ui-tbody" role="rowgroup" aria-label="Tabelleninhalt">
					<template v-for="tab of tabManager().getTabsOfGroup(tabgroup)" :key="tab.name">
						<tr class="svws-ui-tr" role="row">
							<td class="svws-ui-td border-none ml-4" role="cell">
								<svws-ui-menu-item @click="setTab(tab)" :active="isCurrent(tab)" :focus="isCurrent(tab)">
									<template #label><span>{{ tab.text }}</span></template>
								</svws-ui-menu-item>
							</td>
						</tr>
					</template>
				</tbody>
			</table>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { TabData } from '../TabData';
	import type { TabManager } from '../TabManager';

	const props = defineProps<{
		tabManager: () => TabManager;
	}>();

	// Eine Map, welche für die Tab-Gruppen festlegt, ob diese zusammengeklapt sind oder nicht.
	const isCollapsed = ref<Set<string>>(new Set());

	function toggle(tabgroup: string) {
		if (isCollapsed.value.has(tabgroup)) {
			isCollapsed.value.delete(tabgroup);
		} else {
			isCollapsed.value.add(tabgroup);
		}
	}

	function isCurrent(tab: TabData) : boolean {
		return (tab.name === props.tabManager().tab.name);
	}

	async function setTab(tab: TabData) {
		if (!isCurrent(tab))
			await props.tabManager().setTab(tab)
	}

</script>
