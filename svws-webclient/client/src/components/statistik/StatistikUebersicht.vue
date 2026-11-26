<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-uebersicht /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="p-4 page--statistik">
		<div class="svws-ui-dashboard">
			<svws-ui-dashboard-tile title="Adresse">
				<div>{{ statistikState.statistikGesamt.schule.bezeichnung1 }}</div>
				<div>{{ statistikState.statistikGesamt.schule.bezeichnung2 }}</div>
				<div>{{ statistikState.statistikGesamt.schule.bezeichnung3 }}</div>
				<div>{{ statistikState.statistikGesamt.schule.strassenname }} {{ statistikState.statistikGesamt.schule.hausnummer }} {{ statistikState.statistikGesamt.schule.hausnummerZusatz }} </div>
				<div>{{ statistikState.statistikGesamt.schule.plz }} {{ statistikState.statistikGesamt.schule.ort }}</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Kontakt" :span="1">
				<svws-ui-text-input placeholder="Ansprechperson" v-model="model.proxy.statistikAnsprechpartnerName"
					:validation="() => model.getFehler('statistikAnsprechpartnerName')" @change="model.patch" required statistics />
				<svws-ui-text-input placeholder="Telefon" v-model="model.proxy.statistikAnsprechpartnerTelefon"
					:validation="() => model.getFehler('statistikAnsprechpartnerTelefon')" @change="model.patch" type="tel" required statistics />
				<svws-ui-text-input placeholder="E-Mail-Adresse" v-model="model.proxy.statistikAnsprechpartnerEmail"
					:validation="() => model.getFehler('statistikAnsprechpartnerEmail')" @change="model.patch" type="email" required statistics />
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Meldung der Statistik" :span="1" color="dark">
				<div>Nächster Termin <span class="font-bold">{{ new Date().toLocaleDateString("de-DE", { day: '2-digit', month: '2-digit', year: 'numeric' }) }}</span></div>
				<div class="mt-8">Frist zur Einreichung · Schuljahr 2027/28</div>
			</svws-ui-dashboard-tile>

			<svws-ui-dashboard-tile title="Daten senden">
				<div class="font-bold">Amtliche Schulstatistik an das Bildungsportal NRW übermitteln.</div>
				<div v-if="!validator.getFehler().isEmpty()" class="font-bold text-ui-danger">
					<span class="icon i-ri-error-warning-line icon-ui-danger" /> {{ validator.getFehler().size() }} Fehler in der Gesamtprüfung
				</div>
				<div v-else class="font-bold">Keine Fehler</div>
				<div><svws-ui-button type="primary">Senden</svws-ui-button></div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Schülerdaten">
				<div class="font-bold">{{ statistikState.statistikGesamt.schueler.size() }} Schueler angemeldet</div>
				<div class="font-bold cursor-pointer" :class="statistikState.validatorGesamt.getFehlerSchueler().isEmpty() ? 'text-ui-success' : 'text-ui-danger'"
					@click="statistikState.gotoTab('statistik.schueler')">
					{{ statistikState.validatorGesamt.getFehlerSchueler().size() }} Fehler
				</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Lehrerdaten">
				<div class="font-bold">{{ statistikState.statistikGesamt.lehrer.size() }} Lehrkräfte angestellt</div>
				<div class="font-bold cursor-pointer" :class="statistikState.validatorGesamt.getFehlerLehrer().isEmpty() ? 'text-ui-success' : 'text-ui-danger'"
					@click="statistikState.gotoTab('statistik.lehrer')">
					{{ statistikState.validatorGesamt.getFehlerLehrer().size() }} Fehler
				</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Klassendaten">
				<div class="font-bold"> {{ statistikState.statistikGesamt.klassen.size() }} Klassen</div>
				<div class="flex justify-between">
					<div class="font-bold cursor-pointer" :class="statistikState.validatorGesamt.getFehlerKlassen().isEmpty() ? 'text-ui-success' : 'text-ui-danger'"
						@click="statistikState.gotoTab('statistik.klassen')">
						{{ statistikState.validatorGesamt.getFehlerKlassen().size() }} Fehler
					</div>
					<div class="text-lg cursor-pointer" @click="summenmodal = 'klassen'">Summendaten anzeigen</div>
				</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Unterrichtsdaten">
				<div class="font-bold"> {{ statistikState.statistikGesamt.kurse.size() }} Kurse</div>
				<div class="flex justify-between">
					<div class="font-bold cursor-pointer" :class="statistikState.validatorGesamt.getFehlerKurse().isEmpty() ? 'text-ui-success' : 'text-ui-danger'"
						@click="statistikState.gotoTab('statistik.kurse')">
						{{ statistikState.validatorGesamt.getFehlerKurse().size() }} Fehler
					</div>
				</div>
			</svws-ui-dashboard-tile>
		</div>
		<svws-ui-modal :show="summenmodal !== null" @update:show="summenmodal = null" size="fit">
			<template #modalTitle>
				Summendaten
			</template>
			<template #modalContent>
				<ui-table-grid :manager="() => gridManager">
					<template #header>
						<td v-for="col of gridManager.cols.values()" :key="col.kuerzel">{{ col.kuerzel }}</td>
					</template>
					<template #default="{ row }">
						<template v-if="summenmodal === 'klassen'">
							<td>{{ row.kuerzel }}</td> <td>{{ row.groesse }}</td> <td>{{ row.w }}</td> <td>{{ row.m }}</td> <td>{{ row.d }}</td> <td>{{ row.x }}</td> <td>{{ row.a }}</td>
						</template>
					</template>
				</ui-table-grid>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import { Geschlecht } from "@core/asd/types/Geschlecht";
	import { ValidatorGesamt } from "@core/asd/validate/ValidatorGesamt";
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useStatistikState } from "@ui/states/statistik/StatistikState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";

	import { ZusatzerhebungenModelProxy } from "./modelproxy/ZusatzerhebungenModelProxy";
	import type { StatistikUebersichtProps } from "./StatistikUebersichtProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikUebersichtProps>();
	const schuleState = useSchuleState();
	const statistikState = useStatistikState();

	const summenmodal = ref<'klassen' | null>(null);

	const model = new ZusatzerhebungenModelProxy(() => statistikState.statistikGesamt.zusatzerhebungen, statistikState.patchZusatzerhebungen);

	const validator = computed(() => {
		const val = new ValidatorGesamt({ get: () => statistikState.statistikGesamt }, schuleState.validatorKontext);
		val.run();
		return val;
	});

	type GridKlasseData = { id: number, kuerzel: string, groesse: number, m: number, w: number, d: number, x: number, a: number };
	const gridManager = new GridManager<string, GridKlasseData, List<GridKlasseData>>({
		daten: computed(() => {
			const list = new ArrayList<GridKlasseData>();
			const alleKlassen: GridKlasseData = { id: -1, kuerzel: 'Gesammtsumme', groesse: 0, m: 0, w: 0, d: 0, x: 0, a: 0 };
			for (const k of statistikState.statistikGesamt.klassen) {
				const klasse: GridKlasseData = { id: k.id, kuerzel: k.kuerzel ?? 'fehlt', groesse: k.idsSchueler.size(), m: 0, w: 0, d: 0, x: 0, a: 0 };
				for (const s of k.idsSchueler) {
					const schuelerStatistik = statistikState.mapSchuelerStatistik.get(s);
					if (schuelerStatistik === undefined) {
						try {
							throw new DeveloperNotificationException(`In Klasse ${k.kuerzel ?? k.id} wird ein Schüler mit der ID ${s} geführt, der aber nicht in den aggregierten Schülerdaten auftaucht. Die Summendaten können nicht korrekt ermittelt werden.`);
						} catch {
							continue;
						}
					}
					alleKlassen.groesse++;
					switch (Geschlecht.fromValue(schuelerStatistik.geschlecht)) {
						case Geschlecht.D:
							klasse.d++;
							alleKlassen.d++;
							break;
						case Geschlecht.M:
							klasse.m++;
							alleKlassen.m++;
							break;
						case Geschlecht.W:
							klasse.w++;
							alleKlassen.w++;
							break;
						case Geschlecht.X:
							klasse.x++;
							alleKlassen.x++;
							break;
						default:
							break;
					}
					if (schuelerStatistik.hatMigrationshintergrund) {
						klasse.a++;
						alleKlassen.a++;
					}
				}
				list.add(klasse);
			}
			list.add(alleKlassen);
			return list;
		}),
		getRowKey: row => `ID_${row.id}`,
		allowEmptyRowSelection: false,
		columns: [
			{ kuerzel: "Klasse", name: "Klasse", width: '1fr' },
			{ kuerzel: "Gesamt", name: "Gesamt", width: '1fr' },
			{ kuerzel: "W", name: "weiblich", width: '1fr' },
			{ kuerzel: "M", name: "männlich", width: '1fr' },
			{ kuerzel: "D", name: "divers", width: '1fr' },
			{ kuerzel: "X", name: "ohne Angabe", width: '1fr' },
			{ kuerzel: "Migr.", name: "Migrationshintergrund", width: '1fr' },
		],
	});
</script>
