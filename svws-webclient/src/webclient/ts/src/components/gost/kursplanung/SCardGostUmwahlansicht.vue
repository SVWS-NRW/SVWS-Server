<template>
	<svws-ui-content-card :title="`Kurszuordnungen für ${listSchueler.ausgewaehlt?.vorname} ${listSchueler.ausgewaehlt?.nachname}`" class="grow">
		<div class="flex flex-row gap-4">
			<div v-if="selected" class="flex-none">
				<div class="">
					<div class="overflow-hidden rounded-lg shadow">
						<table class="border-collapse text-sm">
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
	import { injectMainApp, Main } from "~/apps/Main";
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

	const main: Main = injectMainApp();
	const schueler_filter = props.listSchueler.filter;

	onErrorCaptured((e)=>{
		alert("Es ist ein Fehler aufgetreten: " + e.message);
		// return false;
	})

	const is_dragging: Ref<boolean> = ref(false)

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		// löse ein erneutes Filtern aus, wenn der Manager sich ändert (z.B. bei Blockungs- oder -Ergebniswechsel)
		props.listSchueler.filter = props.listSchueler.filter;
		return props.blockung.ergebnismanager
	});

	const allow_regeln: ComputedRef<boolean> = computed(() =>
		(props.blockung.datenmanager !== undefined) && (props.blockung.datenmanager.getErgebnisseSortiertNachBewertung().size() === 1)
	);

	const kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => props.blockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector<GostBlockungKurs>());

	const schienen: ComputedRef<List<GostBlockungsergebnisSchiene>> = computed(() => manager.value?.getMengeAllerSchienen() || new Vector<GostBlockungsergebnisSchiene>());

	const schueler: ComputedRef<Array<SchuelerListeEintrag> | undefined> = computed(() => props.listSchueler.gefiltert);

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.listSchueler.ausgewaehlt,
		set: (value) => props.listSchueler.ausgewaehlt = value
	});

	const blockung_aktiv: ComputedRef<boolean> = computed(()=> props.blockung.daten?.istAktiv || false);

	const aktiver_kursname: ComputedRef<String | undefined> = computed(() => schueler_filter.kurs === undefined ? undefined : manager.value?.getOfKursName(schueler_filter.kurs.id));

	const schueler_kollisionen: ComputedRef<number> = computed(() => {
		if (manager.value === undefined)
			return 0;
		if (schueler_filter.kurs?.id !== undefined)
			return manager.value.getOfKursAnzahlKollisionen(schueler_filter.kurs.id);
		return manager.value.getMengeDerSchuelerMitKollisionen().size();
	});

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
		get: () => schueler_filter.fach,
		set: (value) => {
			schueler_filter.fach = value;
			props.listSchueler.filter = schueler_filter;
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
		get: () => schueler_filter.kurs,
		set: (value) => {
			schueler_filter.kurs = value;
			props.listSchueler.filter = schueler_filter;
		}
	})

	const kursart_filter: WritableComputedRef<GostKursart | undefined> = computed({
		get: () => schueler_filter.kursart,
		set: (value) => {
			schueler_filter.kursart = value;
			props.listSchueler.filter = schueler_filter;
		}
	})

	const radio_filter: WritableComputedRef<string> = computed({
		get: () => {
			if (schueler_filter.kollisionen && schueler_filter.nichtwahlen)
				return 'kollisionen_nichtwahlen';
			if (schueler_filter.kollisionen)
				return 'kollisionen';
			if (schueler_filter.nichtwahlen)
				return 'nichtwahlen';
			return 'alle';
		},
		set: (value) => {
			switch (value) {
				case 'alle':
					schueler_filter.kollisionen = false;
					schueler_filter.nichtwahlen = false;
					break;
				case 'kollisionen':
					schueler_filter.kollisionen = true;
					schueler_filter.nichtwahlen = false;
					break;
				case 'nichtwahlen':
					schueler_filter.kollisionen = false;
					schueler_filter.nichtwahlen = true;
					break;
				case 'kollisionen_nichtwahlen':
					schueler_filter.kollisionen = true;
					schueler_filter.nichtwahlen = true;
					break;
			}
			props.listSchueler.filter = schueler_filter;
		}
	});

	const filter_name: WritableComputedRef<string> = computed({
		get: () => props.listSchueler?.filter?.name,
		set: (value) => {
			const filter = schueler_filter;
			filter.name = value;
			props.listSchueler.filter = filter;
		}
	});

	const pending = computed(() => props.ergebnis.pending);

	// Macht Probleme beim Neuverteilen der Kurse. Schüler spring tzurück auf 1
	//watch(()=>schueler.value, (new_val)=> selected.value = new_val ? new_val[0] : undefined)

	async function drop_entferne_kurszuordnung(kurs: any) {
		const schuelerid = selected.value?.id;
		if (!schuelerid || !kurs?.id) return;
		let ok = false
		ok = await props.ergebnis.removeSchuelerKurs(schuelerid, kurs.id);
		if (ok)
			main.config.drag_and_drop_data = undefined;
	}

	async function auto_verteilen() {
		if (selected.value === undefined)
			return;
		await props.ergebnis.multiAssignSchuelerKurs(selected.value.id)
	}

</script>
