<template>
	<svws-ui-content-card v-if="schueler" :title="'Kurszuordnungen für ' + schueler?.vorname + ' ' + schueler?.nachname" class="w-full min-w-[40rem] sticky top-0">
		<template #actions>
			<svws-ui-button type="secondary" @click="routeLaufbahnplanung()">
				Zur Laufbahnplanung
			</svws-ui-button>
		</template>
		<div class="flex gap-4 -mt-2" v-if="fachbelegungen.size() > 0">
			<div class="w-1/6 min-w-[9rem]">
				<svws-ui-drop-data v-slot="{ active }" class="mb-4" @drop="drop_entferne_kurszuordnung">
					<div class="border-2 -m-[2px]" :class="{ 'border-dashed border-error': active, 'border-transparent': !active }">
						<div class="">
							<svws-ui-data-table :items="[]" :no-data="false">
								<template #body>
									<s-kurs-schueler-fachbelegung v-for="fach in fachbelegungen" :key="fach.fachID" :fach="fach" :kurse="blockungsergebnisse"
										:schueler-id="schueler.id" :get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager" :drag-and-drop-data="dragAndDropData" @dnd="updateDragAndDropData" />
								</template>
							</svws-ui-data-table>
							<template v-if="!blockung_aktiv">
								<div class="flex items-center py-2 px-3 m-1" :class="{'bg-error text-white': active}">
									<div v-if="active" class="flex gap-2 items-center w-full h-full">
										<i-ri-delete-bin-line class="w-6 h-6" :class="{ 'bg-error': is_dragging }" />
										<span>Entfernen</span>
									</div>
									<div v-else class="flex gap-2 items-center w-full h-full">
										<i-ri-delete-bin-line class="w-6 h-6 opacity-25" :class="{ 'bg-error': is_dragging }" />
										<span class="opacity-25">Entfernen</span>
									</div>
								</div>
							</template>
						</div>
					</div>
				</svws-ui-drop-data>
				<svws-ui-button class="w-full justify-center" type="secondary" @click="auto_verteilen" :disabled="apiStatus.pending" title="Automatisch verteilen">Verteilen<i-ri-sparkling-line /></svws-ui-button>
			</div>
			<div class="flex-grow">
				<svws-ui-data-table :items="[]" :columns="cols">
					<template #header><div /></template>
					<template #body>
						<s-kurs-schueler-schiene v-for="(schiene, index) in schienen"
							:key="index" :schiene="schiene" :selected="schueler" :max-kurse="maxKurseInSchienen"
							:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
							:api-status="apiStatus" :allow-regeln="allow_regeln" :add-regel="addRegel" :remove-regel="removeRegel"
							:update-kurs-schueler-zuordnung="updateKursSchuelerZuordnung" :drag-and-drop-data="dragAndDropData" @dnd="updateDragAndDropData" />
					</template>
				</svws-ui-data-table>
			</div>
		</div>
		<div v-else class="opacity-50">
			Keine Fachbelegungen vorhanden.
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungsergebnisKurs, GostBlockungsergebnisSchiene, GostFachwahl, List } from "@svws-nrw/svws-core";
	import type { ComputedRef, Ref} from "vue";
	import { computed, ref } from "vue";
	import type { GostUmwahlansichtProps } from "./SCardGostUmwahlansichtProps";
	import type {DataTableColumn} from "@ui";

	type DndData = { id: number, fachID: number, kursart: number };

	const props = defineProps<GostUmwahlansichtProps>();

	const is_dragging: Ref<boolean> = ref(false);

	const dragAndDropData: Ref<DndData | undefined> = ref(undefined);

	const allow_regeln: ComputedRef<boolean> = computed(() => props.getDatenmanager().getErgebnisseSortiertNachBewertung().size() === 1);

	const kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => props.getDatenmanager().getKursmengeSortiertNachKursartFachNummer());

	const schienen: ComputedRef<List<GostBlockungsergebnisSchiene>> = computed(() => props.getErgebnismanager().getMengeAllerSchienen());

	const blockung_aktiv: ComputedRef<boolean> = computed(() => props.getDatenmanager().daten().istAktiv);

	const blockungsergebnisse: ComputedRef<Map<GostBlockungKurs, GostBlockungsergebnisKurs[]>> = computed(() => {
		const map = new Map();
		if (!schienen.value?.size())
			return map;
		for (const k of kurse.value)
			for (const s of schienen.value) {
				const arr = []
				for (const kk of s.kurse)
					kk.id === k.id ? arr.push(kk) : arr.push(undefined)
				map.set(k, arr)
			}
		return map;
	});

	async function drop_entferne_kurszuordnung(kurs: any) {
		const schuelerid = props.schueler.id;
		if (kurs === undefined || kurs.id === undefined)
			return;
		await props.removeKursSchuelerZuordnung(schuelerid, kurs.id);
	}

	async function auto_verteilen() {
		await props.autoKursSchuelerZuordnung(props.schueler.id);
	}

	const fachbelegungen: ComputedRef<List<GostFachwahl>> = computed(() => {
		return props.getDatenmanager().getOfSchuelerFacharten(props.schueler.id);
	});

	function routeLaufbahnplanung() {
		void props.gotoLaufbahnplanung(props.schueler.id);
	}

	function updateDragAndDropData(data: DndData | undefined) {
		dragAndDropData.value = data;
	}

	const maxKurseInSchienen: ComputedRef<number> = computed(() => {
		let max = 0;
		for (let i = 0; i < schienen.value.size(); i++) {
			if (schienen.value?.get(i)?.kurse) {
				max = Math.max(max, schienen.value.get(i).kurse.size());
			}
		}
		return max;
	});

	function calculateColumns(): DataTableColumn[] {
		const cols: Array<DataTableColumn> = [{ key: "schiene", label: "Schiene" }];

		for (let i = 0; i < maxKurseInSchienen?.value; i++) {
			cols.push({ key: "kurs_" + (i+1), label: "Kurs " + (i+1), align: 'center' });
		}

		return cols;
	}

	const cols: ComputedRef<DataTableColumn[]> = computed(() => calculateColumns());

</script>
