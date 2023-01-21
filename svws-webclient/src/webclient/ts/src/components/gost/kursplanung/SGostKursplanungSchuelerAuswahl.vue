<template>
	<svws-ui-content-card title="Schülerauswahl" class="grow">
		<div class="flex flex-row gap-4">
			<div class="flex-none">
				<div class="sticky">
					<div>
						<div class="flex justify-between w-72">
							<svws-ui-checkbox v-model="kurs_filter_toggle" class=""> Kursfilter </svws-ui-checkbox>
							<svws-ui-multi-select v-if="kurs_filter_toggle" v-model="kurs_filter" :items="kurse" headless :item-text="(kurs: GostBlockungKurs) => manager?.getOfKursName(kurs.id).toString() || ''" class="w-52" />
						</div>
						<div class="flex justify-between w-72">
							<svws-ui-checkbox v-model="fach_filter_toggle" class=""> Fachfilter </svws-ui-checkbox>
							<svws-ui-multi-select v-if="fach_filter_toggle" v-model="fach_filter" :items="dataFaecher.daten" headless :item-text="(fach: GostFach) => fach.bezeichnung?.toString() || ''" class="w-36" />
							<svws-ui-multi-select v-if="fach_filter_toggle" v-model="kursart_filter" :items="GostKursart.values()" headless :item-text="(kursart: GostKursart) => kursart.kuerzel.toString()" class="w-16" />
						</div>
						<div class="pl-4">
							<svws-ui-radio-group>
								<svws-ui-radio-option v-model="radio_filter" value="alle" name="Alle" label="Alle" />
								<svws-ui-radio-option v-model="radio_filter" value="kollisionen" name="Kollisionen" label="Kollisionen" />
								<svws-ui-radio-option v-model="radio_filter" value="nichtwahlen" name="Nichtwahlen" label="Nichtwahlen" />
								<svws-ui-radio-option v-model="radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="Kollisionen und Nichtwahlen" />
							</svws-ui-radio-group>
						</div>
						<svws-ui-text-input v-model="filter_name" type="search" placeholder="Suche nach Namen">
							<i-ri-search-line />
						</svws-ui-text-input>
						<div class="v-table--container">
							<table class="v-table--complex">
								<s-kurs-schueler-schueler v-for="s in schueler" :key="s.id" :schueler="s" :selected="selected === s" @click="selected = s"
									:blockung="blockung" :list-schueler="listSchueler" />
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsergebnisManager, GostFach, GostHalbjahr, GostJahrgang, GostKursart, LehrerListeEintrag,
		List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, onErrorCaptured, ShallowRef, WritableComputedRef } from "vue";
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
	import { routeGostKursplanungSchueler } from "~/router/apps/gost/kursplanung/RouteGostKursplanungSchueler";

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

	onErrorCaptured((e)=>{
		alert("Es ist ein Fehler aufgetreten: " + e.message);
		// return false;
	})

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		// löse ein erneutes Filtern aus, wenn der Manager sich ändert (z.B. bei Blockungs- oder -Ergebniswechsel)
		return props.blockung.ergebnismanager
	});

	const kurse: ComputedRef<List<GostBlockungKurs>> = computed(() => props.blockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector<GostBlockungKurs>());

	const schueler: ComputedRef<Array<SchuelerListeEintrag> | undefined> = computed(() => props.listSchueler.gefiltert);

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = routeGostKursplanungSchueler.getSelector();

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

	const radio_filter: WritableComputedRef<string> = computed({
		get: () => {
			if (props.listSchueler.filter.kollisionen && props.listSchueler.filter.nichtwahlen)
				return 'kollisionen_nichtwahlen';
			if (props.listSchueler.filter.kollisionen)
				return 'kollisionen';
			if (props.listSchueler.filter.nichtwahlen)
				return 'nichtwahlen';
			return 'alle';
		},
		set: (value) => {
			switch (value) {
				case 'alle':
					props.listSchueler.filter.kollisionen = false;
					props.listSchueler.filter.nichtwahlen = false;
					break;
				case 'kollisionen':
					props.listSchueler.filter.kollisionen = true;
					props.listSchueler.filter.nichtwahlen = false;
					break;
				case 'nichtwahlen':
					props.listSchueler.filter.kollisionen = false;
					props.listSchueler.filter.nichtwahlen = true;
					break;
				case 'kollisionen_nichtwahlen':
					props.listSchueler.filter.kollisionen = true;
					props.listSchueler.filter.nichtwahlen = true;
					break;
			}
		}
	});

	const filter_name: WritableComputedRef<string> = computed({
		get: () => props.listSchueler?.filter?.name,
		set: (value) => {
			const filter = props.listSchueler.filter;
			filter.name = value;
			props.listSchueler.filter = filter;
		}
	});

</script>
