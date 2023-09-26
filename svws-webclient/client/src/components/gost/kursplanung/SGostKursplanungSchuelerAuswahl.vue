<template>
	<svws-ui-content-card class="-mt-0.5 s-gost-kursplanung-schueler-auswahl" overflow-scroll>
		<svws-ui-table :model-value="schuelerFilter.filtered.value" v-model:clicked="selected" clickable scroll :items="undefined" :filtered="schuelerFilter.kurs_toggle.value === 'kurs' || schuelerFilter.fach_toggle.value === 'fach' || schuelerFilter.radio_filter !== 'alle'" :columns="cols" :no-data="schuelerFilter.filtered.value.length <= 0" :disable-footer="schuelerFilter.filtered.value.length <= 0">
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
				<svws-ui-input-wrapper class="col-span-full" v-if="schuelerFilter.alle_toggle.value === 'alle'">
					<svws-ui-select disabled />
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
						<svws-ui-tooltip v-if="schuelerFilter.filtered.value.length > 0">
							<i-ri-information-line class="-my-0.5 -ml-0.5" />
							<template #content>
								<ul class="flex flex-col gap-3 py-1">
									<li class="flex flex-col">
										<span class="text-sm font-bold mb-0.5">Status</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-alert-line /> Kollision</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-spam3-line /> Nichtverteilung</span>
										<span class="inline-flex gap-0.5 items-center"><i-ri-error-warning-fill /> K/NV</span>
									</li>
								</ul>
							</template>
						</svws-ui-tooltip>
						<span v-if="schuelerFilter.filtered.value.length > 0">{{ schuelerFilter.filtered.value.length }} {{ weitere }}</span>
						<span>Schüler</span>
					</div>
				</div>
			</template>
			<template #body>
				<div role="row" class="svws-ui-tr" :class="{'svws-clicked': selected === s}"
					v-for="(s, index) in schuelerFilter.filtered.value" @click="selected = s" :key="index">
					<div role="cell" class="svws-ui-td svws-align-center pr-0">
						<div class="leading-none w-5 -mb-1" :class="{ 'text-error': kollision(s.id).value, 'text-black': !kollision(s.id).value && selected !== s, }">
							<svws-ui-tooltip v-if="kollision(s.id).value && !nichtwahl(s.id).value" color="danger">
								<i-ri-alert-line /> <template #content> Kollision </template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="!kollision(s.id).value && nichtwahl(s.id).value">
								<i-ri-spam-3-line class="opacity-75" /> <template #content> Nichtverteilt </template>
							</svws-ui-tooltip>
							<svws-ui-tooltip v-else-if="kollision(s.id).value && nichtwahl(s.id).value" color="danger">
								<i-ri-error-warning-fill /> <template #content> Kollision und Nichtverteilt </template>
							</svws-ui-tooltip>
						</div>
					</div>
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
					<div v-if="geschlechtVisible" role="cell" class="svws-ui-td svws-align-center pl-0">
						<span class="w-full text-center">{{ s.geschlecht }}</span>
					</div>
					<div v-if="istSchriftlich(s.id)" role="cell" class="svws-ui-td svws-align-center">
						<span>
							<i-ri-draft-line v-if="istSchriftlich(s.id) === 's'" class="w-5 -my-0.5" />
							<i-ri-chat1-line v-if="istSchriftlich(s.id) === 'm'" class="w-5 -my-0.5 opacity-75" />
						</span>
					</div>
				</div>
			</template>
			<template #footer>
				<div role="row" class="svws-ui-tr">
					<div class="svws-ui-td col-span-full w-full">
						<div class="grid grid-cols-4 w-full gap-y-2 text-button font-medium py-1 normal-nums pl-5" :class="fach !== undefined || schuelerFilter.kurs !== undefined ? 'pt-2' : 'py-1'">
							<template v-if="fach !== undefined || schuelerFilter.kurs !== undefined">
								<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter.statistics.value.schriftlich}">
									<i-ri-draft-line class="w-5 -m-0.5 mr-0.5" />{{ schuelerFilter.statistics.value.schriftlich }} schriftlich
								</span>
								<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter.statistics.value.muendlich}">
									<i-ri-chat1-line class="w-5 opacity-75 -m-0.5 mr-0.5" />{{ schuelerFilter.statistics.value.muendlich }} mündlich
								</span>
							</template>
							<div class="col-span-full flex items-center gap-1">
								<svws-ui-checkbox type="toggle" v-model="geschlechtVisible" :title="geschlechtVisible ? 'Geschlecht in der Tabelle ausblenden' : 'Geschlecht in der Tabelle einblenden'">
									<span class="text-button font-medium">Geschlecht: </span>
								</svws-ui-checkbox>
								<span v-if="schuelerFilter.statistics.value.m">{{ schuelerFilter.statistics.value.m }} m<span v-if="schuelerFilter.statistics.value.w || schuelerFilter.statistics.value.d || schuelerFilter.statistics.value.x">, </span></span>
								<span v-if="schuelerFilter.statistics.value.w">{{ schuelerFilter.statistics.value.w }} w<span v-if="schuelerFilter.statistics.value.d || schuelerFilter.statistics.value.x">, </span></span>
								<span v-if="schuelerFilter.statistics.value.d">{{ schuelerFilter.statistics.value.d }} d<span v-if="schuelerFilter.statistics.value.x">, </span></span>
								<span v-if="schuelerFilter.statistics.value.x">{{ schuelerFilter.statistics.value.x }} x</span>
							</div>
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
	import { computed, ref } from "vue";
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

	const geschlechtVisible = ref(false)

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{key: 'status', label: '  ', fixedWidth: 1.75},
			{key: 'schuelerAuswahl', label: 'Schüler', span: 1},
		];

		if (geschlechtVisible.value) {
			cols.push({key: 'geschlecht', label: 'G', tooltip: "Geschlecht", fixedWidth: 2, align: "center"});
		}

		if (fach.value !== undefined || props.schuelerFilter.kurs !== undefined) {
			cols.push({key: 'schriftlichkeit', label: 'W', tooltip: 'Wahl: schriftlich oder mündlich', fixedWidth: 2, align: "center"});
		}

		return cols;
	}

	const cols = computed(() => calculateColumns());

</script>
