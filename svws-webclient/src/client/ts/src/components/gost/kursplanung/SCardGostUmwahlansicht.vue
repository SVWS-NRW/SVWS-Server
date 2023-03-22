<template>
	<svws-ui-content-card v-if="schueler" :title="'Kurszuordnungen für ' + schueler?.vorname + ' ' + schueler?.nachname" class="sticky top-0">
		<template #actions>
			<svws-ui-button type="secondary" @click="routeLaufbahnplanung()">
				Zur Laufbahnplanung
			</svws-ui-button>
		</template>
		<div class="flex gap-4">
			<svws-ui-drop-data v-slot="{ active }" class="w-1/6" @drop="drop_entferne_kurszuordnung">
				<div :class="{ 'border-2 border-dashed border-red-700': active }">
					<div class="">
						<table class="v-table--complex table-fixed">
							<s-kurs-schueler-fachbelegung v-for="fach in fachbelegungen" :key="fach.fachID" :fach="fach" :kurse="blockungsergebnisse"
								:schueler-id="schueler.id" :get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager" :drag-and-drop-data="dragAndDropData" @dnd="updateDragAndDropData" />
						</table>
						<template v-if="!blockung_aktiv">
							<div class="flex items-center justify-center" :class="{'bg-red-400 text-white': active}">
								<i-ri-delete-bin-2-line class="m-2 text-4xl" :class="{ 'text-red-700': is_dragging }" />
							</div>
							<div class="flex items-center justify-center">
								<svws-ui-button size="small" class="m-2" @click="auto_verteilen" :disabled="apiStatus.pending">Automatisch verteilen</svws-ui-button>
							</div>
						</template>
					</div>
				</div>
			</svws-ui-drop-data>
			<div class="flex-grow">
				<div class="v-table--container">
					<table class="v-table--complex">
						<s-kurs-schueler-schiene v-for="schiene in schienen" :key="schiene.id" :schiene="schiene" :selected="schueler"
							:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
							:api-status="apiStatus" :allow-regeln="allow_regeln" :add-regel="addRegel" :remove-regel="removeRegel"
							:update-kurs-schueler-zuordnung="updateKursSchuelerZuordnung" :drag-and-drop-data="dragAndDropData" @dnd="updateDragAndDropData" />
					</table>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsergebnisKurs, GostBlockungsergebnisSchiene, GostFachwahl, List } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { GostUmwahlansichtProps } from "./SCardGostUmwahlansichtProps";

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

	function routeSchueler() {
		void props.gotoSchueler(props.schueler.id);
	}

	function updateDragAndDropData(data: DndData | undefined) {
		dragAndDropData.value = data;
	}

</script>
