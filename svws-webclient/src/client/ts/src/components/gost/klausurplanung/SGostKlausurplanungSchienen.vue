<template>
	<svws-ui-sub-nav>
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
		<svws-ui-button type="primary" @click="erzeugeKursklausurenAusVorgaben(quartalsauswahl.value)">Erstelle Klausuren</svws-ui-button>
		<svws-ui-button type="secondary" @click="erzeugeKlausurtermin(quartalsauswahl.value)" :disabled="quartalsauswahl.value <= 0">Neuer Termin</svws-ui-button>
		<svws-ui-button type="secondary" @click="modal.openModal()" :disabled="termine.size() > 0"><svws-ui-spinner :spinning="loading" /> Automatisch blocken</svws-ui-button>
		<svws-ui-button type="danger" @click="loescheTermine" :disabled="termine.size() === 0">Alle Termine löschen</svws-ui-button>
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-schienen-hilfe /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>

	<div>
		<svws-ui-modal ref="modal" size="small">
			<template #modalTitle>
				Automatisch Blocken
			</template>
			<template #modalDescription>
				Hier können Sie weitere Einstellungen vornehmen:
			</template>
			<template #modalContent>
				<svws-ui-radio-group :row="true" class="justify-center">
					<svws-ui-radio-option v-for="a in KlausurterminblockungAlgorithmen.values()" :key="a.id" :value="a" v-model="algMode" :name="a.bezeichnung" :label="a.bezeichnung" />
				</svws-ui-radio-group>
				<svws-ui-radio-group :row="true" class="justify-center">
					<svws-ui-radio-option v-for="k in KlausurterminblockungModusKursarten.values()" :key="k.id" :value="k" v-model="lkgkMode" :name="k.bezeichnung" :label="k.bezeichnung" />
				</svws-ui-radio-group>
				<svws-ui-checkbox v-model="blockeGleicheLehrkraft" v-if="algMode.__ordinal === KlausurterminblockungAlgorithmen.NORMAL.__ordinal">
					Falls gleiche Lehrkraft, Fach und Kursart, dann gleicher Termin?
				</svws-ui-checkbox>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="blocken"> Blocken </svws-ui-button>
				<svws-ui-button type="secondary" @click="modal.closeModal()"> Abbrechen </svws-ui-button>
			</template>
		</svws-ui-modal>

		<svws-ui-content-card class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
			<div class="flex flex-row gap-8 mt-4">
				<div class="flex flex-col border" @drop="onDrop(undefined)" @dragover="$event.preventDefault()">
					<div class="text-headline-md">Zu verplanen:</div>
					<table class="w-full">
						<thead />
						<tbody>
							<tr v-for="klausur in klausurenOhneTermin()"
								:key="klausur.id"
								:data="klausur"
								:draggable="true"
								@dragstart="onDrag(klausur)"
								@dragend="onDrag(undefined)"
								:class="klausurCssClasses(klausur, undefined)">
								<td>{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
								<td>{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
								<td class="text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
								<td class="text-center">{{ klausur.dauer }}</td>
								<td>&nbsp;</td>
								<td />
							</tr>
						</tbody>
					</table>
				</div>
				<div class="flex flex-col">
					<div class="flex flex-row flex-wrap gap-4 items-start">
						<s-gost-klausurplanung-schienen-termin v-for="termin of termine" :key="termin.id"
							:jahrgangsdaten="jahrgangsdaten"
							:class="dropOverCssClasses(termin)"
							:kursklausurmanager="kursklausurmanager"
							:termin="() => termin"
							:faecher-manager="faecherManager"
							:map-lehrer="mapLehrer"
							:drag-data="() => dragData"
							:on-drag="onDrag"
							:on-drop="onDrop"
							:map-schueler="mapSchueler"
							:loesche-klausurtermine="loescheKlausurtermine"
							:patch-klausurtermin="patchKlausurtermin"
							:klausur-css-classes="klausurCssClasses"
							:kursmanager="kursmanager" />
					</div>
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { GostKursklausur, GostKlausurtermin } from "@core";
	import { KlausurterminblockungAlgorithmen, GostKlausurterminblockungDaten,
		KlausurterminblockungModusKursarten, KlausurterminblockungModusQuartale } from "@core";
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungSchienenProps } from './SGostKlausurplanungSchienenProps';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";

	const modal = ref<any>(null);

	const props = defineProps<GostKlausurplanungSchienenProps>();

	const loading = ref<boolean>(false);

	const dragData = ref<GostKlausurplanungDragData>(undefined);

	const klausurenOhneTermin = () => props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartalsauswahl.value);

	const onDrag = (data: GostKlausurplanungDragData) => {
		dragData.value = data;
	};

	const onDrop = async (zone: GostKlausurplanungDropZone) => {
		if (dragData.value instanceof GostKursklausur) {
			const klausur = dragData.value;
			if (zone === undefined && klausur.idTermin != null)
				await props.patchKursklausur(klausur.id, {idTermin: null});
			else if (zone instanceof GostKlausurtermin) {
				const termin = zone;
				if (termin.id != klausur.idTermin)
					await props.patchKursklausur(klausur.id, {idTermin: termin.id});
			}
		}
	};

	const dropOverCssClasses = (termin: GostKlausurtermin) => ({
		"bg-success": dragData.value !== undefined && (dragData.value.quartal === termin.quartal || termin.quartal === 0),
		"opacity-40": dragData.value !== undefined && (dragData.value.quartal !== termin.quartal && termin.quartal !== 0),
	});

	const termine = computed(() => props.quartalsauswahl.value === 0 ? props.kursklausurmanager().terminGetMengeAsList() : props.kursklausurmanager().terminGetMengeByQuartal(props.quartalsauswahl.value, true));

	const algMode = ref<KlausurterminblockungAlgorithmen>(KlausurterminblockungAlgorithmen.NORMAL);
	const lkgkMode = ref<KlausurterminblockungModusKursarten>(KlausurterminblockungModusKursarten.BEIDE);
	const blockeGleicheLehrkraft = ref(false);

	const blocken = async () => {
		loading.value = true;
		modal.value.closeModal();
		const daten = new GostKlausurterminblockungDaten();
		daten.klausuren = props.kursklausurmanager().kursklausurOhneTerminGetMengeByQuartal(props.quartalsauswahl.value);
		daten.konfiguration.modusQuartale = KlausurterminblockungModusQuartale.GETRENNT.id;
		daten.konfiguration.algorithmus = algMode.value.id;
		daten.konfiguration.modusKursarten = lkgkMode.value.id;
		daten.konfiguration.regelBeiTerminenGleicheLehrkraftFachKursart = blockeGleicheLehrkraft.value;
		await props.blockenKursklausuren(daten);
		loading.value = false;
	};

	const loescheTermine = async () => await props.loescheKlausurtermine(termine.value);

	const klausurCssClasses = (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => {
		let konfliktfreiZuFremdtermin = false;
		for (const oTermin of termine.value) {
			if (oTermin.id !== klausur.idTermin && oTermin.quartal === klausur.quartal)
				konfliktfreiZuFremdtermin = props.kursklausurmanager().konfliktSchueleridsGetMengeByTerminidAndKursklausurid(oTermin.id, klausur.id).isEmpty();
			if (konfliktfreiZuFremdtermin)
				break;
		}
		const konfliktZuEigenemTermin = termin === undefined || klausur === null ? false : props.kursklausurmanager().konfliktTermininternSchueleridsGetMengeByTerminAndKursklausur(termin, klausur).size() > 0;
		return {
			"bg-success": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"bg-error": konfliktZuEigenemTermin,
		}
	};

</script>
