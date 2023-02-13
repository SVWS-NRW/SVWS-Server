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
							<s-kurs-schueler-fachbelegung v-for="fach in fachbelegungen" :key="fach.fachID" :fach="fach"
								:kurse="blockungsergebnisse" :schueler-id="schueler.id" :blockung="blockung" :ergebnis="ergebnis" />
						</table>
						<template v-if="!blockung_aktiv">
							<div class="flex items-center justify-center" :class="{'bg-red-400 text-white': active}">
								<i-ri-delete-bin-2-line class="m-2 text-4xl" :class="{ 'text-red-700': is_dragging }" />
							</div>
							<div class="flex items-center justify-center">
								<svws-ui-button size="small" class="m-2" @click="auto_verteilen" :disabled="pending">Automatisch verteilen</svws-ui-button>
							</div>
						</template>
					</div>
				</div>
			</svws-ui-drop-data>
			<div class="flex-grow">
				<div class="v-table--container">
					<table class="v-table--complex">
						<s-kurs-schueler-schiene v-for="schiene in schienen" :key="schiene.id" :schiene="schiene" :selected="schueler"
							:blockung="blockung" :ergebnis="ergebnis" :allow_regeln="allow_regeln" />
					</table>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene,
		GostFachwahl, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, ShallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";

	const props = defineProps<{
		gotoSchueler: (idSchueler: number) => Promise<void>;
		gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		mapLehrer: Map<number, LehrerListeEintrag>;
		fachwahlen: List<GostStatistikFachwahl>;
		schueler: SchuelerListeEintrag;
	}>();

	const is_dragging: Ref<boolean> = ref(false)

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		// löse ein erneutes Filtern aus, wenn der Manager sich ändert (z.B. bei Blockungs- oder -Ergebniswechsel)
		return props.blockung.ergebnismanager
	});

	const allow_regeln: ComputedRef<boolean> = computed(() =>
		(props.blockung.datenmanager !== undefined) && (props.blockung.datenmanager.getErgebnisseSortiertNachBewertung().size() === 1)
	);

	const kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => props.blockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector<GostBlockungKurs>());

	const schienen: ComputedRef<List<GostBlockungsergebnisSchiene>> = computed(() => manager.value?.getMengeAllerSchienen() || new Vector<GostBlockungsergebnisSchiene>());

	const blockung_aktiv: ComputedRef<boolean> = computed(()=> props.blockung.daten?.istAktiv || false);

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

	const pending = computed(() => props.ergebnis.pending);

	async function drop_entferne_kurszuordnung(kurs: any) {
		const schuelerid = props.schueler.id;
		if (kurs === undefined)
			return;
		await props.ergebnis.removeSchuelerKurs(schuelerid, kurs.id);
	}

	async function auto_verteilen() {
		await props.ergebnis.multiAssignSchuelerKurs(props.schueler.id);
	}

	const fachbelegungen: ComputedRef<List<GostFachwahl>> = computed(() => {
		if (props.blockung.datenmanager === undefined)
			return new Vector<GostFachwahl>();
		return props.blockung.datenmanager.getOfSchuelerFacharten(props.schueler.id);
	});

	function routeLaufbahnplanung() {
		void props.gotoLaufbahnplanung(props.schueler.id);
	}

	function routeSchueler() {
		void props.gotoSchueler(props.schueler.id);
	}
</script>
