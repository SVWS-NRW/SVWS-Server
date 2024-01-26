<template>
	<svws-ui-table clickable :clicked="auswahlBlockung" @update:clicked="select_blockungauswahl" :items="listBlockungen" :columns="[{ key: 'name', label: 'Blockungen' }]" no-data-text="Es liegt noch keine Planung für dieses Halbjahr vor.">
		<template #noData v-if="istBlockungPersistiert">
			<span class="inline-flex gap-1 leading-tight">
				<i-ri-error-warning-line class="flex-shrink-0" />
				<span>Es liegt bereits eine persistierte Blockung vor, die wiederhergestellt werden kann.</span>
			</span>
		</template>
		<template #header(name)>
			<span>Blockungen</span>
		</template>
		<template #cell(name)="{ rowData: row }">
			<div class="flex justify-between w-full items-start">
				<div class="flex items-center gap-1 w-full">
					<div class="flex" v-if="row === auswahlBlockung">
						<span v-if="(!edit_blockungsname)" class="border-b border-dotted hover:border-transparent cursor-text line-clamp-1 break-all leading-tight -my-0.5" @click.stop="edit_blockungsname = true">
							{{ row.name }}
						</span>
						<svws-ui-text-input v-else :model-value="row.name" headless focus
							@keyup.enter="(e: any) => patch_blockung(e.target.value, row.id)" @keyup.escape="edit_blockungsname=false"
							@change="name => patch_blockung(name, row.id)" class="-my-0.5 w-full" />
					</div>
					<div v-else>
						<span>{{ row.name }}</span>
					</div>
					<div class="-my-1 ml-auto inline-flex">
						<template v-if="visible && (auswahlBlockung !== undefined && !isPending(auswahlBlockung.id)) && row === auswahlBlockung">
							<s-gost-kursplanung-modal-blockung-lokal-berechnen v-if="allow_berechne_blockung_lokal" :get-datenmanager="getDatenmanager" v-slot="{ openModal }">
								<svws-ui-button type="transparent" @click="openModal()" title="Ergebnisse lokal im Browser berechnen" :disabled="apiStatus.pending" class="text-black dark:text-white"> Lokal Berechnen </svws-ui-button>
							</s-gost-kursplanung-modal-blockung-lokal-berechnen>
							<svws-ui-button type="transparent" @click.stop="do_create_blockungsergebnisse" title="Ergebnisse berechnen" :disabled="apiStatus.pending" v-if="allow_berechne_blockung" class="text-black dark:text-white"> Berechnen </svws-ui-button>
							<svws-ui-tooltip position="top" v-else>
								<svws-ui-button type="transparent" disabled> Berechnen </svws-ui-button>
								<template #content>
									<div class="normal-case text-base rich-text">
										Damit Kursblockungen berechnet werden können, müssen zumindest Fachwahlen, Fächer und Kurse existieren.
									</div>
								</template>
							</svws-ui-tooltip>
							<s-gost-kursplanung-remove-blockung-modal :remove-blockung="removeBlockung" v-slot="{ openModal }">
								<svws-ui-button type="icon" @click.stop="openModal()" title="Blockung löschen" :disabled="apiStatus.pending" class="text-black dark:text-white">
									<i-ri-delete-bin-line class="-mx-0.5" />
								</svws-ui-button>
							</s-gost-kursplanung-remove-blockung-modal>
						</template>
						<svws-ui-tooltip v-if="row.istAktiv">
							<span @click="patchBlockung({ istAktiv: false }, row.id)"><i-ri-checkbox-circle-fill class="text-svws text-headline-md ml-2 hover:opacity-75" /></span>
							<template #content> Aktivierte Blockung </template>
						</svws-ui-tooltip>
						<svws-ui-tooltip v-else>
							<span @click="patchBlockung({ istAktiv: true }, row.id)"><i-ri-checkbox-circle-line class="text-svws text-headline-md ml-2 opacity-25 hover:opacity-75" /></span>
							<template #content> Blockung als aktiv markieren </template>
						</svws-ui-tooltip>
					</div>
				</div>
			</div>
		</template>
		<template #actions>
			<slot name="blockungAuswahlActions" />
		</template>
	</svws-ui-table>
	<div v-if="auswahlBlockung !== undefined && isPending(auswahlBlockung.id)" class="my-3">
		<auswahl-blockung-api-status :blockung="auswahlBlockung" :api-status="apiStatus" />
	</div>
	<s-gost-kursplanung-ergebnis-auswahl v-if="hatBlockung" :halbjahr="halbjahr" :api-status="apiStatus"
		:get-datenmanager="getDatenmanager" :patch-ergebnis="patchErgebnis" :remove-ergebnisse="removeErgebnisse"
		:set-auswahl-ergebnis="setAuswahlErgebnis" :auswahl-ergebnis="auswahlErgebnis" />
</template>

<script setup lang="ts">

	import type { GostBlockungListeneintrag, GostBlockungsdaten, GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostHalbjahr, List} from "@core";
	import { ServerMode } from "@core";
	import type { ComputedRef, Ref } from 'vue';
	import type { ApiStatus } from '~/components/ApiStatus';
	import { ArrayList, BlockungsUtils } from "@core";
	import { computed, ref } from 'vue';

	const props = defineProps<{
		patchBlockung: (data: Partial<GostBlockungsdaten>, idBlockung: number) => Promise<boolean>;
		removeBlockung: () => Promise<void>;
		setAuswahlBlockung: (auswahl: GostBlockungListeneintrag | undefined) => Promise<void>;
		auswahlBlockung: GostBlockungListeneintrag | undefined;
		mapBlockungen: () => Map<number, GostBlockungListeneintrag>;
		halbjahr: GostHalbjahr;
		apiStatus: ApiStatus;
		// ... zusätzlich für die Ergebnisauswahl
		getDatenmanager: () => GostBlockungsdatenManager;
		patchErgebnis: (data: Partial<GostBlockungsergebnisListeneintrag>, idErgebnis: number) => Promise<boolean>;
		rechneGostBlockung: () => Promise<List<number>>;
		removeErgebnisse: (ergebnisse: GostBlockungsergebnisListeneintrag[]) => Promise<void>;
		setAuswahlErgebnis: (value: GostBlockungsergebnisListeneintrag | undefined) => Promise<void>;
		hatBlockung: boolean;
		auswahlErgebnis: GostBlockungsergebnisListeneintrag | undefined;
		restoreBlockung: () => Promise<void>;
		istBlockungPersistiert: boolean;
		mode: ServerMode;
	}>();

	const edit_blockungsname: Ref<boolean> = ref(false);

	const allow_berechne_blockung = computed(()=>
		props.getDatenmanager().fachwahlGetAnzahl() > 0
		&& props.getDatenmanager().getFaecherAnzahl() > 0
		&& props.getDatenmanager().kursGetAnzahl() > 0
	)

	const allow_berechne_blockung_lokal = computed(()=> allow_berechne_blockung.value && props.mode === ServerMode.DEV);

	const listBlockungen = computed(()=> {
		const list: List<GostBlockungListeneintrag> = new ArrayList();
		for (const i of props.mapBlockungen().values())
			list.add(i);
		BlockungsUtils.sortGostBlockungListeneintrag(list);
		return list;
	})

	async function select_blockungauswahl(blockung: GostBlockungListeneintrag | null) {
		if ((blockung === null) || props.apiStatus.pending)
			return;
		await props.setAuswahlBlockung(blockung);
	}

	const isPending = (id: number) : boolean => ((props.apiStatus.data !== undefined) && (props.apiStatus.data.name === "gost.kursblockung.berechnen") && (props.apiStatus.data.id === id));

	async function do_create_blockungsergebnisse() {
		const id = props.auswahlBlockung?.id;
		if (id === undefined)
			return;
		await props.rechneGostBlockung();
	}

	async function patch_blockung(value: string, idBlockung : number) {
		const result = await props.patchBlockung({ name: value.toString() }, idBlockung);
		if (result && props.auswahlBlockung)
			props.auswahlBlockung.name = value.toString();
		edit_blockungsname.value = false;
	}

	const visible: ComputedRef<boolean> = computed(() =>
		props.mapBlockungen().size > 0);

</script>
