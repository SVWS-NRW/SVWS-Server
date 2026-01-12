<template>
	<div class="w-full">
		<div class="text-headline-md mb-4">Freigabe der Noteneingabe</div>
		<div class="w-full pl-4">
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
							<td v-if="gridManager.isColVisible(col.name)" class="bg-ui-75 content-center">
								<div v-if="col.name === 'Klasse'"> alle </div>
								<template v-if="(col.name === 'Eingabe von')">
									<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="ab" :row="() => manager().zeileAlleKlassen()">
										<div @click="openModal" class="border border-ui/0 hover:border-ui p-0.5 rounded-md cursor-pointer">
											<span v-if="manager().istNoteneingabeZeitlichGesperrt(manager().zeileAlleKlassen().tsEingabeAb, true).value"
												class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabebeginn liegt in der Zukunft, alle Eingaben sind gesperrt." />
											<span v-if="manager().istNoteneingabeZeitlichUnmoeglich(manager().zeileAlleKlassen().tsEingabeAb, manager().zeileAlleKlassen().tsEingabeBis)"
												class="icon-ui-danger icon i-ri-alert-line align-middle" title="Der Eingabebeginn liegt nach dem Eingabeende" />
											{{ printDate(manager().zeileAlleKlassen().tsEingabeAb) }}
										</div>
									</notenmodul-konfiguration-sperrungen-datum-modal>
								</template>
								<template v-else-if="(col.name === 'Eingabe bis')">
									<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="bis" :row="() => manager().zeileAlleKlassen()">
										<div @click="openModal" class="border border-ui/0 hover:border-ui p-0.5 rounded-md cursor-pointer">
											<span v-if="manager().istNoteneingabeZeitlichGesperrt(manager().zeileAlleKlassen().tsEingabeBis, false).value"
												class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabeende liegt bereits in der Vergangenheit, alle Eingaben sind gesperrt." />
											{{ printDate(manager().zeileAlleKlassen().tsEingabeBis) }}
										</div>
									</notenmodul-konfiguration-sperrungen-datum-modal>
								</template>
								<template v-else-if="(col.name === 'FS klassenweise')">
									<svws-ui-checkbox :model-value="!manager().hatFehlstundeneingabeKlassenweise(manager().zeileAlleKlassen())"
										:indeterminate="manager().hatFehlstundeneingabeKlassenweiseTeilweise(manager().zeileAlleKlassen())"
										@update:model-value="manager().toggleFehlstundeneingabeKlassenweise(manager().zeileAlleKlassen())" />
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
								(col.name === 'Gruppe') ? 'text-left' : '', 'content-center'
							]">
							<template v-if="(col.name === 'Gruppe') && manager().istGruppe(row)">
								<div @click.stop="manager().toggleZeigeGruppenKlassen(row)" class="cursor-pointer">
									<span class="icon-ui-brand icon"
										:class="[manager().zeigeGruppenKlassen(row) ? 'i-ri-arrow-down-s-line' : 'i-ri-arrow-right-s-line']" />
									{{ manager().getGruppenBezeichnung(row) }}
								</div>
							</template>
							<template v-else-if="(col.name === 'Klasse') && !manager().istGruppe(row)">
								{{ manager().getKlassenBezeichnung(row) }}
							</template>
							<template v-else-if="(col.name === 'Eingabe von')">
								<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="ab" :row="() => row">
									<div @click="openModal" class="border border-ui/0 hover:border-ui rounded-md cursor-pointer">
										<span v-if="manager().istNoteneingabeZeitlichGesperrt(row.tsEingabeAb, true).value"
											class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabebeginn liegt in der Zukunft, alle Eingaben sind gesperrt." />
										<span v-if="manager().istNoteneingabeZeitlichUnmoeglich(row.tsEingabeAb, row.tsEingabeBis)"
											class="icon-ui-danger icon i-ri-alert-line align-middle" title="Der Eingabebeginn liegt nach dem Eingabeende" />
										{{ printDate(row.tsEingabeAb) }}
									</div>
								</notenmodul-konfiguration-sperrungen-datum-modal>
							</template>
							<template v-else-if="(col.name === 'Eingabe bis')">
								<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="bis" :row="() => row">
									<div @click="openModal" class="border border-ui/0 hover:border-ui rounded-md cursor-pointer">
										<span v-if="manager().istNoteneingabeZeitlichGesperrt(row.tsEingabeBis, false).value"
											class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabeende liegt bereits in der Vergangenheit, alle Eingaben sind gesperrt." />
										{{ printDate(row.tsEingabeBis) }}
									</div>
								</notenmodul-konfiguration-sperrungen-datum-modal>
							</template>
							<template v-else-if="(col.name === 'FS klassenweise')">
								<svws-ui-checkbox :model-value="!manager().hatFehlstundeneingabeKlassenweise(row)"
									:indeterminate="manager().hatFehlstundeneingabeKlassenweiseTeilweise(row)"
									@update:model-value="manager().toggleFehlstundeneingabeKlassenweise(row)" />
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
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { GridManager, SelectManager } from '@ui';
	import type { List } from '@core';
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

	function printDate(isoDateTime: string | null) {
		return (isoDateTime === null) ? '‒'
			: new Date(isoDateTime).toLocaleDateString('de-DE', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
	}

</script>
