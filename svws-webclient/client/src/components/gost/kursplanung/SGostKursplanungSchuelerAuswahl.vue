<template>
	<svws-ui-content-card class="-mt-0.5 s-gost-kursplanung-schueler-auswahl" overflow-scroll>
		<svws-ui-table :model-value="schuelerFilter.filtered.value" v-model:clicked="selected" clickable scroll :items="undefined" :filter-hide="false" :filter-open="true" :columns="fach !== undefined || schuelerFilter.kurs !== undefined ? cols : colsOhneSchriftlichkeit" :no-data="schuelerFilter.filtered.value.length <= 0" :disable-footer="schuelerFilter.filtered.value.length <= 0">
			<template #search>
				<svws-ui-text-input type="search" v-model="schuelerFilter.name" placeholder="Suche" />
			</template>
			<template #filterAdvanced>
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="schuelerFilter.alle_toggle.value" value="alle" name="Alle" label="">
						<i-ri-filter-off-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="schuelerFilter.kurs_toggle.value" value="kurs" name="Kurs" label="Kursfilter" :icon="false" />
					<svws-ui-radio-option v-model="schuelerFilter.fach_toggle.value" value="fach" name="Fach" label="Fachfilter" :icon="false" />
				</svws-ui-radio-group>
				<svws-ui-input-wrapper class="col-span-full" v-if="schuelerFilter.kurs_toggle.value === 'kurs'">
					<svws-ui-select v-model="schuelerFilter.kurs" :items="schuelerFilter.getKurse()"
						:item-text="(kurs: GostBlockungKurs) => getErgebnismanager().getOfKursName(kurs.id) ?? ''" />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper :grid="2" class="col-span-full" v-if="schuelerFilter.fach_toggle.value === 'fach'">
					<svws-ui-select title="Fach" v-model="fach" :items="faecherManager.faecher()" :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" />
					<svws-ui-select title="Kursart" v-model="schuelerFilter.kursart" :items="GostKursart.values()" :item-text="(kursart: GostKursart) => kursart.kuerzel" removable />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
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
							<i-ri-spam-3-line />
						</svws-ui-radio-option>
						<template #content>
							Nichtverteilt
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/NV">
							<i-ri-error-warning-fill />
						</svws-ui-radio-option>
						<template #content>
							Kollision und Nichtverteilt
						</template>
					</svws-ui-tooltip>
				</svws-ui-radio-group>
			</template>
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td col-span-full" role="columnheader">
						<svws-ui-tooltip indicator="help" class="flex-grow">
							<span v-if="schuelerFilter.filtered.value.length > 0">{{ schuelerFilter.filtered.value.length }} {{ weitere }}</span>
							<span> Schüler</span>
							<span class="flex-grow" />
							<template #content>
								<ul class="flex flex-col gap-3 py-1">
									<li class="flex flex-col">
										<span class="text-sm font-bold">Geschlecht</span>
										<span>m, w, d, x</span>
									</li>
									<li v-if="schuelerFilter.kurs !== undefined" class="flex flex-col">
										<span class="text-sm font-bold">Wahl</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-pen-nib-line /> Schriftlich</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-chat1-line class="opacity-75" /> Mündlich</span>
									</li>
									<li class="flex flex-col">
										<span class="text-sm font-bold">Status</span>
										<span class="inline-flex gap-0.5 -mb-0.5 items-center"><span class="opacity-50 text-sm w-4 text-center pt-[0.1rem]">&check;</span> Gültig</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-alert-line /> Kollision</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-spam3-line /> Nichtverteilung</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-error-warning-fill /> K/NV</span>
									</li>
								</ul>
							</template>
						</svws-ui-tooltip>
					</div>
				</div>
			</template>
			<template #body>
				<div role="row" class="svws-ui-tr" :class="{'svws-clicked': selected === s}"
					v-for="(s, index) in schuelerFilter.filtered.value" @click="selected = s" :key="index">
					<div role="cell" class="svws-ui-td">
						<div class="flex flex-col">
							<span>
								{{ `${s.nachname}, ${s.vorname}` }}
							</span>
							<template v-if="s.status !== 2">
								<span class="mt-0.5 text-sm">({{ SchuelerStatus.fromID(s.status)?.bezeichnung || '' }})</span>
							</template>
						</div>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center pl-0">
						<span class="w-full text-center">{{ s.geschlecht }}</span>
					</div>
					<div v-if="istSchriftlich(s.id)" role="cell" class="svws-ui-td svws-align-center">
						<span>
							<i-ri-pen-nib-line v-if="istSchriftlich(s.id) === 's'" class="w-5 -my-0.5" />
							<i-ri-chat1-line v-if="istSchriftlich(s.id) === 'm'" class="w-5 -my-0.5 opacity-75" />
						</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center pr-0">
						<div class="leading-none w-5 -mb-1" :class="{ 'text-error': kollision(s.id).value, 'text-black': !kollision(s.id).value && selected !== s, }">
							<svws-ui-tooltip v-if="kollision(s.id).value && !nichtwahl(s.id).value" color="danger">
								<i-ri-alert-line /> <template #content> Kollision </template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="!kollision(s.id).value && nichtwahl(s.id).value">
								<i-ri-spam-3-line /> <template #content> Nichtverteilt </template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="kollision(s.id).value && nichtwahl(s.id).value" color="danger">
								<i-ri-error-warning-fill /> <template #content> Kollision und Nichtverteilt </template>
							</svws-ui-tooltip>
							<span v-else class="opacity-25 text-sm">&check;</span>
						</div>
					</div>
				</div>
			</template>
			<template #footer>
				<div role="row" class="svws-ui-tr">
					<div class="svws-ui-td col-span-full w-full">
						<div class="grid grid-cols-4 w-full gap-y-0.5 text-button font-medium py-1">
							<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter.statistics.value.schriftlich}">
								<i-ri-pen-nib-line class="w-5 -m-0.5 mr-0.5" />Schriftlich: {{ schuelerFilter.statistics.value.schriftlich }}
							</span>
							<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter.statistics.value.muendlich}">
								<i-ri-chat1-line class="w-5 opacity-75 -m-0.5 mr-0.5" /> Mündlich: {{ schuelerFilter.statistics.value.muendlich }}
							</span>
							<span v-if="schuelerFilter.statistics.value.m">m: {{ schuelerFilter.statistics.value.m }}</span>
							<span v-if="schuelerFilter.statistics.value.w">w: {{ schuelerFilter.statistics.value.w }}</span>
							<span v-if="schuelerFilter.statistics.value.d">d: {{ schuelerFilter.statistics.value.d }}</span>
							<span v-if="schuelerFilter.statistics.value.x">x: {{ schuelerFilter.statistics.value.x }}</span>
						</div>
					</div>
				</div>
			</template>
		</svws-ui-table>
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
		{key: 'geschlecht', label: 'G', tooltip: "Geschlecht", fixedWidth: 2, align: "center"},
		{key: 'schriftlichkeit', label: 'W', tooltip: 'Wahl: schriftlich oder mündlich', fixedWidth: 2, align: "center"},
		{key: 'status', label: '  ', fixedWidth: 2},
	]

	const colsOhneSchriftlichkeit: DataTableColumn[] = [
		{key: 'schuelerAuswahl', label: 'Schüler', span: 1},
		{key: 'geschlecht', label: 'G', tooltip: "Geschlecht", fixedWidth: 2, align: "center"},
		{key: 'status', label: '  ', fixedWidth: 2},
	]

</script>
