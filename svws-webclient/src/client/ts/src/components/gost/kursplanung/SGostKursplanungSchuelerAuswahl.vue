<template>
	<svws-ui-content-card overflow-scroll class="-mt-0.5">
		<svws-ui-data-table :model-value="schuelerFilter.filtered.value" v-model:clicked="selected" clickable :items="undefined"
			:filter="true" :filter-reverse="false" :filter-hide="false" :filter-open="true"
			:no-data="schuelerFilter.filtered.value.length <= 0" no-data-html="Keine Schüler zu diesem Filter gefunden.">
			<template #search>
				<div class="mb-1 3xl:mb-0.5">
					<svws-ui-text-input type="search" v-model="schuelerFilter.name" placeholder="Suche" />
				</div>
			</template>
			<template #filter>
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="schuelerFilter.alle_toggle.value" value="alle" name="Alle" label="Alle" :icon="false" />
					<svws-ui-radio-option v-model="schuelerFilter.kurs_toggle.value" value="kurs" name="Kurs" label="Kursfilter">
						<i-ri-filter-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="schuelerFilter.fach_toggle.value" value="fach" name="Fach" label="Fachfilter">
						<i-ri-filter-line />
					</svws-ui-radio-option>
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
					<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="alle" name="Alle" label="Alle" :icon="false" />
					<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="kollisionen" name="Kollisionen" label="Kollisionen">
						<i-ri-alert-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="nichtwahlen" name="Nichtwahlen" label="Nichtverteilt">
						<i-ri-forbid-2-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="schuelerFilter.radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/N">
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
			<template #footer>
				Schriftlichkeit:
				<svws-ui-tooltip>
					<span class="badge badge--light badge--lg"> s: {{ schuelerFilter.statistics.value.schriftlich }}</span>
					<template #content>schriftlich</template>
				</svws-ui-tooltip>
				<svws-ui-tooltip>
					<span class="badge badge--light badge--lg"> m: {{ schuelerFilter.statistics.value.muendlich }}</span>
					<template #content>muendlich</template>
				</svws-ui-tooltip>
				<br>
				Geschlecht:
				<svws-ui-tooltip>
					<span class="badge badge--light badge--lg"> m: {{ schuelerFilter.statistics.value.m }}</span>
					<template #content>männlich</template>
				</svws-ui-tooltip>
				<svws-ui-tooltip>
					<span class="badge badge--light badge--lg"> w: {{ schuelerFilter.statistics.value.w }}</span>
					<template #content>weiblich</template>
				</svws-ui-tooltip>
				<svws-ui-tooltip v-if="schuelerFilter.statistics.value.d">
					<span class="badge badge--light badge--lg"> d: {{ schuelerFilter.statistics.value.d }}</span>
					<template #content>divers</template>
				</svws-ui-tooltip>
				<svws-ui-tooltip v-if="schuelerFilter.statistics.value.x">
					<span class="badge badge--light badge--lg"> x: {{ schuelerFilter.statistics.value.x }}</span>
					<template #content>nicht angegeben</template>
				</svws-ui-tooltip>
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

</script>
