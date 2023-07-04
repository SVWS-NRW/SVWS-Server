<template>
	<svws-ui-content-card overflow-scroll class="-mt-0.5">
		<svws-ui-data-table :model-value="filtered" v-model:clicked="selected" clickable :items="undefined"
			:filter="true" :filter-reverse="false"
			:no-data="schuelerFilter.filtered.value.length <= 0" no-data-html="Keine Schüler zu diesem Filter gefunden.">
			<template #search>
				<div class="mb-1 3xl:mb-0.5">
					<svws-ui-text-input type="search" v-model="schuelerFilter.name.value" placeholder="Suche" />
				</div>
			</template>
			<template #filter>
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="kurs_filter_toggle" :value="!kurs_filter_toggle" name="Filter" label="Kursfilter" :force-checked="kurs_filter_toggle ?? false">
						<i-ri-filter-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="fach_filter_toggle" :value="!fach_filter_toggle" name="FilterFa" label="Fachfilter" :force-checked="fach_filter_toggle ?? false">
						<i-ri-filter-line />
					</svws-ui-radio-option>
				</svws-ui-radio-group>
				<svws-ui-input-wrapper class="col-span-full" v-if="kurs_filter_toggle">
					<svws-ui-multi-select v-model="schuelerFilter.kurs.value" :items="schuelerFilter.getKurse()"
						:item-text="(kurs: GostBlockungKurs) => getErgebnismanager().getOfKursName(kurs.id) ?? ''" />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper :grid="2" class="col-span-full" v-if="fach_filter_toggle">
					<svws-ui-multi-select title="Fach" v-model="fach" :items="faecherManager.faecher()" :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" />
					<svws-ui-multi-select title="Kursart" v-model="schuelerFilter.kursart.value" :items="GostKursart.values()" :item-text="(kursart: GostKursart) => kursart.kuerzel" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="radio_filter" value="alle" name="Alle" label="Alle" :icon="false" />
					<svws-ui-radio-option v-model="radio_filter" value="kollisionen" name="Kollisionen" label="Kollisionen">
						<i-ri-alert-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="radio_filter" value="nichtwahlen" name="Nichtwahlen" label="Nichtverteilt">
						<i-ri-forbid-2-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/N">
						<i-ri-alert-fill />
					</svws-ui-radio-option>
				</svws-ui-radio-group>
			</template>
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr">
					<div role="columnheader" class="data-table__th data-table__thead__th">
						<div class="inline-flex items-center gap-2">
							<span>Schülerauswahl</span>
							<svws-ui-badge size="big">
								{{ schuelerFilter.filtered.value.length }}
							</svws-ui-badge>
						</div>
					</div>
				</div>
			</template>
			<template #body>
				<div role="row" class="data-table__tr data-table__tbody__tr" :class="{'data-table__tr--clicked': selected === s}"
					v-for="(s, index) in schuelerFilter.filtered.value" @click="selected = s" :key="index">
					<div role="cell" class="data-table__td">
						<div class="flex items-center justify-between gap-1 w-full">
							<span>
								{{ `${s.nachname}, ${s.vorname}` }}
							</span>
							<div class="flex items-center gap-1 cursor-pointer">
								<svws-ui-tooltip v-if="s.status !== 2">
									<span class="badge badge--light badge--lg badge--short">{{ SchuelerStatus.fromID(s.status)?.bezeichnung }}</span>
									<template #content>{{ SchuelerStatus.fromID(s.status)?.bezeichnung }}</template>
								</svws-ui-tooltip>
								<svws-ui-tooltip>
									<span class="badge badge--light badge--lg">{{ s.geschlecht }}</span>
									<template #content>{{ s.geschlecht }}</template>
								</svws-ui-tooltip>
								<div class="leading-none overflow-hidden w-5" style="margin-bottom: -0.1em;" :class="{ 'text-error': kollision(s.id).value, 'text-black': !kollision(s.id).value, }">
									<svws-ui-tooltip v-if="kollision(s.id).value && !nichtwahl(s.id).value">
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
					</div>
				</div>
			</template>
		</svws-ui-data-table>
		<s-gost-kursplanung-ungueltige-kurswahl-modal v-if="props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().size()" :get-ergebnismanager="getErgebnismanager" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostFach, SchuelerListeEintrag } from "@core";
	import type { KursplanungSchuelerAuswahlProps } from "./SGostKursplanungSchuelerAuswahlProps";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { GostKursart, SchuelerStatus } from "@core";
	import { computed } from "vue";

	const props = defineProps<KursplanungSchuelerAuswahlProps>();

	const kurs_filter_toggle = props.schuelerFilter.kurs_filter_toggle();
	const fach_filter_toggle = props.schuelerFilter.fach_filter_toggle();
	const radio_filter = props.schuelerFilter.radio_filter();

	const fach: WritableComputedRef<GostFach|undefined> = computed({
		get: () => {
			for (const fach of props.faecherManager.faecher())
				if (fach.id === props.schuelerFilter.fach.value)
					return fach;
			return undefined
		},
		set: (value) => props.schuelerFilter.fach.value = value?.id
	})

	const filtered = computed(()=> [...props.schuelerFilter.filtered.value.values()]);

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.schueler,
		set: (value) => { if (value !== undefined) void props.setSchueler(value); }
	});

	const kollision = (idSchueler: number) : ComputedRef<boolean> => computed(() => {
		const kursid = props.schuelerFilter.kurs.value?.id;
		if (kursid === undefined)
			return props.getErgebnismanager().getOfSchuelerHatKollision(idSchueler);
		return props.getErgebnismanager().getOfKursSchuelermengeMitKollisionen(kursid).contains(idSchueler);
	});

	const nichtwahl = (idSchueler: number) : ComputedRef<boolean> => computed(() =>
		props.getErgebnismanager().getOfSchuelerHatNichtwahl(idSchueler)
	);

</script>
