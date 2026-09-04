<template>
	<ui-table-grid name="Unterrichtsfächer" v-if="gridManager.daten.size() !== 0" :manager="() => gridManager" hide-selection>
		<template #header>
			<th class="text-left">Fach</th>
			<th class="text-center">Sek I</th>
			<th class="text-center">Sek II</th>
			<th class="text-left">Bemerkung</th>
			<th />
		</template>
		<template #default="{ row }">
			<td class="text-left">
				{{ getFachText(row.data) }}
			</td>
			<td class="text-center">
				<svws-ui-checkbox v-if="hatUpdateKompetenz" :model-value="row.data.istSek1"
					@update:model-value="value => patchLehrerUnterrichtsfach(row.data, { istSek1: value })" />
				<span v-else>{{ row.data.istSek1 ? 'Ja' : 'Nein' }}</span>
			</td>
			<td class="text-center">
				<svws-ui-checkbox v-if="hatUpdateKompetenz" :model-value="row.data.istSek2"
					@update:model-value="value => patchLehrerUnterrichtsfach(row.data, { istSek2: value })" />
				<span v-else>{{ row.data.istSek2 ? 'Ja' : 'Nein' }}</span>
			</td>
			<td class="text-left">
				<svws-ui-text-input v-if="hatUpdateKompetenz" :model-value="row.data.bemerkung ?? ''"
					@change="value => patchLehrerUnterrichtsfach(row.data, { bemerkung: value || null })" headless />
				<span v-else>{{ row.data.bemerkung ?? '' }}</span>
			</td>
			<td>
				<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
					<svws-ui-button @click="removeLehrerUnterrichtsfach(row.data)" type="trash" />
				</div>
			</td>
		</template>
		<template #footer>
			<template v-if="hatUpdateKompetenz">
				<td class="col-span-5 text-right">
					<svws-ui-tooltip>
						<svws-ui-button type="icon" @click="openHinzufuegen">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Fach hinzufügen
						</template>
					</svws-ui-tooltip>
				</td>
			</template>
			<template v-else>
				<td class="col-span-5" />
			</template>
		</template>
	</ui-table-grid>
	<div v-else>
		<svws-ui-button v-if="hatUpdateKompetenz" @click="openHinzufuegen" type="secondary">Fach hinzufügen</svws-ui-button>
		<div v-else>Keine Unterrichtsfächer zugeordnet.</div>
	</div>
	<svws-ui-modal v-model:show="showHinzufuegen" size="small" class="hidden">
		<template #modalTitle> Unterrichtsfach hinzufügen </template>
		<template #modalContent>
			<ui-select label="Fach" v-model="auswahlFachNeu" :manager="fachSelectManager" required :removable="false" />
			<div class="mt-4 text-left">
				<span class="text-headline-sm mb-2 block">wird unterrichtet in</span>
				<div class="flex gap-4">
					<svws-ui-checkbox v-model="neuIstSek1"> Sekundarstufe I </svws-ui-checkbox>
					<svws-ui-checkbox v-model="neuIstSek2"> Sekundarstufe II </svws-ui-checkbox>
				</div>
			</div>
			<svws-ui-text-input placeholder="Bemerkung" v-model="neuBemerkung" />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showHinzufuegen = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="createLehrerUnterrichtsfach" :disabled="auswahlFachNeu === null"> Anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { HashSet } from "@core/java/util/HashSet";
	import type { List } from "@core/java/util/List";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import { computed, ref, shallowRef } from "vue";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		lehrerUnterrichtsfaecher: () => List<LehrerUnterrichtsfach>;
		mapFaecher: () => Map<number, FachDaten>;
		idLehrer: () => number;
		patchLehrerUnterrichtsfach: (eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>) => Promise<void>;
		addLehrerUnterrichtsfach: (eintrag: Partial<LehrerUnterrichtsfach>) => Promise<void>;
		removeLehrerUnterrichtsfach: (eintrag: LehrerUnterrichtsfach) => Promise<void>;
	}>();

	type Eintrag = { data: LehrerUnterrichtsfach };

	const gridManager = new GridManager<string, Eintrag, List<Eintrag>>({
		daten: computed<List<Eintrag>>(() => {
			const result = new ArrayList<Eintrag>();
			for (const fach of props.lehrerUnterrichtsfaecher()) {
				result.add({ data: fach });
			}
			return result;
		}),
		getRowKey: row => `fach-${row.data.id}`,
		columns: [
			{ kuerzel: "Fach", name: "Fach", width: "minmax(30%,20rem)", hideable: false },
			{ kuerzel: "Sek1", name: "Sek I", width: "5rem", hideable: false },
			{ kuerzel: "Sek2", name: "Sek II", width: "5rem", hideable: false },
			{ kuerzel: "Bemerkung", name: "Bemerkung", width: "minmax(30%,20rem)", hideable: false },
			{ kuerzel: "Buttons", name: "Buttons", width: "4rem", hideable: false },
		],
	});

	function getFachText(eintrag: LehrerUnterrichtsfach): string {
		const fach = props.mapFaecher().get(eintrag.idFach);
		return fach ? `${fach.kuerzel} - ${fach.bezeichnung}` : '—';
	}

	const showHinzufuegen = ref<boolean>(false);
	const auswahlFachNeu = shallowRef<FachDaten | null>(null);
	const neuIstSek1 = ref<boolean>(false);
	const neuIstSek2 = ref<boolean>(false);
	const neuBemerkung = ref<string>("");

	const faecherVorhanden = computed(() => {
		const vorhanden = new HashSet<number>();
		for (const fach of props.lehrerUnterrichtsfaecher()) {
			vorhanden.add(fach.idFach);
		}
		return vorhanden;
	});

	const faecherVerfuegbar = computed<FachDaten[]>(() => {
		const result: FachDaten[] = [];
		for (const fach of props.mapFaecher().values()) {
			if (!faecherVorhanden.value.contains(fach.id)) {
				result.push(fach);
			}
		}
		return result.sort((a, b) => a.kuerzel.localeCompare(b.kuerzel));
	});

	const fachDisplayText = (f: FachDaten) => `${f.kuerzel} - ${f.bezeichnung}`;

	const fachSelectManager = new SelectManager<FachDaten>({
		options: faecherVerfuegbar,
		optionDisplayText: fachDisplayText,
		selectionDisplayText: fachDisplayText,
	});

	function openHinzufuegen() {
		auswahlFachNeu.value = null;
		neuIstSek1.value = false;
		neuIstSek2.value = false;
		neuBemerkung.value = "";
		showHinzufuegen.value = true;
	}

	async function createLehrerUnterrichtsfach() {
		if (auswahlFachNeu.value === null || faecherVorhanden.value.contains(auswahlFachNeu.value.id)) {
			return;
		}
		await props.addLehrerUnterrichtsfach({
			idLehrer: props.idLehrer(),
			idFach: auswahlFachNeu.value.id,
			istSek1: neuIstSek1.value,
			istSek2: neuIstSek2.value,
			bemerkung: neuBemerkung.value === "" ? null : neuBemerkung.value,
		});
		showHinzufuegen.value = false;
	}

</script>
