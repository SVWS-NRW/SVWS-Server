<template>
	<div class="w-full">
		<div class="text-headline-md mb-4">Freigabe der Noteneingabe</div>
		<div class="w-full pl-4">
			<div class="w-64 mb-4">
				<ui-select label="Gruppierung" :manager="selectManagerGruppierung" v-model="manager().gruppierung" :removable="false" />
			</div>
			<ui-table-grid :header-count="3" :manager="() => gridManager" class="min-w-full" name="Matrix Noteneingabe">
				<template #header="{ i }">
					<template v-if="i === 1">
						<th class="ui-divider" :class="{ 'col-span-2': manager().gruppierung !== 'Keine' }" />
						<th class="col-span-2 ui-divider">
							<svws-ui-tooltip autosize>
								Eingabezeitraum
								<template #content>
									Der Eingabezeitraum für die Eingabe von Leistungsdaten durch den Fachlehrer <br>
									Sind keine Werte angegeben, so ist der Eingabezeitraum uneingeschränkt
								</template>
							</svws-ui-tooltip>
						</th>
						<th class="col-span-3 ui-divider">
							<svws-ui-tooltip autosize>
								Klassenleitung
								<template #content>
									Erweiterte Berechtigungen / Aufgaben für Klassenleitungen
								</template>
							</svws-ui-tooltip>
						</th>
						<th :style="`grid-column: span ${countVisibleColumns - ((manager().gruppierung === 'Keine') ? 6 : 7)} / span ${countVisibleColumns - ((manager().gruppierung === 'Keine') ? 6 : 7)}`">
							<svws-ui-tooltip autosize>
								Berechtigungen
								<template #content>
									Regelt, welche Eingaben für Leistungsdaten bei der Klasse erlaubt bzw. gesperrt sind
								</template>
							</svws-ui-tooltip>
						</th>
					</template>
					<template v-else-if="i === 2">
						<template v-for="col of gridManager.cols.values()" :key="col.kuerzel">
							<th v-if="gridManager.isColVisible(col.name)"
								:class="{ 'ui-divider': col.kuerzel === 'Klasse' || col.kuerzel === 'Eingabe bis' || col.kuerzel === 'FS Kl' || ((col.kuerzel === 'Teilnoten') && manager().zeigeTeilnoten) }">
								<div v-if="col.kuerzel === 'Gruppe'"> {{ manager().gruppierung }} </div>
								<div v-else>
									<svws-ui-tooltip v-if="col.name !== col.kuerzel" autosize>
										{{ col.kuerzel }}
										<template #content>
											{{ col.name }}
										</template>
									</svws-ui-tooltip>
									<div v-else>{{ col.kuerzel }}</div>
									<template v-if="col.kuerzel === 'Teilnoten'">
										<span :class="[manager().zeigeTeilnoten ? 'i-ri-arrow-right-s-line' : 'i-ri-arrow-left-s-line', 'icon-ui-brand icon cursor-pointer']"
											@click.stop="manager().toggleZeigeTeilnoten()" />
									</template>
								</div>
							</th>
						</template>
					</template>
					<template v-else-if="manager().gruppierung === 'Keine'">
						<template v-for="col of gridManager.cols.values()" :key="col.kuerzel">
							<td v-if="gridManager.isColVisible(col.name)" class="bg-ui-75 content-center"
								:class="{ 'ui-divider': col.kuerzel === 'Klasse' || col.kuerzel === 'Eingabe bis' || col.kuerzel === 'FS Kl' || ((col.kuerzel === 'Teilnoten') && manager().zeigeTeilnoten) }">
								<div v-if="col.kuerzel === 'Klasse'"> alle </div>
								<template v-if="(col.kuerzel === 'Eingabe von')">
									<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="Beginn" :row="() => manager().zeileAlleKlassen()">
										<div @click="openModal" class="border border-ui/0 hover:border-ui p-0.5 rounded-md cursor-pointer">
											<span v-if="manager().istNoteneingabeZeitlichGesperrt(manager().zeileAlleKlassen().tsEingabeAb, true).value"
												class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabebeginn liegt in der Zukunft, alle Eingaben sind gesperrt." />
											<span v-if="manager().istNoteneingabeZeitlichUnmoeglich(manager().zeileAlleKlassen().tsEingabeAb, manager().zeileAlleKlassen().tsEingabeBis)"
												class="icon-ui-danger icon i-ri-alert-line align-middle" title="Der Eingabebeginn liegt nach dem Eingabeende" />
											{{ printDate(manager().zeileAlleKlassen().tsEingabeAb) }}
										</div>
									</notenmodul-konfiguration-sperrungen-datum-modal>
								</template>
								<template v-else-if="(col.kuerzel === 'Eingabe bis')">
									<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="Ende" :row="() => manager().zeileAlleKlassen()">
										<div @click="openModal" class="border border-ui/0 hover:border-ui p-0.5 rounded-md cursor-pointer">
											<span v-if="manager().istNoteneingabeZeitlichGesperrt(manager().zeileAlleKlassen().tsEingabeBis, false).value"
												class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabeende liegt bereits in der Vergangenheit, alle Eingaben sind gesperrt." />
											{{ printDate(manager().zeileAlleKlassen().tsEingabeBis) }}
										</div>
									</notenmodul-konfiguration-sperrungen-datum-modal>
								</template>
								<template v-else-if="(col.kuerzel === 'ggf. verlängerte Eingabe')">
									<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="EndeKL" :row="() => manager().zeileAlleKlassen()">
										<div @click="openModal" class="border border-ui/0 hover:border-ui p-0.5 rounded-md cursor-pointer">
											<span v-if="manager().istNoteneingabeZeitlichGesperrt(manager().zeileAlleKlassen().tsEingabeBisKlassenleitung, false).value"
												class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabeende für die Klassenleitung liegt bereits in der Vergangenheit." />
											{{ printDate(manager().zeileAlleKlassen().tsEingabeBisKlassenleitung) }}
										</div>
									</notenmodul-konfiguration-sperrungen-datum-modal>
								</template>
								<template v-else-if="(col.kuerzel === 'alle Noten')">
									<svws-ui-checkbox :model-value="manager().hatNoteneingabeDurchKlassenleitung(manager().zeileAlleKlassen())"
										:indeterminate="manager().hatNoteneingabeDurchKlassenleitungTeilweise(manager().zeileAlleKlassen())"
										@update:model-value="manager().toggleNoteneingabeDurchKlassenleitung(manager().zeileAlleKlassen())" />
								</template>
								<template v-else-if="(col.kuerzel === 'FS Kl')">
									<svws-ui-checkbox :model-value="!manager().hatFehlstundeneingabeKlassenweise(manager().zeileAlleKlassen())"
										:indeterminate="manager().hatFehlstundeneingabeKlassenweiseTeilweise(manager().zeileAlleKlassen())"
										@update:model-value="manager().toggleFehlstundeneingabeKlassenweise(manager().zeileAlleKlassen())" />
								</template>
								<template v-else-if="col.kuerzel === 'Teilnoten'">
									<svws-ui-checkbox :model-value="!manager().hatSperrungTeilnoten(manager().zeileAlleKlassen())"
										:indeterminate="manager().hatTeilsperrungTeilnoten(manager().zeileAlleKlassen())"
										@update:model-value="manager().toggleSperrungTeilnoten(manager().zeileAlleKlassen())" />
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
					<template v-for="col of gridManager.cols.values()" :key="col.kuerzel">
						<td v-if="gridManager.isColVisible(col.name)" class="content-center" :class="{
							'ui-divider': col.kuerzel === 'Klasse' || col.kuerzel === 'Eingabe bis' || col.kuerzel === 'FS Kl' || ((col.kuerzel === 'Teilnoten') && manager().zeigeTeilnoten),
							'bg-ui-75': manager().istGruppe(row) || manager().istTeilleistung(row, col.name),
							'text-left': (col.kuerzel === 'Gruppe')
						}">
							<template v-if="(col.kuerzel === 'Gruppe') && manager().istGruppe(row)">
								<div @click.stop="manager().toggleZeigeGruppenKlassen(row)" class="cursor-pointer flex items-center">
									<span class="icon-ui-brand icon"
										:class="[manager().zeigeGruppenKlassen(row) ? 'i-ri-arrow-down-s-line' : 'i-ri-arrow-right-s-line']" />
									{{ manager().getGruppenBezeichnung(row) }}
								</div>
							</template>
							<template v-else-if="(col.kuerzel === 'Klasse') && !manager().istGruppe(row)">
								{{ manager().getKlassenBezeichnung(row) }}
							</template>
							<template v-else-if="(col.kuerzel === 'Eingabe von')">
								<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="Beginn" :row="() => row">
									<div @click="openModal" class="border border-ui/0 hover:border-ui rounded-md cursor-pointer">
										<span v-if="manager().istNoteneingabeZeitlichGesperrt(row.tsEingabeAb, true).value"
											class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabebeginn liegt in der Zukunft, alle Eingaben sind gesperrt." />
										<span v-if="manager().istNoteneingabeZeitlichUnmoeglich(row.tsEingabeAb, row.tsEingabeBis)"
											class="icon-ui-danger icon i-ri-alert-line align-middle" title="Der Eingabebeginn liegt nach dem Eingabeende" />
										{{ printDate(row.tsEingabeAb) }}
									</div>
								</notenmodul-konfiguration-sperrungen-datum-modal>
							</template>
							<template v-else-if="(col.kuerzel === 'Eingabe bis')">
								<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="Ende" :row="() => row">
									<div @click="openModal" class="border border-ui/0 hover:border-ui rounded-md cursor-pointer">
										<span v-if="manager().istNoteneingabeZeitlichGesperrt(row.tsEingabeBis, false).value"
											class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabeende liegt bereits in der Vergangenheit, alle Eingaben sind gesperrt." />
										{{ printDate(row.tsEingabeBis) }}
									</div>
								</notenmodul-konfiguration-sperrungen-datum-modal>
							</template>
							<template v-else-if="(col.kuerzel === 'ggf. verlängerte Eingabe')">
								<notenmodul-konfiguration-sperrungen-datum-modal v-slot="{ openModal }" :manager modus="EndeKL" :row="() => row">
									<div @click="openModal" class="border border-ui/0 hover:border-ui rounded-md cursor-pointer">
										<span v-if="manager().istNoteneingabeZeitlichGesperrt(row.tsEingabeBisKlassenleitung, false).value"
											class="icon-ui-danger icon i-ri-alert-line align-middle" title="Das Eingabeende für die Klassenleitung liegt bereits in der Vergangenheit." />
										<span v-if="!manager().istGruppe(row) && (row.tsEingabeBisKlassenleitung !== null) && (row.tsEingabeBis === null)"
											class="icon-ui-warning icon i-ri-information-line align-middle" title="Ohne Eingabeende gilt für die Klasse keine zeitliche Grenze. Das Eingabeende für die Klassenleitung ist dann wirkungslos." />
										{{ printDate(row.tsEingabeBisKlassenleitung) }}
									</div>
								</notenmodul-konfiguration-sperrungen-datum-modal>
							</template>
							<template v-else-if="(col.kuerzel === 'alle Noten')">
								<svws-ui-checkbox :model-value="manager().hatNoteneingabeDurchKlassenleitung(row)"
									:indeterminate="manager().hatNoteneingabeDurchKlassenleitungTeilweise(row)"
									@update:model-value="manager().toggleNoteneingabeDurchKlassenleitung(row)" />
							</template>
							<template v-else-if="(col.kuerzel === 'FS Kl')">
								<svws-ui-checkbox :model-value="!manager().hatFehlstundeneingabeKlassenweise(row)"
									:indeterminate="manager().hatFehlstundeneingabeKlassenweiseTeilweise(row)"
									@update:model-value="manager().toggleFehlstundeneingabeKlassenweise(row)" />
							</template>
							<template v-else-if="col.kuerzel === 'Teilnoten'">
								<svws-ui-checkbox :model-value="!manager().hatSperrungTeilnoten(row)"
									:indeterminate="manager().hatTeilsperrungTeilnoten(row)"
									@update:model-value="manager().toggleSperrungTeilnoten(row)" />
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

	import type { List } from '@core/java/util/List';
	import { SelectManager } from '@ui/ui/controls/select/manager/SelectManager';
	import { GridManager } from '@ui/ui/controls/tablegrid/GridManager';

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

	const countVisibleColumns = computed<number>(() => {
		let i = 0;
		for (const col of gridManager.cols.values()) {
			if (gridManager.isColVisible(col.name)) {
				i++;
			}
		}
		return i;
	});

	function printDate(isoDateTime: string | null) {
		return (isoDateTime === null) ? '‒'
			: new Date(isoDateTime).toLocaleDateString('de-DE', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
	}

</script>
