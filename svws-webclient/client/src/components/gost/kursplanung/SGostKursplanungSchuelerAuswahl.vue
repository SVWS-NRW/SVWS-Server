<template>
	<svws-ui-content-card overflow-scroll class="-mt-0.5 s-gost-kursplanung-schueler-auswahl">
		<svws-ui-data-table :model-value="schuelerFilter.filtered.value" v-model:clicked="selected" clickable :items="undefined"
			:filter-reverse="true" :filter-hide="false" :filter-open="true" :columns="cols"
			:no-data="schuelerFilter.filtered.value.length <= 0" :disable-footer="schuelerFilter.filtered.value.length <= 0" no-data-html="Keine Ergebnisse.">
			<template #search>
				<svws-ui-text-input type="search" v-model="schuelerFilter.name" placeholder="Suche" />
			</template>
			<template #filter>
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="schuelerFilter.alle_toggle.value" value="alle" name="Alle" label="">
						<i-ri-filter-off-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="schuelerFilter.kurs_toggle.value" value="kurs" name="Kurs" label="Kursfilter" :icon="false" />
					<svws-ui-radio-option v-model="schuelerFilter.fach_toggle.value" value="fach" name="Fach" label="Fachfilter" :icon="false" />
				</svws-ui-radio-group>
				<svws-ui-input-wrapper class="col-span-full" v-if="schuelerFilter.kurs_toggle.value === 'kurs'">
					<svws-ui-multi-select v-model="schuelerFilter.kurs" :items="schuelerFilter.getKurse()"
						:item-text="(kurs: GostBlockungKurs) => getErgebnismanager().getOfKursName(kurs.id) ?? ''" />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper :grid="2" class="col-span-full" v-if="schuelerFilter.fach_toggle.value === 'fach'">
					<svws-ui-multi-select title="Fach" v-model="fach" :items="faecherManager.faecher()" :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" />
					<svws-ui-multi-select title="Kursart" v-model="schuelerFilter.kursart" :items="GostKursart.values()" :item-text="(kursart: GostKursart) => kursart.kuerzel" removable />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="alle" name="AlleA" label="Alle" :icon="false" />
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="kollisionen" name="Kollisionen" label="K">
							<i-ri-alert-line />
						</svws-ui-radio-option>
						<template #content>
							Kollision
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="nichtwahlen" name="Nichtwahlen" label="NV">
							<i-ri-forbid-2-line />
						</svws-ui-radio-option>
						<template #content>
							Nichtverteilt
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/NV">
							<i-ri-alert-fill />
						</svws-ui-radio-option>
						<template #content>
							Kollision und Nichtverteilt
						</template>
					</svws-ui-tooltip>
				</svws-ui-radio-group>
			</template>
			<template #header(schuelerAuswahl)>
				<span class="flex justify-between w-full">
					<span>Schüler</span>
					<span title="Anzahl" class="opacity-50">{{ schuelerFilter.filtered.value.length }} {{ weitere }}</span>
				</span>
			</template>
			<template #header(schriftlichkeit)>
				<svws-ui-tooltip>
					<i-ri-pen-nib-line class="w-5 -ml-0.5" />
					<template #content>
						Wahl: Schriftlich oder Mündlich
					</template>
				</svws-ui-tooltip>
			</template>
			<template #header(geschlecht)>
				<svws-ui-tooltip>
					<i-ri-user-4-line class="w-5 -ml-0.5" />
					<template #content>
						Geschlecht
					</template>
				</svws-ui-tooltip>
			</template>
			<template #header(status)>
				<svws-ui-tooltip>
					<i-ri-more-line class="w-5 -ml-0.5" />
					<template #content>
						Status (Kollision, Nichtverteilung)
					</template>
				</svws-ui-tooltip>
			</template>
			<template #body>
				<div role="row" class="data-table__tr data-table__tbody__tr" :class="{'data-table__tr--clicked': selected === s}"
					v-for="(s, index) in schuelerFilter.filtered.value" @click="selected = s" :key="index">
					<div role="cell" class="data-table__td">
						<div class="flex flex-col py-0.5">
							<span>
								{{ `${s.nachname}, ${s.vorname}` }}
							</span>
							<template v-if="s.status !== 2">
								<span class="mt-0.5 text-sm">({{ SchuelerStatus.fromID(s.status)?.bezeichnung || '' }})</span>
							</template>
						</div>
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__no-padding">
						<span v-if="istSchriftlich(s.id)">{{ istSchriftlich(s.id) }}</span>
						<span v-else class="opacity-25">–</span>
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__no-padding">{{ s.geschlecht }}</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__no-padding">
						<div class="leading-none overflow-hidden w-5" style="margin-bottom: -0.1em;" :class="{ 'text-error': kollision(s.id).value, 'text-black': !kollision(s.id).value, }">
							<svws-ui-tooltip v-if="kollision(s.id).value && !nichtwahl(s.id).value" color="danger">
								<i-ri-alert-line /> <template #content> Kollision </template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="!kollision(s.id).value && nichtwahl(s.id).value">
								<i-ri-forbid-2-line /> <template #content> Nichtverteilt </template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="kollision(s.id).value && nichtwahl(s.id).value" color="danger">
								<i-ri-alert-fill /> <template #content> Kollision und Nichtverteilt </template>
							</svws-ui-tooltip>
							<div v-else class="icon opacity-25"> <i-ri-check-fill /> </div>
						</div>
					</div>
				</div>
			</template>
			<template #footer>
				<div role="row" class="data-table__tr data-table__tfoot__tr">
					<div class="data-table__th data-table__tfoot__th data-table__tfoot-count text-sm font-medium h-auto my-auto w-full">
						<div class="grid grid-cols-4 w-full font-bold">
							<span class="col-span-2" :class="{'opacity-50 font-medium': !schuelerFilter.statistics.value.schriftlich}">Schriftlich: {{ schuelerFilter.statistics.value.schriftlich }}</span>
							<span class="col-span-2" :class="{'opacity-50 font-medium': !schuelerFilter.statistics.value.schriftlich}">Mündlich: {{ schuelerFilter.statistics.value.muendlich }}</span>
							<span v-if="schuelerFilter.statistics.value.m">m: {{ schuelerFilter.statistics.value.m }}</span>
							<span v-if="schuelerFilter.statistics.value.w">w: {{ schuelerFilter.statistics.value.w }}</span>
							<span v-if="schuelerFilter.statistics.value.d">d: {{ schuelerFilter.statistics.value.d }}</span>
							<span v-if="schuelerFilter.statistics.value.x">x: {{ schuelerFilter.statistics.value.x }}</span>
						</div>
					</div>
				</div>
			</template>
		</svws-ui-data-table>
		<s-gost-kursplanung-ungueltige-kurswahl-modal v-if="props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().size()" :get-ergebnismanager="getErgebnismanager" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostFach, SchuelerListeEintrag } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import type { KursplanungSchuelerAuswahlProps } from "./SGostKursplanungSchuelerAuswahlProps";
	import { GostKursart, SchuelerStatus } from "@core";
	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<KursplanungSchuelerAuswahlProps>();

	const fach: WritableComputedRef<GostFach|undefined> = computed({
		get: () => {
			for (const fach of props.faecherManager.faecher())
				if (fach.id === props.schuelerFilter.fach)
					return fach;
			return undefined
		},
		set: (value) => props.schuelerFilter.fach = value?.id
	})

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.schueler,
		set: (value) => { if (value !== undefined) void props.setSchueler(value); }
	});

	const kollision = (idSchueler: number) : ComputedRef<boolean> => computed(() => {
		const kursid = props.schuelerFilter.kurs?.id;
		if (kursid === undefined)
			return props.getErgebnismanager().getOfSchuelerHatKollision(idSchueler);
		return props.getErgebnismanager().getOfSchuelerOfKursHatKollision(idSchueler, kursid);
	});

	const nichtwahl = (idSchueler: number) : ComputedRef<boolean> => computed(() =>
		props.getErgebnismanager().getOfSchuelerHatNichtwahl(idSchueler)
	);

	const weitere = computed(()=>{
		if (!props.schuelerFilter.kurs)
			return '';
		const anzahl = props.getErgebnismanager().getOfKursAnzahlSchuelerDummy(props.schuelerFilter.kurs.id);
		return anzahl > 0 ? `+${anzahl} weitere`:''
	})

	function istSchriftlich(id: number) {
		if (fach.value !== undefined || props.schuelerFilter.kurs !== undefined) {
			const fachId = fach.value?.id || props.schuelerFilter.kurs?.fach_id
			if (fachId !== undefined)
				return props.getErgebnismanager().getParent()?.schuelerGetOfFachFachwahl(id, fachId).istSchriftlich ? 's':'m';
		}
		return '';
	}

	const cols: DataTableColumn[] = [
		{key: 'schuelerAuswahl', label: 'Schüler', span: 1},
		{key: 'schriftlichkeit', label: 'W', tooltip: 'Wahl: schriftlich oder mündlich', fixedWidth: 2, align: "center"},
		{key: 'geschlecht', label: 'G', tooltip: "Geschlecht", fixedWidth: 2, align: "center"},
		{key: 'status', label: '  ', fixedWidth: 2},
	]

</script>

<style lang="postcss">
  .data-table {
    .data-table__th__schuelerAuswahl .data-table__th-title {
      @apply w-full;
    }
  }
</style>
