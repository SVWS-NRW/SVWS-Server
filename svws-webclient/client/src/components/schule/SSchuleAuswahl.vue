<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span class="line-clamp-2">Schule</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
				<table class="svws-ui-table" role="table" aria-label="Tabelle">
					<thead class="svws-ui-thead cursor-default mb-1" role="rowgroup" aria-label="Tabellenkopf">
						<tr class="svws-ui-tr" role="row" @click="toggleSchulbezogen">
							<td class="svws-ui-td" role="columnheader">Schulbezogene Kataloge</td>
						</tr>
					</thead>
					<tbody v-if="!collapsedSchulbezogen" class="svws-ui-tbody" role="rowgroup" aria-label="Tabelleninhalt">
						<template v-for="name of listSchulbezogen" :key="name">
							<tr class="svws-ui-tr" role="row">
								<td class="svws-ui-td border-none ml-4" role="cell">
									<svws-ui-menu-item @click="setChild(mapChildren.get(name) ?? props.child)">
										<template #label><span>{{ mapChildren.get(name)?.text ?? '---' }}</span></template>
									</svws-ui-menu-item>
								</td>
							</tr>
						</template>
					</tbody>
				</table>
				<table class="svws-ui-table" role="table" aria-label="Tabelle">
					<thead class="svws-ui-thead cursor-default mb-1" role="rowgroup" aria-label="Tabellenkopf">
						<tr class="svws-ui-tr" role="row" @click="toggleAllgemein">
							<td class="svws-ui-td" role="columnheader">Allgemeine Kataloge</td>
						</tr>
					</thead>
					<tbody v-if="!collapsedAllgemein" class="svws-ui-tbody" role="rowgroup" aria-label="Tabelleninhalt">
						<template v-for="name of listAllgemein" :key="name">
							<tr class="svws-ui-tr" role="row">
								<td class="svws-ui-td border-none ml-4" role="cell">
									<svws-ui-menu-item @click="setChild(mapChildren.get(name) ?? props.child)">
										<template #label><span>{{ mapChildren.get(name)?.text ?? '---' }}</span></template>
									</svws-ui-menu-item>
								</td>
							</tr>
						</template>
					</tbody>
				</table>
				<table class="svws-ui-table" role="table" aria-label="Tabelle">
					<thead class="svws-ui-thead cursor-default mb-1" role="rowgroup" aria-label="Tabellenkopf">
						<tr class="svws-ui-tr" role="row" @click="toggleDatenaustausch">
							<td class="svws-ui-td" role="columnheader">Datenaustausch</td>
						</tr>
					</thead>
					<tbody v-if="!collapsedDatenaustausch" class="svws-ui-tbody" role="rowgroup" aria-label="Tabelleninhalt">
						<template v-for="name of listDatenaustausch" :key="name">
							<tr class="svws-ui-tr" role="row">
								<td class="svws-ui-td border-none ml-4" role="cell">
									<svws-ui-menu-item @click="setChild(mapChildren.get(name) ?? props.child)">
										<template #label><span>{{ mapChildren.get(name)?.text ?? '---' }}</span></template>
									</svws-ui-menu-item>
								</td>
							</tr>
						</template>
					</tbody>
				</table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { SchuleAuswahlProps } from './SSchuleAuswahlProps';
	import type { AuswahlChildData } from '../AuswahlChildData';

	const props = defineProps<SchuleAuswahlProps>();

	const listSchulbezogen = [ "schule.betriebe", "schule.einwilligungsarten", "schule.faecher", "schule.foerderschwerpunkte", "schule.jahrgaenge", "schule.vermerkarten" ];
	const listAllgemein = [ "schule.religionen", "schule.schulen" ];
	const listDatenaustausch = [ "schule.datenaustausch.enm", "schule.datenaustausch.laufbahnplanung", "schule.datenaustausch.schulbewerbung", "schule.datenaustausch.wenom", "schule.datenaustausch.kurs42", "schule.datenaustausch.untis" ];

	const mapChildren = computed<Map<string, AuswahlChildData>>(() => {
		const result = new Map<string, AuswahlChildData>();
		for (const child of props.children)
			result.set(child.name, child);
		return result;
	});

	const collapsedSchulbezogen = ref(false);
	const collapsedAllgemein = ref(false);
	const collapsedDatenaustausch = ref(false);

	function toggleSchulbezogen() {
		collapsedSchulbezogen.value = !collapsedSchulbezogen.value;
	}

	function toggleAllgemein() {
		collapsedAllgemein.value = !collapsedAllgemein.value;
	}

	function toggleDatenaustausch() {
		collapsedDatenaustausch.value = !collapsedDatenaustausch.value;
	}

</script>
