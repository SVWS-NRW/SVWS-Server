<template>
	<div class="w-full">
		<div class="text-headline-md mb-4">Eingabesperre für Spalten bei den Notenübersichten</div>
		<div class="w-64 mb-4">
			<ui-select label="Gruppierung" :manager="selectManagerGruppierung" v-model="manager().gruppierung" :removable="false" />
		</div>
		<ui-table-grid :header-count="2" :manager="() => gridManager" class="min-w-full" name="Matrix Noteneingabe">
			<template #header="{ i }">
				<template v-if="i === 1">
					<template v-for="col of gridManager.cols.values()" :key="col.name">
						<th v-if="gridManager.isColVisible(col.name)" :title="col.name">
							<div v-if="col.name === 'Gruppe'"> {{ manager().gruppierung }} </div>
							<div v-else>
								{{ col.name }}
								<template v-if="col.name === 'Teilnoten'">
									<span :class="[manager().zeigeTeilnoten ? 'i-ri-arrow-right-s-line' : 'i-ri-arrow-left-s-line', 'icon-ui-brand icon cursor-pointer']"
										@click.stop="manager().toggleZeigeTeilnoten()" />
								</template>
							</div>
						</th>
					</template>
				</template>
				<template v-else-if="manager().gruppierung === 'Keine'">
					<template v-for="col of gridManager.cols.values()" :key="col.name">
						<td v-if="gridManager.isColVisible(col.name)" class="bg-ui-75">
							<div v-if="col.name === 'Klasse'"> alle </div>
							<template v-if="(col.name === 'Eingabe von')">
								<svws-ui-text-input type="datetime-local" headless placeholder="Eingabe von" :model-value="manager().zeileAlleKlassen().tsEingabeAb"
									@change="datum => manager().zeileAlleKlassen().tsEingabeAb = datum" />
							<!-- TODO Änderung an den Manager Weitergeben und in die DB zurückschreiben -->
							</template>
							<template v-else-if="(col.name === 'Eingabe bis')">
								<svws-ui-text-input type="datetime-local" headless placeholder="Eingabe bis" :model-value="manager().zeileAlleKlassen().tsEingabeBis"
									@change="datum => manager().zeileAlleKlassen().tsEingabeBis = datum" />
							<!-- TODO Änderung an den Manager Weitergeben und in die DB zurückschreiben -->
							</template>
							<template v-else-if="(col.name === 'FS klassenweise')">
							<!-- TODO Hier eine Checkbox ergänzen -->
							</template>
							<template v-else-if="manager().istSperrbar(manager().zeileAlleKlassen(), col.name)">
								<svws-ui-checkbox :model-value="!manager().hatSperrung(manager().zeileAlleKlassen(), col.name)"
									:indeterminate="manager().hatTeilsperrung(manager().zeileAlleKlassen(), col.name)"
									@update:model-value="manager().toggleSperrung(manager().zeileAlleKlassen(), col.name)" />
							</template>
						</td>
					</template>
				</template>
			</template>
			<template #default="{ row }">
				<template v-for="col of gridManager.cols.values()" :key="col.name">
					<td v-if="gridManager.isColVisible(col.name)"
						:class="[
							manager().istGruppe(row) || manager().istTeilleistung(row, col.name) ? 'bg-ui-75' : '',
							(col.name === 'Gruppe') ? 'text-left' : ''
						]">
						<template v-if="(col.name === 'Gruppe') && manager().istGruppe(row)">
							<div @click.stop="manager().toggleZeigeGruppenKlassen(row)" class="cursor-pointer">
								<span class="icon-ui-brand icon align-middle"
									:class="[manager().zeigeGruppenKlassen(row) ? 'i-ri-arrow-down-s-line' : 'i-ri-arrow-right-s-line']" />
								{{ manager().getGruppenBezeichnung(row) }}
							</div>
						</template>
						<template v-else-if="(col.name === 'Klasse') && !manager().istGruppe(row)">
							{{ manager().getKlassenBezeichnung(row) }}
						</template>
						<template v-else-if="(col.name === 'Eingabe von')">
							<svws-ui-text-input type="datetime-local" headless placeholder="Eingabe von" :model-value="row.tsEingabeAb"
								@change="datum => row.tsEingabeAb = datum" />
							<!-- TODO Änderung an den Manager Weitergeben und in die DB zurückschreiben -->
						</template>
						<template v-else-if="(col.name === 'Eingabe bis')">
							<svws-ui-text-input type="datetime-local" headless placeholder="Eingabe bis" :model-value="row.tsEingabeBis"
								@change="datum => row.tsEingabeBis = datum" />
							<!-- TODO Änderung an den Manager Weitergeben und in die DB zurückschreiben -->
						</template>
						<template v-else-if="(col.name === 'FS klassenweise')">
							<!-- TODO Hier eine Checkbox ergänzen -->
						</template>
						<template v-else-if="manager().istSperrbar(row, col.name)">
							<svws-ui-checkbox :model-value="!manager().hatSperrung(row, col.name)"
								:indeterminate="manager().hatTeilsperrung(row, col.name)"
								@update:model-value="manager().toggleSperrung(row, col.name)" />
						</template>
					</td>
				</template>
			</template>
		</ui-table-grid>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { List } from '@core';
	import { GridManager, SelectManager } from '@ui';
	import type { NotenmodulConfigManagerSperrungen, NotenmodulConfigManagerSperrungenZeile } from "~/router/apps/notenmodul/NotenmodulConfigManagerSperrungen";

	const props = defineProps<{
		manager: () => NotenmodulConfigManagerSperrungen;
	}>();

	const selectManagerGruppierung = computed(() => new SelectManager({ options: props.manager().gruppierungen }));

	const gridManager = new GridManager<string, NotenmodulConfigManagerSperrungenZeile, List<NotenmodulConfigManagerSperrungenZeile>>({
		daten: computed(() => props.manager().zeilen()),
		getRowKey: row => `${typeof row === 'string' ? row : row.id}`,
		columns: computed(() => props.manager().columns).value,
		colsVisible: computed<Map<string, boolean | null>>({
			get: () => props.manager().columnsVisible,
			set: (value) => {},
		}),
	});

</script>
