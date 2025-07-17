<template>
	<div v-if="hatBlockung && hatErgebnis" class="-mt-0.5 s-gost-kursplanung-schueler-auswahl flex flex-col" overflow-scroll>
		<svws-ui-table :model-value="schuelerFilter().filtered.value" v-model:clicked="selected" clickable scroll :items="undefined"
			:filter-open="isSchuelerFilterOpen()" @update:filter-open="setIsSchuelerFilterOpen"
			:filtered="(schuelerFilter().kurs_toggle.value === 'kurs') || (schuelerFilter().fach_toggle.value === 'fach') || (schuelerFilter().radio_filter !== 'alle')"
			:columns :no-data="schuelerFilter().filtered.value.length <= 0" :disable-footer="schuelerFilter().filtered.value.length <= 0">
			<template #search>
				<svws-ui-text-input type="search" v-model="schuelerFilter().name" placeholder="Suche" removable />
			</template>
			<template #filterAdvanced>
				<div class="radio radio--row flex flex-row">
					<svws-ui-radio-option v-model="schuelerFilter().alle_toggle.value" value="alle" name="Alle" label="&ZeroWidthSpace;">
						<span class="icon ml-0.5 -mt-0.5 i-ri-filter-off-line" />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="schuelerFilter().fach_toggle.value" value="fach" name="Fach" label="Fachfilter" :icon="false" />
					<svws-ui-radio-option v-model="schuelerFilter().kurs_toggle.value" value="kurs" name="Kurs" label="Kursfilter" :icon="false" />
				</div>
				<div class="input-wrapper flex flex-row" v-if="schuelerFilter().kurs_toggle.value === 'kurs'">
					<svws-ui-select v-model="schuelerFilter().kurs" :items="schuelerFilter().getKurse()"
						:item-text="kurs => getErgebnismanager().getOfKursName(kurs.id) ?? ''" />
					<div class="svws-ui-spacing" />
				</div>
				<div :grid="2" class="input-wrapper input-wrapper--2 flex flex-row" v-if="schuelerFilter().fach_toggle.value === 'fach'">
					<svws-ui-select title="Fach" v-model="fach" :items="faecherManager.faecher()" :item-text="fach => fach.bezeichnung ?? ''" />
					<svws-ui-select title="Kursart" v-model="schuelerFilter().kursart" :items="GostKursart.values()" :item-text="kursart => kursart.kuerzel" removable />
					<div class="svws-ui-spacing" />
				</div>
				<div class="input-wrapper flex flex-row" v-if="schuelerFilter().alle_toggle.value === 'alle'">
					<svws-ui-select disabled :model-value="[]" :items="[]" :item-text="() => ''" />
					<div class="svws-ui-spacing" />
				</div>
				<div class="radio radio--row flex flex-row">
					<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="alle" name="Alle" label="Alle" :icon="false" />
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="kollisionen" name="Kollisionen" label="K">
							<span class="icon-sm i-ri-alert-line" />
						</svws-ui-radio-option>
						<template #content>
							Kollision
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="nichtwahlen" name="Nichtwahlen" label="NV">
							<span class="icon-sm i-ri-spam-3-line" />
						</svws-ui-radio-option>
						<template #content>
							Nichtverteilt
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip>
						<svws-ui-radio-option v-model="schuelerFilter().radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/NV">
							<span class="icon-sm i-ri-error-warning-fill" />
						</svws-ui-radio-option>
						<template #content>
							Kollision und Nichtverteilt
						</template>
					</svws-ui-tooltip>
				</div>
			</template>
			<template #header>
				<div class="svws-ui-tr" role="row" :style="gridTemplateColumns">
					<div class="svws-ui-td col-span-full" role="columnheader">
						<svws-ui-tooltip v-if="schuelerFilter().filtered.value.length > 0">
							<span class="icon i-ri-information-line" />
							<template #content>
								<ul class="flex flex-col gap-3 py-1">
									<li class="flex flex-col">
										<span class="font-bold mb-0.5">Status</span>
										<span class="inline-flex gap-0.5 items-center"><span class="icon-sm i-ri-alert-line" /> Kollision</span>
										<span class="inline-flex gap-0.5 items-center"><span class="icon-sm i-ri-spam-3-line" /> Nichtverteilung</span>
										<span class="inline-flex gap-0.5 items-center"><span class="icon-sm i-ri-error-warning-fill" /> Kollision und Nichtverteilung</span>
									</li>
								</ul>
							</template>
						</svws-ui-tooltip>
						<span v-if="schuelerFilter().filtered.value.length > 0" class="pl-8">{{ schuelerFilter().filtered.value.length }} {{ weitere }}</span>
						<span>Schüler</span>
					</div>
				</div>
			</template>
			<template #body>
				<template v-for="(s, index) in schuelerFilter().filtered.value" :key="index">
					<div role="row" class="svws-ui-tr" :class="{'svws-clicked': selected === s}" :style="gridTemplateColumns" @click="selected = s">
						<div role="cell" class="svws-ui-td svws-align-center pr-0">
							<div class="leading-none w-5 -mb-1" :class="{ 'text-ui-danger': kollision(s.id).value, 'text-black': !kollision(s.id).value && (selected !== s), }">
								<svws-ui-tooltip v-if="kollision(s.id).value && !nichtwahl(s.id).value" color="danger">
									<span class="icon icon-ui-danger i-ri-alert-line" />
									<template #content>
										Kollisionen:
										<ul>
											<li v-for="k of getErgebnismanager().getOfSchuelerKursmengeMitKollisionen(s.id).toArray(new Array<GostBlockungsergebnisKurs>())" :key="k.id" class="pl-2">{{ getErgebnismanager().getOfKursName(k.id) }}</li>
										</ul>
									</template>
								</svws-ui-tooltip>
								<svws-ui-tooltip v-else-if="!kollision(s.id).value && nichtwahl(s.id).value">
									<span class="icon i-ri-spam-3-line" /> <template #content>
										Nichtverteilt
										<ul>
											<li v-for="f of getErgebnismanager().getOfSchuelerFachwahlmengeOhneKurszuordnung(s.id).toArray(new Array<GostFachwahl>())" :key="f.fachID" class="pl-2">{{ getErgebnismanager().getFach(f.fachID).bezeichnung }}</li>
										</ul>
									</template>
								</svws-ui-tooltip>
								<svws-ui-tooltip v-else-if="kollision(s.id).value && nichtwahl(s.id).value" color="danger">
									<span class="icon icon-ui-danger i-ri-error-warning-fill" /> <template #content>
										<b>Kollision und Nichtverteilt:</b>
										<br>Kollisionen:
										<ul>
											<li v-for="k of getErgebnismanager().getOfSchuelerKursmengeMitKollisionen(s.id).toArray(new Array<GostBlockungsergebnisKurs>())" :key="k.id" class="pl-2">{{ getErgebnismanager().getOfKursName(k.id) }}</li>
										</ul>
										Nichtverteilt:
										<ul>
											<li v-for="f of getErgebnismanager().getOfSchuelerFachwahlmengeOhneKurszuordnung(s.id).toArray(new Array<GostFachwahl>())" :key="f.fachID" class="pl-2">{{ getErgebnismanager().getFach(f.fachID).bezeichnung }}</li>
										</ul>
									</template>
								</svws-ui-tooltip>
							</div>
						</div>
						<div role="cell" class="svws-ui-td svws-align-center" @click="event => fixieren_regel_toggle(fach?.id ?? schuelerFilter().kurs?.fach_id, s.id, event)">
							<template v-if="fixierRegeln.get(s.id) === undefined">
								<span class="icon i-ri-pushpin-line -my-0.5 opacity-0 hover:opacity-75" v-if="!((fach === undefined) && (schuelerFilter().kurs === undefined))" />
							</template>
							<template v-else>
								<template v-if="fach !== undefined">
									<span v-if="fixierRegelFach(fach?.id, s.id).value" class="icon i-ri-pushpin-fill -my-0.5 hover:opacity-75" />
									<span v-else class="icon i-ri-pushpin-line -my-0.5 opacity-50 hover:opacity-75" />
								</template>
								<template v-else-if="schuelerFilter().kurs !== undefined">
									<span v-if="fixierRegelKurs(schuelerFilter().kurs?.id, s.id).value" class="icon i-ri-pushpin-fill -my-0.5 hover:opacity-75" />
									<span v-else class="icon i-ri-pushpin-line -my-0.5 opacity-50 hover:opacity-75" />
								</template>
								<template v-else>
									<span class="icon i-ri-pushpin-line -my-0.5 opacity-50" />
								</template>
							</template>
						</div>
						<div role="cell" class="svws-ui-td">
							<div class="flex flex-row">
								{{ s.nachname }}, {{ s.vorname }}
								<template v-if="getDatenmanager().schuelerGet(s.id).abschlussjahrgang !== getDatenmanager().daten().abijahrgang">
									<span class="ml-1">(Abi {{ getDatenmanager().schuelerGet(s.id).abschlussjahrgang }})</span>
								</template>
								<template v-if="s.status !== 2">
									<span class="ml-1">({{ SchuelerStatus.data().getWertByKuerzel("" + s.status)?.daten(schuljahr)?.text ?? '—' }}{{ s.externeSchulNr ? ` ${s.externeSchulNr}` : '' }})</span>
								</template>
							</div>
						</div>
						<div v-if="showGeschlecht()" role="cell" class="svws-ui-td svws-align-center pl-0">
							<span class="w-full text-center">{{ Geschlecht.fromValue(s.geschlecht)?.kuerzel }}</span>
						</div>
						<div v-if="fach !== undefined || schuelerFilter().kurs !== undefined" role="cell" class="svws-ui-td svws-align-center">
							<span>
								<span v-if="istSchriftlich(s.id) === 's'" class="icon i-ri-draft-line -my-0.5" />
								<span v-else-if="istSchriftlich(s.id) === 'm'" class="icon i-ri-chat-1-line -my-0.5 opacity-75" />
								<span v-else class="icon i-ri-question-mark -my-0.5 opacity-75" />
							</span>
						</div>
					</div>
				</template>
			</template>
			<template #footer>
				<div role="row" class="svws-ui-tr" :style="gridTemplateColumns">
					<div class="svws-ui-td col-span-full w-full">
						<div class="grid grid-cols-4 w-full gap-y-2 text-button font-medium py-1 normal-nums pl-5" :class="(fach !== undefined) || (schuelerFilter().kurs !== undefined) ? 'pt-2' : 'py-1'">
							<template v-if="(fach !== undefined) || (schuelerFilter().kurs !== undefined)">
								<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter().statistics.value.schriftlich}">
									<span class="icon i-ri-draft-line -m-0.5 mr-0.5" />{{ schuelerFilter().statistics.value.schriftlich }} schriftlich
								</span>
								<span class="col-span-2 inline-flex gap-0.5" :class="{'opacity-50 font-medium': !schuelerFilter().statistics.value.muendlich}">
									<span class="icon i-ri-chat-1-line opacity-75 -m-0.5 mr-0.5" />{{ schuelerFilter().statistics.value.muendlich }} mündlich
								</span>
							</template>
							<div class="col-span-full flex items-center gap-1">
								<svws-ui-checkbox type="toggle" :model-value="showGeschlecht()" @update:model-value="setShowGeschlecht" :title="showGeschlecht() ? 'Geschlecht in der Tabelle ausblenden' : 'Geschlecht in der Tabelle einblenden'">
									<span class="text-button font-medium">Geschlecht: </span>
								</svws-ui-checkbox>
								<span v-if="schuelerFilter().statistics.value.m">{{ schuelerFilter().statistics.value.m }} m<span v-if="schuelerFilter().statistics.value.w || schuelerFilter().statistics.value.d || schuelerFilter().statistics.value.x">, </span></span>
								<span v-if="schuelerFilter().statistics.value.w">{{ schuelerFilter().statistics.value.w }} w<span v-if="schuelerFilter().statistics.value.d || schuelerFilter().statistics.value.x">, </span></span>
								<span v-if="schuelerFilter().statistics.value.d">{{ schuelerFilter().statistics.value.d }} d<span v-if="schuelerFilter().statistics.value.x">, </span></span>
								<span v-if="schuelerFilter().statistics.value.x">{{ schuelerFilter().statistics.value.x }} x</span>
							</div>
						</div>
					</div>
				</div>
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { KursplanungSchuelerAuswahlProps } from "./SGostKursplanungSchuelerAuswahlProps";
	import type { GostBlockungsergebnisKurs, GostFach, GostFachwahl, Schueler } from "@core";
	import { Geschlecht, GostKursart, GostKursblockungRegelTyp, SchuelerStatus, SetUtils } from "@core";

	const props = defineProps<KursplanungSchuelerAuswahlProps>();

	const schuljahr = computed<number>(() => props.getDatenmanager().faecherManager().getSchuljahr());

	const fach = computed<GostFach|undefined>({
		get: () => {
			for (const fach of props.faecherManager.faecher())
				if (fach.id === props.schuelerFilter().fach)
					return fach;
			return undefined;
		},
		set: (value) => props.schuelerFilter().fach = value?.id,
	})

	const selected = computed<Schueler | undefined>({
		get: () => {
			if (props.schueler !== undefined && props.schuelerFilter().filtered.value.includes(props.schueler))
				return props.schueler
			if (props.schuelerFilter().filtered.value.length > 0) {
				void props.setSchueler(props.schuelerFilter().filtered.value[0])
			}
			return undefined;
		},
		set: (value) => {
			if (value !== undefined)
				void props.setSchueler(value);
		},
	});

	const kollision = (idSchueler: number) => computed<boolean>(() => {
		const kursid = props.schuelerFilter().kurs?.id;
		if (kursid === undefined)
			return props.getErgebnismanager().getOfSchuelerHatKollision(idSchueler);
		return props.getErgebnismanager().getOfSchuelerOfKursHatKollision(idSchueler, kursid);
	});

	const nichtwahl = (idSchueler: number) => computed<boolean>(() => props.getErgebnismanager().getOfSchuelerHatNichtwahl(idSchueler));

	const weitere = computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '';
		const anzahl = props.getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id);
		return anzahl > 0 ? `+${anzahl} weitere` : '';
	})


	function istSchriftlich(id: number) {
		const kurs = props.schuelerFilter().kurs;
		if ((fach.value === undefined) && (kurs === undefined))
			return undefined;
		let idFach : number;
		if (fach.value !== undefined)
			idFach = fach.value.id;
		else if (kurs !== undefined)
			idFach = kurs.fach_id;
		else
			return undefined;
		if (!props.getErgebnismanager().getParent().schuelerGetHatFach(id, idFach))
			return undefined;
		return props.getErgebnismanager().getParent().schuelerGetOfFachFachwahl(id, idFach).istSchriftlich ? 's':'m';
	}

	const columns = computed<DataTableColumn[]>(() => {
		const cols: DataTableColumn[] = [
			{key: 'status', label: '  ', fixedWidth: 1.75},
			{key: 'fixiert', label: 'F', tooltip: "Kursfixierung", fixedWidth: 2, align: "center"},
			{key: 'schuelerAuswahl', label: 'Schüler', span: 1},
		];
		if (props.showGeschlecht())
			cols.push({key: 'geschlecht', label: 'G', tooltip: "Geschlecht", fixedWidth: 2, align: "center"});
		if ((fach.value !== undefined) || (props.schuelerFilter().kurs !== undefined))
			cols.push({key: 'schriftlichkeit', label: 'W', tooltip: 'Wahl: schriftlich oder mündlich', fixedWidth: 2, align: "center"});
		return cols;
	})

	const gridTemplateColumns = computed<string> (() => "grid-template-columns: 1.75rem 2rem minmax(4rem, 1fr)" + (props.showGeschlecht() ? " 2rem" : "") +
		(((fach.value !== undefined) || (props.schuelerFilter().kurs !== undefined)) ? " 2rem" : ""));

	const fixierRegeln = computed(() => {
		const regeln = props.getDatenmanager().regelGetListe();
		const map = new Map<number, Set<number>>();
		for (const r of regeln)
			if (r.typ === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ) {
				const entry = map.get(r.parameter.get(0))
				if (entry !== undefined)
					entry.add(r.parameter.get(1));
				else
					map.set(r.parameter.get(0), new Set([r.parameter.get(1)]));
			}
		return map;
	})

	const fixierRegelKurs = (idKurs: number | undefined, idSchueler: number) => computed<boolean>(() =>
		(typeof idKurs === 'number') && props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs)
	);

	const fixierRegelFach = (idFach: number | undefined, idSchueler: number) => computed<boolean>(() => {
		if (idFach === undefined)
			return false
		const kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler, idFach);
		const idKurs = kurs?.id;
		if (idKurs === undefined)
			return false;
		return (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs)) ? true : false;
	});

	const fixier_regel = (idKurs: number, idSchueler: number) => computed<number | undefined>(() => {
		if (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs))
			return props.getDatenmanager().schuelerGetRegelFixiertInKurs(idSchueler, idKurs).id;
		return undefined;
	});

	async function fixieren_regel_hinzufuegen(idKurs: number, idSchueler: number) {
		const update = props.getErgebnismanager().regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(SetUtils.create1(idSchueler), SetUtils.create1(idKurs));
		await props.regelnUpdate(update);
	}

	async function fixieren_regel_entfernen(idKurs: number, idSchueler: number) {
		const idRegel = fixier_regel(idKurs, idSchueler).value;
		if (idRegel === undefined)
			return;
		const update = props.getErgebnismanager().regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(SetUtils.create1(idSchueler), SetUtils.create1(idKurs));
		await props.regelnUpdate(update);
	}

	async function fixieren_regel_toggle(fachID: number | undefined, idSchueler: number, event: Event) {
		if (fachID === undefined)
			return;
		event.stopPropagation();
		const kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID);
		if (kurs === null)
			return;
		const idKurs = kurs.id;
		if (fixier_regel(idKurs, idSchueler).value === undefined)
			await fixieren_regel_hinzufuegen(idKurs, idSchueler)
		else
			await fixieren_regel_entfernen(idKurs, idSchueler);
	}

</script>

<style scoped>

	.svws-ui-table-filter--advanced {
		display: flex;
		flex-direction: column;
	}

</style>
