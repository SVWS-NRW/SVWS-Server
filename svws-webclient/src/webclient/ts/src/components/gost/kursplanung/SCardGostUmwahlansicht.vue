<template>
	<svws-ui-content-card :title="`Kurszuordnungen für ${listSchueler.ausgewaehlt?.vorname} ${listSchueler.ausgewaehlt?.nachname}`" class="grow">
		<div class="flex flex-row gap-4">
			<svws-ui-drop-data v-if="selected" v-slot="{ active }" class="w-40 flex-none" @drop="drop_entferne_kurszuordnung">
				<div :class="{ 'border-2 border-dashed border-red-700': active }">
					<div class="">
						<table class="v-table--complex">
							<s-kurs-schueler-fachbelegung v-for="fach in fachbelegungen" :key="fach.fachID" :fach="fach"
								:kurse="blockungsergebnisse" :schueler-id="selected.id" :blockung="blockung" :ergebnis="ergebnis" />
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
			<div v-if="selected" class="flex-none">
				<div class="">
					<div class="v-table--container">
						<table class="v-table--complex">
							<s-kurs-schueler-schiene v-for="schiene in schienen" :key="schiene.id" :schiene="schiene" :selected="selected"
								:blockung="blockung" :ergebnis="ergebnis" :allow_regeln="allow_regeln" />
						</table>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene,
		GostFach, GostFachwahl, GostHalbjahr, GostJahrgang, GostKursart, LehrerListeEintrag, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, onErrorCaptured, Ref, ref, ShallowRef, WritableComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
	import { DataGostSchuelerFachwahlen } from "~/apps/gost/DataGostSchuelerFachwahlen";
	import { DataSchuelerLaufbahndaten } from "~/apps/gost/DataSchuelerLaufbahnplanung";
	import { ListAbiturjahrgangSchueler } from "~/apps/gost/ListAbiturjahrgangSchueler";
	import { ListKursblockungen } from "~/apps/gost/ListKursblockungen";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		listBlockungen: ListKursblockungen;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
		dataFachwahlen: DataGostSchuelerFachwahlen;
		listSchueler: ListAbiturjahrgangSchueler;
		dataSchueler: DataSchuelerLaufbahndaten;
	}>();

	const is_dragging: Ref<boolean> = ref(false)

	onErrorCaptured((e)=>{
		alert("Es ist ein Fehler aufgetreten: " + e.message);
		// return false;
	})

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		// löse ein erneutes Filtern aus, wenn der Manager sich ändert (z.B. bei Blockungs- oder -Ergebniswechsel)
		return props.blockung.ergebnismanager
	});

	const allow_regeln: ComputedRef<boolean> = computed(() =>
		(props.blockung.datenmanager !== undefined) && (props.blockung.datenmanager.getErgebnisseSortiertNachBewertung().size() === 1)
	);

	const kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => props.blockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector<GostBlockungKurs>());

	const schienen: ComputedRef<List<GostBlockungsergebnisSchiene>> = computed(() => manager.value?.getMengeAllerSchienen() || new Vector<GostBlockungsergebnisSchiene>());

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.listSchueler.ausgewaehlt,
		set: (value) => props.listSchueler.ausgewaehlt = value
	});

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
		const schuelerid = selected.value?.id;
		if (!schuelerid || !kurs?.id) return;
		await props.ergebnis.removeSchuelerKurs(schuelerid, kurs.id);
	}

	async function auto_verteilen() {
		if (selected.value === undefined)
			return;
		await props.ergebnis.multiAssignSchuelerKurs(selected.value.id)
	}

	const fachbelegungen: ComputedRef<List<GostFachwahl>> = computed(() => {
		if (!selected.value?.id || !props.blockung.datenmanager)
			return new Vector<GostFachwahl>()
		return props.blockung.datenmanager.getOfSchuelerFacharten(selected.value.id)
	});

	const fach_filter_toggle: WritableComputedRef<boolean> = computed({
		get: () => (fach_filter.value !== undefined),
		set(value) {
			if (value && props.dataFaecher.daten) {
				kurs_filter_toggle.value = false;
				fach_filter.value = props.dataFaecher.daten.get(0);
				kursart_filter.value = GostKursart.GK;
			} else {
				fach_filter.value = undefined;
				kursart_filter.value = undefined;
			}
		}
	});

	const fach_filter: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.listSchueler.filter.fach,
		set: (value) => {
			props.listSchueler.filter.fach = value;
		}
	})

	const kurs_filter_toggle: WritableComputedRef<boolean> = computed({
		get: () => (kurs_filter.value !== undefined),
		set: (value) => {
			if (value && props.dataFaecher.daten) {
				kurs_filter.value = kurse.value.get(0);
				fach_filter_toggle.value = false;
			} else
				kurs_filter.value = undefined;
		}
	})

	const kurs_filter: WritableComputedRef<GostBlockungKurs | undefined> = computed({
		get: () => props.listSchueler.filter.kurs,
		set: (value) => {
			props.listSchueler.filter.kurs = value;
		}
	})

	const kursart_filter: WritableComputedRef<GostKursart | undefined> = computed({
		get: () => props.listSchueler.filter.kursart,
		set: (value) => {
			props.listSchueler.filter.kursart = value;
		}
	})

</script>
